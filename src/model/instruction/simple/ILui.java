package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.MipsRuntimeException;
import exception.OperandOutOfRangeException;
import exception.WrongArgumentException;
import model.Immediate;
import model.Register;
import model.instruction.Instruction;
import model.instruction.PseudoInstruction;
import model.instruction.Type;

public class ILui extends IFormatInstruction {

	public ILui(){}
	public ILui(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}
	
	public ILui(Register rt, Immediate immediate){
		super(null,rt,immediate);
	}
	
	public void execute() throws MipsRuntimeException {
		super.execute();
		rt.setValue(immediate.getValue()<<16);
	}
	
	@Override
	public int getOpCode() {
		return 15;
	}

	public String getName() {
		return "lui";
	}
	
	@Override
	public Type getType() {
		return Type.ALU;
	}
	
	public static Instruction parseLine(String instructionName, String line) throws MipsCompileException{
		Instruction instruction = null;
		
		Register rt=null;
		Immediate imm = null;
		
		Matcher matcher = Pattern.compile("\\s*" + registerRegex + "\\s*[,]\\s*" + immediateRegex).matcher(line);
		if(!matcher.find())
			throw new WrongArgumentException(instructionName + " " + line);
		rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
		String s = matcher.group(2);
		long l = Long.decode(s);
		Class<Instruction> clazz;
		if(l >= Math.pow(2, 16))
			throw new OperandOutOfRangeException(s);
		clazz = getInstruction(instructionName);
		imm = new Immediate((int)l);
		if(clazz == null)
			throw new MipsException(clazz + " Instruction doesn't exist in instructions");
		try{
			instruction = clazz.getConstructor(Register.class,Register.class,Immediate.class).newInstance(null,rt,imm);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new MipsException("Bad Constructor Exception Class: "+ clazz);
		}
		return instruction;
	}

}
