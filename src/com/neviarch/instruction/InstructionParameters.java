package com.neviarch.instruction;

public enum InstructionParameters
{
	REGISTER(true, false, false),
	ADDRESS(false, false, true),
	DUAL_REGISTER(true, true, false),
	REGISTER_ADDRESS(true, false, true),
	DUAL_REGISTER_ADDRESS(true, true, true);
	
	private final boolean leftRegister, rightRegister, address; 
	
	private InstructionParameters(boolean leftRegister, boolean rightRegister, boolean address)
	{
		this.leftRegister = leftRegister;
		this.rightRegister = rightRegister;
		this.address = address;
	}
	
	public boolean hasLeftRegister() {
		return this.leftRegister;
	}
	
	public boolean hasRightRegister() {
		return this.rightRegister;
	}
	
	public boolean hasAddress() {
		return this.address;
	}
}
