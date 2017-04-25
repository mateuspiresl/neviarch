package com.neviarch;

import com.neviarch.instruction.Instruction;

public class ArithmeticLogicUnit
{
	private Registers registers;
	
	/**
	 * ALU constructor.
	 * The ALU needs access to the registers.
	 * @param registers the CPU registers.
	 */
	public ArithmeticLogicUnit(Registers registers) {
		this.registers = registers;
	}
	
	/**
	 * Performs an arithmetic calculation.
	 * @param register the register with will do the calculation.
	 * @throws NotSupportedActionExpcetion if the instruction set is not supported.
	 */
	public void performWith(Instruction action, Register register) throws NotSupportedActionExpcetion
	{
		switch (action)
		{
		case ADD:
			this.registers.set(Register.ACC, this.registers.get(Register.ACC) + this.registers.get(register));
			break;
			
		case SUB:
			this.registers.set(Register.ACC, this.registers.get(Register.ACC) - this.registers.get(register));
			break;
			
		case MULT:
			this.registers.set(Register.ACC, this.registers.get(Register.ACC) * this.registers.get(register));
			break;
			
		case DIV:
			this.registers.set(Register.ACC, this.registers.get(Register.ACC) / this.registers.get(register));
			break;
			
		default:
			throw new NotSupportedActionExpcetion();
		}
	}
}
