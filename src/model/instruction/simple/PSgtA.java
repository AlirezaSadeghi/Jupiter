package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.WrongArgumentException;
import model.Immediate;
import model.Register;
import model.Word;
import model.instruction.Instruction;
import model.instruction.PseudoInstruction;

public class PSgtA extends PseudoInstruction
{
	protected Register rs, rt, rd; 
	
	public PSgtA(Register rd , Register rs , Register rt)
	{
		this.rd = rd ;
		this.rs = rs ;
		this.rt = rt ;
	}
	
	public int getSimpleInstructionCount() {
		return 1;
	}

	public Word[] generateMachineCode(Word address) {
		Word [] word = new Word[1] ;
		word[0] = new RSlt(this.rs, this.rt, this.rd,0).generateMachineCode(address)[0];
		return word ;
	}
	
	public static Instruction parseLine(String instructionName,String line) throws MipsCompileException{
		Register rs = null,rt = null,rd = null;
		Class<Instruction> clazz = null;
		Matcher matcher = Pattern.compile("\\s*" + registerRegex + "\\s*[,]\\s*" + registerRegex + "\\s*[,]\\s*" + registerRegex).matcher(line);
		if(matcher.find()){
			rd = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
			rs = Controller.getInstance().getRegisterFile().getRegister(matcher.group(2));
			rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(3));
			
			return new PSgtA(rs,rt,rd);
		}
		
		matcher = Pattern.compile("\\s*" + registerRegex + "\\s*[,]\\s*" + registerRegex + "\\s*[,]\\s*" + immediateRegex).matcher(line);
		if(matcher.find()){
			rs = Controller.getInstance().getRegisterFile().getRegister(matcher.group(2));
			rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(3));
			String s = matcher.group(3);
			long l = Long.decode(s);
			return new PSgtB(rs,rt,new Immediate((int)l));
		}
		
		throw new WrongArgumentException(instructionName + " " + line);
		
	}

}
