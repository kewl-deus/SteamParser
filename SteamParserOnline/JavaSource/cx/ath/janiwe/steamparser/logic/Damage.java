/*
 * Created on 04.10.2005
 */
package cx.ath.janiwe.steamparser.logic;

public class Damage
{

    private int damage;

    private int armorDamage;

    private Weapon weapon;

    private Hitgroup hitgroup;

    private Player attacker;

    private Player victim;

    public Damage(Player attacker, Player victim, Weapon weapon, int damage,
            int armorDamage, Hitgroup hitgroup)
    {
        this.attacker = attacker;
        this.victim = victim;
        this.weapon = weapon;
        this.damage = damage;
        this.armorDamage = armorDamage;
        this.hitgroup = hitgroup;
    }

    public int getArmorDamage()
    {
        return armorDamage;
    }

    public Player getAttacker()
    {
        return attacker;
    }

    public int getDamage()
    {
        return damage;
    }

    public Hitgroup getHitgroup()
    {
        return hitgroup;
    }

    public Player getVictim()
    {
        return victim;
    }

    public Weapon getWeapon()
    {
        return weapon;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer(attacker.getName());
        result.append(" -(");
        result.append(damage);
        result.append("/");
        result.append(armorDamage);
        result.append(")-> ");
        result.append(victim.getName());
        result.append(" w/ ");
        result.append(weapon);
        result.append(" (");
        result.append(hitgroup);
        result.append(")");
        return result.toString();
    }

}
