package model.instruction.simple;

import model.Immediate;
import model.Register;
import model.RegisterFile;
import model.Word;
import control.Controller;

public class PBneB extends PBranchB
{

	public PBneB(Register rt , Immediate im , Immediate label )
	{
		super(rt , im , label ) ;
	}
	public int getSimpleInstructionCount() {
		return 2 ;
	}

	public Word[] generateMachineCode(Word address) {
		Register at = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.at);
		Register zero = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
		if(im.is16bit()){
			Word [] words = new Word [2] ;
			words[0] = ( new IAddi(zero, at, this.im)).generateMachineCode(address)[0];
			words[1] = ( new IBne(at, this.rs, this.label)).generateMachineCode(address.add(4))[0];
			return words;
		}else{
			Word[] words = new Word[3];
			words[0] = (new ILui(at, this.im.getHigh())).generateMachineCode(address)[0];
			words[1] = (new IOri(at, at, this.im.getLow())).generateMachineCode(address.add(4))[0];
			words[2] = ( new IBne(at, this.rs, this.label)).generateMachineCode(address.add(8))[0];
			return words;
		}
	}

}
