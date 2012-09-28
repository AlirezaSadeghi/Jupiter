package exception;

public class UnknownCommandException extends MipsCompileException
{
	String command;
	public UnknownCommandException(){
		command = null;
	}
	public UnknownCommandException(String command){
		this.command = command;
	}
	public String getMessage() {
		return "Unknown Command Exception ( " + this.command+ " ) Occured In Line : " ;
	}
	
}
