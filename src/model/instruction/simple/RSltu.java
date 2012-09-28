package model.instruction.simple;

import exception.MipsRuntimeException;
import model.Register;
import model.instruction.Type;

public class RSltu extends RFormatInstruction {

	public RSltu(){}
	public RSltu(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs,rd,rt,shiftAmount);
	}
	
	public void execute() throws MipsRuntimeException {
		super.execute();
		if ( rs.getUnsignedValue() < rt.getUnsignedValue())
			rd.setValue(1);
		else
			rd.setValue(0);
	}
	
	@Override
	public int getFunc() {
		return 43;
	}

	@Override
	public String getName() {
		return "sltu";
	}
	
	@Override
	public Type getType() {
		return Type.Other;
	}

}
