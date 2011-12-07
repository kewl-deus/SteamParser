/*
 * Created on 10.10.2005
 */
package cx.ath.janiwe.steamparser.db;

public class MapFilter implements DBFilter
{

    private String mapName;

    public MapFilter(String mapName)
    {
        this.mapName = mapName;
    }

    private String getWherePart()
    {
        return "map = '" + mapName + "'";
    }

    public String getAttackerPart()
    {
        return getWherePart();
    }

    public String getVictimPart()
    {
        return getWherePart();
    }

    public boolean equals(MapFilter f)
    {
        return true;
    }

}
