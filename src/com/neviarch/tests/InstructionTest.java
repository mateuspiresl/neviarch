package com.neviarch.tests;

import org.junit.Assert;
import org.junit.Test;

import com.neviarch.instruction.Instruction;

public class InstructionTest
{
	@Test
	public void test() throws Exception
	{
		testInstruction(Instruction.LOAD,		"LOAD",		0x0);
		testInstruction(Instruction.STORE,		"STORE",	0x1);
		testInstruction(Instruction.SET,		"SET",		0x2);
		testInstruction(Instruction.ADD,		"ADD",		0x3);
		testInstruction(Instruction.SUB,		"SUB",		0x4);
		testInstruction(Instruction.MULT,		"MULT",		0x5);
		testInstruction(Instruction.DIV,		"DIV",		0x6);
		testInstruction(Instruction.JUMP,		"JUMP",		0x7);
		testInstruction(Instruction.JUMPEQ,		"JUMPEQ",	0x8);
		testInstruction(Instruction.JUMPGR,		"JUMPGR",	0x9);
		testInstruction(Instruction.JUMPGREQ,	"JUMPGREQ",	0xA);
	}
	
	private void testInstruction(Instruction instruction, String name, int code)
	{
		Assert.assertEquals(code, instruction.ordinal());
		Assert.assertEquals(instruction, Instruction.fromCode(code));
		Assert.assertEquals(name, instruction.toString());
		Assert.assertEquals(instruction, Instruction.compile(name));
		Assert.assertEquals(code << 28, instruction.shifted());
	}
}
