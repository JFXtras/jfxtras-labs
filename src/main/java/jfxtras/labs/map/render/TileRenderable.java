/**
 * TileRenderable.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
	void render(Group tilesGroup);
	
	/**
	 * Reloads all tile which currently visible.
	 * @param mapController
	 */
	void refresh(Moveable mapController);
	
	/**
	 * Turn monochrome mode on/off.
	 * @param val
	 */
	void setMonoChrome(boolean val);
	
	/**
	 * Turn tiles grid on/off.
	 * @param val
	 */
	void setTileGridVisible(boolean val);
}