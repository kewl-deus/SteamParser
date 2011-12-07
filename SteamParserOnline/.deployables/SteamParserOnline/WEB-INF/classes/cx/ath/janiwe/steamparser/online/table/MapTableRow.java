/*
 * Created on 12.10.2005
 */
package cx.ath.janiwe.steamparser.online.table;

import cx.ath.janiwe.steamparser.db.MapFilter;
import cx.ath.janiwe.steamparser.stats.MapStats;
import cx.ath.janiwe.util.web.StaticFormatter;
import de.hdi.gui.web.taglib.table.TableRowAdapter;

public class MapTableRow extends TableRowAdapter
{

    private static final String HS_BAR_IMAGE = "static/cs/bar6.gif";

    private MapStats rowStats;

    private int rang;

    public MapTableRow(int rang, MapStats rowStats)
    {
        this.rang = rang;
        this.rowStats = rowStats;
    }

    public Object[] getCellValue(String column)
    {
        if (column.equals("rang"))
        {
            return new Object[] { rang };
        }
        else if (column.equals("name"))
        {
            return new Object[] { rowStats.getName() };
        }
        else if (column.equals("kills"))
        {
            return new Object[] { rowStats.getKills() };
        }
        else if (column.equals("headshots"))
        {
            return new Object[] { rowStats.getHeadshots() };
        }
        else if (column.equals("hsPercentImage"))
        {
        	float hsPercentage = rowStats.getHeadshotPercentage();
        	String hsPercentageString = StaticFormatter.PERCENT_DECIMAL_FORMATTER.format(hsPercentage);
            return new Object[] { hsPercentage, "<img src=\"" + HS_BAR_IMAGE + "\" width=\""
                    + hsPercentageString
                    + "%\" height=10 border=0 alt=\""
                    + hsPercentageString + "%\" />",
                    hsPercentageString};
        }
        else if (column.equals("damageGiven"))
        {
            return new Object[] { rowStats.getTotalDamage() };
        }
        else
        {
            return new Object[] { "" };
        }
    }

    public MapFilter getFilter()
    {
        return new MapFilter(rowStats.getName());
    }

}
