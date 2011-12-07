/*
 * Created on 11.10.2005
 */
package cx.ath.janiwe.steamparser.stats;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import cx.ath.janiwe.util.web.StaticFormatter;

public class GameStats
{

    private static final String HS_BAR_IMAGE = "images/bar6.gif";

    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "dd.MM.yyyy - HH:mm:ss");

    private int id;

    private Date started;

    private Date stopped;

    private Vector<String> mapsPlayed;
    
    private Vector<String> involvedPlayers;

    private int kills;

    private int headshots;

    private int totalDamage;

    public GameStats(int id, Timestamp started, Timestamp stopped,
            Vector<String> mapsPlayed, Vector<String> involvedPlayers, int kills, int headshots, int totalDamage)
    {
        this.id = id;
        this.started = started;
        this.stopped = stopped;
        this.mapsPlayed = new Vector<String>(mapsPlayed);
        Collections.sort(this.mapsPlayed);
        this.involvedPlayers = new Vector<String>(involvedPlayers);
        Collections.sort(this.involvedPlayers);
        this.kills = kills;
        this.headshots = headshots;
        this.totalDamage = totalDamage;
    }

    public int getId()
    {
        return id;
    }

    public Date getStarted()
    {
        return started;
    }

    public Date getStopped()
    {
        return stopped;
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
            result.append(sdf.format(started));
            result.append("</a>");
        }
        else
        {
            result.append(sdf.format(started));
        }

        result.append("</font></td>");
        result
                .append("<td align=\"left\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(sdf.format(stopped));
        result.append("</font></td>");
        result
                .append("<td align=\"center\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(getMapsPlayedAsString());
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

    public String getMapsPlayedAsString()
    {
        StringBuffer result = new StringBuffer();
        for (Iterator<String> iter = mapsPlayed.iterator(); iter.hasNext();) {
			String s = iter.next();
			result.append(s);
			if (iter.hasNext()) result.append("<br/>");
		}
        return result.toString();
    }
    
    public String getInvolvedPlayersAsString()
    {
        StringBuffer result = new StringBuffer();
        for (Iterator<String> iter = involvedPlayers.iterator(); iter.hasNext();) {
			String s = iter.next();
			result.append(s);
			if (iter.hasNext()) result.append("<br/>");
		}
        return result.toString();
    }

    public int getHeadshots()
    {
        return headshots;
    }

    public int getKills()
    {
        return kills;
    }

    public int getTotalDamage()
    {
        return totalDamage;
    }

}
