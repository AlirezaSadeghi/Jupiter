package model.instruction.simple;

import control.Controller;
import exception.MipsRuntimeException;
import model.Immediate;
import model.Register;
import model.RegisterFile;
import model.instruction.Type;

public class JJal extends JFormatInstruction {

	public JJal(){}
	public JJal(Immediate immediate) {
		super(immediate);
	}
	
	@Override
	public void execute() throws MipsRuntimeException {
		super.execute();
		
		Register pc = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.pc);
		
		Register ra = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.ra);
		ra.setValue(pc.getValue());
		
		int pcUp = (pc.getValue() / (int)Math.pow(2, 28)) * (int)Math.pow(2, 28);
		int loc = pcUp + immediate.getValue()*4;
		pc.setValue(loc);
	}
	
	@Override
	public int getOpCode() {
		return 3;
	}
	
	@Override
	public String getName() {
		return "jal";
	}

	@Override
	public Type getType() {
		return Type.Jump;
	}
	

}
