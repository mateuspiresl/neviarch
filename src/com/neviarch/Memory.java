package com.neviarch;

import com.neviarch.register.Register;
import com.neviarch.register.Registers;
import com.neviarch.util.Bits;

public class Memory
{
	private final Registers registers;
	private final int[] memory = new int[0xFFFF];
	private int programEnd = 0;
	
	/**
	 * Memory constructor.
	 * Needs access to the registers.
	 * @param registers the registers.
	 */
	public Memory(Registers registers) {
		this.registers = registers;
	}
	
	/**
	 * Returns the memory index for the program end.
	 * @return the memory index for the program end.
	 */
	public int getProgramEnd() {
		return this.programEnd;
	}
	
	/**
	 * Stores the value in MBR in the memory of address specified by MAR. 
	 */
	public void store() {
		this.memory[this.programEnd + this.registers.get(Register.MAR)] = this.registers.get(Register.MBR);
	}
	
	/**
	 * Fetches the value in memory of address specified by MAR.
	 * Used to fetch the real memory address.
	 */
	public void fetchProgram() {
		this.registers.set(Register.MBR, this.memory[this.registers.get(Register.MAR)]);
	}
	
	/**
	 * Fetches the value in memory of address specified by MAR, not considering the program instructions space.
	 * Used to fetch the memory of the program's variables.
	 */
	public void fetch() {
		this.registers.set(Register.MBR, this.memory[this.programEnd + this.registers.get(Register.MAR)]);
	}
	
	/**
	 * Stores an entire program in the memory.
	 * @param program the program's bytes.
	 */
	public void storeProgram(byte[] program)
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
	
	/**
	 * Frees the program memory.
	 */
	public void freeProgram() {
		this.programEnd = 0;
	}
	
	/**
	 * DEBUG
	 * Returns the real memory.
	 * @return the integer array of the memory.
	 */
	public int[] getMemory() {
		return this.memory;
	}
}
