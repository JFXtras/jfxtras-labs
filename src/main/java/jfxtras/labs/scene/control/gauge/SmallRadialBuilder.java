/*
 * Copyright (c) 2012, JFXtras
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *      * Redistributions of source code must retain the above copyright
 *        notice, this list of conditions and the following disclaimer.
 *      * Redistributions in binary form must reproduce the above copyright
 *        notice, this list of conditions and the following disclaimer in the
 *        documentation and/or other materials provided with the distribution.
 *      * Neither the name of the <organization> nor the
 *        names of its contributors may be used to endorse or promote products
 *        derived from this software without specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 *  DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
import javafx.scene.control.ControlBuilder;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.util.HashMap;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: hansolo
 * Date: 09.10.12
 * Time: 09:30
 * To change this template use File | Settings | File Templates.
 */
public class SmallRadialBuilder<B extends SmallRadialBuilder<B>> extends ControlBuilder<B> implements Builder<SmallRadial> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();


    // ******************** Constructors **************************************
    protected SmallRadialBuilder() {}


    // ******************** Methods *******************************************
    public static final SmallRadialBuilder create() {
        return new SmallRadialBuilder();
    }

    public final SmallRadialBuilder model(final GaugeModel GAUGE_MODEL) {
        properties.put("GAUGE_MODEL", new SimpleObjectProperty<GaugeModel>(GAUGE_MODEL));
        return this;
    }

    public final SmallRadialBuilder minValue(final double MIN_VALUE) {
        properties.put("MIN_VALUE", new SimpleDoubleProperty(MIN_VALUE));
        return this;
    }

    public final SmallRadialBuilder maxValue(final double MAX_VALUE) {
        properties.put("MAX_VALUE", new SimpleDoubleProperty(MAX_VALUE));
        return this;
    }

    public final SmallRadialBuilder value(final double VALUE) {
        properties.put("VALUE", new SimpleDoubleProperty(VALUE));
        return this;
    }

    public final SmallRadialBuilder valueAnimationEnabled(final boolean VALUE_ANIMATION_ENABLED) {
        properties.put("VALUE_ANIMATION_ENABLED", new SimpleBooleanProperty(VALUE_ANIMATION_ENABLED));
        return this;
    }

    public final SmallRadialBuilder noOfDecimals(final int NO_OF_DECIMALS) {
        properties.put("NO_OF_DECIMALS", new SimpleIntegerProperty(NO_OF_DECIMALS));
        return this;
    }

    public final SmallRadialBuilder sections(final Section[] SECTIONS) {
        properties.put("SECTIONS_ARRAY", new SimpleObjectProperty<Section[]>(SECTIONS));
        return this;
    }

    public final SmallRadialBuilder sections(final List<Section> SECTIONS) {
        properties.put("SECTIONS_LIST", new SimpleObjectProperty<List<Section>>(SECTIONS));
        return this;
    }

    public final SmallRadialBuilder frameColor(final Color FRAME_COLOR) {
        properties.put("FRAME_COLOR", new SimpleObjectProperty<Color>(FRAME_COLOR));
        return this;
    }

    public final SmallRadialBuilder backgroundColor(final Color BACKGROUND_COLOR) {
        properties.put("BACKGROUND_COLOR", new SimpleObjectProperty<Color>(BACKGROUND_COLOR));
        return this;
    }

    public final SmallRadialBuilder tickMarkColor(final Color TICK_MARK_COLOR) {
        properties.put("TICK_MARK_COLOR", new SimpleObjectProperty<Color>(TICK_MARK_COLOR));
        return this;
    }

    public final SmallRadialBuilder valueLabelColor(final Color VALUE_LABEL_COLOR) {
        properties.put("VALUE_LABEL_COLOR", new SimpleObjectProperty<Color>(VALUE_LABEL_COLOR));
        return this;
    }

    public final SmallRadialBuilder pointerColor(final Color POINTER_COLOR) {
        properties.put("POINTER_COLOR", new SimpleObjectProperty<Color>(POINTER_COLOR));
        return this;
    }

    public final SmallRadialBuilder centerKnobColor(final Color CENTER_KNOB_COLOR) {
        properties.put("CENTER_KNOB_COLOR", new SimpleObjectProperty<Color>(CENTER_KNOB_COLOR));
        return this;
    }

    public final SmallRadialBuilder valueLabelVisible(final boolean VALUE_LABEL_VISIBLE) {
        properties.put("VALUE_LABEL_VISIBLE", new SimpleBooleanProperty(VALUE_LABEL_VISIBLE));
        return this;
    }

    public final SmallRadialBuilder timeToValueInMs(final double TIME_TO_VALUE_IN_MS) {
        properties.put("TIME_TO_VALUE_IN_MS", new SimpleDoubleProperty(TIME_TO_VALUE_IN_MS));
        return this;
    }

    @Override public final B prefWidth(final double PREF_WIDTH) {
        properties.put("PREF_WIDTH", new SimpleDoubleProperty(PREF_WIDTH));
        return (B)this;
    }

    @Override public final B prefHeight(final double PREF_HEIGHT) {
        properties.put("PREF_HEIGHT", new SimpleDoubleProperty(PREF_HEIGHT));
        return (B)this;
    }

    @Override public final B layoutX(final double LAYOUT_X) {
        properties.put("LAYOUT_X", new SimpleDoubleProperty(LAYOUT_X));
        return (B)this;
    }

    @Override public final B layoutY(final double LAYOUT_Y) {
        properties.put("LAYOUT_Y", new SimpleDoubleProperty(LAYOUT_Y));
        return (B)this;
    }

    @Override public final SmallRadial build() {
        final SmallRadial CONTROL = new SmallRadial();
        for (String key : properties.keySet()) {
            if ("GAUGE_MODEL".equals(key)) {
                CONTROL.setGaugeModel(((ObjectProperty<GaugeModel>) properties.get(key)).get());
            } else if ("MIN_VALUE".equals(key)) {
                CONTROL.setMinValue(((DoubleProperty) properties.get(key)).get());
            } else if("MAX_VALUE".equals(key)) {
                CONTROL.setMaxValue(((DoubleProperty) properties.get(key)).get());
            } else if ("VALUE".equals(key)) {
                CONTROL.setValue(((DoubleProperty) properties.get(key)).get());
            } else if ("VALUE_ANIMATION_ENABLED".equals(key)) {
                CONTROL.setValueAnimationEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("FRAME_COLOR".equals(key)) {
                CONTROL.setFrameColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("BACKGROUND_COLOR".equals(key)) {
                CONTROL.setBackgroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("TICK_MARK_COLOR".equals(key)) {
                CONTROL.setTickMarkColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("POINTER_COLOR".equals(key)) {
                CONTROL.setPointerColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("CENTER_KNOB_COLOR".equals(key)) {
                CONTROL.setCenterKnobColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("NO_OF_DECIMALS".equals(key)) {
                CONTROL.setNoOfDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("SECTIONS_ARRAY".equals(key)) {
                CONTROL.setSections(((ObjectProperty<Section[]>) properties.get(key)).get());
            } else if ("SECTIONS_LIST".equals(key)) {
                CONTROL.setSections(((ObjectProperty<List<Section>>) properties.get(key)).get());
            } else if ("VALUE_LABEL_COLOR".equals(key)) {
                CONTROL.setValueLabelColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("VALUE_LABEL_VISIBLE".equals(key)) {
                CONTROL.setValueLabelVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("TIME_TO_VALUE_IN_MS".equals(key)) {
                CONTROL.setTimeToValueInMs(((DoubleProperty) properties.get(key)).get());
            } else if("PREF_WIDTH".equals(key)) {
                CONTROL.setPrefWidth(((DoubleProperty) properties.get(key)).get());
            } else if("PREF_HEIGHT".equals(key)) {
                CONTROL.setPrefHeight(((DoubleProperty) properties.get(key)).get());
            } else if ("LAYOUT_X".equals(key)) {
                CONTROL.setLayoutX(((DoubleProperty) properties.get(key)).get());
            } else if ("LAYOUT_Y".equals(key)) {
                CONTROL.setLayoutY(((DoubleProperty) properties.get(key)).get());
            }
        }

        return CONTROL;
    }
}

