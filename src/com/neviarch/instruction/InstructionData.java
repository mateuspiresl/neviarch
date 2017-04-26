package com.neviarch.instruction;

import com.neviarch.Register;
import com.neviarch.util.Bits;

public class InstructionData
{
	private final Instruction instruction;
	private final Register left, right;
	private final Integer address;
	
	/**
	 * Generic constructor.
	 * @param instruction the instruction.
	 * @param left the left register.
	 * @param right the right register.
	 * @param address the address.
	 */
	public InstructionData(Instruction instruction, Register left, Register right, Integer address)
	{
		this.instruction = instruction;
		
		if (instruction.parameters.hasLeftRegister())
			if (left == null)
				throw new MissingInstructionParamenterException();
			else
				this.left = left;
		else this.left = null;
		
		if (instruction.parameters.hasRightRegister())
			if (right == null)
				throw new MissingInstructionParamenterException();
			else
				this.right = right;
		else this.right = null;
		
		if (instruction.parameters.hasAddress())
			if (address == null)
				throw new MissingInstructionParamenterException();
			else
				this.address = address;
		else this.address = null;
	}
	
	/**
	 * Constructor for a instruction that accepts only the left register.
	 * @param instruction the instruction.
	 * @param register the left register.
	 */
	public InstructionData(Instruction instruction, Register register) {
		this(instruction, register, null, null);
	}
	
	/**
	 * Constructor for a instruction that accepts only the address.
	 * @param instruction the instruction.
	 * @param address the address.
	 */
	public InstructionData(Instruction instruction, Integer address) {
		this(instruction, null, null, address);
	}
	
	/**
	 * Constructor for a instruction that accepts the left and right registers.
	 * @param instruction the instruction.
	 * @param left the left register.
	 * @param right the right register.
	 */
	public InstructionData(Instruction instruction, Register left, Register right) {
		this(instruction, left, right, null);
	}
	
	/**
	 * Constructor for a instruction that accepts the left register and the address.
	 * @param instruction the instruction.
	 * @param register the left register.
	 * @param address the address.
	 */
	public InstructionData(Instruction instruction, Register register, Integer address) {
		this(instruction, register, null, address);
	}
	
	/**
	 * Creates from the instruction data.
	 * @param instruction the instruction data.
	 */
	// public static InstructionData createFromData(int instruction)
	public InstructionData(int instruction)
	{
		this(Instruction.interpret(instruction),
				Register.interpret(instruction, Register.LEFT),
				Register.interpret(instruction, Register.RIGHT),
				instruction & 0xFFFF);
	}
	
	/**
	 * Creates from the instruction data in a byte block.
	 * @param instruction the byte block.
	 * @param offset the index of the first byte.
	 */
	// public static InstructionData createFromDataBlock(byte[] instruction, int offset)
	public InstructionData(byte[] instruction, int offset)
	{
		this(Bits.leftShift(instruction[offset], 24) | Bits.leftShift(instruction[offset + 1], 16)
				| Bits.leftShift(instruction[offset + 2], 8) | Bits.leftShift(instruction[offset + 3], 0));
	}
	
	/**
	 * Creates from the instruction data in a byte block.
	 * @param instruction the byte block.
	 */
	// public static InstructionData createFromDataBlock(byte[] instruction)
	public InstructionData(byte[] instruction) {
		this(instruction, 0);
	}
	
	/**
	 * Returns the instruction.
	 * @return the instruction.
	 */
	public Instruction getInstruction() {
		return this.instruction;
	}

	/**
	 * Returns the left register.
	 * @return the left register or null if the instruction does not support.
	 */
	public Register getLeftRegister() {
		return this.left;
	}

	/**
	 * Returns the right register.
	 * @return the right register or null if the instruction does not support.
	 */
	public Register getRightRegister() {
		return this.right;
	}

	/**
	 * Returns the address.
	 * @return the address or null if the instruction does not support.
	 */
	public Integer getAddress() {
		return this.address;
	}

	/**
	 * Parses the instruction data to an integer. 
	 * @return the instruction as integer.
	 */
	public int toBytes()
	{
		int bytes = this.instruction.shifted();
		if (this.left != null) bytes |= this.left.shifted(Register.LEFT);
		if (this.right != null) bytes |= this.left.shifted(Register.RIGHT);
		if (this.address > -1) bytes |= this.address;
		
		return bytes;
	}
	
	@Override
	public String toString() {
		return "{ " + this.instruction + ", " + this.left + ", " + this.right + ", 0x" + Integer.toHexString(this.address) + " }";
	}
}
