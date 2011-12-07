/*
 * Created on 10.10.2005
 */
package cx.ath.janiwe.steamparser.db;

public class WeaponFilter implements DBFilter
{

    private String weaponName;

    public WeaponFilter(String weaponName)
    {
        this.weaponName = weaponName;
    }

    private String getWherePart()
    {
        return "weapon = '" + weaponName + "'";
    }

    public String getAttackerPart()
    {
        return getWherePart();
    }

    public String getVictimPart()
    {
        return getWherePart();
    }

    public boolean equals(WeaponFilter f)
    {
        return true;
    }

}
