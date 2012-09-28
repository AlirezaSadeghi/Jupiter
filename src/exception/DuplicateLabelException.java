package exception;

public class DuplicateLabelException extends MipsCompileException
{
	private String labelName;
	
	public DuplicateLabelException(){
		super();
	}
	
	public DuplicateLabelException(String labelName){
		super();
		this.labelName = labelName;
	}

	public String getLabelName() {
		return labelName;
	}

	public void setLabelName(String labelName) {
		this.labelName = labelName;
	}
	
	public String getMessage() {
		return "Duplicate Label Exception ( " + this.getLabelName() + " ) Occured in Line : " ;
	}
}
