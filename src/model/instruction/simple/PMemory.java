package model.instruction.simple;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import model.Immediate;
import model.Label;
import model.Register;
import model.RegisterFile;
import model.Word;
import model.instruction.Instruction;
import model.instruction.PseudoInstruction;
import control.Controller;

public class PMemory extends PseudoInstruction
{
	public PMemory(){}
	public PMemory(Register rs, Register rt, Label label, Immediate immediate, Class<Instruction> clazz){
		this.rs = rs;
		this.rt = rt;
		this.label = label;
		this.immediate = immediate;
		this.clazz = clazz;
	}
	
	Register rs;
	Register rt;
	Label label;
	Immediate immediate;
	Class<Instruction> clazz;
	
	@Override
	public int getSimpleInstructionCount() {
		if(rs.getRegisterNumber() == 0)
			return 2;
		else
			return 3;
	}

	@Override
	public Word[] generateMachineCode(Word address) {
		Register at = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.at);
		Register zero = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.zero);
		Immediate imm = new Immediate((immediate == null ? 0 :this.immediate.getValue()) + (label==null ? 0 :this.label.getValue()));
		Constructor c = null;
		try {
			c =  clazz.getConstructor(Register.class,Register.class,Immediate.class);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
//		imm.setValue(imm.getValue()); // WHY?
		if(rs.getRegisterNumber() == 0){
			Word [] words = new Word [2];
			words[0] = (new ILui(at, imm.getHigh())).generateMachineCode(address)[0];
			try {
				words[1] = ((IFormatInstruction)c.newInstance(at,this.rt,imm.getLow())).generateMachineCode(address.add(4))[0];
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			return words; 
		}else{
			Word [] words = new Word [3];
			words[0] = (new ILui(at, imm.getHigh())).generateMachineCode(address)[0];
			words[1] = (new RAddu(at, this.rt, at,0)).generateMachineCode(address.add(4))[0];
			try {
				words[2] = ((IFormatInstruction)c.newInstance(at,this.rt,imm.getLow())).generateMachineCode(address.add(8))[0];
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			return words;
		}
	}
}
