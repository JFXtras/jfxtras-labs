/*
 * Copyright (c) 2012, JFXtras
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the <organization> nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.util;

import javafx.animation.Interpolator;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * Created by
 * User: hansolo
 * Date: 10.07.12
 * Time: 09:46
 */
public class ConicalGradient {
    private Point2D    center;
    private List<Stop> sortedStops;


    // ******************** Constructors **************************************
    public ConicalGradient(final Stop... STOPS) {
        this(null, Arrays.asList(STOPS));
    }

    public ConicalGradient(final List<Stop> STOPS) {
        this(null, STOPS);
    }

    public ConicalGradient(final Point2D CENTER, final Stop... STOPS) {
        this(CENTER, 0.0, Arrays.asList(STOPS));
    }

    public ConicalGradient(final Point2D CENTER, final List<Stop> STOPS) {
        this(CENTER, 0.0, STOPS);
    }

    public ConicalGradient(final Point2D CENTER, final double OFFSET, final Stop... STOPS) {
        this(CENTER, OFFSET, Arrays.asList(STOPS));
    }

    public ConicalGradient(final Point2D CENTER, final double OFFSET, final List<Stop> STOPS) {
        double offset = Util.clamp(0, 1, OFFSET);
        center = CENTER;
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
            stopMap.put(1.0, stopMap.get(sortedFractions.first()));
            sortedFractions.add(1.0);
        }
        if (sortedFractions.first() > 0) {
            stopMap.put(0.0, stopMap.get(sortedFractions.last()));
            sortedFractions.add(0.0);
        }
        for (final Double FRACTION : sortedFractions) {
            sortedStops.add(new Stop(FRACTION, stopMap.get(FRACTION)));
        }
        if (offset > 0) {
            recalculate(offset);
        }
    }


    // ******************** Methods *******************************************
    private void recalculate(double offset) {
        offset = Util.clamp(0, 1, offset);
        List<Stop> stops = new ArrayList<Stop>(sortedStops.size());
        Stop lastStop = null;
        for (Stop stop : sortedStops) {
            double newOffset = stop.getOffset() + offset;
            if (newOffset > 1) {
                newOffset -= 1.000001;
                if (lastStop == null) {
                    continue;
                }
                stops.add(new Stop(0.0, (Color) Interpolator.LINEAR.interpolate(lastStop.getColor(), stop.getColor(), (1.0 - offset))));
                stops.add(new Stop(1.0, (Color) Interpolator.LINEAR.interpolate(lastStop.getColor(), stop.getColor(), (1.0 - offset))));
            }
            stops.add(new Stop(newOffset, stop.getColor()));
            lastStop = stop;
        }

        HashMap<Double, Color> stopMap = new LinkedHashMap<Double, Color>(stops.size());
        for (Stop stop : stops) {
            stopMap.put(stop.getOffset(), stop.getColor());
        }

        List<Stop> sortedStops2 = new LinkedList<Stop>();
        SortedSet<Double> sortedFractions = new TreeSet<Double>(stopMap.keySet());
        if (sortedFractions.last() < 1) {
            stopMap.put(1.0, stopMap.get(sortedFractions.first()));
            sortedFractions.add(1.0);
        }
        if (sortedFractions.first() > 0) {
            stopMap.put(0.0, stopMap.get(sortedFractions.last()));
            sortedFractions.add(0.0);
        }
        for (Double fraction : sortedFractions) {
            sortedStops2.add(new Stop(fraction, stopMap.get(fraction)));
        }
        sortedStops.clear();
        sortedStops.addAll(sortedStops2);
    }

    public List<Stop> getStops() {
        return sortedStops;
    }

    public Point2D getCenter() {
        return center;
    }

    public Image getImage(final double WIDTH, final double HEIGHT) {
        int width = (int) WIDTH <= 0 ? 100 : (int) WIDTH;
        int height = (int) HEIGHT <= 0 ? 100 : (int) HEIGHT;
        final WritableImage RASTER = new WritableImage(width, height);
        final PixelWriter PIXEL_WRITER = RASTER.getPixelWriter();
        Color color = Color.TRANSPARENT;
        if (center == null) {
            center = new Point2D(width / 2, height / 2);
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dx = x - center.getX();
                double dy = y - center.getY();
                double distance = Math.sqrt((dx * dx) + (dy * dy));
                distance = Double.compare(distance, 0) == 0 ? 1 : distance;

                double angle = Math.abs(Math.toDegrees(Math.acos(dx / distance)));
                if (dx >= 0 && dy <= 0) {
                    angle = 90.0 - angle;
                } else if (dx >= 0 && dy >= 0) {
                    angle += 90.0;
                } else if (dx <= 0 && dy >= 0) {
                    angle += 90.0;
                } else if (dx <= 0 && dy <= 0) {
                    angle = 450.0 - angle;
                }
                for (int i = 0; i < (sortedStops.size() - 1); i++) {
                    if (angle >= (sortedStops.get(i).getOffset() * 360) && angle < (sortedStops.get(i + 1).getOffset() * 360)) {
                        double fraction = (angle - sortedStops.get(i).getOffset() * 360) / ((sortedStops.get(i + 1).getOffset() - sortedStops.get(i).getOffset()) * 360);
                        color = (Color) Interpolator.LINEAR.interpolate(sortedStops.get(i).getColor(), sortedStops.get(i + 1).getColor(), fraction);
                    }
                }
                PIXEL_WRITER.setColor(x, y, color);
            }
        }
        return RASTER;
    }

    public ImagePattern apply(final Shape SHAPE) {
		double x      = SHAPE.getLayoutBounds().getMinX();
		double y      = SHAPE.getLayoutBounds().getMinY();
		double width  = SHAPE.getLayoutBounds().getWidth();
		double height = SHAPE.getLayoutBounds().getHeight();
		center        = new Point2D(width / 2.0, height / 2.0);
		return new ImagePattern(getImage(width, height), x, y, width, height, false);
    }
}
