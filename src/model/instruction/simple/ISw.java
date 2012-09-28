package model.instruction.simple;

import control.Controller;
import exception.MipsRuntimeException;
import model.Immediate;
import model.Memory;
import model.Register;
import model.Word;
import model.instruction.Type;

public class ISw extends IMemory {
	public ISw(){
	}
	public ISw(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}
	
	public void execute() throws MipsRuntimeException {
		super.execute();
		Controller controller = Controller.getInstance();
		Memory memory = controller.getMainMemory();
		Word temp =  new Word(rs.getValue()+immediate.getValue());
		memory.setWord(temp , rt);
	}
	
	@Override
	public int getOpCode() {
		return 43;
	}
	
	@Override
	public String getName() {
		return "sw";
	}
	
	@Override
	public Type getType() {
		return Type.Memory;
	}
	
}
