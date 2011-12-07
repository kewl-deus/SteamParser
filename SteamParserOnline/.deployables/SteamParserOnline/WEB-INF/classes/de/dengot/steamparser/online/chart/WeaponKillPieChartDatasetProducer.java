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
import cx.ath.janiwe.steamparser.stats.WeaponStats;
import de.laures.cewolf.DatasetProduceException;
import de.laures.cewolf.DatasetProducer;

/**
 * @author Dennis
 * @deprecated KillPieChartDatasetProducer<T extends IStats>
 */
public class WeaponKillPieChartDatasetProducer implements DatasetProducer,
        Serializable
{
    private String producerId;

    private List<WeaponStats> weaponStats;

    public WeaponKillPieChartDatasetProducer(String producerId,
            List<WeaponStats> weaponStats)
    {
        this.producerId = producerId;
        this.weaponStats = weaponStats;
    }

    public Object produceDataset(Map params) throws DatasetProduceException
    {
        DefaultKeyedValues values = new DefaultKeyedValues();
        double killSum = 0;
        for (WeaponStats w : this.weaponStats)
        {
            double kills = (double) w.getKills();
            values.addValue(w.getName(), kills);
            killSum += kills;
        }
        values.sortByValues(SortOrder.DESCENDING);

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
                .createConsolidatedPieDataset(pieset, "Others", 0.05d, 7);

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
}
