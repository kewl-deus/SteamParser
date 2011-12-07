/*
 * Created on 12.10.2005
 */
package cx.ath.janiwe.steamparser.online.table;

import java.util.List;

import cx.ath.janiwe.steamparser.stats.GameStats;
import cx.ath.janiwe.util.web.SimpleTableModel;

public class SessionTableModel extends SimpleTableModel
{

    public SessionTableModel(List<GameStats> statList)
    {
        for (GameStats s : statList)
        {
            super.addRow(new SessionTableRow(s));
        }
        super.addSortableColumn("duration");
        super.addSortableColumn("kills");
        super.addSortableColumn("headshots");
        super.addSortableColumn("damageGiven");
        super.addSortableColumn("hsPercentImage");
    }

    public SessionTableRow getRow(int node)
    {
        return (SessionTableRow) super.getRow(node + "");
    }

}
