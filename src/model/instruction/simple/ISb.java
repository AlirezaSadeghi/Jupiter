package model.instruction.simple;

import control.Controller;
import exception.MipsRuntimeException;
import model.Immediate;
import model.Memory;
import model.Register;
import model.Word;
import model.instruction.Type;

public class ISb extends IMemory {
	public ISb(){
	}
	public ISb(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}
	
	public void execute() throws MipsRuntimeException {
		super.execute();
		Controller controller = Controller.getInstance();
		Memory memory = controller.getMainMemory();
		Word temp =  new Word(rs.getValue()+immediate.getValue());
		rt.getByte(0);
		memory.setByte(temp, rt.getByte(0) );
	}
	
	@Override
	public int getOpCode() {
		return 40;
	}
	
	@Override
	public String getName() {
		return "sb";
	}
	
	@Override
	public Type getType() {
		return Type.Memory;
	}
	
	
}
