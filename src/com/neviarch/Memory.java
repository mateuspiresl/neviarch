package com.neviarch;

import com.neviarch.util.Bits;

public class Memory
{
	private final Registers registers;
	private final int[] memory = new int[0xFFFF];
	private int programEnd = 0;
	
	public Memory(Registers registers) {
		this.registers = registers;
	}
	
	public int getProgramEnd() {
		return this.programEnd;
	}
	
	public void store() {
		this.memory[this.programEnd + this.registers.get(Register.MAR)] = this.registers.get(Register.MBR);
	}
	
	public void allocateProgram(byte[] program)
	{
		freeProgram();
		
		for (int i = 0; i < program.length; i += 4)
		{
			this.registers.set(Register.MBR, Bits.leftShift(program[i], 24) | Bits.leftShift(program[i + 1], 16)
					| Bits.leftShift(program[i + 2], 8) | Bits.leftShift(program[i + 3], 0));
			this.store();
			this.programEnd++;
		}
	}
	
	public void freeProgram() {
		this.programEnd = 0;
	}
	
	public void fetchProgram()
	{
		if (this.registers.get(Register.MAR) < this.programEnd)
			this.registers.set(Register.MBR, this.memory[this.registers.get(Register.MAR)]);
	}
	
	public void fetch() {
		this.registers.set(Register.MBR, this.memory[this.programEnd + this.registers.get(Register.MAR)]);
	}
	
	public int[] getMemory() {
		return this.memory;
	}
}
