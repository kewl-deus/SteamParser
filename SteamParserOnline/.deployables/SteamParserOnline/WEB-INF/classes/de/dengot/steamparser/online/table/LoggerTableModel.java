package de.dengot.steamparser.online.table;

import java.util.Collection;

import cx.ath.janiwe.util.web.SimpleTableModel;
import de.dengot.steamparser.net.UdpLogger;

public class LoggerTableModel extends SimpleTableModel
{

    public LoggerTableModel(Collection<UdpLogger> loggers)
    {
        for (UdpLogger logger : loggers)
        {
            LoggerTableRow row = new LoggerTableRow(logger);
            super.addRow(row);
        }
    }

}
