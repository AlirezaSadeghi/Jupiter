package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.OperandOutOfRangeException;
import exception.WrongArgumentException;
import model.Immediate;
import model.Register;
import model.instruction.Instruction;

public abstract class RShift extends RFormatInstruction
{
	public RShift(){}
	public RShift(Register rs, Register rt, Register rd, int shiftAmount) {
		super(rs,rt,rd,shiftAmount);
	}

	@Override
	public String toString() {
		return getName() + " $" + rd.getRegisterNumber() + ", $" + rs.getRegisterNumber() + ", " + shiftAmount;
	}
	
	// ############ STATIC #############
	
	public static Instruction parseLine(String instructionName, String line) throws MipsCompileException{
	Instruction instruction = null;
		
		Register rd = null, rt=null;
		int shiftAmount= 0;
		
		Matcher matcher = Pattern.compile("[$]([0-9a-z]{1,2})\\s*[,]\\s*[$]([0-9a-z]{1,2})\\s*[,]\\s*([(?:0x)[-]]?[0-9]+)").matcher(line);
		if(!matcher.find())
			throw new WrongArgumentException(instructionName + " " + line);
		rd = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
		rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(2));
		String s = matcher.group(3);
		shiftAmount = Integer.decode(s);
		if(shiftAmount > 31)
			throw new OperandOutOfRangeException(s);
		Class<Instruction> clazz = getInstruction(instructionName);
		if(clazz == null)
			throw new MipsException(clazz + " Instruction doesn't exist in instructions");
		try{
			instruction = clazz.getConstructor(Register.class,Register.class,Register.class,int.class).newInstance(null,rt,rd,shiftAmount);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new MipsException("Bad Constructor Exception Class: "+clazz);
		}
		return instruction;
	}
}
