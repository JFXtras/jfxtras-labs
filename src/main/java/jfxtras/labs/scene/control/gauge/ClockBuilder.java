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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ControlBuilder;
import javafx.scene.paint.Paint;
import javafx.util.Builder;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 12.03.12
 * Time: 19:14
 */
public class ClockBuilder { //<B extends ClockBuilder<B>> extends ControlBuilder<B> implements Builder<Clock> {
//    private HashMap<String, Property> properties = new HashMap<String, Property>();
//
//
//    // ******************** Constructors **************************************
//    protected ClockBuilder() {};
//
//
//    // ******************** Methods *******************************************
//    public static final ClockBuilder create() {
//        return new ClockBuilder();
//    }
//
//    public final ClockBuilder timeZone(final String TIME_ZONE) {
//        properties.put("timeZone", new SimpleStringProperty(TIME_ZONE));
//        return this;
//    }
//
//    public final ClockBuilder daylightSavingTime(final boolean DAYLIGHT_SAVING_TIME) {
//        properties.put("daylightSavingTime", new SimpleBooleanProperty(DAYLIGHT_SAVING_TIME));
//        return this;
//    }
//
//    public final ClockBuilder secondPointerVisible(final boolean SECOND_POINTER_VISIBLE) {
//        properties.put("secondPointerVisible", new SimpleBooleanProperty(SECOND_POINTER_VISIBLE));
//        return this;
//    }
//
//    public final ClockBuilder autoDimEnabled(final boolean AUTO_DIM_ENABLED) {
//        properties.put("autoDimEnabled", new SimpleBooleanProperty(AUTO_DIM_ENABLED));
//        return this;
//    }
//
//    public final ClockBuilder running(final boolean RUNNING) {
//        properties.put("running", new SimpleBooleanProperty(RUNNING));
//        return this;
//    }
//
//    public final ClockBuilder clockStyle(final Clock.ClockStyle CLOCK_STYLE) {
//        properties.put("clockStyle", new SimpleObjectProperty<Clock.ClockStyle>(CLOCK_STYLE));
//        return this;
//    }
//
//    public final ClockBuilder theme(final Clock.Theme Theme) {
//        properties.put("theme", new SimpleObjectProperty<Clock.Theme>(Theme));
//        return this;
//    }
//
//    public final ClockBuilder brightBackgroundPaint(final Paint BRIGHT_BACKGROUND_PAINT) {
//        properties.put("brightBackgroundPaint", new SimpleObjectProperty<Paint>(BRIGHT_BACKGROUND_PAINT));
//        return this;
//    }
//
//    public final ClockBuilder darkBackgroundPaint(final Paint DARK_BACKGROUND_PAINT) {
//        properties.put("darkBackgroundPaint", new SimpleObjectProperty<Paint>(DARK_BACKGROUND_PAINT));
//        return this;
//    }
//
//    public final ClockBuilder brightPointerPaint(final Paint BRIGHT_POINTER_PAINT) {
//        properties.put("brightPointerPaint", new SimpleObjectProperty<Paint>(BRIGHT_POINTER_PAINT));
//        return this;
//    }
//
//    public final ClockBuilder darkPointerPaint(final Paint DARK_POINTER_PAINT) {
//        properties.put("darkPointerPaint", new SimpleObjectProperty<Paint>(DARK_POINTER_PAINT));
//        return this;
//    }
//
//    public final ClockBuilder brightTickMarkPaint(final Paint BRIGHT_TICK_MARK_PAINT) {
//        properties.put("brightTickMarkPaint", new SimpleObjectProperty<Paint>(BRIGHT_TICK_MARK_PAINT));
//        return this;
//    }
//
//    public final ClockBuilder darkTickMarkPaint(final Paint DARK_TICK_MARK_PAINT) {
//        properties.put("darkTickMarkPaint", new SimpleObjectProperty<Paint>(DARK_TICK_MARK_PAINT));
//        return this;
//    }
//
//    public final ClockBuilder secondPointerPaint(final Paint SECOND_POINTER_PAINT) {
//        properties.put("secondPointerPaint", new SimpleObjectProperty<Paint>(SECOND_POINTER_PAINT));
//        return this;
//    }
//
//    public final ClockBuilder title(final String TITLE) {
//        properties.put("title", new SimpleStringProperty(TITLE));
//        return this;
//    }
//
//    public final ClockBuilder hour(final int HOUR) {
//        properties.put("hour", new SimpleIntegerProperty(HOUR));
//        return this;
//    }
//
//    public final ClockBuilder minute(final int MINUTE) {
//        properties.put("minute", new SimpleIntegerProperty(MINUTE));
//        return this;
//    }
//
//    public final ClockBuilder second(final int SECOND) {
//        properties.put("second", new SimpleIntegerProperty(SECOND));
//        return this;
//    }
//
//    @Override public final B prefWidth(final double PREF_WIDTH) {
//        properties.put("prefWidth", new SimpleDoubleProperty(PREF_WIDTH));
//        return (B)this;
//    }
//
//    @Override public final B prefHeight(final double PREF_HEIGHT) {
//        properties.put("prefHeight", new SimpleDoubleProperty(PREF_HEIGHT));
//        return (B)this;
//    }
//
//    @Override public final B layoutX(final double LAYOUT_X) {
//            properties.put("layoutX", new SimpleDoubleProperty(LAYOUT_X));
//            return (B)this;
//        }
//
//    @Override public final B layoutY(final double LAYOUT_Y) {
//        properties.put("layoutY", new SimpleDoubleProperty(LAYOUT_Y));
//        return (B)this;
//    }
//
//    @Override public final Clock build() {
//        final Clock CONTROL = new Clock();
//        for (String key : properties.keySet()) {
//            if ("timeZone".equals(key)) {
//                CONTROL.setTimeZone(((StringProperty) properties.get(key)).get());
//            } else if ("daylightSavingTime".equals(key)) {
//                CONTROL.setDaylightSavingTime(((BooleanProperty) properties.get(key)).get());
//            } else if ("secondPointerVisible".equals(key)) {
//                CONTROL.setSecondPointerVisible(((BooleanProperty) properties.get(key)).get());
//            } else if ("autoDimEnabled".equals(key)) {
//                CONTROL.setAutoDimEnabled(((BooleanProperty) properties.get(key)).get());
//            } else if ("running".equals(key)) {
//                CONTROL.setRunning(((BooleanProperty) properties.get(key)).get());
//            } else if ("clockStyle".equals(key)) {
//                CONTROL.setClockStyle(((ObjectProperty<Clock.ClockStyle>) properties.get(key)).get());
//            } else if ("prefWidth".equals(key)) {
//                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
//            } else if ("prefHeight".equals(key)) {
//                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
//            } else if ("theme".equals(key)) {
//                CONTROL.setTheme(((ObjectProperty<Clock.Theme>) properties.get(key)).get());
//            } else if ("brightBackgroundPaint".equals(key)) {
//                CONTROL.setBrightBackgroundPaint(((ObjectProperty<Paint>) properties.get(key)).get());
//            } else if ("darkBackgroundPaint".equals(key)) {
//                CONTROL.setDarkBackgroundPaint(((ObjectProperty<Paint>) properties.get(key)).get());
//            } else if ("brightPointerPaint".equals(key)) {
//                CONTROL.setBrightPointerPaint(((ObjectProperty<Paint>) properties.get(key)).get());;
//            } else if ("darkPointerPaint".equals(key)) {
//                CONTROL.setDarkPointerPaint(((ObjectProperty<Paint>) properties.get(key)).get());
//            } else if ("brightTickMarkPaint".equals(key)) {
//                CONTROL.setBrightTickMarkPaint(((ObjectProperty<Paint>) properties.get(key)).get());
//            } else if ("darkTickMarkPaint".equals(key)) {
//                CONTROL.setDarkTickMarkPaint(((ObjectProperty<Paint>) properties.get(key)).get());
//            } else if ("secondPointerPaint".equals(key)) {
//                CONTROL.setSecondPointerPaint(((ObjectProperty<Paint>) properties.get(key)).get());
//            } else if ("title".equals(key)) {
//                CONTROL.setTitle(((StringProperty) properties.get(key)).get());
//            } else if ("hour".equals(key)) {
//                CONTROL.setHour(((IntegerProperty) properties.get(key)).get());
//            } else if ("minute".equals(key)) {
//                CONTROL.setMinute(((IntegerProperty) properties.get(key)).get());
//            } else if ("second".equals(key)) {
//                CONTROL.setSecond(((IntegerProperty) properties.get(key)).get());
//            } else if ("layoutX".equals(key)) {
//                CONTROL.setLayoutX(((DoubleProperty) properties.get(key)).get());
//            } else if ("layoutY".equals(key)) {
//                CONTROL.setLayoutY(((DoubleProperty) properties.get(key)).get());
//            }
//        }
//        return CONTROL;
//    }
}
