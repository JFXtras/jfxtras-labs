package jfxtras.labs.map.tile.attribution;

import jfxtras.labs.map.Coordinate;

/**
 * Builder for attributions.
 * @author Mario Schröder
 */
public class AttributtionStringBuilder {

    private StringBuilder builder;

    private int zoom;

    private Coordinate topLeft, bottomRight;

    public AttributtionStringBuilder(int zoom, Coordinate topLeft, Coordinate bottomRight) {
        builder = new StringBuilder();
        this.zoom = zoom;
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }

    public void append(Attribution attr) {
        
        if(attr.getMaxZoom() == Integer.MAX_VALUE){
            appendText(attr);
            
        } else if (zoom <= attr.getMaxZoom() && zoom >= attr.getMinZoom()) {
            if (isMaxLongitudeGreater(attr) && isMinLongitudeSmaller(attr)
                && isMinLatitudeSmaller(attr) && isMaxLatitudeGreater(attr)) {

                appendText(attr);
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

    private void appendText(Attribution attr) {
        builder.append(attr.getText()).append(" ");
    }
}
