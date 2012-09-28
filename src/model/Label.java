package model;

public class Label extends Immediate
{
	private String name;
	
	public Label(String name, int value){
		super(value);
		this.name = name;
	}
	
	public Label(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}
