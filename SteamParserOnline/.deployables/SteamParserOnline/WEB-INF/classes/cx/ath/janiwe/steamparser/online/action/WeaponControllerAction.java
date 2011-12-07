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

import cx.ath.janiwe.steamparser.online.table.WeaponTableModel;

import de.hdi.gui.web.taglib.table.TableModel;

public class WeaponControllerAction extends Action
{

    public ActionForward perform(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {

        HttpSession session = request.getSession();

        WeaponTableModel model = (WeaponTableModel) session
                .getAttribute("weaponStatsTable");

        String column = request.getParameter("column");
        String direction = request.getParameter("direction");
        model
                .resort(
                        column,
                        direction.equals("descending") ? TableModel.SORT_DIRECTION_DESCENDING
                                : TableModel.SORT_DIRECTION_ASCENDING);

        session.setAttribute("weaponStatsTable", model);

        return mapping.findForward("stats");
    }

}
