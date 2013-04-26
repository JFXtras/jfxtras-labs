package jfxtras.labs.map.tile.attribution;

import jfxtras.labs.map.Coordinate;

/**
 *
 * @author Mario Schröder
 */
public class Attribution {

    private String text;

    private int minZoom;

    private int maxZoom;

    private Coordinate min;

    private Coordinate max;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    public int getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }

    public Coordinate getMin() {
        return min;
    }

    public void setMin(Coordinate min) {
        this.min = min;
    }

    public Coordinate getMax() {
        return max;
    }

    public void setMax(Coordinate max) {
        this.max = max;
    }
}
