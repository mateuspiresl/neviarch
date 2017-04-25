package com.neviarch;

import com.neviarch.instruction.InstructionNotFoundException;

public enum Register
{
	AX, BX, CX, DX, EX, FX, GX, HX,
	PC, IR, MAR, MBR, ACC;
	
	public static final boolean LEFT = false;
	public static final boolean RIGHT = true;
	
	/**
	 * Returns the register code shifted to the left or right register place.
	 * @param place the place to shift (should be LEFT ot RIGHT).
	 * @return the shifted value.
	 */
	public int shifted(boolean place) {
		return this.ordinal() << (place == LEFT ? 22 : 16);
	}
	
	/**
	 * Interprets a code into a register.
	 * @param code the register code.
	 * @return the register.
	 * @throws InstructionNotFoundException if the code does not represent any register.
	 */
	public static Register fromCode(int code) throws RegisterNotFoundException
	{
		switch (code)
		{
		case 0x00: return AX;
		case 0x01: return BX;
		case 0x02: return CX;
		case 0x03: return DX;
		case 0x04: return EX;
		case 0x05: return FX;
		case 0x06: return GX;
		case 0x07: return HX;
		}
		
		throw new RegisterNotFoundException("The code '" + code + "' does not represent any instruction.");
	}
	
	public static Register interpret(int instruction, boolean place) throws RegisterNotFoundException
	{
		if (place == LEFT)
			return fromCode(instruction >> 22 & 0x3F);
		else
			return fromCode(instruction >> 16 & 0x3F);
	}
	
	/**
	 * Compiles a code into a register.
	 * @param name the register name.
	 * @return the register.
	 * @throws InstructionNotFoundException if the name does not represent any register.
	 */
	public static Register compile(String name) throws RegisterNotFoundException
	{
		switch (name.toLowerCase())
		{
		case "ax": return AX;
		case "bx": return BX;
		case "cx": return CX;
		case "dx": return DX;
		case "ex": return EX;
		case "fx": return FX;
		case "gx": return GX;
		case "hx": return HX;
		}
		
		throw new RegisterNotFoundException("There is no register named '" + name + "'.");
	}
}
