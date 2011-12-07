package de.dengot.hl2stats;
import java.awt.Frame;
import java.awt.GridLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.DefaultPieDataset;

/**
 * Simple SWT demonstration.
 * http://www.jfree.org/phpBB2/viewtopic.php?t=4693&highlight=swt
 * @author Robert Gash (gashalot [at] gashalot.com)
 */
public class JFreeChartSWTDemo
{
	/**
	 * Simple driving main, a quick and dirty intro to SWT_AWT using JFreeChart.
	 * 
	 * @param args
	 *            command line arguments (ignored)
	 */
	public static void main(String[] args)
	{
		// create the SWT display and the new window (Shell)
		Display display = new Display();
		Shell shell = new Shell(display);

		// Set the layout to a simple FillLayout
		FillLayout layout = new FillLayout();
		layout.type = SWT.VERTICAL;
		shell.setLayout(layout);

		// Create the JFreeChart data
		DefaultPieDataset pieData = new DefaultPieDataset();
		pieData.setValue("Slice 1", 50);
		pieData.setValue("Slice 2", 70);
		pieData.setValue("Slice 3", 90);
		pieData.setValue("Slice 4", 20);

		// Create the actuall JFreeChart chart
		JFreeChart chart = ChartFactory.createPieChart3D("Sample Chart",
				pieData, true, true, false);

		// grab a new AWT frame from our shell
		Frame chartFrame = SWT_AWT.new_Frame(shell);

		// set the layout of our frame to a GridLayout so the chart will
		// automatically fill the entire area
		chartFrame.setLayout(new GridLayout());
		ChartPanel cp = new ChartPanel(chart);
		chartFrame.add(cp);

		// pack the shell, set the size to a reasonable default, and show it
		shell.pack();
		shell.setSize(400, 400);
		shell.open();

		// standard SWT dispose loop
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();

	}
}
