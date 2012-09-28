package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Immediate;
import model.Register;
import model.Word;
import model.instruction.Instruction;
import model.instruction.PseudoInstruction;
import model.instruction.SimpleInstruction;
import control.Controller;
import exception.OperandOutOfRangeException;
import exception.MipsCompileException;
import exception.MipsException;
import exception.WrongArgumentException;

public abstract class IFormatInstruction extends SimpleInstruction 
{
	protected Register rs, rt;
	protected Immediate immediate;
	
	
	
	public IFormatInstruction(){}
	public IFormatInstruction(Register rs, Register rt, Immediate immediate){
		this.rs = rs;
		this.rt = rt;
		this.immediate = immediate;
	}
	
	public Word[] generateMachineCode(Word address){
		Word word = new Word(0);
		word.setSubWord(getOpCode(), 26, 31);
		if(rs != null)
			word.setSubWord(rs.getRegisterNumber(), 21, 25);
		if(rt != null)
		word.setSubWord(rt.getRegisterNumber(), 16, 20);
		if(immediate != null)
			word.setSubWord((int)immediate.getValue(), 0,15);
		return new Word[]{word};
	}
	
	@Override
	public String toString() {
		return getName() + " $" + rt.getRegisterNumber() + ", $" + rs.getRegisterNumber() + ", " + immediate.getValue();
	}
	
	// ############ STATIC #############

	public static HashMap<String, Class<Instruction>> instructionNameHash  = new HashMap<String, Class<Instruction>>();
	
	public static Instruction parseLine(String instructionName, String line) throws MipsCompileException{
		Instruction instruction = null;
		
		Register rs = null, rt=null;
		Immediate imm = null;
		
		Matcher matcher = Pattern.compile("\\s*" + registerRegex + "\\s*[,]\\s*"+ registerRegex+"\\s*[,]\\s*"+immediateRegex).matcher(line);
		if(!matcher.find())
			throw new WrongArgumentException(instructionName + " " + line);
		rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
		rs = Controller.getInstance().getRegisterFile().getRegister(matcher.group(2));
		String s = matcher.group(3);
		long l = Long.decode(s);
		Class<Instruction> clazz;
		// If operand is big, check too see if pseudo exists and use that, otherwise throw exception
		if(l >= Math.pow(2, 16)){
			if(l >= Math.pow(2, 32))
				throw new OperandOutOfRangeException(s);
			clazz = PseudoInstruction.getInstruction(instructionName);
			if(clazz == null)
				throw new OperandOutOfRangeException(s);
		}else{
			clazz = getInstruction(instructionName);
			// If class doesn't exist in IFormat, check Psuedos,  such as subi
			if(clazz == null)
				clazz = PseudoInstruction.getInstruction(instructionName);
		}
		imm = new Immediate((int)l);
		if(clazz == null)
			throw new MipsException(clazz + " Instruction doesn't exist in instructions");
		try{
			instruction = clazz.getConstructor(Register.class,Register.class,Immediate.class).newInstance(rs,rt,imm);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			throw new MipsException("Bad Constructor Exception Class: "+ clazz);
		}
		return instruction;
	}

	public static Class<IFormatInstruction>[] getIFormatInstructions(){
		return Instruction.getIFormatInstructions();
	}
	
	public static Class<Instruction> getInstruction(String instructionName){
		return instructionNameHash.get(instructionName);
	}
	public Register getRs() {
		return rs;
	}
	public Register getRt() {
		return rt;
	}
	public Immediate getImmediate(){
		return immediate;
	}
}
