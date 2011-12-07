package de.dengot.hl2stats.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class HL2StatsPerspective implements IPerspectiveFactory
{

    public static final String ID = "de.dengot.hl2stats.ui.HL2StatsPerspective";

    public void createInitialLayout(IPageLayout layout)
    {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        layout.addStandaloneView(LogfileBrowser.ID, false, IPageLayout.LEFT,
                0.25f, editorArea);
        layout.addStandaloneView(ChartConfigView.ID, false, IPageLayout.TOP, 0.1f, editorArea);
        layout.addStandaloneView(ChartView.ID, false, IPageLayout.RIGHT, 0.75f, editorArea);
        /*
        IFolderLayout folder = layout.createFolder("visuals", IPageLayout.BOTTOM,
                0.5f, editorArea);
        folder.addPlaceholder(ChartView.ID + ":*");
        folder.addView(ChartView.ID);
        */

        layout.getViewLayout(LogfileBrowser.ID).setCloseable(false);
        layout.getViewLayout(ChartConfigView.ID).setCloseable(false);
    }
}
