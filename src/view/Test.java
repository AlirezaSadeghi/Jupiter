package view;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;

public class Test extends JFrame
{
	public Test(){
		this.setBounds(100,100,600,600);
		
		JTextPane t = new JTextPane();
		t.setBounds(10,10,200,200);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.add(t);
		
		this.setVisible(true);
		
		Action aa = new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("cOntrOl sPacE");
			}
		};
		
		t.getInputMap().put(KeyStroke.getKeyStroke("control SPACE"), "salam");
		t.getActionMap().put("salam", aa);
		
	}
	
	public static void main(String[] args) {
		new Test();
	}
	
	
}
