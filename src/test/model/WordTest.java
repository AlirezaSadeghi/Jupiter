package test.model;

import static org.junit.Assert.assertEquals;

import javax.swing.JProgressBar;

import model.Word;

import org.junit.Test;

public class WordTest
{
	@Test
	public void testWord(){
		Word adr = new Word(0);
		adr.setValue("0x2ab30d45");
		assertEquals(adr.getBinaryValue(),"00101010101100110000110101000101");
		assertEquals(adr.getSubWord(6, 11),53);
		assertEquals(adr.getSubWord(6, 11),adr.getSubWord(6, 13));
		adr.setSubWord(4,6, 15);
		assertEquals(adr.getBinaryValue(),"00101010101100110000000100000101");
		assertEquals(adr.getSubWord(8, 11),1);
		assertEquals(adr.getHexValue().toLowerCase(),"0x2ab30105");
		adr.setSubWord(-1,6, 15);
		assertEquals(adr.getBinaryValue(),"00101010101100111111111111000101");
	}
	
	@Test
	public void testByte(){
		Word adr = new Word(0);
		adr.setValue("0x30be83af");
		assertEquals(adr.getByte(0), 175);
		assertEquals(adr.getByte(1), 131);
		assertEquals(adr.getByte(2), 190);
		assertEquals(adr.getByte(3), 48);
		assertEquals(adr.getHexValue(), "0x30be83af");
		adr.setByte(139, 2);
		adr.setByte(1, 0);
		assertEquals(adr.getHexValue(), "0x308b8301");
		adr.reverseBytes();
		assertEquals(adr.getHexValue(), "0x01838b30");
	}
	
	@Test
	public void testOperations(){
		// TODO operations return null!!!!
	}
	
	public static void main(String[] args) {
		new WordTest();
	}
}

