package de.dengot.steamcommunityclient;

import generated.GamesList;
import generated.Playerstats;
import generated.GamesList.Games.Game;
import generated.Playerstats.Achievements.Achievement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;
import java.util.Calendar;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import de.dengot.steamcommunityclient.model.PlayerProfile;
import de.dengot.steamcommunityclient.model.SteamGame;
import de.dengot.steamcommunityclient.parser.GamesListParser;
import de.dengot.steamcommunityclient.parser.PlayerstatsParser;

public class SteamCommunityClient {

    private static final XLogger LOGGER = XLoggerFactory.getXLogger(SteamCommunityClient.class);

    private Proxy proxy;

    public SteamCommunityClient() {
        this.proxy = null;
    }

    public SteamCommunityClient(String httpProxyHost, int httpProxyPort) {
        SocketAddress addr = new InetSocketAddress(httpProxyHost, httpProxyPort);
        this.proxy = new Proxy(Proxy.Type.HTTP, addr);
    }

    public PlayerProfile getPlayerProfile(long steamID64, String gameName) {

        Playerstats playerstats = getPlayerstats(steamID64, gameName);
        GamesList gamesList = getGamesList(steamID64);

        Game game = findGame(gamesList, playerstats.getGame().getGameName());
        SteamGame steamGame =
                new SteamGame(game.getAppID(), playerstats.getGame().getGameFriendlyName(),
                        playerstats.getGame().getGameName());

        PlayerProfile playerProfile =
                new PlayerProfile(steamID64, gamesList.getSteamID(), steamGame);

        playerProfile.setPlaytime(game.getHoursOnRecord().doubleValue());

        for (Achievement achieve : playerstats.getAchievements().getAchievement()) {
            if (achieve.getClosed() == 1) {
                playerProfile.addAchievement(achieve.getApiname(), toDate(achieve
                        .getUnlockTimestamp()));
            }
        }

        return playerProfile;
    }

    private Playerstats getPlayerstats(long steamID64, String gameName) {
        try {
            String rawLink = "http://steamcommunity.com/profiles/{0,number,0}/stats/{1}/?xml=1";
            String link = MessageFormat.format(rawLink, steamID64, gameName);
            return getPlayerstats(new URL(link), gameName);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Playerstats getPlayerstats(String steamProfileName, String gameName) {
        try {
            String rawLink = "http://steamcommunity.com/id/{0}/stats/{1}/?xml=1";
            String link = MessageFormat.format(rawLink, steamProfileName, gameName);
            return getPlayerstats(new URL(link), gameName);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Playerstats getPlayerstats(URL url, String gameName) throws IOException {
        LOGGER.trace("Requesting {}", url.toString());
        Reader reader = openURL(url);
        PlayerstatsParser parser = new PlayerstatsParser();
        Playerstats playerstats = parser.parse(reader);
        return playerstats;
    }

    private GamesList getGamesList(String steamProfileName) {
        try {
            String rawLink = "http://steamcommunity.com/id/{0}/games/?xml=1";
            String link = MessageFormat.format(rawLink, steamProfileName);
            return getGamesList(new URL(link));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GamesList getGamesList(long steamID64) {
        try {
            String rawLink = "http://steamcommunity.com/profiles/{0,number,0}/games/?xml=1";
            String link = MessageFormat.format(rawLink, steamID64);
            return getGamesList(new URL(link));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GamesList getGamesList(URL url) throws MalformedURLException, IOException {
        LOGGER.trace("Requesting {}", url.toString());
        Reader reader = openURL(url);
        GamesListParser parser = new GamesListParser();
        GamesList gamesList = parser.parse(reader);
        return gamesList;
    }

    private Reader openURL(URL url) throws IOException {
        BufferedReader reader;
        if (this.proxy == null) {
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
        } else {
            URLConnection connection = url.openConnection(this.proxy);
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        }
        return reader;
    }

    private Calendar toDate(long steamTimestamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(steamTimestamp * 1000);
        return cal;
    }

    private Game findGame(GamesList gamesList, String gameTitle) {
        for (Game game : gamesList.getGames().getGame()) {
            if (game.getName().equals(gameTitle)) {
                return game;
            }
        }
        return null;
    }

}
