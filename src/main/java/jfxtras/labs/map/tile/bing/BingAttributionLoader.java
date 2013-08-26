package jfxtras.labs.map.tile.bing;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import jfxtras.labs.map.tile.Attribution;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * Loads the attributions for the bing map.
 * @author Mario Schroeder
 */
public class BingAttributionLoader {

    private BingMetaDataHandler handler;

    public BingAttributionLoader(BingMetaDataHandler handler) {
        this.handler = handler;
    }

    public List<Attribution> load() {

        try {
            URL url = new URL(handler.getMetaDataUrl());
            URLConnection conn = url.openConnection();

            InputStream stream = conn.getInputStream();

            XMLReader parser = XMLReaderFactory.createXMLReader();
            parser.setContentHandler(handler);
            parser.parse(new InputSource(stream));

        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        
        return handler.getAttributions();
    }

}
