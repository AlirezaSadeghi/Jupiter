package model.instruction.simple;

import control.Controller;
import model.Immediate;
import model.Register;
import model.RegisterFile;
import model.Word;

public class PBeqB extends PBranchB
{
	public PBeqB(Register rs , Immediate im , Immediate label )
	{
		super(rs , im , label ) ;
	}
	
	public int getSimpleInstructionCount() {
		if(im.is16bit())
			return 2;
		else
			return 3;
	}
	
	public Word[] generateMachineCode(Word address) {
		Register at = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.at);
		Register zero = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
		if(im.is16bit()){
			Word [] words = new Word [2] ;
			words[0] = ( new IAddi(zero, at, this.im)).generateMachineCode(address)[0];
			words[1] = ( new IBeq(at, this.rs, this.label)).generateMachineCode(address.add(4))[0];
			return words;
		}else{
			Word[] words = new Word[3];
			words[0] = (new ILui(at, this.im.getHigh())).generateMachineCode(address)[0];
			words[1] = (new IOri(at, at, this.im.getLow())).generateMachineCode(address.add(4))[0];
			words[2] = ( new IBeq(at, this.rs, this.label)).generateMachineCode(address.add(8))[0];
			return words;
		}
	}
}
