package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Immediate;
import model.Register;
import model.Word;
import model.instruction.Instruction;
import model.instruction.PseudoInstruction;
import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.NoSuchLabelException;
import exception.OperandOutOfRangeException;
import exception.WrongArgumentException;

public class MBranch
{
	public static Instruction parseLine(String instructionName, String line) throws MipsCompileException{
		Instruction instruction = null;
		
		// A type  $s0 $s1 L
		Matcher matcher = Pattern.compile(Instruction.registerRegex + "\\s*[,]\\s*" + Instruction.registerRegex + "\\s*[,]\\s*" + Instruction.labelRegex).matcher(line);
		if(matcher.find()){
			Register rs = null, rt=null;
			Immediate imm = null;
			rs = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
			rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(2));
			String s = matcher.group(3);
			imm = Controller.getInstance().getLabelManager().getLabel(s);
			if(imm == null)
				throw new NoSuchLabelException(s);
			Class<Instruction> clazz = IFormatInstruction.getInstruction(instructionName);
			if(clazz == null)
				clazz = PseudoInstruction.getInstruction(instructionName+"-a");
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
		
		// B type $s0 0 L
		matcher = Pattern.compile(Instruction.registerRegex + "\\s*[,]\\s*" + Instruction.immediateRegex + "\\s*[,]\\s*" + Instruction.labelRegex).matcher(line);
		if(matcher.find()){
			Register r = null;
			Immediate num = null;
			Immediate label = null;
			r = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
			String s = matcher.group(2);
			int l = Integer.decode(s);
			if(l >= Math.pow(2, 32))
				throw new OperandOutOfRangeException(s);
			num = new Immediate(l);
			s = matcher.group(3);
			label = Controller.getInstance().getLabelManager().getLabel(s);
			if(label == null)
				throw new NoSuchLabelException(s);
			Class<Instruction> 	clazz = PseudoInstruction.getInstruction(instructionName);
			if(clazz == null)
				clazz = PseudoInstruction.getInstruction(instructionName+"-b");
			if(clazz == null)
				throw new MipsException(clazz + " Instruction doesn't exist in instructions");
			try{
				instruction = clazz.getConstructor(Register.class,Immediate.class,Immediate.class).newInstance(r,num,label);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				throw new MipsException("Bad Constructor Exception Class: "+ clazz);
			}
			return instruction;
		}
		throw new WrongArgumentException(instructionName + " " + line);
	}
}