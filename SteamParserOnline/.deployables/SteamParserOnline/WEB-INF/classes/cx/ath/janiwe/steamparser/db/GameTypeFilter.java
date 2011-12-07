package cx.ath.janiwe.steamparser.db;

public class GameTypeFilter implements DBFilter
{

    public final static String CS = "cs";

    public final static String HL2DM = "hl2dm";

    private String gameType;

    public GameTypeFilter(String gameType)
    {
        this.gameType = gameType;
    }

    private String getWhere()
    {
        return "gameType = '" + gameType + "'";
    }

    public String getAttackerPart()
    {
        return getWhere();
    }

    public String getVictimPart()
    {
        return getWhere();
    }

    public boolean equals(GameTypeFilter f)
    {
        return true;
    }

}
