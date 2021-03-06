package com.neviarch;

import com.neviarch.instruction.Instruction;
import com.neviarch.instruction.InstructionData;
import com.neviarch.register.Register;
import com.neviarch.register.Registers;

public class ControlUnit
{
	public static boolean LOG = false;
	
	private final Registers registers;
	private final Memory memory;
	private final ArithmeticLogicUnit alu;
	private final IO io;
	
	private InstructionData instruction;
	
	/**
	 * ControlUnit constructor.
	 * @param registers the registers.
	 * @param memory the memory.
	 * @param alu the aithmetic logic unit.
	 */
	public ControlUnit(Registers registers, Memory memory, ArithmeticLogicUnit alu, IO io)
	{
		this.registers = registers;
		this.memory = memory;
		this.alu = alu;
		this.io = io;
	}
	
	/**
	 * Sets the program and stores in memory.
	 * @param program the program's bytes.
	 */
	public ControlUnit setProgram(byte[] program)
	{
		this.memory.storeProgram(program);
		this.registers.set(Register.PC, 0);
		
		return this;
	}
	
	/**
	 * Runs the program set.
	 */
	public ControlUnit run()
	{
		while (fetchAndDecode())
		{
			if (LOG)
			{
				System.out.println("Fetching and decoding 0x" + Integer.toHexString(this.registers.get(Register.IR))
						+ " at line " + (this.registers.get(Register.PC) - 1));
				System.out.println("Instruction " + this.instruction.getInstruction() + " "
						+ (this.instruction.getLeftRegister() != null ? this.instruction.getLeftRegister() + " " : "")
						+ (this.instruction.getRightRegister() != null ? this.instruction.getRightRegister() + " " : "")
						+ (this.instruction.getAddress() != null ? "0x" + Integer.toHexString(this.instruction.getAddress()).toUpperCase() : ""));
			}
			
			execute();
			
			if (LOG)
			{
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
		
		return this;
	}
	
	/**
	 * Fetchs and decodes the instruction at the address specified by PC.
	 * @return true if there is an instruction to be executed; false, otherwise.
	 */
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
	
	/**
	 * Executes the instruction at IR.
	 */
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
			
		case IN:
			this.registers.set(left, this.io.read());
			break;
			
		case OUT:
			this.io.write(this.registers.get(left));
			break;
		
		case LOADREG:
			this.registers.set(Register.MAR, this.registers.get(right));
			this.memory.fetch();
			this.registers.set(left, Register.MBR);
			break;
			
		case STOREREG:
			this.registers.set(Register.MAR, this.registers.get(right));
			this.registers.set(Register.MBR, left);
			this.memory.store();
			break;
			
		case SETREG:
			this.registers.set(left, this.registers.get(right));
			break;
		}
	}
}
