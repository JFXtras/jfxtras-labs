package jfxtras.labs.map.tile.osm;

/**
 * This enum contains values of already provided osm tile types.
 * @author Mario Schroeder
 */
public enum OsmType {
	
    Mapnik, CycleMap, Transport, Landscape;
    
    private static final String OSM = "OSM";

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(OSM).append(" ").append(name());
		return builder.toString();
	}
    
    
}
