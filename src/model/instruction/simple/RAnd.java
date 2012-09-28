package model.instruction.simple;

import exception.MipsRuntimeException;
import model.Register;
import model.instruction.Type;

public class RAnd extends RFormatInstruction{
	
	public RAnd(){}
	public RAnd(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs, rt, rd, shiftAmount);
	}

	@Override
	public int getFunc() {
		return 36;
	}

	@Override
	public Type getType() {
		return Type.ALU;
	}
	
	@Override
	public String getName() {
		return "and";
	}
	public void execute() throws MipsRuntimeException {
		super.execute();
		
		rd.setValue(rt.getValue()&rs.getValue());
	}
	

}
