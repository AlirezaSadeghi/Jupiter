package exception;

import model.RegisterFile;

public class NoSuchRegisterException extends MipsCompileException {
	private String register;
	
	public NoSuchRegisterException(){
		super();
	}
	
	public NoSuchRegisterException(String register){
		this.register = "$"+register;
	}
	
	public String getRegister() {
		return register;
	}

	public void setRegister(String register) {
		this.register = register;
	}
	
	public String getMessage() {
		return "No Such Register Exception ( "+ this.getRegister() +" ) Occured In Line : ";
	}

}
