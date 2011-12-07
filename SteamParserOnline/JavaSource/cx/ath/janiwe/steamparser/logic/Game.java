/*
 * Created on 04.10.2005
 */
package cx.ath.janiwe.steamparser.logic;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cx.ath.janiwe.steamparser.stats.MapStats;
import cx.ath.janiwe.steamparser.stats.PlayerStats;
import cx.ath.janiwe.steamparser.stats.WeaponStats;

/**
 * Game is manager of maps.
 * 
 * @author 010626
 */
public class Game
{

    private static final String HS_BAR_IMAGE = "images/bar6.gif";

    private static final SimpleDateFormat sdf = new SimpleDateFormat(
            "dd.MM.yyyy - HH:mm:ss");

    private int id;

    private Date gameStarted;

    private Date gameStopped;

    private Set<GameMap> mapsPlayed;

    private GameMap currentMap;

    public Game()
    {
        this.gameStarted = null;
        this.gameStopped = null;
        this.mapsPlayed = new HashSet<GameMap>();
        this.currentMap = null;
    }

    public Date getGameStarted()
    {
        return gameStarted;
    }

    public void setGameStarted(Date gameStarted)
    {
        this.gameStarted = gameStarted;
    }

    public Date getGameStopped()
    {
        return gameStopped;
    }

    public void setGameStopped(Date gameStopped)
    {
        if (currentMap != null && currentMap.getMapStopped() == null)
        {
            currentMap.setMapStopped(gameStopped);
        }
        this.gameStopped = gameStopped;
    }

    public Set<GameMap> getMapsPlayed()
    {
        return mapsPlayed;
    }

    public void newMap(String mapName, Date timestamp)
    {
        if (currentMap != null)
        {
            currentMap.setMapStopped(timestamp);
        }
        GameMap m = new GameMap(mapName, timestamp);
        mapsPlayed.add(m);
        currentMap = m;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void mapStopped(Date timestamp)
    {
        if (currentMap != null && currentMap.getMapStopped() == null)
        {
            currentMap.setMapStopped(timestamp);
        }
        currentMap = null;
    }

    public void playerConnected(String nick, Date joinedAt)
    {
        if (currentMap != null)
            currentMap.addPlayer(nick, joinedAt);
    }

    public void playerDisconnected(String nick, Date partedAt)
    {
        if (currentMap != null)
            currentMap.remPlayer(nick, partedAt);
    }

    public void suicide(String nick)
    {
        if (currentMap != null)
            currentMap.suicide(nick);
    }

    public void kill(String attackerNick, String victimNick,
            String weaponString, boolean headshot)
    {
        if (currentMap != null)
            currentMap.kill(attackerNick, victimNick, weaponString, headshot);
    }

    public void damage(String attackerNick, String victimNick,
            String weaponString, int damage, int armorDamage,
            String hitgroupString)
    {
        if (currentMap != null)
            currentMap.damage(attackerNick, victimNick, weaponString, damage,
                    armorDamage, hitgroupString);
    }

    // Statistiken
    public Map<String, PlayerStats> getPlayerStats()
    {
        Map<String, PlayerStats> result = new HashMap<String, PlayerStats>();
        for (GameMap map : mapsPlayed)
        {
            Map<String, PlayerStats> mapResult = map.getPlayerStats();
            for (Map.Entry<String, PlayerStats> e : mapResult.entrySet())
            {
                if (result.containsKey(e.getKey()))
                {
                    result.get(e.getKey()).add(e.getValue());
                }
                else
                {
                    result.put(e.getKey(), e.getValue());
                }
            }

        }
        return result;
    }

    public Map<String, PlayerStats> getPlayerStats(MapStats stats)
    {
        Map<String, PlayerStats> result = new HashMap<String, PlayerStats>();
        for (GameMap map : mapsPlayed)
        {
            if (map.getMapName().equals(stats.getName()))
            {
                Map<String, PlayerStats> mapResult = map.getPlayerStats();
                for (Map.Entry<String, PlayerStats> e : mapResult.entrySet())
                {
                    if (result.containsKey(e.getKey()))
                    {
                        result.get(e.getKey()).add(e.getValue());
                    }
                    else
                    {
                        result.put(e.getKey(), e.getValue());
                    }
                }
            }
        }
        return result;
    }

    public Map<String, PlayerStats> getPlayerStats(WeaponStats stats)
    {
        Map<String, PlayerStats> result = new HashMap<String, PlayerStats>();
        for (GameMap map : mapsPlayed)
        {
            Map<String, PlayerStats> mapResult = map.getPlayerStats(stats);
            for (Map.Entry<String, PlayerStats> e : mapResult.entrySet())
            {
                if (result.containsKey(e.getKey()))
                {
                    result.get(e.getKey()).add(e.getValue());
                }
                else
                {
                    result.put(e.getKey(), e.getValue());
                }
            }
        }
        return result;
    }

    public Map<Weapon, WeaponStats> getWeaponStats()
    {
        Map<Weapon, WeaponStats> result = new HashMap<Weapon, WeaponStats>();
        for (GameMap map : mapsPlayed)
        {
            Map<Weapon, WeaponStats> mapResult = map.getWeaponStats();
            for (Weapon w : mapResult.keySet())
            {
                if (result.containsKey(w))
                {
                    result.get(w).add(mapResult.get(w));
                }
                else
                {
                    result.put(w, mapResult.get(w));
                }
            }
        }
        return result;
    }

    public Map<Weapon, WeaponStats> getWeaponStats(PlayerStats stats)
    {
        Map<Weapon, WeaponStats> result = new HashMap<Weapon, WeaponStats>();
        for (GameMap map : mapsPlayed)
        {
            Map<Weapon, WeaponStats> mapResult = map.getWeaponStats(stats);
            for (Weapon w : mapResult.keySet())
            {
                if (result.containsKey(w))
                {
                    result.get(w).add(mapResult.get(w));
                }
                else
                {
                    result.put(w, mapResult.get(w));
                }
            }
        }
        return result;
    }

    public Map<Weapon, WeaponStats> getWeaponStats(MapStats stats)
    {
        Map<Weapon, WeaponStats> result = new HashMap<Weapon, WeaponStats>();
        for (GameMap map : mapsPlayed)
        {
            if (map.getMapName().equals(stats.getName()))
            {
                Map<Weapon, WeaponStats> mapResult = map.getWeaponStats();
                for (Weapon w : mapResult.keySet())
                {
                    if (result.containsKey(w))
                    {
                        result.get(w).add(mapResult.get(w));
                    }
                    else
                    {
                        result.put(w, mapResult.get(w));
                    }
                }
            }
        }
        return result;
    }

    public Map<String, MapStats> getMapStats()
    {
        Map<String, MapStats> result = new HashMap<String, MapStats>();
        for (GameMap m : mapsPlayed)
        {
            MapStats stats = new MapStats(m);
            if (result.containsKey(stats.getName()))
            {
                result.get(stats.getName()).add(stats);
            }
            else
            {
                result.put(stats.getName(), stats);
            }
        }
        return result;
    }

    public Map<String, MapStats> getMapStats(PlayerStats p)
    {
        Map<String, MapStats> result = new HashMap<String, MapStats>();
        for (GameMap m : mapsPlayed)
        {
            if (m.getPlayers().containsKey(p.getName()))
            {
                MapStats stats = new MapStats(m, p);
                if (result.containsKey(stats.getName()))
                {
                    result.get(stats.getName()).add(stats);
                }
                else
                {
                    result.put(stats.getName(), stats);
                }
            }
        }
        return result;
    }

    public Map<String, MapStats> getMapStats(WeaponStats p)
    {
        Map<String, MapStats> result = new HashMap<String, MapStats>();
        for (GameMap m : mapsPlayed)
        {
            MapStats stats = new MapStats(m, p);
            if (result.containsKey(stats.getName()))
            {
                result.get(stats.getName()).add(stats);
            }
            else
            {
                result.put(stats.getName(), stats);
            }
        }
        return result;
    }

    public String toHTMLString(String detailLink)
    {
        StringBuffer result = new StringBuffer();
        result
                .append("<td align=\"left\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        if (detailLink != null)
        {
            result.append("<a href=\"");
            result.append(detailLink);
            result.append("\" alt=\"Details\">");
            result.append(sdf.format(gameStarted));
            result.append("</a>");
        }
        else
        {
            result.append(sdf.format(gameStarted));
        }

        result.append("</font></td>");
        result
                .append("<td align=\"left\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(sdf.format(gameStopped));
        result.append("</font></td>");
        result
                .append("<td align=\"center\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(getMapsPlayedAsString());
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(getMaxPlayers());
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(getTotalKills());
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(getTotalHeadshots());
        result.append("</font></td>");
        result
                .append("<td align=\"left\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append("<img src=\"" + HS_BAR_IMAGE + "\" width=\""
                + getHeadshotPercentageAsString()
                + "%\" height=10 border=0 alt=\""
                + getHeadshotPercentageAsString() + "%\"");
        result.append("</font></td>");
        result
                .append("<td align=\"right\" bgcolor=\"#FFFFFF\"><font size=2 class=\"fontNormal\">");
        result.append(getTotalDamage());
        result.append("</font></td>");

        return result.toString();
    }

    private int getMaxPlayers()
    {
        int result = 0;
        for (GameMap m : mapsPlayed)
        {
            if (m.getNoOfPlayers() > result)
            {
                result = m.getNoOfPlayers();
            }
        }
        return result;
    }

    private String getMapsPlayedAsString()
    {
        StringBuffer result = new StringBuffer();
        for (GameMap g : mapsPlayed)
        {
            result.append(g.getMapName());
            result.append("<br>");
        }
        result.delete(result.length() - 4, result.length());
        return result.toString();
    }

    private int getTotalKills()
    {
        int result = 0;
        for (GameMap g : mapsPlayed)
        {
            result += g.getTotalKills();
        }
        return result;
    }

    private int getTotalDamage()
    {
        int result = 0;
        for (GameMap g : mapsPlayed)
        {
            result += g.getTotalDamage();
        }
        return result;
    }

    private int getTotalHeadshots()
    {
        int result = 0;
        for (GameMap g : mapsPlayed)
        {
            result += g.getTotalHeadshots();
        }
        return result;
    }

    private String getHeadshotPercentageAsString()
    {
        return new DecimalFormat("##0.00").format((float) getTotalHeadshots()
                / (float) getTotalKills() * 100);
    }
}
