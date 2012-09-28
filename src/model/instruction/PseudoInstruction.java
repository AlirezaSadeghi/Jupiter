package model.instruction;

import java.util.HashMap;

public abstract class PseudoInstruction extends Instruction
{
	public abstract int getSimpleInstructionCount();
	
	
	// ############ STATIC #############
	
	public static HashMap<String, Class<Instruction>> instructionNameHash = new HashMap<String, Class<Instruction>>();
	
	public static Class<PseudoInstruction>[] getPseudoInstructions(){
		return Instruction.getPseudoInstructions();
	}
	
	public static Class<Instruction> getInstruction(String instructionName){
		return instructionNameHash.get(instructionName);
	}
}