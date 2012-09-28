package exception;

public class MipsCompileException extends Exception
{
	private int lineNumber;
	public MipsCompileException(){
		lineNumber = 0;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
}
