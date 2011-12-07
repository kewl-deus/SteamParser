/*
 * Created on 04.10.2005
 */
package cx.ath.janiwe.steamparser.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cx.ath.janiwe.steamparser.logic.Game;

public class CounterStrikeParser
{

    private static final String eventDateFormat = "MM/dd/yyyy - HH:mm:ss";

    private SimpleDateFormat sdf;

    private boolean cvars;

    private Writer errorReporter;

    public CounterStrikeParser()
    {
        this(new PrintWriter(System.out));
    }

    public CounterStrikeParser(Writer errorReporter)
    {
        sdf = new SimpleDateFormat(eventDateFormat);
        this.errorReporter = errorReporter;
    }

    public Game parse(InputStream is, Game game) throws IOException
    {
        return this.parse(new InputStreamReader(is), game);
    }

    /**
     * @param inputReader
     * @param game
     *            optional: Wenn übergeben, werden die Logdaten an dieses
     *            Game-Objekt angehangen
     * @return
     * @throws IOException
     */
    public Game parse(Reader inputReader, Game game) throws IOException
    {
        BufferedReader reader = new BufferedReader(inputReader);
        if (game == null)
        {
            game = new Game();
        }
        String line;
        while ((line = reader.readLine()) != null)
        {
            try
            {
                parseLine(game, line);
            }
            catch (StringIndexOutOfBoundsException e)
            {
            }
        }
        return game;
    }

    public Game parse(Reader inputReader) throws IOException
    {
        return this.parse(inputReader, new Game());
    }

    public Game parse(File csLogFile) throws FileNotFoundException, IOException
    {
        return parse(csLogFile, new Game());
    }

    public Game parse(File csLogFile, Game game) throws FileNotFoundException,
            IOException
    {
        InputStream inStream = new FileInputStream(csLogFile);
        return parse(inStream, game);
    }

    private void parseLine(Game game, String line)
            throws StringIndexOutOfBoundsException
    {
        line = line.substring(2);
        String eventDateString = line.substring(0, 21);
        line = line.substring(23);

        // Behandlung der cvars
        if (line.equals("server cvars start"))
        {
            cvars = true;
            return;
        }
        else if (line.equals("server cvars end"))
        {
            cvars = false;
            return;
        }
        if (cvars || line.startsWith("server_cvar: "))
        {
            return;
        }

        // EventDate parsen
        Date eventDate;
        try
        {
            eventDate = sdf.parse(eventDateString);
        }
        catch (ParseException pEx)
        {
            String errorMsg = "Fehler beim Parsen des EventDate, ignoriere Event!";
            try
            {
                this.errorReporter.write(errorMsg);
            }
            catch (IOException e)
            {
            }
            return;
        }

        // Personal-Events
        if (line.startsWith("\""))
        {

            // Username identifizieren
            int nextIdx = line.indexOf("\"", 1);
            String userComplex = line.substring(1, nextIdx);
            String userName = getNickFromComplex(userComplex);

            line = line.substring(nextIdx + 2);

            if (line.startsWith("attacked "))
            {
                String[] rest = line.split("\"");
                String victimNick = getNickFromComplex(rest[1]);
                String weaponString = rest[3];
                int damage = Integer.parseInt(rest[5]);
                int armorDamage = Integer.parseInt(rest[7]);
                String hitgroupString = rest[13];
                game.damage(userName, victimNick, weaponString, damage,
                        armorDamage, hitgroupString);
            }
            else if (line.startsWith("killed "))
            {
                String[] rest = line.split("\"");
                String victimNick = getNickFromComplex(rest[1]);
                String weaponString = rest[3];
                boolean headshot = line.endsWith("(headshot)");
                game.kill(userName, victimNick, weaponString, headshot);
            }
            else if (line.equals("entered the game")
                    || line.startsWith("connected, "))
            {
                game.playerConnected(userName, eventDate);
            }
            else if (line.startsWith("disconnected"))
            {
                game.playerDisconnected(userName, eventDate);
            }
            else if (line.startsWith("committed suicide"))
            {
                game.suicide(userName);
            }
            else
            {
                try
                {
                    this.errorReporter.write("Unbekanntes Personal-Event: "
                            + line +"\n");
                }
                catch (IOException e)
                {
                }
            }
            return;
        }

        // Map-Start
        if (line.startsWith("Started map \""))
        {
            int nextIdx = line.indexOf("\"", 13);
            String mapName = line.substring(13, nextIdx);
            game.newMap(mapName, eventDate);
            return;
        }

        // Map-Ende
        // if (line.equals("Log file closed")){
        // game.mapStopped(eventDate);
        // }
        // Server-Start
        if (line.startsWith("Log file started"))
        {
            if (game.getGameStarted() == null)
            {
                game.setGameStarted(eventDate);
            }
            return;
        }

        // Server-Quit
        if (line.equals("server_message: \"quit\""))
        {
            game.setGameStopped(eventDate);
            return;
        }
    }

    private String getNickFromComplex(String complex)
    {
        int userEndIdx = complex.indexOf("<");
        return complex.substring(0, userEndIdx);
    }
}
