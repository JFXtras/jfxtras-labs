package jfxtras.labs.map;

/**
 * Values for minimum and maximum zoom.
 * 
 * @author Mario Schröder
 */
enum ZoomBounds {
    
    MAX(22), Min(0);
    
    private int value;

    private ZoomBounds(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
}
