/*
 * Created on 12.10.2005
 */
package cx.ath.janiwe.steamparser.online.table;

import cx.ath.janiwe.steamparser.db.WeaponFilter;
import cx.ath.janiwe.steamparser.stats.WeaponStats;
import de.hdi.gui.web.taglib.table.TableRowAdapter;

public class WeaponTableRow extends TableRowAdapter
{

    private static final String WEAPON_IMAGE_BASE = "static/cs/weapons/";

    private static final String HS_BAR_IMAGE = "static/cs/bar6.gif";

    private WeaponStats rowStats;

    private int rang;

    private int overallKills;

    private int overallHeadshots;

    public WeaponTableRow(int rang, WeaponStats rowStats, int overallKills,
            int overallHeadshots)
    {
        this.rang = rang;
        this.rowStats = rowStats;
        this.overallKills = overallKills;
        this.overallHeadshots = overallHeadshots;
    }

    public Object[] getCellValue(String column)
    {
        if (column.equals("rang"))
        {
            return new Object[] { rang };
        }
        else if (column.equals("weapon"))
        {
            return new Object[] {
                    rowStats.getName(),
                    "<img src=\"" + WEAPON_IMAGE_BASE + rowStats.getImageFile()
                            + "\" width=\"90\" height=\"17\" border=0 alt=\""
                            + rowStats.getName() + "\" />" };
        }
        else if (column.equals("kills"))
        {
            return new Object[] { rowStats.getKills() };
        }
        else if (column.equals("percentKillsImage"))
        {
        	String overallKillPercentageString = rowStats.getOverallKillPercentageAsString(overallKills);
            return new Object[] { "<img src=\"" + HS_BAR_IMAGE + "\" width=\""
                    + overallKillPercentageString
                    + "%\" height=10 border=0 alt=\""
                    + overallKillPercentageString
                    + "%\">" };
        }
        else if (column.equals("percentKills"))
        {
            return new Object[] { rowStats
                    .getOverallKillPercentageAsString(overallKills) };
        }
        else if (column.equals("headshots"))
        {
            return new Object[] { rowStats.getHeadshots() };
        }
        else if (column.equals("percentHeadshotsImage"))
        {
        	String headShotPercent = rowStats.getHeadshotPercentAsString();
            return new Object[] { "<img src=\""
                    + HS_BAR_IMAGE
                    + "\" width=\""
                    + headShotPercent
                    + "%\" height=10 border=0 alt=\""
                    + headShotPercent
                    + "%\" />" };
        }
        else if (column.equals("percentHeadshots"))
        {
            return new Object[] { rowStats.getHeadshotPercentage(),
            		rowStats.getHeadshotPercentAsString() };
        }
        else if (column.equals("damageGiven"))
        {
            return new Object[] { rowStats.getDamageGiven() };
        }
        else if (column.equals("hsPerKill"))
        {
            return new Object[] { rowStats.getHeadshotPercentageAsString() };
        }
        else
        {
            return new Object[] { "" };
        }
    }

    public WeaponFilter getFilter()
    {
        return new WeaponFilter(rowStats.getWeaponShortName());
    }

}
