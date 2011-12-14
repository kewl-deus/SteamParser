package de.dengot.steamcommunityclient.test;

import static org.testng.Assert.assertEquals;
import generated.Playerstats;

import java.io.IOException;

import org.testng.annotations.Test;

import de.dengot.steamcommunityclient.SteamCommunityClient;

public class TestSteamCommunityClient {

	@Test(enabled = false)
	public void testGetSkyrimPlayerStats() throws IOException {
		SteamCommunityClient client = new SteamCommunityClient();
		Playerstats playerstats = client.getPlayerstats("kewl-deus", "TheElderScrollsVSkyrim");

		assertEquals(playerstats.getGame().getGameName(), "The Elder Scrolls V: Skyrim");
	}
}
