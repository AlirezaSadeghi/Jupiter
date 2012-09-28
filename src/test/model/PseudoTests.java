package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import model.Label;
import model.RegisterFile;
import model.Word;
import model.instruction.Instruction;
import model.instruction.SimpleInstruction;
import model.instruction.simple.IFormatInstruction;
import model.instruction.simple.JFormatInstruction;
import model.instruction.simple.RFormatInstruction;

import org.junit.BeforeClass;
import org.junit.Test;

import control.Controller;

import exception.DuplicateLabelException;
import exception.MipsCompileException;
import exception.MipsRuntimeException;

public class PseudoTests
{
	@BeforeClass
	public static void init(){
		try {
			address = new Word("0x00000000");
			Instruction.init();
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
	
	@Test
	public void testPBgt(){
		try {
			Controller.getInstance().getLabelManager().addLabel(new Label("hadi", Integer.decode("0x00000018")));
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (DuplicateLabelException e1) {
			e1.printStackTrace();
		} // deghat!
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "bgt $s0, -32768, hadi";
			rFile.getRegister(s0).setValue(0);
			rFile.getRegister(s1).setValue(-100);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),3);
			Word[] code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x20018000"));
			assertEquals(code[1],new Word("0x0030082a"));
			assertEquals(code[1],new Word("0x14200003"));
			SimpleInstruction si1 = Instruction.decodeInstruction(code[0]);
			SimpleInstruction si2= Instruction.decodeInstruction(code[1]);
			SimpleInstruction si3= Instruction.decodeInstruction(code[2]);
			si1.execute();
			si2.execute();
			si3.execute();
			assertEquals(rFile.getRegister(s0).getValue(), 1);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x000000018");
			
			s = "bgt  $s0, 32768, hadi";
			rFile.getRegister(s0).setValue(0);
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),4);
			
			code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x3c010000"));
			assertEquals(code[1],new Word("0x34218000"));
			assertEquals(code[2],new Word("0x0201082a"));
			assertEquals(code[3],new Word("0x10200002"));
			si1 = Instruction.decodeInstruction(code[0]);
			si2 = Instruction.decodeInstruction(code[1]);
			si3 = Instruction.decodeInstruction(code[2]);
			SimpleInstruction si4 = Instruction.decodeInstruction(code[3]);
			si1.execute();
			si2.execute();
			si3.execute();
			si4.execute();
			assertEquals(rFile.getRegister(s0).getValue(), 0);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000010");
			
			
			
			s = "bgt  $s0, $2, hadi";
			rFile.getRegister(s0).setValue(32769);
			rFile.getRegister(2).setValue(9);
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),2);
			
			code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x0050082a"));
			assertEquals(code[1],new Word("0x14200004"));
			si1 = Instruction.decodeInstruction(code[0]);
			si2= Instruction.decodeInstruction(code[1]);
			si1.execute();
			si2.execute();
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x000000018");
			assertEquals(rFile.getRegister(s0).getValue(), 1);
			
			s = "bgt  $s0, -32768, hadiPalang!";
			i = Instruction.parseLine(s);
			code = i.generateMachineCode(address);
			si1 = Instruction.decodeInstruction(code[0]);
			si2= Instruction.decodeInstruction(code[1]);
			si1.execute();
			si2.execute();
			fail();		// labelesh cherte!
		} catch (MipsCompileException e) {
		e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}
	@Test
	public void testPBlt(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "blt $s0, $s1, hadi";
			rFile.getRegister(s0).setValue(0);
			rFile.getRegister(s1).setValue(45634747);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),2);
			Word[] code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x0211082a"));
			assertEquals(code[1],new Word("0x14200004"));
			
			SimpleInstruction si1 = Instruction.decodeInstruction(code[0]);
			SimpleInstruction si2= Instruction.decodeInstruction(code[1]);
			si1.execute();
			si2.execute();
			assertEquals(rFile.getRegister(s0).getValue(), 1);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x000000018");
			
			
			s = "blt  $s0, -32769, hadi";
			rFile.getRegister(s0).setValue(0);
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),4);
			
			code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x3c01ffff"));
			assertEquals(code[1],new Word("0x34217fff"));
			assertEquals(code[2],new Word("0x0201082a"));
			assertEquals(code[3],new Word("0x14200002"));
			si1 = Instruction.decodeInstruction(code[0]);
			si2 = Instruction.decodeInstruction(code[1]);
			SimpleInstruction si3 = Instruction.decodeInstruction(code[2]);
			SimpleInstruction si4 = Instruction.decodeInstruction(code[3]);
			si1.execute();
			si2.execute();
			si3.execute();
			si4.execute();
			assertEquals(rFile.getRegister(s0).getValue(), 1);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000018");
			
			
			
			s = "blt  $s0, -32768, hadi";
			rFile.getRegister(s1).setValue(-23683468);
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),2);
			
			code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x2a018000"));
			assertEquals(code[1],new Word("0x14200004"));
			si1 = Instruction.decodeInstruction(code[0]);
			si2= Instruction.decodeInstruction(code[1]);
			si1.execute();
			si2.execute();
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000008");
			assertEquals(rFile.getRegister(s0).getValue(), 0);
			
			s = "blt  $s0, -32768, hadiPalang!";
			rFile.getRegister(s1).setValue(-23683468);
			i = Instruction.parseLine(s);
			code = i.generateMachineCode(address);
			si1 = Instruction.decodeInstruction(code[0]);
			si2= Instruction.decodeInstruction(code[1]);
			si1.execute();
			si2.execute();
			fail();		// labelesh cherte!
		} catch (MipsCompileException e) {
		e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}
	@Test
	public void testPSgt(){
	
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "sgt $s0, $s1, 34590888";
			rFile.getRegister(s1).setValue(45634747);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),3);
			Word[] code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x3c01020f"));
			assertEquals(code[1],new Word("0x3421d0a8"));
			assertEquals(code[2],new Word("0x0031802a"));
			
			SimpleInstruction si1 = Instruction.decodeInstruction(code[0]);
			SimpleInstruction si2= Instruction.decodeInstruction(code[1]);
			SimpleInstruction si3 = Instruction.decodeInstruction(code[2]);
			si1.execute();
			assertEquals(rFile.getRegister(at).getHexValue(), "0x020f0000");
			si2.execute();
			assertEquals(rFile.getRegister(at).getHexValue(), "0x020fd08a");
			si3.execute();
			assertEquals(rFile.getRegister(s0).getValue(), 1);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x0000000C");
			
			
			s = "sgt  $s0, $s1, $s2";
			rFile.getRegister(s1).setValue(45634747);
			rFile.getRegister(s2).setValue(34590888);
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			
			code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x0251802a"));
			si1 = Instruction.decodeInstruction(code[0]);
			si1.execute();
			assertEquals(rFile.getRegister(s0).getValue(), 1);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
			
			s = "sgt  $s0, $s1, 125";
			rFile.getRegister(s1).setValue(0);
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),2);
			
			code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x2001007d"));
			assertEquals(code[1],new Word("0x0031802a"));
			si1 = Instruction.decodeInstruction(code[0]);
			si2= Instruction.decodeInstruction(code[1]);
			si1.execute();
			si2.execute();
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000008");
			assertEquals(rFile.getRegister(s0).getValue(), 0);
		} catch (MipsCompileException e) {
		e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}	
	@Test
	public void testPSubi(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "subi $s0, $s1, 34590888";
			rFile.getRegister(s1).setValue(125);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),3);
			/*RFormatInstruction r = (RFormatInstruction)i;			hannnnnnnn???
			assertEquals(r.getRs().getRegisterNumber(), 17);
			assertEquals(r.getRt().getRegisterNumber(), 18);
			assertEquals(r.getRd().getRegisterNumber(), 16);*/
			Word[] code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x3c01020f"));
			assertEquals(code[1],new Word("0x3421d0a8"));
			assertEquals(code[2],new Word("0x02218022"));
			
			SimpleInstruction si1 = Instruction.decodeInstruction(code[0]);
			SimpleInstruction si2= Instruction.decodeInstruction(code[1]);
			SimpleInstruction si3 = Instruction.decodeInstruction(code[2]);
			si1.execute();
			assertEquals(rFile.getRegister(at).getHexValue(), "0x020f0000");
			si2.execute();
			assertEquals(rFile.getRegister(at).getHexValue(), "0x020fd08a");
			si3.execute();
			assertEquals(rFile.getRegister(s0).getHexValue(), "0xfdf0dfd5");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x0000000C");
			
			
			s = "subi $s0, $s1, 345";
			rFile.getRegister(s1).setValue(125);
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),2);
			
			code = i.generateMachineCode(address);
			assertEquals(code[0],new Word("0x20010159"));
			assertEquals(code[1],new Word("0x02218022"));
			si1 = Instruction.decodeInstruction(code[0]);
			si2= Instruction.decodeInstruction(code[1]);
			si1.execute();
			si2.execute();
			assertEquals(rFile.getRegister(at).getValue(), 159);
			assertEquals(rFile.getRegister(s0).getHexValue(), "0xffffff24");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000008");
		} catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}
	public static void main(String[] args) {
		new PseudoTests();
	}
}

