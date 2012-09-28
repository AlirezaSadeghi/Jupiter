package model.instruction.simple;

import model.Immediate;
import model.Memory;
import model.Register;
import model.Word;
import model.instruction.Type;
import control.Controller;
import exception.MipsRuntimeException;

public class ILw extends IMemory {
	
	
	public ILw(){
	}
	public ILw(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}
	
	public void execute() throws MipsRuntimeException {
		super.execute();
		Controller controller = Controller.getInstance();
		Memory memory = controller.getMainMemory();
		Word temp =  new Word(rs.getValue()+immediate.getValue());
		rt.setValue(memory.getWord(temp));
	}
	
	@Override
	public int getOpCode() {
		return 35;
	}
	
	public String getName() {
		return "lw";
	}

	@Override
	public Type getType() {
		return Type.Memory;
	}

}
