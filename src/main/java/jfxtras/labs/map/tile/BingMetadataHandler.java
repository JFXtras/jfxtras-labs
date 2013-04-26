package jfxtras.labs.map.tile;

import java.util.ArrayList;
import java.util.List;
import jfxtras.labs.map.Coordinate;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Metadata XML handler for bin map. 
 * @author Mario Schröder
 */
class BingMetadataHandler extends DefaultHandler {
    
    protected String text;

    protected Attribution attribution;

    protected List<Attribution> attributions;

    BingMetadataHandler() {
        attributions = new ArrayList<>();
    }

    List<Attribution> getAttributions() {
        return attributions;
    }

    @Override
    public void startElement(String uri, String stripped, String tagName, Attributes attrs) throws SAXException {
        switch (tagName) {
            case "string":
                attribution = new Attribution();
                //use a pointless max zoom level to indicate that we don't care about zoom and coordinates
                attribution.setMaxZoom(Integer.MAX_VALUE);
                break;
            default:
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        text = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String stripped, String tagName) throws SAXException {

        if ("string".equals(tagName)) {
            attribution.setText(text);
            attributions.add(attribution);
        } 
    }
}
