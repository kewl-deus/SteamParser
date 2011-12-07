package de.dengot.hl2stats.model;

public abstract class HL2LogEntry
{

    protected String date;

    protected String time;

    protected String attacker;

    protected String victim;

    protected String weapon;
    
    
    public HL2LogEntry(String date, String time, String attacker, String victim,
            String weapon)
    {
        this.date = date;
        this.time = time;
        this.attacker = attacker;
        this.victim = victim;
        this.weapon = weapon;
    }


    public String getAttacker()
    {
        return attacker;
    }


    public void setAttacker(String attacker)
    {
        this.attacker = attacker;
    }


    public String getDate()
    {
        return date;
    }


    public void setDate(String date)
    {
        this.date = date;
    }


    public String getTime()
    {
        return time;
    }


    public void setTime(String time)
    {
        this.time = time;
    }


    public String getVictim()
    {
        return victim;
    }


    public void setVictim(String victim)
    {
        this.victim = victim;
    }


    public String getWeapon()
    {
        return weapon;
    }


    public void setWeapon(String weapon)
    {
        this.weapon = weapon;
    }
    
}
