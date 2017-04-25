package com.neviarch.tests;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import com.neviarch.Compiler;
import com.neviarch.Register;
import com.neviarch.instruction.Instruction;
import com.neviarch.instruction.InstructionData;
import com.neviarch.util.Bits;

public class InstructionDataTest
{
	@Test
	public void test() throws Exception
	{
		InstructionData data;
		
		data = new InstructionData(compile("LOAD ax 0x12"));
		Assert.assertEquals(Instruction.LOAD, data.getInstruction());
		Assert.assertEquals(Register.AX, data.getLeftRegister());
		Assert.assertEquals(null, data.getRightRegister());
		Assert.assertEquals(0x12, (int) data.getAddress());
		
		data = new InstructionData(compile("add ax bx"));
		Assert.assertEquals(Instruction.ADD, data.getInstruction());
		Assert.assertEquals(Register.AX, data.getLeftRegister());
		Assert.assertEquals(Register.BX, data.getRightRegister());
		Assert.assertEquals(null, data.getAddress());
		
		data = new InstructionData(compile("JUMP 0xFF12"));
		Assert.assertEquals(Instruction.JUMP, data.getInstruction());
		Assert.assertEquals(null, data.getLeftRegister());
		Assert.assertEquals(null, data.getRightRegister());
		Assert.assertEquals(0xFF12, (int) data.getAddress());
		
		data = new InstructionData(compile("jumpgr gx hx 0xEFFF"));
		Assert.assertEquals(Instruction.JUMPGR, data.getInstruction());
		Assert.assertEquals(Register.GX, data.getLeftRegister());
		Assert.assertEquals(Register.HX, data.getRightRegister());
		Assert.assertEquals(0xEFFF, (int) data.getAddress());
	}
	
	private static int compile(String code)
	{
		byte[] data = new Compiler(new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8))).compile().getBytes();
		return Bits.leftShift(data[0], 24) | Bits.leftShift(data[1], 16)
				| Bits.leftShift(data[2], 8) | Bits.leftShift(data[3], 0);
	}
}
