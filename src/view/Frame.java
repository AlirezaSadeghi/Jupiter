package view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Frame extends JFrame
{
	private JPanel toolBar;
	CodePanel codePanel;
	
	public Frame(){
		JMaker maker = new JMaker();
		TextEditorComponent.init();
		
		this.setJMenuBar(maker.getMenus());
		codePanel = new CodePanel(maker);
		
//		codePanel.setBackground(new Color(160,80,14));
		this.add(codePanel);
		this.setVisible(true);
		this.pack();
		
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		 
		this.setBounds(100, 0, 1000, 830);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setName("Jupiter Emulator");
		

		codePanel.refresh();
		
//		String[] lines = { "addi $s1, $0, 0x23", "add $s0, $s1, $s2",".word 0x100"};
//		codePanel.assemble(lines);
		
	}
	
	
	
}