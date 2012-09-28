package model.instruction.simple;

import model.Register;
import model.instruction.Type;
import exception.MipsRuntimeException;

public class ROr extends RFormatInstruction {
	
	public ROr(){}
	public ROr(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs, rt, rd, shiftAmount);
	}
	
	public void execute() throws MipsRuntimeException {
		super.execute();
		
		rd.setValue(rt.getValue()|rs.getValue());
	}

	@Override
	public Type getType() {
		return Type.ALU;
	}

	@Override
	public String getName() {
		return "or";
	}
	
	@Override
	public int getFunc() {
		return 37;
	}
	
	

}
