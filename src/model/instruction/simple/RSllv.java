package model.instruction.simple;

import exception.MipsRuntimeException;
import exception.OverflowException;
import model.Register;
import model.instruction.Type;

public class RSllv extends RShiftV
{
	public RSllv(){}
	public RSllv(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs, rt, rd, shiftAmount);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		rd.setValue(rt.getValue()<<rs.getValue());
	}
	
	public int getFunc() {
		return 4;
	}
	
	@Override
	public String getName() {
		return "sllv";
	}
	
	@Override
	public Type getType() {
		return Type.ALU;
	}
}
