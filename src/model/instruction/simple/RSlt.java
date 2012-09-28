package model.instruction.simple;

import model.Register;
import model.instruction.Type;
import exception.MipsRuntimeException;

public class RSlt extends RFormatInstruction
{
	public RSlt(){}
	public RSlt(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs,rt,rd,shiftAmount);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		if ( rs.getValue() < rt.getValue())
			rd.setValue(1);
		else
			rd.setValue(0);
	}
	
	@Override
	public int getFunc() {
		return 42;
	}
	
	@Override
	public String getName() {
		return "slt";
	}
	
	@Override
	public Type getType() {
		return Type.Other;
	}

}
