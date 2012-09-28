package model.instruction.simple;

import model.Immediate;
import model.Register;
import model.Word;
import model.instruction.Type;

public abstract class IBranch extends IFormatInstruction
{
	public IBranch(){}
	public IBranch(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}

	@Override
	public Type getType() {
		return Type.Branch;
	}

	@Override
	public Word[] generateMachineCode(Word address) {
		Word word = new Word(0);
		word.setSubWord(getOpCode(), 26, 31);
		if(rs != null)
			word.setSubWord(rs.getRegisterNumber(), 21, 25);
		if(rt != null)
		word.setSubWord(rt.getRegisterNumber(), 16, 20);
		if(immediate != null){
			int imm = (immediate.getValue() - address.getValue() -4)/4;
			word.setSubWord(imm, 0,15);
		}
		return new Word[]{word};
	}

}
