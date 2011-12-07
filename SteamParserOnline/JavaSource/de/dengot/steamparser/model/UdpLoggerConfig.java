package de.dengot.steamparser.model;

public class UdpLoggerConfig
{
    private int port;

    private String gameType;

    private String notifyPattern;

    private boolean writeLogfile;

    public UdpLoggerConfig(int port, String gameType, String notifyPattern,
            boolean writeLogfile)
    {
        this.port = port;
        this.gameType = gameType;
        this.notifyPattern = notifyPattern;
        this.writeLogfile = writeLogfile;
    }

    public String getGameType()
    {
        return gameType;
    }

    public String getNotifyPattern()
    {
        return notifyPattern;
    }

    public int getPort()
    {
        return port;
    }

    public boolean isWriteLogfile()
    {
        return writeLogfile;
    }
}
