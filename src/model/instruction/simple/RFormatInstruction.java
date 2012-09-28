package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Register;
import model.Word;
import model.instruction.Instruction;
import model.instruction.SimpleInstruction;
import model.instruction.Type;
import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.NoSuchRegisterException;
import exception.WrongArgumentException;

public abstract class RFormatInstruction extends SimpleInstruction
{
	protected Register rs,rt,rd;
	protected int shiftAmount;
	
	public RFormatInstruction() {
	}
	
	public RFormatInstruction(Register rs, Register rt, Register rd, int shiftAmount){
		this.rs = rs;
		this.rt = rt;
		this.rd = rd;
		this.shiftAmount = shiftAmount;
	}
	
	public abstract int getFunc();
	
	public int getOpCode(){
		return 0;
	}
	
	public Type getType() {
		return Type.ALU;
	}
	
	public Word[] generateMachineCode(Word address){
		Word word = new Word(0);
		word.setSubWord(getOpCode(), 26, 31);
		if(rs != null)
			word.setSubWord(rs.getRegisterNumber(), 21, 25);
		if(rt != null)
			word.setSubWord(rt.getRegisterNumber(), 16, 20);
		if(rd != null)
			word.setSubWord(rd.getRegisterNumber(), 11, 15);
		word.setSubWord(shiftAmount,6,10);
		word.setSubWord(getFunc(), 0, 5);
		return new Word[]{word};
	}
	
	@Override
	public String toString() {
		return getName() + " $" + rd.getRegisterNumber() + ", $" + rs.getRegisterNumber() + ", $" + rt.getRegisterNumber();
	}
	
	public Register getRs() {
		return rs;
	}
	
	public Register getRt() {
		return rt;
	}
	
	public Register getRd() {
		return rd;
	}
	public int getShiftAmount(){
		return this.shiftAmount;
	}
	// ############ STATIC #############
	


	public static HashMap<String, Class<Instruction>> instructionNameHash = new HashMap<String, Class<Instruction>>();
	
	public static Instruction parseLine(String instructionName,String line) throws MipsCompileException{
		Register rs = null,rt = null,rd = null;
		Class<Instruction> clazz = null;
		Matcher matcher = Pattern.compile("\\s*" + registerRegex + "\\s*[,]\\s*" + registerRegex + "\\s*[,]\\s*" + registerRegex).matcher(line);
		if(!matcher.find())
			throw new WrongArgumentException(instructionName + " " + line);
		rd = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
		rs = Controller.getInstance().getRegisterFile().getRegister(matcher.group(2));
		rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(3));
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
	
	public static Class<RFormatInstruction>[] getRFormatInstructions(){
		return Instruction.getRFormatInstructions();
	}
	
	public static Class<Instruction> getInstruction(String instructionName){
		return instructionNameHash.get(instructionName);
	}
}