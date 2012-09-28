package control;

import java.util.ArrayList;

import model.instruction.SimpleInstruction;
import model.instruction.Type;
import model.instruction.simple.IFormatInstruction;
import model.instruction.simple.JFormatInstruction;

public class Statistics {
	public Statistics() {
		
	}
	public Integer iFormatNumber=0 , rFormatNumber=0 , jFormatNumber=0 ;
	public Integer ALUNumber=0 , memoryNumber=0 , jumpNumber=0 , branchNumber=0 , otherNumber =0  ;
	public void incIFormat( ){
		iFormatNumber++;
	}
	
	public void incRFormat( ){
		rFormatNumber++;
	}
	
	public void incJFormat( ){
		jFormatNumber++;
	}
	
	public int getIFormatNumber (){
		return iFormatNumber;
	}
	
	public int getRFormatNumber (){
		return rFormatNumber;
	}
	
	public int getJFormatNumber (){
		return jFormatNumber;
	}
	
	void staticIncrease (SimpleInstruction simple  ){
		if ( simple instanceof JFormatInstruction )
			this.incJFormat();
		else if  ( simple instanceof IFormatInstruction )
			this.incIFormat();
		else this.incRFormat();
		
		if ( simple.getType() == Type.ALU)
			ALUNumber++;
		else if ( simple.getType() == Type.Memory)
			memoryNumber++;
		else if ( simple.getType() == Type.Branch)
			branchNumber++;
		else if ( simple.getType() == Type.Jump)
			jumpNumber++;
		else otherNumber++;
	}
	
	public int getALUNumber ( ){
		return ALUNumber;
	}
	public int getMemoryNumber() {
		return memoryNumber;
	}
	public int getJumpNumber( ) {
		return jumpNumber;
	}
	public int getBranchNumber ( ) {
		return branchNumber;
	}
	public int getOtherNumber() {
		return otherNumber;
	}
	
	public int[] getFormatValArray(){
		return new int[]{rFormatNumber, iFormatNumber, jFormatNumber};
	}
	
	public static String[] getFormatNameArray(){
		return new String[]{"R Format", "I Format", "J Format"};
	}
	
	public int[] getTypeValArray(){
		return new int[]{ALUNumber, memoryNumber, jumpNumber, branchNumber, otherNumber};
	}
	
	public static String[] getTypeNameArray(){
		return new String[]{"ALU","Memory","Jump","Branch","Other"};
	}

}