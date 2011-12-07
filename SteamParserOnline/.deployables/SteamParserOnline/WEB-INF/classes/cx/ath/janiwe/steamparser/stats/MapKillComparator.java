package cx.ath.janiwe.steamparser.stats;

import java.util.Comparator;

public class MapKillComparator implements Comparator<MapStats>
{

    private static MapKillComparator singleton;

    private MapKillComparator()
    {

    }

    public static MapKillComparator getInstance()
    {
        if (singleton == null)
        {
            singleton = new MapKillComparator();
        }
        return singleton;
    }

    public int compare(MapStats s1, MapStats s2)
    {
        return Integer.signum(s2.getKills() - s1.getKills());
    }

}
