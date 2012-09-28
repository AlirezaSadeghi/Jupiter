package model;

import control.Controller;
import exception.NoSuchRegisterException;

/**
 * Register file containing 32 registers
 * 
 * @author Hadi
 * 
 */
public class RegisterFile {
	private Register[] registers;
	
	public RegisterFile() {
		registers = new Register[35];
		for (int i = 0; i < registers.length; i++) 
			registers[i] = new Register(i,0);
	}

	public final static int zero = 0, at = 1,  v0 = 2,  v1 = 3,  a0 = 4,  a1 = 5,  a2 = 6,
					 a3 = 7,   t0 = 8,  t1 = 9,  t2 = 10, t3 = 11, t4 = 12, t5 = 13,
					 t6 = 14,  t7 = 15, s0 = 16, s1 = 17, s2 = 18, s3 = 19, s4 = 20,
					 s5 = 21,  s6 = 22, s7 = 23, t8 = 24, t9 = 25, k0 = 26, k1 = 27,
					 gp = 28,  sp = 29, fp = 30, ra = 31, pc = 32, lo = 33, hi = 34;
	public final static String[] registerNames = {"zero", "at", "v0", "v1", "a0", "a1", "a2", "a3", "t0", "t1", "t2", 
			"t3", "t4", "t5", "t6", "t7", "s0", "s1", "s2", "s3", "s4", "s5", "s6", "s7", 
			"t8", "t9", "k0", "k1", "gp", "sp", "fp", "ra", "pc", "lo", "hi"};
	public final static int registerCount = 35;
	/**
	 * @param registerNumber the number of the register to return
	 * @return returns the register with the given number, returns null if number is out of range
	 */
	public Register getRegister(int registerNumber){
		if(registerNumber > registers.length || registerNumber < 0)
			return null;
		return registers[registerNumber];
	}

	
	/**
	 * Returns a register based on its name
	 * @param sReg a string with the number or name of a register
	 * @return returns the Register with the given name or number on String format
	 * @throws NoSuchRegisterException throws exception if such a register doesn't exist
	 */
	public Register getRegister(String sReg) throws NoSuchRegisterException {
		if (Character.isDigit(sReg.charAt(0))){
			try{
				Register r =  getRegister(Integer.parseInt(sReg));
				if(r == null)
					throw new NoSuchRegisterException(sReg);
				return r;
			}catch(NumberFormatException e){
				throw new NoSuchRegisterException(sReg);
			}
		}
		else
		{
			
			for (int i = 0; i < 32; i++)  // JUST UP TO 32, no pc hi lo access
				if (sReg.equals(registerNames[i]))
					return registers[i];
			
		}
		throw new NoSuchRegisterException(sReg);
	}
	
	public Register getRegisterUnsafe(String sReg) throws NoSuchRegisterException {
		if (Character.isDigit(sReg.charAt(0))){
			try{
				Register r =  getRegister(Integer.parseInt(sReg));
				if(r == null)
					throw new NoSuchRegisterException(sReg);
				return r;
			}catch(NumberFormatException e){
				throw new NoSuchRegisterException(sReg);
			}
		}
		else
		{
			
			for (int i = 0; i < registerCount; i++)  // JUST UP TO 32, no pc hi lo access
				if (sReg.equals(registerNames[i]))
					return registers[i];
			
		}
		throw new NoSuchRegisterException(sReg);
	}
	
	public void resetRegisters(){
		for (int i = 0; i < 32; i++) {
			registers[i].setValue(0);
		}
		registers[pc].setValue(Controller.codeStart);
	}
}