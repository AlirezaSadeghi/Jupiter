package model.instruction.simple;

import model.Immediate;
import model.Register;
import model.RegisterFile;
import model.instruction.Type;
import control.Controller;
import exception.MipsRuntimeException;

public class IBeq extends IBranch
{
	public IBeq(){}
	public IBeq(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		System.out.println(immediate.getValue());
		Register pc = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.pc);
		if (rs.equals(rt)) {
			pc.setValue(pc.add((int)immediate.getValue() * 4));
		}
		
	}
	
	public int getOpCode(){
		return 4;
	}
	
	public String getName() {
		return "beq";
	}
	
	@Override
	public Type getType() {
		return Type.Branch;
	}
}
