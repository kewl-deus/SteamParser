package de.dengot.steamcommunityclient.parser;

import generated.Playerstats;

import java.io.Reader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class PlayerstatsParser {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(PlayerstatsParser.class);

    public Playerstats parse(Reader reader) {
        LOGGER.entry();
        Playerstats playerstats = null;
        try {
            String packageName = Playerstats.class.getPackage().getName();
            JAXBContext jc = JAXBContext.newInstance(packageName);
            Unmarshaller u = jc.createUnmarshaller();

            playerstats = (Playerstats) u.unmarshal(reader);

            LOGGER.exit(playerstats);
        } catch (JAXBException e) {
            throw new RuntimeException("Parsing xml data failed: XML-Binding-Error", e);
        }
        return playerstats;
    }
}
