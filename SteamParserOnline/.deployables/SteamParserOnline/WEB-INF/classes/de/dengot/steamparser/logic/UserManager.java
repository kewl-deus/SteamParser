package de.dengot.steamparser.logic;

import de.dengot.steamparser.exceptions.UnknownUserException;
import de.dengot.steamparser.model.User;

public class UserManager
{
    private static UserManager manager;

    private UserManager()
    {
    }

    public static synchronized UserManager getTheManager()
    {
        if (manager == null)
        {
            manager = new UserManager();
        }
        return manager;
    }

    public User checkLogin(String username, String password)
            throws UnknownUserException
    {
        if ("kewl".equals(username) && "deus".equals(password))
        {
            return new User("kewl", "deus");
        }
        else
        {
            throw new UnknownUserException("User: " + username
                    + " with password '" + password + "' is unknown!");
        }
    }

}
