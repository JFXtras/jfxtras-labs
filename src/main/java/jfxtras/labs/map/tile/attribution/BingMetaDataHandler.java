package jfxtras.labs.map.tile.attribution;

import java.util.ArrayList;
import java.util.List;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Parent class for bing metadata handling.
 * @author Mario Schroeder
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
    
    /**
     * This method returns the string to the metadata url including the api key.
     * @return url to metadata as string
     */
    public String getMetaDataUrl(){
        return getMetaData("&key="+ apiKey);
    }

    /**
     * This method needs to return the path to the metadata with the api key appended.
     * Sample: http://dev.xy.com/meta/xxx&key=uvwxyz
     * @return path to metadata as string
     */
    protected abstract String getMetaData(String keyToAppend);

    
}
