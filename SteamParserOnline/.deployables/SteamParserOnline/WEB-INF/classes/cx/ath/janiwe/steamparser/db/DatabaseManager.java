package cx.ath.janiwe.steamparser.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import cx.ath.janiwe.steamparser.Config;
import cx.ath.janiwe.steamparser.logic.Damage;
import cx.ath.janiwe.steamparser.logic.Game;
import cx.ath.janiwe.steamparser.logic.GameMap;
import cx.ath.janiwe.steamparser.logic.Kill;
import cx.ath.janiwe.steamparser.logic.Player;

public class DatabaseManager
{
    private static final String PLAYERS_FILE = "players.sql";

    private static DatabaseManager singleton;

    private Connection c;

    private DatabaseManager() throws SQLException
    {
        c = createConnection();
    }

    public static DatabaseManager getInstance() throws SQLException
    {
        if (singleton == null)
        {
            singleton = new DatabaseManager();
        }
        return singleton;
    }

    public Connection getConnection() throws SQLException
    {
        if (c.isClosed())
        {
            c = createConnection();
        }
        return c;
    }

    private Connection createConnection() throws SQLException
    {
        try
        {
            Class.forName(Config.getInstance().getDbDriver());
        }
        catch (Exception e)
        {
            System.out.println("ERROR: failed to load JDBC driver.");
            e.printStackTrace();
        }
        String uri = Config.getInstance().getDbUri();
        String user = Config.getInstance().getDbUser();
        String pw = Config.getInstance().getDbPass();
        return DriverManager.getConnection(uri, user, pw);
    }

    private void readIntoDB(String location, String filename)
    {
        BufferedReader r = new BufferedReader(new InputStreamReader(Config
                .getInstance().getResourceStream(location, filename)));
        StringBuffer stmt = new StringBuffer();
        String line;
        try
        {
            while ((line = r.readLine()) != null)
            {
                stmt.append(line);
                if (line.endsWith(";"))
                {
                    try
                    {
                        getConnection().createStatement().execute(
                                stmt.toString());
                    }
                    catch (SQLException sqlEx)
                    {
                        System.out.println(sqlEx);
                    }
                    stmt = new StringBuffer();
                }
            }
        }
        catch (IOException ioEx)
        {
            System.out.println(ioEx);
        }
    }

    public void createDB()
    {
        // Create Database (Tables, Views, etc.)
        this.readIntoDB(this.getClass().getPackage().getName(), Config
                .getInstance().getDbCreateFile());

        // Insert Predefined Players and Nicks
        this.readIntoDB(this.getClass().getPackage().getName(), PLAYERS_FILE);
    }

//    public void put(Game g, String gameType) throws SQLException
//    {
//        StorageService ss = StorageService.getInstance();
//        try
//        {
//            ss.put(g, gameType);
//        }
//        catch (SQLException e)
//        {
//            GlobalExceptionLogger.getInstance().log(ss, e);
//        }
//    }

    /**
     * @param g
     * @param gameType
     * @throws SQLException
     * @deprecated Use StorageService.put(Game, String) instead.
     */
    public void deprecatedPut(Game g, String gameType) throws SQLException
    {
        StringBuffer values = new StringBuffer();
        int gameId = getNextGameId();
        values.append("'");
        values.append(gameId);
        values.append("','");
        values.append(new Timestamp(g.getGameStarted().getTime()));
        values.append("','");
        values.append(new Timestamp(g.getGameStopped().getTime()));
        values.append("','");
        values.append(gameType);
        values.append("'");

        getConnection().createStatement().execute(
                "INSERT INTO t_session(id, gameStarted, gameStopped, gameType) VALUES ("
                        + values.toString() + ");");
        g.setId(gameId);

        for (GameMap m : g.getMapsPlayed())
        {
            deprecatedPut(m);
            for (Player p : m.getPlayers().values())
            {
                deprecatedPut(p);

            }
            for (Player p : m.getPlayers().values())
            {
                for (Kill k : p.getKills())
                {
                    deprecatedPut(k, m.getMapName(), gameId, gameType);
                }
                for (Damage d : p.getDamageGiven())
                {
                    deprecatedPut(d, m.getMapName(), gameId, gameType);
                }
            }
        }
    }

    /**
     * @return
     * @throws SQLException
     * @deprecated Use StorageService.getNextGameId() instead.
     */
    private int getNextGameId() throws SQLException
    {
        Statement s = getConnection().createStatement();
        s.execute(Config.getInstance().getDbSeqCall());
        ResultSet rs = s.getResultSet();
        rs.next();
        int i = rs.getInt(1);
        return i;
    }

    /**
     * @param p
     * @throws SQLException
     * @deprecated Use StorageService.put(Player) instead.
     */
    private void deprecatedPut(Player p) throws SQLException
    {
        try
        {
            getConnection().createStatement().execute(
                    "INSERT INTO t_nickname(nickname) VALUES ('" + p.getName()
                            + "');");
        }
        catch (SQLException ex)
        {
            System.out.println(ex);
        }
    }

    /**
     * @param k
     * @param map
     * @param gameSession
     * @param gameType
     * @throws SQLException
     * @deprecated Use StorageService.collect(Kill, String, int, String)) instead.
     */
    private void deprecatedPut(Kill k, String map, int gameSession, String gameType)
            throws SQLException
    {
        StringBuffer values = new StringBuffer();
        values.append("'");
        values.append(k.getKiller().getName());
        values.append("','");
        values.append(k.getGotKilled().getName());
        values.append("','");
        values.append(k.getWeapon().name());
        values.append("','");
        values.append(map);
        values.append("',");
        values.append(gameSession);
        values.append(",");
        // values.append(bitPrefix + (k.isHeadshot() ? 1 : 0) + bitPostfix);
        values.append(k.isHeadshot());
        values.append(",'");
        values.append(gameType);
        values.append("'");
        try
        {
            getConnection()
                    .createStatement()
                    .execute(
                            "INSERT INTO t_kill(attacker, victim, weapon, map, gameSession, headshot, gameType) VALUES("
                                    + values.toString() + ");");
        }
        catch (SQLException ex)
        {
            System.out.println(ex);
        }
    }

    /**
     * @param d
     * @param map
     * @param gameSession
     * @param gameType
     * @throws SQLException
     * @deprecated Use StorageService.collect(Damage, String, int, String)) instead.
     */
    private void deprecatedPut(Damage d, String map, int gameSession, String gameType)
            throws SQLException
    {
        StringBuffer values = new StringBuffer();
        values.append("'");
        values.append(d.getAttacker().getName());
        values.append("','");
        values.append(d.getVictim().getName());
        values.append("','");
        values.append(d.getWeapon().name());
        values.append("','");
        values.append(map);
        values.append("',");
        values.append(gameSession);
        values.append(",");
        values.append(d.getDamage());
        values.append(",");
        values.append(d.getArmorDamage());
        values.append(",'");
        values.append(d.getHitgroup().name());
        values.append("','");
        values.append(gameType);
        values.append("'");
        try
        {
            getConnection()
                    .createStatement()
                    .execute(
                            "INSERT INTO t_damage(attacker, victim, weapon, map, gameSession, healthDamage, armorDamage, hitgroup, gameType) VALUES("
                                    + values.toString() + ");");
        }
        catch (SQLException sqlEx)
        {
            System.out.println(sqlEx);
        }
    }

    /**
     * @param m
     * @throws SQLException
     * @deprecated Use StorageService.put(GameMap) instead.
     */
    private void deprecatedPut(GameMap m) throws SQLException
    {
        try
        {
            getConnection().createStatement().execute(
                    "INSERT INTO t_map(mapName) VALUES ('" + m.getMapName()
                            + "');");
        }
        catch (SQLException sqlEx)
        {
            System.out.println(sqlEx);
        }
    }

    

}
