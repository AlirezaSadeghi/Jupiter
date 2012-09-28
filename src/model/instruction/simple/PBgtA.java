package model.instruction.simple;

import control.Controller;
import model.Immediate;
import model.Register;
import model.RegisterFile;
import model.Word;

public class PBgtA extends PBranchA
{
	public PBgtA (Register rs , Register rt , Immediate immediate )
	{
		super(rs , rt , immediate) ;
	}
	
	public int getSimpleInstructionCount() {
		return 2;
	}

	public Word[] generateMachineCode(Word address) {
		Register at = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.at);
		Register zero = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
		Word [] words = new Word [2] ;
		words[0] = (new RSlt(this.rt, this.rs, at,0)).generateMachineCode(address)[0];
		words[1] = (new IBne(at, zero, this.im)).generateMachineCode(address.add(4))[0];
		return words;
	}

}
