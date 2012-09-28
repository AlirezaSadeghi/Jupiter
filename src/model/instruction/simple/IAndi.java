package model.instruction.simple;

import model.Immediate;
import model.Register;
import model.instruction.Type;
import exception.MipsRuntimeException;

public class IAndi extends IFormatInstruction {
	
	public IAndi(){
	}
	public IAndi(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}
	
	public void execute() throws MipsRuntimeException {
		super.execute();
		rt.setValue(rs.getValue()&immediate.getValue());
	}
	
	@Override
	public int getOpCode() {
		return 12;
	}

	public String getName() {
		return "andi";
	}
	
	@Override
	public Type getType() {
		return Type.ALU;
	}
	

}
