package model.instruction.simple;

import model.Register;
import model.instruction.Type;
import exception.MipsRuntimeException;

public class RSll extends RShift
{
	public RSll(){}
	public RSll(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs,rt,rd,shiftAmount);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		rd.setValue(rt.getValue()<<shiftAmount);
	}
	
	public int getFunc() {
		return 0;
	}
	
	@Override
	public String getName() {
		return "rsll";
	}
	
	@Override
	public Type getType() {
		return Type.ALU;
	}
}
