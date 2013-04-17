package jfxtras.labs.map.tile;

/**
 * This enum contains values of already provided bing tile types.
 * @author msc
 */
public enum BingType {
    
    Bing("Bing"), BingAerial("Bing Aerial");
    
    private String val;
    
    private BingType(String val){
        this.val = val;
    }

    @Override
    public String toString() {
        return val;
    }
}
