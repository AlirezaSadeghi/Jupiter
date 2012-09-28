package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.NoSuchLabelException;
import exception.WrongArgumentException;
import model.Immediate;
import model.Label;
import model.Register;
import model.RegisterFile;
import model.Word;
import model.instruction.Instruction;
import model.instruction.PseudoInstruction;

public class PLa extends PseudoInstruction
{
	public PLa(){}
	public PLa(Register rt, Label label){
		this.rt = rt;
		this.label = label;
	}
	
	Register rt;
	Label label;
	
	@Override
	public int getSimpleInstructionCount() {
		return 2;
	}

	@Override
	public Word[] generateMachineCode(Word address) {
		Register at = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.at);
		Word[] words = new Word[2];
		words[0] = (new ILui(at, this.label.getHigh())).generateMachineCode(address)[0];
		words[1] = (new IOri(at, this.rt, this.label.getLow())).generateMachineCode(address.add(4))[0];
		return words;
	}
	
	public static Instruction parseLine(String instructionName, String line) throws MipsCompileException{
		Instruction instruction = null;
		Label lab = null;
		Matcher matcher;
		
		matcher = Pattern.compile(registerRegex + "\\s*[,]\\s*" + labelRegex).matcher(line);
		if(matcher.find()){
			Register rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
			String s = matcher.group(2);
			lab = Controller.getInstance().getLabelManager().getLabel(s);
			if(lab == null)
				throw new NoSuchLabelException(s);
			Class<Instruction> clazz = getInstruction(instructionName);
			if(clazz == null)
				throw new MipsException(clazz + " Instruction doesn't exist in instructions");
			try{
				instruction = clazz.getConstructor(Register.class ,Label.class).newInstance(rt, lab);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				throw new MipsException("Bad Constructor Exception Class: "+ clazz);
			}
			return instruction;
		}
		throw new WrongArgumentException(instructionName + " " + line);
	}
	

}
