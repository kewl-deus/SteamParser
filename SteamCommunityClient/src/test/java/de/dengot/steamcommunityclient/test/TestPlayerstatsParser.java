package de.dengot.steamcommunityclient.test;

import static org.testng.Assert.assertEquals;
import generated.Playerstats;
import generated.Playerstats.Achievements.Achievement;

import java.io.InputStreamReader;

import org.testng.annotations.Test;

import de.dengot.steamcommunityclient.parser.PlayerstatsParser;

public class TestPlayerstatsParser {

    @Test
    public void testParsing() {
        PlayerstatsParser parser = new PlayerstatsParser();
        Playerstats playerstats =
                parser.parse(new InputStreamReader(getClass().getResourceAsStream(
                        "/sampledata/steamplayerstats-sample.xml")));

        assertEquals(playerstats.getGame().getGameName(), "The Elder Scrolls V: Skyrim");
        
        for(Achievement achieve : playerstats.getAchievements().getAchievement()){
            System.out.println(achieve.getApiname() + "=" + achieve.getName());
        }
    }
}
