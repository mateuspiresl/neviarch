package com.neviarch;

public class RegisterNotFoundException extends RuntimeException {
	public RegisterNotFoundException() { super(); }
	public RegisterNotFoundException(String message) { super(message); }
}
