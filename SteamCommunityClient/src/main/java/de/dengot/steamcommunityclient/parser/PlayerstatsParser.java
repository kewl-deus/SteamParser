package de.dengot.steamcommunityclient.parser;

import generated.ObjectFactory;
import generated.Playerstats;

import java.io.Reader;
import java.io.Writer;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.xml.sax.SAXException;


public class PlayerstatsParser {
    private static final XLogger LOGGER = XLoggerFactory.getXLogger(PlayerstatsParser.class);

    public Playerstats read(Reader reader) {
        LOGGER.entry();
        Playerstats playerstats = null;
        ValidationEventCollector vec = new ValidationEventCollector();
        try {
            String packageName = Playerstats.class.getPackage().getName();
            JAXBContext jc = JAXBContext.newInstance(packageName);
            Unmarshaller u = jc.createUnmarshaller();
            u.setSchema(getSchema());
            u.setEventHandler(vec);

//            JAXBElement<Playerstats> unmarshalled = (JAXBElement<Playerstats>) u.unmarshal(reader);
//            playerstats = unmarshalled.getValue();
            
            playerstats = (Playerstats) u.unmarshal(reader);
            
            LOGGER.exit(playerstats);
        } catch (JAXBException e) {
            if (vec.hasEvents()) {
                for (ValidationEvent ve : vec.getEvents()) {
                    String msg = ve.getMessage();
                    ValidationEventLocator vel = ve.getLocator();
                    int line = vel.getLineNumber();
                    int column = vel.getColumnNumber();
                    LOGGER.error("XML-Validation-Error: " + line + "." + column + ": " + msg);
                }
            }
            throw new RuntimeException("Deserializing xml data failed: XML-Binding-Error", e);
        } catch (SAXException e) {
            throw new RuntimeException("Unable to load xml-schema", e);
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

    private Schema getSchema() throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema =
                sf.newSchema(getClass().getResource("/META-INF/schema/steamplayerstats.xsd"));
        return schema;
    }
}
