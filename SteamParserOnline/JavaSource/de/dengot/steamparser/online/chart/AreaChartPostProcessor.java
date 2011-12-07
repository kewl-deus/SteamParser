package de.dengot.steamparser.online.chart;

import java.util.Map;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.ui.RectangleInsets;
import org.jfree.util.SortOrder;

import de.laures.cewolf.ChartPostProcessor;

public class AreaChartPostProcessor implements ChartPostProcessor
{

    public AreaChartPostProcessor()
    {
    }

    public void processChart(Object chart, Map params)
    {
        JFreeChart jfreechart = (JFreeChart) chart;
        // jfreechart.setBackgroundPaint(java.awt.Color.lightGray);

        CategoryPlot categoryplot = jfreechart.getCategoryPlot();

        
        //categoryplot.setColumnRenderingOrder(SortOrder.DESCENDING);
        categoryplot.setRowRenderingOrder(SortOrder.ASCENDING);

        categoryplot.setForegroundAlpha(0.75F);
        categoryplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        categoryplot.setBackgroundPaint(java.awt.Color.white);
        categoryplot.setDomainGridlinesVisible(true);
        // categoryplot.setDomainGridlinePaint(java.awt.Color.black);
        categoryplot.setRangeGridlinesVisible(true);
        // categoryplot.setRangeGridlinePaint(java.awt.Color.black);

        CategoryAxis categoryaxis = categoryplot.getDomainAxis();
        categoryaxis
                .setCategoryLabelPositions(org.jfree.chart.axis.CategoryLabelPositions.UP_45);
        categoryaxis.setLowerMargin(0.0D);
        categoryaxis.setUpperMargin(0.0D);

        NumberAxis numberaxis = (NumberAxis) categoryplot.getRangeAxis();
        numberaxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        numberaxis.setLabelAngle(0.0D);
    }

}
