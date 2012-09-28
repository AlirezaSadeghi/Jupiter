package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

public class TextEditorComponent extends JPanel {
	private JTextPane textPane ; 
	private JScrollPane textScrollPane ;
	private CodeDocument document ;
	public static HashMap<Object, Action> actions ;
	protected UndoAction undo ; 
	protected RedoAction redo ;
	protected UndoManager undoManager = new UndoManager();
	private Vector<String> keyWords = new Vector<String>();
	public static int column = 0 ;
	public static int line = 0 ;
	protected TextLineNumber tnl ;
	public static void init()
	{
		actions = createActionTable(new JTextPane());
	}
	
	public JTextPane getTextPane()
	{
		return this.textPane ;
	}
	
	private static HashMap<Object , Action> createActionTable(JTextComponent textComponent){
	HashMap<Object , Action> actions = new HashMap<Object, Action>() ;
	Action[] actionsArray = textComponent.getActions();
    for (int i = 0; i < actionsArray.length; i++) {
        Action a = actionsArray[i];
        actions.put(a.getValue(Action.NAME), a);
    	}
    return actions ;
	}	
	
	public TextEditorComponent()
	{
		textPane = new JTextPane();
		textPane.setCaretPosition(0);
		textPane.setMargin(new Insets(10,20,10,10));
		textPane.setBorder(BorderFactory.createEtchedBorder(1, Color.black, Color.gray));
		initializeKeyWords();
		textScrollPane = new JScrollPane(textPane);
		tnl = new TextLineNumber(textPane);
		textScrollPane.setAutoscrolls(true);
		textScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED) ;
		textScrollPane.setPreferredSize(new Dimension(1000,600)) ;
		textScrollPane.setRowHeaderView(tnl);
		undo = new UndoAction() ;
		redo = new RedoAction() ;
		textScrollPane.setBounds(0, 0, 1000, 600);
		addBindings();
		document.addUndoableEditListener(new TheUndoableEditListener()) ;
		document.addDocumentListener(new TheDocumentListener());
		textPane.setSelectedTextColor(Color.black);
		textPane.setSelectionColor(Color.orange);
		this.add(textScrollPane);
		this.setLayout(null);
		this.setSize(1000, 600);
	}
		
	public void refreshBounds(int width, int height){
//		textScrollPane.setPreferredSize(new Dimension(width,height)) ;
		textScrollPane.setBounds(0, 10, width, height);
//		this.setSize(width, height);
	}
	
	private void initializeKeyWords()
	{
		document = new CodeDocument() ;
		keyWords.add("add");
		keyWords.add("sub");
		keyWords.add("subi");
		keyWords.add("addi");
		keyWords.add("addu");
		keyWords.add("and");
		keyWords.add("or");
		keyWords.add("xor");
		keyWords.add("nor");
		keyWords.add("bne");
		keyWords.add("beq");
		keyWords.add("slti");
		keyWords.add("j");
		keyWords.add("bgt");
		keyWords.add("blt");
		keyWords.add("sgt");
		keyWords.add("sll");
		keyWords.add("sllv");
		keyWords.add("slt");
		keyWords.add("sra");
		keyWords.add("srav");
		keyWords.add("srl");
		keyWords.add("ori");
		keyWords.add("lui");
		keyWords.add("andi");
		keyWords.add("addui");
		keyWords.add("srlv");
		keyWords.add("not");
		keyWords.add("lw");
		keyWords.add("beqz");
		document.setKeywords(keyWords);
		textPane.setDocument(document);
	}
	
	class TheUndoableEditListener implements UndoableEditListener
	{
		public void undoableEditHappened(UndoableEditEvent e) {
			undoManager.addEdit(e.getEdit()) ;
			undo.updateUndoState();
			redo.updateRedoState();
		}
	}

	class TheDocumentListener implements DocumentListener
	{
		public void insertUpdate(DocumentEvent e) {
			try {
					String lastChar = e.getDocument().getText(e.getOffset(),e.getDocument().getLength()-e.getOffset());
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			Element root = textPane.getDocument().getDefaultRootElement();
			int index = root.getElementIndex(e.getOffset());
			Element lin = root.getElement(index);
			JMaker.col.setText("Column : " + (textPane.getCaretPosition() - lin.getStartOffset() + 1));
		}
		public void removeUpdate(DocumentEvent e) {
		
		}
		public void changedUpdate(DocumentEvent e) {
		}
		
	}
	
	protected void addBindings ()
	{
		InputMap inputMap = textPane.getInputMap(); 
		
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_B , Event.CTRL_MASK) ;
		inputMap.put(key, DefaultEditorKit.backwardAction) ;
		
		key = KeyStroke.getKeyStroke(KeyEvent.VK_H,Event.CTRL_MASK) ;
		inputMap.put(key, DefaultEditorKit.backwardAction) ;
		
		key = KeyStroke.getKeyStroke(KeyEvent.VK_L , Event.CTRL_MASK) ;
		inputMap.put(key, DefaultEditorKit.forwardAction);
		
		key = KeyStroke.getKeyStroke(KeyEvent.VK_J ,Event.CTRL_MASK) ;
		inputMap.put(key, DefaultEditorKit.upAction) ;
		
		key = KeyStroke.getKeyStroke(KeyEvent.VK_K ,Event.CTRL_MASK) ;
		inputMap.put(key, DefaultEditorKit.downAction) ;
		
		key = KeyStroke.getKeyStroke(KeyEvent.VK_Z,Event.CTRL_MASK) ;
		inputMap.put(key, "Undo") ;
		textPane.getActionMap().put("Undo", undo) ; 
		
		key = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK ) ;
		inputMap.put(key, "Redo");
		textPane.getActionMap().put("Redo", redo) ;
		
		MyCtrlSpaceAction ctrlAct = new MyCtrlSpaceAction() ; 
		key = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE ,Event.CTRL_MASK ) ;
		inputMap.put(key, "CtrlSpace") ;
		textPane.getActionMap().put("CtrlSpace", ctrlAct) ;
		
	}
	// TODO : implement later 
	class MyCtrlSpaceAction extends AbstractAction {
		public void actionPerformed(ActionEvent e)
		{
			replaceStuff() ;
		}
	}
	
	private void replaceStuff()
	{
		
	}

	class UndoAction extends AbstractAction {

		public UndoAction()
		{
			super("Undo") ;
			setEnabled(false);
		}
		public void actionPerformed(ActionEvent e)
		{
			try{
				undoManager.undo() ;
			}catch(CannotUndoException ex){System.out.println("Cannot Undo!!");}
			updateUndoState() ;
			redo.updateRedoState();
		}
		
		protected void updateUndoState()
		{
			if (undoManager.canUndo())
			{
				setEnabled(true);
				putValue(Action.NAME, undoManager.getUndoPresentationName()) ;
			}
			else
			{
				setEnabled(false) ;
				putValue(Action.NAME , "Undo"); 
			}
		}
	}
	
	class RedoAction extends AbstractAction {
		
		public RedoAction()
		{
			super("Redo") ;
			setEnabled(false);
		}
		
		public void actionPerformed(ActionEvent e) 
		{
			try{
				undoManager.redo() ; 
			}catch(CannotRedoException ex){System.out.println("Cannot Redo!!");}
			updateRedoState() ;
			undo.updateUndoState();
		}
		
		protected void updateRedoState() 
		{
			if(undoManager.canRedo())
			{
				setEnabled(true);
				putValue(Action.NAME, undoManager.getRedoPresentationName());
			}
			else
			{
				setEnabled(false);
				putValue(Action.NAME, "Redo") ;
			}
		}
	}
	
}