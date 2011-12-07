/*
 * Created on 12.10.2005
 */
package cx.ath.janiwe.steamparser.online.table;

import static cx.ath.janiwe.util.web.StaticFormatter.GERMAN_DATE_FORMATTER;
import cx.ath.janiwe.steamparser.db.SessionFilter;
import cx.ath.janiwe.steamparser.stats.GameStats;
import cx.ath.janiwe.util.web.StaticFormatter;
import de.hdi.gui.web.taglib.table.TableRowAdapter;

public class SessionTableRow extends TableRowAdapter
{

    private static final String HS_BAR_IMAGE = "static/cs/bar6.gif";

    private GameStats rowStats;

    public SessionTableRow(GameStats rowStats)
    {
        this.rowStats = rowStats;
    }

    public Object[] getCellValue(String column)
    {
        if (column.equals("duration"))
        {
            return new Object[] { rowStats.getStarted(),
            		GERMAN_DATE_FORMATTER.format(rowStats.getStarted()) 
            		+ "<br/>bis<br/>" 
            		+ GERMAN_DATE_FORMATTER.format(rowStats.getStopped()) };
        }
        else if (column.equals("mapsPlayed"))
        {
            return new Object[] { rowStats.getMapsPlayedAsString() };
        }
        else if (column.equals("involvedPlayers"))
        {
            return new Object[] { rowStats.getInvolvedPlayersAsString() };
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

    public SessionFilter getFilter()
    {
        return new SessionFilter(rowStats.getId());
    }

}
