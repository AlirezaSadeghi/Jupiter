package view ;

import java.io.File;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import control.Controller;
import control.Statistics;

public class StatisticsGraph
{
	private int count ; 
	private Statistics stat ;
	public StatisticsGraph(int count)
	{
		this.count = count ;
		drawGraph();
	}
	
	private void drawGraph(){
		this.stat  = Controller.getInstance().getStat();

		DefaultPieDataset dpd = new DefaultPieDataset();
		JFreeChart chart = ChartFactory.createPieChart("Instruction Format Statistics", dpd, true, true, false);
		dpd.setValue("I-Format Instructions", stat.getIFormatNumber()) ;
		dpd.setValue("J-Format Instructions", stat.getJFormatNumber()) ;
		dpd.setValue("R-Format Instructions", stat.getRFormatNumber()) ;
		
		DefaultPieDataset types = new DefaultPieDataset();
		JFreeChart tChart = ChartFactory.createPieChart("Instruction Type Statistics", types, true, true, false);
		types.setValue("ALU Instructions", stat.getALUNumber()) ;
		types.setValue("Branch Instructions", stat.getBranchNumber());
		types.setValue("Jump Instructions", stat.getJumpNumber());
		types.setValue("Memory Instructions", stat.getMemoryNumber());
		types.setValue("Other Instructions", stat.getOtherNumber());
		
		
		try{
			ChartUtilities.saveChartAsJPEG(new File("Resources/Charts/formatChart" + this.count + ".jpg"), chart, 500, 300);
			ChartUtilities.saveChartAsJPEG(new File("Resources/Charts/typeChart" + this.count + ".jpg"), tChart, 500, 300);
		}catch(Exception e )
		{
			e.printStackTrace();
		}
	}
	
}