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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.LinkedList;


/**
 * Created by
 * User: hansolo
 * Date: 09.03.12
 * Time: 16:37
 */
public class LedBargraphBuilder {
    private HashMap<String, Property> properties = new HashMap<String, Property>();

    public static final LedBargraphBuilder create() {
        return new LedBargraphBuilder();
    }

    public final LedBargraphBuilder noOfLeds(final int NO_OF_LEDS) {
        properties.put("noOfLeds", new SimpleIntegerProperty(NO_OF_LEDS));
        return this;
    }

    public final LedBargraphBuilder ledType(final Led.Type LED_TYPE) {
        properties.put("ledType", new SimpleObjectProperty<Led.Type>(LED_TYPE));
        return this;
    }

    public final LedBargraphBuilder orientation(final Orientation ORIENTATION) {
        properties.put("orientation", new SimpleObjectProperty<Orientation>(ORIENTATION));
        return this;
    }

    public final LedBargraphBuilder peakValueVisible(final boolean PEAK_VALUE_VISIBLE) {
        properties.put("peakValueVisible", new SimpleBooleanProperty(PEAK_VALUE_VISIBLE));
        return this;
    }

    public final LedBargraphBuilder ledSize(final double LED_SIZE) {
        properties.put("ledSize", new SimpleDoubleProperty(LED_SIZE));
        return this;
    }

    public final LedBargraphBuilder ledColors(final LinkedList<Color> LED_COLORS) {
        properties.put("ledColors", new SimpleObjectProperty<LinkedList<Color>>(LED_COLORS));
        return this;
    }

    public final LedBargraphBuilder ledColor(final int INDEX, final Color COLOR) {
        properties.put("ledColorIndex", new SimpleIntegerProperty(INDEX));
        properties.put("ledColor", new SimpleObjectProperty<Color>(COLOR));
        return this;
    }

    public final LedBargraphBuilder value(final double VALUE) {
        properties.put("value", new SimpleDoubleProperty(VALUE));
        return this;
    }

    public final LedBargraphBuilder prefWidth(final double PREF_WIDTH) {
        properties.put("prefWidth", new SimpleDoubleProperty(PREF_WIDTH));
        return this;
    }

    public final LedBargraphBuilder prefHeight(final double PREF_HEIGHT) {
        properties.put("prefHeight", new SimpleDoubleProperty(PREF_HEIGHT));
        return this;
    }

    public final LedBargraph build() {
        final LedBargraph CONTROL = new LedBargraph();
        for (String key : properties.keySet()) {
            if ("noOfLeds".equals(key)) {
                CONTROL.setNoOfLeds(((IntegerProperty) properties.get(key)).get());
            } else if ("ledType".equals(key)) {
                CONTROL.setLedType(((ObjectProperty<Led.Type>) properties.get(key)).get());
            } else if ("orientation".equals(key)) {
                CONTROL.setOrientation(((ObjectProperty<Orientation>) properties.get(key)).get());
            } else if ("peakValueVisible".equals(key)) {
                CONTROL.setPeakValueVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("ledSize".equals(key)) {
                CONTROL.setLedSize(((DoubleProperty) properties.get(key)).get());
            } else if ("ledColors".equals(key)) {
                CONTROL.setLedColors(((ObjectProperty<LinkedList<Color>>) properties.get(key)).get());
            } else if ("ledColor".equals(key)) {
                CONTROL.setLedColor(((IntegerProperty) properties.get("ledColorIndex")).get(), ((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("value".equals(key)) {
                CONTROL.setValue(((DoubleProperty) properties.get(key)).get());
            }  else if ("prefWidth".equals(key)) {
                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
            } else if ("prefHeight".equals(key)) {
                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
