package com.neviarch;

public enum Register
{
	AX((byte) 0x0), BX((byte) 0x1), CX((byte) 0x2), DX((byte) 0x3),
	EX((byte) 0x4), FX((byte) 0x5), GX((byte) 0x6), HX((byte) 0x7);
	
	public final byte code;
	
	private Register(byte code) {
		this.code = code;
	}
	
	/**
	 * Interprets a code into a register.
	 * @param code the register code.
	 * @return the register.
	 * @throws InstructionNotFoundException if the code does not represent any register.
	 */
	public static Register interpret(byte code) throws RegisterNotFoundException
	{
		switch (code)
		{
		case 0x0: return AX;
		case 0x1: return BX;
		case 0x2: return CX;
		case 0x3: return DX;
		case 0x4: return EX;
		case 0x5: return FX;
		case 0x6: return GX;
		case 0x7: return HX;
		}
		
		throw new RegisterNotFoundException("The code '" + code + "' does not represent any instruction.");
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
