package model.instruction;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import model.Immediate;
import model.Register;
import model.Word;
import model.instruction.simple.IFormatInstruction;
import model.instruction.simple.JFormatInstruction;
import model.instruction.simple.RFormatInstruction;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import control.Controller;
import exception.BadInstructionCodeException;
import exception.MipsCompileException;
import exception.ParserException;
import exception.UnknownCommandException;
import exception.UnknownExpressionException;
import exception.WrongArgumentException;

/**
 * 
 * @author hadi
 * 
 */
public abstract class Instruction
{
	public abstract int getSimpleInstructionCount();

	public abstract Word[] generateMachineCode(Word address);

	// ############ STATIC ############# 
	
	public static String registerRegex ="[$]([0-9a-z]{1,2})";
//	public static String immediateRegex ="([-]?(?:(?:0x[0-9a-fA-F]+)|(?:[0-9]+))?)";
	public static String immediateRegex ="([-]?(?:(?:0x[0-9a-fA-F]+)|(?:[0-9]+)))";
	public static String labelRegex ="([a-z0-9]*)";
	private static Class<SimpleInstruction>[] instructions;
	private static Class<RFormatInstruction>[] rInstructions;
	private static Class<IFormatInstruction>[] iInstructions;
	private static Class<JFormatInstruction>[] jInstructions;
	private static Class<PseudoInstruction>[] pInstructions;
	private static HashMap<String, Class<Instruction>> instructionMakerHash;
	private static HashMap<Integer, Class<SimpleInstruction>> instructionOpCodeHash; 
	private static HashMap<Integer, Class<SimpleInstruction>> rInstructionFuncHash ;
	private static Vector<String> keyWords;

	public static void main(String[] args) {
		instructionMakerHash = new HashMap<String , Class<Instruction>>();
		try {
			init();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static{
		try {
			init();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void init() throws IOException, InstantiationException, IllegalAccessException {
		keyWords = new Vector<String>();
		parseXML();
		instructionOpCodeHash = new HashMap<Integer, Class<SimpleInstruction>>() ;
		rInstructionFuncHash = new HashMap<Integer, Class<SimpleInstruction>>() ;
		SimpleInstruction x ;
		for (int i = 0; i < instructions.length; i++)
		{
			x = instructions[i].newInstance() ;
			instructionOpCodeHash.put(x.getOpCode(),instructions[i] ) ;
			if (x.getOpCode() == 0)
				rInstructionFuncHash.put(((RFormatInstruction)x).getFunc(), instructions[i]) ;
		}
		
	}

	private static void parseXML() throws IOException {
		ArrayList<Class<SimpleInstruction>> instructions = new ArrayList<Class<SimpleInstruction>>();
		ArrayList<Class<RFormatInstruction>> rInstructions = new ArrayList<Class<RFormatInstruction>>();
		ArrayList<Class<JFormatInstruction>> jInstructions = new ArrayList<Class<JFormatInstruction>>();
		ArrayList<Class<IFormatInstruction>> iInstructions = new ArrayList<Class<IFormatInstruction>>();
		ArrayList<Class<PseudoInstruction>> pInstructions = new ArrayList<Class<PseudoInstruction>>();

		instructionMakerHash = new HashMap<String, Class<Instruction>>();
		
		File file = new File("Data/instructions.xml");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList nodeLst = doc.getElementsByTagName("ins");
			for (int i = 0; i < nodeLst.getLength(); i++) {
				Element elem = (Element) nodeLst.item(i);
				
				Node n = elem.getElementsByTagName("name").item(0).getChildNodes().item(0);
				if(n.getNodeValue() == null)
					continue;
				String name = n.getNodeValue().toLowerCase().trim();
				
				Class<Instruction> makerClass = null;
				
				NodeList nlst = elem.getElementsByTagName("class");
				for(int j = 0; j < nlst.getLength(); j++){
					Element el = (Element)nlst.item(j);
					n = el.getElementsByTagName("name").item(0).getChildNodes().item(0);
					if(n.getNodeValue() == null)
						continue;
					String className = n.getNodeValue().trim();
					
					n = el.getElementsByTagName("type").item(0).getChildNodes().item(0);
					if(n.getNodeValue() == null)
						continue;
					char type = n.getNodeValue().toLowerCase().trim().charAt(0);
					
					Class<Instruction> clazz = (Class<Instruction>) Class.forName("model.instruction.simple." + className);
					
					switch (type) {
					case 'r':
						rInstructions.add((Class<RFormatInstruction>) clazz.asSubclass(RFormatInstruction.class));
						RFormatInstruction.instructionNameHash.put(name, clazz);
						break;
					case 'i':
						iInstructions.add((Class<IFormatInstruction>) clazz.asSubclass(IFormatInstruction.class));
						IFormatInstruction.instructionNameHash.put(name, clazz);
						break;
					case 'j':
						jInstructions.add((Class<JFormatInstruction>) clazz.asSubclass(JFormatInstruction.class));
						JFormatInstruction.instructionNameHash.put(name, clazz);
						break;
					case 'p':
						pInstructions.add((Class<PseudoInstruction>) clazz.asSubclass(PseudoInstruction.class));
						PseudoInstruction.instructionNameHash.put(name, clazz);
						break;
					default:
						throw new Exception(); // FIX
					}
					if(type != 'p')
						instructions.add((Class<SimpleInstruction>) clazz.asSubclass(SimpleInstruction.class));
					makerClass = clazz;
				}
				
				n = elem.getElementsByTagName("maker").item(0);
				String maker = null;
				boolean key = true;
				if(n!=null){
					n = n.getChildNodes().item(0);
					if(n.getNodeValue() != null)
						maker = n.getNodeValue().trim();
					if(maker.equals("null")){
						maker = null;
						key = false;
					}
				}
				if(maker != null)
					makerClass = (Class<Instruction>) Class.forName("model.instruction.simple." + maker);
				instructionMakerHash.put(name, makerClass);
				if(key)
					keyWords.add(name);
			}
			Instruction.instructions = instructions.toArray(new Class[0]);
			Instruction.rInstructions = instructions.toArray(new Class[0]);
			Instruction.iInstructions = instructions.toArray(new Class[0]);
			Instruction.jInstructions = instructions.toArray(new Class[0]);
			Instruction.pInstructions = instructions.toArray(new Class[0]);
		} catch (Exception e) {
			// TODO handle exceptions
			e.printStackTrace();
			throw new IOException("Couldn't parse xml file");
		}
	}

		/**
	 * Decodes a word into a simple instruction
	 * 
	 * @param instruction
	 * @return the instruction represented by the word
		 * @throws IllegalAccessException 
		 * @throws InstantiationException 
	 */
	public static SimpleInstruction decodeInstruction(Word instruction) throws BadInstructionCodeException{
		try {
			if (instruction.getSubWord(26, 31) == 0 )
			{
				if (!rInstructionFuncHash.containsKey(instruction.getSubWord(0, 5)))
					throw (new BadInstructionCodeException());
				else{
					Constructor rFormatConstructor = rInstructionFuncHash.get(instruction.getSubWord(0, 5)).getConstructor(Register.class,Register.class,Register.class,int.class);
					Register rs = Controller.getInstance().getRegisterFile().getRegister(instruction.getSubWord(21, 25)) ;
					Register rt = Controller.getInstance().getRegisterFile().getRegister(instruction.getSubWord(16, 20)) ;
					Register rd = Controller.getInstance().getRegisterFile().getRegister(instruction.getSubWord(11, 15)) ;
					int shiftAmount = instruction.getSubWord(6, 10);
					return (SimpleInstruction)rFormatConstructor.newInstance(rs, rt, rd, shiftAmount) ;
				}
			}
			else 
			{
				if (!instructionOpCodeHash.containsKey(instruction.getSubWord(26, 31))) 
					throw(new BadInstructionCodeException()) ;
				else 
				{
					if ( IFormatInstruction.class.isAssignableFrom(instructionOpCodeHash.get(instruction.getSubWord(26, 31))))
					{
						Constructor iFormatConstructor = instructionOpCodeHash.get(instruction.getSubWord(26, 31)).getConstructor(Register.class, Register.class, Immediate.class);
						Register rs = Controller.getInstance().getRegisterFile().getRegister(instruction.getSubWord(21, 25)); 
						Register rt = Controller.getInstance().getRegisterFile().getRegister(instruction.getSubWord(16, 20));
						Immediate im = new Immediate(instruction.getSubWordSigned(0, 15)) ;
						return (SimpleInstruction) iFormatConstructor.newInstance(rs,rt ,im);
					}
					else if (JFormatInstruction.class.isAssignableFrom(instructionOpCodeHash.get(instruction.getSubWord(26, 31))))
							{
						Constructor jFormatConstructor = instructionOpCodeHash.get(instruction.getSubWord(26, 31)).getConstructor(Immediate.class);
						Immediate im = new Immediate (instruction.getSubWord(0, 25));
						return (SimpleInstruction) jFormatConstructor.newInstance(im);
					}
				}
			}
		} catch (InstantiationException | IllegalAccessException | NoSuchMethodException | SecurityException | IllegalArgumentException| InvocationTargetException e)  {
			throw(new BadInstructionCodeException()) ;
		}
		return null ;
	}


	/**
	 * Parses a line and creates an Instruction
	 * 
	 * @param line
	 * @return an Instruction, either a simple one or pseudo
	 */ 
	public static Instruction parseLine(String line) throws MipsCompileException {
		line = line.toLowerCase().trim();
		String name = null;
		Matcher m = Pattern.compile("[a-z]*").matcher(line);

		if (m.find()) {
			if (m.start() != 0)
				throw new UnknownExpressionException(line.substring(0,m.start()));
			name = m.group();
			line = line.substring(m.end()).trim();
		} else
			throw new UnknownExpressionException(line);
		
		Class<Instruction> maker = instructionMakerHash.get(name);
		if(maker == null)
			throw new UnknownCommandException(name);

		try {
			Method parser = maker.getMethod("parseLine", String.class, String.class);
			return (Instruction)parser.invoke(null, name, line);
		} catch (SecurityException |NoSuchMethodException | IllegalAccessException | IllegalArgumentException e) {
			System.out.println(maker);
			e.printStackTrace();
			ParserException ex = new ParserException(e);
			throw ex;
		} catch(InvocationTargetException e){
			if(e.getTargetException() instanceof MipsCompileException)
				throw (MipsCompileException)e.getTargetException();
			else if(e.getTargetException() instanceof NumberFormatException)
				throw new WrongArgumentException(name + " " + line);
			else{
				ParserException ex = new ParserException(e);
				throw ex;
			}
		}
	}
	
	public static Class<SimpleInstruction>[] getAllInstructions() {
		return instructions;
	}

	public static Class<RFormatInstruction>[] getRFormatInstructions() {
		return rInstructions;
	}

	public static Class<JFormatInstruction>[] getJFormatInstructions() {
		return jInstructions;
	}

	public static Class<IFormatInstruction>[] getIFormatInstructions() {
		return iInstructions;
	}

	public static Class<PseudoInstruction>[] getPseudoInstructions() {
		return pInstructions;
	}

	public static Vector<String> keyWords(){
		return keyWords;
	}

}