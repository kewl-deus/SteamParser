/*
 * Created on 04.10.2005
 */
package cx.ath.janiwe.steamparser.logic;

public enum Hitgroup
{

    GENERIC("generic"), LEFT_ARM("left arm"), RIGHT_ARM("right arm"), CHEST(
            "chest"), HEAD("head"), LEFT_LEG("left leg"), RIGHT_LEG("right leg"), STOMACH(
            "stomach"), UNKNOWN("unknown");

    private String name;

    Hitgroup(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return this.name;
    }
}
