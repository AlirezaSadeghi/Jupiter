package model.instruction.simple;

import model.Register;
import model.Word;
import exception.MipsRuntimeException;
import exception.OverflowException;

public class RAdd extends RFormatInstruction
{
	public RAdd(){}
	public RAdd(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs, rt, rd, shiftAmount);
	}

	public void execute() throws MipsRuntimeException  {
		super.execute();
		Word temp = new Word(0);
		if ( Integer.signum(rs.getValue())*Integer.signum(rt.getValue()) > 0 ){
			temp.setValue(rs.getValue()+rt.getValue());
			if ( Integer.signum(temp.getValue())*Integer.signum(rs.getValue()) < 0 )
				throw new OverflowException();
		}
		
		rd.setValue(rs.add(rt));
	}
	
	@Override
	public String getName() {
		return "add";
	}
	
	public int getFunc() {
		return 32;
	}
}