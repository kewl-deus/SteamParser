package de.dengot.steamcommunityclient.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.tidy.Tidy;

import de.dengot.steamcommunityclient.parser.AchievementsWebsiteParser;

public class TestAchievementsWebsiteParser {

	@Test(enabled = false)
	public void testParsing() throws Exception {
		InputStream input = getClass().getResourceAsStream("/sampledata/SkyrimSteamGlobalAchievements.xhtml");
		Document document = buildTidyDom(input);
		AchievementsWebsiteParser parser = new AchievementsWebsiteParser();
		parser.parseDescriptions(document);
		parser.parseImages(document);
	}

	private Document buildDom(InputStream is) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(is);
		return doc;
	}

	private Document buildTidyDom(InputStream in) throws Exception {
		Tidy tidy = new Tidy();
		tidy.setQuiet(true);
		tidy.setXHTML(true);
		tidy.setDocType("omit");
		tidy.setNumEntities(true);
		tidy.setErrout(new PrintWriter(new NullOutputStream()));
		Document dom = tidy.parseDOM(in, new NullOutputStream());
		removeScripts(dom);
		return dom;
	}

	private void removeScripts(Document dom) {
		NodeList scripts = dom.getElementsByTagName("script");

		for (int i = 0; i < scripts.getLength(); i++) {
			Node script = scripts.item(0);
			script.getParentNode().removeChild(script);
		}
	}

	static class NullOutputStream extends OutputStream {

		@Override
		public void write(int b) throws IOException {
		}

		@Override
		public void write(byte[] b) throws IOException {
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
		}

	}
}
