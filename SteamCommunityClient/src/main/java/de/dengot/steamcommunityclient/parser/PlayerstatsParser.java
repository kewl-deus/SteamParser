package de.dengot.steamcommunityclient.parser;

import generated.ObjectFactory;
import generated.Playerstats;

import java.io.Reader;
import java.io.Writer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

public class PlayerstatsParser {
	private static final XLogger LOGGER = XLoggerFactory.getXLogger(PlayerstatsParser.class);

	public Playerstats read(Reader reader) {
		LOGGER.entry();
		Playerstats playerstats = null;
		try {
			String packageName = Playerstats.class.getPackage().getName();
			JAXBContext jc = JAXBContext.newInstance(packageName);
			Unmarshaller u = jc.createUnmarshaller();

			playerstats = (Playerstats) u.unmarshal(reader);

			LOGGER.exit(playerstats);
		} catch (JAXBException e) {
			throw new RuntimeException("Deserializing xml data failed: XML-Binding-Error", e);
		}
		return playerstats;
	}

	public void write(Playerstats playerstats, Writer writer) {
		LOGGER.entry(playerstats);
		final String packageName = Playerstats.class.getPackage().getName();
		try {
			JAXBContext jc = JAXBContext.newInstance(packageName);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			ObjectFactory of = new ObjectFactory();
			Playerstats xmlRoot = of.createPlayerstats();
			m.marshal(xmlRoot, writer);
		} catch (JAXBException e) {
			throw new RuntimeException("Serializing xml data failed: XML-Binding-Error", e);
		}
		LOGGER.exit();
	}
}
