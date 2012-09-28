package model.instruction.simple;

import model.Register;
import model.instruction.Type;
import exception.MipsRuntimeException;

public class RSubu extends RFormatInstruction {

	public RSubu(){}
	public RSubu(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs, rt, rd, shiftAmount);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		rd.setValue((int)(rs.getUnsignedValue()-rt.getUnsignedValue()));
	}
	
	
	@Override
	public int getFunc() {
		return 36;
	}

	@Override
	public String getName() {
		return "subu";
	}
	
	@Override
	public Type getType() {
		return Type.ALU;
	}

}
