package de.dengot.steamparser.logic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.dengot.steamparser.net.LogObserver;

public class LogfileWriter implements LogObserver
{
    private String path;

    private String gameType;

    private static final String DATE_FORMAT = "yyyyMMddHHmm";

    private SimpleDateFormat sdf;

    public LogfileWriter(String path, String gameType)
    {
        if (!path.endsWith("\\"))
        {
            this.path = path + "\\";
        }
        else
        {
            this.path = path;
        }
        this.gameType = gameType;
        sdf = new SimpleDateFormat(DATE_FORMAT);
    }

    public void update(String logdata)
    {
        String cleanLog = logdata.replaceAll("RL", "\nL");
        try
        {
            String strDate = sdf.format(new Date(System.currentTimeMillis()));
            String filename = this.path + this.gameType + strDate + ".log";
            BufferedWriter logwriter = new BufferedWriter(new FileWriter(
                    new File(filename)));
            logwriter.write(cleanLog);
            logwriter.flush();
            logwriter.close();
        }
        catch (IOException e)
        {
            GlobalExceptionLogger.getInstance().log(this, e);
        }
    }

    public String toString()
    {
        return "logfile-writer(" + this.path + this.gameType + "*.log)";
    }

}
