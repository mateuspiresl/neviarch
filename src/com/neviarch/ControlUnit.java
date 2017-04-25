package com.neviarch;

import com.neviarch.instruction.Instruction;
import com.neviarch.instruction.InstructionData;

public class ControlUnit
{
	private Registers registers;
	private Memory memory;
	private ArithmeticLogicUnit alu;
	
	private InstructionData instruction;
	
	public ControlUnit(Registers registers, Memory memory, ArithmeticLogicUnit alu)
	{
		this.registers = registers;
		this.memory = memory;
		this.alu = alu;
	}
	
	public void allocateProgram(byte[] program)
	{
		this.memory.allocateProgram(program);
		this.registers.set(Register.PC, 0);
	}
	
	public void start()
	{
		while (fetchAndDecode())
		{
			System.out.println("Fetching and decoding 0x" + Integer.toHexString(this.registers.get(Register.IR)));
			System.out.println("Instruction " + this.instruction.getInstruction() + " "
					+ (this.instruction.getLeftRegister() != null ? this.instruction.getLeftRegister() + " " : "")
					+ (this.instruction.getRightRegister() != null ? this.instruction.getRightRegister() + " " : "")
					+ (this.instruction.getAddress() != null ? "0x" + Integer.toHexString(this.instruction.getAddress()).toUpperCase() : ""));
			execute();
			
			System.out.print("REG [ ");
			for (int i = 0; i < 8; i++)
				System.out.print(this.registers.get(Register.fromCode(i)) + " ");
			System.out.println("]");
			
			System.out.print("MEM [ ");
			for (int i = 0; i < 8; i++) {
				this.registers.set(Register.MAR, i);
				this.memory.fetch();
				System.out.print(this.registers.get(Register.MBR) + " ");
			}
			System.out.println("]");
			
			System.out.println();
		}
	}
	
	private boolean fetchAndDecode()
	{
		if (this.registers.get(Register.PC) >= this.memory.getProgramEnd()) return false;
		
		// Fetch
		this.registers.set(Register.MAR, Register.PC);
		this.memory.fetchProgram();
		
		// Decode
		this.registers.set(Register.IR, Register.MBR);
		this.instruction = new InstructionData(this.registers.get(Register.IR));
		
		this.registers.set(Register.PC, this.registers.get(Register.PC) + 1);
		return true;
	}
	
	private void execute()
	{
		Register left = this.instruction.getLeftRegister();
		Register right = this.instruction.getRightRegister();
		Integer address = this.instruction.getAddress();
		
		switch (this.instruction.getInstruction())
		{
		case LOAD:
			this.registers.set(Register.MAR, address);
			this.memory.fetch();
			this.registers.set(left, Register.MBR);
			break;
			
		case STORE:
			this.registers.set(Register.MAR, address);
			this.registers.set(Register.MBR, left);
			this.memory.store();
			break;
			
		case SET:
			this.registers.set(left, address);
			break;
			
		case ADD:
		case SUB:
		case MULT:
		case DIV:
			this.registers.set(Register.ACC, left);
			this.alu.performWith(this.instruction.getInstruction(), right);
			this.registers.set(left, Register.ACC);
			break;
			
		case JUMP:
			this.registers.set(Register.MAR, address);
			this.registers.set(Register.PC, Register.MAR);
			return;
			
		case JUMPEQ:
			this.registers.set(Register.MAR, address);
			this.registers.set(Register.ACC, left);
			this.alu.performWith(Instruction.SUB, right);
			
			if (this.registers.get(Register.ACC) == 0)
				this.registers.set(Register.PC, Register.MAR);
			break;
			
		case JUMPGR:
			this.registers.set(Register.MAR, address);
			this.registers.set(Register.ACC, left);
			this.alu.performWith(Instruction.SUB, right);
			
			if (this.registers.get(Register.ACC) > 0)
				this.registers.set(Register.PC, Register.MAR);
			break;
			
		case JUMPGREQ:
			this.registers.set(Register.MAR, address);
			this.registers.set(Register.ACC, left);
			this.alu.performWith(Instruction.SUB, right);
			
			if (this.registers.get(Register.ACC) >= 0)
				this.registers.set(Register.PC, Register.MAR);
			break;
		}
	}
}
