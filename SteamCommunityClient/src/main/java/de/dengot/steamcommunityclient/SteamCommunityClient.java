package de.dengot.steamcommunityclient;

import generated.GamesList;
import generated.Playerstats;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import de.dengot.steamcommunityclient.parser.PlayerstatsParser;

public class SteamCommunityClient {

	private static final XLogger LOGGER = XLoggerFactory.getXLogger(SteamCommunityClient.class);
	
	public Playerstats getPlayerstats(String steamProfileName, String gameName) throws IOException {
		String rawLink = "http://steamcommunity.com/id/{0}/stats/{1}/?xml=1";
		String link = MessageFormat.format(rawLink, steamProfileName, gameName);
		
		LOGGER.trace("Requesting {}", link);
		
		try {
			URL url = new URL(link);
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
			PlayerstatsParser parser = new PlayerstatsParser();
			Playerstats playerstats = parser.read(reader);
			return playerstats;
			
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	public GamesList getGamesList(String steamProfileName){
		String rawLink = "http://steamcommunity.com/id/{0}/games/?xml=1";
		String link = MessageFormat.format(rawLink, steamProfileName);
		throw new UnsupportedOperationException("todo");
	}
}
