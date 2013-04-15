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
package jfxtras.labs.map.render;

import java.awt.Point;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import jfxtras.labs.map.Coordinate;
import jfxtras.labs.map.MapControlable;

/**
 *
 * @author smithjel
 */
public class DefaultMapLine implements MapLineable {

    private String name;

    private Color color;

    private double dashOffset;

    private double width;

    private ObservableList<Coordinate> coordinates;

    public DefaultMapLine(List<Coordinate> coordinates) {
        this("", Color.BLACK, 1.0, 0.0, FXCollections.observableList(coordinates));
    }

    public DefaultMapLine(String n, Color c, double width, double dashOffset, List<Coordinate> coords) {
        this.name = n;
        this.color = c;
        this.dashOffset = dashOffset;
        this.width = width;
        this.coordinates = FXCollections.observableList(coords);
    }

    @Override
    public void addCoordinate(Coordinate coordinate) {
        this.coordinates.add(coordinate);
    }

    @Override
    public void render(MapControlable viewer) {
        Polyline polyline = new Polyline();

        polyline.setStrokeType(StrokeType.CENTERED);
        polyline.setStrokeDashOffset(this.dashOffset);
        polyline.setStroke(color);
        polyline.setStrokeWidth(width);
        polyline.setStrokeLineCap(StrokeLineCap.ROUND);
        polyline.setStrokeLineJoin(StrokeLineJoin.ROUND);


        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(10);
        dropShadow.setOffsetY(10);
        dropShadow.setColor(Color.rgb(50, 50, 50, 0.7));
        polyline.setEffect(dropShadow);

        for (Coordinate coordinate : coordinates) {
            Point p = viewer.getMapPoint(coordinate.getLat(), coordinate.getLon(), false);
            polyline.getPoints().add((double) p.x);
            polyline.getPoints().add((double) p.y);
        }
        viewer.getTilesGroup().getChildren().add(polyline);
    }

    @Override
    public String toString() {
        return "MapLine" + name;
    }
}
