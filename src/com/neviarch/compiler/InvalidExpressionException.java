package com.neviarch.compiler;

public class InvalidExpressionException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public InvalidExpressionException() { super(); }
	public InvalidExpressionException(String message) { super(message); }
}
