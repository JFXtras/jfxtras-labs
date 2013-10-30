/**
 * Moveable.java
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

package jfxtras.labs.map;

import java.awt.Point;

import javafx.scene.Group;

/**
 * This interface defines methods to move the map.
 *
 * @author Mario Schroeder
 */
public interface Moveable extends Zoomable{

    /**
     * Moves the map to the given coordinates so that x and y is in the center.
     *
     * @param x
     * @param y
     */
    void moveMap(int x, int y);

    /**
     * This method returns the center of the map as a point.<br/>
     * X&Y position of the center of this map on the world
	 * in screen pixels for the current zoom level.
     *
     * @return the center as point
     */
    Point getCenter();

    int getMapHeight();
    
    int getMapWidth();
    
    int getMapX();

    int getMapY();
    
    /**
     * This method should return <code>true</code> if the map can be moved otherwise false.
     * @return
     */
    boolean isMapMoveable();
    
    /**
     * Centers the map: latitude and longitude are zero.
     */
    void centerMap();
    
	Group getTilesGroup();
    
}
