package model.instruction.simple;

import exception.MipsRuntimeException;
import model.Register;
import model.instruction.Type;

public class RSra extends RShift
{
	public RSra(){}
	public RSra(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs,rt,rd,shiftAmount);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		rd.setValue(rt.getValue()>>>shiftAmount);
	}
	
	public int getFunc() {
		return 3;
	}
	
	@Override
	public String getName() {
		return "sra";
	}
	
	@Override
	public Type getType() {
		return Type.ALU;
	}
	

}
