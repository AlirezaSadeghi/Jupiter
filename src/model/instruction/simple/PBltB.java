package model.instruction.simple;

import control.Controller;
import model.Immediate;
import model.Register;
import model.RegisterFile;
import model.Word;

public class PBltB extends PBranchB
{
	public PBltB(Register rt , Immediate im , Immediate label )
	{
		super(rt , im , label ) ;
	}

	public int getSimpleInstructionCount() {
		if(im.is16bit())
			return 2;
		else
			return 4;
	}

	public Word[] generateMachineCode(Word address) {
		Register at = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.at);
		Register zero = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
		if(im.is16bit()){
			Word[] words = new Word[2];
			words[0] = ( new ISlti(this.rs , at , this.im)).generateMachineCode(address)[0]; 
			words[1] = ( new IBne(this.rs, zero, this.label)).generateMachineCode(address.add(4))[0] ;
			return words;
		}else{
			Word [] words = new Word[4] ;
			words[0] = (new ILui(at, this.im.getHigh())).generateMachineCode(address)[0];
			words[1] = (new IOri(at, at, this.im.getLow())).generateMachineCode(address.add(4))[0];
			words[2] = ( new RSlt(this.rs, at, at,0)).generateMachineCode(address.add(8))[0] ;  
			words[3] = ( new IBne(at, zero, this.label)).generateMachineCode(address.add(12))[0] ;
			return words;
		}
	}

}
