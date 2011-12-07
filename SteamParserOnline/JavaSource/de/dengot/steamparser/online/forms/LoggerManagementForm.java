package de.dengot.steamparser.online.forms;

import org.apache.struts.action.ActionForm;

public class LoggerManagementForm extends ActionForm
{

    private int port = 0;

    private String gameType = "";

    private String notifyPattern = "server_message: \"quit\"";

    public LoggerManagementForm()
    {
        super();
    }

    public String getGameType()
    {
        return gameType;
    }

    public void setGameType(String gameType)
    {
        this.gameType = gameType;
    }

    public String getNotifyPattern()
    {
        return notifyPattern;
    }

    public void setNotifyPattern(String notifyPattern)
    {
        this.notifyPattern = notifyPattern;
    }

    public int getPortAsInt()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public void setPort(String port)
    {
        this.port = Integer.parseInt(port);
    }

    public String getPort()
    {
        return this.port + "";
    }

}
