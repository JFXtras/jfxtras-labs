package jfxtras.labs.map.tile;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parent class for bing metadata handling.
 * @author msc
 */
abstract class BingMetadataHandler extends DefaultHandler {

    protected Attribution attribution;

    protected List<Attribution> attributions;

    protected String text;

    BingMetadataHandler() {
        attributions = new ArrayList<>();
    }
    
    List<Attribution> getAttributions() {
        return attributions;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        text = new String(ch, start, length);
    }

}
