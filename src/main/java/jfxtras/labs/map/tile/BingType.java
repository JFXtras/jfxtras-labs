package jfxtras.labs.map.tile;

/**
 * This enum contains values of already provided bing tile types.
 * @author Mario Schroeder
 */
public enum BingType {
    
    Map, Aerial;
    
    private static final String BING = "Bing";

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(BING).append(" ").append(name());
		return builder.toString();
	}
    
    
}
