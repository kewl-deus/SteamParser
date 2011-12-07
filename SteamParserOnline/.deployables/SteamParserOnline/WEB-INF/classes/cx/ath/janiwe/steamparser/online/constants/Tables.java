package cx.ath.janiwe.steamparser.online.constants;

public enum Tables
{
    PLAYER_STATS_TABLE("playerStatsTable"), SESSION_STATS_TABLE(
            "sessionStatsTable"), MAP_STATS_TABLE("mapStatsTable"), WEAPON_STATS_TABLE(
            "weaponStatsTable");

    private final String label;

    Tables(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return this.label;
    }

    public String toString()
    {
        return this.getLabel();
    }
}
