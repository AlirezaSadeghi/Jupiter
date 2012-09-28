package model.instruction.simple;

import control.Controller;
import model.Immediate;
import model.Register;
import model.RegisterFile;
import model.Word;
import model.instruction.PseudoInstruction;

public class PSubi extends PseudoInstruction 
{
	protected Register rs , rt ; 
	protected Immediate immediate ;
	
	public PSubi(Register rs , Register rt , Immediate im){
		this.rs = rs ;
		this.rt = rt ;
		this.immediate = im ;
	}
	
	public int getSimpleInstructionCount() {
		return 0;
	}

	// Checked
	public Word[] generateMachineCode(Word address) {
		Register at = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.at);
		Register zero = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
		if(immediate.is16bit()){
			Word [] words = new Word [2];
			words[0] = (new IAddi(zero, at, this.immediate)).generateMachineCode(address)[0];
			words[1] = (new RSub(this.rs, at, this.rt,0)).generateMachineCode(address.add(4))[0];
			return words; 
		}else{
			Word [] words = new Word [3];
			words[0] = (new ILui(at, this.immediate.getHigh())).generateMachineCode(address)[0];
			words[1] = (new IOri(at, at, this.immediate.getLow())).generateMachineCode(address.add(4))[0];
			words[2] = (new RSub(this.rs, at, this.rt,0)).generateMachineCode(address.add(8))[0];
			return words;
		}
	}
	
}
