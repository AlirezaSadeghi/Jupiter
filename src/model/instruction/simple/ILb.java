package model.instruction.simple;

import control.Controller;
import exception.MipsRuntimeException;
import model.Immediate;
import model.Memory;
import model.Register;
import model.Word;

public class ILb extends IMemory
{
	public ILb(){
	}
	public ILb(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}
	
	@Override
	public void execute() throws MipsRuntimeException {
		super.execute();
		Controller controller = Controller.getInstance();
		Memory memory = controller.getMainMemory();
		Word temp =  new Word(rs.getValue()+immediate.getValue());
		int w = memory.getByteSigned(temp);
		rt.setValue(w);
	}

	@Override
	public int getOpCode() {
		return 32;
	}

	@Override
	public String getName() {
		return "lb";
	}

}
