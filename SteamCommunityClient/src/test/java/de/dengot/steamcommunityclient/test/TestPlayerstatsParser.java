package de.dengot.steamcommunityclient.test;

import static org.testng.Assert.assertEquals;
import generated.Playerstats;

import java.io.InputStreamReader;

import org.testng.annotations.Test;

import de.dengot.steamcommunityclient.parser.PlayerstatsParser;

public class TestPlayerstatsParser {

    @Test
    public void testParsing() {
        PlayerstatsParser parser = new PlayerstatsParser();
        Playerstats playerstats =
                parser.read(new InputStreamReader(getClass().getResourceAsStream(
                        "/sampledata/steamplayerstats-sample.xml")));

        assertEquals(playerstats.getGame().getGameName(), "The Elder Scrolls V: Skyrim");
    }
}
