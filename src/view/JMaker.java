package view;

import java.awt.AWTKeyStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.StyledEditorKit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import control.CompileError;

public class JMaker {
	protected JPanel canvas ; 
	public JTabbedPane tabbedPane ;
	private TextEditorComponent textEditor ;
	private DefaultListModel<CompileError> compileInformation ;
	private DefaultListModel<String> runtimeInformation ;
	private JTabbedPane outputTabbedPane;
	private JScrollPane compileInfoScrollPane ;
	private JScrollPane runtimeInfoScrollPane ;

	private ArrayList<TextEditorComponent> textEditors = new ArrayList<TextEditorComponent>() ;
	
	private JMenu file;
	private JMenu edit;
	private JMenu run;
	private JMenu view;
	private JMenu tools;
	private JMenu help;
	private JMenuBar menuBar;
	private DistroyDocument DD;
	private JPanel leftSide; 
	private JPanel statisticsPanel ;
	private JLabel trade ;
	private int count;
	private JMenuItem newDocument;
	private JMenuItem removeDocument;
	private JMenuItem openFile;
	private JMenuItem closeAllDocuments;
	private JMenuItem save;
	private JMenuItem saveAs ;
	private JFileChooser chooser ;
	private File openedFile ;
	private JButton fast_forward ;
	private JButton backward ;
	private JButton runner  ;
	private JButton pause ;
	private JButton terminate ;
	private Formatter formatter ; 
	private JPanel tradeMark ;
	private ActionHandler handle ;
	private File currentFile = null ;
	private ControlHandler controller ;
	public static JLabel line ;
	public static JLabel col;
	boolean b ;
	private JMenuItem statistics; 
	public JMaker ()
	{
		canvas = new JPanel() ;
		leftSide = new JPanel() ;
		handle = new ActionHandler();
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		textEditor = new TextEditorComponent();
		textEditors.add(textEditor);
		tabbedPane.addTab("Document 0",textEditor);
		canvas.add(tabbedPane);
		canvas.setLayout(null);
		tabbedPane.setBounds(0, 0, 1000, 600);
		compileInformation = new DefaultListModel<CompileError>();
		runtimeInformation = new DefaultListModel<String>();
		final JList<CompileError> cList = new JList<CompileError>(compileInformation);
		JList<String> rList = new JList<String>(runtimeInformation);
		compileInfoScrollPane = new JScrollPane(cList);
		cList.setForeground(Color.red);
		rList.setForeground(Color.red);
//		cList.addMouseListener(new MouseAdapter() {
//			public void mouseClicked(MouseEvent e) {
//				if(e.getClickCount() == 2 && cList.getSelectedIndex() != -1){
//					CompileError er = (CompileError)compileInformation.getElementAt(cList.getSelectedIndex());
//				}
//			}
//		});
		compileInfoScrollPane.setPreferredSize(new Dimension(700, 200));
		runtimeInfoScrollPane = new JScrollPane(rList);
		runtimeInfoScrollPane.setPreferredSize(new Dimension(700, 200));
		outputTabbedPane =  new JTabbedPane();
		outputTabbedPane.addTab("Errors", compileInfoScrollPane);
		outputTabbedPane.addTab("Runtime", runtimeInfoScrollPane);
		JPanel thing = new JPanel() ;
		line = new JLabel("Line: "+0);
		col = new JLabel("Column: "+0);
		thing.add(line);
		thing.add(col);
		keyBindings(this.textEditor.getTextPane()) ;
		canvas.setBackground(new Color(61, 61, 61));
		trade = new JLabel(new ImageIcon(Resources.JUPITER.getScaledInstance(290, 200, 0)));
		statisticsPanel = new JPanel();
		
	}
	
	public JPanel getCanvas()
	{
		return this.canvas ;
	}
	
	public JLabel getTradeMark()
	{
		return this.trade;
	}
	
	private Action getActionByName(String s)
	{
		return TextEditorComponent.actions.get(s);
	}
	
	private JMenu createViewMenu()
	{
		JMenu view = new JMenu("View") ;
		Action action = new StyledEditorKit.BoldAction() ;
		action.putValue(action.NAME, "Bold") ;
		view.add(action).setIcon(new ImageIcon(Resources.BOLD.getScaledInstance(20, 20, 0)));
		action = new StyledEditorKit.ItalicAction() ;
		action.putValue(action.NAME, "Italic") ;
		view.add(action).setIcon(new ImageIcon(Resources.ITALIC.getScaledInstance(20, 20, 0)));
		action = new StyledEditorKit.UnderlineAction() ;
		action.putValue(action.NAME, "Underlined");
		view.add(action).setIcon(new ImageIcon(Resources.UNDERLINED.getScaledInstance(20, 20, 0)));
		view.addSeparator(); 
        view.add(new StyledEditorKit.FontSizeAction("12", 12)).setIcon(new ImageIcon(Resources.FONT_SIZE.getScaledInstance(20, 20, 0)));
        view.add(new StyledEditorKit.FontSizeAction("14", 14)).setIcon(new ImageIcon(Resources.FONT_SIZE.getScaledInstance(20, 20, 0)));;
        view.add(new StyledEditorKit.FontSizeAction("18", 18)).setIcon(new ImageIcon(Resources.FONT_SIZE.getScaledInstance(20, 20, 0)));;
        view.addSeparator();
        view.add(new StyledEditorKit.FontFamilyAction("Serif","Serif")).setIcon(new ImageIcon(Resources.FONT_FAMILY.getScaledInstance(20, 20, 0)));
        view.add(new StyledEditorKit.FontFamilyAction("SansSerif","SansSerif")).setIcon(new ImageIcon(Resources.FONT_FAMILY.getScaledInstance(20, 20, 0)));
        view.addSeparator();
        return view ;
	}
	
	public void refreshBounds(int width, int height){
		textEditor.refreshBounds(width, height);
	}
	private class CreateNewDocument extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			count++ ;
			TextEditorComponent TEC = new TextEditorComponent();
			textEditors.add(TEC);
			keyBindings(TEC.getTextPane());
			tabbedPane.addTab("Document " + count, TEC);
		}
	}
	
	class DistroyDocument extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			doRemoveStuff(e,null);
		}
	}
	
	private class OpenDocument extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			doOpenStuff();
		}
	}
	
	private class SaveDocument extends AbstractAction{
		public void actionPerformed(ActionEvent e) {
			saveStuff(e);
		}
	}
	
	private class SaveDocumentAs extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			saveAsStuff(e);
		}
	}
	
	private class TabChange extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			switchTabStuff(e);
		}
	}
	
	private class ReverseTabChange extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			switchReverseTabStuff(e); 
		}
	}
	
	private class DistroyWholeDocument extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			doRemoveWholeStuff(e);
		}
	}

	public void keyBindings(JTextPane text)
	{
		addCreateNew(text);
		addDistroy(text);
		addDistroyWhole(text);
		addOpen(text);
		addSave(text);
		addSaveAs(text);
		addChangeTab(text);
		addReverseChangeTab(text);
	}
	private void addCreateNew(JTextPane text)
	{
		CreateNewDocument CND = new CreateNewDocument();
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK);
		text.getInputMap().put(key, "createNewDoc") ;
		text.getActionMap().put("createNewDoc", CND) ;
	}
	private void addDistroyWhole(JTextPane text)
	{
		DistroyWholeDocument DWD = new DistroyWholeDocument();
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK + Event.SHIFT_MASK);
		text.getInputMap().put(key, "distroyAll");
		text.getActionMap().put("distroyAll", DWD);
	}
	private void addDistroy(JTextPane text)
	{
		DistroyDocument DD = new DistroyDocument();
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_W,Event.CTRL_MASK);
		text.getInputMap().put(key, "distroy");
		text.getActionMap().put("distroy", DD);
	}
	private void addOpen(JTextPane text)
	{
		OpenDocument OD = new OpenDocument();
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_O,Event.CTRL_MASK);
		text.getInputMap().put(key, "open");
		text.getActionMap().put("open", OD);
	}
	private void addSave(JTextPane text)
	{
		SaveDocument SD = new SaveDocument();
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_S ,Event.CTRL_MASK);
		text.getInputMap().put(key, "save");
		text.getActionMap().put("save", SD) ;
	}
	private void addSaveAs(JTextPane text)
	{
		SaveDocumentAs SDA = new SaveDocumentAs();
		KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.SHIFT_MASK + Event.CTRL_MASK);
		text.getInputMap().put(key, "saveAs");
		text.getActionMap().put("saveAs", SDA) ;
	}
	private void addChangeTab(JTextPane text)
	{
		TabChange TC = new TabChange ();
		KeyStroke key = KeyStroke.getKeyStroke("ctrl TAB");
		Set<AWTKeyStroke> fKeys = new HashSet<AWTKeyStroke>(tabbedPane.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
		fKeys.remove(key);
		tabbedPane.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, fKeys);
		text.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(key, "changeTab");
		text.getActionMap().put("changeTab", TC);
	}
	
	private void addReverseChangeTab(JTextPane text)
	{
		ReverseTabChange RTC = new ReverseTabChange();
		KeyStroke key = KeyStroke.getKeyStroke("ctrl shift TAB");
		Set<AWTKeyStroke>bKeys = new HashSet<AWTKeyStroke>(tabbedPane.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS));
		bKeys.remove(key);
		tabbedPane.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, bKeys);
		text.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(key, "reverseTabChange");
		text.getActionMap().put("reverseTabChange", RTC);
	}
	
	public JMenuBar getMenus()
	{
		file = new JMenu("File") ;
		file.setMnemonic('f') ;
		fileInit(file);
		edit = new JMenu("Edit") ;
		JMenuItem a = new JMenuItem("Ctrl_Z for undo") ;
		a.setIcon(new ImageIcon(Resources.UNDO.getScaledInstance(20, 20, 0)));
		edit.add(a).setEnabled(false);
		JMenuItem b = new JMenuItem("Ctrl_Y for redo");
		b.setIcon(new ImageIcon(Resources.REDO.getScaledInstance(20, 20, 0)));
		edit.add(b).setEnabled(false);
		edit.addSeparator();
		edit.add(getActionByName(DefaultEditorKit.cutAction)).setIcon(new ImageIcon(Resources.CUT.getScaledInstance(20, 20, 0)));
		edit.add(getActionByName(DefaultEditorKit.copyAction)).setIcon(new ImageIcon(Resources.COPY.getScaledInstance(20, 20, 0)));
		edit.add(getActionByName(DefaultEditorKit.pasteAction)).setIcon(new ImageIcon(Resources.PASTE.getScaledInstance(20, 20, 0)));
		edit.addSeparator();
		edit.add(getActionByName(DefaultEditorKit.selectAllAction)).setIcon(new ImageIcon(Resources.SELECT_ALL.getScaledInstance(20, 20, 0)));
		edit.setMnemonic('e') ;
		view = createViewMenu();
		view.setMnemonic('v') ;
		run  = new JMenu("Run") ;
		runMenu(run);
		tools = new JMenu("Tools") ;
		tools.setMnemonic('t') ;
		statistics = new JMenuItem(new ImageIcon(Resources.STATISTICS.getScaledInstance(20, 20, 0)));
		JMenuItem count = new JMenuItem(new ImageIcon(Resources.COUNT.getScaledInstance(20, 20, 0)));
		count.setText("Instruction Counter");
		statistics.setText("Instruction Statistics");
		count.setMnemonic('c');
		statistics.setMnemonic('s');
		tools.add(statistics);
		tools.add(count);
		help = new JMenu("Help") ;
		help.setMnemonic('h') ;
		menuBar = new JMenuBar() ;
		menuBar.add(file) ;
		menuBar.add(edit) ;
		menuBar.add(view);
		menuBar.add(run) ;
		menuBar.add(tools) ;
		menuBar.add(help) ;
		return menuBar ;
	}
	
	public JMenuItem getStatistics()
	{
		return this.statistics;
	}
	
	
	
	private void runMenu(JMenu run)
	{
		run.setMnemonic('r');
		JMenuItem assemble = new JMenuItem(new ImageIcon(Resources.ASSEMBLE.getScaledInstance(20, 20, 0)));
		assemble.setText("Assemble  Ctrl-F11");
		run.add(assemble);
		run.addSeparator();
		JMenuItem go = new JMenuItem(new ImageIcon(Resources.FORWARD.getScaledInstance(20, 20, 0)));
		go.setText("Run       			     Ctrl-F3");
		go.setMnemonic('r');
		JMenuItem step = new JMenuItem(new ImageIcon(Resources.RUN.getScaledInstance(20, 20, 0)));
		step.setText("Step                F6");
		step.setMnemonic('s');
		JMenuItem backStep = new JMenuItem(new ImageIcon(Resources.STEP_BACK.getScaledInstance(20, 20, 0)));
		backStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "This Feature Is Not Available In Trial Version \nRegister Online To Use Full Functionality");
			}
		});
		backStep.setText("BackStep         F5");
		backStep.setMnemonic('B');
		JMenuItem pause = new JMenuItem(new ImageIcon(Resources.PAUSE.getScaledInstance(20, 20, 0)));
		pause.setText("Pause");
		pause.setMnemonic('p');
		JMenuItem stop = new JMenuItem(new ImageIcon(Resources.TERMINATE.getScaledInstance(20, 20, 0)));
		stop.setText("Terminate");
		stop.setMnemonic('t');
		JMenuItem reset = new JMenuItem(new ImageIcon(Resources.BACKWARD.getScaledInstance(20, 20, 0)));
		reset.setText("Reset");
		assemble.setMnemonic('a');
		run.add(go);
		run.add(step);
		run.add(backStep);
		run.addSeparator();
		run.add(pause);
		run.add(stop);
		run.add(reset);
	}
	
	private void fileInit(JMenu file)
	{
		newDocument = new JMenuItem("New Document           Ctrl-N");
		newDocument.setIcon(new ImageIcon(Resources.NEW.getScaledInstance(20, 20, 0))) ;
		newDocument.addActionListener(handle);
		
		removeDocument = new JMenuItem("Close Document         Ctrl-W");
		removeDocument.setIcon(new ImageIcon(Resources.REMOVE.getScaledInstance(20, 20, 0)));
		removeDocument.addActionListener(handle);
		
		openFile = new JMenuItem("Open Document         Ctrl-O") ;
		openFile.setIcon(new ImageIcon(Resources.OPEN.getScaledInstance(20, 20, 0)));
		openFile.addActionListener(handle);
		
		closeAllDocuments = new JMenuItem("Close All             Ctrl-Shift-W");
		closeAllDocuments.setIcon(new ImageIcon(Resources.DELETE_ALL.getScaledInstance(20, 20, 0)));
		closeAllDocuments.addActionListener(handle);
		
		save = new JMenuItem("Save                           Ctrl-S") ;
		save.setIcon(new ImageIcon(Resources.SAVE.getScaledInstance(20, 20, 0)));
		save.addActionListener(handle);
		
		saveAs = new JMenuItem("Save As..            Ctrl-Shift-S");
		saveAs.setIcon(new ImageIcon(Resources.SAVE_AS));
		saveAs.addActionListener(handle);
		
		file.add(newDocument);
		newDocument.setMnemonic('n');
		
		file.add(removeDocument);
		removeDocument.setMnemonic('r');
		file.add(openFile);
		openFile.setMnemonic('o');
		file.add(closeAllDocuments);
		closeAllDocuments.setMnemonic('c');
		file.addSeparator();
		file.add(save);
		save.setMnemonic('s');
		file.add(saveAs);
	}
	
	JButton assemble;
	public JButton[] getButtons()
	{
		JButton copy = new JButton(new ImageIcon(Resources.COPY)) ; 
		JButton paste = new JButton(new ImageIcon(Resources.PASTE));
		JButton cut = new JButton(new ImageIcon(Resources.CUT));
		assemble = new JButton(new ImageIcon(Resources.ASSEMBLE)) ;
		JButton newDoc = new JButton(new ImageIcon(Resources.NEW)) ;
		JButton openDoc = new JButton(new ImageIcon(Resources.OPEN)) ;
		JButton save = new JButton(new ImageIcon(Resources.SAVE)) ;
		JButton undo = new JButton(new ImageIcon(Resources.UNDO)) ;
		undo.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				try{
				textEditor.undoManager.undo();
				}catch(CannotUndoException ex){}
			}
		});
		JButton redo = new JButton(new ImageIcon(Resources.REDO)) ;
		redo.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {
				try{
					textEditor.undoManager.redo();
				}catch(CannotRedoException ex){}
			}
		});
		runner = new JButton(new ImageIcon(Resources.RUN)) ;
		copy.setToolTipText("Copy");
		paste.setToolTipText("Paste");
		cut.setToolTipText("Cut");
		assemble.setToolTipText("Assemble  Ctrl-F3");
		newDoc.setToolTipText("New Document  Ctrl-N");
		openDoc.setToolTipText("Open Document  Ctrl-O");
		openDoc.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				doOpenStuff();
			}
		});
		newDoc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				count++ ;
				TextEditorComponent TEC = new TextEditorComponent();
				textEditors.add(TEC);
				keyBindings(TEC.getTextPane());
				tabbedPane.addTab("Document " + count, TEC);
			}
		});
		save.setToolTipText("Save") ;
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveAsStuff(e);
			}
		});
		undo.setToolTipText("Undo  Ctrl-Z");
		redo.setToolTipText("Redo Ctrl-Y");
		runner.setToolTipText("Run the Program  Ctrl-F11");
		controller = new ControlHandler();
		runner.addActionListener(controller);
		JButton [] buttons = { assemble,  newDoc,save, openDoc,redo, undo};
		return buttons ;
	}
	
	
	private class ControlHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == runner )
				doRunStuff();
			if (e.getSource() == fast_forward)
				doForwardStuff();
			if (e.getSource() == terminate)
				doTerminateStuff();
			if (e.getSource() == backward)
				doBackwardStuff();
			if (e.getSource() == pause)
				doPauseStuff() ;
		}
	}
	// TODO
	private void doRunStuff()
	{

	}
	// TODO
	private void doForwardStuff()
	{
		
	}
	// TODO
	private void doTerminateStuff()
	{
		
	}
	// TODO
	private void doBackwardStuff()
	{
		
	}
	//TODO 
	private void doPauseStuff() 
	{
		
	}
	
	private class ActionHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == newDocument)
				createDoc();
			if (e.getSource() == removeDocument)
				removeDoc(e, tabbedPane.getSelectedComponent());
			if ( e.getSource() == openFile )
				openDoc();
			if(e.getSource() == closeAllDocuments)
				closeAllDocs();
			if(e.getSource() == save)
				saveDoc(e);
			if(e.getSource() == saveAs)
				saveDocAs(e);
		}
		
		private void createDoc()
		{
			count++ ;
			TextEditorComponent TEC = new TextEditorComponent();
			textEditors.add(TEC);
			keyBindings(TEC.getTextPane());
			tabbedPane.addTab("Document " + count, TEC);
		}
		
		private void removeDoc(ActionEvent e, Component component)
		{
			doRemoveStuff(e, component);
		}

		private void openDoc()
		{
			doOpenStuff();
		}
		
		private void closeAllDocs()
		{
			int result = JOptionPane.showConfirmDialog(null, "All unsaved changes will be lost , Continue ?", "Warning !!",JOptionPane.INFORMATION_MESSAGE);
			if (result == 1)
				return ;
			else if (result == 2)
				return ;
			else if (result == 0 )
			{
				for (int i = 0 ; i<textEditors.size();i++)
				{
					tabbedPane.remove(textEditors.get(i));
				}
				textEditors.removeAll(textEditors);
			}
		}
		
		private void saveDocAs(ActionEvent e)
		{
			saveAsStuff(e);
		}
		
		private void saveDoc(ActionEvent e)
		{
			saveStuff(e);
		}
	}
	
	private void switchReverseTabStuff(ActionEvent e)
	{
		for (int i = 0 ; i < textEditors.size() ;i++)
			if (e.getSource() == textEditors.get(i).getTextPane())
				if (i-1 >= 0)
					tabbedPane.setSelectedIndex(i-1);
	}
	
	private void switchTabStuff(ActionEvent e)
	{
		for (int i = 0 ; i < textEditors.size();i++)
			if (e.getSource() == textEditors.get(i).getTextPane())
				if( i + 1 <= textEditors.size()-1)
					tabbedPane.setSelectedIndex(i+1);
	}
	
	private void saveAsStuff(ActionEvent e)
	{
		chooser = new JFileChooser();
		String code = ""; 
		File file = null ;
		int result = chooser.showSaveDialog(null);
		if(result == JFileChooser.CANCEL_OPTION)
 		{
			JOptionPane.showMessageDialog(null,"Selection Canceled","Interrupted !!",JOptionPane.INFORMATION_MESSAGE);
			return ;
 		}
		else if(result == JFileChooser.APPROVE_OPTION)
		{
			int rersult ;
			currentFile = file = chooser.getSelectedFile();
			for (int i = 0 ; i < textEditors.size();i++)
				if(e.getSource() == textEditors.get(i).getTextPane())
				{	
					tabbedPane.setTitleAt(i, file.getName());
					try {
						code += textEditors.get(i).getTextPane().getDocument().getText(0, textEditors.get(i).getTextPane().getDocument().getLength());
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
			if (file.exists())
			{
				result = JOptionPane.showConfirmDialog(null, "This will overwrite an existing file,Continue ?","Overwrite" ,JOptionPane.INFORMATION_MESSAGE) ;
				if (result == 0)
				{
					try {
						formatter =  new Formatter(file);
						formatter.format(code);
						formatter.close();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					}
				}
				else 
					return ;
			}
			try {
				formatter = new Formatter(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			formatter.format(code);
			formatter.close();
		}
	}
	
	
	private void saveStuff(ActionEvent e)
	{
		if (currentFile == null )
		{
			saveAsStuff(e);
			return ;
		}
		String code = "";
		for (int i = 0 ; i < textEditors.size() ;i++)
			if (e.getSource() == textEditors.get(i).getTextPane())
				code  = textEditors.get(i).getTextPane().getText() ;
		try {
			formatter = new Formatter(currentFile);
			formatter.format(code) ;
			formatter.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}
	
	private void doRemoveWholeStuff(ActionEvent e)
	{
		int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to close ? all unsaved changes will be lost","Confirmation",JOptionPane.INFORMATION_MESSAGE);
		if (choice == 1 || choice == 2)
			return ;
		else if (choice == 0)
		{
			for(int i = 0 ; i < textEditors.size();i++)
				tabbedPane.remove(textEditors.get(i));
			textEditors.removeAll(textEditors);
			count = 0 ;
			TextEditorComponent TEC = new TextEditorComponent();
			textEditors.add(TEC);
			keyBindings(TEC.getTextPane());
			tabbedPane.addTab("Document " + count, TEC);
		}
	}
	
	private void doRemoveStuff(ActionEvent e,Component c)
	{
		int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to close ? all unsaved changes will be lost","Confirmation",JOptionPane.INFORMATION_MESSAGE);
		if (choice == 1 || choice == 2)
			return ;
		else if (choice == 0)
		{
			ArrayList<TextEditorComponent> deleted = new ArrayList<TextEditorComponent>();
			for (int i = 0 ; i < textEditors.size();i++)
				if (e.getSource()==textEditors.get(i).getTextPane())
				{
					tabbedPane.remove(textEditors.get(i));
					deleted.add(textEditors.get(i));
					if(i == textEditors.size()-1)
						count-- ;
					break ;
				}
			textEditors.removeAll(deleted);
			if(c != null)
				tabbedPane.remove(c);
		}
	}
	
	private void doOpenStuff()
	{
		chooser = new JFileChooser() ;
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int result = chooser.showOpenDialog(null);
		if(result == JFileChooser.CANCEL_OPTION)
 		{
			JOptionPane.showMessageDialog(null,"Selection Canceled","Interrupted !!",JOptionPane.INFORMATION_MESSAGE);
			return ;
 		}
		else
		{
			openedFile = chooser.getSelectedFile();
			if(!openedFile.exists())
				{
					JOptionPane.showMessageDialog(null, "The specified file doesn't exist","Not Found",JOptionPane.ERROR_MESSAGE);
					return ;
				}
			Scanner sc = null ;
			try {
				sc = new Scanner (openedFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			count++ ;
			TextEditorComponent TEC = new TextEditorComponent();
			textEditors.add(TEC);
			keyBindings(TEC.getTextPane());
			tabbedPane.addTab(openedFile.getName(), TEC);
			String code = "";
			while(sc.hasNextLine())
				code += (sc.nextLine()+"\n");
			TEC.getTextPane().setText(code);
		}
	}
	
	public JPanel statisticsPanel()
	{
		return this.statisticsPanel;
	}
	
	public ArrayList<TextEditorComponent> getTextEditors()
	{
		return this.textEditors;
	}
	
	public JTabbedPane getOutputPanel(){
		return this.outputTabbedPane;
	}
	
	public JTabbedPane getTabbedPane(){
		return this.tabbedPane;
	}
	public DefaultListModel<CompileError> getCompileList(){
		return compileInformation;
	}
	
	public DefaultListModel<String> getRuntimeList(){
		return runtimeInformation;
	}
	
	public void focusCompileList(){
		outputTabbedPane.setSelectedIndex(0);
	}
	
	public void focusRuntimeList(){
		outputTabbedPane.setSelectedIndex(1);
	}
	
	public JButton getAssembleButton(){
		return assemble;
	}
	
	
	public TextEditorComponent getTextEditor(){
		return textEditor;
	}
}