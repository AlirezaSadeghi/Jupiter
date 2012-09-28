package exception;

public class WrongArgumentException extends MipsCompileException
{
	String argument;
	public WrongArgumentException(String arg){
		this.argument = arg;
	}
	
	public String getMessage() {
		return "Wrong Arguments for Instruction Occured " + argument+ "  Line:";
	}
}
