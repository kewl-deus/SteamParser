package de.dengot.steamparser.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CssColorChanger
{

    static final String DUNKEL_GRUEN = "#097A36";

    static final String HELL_GRUEN = "#D6EFDE";

    static final String DUNKEL_BLAU = "#A0B0FF";

    static final String HELL_BLAU = "#D0DCFF";

    static final String DEFAULT_PATH = "U:\\Eigene Dateien\\Eclipse Workspace\\SteamParserOnline\\WebContent\\static\\stylesheets";

    static String[][] replaceMap = new String[][] {
            { DUNKEL_GRUEN, DUNKEL_BLAU }, { HELL_GRUEN, HELL_BLAU },
            { "#009743", "#094392" }, { "#B5DBC6", DUNKEL_BLAU } };

    public static String getFileContent(File file) throws IOException
    {
        int size = (int) file.length();
        int offset = 0;
        FileReader in = new FileReader(file);
        char data[] = new char[size];
        while (in.ready())
        {
            offset += in.read(data, offset, size - offset);
        }
        in.close();
        String content = new String(data, 0, offset);
        return content;
    }

    public static void writeFileContent(File file, String content)
            throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(content);
        writer.flush();
        writer.close();
    }

    public static void main(String[] args)
    {
        try
        {
            File rootFile = new File(DEFAULT_PATH);
            if (args.length != 0)
            {
                rootFile = new File(args[0]);
            }
            changeRecursive(rootFile);
            System.out.println("DONE");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void changeRecursive(File parent) throws IOException
    {
        for (File file : parent.listFiles())
        {
            if (file.isDirectory())
            {
                changeRecursive(file);
            }
            else
            {
                if (file.getName().endsWith(".css"))
                {
                    String content = getFileContent(file);

                    System.out.println("Changing: " + file.getPath());

                    for (String[] replaceTupel : replaceMap)
                    {
                        String colorToReplace = replaceTupel[0];
                        String replacementColor = replaceTupel[1];
                        content = content.replaceAll(colorToReplace,
                                replacementColor);
                    }

                    writeFileContent(file, content);
                }
            }
        }
    }

}
