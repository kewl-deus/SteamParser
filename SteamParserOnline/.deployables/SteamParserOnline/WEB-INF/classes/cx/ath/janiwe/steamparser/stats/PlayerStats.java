package cx.ath.janiwe.steamparser.stats;

import cx.ath.janiwe.steamparser.logic.Player;
import cx.ath.janiwe.steamparser.logic.Weapon;
import cx.ath.janiwe.util.web.StaticFormatter;

public class PlayerStats implements IStats
{

    public static final String HS_BAR_IMAGE = "static/cs/bar6.gif";

    private String name;

    private int kills;

    private int headshots;

    private int deaths;

    private int damageGiven;

    private int damageTaken;

    private float fph;

    private int sessionsPlayed;
    
    public PlayerStats(String name, int kills, int headshots, int deaths,
            int damageGiven, int damageTaken, float fph, int sessionsPlayed)
    {
        this.name = name;
        this.kills = kills;
        this.headshots = headshots;
        this.deaths = deaths;
        this.damageGiven = damageGiven;
        this.damageTaken = damageTaken;
        this.fph = fph;
        this.sessionsPlayed = sessionsPlayed;
    }

    public PlayerStats(Player p)
    {
        this.name = p.getName();
        this.kills = p.getOverallKills();
        this.headshots = p.getHeadshots();
        this.deaths = p.getOverallDeaths();
        this.damageGiven = p.getOverallDamageGiven();
        this.damageTaken = p.getOverallDamageTaken();
    }

    public PlayerStats(Player p, WeaponStats stats)
    {
        Weapon w = stats.getWeapon();
        this.name = p.getName();
        this.kills = p.getOverallKills(w);
        this.headshots = p.getHeadshots(w);
        this.deaths = p.getOverallDeaths(w);
        this.damageGiven = p.getOverallDamageGiven(w);
        this.damageTaken = p.getOverallDamageTaken(w);
    }

    public void add(PlayerStats stats)
    {
        if (stats.getName().equals(name))
        {
            this.kills += stats.getKills();
            this.headshots += stats.getHeadshots();
            this.deaths += stats.getDeaths();
            this.damageGiven += stats.getDamageGiven();
            this.damageTaken += stats.getDamageTaken();
        }
    }

    public int getDamageGiven()
    {
        return damageGiven;
    }

    public int getDamageTaken()
    {
        return damageTaken;
    }

    public int getDeaths()
    {
        return deaths;
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

    public float getKillsPerDeath()
    {
        return deaths == 0 ? 0 : (float) kills / (float) deaths;
    }

    public String getKillsPerDeathAsString()
    {
        return StaticFormatter.PERCENT_DECIMAL_FORMATTER.format(getKillsPerDeath());
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
        result.append(deaths);
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
        result.append(getKillsPerDeathAsString());
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(damageGiven);
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(damageTaken);
        result.append("</font></td>");

        return result.toString();
    }

    public boolean isEmpty()
    {
        return kills == 0 && headshots == 0;
    }

    public float getFph()
    {
        return fph;
    }
    
    public int getSessionsPlayed()
    {
    	return sessionsPlayed;
    }

}
