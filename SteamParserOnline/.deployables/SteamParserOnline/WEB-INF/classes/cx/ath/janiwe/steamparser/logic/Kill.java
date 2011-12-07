/*
 * Created on 04.10.2005
 */
package cx.ath.janiwe.steamparser.logic;

/**
 * A Kill shows who killed whom with which weapon.
 * 
 * @author 010626
 */
public class Kill
{

    private Player gotKilled;

    private Player killer;

    private Weapon weapon;

    private boolean headshot;

    public Kill(Player killer, Player gotKilled, Weapon weapon, boolean headshot)
    {
        this.gotKilled = gotKilled;
        this.killer = killer;
        this.weapon = weapon;
        this.headshot = headshot;
    }

    public Player getGotKilled()
    {
        return gotKilled;
    }

    public Player getKiller()
    {
        return killer;
    }

    public Weapon getWeapon()
    {
        return weapon;
    }

    public boolean isHeadshot()
    {
        return headshot;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer(killer.getName());
        result.append(" killed ");
        result.append(gotKilled.getName());
        result.append(" with ");
        result.append(weapon.getLongName());
        if (headshot)
            result.append(" (headshot)");
        return result.toString();
    }

}
