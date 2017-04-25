package com.neviarch;

public class Registers
{
	private int[] registers = new int[0x3F];
	
	public void set(Register register, int value) {
		this.registers[register.ordinal()] = value;
	}
	
	public void set(Register from, Register to) {
		this.registers[from.ordinal()] = this.registers[to.ordinal()];
	}
	
	public int get(Register register) {
		return this.registers[register.ordinal()];
	}
}
