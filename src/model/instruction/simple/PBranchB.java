package model.instruction.simple;

import model.Immediate;
import model.Register;
import model.Word;
import model.instruction.PseudoInstruction;

public abstract class PBranchB extends PseudoInstruction
{
	Register rs ;
	Immediate im ; 
	Immediate label ; 
	public PBranchB(Register rs , Immediate im , Immediate label)
	{
		this.rs = rs ; 
		this.im = im ;
		this.label = label ;
	}
	public abstract Word[] generateMachineCode(Word address) ;
	public abstract int getSimpleInstructionCount() ;
}
