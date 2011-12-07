package de.dengot.hl2stats.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class ChartConfigView extends ViewPart
{

	public static final String ID = "de.dengot.hl2stats.ui.ChartConfigView";

	private Composite top = null;

	private Label xAxisLabel = null;

	private ComboViewer xAxisCombo = null;

	private Label yAxisLabel = null;

	private ComboViewer yAxisCombo = null;

	private List<ComboViewer> mutableCombos = new ArrayList<ComboViewer>();

	private List<Label> mutableLabels = new ArrayList<Label>();

	private final int NUM_BOXES = 3;

	@Override
	public void createPartControl(Composite parent)
	{
		top = new Composite(parent, SWT.NONE);
		GridLayout gridLayout1 = new GridLayout();
		gridLayout1.makeColumnsEqualWidth = true;
		gridLayout1.numColumns = 2 + NUM_BOXES;
		top.setLayout(gridLayout1);

		// 1.row: the labels

		xAxisLabel = new Label(top, SWT.NONE);
		xAxisLabel.setText("xAxis");
		xAxisLabel.setLayoutData(createGridData());

		yAxisLabel = new Label(top, SWT.NONE);
		yAxisLabel.setText("yAxis");
		yAxisLabel.setLayoutData(createGridData());
		
		for (int i = 0; i < NUM_BOXES; i++)
		{
			Label mutableLabel = new Label(top, SWT.NONE);
			mutableLabel.setText("mutableLabel" + i);
			mutableLabel.setLayoutData(createGridData());
			mutableLabels.add(mutableLabel);
		}
		
		// 2.row : the combos
		createXAxisCombo(createGridData());
		createYAxisCombo(createGridData());

		for (int i = 0; i < NUM_BOXES; i++)
		{
			mutableCombos.add(createMutableCombo(createGridData()));
		}
	}

	private GridData createGridData()
	{
		GridData gridData = new org.eclipse.swt.layout.GridData();
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.CENTER;
		return gridData;
	}

	@Override
	public void setFocus()
	{
	}

	/**
	 * This method initializes xAxisCombo
	 * 
	 */
	private void createXAxisCombo(GridData gd)
	{
		xAxisCombo = new ComboViewer(top, SWT.NONE);
		xAxisCombo.getCombo().setLayoutData(gd);
	}

	/**
	 * This method initializes yAxisCombo
	 * 
	 */
	private void createYAxisCombo(GridData gd)
	{
		yAxisCombo = new ComboViewer(top, SWT.NONE);
		yAxisCombo.getCombo().setLayoutData(gd);
		yAxisCombo.setLabelProvider(new ComboLabelProvider());

		// yAxisCombo.addSelectionListener(new
		// org.eclipse.swt.events.SelectionListener()
		// {
		// public void widgetSelected(org.eclipse.swt.events.SelectionEvent e)
		// {
		// int idx = 0;
		// for(String s : dataColumns)
		// {
		// if (! s.equals(yAxisCombo.getText()))
		// {
		// mutableLabels[idx].setText(s);
		// //mutableCombos[idx]
		// idx++;
		// }
		// }
		// }
		// public void
		// widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e)
		// {
		// }
		// });
	}

	/**
	 * This method initializes mutableCombo1
	 * 
	 */
	private ComboViewer createMutableCombo(GridData gd)
	{
		ComboViewer mutableCombo = new ComboViewer(top, SWT.NONE);
		mutableCombo.getCombo().setLayoutData(gd);
		return mutableCombo;
	}

} // @jve:decl-index=0:visual-constraint="8,22"
