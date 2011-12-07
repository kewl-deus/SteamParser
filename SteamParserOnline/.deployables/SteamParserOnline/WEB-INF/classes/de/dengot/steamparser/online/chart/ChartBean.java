package de.dengot.steamparser.online.chart;

import de.laures.cewolf.DatasetProducer;

public class ChartBean
{
    private DatasetProducer producer;

    private String chartTitle = "Titel";

    private String chartXAxisLabel = "X-Achse";

    private String chartYAxisLabel = "Y-Achse";

    public ChartBean(DatasetProducer producer, String chartTitle,
            String chartXAxisLabel, String chartYAxisLabel)
    {
        this.producer = producer;
        this.chartTitle = chartTitle;
        this.chartXAxisLabel = chartXAxisLabel;
        this.chartYAxisLabel = chartYAxisLabel;
    }

    public String getChartTitle()
    {
        return chartTitle;
    }

    public void setChartTitle(String chartTitle)
    {
        this.chartTitle = chartTitle;
    }

    public String getChartXAxisLabel()
    {
        return chartXAxisLabel;
    }

    public void setChartXAxisLabel(String chartXAxisLabel)
    {
        this.chartXAxisLabel = chartXAxisLabel;
    }

    public String getChartYAxisLabel()
    {
        return chartYAxisLabel;
    }

    public void setChartYAxisLabel(String chartYAxisLabel)
    {
        this.chartYAxisLabel = chartYAxisLabel;
    }

    public DatasetProducer getProducer()
    {
        return producer;
    }

    public void setProducer(DatasetProducer producer)
    {
        this.producer = producer;
    }

}
