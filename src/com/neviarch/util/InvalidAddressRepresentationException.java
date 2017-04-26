package com.neviarch.util;

/**
 * Thrown when an address parsing fails.
 */
public class InvalidAddressRepresentationException extends RuntimeException {
	private static final long serialVersionUID = -826633435910985956L;
	
	public InvalidAddressRepresentationException() { super(); }
	public InvalidAddressRepresentationException(String message) { super(message); }
}
