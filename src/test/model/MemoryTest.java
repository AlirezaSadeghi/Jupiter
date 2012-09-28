package test.model;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import model.Memory;
import model.Word;

import org.junit.BeforeClass;
import org.junit.Test;

import exception.AddressOutOfRangeException;
import exception.AlligmentRestrictionException;

public class MemoryTest
{
	static Memory mem;
	
	@BeforeClass
	public static void init(){
		mem = new Memory(256000000l);
	}	
	
	@Test
	public void test(){
		try {
			Word adr = new Word("0x00000000");
			Word w = mem.getWord(adr);
			assertEquals(w.getValue(), 0);
			adr = new Word("0x00c20a0");
			w = mem.getWord(adr);
			assertEquals(w.getValue(), 0);
			
			adr = new Word("0x00c20a0");
			w = new Word("0x00000001");
			mem.setWord(adr, w);
			w = mem.getWord(adr);
			assertEquals(w.getValue(), 1);
			
			adr = new Word();
			w = mem.getWord(adr);
			assertEquals(w.getValue(), 0);
			w = new Word("0x00000001");
			mem.setWord(adr,w);
			w = mem.getWord(adr);
			assertEquals(w.getValue(), 1);
			
			adr = new Word("0x00000001");
			w = mem.getWord(adr);
			fail();
		} catch (AddressOutOfRangeException e) {
		} catch (AlligmentRestrictionException e) {
		}
	}
	
	@Test
	public void endianTest(){
		try {
			Word adr = new Word("0x00000000");
			Word w = new Word("0x2ab30105");
			mem.setWord(adr, w);
			assertEquals(mem.getByte(adr), 5);
			adr  = new Word(adr.getValue() + 1);
			assertEquals(mem.getByte(adr), 1);
			adr  = new Word(adr.getValue() + 1);
			assertEquals(mem.getByte(adr), 179);
			adr  = new Word(adr.getValue() + 1);
			assertEquals(mem.getByte(adr), 42);
			adr = new Word("0xFFFFFFFF");
			mem.setByte(adr, 10);
			fail();
		} catch (AddressOutOfRangeException e) {
		}
	}
	
	public static void main(String[] args) {
		new MemoryTest();
	}
}
