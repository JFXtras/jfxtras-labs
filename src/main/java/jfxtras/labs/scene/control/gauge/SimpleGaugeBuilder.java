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
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.util.HashMap;
import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: hansolo
 * Date: 10.09.12
 * Time: 15:16
 * To change this template use File | Settings | File Templates.
 */
public class SimpleGaugeBuilder<B extends SimpleGaugeBuilder<B>> extends ControlBuilder<B> implements Builder<SimpleGauge> {
    private HashMap<String, Property> properties = new HashMap<String, Property>();
    public static enum GaugeType {
        SIMPLE_RADIAL_GAUGE,
        SIMPLE_LINEAR_GAUGE
    }


    // ******************** Constructors **************************************
    protected SimpleGaugeBuilder() {}


    // ******************** Methods *******************************************
    public static final SimpleGaugeBuilder create() {
        return new SimpleGaugeBuilder();
    }

    public final SimpleGaugeBuilder type(final GaugeType TYPE) {
        properties.put("TYPE", new SimpleObjectProperty<GaugeType>(TYPE));
        return this;
    }

    public final SimpleGaugeBuilder model(final GaugeModel MODEL) {
        properties.put("MODEL", new SimpleObjectProperty<GaugeModel>(MODEL));
        return this;
    }

    public final SimpleGaugeBuilder minValue(final double MIN_VALUE) {
        properties.put("MIN_VALUE", new SimpleDoubleProperty(MIN_VALUE));
        return this;
    }

    public final SimpleGaugeBuilder maxValue(final double MAX_VALUE) {
        properties.put("MAX_VALUE", new SimpleDoubleProperty(MAX_VALUE));
        return this;
    }

    public final SimpleGaugeBuilder value(final double VALUE) {
        properties.put("VALUE", new SimpleDoubleProperty(VALUE));
        return this;
    }

    public final SimpleGaugeBuilder valueAnimationEnabled(final boolean VALUE_ANIMATION_ENABLED) {
        properties.put("VALUE_ANIMATION_ENABLED", new SimpleBooleanProperty(VALUE_ANIMATION_ENABLED));
        return this;
    }

    public final SimpleGaugeBuilder barColor(final Color BAR_COLOR) {
        properties.put("BAR_COLOR", new SimpleObjectProperty<Color>(BAR_COLOR));
        return this;
    }

    public final SimpleGaugeBuilder barWidth(final double BAR_WIDTH) {
        properties.put("BAR_WIDTH", new SimpleDoubleProperty(BAR_WIDTH));
        return this;
    }

    public final SimpleGaugeBuilder labelFontSize(final double LABEL_FONT_SIZE) {
        properties.put("LABEL_FONT_SIZE", new SimpleDoubleProperty(LABEL_FONT_SIZE));
        return this;
    }

    public final SimpleGaugeBuilder unitFontSize(final double UNIT_FONT_SIZE) {
        properties.put("UNIT_FONT_SIZE", new SimpleDoubleProperty(UNIT_FONT_SIZE));
        return this;
    }

    public final SimpleGaugeBuilder noOfDecimals(final int NO_OF_DECIMALS) {
        properties.put("NO_OF_DECIMALS", new SimpleIntegerProperty(NO_OF_DECIMALS));
        return this;
    }

    public final SimpleGaugeBuilder sections(final Section[] SECTIONS) {
        properties.put("SECTIONS_ARRAY", new SimpleObjectProperty<Section[]>(SECTIONS));
        return this;
    }

    public final SimpleGaugeBuilder sections(final List<Section> SECTIONS) {
        properties.put("SECTIONS_LIST", new SimpleObjectProperty<List<Section>>(SECTIONS));
        return this;
    }

    public final SimpleGaugeBuilder unit(final String UNIT) {
        properties.put("UNIT", new SimpleStringProperty(UNIT));
        return this;
    }

    public final SimpleGaugeBuilder barBackgroundColor(final Color BAR_BACKGROUND_COLOR) {
        properties.put("BAR_BACKGROUND_COLOR", new SimpleObjectProperty<Color>(BAR_BACKGROUND_COLOR));
        return this;
    }

    public final SimpleGaugeBuilder labelColor(final Color LABEL_COLOR) {
        properties.put("LABEL_COLOR", new SimpleObjectProperty<Color>(LABEL_COLOR));
        return this;
    }

    public final SimpleGaugeBuilder unitColor(final Color UNIT_COLOR) {
        properties.put("UNIT_COLOR", new SimpleObjectProperty<Color>(UNIT_COLOR));
        return this;
    }

    public final SimpleGaugeBuilder minLabelVisible(final boolean MIN_LABEL_VISIBLE) {
        properties.put("MIN_LABEL_VISIBLE", new SimpleBooleanProperty(MIN_LABEL_VISIBLE));
        return this;
    }

    public final SimpleGaugeBuilder maxLabelVisible(final boolean MAX_LABEL_VISIBLE) {
        properties.put("MAX_LABEL_VISIBLE", new SimpleBooleanProperty(MAX_LABEL_VISIBLE));
        return this;
    }

    public final SimpleGaugeBuilder minMaxLabelFontSize(final double MIN_MAX_LABEL_FONT_SIZE) {
        properties.put("MIN_MAX_LABEL_FONT_SIZE", new SimpleDoubleProperty(MIN_MAX_LABEL_FONT_SIZE));
        return this;
    }

    public final SimpleGaugeBuilder minLabelColor(final Color MIN_LABEL_COLOR) {
        properties.put("MIN_LABEL_COLOR", new SimpleObjectProperty<Color>(MIN_LABEL_COLOR));
        return this;
    }

    public final SimpleGaugeBuilder maxLabelColor(final Color MAX_LABEL_COLOR) {
        properties.put("MAX_LABEL_COLOR", new SimpleObjectProperty<Color>(MAX_LABEL_COLOR));
        return this;
    }

    public final SimpleGaugeBuilder roundedBar(final boolean ROUNDED_BAR) {
        properties.put("ROUNDED_BAR", new SimpleBooleanProperty(ROUNDED_BAR));
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

    @Override public final SimpleGauge build() {
        final SimpleGauge CONTROL;

        if (properties.containsKey("TYPE")) {
            switch(((ObjectProperty<GaugeType>)properties.get("TYPE")).get()) {
                case SIMPLE_LINEAR_GAUGE:
                    CONTROL = new SimpleLinearGauge();
                    break;
                case SIMPLE_RADIAL_GAUGE:
                default:
                    CONTROL = new SimpleRadialGauge();
            }
        } else {
            CONTROL = new SimpleRadialGauge();
        }

        if (properties.containsKey("MODEL")) {
            CONTROL.setGaugeModel(((ObjectProperty<GaugeModel>) properties.get("MODEL")).get());
        }

        if (properties.containsKey("PREF_WIDTH") && properties.containsKey("PREF_HEIGHT")) {
            CONTROL.setPrefSize(((DoubleProperty) properties.get("PREF_WIDTH")).get(), ((DoubleProperty) properties.get("PREF_HEIGHT")).get());
        }

        for (String key : properties.keySet()) {
            if ("MIN_VALUE".equals(key)) {
                CONTROL.setMinValue(((DoubleProperty) properties.get(key)).get());
            } else if("MAX_VALUE".equals(key)) {
                CONTROL.setMaxValue(((DoubleProperty) properties.get(key)).get());
            } else if ("VALUE".equals(key)) {
                CONTROL.setValue(((DoubleProperty) properties.get(key)).get());
            }  else if ("LAYOUT_X".equals(key)) {
                CONTROL.setLayoutX(((DoubleProperty) properties.get(key)).get());
            } else if ("LAYOUT_Y".equals(key)) {
                CONTROL.setLayoutY(((DoubleProperty) properties.get(key)).get());
            } else if ("VALUE_ANIMATION_ENABLED".equals(key)) {
                CONTROL.setValueAnimationEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("BAR_COLOR".equals(key)) {
                CONTROL.setValueColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("BAR_WIDTH".equals(key)) {
                CONTROL.setBarWidth(((DoubleProperty) properties.get(key)).get());
            } else if ("LABEL_FONT_SIZE".equals(key)) {
                CONTROL.setLabelFontSize(((DoubleProperty) properties.get(key)).get());
            } else if ("NO_OF_DECIMALS".equals(key)) {
                CONTROL.setNoOfDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("SECTIONS_ARRAY".equals(key)) {
                CONTROL.setSections(((ObjectProperty<Section[]>) properties.get(key)).get());
            } else if ("SECTIONS_LIST".equals(key)) {
                CONTROL.setSections(((ObjectProperty<List<Section>>) properties.get(key)).get());
            } else if ("UNIT".equals(key)) {
                CONTROL.setUnit(((StringProperty) properties.get(key)).get());
            } else if ("BAR_BACKGROUND_COLOR".equals(key)) {
                CONTROL.setBarBackgroundColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("LABEL_COLOR".equals(key)) {
                CONTROL.setLabelColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("UNIT_COLOR".equals(key)) {
                CONTROL.setUnitColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("UNIT_FONT_SIZE".equals(key)) {
                CONTROL.setUnitFontSize(((DoubleProperty) properties.get(key)).get());
            } else if ("MIN_LABEL_VISIBLE".equals(key)) {
                CONTROL.setMinLabelVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("MAX_LABEL_VISIBLE".equals(key)) {
                CONTROL.setMaxLabelVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("MIN_MAX_LABEL_FONT_SIZE".equals(key)) {
                CONTROL.setMinMaxLabelFontSize(((DoubleProperty) properties.get(key)).get());
            } else if ("MIN_LABEL_COLOR".equals(key)) {
                CONTROL.setMinLabelColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("MAX_LABEL_COLOR".equals(key)) {
                CONTROL.setMaxLabelColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("ROUNDED_BAR".equals(key)) {
                CONTROL.setRoundedBar(((BooleanProperty) properties.get(key)).get());
            }
        }
        return CONTROL;
    }
}
