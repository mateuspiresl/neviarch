package com.neviarch.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

import com.neviarch.Instruction;
import com.neviarch.InstructionNotFoundException;
import com.neviarch.InvalidAddressRepresentationException;
import com.neviarch.Register;

public class Compiler
{
	private Scanner in;
	private ByteBuffer buffer;
	
	public Compiler(InputStream in)
	{
		this.in = new Scanner(in);
		this.buffer = new ByteBuffer();
	}
	
	public Compiler compile()
	{
		// LABEL
		// Map<String, Short> labels = new HashMap<>();
		short counter = 0;
		
		try {
			while (in.hasNext())
			{
				String token = in.next();
				
				/* LABEL
				if (token.matches("\\w+:"))
				{
					labels.put(token.substring(0, token.length() - 1), (short) (counter * 2));
					continue;
				}
				*/
				
				counter++;
				
				Instruction instruction = Instruction.compile(token);
				short instructionBytes = (short) (instruction.code << 13);
				
				switch (instruction.parameters)
				{
				case REGISTER:
					instructionBytes |= Register.compile(in.next()).code << 10;
					break;
					
				case MEMORY:
					instructionBytes |= addressToByte(in.next());
					break;
					
				case REGISTER_REGISTER:
					instructionBytes |= Register.compile(in.next()).code << 10;
					instructionBytes |= Register.compile(in.next()).code << 7;
					break;
					
				case REGISTER_MEMORY:
					instructionBytes |= Register.compile(in.next()).code << 10;
					instructionBytes |= addressToByte(in.next());
					break;
				}
				
				this.buffer.add(instructionBytes);
			}
		}
		catch (InstructionNotFoundException | InvalidAddressRepresentationException e)
		{
			System.out.println("Compilation error at instruction " + counter + ".");
			System.out.println(e.getMessage());
		}
		
		return this;
	}
	
	public void writeTo(OutputStream out) throws IOException
	{
		while (!this.buffer.isEmpty())
			out.write(this.buffer.getPiece());
	}
	
	public byte[] getBytes() {
		return this.buffer.getBytes();
	}
	
	private static short addressToByte(String address) throws InvalidAddressRepresentationException
	{
		if (!address.matches("0x0*[12]?[\\dABCDEF]{1,2}"))
			throw new InvalidAddressRepresentationException("The address '" + address + "' is invalid.");
		
		short value = 0;
		int digit = 0;
		
		for (int i = address.length() - 1; i >= 2 && digit < 3; i--)
			value |= Character.digit(address.charAt(i), 16) << (digit++ * 4);
		
		return value;
	}
	
	private class ByteBuffer
	{
		private static final int BLOCK_SIZE = 128;
		
		private LinkedList<byte[]> buffer = new LinkedList<>();
		private byte[] data = new byte[BLOCK_SIZE];
		private int size = 0;
		
		public void add(short data)
		{
			if (this.size == BLOCK_SIZE)
			{
				this.buffer.add(this.data);
				this.data = new byte[128];
			}
			
			this.data[size++] = (byte) (data >> 8);
			this.data[size++] = (byte) data;
		}
		
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
		
		public boolean isEmpty() {
			return this.buffer.isEmpty() && this.size == 0;
		}
		
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
