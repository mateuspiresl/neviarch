package com.neviarch.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.neviarch.instruction.Instruction;
import com.neviarch.instruction.InstructionNotFoundException;
import com.neviarch.register.Register;
import com.neviarch.register.RegisterNotFoundException;
import com.neviarch.util.Bits;

public class Compiler
{
	private Scanner in;
	private ByteBuffer buffer;
	private int linesCompiledCount = 0;
	private String lastLine;
	
	/**
	 * Compiler constructor.
	 * @param in the stream with the code.
	 */
	public Compiler(InputStream in)
	{
		this.in = new Scanner(in);
		this.buffer = new ByteBuffer();
	}
	
	/**
	 * Compiles the code from the stream.
	 * @return itself.
	 */
	public Compiler compile() throws InstructionNotFoundException, RegisterNotFoundException,
			PreviouslyDefinedLabelException, NotDefinedLabelException, InvalidExpressionException
	{
		List<Integer> instructions = new ArrayList<>();
		Map<String, Integer> labels = new HashMap<>();
		List<LabelUse> labelUses = new ArrayList<>();
		
		while (this.in.hasNext())
		{
			this.lastLine = this.in.nextLine().trim().toLowerCase();
			
			if (this.lastLine.isEmpty() || this.lastLine.startsWith("#"))
			{
				this.linesCompiledCount++;
				continue;
			}
			
			String[] tokens = this.lastLine.split("\\s+");
			
			String label = extractLabel(tokens[0]);
			if (label != null && (tokens.length == 1 || tokens[1].startsWith("#")))
			{
				if (labels.containsKey(label))
					throw new PreviouslyDefinedLabelException("The label " + label
							+ " was previously defined at line " + labels.get(label) + ".");
				
				labels.put(label, instructions.size());
				
				this.linesCompiledCount++;
				continue;
			}
			
			Instruction instruction = Instruction.compile(tokens[0]);
			int compiled = instruction.shifted();
			
			if (instruction.parameters.count() != tokens.length - 1 && (tokens.length <= instruction.parameters.count() + 1
					|| !tokens[instruction.parameters.count() + 1].startsWith("#")))
				throw new InvalidExpressionException("The instruction " + instruction + " supports "
						+ instruction.parameters.count() + ", but " + (tokens.length - 1)
						+ " was provided.");
			
			switch (instruction.parameters)
			{
			case REGISTER:
				compiled |= Register.compile(tokens[1]).shifted(Register.LEFT);
				break;
				
			case ADDRESS:
				if (instruction == Instruction.JUMP && tokens[1].matches("[a-z_]+"))
					labelUses.add(new LabelUse(instructions.size(), tokens[1]));
				else
					compiled |= Bits.addressToInstruction(tokens[1]);
				
				break;
			
			case DUAL_REGISTER:
				compiled |= Register.compile(tokens[1]).shifted(Register.LEFT);
				compiled |= Register.compile(tokens[2]).shifted(Register.RIGHT);
				break;
				
			case REGISTER_ADDRESS:
				compiled |= Register.compile(tokens[1]).shifted(Register.LEFT);
				
				if (instruction.ordinal() <= Instruction.SET.ordinal())
				{
					Register deref = dereference(tokens[2]);
					if (deref != null)
					{
						if (instruction == Instruction.LOAD)
							instruction = Instruction.LOADREG;
						
						else if (instruction == Instruction.STORE)
							instruction = Instruction.STOREREG;
						
						else if (instruction == Instruction.SET)
							instruction = Instruction.SETREG;
						
						compiled = (compiled & 0x0FFFFFFF) | instruction.shifted() | deref.shifted(Register.RIGHT);
						break;
					}
				}
				
				compiled |= Bits.addressToInstruction(tokens[2]);
				break;
			
			case DUAL_REGISTER_ADDRESS:
				compiled |= Register.compile(tokens[1]).shifted(Register.LEFT);
				compiled |= Register.compile(tokens[2]).shifted(Register.RIGHT);
				
				if ((instruction == Instruction.JUMPEQ || instruction == Instruction.JUMPGR
						|| instruction == Instruction.JUMPGREQ) && tokens[3].matches("[a-z_]+"))
					labelUses.add(new LabelUse(instructions.size(), tokens[3]));
				else
					compiled |= Bits.addressToInstruction(tokens[3]);
				
				break;
			}
			
			instructions.add(compiled);
			this.linesCompiledCount++;
		}
		
		
		for (LabelUse use : labelUses)
		{
			try {
				int compiled = instructions.get(use.line) | labels.get(use.label);
				instructions.set(use.line, compiled);
			}
			catch (NullPointerException npe) {
				this.linesCompiledCount = use.line;
				throw new NotDefinedLabelException("The label " + use.label + " was not defined.");
			}
		}
		
		for (int compiled : instructions)
			addInstruction(compiled);

		return this;
	}
	
	private Register dereference(String toParse)
	{
		if (toParse.matches("\\([a-hA-H][xX]\\)"))
			return Register.compile(toParse.substring(1, toParse.length() - 1));
		else
			return null;
	}
	
	private String extractLabel(String word) {
		return word.matches("[a-z_]+:") ? word.substring(0, word.length() - 1) : null;
	}
	
	/**
	 * Returns the number of lines compiled.
	 * @return the number of lines compiled.
	 */
	public int getLinesCompiledCount() {
		return this.linesCompiledCount;
	}
	
	/**
	 * Add a instruction to the byte block.
	 * The instructions are added separated in bytes.
	 * @param instruction the instruction's integer value.
	 */
	private void addInstruction(int instruction)
	{
		this.buffer.add((byte) ((instruction >> 24) & 0xFF));
		this.buffer.add((byte) ((instruction >> 16) & 0xFF));
		this.buffer.add((byte) ((instruction >> 8) & 0xFF));
		this.buffer.add((byte) (instruction & 0xFF));
	}
	
	/**
	 * Writes the bytes to the output stream given.
	 * @param out output stream.
	 * @throws IOException if an I/O error occur.
	 */
	public void writeTo(OutputStream out) throws IOException
	{
		while (!this.buffer.isEmpty())
			out.write(this.buffer.getPiece());
	}
	
	/**
	 * Returns the bytes of the compiled.
	 * A call to this method erases the buffer.
	 * @return the byte array.
	 */
	public byte[] getBytes() {
		return this.buffer.getBytes();
	}
	
	
	/**
	 * Provides store and access methods to a byte buffer.
	 */
	private class ByteBuffer
	{
		private static final int BLOCK_SIZE = 128;
		
		private LinkedList<byte[]> buffer = new LinkedList<>();
		private byte[] data = new byte[BLOCK_SIZE];
		private int size = 0;
		
		/**
		 * Add a byte.
		 * @param data the byte to add.
		 */
		public void add(byte data)
		{
			if (this.size == BLOCK_SIZE)
			{
				this.buffer.add(this.data);
				this.data = new byte[128];
			}
			
			this.data[size++] = data;
		}
		
		/**
		 * Returns first block of bytes.
		 * Maximum of 128 byte array.
		 * @return the block as byte array.
		 */
		public byte[] getPiece()
		{
			if (this.buffer.isEmpty())
			{
				int size = this.size;
				this.size = 0;
				
				return Arrays.copyOf(this.data, size);
			}
			else return this.buffer.pollFirst();
		}
		
		/**
		 * Checks if the buffer is empty.
		 * @return true if the buffer is empty.
		 */
		public boolean isEmpty() {
			return this.buffer.isEmpty() && this.size == 0;
		}
		
		/**
		 * Returns the entire buffer as a byte array.
		 * @return the buffer as a byte array.
		 */
		public byte[] getBytes()
		{
			byte[] data = new byte[this.buffer.size() * BLOCK_SIZE + this.size];
			int offset = 0;
			
			while (!this.buffer.isEmpty() || this.size > 0)
			{
				byte[] toCopy;
				int size;
				
				if (this.buffer.isEmpty())
				{
					toCopy = this.data;
					size = this.size;
					
					this.size = 0;
				}
				else
				{
					toCopy = this.buffer.pollFirst();
					size = BLOCK_SIZE;
				}
				
				for (int i = 0; i < size; i++)
					data[offset + i] = toCopy[i];
				
				offset += size;
			}
			
			return data;
		}
	}
	
	private class LabelUse {
		public final int line;
		public final String label;
		
		public LabelUse(int line, String label)
		{
			this.line = line;
			this.label = label;
		}
	}
}
