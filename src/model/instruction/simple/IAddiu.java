package model.instruction.simple;

import model.Immediate;
import model.Register;
import model.instruction.Type;
import exception.MipsRuntimeException;

public class IAddiu extends IFormatInstruction {

	public IAddiu(){
	}
	public IAddiu(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}
	
	public void execute() throws MipsRuntimeException {
		super.execute();
		
		rt.setValue((int)(rs.getUnsignedValue() + immediate.getValue()));
	}
	
	
	@Override
	public int getOpCode() {
		return 9;
	}

	public String getName() {
		return "addu";
	}
	
	@Override
	public Type getType() {
		return Type.ALU;
	}
	
}
