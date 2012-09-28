package exception;

public class NoSuchLabelException extends MipsCompileException
{
	private String labelName;
	public NoSuchLabelException(){
		super();
	}
	
	public NoSuchLabelException(String labelName){
		this.labelName = labelName;
	}

	public String getLabelName(){
		return labelName;
	}

	public void setLabelName(String labelName){
		this.labelName = labelName;
	}
	
	public String getMessage() {
		return "No Such Label Exception ( " + this.getLabelName() + " ( Occured In Line : " ;
	}
}
