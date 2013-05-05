package jfxtras.labs.map.tile;

import jfxtras.labs.map.tile.attribution.AttributtionStringBuilder;
import jfxtras.labs.map.tile.attribution.Attribution;
import java.util.List;
import jfxtras.labs.map.Coordinate;

/**
 * {@link TileSource} for bing maps.
 * @author Mario Schroeder
 */
public class BingTileSource extends DefaultTileSource {

    private List<Attribution> attributions;

    public BingTileSource(String name, String base_url) {
        super(name, base_url);
    }

    void setAttributions(List<Attribution> attributions) {
        this.attributions = attributions;
    }

    @Override
    public String getAttributionText(int zoom, Coordinate topLeft, Coordinate botRight) {

        AttributtionStringBuilder builder = new AttributtionStringBuilder(zoom, topLeft, botRight);
        for (Attribution attr : attributions) {
            builder.append(attr);
        }

        return builder.toString();

    }
}