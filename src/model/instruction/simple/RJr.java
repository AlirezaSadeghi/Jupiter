package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Register;
import model.RegisterFile;
import model.instruction.Instruction;
import model.instruction.Type;
import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.MipsRuntimeException;
import exception.WrongArgumentException;

public class RJr extends RFormatInstruction {

	public RJr(){}
	public RJr(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs, rt, rd, shiftAmount);
	}

	
	public void execute() throws MipsRuntimeException {
		super.execute();
		Register pc = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.pc);
		
		pc.setValue(rs.getValue());
	}
	

	@Override
	public Type getType() {
		return Type.Jump;
	}
	
	@Override
	public String getName() {
		return "jr";
	}
	
	@Override
	public int getFunc() {
		return 8;
	}
	
	public static Instruction parseLine(String instructionName,String line) throws MipsCompileException{
		Register rs = null;
		Class<Instruction> clazz = null;
		Matcher matcher = Pattern.compile("\\s*" + registerRegex).matcher(line);
		if(!matcher.find())
			throw new WrongArgumentException(instructionName + " " + line);
		rs = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
		return new RJr(rs,null,null,0);
	}

}
