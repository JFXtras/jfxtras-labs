/**
 * DefaultMapLine.java
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import jfxtras.labs.map.Coordinate;
import jfxtras.labs.map.MapTilesourceable;

import static jfxtras.labs.map.CoordinatesConverter.*;

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
    public void render(MapTilesourceable viewer) {
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
            Point p = toMapPoint(coordinate, viewer);
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
