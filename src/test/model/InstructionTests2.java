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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import control.Controller;

import exception.DuplicateLabelException;
import exception.MipsCompileException;
import exception.MipsRuntimeException;

public class InstructionTests2
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

	@Before
	public void before(){
		address.setValue(0);
		if(Controller.getInstance().getLabelManager().getLabel("hadi") == null)
			try {
				Controller.getInstance().getLabelManager().addLabel(new Label("hadi", Integer.decode("0x00000018")));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (DuplicateLabelException e) {
				e.printStackTrace();
			}
		if(Controller.getInstance().getLabelManager().getLabel("hadi2") == null)
			try {
				Controller.getInstance().getLabelManager().addLabel(new Label("hadi2", Integer.decode("0x00000018")));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (DuplicateLabelException e) {
				e.printStackTrace();
			}
	}
	
	@Test
	public void testRAnd(){
		rFile.getRegister(pc).setValue("0x00000000");
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
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
			
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
		} catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}

	@Test
	public void testRJr(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "jr $8";
			rFile.getRegister(8).setValue("0x00400000");
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			RFormatInstruction r = (RFormatInstruction)i;
			assertEquals(r.getRs().getRegisterNumber(), 8);
			assertEquals(r.getRt(), null); // ina ro havaset bashe
			assertEquals(r.getRd(), null);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x01000008"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(8).getHexValue(), "0x00400000");
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00400000");
			
			s = "jr $5";
			rFile.getRegister(5).setValue("0xF3464854");
			i = Instruction.parseLine(s);
			code = i.generateMachineCode(address)[0];
			si = Instruction.decodeInstruction(code);
			si.execute();
			fail(); // nabayad berese. akhe adresse manfi nadarim.
			
		} 
		catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}

	@Test
	public void testRNor(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "nor $s0, $s1, $s2";
			rFile.getRegister(s1).setValue(-123);
			rFile.getRegister(s2).setValue(125);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			RFormatInstruction r = (RFormatInstruction)i;
			assertEquals(r.getRs().getRegisterNumber(), 17);
			assertEquals(r.getRt().getRegisterNumber(), 18);
			assertEquals(r.getRd().getRegisterNumber(), 16);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x02328027"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(s0).getValue(), 2);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
		} 
		catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}

	@Test
	public void testISltiu(){
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
		//	System.out.println(rFile.getRegister(pc).getHexValue());
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
		//	System.out.println(ifi.getRt().getHexValue());
		} catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}
	
	
	@Test
	public void testIBeq(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			
			String s = "beq $a0, $a1, hadi";
			rFile.getRegister(a0).setValue(453);
			rFile.getRegister(a1).setValue(-200);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			IFormatInstruction ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs().getRegisterNumber(), 4);
			assertEquals(ifi.getRt().getRegisterNumber(), 5);	// rs o rt jab e jan tu brancha!
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x10850005"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			
			assertEquals(rFile.getRegister(a0).getValue(), 453);
			assertEquals(rFile.getRegister(a1).getValue(), -200);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
			address.setValue(address.getValue()+4);
			s = "beq $4, $5, hadi";
			rFile.getRegister(a0).setValue(-200);
			i = Instruction.parseLine(s);
			ifi = (IFormatInstruction)i;
			code = i.generateMachineCode(address)[0];
			si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000018");
			
			

			
			// CHECK TEST
			address.setValue(address.getValue()+4);
			s = "beq $4, $5, hadi2";
			i = Instruction.parseLine(s);
			ifi = (IFormatInstruction)i;
			code = i.generateMachineCode(address)[0];
			si = Instruction.decodeInstruction(code);
			si.execute();
//			fail();
		} catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
			fail();
		}
	}

	@Test
	public void testIBne(){
		rFile.getRegister(pc).setValue("0x00000000");
		try {
			String s = "bne $a0, $a1, hadi";
			rFile.getRegister(a0).setValue(-200);
			rFile.getRegister(a1).setValue(-200);
			Instruction i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			IFormatInstruction ifi = (IFormatInstruction)i;
			assertEquals(ifi.getRs().getRegisterNumber(), 4);
			assertEquals(ifi.getRt().getRegisterNumber(), 5);	// rs o rt jab e jan tu brancha!
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x14850005"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			
			
			assertEquals(rFile.getRegister(a0).getValue(), -200);
			assertEquals(rFile.getRegister(a1).getValue(), -200);
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000004");
			
			address.setValue(address.getValue()+4);
			s = "bne $4, $5, hadi";
			rFile.getRegister(a0).setValue(453);
			i = Instruction.parseLine(s);
			ifi = (IFormatInstruction)i;
			code = i.generateMachineCode(address)[0];
			si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000018");
			

			// CHECK TEST
			s = "beq $4, $5, hadi2";
			i = Instruction.parseLine(s);
			ifi = (IFormatInstruction)i;
			code = i.generateMachineCode(address)[0];
			si = Instruction.decodeInstruction(code);
			si.execute();
//			fail();									// negative address.
		} catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
	}
	
	@Test
	public void testJJ()
	{
		rFile.getRegister(pc).setValue("0x00000000");

		try {
			String s = "j hadi";
			Instruction i;
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x081000006"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000018");
			
			s = "j hadi2";
			i = Instruction.parseLine(s);
			code = i.generateMachineCode(address)[0];
			si = Instruction.decodeInstruction(code);
			si.execute();
			fail();
			
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
		
	}
	
	
	@Test
	public void testJJal()
	{
		rFile.getRegister(pc).setValue("0x00000000");
		
		try {
			String s = "j hadi";
			Instruction i;
			i = Instruction.parseLine(s);
			assertEquals(i.getSimpleInstructionCount(),1);
			Word code = i.generateMachineCode(address)[0];
			assertEquals(code,new Word("0x081000006"));
			SimpleInstruction si = Instruction.decodeInstruction(code);
			si.execute();
			assertEquals(rFile.getRegister(pc).getHexValue(), "0x00000018");
			assertEquals(rFile.getRegister(ra).getHexValue(), "0x00000004");
			
			s = "j hadi2";
			i = Instruction.parseLine(s);
			code = i.generateMachineCode(address)[0];
			si = Instruction.decodeInstruction(code);
			si.execute();
			fail();
			
			
		} catch (MipsCompileException e) {
			e.printStackTrace();
		} catch (MipsRuntimeException e) {
		}
		
	}
	
	
	
	public static void main(String[] args) {
		new InstructionTests2();
	}
}
