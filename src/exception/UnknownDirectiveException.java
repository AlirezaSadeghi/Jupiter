package exception;

public class UnknownDirectiveException extends MipsCompileException
{
	private String directive;
	public UnknownDirectiveException(){
		
	}
	
	public UnknownDirectiveException(String directive){
		this.directive = directive;
	}

	public String getDirective() {
		return directive;
	}

	public void setDirective(String directive) {
		this.directive = directive;
	}
	public String getMessage() {
		return "Unknown Directive Exception ( " +this.getDirective()+ " )Occured In Line : ";
	}
}
