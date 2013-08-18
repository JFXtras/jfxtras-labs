//==============================================================================
//   map is a Java library for parsing raw weather data
//   Copyright (C) 2012 Jeffrey L Smith
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the GNU Lesser General Public
//  License as published by the Free Software Foundation; either
//  version 2.1 of the License, or (at your option) any later version.
//    
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//  Lesser General Public License for more details.
//    
//  You should have received a copy of the GNU Lesser General Public
//  License along with this library; if not, write to the Free Software
//  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//    
//  For more information, please email jsmith.carlsbad@gmail.com
//    
//==============================================================================
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
