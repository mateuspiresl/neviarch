package com.neviarch;

public class InstructionNotFoundException extends RuntimeException {
	public InstructionNotFoundException() { super(); }
	public InstructionNotFoundException(String message) { super(message); }
}
