package com.neviarch.compiler;

public class PreviouslyDefinedLabelException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PreviouslyDefinedLabelException() { super(); }
	public PreviouslyDefinedLabelException(String message) { super(message); }
}
