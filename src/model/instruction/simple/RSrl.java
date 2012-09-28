package model.instruction.simple;

import exception.MipsRuntimeException;
import model.Register;
import model.instruction.Type;

public class RSrl extends RShift
{
	public RSrl(){}
	public RSrl(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs,rt,rd,shiftAmount);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		rd.setValue(rt.getValue()>>shiftAmount);
		
	}
	
	public int getFunc() {
		return 2;
	}
	
	@Override
	public String getName() {
		return "srl";
	}
	
	@Override
	public Type getType() {
		return Type.ALU;
	}

}
