/*
 * Created on 10.10.2005
 */
package cx.ath.janiwe.steamparser.db;

public class SessionFilter implements DBFilter
{

    private int sessionId;

    public SessionFilter(int sessionId)
    {
        this.sessionId = sessionId;
    }

    private String getWherePart()
    {
        return "gameSession = " + sessionId;
    }

    public String getAttackerPart()
    {
        return getWherePart();
    }

    public String getVictimPart()
    {
        return getWherePart();
    }

    public boolean equals(SessionFilter f)
    {
        return true;
    }

}
