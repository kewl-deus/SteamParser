package cx.ath.janiwe.steamparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import de.dengot.steamparser.logic.GlobalExceptionLogger;
import de.dengot.steamparser.model.UdpLoggerConfig;

public class Config implements ErrorHandler
{

    private static Config singleton;

    private String dbBitPrefix;

    private String dbBitPostfix;

    private String dbNextValStmt;

    private String dbDriver;

    private String dbUri;

    private String dbUser;

    private String dbPass;

    private String dbSeqCall;

    private String dbCreateFile;

    private String guiDefaultPath;

    private Set<DefaultLogDir> logs;

    private Set<UdpLoggerConfig> udploggers;

    private Config()
    {
        logs = new HashSet<DefaultLogDir>();
        udploggers = new HashSet<UdpLoggerConfig>();
        try
        {
            readConfig();
        }
        catch (Exception e)
        {
            GlobalExceptionLogger.getInstance().log(this, e);
            dbBitPrefix = "";
            dbBitPostfix = "";
            dbNextValStmt = "";
            dbDriver = "";
            dbUri = "";
            dbUser = "";
            dbPass = "";
            dbSeqCall = "";
            dbCreateFile = "";
            guiDefaultPath = "ERROR";
        }
    }

    public static Config getInstance()
    {
        if (singleton == null)
        {
            singleton = new Config();
        }
        return singleton;
    }

    private void readConfig() throws Exception
    {
        Document configDoc = getDOMFromStream(getResourceStream(Config.class
                .getPackage().getName(), "config.xml"));
        // Datenbank
        NodeList dbNodes = configDoc.getElementsByTagName("database");
        Node dbNode = dbNodes.item(0);
        NodeList dbEntries = dbNode.getChildNodes();
        for (int i = 0; i < dbEntries.getLength(); i++)
        {
            Node current = dbEntries.item(i);
            if (current.getNodeName().equals("driver"))
            {
                dbDriver = current.getAttributes().getNamedItem("name")
                        .getNodeValue();
            }
            else if (current.getNodeName().equals("address"))
            {
                dbUri = current.getAttributes().getNamedItem("uri")
                        .getNodeValue();
            }
            else if (current.getNodeName().equals("user"))
            {
                dbUser = current.getAttributes().getNamedItem("name")
                        .getNodeValue();
                dbPass = current.getAttributes().getNamedItem("pw")
                        .getNodeValue();
            }
            else if (current.getNodeName().equals("bitFixes"))
            {
                dbBitPrefix = current.getAttributes().getNamedItem("pre")
                        .getNodeValue();
                dbBitPostfix = current.getAttributes().getNamedItem("post")
                        .getNodeValue();
            }
            else if (current.getNodeName().equals("sequence"))
            {
                dbSeqCall = current.getAttributes().getNamedItem("call")
                        .getNodeValue();
            }
            else if (current.getNodeName().equals("create"))
            {
                dbCreateFile = current.getAttributes().getNamedItem("file")
                        .getNodeValue();
            }
        }

        // Default-Pfad
        NodeList guiNodes = configDoc.getElementsByTagName("gui");
        Node guiNode = guiNodes.item(0);
        guiDefaultPath = guiNode.getAttributes().getNamedItem("default")
                .getNodeValue();

        // Default-Logs
        NodeList defaultNodes = configDoc.getElementsByTagName("logs");
        for (int i = 0; i < defaultNodes.getLength(); i++)
        {
            Node current = defaultNodes.item(i);
            String type = current.getAttributes().getNamedItem("type")
                    .getNodeValue();
            String path = current.getAttributes().getNamedItem("path")
                    .getNodeValue();
            if (type != null && path != null)
            {
                logs.add(new DefaultLogDir(type, path));
            }
        }

        NodeList udploggers = configDoc.getElementsByTagName("udplogger");
        for (int i = 0; i < udploggers.getLength(); i++)
        {
            Node current = udploggers.item(i);
            int port = Integer.parseInt(current.getAttributes().getNamedItem(
                    "port").getNodeValue());
            String gameType = current.getAttributes().getNamedItem("gametype")
                    .getNodeValue();
            String notifyPattern = current.getAttributes().getNamedItem(
                    "notifypattern").getNodeValue();
            boolean writeLogfile = Boolean.parseBoolean(current.getAttributes()
                    .getNamedItem("writelogfile").getNodeValue());
            this.udploggers.add(new UdpLoggerConfig(port, gameType,
                    notifyPattern, writeLogfile));
        }
    }

    public InputStream getResourceStream(String pkgname, String fname)
    {
        String resname = "/" + pkgname.replace('.', '/') + "/" + fname;
        Class clazz = getClass();
        InputStream is = clazz.getResourceAsStream(resname);
        return is;
    }

    private Document getDOMFromStream(InputStream is) throws IOException,
            SAXException, ParserConfigurationException
    {
        Document result;
        DocumentBuilderFactory domFac = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = domFac.newDocumentBuilder();
        builder.setErrorHandler(this);
        result = builder.parse(is);
        return result;
    }

    public void error(SAXParseException ex)
    {
        System.out.println(ex);
    }

    public void fatalError(SAXParseException ex)
    {
        System.out.println(ex);
    }

    public void warning(SAXParseException ex)
    {
        System.out.println(ex);
    }

    public String getDbBitPostfix()
    {
        return dbBitPostfix;
    }

    public String getDbBitPrefix()
    {
        return dbBitPrefix;
    }

    public String getDbDriver()
    {
        return dbDriver;
    }

    public String getDbNextValStmt()
    {
        return dbNextValStmt;
    }

    public String getDbPass()
    {
        return dbPass;
    }

    public String getDbUri()
    {
        return dbUri;
    }

    public String getDbUser()
    {
        return dbUser;
    }

    public String getDbSeqCall()
    {
        return dbSeqCall;
    }

    public String getGuiDefaultPath()
    {
        return guiDefaultPath;
    }

    public String getDbCreateFile()
    {
        return dbCreateFile;
    }

    public Set<DefaultLogDir> getLogs()
    {
        return logs;
    }

    public Set<UdpLoggerConfig> getUdpLoggerConfigs()
    {
        return udploggers;
    }

}
