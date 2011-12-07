package de.dengot.steamparser.online.chart;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.util.SortOrder;

import de.dengot.steamparser.exceptions.SessionNotPlayedException;
import de.dengot.steamparser.model.QueryablePlayer;
import de.dengot.steamparser.model.StatsType;
import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.tooltips.CategoryToolTipGenerator;

public class PlayerStatsDatasetProducer implements DatasetProducer,
        CategoryToolTipGenerator, Serializable
{
    protected final Comparator<QueryablePlayer> playerNameComparator = new Comparator<QueryablePlayer>()
    {
        public int compare(QueryablePlayer p1, QueryablePlayer p2)
        {
            return p1.getName().compareTo(p2.getName());
        }
    };

    protected final Comparator<QueryablePlayer> playerOverallKillComparator = new Comparator<QueryablePlayer>()
    {
        public int compare(QueryablePlayer p1, QueryablePlayer p2)
        {
            int k1 = p1.getOverallKills();
            int k2 = p2.getOverallKills();
            if (k1 == k2)
            {
                return 0;
            }
            if (k1 < k2)
            {
                return +1;
            }
            else
            {
                return -1;
            }
        }
    };
    
    private String producerId;

    private LinkedList<QueryablePlayer> playerList;

    private StatsType typeToQuery;

    public PlayerStatsDatasetProducer(String producerId,
            List<QueryablePlayer> playerList, StatsType typeToQuery)
    {
        this.producerId = producerId;
        this.playerList = new LinkedList<QueryablePlayer>(playerList);
        Collections.sort(this.playerList, this.playerOverallKillComparator);
        this.typeToQuery = typeToQuery;
    }

    private SortedSet<String> collectPlayedSessions()
    {
        SortedSet<String> playedSessions = new TreeSet<String>();
        for (QueryablePlayer player : this.playerList)
        {
            playedSessions.addAll(player.getPlayedSessions());
        }
        return playedSessions;
    }

    public Object produceDataset(Map params) throws DatasetProduceException
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        SortedSet<String> playedSessions = this.collectPlayedSessions();

        for (String session : playedSessions)
        {
//            Collections.sort(this.playerList, new StatsComparator(
//                    this.typeToQuery, session, SortOrder.DESCENDING));
            for (QueryablePlayer player : this.playerList)
            {
                try
                {
                    dataset.addValue(player.getStatsValue(session,
                            this.typeToQuery).doubleValue(), player.getName(),
                            session);

//                    System.out.println(session
//                            + " "
//                            + player.getName()
//                            + " = "
//                            + player.getStatsValue(session, this.typeToQuery)
//                                    .doubleValue() + " "
//                            + typeToQuery.getLabel());
                }
                catch (SessionNotPlayedException e)
                {
                    dataset.addValue(0.0d, player.getName(), session);
                    // TODO Bei StatsType.CUMUL_KILL ist 0 falsch! Problem liegt
                    // aber wahrscheinlich beim SQL-Select im DatabaseManager
                }

            }
        }
        return dataset;
    }

    public String generateToolTip(CategoryDataset dataset, int row, int col)
    {
        if (dataset != null)
        {
            double value = dataset.getValue(row, col).doubleValue();
            value = (value * 10) + 0.5d;
            int valueInt = (int) value;
            value = (double) valueInt / 10;
            return dataset.getRowKey(row).toString() + "("
                    + dataset.getColumnKey(col).toString() + ") " + value + " "
                    + this.typeToQuery.getLabel();
        }
        else
        {
            return "no Value";
        }
    }

    public boolean hasExpired(Map params, Date since)
    {
        return (System.currentTimeMillis() - since.getTime()) > 5000;
    }

    public String getProducerId()
    {
        return producerId;
    }

    private class StatsComparator implements Comparator<QueryablePlayer>
    {
        private StatsType type;

        private String session;

        private SortOrder sortOrder;

        public StatsComparator(StatsType type, String session,
                SortOrder sortOrder)
        {
            this.type = type;
            this.session = session;
            this.sortOrder = sortOrder;
        }

        public int compare(QueryablePlayer p1, QueryablePlayer p2)
        {
            double v1 = 0d;
            try
            {
                v1 = p1.getStatsValue(session, type).doubleValue();
            }
            catch (SessionNotPlayedException e)
            {
            }

            double v2 = 0d;
            try
            {
                v2 = p2.getStatsValue(session, type).doubleValue();
            }
            catch (SessionNotPlayedException e)
            {
            }

            if (v1 == v2)
            {
                return 0;
            }

            if (this.sortOrder == SortOrder.ASCENDING)
            {
                return sortAscending(v1, v2);
            }
            else
            {
                return this.sortDescending(v1, v2);
            }

        }

        private int sortAscending(double v1, double v2)
        {
            if (v1 < v2)
            {
                return -1;
            }
            else
            {
                return +1;
            }
        }

        private int sortDescending(double v1, double v2)
        {
            if (v1 < v2)
            {
                return +1;
            }
            else
            {
                return -1;
            }
        }
    }

}
