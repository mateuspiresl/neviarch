package com.neviarch.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neviarch.ArithmeticLogicUnit;
import com.neviarch.Register;
import com.neviarch.Registers;
import com.neviarch.instruction.Instruction;

public class ArithmeticLogicUnitTest
{
	@Test
	public void test() throws Exception
	{
		Registers registers = new Registers();
		ArithmeticLogicUnit alu = new ArithmeticLogicUnit(registers);
		
		registers.set(Register.ACC, 10);
		alu.performWith(Instruction.ADD, Register.AX);
		assertEquals(10, registers.get(Register.ACC));
		
		registers.set(Register.AX, 10);
		registers.set(Register.ACC, 20);
		alu.performWith(Instruction.ADD, Register.AX);
		assertEquals(30, registers.get(Register.ACC));
		
		registers.set(Register.BX, 20);
		alu.performWith(Instruction.SUB, Register.BX);
		assertEquals(10, registers.get(Register.ACC));
		
		registers.set(Register.CX, 5);
		alu.performWith(Instruction.MULT, Register.CX);
		assertEquals(50, registers.get(Register.ACC));

		registers.set(Register.DX, 2);
		alu.performWith(Instruction.DIV, Register.DX);
		assertEquals(25, registers.get(Register.ACC));
	}
}
