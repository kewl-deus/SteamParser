/*
 * Created on 13.09.2005
 */
package cx.ath.janiwe.steamparser.online.action;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.DiskFile;
import org.apache.struts.upload.FormFile;

import cx.ath.janiwe.steamparser.Config;
import cx.ath.janiwe.steamparser.DefaultLogDir;
import cx.ath.janiwe.steamparser.db.DatabaseManager;
import cx.ath.janiwe.steamparser.logic.Game;
import cx.ath.janiwe.steamparser.online.forms.AdminForm;
import cx.ath.janiwe.steamparser.parser.CounterStrikeParser;
import de.dengot.steamparser.logic.StorageService;

public class AdminAction extends Action
{

    private StringBuffer alreadyFlushed;

    public ActionForward perform(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {

        request.removeAttribute("parseLog");

        AdminForm adminForm = (AdminForm) form;
        String submit = adminForm.getHarpoon().getSubmit();

        Date start = new Date();
        String log = "";
        
        if (submit.equals("uploadFile"))
        {
            String gameType = adminForm.getGameType();
            FormFile f = adminForm.getZipFile();
            if (f != null)
            {
                if (f.getFileName().endsWith(".zip"))
                {
                    log = parseZipFile(f, gameType);
                }
                else
                {
                	log = "Kein Zip-File hochgeladen!";
                }
            }
        }
        else if (submit.equals("resetDB"))
        {
            log = initDB();
        }
        else if (submit.equals("insertDefault"))
        {
            log = insertDefault();
        }
        
        populateParserLog(request, log, start, new Date());
        
        return mapping.findForward("admin");

    }
    
    private void populateParserLog(HttpServletRequest request, String log, Date start, Date end)
    {
    	long seconds = (end.getTime() - start.getTime()) / 1000;
    	String logheader = "<b>";
        logheader += "Started: " + start.toString() + "<br/>";
        logheader += "Finished: " + end.toString() + "<br/>";
        logheader += "Runtime: " + seconds + " seconds<br/>";
        logheader += "</b>";
    	String overallLog = logheader + log;
    	overallLog = overallLog.replaceAll("\r", "");
    	overallLog = overallLog.replaceAll("\n", "<br/>");
    	overallLog = overallLog.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    	overallLog = overallLog.replaceAll("OK",
                "<br/><b><font color='red'>OK</font></b><br/>");
        request.setAttribute("parseLog", overallLog);
    }

    private String initDB()
    {
        PrintStream outStream = new PrintStream(new ByteOutputStream(), true);
        PrintStream outBak = System.out;
        PrintStream errBak = System.err;
        System.setOut(outStream);
        System.setErr(outStream);
        try
        {
            DatabaseManager.getInstance().createDB();
            alreadyFlushed.append("OK");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.setOut(outBak);
        System.setErr(errBak);

        return alreadyFlushed.toString();

    }

    private String parseZipFile(FormFile f, String gameType)
    {

        PrintStream outStream = new PrintStream(new ByteOutputStream(), true);
        PrintStream outBak = System.out;
        PrintStream errBak = System.err;
        System.setOut(outStream);
        System.setErr(outStream);

        DiskFile df = null;
        try
        {
            df = (DiskFile) f;
        }
        catch (ClassCastException ccEx)
        {
            System.out.println("Fehler beim Casten des FormFile auf DiskFile");
            return alreadyFlushed.toString();
        }

        try
        {

            ZipFile zip = new ZipFile(df.getFilePath());
            Enumeration<? extends ZipEntry> zipEnum = zip.entries();
            TreeSet<ZipEntry> zipEntries = new TreeSet<ZipEntry>(
                    new ZipEntryComparator());
            while (zipEnum.hasMoreElements())
            {
                ZipEntry entry = zipEnum.nextElement();
                if (entry.getName().endsWith(".log"))
                    ;
                zipEntries.add(entry);
            }
            CounterStrikeParser p = new CounterStrikeParser();
            Game game = new Game();
            for (ZipEntry entry : zipEntries)
            {
                if (game.getGameStopped() != null)
                {
                    try
                    {
                        StorageService.getInstance().put(game, gameType);
                    }
                    catch (SQLException sEx)
                    {
                        sEx.printStackTrace();
                        System.out
                                .println("Fehler beim Speichern der GameSession - überspringe!");
                    }
                    game = new Game();
                }
                p.parse(zip.getInputStream(entry), game);
            }
            try
            {
            	StorageService.getInstance().put(game, gameType);
            }
            catch (SQLException sEx)
            {
                sEx.printStackTrace();
                System.out
                        .println("Fehler beim Speichern der GameSession - überspringe!");
            }
            alreadyFlushed.append("OK");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.setOut(outBak);
        System.setErr(errBak);

        return alreadyFlushed.toString();

    }

    private String insertDefault()
    {
        PrintStream outStream = new PrintStream(new ByteOutputStream(), true);
        PrintStream outBak = System.out;
        PrintStream errBak = System.err;
        System.setOut(outStream);
        System.setErr(outStream);

        try
        {
            CounterStrikeParser p = new CounterStrikeParser();
            Game game = new Game();
            for (DefaultLogDir defLogDir : Config.getInstance().getLogs())
            {
                String gameType = defLogDir.getType();
                File logDir = new File(defLogDir.getDir());
                if (logDir.exists())
                {
                    // anonyme Klasse FileFilter im Schleifenkopf
                    for (File f : logDir.listFiles(new FileFilter()
                    {
                        public boolean accept(File pathname)
                        {
                            if (pathname.isFile()
                                    && pathname.getName().endsWith(".log"))
                            {
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                        }
                    }))
                    {
                        // hier: Start des Schleifenrumpfes
                        if (game.getGameStopped() != null)
                        {
                            try
                            {
                                System.out.print("Parsing File: " + f.getName()
                                        + "\n");
                                StorageService.getInstance().put(game,
                                        gameType);
                            }
                            catch (SQLException sEx)
                            {
                                sEx.printStackTrace();
                                System.out
                                        .println("Fehler beim Speichern der GameSession - überspringe!");
                            }
                            game = new Game();
                        }
                        p.parse(f, game);
                    }

                }
                try
                {
                	StorageService.getInstance().put(game, gameType);
                }
                catch (SQLException sEx)
                {
                    sEx.printStackTrace();
                    System.out
                            .println("Fehler beim Speichern der GameSession - überspringe!");
                }
                alreadyFlushed.append("OK");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        System.setOut(outBak);
        System.setErr(errBak);

        return alreadyFlushed.toString();
    }

    class ZipEntryComparator implements Comparator<ZipEntry>
    {
        public int compare(ZipEntry entry1, ZipEntry entry2)
        {

            return entry1.getName().compareTo(entry2.getName());
        }
    }
    
    class ByteOutputStream extends OutputStream
    {
        private ArrayList<Byte> byteList;

        {
            byteList = new ArrayList<Byte>();
            alreadyFlushed = new StringBuffer();
        }

        public void write(int b)
        {
            byteList.add(new Byte((byte) b));
        }

        public void flush()
        {
            byte[] bytes = new byte[byteList.size()];

            for (int i = 0; i < bytes.length; i++)
            {
                bytes[i] = ((Byte) byteList.get(i)).byteValue();
            }

            byteList.clear();

            alreadyFlushed.append(new String(bytes));
        }
    }

}
