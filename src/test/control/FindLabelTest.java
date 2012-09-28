package test.control;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import control.CompileError;
import control.Controller;
import exception.DuplicateLabelException;
import exception.MipsCompileException;
import exception.UnknownExpressionException;

public class FindLabelTest {

	@BeforeClass
	public static void init(){
		con = Controller.getInstance();
	}	
	static Controller con;
	@Test
	public void test(){
		String[] temp = {
			"L1: ",
			"add asdf",
			"L2 : add $t1,$t2,$t3  L3 : asdf", 
			": asldfj a L5 :" ,
			"L1 : " } ;
		CompileError[] errors = con.compile(temp);
		assertEquals(errors.length, 2);
		assertEquals(con.getLabelManager().getLabelCount(), 4);
		assertNotNull(con.getLabelManager().getLabel("L1"));
		assertNotNull(con.getLabelManager().getLabel("L2"));
		assertNotNull(con.getLabelManager().getLabel("L1"));
		assertNull(con.getLabelManager().getLabel("L4"));
		assertNotNull(con.getLabelManager().getLabel("L5"));
		assertEquals(errors[0].getLineNumber(), 3);
		assertEquals(errors[0].getClass(), UnknownExpressionException.class);
		assertEquals(errors[1].getLineNumber(), 4);
		assertEquals(errors[1].getClass(), DuplicateLabelException.class);
	}
	
}
