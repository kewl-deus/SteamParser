package cx.ath.janiwe.steamparser.stats;

import cx.ath.janiwe.steamparser.logic.GameMap;
import cx.ath.janiwe.steamparser.logic.Player;
import cx.ath.janiwe.steamparser.logic.Weapon;
import cx.ath.janiwe.util.web.StaticFormatter;

public class MapStats
{

    private static final String HS_BAR_IMAGE = "images/bar6.gif";

    private String name;

    private int kills;

    private int headshots;

    private int totalDamage;

    public MapStats(String name, int kills, int headshots, int totalDamage)
    {
        this.name = name;
        this.kills = kills;
        this.headshots = headshots;
        this.totalDamage = totalDamage;
    }

    public MapStats(GameMap m)
    {
        this.name = m.getMapName();
        this.kills = m.getTotalKills();
        this.headshots = m.getTotalHeadshots();
        this.totalDamage = m.getTotalDamage();
    }

    public MapStats(GameMap m, PlayerStats p)
    {
        Player pl = m.getPlayers().get(p.getName());
        this.name = m.getMapName();

        if (pl != null)
        {
            this.kills = pl.getOverallKills();
            this.headshots = pl.getHeadshots();
            this.totalDamage = pl.getOverallDamageGiven();
        }
        else
        {
            this.kills = 0;
            this.headshots = 0;
            this.totalDamage = 0;
        }

    }

    public MapStats(GameMap m, WeaponStats w)
    {
        this.name = m.getMapName();
        Weapon weapon = w.getWeapon();
        this.kills = m.getTotalKills(weapon);
        this.headshots = m.getTotalHeadshots(weapon);
        this.totalDamage = m.getTotalDamage(weapon);
    }

    public void add(MapStats stats)
    {
        if (stats.getName().equals(name))
        {
            this.kills += stats.getKills();
            this.headshots += stats.getHeadshots();
            this.totalDamage += stats.getTotalDamage();
        }
    }

    public int getTotalDamage()
    {
        return totalDamage;
    }

    public int getHeadshots()
    {
        return headshots;
    }

    public int getKills()
    {
        return kills;
    }

    public String getName()
    {
        return name;
    }

    public float getHeadshotPercentage()
    {
        return kills == 0 ? 0 : (float) headshots / (float) kills * 100;
    }

    public String getHeadshotPercentageAsString()
    {
        return StaticFormatter.PERCENT_DECIMAL_FORMATTER.format(getHeadshotPercentage());
    }

    public String toHTMLString(String detailLink)
    {
        StringBuffer result = new StringBuffer();
        result
                .append("<td align=\"left\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        if (detailLink != null)
        {
            result.append("<a href=\"");
            result.append(detailLink);
            result.append("\" alt=\"Details\">");
            result.append(name);
            result.append("</a>");
        }
        else
        {
            result.append(name);
        }
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(kills);
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(headshots);
        result.append("</font></td>");
        result
                .append("<td align=\"left\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append("<img src=\"" + HS_BAR_IMAGE + "\" width=\""
                + getHeadshotPercentageAsString()
                + "%\" height=10 border=0 alt=\""
                + getHeadshotPercentageAsString() + "%\"");
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(totalDamage);
        result.append("</font></td>");

        return result.toString();
    }
}
