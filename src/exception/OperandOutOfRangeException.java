package exception;

public class OperandOutOfRangeException extends MipsCompileException
{
	String operand;
	public OperandOutOfRangeException(){
		super();
	}
	
	public OperandOutOfRangeException(String operand){
		this.operand = operand;
	}
	
	public String getMessage() {
		return "Operand Out Of Range Exception Occured In Line : ";
	}
}
