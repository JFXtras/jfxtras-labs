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

import javafx.geometry.Orientation;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * Created by
 * User: hansolo
 * Date: 09.03.12
 * Time: 16:37
 */
public class LedBargraphBuilder {
    private LedBargraph bargraph;

    public final LedBargraphBuilder create() {
        bargraph = new LedBargraph();
        return this;
    }

    public final LedBargraphBuilder noOfLeds(final int NO_OF_LEDS) {
        bargraph.setNoOfLeds(NO_OF_LEDS);
        return this;
    }

    public final LedBargraphBuilder ledType(final Led.Type LED_TYPE) {
        bargraph.setLedType(LED_TYPE);
        return this;
    }

    public final LedBargraphBuilder orientation(final Orientation ORIENTATION) {
        bargraph.setOrientation(ORIENTATION);
        return this;
    }

    public final LedBargraphBuilder peakValueVisible(final boolean PEAK_VALUE_VISIBLE) {
        bargraph.setPeakValueVisible(PEAK_VALUE_VISIBLE);
        return this;
    }

    public final LedBargraphBuilder ledSize(final double LED_SIZE) {
        bargraph.setLedSize(LED_SIZE);
        return this;
    }

    public final LedBargraphBuilder ledColors(final LinkedList<Color> LED_COLORS) {
        bargraph.setLedColors(LED_COLORS);
        return this;
    }

    public final LedBargraphBuilder ledColor(final int INDEX, final Color COLOR) {
        bargraph.setLedColor(INDEX, COLOR);
        return this;
    }

    public final LedBargraphBuilder value(final double VALUE) {
        bargraph.setValue(VALUE);
        return this;
    }

    public final LedBargraph build() {
        return bargraph != null ? bargraph : new LedBargraph();
    }
}
