package model.instruction.simple;

import model.Immediate;
import model.Register;
import model.Word;
import model.instruction.PseudoInstruction;

public abstract class PBranchA extends PseudoInstruction
{
	protected Register rs ;
	protected Register rt ;
	protected Immediate im ;
	public PBranchA (Register rs , Register rt , Immediate immediate )
	{
		this.rs = rs ;
		this.rt = rt ;
		this.im = immediate ;
	}
	
	public abstract Word[] generateMachineCode(Word address) ;
	public abstract int getSimpleInstructionCount() ;
}
