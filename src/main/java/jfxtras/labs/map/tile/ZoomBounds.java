package jfxtras.labs.map.tile;

/**
 * Values for minimum and maximum zoom.
 * 
 * @author Mario Schroeder
 */
public enum ZoomBounds {
    
    MAX(22), Min(1);
    
    private int value;

    private ZoomBounds(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
    
}
