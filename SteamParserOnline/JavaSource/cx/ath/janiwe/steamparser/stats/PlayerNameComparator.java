package cx.ath.janiwe.steamparser.stats;

import java.util.Comparator;

public class PlayerNameComparator implements Comparator<PlayerStats>
{

    private static PlayerNameComparator singleton;

    private PlayerNameComparator()
    {

    }

    public static PlayerNameComparator getInstance()
    {
        if (singleton == null)
        {
            singleton = new PlayerNameComparator();
        }
        return singleton;
    }

    public int compare(PlayerStats s1, PlayerStats s2)
    {
        return s1.getName().compareTo(s2.getName());
    }

}
