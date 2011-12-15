package de.dengot.steamcommunityclient.parser;

import generated.GamesList;

import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class GamesListParser {

    private static final XLogger LOGGER = XLoggerFactory.getXLogger(GamesListParser.class);

    public GamesList parse(Reader reader) {
        LOGGER.entry();
        GamesList gamesList = null;
        try {
            String packageName = GamesList.class.getPackage().getName();
            JAXBContext jc = JAXBContext.newInstance(packageName);
            Unmarshaller u = jc.createUnmarshaller();

            gamesList = (GamesList) u.unmarshal(reader);

            LOGGER.exit(gamesList);
        } catch (JAXBException e) {
            throw new RuntimeException("Parsing xml data failed: XML-Binding-Error", e);
        }
        return gamesList;
    }
}
