package test.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import model.RegisterFile;
import model.Word;
import model.instruction.Instruction;
import model.instruction.SimpleInstruction;
import model.instruction.simple.IFormatInstruction;
import model.instruction.simple.RFormatInstruction;

import org.junit.BeforeClass;
import org.junit.Test;

import control.Controller;
import exception.MipsCompileException;
import exception.MipsRuntimeException;

public class InstructionTest
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
	
	@Test
	public void testIAndi(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "andi $k0, $k1, 100";
			rFile.getRegister(k1).setValue("0x439046fe");
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			IFormatInstruction ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs().getRegisterNumber(), 27);
			assertEquals(ifi.getRt().getRegisterNumber(), 26);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x337a0064"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			
			assertEquals(rFile.getRegister(k1).getHexValue(), "0x439046fe");
			assertEquals(rFile.getRegister(k0).getValue(), 100);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	
	@Test
	public void testILb(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "lb $t1, 7($t0)";
			rFile.getRegister(t0).setValue("0x00010007");
			Controller.getInstance().getMainMemory().setByte(new Word("0x0001000e"), 128);
			// dar .date in neveshte she
			// hadi: .byte 0,1,2,3,4,5,6,128
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			IFormatInstruction ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs().getRegisterNumber(), 8);
			assertEquals(ifi.getRt().getRegisterNumber(), 9);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x81090007"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			
			assertEquals(rFile.getRegister(t1).getHexValue(), "0xffffff80");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	
	
	@Test
	public void testILbu(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "lbu $t1, 7($t0)";
			rFile.getRegister(t0).setValue("0x00010007");
			Controller.getInstance().getMainMemory().setByte(new Word("0x0001000e"), 128);
			// dar .date in neveshte she
			// hadi: .byte 0,1,2,3,4,5,6,128
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			IFormatInstruction ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs().getRegisterNumber(), 8);
			assertEquals(ifi.getRt().getRegisterNumber(), 9);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x91090007"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			
			assertEquals(rFile.getRegister(t1).getHexValue(), "0x00000080");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	
	@Test
	public void testILui(){
		rFile.getRegister(pc).setValue("0x000000FC");
		try {
			String s = "lui $1, 98";
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			IFormatInstruction ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs(),null);
			assertEquals(ifi.getRt().getRegisterNumber(), 1);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x3c010062"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(1).getHexValue(), "0x00620000");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000100");
			
			s = "lui $1, -7";
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			ifi = (IFormatInstruction)i;
			code = i.generateMachineCode(address)[0];
			si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(1).getHexValue(), "0xfff90000");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000104");
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	
	@Test
	public void testIOri(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "ori $6, $7, 16";
			rFile.getRegister(7).setValue("0xf9395024");
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			IFormatInstruction ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs().getRegisterNumber(), 7);
			assertEquals(ifi.getRt().getRegisterNumber(), 6);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x34e60010"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			
			assertEquals(rFile.getRegister(a2).getHexValue(), "0xf9395034");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	
	@Test
	public void testISb(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "sb $8, 7($2)";
			rFile.getRegister(8).setValue(-56);
			rFile.getRegister(2).setValue("0x00010000");
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			IFormatInstruction ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs().getRegisterNumber(), 2);
			assertEquals(ifi.getRt().getRegisterNumber(), 8);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0xa0480007"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			
			assertEquals(Controller.getInstance().getMainMemory().getByteSigned(new Word("0x00010007")), -56);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	
	@Test
	public void testISw(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "sw $8, 8($2)";
			rFile.getRegister(8).setValue(-4905);
			rFile.getRegister(2).setValue("0x10010000");
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			IFormatInstruction ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs().getRegisterNumber(), 2);
			assertEquals(ifi.getRt().getRegisterNumber(), 8);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0xac480008"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			
			assertEquals(Controller.getInstance().getMainMemory().getWord(new Word("0x10010008")), new Word(-4905));
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");

			s = "sw $7, 5($2)";
			i = Instruction.parseLine(s);
			ifi = (IFormatInstruction)i;
			code = i.generateMachineCode(address)[0];
			si = Instruction.decodeInstruction(code);
			si.execute();
			fail();
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
		}
	}
	
	@Test
	public void testIXori(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "xori $6, $7, 0x000064ab";
			rFile.getRegister(7).setValue("0x2340ac98");
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			IFormatInstruction ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs().getRegisterNumber(), 7);
			assertEquals(ifi.getRt().getRegisterNumber(), 6);
			assertEquals(ifi.getImmediate().getValue(), 25771);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x38e664ab"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			
			assertEquals(rFile.getRegister(6).getHexValue(), "0x2340c833");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	
	@Test
	public void testRAdd(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "add $s0, $s1, $s2";
			rFile.getRegister(s1).setValue(45);
			rFile.getRegister(s2).setValue(-90);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			RFormatInstruction r = (RFormatInstruction)i;
			assertEquals(r.getRs().getRegisterNumber(), 17);
			assertEquals(r.getRt().getRegisterNumber(), 18);
			assertEquals(r.getRd().getRegisterNumber(), 16);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x02328020"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(s1).getValue(), 45);
			assertEquals(rFile.getRegister(s2).getValue(), -90);
			assertEquals(rFile.getRegister(s0).getValue(), -45);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
			
			s = "add $a0, $t1, $v1";
			rFile.getRegister(v1).setValue("0x07ffffffe");
			rFile.getRegister(t1).setValue("0x05235443f");
			i = Instruction.parseLine(s);
//			assertEquals(i.getSimpleInstructionCount(),1);
			r = (RFormatInstruction)i;
			assertEquals(r.getRd().getRegisterNumber(), 4);
			assertEquals(r.getRs().getRegisterNumber(), 9);
			code = i.generateMachineCode(address)[0];
		//	assertEquals(code,new Word("0x21247fff"));
			si = Instruction.decodeInstruction(code);
			si.execute();  // overflow
			assertEquals(rFile.getRegister(t3).getValue(), 0);
			System.out.println(r.getRs().getHexValue());
			fail();
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
		}
	}
	
	@Test
	public void testRSllv(){
		rFile.getRegister(pc).setValue("0x000000FC");
		try {
			String s = "Sllv $s0, $s1, $s2";
			rFile.getRegister(s1).setValue("0x00065E00");
			rFile.getRegister(s2).setValue(4);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			RFormatInstruction r = (RFormatInstruction)i;
			assertEquals(r.getRs().getRegisterNumber(), 18);
			assertEquals(r.getRt().getRegisterNumber(), 17);
			assertEquals(r.getRd().getRegisterNumber(), 16);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x02518004"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(s1).getValue(), 417280);
			assertEquals(rFile.getRegister(s0).getValue(), 6676480);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000100");
		} 
		catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	
	
	@Test
	public void testRSub(){
		rFile.getRegister(pc).setValue("0x000000FC");
		try {
			String s = "sub $s0, $s1, $s2";
			rFile.getRegister(s1).setValue(9899);
			rFile.getRegister(s2).setValue(99);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			RFormatInstruction r = (RFormatInstruction)i;
			assertEquals(r.getRs().getRegisterNumber(), 17);
			assertEquals(r.getRt().getRegisterNumber(), 18);
			assertEquals(r.getRd().getRegisterNumber(), 16);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x02328022"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(s1).getValue(), 9899);
			assertEquals(rFile.getRegister(s0).getValue(), 9800);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000100");
			
			
			s = "sub $a0, $t1, $v1";
			rFile.getRegister(t1).setValue("0xf00000000");
			rFile.getRegister(v1).setValue("0x7fffffffe");
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			r = (RFormatInstruction)i;
			assertEquals(r.getRd().getRegisterNumber(), 4);
			assertEquals(r.getRs().getRegisterNumber(), 9);
			assertEquals(r.getRt().getRegisterNumber(), 3);
			code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x01232022"));
			si = Instruction.decodeInstruction(code);
			si.execute();  // overflow
			fail("overflow");
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
		}
	}
	
	@Test
	public void testISlti(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "slti $k0, $k1, -5";
			rFile.getRegister(k1).setValue(-453);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			IFormatInstruction ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs().getRegisterNumber(), 27);
			assertEquals(ifi.getRt().getRegisterNumber(), 26);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x2b7afffb"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			
			assertEquals(rFile.getRegister(k1).getValue(), -453);
			assertEquals(rFile.getRegister(k0).getValue(), 1);
			System.out.println(rFile.getRegister(pc).getHexValue());
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
			s = "slti $t3, $t4, -5";
			rFile.getRegister(t4).setValue("0x0000000F");
			i = Instruction.parseLine(s);
			ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs().getRegisterNumber(), 12);
			assertEquals(ifi.getRt().getRegisterNumber(), 11);
			code = i.generateMachineCode(address)[0];
			si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(t3).getValue(), 0);
			System.out.println(ifi.getRt().getHexValue());
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	

	@Test
	public void testRSll(){
		rFile.getRegister(pc).setValue("0x000000FC");
		try {
			String s = "Sll $s0, $s1, 4";
			rFile.getRegister(s1).setValue("0x00065E00");
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			RFormatInstruction r = (RFormatInstruction)i;
			assertEquals(r.getRs(), null);
			assertEquals(r.getRt().getRegisterNumber(), 17);
			assertEquals(r.getRd().getRegisterNumber(), 16);
			assertEquals(r.getShiftAmount(), 4);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x00118100"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(s1).getValue(), 417280);
			assertEquals(rFile.getRegister(s0).getValue(), 6676480);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000100");
			
			
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	
	@Test
	public void testRSlt(){
		rFile.getRegister(pc).setValue("0x000000FC");
		try {
			String s = "Slt $s0, $s1, $s2";
			rFile.getRegister(s1).setValue(890);
			rFile.getRegister(s2).setValue(666);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			RFormatInstruction r = (RFormatInstruction)i;
			assertEquals(r.getRs().getRegisterNumber(), 17);
			assertEquals(r.getRt().getRegisterNumber(), 18);
			assertEquals(r.getRd().getRegisterNumber(), 16);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x0232802a"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(s0).getValue(), 0);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000100");
			
			
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	
	@Test
	public void testRSrav(){
		rFile.getRegister(pc).setValue("0x000000FC");
		try {
			String s = "Sllv $s0, $s1, $s2";
			rFile.getRegister(s1).setValue("0x00065E00");
			rFile.getRegister(s2).setValue(4);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			RFormatInstruction r = (RFormatInstruction)i;
			assertEquals(r.getRs().getRegisterNumber(), 18);
			assertEquals(r.getRt().getRegisterNumber(), 17);
			assertEquals(r.getRd().getRegisterNumber(), 16);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x02518004"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(s1).getValue(), 417280);
			assertEquals(rFile.getRegister(s0).getValue(), 6676480);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000100");
			
			
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
			fail();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}
	public static void main(String[] args) {
		new InstructionTest();
	}
}