/**
 * Copyright (c) 2011-2021, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *    Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *    Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *    Neither the name of the organization nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL JFXTRAS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package jfxtras.labs.scene.control.gauge;

import java.util.Map;
import java.util.TreeMap;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;


/**
 * Created by
 * User: hansolo
 * Date: 21.03.12
 * Time: 09:14
 */
public class GradientLookup {
    private Map<Double, Stop> stops;

    // ******************** Constructors **************************************
    public GradientLookup(final Stop... STOPS) {
        stops = new TreeMap<Double, Stop>();
        for (Stop stop : STOPS) {
            stops.put(stop.getOffset(), stop);
        }
        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        double minFraction = 1;
        double maxFraction = 0;
        for (Double fraction : stops.keySet()) {
            minFraction = Math.min(fraction, minFraction);
            maxFraction = Math.max(fraction, maxFraction);
        }
        if (minFraction > 0) {
            stops.put(0.0, new Stop(0.0, stops.get(minFraction).getColor()));
        }
        if (maxFraction < 1){
            stops.put(1.0, new Stop(1.0, stops.get(maxFraction).getColor()));
        }
    }


    // ******************** Methods *******************************************
    public Color getColorAt(final double POSITION_OF_COLOR) {
        final double POSITION = POSITION_OF_COLOR < 0 ? 0 : (POSITION_OF_COLOR > 1 ? 1 : POSITION_OF_COLOR);
        final Color COLOR;
        if (stops.size() == 1) {
            final Map<Double, Color> ONE_ENTRY = (Map<Double, Color>) stops.entrySet().iterator().next();
            COLOR = stops.get(ONE_ENTRY.keySet().iterator().next()).getColor();
        } else {
            Stop lowerBound = stops.get(0.0);
            Stop upperBound = stops.get(1.0);
            for (Double fraction : stops.keySet()) {
                if (Double.compare(fraction,POSITION) < 0) {
                    lowerBound = stops.get(fraction);
                }
                if (Double.compare(fraction, POSITION) > 0) {
                    upperBound = stops.get(fraction);
                    break;
                }
            }
            COLOR = interpolateColor(lowerBound, upperBound, POSITION);
        }
        return COLOR;
    }

    private Color interpolateColor(final Stop LOWER_BOUND, final Stop UPPER_BOUND, final double POSITION) {
        final double POS  = (POSITION - LOWER_BOUND.getOffset()) / (UPPER_BOUND.getOffset() - LOWER_BOUND.getOffset());

        final double DELTA_RED     = (UPPER_BOUND.getColor().getRed() - LOWER_BOUND.getColor().getRed()) * POS;
        final double DELTA_GREEN   = (UPPER_BOUND.getColor().getGreen() - LOWER_BOUND.getColor().getGreen()) * POS;
        final double DELTA_BLUE    = (UPPER_BOUND.getColor().getBlue() - LOWER_BOUND.getColor().getBlue()) * POS;
        final double DELTA_OPACITY = (UPPER_BOUND.getColor().getOpacity() - LOWER_BOUND.getColor().getOpacity()) * POS;

        double red     = LOWER_BOUND.getColor().getRed() + DELTA_RED;
        double green   = LOWER_BOUND.getColor().getGreen() + DELTA_GREEN;
        double blue    = LOWER_BOUND.getColor().getBlue() + DELTA_BLUE;
        double opacity = LOWER_BOUND.getColor().getOpacity() + DELTA_OPACITY;

        red     = red < 0 ? 0   : (red > 1 ? 1   : red);
        green   = green < 0 ? 0 : (green > 1 ? 1 : green);
        blue    = blue < 0 ? 0  : (blue > 1 ? 1  : blue);
        opacity = opacity < 0 ? 0 : (opacity > 1 ? 1 : opacity);

        return Color.color(red, green, blue, opacity);
    }
}
