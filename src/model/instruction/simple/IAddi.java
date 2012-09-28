package model.instruction.simple;

import model.Immediate;
import model.Register;
import model.Word;
import model.instruction.Type;
import exception.MipsRuntimeException;
import exception.OverflowException;

public class IAddi extends IFormatInstruction
{
	public IAddi(){}
	public IAddi(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		Word temp = new Word(0);
		if ( Integer.signum(rs.getValue())*Integer.signum(immediate.getValue()) > 0 ){
			temp.setValue(rs.getValue()+immediate.getValue());
			if ( Integer.signum(temp.getValue())*Integer.signum(rs.getValue()) < 0 )
				throw new OverflowException();
		}
		
		rt.setValue(rs.getValue() + immediate.getValue());
	}
	
	public int getOpCode(){
		return 8;
	}
	
	public String getName() {
		return "addi";
	}
	
	public Type getType() {
		return Type.ALU;
	}
}
