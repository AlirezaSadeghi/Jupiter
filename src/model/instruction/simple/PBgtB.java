package model.instruction.simple;

import control.Controller;
import model.Immediate;
import model.Register;
import model.RegisterFile;
import model.Word;

public class PBgtB extends PBranchB
{
	public PBgtB(Register rs , Immediate im , Immediate label )
	{
		super(rs , im , label ) ;
	}

	public int getSimpleInstructionCount() {
		if(im.is16bit())
			return 3;
		else
			return 4;
	}

	public Word[] generateMachineCode(Word address) {
		Register at = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.at);
		Register zero = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
		if(im.is16bit()){
			Word [] words = new Word[3] ;
			words[0] = ( new IAddi(zero, at, this.im)).generateMachineCode(address)[0] ;
			words[1] = ( new RSlt(at, this.rs, at,0)).generateMachineCode(address.add(4))[0] ;  
			words[2] = ( new IBne(at, zero, this.label)).generateMachineCode(address.add(8))[0] ;
			return words;
		}else{
			Word [] words = new Word[4] ;
			words[0] = (new ILui(at, this.im.getHigh())).generateMachineCode(address)[0];
			words[1] = (new IOri(at, at, this.im.getLow())).generateMachineCode(address.add(4))[0];
			words[2] = ( new RSlt(this.rs, at, at,0)).generateMachineCode(address.add(8))[0] ;  
			words[3] = ( new IBeq(at, zero, this.label)).generateMachineCode(address.add(12))[0] ;
			return words;
		}
	}

}
