package de.dengot.hl2stats.ui;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.jfree.chart.JFreeChart;

public class ChartControlListener implements ControlListener
{
	private Label chartLayer;

	private JFreeChart chart;

	public static Image createChartImage(Control parent, JFreeChart chart,
			int width, int height)
	{
		// Color adjustment
		Color swtBackground = parent.getBackground();
		java.awt.Color awtBackground = new java.awt.Color(swtBackground
				.getRed(), swtBackground.getGreen(), swtBackground.getBlue());
		chart.setBackgroundPaint(awtBackground);

		// Draw the chart in an AWT buffered image
		BufferedImage bufferedImage = chart.createBufferedImage(width, height,
				null);

		// Get the data buffer of the image
		DataBuffer buffer = bufferedImage.getRaster().getDataBuffer();
		DataBufferInt intBuffer = (DataBufferInt) buffer;

		// Copy the data from the AWT buffer to a SWT buffer
		PaletteData paletteData = new PaletteData(0x00FF0000, 0x0000FF00,
				0x000000FF);
		ImageData imageData = new ImageData(width, height, 32, paletteData);
		for (int bank = 0; bank < intBuffer.getNumBanks(); bank++)
		{
			int[] bankData = intBuffer.getData(bank);
			imageData.setPixels(0, bank, bankData.length, bankData, 0);
		}

		// Create an SWT image
		return new Image(parent.getDisplay(), imageData);
	}

	public ChartControlListener(Label chartLayer, JFreeChart chart)
	{
		this.chart = chart;
		this.chartLayer = chartLayer;
	}

	public void setChart(JFreeChart chart)
	{
		this.chart = chart;
	}

	public void controlMoved(ControlEvent e)
	{
	}

	public void controlResized(ControlEvent e)
	{
		if (chart != null)
		{
			Point p = ((Control) e.getSource()).getSize();
			this.chartLayer.setImage(createChartImage(this.chartLayer,
					this.chart, p.x, p.y));
		}
	}

}
