package com.neviarch.compiler;

public class NotDefinedLabelException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public NotDefinedLabelException() { super(); }
	public NotDefinedLabelException(String message) { super(message); }
}
