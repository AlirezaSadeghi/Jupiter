package exception;

public class AlligmentRestrictionException extends MipsRuntimeException {
	public String getMessage() {
		return "Allignment Restriction Violation Exception Occured in line : ";
	}
}