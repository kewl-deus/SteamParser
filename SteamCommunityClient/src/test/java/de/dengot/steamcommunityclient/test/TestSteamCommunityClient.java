package de.dengot.steamcommunityclient.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import generated.GamesList;
import generated.Playerstats;

import org.testng.annotations.Test;

import de.dengot.steamcommunityclient.SteamCommunityClient;

public class TestSteamCommunityClient {

    @Test(enabled = false)
    public void testGetSkyrimPlayerStats() {
        SteamCommunityClient client = new SteamCommunityClient();
        Playerstats playerstats = client.getPlayerstats("kewl-deus", "TheElderScrollsVSkyrim");

        assertEquals(playerstats.getGame().getGameName(), "The Elder Scrolls V: Skyrim");
    }

    @Test(enabled = false)
    public void testGetSkyrimPlaytime() {
        SteamCommunityClient client = new SteamCommunityClient();
        GamesList gamesList = client.getGamesList(76561197977709598l);

        assertFalse(gamesList.getGames().getGame().isEmpty());
    }
}
