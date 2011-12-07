package cx.ath.janiwe.steamparser.stats;

import java.text.DecimalFormat;

import cx.ath.janiwe.steamparser.logic.Damage;
import cx.ath.janiwe.steamparser.logic.Kill;
import cx.ath.janiwe.steamparser.logic.Weapon;

public class WeaponStats implements IStats
{

    private static final String WEAPON_IMAGE_BASE = "images/weapons/";

    private static final String HS_BAR_IMAGE = "images/bar6.gif";

    private Weapon weapon;

    private int kills;

    private int headshots;

    private int damageGiven;

    public WeaponStats(Weapon w, int kills, int headshots, int damageGiven)
    {
        this.weapon = w;
        this.kills = kills;
        this.headshots = headshots;
        this.damageGiven = damageGiven;
    }

    public WeaponStats(Weapon w)
    {
        this.weapon = w;
        this.kills = 0;
        this.headshots = 0;
        this.damageGiven = 0;

    }

    public void add(WeaponStats ws)
    {
        if (ws.getWeapon() == weapon)
        {
            this.kills += ws.getKills();
            this.headshots += ws.getHeadshots();
            this.damageGiven += ws.getDamageGiven();
        }
    }

    public void add(Kill k)
    {
        if (k.getWeapon() == weapon)
        {
            this.kills++;
            if (k.isHeadshot())
                this.headshots++;
        }
    }

    public void add(Damage d)
    {
        if (d.getWeapon() == weapon)
        {
            this.damageGiven += (d.getArmorDamage() + d.getDamage());
        }
    }

    public int getDamageGiven()
    {
        return damageGiven;
    }

    public int getHeadshots()
    {
        return headshots;
    }

    public int getKills()
    {
        return kills;
    }

    public Weapon getWeapon()
    {
        return weapon;
    }

    public String getName()
    {
        return weapon.getLongName();
    }

    public String getWeaponShortName()
    {
        return weapon.name();
    }

    public String getImageFile()
    {
        return weapon.getImageFile();
    }

    public float getHeadshotPercentage()
    {
        return kills == 0 ? 0 : (float) headshots / (float) kills;
    }

    /**
     * Gibt den Anteil Headshots als Kommazahl also z.B. "0.35" in String-Form zurück.
     */
    public String getHeadshotPercentageAsString()
    {
        return new DecimalFormat("##0.00").format(getHeadshotPercentage());
    }

    /**
     * Gibt den Anteil Headshots als Prozentzahl zurück, also z.B. "35".
     */
    public String getHeadshotPercentAsString()
    {
    	return new DecimalFormat("##0.00").format(getHeadshotPercentage()*100);
    }
    
    public String toHTMLString(int overallKills, int overallHeadshots,
            String detailLink)
    {
        StringBuffer result = new StringBuffer();
        result
                .append("<td align=\"center\" bgcolor=\"#000000\"><font size=2 class=\"fontNormal\">");
        if (detailLink != null)
        {
            result.append("<a href=\"");
            result.append(detailLink);
            result.append("\" alt=\"Details\">");
            result.append("<img src=\"" + WEAPON_IMAGE_BASE + getImageFile()
                    + "\" width=\"90\" height=\"17\" border=0 alt=\""
                    + getName() + "\" />");
            result.append("</a>");
        }
        else
        {
            result.append("<img src=\"" + WEAPON_IMAGE_BASE + getImageFile()
                    + "\" width=\"90\" height=\"17\" border=0 alt=\""
                    + getName() + "\" />");
        }

        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(kills);
        result.append("</font></td>");
        result
                .append("<td align=\"left\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append("<img src=\"" + HS_BAR_IMAGE + "\" width=\""
                + getOverallKillPercentageAsString(overallKills)
                + "%\" height=10 border=0 alt=\""
                + getOverallKillPercentageAsString(overallKills) + "%\"");
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(getOverallKillPercentageAsString(overallKills) + "%");
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
        result.append(getHeadshotPercentageAsString() + "%");
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(getHeadshotPercentageAsString());
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(damageGiven);
        result.append("</font></td>");

        return result.toString();
    }

    public String getOverallKillPercentageAsString(int overallKills)
    {
        float percentage = overallKills == 0 ? 0 : (float) kills
                / (float) overallKills * 100;
        return new DecimalFormat("##0.00").format(percentage);
    }

//    public String getOverallHeadShotPercentageAsString(int overallHeadshots)
//    {
//        float percentage = overallHeadshots == 0 ? 0 : (float) headshots
//                / (float) overallHeadshots * 100;
//        return new DecimalFormat("##0.00").format(percentage);
//    }
}