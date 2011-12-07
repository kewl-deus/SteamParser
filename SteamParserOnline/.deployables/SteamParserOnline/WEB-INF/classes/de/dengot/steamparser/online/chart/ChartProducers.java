package de.dengot.steamparser.online.chart;

public enum ChartProducers
{
    PLAYER_KILL_SPREADING_PIE_PRODUCER("playerKillSpreadingPieProducer"),

    WEAPON_KILL_SPREADING_PIE_PRODUCER("weaponKillSpreadingPieProducer"),

    STACKED_PLAYER_STATS_PRODUCER("stackedPlayerStatsProducer");

    private final String id;

    ChartProducers(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
    }

    public String toString()
    {
        return this.getId();
    }
}
