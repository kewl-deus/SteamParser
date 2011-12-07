/*
 * Created on 12.10.2005
 */
package cx.ath.janiwe.steamparser.online.table;

import java.util.Iterator;
import java.util.List;

import cx.ath.janiwe.steamparser.stats.MapStats;
import cx.ath.janiwe.util.web.SimpleTableModel;

public class MapTableModel extends SimpleTableModel
{

    public MapTableModel(List<MapStats> statList)
    {
        Iterator<MapStats> it = statList.iterator();
        for (int i = 1; it.hasNext(); i++)
        {
            super.addRow(new MapTableRow(i, it.next()));
        }
        super.addSortableColumn("rang");
        super.addSortableColumn("name");
        super.addSortableColumn("kills");
        super.addSortableColumn("headshots");
        super.addSortableColumn("damageGiven");
        super.addSortableColumn("hsPercentImage");

    }

    public MapTableRow getRow(int node)
    {
        return (MapTableRow) super.getRow(node + "");
    }
}
