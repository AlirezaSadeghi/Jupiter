package model.instruction.simple;

import control.Controller;
import exception.MipsRuntimeException;
import model.Immediate;
import model.Register;
import model.RegisterFile;
import model.instruction.Type;

public class IBne extends IBranch
{
	public IBne(){}
	public IBne(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		System.out.println(immediate.getValue());
		Register pc = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.pc);
		if (!rs.equals(rt)) {
			pc.setValue(pc.add((int)immediate.getValue() * 4));
		}
		
	}
	@Override
	public int getOpCode() {
		return 5;
	}
	
	public String getName() {
		return "bne";
	}
	
	@Override
	public Type getType() {
		return Type.Branch;
	}
	
	

}
