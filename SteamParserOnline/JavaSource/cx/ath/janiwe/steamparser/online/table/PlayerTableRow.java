/*
 * Created on 12.10.2005
 */
package cx.ath.janiwe.steamparser.online.table;

import cx.ath.janiwe.steamparser.db.PlayerFilter;
import cx.ath.janiwe.steamparser.stats.PlayerStats;
import cx.ath.janiwe.util.web.StaticFormatter;
import de.hdi.gui.web.taglib.table.TableRowAdapter;

public class PlayerTableRow extends TableRowAdapter
{

    private static final String HS_BAR_IMAGE = "static/cs/bar6.gif";

    private int rang;

    private PlayerStats statsInRow;

    public PlayerTableRow(int rang, PlayerStats statsInRow)
    {
        super();
        this.rang = rang;
        this.statsInRow = statsInRow;
    }

    public Object[] getCellValue(String column)
    {
        if (column.equals("rang"))
        {
            return new Object[] { rang };
        }
        else if (column.equals("hsPercentImage"))
        {
        	float hsPercentage = statsInRow.getHeadshotPercentage();
        	String hsPercentageString = StaticFormatter.PERCENT_DECIMAL_FORMATTER.format(hsPercentage);
            return new Object[] { hsPercentage, "<img src=\"" + HS_BAR_IMAGE + "\" width=\""
                    + hsPercentageString
                    + "%\" height=10 border=0 alt=\""
                    + hsPercentageString + "%\" />",
                    hsPercentageString};
        }
        else if (column.equals("name"))
        {
        	return new Object[] { statsInRow.getName() };
        }
        else if (column.equals("kills"))
        {
        	return new Object[] { statsInRow.getKills() };
        }
        else if (column.equals("deaths"))
        {
        	return new Object[] { statsInRow.getDeaths() };
        }
        else if (column.equals("headshots"))
        {
        	return new Object[] { statsInRow.getHeadshots() };
        }
        else if (column.equals("killsPerDeathAsString"))
        {
        	return new Object[] { statsInRow.getKillsPerDeathAsString() };
        }
        else if (column.equals("sessionsPlayed"))
        {
        	return new Object[] { statsInRow.getSessionsPlayed() };
        }
        else if (column.equals("fph"))
        {
        	return new Object[] { statsInRow.getFph() };
        }
        else
        {
            return new Object[] { "" };
        }
    }

    public PlayerFilter getFilter()
    {
        return new PlayerFilter(statsInRow.getName());
    }
}
