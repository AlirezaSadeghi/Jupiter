package exception;

public class UnknownExpressionException extends MipsCompileException
{
	private String expression;
	public UnknownExpressionException(){
		expression = null;
	}
	public UnknownExpressionException(String expression){
		this.expression = expression;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getMessage() {
		return "Unknown Expression Exception ( " +this.getExpression()+ " ) Occured In Line : " ;
	}
}
