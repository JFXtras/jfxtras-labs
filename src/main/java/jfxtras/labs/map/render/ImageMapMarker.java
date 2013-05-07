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
import java.util.Collections;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light.Distant;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;

/**
 * Paint an image on the map.
 *
 * @author smithjel
 */
public class ImageMapMarker extends AbstractMapMarker{

    private double rotate;

    private ImageView imageView;
    
    public ImageMapMarker(Image img, double lat, double lon){
        this(img, lat, lon, 0);
    }

    public ImageMapMarker(Image img, double lat, double lon, double rotate) {
        super(lat, lon);
        this.rotate = rotate;
        this.imageView = new ImageView(img);

        Distant light = new Distant();
        light.setAzimuth(-135.0f);

        Lighting lighting = new Lighting();
        lighting.setLight(light);
        lighting.setSurfaceScale(5.0f);

        Color color = Color.rgb(0, 0, 0, 0.7);
        DropShadow dropShadow = createShadow(color);
        dropShadow.setInput(lighting);
        imageView.setEffect(dropShadow);
    }

    public double getRotate() {
        return rotate;
    }

    public void setRotate(double val) {
        this.rotate = val;
    }

    @Override
    List<? extends Node> createChildren(Point position) {
        int size_w = (int) imageView.getImage().getWidth();
        int size_h = (int) imageView.getImage().getHeight();

        imageView.setTranslateX(position.x - (size_w / 2));
        imageView.setTranslateY(position.y - (size_h / 2));
        imageView.setRotate(rotate);

        return Collections.singletonList(imageView);
    }

    @Override
    public Node getNode() {
        return imageView;
    }

}
