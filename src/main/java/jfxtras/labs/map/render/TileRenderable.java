package jfxtras.labs.map.render;


import java.awt.Point;

import javafx.scene.Group;

import jfxtras.labs.map.Moveable;

/**
 * Interface for the tile renderer.
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
	int prepareTiles(Moveable mapController);
	
	/**
	 * Gets the bounds of all tiles to be painted. <br/>
	 * The first point in the array is the upper left corner.<br/>
	 * The second point is the lower right corner.
	 * 
	 * @return points of the upper left and lower right corner
	 */
	Point[] getBounds();
	
	/**
	 * Renders all tiles which are loaded before.
	 * 
	 * @param mapController
	 */
	void doRender(Group tilesGroup);
}