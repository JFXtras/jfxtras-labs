package jfxtras.labs.map.tile;

/**
 * This enum contains values of already provided bing tile types.
 * @author msc
 */
public enum BingType {
    
    Bing("Bing"), BingAerial("Bing Aerial");
    
    private final String description;
    
    private BingType(final String val){
        this.description = val;
    }

	public String getDescription() {
		return description;
	}
}
