package com.neviarch.util;

public class Bits
{
	/**
	 * Left shifts a byte.
	 * When the byte first bit is 1, the cast to integer, that happens before the shift,
	 * turns every bit before that to 1. The same of do: byte | 0xFFFFFF00.
	 * Use only if the behaviour above is not expected.
	 * @param value the byte to shift.
	 * @param shift the amount to shift.
	 * @return the shifted byte as integer.
	 */
	public static int leftShift(byte value, int shift) {
		return (value & 0xFF) << shift;
	}

	/**
	 * Parses a string address in hexa to its binary representation.
	 * Address limits: 0x0 to 0xFFFF and can be prefixed with any length of 0's.
	 * @param address the address.
	 * @return the binary representation.
	 * @throws InvalidAddressRepresentationException if the address does not follow its limits.
	 */
	public static int addressToInstruction(String address) throws InvalidAddressRepresentationException
	{
		if (!address.toLowerCase().matches("0x0*[\\dabcdef]{1,4}"))
			throw new InvalidAddressRepresentationException("The address '" + address + "' is invalid.");
		
		int value = 0;
		int digit = 0;
		
		for (int i = address.length() - 1; i >= 2 && digit < 4; i--)
			value |= Character.digit(address.charAt(i), 16) << (digit++ * 4);
		
		return value;
	}
}
