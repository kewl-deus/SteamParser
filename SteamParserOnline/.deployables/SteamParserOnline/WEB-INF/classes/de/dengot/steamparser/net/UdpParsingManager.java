package de.dengot.steamparser.net;

import java.net.SocketException;
import java.util.Collection;
import java.util.HashMap;

import cx.ath.janiwe.steamparser.Config;
import cx.ath.janiwe.steamparser.DefaultLogDir;

import de.dengot.steamparser.logic.GlobalExceptionLogger;
import de.dengot.steamparser.logic.LogfileWriter;
import de.dengot.steamparser.logic.UdpLogParserMediator;
import de.dengot.steamparser.model.UdpLoggerConfig;

public class UdpParsingManager
{
    private final String DEFAULT_NOTIFY_PATTERN = "server_message: \"quit\"";

    private static UdpParsingManager instance;

    private HashMap<Integer, UdpLogger> loggers;

    private UdpParsingManager()
    {
        this.loggers = new HashMap<Integer, UdpLogger>();
    }

    public static synchronized UdpParsingManager getInstance()
    {
        if (instance == null)
        {
            instance = new UdpParsingManager();
        }
        return instance;
    }

    /**
     * Creates and activates UdpLoggers defined in config.xml
     */
    public void createDefaultLoggers()
    {
        GlobalExceptionLogger el = GlobalExceptionLogger.getInstance();
        for (UdpLoggerConfig ulc : Config.getInstance().getUdpLoggerConfigs())
        {
            try
            {
                instance.createUdpLogParser(ulc.getPort(), ulc.getGameType(),
                        ulc.getNotifyPattern(), true);
                if (ulc.isWriteLogfile())
                {
                    for (DefaultLogDir dir : Config.getInstance().getLogs())
                    {
                        if (dir.getType().equals(ulc.getGameType()))
                        {
                            LogfileWriter lfw = new LogfileWriter(dir.getDir(),
                                    ulc.getGameType());
                            this.addObserver(ulc.getPort(), lfw);
                            break;
                        }
                    }
                }

            }
            catch (SocketException e)
            {
                el.log(instance, "Error by creating new UDP-Logger @ Port "
                        + ulc.getPort() + ". Details: ");
                el.log(instance, e);
            }
        }

    }

    /**
     * Create an UDP-Steam-Logger and given Port + a Parser for given gameType +
     * a LogfileWriter
     * 
     * @param port
     * @param gameType
     * @param notifyPattern
     * @param activate
     * @throws SocketException
     */
    public void createUdpLogParser(int port, String gameType,
            String notifyPattern, boolean activate) throws SocketException
    {
        // create Logger and Thread
        UdpLogger logger = this.loggers.get(port);
        if (logger == null)
        {
            logger = new UdpLogger(port, notifyPattern, activate);
            this.loggers.put(port, logger);
        }
        // Attach LogObservers
        // den Parser
        UdpLogParserMediator mediator = new UdpLogParserMediator(gameType);
        logger.addObserver(mediator);

        // Activate
        if (activate)
        {
            logger.start();
        }
    }

    public void setLoggerState(int port, boolean enable)
    {
        UdpLogger logger = this.loggers.get(port);
        if (logger != null)
        {
            logger.setEnabled(enable);
            if (enable & (!logger.isAlive()))
            {
                logger.start();
            }
        }
    }

    public synchronized void terminateLogger(int port)
    {
        UdpLogger logger = this.loggers.get(port);
        if (logger != null)
        {
            logger.interrupt();
            logger.removeAllObservers();
            logger.terminate();
            this.loggers.remove(port);
        }
    }

    public String getLogData(int port)
    {
        UdpLogger logger = this.loggers.get(port);
        if (logger != null)
        {
            return logger.getLogData();
        }
        else
        {
            return "no logger @ port " + port;
        }
    }

    public Collection<UdpLogger> getLoggers()
    {
        return this.loggers.values();
    }

    public boolean addObserver(int port, LogObserver observer)
    {
        UdpLogger logger = this.loggers.get(port);
        if (logger != null)
        {
            logger.addObserver(observer);
            return true;
        }
        else
        {
            return false;
        }
    }

    public void removeObserver(int port, LogObserver observer)
    {
        UdpLogger logger = this.loggers.get(port);
        if (logger != null)
        {
            logger.removeObserver(observer);
        }
    }

}
