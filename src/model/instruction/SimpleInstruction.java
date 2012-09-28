package model.instruction;

import model.Register;
import model.RegisterFile;
import control.Controller;
import exception.MipsRuntimeException;

public abstract class SimpleInstruction extends Instruction
{
	public SimpleInstruction() {
		
	}
	
	/**
	 * Executes the instruction
	 * @throws MipsRuntimeException 
	 */
	public void execute() throws MipsRuntimeException {
		// Increase program counter register
		Register pc = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.pc);
		pc.setValue(pc.getValue()+4);
	}
	
	public abstract int getOpCode();
	
	public abstract String getName();

	public abstract String toString();
	
	public int getSimpleInstructionCount() {
		return 1;
	}

	public abstract Type getType();
}