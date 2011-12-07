package de.dengot.hl2stats.model;
public class Attack extends HL2LogEntry
{

    private int damage;

    private int damage_armor;

    private int health;

    private int armor;

    private String hitgroup;

    public Attack(String date, String time, String attacker, String victim,
            String weapon, int damage, int damage_armor, int health, int armor,
            String hitgroup)
    {
        super(date, time, attacker, victim, weapon);
        this.armor = armor;
        this.damage = damage;
        this.damage_armor = damage_armor;
        this.health = health;
        this.hitgroup = hitgroup;
    }

    public int getArmor()
    {
        return armor;
    }

    public void setArmor(int armor)
    {
        this.armor = armor;
    }

    public int getDamage()
    {
        return damage;
    }

    public void setDamage(int damage)
    {
        this.damage = damage;
    }

    public int getDamage_armor()
    {
        return damage_armor;
    }

    public void setDamage_armor(int damage_armor)
    {
        this.damage_armor = damage_armor;
    }

    public int getHealth()
    {
        return health;
    }

    public void setHealth(int health)
    {
        this.health = health;
    }

    public String getHitgroup()
    {
        return hitgroup;
    }

    public void setHitgroup(String hitgroup)
    {
        this.hitgroup = hitgroup;
    }

    public String toString()
    {
        return attacker + " attacked " + victim + " with " + weapon + " at "
                + hitgroup + " damage:" + damage + " damage_armor:"
                + damage_armor + " health:" + health + " armor:" + armor;
    }

}
