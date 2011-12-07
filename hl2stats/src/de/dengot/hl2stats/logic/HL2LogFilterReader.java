package de.dengot.hl2stats.logic;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class HL2LogFilterReader extends BufferedReader
{
    private final String ATTACKED = "attacked";
    
    private final String KILLED = "killed";
    
    private final String SPACE = " ";
    
    public HL2LogFilterReader(Reader in, int sz)
    {
        super(in, sz);
        // TODO Auto-generated constructor stub
    }

    public HL2LogFilterReader(Reader in)
    {
        super(in);
        // TODO Auto-generated constructor stub
    }

    @Override
    public String readLine() throws IOException
    {
        String line = super.readLine();
        if (line.contains(ATTACKED) || line.contains(KILLED))
            return line;
        else
            return SPACE;
    }

}
