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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 12.03.12
 * Time: 19:14
 */
public class ClockBuilder {
    private HashMap<String, Property> properties = new HashMap<String, Property>();

    public static final ClockBuilder create() {
        return new ClockBuilder();
    }

    public final ClockBuilder timeZone(final String TIME_ZONE) {
        properties.put("timeZone", new SimpleStringProperty(TIME_ZONE));
        return this;
    }

    public final ClockBuilder daylightSavingTime(final boolean DAYLIGHT_SAVING_TIME) {
        properties.put("daylightSavingTime", new SimpleBooleanProperty(DAYLIGHT_SAVING_TIME));
        return this;
    }

    public final ClockBuilder secondPointerVisible(final boolean SECOND_POINTER_VISIBLE) {
        properties.put("secondPointerVisible", new SimpleBooleanProperty(SECOND_POINTER_VISIBLE));
        return this;
    }

    public final ClockBuilder autoDimEnabled(final boolean AUTO_DIM_ENABLED) {
        properties.put("autoDimEnabled", new SimpleBooleanProperty(AUTO_DIM_ENABLED));
        return this;
    }

    public final ClockBuilder running(final boolean RUNNING) {
        properties.put("running", new SimpleBooleanProperty(RUNNING));
        return this;
    }

    public final ClockBuilder clockStyle(final Clock.ClockStyle CLOCK_STYLE) {
        properties.put("clockStyle", new SimpleObjectProperty<Clock.ClockStyle>(CLOCK_STYLE));
        return this;
    }

    public final ClockBuilder backgroundStyle(final Clock.BackgroundStyle BACKGROUND_STYLE) {
        properties.put("backgroundStyle", new SimpleObjectProperty<Clock.BackgroundStyle>(BACKGROUND_STYLE));
        return this;
    }

    public final ClockBuilder backgroundColor(final Color BACKGROUND_COLOR) {
        properties.put("backgroundColor", new SimpleObjectProperty<Color>(BACKGROUND_COLOR));
        return this;
    }

    public final ClockBuilder pointerColor(final Color POINTER_COLOR) {
        properties.put("pointerColor", new SimpleObjectProperty<Color>(POINTER_COLOR));
        return this;
    }

    public final ClockBuilder secondPointerColor(final Color SECOND_POINTER_COLOR) {
        properties.put("secondPointerColor", new SimpleObjectProperty<Color>(SECOND_POINTER_COLOR));
        return this;
    }

    public final ClockBuilder tickMarkColor(final Color TICK_MARK_COLOR) {
        properties.put("tickMarkColor", new SimpleObjectProperty<Color>(TICK_MARK_COLOR));
        return this;
    }

    public final ClockBuilder prefWidth(final double PREF_WIDTH) {
        properties.put("prefWidth", new SimpleDoubleProperty(PREF_WIDTH));
        return this;
    }

    public final ClockBuilder prefHeight(final double PREF_HEIGHT) {
        properties.put("prefHeight", new SimpleDoubleProperty(PREF_HEIGHT));
        return this;
    }

    public final Clock build() {
        final Clock CONTROL = new Clock();
        for (String key : properties.keySet()) {
            if ("timeZone".equals(key)) {
                CONTROL.setTimeZone(((StringProperty) properties.get(key)).get());
            } else if ("daylightSavingTime".equals(key)) {
                CONTROL.setDaylightSavingTime(((BooleanProperty) properties.get(key)).get());
            } else if ("secondPointerVisible".equals(key)) {
                CONTROL.setSecondPointerVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("autoDimEnabled".equals(key)) {
                CONTROL.setAutoDimEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("running".equals(key)) {
                CONTROL.setRunning(((BooleanProperty) properties.get(key)).get());
            } else if ("clockStyle".equals(key)) {
                CONTROL.setClockStyle(((ObjectProperty<Clock.ClockStyle>) properties.get(key)).get());
            } else if ("prefWidth".equals(key)) {
                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
            } else if ("prefHeight".equals(key)) {
                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
            } else if ("backgroundStyle".equals(key)) {
                CONTROL.setBackgroundStyle(((ObjectProperty<Clock.BackgroundStyle>) properties.get(key)).get());
            } else if ("backgroundColor".equals(key)) {
                CONTROL.setBackgroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("pointerColor".equals(key)) {
                CONTROL.setPointerColor(((ObjectProperty<Color>) properties.get(key)).get());;
            } else if ("secondPointerColor".equals(key)) {
                CONTROL.setSecondPointerColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("tickMarkColor".equals(key)) {
                CONTROL.setTickMarkColor(((ObjectProperty<Color>) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
