package de.dengot.hl2stats.model;

public enum DbStructure
{
	TABLE_ATTACK("t_attack"), TABLE_KILL("t_kill"), VIEW_ATTACK("v_attack"), VIEW_KILL(
			"v_kill"), VIEW_KILL_COUNT("v_kill_count"), VIEW_HEADSHOT(
			"v_headshot"), VIEW_HEADSHOT_COUNT("v_headshot_count"), TABLE_XAXIS(
			"t_xaxis"), TABLE_YAXIS("t_yaxis");

	private final String technicalName;

	DbStructure(String technicalName)
	{
		this.technicalName = technicalName;
	}

	public String toString()
	{
		return this.technicalName;
	}

	public String getTechnicalName()
	{
		return this.technicalName;
	}
}
