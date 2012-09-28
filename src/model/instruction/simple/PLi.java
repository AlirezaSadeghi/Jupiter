package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.NoSuchLabelException;
import exception.OperandOutOfRangeException;
import exception.WrongArgumentException;
import model.Immediate;
import model.Label;
import model.Register;
import model.RegisterFile;
import model.Word;
import model.instruction.Instruction;
import model.instruction.PseudoInstruction;

public class PLi extends PseudoInstruction
{
	public PLi() {}
	public PLi(Register r, Immediate immediate){
		this.r = r;
		this.immediate = immediate;
	}
	Register r;
	Immediate immediate;
	
	@Override
	public int getSimpleInstructionCount() {
		if(immediate.is16bit())
			return 1;
		else
			return 2;
	}

	@Override
	public Word[] generateMachineCode(Word address) {
		Register at = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.at);
		Register zero = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
		if(immediate.is16bit()){
			Word w = (new IAddiu(zero, this.r, this.immediate)).generateMachineCode(address)[0];
			return new Word[]{w};
		}else{
			Word[] words = new Word[2];
			words[0] = (new ILui(at, this.immediate.getHigh())).generateMachineCode(address)[0];
			words[1] = (new IOri(at, this.r, this.immediate.getLow())).generateMachineCode(address.add(4))[0];
			return words;
		}
	}
	
	public static Instruction parseLine(String instructionName, String line) throws MipsCompileException{
		Instruction instruction = null;
		Immediate imm = null;
		Matcher matcher;
		
		matcher = Pattern.compile(registerRegex + "\\s*[,]\\s*" + immediateRegex).matcher(line);
		if(matcher.find()){
			Register rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
			String s = matcher.group(2);
			long l = Long.decode(s);
			if(l >= Math.pow(2, 32))
				throw new OperandOutOfRangeException(s);
			imm = new Immediate((int)l);
			Class<Instruction> clazz = getInstruction(instructionName);
			if(clazz == null)
				throw new MipsException(clazz + " Instruction doesn't exist in instructions");
			try{
				instruction = clazz.getConstructor(Register.class ,Immediate.class).newInstance(rt, imm);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
					| SecurityException e) {
				throw new MipsException("Bad Constructor Exception Class: "+ clazz);
			}
			return instruction;
		}
		throw new WrongArgumentException(instructionName + " " + line);
	}

}
