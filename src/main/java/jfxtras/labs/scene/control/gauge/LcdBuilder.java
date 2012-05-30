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

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 14.03.12
 * Time: 15:34
 */
public class LcdBuilder {
    private HashMap<String, Property> properties = new HashMap<String, Property>();

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

    public final LcdBuilder digitalFontEnabled(final boolean DIGITAL_FONT_ENABLED) {
        properties.put("digitalFontEnabled", new SimpleBooleanProperty(DIGITAL_FONT_ENABLED));
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

    public final Lcd build() {
        final Lcd LCD = new Lcd();

        for (String key : properties.keySet()) {
            if("styleModel".equals(key)) {
                LCD.setStyleModel(((ObjectProperty<StyleModel>) properties.get(key)).get());
            } else if("design".equals(key)) {
                LCD.setLcdDesign(((ObjectProperty<LcdDesign>) properties.get(key)).get());
            } else if("value".equals(key)) {
                LCD.setValue(((DoubleProperty) properties.get(key)).get());
            } else if("valueAnimationEnabled".equals(key)) {
                LCD.setValueAnimationEnabled(((BooleanProperty) properties.get(key)).get());
            } else if("threshold".equals(key)) {
                LCD.setThreshold(((DoubleProperty) properties.get(key)).get());
                LCD.setLcdThreshold(((DoubleProperty) properties.get(key)).get());
            } else if("decimals".equals(key)) {
                LCD.setLcdDecimals(((IntegerProperty) properties.get(key)).get());
            } else if("digitalFontEnabled".equals(key)) {
                LCD.setLcdDigitalFontEnabled(((BooleanProperty) properties.get(key)).get());
            } else if("minMeasuredValueVisible".equals(key)) {
                LCD.setLcdMinMeasuredValueVisible(((BooleanProperty) properties.get(key)).get());
            } else if("minMeasuredValueDecimals".equals(key)) {
                LCD.setLcdMinMeasuredValueDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("maxMeasuredValueVisible".equals(key)) {
                LCD.setLcdMaxMeasuredValueVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("maxMeasuredValueDecimals".equals(key)) {
                LCD.setLcdMaxMeasuredValueDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("formerValueVisible".equals(key)) {
                LCD.setLcdFormerValueVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("bargraphVisible".equals(key)) {
                LCD.setBargraphVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("title".equals(key)) {
                LCD.setTitle(((StringProperty) properties.get(key)).get());
            } else if ("titleVisible".equals(key)) {
                LCD.setTitleVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("unit".equals(key)) {
                LCD.setUnit(((StringProperty) properties.get(key)).get());
                LCD.setLcdUnit(((StringProperty) properties.get(key)).get());
            } else if ("unitVisible".equals(key)) {
                LCD.setUnitVisible(((BooleanProperty) properties.get(key)).get());
                LCD.setLcdUnitVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("trendVisible".equals(key)) {
                LCD.setTrendVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("thresholdVisible".equals(key)) {
                LCD.setThresholdVisible(((BooleanProperty) properties.get(key)).get());
                LCD.setLcdThresholdVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("thresholdBehaviorInverted".equals(key)) {
                LCD.setThresholdBehaviorInverted(((BooleanProperty) properties.get(key)).get());
                LCD.setLcdThresholdBehaviorInverted(((BooleanProperty) properties.get(key)).get());
            } else if ("numberSystemVisible".equals(key)) {
                LCD.setLcdNumberSystemVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("unitFont".equals(key)) {
                LCD.setUnitFont(((StringProperty) properties.get(key)).get());
                LCD.setLcdUnitFont(((StringProperty) properties.get(key)).get());
            } else if ("titleFont".equals(key)) {
                LCD.setTitleFont(((StringProperty) properties.get(key)).get());
                LCD.setLcdTitleFont(((StringProperty) properties.get(key)).get());
            }
        }
        return LCD;
    }
}
