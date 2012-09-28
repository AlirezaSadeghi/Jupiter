package model.instruction.simple;

import model.Immediate;
import model.Register;
import model.instruction.Type;
import exception.MipsRuntimeException;

public class IXori extends IFormatInstruction {

	public IXori(){
	}
	public IXori(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}
	
	public void execute() throws MipsRuntimeException {
		super.execute();
		rt.setValue(rs.getValue() ^ immediate.getValue());
	}
	
	@Override
	public int getOpCode() {
		return 14;
	}
	
	@Override
	public String getName() {
		return "xori";
	}

	@Override
	public Type getType() {
		return Type.ALU;
	}
	

}
