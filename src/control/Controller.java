package control;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Label;
import model.LabelManager;
import model.Memory;
import model.RegisterFile;
import model.Word;
import model.instruction.Instruction;
import model.instruction.SimpleInstruction;
import exception.BadInstructionCodeException;
import exception.DuplicateLabelException;
import exception.MipsCompileException;
import exception.MipsRuntimeException;
import exception.NoSuchLabelException;
import exception.OperandOutOfRangeException;
import exception.UnknownDirectiveException;
import exception.UnknownExpressionException;

public class Controller
{
	private static Controller instance;
	public static Controller getInstance(){
		if(instance == null){
			// NOOO
			instance = new Controller();
		}
		return instance;
	}
	
	public static void newInstance(){
		instance = new Controller();
	}
	
	public static Word codeStart;
	public static Word dataStart;
	static{
		codeStart = new Word("0x000000");
		dataStart = new Word("0x010000");
	}
	
	public Controller(){
		Controller.instance = this;
		registerFile = new RegisterFile();
		mainMemory = new Memory(512000000l);
		labelManager = new LabelManager();
		compileErrors = new ArrayList<CompileError>();
		codeAddress = new Word(codeStart);
		dataAddress = new Word(dataStart);
		breakPoints = new ArrayList<BreakPoint>();
		statistics = new Statistics();
		instructions = new ArrayList<Controller.InstructionKeeper>();
	}
	
	private RegisterFile registerFile;
	private Memory mainMemory;
	private LabelManager labelManager;
	private ArrayList<BreakPoint> breakPoints ;
	private ArrayList<CompileError> compileErrors;
	private ArrayList<InstructionKeeper> instructions;
	private Statistics statistics ;
	public Word dataAddress;
	public Word codeAddress;
	private boolean codeFlag = true;
	public boolean execute = true;
	
	public void executeAll( ) throws MipsRuntimeException{
		Word pc = registerFile.getRegister(RegisterFile.pc);
		while (execute && pc.getValue() >= 0  && !breakPoints.contains(pc))
			executeSimpleInstruction();
	}
	public boolean executeSimpleInstruction() throws MipsRuntimeException {
		Word pc = registerFile.getRegister(RegisterFile.pc);
		if(pc.getValue() > codeAddress.getValue())
			return false;
		Word machineCode = mainMemory.getWord(pc);
		SimpleInstruction simple;
		try {
			simple = Instruction.decodeInstruction(machineCode);
			statistics.staticIncrease(simple);
			simple.execute();
		}
		catch (BadInstructionCodeException e) {
			if(pc.getValue() < codeAddress.getValue())
				pc.setValue(pc.getValue() + 4 );
			throw e;
		}
		int index = breakPoints.indexOf(new BreakPoint(pc));
		if((index != -1 && breakPoints.get(index).conditionTrue()) || pc.getValue() > codeAddress.getValue()){
			return false;
		}
		return true;
	}
	
	private void findLabels(String[] lines){
		for (int i = 0; i < lines.length; i++) {
			if ( lines[i].contains(":") ){
				String[] temp = lines[i].split(" ");
				for (int j = 0; j < temp.length; j++) {
					if( temp[j].contains(":")){
						if (temp[j].charAt(0)== ':' ){
							if ( j != 0){
								try {
									labelManager.addLabel(new model.Label(temp[j-1].trim().toLowerCase()));
								} catch (DuplicateLabelException e) {
									compileErrors.add(new CompileError(e,i));
								}
							}
							else {
								UnknownExpressionException e = new UnknownExpressionException();
								compileErrors.add(new CompileError(e,i));
							}
						}
						else{
							String temp2[] = temp[j].split(":");
							try {
								labelManager.addLabel(new model.Label(temp2[0].trim().toLowerCase()));
							} catch (DuplicateLabelException e) {
								e.setLineNumber(i);
								compileErrors.add(new CompileError(e,i));
							}
						}
							
					}
				}
			}
		}
	}

	public CompileError[] compile(String[] lines){
		compileErrors.clear();
		instructions.clear();
		findLabels(lines);
		for (int i = 0; i < lines.length; i++) {
			try {
				compileLine(lines[i]);
			} catch (MipsCompileException e) {
				compileErrors.add(new CompileError(e, i));
			}
		}
		for (InstructionKeeper instruction : instructions) {
			Word adr = instruction.address;
			Word[] codes = instruction.instruction.generateMachineCode(adr);
			for (Word word : codes) {
				mainMemory.setWord(adr, word);
				adr.setValue(adr.getValue()+4);
			}
		}
		registerFile.getRegister(RegisterFile.pc).setValue(codeStart);
		return compileErrors.toArray(new CompileError[0]);
	}
	
	private void compileLine(String line) throws MipsCompileException{
		if(line == null)
			return;
		line = line.trim().toLowerCase();
		if(line.length() == 0)
			return;
		Matcher comment = Pattern.compile("[#]").matcher(line);
		Matcher label = Pattern.compile("^([a-z0-9]+)[:]").matcher(line);
//		Matcher dir = Pattern.compile("^[.]([a-z]*\\s*(?:(?:(?:0x)?[0-9]+(?:\\s*[,]\\s*(?:0x)?[0-9]+)*)|(?:[\"](.*)?[\"]))\\s*)$").matcher(line);
		Matcher dir = Pattern.compile("^[.]([a-z]*)").matcher(line);
		if(comment.find()){
			if(comment.start() == 0)
				return;
			line = line.substring(0,comment.start()-1);
			compileLine(line);
		}else if(label.find()){
			compileLabel(label.group(1));
			compileLine(line.substring(label.end(1)+1));
		}else if(dir.find()){
			line = compileDirective(line.substring(dir.start()+1));
			compileLine(line);
		}else{
			Instruction ins = Instruction.parseLine(line);
			instructions.add(new InstructionKeeper(ins, getCurrentAddress()));
			setCurrentAddress(getCurrentAddress().getValue() + ins.getSimpleInstructionCount()*4);
		}
	}
	private String compileDirective(String directive) throws MipsCompileException{
		directive = directive.trim();
		if(directive.contains("text")){
			codeFlag = true;
			directive = directive.replace("text", "");
		}else if(directive.contains("data")){
			codeFlag = false;
			directive = directive.replace("data", "");
		}else if(directive.contains("word")){
			Matcher m = Pattern.compile(Instruction.immediateRegex).matcher(directive);
			directive = directive.replace("word", "");
			while(m.find()){
				String s = m.group();
				Long l = Long.decode(m.group());
				if(l > Long.decode("0x00000000ffffffff"))
					throw new OperandOutOfRangeException();
				Word word = new Word(l.intValue());
				writeToMemory(word);
				directive = directive.replace(m.group(), "").trim();
				if(directive.length() > 1 && directive.charAt(0) == ',')
					directive = directive.substring(1);
			}
		}else if(directive.contains("byte")){
			Matcher m = Pattern.compile(Instruction.immediateRegex).matcher(directive);
			directive = directive.replace("byte", "");
			while(m.find()){
				String s = m.group();
				Integer l = Integer.decode(m.group());
				if(l > Integer.decode("0x000000ff"))
					throw new OperandOutOfRangeException();
				writeByteToMemory(l);
				directive = directive.replace(m.group(), "").trim();
				if(directive.length() > 1 && directive.charAt(0) == ',')
					directive = directive.substring(1);
			}
		}else if(directive.contains("ascii")){
			Matcher m = Pattern.compile("\"(.*)\"").matcher(directive);
			while(m.find()){
				String s = m.group(1);
				for (int i = 0; i < s.length(); i++) {
					writeByteToMemory((int)s.charAt(i));
				}
				directive = directive.replace(m.group(), "").trim();
				if(directive.length() > 1 && directive.charAt(0) == ',')
					directive = directive.substring(1);
			}
			if(directive.contains("asciiz")){
				writeByteToMemory((int)'\0');
				directive = directive.replace("asciiz", "");
			}else
				directive = directive.replace("ascii", "");
			
		}else
			throw new UnknownDirectiveException(directive);
//		if(directive.trim().length() != 0)
//			throw new UnknownExpressionException(directive);
		return directive;
	}
	
	private void compileLabel(String labelName) throws NoSuchLabelException{
		Label label = labelManager.getLabel(labelName);
		if(label == null)
			throw new NoSuchLabelException();
		label.setValue(getCurrentAddress().getValue());
	}
	
	private void writeToMemory(Word word){
		if(byteCount != 0){
			byteCount = 0;
			setCurrentAddress(getCurrentAddress().add(4).getValue());
		}
		mainMemory.setWord(getCurrentAddress(), word);
		setCurrentAddress(getCurrentAddress().getValue()+4);
	}
	
	int byteCount = 0;
	private void writeByteToMemory(int bt){
		mainMemory.getWord(getCurrentAddress());
		mainMemory.setByte(getCurrentAddress().add(byteCount), bt);
		byteCount++;
		if(byteCount == 4){
			byteCount = 0;
			setCurrentAddress(getCurrentAddress().getValue()+4);
		}
	}
	
	private Word getCurrentAddress(){
		return codeFlag ? codeAddress : dataAddress;
	}
	
	private void setCurrentAddress(int word){
		if(codeFlag)
			codeAddress.setValue(word);
		else
			dataAddress.setValue(word);
	}
	
	public boolean toggleBreakPoint(BreakPoint b){
		if(breakPoints.contains(b)){
			breakPoints.remove(b);
			return false;
		}
		else{
			breakPoints.add(b);
			return true;
		}
	}
	
	public Memory getMainMemory() {
		return mainMemory;
	}
	public void setMainMemory(Memory mainMemory) {
		this.mainMemory = mainMemory;
	}
	public RegisterFile getRegisterFile() {
		return registerFile;
	}
	public void setRegisterFile(RegisterFile registerFile) {
		this.registerFile = registerFile;
	}
	public LabelManager getLabelManager() {
		return labelManager;
	}
	public void setLabelManager(LabelManager labelManager) {
		this.labelManager = labelManager;
	}
	public Statistics getStat (){
		return statistics;
	}
	public BreakPoint getBreakPoint(Word address){
		int index = breakPoints.indexOf(new BreakPoint(address));
		if(index != -1)
			return breakPoints.get(index);
		return null;
	}
	
	private class InstructionKeeper{
		public Instruction instruction;
		public Word address;
		public InstructionKeeper(Instruction instruction, Word address){
			this.instruction = instruction;
			this.address = new Word(address);
		}
	}
}
