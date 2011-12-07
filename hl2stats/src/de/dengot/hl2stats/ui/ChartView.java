package de.dengot.hl2stats.ui;

import java.awt.Frame;
import java.awt.GradientPaint;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPosition;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.CategoryItemRenderer;
import org.jfree.data.CategoryDataset;
import org.jfree.data.DefaultCategoryDataset;
import org.jfree.text.TextBlockAnchor;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

public class ChartView extends ViewPart
{
	public static final String ID = "de.dengot.hl2stats.ui.ChartView";

	private Composite chartComposite;

	private Label chartLabel;

	private JFreeChart chart;
	
	private ChartControlListener chartPainter;

	public void createPartControl(Composite parent)
	{
		chartComposite = new Composite(parent, SWT.EMBEDDED);
		// Set the layout to a simple FillLayout
		FillLayout chartLayout = new FillLayout();
		chartLayout.type = SWT.VERTICAL;
		chartComposite.setLayout(chartLayout);
		chartLabel = new Label(this.chartComposite, SWT.NONE);
		this.chartPainter = new ChartControlListener(this.chartLabel, null);
		this.chartComposite.addControlListener(this.chartPainter);
	}

	private void drawChart()
	{
		if (this.chart == null)
		{
			DefaultCategoryDataset dummySet = new DefaultCategoryDataset();
			this.chart = createChart(dummySet, "Deaths/Kills", "Frags",
					"Weapon");
		}

		// grab a new AWT frame from our shell
		Frame chartFrame = SWT_AWT.new_Frame(chartComposite);

		// set the layout of our frame to a GridLayout so the chart will
		// automatically fill the entire area
		chartFrame.setLayout(new java.awt.GridLayout());
		ChartPanel chartPanel = new ChartPanel(chart);
		chartFrame.add(chartPanel);

	}

	
	public void setFocus()
	{
		this.chartComposite.setFocus();
	}

	public void visualize(CategoryDataset dataset, String title,
			String xAxisTitle, String yAxisTitle)
	{
		this.chart = createChart(dataset, title, xAxisTitle, yAxisTitle);
		Point p = chartLabel.getSize();
		this.chartLabel.setImage(ChartControlListener.createChartImage(chartLabel, chart, p.x, p.y));
		this.chartPainter.setChart(this.chart);
	}

	private JFreeChart createChart(CategoryDataset dataset, String title,
			String xAxisTitle, String yAxisTitle)
	{

		JFreeChart chart = ChartFactory.createBarChart3D(title, // chart
				// title
				yAxisTitle, // domain axis label
				xAxisTitle, // range axis label
				dataset, // data
				PlotOrientation.HORIZONTAL, // orientation
				false, // include legend
				true, // tooltips
				false // urls
				);

		CategoryPlot plot = chart.getCategoryPlot();
		plot.setForegroundAlpha(1.0f);

		// left align the category labels...
		CategoryAxis axis = plot.getDomainAxis();
		CategoryLabelPositions p = axis.getCategoryLabelPositions();

		CategoryLabelPosition left = new CategoryLabelPosition(
				RectangleAnchor.LEFT, TextBlockAnchor.CENTER_LEFT,
				TextAnchor.CENTER_LEFT, 0.0);
		axis.setCategoryLabelPositions(CategoryLabelPositions
				.replaceLeftPosition(p, left));

		axis.setMaxCategoryLabelWidthRatio(3.0f);

		// Plot Values
		CategoryItemRenderer renderer = plot.getRenderer();
		renderer.setItemLabelsVisible(true);

		// set up gradient paints for series...
		GradientPaint paintGiven = new GradientPaint(0.0f, 0.0f,
				java.awt.Color.green, 0.0f, 0.0f, java.awt.Color.blue);
		GradientPaint paintTaken = new GradientPaint(0.0f, 0.0f,
				java.awt.Color.red, 0.0f, 0.0f, java.awt.Color.orange);

		renderer.setSeriesPaint(0, java.awt.Color.GREEN);
		renderer.setSeriesPaint(1, java.awt.Color.RED);

		// renderer.setSeriesPaint(0, paintGiven);
		// renderer.setSeriesPaint(1, paintTaken);

		return chart;

	}

}
