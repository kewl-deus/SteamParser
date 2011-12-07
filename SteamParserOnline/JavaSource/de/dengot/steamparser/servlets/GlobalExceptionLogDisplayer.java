package de.dengot.steamparser.servlets;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.dengot.steamparser.logic.GlobalExceptionLogger;

public class GlobalExceptionLogDisplayer extends HttpServlet
{

    public GlobalExceptionLogDisplayer()
    {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException
    {
        Writer writer = response.getWriter();
        writer.write("<HTML><BODY>");

        GlobalExceptionLogger el = GlobalExceptionLogger.getInstance();

        if (el.getRegisteredThrowers().isEmpty())
        {
            writer.write("No Errors collected so far.");
        }
        else
        {
            for (Object thrower : el.getRegisteredThrowers())
            {
                writer.write("<h3>");
                writer.write("<font color='red'>");
                writer.write(String.valueOf(thrower));
                writer.write("</font>");
                writer.write("</h3><br/>");
                String htmlLog = el.getLog(thrower).replaceAll("\n", "<br/>");
                writer
                        .write("<span style='font-family:Courier New; font-size:80%'>");
                writer.write(htmlLog);
                writer.write("</span>");
                writer.write("<hr/>");
            }
        }
        writer.write("</BODY></HTML>");
        writer.close();

    }
}
