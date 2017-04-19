package com.neviarch.tests.interpreter;

import org.junit.Assert;
import org.junit.Test;

import com.neviarch.Instruction;

public class InstructionTest
{
	@Test
	public void test() throws Exception
	{
		Assert.assertEquals(0x0, Instruction.READ.code);
		Assert.assertEquals(Instruction.READ, Instruction.interpret((byte) 0x0));
		Assert.assertEquals("READ", Instruction.READ.toString());
		Assert.assertEquals(Instruction.READ, Instruction.compile("READ"));
		
		Assert.assertEquals(0x4, Instruction.DIV.code);
		Assert.assertEquals(Instruction.DIV, Instruction.interpret((byte) 0x4));
		Assert.assertEquals("DIV", Instruction.DIV.toString());
		Assert.assertEquals(Instruction.DIV, Instruction.compile("DIV"));
	}
}
