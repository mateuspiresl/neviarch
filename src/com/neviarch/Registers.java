package com.neviarch;

public class Registers
{
	private int[] registers = new int[0x3F];
	
	/**
	 * Sets the value of a register.
	 * @param register the register.
	 * @param value the value to set.
	 */
	public void set(Register register, int value) {
		this.registers[register.ordinal()] = value;
	}
	
	/**
	 * Copies a value from a register to another.
	 * @param from the register from which the value will be copied.
	 * @param to the register where the copied value will be stored.
	 */
	public void set(Register from, Register to) {
		this.registers[from.ordinal()] = this.registers[to.ordinal()];
	}
	
	/**
	 * Gets a register value.
	 * @param register the register.
	 * @return the value from the register.
	 */
	public int get(Register register) {
		return this.registers[register.ordinal()];
	}
}
