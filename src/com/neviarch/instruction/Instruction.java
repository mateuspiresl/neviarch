package com.neviarch.instruction;

public enum Instruction
{
	/**
	 * Retrieves a value from the memory at the address specified in the MAR
	 * to a register.
	 */
	LOAD(InstructionParameters.REGISTER_ADDRESS),
	/**
	 * Stores a value in a register to the memory at the address specified
	 * in the MAR.
	 */
	STORE(InstructionParameters.REGISTER_ADDRESS),
	/**
	 * Set a given value to a register.
	 */
	SET(InstructionParameters.REGISTER_ADDRESS),
	
	/**
	 * Adds the values from two registers and stores in the first.
	 */
	ADD(InstructionParameters.DUAL_REGISTER),
	/**
	 * Subtracts the values from two registers and stores in the first.
	 */
	SUB(InstructionParameters.DUAL_REGISTER),
	/**
	 * Multiplies the values from two registers and stores in the first.
	 */
	MULT(InstructionParameters.DUAL_REGISTER),
	/**
	 * Divides the values from one registers to another, storing in the first.
	 */
	DIV(InstructionParameters.DUAL_REGISTER),
	
	/**
	 * Jumps unconditionaly to the address.
	 */
	JUMP(InstructionParameters.ADDRESS),
	/**
	 * Jumps to the address if the value of the first register is equal to
	 * the value of the second.
	 */
	JUMPEQ(InstructionParameters.DUAL_REGISTER_ADDRESS),
	/**
	 * Jumps to the address if the value of the first register is greater than
	 * or equal to the value of the second.
	 */
	JUMPGR(InstructionParameters.DUAL_REGISTER_ADDRESS),
	/**
	 * Jumps to the address if the value of the first register is greater than
	 * the value of the second.
	 */
	JUMPGREQ(InstructionParameters.DUAL_REGISTER_ADDRESS),
	
	/**
	 * Prompts an integer from user and stores int the first register.
	 */
	IN(InstructionParameters.REGISTER),
	/**
	 * Ouputs an integer int the first register.
	 */
	OUT(InstructionParameters.REGISTER),
	
	/**
	 * Read from memory at address specified in the second register
	 * and store in the first register.
	 */
	LOADREG(InstructionParameters.DUAL_REGISTER),
	/**
	 * Store the value of the first register in memory at address
	 * specified in the second register.
	 */
	STOREREG(InstructionParameters.DUAL_REGISTER);
	
	public final InstructionParameters parameters;
	
	/**
	 * Instruction constructor.
	 * @param parameters number of parameters.
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
		case 0xB: return IN;
		case 0xC: return OUT;
		case 0xD: return LOADREG;
		case 0xE: return STOREREG;
		}
		
		throw new InstructionNotFoundException("The code '" + Integer.toHexString(code)
				+ "' does not represent an instruction.");
	}
	
	/**
	 * Extracts the instruction from the instruction data.
	 * @param instruction the instruction as integer.
	 * @return the instruction type.
	 */
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
		case "in":		 return IN;
		case "out":		 return OUT;
		case "loadreg":	 return LOADREG;
		case "storereg": return STOREREG;
		}
		
		throw new InstructionNotFoundException("There is no instruction named '" + name + "'.");
	}
}
