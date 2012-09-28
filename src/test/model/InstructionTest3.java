package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import model.Label;
import model.RegisterFile;
import model.Word;
import model.instruction.Instruction;
import model.instruction.SimpleInstruction;
import model.instruction.simple.IFormatInstruction;
import model.instruction.simple.RFormatInstruction;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import control.Controller;
import exception.MipsCompileException;
import exception.MipsRuntimeException;

public class InstructionTest3
{
	@BeforeClass
	public static void init(){
		try {
			Instruction.init();
			address = new Word("0x00000000");
		} catch (Exception e ) {
			e.printStackTrace();
		}
		rFile = Controller.getInstance().getRegisterFile();
	}
	public final static int zero = 0, at = 1,  v0 = 2,  v1 = 3,  a0 = 4,  a1 = 5,  a2 = 6,
	 a3 = 7,   t0 = 8,  t1 = 9,  t2 = 10, t3 = 11, t4 = 12, t5 = 13,
	 t6 = 14,  t7 = 15, s0 = 16, s1 = 17, s2 = 18, s3 = 19, s4 = 20,
	 s5 = 21,  s6 = 22, s7 = 23, t8 = 24, t9 = 25, k0 = 26, k1 = 27,
	 gp = 28,  sp = 29, fp = 30, ra = 31, pc = 32, lo = 33, hi = 34;
	
	static RegisterFile rFile;
	static Word address;
	
	@Before
	public void before(){
		address.setValue(0);
	}
	
	@Test
	public void testILw(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "lw $s2, 10010000"; 		// har addressi ke .data neveshte mishe.
			Controller.getInstance().getLabelManager().addLabel(new Label("alo", Controller.dataStart.getValue()));
			Controller.getInstance().getLabelManager().addLabel(new Label("mamal",Controller.dataStart.getValue()+4));
			// dar .date in neveshte she
			// alo: .byte 0,1,2,3,4,5,6,128
			// mamal: .word -2346
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),2);
			Word[] code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x3c010099"));
			assertEquals(code[1],new Word("0x8c32bd90"));
			
			SimpleInstruction si1 = Instruction.decodeInstruction(code[0]);
			SimpleInstruction si2= Instruction.decodeInstruction(code[1]);
			si1.execute();
			si2.execute();
			assertEquals(rFile.getRegister(at).getHexValue(), "0x10010000");
			assertEquals(rFile.getRegister(s2).getHexValue(), "0x03020100");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000008");
			
		
			
			s = "lw  $s2, 8($s2)";
			rFile.getRegister(pc).setValue("0x00000000");
			rFile.getRegister(s2).setValue("0x10010000");
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x8e520008"));
			si1 = Instruction.decodeInstruction(code[0]);
			si1.execute();
			assertEquals(rFile.getRegister(s2).getValue(), -2346);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
		
			
			
			rFile.getRegister(pc).setValue("0x00000000");
			s = "lw  $s2, mamal";
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),2);
			code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x3c011001"));
			assertEquals(code[1],new Word("0x8e320008"));
			si1 = Instruction.decodeInstruction(code[0]);
			si2= Instruction.decodeInstruction(code[1]);
			si1.execute();
			si2.execute();
			assertEquals(rFile.getRegister(at).getHexValue(), "0x10010000");
			assertEquals(rFile.getRegister(s2).getValue(), -2346);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000008");
		
			
			
			
			s = "lw  $s2, alo + 4($s2)";
			rFile.getRegister(pc).setValue("0x00000000");
			rFile.getRegister(s2).setValue(4);
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),3);
			code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x3c011001"));
			assertEquals(code[1],new Word("0x00320821"));
			assertEquals(code[2],new Word("0x8c320004"));
			
			si1 = Instruction.decodeInstruction(code[0]);
			si2= Instruction.decodeInstruction(code[1]);
			SimpleInstruction si3 = Instruction.decodeInstruction(code[2]);
			si1.execute();
			si2.execute();
			si3.execute();
			assertEquals(rFile.getRegister(s2).getHexValue(), "0xfffff6d6");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x0000000c");
			
			
			s = "lw  $s2, alo + 4";
			rFile.getRegister(pc).setValue("0x00000000");
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),2);
			code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x3c011001"));
			assertEquals(code[1],new Word("0x8c320004"));
			
			si1 = Instruction.decodeInstruction(code[0]);
			si2= Instruction.decodeInstruction(code[1]);
			si1.execute();
			si2.execute();
			assertEquals(rFile.getRegister(s2).getHexValue(), "0x80060504");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000008");
			
			
			s = "lw  $s2, 4 + alo";
			rFile.getRegister(pc).setValue("0x00000000");
			i = Instruction.parseLine(s);
			code = i.generateMachineCode(address);
			si1 = Instruction.decodeInstruction(code[0]);
			si1.execute();
			fail(); //formate dastur kharabe.
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}
	
	@Test
	public void testILi(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "li $s2, 10000"; 
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			Word[] code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x24122710"));	// tabdil mishe be addiu
			SimpleInstruction si1 = Instruction.decodeInstruction(code[0]);
			si1.execute();
			assertEquals(rFile.getRegister(s2).getValue(), 10000);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
			
			
			
			rFile.getRegister(pc).setValue("0x00000000");
			s = "li $s2, 10010000";
			i = Instruction.parseLine(s);
			code = i.generateMachineCode(address);
			assertEquals(i.getSimpleInstructionCount(),2);
			si1 = Instruction.decodeInstruction(code[0]);
			SimpleInstruction si2 = Instruction.decodeInstruction(code[1]); 
			assertEquals(code[0],new Word("0x3c010098"));
			assertEquals(code[1],new Word("0x3432bd90"));
			si1.execute();
			si2.execute();
			assertEquals(rFile.getRegister(s2).getValue(), 10010000);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
		} catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}
	
	
	
	@Test
	public void testILa(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "la $s2, alo"; 
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),2);
			Word[] code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x3c010098"));	
			assertEquals(code[1],new Word("0x3432bd90"));	
			SimpleInstruction si1 = Instruction.decodeInstruction(code[0]);
			SimpleInstruction si2 = Instruction.decodeInstruction(code[1]);
			si1.execute();
			si2.execute();
			assertEquals(rFile.getRegister(s2).getHexValue(), "0x10010008");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000008");

		} catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}
	public static void main(String[] args) {
		new InstructionTest3();
	}
}
