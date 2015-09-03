import java.awt.Color;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;


public class GraphResult extends ApplicationFrame {

   
    public GraphResult(final String title, ArrayList<Double> firstLine, String firstLineTitle, ArrayList<Double> secondLine, String secondLineTitle, ArrayList<Double> thirdLine, String thirdLineTitle, ArrayList<Double> fourthLine, String fourthLineTitle) 
    {
    	super("MyGraph");
        final XYDataset dataset = createDataset(firstLine, firstLineTitle, secondLine, secondLineTitle, thirdLine, thirdLineTitle, fourthLine, fourthLineTitle);
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    public GraphResult(final String title, ArrayList<Double> firstLine, String firstLineTitle, ArrayList<Double> secondLine, String secondLineTitle, ArrayList<Double> thirdLine, String thirdLineTitle) 
    {
    	super("MyGraph");
        final XYDataset dataset = createDataset(firstLine, firstLineTitle, secondLine, secondLineTitle, thirdLine, thirdLineTitle);
        final JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }
    
    private XYDataset createDataset(ArrayList<Double> firstLine, String firstLineTitle, ArrayList<Double> secondLine, String secondLineTitle, ArrayList<Double> thirdLine, String thirdLineTitle,ArrayList<Double> fourthLine, String fourthLineTitle) {
        
        final XYSeries series1 = new XYSeries(firstLineTitle);
        
        for (int i=0; i <firstLine.size(); i++)
            series1.add(i, firstLine.get(i));
        
        final XYSeries series2 = new XYSeries(secondLineTitle);
        
        for (int i=0; i <secondLine.size(); i++)
            series2.add(i, secondLine.get(i));
        
        final XYSeries series3 = new XYSeries(thirdLineTitle);
        
        for (int i=0; i <thirdLine.size(); i++)
            series3.add(i, thirdLine.get(i));
        
        final XYSeries series4 = new XYSeries(fourthLineTitle);
        
        for (int i=0; i <fourthLine.size(); i++)
            series4.add(i, fourthLine.get(i));


        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
        dataset.addSeries(series4);
                
        return dataset;        
    }
    
private XYDataset createDataset(ArrayList<Double> firstLine, String firstLineTitle, ArrayList<Double> secondLine, String secondLineTitle, ArrayList<Double> thirdLine, String thirdLineTitle) 
{
        
        final XYSeries series1 = new XYSeries(firstLineTitle);
        
        for (int i=0; i <firstLine.size(); i++)
            series1.add(i, firstLine.get(i));
        
        final XYSeries series2 = new XYSeries(secondLineTitle);
        
        for (int i=0; i <secondLine.size(); i++)
            series2.add(i, secondLine.get(i));
        
        final XYSeries series3 = new XYSeries(thirdLineTitle);
        
        for (int i=0; i <thirdLine.size(); i++)
            series3.add(i, thirdLine.get(i));
        

        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
        dataset.addSeries(series2);
        dataset.addSeries(series3);
                
        return dataset;        
    }
    
    private JFreeChart createChart(XYDataset dataset) {
        
        // create the chart...
        final JFreeChart chart = ChartFactory.createXYLineChart(
            "My Chart",      // chart title
            "X",                      // x axis label
            "Y",                      // y axis label
            dataset,                  // data
            PlotOrientation.VERTICAL,
            true,                     // include legend
            true,                     // tooltips
            false                     // urls
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.white);
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(0, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.               
        return chart;
        
    }
 }


