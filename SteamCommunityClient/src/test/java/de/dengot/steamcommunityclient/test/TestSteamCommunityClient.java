package de.dengot.steamcommunityclient.test;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import de.dengot.steamcommunityclient.SteamCommunityClient;
import de.dengot.steamcommunityclient.model.PlayerProfile;

public class TestSteamCommunityClient {

    @Test(enabled = true)
    public void testGetPlayerProfile() {
        SteamCommunityClient client = new SteamCommunityClient();
        PlayerProfile playerProfile =
                client.getPlayerProfile(76561197963574585L, "TheElderScrollsVSkyrim");

        assertEquals(playerProfile.getSteamId(), 76561197963574585L);
        assertEquals(playerProfile.getSteamName(), "kewl-deus");
        assertEquals(playerProfile.getGame().getTitle(), "The Elder Scrolls V: Skyrim");
        assertEquals(playerProfile.getGame().getTechnicalName(), "TheElderScrollsVSkyrim");
        assertEquals(playerProfile.getGame().getAppId(), 72850);
    }

}
