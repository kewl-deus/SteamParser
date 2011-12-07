package de.dengot.steamparser.model;

public enum StatsType
{
    KILLS("Kills"), FRAGS("Frags"), HEADSHOTS("Headshots"), DEATHS("Deaths"), NON_HEADSHOT_KILLS(
            "Non Headshot Kills"), CUMUL_KILLS("Cumulated Kills"), KILLS_PER_DEATH(
            "Kills per Death"), HEADSHOT_PERCENTAGE("Percentage of Headshots");

    private final String label;

    StatsType(String label)
    {
        this.label = label;
    }

    public String getLabel()
    {
        return this.label;
    }

    public int getOrdinal()
    {
        return this.ordinal();
    }

    public String toString()
    {
        return this.label;
    }
}
