package cx.ath.janiwe.steamparser.stats;

import java.util.Comparator;

public class WeaponKillComparator implements Comparator<WeaponStats>
{

    private static WeaponKillComparator singleton;

    private WeaponKillComparator()
    {

    }

    public static WeaponKillComparator getInstance()
    {
        if (singleton == null)
        {
            singleton = new WeaponKillComparator();
        }
        return singleton;
    }

    public int compare(WeaponStats s1, WeaponStats s2)
    {
        int killSignum = Integer.signum(s2.getKills() - s1.getKills());
        if (killSignum == 0)
        {
            return Integer.signum(s2.getDamageGiven() - s1.getDamageGiven());
        }
        else
        {
            return killSignum;
        }
    }

}
