package model.instruction.simple;

import control.Controller;
import model.Immediate;
import model.Register;
import model.RegisterFile;
import model.Word;
import model.instruction.PseudoInstruction;

public class PSgtB extends PseudoInstruction {

	protected Register rs , rt ;
	protected Immediate im ;
	
	public PSgtB (Register rs , Register rt , Immediate im )
	{
		this.rs = rs ;
		this.rt = rt ;
		this.im = im ;
	}
	
	public int getSimpleInstructionCount() {
		return 2;
	}

	public Word[] generateMachineCode(Word address) {
		Register rt = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.at);
		Register zero = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
		Word [] words = new Word[2];
		words[0] = ( new IAddi(rt, zero, this.im)).generateMachineCode(address)[0];
		words[1] = ( new RSlt(this.rs, rt, this.rt,0)).generateMachineCode(address.add(4))[0];
		return words ;
	}

}
