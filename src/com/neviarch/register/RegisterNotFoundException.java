package com.neviarch.register;

/**
 * Thrown when a register parsing fails.
 */
public class RegisterNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1279231062204043214L;
	
	public RegisterNotFoundException() { super(); }
	public RegisterNotFoundException(String message) { super(message); }
}
