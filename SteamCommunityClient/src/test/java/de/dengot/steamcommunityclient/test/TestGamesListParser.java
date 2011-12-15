package de.dengot.steamcommunityclient.test;

import static org.testng.Assert.assertEquals;
import generated.GamesList;
import generated.GamesList.Games.Game;

import java.io.InputStreamReader;

import org.testng.annotations.Test;

import de.dengot.steamcommunityclient.parser.GamesListParser;

public class TestGamesListParser {

    
    @Test
    public void testParsing(){
        GamesListParser parser = new GamesListParser();
        GamesList gamesList =
                parser.parse(new InputStreamReader(getClass().getResourceAsStream(
                        "/sampledata/steamgamelist-sample.xml")));

        assertEquals(gamesList.getGames().getGame().size(), 15);
        
        Game skyrim = findGame(gamesList, "The Elder Scrolls V: Skyrim");
        
        assertEquals(skyrim.getHoursOnRecord().doubleValue(), 157.1d);
        assertEquals(skyrim.getHoursLast2Weeks().doubleValue(), 44.3d);
    }
    
    private Game findGame(GamesList gamesList, String name){
        for (Game game : gamesList.getGames().getGame()) {
            if (game.getName().equals(name)){
                return game;
            }
        }
        return null;
    }
}
