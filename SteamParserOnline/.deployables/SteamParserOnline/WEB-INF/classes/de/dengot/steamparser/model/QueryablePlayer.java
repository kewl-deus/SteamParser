package de.dengot.steamparser.model;

import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

import de.dengot.steamparser.exceptions.SessionNotPlayedException;

public class QueryablePlayer
{
    private HashMap<String, HashMap<StatsType, Number>> gameStats = new HashMap<String, HashMap<StatsType, Number>>();

    private String name;

    public QueryablePlayer(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void put(String session, StatsType statsType, Number statsValue)
    {
        HashMap<StatsType, Number> sessionStats = this.gameStats.get(session);
        if (sessionStats == null)
        {
            sessionStats = new HashMap<StatsType, Number>();
            this.gameStats.put(session, sessionStats);
        }
        sessionStats.put(statsType, statsValue);
    }

    public Number getStatsValue(String session, StatsType statsType)
            throws SessionNotPlayedException
    {
        HashMap<StatsType, Number> sessionStats = this.gameStats.get(session);
        if (sessionStats == null)
        {
            throw new SessionNotPlayedException(this.getName()
                    + "didn't play @ Session: " + session);
        }
        else
        {
            Number ret = sessionStats.get(statsType);
            if (ret == null)
            {
                return new Integer(0);
            }
            else
            {
                return ret;
            }
        }
    }

    public SortedSet<String> getPlayedSessions()
    {
        SortedSet<String> sessions = new TreeSet<String>();
        sessions.addAll(this.gameStats.keySet());
        return sessions;
    }

    public int getOverallKills()
    {
        int killSum = 0;
        for (String session : this.gameStats.keySet())
        {
            try
            {
                killSum += this.getStatsValue(session, StatsType.KILLS)
                        .intValue();
            }
            catch (SessionNotPlayedException e)
            {
            }
        }
        return killSum;
    }

}
