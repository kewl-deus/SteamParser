package de.dengot.steamparser.online.chart;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import cx.ath.janiwe.steamparser.stats.PlayerStats;
import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.tooltips.CategoryToolTipGenerator;

public class StackedPlayerStatsDatasetProducer implements Serializable,
        DatasetProducer, CategoryToolTipGenerator
{

    protected final Comparator<PlayerStats> playerOverallKillComparator = new Comparator<PlayerStats>()
    {
        public int compare(PlayerStats p1, PlayerStats p2)
        {
            int k1 = p1.getKills();
            int k2 = p2.getKills();
            if (k1 == k2)
            {
                return 0;
            }
            if (k1 < k2)
            {
                // weniger Kills = hinterer Platz
                return 1;
            }
            else
            {
                // mehr Kills = davor einsortieren
                return -1;
            }
        }
    };

    private String producerId;

    private List<PlayerStats> playerStats;

    private int numOfPlayersToShow;

    public StackedPlayerStatsDatasetProducer(String producerId,
            List<PlayerStats> playerStats, int numOfPlayersToShow)
    {
        this.producerId = producerId;
        this.playerStats = new Vector<PlayerStats>(playerStats);
        Collections.sort(this.playerStats, this.playerOverallKillComparator);
        this.numOfPlayersToShow = numOfPlayersToShow;
    }

    public Object produceDataset(Map params) throws DatasetProduceException
    {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < playerStats.size() && i < this.numOfPlayersToShow; i++)
        {
            PlayerStats p = playerStats.get(i);
            dataset.addValue(p.getDeaths() * -1, "Deaths", p.getName());

            dataset.addValue(p.getHeadshots(), "Headshots", p.getName());

            dataset.addValue(p.getKills() - p.getHeadshots(),
                    "Non Headshot Kills", p.getName());
        }
        return dataset;
    }

    public boolean hasExpired(Map params, Date since)
    {
        return (System.currentTimeMillis() - since.getTime()) > 5000;
    }

    public String getProducerId()
    {
        return producerId;
    }
    
    public String generateToolTip(CategoryDataset data, int series, int item)
    {
        return String.valueOf(data.getValue(series, item));
    }
}
