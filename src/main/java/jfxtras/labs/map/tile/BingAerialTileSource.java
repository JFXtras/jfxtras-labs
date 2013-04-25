package jfxtras.labs.map.tile;

import jfxtras.labs.map.Coordinate;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

class BingAerialTileSource extends DefaultTileSource {

    private static volatile Future<List<Attribution>> attributions;

    public BingAerialTileSource(String name, String base_url) {
        super(name, base_url);

        if (attributions == null) {
            attributions = Executors.newSingleThreadExecutor().submit(new Callable<List<Attribution>>() {
                @Override
                public List<Attribution> call() throws Exception {
                    return loadAttributionText();
                }
            });
        }
    }

    private static class AttrHandler extends DefaultHandler {

        private String string;

        private Attribution curr;

        private List<Attribution> attributions = new ArrayList<>();

        private double southLat;

        private double northLat;

        private double eastLon;

        private double westLon;

        private boolean inCoverage = false;

        @Override
        public void startElement(String uri, String stripped, String tagName, Attributes attrs) throws SAXException {
            switch (tagName) {
                case "ImageryProvider":
                    curr = new Attribution();
                    break;
                case "CoverageArea":
                    inCoverage = true;
                    break;
                default:
                    break;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            string = new String(ch, start, length);
        }

        @Override
        public void endElement(String uri, String stripped, String tagName) throws SAXException {
            if ("ImageryProvider".equals(tagName)) {
                attributions.add(curr);
            } else if ("Attribution".equals(tagName)) {
                curr.setText(string);
            } else if (inCoverage && "ZoomMin".equals(tagName)) {
                curr.setMinZoom(Integer.parseInt(string));
            } else if (inCoverage && "ZoomMax".equals(tagName)) {
                curr.setMaxZoom(Integer.parseInt(string));
            } else if (inCoverage && "SouthLatitude".equals(tagName)) {
                southLat = Double.parseDouble(string);
            } else if (inCoverage && "NorthLatitude".equals(tagName)) {
                northLat = Double.parseDouble(string);
            } else if (inCoverage && "EastLongitude".equals(tagName)) {
                eastLon = Double.parseDouble(string);
            } else if (inCoverage && "WestLongitude".equals(tagName)) {
                westLon = Double.parseDouble(string);
            } else if ("BoundingBox".equals(tagName)) {
                curr.setMin(new Coordinate(southLat, westLon));
                curr.setMax(new Coordinate(northLat, eastLon));
            } else if ("CoverageArea".equals(tagName)) {
                inCoverage = false;
            }
            string = "";
        }
    }

    private List<Attribution> loadAttributionText() {

        List<Attribution> attr = null;

        try {
            URL url = buildUrl();
            URLConnection conn = url.openConnection();

            // This is not JOSM! Do not use anything other than standard JRE classes within this package!
            // See package.html for details
            //conn.setConnectTimeout(Main.pref.getInteger("imagery.bing.load-attribution-text.timeout", 4000));

            InputStream stream = conn.getInputStream();

            XMLReader parser = XMLReaderFactory.createXMLReader();
            AttrHandler handler = new AttrHandler();
            parser.setContentHandler(handler);
            parser.parse(new InputSource(stream));

            attr = handler.attributions;
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        return attr;
    }

    @Override
    public String getAttributionText(int zoom, Coordinate topLeft, Coordinate botRight) {

        String text;
        try {
            if (!attributions.isDone()) {
                text = "Loading Bing attribution data...";
            } else if (attributions.get() == null) {
                text = "Error loading Bing attribution data";
            } else {
                AttributtionStringBuilder builder = new AttributtionStringBuilder(zoom, topLeft, botRight);
                for (Attribution attr : attributions.get()) {
                    builder.append(attr);
                }
                text = builder.toString();
            }
        } catch (InterruptedException | ExecutionException e) {
            text = e.getMessage();
        }
        return text;
    }

    private URL buildUrl() throws MalformedURLException {
        StringBuilder builder = new StringBuilder();
        builder.append("http://dev.virtualearth.net/REST/v1/Imagery/Metadata/Aerial/0,0?zl=1&mapVersion=v1");
        builder.append("&include=ImageryProviders&output=xml");
        builder.append("&key=").append(getApiKey());
        return new URL(builder.toString());
    }
}