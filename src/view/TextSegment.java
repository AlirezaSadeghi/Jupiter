package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import model.Register;
import model.RegisterFile;
import model.Word;
import model.instruction.Instruction;
import control.BreakPoint;
import control.Controller;
import exception.BadInstructionCodeException;

class TextSegment extends JScrollPane
{
	JTable table = null;

	public TextSegment() {
		makeTable();
	}

	int currentRow = 1;
	boolean listen = true;
	boolean[] breaks;
	public void makeTable() {
		ArrayList<Word> addresses = new ArrayList<Word>();
		ArrayList<Word> codes = new ArrayList<Word>();

		Word word = Controller.codeStart;
		int count = 0;
		while (word.getValue() < Controller.getInstance().codeAddress.getValue()) {
			count++;
			addresses.add(word);
			codes.add(Controller.getInstance().getMainMemory().getWord(word));
			word = word.add(4);
		}

		String[] head = { "", "Address", "Code", "Assembly" };
		Object[][] data = new Object[count][4];
		for (int i = 0; i < count; i++) {
			JPanel p = new JPanel();
			data[i][0] = "";
			data[i][1] = addresses.get(i);
			data[i][2] = codes.get(i);
			try {
				Instruction ins = Instruction.decodeInstruction(codes.get(i));
				data[i][3] = ins.toString();
			} catch (BadInstructionCodeException e) {
				data[i][3] = "---";
			}
		}
		breaks = new boolean[count];
		for (int i = 0; i < breaks.length; i++) 
			breaks[i] = false;
		
		table = new JTable(new CustomModel(data, head));
		

		table.addMouseListener(new MouseListen());
		this.setViewportView(table);
		table.setDefaultRenderer(Object.class, new CustomRenderer());
		table.setFillsViewportHeight(true);
		table.setFont(new Font(Font.MONOSPACED, table.getFont().getStyle(), table.getFont().getSize()));
		table.getColumnModel().getColumn(0).setResizable(false);
		table.getColumnModel().getColumn(0).setMaxWidth(16);
	}

	public void refresh() {
		table.repaint();
	}

	class MouseListen extends MouseAdapter
	{
		public void mouseClicked(MouseEvent e) {
			 JTable target = (JTable)e.getSource();
		     int col = target.getSelectedColumn();
		     if(col != 0)
		    	 return;
		     int row = target.getSelectedRow();
		     if(e.getClickCount() == 2){
			     Word address = new Word(table.getValueAt(row, 1).toString());
			     breaks[row] = Controller.getInstance().toggleBreakPoint(new BreakPoint(address));
			     target.repaint();
		     }else if(e.getButton() == MouseEvent.BUTTON2){
		    	 Word address = new Word(table.getValueAt(row, 1).toString());
		    	 String s = "";
		    	 BreakPoint b = null;
		    	 if(breaks[row]){
		    		 b = Controller.getInstance().getBreakPoint(address);
		    		 if(b != null)
		    			 s = b.getCondition();
		    	 }
		    	 s = JOptionPane.showInputDialog("Enter Condition:",s);
		    	 if(b == null)
		    		 b = new BreakPoint(address,s);
		    	 else
		    		 b.setCondition(s);
		    	 if(!breaks[row])
		    		 breaks[row] = Controller.getInstance().toggleBreakPoint(b);
		    	 target.repaint();
		     }
		}
	}

	class CustomModel extends DefaultTableModel implements TableModelListener
	{
		public CustomModel(Object[][] data, Object[] head) {
			super(data,head);
			this.addTableModelListener(this);
		}
		public boolean isCellEditable(int row, int column) {
			return column == 2;
		}
		
		public void tableChanged(TableModelEvent e) {
			if (!listen)
				return;
			listen = false;
			int row = e.getFirstRow();
			int column = e.getColumn();
			TableModel model = (TableModel) e.getSource();
			String columnName = model.getColumnName(column);
			String code = (String) model.getValueAt(row, 1);
			Word address = (Word) model.getValueAt(row, 0);
			try {
				Instruction ins = Instruction.decodeInstruction(new Word(code));
				Controller.getInstance().getMainMemory().setWord(address, new Word(code));
				table.setValueAt(ins.toString(), row, 2);
			} catch (BadInstructionCodeException ex) {
				table.setValueAt("---", row, 2);
			}
			listen = true;
		}
	}
	class CustomRenderer extends DefaultTableCellRenderer
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, final int row, int column) {
			Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			Register pc = Controller.getInstance().getRegisterFile().getRegister(RegisterFile.pc);
			
			if (table.getModel().getValueAt(row, 1).toString().equals(pc.getHexValue())) {
				comp.setForeground(Color.BLUE);
			} else {
				comp.setForeground(null);
			}

			if(column == 0 ){
				JPanel panel = new JPanel(){
					public void paint(Graphics g) {
						Graphics2D g2 = (Graphics2D)g;
						g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
					    g2.setRenderingHint(RenderingHints.KEY_RENDERING,
					          RenderingHints.VALUE_RENDER_QUALITY);
						g2.setColor(new Color(150,10,10));
						if(breaks[row])
							g2.fillOval(3, 3, 10, 10);
					}
				};
				return panel;
			}
			return (comp);
		}
	}

}