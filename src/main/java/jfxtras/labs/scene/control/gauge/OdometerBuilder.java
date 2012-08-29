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

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ControlBuilder;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 12.04.12
 * Time: 15:38
 */
public class OdometerBuilder<B extends OdometerBuilder<B>> extends ControlBuilder<B> implements Builder<Odometer> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();


    // ******************** Constructors **************************************
    protected OdometerBuilder() {};


    // ******************** Methods *******************************************
    public static final OdometerBuilder create() {
        return new OdometerBuilder();
    }

    public final OdometerBuilder rotationPreset(final int ROTATION_PRESET) {
        properties.put("rotationPreset", new SimpleIntegerProperty(ROTATION_PRESET));
        return this;
    }

    public final OdometerBuilder color(final Color COLOR) {
        properties.put("color", new SimpleObjectProperty<Color>(COLOR));
        return this;
    }

    public final OdometerBuilder decimalColor(final Color DECIMAL_COLOR) {
        properties.put("decimalColor", new SimpleObjectProperty<Color>(DECIMAL_COLOR));
        return this;
    }

    public final OdometerBuilder numberColor(final Color NUMBER_COLOR) {
        properties.put("numberColor", new SimpleObjectProperty<Color>(NUMBER_COLOR));
        return this;
    }

    public final OdometerBuilder noOfDecimals(final int NO_OF_DECIMALS) {
        properties.put("noOfDecimals", new SimpleIntegerProperty(NO_OF_DECIMALS));
        return this;
    }

    public final OdometerBuilder noOfDigits(final int NO_OF_DIGITS) {
        properties.put("noOfDigits", new SimpleIntegerProperty(NO_OF_DIGITS));
        return this;
    }

    public final OdometerBuilder interval(final long INTERVAL) {
        properties.put("interval", new SimpleLongProperty(INTERVAL));
        return this;
    }

    @Override public final B prefWidth(final double PREF_WIDTH) {
        properties.put("prefWidth", new SimpleDoubleProperty(PREF_WIDTH));
        return (B)this;
    }

    @Override public final B prefHeight(final double PREF_HEIGHT) {
        properties.put("prefHeight", new SimpleDoubleProperty(PREF_HEIGHT));
        return (B)this;
    }

    @Override public final B layoutX(final double LAYOUT_X) {
            properties.put("layoutX", new SimpleDoubleProperty(LAYOUT_X));
            return (B)this;
        }

    @Override public final B layoutY(final double LAYOUT_Y) {
        properties.put("layoutY", new SimpleDoubleProperty(LAYOUT_Y));
        return (B)this;
    }

    @Override public final Odometer build() {
        final Odometer CONTROL = new Odometer();
        for (String key : properties.keySet()) {
            if ("rotationPreset".equals(key)) {
                CONTROL.setRotationPreset(((IntegerProperty) properties.get(key)).get());
            } else if ("color".equals(key)) {
                CONTROL.setColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("decimalColor".equals(key)) {
                CONTROL.setDecimalColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("numberColor".equals(key)) {
                CONTROL.setNumberColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("noOfDecimals".equals(key)) {
                CONTROL.setNoOfDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("noOfDigits".equals(key)) {
                CONTROL.setNoOfDigits(((IntegerProperty) properties.get(key)).get());
            } else if ("interval".equals(key)) {
                CONTROL.setInterval(((LongProperty) properties.get(key)).get());
            }  else if ("prefWidth".equals(key)) {
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
