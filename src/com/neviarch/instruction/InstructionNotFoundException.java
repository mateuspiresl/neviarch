package com.neviarch.instruction;

public class InstructionNotFoundException extends RuntimeException
{
	private static final long serialVersionUID = -3345901789040896783L;
	
	public InstructionNotFoundException() { super(); }
	public InstructionNotFoundException(String message) { super(message); }
}
