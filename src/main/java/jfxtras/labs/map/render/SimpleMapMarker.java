/**
 * SimpleMapMarker.java
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
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.RadialGradientBuilder;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;

/**
 * A simple implementation of the {@link MapMarkable} interface. Each map marker is painted as a circle with a black
 * border line and filled with a specified color.
 *
 * @author Jan Peter Stotz
 *
 */
public class SimpleMapMarker extends AbstractMapMarker {

    private Circle sphere;

    private int radius;

    public SimpleMapMarker(double lat, double lon) {
        this(10, Color.YELLOW, lat, lon);
    }

    public SimpleMapMarker(int radius, Color color, double lat, double lon) {
        super(lat, lon);
        this.radius = radius;

        this.sphere = CircleBuilder.create()
            .centerX(radius)
            .centerY(radius)
            .radius(radius)
            .cache(true)
            .build();

        RadialGradient rgrad = RadialGradientBuilder.create()
            .centerX(sphere.getCenterX() - sphere.getRadius() / 3)
            .centerY(sphere.getCenterY() - sphere.getRadius() / 3)
            .radius(sphere.getRadius())
            .proportional(false)
            .stops(new Stop(0.0, color), new Stop(1.0, Color.BLACK))
            .build();

        sphere.setFill(rgrad);
        sphere.setEffect(createShadow(Color.rgb(50, 50, 50, 0.7)));

    }

    @Override
    List<? extends Node> createChildren(Point position){
        sphere.setTranslateX(position.x - this.radius);
        sphere.setTranslateY(position.y - this.radius);
        return Collections.singletonList(sphere);
    }

    @Override
    public Node getNode() {
        return sphere;
    }
}
