package de.dengot.steamparser.logic;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import cx.ath.janiwe.steamparser.db.DBFilter;
import cx.ath.janiwe.steamparser.db.DatabaseManager;
import cx.ath.janiwe.steamparser.logic.Weapon;
import cx.ath.janiwe.steamparser.stats.GameStats;
import cx.ath.janiwe.steamparser.stats.MapStats;
import cx.ath.janiwe.steamparser.stats.PlayerStats;
import cx.ath.janiwe.steamparser.stats.WeaponStats;
import de.dengot.steamparser.model.QueryablePlayer;
import de.dengot.steamparser.model.StatsType;

public class QueryService {

	private static final String COMPUTER_NICK = "Computer";
	
	public static List<QueryablePlayer> getPlayerSessionStats()
    {
        List<QueryablePlayer> result = new Vector<QueryablePlayer>();
        try
        {
            Statement stmt = getConnection().createStatement();
            String sql = "select ss.name as player, ss.gamestarted as session, ss.kills_in_session, ss.deaths_in_session, "
                    + " ss.headshots_in_session, ss.nonheadshots_in_session, ss.frags_in_session "
                    + " from v_sessionstats_allplayers as ss"
                    // nur die besten 5 Spieler anzeigen
                    // + ",(select attacker, count(attacker) as kills from
                    // v_kill group by attacker order by kills desc limit 5) as
                    // topkillers "
                    // + " where ss.name like topkillers.attacker "
                    + " order by ss.gamestarted, kills_in_session desc, ss.name ";
            stmt.executeQuery(sql);
            ResultSet rs = stmt.getResultSet();

            HashMap<String, QueryablePlayer> players = new HashMap<String, QueryablePlayer>();
            HashMap<QueryablePlayer, Integer> lastKillCumulValueForPlayer = new HashMap<QueryablePlayer, Integer>();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd - HH:mm");

            while (rs.next())
            {
                // read database
                String playerName = rs.getString("player");

                Timestamp sessionStart = rs.getTimestamp("session");
                // String session = rs.getString("session");
                String session = sdf.format(sessionStart);

                int kills = rs.getInt("kills_in_session");
                int deaths = rs.getInt("deaths_in_session");
                int headshots = rs.getInt("headshots_in_session");
                int frags = rs.getInt("frags_in_session");
                int nonHeadshotKills = rs.getInt("nonheadshots_in_session");
                double killsPerDeath = ((double) kills)
                        / ((double) (deaths > 0 ? deaths : 1));
                double headshotPercentage = ((double) headshots)
                        / ((double) (kills > 0 ? kills : 1));

                // get PlayerObject
                QueryablePlayer player = players.get(playerName);
                if (player == null)
                {
                    player = new QueryablePlayer(playerName);
                    players.put(playerName, player);
                }

                // add infos to player
                player.put(session, StatsType.KILLS, kills);
                player.put(session, StatsType.DEATHS, deaths);
                player.put(session, StatsType.KILLS_PER_DEATH, killsPerDeath);
                player.put(session, StatsType.FRAGS, frags);
                player.put(session, StatsType.HEADSHOTS, headshots);
                player.put(session, StatsType.NON_HEADSHOT_KILLS,
                        nonHeadshotKills);
                player.put(session, StatsType.HEADSHOT_PERCENTAGE,
                        headshotPercentage);

                // calculate cumulation
                Integer oldValue = lastKillCumulValueForPlayer.get(player);
                if (oldValue == null)
                {
                    oldValue = new Integer(0);
                }
                int newValue = oldValue + kills;
                lastKillCumulValueForPlayer.put(player, newValue);
                // cumul-kills-value für einen player wird ständig überschrieben
                player.put(session, StatsType.CUMUL_KILLS, newValue);

            }
            result.addAll(players.values());
            return result;
        }
        catch (SQLException sqlEx)
        {
            System.out.println(sqlEx);
        }
        finally
        {

        }
        return result;
    }

    public static List<PlayerStats> getPlayerStats(DBFilter... filters)
    {
        String attackerWhere = null;
        if (filters.length > 0)
        {
            StringBuffer attackerFilter = new StringBuffer("WHERE ");
            for (int i = 0; i < filters.length; i++)
            {
                attackerFilter.append(filters[i].getAttackerPart());
                if (i + 1 < filters.length)
                    attackerFilter.append(" AND ");
            }
            attackerWhere = attackerFilter.toString();
        }
        String victimWhere = null;
        if (filters.length > 0)
        {
            StringBuffer victimFilter = new StringBuffer("WHERE ");
            for (int i = 0; i < filters.length; i++)
            {
                victimFilter.append(filters[i].getVictimPart());
                if (i + 1 < filters.length)
                    victimFilter.append(" AND ");
            }
            victimWhere = victimFilter.toString();
        }
        try
        {
            Statement stmt = getConnection().createStatement();

            attackerWhere = coalesce(attackerWhere);
            victimWhere = coalesce(victimWhere);
            stmt
                    .execute("SELECT killer, coalesce(kills,0) as coal_kills, coalesce(headshots,0) as coal_hs, coalesce(deaths,0) as coal_deaths, coalesce(damageGiven,0) as coal_damGiven, coalesce(damageTaken,0) as coal_DamTaken, round(fphSelect.fph,2), sessionsPlayed FROM "
                            + "(SELECT k.attacker AS killer, COUNT(k.*) AS kills  FROM v_kill k  "
                            + attackerWhere
                            + " GROUP BY  k.attacker) killSelect LEFT OUTER JOIN "
                            + "(SELECT k.victim, COUNT(k.*) AS deaths  FROM v_kill k "
                            + victimWhere
                            + " GROUP BY k.victim) deathSelect ON killSelect.killer = deathSelect.victim LEFT OUTER JOIN "
                            + "(SELECT k.attacker AS headshotter, COUNT (k.*) AS headshots FROM v_kill k "
                            + (isLogicalNull(attackerWhere) ? "WHERE headshot" : attackerWhere + " AND headshot")
                            + " GROUP BY  headshotter) hsSelect ON killSelect.killer = hsSelect.headshotter LEFT OUTER JOIN "
                            + "(SELECT d.attacker, SUM(d.healthDamage+d.armorDamage) AS damageGiven FROM v_damage d "
                            + attackerWhere
                            + " GROUP BY  attacker) damGivenSelect ON killSelect.killer = damGivenSelect.attacker LEFT OUTER JOIN "
                            + "(SELECT d.victim AS damVictim, SUM(d.healthDamage+d.armorDamage) AS damageTaken FROM v_damage d "
                            + victimWhere
                            + " GROUP BY  damVictim) damTakenSelect ON killSelect.killer = damTakenSelect.damVictim LEFT OUTER JOIN "
                            + "(SELECT v.attacker AS fph_attacker, SUM(v.kills)*1.0000000000000000 / (SUM(v.timeplayed)/60.00) AS fph FROM v_kills_per_time v GROUP BY v.attacker) fphSelect ON killSelect.killer = fphSelect.fph_attacker LEFT OUTER JOIN "
                            + "(SELECT DISTINCT k.attacker AS sum_attacker, COUNT(DISTINCT k.gamesession) AS sessionsPlayed FROM v_kill k "
                            + attackerWhere
                            + "GROUP BY sum_attacker) sessionSumSelect ON killSelect.killer = sessionSumSelect.sum_attacker "
                            + "ORDER BY coal_kills DESC, coal_deaths ASC, coal_damGiven DESC");

            Vector<PlayerStats> result = new Vector<PlayerStats>();
            ResultSet rs = stmt.getResultSet();
            while (rs.next())
            {
                if (rs.getString("killer").equals(COMPUTER_NICK))
                {
                    // Achtung: Hier wird im IF-Test der RS-Cursor 1 weiter
                    // geschoben
                    // sollte es keinen weiteren Satz, wird sinnigerweise aus
                    // der Schleife gesprungen
                    if (!rs.next())
                        break;
                }
                PlayerStats stats = new PlayerStats(rs.getString(1), rs
                        .getInt(2), rs.getInt(3), rs.getInt(4), rs.getInt(5),
                        rs.getInt(6), rs.getFloat(7), rs.getInt(8));
                result.add(stats);
            }
            return result;
        }
        catch (SQLException sqlEx)
        {
            System.out.println(sqlEx);
            return new Vector<PlayerStats>();
        }
    }

    public static List<WeaponStats> getWeaponStats(DBFilter... filters)
    {
        String attackerWhere = null;
        if (filters.length > 0)
        {
            StringBuffer attackerFilter = new StringBuffer("WHERE ");
            for (int i = 0; i < filters.length; i++)
            {
                attackerFilter.append(filters[i].getAttackerPart());
                if (i + 1 < filters.length)
                    attackerFilter.append(" AND ");
            }
            attackerWhere = attackerFilter.toString();
        }
        try
        {
            Statement stmt = getConnection().createStatement();

            attackerWhere = coalesce(attackerWhere);
            stmt
                    .execute("SELECT killWeapon, coalesce(kills,0) as coal_kills, coalesce(headshots,0), coalesce(totalDamage,0) as coal_dam FROM "
                            + "(SELECT k.weapon AS killWeapon, COUNT(k.*) AS kills FROM v_kill k "
                            + attackerWhere
                            + " GROUP BY killWeapon) killSelect LEFT OUTER JOIN "
                            + "(SELECT d.weapon AS damWeapon, sum(d.healthDamage + d.armorDamage) AS totalDamage FROM v_damage d "
                            + attackerWhere
                            + " GROUP BY damWeapon) damGivenSelect ON killSelect.killWeapon = damGivenSelect.damWeapon LEFT OUTER JOIN "
                            + "(SELECT k.weapon AS hsWeapon, COUNT (k.*) AS headshots FROM v_kill k "
                            + (isLogicalNull(attackerWhere) ? "WHERE headshot"
                                    : attackerWhere + " AND headshot")
                            + " GROUP BY hsWeapon) hsSelect ON killSelect.killWeapon = hsSelect.hsWeapon "
                            + "ORDER BY coal_kills DESC, coal_dam DESC");

            Vector<WeaponStats> result = new Vector<WeaponStats>();
            ResultSet rs = stmt.getResultSet();
            while (rs.next())
            {
                WeaponStats stats = new WeaponStats(Weapon.valueOf(rs
                        .getString(1)), rs.getInt(2), rs.getInt(3), rs
                        .getInt(4));
                result.add(stats);
            }
            return result;
        }
        catch (SQLException sqlEx)
        {
            System.out.println(sqlEx);
            return new Vector<WeaponStats>();
        }
    }

    public static List<MapStats> getMapStats(DBFilter... filters)
    {
        String attackerWhere = null;
        if (filters.length > 0)
        {
            StringBuffer attackerFilter = new StringBuffer("WHERE ");
            for (int i = 0; i < filters.length; i++)
            {
                attackerFilter.append(filters[i].getAttackerPart());
                if (i + 1 < filters.length)
                    attackerFilter.append(" AND ");
            }
            attackerWhere = attackerFilter.toString();
        }
        try
        {
            Statement stmt = getConnection().createStatement();

            attackerWhere = coalesce(attackerWhere);
            stmt
                    .execute("SELECT gameMap, coalesce(kills,0) as coal_kills, coalesce(headshots,0), coalesce(totalDamage,0) as coal_Dam FROM "
                            + "(SELECT k.map AS gameMap, COUNT(k.*) AS kills FROM v_kill k "
                            + attackerWhere
                            + " GROUP BY gameMap) killSelect LEFT OUTER JOIN "
                            + "(SELECT d.map AS damMap, SUM(d.healthDamage + d.armorDamage) AS totalDamage FROM v_damage d "
                            + attackerWhere
                            + " GROUP BY damMap) damSelect ON killSelect.gameMap = damSelect.damMap LEFT OUTER JOIN "
                            + "(SELECT k.map AS hsmap, COUNT(k.*) AS headshots FROM v_kill k "
                            + (isLogicalNull(attackerWhere) ? "WHERE headshot"
                                    : attackerWhere + " AND headshot")
                            + " GROUP BY hsmap) hsSelect ON killSelect.gameMap = hsSelect.hsMap "
                            + "ORDER BY coal_kills DESC, coal_Dam DESC");
            Vector<MapStats> result = new Vector<MapStats>();
            ResultSet rs = stmt.getResultSet();
            while (rs.next())
            {
                MapStats stats = new MapStats(rs.getString(1), rs.getInt(2), rs
                        .getInt(3), rs.getInt(4));
                result.add(stats);
            }
            return result;
        }
        catch (SQLException sqlEx)
        {
            System.out.println(sqlEx);
            return new Vector<MapStats>();
        }
    }

    public static List<GameStats> getGameStats(DBFilter... filters)
    {
        String attackerWhere = null;
        if (filters.length > 0)
        {
            StringBuffer attackerFilter = new StringBuffer("WHERE ");
            for (int i = 0; i < filters.length; i++)
            {
                attackerFilter.append(filters[i].getAttackerPart());
                if (i + 1 < filters.length)
                    attackerFilter.append(" AND ");
            }
            attackerWhere = attackerFilter.toString();
        }
        try
        {
            Statement stmt = getConnection().createStatement();
            attackerWhere = coalesce(attackerWhere);
            stmt
                    .execute("SELECT sid, gameStarted, gameStopped, coalesce(kills,0), coalesce(headshots,0), coalesce(totalDamage,0) FROM "
                            + "(SELECT k.gamesession AS sid, COUNT (k.*) AS kills FROM v_kill k "
                            + attackerWhere
                            + " GROUP BY sid) killSelect LEFT OUTER JOIN "
                            + "(SELECT d.gameSession AS dSid, SUM(d.healthDamage+d.armorDamage) AS totalDamage FROM v_damage d "
                            + attackerWhere
                            + " GROUP BY dSid) damSelect ON damSelect.dSid = killSelect.sid LEFT OUTER JOIN "
                            + "(SELECT s.id AS sessId, s.gameStarted, s.gameStopped FROM t_session s) sessionSelect ON killSelect.sid = sessionSelect.sessId LEFT OUTER JOIN"
                            + "(SELECT k.gamesession AS hSid, COUNT(k.*) AS headshots FROM v_kill k "
                            + (isLogicalNull(attackerWhere) ? "WHERE headshot"
                                    : attackerWhere + " AND headshot")
                            + " GROUP BY hSid) hsSelect ON killSelect.sid = hsSelect.hSid "
                            + "ORDER BY gameStarted DESC");
            Vector<GameStats> result = new Vector<GameStats>();
            ResultSet rs = stmt.getResultSet();
            while (rs.next())
            {
                // gespielte Maps
                Vector<String> mapsPlayed = new Vector<String>();
                Statement detStmt = getConnection().createStatement();
                detStmt
                        .execute("SELECT DISTINCT k.map FROM v_kill k where k.gamesession = "
                                + rs.getInt(1));
                ResultSet detRs = detStmt.getResultSet();
                while (detRs.next())
                {
                    mapsPlayed.add(detRs.getString(1));
                }
                // beteiligte Spieler
                // Prämisse: Jeder Spieler hat einen Kill gemacht, auf den union
                // mit k.victim wird hier verzichtet
                Vector<String> involvedPlayers = new Vector<String>();
                Statement playerStmt = getConnection().createStatement();
                ResultSet rsPlayers = playerStmt
                        .executeQuery("SELECT DISTINCT k.attacker FROM v_kill k where k.attacker not like '"
                                + COMPUTER_NICK
                                + "' and k.gamesession = "
                                + rs.getInt(1));
                while(rsPlayers.next())
                {
                    involvedPlayers.add(rsPlayers.getString("attacker"));
                }
                GameStats stats = new GameStats(rs.getInt(1), rs
                        .getTimestamp(2), rs.getTimestamp(3), mapsPlayed,
                        involvedPlayers, rs.getInt(4), rs.getInt(5), rs
                                .getInt(6));
                result.add(stats);
            }
            return result;
        }
        catch (SQLException sqlEx)
        {
            System.out.println(sqlEx);
            return new Vector<GameStats>();
        }
    }

    private static String coalesce(String input)
    {
        return coalesce(input, "");
    }

    private static String coalesce(String input, String alternativeOutput)
    {
        return input == null ? alternativeOutput : input;
    }

    private static boolean isLogicalNull(String input)
    {
        return input == null || input.length() == 0;
    }
	
    private static Connection getConnection() throws SQLException{
    	return DatabaseManager.getInstance().getConnection();
    }
}
