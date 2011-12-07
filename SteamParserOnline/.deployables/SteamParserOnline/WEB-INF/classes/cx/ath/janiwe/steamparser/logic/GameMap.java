/*
 * Created on 04.10.2005
 */
package cx.ath.janiwe.steamparser.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cx.ath.janiwe.steamparser.stats.PlayerStats;
import cx.ath.janiwe.steamparser.stats.WeaponStats;

/**
 * Map is manager of players and kills.
 * 
 * @author 010626
 */
public class GameMap
{

    private Date mapStarted;

    private Date mapStopped;

    private String mapName;

    private Map<String, Player> players;

    public GameMap(String mapName, Date mapStarted)
    {
        this.mapName = mapName;
        this.mapStarted = mapStarted;
        this.mapStopped = null;
        this.players = new HashMap<String, Player>();
    }

    public Date getMapStarted()
    {
        return mapStarted;
    }

    public Date getMapStopped()
    {
        return mapStopped;
    }

    public void setMapStopped(Date mapStopped)
    {
        this.mapStopped = mapStopped;
    }

    public String getMapName()
    {
        return mapName;
    }

    public Map<String, Player> getPlayers()
    {
        return players;
    }

    public void addPlayer(String nick, Date joinedAt)
    {

        if (players.containsKey(nick))
        {
            Player p = players.get(nick);
            if (p.getPartedAt() != null)
            {
                p.setPartedAt(null);
            }
            p.setJoinedAt(joinedAt);
        }
        else
        {
            Player p = new Player(nick, joinedAt);
            players.put(nick, p);
        }
    }

    public void remPlayer(String nick, Date partedAt)
    {
        Player p = players.get(nick);
        if (p != null)
        {
            p.setPartedAt(partedAt);
        }
    }

    public void suicide(String nick)
    {
        Player p = players.get(nick);
        if (p != null)
        {
            Kill k = new Kill(p, p, Weapon.suicide, false);
            p.addKill(k);
            p.addDeath(k);
        }
        else
        {
            System.out.println("Suicide mit ungültigem PlayerNick, ignoriere!");
        }
    }

    public void kill(String attackerNick, String victimNick,
            String weaponString, boolean headshot)
    {
        Player attacker = players.get(attackerNick);
        Player victim = players.get(victimNick);
        if (attacker != null && victim != null)
        {
            Weapon w = getWeapon(weaponString);
            Kill k = new Kill(attacker, victim, w, headshot);
            attacker.addKill(k);
            victim.addDeath(k);
        }
        else
        {
            System.out.println("Kill mit ungültigen PlayerNicks, ignoriere!");
        }
    }

    public void damage(String attackerNick, String victimNick,
            String weaponString, int damage, int armorDamage,
            String hitgroupString)
    {
        Player attacker = players.get(attackerNick);
        Player victim = players.get(victimNick);
        if (attacker != null && victim != null)
        {
            Weapon w = getWeapon(weaponString);
            Hitgroup h = getHitgroup(hitgroupString);
            Damage d = new Damage(attacker, victim, w, damage, armorDamage, h);
            attacker.addDamageGiven(d);
            victim.addDamageTaken(d);
        }
        else
        {
            System.out.println("Damage mit ungültigen PlayerNicks, ignoriere!");
        }

    }

    private Weapon getWeapon(String weaponString)
    {
        Weapon[] wArray = Weapon.class.getEnumConstants();
        if (weaponString.equals("357"))
            weaponString = "c357";
        for (Weapon w : wArray)
        {
            if (w.name().equals(weaponString))
                return w;
        }
        return Weapon.unknown;
    }

    private Hitgroup getHitgroup(String hitgroupString)
    {
        Hitgroup[] hArray = Hitgroup.class.getEnumConstants();
        for (Hitgroup h : hArray)
        {
            if (h.toString().equals(hitgroupString))
                return h;
        }
        return Hitgroup.UNKNOWN;
    }

    public Map<String, PlayerStats> getPlayerStats()
    {
        Map<String, PlayerStats> result = new HashMap<String, PlayerStats>();
        for (Player p : players.values())
        {
            result.put(p.getName(), new PlayerStats(p));
        }
        return result;
    }

    public Map<String, PlayerStats> getPlayerStats(WeaponStats stats)
    {
        Map<String, PlayerStats> result = new HashMap<String, PlayerStats>();
        for (Player p : players.values())
        {
            PlayerStats pStats = new PlayerStats(p, stats);
            if (!pStats.isEmpty())
            {
                result.put(p.getName(), pStats);
            }
        }
        return result;
    }

    public Map<Weapon, WeaponStats> getWeaponStats()
    {
        Map<Weapon, WeaponStats> result = new HashMap<Weapon, WeaponStats>();
        for (Player p : players.values())
        {
            Map<Weapon, WeaponStats> userResult = p.getWeaponStats();
            for (Weapon w : userResult.keySet())
            {
                if (result.containsKey(w))
                {
                    result.get(w).add(userResult.get(w));
                }
                else
                {
                    result.put(w, userResult.get(w));
                }
            }
        }
        return result;
    }

    public Map<Weapon, WeaponStats> getWeaponStats(PlayerStats stats)
    {
        Map<Weapon, WeaponStats> result = new HashMap<Weapon, WeaponStats>();
        Player p = players.get(stats.getName());
        if (p != null)
        {
            Map<Weapon, WeaponStats> userResult = p.getWeaponStats();
            for (Weapon w : userResult.keySet())
            {
                if (result.containsKey(w))
                {
                    result.get(w).add(userResult.get(w));
                }
                else
                {
                    result.put(w, userResult.get(w));
                }
            }
        }
        return result;
    }

    public int getNoOfPlayers()
    {
        return players.size();
    }

    public int getTotalKills()
    {
        int result = 0;
        for (Player p : players.values())
        {
            result += p.getOverallKills();
        }
        return result;
    }

    public int getTotalKills(Weapon w)
    {
        int result = 0;
        for (Player p : players.values())
        {
            result += p.getOverallKills(w);
        }
        return result;
    }

    public int getTotalHeadshots()
    {
        int result = 0;
        for (Player p : players.values())
        {
            result += p.getHeadshots();
        }
        return result;
    }

    public int getTotalHeadshots(Weapon w)
    {
        int result = 0;
        for (Player p : players.values())
        {
            result += p.getHeadshots(w);
        }
        return result;
    }

    public int getTotalDamage()
    {
        int result = 0;
        for (Player p : players.values())
        {
            result += p.getOverallDamageGiven();
        }
        return result;
    }

    public int getTotalDamage(Weapon w)
    {
        int result = 0;
        for (Player p : players.values())
        {
            result += p.getOverallDamageGiven(w);
        }
        return result;
    }
}
