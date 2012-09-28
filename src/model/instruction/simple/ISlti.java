package model.instruction.simple;

import model.Immediate;
import model.Register;
import model.instruction.Type;

public class ISlti extends IFormatInstruction{

	public ISlti(){}
	public ISlti(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
		
	}
	
	@Override
	public void execute(){
		super.execute();
		if ( rs.getValue() < immediate.getValue())
			rt.setValue(1);
		else
			rt.setValue(0);
	}

	
	public int getOpCode() {
		return 10 ;
	}
	
	@Override
	public String getName() {
		return "slti";
	}
	
	@Override
	public Type getType() {
		return Type.ALU ;
	}
	

}
