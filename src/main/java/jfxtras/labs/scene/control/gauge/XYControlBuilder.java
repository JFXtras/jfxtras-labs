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
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.ControlBuilder;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 15.05.12
 * Time: 08:59
 */
public class XYControlBuilder <B extends XYControlBuilder<B>> extends ControlBuilder<B> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();


    // ******************** Constructors **************************************
    protected XYControlBuilder() {};


    // ******************** Methods *******************************************
    public static final XYControlBuilder create() {
        return new XYControlBuilder();
    }

    public final XYControlBuilder xValue(final double X_VALUE) {
        properties.put("xValue", new SimpleDoubleProperty(X_VALUE));
        return this;
    }

    public final XYControlBuilder yValue(final double Y_VALUE) {
        properties.put("yValue", new SimpleDoubleProperty(Y_VALUE));
        return this;
    }

    public final XYControlBuilder xAxisLabel(final String X_AXIS_LABEL) {
        properties.put("xAxisLabel", new SimpleStringProperty(X_AXIS_LABEL));
        return this;
    }

    public final XYControlBuilder yAxisLabel(final String Y_AXIS_LABEL) {
        properties.put("yAxisLabel", new SimpleStringProperty(Y_AXIS_LABEL));
        return this;
    }

    public final XYControlBuilder xAxisLabelVisible(final boolean X_AXIS_LABEL_VISIBLE) {
        properties.put("xAxisLabelVisible", new SimpleBooleanProperty(X_AXIS_LABEL_VISIBLE));
        return this;
    }

    public final XYControlBuilder yAxisLabelVisible(final boolean Y_AXIS_LABEL_VISIBLE) {
        properties.put("yAxisLabelVisible", new SimpleBooleanProperty(Y_AXIS_LABEL_VISIBLE));
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

    @Override public final XYControl build() {
        final XYControl CONTROL = new XYControl();
        for (String key : properties.keySet()) {
            if ("xValue".equals(key)) {
                CONTROL.setXValue(((DoubleProperty) properties.get(key)).get());
            } else if ("yValue".equals(key)) {
                CONTROL.setYValue(((DoubleProperty) properties.get(key)).get());
            } else if ("xAxisLabel".equals(key)) {
                CONTROL.setXAxisLabel(((StringProperty) properties.get(key)).get());
            } else if ("yAxisLabel".equals(key)) {
                CONTROL.setYAxisLabel(((StringProperty) properties.get(key)).get());
            } else if ("xAxisLabelVisible".equals(key)) {
                CONTROL.setXAxisLabelVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("yAxisLabelVisible".equals(key)) {
                CONTROL.setYAxisLabelVisible(((BooleanProperty) properties.get(key)).get());
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
