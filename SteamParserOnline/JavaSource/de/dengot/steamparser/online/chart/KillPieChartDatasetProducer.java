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

import cx.ath.janiwe.steamparser.stats.IStats;
import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;
import de.laures.cewolf.tooltips.PieToolTipGenerator;

public class KillPieChartDatasetProducer<T extends IStats> implements
        DatasetProducer, PieToolTipGenerator, Serializable
{

    private static final String DEFAULT_OTHERS_LABEL = "Others";

    private static final double DEFAULT_CONSOLIDATE_MIN_PERCENTAGE = 0.10d;

    private static final long REFRESH_TIME_IN_MILLIS = 5000;

    private String producerId;

    private List<T> stats;

    private PieDataset realValuePieset;

    private double consolidateMinPercentage;

    private String othersLabel;

    public KillPieChartDatasetProducer(String producerId, List<T> stats,
            double consolidateMinPercentage, String othersLabel)
    {
        this.producerId = producerId;
        this.stats = stats;
        if (consolidateMinPercentage > 1)
        {
            this.consolidateMinPercentage = consolidateMinPercentage / 100;
        }
        else
        {
            this.consolidateMinPercentage = consolidateMinPercentage;
        }
        this.othersLabel = othersLabel;
    }

    public KillPieChartDatasetProducer(String producerId, List<T> stats,
            double consolidateMinPercentage)
    {
        this(producerId, stats, consolidateMinPercentage, DEFAULT_OTHERS_LABEL);
    }

//    public KillPieChartDatasetProducer(String producerId, List<T> stats)
//    {
//        this(producerId, stats, DEFAULT_CONSOLIDATE_MIN_PERCENTAGE,
//                DEFAULT_OTHERS_LABEL);
//    }

    public Object produceDataset(Map params) throws DatasetProduceException
    {
        DefaultKeyedValues values = new DefaultKeyedValues();
        double killSum = 0;
        for (T aStat : this.stats)
        {
            double kills = (double) aStat.getKills();
            values.addValue(aStat.getName(), kills);
            killSum += kills;
        }
        values.sortByValues(SortOrder.DESCENDING);
        realValuePieset = DatasetUtilities.createConsolidatedPieDataset(
                new DefaultPieDataset(values), othersLabel,
                this.consolidateMinPercentage);

        DefaultKeyedValues percentValues = new DefaultKeyedValues();
        for (int i = 0; i < values.getItemCount(); i++)
        {
            double perc = values.getValue(i).doubleValue() / killSum;
            perc = (perc * 1000) + 0.5d;
            int percInt = (int) perc;
            perc = (double) percInt / 10;
            percentValues.addValue(values.getKey(i), perc);
        }

        DefaultPieDataset pieset = new DefaultPieDataset(percentValues);

        PieDataset consolPieset = DatasetUtilities
                .createConsolidatedPieDataset(pieset, othersLabel,
                        this.consolidateMinPercentage);

        return consolPieset;
    }

    public boolean hasExpired(Map params, Date since)
    {
        return (System.currentTimeMillis() - since.getTime()) > REFRESH_TIME_IN_MILLIS;
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
