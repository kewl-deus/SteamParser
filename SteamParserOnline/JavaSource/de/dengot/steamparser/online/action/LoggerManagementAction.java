package de.dengot.steamparser.online.action;

import java.io.IOException;
import java.net.SocketException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.dengot.steamparser.logic.GlobalExceptionLogger;
import de.dengot.steamparser.net.UdpParsingManager;
import de.dengot.steamparser.online.forms.LoggerManagementForm;
import de.dengot.steamparser.online.table.LoggerTableModel;
import de.dengot.steamparser.online.table.LoggerTableRow;

public class LoggerManagementAction extends Action
{

    public LoggerManagementAction()
    {
        super();
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        HttpSession session = request.getSession();

        // ist ein berechtigter User angemeldet?
//        User user = (User) session.getAttribute("user");
//        if (user == null)
//        {
//            return mapping.findForward("login");
//        }

        // String function = "" + request.getParameter("function");
        // int node = Integer.parseInt(request.getParameter("node"));

        String submit = request.getParameter("harpoon.submit");
        if ("createLogger".equals(submit))
        {
            LoggerManagementForm loggerForm = (LoggerManagementForm) form;
            try
            {
                UdpParsingManager.getInstance().createUdpLogParser(
                        loggerForm.getPortAsInt(), loggerForm.getGameType(),
                        loggerForm.getNotifyPattern(), true);
            }
            catch (SocketException e)
            {
                GlobalExceptionLogger.getInstance().log(
                        UdpParsingManager.getInstance(), e);
            }
        }

        String function = request.getParameter("function");
        String node = request.getParameter("node");
        LoggerTableModel loggerTableModel = (LoggerTableModel) session
                .getAttribute("loggerTable");

        if (loggerTableModel != null && function != null && node != null)
        {

            LoggerTableRow row = (LoggerTableRow) loggerTableModel.getRow(node);
            int port = row.getLogger().getPort();
            UdpParsingManager upm = UdpParsingManager.getInstance();

            if ("activateLogger".equals(function))
            {
                upm.setLoggerState(port, true);
            }
            else if ("deactivateLogger".equals(function))
            {
                upm.setLoggerState(port, false);
            }
            else if ("deleteLogger".equals(function))
            {
                upm.terminateLogger(port);
            }
        }

        LoggerTableModel newLoggerTableModel = new LoggerTableModel(
                UdpParsingManager.getInstance().getLoggers());
        session.setAttribute("loggerTable", newLoggerTableModel);

        return mapping.findForward("loggers");
    }
}
