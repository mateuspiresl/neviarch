package com.neviarch.tests;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

import com.neviarch.ArithmeticLogicUnit;
import com.neviarch.ControlUnit;
import com.neviarch.IO;
import com.neviarch.Memory;
import com.neviarch.compiler.Compiler;
import com.neviarch.register.Register;
import com.neviarch.register.Registers;

public class ControlUnitTest
{
	@Test
	public void test()
	{
		Registers registers = new Registers();
		Memory memory = new Memory(registers);
		ControlUnit controlUnit = new ControlUnit(registers, memory, new ArithmeticLogicUnit(registers), new IO(toStream(""), System.out));
		
		String code = "SET AX 0xA\n SET BX 0x14\n ADD AX BX\n STORE AX 0x0\n LOAD CX 0x0\n SUB BX CX";
		controlUnit.setProgram(compile(code));
		controlUnit.run();
		
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
		
		String code = "set ax 0x1\n set bx 0x2\n set cx 0x0\n set dx 0x6\n add cx ax\n add cx bx\n jumpgreq dx cx 0x4\n store cx 0x0";
		controlUnit.setProgram(compile(code));
		controlUnit.run();
		
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
		
		String code = "in ax\n set bx 0x1\n add cx ax\n add cx bx\n out cx";
		controlUnit.setProgram(compile(code));
		controlUnit.run();
		
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
		
		String code = "set ax 0xF\n store ax 0x0\n set ax 0x0\n loadreg bx ax";
		controlUnit.setProgram(compile(code));
		controlUnit.run();
		
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
		
		String code = "set ax 0xF\n set bx 0x1\n storereg ax bx";
		controlUnit.setProgram(compile(code));
		controlUnit.run();
		
		Assert.assertEquals(0xF, registers.get(Register.AX));
		Assert.assertEquals(0x1, registers.get(Register.BX));
		
		registers.set(Register.MAR, 0x1);
		memory.fetch();
		Assert.assertEquals(0xF, registers.get(Register.MBR));
	}
	
	@Test
	public void setWithRegisters()
	{
		Registers registers = new Registers();
		Memory memory = new Memory(registers);
		IO io = new IO(toStream(""), System.out);
		ControlUnit controlUnit = new ControlUnit(registers, memory, new ArithmeticLogicUnit(registers), io);
		
		String code = "set ax 0xF\n setreg bx ax";
		controlUnit.setProgram(compile(code)).run();
		
		Assert.assertEquals(0xF, registers.get(Register.AX));
		Assert.assertEquals(0xF, registers.get(Register.BX));
	}
	
	@Test
	public void selectionSortProgram() throws FileNotFoundException
	{
		Registers registers = new Registers();
		Memory memory = new Memory(registers);
		IO io = new IO(toStream("3 2 1 4 8 7 9 5 -1"), System.out);
		ControlUnit controlUnit = new ControlUnit(registers, memory, new ArithmeticLogicUnit(registers), io);
		
		byte[] program = new Compiler(new FileInputStream("selectionsort.nevl")).compile().getBytes();
		controlUnit.setProgram(program).run();
	}
	
	private static byte[] compile(String code) {
		return new Compiler(toStream(code)).compile().getBytes();
	}
	
	private static InputStream toStream(String code) {
		return new ByteArrayInputStream(code.getBytes(StandardCharsets.UTF_8));
	}
}
