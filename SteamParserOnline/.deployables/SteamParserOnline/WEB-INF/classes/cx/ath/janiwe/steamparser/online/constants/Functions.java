package cx.ath.janiwe.steamparser.online.constants;

public enum Functions
{
    ADD_PLAYER_FILTER("addPlayerFilter"), REMOVE_PLAYER_FILTER(
            "removePlayerFilter"), ADD_SESSION_FILTER("addSessionFilter"), REMOVE_SESSION_FILTER(
            "removeSessionFilter"), ADD_MAP_FILTER("addMapFilter"), REMOVE_MAP_FILTER(
            "removeMapFilter"), ADD_WEAPON_FILTER("addWeaponFilter"), REMOVE_WEAPON_FILTER(
            "removeWeaponFilter");

    private final String label;

    Functions(String label)
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
