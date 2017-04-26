package com.neviarch.instruction;

public enum InstructionParameters
{
	REGISTER(true, false, false),
	ADDRESS(false, false, true),
	DUAL_REGISTER(true, true, false),
	REGISTER_ADDRESS(true, false, true),
	DUAL_REGISTER_ADDRESS(true, true, true);
	
	private final boolean leftRegister, rightRegister, address; 
	
	/**
	 * InstructionParameters constructor.
	 * @param leftRegister specifies if this type supports the left register.
	 * @param rightRegister specifies if this type supports the right register.
	 * @param address specifies if this type supports the address.
	 */
	private InstructionParameters(boolean leftRegister, boolean rightRegister, boolean address)
	{
		this.leftRegister = leftRegister;
		this.rightRegister = rightRegister;
		this.address = address;
	}
	
	/**
	 * Checks if supports the left register.
	 * @return true if supports the left register.
	 */
	public boolean hasLeftRegister() {
		return this.leftRegister;
	}
	
	/**
	 * Checks if supports the right register.
	 * @return true if supports the right register.
	 */
	public boolean hasRightRegister() {
		return this.rightRegister;
	}
	
	/**
	 * Checks if supports the address.
	 * @return true if supports the address.
	 */
	public boolean hasAddress() {
		return this.address;
	}
}
