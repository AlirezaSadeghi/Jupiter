package view;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import model.RegisterFile;
import model.Word;
import control.Controller;

public class RegisterTable extends JScrollPane implements TableModelListener
{
	private static String [] names = {"Name" , "Number", "Value"} ;
	private static Object [][] attrs = new Object[RegisterFile.registerCount][3];
	static{
		for (int i = 0; i < 32; i++) {
			attrs[i][0] ="$"+RegisterFile.registerNames[i];
			attrs[i][1] =  new Integer(i);
			attrs[i][2] =  new String("0x00000000");
		}
		for (int i = 32; i < RegisterFile.registerCount; i++) {
			attrs[i][0] = RegisterFile.registerNames[i];
			attrs[i][1] =  new Integer(i);
			attrs[i][2] =  new String("0x00000000");
		}
	}
	
	JTable table;
	boolean listen = true;
	
	public RegisterTable(){
		table = new JTable(new CustomModel(attrs, names));
		table.getModel().addTableModelListener(this);
//		this.setBorder(BorderFactory.createEtchedBorder(1));
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		TableColumn col = table.getColumnModel().getColumn(0);
		col.setPreferredWidth(50);
		
		this.setViewportView(table);
		table.setFillsViewportHeight(true);
	}
	
	public void refreshValues(){
		listen = false;
		for (int i = 0; i < RegisterFile.registerCount; i++) {
			table.setValueAt(Controller.getInstance().getRegisterFile().getRegister(i).getHexValue(), i,2);
		}
		
		listen = true;
	}
	
    public void tableChanged(TableModelEvent e) {
    	if(!listen)
    		return;
        int row = e.getFirstRow();
        int column = e.getColumn();
        TableModel model = (TableModel)e.getSource();
        String columnName = model.getColumnName(column);
        Object data = model.getValueAt(row, column);
        Word word = null;
        try{
        	word = new Word((String)data);
        }catch(NumberFormatException ex){
        	word = Controller.getInstance().getRegisterFile().getRegister(row);
        }
        Controller.getInstance().getRegisterFile().getRegister(row).setValue(word);
        refreshValues();
    }
    
    class CustomModel extends DefaultTableModel
	{
		public CustomModel(Object[][] data, Object[] head) {
			super(data,head);
		}
		public boolean isCellEditable(int row, int column) {
			return column == 2;
		}
	}
	
}
