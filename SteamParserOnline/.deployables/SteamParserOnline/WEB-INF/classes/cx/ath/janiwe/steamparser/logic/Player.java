/*
 * Created on 04.10.2005
 */
package cx.ath.janiwe.steamparser.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cx.ath.janiwe.steamparser.stats.WeaponStats;

/**
 * A player is anybody participating in a <code>Game</code>, whether
 * <code>Bot</code> or <code>Human</code>.
 * 
 * @author 010626
 */
public class Player
{

    private Date joinedAt;

    private Date partedAt;

    private String name;

    private Set<Kill> kills;

    private Set<Kill> deaths;

    private Set<Damage> damageGiven;

    private Set<Damage> damageTaken;

    public Player(String name, Date joinedAt)
    {
        this.name = name;
        this.kills = new HashSet<Kill>();
        this.deaths = new HashSet<Kill>();
        this.damageGiven = new HashSet<Damage>();
        this.damageTaken = new HashSet<Damage>();
        this.joinedAt = joinedAt;
    }

    public void addKill(Kill k)
    {
        kills.add(k);
    }

    public void addDeath(Kill k)
    {
        deaths.add(k);
    }

    public void addDamageGiven(Damage d)
    {
        damageGiven.add(d);
    }

    public void addDamageTaken(Damage d)
    {
        damageTaken.add(d);
    }

    public int getOverallKills()
    {
        return kills.size();
    }

    public int getOverallKills(Weapon w)
    {
        int result = 0;
        for (Kill k : kills)
        {
            if (k.getWeapon() == w)
            {
                result++;
            }
        }
        return result;
    }

    public int getOverallDeaths()
    {
        return deaths.size();
    }

    public int getOverallDeaths(Weapon w)
    {
        int result = 0;
        for (Kill k : deaths)
        {
            if (k.getWeapon() == w)
            {
                result++;
            }
        }
        return result;
    }

    public int getOverallDamageGiven()
    {
        int result = 0;
        for (Damage d : damageGiven)
        {
            result += d.getDamage() + d.getArmorDamage();
        }
        return result;
    }

    public int getOverallDamageGiven(Weapon w)
    {
        int result = 0;
        for (Damage d : damageGiven)
        {
            if (d.getWeapon() == w)
                result += d.getDamage() + d.getArmorDamage();
        }
        return result;
    }

    public int getOverallDamageTaken()
    {
        int result = 0;
        for (Damage d : damageTaken)
        {
            result += d.getDamage() + d.getArmorDamage();
        }
        return result;
    }

    public int getOverallDamageTaken(Weapon w)
    {
        int result = 0;
        for (Damage d : damageTaken)
        {
            if (d.getWeapon() == w)
                result += d.getDamage() + d.getArmorDamage();
        }
        return result;
    }

    public int getHeadshots()
    {
        int result = 0;
        for (Kill k : kills)
        {
            if (k.isHeadshot())
                result++;
        }
        return result;
    }

    public int getHeadshots(Weapon w)
    {
        int result = 0;
        for (Kill k : kills)
        {
            if (k.isHeadshot() && k.getWeapon() == w)
                result++;
        }
        return result;
    }

    public String getName()
    {
        return name;
    }

    public Date getJoinedAt()
    {
        return joinedAt;
    }

    public void setJoinedAt(Date joinedAt)
    {
        this.joinedAt = joinedAt;
    }

    public Date getPartedAt()
    {
        return partedAt;
    }

    public void setPartedAt(Date partedAt)
    {
        this.partedAt = partedAt;
    }

    public String toString()
    {
        StringBuffer result = new StringBuffer(name);
        result.append(" (");
        result.append(getOverallKills());
        result.append("/");
        result.append(getOverallDeaths());
        result.append(")");
        return result.toString();
    }

    public Map<Weapon, WeaponStats> getWeaponStats()
    {
        Map<Weapon, WeaponStats> result = new HashMap<Weapon, WeaponStats>();
        for (Kill k : kills)
        {
            if (result.containsKey(k.getWeapon()))
            {
                result.get(k.getWeapon()).add(k);
            }
            else
            {
                WeaponStats w = new WeaponStats(k.getWeapon());
                w.add(k);
                result.put(k.getWeapon(), w);
            }
        }
        for (Damage d : damageGiven)
        {
            if (result.containsKey(d.getWeapon()))
            {
                result.get(d.getWeapon()).add(d);
            }
            else
            {
                WeaponStats w = new WeaponStats(d.getWeapon());
                w.add(d);
                result.put(d.getWeapon(), w);
            }
        }
        return result;
    }

    public Set<Kill> getKills()
    {
        return kills;
    }

    public Set<Damage> getDamageGiven()
    {
        return damageGiven;
    }

}
