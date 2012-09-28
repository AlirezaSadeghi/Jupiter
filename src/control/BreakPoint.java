package control;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import exception.NoSuchRegisterException;

import model.Word;

public class BreakPoint
{
	public BreakPoint(Word address){
		this.address = address;
		condition = "";
	}
	
	public BreakPoint(Word address, String condition){
		this.address = address;
		if(condition != null)
			this.condition = condition.trim();
	}
	
	Word address;
	String condition;
	
	public Word getAddress() {
		return address;
	}
	public void setAddress(Word address) {
		this.address = address;
	}
	
	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		if(condition != null)
			this.condition = condition.trim();
	}

	public boolean equals(Object o){
		if(o instanceof BreakPoint)
			return ((BreakPoint)o).getAddress().equals(address);
		else if(o instanceof Word)
			return o.equals(address);
		return false;
	}
	
	private Integer findValue(String s){
		Integer val = null;
		if(s.matches("[$](?:[0-9a-z]{1,2})")){
			s = s.replaceFirst("[$]", "");
			try{
				val = Controller.getInstance().getRegisterFile().getRegisterUnsafe(s).getValue();
			}catch (NoSuchRegisterException e) {}
		}else if(s.matches("[-]?(?:(?:0x[0-9a-fA-F]+)|(?:[0-9]+))")){
			try{
				val = Integer.decode(s);
			}catch (NumberFormatException e) {}
		}
		return val;
	}
	public boolean conditionTrue(){
		if(condition == null || condition.length() == 0)
			return true;
		Matcher m = Pattern.compile("(.+)\\s*((?:=)|(?:!=))\\s*(.+)").matcher(condition);
		if(m.find()){
			String s = m.group(1);
			Integer val1 = findValue(s);
			if(val1 == null)
				return false;
			s = m.group(3);
			Integer val2 = findValue(s);
			if(val2 == null)
				return false;
			s = m.group(2);
			return (s.equals("=") && val1.intValue() == val2.intValue()) || (s.equals("!=") && val1.intValue() != val2.intValue());
		}
		return false;	
	}
}
