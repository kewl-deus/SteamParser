package de.dengot.steamparser.servlets;

import javax.servlet.http.HttpServlet;

import de.dengot.steamparser.net.UdpParsingManager;

public class AutorunServlet extends HttpServlet
{

    public AutorunServlet()
    {
        UdpParsingManager.getInstance().createDefaultLoggers();
    }

}
