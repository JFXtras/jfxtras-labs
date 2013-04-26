package jfxtras.labs.map.tile.attribution;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parent class for bing metadata handling.
 * @author msc
 */
public abstract class BingMetaDataHandler extends DefaultHandler {

    protected Attribution attribution;

    protected List<Attribution> attributions;

    protected String text;
    
    private String apiKey;

    public BingMetaDataHandler(String apiKey) {
        this.apiKey = apiKey;
        attributions = new ArrayList<>();
    }
    
    public List<Attribution> getAttributions() {
        return attributions;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {

        text = new String(ch, start, length);
    }
    
    public String getMetaDataUrl(){
        return getMetaData() + apiKey;
    }

    protected abstract String getMetaData();

    
}
