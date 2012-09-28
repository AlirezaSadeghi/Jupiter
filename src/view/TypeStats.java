package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import control.Controller;
import control.Statistics;

public class TypeStats extends JPanel
{
	PieChart chart;
	public TypeStats(){
		chart = new PieChart(null, Statistics.getTypeNameArray());
		chart.setBounds(0,0,500,500);
		this.add(chart, BorderLayout.CENTER);
		this.setLayout(null);
	}
	
	public void refresh(){
		Statistics stat = Controller.getInstance().getStat();
		chart.setData(stat.getTypeValArray());
		updateBounds();
		chart.repaint();
	}
	
	public void updateBounds(){
		if(getWidth() < 500)
			chart.setBounds(getWidth()/2 - 200, getHeight()/2-200, 400,400);
		else if(getWidth() < 700)
			chart.setBounds(getWidth()/2 - 250, getHeight()/2-200, 500,500);
		else
			chart.setBounds(getWidth()/2 - 300, getHeight()/2-230, 650,650);
	}
}
