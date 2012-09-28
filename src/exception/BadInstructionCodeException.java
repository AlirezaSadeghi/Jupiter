package exception;

public class BadInstructionCodeException extends MipsRuntimeException
{
	public String getMessage() {
		return "Not an Instruction Code";
	}
}
