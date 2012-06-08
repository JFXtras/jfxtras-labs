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

import com.sun.org.apache.xpath.internal.functions.FuncLocalPart;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 12.04.12
 * Time: 14:48
 */
public class SplitFlapBuilder {
    private HashMap<String, Property> properties = new HashMap<String, Property>();

    public static final SplitFlapBuilder create() {
        return new SplitFlapBuilder();
    }

    public final SplitFlapBuilder textColor(final Color TEXT_COLOR) {
        properties.put("textColor", new SimpleObjectProperty<Color>(TEXT_COLOR));
        return this;
    }

    public final SplitFlapBuilder color(final Color COLOR) {
        properties.put("color", new SimpleObjectProperty<Color>(COLOR));
        return this;
    }

    public final SplitFlapBuilder text(final String TEXT) {
        properties.put("text", new SimpleStringProperty(TEXT));
        return this;
    }

    public final SplitFlapBuilder selection(final String[] SELECTION) {
        properties.put("selection", new SimpleObjectProperty<String[]>(SELECTION));
        return this;
    }

    public final SplitFlapBuilder soundOn(final boolean SOUND_ON) {
        properties.put("soundOn", new SimpleBooleanProperty(SOUND_ON));
        return this;
    }

    public final SplitFlapBuilder sound(final SplitFlap.Sound SOUND) {
        properties.put("sound", new SimpleObjectProperty<SplitFlap.Sound>(SOUND));
        return this;
    }

    public final SplitFlapBuilder frameVisible(final boolean FRAME_VISIBLE) {
        properties.put("frameVisible", new SimpleBooleanProperty(FRAME_VISIBLE));
        return this;
    }

    public final SplitFlapBuilder backgroundVisible(final boolean BACKGROUND_VISIBLE) {
        properties.put("backgroundVisible", new SimpleBooleanProperty(BACKGROUND_VISIBLE));
        return this;
    }

    public final SplitFlapBuilder interactive(final boolean INTERACTIVE) {
        properties.put("interactive", new SimpleBooleanProperty(INTERACTIVE));
        return this;
    }

    public final SplitFlapBuilder flipTimeInMs(final long FLIP_TIME_IN_MS) {
        properties.put("flipTimeInMs", new SimpleLongProperty(FLIP_TIME_IN_MS));
        return this;
    }

    public final SplitFlapBuilder prefWidth(final double PREF_WIDTH) {
        properties.put("prefWidth", new SimpleDoubleProperty(PREF_WIDTH));
        return this;
    }

    public final SplitFlapBuilder prefHeight(final double PREF_HEIGHT) {
        properties.put("prefHeight", new SimpleDoubleProperty(PREF_HEIGHT));
        return this;
    }

    public final SplitFlap build() {
        final SplitFlap CONTROL = new SplitFlap();
        for (String key : properties.keySet()) {
            if ("textColor".equals(key)) {
                CONTROL.setTextColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("color".equals(key)) {
                CONTROL.setColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("text".equals(key)) {
                CONTROL.setText(((StringProperty) properties.get(key)).get());
            } else if ("selection".equals(key)) {
                CONTROL.setSelection(((SimpleObjectProperty<String[]>) properties.get(key)).get());
            } else if ("soundOn".equals(key)) {
                CONTROL.setSoundOn(((BooleanProperty) properties.get(key)).get());
            } else if ("sound".equals(key)) {
                CONTROL.setSound(((ObjectProperty<SplitFlap.Sound>) properties.get(key)).get());
            } else if ("frameVisible".equals(key)) {
                CONTROL.setFrameVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("backgroundVisible".equals(key)) {
                CONTROL.setBackgroundVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("interactive".equals(key)) {
                CONTROL.setInteractive(((BooleanProperty) properties.get(key)).get());
            } else if ("flipTimeInMs".equals(key)) {
                CONTROL.setFlipTimeInMs(((LongProperty) properties.get(key)).get());
            }  else if ("prefWidth".equals(key)) {
                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
            } else if ("prefHeight".equals(key)) {
                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
