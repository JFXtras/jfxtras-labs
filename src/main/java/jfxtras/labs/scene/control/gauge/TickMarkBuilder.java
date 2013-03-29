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
import javafx.scene.text.Font;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 31.07.12
 * Time: 06:43
 */
public class TickMarkBuilder <B extends TickMarkBuilder<B>> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();


    // ******************** Constructors **************************************
    protected TickMarkBuilder() {};


    // ******************** Methods *******************************************
    public static final TickMarkBuilder create() {
            return new TickMarkBuilder();
        }

    public final TickMarkBuilder type(final TickMark.Type TYPE) {
        properties.put("type", new SimpleObjectProperty<TickMark.Type>(TYPE));
        return this;
    }

    public final TickMarkBuilder indicator(final TickMark.Indicator INDICATOR) {
        properties.put("indicator", new SimpleObjectProperty<TickMark.Indicator>(INDICATOR));
        return this;
    }

    public final TickMarkBuilder indicatorColor(final Color INDICATOR_COLOR) {
        properties.put("indicatorColor", new SimpleObjectProperty<Color>(INDICATOR_COLOR));
        return this;
    }

    public final TickMarkBuilder indicatorVisible(final boolean INDICATOR_VISIBLE) {
        properties.put("indicatorVisible", new SimpleBooleanProperty(INDICATOR_VISIBLE));
        return this;
    }

    public final TickMarkBuilder label(final String LABEL) {
        properties.put("label", new SimpleStringProperty(LABEL));
        return this;
    }

    public final TickMarkBuilder labelColor(final Color LABEL_COLOR) {
        properties.put("labelColor", new SimpleObjectProperty<Color>(LABEL_COLOR));
        return this;
    }

    public final TickMarkBuilder labelVisible(final boolean LABEL_VISIBLE) {
        properties.put("labelVisible", new SimpleBooleanProperty(LABEL_VISIBLE));
        return this;
    }

    public final TickMarkBuilder labelFont(final Font LABEL_FONT) {
        properties.put("labelFont", new SimpleObjectProperty<Font>(LABEL_FONT));
        return this;
    }

    public final TickMarkBuilder labelFontSizeFactor(final double LABEL_FONT_SIZE_FACTOR) {
        properties.put("labelFontSizeFactor", new SimpleDoubleProperty(LABEL_FONT_SIZE_FACTOR));
        return this;
    }

    public final TickMarkBuilder tickLabelOrientation(final TickMark.TickLabelOrientation TICK_LABEL_ORIENTATION) {
        properties.put("tickLabelOrientation", new SimpleObjectProperty<TickMark.TickLabelOrientation>(TICK_LABEL_ORIENTATION));
        return this;
    }

    public TickMark build() {
        final TickMark CONTROL = new TickMark();
        for (String key : properties.keySet()) {
            if ("type".equals(key)) {
                CONTROL.setType(((ObjectProperty<TickMark.Type>) properties.get(key)).get());
            } else if ("indicator".equals(key)) {
                CONTROL.setIndicator(((ObjectProperty<TickMark.Indicator>) properties.get(key)).get());
            } else if ("indicatorColor".equals(key)) {
                CONTROL.setIndicatorColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("indicatorVisible".equals(key)) {
                CONTROL.setIndicatorVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("label".equals(key)) {
                CONTROL.setLabel(((StringProperty) properties.get(key)).get());
            } else if ("labelColor".equals(key)) {
                CONTROL.setLabelColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("labelVisible".equals(key)) {
                CONTROL.setLabelVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("labelFont".equals(key)) {
                CONTROL.setLabelFont(((ObjectProperty<Font>) properties.get(key)).get());
            } else if ("labelFontSizeFactor".equals(key)) {
                CONTROL.setLabelFontSizeFactor(((DoubleProperty) properties.get(key)).get());
            } else if ("tickLabelOrientation".equals(key)) {
                CONTROL.setTickLabelOrientation(((ObjectProperty<TickMark.TickLabelOrientation>) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
