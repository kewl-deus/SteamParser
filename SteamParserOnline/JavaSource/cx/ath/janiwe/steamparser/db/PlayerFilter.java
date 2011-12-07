/*
 * Created on 10.10.2005
 */
package cx.ath.janiwe.steamparser.db;

public class PlayerFilter implements DBFilter
{

    private String playerName;

    public PlayerFilter(String playerName)
    {
        this.playerName = playerName;
    }

    public String getAttackerPart()
    {
        return "attacker = '" + playerName + "'";
    }

    public String getVictimPart()
    {
        return "victim = '" + playerName + "'";
    }

    public boolean equals(PlayerFilter f)
    {
        return true;
    }

}
