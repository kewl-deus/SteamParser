package cx.ath.janiwe.steamparser;

public class DefaultLogDir
{

    private String type;

    private String dir;

    public DefaultLogDir(String type, String dir)
    {
        this.type = type;
        this.dir = dir;
    }

    public String getDir()
    {
        return dir;
    }

    public String getType()
    {
        return type;
    }
}
