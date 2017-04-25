package com.neviarch;

public class CentralProcessingUnit
{
	private Registers registers = new Registers();
	private Memory memory;
	private ControlUnit controlUnit;
	private ArithmeticLogicUnit arithmeticLogicUnit;
	
	public CentralProcessingUnit()
	{
		this.memory = new Memory(this.registers);
		this.arithmeticLogicUnit = new ArithmeticLogicUnit(this.registers);
		this.controlUnit = new ControlUnit(this.registers, this.memory, this.arithmeticLogicUnit);
	}
	
	public Registers getRegisters() {
		return this.registers;
	}

	public Memory getMemory() {
		return this.memory;
	}

	public void run(byte[] program)
	{
		this.controlUnit.allocateProgram(program);
		this.controlUnit.start();
	}
}
