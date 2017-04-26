package com.neviarch.instruction;

/**
 * Thrown when an instruction parsing fails.
 */
public class InstructionNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = -3345901789040896783L;
	
	public InstructionNotFoundException() { super(); }
	public InstructionNotFoundException(String message) { super(message); }
}
