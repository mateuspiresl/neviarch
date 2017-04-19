package com.neviarch;

public enum Instruction
{
	READ((byte) 0x0, InstructionParameters.REGISTER_MEMORY),
	LOAD((byte) 0x1, InstructionParameters.REGISTER_MEMORY),
	ADD((byte) 0x02, InstructionParameters.REGISTER_REGISTER),
	SUB((byte) 0x3, InstructionParameters.REGISTER_REGISTER),
	DIV((byte) 0x4, InstructionParameters.REGISTER_REGISTER),
	MULT((byte) 0x5, InstructionParameters.REGISTER_REGISTER),
	JUMP((byte) 0x6, InstructionParameters.MEMORY),
	JUMPCOND((byte) 0x7, InstructionParameters.REGISTER_MEMORY);
	
	public final byte code;
	public final InstructionParameters parameters;
	
	/**
	 * Instruction constructor.
	 * @param code instruction code.
	 * @param params number of parameters.
	 */
	private Instruction(byte code, InstructionParameters parameters)
	{
		this.code = code;
		this.parameters = parameters;
	}
	
	/**
	 * Interprets a code into an instruction.
	 * @param code the instruction code.
	 * @return the instruction.
	 * @throws InstructionNotFoundException if the code does not represent any instruction.
	 */
	public static Instruction interpret(byte code) throws InstructionNotFoundException
	{
		switch (code)
		{
		case 0x0: return READ;
		case 0x1: return LOAD;
		case 0x2: return ADD;
		case 0x3: return SUB;
		case 0x4: return DIV;
		case 0x5: return MULT;
		case 0x6: return JUMP;
		case 0x7: return JUMPCOND;
		}
		
		throw new InstructionNotFoundException("The code '" + code + "' does not represent an instruction.");
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
		case "read":	 return READ;
		case "load":	 return LOAD;
		case "add":		 return ADD;
		case "sub":		 return SUB;
		case "div":		 return DIV;
		case "mult":	 return MULT;
		case "jump":	 return JUMP;
		case "jumpcond": return JUMPCOND;
		}
		
		throw new InstructionNotFoundException("There is no instruction named '" + name + "'.");
	}
}
