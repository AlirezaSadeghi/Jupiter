package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PieChart extends JPanel
{
	public PieChart(){
	}
	public PieChart(int[] values, String[] names){
		this.values = values;
		this.names = names;
	}
	
	int[] values;
	String[] names;
	public void setData(int[] values, String[] names){
		this.values = values;
		this.names = names;
	}
	public void setData(int[] values){
		this.values = values;
	}
	static Color[] colors = {Color.red, Color.blue, Color.green, Color.orange, Color.yellow};
	public void paint(Graphics graphics) {
		super.paint(graphics);
		if(values == null || names == null)
			return;
		Graphics2D g = (Graphics2D)graphics;
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	    g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		int sum = 0;
		for (int val : values) 
			sum += val;
		
		int last = 0;
		for (int i = 0; i < values.length; i++) {
			int start = last ;
			int end = (int)(360*(double)values[i]/sum); 
			if(i == values.length-1)
				end = 360 - start;
			last = start + end;
			g.setColor(colors[i]);
			g.fillArc(0, 0, getWidth()-180, getHeight()-180, start, end);
		}
		
		int x = getWidth() - 130;
		int y = 50;
		int size = 20;
		int gap = 5;
		for (int i = 0; i < values.length; i++) {
			g.setColor(colors[i]);
			g.fillRect(x, y + i*(size + gap) - size/2 , size, size);
			g.setColor(Color.black);
			g.drawString(names[i] + ": " + values[i], x + size + 10, y + i*(size + gap) + 3);
		}
	}
	
//	public static void main(String[] args) {
//		JFrame f = new JFrame();
//		f.setBounds(10, 10, 500, 500);
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		ArrayList<Integer> val = new ArrayList<Integer>();
//		ArrayList<String> names= new ArrayList<String>();
//		val.add(10);
//		val.add(20);
//		val.add(40);
//		names.add("IFormat");
//		names.add("RFormat");
//		names.add("JFormat");
//		PieChart p = new PieChart(val,names);
//		f.add(p);
//		f.setVisible(true);
//		while(val.get(1) < 2000){
//			val.set(1, val.get(1)+1);
//			p.repaint();
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
}
