package com.neviarch.tests;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import com.neviarch.ArithmeticLogicUnit;
import com.neviarch.Compiler;
import com.neviarch.ControlUnit;
import com.neviarch.IO;
import com.neviarch.Memory;
import com.neviarch.Register;
import com.neviarch.Registers;

public class ControlUnitTest
{
	@Test
	public void test()
	{
		Registers registers = new Registers();
		Memory memory = new Memory(registers);
		ControlUnit controlUnit = new ControlUnit(registers, memory, new ArithmeticLogicUnit(registers), new IO(toStream(""), System.out));
		
		String code = "SET AX 0xA SET BX 0x14 ADD AX BX STORE AX 0x0 LOAD CX 0x0 SUB BX CX";
		controlUnit.setProgram(compile(code));
		controlUnit.start();
		
		Assert.assertEquals(30, registers.get(Register.AX));
		Assert.assertEquals(-10, registers.get(Register.BX));
		Assert.assertEquals(30, registers.get(Register.CX));
		
		registers.set(Register.MAR, 0);
		memory.fetch();
		Assert.assertEquals(30, registers.get(Register.MBR));
	}
	
	@Test
	public void jump()
	{
		Registers registers = new Registers();
		Memory memory = new Memory(registers);
		ControlUnit controlUnit = new ControlUnit(registers, memory, new ArithmeticLogicUnit(registers), new IO(toStream(""), System.out));
		
		String code = "set ax 0x1 set bx 0x2 set cx 0x0 set dx 0x6 add cx ax add cx bx jumpgreq dx cx 0x4 store cx 0x0";
		controlUnit.setProgram(compile(code));
		controlUnit.start();
		
		Assert.assertEquals(1, registers.get(Register.AX));
		Assert.assertEquals(2, registers.get(Register.BX));
		Assert.assertEquals(9, registers.get(Register.CX));
		Assert.assertEquals(6, registers.get(Register.DX));
		
		registers.set(Register.MAR, 0);
		memory.fetch();
		Assert.assertEquals(9, registers.get(Register.MBR));
	}
	
	@Test
	public void io()
	{
		Registers registers = new Registers();
		Memory memory = new Memory(registers);
		IO io = new IO(toStream("15"), System.out);
		ControlUnit controlUnit = new ControlUnit(registers, memory, new ArithmeticLogicUnit(registers), io);
		
		String code = "in ax set bx 0x1 add cx ax add cx bx out cx";
		controlUnit.setProgram(compile(code));
		controlUnit.start();
		
		Assert.assertEquals(0xF, registers.get(Register.AX));
		Assert.assertEquals(0x1, registers.get(Register.BX));
		Assert.assertEquals(0x10, registers.get(Register.CX));
	}
	
	@Test
	public void readWithRegisters()
	{
		Registers registers = new Registers();
		Memory memory = new Memory(registers);
		IO io = new IO(toStream(""), System.out);
		ControlUnit controlUnit = new ControlUnit(registers, memory, new ArithmeticLogicUnit(registers), io);
		
		String code = "set ax 0xF store ax 0x0 set ax 0x0 loadreg bx ax";
		controlUnit.setProgram(compile(code));
		controlUnit.start();
		
		Assert.assertEquals(0x0, registers.get(Register.AX));
		Assert.assertEquals(0xF, registers.get(Register.BX));
		
		registers.set(Register.MAR, 0);
		memory.fetch();
		Assert.assertEquals(0xF, registers.get(Register.MBR));
	}
	
	@Test
	public void storeWithRegisters()
	{
		Registers registers = new Registers();
		Memory memory = new Memory(registers);
		IO io = new IO(toStream(""), System.out);
		ControlUnit controlUnit = new ControlUnit(registers, memory, new ArithmeticLogicUnit(registers), io);
		
		String code = "set ax 0xF set bx 0x1 storereg ax bx";
		controlUnit.setProgram(compile(code));
		controlUnit.start();
		
		Assert.assertEquals(0xF, registers.get(Register.AX));
		Assert.assertEquals(0x1, registers.get(Register.BX));
		
		registers.set(Register.MAR, 0x1);
		memory.fetch();
		Assert.assertEquals(0xF, registers.get(Register.MBR));
	}
	
	private static byte[] compile(String code) {
		return new Compiler(toStream(code)).compile().getBytes();
	}
	
	private static InputStream toStream(String code) {
		return new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
	}
}
