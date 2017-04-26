package com.neviarch;

public class CentralProcessingUnit
{
	private final Registers registers;
	private final Memory memory;
	private final ControlUnit controlUnit;
	private final ArithmeticLogicUnit arithmeticLogicUnit;
	
	/**
	 * CentralProcessingUnit constructor.
	 */
	public CentralProcessingUnit()
	{
		this.registers = new Registers();
		this.memory = new Memory(this.registers);
		this.arithmeticLogicUnit = new ArithmeticLogicUnit(this.registers);
		this.controlUnit = new ControlUnit(this.registers, this.memory, this.arithmeticLogicUnit);
	}
	
	/**
	 * DEBUG
	 * Returns the registers.
	 * @return the registers.
	 */
	public Registers getRegisters() {
		return this.registers;
	}

	/**
	 * DEBUG
	 * Returns the memory.
	 * @return the memory.
	 */
	public Memory getMemory() {
		return this.memory;
	}

	/**
	 * Run a program.
	 * @param program the program's bytes.
	 * @return itself.
	 */
	public CentralProcessingUnit run(byte[] program)
	{
		this.controlUnit.setProgram(program);
		this.controlUnit.start();
		
		return this;
	}
}
