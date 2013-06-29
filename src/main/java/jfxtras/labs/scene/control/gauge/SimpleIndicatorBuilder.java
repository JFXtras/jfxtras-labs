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
import javafx.scene.paint.Color;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 09.03.12
 * Time: 16:04
 */
public class SimpleIndicatorBuilder <B extends SimpleIndicatorBuilder<B>> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();


    // ******************** Constructors **************************************
    protected SimpleIndicatorBuilder() {};


    // ******************** Methods *******************************************
    public static final SimpleIndicatorBuilder create() {
        return new SimpleIndicatorBuilder();
    }

    public final SimpleIndicatorBuilder innerColor(final Color INNER_COLOR) {
        properties.put("innerColor", new SimpleObjectProperty<Color>(INNER_COLOR));
        return this;
    }

    public final SimpleIndicatorBuilder outerColor(final Color OUTER_COLOR) {
        properties.put("outerColor", new SimpleObjectProperty<Color>(OUTER_COLOR));
        return this;
    }

    public final SimpleIndicatorBuilder glowVisible(final boolean GLOW_VISIBLE) {
        properties.put("glowVisible", new SimpleBooleanProperty(GLOW_VISIBLE));
        return this;
    }

    public final B prefWidth(final double PREF_WIDTH) {
        properties.put("prefWidth", new SimpleDoubleProperty(PREF_WIDTH));
        return (B)this;
    }

    public final B prefHeight(final double PREF_HEIGHT) {
        properties.put("prefHeight", new SimpleDoubleProperty(PREF_HEIGHT));
        return (B)this;
    }

    public final B layoutX(final double LAYOUT_X) {
            properties.put("layoutX", new SimpleDoubleProperty(LAYOUT_X));
            return (B)this;
        }

    public final B layoutY(final double LAYOUT_Y) {
        properties.put("layoutY", new SimpleDoubleProperty(LAYOUT_Y));
        return (B)this;
    }

    public final SimpleIndicator build() {
        final SimpleIndicator CONTROL = new SimpleIndicator();
        for (String key : properties.keySet()) {
            if ("innerColor".equals(key)) {
                CONTROL.setInnerColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("outerColor".equals(key)) {
                CONTROL.setOuterColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("glowVisible".equals(key)) {
                CONTROL.setGlowVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("prefWidth".equals(key)) {
                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
            } else if ("prefHeight".equals(key)) {
                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
            } else if ("layoutX".equals(key)) {
                CONTROL.setLayoutX(((DoubleProperty) properties.get(key)).get());
            } else if ("layoutY".equals(key)) {
                CONTROL.setLayoutY(((DoubleProperty) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
