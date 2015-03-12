package com.ehsunbehravesh.mypasswords.gui.embeddedweb.utils;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author ehsun.behravesh
 */
public class Utils {

  public static Document parseXml(String xmlString) throws SAXException, IOException, ParserConfigurationException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;

    builder = factory.newDocumentBuilder();
    return builder.parse(new InputSource(new StringReader(xmlString)));
  }

}
