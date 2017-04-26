package com.neviarch.tests;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import com.neviarch.Memory;
import com.neviarch.Register;
import com.neviarch.Registers;

public class MemoryTest
{
	@Test
	public void initialState() throws Exception
	{
		Registers registers = new Registers();
		Memory memory = new Memory(registers);
		assertEquals(0, memory.getProgramEnd());
	}
	
	@Test
	public void programLimits() throws Exception
	{
		Registers registers = new Registers();
		Memory memory = new Memory(registers);
		
		memory.storeProgram(new byte[] { 0, 0, 0xF, 0 });
		assertEquals(1, memory.getProgramEnd());
		
		registers.set(Register.MAR, 0);
		memory.fetchProgram();
		Assert.assertEquals(0xF00, registers.get(Register.MBR));
		
		memory.freeProgram();
		assertEquals(0, memory.getProgramEnd());
		
		memory.storeProgram(new byte[] { 0, 0, 0xF, 0, 0xF, 0, 0, 1 });
		assertEquals(2, memory.getProgramEnd());
		
		registers.set(Register.MAR, 0);
		memory.fetchProgram();
		Assert.assertEquals(0xF00, registers.get(Register.MBR));
		
		registers.set(Register.MAR, 1);
		memory.fetchProgram();
		Assert.assertEquals(0xF000001, registers.get(Register.MBR));
	}
	
	@Test
	public void use() throws Exception
	{
		Registers registers = new Registers();
		Memory memory = new Memory(registers);
		memory.storeProgram(new byte[] { 0, 0, 0, 0 });
		
		registers.set(Register.MAR, 0);
		registers.set(Register.MBR, 0xA);
		memory.store();
		memory.fetch();
		assertEquals(0xA, registers.get(Register.MBR));
		
		registers.set(Register.MAR, 1);
		registers.set(Register.MBR, 0xB);
		memory.store();
		memory.fetch();
		assertEquals(0xB, registers.get(Register.MBR));
		
		registers.set(Register.MAR, 0);
		memory.fetch();
		assertEquals(0xA, registers.get(Register.MBR));
		
		registers.set(Register.MAR, 2);
		registers.set(Register.MBR, 0xC);
		memory.store();
		memory.fetch();
		assertEquals(0xC, registers.get(Register.MBR));
		
		registers.set(Register.MAR, 1);
		memory.fetch();
		assertEquals(0xB, registers.get(Register.MBR));
		
		registers.set(Register.MAR, 0);
		memory.fetch();
		assertEquals(0xA, registers.get(Register.MBR));
	}
}
