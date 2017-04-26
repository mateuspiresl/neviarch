package com.neviarch;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Scanner;

import com.neviarch.instruction.Instruction;
import com.neviarch.instruction.InstructionNotFoundException;
import com.neviarch.util.Bits;

public class Compiler
{
	private Scanner in;
	private ByteBuffer buffer;
	private int linesCompiledCount = 0;
	
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
	public Compiler compile() throws InstructionNotFoundException, RegisterNotFoundException
	{
		while (in.hasNext())
		{
			Instruction instruction = Instruction.compile(in.next());
			int compiled = instruction.shifted();
			
			switch (instruction.parameters)
			{
			case REGISTER:
				compiled |= Register.compile(in.next()).shifted(Register.LEFT);
				break;
				
			case ADDRESS:
				compiled |= Bits.addressToInstruction(in.next());
				break;
				
			case DUAL_REGISTER:
				compiled |= Register.compile(in.next()).shifted(Register.LEFT);
				compiled |= Register.compile(in.next()).shifted(Register.RIGHT);
				break;
				
			case REGISTER_ADDRESS:
				compiled |= Register.compile(in.next()).shifted(Register.LEFT);
				compiled |= Bits.addressToInstruction(in.next());
				break;
			
			case DUAL_REGISTER_ADDRESS:
				compiled |= Register.compile(in.next()).shifted(Register.LEFT);
				compiled |= Register.compile(in.next()).shifted(Register.RIGHT);
				compiled |= Bits.addressToInstruction(in.next());
				break;
			}
			
			addInstruction(compiled);
			this.linesCompiledCount++;
		}
		
		return this;
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
}
