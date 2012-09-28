package model;

import java.lang.Character.Subset;

/**
 * A 32 bit word
 * @author hadi
 *
 */
public class Word {
	private int value;
	
	/**
	 * Defualt Constructor, sets the value of the word to 0 by defualt
	 */
	public Word(){
		value = 0;
	}
	
	/**
	 * Creates a word based on an existing word
	 * @param word
	 */
	public Word(Word word){
		if(word != null)
			this.value = word.value;
		else
			this.value = 0;
	}
	
	/**
	 * Creates a word with four given bytes in big endian format
	 * @param bytes
	 */
	public Word(Byte[] bytes){
		
	}
	
	/**
	 * Creates a word with a given decimal number
	 * @param value
	 */
	public Word(int value){
		this.value = value;
	}
	
	public Word(String value){
		this.value = Long.decode(value).intValue();
	}
	
	/**
	 * @param word
	 * @return
	 */
	public Word add(Word word) // bayad exeption throw kone{
	{
		return new Word(this.value + word.getValue());
	}
	public Word add(int n)
	{
		return new Word(this.value + n);
	}
	
	public Word sub(Word word){
		this.value = value - word.value;
		return null;
	}
	public Word sub(int n){
		this.value = value - n;
		return null;
	}
	
	public Word[] mul(Word word)
	{
		long l = value * word.value;
		long m = Long.decode("0x0000000011111111");
		long p = Long.decode("0x1111111100000000");
		Word[] w = new Word[2];	// word[0] as lo register, word[1] as hi
		//w[0].setValue(l & m);
		//w[1].setValue(l & p);  // ESHTEP
		return null;
	}
	
	public Word[] div(Word word)
	{
		Word[] w = new Word[2];	// word[0] as lo register, word[1] as hi
		w[0].setValue(this.value / word.getValue());
		w[1].setValue(this.value % word.getValue());
		return null;
	}
	
	/**
	 * Set value to the given integer
	 * @param value
	 */
	public void setValue(int value){
		this.value = value;
	}
	
	/**
	 * Set value to the given word's value
	 * @param word
	 */
	public void setValue(Word word){
		this.value = word.getValue();
	}
	
	public void setValue(String value){
		this.value = Long.decode(value).intValue();
	}
	
	/**
	 * Gets the number represented by a number of bits in the word
	 * Least Significant bit is 0, Most Significant bit is 31 
	 * @param startBit the start of the subword
	 * @param endBit the end of the subword
	 * @return the decimal representation of the subword
	 */
	
	/**
	 * fill the beginning of given String with 0 of 1 base on sign of the number.
	 */
	private String fixLength (String binary,int constantLength)
	{
		String binaryNum = binary;
		if(binaryNum.length() > constantLength)
			return binaryNum.substring(binaryNum.length() - constantLength);
		int leak = constantLength - binaryNum.length();			// length of s must be 32"
		if (leak > 0)
		{
			String leakness = "";
			for (int i = 0; i < leak; i++) 
				leakness += "0";
			binaryNum = leakness.concat(binaryNum);
		}
		return binaryNum;
	}
	
	private String fixLengthSigned (String binary,int constantLength)
	{
		String binaryNum = binary;
		char c = binaryNum.charAt(0);
		if(binaryNum.length() > constantLength)
			return binaryNum.substring(binaryNum.length() - constantLength);
		int leak = constantLength - binaryNum.length();			// length of s must be 32"
		if (leak > 0)
		{
			String leakness = "";
			if(c == '0'){
				for (int i = 0; i < leak; i++) 
					leakness += "0";
			}else{
				for (int i = 0; i < leak; i++) 
					leakness += "1";
			}
			binaryNum = leakness.concat(binaryNum);
		}
		return binaryNum;
	}
	
	public int getSubWord(int startBit, int endBit){	// badan daghigh ba hadi check kon ke systeme joda
		String s = Long.toBinaryString(this.value);
		s = fixLength(s, 32);
		s = s.substring(s.length() - endBit - 1 , s.length() - startBit );
		int n = Integer.parseInt(s, 2);
		return n;
	}
	
	public int getSubWordSigned(int startBit, int endBit){
		String s = Long.toBinaryString(this.value);
		s = fixLength(s, 32);
		s = s.substring(s.length() - endBit - 1 , s.length() - startBit );
		s = fixLengthSigned(s, 63);
		int n = (int)Long.parseLong(s, 2);
		return n;
	}
	/**
	 * Sets the number represented by a number of bits in the word
	 * Least Significant bit is 0, Most Significant bit is 31
	 * @param value the value to put in the subword 
	 * @param startBit the start of the subword
	 * @param endBit the end of the subword
	 * @return the decimal representation of the subword
	 */
	public void setSubWord(int value, int startBit, int endBit){
		String s = Long.toBinaryString(this.value);
		s = fixLength(s, 32);
		String s2 = Long.toBinaryString(value);
		s2 = fixLength(s2, endBit - startBit + 1);
		s = s.substring(0, s.length() - endBit - 1) + s2 + s.substring(s.length() - startBit);
		this.value = new Long(Long.parseLong(s, 2)).intValue();
//		if(s.charAt(0) == '1')
//			s = s.substring(1);
//		this.value = Integer.parseInt(s,2);
	}
	
	/**
	 * @param n the index of the byte to return
	 * 0 base dar nazar gereftam. yani byte 0 darim ta 3.
	 * @return
	 */
	public int getByte(int n){		
		String s = fixLength(Long.toBinaryString(this.value), 32);
		int[] parts = {0, 8, 16, 24};
		return getSubWord(parts[n], parts[n] + 7);
	}
	
	public int getByteSigned(int n){		
		String s = fixLength(Long.toBinaryString(this.value), 32);
		int[] parts = {0, 8, 16, 24};
		return getSubWordSigned(parts[n], parts[n] + 7);
	}
	/**
	 * 
	 * @param value value of byte to be placed.
	 * @param the index of the byte to set.
	 */
	public void setByte(int value, int n)
	{
		int[] parts = {0, 8, 16, 24};
		setSubWord(value, parts[n], parts[n] + 7);
	}
	
	/**
	 * reverses the bytes of a word and return it on a new word.
	 * @return byte_reversed word.
	 */
	
	public void reverseBytes()
	{
		Word w = new Word(this);
		int a = w.getByte(0);
		w.setByte(w.getByte(3), 0);
		w.setByte(a, 3);
		a = getByte(1);
		w.setByte(w.getByte(2), 1);
		w.setByte(a, 2);
		this.setValue(w);
	}
	
	/**
	 * @return the value of the byte as a decimal int
	 */
	public int getValue(){
		return value;
	}
	
	public long getUnsignedValue(){
		if( value >= 0)
			return (long)value;
		else{
			long temp = 1;
			temp = temp << 32;
			return temp + value;
		}
	}
	
	/**
	 * @return value of the byte in hexadecimal
	 */
	public String getHexValue(){
		return "0x"+fixLength(Long.toHexString(value),8);
	}
	
	/**
	 * @return value of the byte in binary
	 */
	public String getBinaryValue(){
		return  fixLength(Long.toBinaryString(value),32);
	}
	
	public String toString(){
		return getHexValue();
	}
	
	public boolean equals(Object o){
		if (o instanceof Word)
		{
			return value == ((Word) o).getValue();
		}
		return false;
	}
	
	public int hashCode() {
		return (new Long(value)).hashCode();
	}
}