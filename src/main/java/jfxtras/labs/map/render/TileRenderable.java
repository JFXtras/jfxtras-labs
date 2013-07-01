package jfxtras.labs.map.render;


import jfxtras.labs.map.MapControlable;

/**
 * Interface for the map renderer.
 * 
 * @author Mario Schroeder
 */
public interface TileRenderable {

	/**
	 * Paints the map.
	 * 
	 * @param mapController
	 * @return the implementation should return the number of actual tiles
	 */
	int prepareTiles(MapControlable mapController);
	
	void doRender(MapControlable mapController);
}