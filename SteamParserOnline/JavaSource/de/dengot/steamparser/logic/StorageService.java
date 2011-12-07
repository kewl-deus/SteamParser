package de.dengot.steamparser.logic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import cx.ath.janiwe.steamparser.Config;
import cx.ath.janiwe.steamparser.db.DatabaseManager;
import cx.ath.janiwe.steamparser.logic.Damage;
import cx.ath.janiwe.steamparser.logic.Game;
import cx.ath.janiwe.steamparser.logic.GameMap;
import cx.ath.janiwe.steamparser.logic.Kill;
import cx.ath.janiwe.steamparser.logic.Player;

public class StorageService
{
    private static StorageService instance;

    private PreparedStatement pstmtPutGameSession;

    private PreparedStatement pstmtPutDamage;

    private PreparedStatement pstmtPutKill;

    private PreparedStatement pstmtPutPlayer;

    private PreparedStatement pstmtGetNextGameSessionId;

    private PreparedStatement pstmtPutMap;

    public static synchronized StorageService getInstance() throws SQLException
    {
        if (instance == null)
        {
            instance = new StorageService();
        }
        return instance;
    }

    private StorageService() throws SQLException
    {
        Connection con = DatabaseManager.getInstance().getConnection();
        this.pstmtGetNextGameSessionId = con.prepareStatement(Config
                .getInstance().getDbSeqCall());
        this.pstmtPutGameSession = con
                .prepareStatement("INSERT INTO t_session(id, gameStarted, gameStopped, gameType) VALUES (?, ?, ?, ?)");
        this.pstmtPutPlayer = con
                .prepareStatement("INSERT INTO t_nickname(nickname) VALUES (?)");
        this.pstmtPutDamage = con
                .prepareStatement("INSERT INTO t_damage(attacker, victim, weapon, map, gameSession, healthDamage, armorDamage, hitgroup, gameType) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        this.pstmtPutKill = con
                .prepareStatement("INSERT INTO t_kill(attacker, victim, weapon, map, gameSession, headshot, gameType) VALUES(?, ?, ?, ?, ?, ?, ?)");
        this.pstmtPutMap = con
                .prepareStatement("INSERT INTO t_map(mapName) VALUES (?)");

    }

    /**
     * Die GameSession selbst ist nur 1 Datensatz, daher kein Batch-Insert aber
     * die in der Session enthaltenen Elemente KILL und DAMAGE sind zahlreich
     * und werden per Batch eingefügt
     * 
     * @param g
     * @param gameType
     * @throws SQLException
     */
    public synchronized void put(Game g, String gameType) throws SQLException
    {
        int gameId = getNextGameId();
        this.pstmtPutGameSession.clearParameters();
        this.pstmtPutGameSession.setInt(1, gameId);
        Timestamp startedStamp = new Timestamp(g.getGameStarted().getTime());
        this.pstmtPutGameSession.setTimestamp(2, startedStamp);
        Timestamp stoppedStamp = new Timestamp(g.getGameStopped().getTime());
        this.pstmtPutGameSession.setTimestamp(3, stoppedStamp);
        this.pstmtPutGameSession.setString(4, gameType);
        int rcSessionInsert = this.pstmtPutGameSession.executeUpdate();
        System.out.print("********* GameSession No. " + gameId
                + " inserted? rc: " + rcSessionInsert + "\n");
        if (rcSessionInsert == 1)
        {
            g.setId(gameId);
            this.pstmtPutDamage.clearBatch();
            this.pstmtPutKill.clearBatch();

            for (GameMap m : g.getMapsPlayed())
            {
                this.put(m);
                for (Player p : m.getPlayers().values())
                {
                    this.put(p);

                }
                for (Player p : m.getPlayers().values())
                {
                    for (Kill k : p.getKills())
                    {
                        collect(k, m.getMapName(), gameId, gameType);
                    }
                    for (Damage d : p.getDamageGiven())
                    {
                        collect(d, m.getMapName(), gameId, gameType);
                    }
                }
            }
            int[] rcKills = this.pstmtPutKill.executeBatch();
            int[] rcDamages = this.pstmtPutDamage.executeBatch();
        }
    }

    /**
     * Für die nächste Gamesession wird die ID sofort benötigt, es handelt sich
     * auch um eine Query und keinen Insert, somit kein Batch!
     * 
     * @return
     * @throws SQLException
     */
    private synchronized int getNextGameId() throws SQLException
    {
        ResultSet rs = this.pstmtGetNextGameSessionId.executeQuery();
        rs.next();
        int i = rs.getInt(1);
        return i;
    }

    /**
     * Player sind sehr wenig Datensätze, daher kein Batch sondern direkter
     * Insert
     * 
     * @param p
     * @throws SQLException
     */
    private synchronized void put(Player p)
    {
        try
        {
            this.pstmtPutPlayer.clearParameters();
            this.pstmtPutPlayer.setString(1, p.getName());
            this.pstmtPutPlayer.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }

    /**
     * Kill sind viele Datensätze, daher Batch-Insert es werden nur Parameter
     * für Batch gesammelt, aber die Stapelverarbeitung nicht angestoßen
     * 
     * @param k
     * @param map
     * @param gameSession
     * @param gameType
     * @throws SQLException
     */
    private synchronized void collect(Kill k, String map, int gameSession,
            String gameType) throws SQLException
    {
        PreparedStatement p = this.pstmtPutKill;
        int i = 0;
        p.clearParameters();
        p.setString(++i, k.getKiller().getName());
        p.setString(++i, k.getGotKilled().getName());
        p.setString(++i, k.getWeapon().name());
        p.setString(++i, map);
        p.setInt(++i, gameSession);
        p.setBoolean(++i, k.isHeadshot());
        p.setString(++i, gameType);
        p.addBatch();
    }

    /**
     * Damage sind viele Datensätze, daher Batch-Insert es werden nur Parameter
     * für Batch gesammelt, aber die Stapelverarbeitung nicht angestoßen
     * 
     * @param d
     * @param map
     * @param gameSession
     * @param gameType
     * @throws SQLException
     */
    private synchronized void collect(Damage d, String map, int gameSession,
            String gameType) throws SQLException
    {
        PreparedStatement p = this.pstmtPutDamage;
        int i = 0;
        p.clearParameters();
        p.setString(++i, d.getAttacker().getName());
        p.setString(++i, d.getVictim().getName());
        p.setString(++i, d.getWeapon().name());
        p.setString(++i, map);
        p.setInt(++i, gameSession);
        p.setInt(++i, d.getDamage());
        p.setInt(++i, d.getArmorDamage());
        p.setString(++i, d.getHitgroup().name());
        p.setString(++i, gameType);
        p.addBatch();
    }

    /**
     * Maps sind relativ wenig Datensätze, daher kein Batch sondern direkter
     * Insert SQL-Exception intern abgefangen: "duplicate key violates unique
     * constraint" wird erwartet
     * 
     * @param m
     * 
     */
    private synchronized void put(GameMap m)
    {
        try
        {
            this.pstmtPutMap.clearParameters();
            this.pstmtPutMap.setString(1, m.getMapName());
            this.pstmtPutMap.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println(e);
        }
    }

}
