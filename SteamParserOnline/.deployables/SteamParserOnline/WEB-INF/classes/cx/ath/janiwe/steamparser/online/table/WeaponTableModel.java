/*
 * Created on 12.10.2005
 */
package cx.ath.janiwe.steamparser.online.table;

import java.util.Iterator;
import java.util.List;

import cx.ath.janiwe.steamparser.stats.WeaponStats;
import cx.ath.janiwe.util.web.SimpleTableModel;

public class WeaponTableModel extends SimpleTableModel
{

    public WeaponTableModel(List<WeaponStats> statList)
    {
        int totalKills = 0;
        int totalHeadshots = 0;
        for (WeaponStats s : statList)
        {
            totalKills += s.getKills();
            totalHeadshots += s.getHeadshots();
        }
        Iterator<WeaponStats> it = statList.iterator();
        for (int i = 1; it.hasNext(); i++)
        {
            super.addRow(new WeaponTableRow(i, it.next(), totalKills,
                    totalHeadshots));
        }
        super.addSortableColumn("rang");
        super.addSortableColumn("weapon");
        super.addSortableColumn("kills");
        super.addSortableColumn("percentKills");
        super.addSortableColumn("headshots");
        super.addSortableColumn("percentHeadshots");
        super.addSortableColumn("damageGiven");
        super.addSortableColumn("hsPerKill");
    }

    public WeaponTableRow getRow(int node)
    {
        return (WeaponTableRow) super.getRow(node + "");
    }
}
