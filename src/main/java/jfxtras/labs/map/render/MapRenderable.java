package jfxtras.labs.map.render;

import jfxtras.labs.map.MapControlable;

/**
 * Interface for the map.
 * 
 * @author Mario Schroeder
 */
public interface MapRenderable {

	/**
	 * Paints the map.
	 * 
	 * @param mapController
	 * @return the implementation should return true if the map was updated
	 */
	boolean render(MapControlable mapController);
}
