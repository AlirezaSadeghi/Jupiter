package model.instruction.simple;

import exception.MipsRuntimeException;
import model.Register;
import model.instruction.Type;

public class RSrlv extends RShiftV
{
	public RSrlv(){}
	public RSrlv(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs, rt, rd, shiftAmount);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		rd.setValue(rs.getValue()>>rt.getValue());
	}
	
	public int getFunc() {
		return 6;
	}
	
	@Override
	public String getName() {
		return "srlv";
	}
	
	@Override
	public Type getType() {
		return Type.ALU;
	}
}
