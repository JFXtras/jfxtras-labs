package jfxtras.labs.map.tile;

import jfxtras.labs.map.Coordinate;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import javafx.scene.image.Image;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

class BingTileSource extends DefaultTileSource {

    private static String API_KEY = "Arzdiw4nlOJzRwOz__qailc8NiR31Tt51dN2D7cm57NrnceZnCpgOkmJhNpGoppU";

    private static volatile Future<List<Attribution>> attributions;

    public BingTileSource() {
        super("Bing Aerial Maps", "http://ecn.t2.tiles.virtualearth.net/tiles/");

        if (attributions == null) {
            attributions = Executors.newSingleThreadExecutor().submit(new Callable<List<Attribution>>() {
                @Override
                public List<Attribution> call() throws Exception {
                    return loadAttributionText();
                }
            });
        }
    }

    private static class Attribution {

        String attribution;

        int minZoom;

        int maxZoom;

        Coordinate min;

        Coordinate max;

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
                curr.attribution = string;
            } else if (inCoverage && "ZoomMin".equals(tagName)) {
                curr.minZoom = Integer.parseInt(string);
            } else if (inCoverage && "ZoomMax".equals(tagName)) {
                curr.maxZoom = Integer.parseInt(string);
            } else if (inCoverage && "SouthLatitude".equals(tagName)) {
                southLat = Double.parseDouble(string);
            } else if (inCoverage && "NorthLatitude".equals(tagName)) {
                northLat = Double.parseDouble(string);
            } else if (inCoverage && "EastLongitude".equals(tagName)) {
                eastLon = Double.parseDouble(string);
            } else if (inCoverage && "WestLongitude".equals(tagName)) {
                westLon = Double.parseDouble(string);
            } else if ("BoundingBox".equals(tagName)) {
                curr.min = new Coordinate(southLat, westLon);
                curr.max = new Coordinate(northLat, eastLon);
            } else if ("CoverageArea".equals(tagName)) {
                inCoverage = false;
            }
            string = "";
        }
    }

    private List<Attribution> loadAttributionText() {
        try {
            URL u = new URL("http://dev.virtualearth.net/REST/v1/Imagery/Metadata/Aerial/0,0?zl=1&mapVersion=v1&key="
                + API_KEY + "&include=ImageryProviders&output=xml");
            URLConnection conn = u.openConnection();

            // This is not JOSM! Do not use anything other than standard JRE classes within this package!
            // See package.html for details
            //conn.setConnectTimeout(Main.pref.getInteger("imagery.bing.load-attribution-text.timeout", 4000));

            InputStream stream = conn.getInputStream();

            XMLReader parser = XMLReaderFactory.createXMLReader();
            AttrHandler handler = new AttrHandler();
            parser.setContentHandler(handler);
            parser.parse(new InputSource(stream));
            //System.err.println("Added " + handler.attributions.size() + " attributions.");
            return handler.attributions;
        } catch (IOException | SAXException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getTilePath(int zoom, int tilex, int tiley) {

        String quadtree = computeQuadTree(zoom, tilex, tiley);
        return "/tiles/a" + quadtree + "." + getTileType() + "?g=587";
    }

    @Override
    public boolean isAttributionRequired() {
        return true;
    }

    @Override
    public Image getAttributionImage() {
        return new Image(getClass().getResourceAsStream("bing_maps.png"));
    }

    @Override
    public String getAttributionLinkURL() {
        //return "http://bing.com/maps"
        // FIXME: I've set attributionLinkURL temporarily to ToU URL to comply with bing ToU
        // (the requirement is that we have such a link at the bottom of the window)
        return "http://go.microsoft.com/?linkid=9710837";
    }

    @Override
    public String getTermsOfUseURL() {
        return "http://opengeodata.org/microsoft-imagery-details";
    }

    @Override
    public String getAttributionText(int zoom, Coordinate topLeft, Coordinate botRight) {
        try {
            if (!attributions.isDone()) {
                return "Loading Bing attribution data...";
            }
            if (attributions.get() == null) {
                return "Error loading Bing attribution data";
            }
            StringBuilder a = new StringBuilder();
            for (Attribution attr : attributions.get()) {
                if (zoom <= attr.maxZoom && zoom >= attr.minZoom) {
                    if (topLeft.getLon() < attr.max.getLon() && botRight.getLon() > attr.min.getLon()
                        && topLeft.getLat() > attr.min.getLat() && botRight.getLat() < attr.max.getLat()) {
                        a.append(attr.attribution);
                        a.append(" ");
                    }
                }
            }
            return a.toString();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return "Error loading Bing attribution data";
    }

    static String computeQuadTree(int zoom, int tilex, int tiley) {
        StringBuilder k = new StringBuilder();
        for (int i = zoom; i > 0; i--) {
            char digit = 48;
            int mask = 1 << (i - 1);
            if ((tilex & mask) != 0) {
                digit += 1;
            }
            if ((tiley & mask) != 0) {
                digit += 2;
            }
            k.append(digit);
        }
        return k.toString();
    }
}