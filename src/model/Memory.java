package model;

import java.util.HashMap;

import javax.sound.sampled.ReverbType;

import exception.AddressOutOfRangeException;
import exception.AlligmentRestrictionException;

/**
 * @author Sina
 *
 */
public class Memory {
	long instructionNumbers = 0;
	HashMap<Word, Word> map ;
	
	/**
	 * @param memorySize number of Words in the memory
	 */
	public Memory(long memorySize){
		this.instructionNumbers = memorySize/4 ;
		map = new HashMap<Word, Word>();//(memorySize/4);
	}
	
	/**
	 * Gets the memory Word in the given Address 
	 * @param address
	 * @return
	 * @throws AddressOutOfRangeException
	 * @throws AlligmentRestrictionException 
	 */
	public Word getWord(Word address) throws AddressOutOfRangeException, AlligmentRestrictionException{
		if ( address.getValue() % 4 == 0)
			if ( 0 <= address.getValue() && (address.getValue()/4) < instructionNumbers){
				Word w = new Word(map.get(address)); 
				if ( w != null ){
					w.reverseBytes();
					return w;
				}
				else return new Word(0) ;
			}
			else 
				throw new AddressOutOfRangeException();
		else 
			throw new AlligmentRestrictionException();
	}
	
	/**
	 * Sets the word in the given address
	 * @param address
	 * @param word
	 * @throws AddressOutOfRangeException
	 * @throws AlligmentRestrictionException 
	 */
	public void setWord(Word address, Word word) throws AddressOutOfRangeException, AlligmentRestrictionException{
		if ( address.getValue() % 4 == 0)
			if ( 0 <= address.getValue() && (address.getValue()/4) < instructionNumbers){
				Word w = new Word(word);
				w.reverseBytes();
				map.put(new Word(address), w);
			}
			else 
				throw new AddressOutOfRangeException();
		else 
			throw new AlligmentRestrictionException();
	}
	
	public int getByte(Word address) throws AddressOutOfRangeException{
		if ( address.getValue()/4 < instructionNumbers && 0 <= address.getValue()){
			Word a = new Word((address.getValue()/4)*4);
			Word w = map.get(a);
			if(w == null)
				w = new Word(0);
			return w.getByte(3 - (int)(address.getValue()%4));
		}
		else
			throw new AddressOutOfRangeException();
	}
	
	public int getByteSigned(Word address) throws AddressOutOfRangeException{
		if ( address.getValue()/4 < instructionNumbers && 0 <= address.getValue()){
			Word a = new Word((address.getValue()/4)*4);
			Word w = map.get(a);
			if(w == null)
				w = new Word(0);
			return w.getByteSigned(3 - (int)(address.getValue()%4));
		}
		else
			throw new AddressOutOfRangeException();
	}
	
	public void setByte(Word address, int byt) throws AddressOutOfRangeException{
		if ( address.getValue() < instructionNumbers && 0 <= address.getValue() ){
			Word a = new Word((address.getValue()/4)*4);
			Word w = map.get(a);
			if(w == null){
				w = new Word(0);
				w.setByte(byt,(int)(3 - address.getValue()%4));
				map.put(a, w);
			}else{
				w.setByte(byt,(int)(3 - address.getValue()%4));
			}
		}
		else
			throw new AddressOutOfRangeException();		
	}
}