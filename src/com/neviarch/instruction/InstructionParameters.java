package com.neviarch.instruction;

public enum InstructionParameters
{
	/**
	 * Supports one register.
	 */
	REGISTER(true, false, false),
	/**
	 * Supports address.
	 */
	ADDRESS(false, false, true),
	/**
	 * Supports two registers.
	 */
	DUAL_REGISTER(true, true, false),
	/**
	 * Supports one register and address.
	 */
	REGISTER_ADDRESS(true, false, true),
	/**
	 * Supports two registers and address.
	 */
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
	
	/**
	 * Returns the number of parameters supported.
	 * @return the number of parameters.
	 */
	public int count() {
		return (this.leftRegister ? 1 : 0) + (this.rightRegister ? 1 : 0) + (this.address ? 1 : 0);
	}
}
