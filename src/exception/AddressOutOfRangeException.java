package exception;

public class AddressOutOfRangeException extends MipsRuntimeException {
	
	public String getMessage() {
		return "Address Out Of Range Exception Occured In Line : ";
	}
	
}