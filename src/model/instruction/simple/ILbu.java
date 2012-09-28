package model.instruction.simple;

import control.Controller;
import exception.MipsRuntimeException;
import model.Immediate;
import model.Memory;
import model.Register;
import model.Word;
import model.instruction.Type;

public class ILbu extends IMemory{

	public ILbu(){
	}
	public ILbu(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}
	
	public void execute() throws MipsRuntimeException {
		super.execute();
		Controller controller = Controller.getInstance();
		Memory memory = controller.getMainMemory();
		Word temp =  new Word(rs.getValue()+immediate.getValue());
		if ( memory.getByte(temp) >= 0 )
			rt.setValue(memory.getByte(temp));
		else {
			long complement = 1;
			complement = complement << 8 ;
			complement += complement + memory.getByte(temp) ;
			rt.setValue((int)complement);
		}
	}
	
	@Override
	public int getOpCode() {
		return 36;
	}

	public String getName() {
		return "lbu";
	}
	
	@Override
	public Type getType() {
		return Type.Memory;
	}
	

}
