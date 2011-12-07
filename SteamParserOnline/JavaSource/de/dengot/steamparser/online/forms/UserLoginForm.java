package de.dengot.steamparser.online.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

public class UserLoginForm extends ActionForm
{
    private String username;

    private String password;

    public UserLoginForm()
    {
        super();
    }

    @Override
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        if (this.username == null)
        {
            errors.add("username", new ActionError(
                    "error.userlogin.username.missing"));
        }
        if (this.password == null)
        {
            errors.add("password", new ActionError(
                    "error.userlogin.password.missing"));
        }
        return errors;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

}
