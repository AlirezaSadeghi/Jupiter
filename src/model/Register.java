package model;

/**
 * A 32 bit register
 * @author Hadi
 *
 */
public class Register extends Word{
	int registerNumber;
	Register(int registerNumber){
		this(registerNumber,0);
	}
	Register(int registerNumber, Word word){
		super(word);
	}
	Register(int registerNumber, int value){
		super(value);
		this.registerNumber = registerNumber;
	}
	
	public void setValue(int value){
		if(registerNumber != 0)
			super.setValue(value);
	}

	public void setValue(Word word){
		if(registerNumber != 0)
			super.setValue(word);
	}
	
	public void setValue(String value){
		if(registerNumber != 0)
			super.setValue(value);
	}
	
	public int getRegisterNumber() {
		return registerNumber;
	}
	public void setRegisterNumber(int registerNumber) {
		this.registerNumber = registerNumber;
	}
}
