package model.instruction.simple;

import model.Register;
import model.Word;
import model.instruction.Type;
import exception.MipsRuntimeException;
import exception.OverflowException;

public class RAddu extends RFormatInstruction
{
	public RAddu(){}
	public RAddu(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs, rt, rd, shiftAmount);
	}

	public void execute() throws MipsRuntimeException {
		super.execute();
		rd.setValue((int)(rs.getUnsignedValue()+ rt.getUnsignedValue()));
	}
	
	public int getFunc() {
		return 33;
	}
	
	@Override
	public String getName() {
		return "addu";
	}
	
	@Override
	public Type getType() {
		return Type.ALU;
	}
}
