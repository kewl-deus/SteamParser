package de.dengot.steamparser.online.forms;

import org.apache.struts.action.ActionForm;

import de.dengot.steamparser.model.StatsType;

public class StatsTypeSelectionForm extends ActionForm
{

    private int ordinalOfTypeToShow = StatsType.FRAGS.ordinal();

    public StatsTypeSelectionForm()
    {
    }

    public int getOrdinalOfTypeToShow()
    {
        return ordinalOfTypeToShow;
    }

    public void setOrdinalOfTypeToShow(int ordinalOfTypeToShow)
    {
        this.ordinalOfTypeToShow = ordinalOfTypeToShow;
    }

    public StatsType getTypeToShow()
    {
        for (StatsType t : StatsType.values())
        {
            if (t.ordinal() == this.ordinalOfTypeToShow)
            {
                return t;
            }
        }
        // default
        return StatsType.FRAGS;
    }

}
