/*
 * Copyright (c) 2012, JFXtras
 *   All rights reserved.
 *
 *   Redistribution and use in source and binary forms, with or without
 *   modification, are permitted provided that the following conditions are met:
 *       * Redistributions of source code must retain the above copyright
 *         notice, this list of conditions and the following disclaimer.
 *       * Redistributions in binary form must reproduce the above copyright
 *         notice, this list of conditions and the following disclaimer in the
 *         documentation and/or other materials provided with the distribution.
 *       * Neither the name of the <organization> nor the
 *         names of its contributors may be used to endorse or promote products
 *         derived from this software without specific prior written permission.
 *
 *   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *   DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.scene.control.gauge;

import javafx.scene.paint.Color;
import javafx.scene.paint.Stop;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by
 * User: hansolo
 * Date: 21.03.12
 * Time: 09:14
 */
public class GradientLookup {
    private Map<Double, Stop> stops;

    public GradientLookup(final Stop... STOPS) {
        stops = new HashMap<Double, Stop>(STOPS.length);
        for (Stop stop : STOPS) {
            stops.put(stop.getOffset(), stop);
        }
    }

    public Color getColorAt(final double POSITION_OF_COLOR) {
        final double POSITION = POSITION_OF_COLOR < 0 ? 0 : (POSITION_OF_COLOR > 1 ? 1 : POSITION_OF_COLOR);
        final Color COLOR;
        if (stops.size() == 1) {
            final Map<Double, Color> ONE_ENTRY = (Map<Double, Color>) stops.entrySet().iterator().next();
            COLOR = stops.get(ONE_ENTRY.keySet().iterator().next()).getColor();
        } else {
            double minFraction = 0;
            double maxFraction = 1;
            for (Double fraction : stops.keySet()) {
                minFraction = Math.min(fraction, minFraction);
                maxFraction = Math.max(fraction, maxFraction);
            }
            Stop lowerBound = stops.get(minFraction);
            Stop upperBound = stops.get(maxFraction);

            for (Double fraction : stops.keySet()) {
                if (fraction < POSITION && fraction > lowerBound.getOffset()) {
                    lowerBound = stops.get(fraction);
                }
                if (fraction > POSITION && fraction < upperBound.getOffset()) {
                    upperBound = stops.get(fraction);
                }
            }
            COLOR = interpolateColor(lowerBound, upperBound, POSITION);
        }
        return COLOR;
    }

    private Color interpolateColor(final Stop STOP1, final Stop STOP2, final double POSITION) {
        final double POS1 = STOP1.getOffset();
        final double POS2 = STOP2.getOffset();
        final double POS3 = POSITION / (POS2 - POS1);

        final double RED1   = STOP1.getColor().getRed();
        final double GREEN1 = STOP1.getColor().getGreen();
        final double BLUE1  = STOP1.getColor().getBlue();
        final double ALPHA1 = STOP1.getColor().getOpacity();

        final double RED2   = STOP2.getColor().getRed();
        final double GREEN2 = STOP2.getColor().getGreen();
        final double BLUE2  = STOP2.getColor().getBlue();
        final double ALPHA2 = STOP2.getColor().getOpacity();

        final double DELTA_RED   = RED2 - RED1;
        final double DELTA_GREEN = GREEN2 - GREEN1;
        final double DELTA_BLUE  = BLUE2 - BLUE1;
        final double DELTA_ALPHA = ALPHA2 - ALPHA1;

        double red   = RED1 + (DELTA_RED * POS3);
        double green = GREEN1 + (DELTA_GREEN * POS3);
        double blue  = BLUE1 + (DELTA_BLUE * POS3);
        double alpha = ALPHA1 + (DELTA_ALPHA * POS3);

        red   = red < 0 ? 0   : (red > 1 ? 1   : red);
        green = green < 0 ? 0 : (green > 1 ? 1 : green);
        blue  = blue < 0 ? 0  : (blue > 1 ? 1  : blue);
        alpha = alpha < 0 ? 0 : (alpha > 1 ? 1 : alpha);

        return Color.color(red, green, blue, alpha);
    }
}
