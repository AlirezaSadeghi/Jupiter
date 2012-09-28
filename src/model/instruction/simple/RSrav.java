package model.instruction.simple;

import exception.MipsRuntimeException;
import model.Register;
import model.instruction.Type;

public class RSrav extends RShiftV
{
	public RSrav(){}
	public RSrav(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs, rt, rd, shiftAmount);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		rd.setValue(rs.add(rt));
	}
	
	public int getFunc() {
		return 7;
	}
	
	@Override
	public String getName() {
		return "srav";
	}
	
	@Override
	public Type getType() {
		return Type.ALU;
	}
}
