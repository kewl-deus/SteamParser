package de.dengot.steamparser.online.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import de.dengot.steamparser.exceptions.UnknownUserException;
import de.dengot.steamparser.logic.GlobalExceptionLogger;
import de.dengot.steamparser.logic.UserManager;
import de.dengot.steamparser.model.User;
import de.dengot.steamparser.online.forms.UserLoginForm;

public class UserLoginAction extends Action
{

    public UserLoginAction()
    {
        super();
    }

    public ActionForward perform(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        UserLoginForm ulf = (UserLoginForm) form;
        User user = null;
        try
        {
            user = UserManager.getTheManager().checkLogin(ulf.getUsername(),
                    ulf.getPassword());
        }
        catch (UnknownUserException e)
        {
            GlobalExceptionLogger.getInstance().log(
                    UserManager.getTheManager(), e, false);
        }
        if (ulf.validate(mapping, request).empty() && user != null)
        {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            return mapping.findForward("indexPage");
        }
        else
        {
            request.setAttribute("error", "error.userlogin");
            return mapping.findForward("loginPage");
        }
    }
}
