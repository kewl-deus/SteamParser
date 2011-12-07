/*
 * Created on 13.10.2005
 */
package cx.ath.janiwe.steamparser.online.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import cx.ath.janiwe.steamparser.online.table.SessionTableModel;

import de.hdi.gui.web.taglib.table.TableModel;

public class SessionControllerAction extends Action
{

    public ActionForward perform(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {

        HttpSession session = request.getSession();

        SessionTableModel model = (SessionTableModel) session
                .getAttribute("sessionStatsTable");

        String column = request.getParameter("column");
        String direction = request.getParameter("direction");
        model
                .resort(
                        column,
                        direction.equals("descending") ? TableModel.SORT_DIRECTION_DESCENDING
                                : TableModel.SORT_DIRECTION_ASCENDING);

        session.setAttribute("sessionStatsTable", model);

        return mapping.findForward("stats");
    }

}
