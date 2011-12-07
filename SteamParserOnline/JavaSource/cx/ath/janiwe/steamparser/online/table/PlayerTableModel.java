/*
 * Created on 12.10.2005
 */
package cx.ath.janiwe.steamparser.online.table;

import java.util.Iterator;
import java.util.List;

import cx.ath.janiwe.steamparser.stats.PlayerStats;
import cx.ath.janiwe.util.web.SimpleTableModel;

public class PlayerTableModel extends SimpleTableModel
{

    public PlayerTableModel(List<PlayerStats> stats)
    {
        Iterator<PlayerStats> it = stats.iterator();
        for (int i = 1; it.hasNext(); i++)
        {
            super.addRow(new PlayerTableRow(i, it.next()));
        }
        super.addSortableColumn("rang");
        super.addSortableColumn("name");
        super.addSortableColumn("kills");
        super.addSortableColumn("deaths");
        super.addSortableColumn("headshots");
        super.addSortableColumn("killsPerDeathAsString");
        super.addSortableColumn("sessionsPlayed");
        super.addSortableColumn("hsPercentImage");
    }

    public PlayerTableRow getRow(int node)
    {
        return (PlayerTableRow) super.getRow(node + "");
    }

}
