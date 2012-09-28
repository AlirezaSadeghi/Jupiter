package model.instruction.simple;

import exception.MipsRuntimeException;
import model.Register;
import model.instruction.Type;

public class RNor extends RFormatInstruction{
	
	public RNor(){}
	public RNor(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs, rt, rd, shiftAmount);
	}

	@Override
	public int getFunc() {
		return 39;
	}

	@Override
	public Type getType() {
		return Type.ALU;
	}
	
	public String getName() {
		return "nor";
	}
	
	public void execute() throws MipsRuntimeException {
		super.execute();
		
		rd.setValue(~(rt.getValue()|rs.getValue()));
	}
	
}
