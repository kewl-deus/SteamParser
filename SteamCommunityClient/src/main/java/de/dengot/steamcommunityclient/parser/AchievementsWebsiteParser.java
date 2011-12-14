package de.dengot.steamcommunityclient.parser;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AchievementsWebsiteParser {

	private final String XPATH_ACHIEVE_IMG_HOLDER = "//div[@class='achieveImgHolder']";

	private final String XPATH_ACHIEVE_TXT = XPATH_ACHIEVE_IMG_HOLDER
			+ "/../div[@class='achieveTxtHolder']/div[@class='achieveTxt']";

	public Properties parseDescriptions(Document document) throws XPathExpressionException {
		Properties props = new Properties();

		NodeList txtHolders = (NodeList) getXPath().evaluate(XPATH_ACHIEVE_TXT, document, XPathConstants.NODESET);

		for (int i = 0; i < txtHolders.getLength(); i++) {
			Node txtHolder = txtHolders.item(i);

			Node txtNameNode = txtHolder.getChildNodes().item(0);
			Node txtDescriptionNode = txtHolder.getChildNodes().item(1);

			String achieveName = getText(txtNameNode);
			String achieveDescription = getText(txtDescriptionNode);
			String achiveKey = clean(achieveName);

			props.setProperty(achiveKey, achieveDescription);

			System.out.println(achiveKey + "=" + achieveDescription);
			//System.out.println(achiveKey + "=" + achieveName);
		}

		return props;
	}


	public Properties parseImages(Document document) throws XPathExpressionException {
		Properties props = new Properties();

		NodeList imgHolders = (NodeList) getXPath()
				.evaluate(XPATH_ACHIEVE_IMG_HOLDER, document, XPathConstants.NODESET);
		NodeList txtHolders = (NodeList) getXPath().evaluate(XPATH_ACHIEVE_TXT, document, XPathConstants.NODESET);

		if (txtHolders.getLength() != imgHolders.getLength()) {
			throw new RuntimeException("number of image and text nodes do not match!");
		}

		for (int i = 0; i < txtHolders.getLength(); i++) {
			Node imgHolder = imgHolders.item(i);
			Node txtHolder = txtHolders.item(i);

			Attr imgSrcAttr = (Attr) imgHolder.getFirstChild().getAttributes().getNamedItem("src");
			Node txtNameNode = txtHolder.getChildNodes().item(0);

			String imgFilename = imgSrcAttr.getValue();
			String achieveName = getText(txtNameNode);
			String achiveKey = clean(achieveName);

			props.setProperty(achiveKey, imgFilename);
			System.out.println(achiveKey + "=" + imgFilename);
		}

		return props;
	}

	private XPath getXPath() {
		XPath xpath = XPathFactory.newInstance().newXPath();
		return xpath;
	}

	
	private String getText(Node node){
		return unescapeHtml(node.getFirstChild().getNodeValue());
	}

	private String clean(String s) {
		Matcher matcher = Pattern.compile("([A-z]|\\d)*").matcher(s);
		StringBuilder sb = new StringBuilder();
		while(matcher.find()){
			String group = matcher.group();
			sb.append(group);
		}
		return sb.toString();
	}
	
	private String unescapeHtml(String s) {

		StringBuffer sbuf = new StringBuffer();
		int l = s.length();
		int ch = -1;
		int b, sumb = 0;
		for (int i = 0, more = -1; i < l; i++) {
			/* Get next byte b from URL segment s */
			switch (ch = s.charAt(i)) {
			case '%':
				ch = s.charAt(++i);
				int hb = (Character.isDigit((char) ch) ? ch - '0' : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
				ch = s.charAt(++i);
				int lb = (Character.isDigit((char) ch) ? ch - '0' : 10 + Character.toLowerCase((char) ch) - 'a') & 0xF;
				b = (hb << 4) | lb;
				break;
			case '+':
				b = ' ';
				break;
			default:
				b = ch;
			}
			/* Decode byte b as UTF-8, sumb collects incomplete chars */
			if ((b & 0xc0) == 0x80) { // 10xxxxxx (continuation byte)
				sumb = (sumb << 6) | (b & 0x3f); // Add 6 bits to sumb
				if (--more == 0)
					sbuf.append((char) sumb); // Add char to sbuf
			} else if ((b & 0x80) == 0x00) { // 0xxxxxxx (yields 7 bits)
				sbuf.append((char) b); // Store in sbuf
			} else if ((b & 0xe0) == 0xc0) { // 110xxxxx (yields 5 bits)
				sumb = b & 0x1f;
				more = 1; // Expect 1 more byte
			} else if ((b & 0xf0) == 0xe0) { // 1110xxxx (yields 4 bits)
				sumb = b & 0x0f;
				more = 2; // Expect 2 more bytes
			} else if ((b & 0xf8) == 0xf0) { // 11110xxx (yields 3 bits)
				sumb = b & 0x07;
				more = 3; // Expect 3 more bytes
			} else if ((b & 0xfc) == 0xf8) { // 111110xx (yields 2 bits)
				sumb = b & 0x03;
				more = 4; // Expect 4 more bytes
			} else /* if ((b & 0xfe) == 0xfc) */{ // 1111110x (yields 1 bit)
				sumb = b & 0x01;
				more = 5; // Expect 5 more bytes
			}
			/* We don't test if the UTF-8 encoding is well-formed */
		}
		return sbuf.toString();
	}
}
