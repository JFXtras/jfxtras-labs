/**
 * EllipticalGradient.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

package jfxtras.labs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Shape;


/**
 * Created by
 * User: hansolo
 * Date: 20.08.12
 * Time: 10:37
 */
public class EllipticalGradient {
    private List<Stop> sortedStops;

    public EllipticalGradient(final Stop... STOPS) {
        this(Arrays.asList(STOPS));
    }

    public EllipticalGradient(final List<Stop> STOPS) {
        List<Stop> stops;
        if (STOPS == null || STOPS.isEmpty()) {
            stops = new ArrayList<Stop>();
            stops.add(new Stop(0.0, Color.TRANSPARENT));
            stops.add(new Stop(1.0, Color.TRANSPARENT));
        } else {
            stops = STOPS;
        }

        HashMap<Double, Color> stopMap = new LinkedHashMap<Double, Color>(stops.size());
        for (Stop stop : stops) {
            stopMap.put(stop.getOffset(), stop.getColor());
        }

        sortedStops = new LinkedList<Stop>();
        final SortedSet<Double> sortedFractions = new TreeSet<Double>(stopMap.keySet());
        if (sortedFractions.last() < 1) {
            stopMap.put(1.0, stopMap.get(sortedFractions.last()));
            sortedFractions.add(1.0);
        }
        if (sortedFractions.first() > 0) {
            stopMap.put(0.0, stopMap.get(sortedFractions.first()));
            sortedFractions.add(0.0);
        }
        for (final Double FRACTION : sortedFractions) {
            sortedStops.add(new Stop(FRACTION, stopMap.get(FRACTION)));
        }
    }

    public List<Stop> getStops() {
        return sortedStops;
    }

    public Image getImage(final double WIDTH, final double HEIGHT) {
        return getImage(WIDTH, HEIGHT, new Point2D(WIDTH / 2.0, HEIGHT / 2.0));
    }

    public Image getImage(final double WIDTH, final double HEIGHT, final Point2D CENTER) {
        int     width   = (int) WIDTH <= 0 ? 100 : (int) WIDTH;
        int     height  = (int) HEIGHT <= 0 ? 50 : (int) HEIGHT;
        double  a       = WIDTH / 2.0;
        double  aSquare = a * a;
        double  b       = HEIGHT / 2.0;
        double  bSquare = b * b;
        Color   color   = Color.TRANSPARENT;
        final WritableImage RASTER       = new WritableImage(width, height);
        final PixelWriter PIXEL_WRITER = RASTER.getPixelWriter();
        double isInside;
        double fraction;
        double currentOffset;
        double nextOffset;
        for (int y = 0 ; y < height ; y++) {
            for (int x = 0 ; x < width ; x++) {
                isInside = ((x - CENTER.getX()) * (x - CENTER.getX())) / aSquare + ((y - CENTER.getY()) * (y - CENTER.getY())) / bSquare;
                isInside = isInside > 1 ? 1 : isInside;
                for (int i = 0; i < (sortedStops.size() - 1); i++) {
                    currentOffset = sortedStops.get(i).getOffset();
                    nextOffset    = sortedStops.get(i + 1).getOffset();
                    if (Double.compare(isInside, currentOffset) > 0 && Double.compare(isInside, nextOffset) <= 0) {
                        fraction = (isInside - currentOffset) / (nextOffset - currentOffset);
                        color    = (Color) Interpolator.LINEAR.interpolate(sortedStops.get(i).getColor(), sortedStops.get(i + 1).getColor(), fraction);
                    }
                }
                PIXEL_WRITER.setColor(x, y, color);
            }
        }
        return RASTER;
    }

    public ImagePattern getFill(final Shape SHAPE) {
        return getFill(SHAPE, new Point2D(SHAPE.getLayoutBounds().getWidth() / 2.0, SHAPE.getLayoutBounds().getHeight() / 2.0));
    }

    public ImagePattern getFill(final Shape SHAPE, final Point2D CENTER) {
        double x      = SHAPE.getLayoutBounds().getMinX();
        double y      = SHAPE.getLayoutBounds().getMinY();
        double width  = SHAPE.getLayoutBounds().getWidth();
        double height = SHAPE.getLayoutBounds().getHeight();
        return new ImagePattern(getImage(width, height, CENTER), x, y, width, height, false);
    }
}
