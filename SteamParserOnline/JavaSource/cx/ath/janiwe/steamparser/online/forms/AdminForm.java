package cx.ath.janiwe.steamparser.online.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;

public class AdminForm extends ActionForm
{

    private FormFile zipFile;

    private Harpoon harpoon = new Harpoon();

    private String gameType;

    public AdminForm()
    {
    }

    public FormFile getZipFile()
    {
        return zipFile;
    }

    public void setZipFile(FormFile zipFile)
    {
        this.zipFile = zipFile;
    }

    public Harpoon getHarpoon()
    {
        return harpoon;
    }

    public void setHarpoon(Harpoon harpoon)
    {
        this.harpoon = harpoon;
    }

    public String getGameType()
    {
        return gameType;
    }

    public void setGameType(String gameType)
    {
        this.gameType = gameType;
    }

    public void reset(ActionMapping arg0, HttpServletRequest arg1)
    {
        super.reset(arg0, arg1);
        zipFile = null;
        harpoon = new Harpoon();
    }

}
