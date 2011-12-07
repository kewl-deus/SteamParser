package de.dengot.steamparser.online.table;

import java.util.Iterator;

import de.dengot.steamparser.net.UdpLogger;
import de.hdi.gui.web.taglib.table.TableRowAdapter;

public class LoggerTableRow extends TableRowAdapter
{
    private UdpLogger logger;

    public LoggerTableRow(UdpLogger logger)
    {
        super();
        this.logger = logger;
    }

    @Override
    public Object[] getCellValue(String columnName)
    {
        if ("observers".equals(columnName))
        {
            StringBuffer obsList = new StringBuffer("");
            Iterator it = this.logger.getObservers().iterator();
            while (it.hasNext())
            {
                obsList.append(it.next().toString());
                if (it.hasNext())
                {
                    obsList.append(", ");
                }
            }
            return toArray(obsList.toString());
        }
        else if ("status".equals(columnName))
        {
            String status = logger.isEnabled() ? "enabled" : "disabled";
            return toArray(status);
        }
        else if ("port".equals(columnName))
        {
            return toArray(logger.getPort());
        }
        else if ("notifyPattern".equals(columnName))
        {
            return toArray(logger.getNotifyPattern());
        }
        else
        {
            return toArray();
        }
    }

    private Object[] toArray(Object... objects)
    {
        if (objects.length == 0 || (objects.length == 1 & objects[0] == null))
        {
            objects = new Object[] { "" };
        }
        return objects;
    }

    public UdpLogger getLogger()
    {
        return logger;
    }

}
