package com.neviarch;

public class InvalidAddressRepresentationException extends RuntimeException {
	public InvalidAddressRepresentationException() { super(); }
	public InvalidAddressRepresentationException(String message) { super(message); }
}
