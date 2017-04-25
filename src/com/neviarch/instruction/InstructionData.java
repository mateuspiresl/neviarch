package com.neviarch.instruction;

import com.neviarch.Register;
import com.neviarch.util.Bits;

public class InstructionData
{
	private final Instruction instruction;
	private final Register left, right;
	private final Integer address;
	
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
	
	public InstructionData(Instruction instruction, Register register) {
		this(instruction, register, null, null);
	}
	
	public InstructionData(Instruction instruction, Integer address) {
		this(instruction, null, null, address);
	}
	
	public InstructionData(Instruction instruction, Register left, Register right) {
		this(instruction, left, right, null);
	}
	
	public InstructionData(Instruction instruction, Register register, Integer address) {
		this(instruction, register, null, address);
	}
	
	public InstructionData(int instruction)
	{
		this(Instruction.interpret(instruction),
				Register.interpret(instruction, Register.LEFT),
				Register.interpret(instruction, Register.RIGHT),
				instruction & 0xFFFF);
	}
	
	public InstructionData(byte[] instruction, int offset)
	{
		this(Bits.leftShift(instruction[offset], 24) | Bits.leftShift(instruction[offset + 1], 16)
				| Bits.leftShift(instruction[offset + 2], 8) | Bits.leftShift(instruction[offset + 3], 0));
	}
	
	public InstructionData(byte[] instruction) {
		this(instruction, 0);
	}
	
	public Instruction getInstruction() {
		return this.instruction;
	}

	public Register getLeftRegister() {
		return this.left;
	}

	public Register getRightRegister() {
		return this.right;
	}

	public Integer getAddress() {
		return this.address;
	}

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
