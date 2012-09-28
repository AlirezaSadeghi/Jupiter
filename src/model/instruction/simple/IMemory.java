package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Immediate;
import model.Label;
import model.Register;
import model.RegisterFile;
import model.instruction.Instruction;
import model.instruction.PseudoInstruction;
import model.instruction.Type;
import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.OperandOutOfRangeException;
import exception.WrongArgumentException;

public abstract class IMemory extends IFormatInstruction
{
	public IMemory(){
	}
	public IMemory(Register rs, Register rt, Immediate immediate) {
		super(rs, rt, immediate);
	}
	
	public String toString() {
		return getName() + " $" + rt.getRegisterNumber() + ", " + immediate.getValue() +"($" + rs.getRegisterNumber() + ")";
	}
	
	@Override
	public Type getType() {
		return Type.Memory;
	}
	
	// ############ STATIC #############
	public static void main(String[] args) {
		Matcher matcher = Pattern.compile(registerRegex + "\\s*[,]\\s*" + labelRegex + "\\s*[\\+]\\s*([0-9]*)\\s*(?:[(]\\s*" + registerRegex + "\\s*[)])?")
				.matcher("la $t1, a + 1");
		System.out.println(matcher.find());
	}
	public static Instruction parseLine(String instructionName, String line) throws MipsCompileException{
		Instruction instruction = null;
		
		Register rs = null, rt=null;
		Immediate imm = null;
		
		Matcher matcher = Pattern.compile(registerRegex + "\\s*[,]\\s*" + "([0-9]+)\\s*(?:[(]\\s*" + registerRegex + "\\s*[)])?").matcher(line);
		if(matcher.find()){
			rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
			
			Class<Instruction> clazz;
			
			String s = matcher.group(2);
			long l = 0;
			clazz = getInstruction(instructionName);
			if(s.length() > 0){
				l = Long.decode(s);
				imm = new Immediate((int)l);
			}else{
				imm = new Immediate(0);
			}
			
			s = matcher.group(3);
			if(s != null){
				rs = Controller.getInstance().getRegisterFile().getRegister(s);
			}else
				rs = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
			
			if(clazz == null)
				throw new MipsException(clazz + " Instruction doesn't exist in instructions");
			
			if(l >= Math.pow(2, 15)){
				if(l >= Math.pow(2, 32))
					throw new OperandOutOfRangeException();
				return new PMemory(rs,rt, null, imm, clazz);
			}
			
			try{
				instruction = clazz.getConstructor(Register.class,Register.class,Immediate.class).newInstance(rs,rt,imm);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				throw new MipsException("Bad Constructor Exception Class: "+ clazz);
			}
			return instruction;
		}
		
		matcher = Pattern.compile(registerRegex + "\\s*[,]\\s*" + labelRegex + "\\s*[\\+]\\s*([0-9]*)\\s*(?:[(]\\s*" + registerRegex + "\\s*[)])?").matcher(line);
		if(matcher.find()){
			rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
			
			Class<Instruction> clazz;
			
			Label label;
			String s = matcher.group(2);
			if(s!= null && s.length() > 0)
				label = Controller.getInstance().getLabelManager().getLabel(s);
			else
				label = null;
			
			s = matcher.group(3);
			if(s != null && s.length()>0){
				long l = Long.decode(s);
				if(l >= Math.pow(2, 32))
					throw new OperandOutOfRangeException(s);
				else
					imm = new Immediate((int)l);
			}else
				imm = new Immediate(0);
			
			s = matcher.group(4);
			if(s != null && s.length()>0){
				rs = Controller.getInstance().getRegisterFile().getRegister(s);
			}else
				rs = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
			
			
			clazz = getInstruction(instructionName);
			if(clazz == null)
				throw new MipsException(clazz + " Instruction doesn't exist in instructions");
			return new PMemory(rs,rt, label, imm, clazz);
		}
		
		matcher = Pattern.compile(registerRegex + "\\s*[,]\\s*" + labelRegex).matcher(line);
		if(matcher.find()){
			rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
			
			Class<Instruction> clazz;
			
			Label label;
			String s = matcher.group(2);
			if(s!= null && s.length() > 0)
				label = Controller.getInstance().getLabelManager().getLabel(s);
			else
				label = null;
			
			imm = new Immediate(0);
			
			rs = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
			
			clazz = getInstruction(instructionName);
			if(clazz == null)
				throw new MipsException(clazz + " Instruction doesn't exist in instructions");
			return new PMemory(rs,rt, label, imm, clazz);
		}
		throw new WrongArgumentException(instructionName + " " + line);
	}
	
}
