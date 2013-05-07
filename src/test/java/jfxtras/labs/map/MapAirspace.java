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
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;

import jfxtras.labs.map.render.MapPolygonable;

/**
 *
 * @author smithjel
 */
public class MapAirspace implements MapPolygonable {
    
    private final List<Coordinate> coordinates;
    private Color fillColor;
    private Color outlineColor;
    
    public MapAirspace( List<Coordinate> points ) {
        this(Color.BLACK, Color.YELLOW, points );
    }

    public MapAirspace(Color fcolor, Color ocolor, List<Coordinate> coords ) {
        super();
        this.fillColor      = fcolor;
        this.outlineColor   = ocolor;
        this.coordinates    = coords;
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
    public void render(MapControlable viewer) {
        int nPoints = coordinates.size();
        Path path = new Path();
        Polyline polyline = new Polyline();
        Coordinate c0 = coordinates.get(0);
        Point p0 = viewer.getMapPoint( c0 );
        path.getElements().add( new MoveTo( p0.x, p0.y ) );
        for (int p = 0; p < nPoints; p++ ) {
            Coordinate coordinate = coordinates.get(p);
            Point point = viewer.getMapPoint( coordinate );
            path.getElements().add(new LineTo(point.x, point.y));
            polyline.getPoints().add((double)point.x);
            polyline.getPoints().add((double)point.y);
        }
        path.getElements().add( new LineTo(p0.x, p0.y) );
        
        polyline.getPoints().add((double)p0.x);
        polyline.getPoints().add((double)p0.y);

        path.setStrokeWidth(1);
        path.setStroke( fillColor );

        Bloom bloom = new Bloom();
        bloom.setThreshold(0.0);
        
        InnerShadow is = new InnerShadow();
        is.setOffsetX(0.0f);
        is.setOffsetY(0.0f);
        is.setWidth(30.0);
        is.setColor(outlineColor);
        path.setEffect(bloom);
        
        viewer.getTilesGroup().getChildren().add(path);
        
        polyline.setFill( outlineColor );
        

        DropShadow ds = new DropShadow();
        ds.setOffsetY(10.0f);
        ds.setOffsetX(10.0f);
        ds.setColor( outlineColor ); // Color.color(0.4f, 0.4f, 0.4f));
        polyline.setEffect(ds);
        
        viewer.getTilesGroup().getChildren().add(polyline);
    }
    
}
