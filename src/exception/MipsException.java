package exception;

public class MipsException extends RuntimeException
{
	public MipsException(){
		super();
	}
	public MipsException(String message){
		super(message);
	}
	public MipsException(Throwable e){
		super(e);
	}
}
