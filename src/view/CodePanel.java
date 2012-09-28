package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.BadLocationException;

import model.RegisterFile;
import control.CompileError;
import control.Controller;
import control.Statistics;
import exception.MipsRuntimeException;


class CodePanel extends JPanel
{
	protected JPanel canvas ; 
	public JTabbedPane tabbedPane ;
	private TextEditorComponent textEditor ;
	private int statCount = 0 ;
	private JPanel controlPanel;
	private JSplitPane hSplit;
	
	private RegisterTable registerTable;
	
	private JSplitPane hPane;
	private JSplitPane vPane;
	private JSplitPane cPane;

	private CodeToolBar codeToolBar;
	private TextToolBar textToolBar;
	
	private JPanel textPanel;
	private JPanel tabPane;
	
	private JTabbedPane stuffPanel;
	private JPanel docPanel;
	private JTabbedPane statusPanel;
	
	private TextSegment textSegment;
	
	private FormatStats formatStats;
	private TypeStats typeStats;
	
	JMaker maker;
	
	public CodePanel(JMaker maker) {
		Runnable doWorkRunnable = new Runnable() {
		    public void run() { 
		    	vPane.setDividerLocation(0.8);
		    	hPane.setDividerLocation(0.82);
		    	cPane.setDividerLocation(0.35);
		    }
		};
		this.maker = maker;
		this.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.fill = GridBagConstraints.BOTH;
	
		
		docPanel = maker.getCanvas();

		JPanel leftPanel = new JPanel();
		leftPanel.setBackground(new Color(61,61,61));
		leftPanel.setLayout(new BorderLayout());
		stuffPanel =  new JTabbedPane(JTabbedPane.TOP);
		
		textPanel = new JPanel();
		textPanel.setBackground(Color.red);
		tabPane = new JPanel();
		stuffPanel.setMinimumSize(new Dimension(0, 0));
		docPanel.setMinimumSize(new Dimension(0, 0));
		tabPane.setBackground(new Color(61,61,61));
		textSegment = new TextSegment();
		tabPane.add(textSegment);
		stuffPanel.addTab("Memory Table", tabPane);
//		stuffPanel.setLayout(new GridBagLayout());
		cPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,docPanel, stuffPanel);
		
		cPane.setOneTouchExpandable(true);
		cPane.setDividerLocation(1.0);
		cPane.addPropertyChangeListener("dividerLocation", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if(cPane.getLeftComponent().getWidth() > 200)
					CodePanel.this.maker.refreshBounds(cPane.getLeftComponent().getWidth(), cPane.getLeftComponent().getHeight()-50);
				typeStats.updateBounds();
				formatStats.updateBounds();
			}
		});
		cPane.getRightComponent().setMaximumSize(new Dimension(500,1000));
		textToolBar = new TextToolBar(maker);
		textToolBar.setBackground(new Color(255,69,0));
		codeToolBar = new CodeToolBar();
		codeToolBar.setBackground(new Color(255,69,0));
		leftPanel.add(codeToolBar, BorderLayout.EAST);
		leftPanel.add(cPane, BorderLayout.CENTER);
		leftPanel.add(textToolBar, BorderLayout.WEST);
		
		
		statusPanel = maker.getOutputPanel();
				
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		registerTable = new RegisterTable();
		pan.add(registerTable, BorderLayout.NORTH);
		pan.add(maker.getTradeMark(), BorderLayout.SOUTH);
		
		vPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,leftPanel,statusPanel);
		hPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,vPane,pan);
		vPane.addPropertyChangeListener("dividerLocation", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				System.out.println(cPane.getLeftComponent().getWidth()+ "  " +cPane.getLeftComponent().getHeight());
//				CodePanel.this.maker.refreshBounds(cPane.getLeftComponent().getWidth(), cPane.getLeftComponent().getHeight());
			}
		});
		
		formatStats = new FormatStats();
		typeStats = new TypeStats();
		stuffPanel.addTab("Format Statistics", formatStats);
		stuffPanel.addTab("Type Statistics", typeStats);
		con.gridx = 0;
		con.gridy = 0;
		con.weightx = 1.0; 
		con.weighty = 1.0;
		this.add(hPane, con);
		addBindings();
		JMenuItem stat = maker.getStatistics();
		stat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showStatisticsStuff();	
			}
		});
		bindKeys();
		
		new Timer().schedule(new TimerTask() {
			
			@Override
			public void run() {
				textState();
				
			}
		},200);
	}
	
	private void bindKeys()
	{
		DistroyDoc DD = new DistroyDoc() ;
		KeyStroke key = KeyStroke.getKeyStroke("ctrl W");
		stuffPanel.getInputMap().put(key, "stuff");
		stuffPanel.getActionMap().put("stuff", DD);
	}
	
	private class DistroyDoc extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			stuffPanel.remove(stuffPanel.getSelectedComponent());
			System.out.println("SHIT");
		}
	}
	
	private class AssembleCode extends AbstractAction 
	{
		public void actionPerformed(ActionEvent e) {
			String s = null;
			try {
				s = maker.getTextEditor().getTextPane().getDocument().getText(0, maker.getTextEditor().getTextPane().getDocument().getLength());
			} catch (BadLocationException e1) {}
			assemble(s.split("\n"));
			fast_forwardStuff();
		}
	}
	
	private class JustAssembleCode extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			justAssembleStuff(e , maker.tabbedPane.getSelectedComponent());
		}
	}
	
	private class RunCode extends AbstractAction
	{
		public void actionPerformed(ActionEvent e) {
			runnerStuff();
		}
	}
	
	private void midState(){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				cPane.getRightComponent().setPreferredSize(new Dimension(0,0));	
				cPane.setDividerLocation(0.5);
				CodePanel.this.maker.refreshBounds(cPane.getLeftComponent().getWidth(), cPane.getLeftComponent().getHeight()-50);
				typeStats.updateBounds();
				formatStats.updateBounds();
			}
		});
	}
	
	private void textState(){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				cPane.setDividerLocation(1000);
				CodePanel.this.maker.refreshBounds(cPane.getLeftComponent().getWidth(), cPane.getLeftComponent().getHeight()-50);
			}
		});
	}
	
	private void codeState(){
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				cPane.setDividerLocation(0);
				CodePanel.this.maker.refreshBounds(cPane.getLeftComponent().getWidth(), cPane.getLeftComponent().getHeight()-50);
			}
		});
	}
	
	private void showStatisticsStuff()
	{
		StatisticsGraph SG = new StatisticsGraph(statCount) ;
		Image statisticsImage = null;
		Image typesImage = null;
		try {
			statisticsImage = ImageIO.read(new File("Resources/Charts/formatChart"+statCount+".jpg"));
			typesImage = ImageIO.read(new File("Resources/Charts/typeChart"+statCount + ".jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		JLabel formatChart = new JLabel(new ImageIcon(statisticsImage));
		JLabel typeChart = new JLabel (new ImageIcon(typesImage));
		formatChart.setBounds(50, 50, 500, 300);
		typeChart.setBounds(50, 50, 500, 300);
		stuffPanel.addTab("Instruction Format "+statCount, formatChart);
		stuffPanel.addTab("Insturction Types "+statCount, typeChart);
		this.statCount++ ;
	}
	
	private void addBindings()
	{
		AssembleCode AC = new AssembleCode();
		KeyStroke key = KeyStroke.getKeyStroke("ctrl F11");
		maker.getTextEditor().getTextPane().getInputMap().put(key, "assemble");
		maker.getTextEditor().getTextPane().getActionMap().put("assemble", AC);
		RunCode RC = new RunCode();
		key = KeyStroke.getKeyStroke("F6");
		maker.getTextEditor().getTextPane().getInputMap().put(key, "runIt");
		maker.getTextEditor().getTextPane().getActionMap().put("runIt", RC);
		JustAssembleCode JAC = new JustAssembleCode();
		key = KeyStroke.getKeyStroke("ctrl F3");
		maker.getTextEditor().getTextPane().getInputMap().put(key, "just");
		maker.getTextEditor().getTextPane().getActionMap().put("just", JAC);
	}
	
	
	public void refresh(){
		Runnable doWorkRunnable = new Runnable() {
		    public void run() { 
		    	vPane.setDividerLocation(0.8);
		    	hPane.setDividerLocation(0.82);
		    	cPane.setDividerLocation(1.0);
		    }
		};
		SwingUtilities.invokeLater(doWorkRunnable);
	}

	public void assemble(String[] lines){
		refresh();
		Controller.newInstance();
		maker.getCompileList().clear();
		CompileError[] e=Controller.getInstance().compile(lines);
		for (int i = 0; i < e.length; i++) {
			maker.getCompileList().addElement(e[i]);
			System.out.println(e[i]);
		}
		if(e.length == 0){
			textSegment.makeTable();
			midState();
		}else{
			maker.focusCompileList();
		}
		registerTable.refreshValues();
		textSegment.refresh();

	}
	
	
	private class CodeToolBar extends JPanel
	{
		JButton fast_forward ;
		JButton backward ;
		JButton runner  ;
		JButton pause ;
		JButton terminate ;
		public CodeToolBar(){
			fast_forward = new JButton(new ImageIcon(Resources.FORWARD));
			backward = new JButton(new ImageIcon(Resources.BACKWARD));
			runner = (new JButton(new ImageIcon(Resources.RUN))) ;
			pause = new JButton(new ImageIcon(Resources.PAUSE));
			terminate = new JButton(new ImageIcon(Resources.TERMINATE)) ;
			
			fast_forward.setToolTipText("End of the program");
			backward.setToolTipText("Reset Registers") ;
			runner.setToolTipText("Run the Program");
			pause.setToolTipText("Pause the Program");
			terminate.setToolTipText("Terminate") ;
			
			int size =  10;
			terminate.setSize(size,size);
			fast_forward.setSize(size,size);
			backward.setSize(size,size);
			runner.setSize(size,size);
			pause.setSize(size,size);
			
			runner.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					paused = false ;
					runnerStuff();
				}
			});
			
			fast_forward.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					paused = false;
					fast_forwardStuff();
				}
			});	
			
			pause.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					paused = true;
				}
			});
			
			backward.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Controller.getInstance().getRegisterFile().getRegister(RegisterFile.pc).setValue(Controller.codeStart);
					registerTable.refreshValues();
					textSegment.table.repaint();
				}
			});
			
			GridLayout g = new GridLayout(9, 1);
			this.setLayout(g);
			this.add(runner);
			this.add(fast_forward);
			this.add(pause);
			this.add(terminate);
			this.add(backward);
		}
		
	}
	
	private void justAssembleStuff(ActionEvent e,Component c )
	{
		String s = null;
		int index = 0 ;
		for (int i = 0 ; i < maker.getTextEditors().size();i++)
		{
			if (maker.getTextEditors().get(i) == c)
				index = i ;
		}
		try {
			s = maker.getTextEditors().get(index).getTextPane().getDocument().getText(0, maker.getTextEditors().get(index).getTextPane().getDocument().getLength());
		} catch (BadLocationException e1) {}
		assemble(s.split("\n"));	
	}
	
	boolean paused = false;
	private void fast_forwardStuff()
	{
		new Thread(){
			public void run() {
				try{
					while(!paused && Controller.getInstance().executeSimpleInstruction()){
						registerTable.refreshValues();
						textSegment.table.repaint();
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {}
						registerTable.refreshValues();
						textSegment.table.repaint();
						formatStats.refresh();
						typeStats.refresh();
					}
				}catch(MipsRuntimeException e){
					maker.getRuntimeList().addElement(e.getMessage());
					maker.focusCompileList();
					registerTable.refreshValues();
					textSegment.table.repaint();
					
				}
			}
	}.start();
	}
	
	private void runnerStuff()
	{
		try{
			Controller.getInstance().executeSimpleInstruction();
		}catch(MipsRuntimeException e){
			maker.getRuntimeList().addElement(e.getMessage());
			maker.focusRuntimeList();
		}
		registerTable.refreshValues();
		textSegment.table.repaint();
		formatStats.refresh();
		typeStats.refresh();
	}
	
	private class TextToolBar extends JPanel
	{
		boolean paused = false;
		public TextToolBar(final JMaker maker){
			GridLayout g = new GridLayout(9, 1);
			this.setLayout(g);
			for (JButton button : maker.getButtons()) {
				this.add(button);
			}
			
			JButton assemble = maker.getAssembleButton();
			assemble.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String s = null;
					int index = 0 ;
					for (int i = 0 ; i < maker.getTextEditors().size();i++)
						if (maker.getTextEditors().get(i) == maker.tabbedPane.getSelectedComponent())
							index = i ;
					try {
						s = maker.getTextEditors().get(index).getTextPane().getDocument().getText(0, maker.getTextEditors().get(index).getTextPane().getDocument().getLength());
						assemble(s.split("\n"));
					} catch (BadLocationException e1) {
						e1.printStackTrace();
					}
				}
				
			});
		}
	}
	

	public static void main(String[] args) {
		   for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels())
			    {
			     if("Nimbus".equals(info.getName()))
			     {
			      try {
					UIManager.setLookAndFeel (info.getClassName() );
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException | UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
			      break ;
			     }
			    }
		Frame frame = new Frame();
	}
}