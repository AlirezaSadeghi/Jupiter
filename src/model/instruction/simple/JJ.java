package model.instruction.simple;

import model.Immediate;
import model.Register;
import model.RegisterFile;
import model.instruction.Type;
import control.Controller;
import exception.MipsRuntimeException;

public class JJ extends JFormatInstruction
{
	public JJ(){}
	public JJ(Immediate immediate) {
		super(immediate);
	}
	
	@Override
	public void execute() throws MipsRuntimeException {
		super.execute();
		Register pc = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.pc);
		
		// 4 bit e balaye pc ro bedast byar
		int pcUp = (pc.getValue() / (int)Math.pow(2, 28)) * (int)Math.pow(2, 28);
		// address nahai ro bedast byar
		int loc = pcUp + immediate.getValue()*4;
		// beriz to pc
		pc.setValue(loc);
		System.out.println(pc);
	}
	
	public int getOpCode(){
		return 2;
	}
	
	@Override
	public String getName() {
		return "j";
	}
	
	
	@Override
	public Type getType() {
		return Type.Jump;
	}
}
