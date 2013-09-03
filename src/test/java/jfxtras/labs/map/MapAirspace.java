/**
 * MapAirspace.java
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
import java.util.List;

import javafx.scene.effect.Bloom;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;

import jfxtras.labs.map.render.MapPolygonable;

import static jfxtras.labs.map.CoordinatesConverter.*;

/**
 *
 * @author smithjel
 */
public class MapAirspace implements MapPolygonable {
    
    private final List<Coordinate> coordinates;
    private Color fillColor;
    private Color outlineColor;
    private Bloom bloom;
    private DropShadow dropShadow;
    
    public MapAirspace( List<Coordinate> points ) {
        this(Color.BLACK, Color.YELLOW, points );
    }

    public MapAirspace(Color fcolor, Color ocolor, List<Coordinate> coords ) {
        this.fillColor      = fcolor;
        this.outlineColor   = ocolor;
        this.coordinates    = coords;
        
        bloom = new Bloom();
        bloom.setThreshold(0.0);
        
        dropShadow = new DropShadow();
        dropShadow.setOffsetY(10.0f);
        dropShadow.setOffsetX(10.0f);
        dropShadow.setColor( outlineColor ); 
    }

    @Override
    public List<Coordinate> getCoordinates() {
        return this.coordinates;
    }

    @Override
    public Color getOutlineColor() {
        return outlineColor;
    }

    @Override
    public Color getFillColor() {
        return fillColor;
    }

    @Override
    public void render(MapTilesourceable viewer) {
    	
        Path path = new Path();
        Polyline polyline = new Polyline();
        Coordinate c0 = coordinates.get(0);
        Point p0 = toMapPoint( c0, viewer );
        path.getElements().add( new MoveTo( p0.x, p0.y ) );
        for (Coordinate coordinate : coordinates) {
            Point point = toMapPoint( coordinate , viewer );
            path.getElements().add(new LineTo(point.x, point.y));
            polyline.getPoints().add((double)point.x);
            polyline.getPoints().add((double)point.y);
        }
        path.getElements().add( new LineTo(p0.x, p0.y) );
        polyline.getPoints().add((double)p0.x);
        polyline.getPoints().add((double)p0.y);

        path.setStrokeWidth(1);
        path.setStroke(fillColor);
        path.setEffect(bloom);
        
        viewer.getTilesGroup().getChildren().add(path);
        
        polyline.setFill(outlineColor);
        polyline.setEffect(dropShadow);
        
        viewer.getTilesGroup().getChildren().add(polyline);
    }
    
}
