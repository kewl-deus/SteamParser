package de.dengot.steamparser.logic;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GlobalExceptionLogger
{
    private static GlobalExceptionLogger instance;

    private Map<Object, StringWriter> errorlog;

    private GlobalExceptionLogger()
    {
        this.errorlog = new HashMap<Object, StringWriter>();
    }

    public static synchronized GlobalExceptionLogger getInstance()
    {
        if (instance == null)
        {
            instance = new GlobalExceptionLogger();
        }
        return instance;
    }

    public void log(Object thrower, Exception e)
    {
        this.log(thrower, e, true);
    }

    public void log(Object thrower, Exception e, boolean logStackTrace)
    {
        Writer logger = this.getLoggerFor(thrower);
        try
        {
            logger.write("ExceptionType: " + e.getClass().getName());
            logger.write("\n");
            logger.write("Throwtime: " + new Date().toString());
            logger.write("\n");
            logger.write(e.getMessage());
            
            if (e instanceof SQLException)
            {
                SQLException se = (SQLException) e;
                while(se.getNextException() != null)
                {
                    se = se.getNextException();
                    logger.write(se.getMessage());
                }
            }
            
            if (logStackTrace)
            {
                e.printStackTrace(new PrintWriter(logger));
            }
            logger.write("\n----------------------------------------\n");
        }
        catch (IOException ioe)
        {
            // Tritt niemals auf, da StringWriter
            // muss aber abgefangen werden, da in Interface Writer definiert
        }
    }

    public void log(Object thrower, String log)
    {
        Writer logger = this.getLoggerFor(thrower);
        try
        {
            logger.write(log);
        }
        catch (IOException e)
        {
        }
    }

    public String getLog(Object thrower)
    {
        StringWriter logger = this.errorlog.get(thrower);
        if (logger != null)
        {
            return logger.toString();
        }
        else
        {
            return "no Errors!";
        }
    }

    public void removeLog(Object thrower)
    {
        this.errorlog.remove(thrower);
    }

    public Set<Object> getRegisteredThrowers()
    {
        return new HashSet<Object>(errorlog.keySet());
    }

    public Writer getLoggerFor(Object thrower)
    {
        StringWriter logger = this.errorlog.get(thrower);
        if (logger == null)
        {
            logger = new StringWriter();
            this.errorlog.put(thrower, logger);
        }
        return logger;
    }

}
