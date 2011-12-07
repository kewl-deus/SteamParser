package de.dengot.steamparser.logic;

import java.io.StringReader;

import cx.ath.janiwe.steamparser.logic.Game;
import cx.ath.janiwe.steamparser.parser.CounterStrikeParser;
import de.dengot.steamparser.net.LogObserver;

public class UdpLogParserMediator implements LogObserver
{
    private String gameType;

    private CounterStrikeParser parser;

    public UdpLogParserMediator(String gameType)
    {
        this.gameType = gameType;
        this.parser = new CounterStrikeParser(GlobalExceptionLogger
                .getInstance().getLoggerFor(this.parser));
    }

    public void update(String logdata)
    {
        String cleanLog = logdata.replaceAll("RL", "\nL");
        StringReader reader = new StringReader(cleanLog);
        try
        {
            Game theGame = parser.parse(reader);
            StorageService.getInstance().put(theGame, this.gameType);
        }
        catch (Exception e)
        {
            GlobalExceptionLogger.getInstance().log(this, e);
        }
    }

    public String getGameType()
    {
        return gameType;
    }

    public void setGameType(String gameType)
    {
        this.gameType = gameType;
    }

    public String toString()
    {
        return this.gameType + "-parser";
    }

}
