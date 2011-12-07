package cx.ath.janiwe.steamparser.online.constants;

public enum DBFilters
{
    PLAYER_FILTER("playerFilter"), SESSION_FILTER("sessionFilter"), MAP_FILTER(
            "mapFilter"), WEAPON_FILTER("weaponFilter");

    private final String label;

    DBFilters(String label)
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
