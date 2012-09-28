package model;

public class Immediate
{
	private int value;
	public Immediate(){
		
	}
	public Immediate(int value) {
		this.value = value;
	}
	public int getValue(){
		return value;
	}
	
	public void setValue(int value){
		this.value = value;
	}
	
	public boolean is16bit(){
		return value < Math.pow(2, 16);
	}
	
	public boolean is32bit(){
		return value < Math.pow(2, 32);
	}
	
	public Immediate getHigh(){
		String s = Integer.toBinaryString(value);
		while(s.length() < 32)
			s = "0" + s;
		return new Immediate(Integer.parseInt(s.substring(0,16),2));
	}
	
	public Immediate getLow(){
		String s = Integer.toBinaryString(value);
		while(s.length() < 32)
			s = "0" + s;
		System.out.println(s);
		return new Immediate(Integer.parseInt(s.substring(16),2));
	}
	
	public boolean equals(Object o){
		if(o instanceof Immediate)
			return ((Immediate) o).value == this.value;
		return false;
	}
	
	public static void main(String[] args) {
		int z = 256;
		Immediate i = new Immediate(z);
		System.out.println(i.getLow().value);
		
		Long l = Long.decode("0x000064ab");
		System.out.println(l);
		System.out.println(l.toHexString(l));
		int x = l.intValue();
		
		Immediate m = new Immediate(x);
		Word w = new Word(0);
		w.setSubWord(x, 0, 15);
		int y = w.getSubWordSigned(0, 15);
		System.out.println(Integer.toHexString(y));
		System.out.println(w);
	}
}
