package cx.ath.janiwe.steamparser.stats;

import java.util.Comparator;

public class PlayerKillComparator implements Comparator<PlayerStats>
{

    private static PlayerKillComparator singleton;

    private PlayerKillComparator()
    {

    }

    public static PlayerKillComparator getInstance()
    {
        if (singleton == null)
        {
            singleton = new PlayerKillComparator();
        }
        return singleton;
    }

    public int compare(PlayerStats s1, PlayerStats s2)
    {
        int killSignum = Integer.signum(s2.getKills() - s1.getKills());
        if (killSignum != 0)
        {
            return killSignum;
        }
        else
        {
            int deathSignum = Integer.signum(s1.getDeaths() - s2.getDeaths());
            if (deathSignum != 0)
            {
                return killSignum;
            }
            else
            {
                return Integer
                        .signum(s2.getDamageGiven() - s1.getDamageGiven());
            }
        }
    }

}
