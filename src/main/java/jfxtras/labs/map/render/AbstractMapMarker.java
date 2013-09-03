/**
 * AbstractMapMarker.java
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
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import jfxtras.labs.map.Coordinate;
import jfxtras.labs.map.Moveable;
import jfxtras.labs.map.MapTilesourceable;

import static jfxtras.labs.map.CoordinatesConverter.*;

/**
 *
 * @author Mario Schroeder
 */
abstract class AbstractMapMarker implements MapMarkable {

    private static final int SHADOW_OFFSET = 10;

    private double lat;

    private double lon;

    AbstractMapMarker() {
    }

    AbstractMapMarker(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public double getLat() {
        return lat;
    }

    @Override
    public double getLon() {
        return lon;
    }

    @Override
    public void render(MapTilesourceable mapController) {
        
        Point postion = getPoint(mapController);
        if(postion != null){
            Group tilesGroup = mapController.getTilesGroup();
            ObservableList<Node> children = tilesGroup.getChildren();
			List<? extends Node> nodes = createChildren(postion);
			for(Node node : nodes){
				if(!children.contains(node)){
					children.add(node);
				}
			}
        }
    }
    
    private Point getPoint(Moveable mapController){
    	Coordinate coordinate = new Coordinate(lat, lon);
    	return toMapPoint(coordinate, mapController);
    }
    
    abstract List<? extends Node> createChildren(Point position);
    
    public void setLat(double val) {
        this.lat = val;
    }

    public void setLon(double val) {
        this.lon = val;
    }

    protected DropShadow createShadow(Color color) {
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(SHADOW_OFFSET);
        dropShadow.setOffsetY(SHADOW_OFFSET);
        dropShadow.setColor(color);
        return dropShadow;
    }

    @Override
    public String toString() {
        return getClass().getName() + " " + getLat() + " " + getLon();
    }
}
