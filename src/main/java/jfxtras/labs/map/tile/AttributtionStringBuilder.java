package jfxtras.labs.map.tile;

import jfxtras.labs.map.Coordinate;

/**
 *
 * @author Mario Schröder
 */
class AttributtionStringBuilder {

    private StringBuilder builder;

    private int zoom;

    private Coordinate topLeft, bottomRight;

    AttributtionStringBuilder(int zoom, Coordinate topLeft, Coordinate bottomRight) {
        builder = new StringBuilder();
        this.zoom = zoom;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    void append(Attribution attr) {
        if (zoom <= attr.getMaxZoom() && zoom >= attr.getMinZoom()) {
            if (isMaxLongitudeGreater(attr) && isMinLongitudeSmaller(attr)
                && isMinLatitudeSmaller(attr) && isMaxLatitudeGreater(attr)) {

                builder.append(attr.getText());
                builder.append(" ");
            }
        }
    }

    private boolean isMaxLongitudeGreater(Attribution attr) {
        return topLeft.getLongitude() < attr.getMax().getLongitude();
    }

    private boolean isMinLongitudeSmaller(Attribution attr) {
        return bottomRight.getLongitude() > attr.getMin().getLongitude();
    }

    private boolean isMinLatitudeSmaller(Attribution attr) {
        return topLeft.getLatitude() > attr.getMin().getLatitude();
    }

    private boolean isMaxLatitudeGreater(Attribution attr) {
        return bottomRight.getLatitude() < attr.getMax().getLatitude();
    }
    
    @Override
    public String toString() {
        return builder.toString();
    }
}
