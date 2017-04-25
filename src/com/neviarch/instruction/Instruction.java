package com.neviarch.instruction;

public enum Instruction
{
	LOAD(InstructionParameters.REGISTER_ADDRESS),
	STORE(InstructionParameters.REGISTER_ADDRESS),
	SET(InstructionParameters.REGISTER_ADDRESS),
	
	// Stores the result in R1
	ADD(InstructionParameters.DUAL_REGISTER),
	SUB(InstructionParameters.DUAL_REGISTER),
	MULT(InstructionParameters.DUAL_REGISTER),
	DIV(InstructionParameters.DUAL_REGISTER),
	
	// Jumps unconditionally
	JUMP(InstructionParameters.ADDRESS),
	// Jumps if R1 == R2
	JUMPEQ(InstructionParameters.DUAL_REGISTER_ADDRESS),
	// Jumps if R1 > R2 (R2 <= R1)
	JUMPGR(InstructionParameters.DUAL_REGISTER_ADDRESS),
	// Jumps if R1 >= R2 (R2 < R1)
	JUMPGREQ(InstructionParameters.DUAL_REGISTER_ADDRESS);
	
	public final InstructionParameters parameters;
	
	/**
	 * Instruction constructor.
	 * @param params number of parameters.
	 */
	private Instruction(InstructionParameters parameters) {
		this.parameters = parameters;
	}
	
	/**
	 * Returns the instruction code shifted to it's place.
	 * @return the shifted value.
	 */
	public int shifted() {
		return this.ordinal() << 28;
	}
	
	/**
	 * Interprets a code into an instruction.
	 * @param code the instruction code.
	 * @return the instruction.
	 * @throws InstructionNotFoundException if the code does not represent any instruction.
	 */
	public static Instruction fromCode(int code) throws InstructionNotFoundException
	{
		switch (code)
		{
		case 0x0: return LOAD;
		case 0x1: return STORE;
		case 0x2: return SET;
		case 0x3: return ADD;
		case 0x4: return SUB;
		case 0x5: return MULT;
		case 0x6: return DIV;
		case 0x7: return JUMP;
		case 0x8: return JUMPEQ;
		case 0x9: return JUMPGR;
		case 0xA: return JUMPGREQ;
		}
		
		throw new InstructionNotFoundException("The code '" + Integer.toHexString(code)
				+ "' does not represent an instruction.");
	}
	
	public static Instruction interpret(int instruction) {
		return fromCode(instruction >> 28 & 0xF);
	}
	
	/**
	 * Compiles a code into an instruction.
	 * @param name the instruction name.
	 * @return the instruction.
	 * @throws InstructionNotFoundException if the name does not represent any instruction.
	 */
	public static Instruction compile(String name) throws InstructionNotFoundException
	{
		switch (name.toLowerCase())
		{
		case "load":	 return LOAD;
		case "store":	 return STORE;
		case "set":		 return SET;
		case "add":		 return ADD;
		case "sub":		 return SUB;
		case "mult":	 return MULT;
		case "div":		 return DIV;
		case "jump":	 return JUMP;
		case "jumpeq":	 return JUMPEQ;
		case "jumpgr":	 return JUMPGR;
		case "jumpgreq": return JUMPGREQ;
		}
		
		throw new InstructionNotFoundException("There is no instruction named '" + name + "'.");
	}
}
