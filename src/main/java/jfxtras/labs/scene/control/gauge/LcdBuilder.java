/**
 * LcdBuilder.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
import javafx.util.Builder;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 14.03.12
 * Time: 15:34
 */
public class LcdBuilder<B extends LcdBuilder<B>> extends ControlBuilder<B> implements Builder<Lcd> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();


    // ******************** Constructors **************************************
    protected LcdBuilder() {};


    // ******************** Methods *******************************************
    public static final LcdBuilder create() {
        return new LcdBuilder();
    }

    public final LcdBuilder styleModel(final StyleModel STYLE_MODEL) {
        properties.put("styleModel", new SimpleObjectProperty<StyleModel>(STYLE_MODEL));
        return this;
    }

    public final LcdBuilder design(final LcdDesign DESIGN) {
        properties.put("design", new SimpleObjectProperty<LcdDesign>(DESIGN));
        return this;
    }

    public final LcdBuilder value(final double VALUE) {
        properties.put("value", new SimpleDoubleProperty(VALUE));
        return this;
    }

    public final LcdBuilder valueAnimationEnabled(final boolean VALUE_ANIMATION_ENABLED) {
        properties.put("valueAnimationEnabled", new SimpleBooleanProperty(VALUE_ANIMATION_ENABLED));
        return this;
    }

    public final LcdBuilder threshold(final double THRESHOLD) {
        properties.put("threshold", new SimpleDoubleProperty(THRESHOLD));
        return this;
    }

    public final LcdBuilder decimals(final int DECIMALS) {
        properties.put("decimals", new SimpleIntegerProperty(DECIMALS));
        return this;
    }

    public final LcdBuilder backgroundVisible(final boolean BACKGROUND_VISIBLE) {
        properties.put("backgroundVisible", new SimpleBooleanProperty(BACKGROUND_VISIBLE));
        return this;
    }

    public final LcdBuilder minMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE) {
        properties.put("minMeasuredValueVisible", new SimpleBooleanProperty(MIN_MEASURED_VALUE_VISIBLE));
        return this;
    }

    public final LcdBuilder minMeasuredValueDecimals(final int MIN_MEASURED_VALUE_DECIMALS) {
        properties.put("minMeasuredValueDecimals", new SimpleIntegerProperty(MIN_MEASURED_VALUE_DECIMALS));
        return this;
    }

    public final LcdBuilder maxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE) {
        properties.put("maxMeasuredValueVisible", new SimpleBooleanProperty(MAX_MEASURED_VALUE_VISIBLE));
        return this;
    }

    public final LcdBuilder maxMeasuredValueDecimals(final int MAX_MEASURED_VALUE_DECIMALS) {
        properties.put("maxMeasuredValueDecimals", new SimpleIntegerProperty(MAX_MEASURED_VALUE_DECIMALS));
        return this;
    }

    public final LcdBuilder formerValueVisible(final boolean FORMER_VALUE_VISIBLE) {
        properties.put("formerValueVisible", new SimpleBooleanProperty(FORMER_VALUE_VISIBLE));
        return this;
    }

    public final LcdBuilder bargraphVisible(final boolean BARGRAPH_VISIBLE) {
        properties.put("bargraphVisible", new SimpleBooleanProperty(BARGRAPH_VISIBLE));
        return this;
    }

    public final LcdBuilder title(final String TITLE) {
        properties.put("title", new SimpleStringProperty(TITLE));
        return this;
    }

    public final LcdBuilder titleVisible(final boolean TITLE_VISIBLE) {
        properties.put("titleVisible", new SimpleBooleanProperty(TITLE_VISIBLE));
        return this;
    }

    public final LcdBuilder unit(final String UNIT) {
        properties.put("unit", new SimpleStringProperty(UNIT));
        return this;
    }

    public final LcdBuilder unitVisible(final boolean UNIT_VISIBLE) {
        properties.put("unitVisible", new SimpleBooleanProperty(UNIT_VISIBLE));
        return this;
    }

    public final LcdBuilder trendVisible(final boolean TREND_VISIBLE) {
        properties.put("trendVisible", new SimpleBooleanProperty(TREND_VISIBLE));
        return this;
    }

    public final LcdBuilder thresholdVisible(final boolean THRESHOLD_VISIBLE) {
        properties.put("thresholdVisible", new SimpleBooleanProperty(THRESHOLD_VISIBLE));
        return this;
    }

    public final LcdBuilder thresholdBehaviorInverted(final boolean THRESHOLD_BEHAVIOR_INVERTED) {
        properties.put("thresholdBehaviorInverted", new SimpleBooleanProperty(THRESHOLD_BEHAVIOR_INVERTED));
        return this;
    }

    public final LcdBuilder numberSystem(final Gauge.NumberSystem NUMBER_SYSTEM) {
        properties.put("numberSystem", new SimpleObjectProperty<Gauge.NumberSystem>(NUMBER_SYSTEM));
        return this;
    }

    public final LcdBuilder numberSystemVisible(final boolean NUMBER_SYSTEM_VISIBLE) {
        properties.put("numberSystemVisible", new SimpleBooleanProperty(NUMBER_SYSTEM_VISIBLE));
        return this;
    }

    public final LcdBuilder titleFont(final String TITLE_FONT) {
        properties.put("titleFont", new SimpleStringProperty(TITLE_FONT));
        return this;
    }

    public final LcdBuilder unitFont(final String UNIT_FONT) {
        properties.put("unitFont", new SimpleStringProperty(UNIT_FONT));
        return this;
    }

    public final LcdBuilder valueFont(final Gauge.LcdFont VALUE_FONT) {
        properties.put("valueFont", new SimpleObjectProperty<Gauge.LcdFont>(VALUE_FONT));
        return this;
    }

    public final LcdBuilder clockMode(final boolean CLOCK_MODE) {
        properties.put("clockMode", new SimpleBooleanProperty(CLOCK_MODE));
        return this;
    }

    public final LcdBuilder clockSecondsVisible(final boolean CLOCK_SECONDS_VISIBLE) {
        properties.put("clockSecondsVisible", new SimpleBooleanProperty(CLOCK_SECONDS_VISIBLE));
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

    @Override public final Lcd build() {
        final Lcd CONTROL = new Lcd();
        for (String key : properties.keySet()) {
            if("styleModel".equals(key)) {
                CONTROL.setStyleModel(((ObjectProperty<StyleModel>) properties.get(key)).get());
            } else if("design".equals(key)) {
                CONTROL.setLcdDesign(((ObjectProperty<LcdDesign>) properties.get(key)).get());
            } else if("value".equals(key)) {
                CONTROL.setValue(((DoubleProperty) properties.get(key)).get());
                CONTROL.setLcdValue(((DoubleProperty) properties.get(key)).get());
            } else if("valueAnimationEnabled".equals(key)) {
                CONTROL.setValueAnimationEnabled(((BooleanProperty) properties.get(key)).get());
            } else if("threshold".equals(key)) {
                CONTROL.setThreshold(((DoubleProperty) properties.get(key)).get());
                CONTROL.setLcdThreshold(((DoubleProperty) properties.get(key)).get());
            } else if("decimals".equals(key)) {
                CONTROL.setLcdDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("backgroundVisible".equals(key)) {
                CONTROL.setLcdBackgroundVisible(((BooleanProperty) properties.get(key)).get());
            } else if("minMeasuredValueVisible".equals(key)) {
                CONTROL.setLcdMinMeasuredValueVisible(((BooleanProperty) properties.get(key)).get());
            } else if("minMeasuredValueDecimals".equals(key)) {
                CONTROL.setLcdMinMeasuredValueDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("maxMeasuredValueVisible".equals(key)) {
                CONTROL.setLcdMaxMeasuredValueVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("maxMeasuredValueDecimals".equals(key)) {
                CONTROL.setLcdMaxMeasuredValueDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("formerValueVisible".equals(key)) {
                CONTROL.setLcdFormerValueVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("bargraphVisible".equals(key)) {
                CONTROL.setBargraphVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("title".equals(key)) {
                CONTROL.setTitle(((StringProperty) properties.get(key)).get());
            } else if ("titleVisible".equals(key)) {
                CONTROL.setTitleVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("unit".equals(key)) {
                CONTROL.setUnit(((StringProperty) properties.get(key)).get());
                CONTROL.setLcdUnit(((StringProperty) properties.get(key)).get());
            } else if ("unitVisible".equals(key)) {
                CONTROL.setUnitVisible(((BooleanProperty) properties.get(key)).get());
                CONTROL.setLcdUnitVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("trendVisible".equals(key)) {
                CONTROL.setTrendVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("thresholdVisible".equals(key)) {
                CONTROL.setThresholdVisible(((BooleanProperty) properties.get(key)).get());
                CONTROL.setLcdThresholdVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("thresholdBehaviorInverted".equals(key)) {
                CONTROL.setThresholdBehaviorInverted(((BooleanProperty) properties.get(key)).get());
                CONTROL.setLcdThresholdBehaviorInverted(((BooleanProperty) properties.get(key)).get());
            } else if("numberSystem".equals(key)) {
                CONTROL.setLcdNumberSystem(((ObjectProperty<Gauge.NumberSystem>) properties.get(key)).get());
            } else if ("numberSystemVisible".equals(key)) {
                CONTROL.setLcdNumberSystemVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("unitFont".equals(key)) {
                CONTROL.setUnitFont(((StringProperty) properties.get(key)).get());
                CONTROL.setLcdUnitFont(((StringProperty) properties.get(key)).get());
            } else if ("titleFont".equals(key)) {
                CONTROL.setTitleFont(((StringProperty) properties.get(key)).get());
                CONTROL.setLcdTitleFont(((StringProperty) properties.get(key)).get());
            } else if ("valueFont".equals(key)) {
                CONTROL.setLcdValueFont(((ObjectProperty<Gauge.LcdFont>) properties.get(key)).get());
            } else if ("clockMode".equals(key)) {
                CONTROL.setClockMode(((BooleanProperty) properties.get(key)).get());
            } else if ("clockSecondsVisible".equals(key)) {
                CONTROL.setClockSecondsVisible(((BooleanProperty) properties.get(key)).get());
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
