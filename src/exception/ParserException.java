package exception;

public class ParserException extends MipsException
{
	public ParserException(){
		super();
	}
	public ParserException(String message) {
		super(message);
	}
	public ParserException(Throwable e){
		super(e);
	}
}
