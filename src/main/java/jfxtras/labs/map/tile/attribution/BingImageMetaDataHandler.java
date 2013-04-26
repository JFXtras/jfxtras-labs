package jfxtras.labs.map.tile.attribution;

import jfxtras.labs.map.Coordinate;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Metadata XML handler for bing images.
 * @author Mario Schröder
 */
public class BingImageMetaDataHandler extends BingMetaDataHandler {
    
    private static final String METADATA = "http://dev.virtualearth.net/REST/v1/Imagery/Metadata/Aerial/0,0?zl=1&mapVersion=v1&include=ImageryProviders&output=xml&key=";

    private double southLat;

    private double northLat;

    private double eastLon;

    private double westLon;

    private boolean inCoverage = false;

    public BingImageMetaDataHandler(String apiKey) {
        super(apiKey);
    }
    
    @Override
    public void startElement(String uri, String stripped, String tagName, Attributes attrs) throws SAXException {
        switch (tagName) {
            case "ImageryProvider":
                attribution = new Attribution();
                break;
            case "CoverageArea":
                inCoverage = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void endElement(String uri, String stripped, String tagName) throws SAXException {

        if ("ImageryProvider".equals(tagName)) {
            attributions.add(attribution);
        } else if ("Attribution".equals(tagName)) {
            attribution.setText(text);
        } else if (inCoverage && "ZoomMin".equals(tagName)) {
            attribution.setMinZoom(Integer.parseInt(text));
        } else if (inCoverage && "ZoomMax".equals(tagName)) {
            attribution.setMaxZoom(Integer.parseInt(text));
        } else if (inCoverage && "SouthLatitude".equals(tagName)) {
            southLat = Double.parseDouble(text);
        } else if (inCoverage && "NorthLatitude".equals(tagName)) {
            northLat = Double.parseDouble(text);
        } else if (inCoverage && "EastLongitude".equals(tagName)) {
            eastLon = Double.parseDouble(text);
        } else if (inCoverage && "WestLongitude".equals(tagName)) {
            westLon = Double.parseDouble(text);
        } else if ("BoundingBox".equals(tagName)) {
            attribution.setMin(new Coordinate(southLat, westLon));
            attribution.setMax(new Coordinate(northLat, eastLon));
        } else if ("CoverageArea".equals(tagName)) {
            inCoverage = false;
        }
        text = "";
    }

    @Override
    public String getMetaData() {
        return METADATA;
    }

}
