package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Immediate;
import model.Register;
import model.Word;
import model.instruction.Instruction;
import model.instruction.SimpleInstruction;
import model.instruction.Type;
import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.NoSuchLabelException;
import exception.WrongArgumentException;

public abstract class JFormatInstruction extends SimpleInstruction
{
	Immediate immediate;
	public JFormatInstruction(){}
	public JFormatInstruction(Immediate immediate){
		this.immediate = immediate;
	}
	
	public Word[] generateMachineCode(Word address) {
		Word word = new Word(0);
		word.setSubWord(getOpCode(), 26, 31);
		if(immediate != null)
			word.setSubWord((int)immediate.getValue()/4, 0,25);
		return new Word[]{word};
	}
	
	public String toString() {
		return getName() + " " +Long.toHexString(immediate.getValue());
	}
	
	@Override
	public Type getType() {
		return Type.Jump;
	}
	// ############ STATIC #############
	
	public static HashMap<String, Class<Instruction>> instructionNameHash = new HashMap<String, Class<Instruction>>();
	
	public static Instruction parseLine(String instructionName, String line) throws MipsCompileException{
		Instruction instruction = null;
		Immediate imm = null;
		Matcher matcher;
		
		matcher = Pattern.compile(labelRegex).matcher(line);
		if(matcher.find()){
			String s = matcher.group(1);
			imm = Controller.getInstance().getLabelManager().getLabel(s);
			if(imm == null)
				throw new NoSuchLabelException(s);
			Class<Instruction> clazz = getInstruction(instructionName);
			if(clazz == null)
				throw new MipsException(clazz + " Instruction doesn't exist in instructions");
			try{
				instruction = clazz.getConstructor(Immediate.class).newInstance(imm);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				throw new MipsException("Bad Constructor Exception Class: "+ clazz);
			}
			return instruction;
		}
		throw new WrongArgumentException(instructionName + " " + line);
	}
	
	
	public static Class<JFormatInstruction>[] getAllJFormatInstructions(){
		return Instruction.getJFormatInstructions();
	}
	
	public static Class<Instruction> getInstruction(String instructionName){
		return instructionNameHash.get(instructionName);
	}
}
