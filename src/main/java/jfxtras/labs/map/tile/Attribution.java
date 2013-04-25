package jfxtras.labs.map.tile;

import jfxtras.labs.map.Coordinate;

/**
 *
 * @author Mario Schröder
 */
class Attribution {

    private String text;

    private int minZoom;

    private int maxZoom;

    private Coordinate min;

    private Coordinate max;

    public String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }

    int getMinZoom() {
        return minZoom;
    }

    void setMinZoom(int minZoom) {
        this.minZoom = minZoom;
    }

    int getMaxZoom() {
        return maxZoom;
    }

    void setMaxZoom(int maxZoom) {
        this.maxZoom = maxZoom;
    }

    Coordinate getMin() {
        return min;
    }

    void setMin(Coordinate min) {
        this.min = min;
    }

    Coordinate getMax() {
        return max;
    }

    void setMax(Coordinate max) {
        this.max = max;
    }
}
