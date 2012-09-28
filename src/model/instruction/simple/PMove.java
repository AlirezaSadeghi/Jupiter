package model.instruction.simple;

import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import control.Controller;
import exception.MipsCompileException;
import exception.MipsException;
import exception.WrongArgumentException;
import model.Register;
import model.RegisterFile;
import model.Word;
import model.instruction.Instruction;
import model.instruction.PseudoInstruction;

public class PMove extends PseudoInstruction
{
	public PMove() {}
	public PMove(Register source, Register dest){
		this.source = source;
		this.dest = dest;
	}
	
	Register source;
	Register dest;
	@Override
	public int getSimpleInstructionCount() {
		return 1;
	}

	@Override
	public Word[] generateMachineCode(Word address) {
		Register zero = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
		return new Word[]{new RAddu(dest,zero,source,0).generateMachineCode(address)[0]};
	}
	
	public static Instruction parseLine(String instructionName,String line) throws MipsCompileException{
		Register rs = null,rt = null;
		Matcher matcher = Pattern.compile("\\s*" + registerRegex + "\\s*[,]\\s*" + registerRegex).matcher(line);
		if(!matcher.find())
			throw new WrongArgumentException(instructionName + " " + line);
		rs = Controller.getInstance().getRegisterFile().getRegister(matcher.group(1));
		rt = Controller.getInstance().getRegisterFile().getRegister(matcher.group(2));
		return new PMove(rt,rs);
	}

}
