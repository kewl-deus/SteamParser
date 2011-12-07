package de.dengot.steamparser.online.chart;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.SortOrder;

import cx.ath.janiwe.steamparser.stats.PlayerStats;
import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.tooltips.PieToolTipGenerator;

/**
 * @author Dennis
 * @deprecated Use Generic-Class KillPieChartDatasetProducer<T extends IStats>
 */
public class PlayerKillPieChartDatasetProducer implements DatasetProducer,
        PieToolTipGenerator, Serializable
{

    private String producerId;

    private List<PlayerStats> playerStats;

    private PieDataset realValuePieset;

    public PlayerKillPieChartDatasetProducer(String producerId,
            List<PlayerStats> playerStats)
    {
        this.producerId = producerId;
        this.playerStats = playerStats;
    }

    public Object produceDataset(Map params) throws DatasetProduceException
    {
        DefaultKeyedValues values = new DefaultKeyedValues();
        double killSum = 0;
        for (PlayerStats p : this.playerStats)
        {
            double kills = (double) p.getKills();
            values.addValue(p.getName(), kills);
            killSum += kills;
        }
        values.sortByValues(SortOrder.DESCENDING);
        realValuePieset = DatasetUtilities.createConsolidatedPieDataset(
                new DefaultPieDataset(values), "Others", 0.10d, 5);

        // System.out.println("KillSum = " + killSum);

        DefaultKeyedValues percentValues = new DefaultKeyedValues();
        for (int i = 0; i < values.getItemCount(); i++)
        {
            double perc = values.getValue(i).doubleValue() / killSum;
            perc = (perc * 1000) + 0.5d;
            int percInt = (int) perc;
            perc = (double) percInt / 10;
            percentValues.addValue(values.getKey(i), perc);

            // if (i < 6)
            // {
            // System.out.println(values.getKey(i) + ": "
            // + values.getValue(i).doubleValue() + " kills = " + perc
            // + " %");
            // }
        }

        DefaultPieDataset pieset = new DefaultPieDataset(percentValues);

        PieDataset consolPieset = DatasetUtilities
                .createConsolidatedPieDataset(pieset, "Others", 0.10d, 5);

        return consolPieset;
    }

    public boolean hasExpired(Map params, Date since)
    {
        return (System.currentTimeMillis() - since.getTime()) > 5000;
    }

    public String getProducerId()
    {
        return this.producerId;
    }

    public String generateToolTip(PieDataset data, Comparable key, int pieIndex)
    {
        // return data.getValue(key) + " %";
        return this.realValuePieset.getValue(key).intValue() + " Kills";
    }

}
