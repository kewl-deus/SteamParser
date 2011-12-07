package de.dengot.hl2stats.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ComboLabelProvider extends LabelProvider
{
	public Image getImage(Object element)
	{
		return null;
	}

	public String getText(Object element)
	{
		return element == null ? "" : element.toString();
	}
}
