package de.dengot.steamparser.servlets;

import java.io.IOException;
import java.io.Writer;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dengot.steamparser.net.UdpParsingManager;

public class SteamUdpLogDisplayer extends HttpServlet
{
    public SteamUdpLogDisplayer()
    {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        int port = -1;
        try
        {
            // String strPort = request.getPathInfo().substring(1);
            String strPort = request.getParameter("port");
            port = Integer.parseInt(strPort);
        }
        catch (StringIndexOutOfBoundsException oobe)
        {
        }
        catch (NullPointerException npe)
        {
        }
        catch (NumberFormatException nfe)
        {
        }

        String highlightPattern = null;
        try
        {
            highlightPattern = request.getParameter("highlight");
        }
        catch (NullPointerException npe)
        {
        }

        Writer writer = response.getWriter();
        writer.write("<HTML><BODY>");

        writer.write("<b>Logdata from SteamLogger at Port " + port
                + ":</b><br/>");

        String steamlog = UdpParsingManager.getInstance().getLogData(port);
        steamlog = steamlog.replaceAll("<", "[");
        steamlog = steamlog.replaceAll(">", "]");
        steamlog = steamlog.replaceAll("RL", "<br/>L");

        if (highlightPattern != null)
        {
            steamlog = steamlog.replaceAll(highlightPattern,
                    "<b><font color='red'>" + highlightPattern + "</font></b>");
        }

        writer.write(steamlog);
        writer.write("</BODY></HTML>");
        writer.close();
    }

    private void plotDebug(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        Writer writer = response.getWriter();
        writer.write("Parameters:<br/>");
        Enumeration pnames = request.getParameterNames();
        while (pnames.hasMoreElements())
        {
            String param = pnames.nextElement().toString();
            writer.write(param);
            writer.write(" = ");
            writer.write(request.getParameterValues(param).toString());
            writer.write("<br/>");
        }

        writer.write("Attributes:<br/>");
        Enumeration attrs = request.getAttributeNames();
        while (attrs.hasMoreElements())
        {
            String attr = attrs.nextElement().toString();
            writer.write(attr);
            writer.write(" = ");
            writer.write(request.getAttribute(attr).toString());
            writer.write("<br/>");
        }
    }

}
