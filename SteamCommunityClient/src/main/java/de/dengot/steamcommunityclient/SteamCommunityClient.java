package de.dengot.steamcommunityclient;

import generated.GamesList;
import generated.Playerstats;

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

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

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

    public Playerstats getPlayerstats(long steamID64, String gameName) {
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

    public Playerstats getPlayerstats(String steamProfileName, String gameName) {
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

    public GamesList getGamesList(String steamProfileName) {
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

    public GamesList getGamesList(long steamID64) {
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
}
