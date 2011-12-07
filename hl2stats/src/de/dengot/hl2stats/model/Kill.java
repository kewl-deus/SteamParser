package de.dengot.hl2stats.model;
public class Kill extends HL2LogEntry
{
	
	private boolean headshot;
	
    public Kill(String date, String time, String attacker, String victim,
            String weapon, boolean headshot)
    {
        super(date, time, attacker, victim, weapon);
        this.headshot = headshot;
    }
    
    public Kill(String date, String time, String attacker, String victim,
            String weapon)
    {
        this(date, time, attacker, victim, weapon, false);
    }
    
    public String toString()
    {
        String ret = attacker + " killed " + victim + " with " + weapon;
        if (this.isHeadshot())
        {
        	ret += " (headshot)";
        }
        return ret;
    }
    
    public boolean isHeadshot()
    {
    	return this.headshot;
    }

	public void setHeadshot(boolean headshot)
	{
		this.headshot = headshot;
	}
    
    
}
