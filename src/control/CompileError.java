package control;

import exception.MipsCompileException;

public class CompileError
{
	private int lineNumber;
	private MipsCompileException exception;
	public CompileError(){
		this(null,0);
	}
	
	public CompileError(MipsCompileException exception , int lineNumber){
		this.exception = exception;
		this.lineNumber = lineNumber;
	}
	
	public String getMessage() {
		return null;
	}
	
	public int getLineNumber() {
		return lineNumber;
	}
	
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public MipsCompileException getException() {
		return exception;
	}

	public void setException(MipsCompileException exception) {
		this.exception = exception;
	}
	
	
	public String toString(){
		return exception.toString() + (this.getLineNumber() + 1) ;
	}
}