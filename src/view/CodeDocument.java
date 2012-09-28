package view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class CodeDocument extends DefaultStyledDocument {
	private String word = "" ;
	 private SimpleAttributeSet keyWord = new SimpleAttributeSet();
	 private SimpleAttributeSet string = new SimpleAttributeSet(); 
	 private SimpleAttributeSet normal = new SimpleAttributeSet(); 
	 private SimpleAttributeSet number = new SimpleAttributeSet(); 
	 private SimpleAttributeSet comments = new SimpleAttributeSet(); 
	 private SimpleAttributeSet lables = new SimpleAttributeSet();
	 private SimpleAttributeSet registers = new SimpleAttributeSet();
	 private int currentPos = 0; 
	 private Vector keywords = new Vector(); 
	 public static int STRING_MODE = 10; 
	 public static int TEXT_MODE = 11; 
	 public static int NUMBER_MODE = 12; 
	 public static int COMMENT_MODE = 13; 
	 public static int REGISTER_MODE = 14;
	 public static int DOT_MODE = 15 ;
	 private int mode = TEXT_MODE; 
	 

	 public CodeDocument() 
	 {
	   StyleConstants.setItalic(lables, true);
	   StyleConstants.setForeground(registers, Color.red);
	   StyleConstants.setForeground(normal, Color.black);
	   StyleConstants.setForeground(keyWord, Color.blue);
	   StyleConstants.setForeground(string, Color.green); 
	   StyleConstants.setForeground(number, Color.magenta); 
	   StyleConstants.setForeground(comments, Color.green); 
	   StyleConstants.setItalic(comments, true); 
	  } 
	
	  private void insertKeyword(String str, int pos){ 
		  try{ 
	      this.remove(pos - str.length(), str.length()); 
	      super.insertString(pos - str.length(), str, keyWord); 
	    } 
	    catch (Exception ex){ 
	      ex.printStackTrace(); 
	    } 
	  } 
	 private void insertTextString(String str, int pos){ 
	    try{ 
	      this.remove(pos,str.length()); 
	      if (mode == REGISTER_MODE )
	   	  {
	    	  mode = TEXT_MODE;
	    	  super.insertString(pos, str, registers);
	      }
	      else
	    	  super.insertString(pos, str, string); 
	    } 
	    catch (Exception ex){ 
	      ex.printStackTrace(); 
	    } 
	  } 
	private void insertNumberString(String str, int pos){ 
		try{ 
		   this.remove(pos,str.length()); 
		      super.insertString(pos, str, number); 
		    } 
		    catch (Exception ex){ 
		      ex.printStackTrace(); 
		    } 
		 } 
		 
	private void insertCommentString(String str, int pos,String st)
	{
		if (!st.contains("#"))
		{
			mode = TEXT_MODE;
			try {
				this.remove(pos, str.length());
				super.insertString(pos, str, normal);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		try{
			this.remove(pos, str.length());
			super.insertString(pos, str, comments);
		}catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	private void checkForString()
	{
		int offs = this.currentPos ;
		Element elem = this.getParagraphElement(offs);
		String elementText= "";
		try{
			elementText = this.getText(elem.getStartOffset(), elem.getEndOffset()-elem.getStartOffset());
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		int strLen = elementText.length(); 
	    if ( strLen == 0 ) 
	    	return;
	    int i = 0; 
	    if (elem.getStartOffset() > 0){ 
	      offs = offs - elem.getStartOffset(); 
	    } 
	    int quoteCount = 0; 
	    if ((offs >= 0) && (offs <= strLen-1)){ 
	      i = offs; 
	      while (i >0){ 
	        char charAt = elementText.charAt(i); 
	        if ((charAt == '"')){ 
	         quoteCount ++; 
	        } 
	        i--; 
	      } 
	      if (mode == REGISTER_MODE)
	    	  return ;
	      int rem = quoteCount % 2; 
	      mode = (rem == 0) ? TEXT_MODE: STRING_MODE; 
	    }
	}
	
	private void checkForKeyword(){ 
		    if (mode != TEXT_MODE) { 
		    	return; 
		    } 
		    int offs = this.currentPos; 
		    Element element = this.getParagraphElement(offs); 
		    String elementText = ""; 
		    try{ 
		      elementText = this.getText(element.getStartOffset(), 
		    		  element.getEndOffset() - element.getStartOffset()); 
		    } 
		    catch(Exception ex){ 
		      System.out.println("no text"); 
		    }
		    int strLen = elementText.length(); 
		    if (strLen == 0) {return;} 
		    int i = 0; 
		    if (element.getStartOffset() > 0){ 
		      offs = offs - element.getStartOffset(); 
		    } 
		    if ((offs >= 0) && (offs <= strLen-1)){ 
		      for ( i = offs ; i >= 0 ; i--){
		      char charAt = elementText.charAt(i); 
			        word = elementText.substring(i, offs);//skip the period 
			        String s = word.trim().toLowerCase(); 
			        if (keywords.contains(s)){ 
		            insertKeyword(word, currentPos); 
		        break; 
		        } 
		      } 
		    } 
		  }
	 
	 
	 private void checkForNumber(){ 
		    int offs = this.currentPos; 
		    Element element = this.getParagraphElement(offs); 
		    String elementText = ""; 
		    try{ 
		      elementText = this.getText(element.getStartOffset(), 
		element.getEndOffset() - element.getStartOffset()); 
		    } 
		    catch(Exception ex){ 
		      System.out.println("no text"); 
		    } 
		    int strLen = elementText.length(); 
		    if (strLen == 0) {return;} 
		    int i = 0; 
		    if (element.getStartOffset() > 0){ 
		      offs = offs - element.getStartOffset(); 
		    } 
		    int prevMode = mode;
		    if ((offs >= 0) && (offs <= strLen-1)){ 
		      i = offs; 
		      while (i >0){ 
		      char charAt = elementText.charAt(i); 
		      if ((charAt == ' ') | (i == 0)){ 
		          if(i != 0)
		              i++; 
		          if(mode != COMMENT_MODE)
		        	  mode = NUMBER_MODE; 
		          break; 
		        } 
		        else if (!(charAt >= '0' & charAt <= '9'))
		        { 
		          mode = prevMode; 
		          break; 
		        } 
		        i--; 
		      } 
		    } 
		  }
	 private void insertRegisterString(String str, int pos)
	 {
		 try{ 
			   this.remove(pos,str.length()); 
			      super.insertString(pos, str, registers); 
			    } 
			    catch (Exception ex){ 
			      ex.printStackTrace(); 
			    }
	 }
	 
	 private void insertLableString(String str , int pos){
		 try{ 
		   this.remove(pos,str.length()); 
		      super.insertString(pos, str, this.lables); 
		    } 
		    catch (Exception ex){ 
		      ex.printStackTrace(); 
		    }
	 }
	 
	 private void checkForLabel(char str){
		 int offs = this.currentPos; 
		    Element element = this.getParagraphElement(offs); 
		    String elementText = ""; 
		    try{ 
		      elementText = this.getText(element.getStartOffset(), 
		element.getEndOffset() - element.getStartOffset()); 
		    } 
		    catch(Exception ex){ 
		      System.out.println("no text"); 
		    } 
		    int strLen = elementText.length(); 
		    if (strLen == 0) {return;} 
		    int i = 0; 
		 

		    if (element.getStartOffset() > 0){ 
		      offs = offs - element.getStartOffset(); 
		    }
		    if(mode != COMMENT_MODE)
		    {
		    	if (str != '.')
		    		insertLableString(elementText.split(":")[0].trim(),element.getStartOffset());
		    	else if(str == '.'){
		    		mode = DOT_MODE;
		    	}
		    }
	 }
	 private void checkForRegister()
	 {
		 int offs = this.currentPos; 
		    Element element = this.getParagraphElement(offs); 
		    String elementText = ""; 
		    try{ 
		      elementText = this.getText(element.getStartOffset(), 
		element.getEndOffset() - element.getStartOffset()); 
		    } 
		    catch(Exception ex){ 
		      System.out.println("no text"); 
		    } 
		    int strLen = elementText.length(); 
		    if (strLen == 0) {return;} 
		    int i = 0; 
		 

		    if (element.getStartOffset() > 0){ 
		      offs = offs - element.getStartOffset(); 
		    } 
		    if ((offs >= 1) && (offs <= strLen-1)){ 
		      i = offs; 
		      char registerStartChar1 = elementText.charAt(i); 
		      if (registerStartChar1== '$' && mode != COMMENT_MODE)
		      {
		    	  this.mode = REGISTER_MODE;
		    	  this.insertRegisterString(" $", currentPos-1);
		      }
		    }
	 }
	 private void checkForComment(){ 
		    int offs = this.currentPos; 
		    Element element = this.getParagraphElement(offs); 
		    String elementText = ""; 
		    try{ 
		      elementText = this.getText(element.getStartOffset(), 
		element.getEndOffset() - element.getStartOffset()); 
		    } 
		    catch(Exception ex){ 
		      System.out.println("no text"); 
		    } 
		    int strLen = elementText.length(); 
		    if (strLen == 0) {return;} 
		    int i = 0; 
		 

		    if (element.getStartOffset() > 0){ 
		      offs = offs - element.getStartOffset(); 
		    } 
		    if ((offs >= 1) && (offs <= strLen-1)){ 
		      i = offs; 
		      char commentStartChar1 = elementText.charAt(i); 
		      if(!elementText.contains("#"))
		      {
		    	  mode = TEXT_MODE;
		    	  return ;
		      }
		      if (commentStartChar1 == '#' ){ 
		          mode = COMMENT_MODE; 
		          this.insertCommentString("", currentPos-1,elementText); 
		      } 
		    } 
		  }
	 
	  private void processChar(String str){ 
		    char strChar = str.charAt(0); 
		    if (mode != CodeDocument.COMMENT_MODE && mode != REGISTER_MODE && mode != DOT_MODE){ 
		      mode = TEXT_MODE; 
		    } 
		      Element element = this.getParagraphElement(currentPos);
		      String elText = null;
			try {
				elText = this.getText(element.getStartOffset(), element.getEndOffset()-element.getStartOffset());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		    
		    switch (strChar){ 
		      case (':'):case ('.'):
		    	  checkForLabel(strChar);
		      break ;
		     case (' '): case('\n'): { 
		          checkForKeyword(); 
		          if (strChar == '\n'){ 
		            mode = TEXT_MODE; 
		          } 
		        } 
		        break; 
		        case ('"'):{ 
		          insertTextString(str, currentPos); 
		          this.checkForString(); 
		        } 
		        break; 
		        case ('0'):case ('1'):case ('2'):case ('3'):case ('4'): 
		        case ('5'):case ('6'):case ('7'):case ('8'):case ('9'):{ 
		          checkForNumber(); 
		        } 
		        break; 
		        case ('#'):{ 
		          checkForComment(); 
		        } 
		        break;
		        case ('$'):{
		        	checkForRegister();
		        }
		        break ;
		        case (','):{
		        	if (mode != COMMENT_MODE)
		        		mode = TEXT_MODE;
		        	insertTextString(str, this.currentPos);
		        }
		        default :
		        	checkForKeyword();
		      } 
		
		      if (mode == CodeDocument.TEXT_MODE){ 
		        this.checkForString(); 
		      } 
		      if (mode == CodeDocument.STRING_MODE){ 
		        insertTextString(str, this.currentPos); 
		      } 
		      else if (mode == CodeDocument.NUMBER_MODE){ 
		        insertNumberString(str, this.currentPos); 
		      } 
		      else if (mode == CodeDocument.COMMENT_MODE){ 
		        insertCommentString(str, this.currentPos,elText); 
		      } 
		      else if (mode == CodeDocument.REGISTER_MODE){
		    	  insertRegisterString(str, this.currentPos);
		      }
		      else if (mode == DOT_MODE)
		    	  insertLableString(str, this.currentPos);
	  } 
	  
	  private void processChar(char strChar){ 
	      char[] chrstr = new char[1]; 
	      chrstr[0] = strChar; 
	      String str = new String(chrstr); 
	      processChar(str); 
	  }
	  
	  public void insertString (int offs, String str, AttributeSet a) throws BadLocationException
	  { 
		super.insertString(offs, str, normal); 
		int strLen = str.length();
		int endpos = offs + strLen; 
		int strpos;
		for (int i=offs;i<endpos;i++){ 
		currentPos = i;
		strpos = i - offs;
		processChar(str.charAt(strpos));
		}
		currentPos = offs; 
	  }
	  public Vector<String> getKeywords()
	  { 
		    return this.keywords; 
	  } 
	  public void setKeywords(Vector<String> aKeywordList){ 
	      if (aKeywordList != null)
	          this.keywords = aKeywordList;
	  } 
}
