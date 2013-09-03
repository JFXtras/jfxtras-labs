/**
 * ImageMapMarker.java
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
import java.util.Collections;
import java.util.List;

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
