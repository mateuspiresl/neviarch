package com.neviarch.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.neviarch.register.Register;
import com.neviarch.register.Registers;

public class RegistersTest
{
	@Test
	public void test() throws Exception
	{
		Registers registers = new Registers();
		
		registers.set(Register.AX, 1);
		assertEquals(1, registers.get(Register.AX));
		
		registers.set(Register.HX, 2);
		assertEquals(2, registers.get(Register.HX));
		
		registers.set(Register.IR, 3);
		assertEquals(3, registers.get(Register.IR));
		
		registers.set(Register.MAR, 4);
		assertEquals(4, registers.get(Register.MAR));
		
		registers.set(Register.MBR, 5);
		assertEquals(5, registers.get(Register.MBR));
		
		registers.set(Register.PC, 6);
		assertEquals(6, registers.get(Register.PC));
	}
}
