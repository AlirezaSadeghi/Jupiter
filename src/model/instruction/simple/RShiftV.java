package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.WrongArgumentException;
import model.Register;
import model.Word;
import model.instruction.Instruction;

public abstract class RShiftV extends RFormatInstruction
{

	public RShiftV() {
	}
	
	public RShiftV(Register rs, Register rt, Register rd, int shiftAmount){
		super(rs,rt,rd,shiftAmount);
	}
	
	
	@Override
	public String toString() {
		return getName() + " $" + rd.getRegisterNumber() + ", $" + rt.getRegisterNumber() + ", $" + rs.getRegisterNumber();
	}
	
	
	public static Instruction parseLine(String instructionName,String line) throws MipsCompileException{
		Register rs = null,rt = null,rd = null;
		Class<Instruction> clazz = null;
		Matcher matcher = Pattern.compile("\\s*[$]([0-9a-z]{1,2})\\s*[,]\\s*[$]([0-9a-z]{1,2})\\s*[,]\\s*[$]([0-9a-z]{1,2})").matcher(line);
		if(!matcher.find())
			throw new WrongArgumentException(instructionName + " " + line);
		rd = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
		// RT then RS
		rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(2));
		rs = Controller.getInstance().getRegisterFile().getRegister(matcher.group(3));
		clazz = getInstruction(instructionName);
		if(clazz == null)
			throw new MipsException(clazz + " Instruction doesn't exist in instructions");
		
		try {
			return clazz.getConstructor(Register.class,Register.class,Register.class, int.class).newInstance(rs,rt,rd,0);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new MipsException("Bad Constructor Exception Class: "+clazz);
		}
	}

}
