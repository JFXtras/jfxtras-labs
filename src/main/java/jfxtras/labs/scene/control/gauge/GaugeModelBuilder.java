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
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.scene.control.gauge.Gauge.NumberSystem;
import jfxtras.labs.scene.control.gauge.Gauge.Trend;

import java.util.HashMap;
import java.util.List;


/**
 * Created by
 * User: hansolo
 * Date: 02.01.12
 * Time: 15:50
 */
public class GaugeModelBuilder {
    private HashMap<String, Property> properties = new HashMap<String, Property>();

    public static final GaugeModelBuilder create() {
        return new GaugeModelBuilder();
    }

    public final GaugeModelBuilder value(final double VALUE) {
        properties.put("value", new SimpleDoubleProperty(VALUE));
        return this;
    }

    public final GaugeModelBuilder valueAnimationEnabled(final boolean VALUE_ANIMATION_ENABLED) {
        properties.put("valueAnimationEnabled", new SimpleBooleanProperty(VALUE_ANIMATION_ENABLED));
        return this;
    }

    public final GaugeModelBuilder animationDuration(final double ANIMATION_DURATION) {
        properties.put("animationDuration", new SimpleDoubleProperty(ANIMATION_DURATION));
        return this;
    }

    public final GaugeModelBuilder redrawTolerance(final double REDRAW_TOLERANCE) {
        properties.put("redrawTolerance", new SimpleDoubleProperty(REDRAW_TOLERANCE));
        return this;
    }

    public final GaugeModelBuilder minValue(final double MIN_VALUE) {
        properties.put("minValue", new SimpleDoubleProperty(MIN_VALUE));
        return this;
    }

    public final GaugeModelBuilder maxValue(final double MAX_VALUE) {
        properties.put("maxValue", new SimpleDoubleProperty(MAX_VALUE));
        return this;
    }

    public final GaugeModelBuilder threshold(final double THRESHOLD) {
        properties.put("threshold", new SimpleDoubleProperty(THRESHOLD));
        return this;
    }

    public final GaugeModelBuilder thresholdBehaviorInverted(final boolean THRESHOLD_BEHAVIOR_INVERTED) {
        properties.put("thresholdBehaviorInverted", new SimpleBooleanProperty(THRESHOLD_BEHAVIOR_INVERTED));
        return this;
    }

    public final GaugeModelBuilder title(final String TITLE) {
        properties.put("title", new SimpleStringProperty(TITLE));
        return this;
    }

    public final GaugeModelBuilder unit(final String UNIT) {
        properties.put("unit", new SimpleStringProperty(UNIT));
        return this;
    }

    public final GaugeModelBuilder lcdValueCoupled(final boolean LCD_VALUE_COUPLED) {
        properties.put("lcdValueCoupled", new SimpleBooleanProperty(LCD_VALUE_COUPLED));
        return this;
    }

    public final GaugeModelBuilder lcdThreshold(final double LCD_THRESHOLD) {
        properties.put("lcdThreshold", new SimpleDoubleProperty(LCD_THRESHOLD));
        return this;
    }

    public final GaugeModelBuilder lcdThresholdBehaviorInverted(final boolean LCD_THRESHOLD_BEHAVIOR_INVERTED) {
        properties.put("lcdThresholdBehaviorInverted", new SimpleBooleanProperty(LCD_THRESHOLD_BEHAVIOR_INVERTED));
        return this;
    }

    public final GaugeModelBuilder lcdUnitString(final String LCD_UNIT_STRING) {
        properties.put("lcdUnitString", new SimpleStringProperty(LCD_UNIT_STRING));
        return this;
    }

    public final GaugeModelBuilder lcdNumberSystem(final NumberSystem LCD_NUMBER_SYSTEM) {
        properties.put("lcdNumberSystem", new SimpleObjectProperty<NumberSystem>(LCD_NUMBER_SYSTEM));
        return this;
    }

    public final GaugeModelBuilder maxNoOfMajorTicks(final int MAX_NO_OF_MAJOR_TICKS) {
        properties.put("maxNoOfMajorTicks", new SimpleIntegerProperty(MAX_NO_OF_MAJOR_TICKS));
        return this;
    }

    public final GaugeModelBuilder maxNoOfMinorTicks(final int MAX_NO_OF_MINOR_TICKS) {
        properties.put("maxNoOfMinorTicks", new SimpleIntegerProperty(MAX_NO_OF_MINOR_TICKS));
        return this;
    }

    public final GaugeModelBuilder majorTickSpacing(final int MAJOR_TICKSPACING) {
        properties.put("majorTickSpacing", new SimpleIntegerProperty(MAJOR_TICKSPACING));
        return this;
    }

    public final GaugeModelBuilder minorTickSpacing(final int MINOR_TICKSPACING) {
        properties.put("minorTickSpacing", new SimpleIntegerProperty(MINOR_TICKSPACING));
        return this;
    }

    public final GaugeModelBuilder trend(final Trend TREND) {
        properties.put("trend", new SimpleObjectProperty<Trend>(TREND));
        return this;
    }

    public final GaugeModelBuilder niceScaling(final boolean NICE_SCALING) {
        properties.put("niceScaling", new SimpleBooleanProperty(NICE_SCALING));
        return this;
    }

    public final GaugeModelBuilder sections(final Section... SECTION_ARRAY) {
        properties.put("sectionsArray", new SimpleObjectProperty<Section[]>(SECTION_ARRAY));
        return this;
    }

    public final GaugeModelBuilder sections(final List<Section> SECTIONS) {
        properties.put("sectionsList", new SimpleObjectProperty<List<Section>>(SECTIONS));
        return this;
    }

    public final GaugeModelBuilder areas(final Section... AREAS_ARRAY) {
        properties.put("areasArray", new SimpleObjectProperty<Section[]>(AREAS_ARRAY));
        return this;
    }

    public final GaugeModelBuilder areas(final List<Section> AREAS) {
        properties.put("areasList", new SimpleObjectProperty<List<Section>>(AREAS));
        return this;
    }

    public final GaugeModelBuilder tickMarkSections(final Section... TICK_MARK_SECTION_ARRAY) {
        properties.put("tickMarkSectionsArray", new SimpleObjectProperty<Section[]>(TICK_MARK_SECTION_ARRAY));
        return this;
    }

    public final GaugeModelBuilder tickMarkSections(final List<Section> TICK_MARK_SECTIONS) {
        properties.put("tickMarkSectionsList", new SimpleObjectProperty<List<Section>>(TICK_MARK_SECTIONS));
        return this;
    }

    public final GaugeModelBuilder markers(final Marker... MARKER_ARRAY) {
        properties.put("markersArray", new SimpleObjectProperty<Marker[]>(MARKER_ARRAY));
        return this;
    }

    public final GaugeModelBuilder markers(final List<Marker> MARKERS) {
        properties.put("markersList", new SimpleObjectProperty<List<Marker>>(MARKERS));
        return this;
    }

    public final GaugeModelBuilder endlessMode(final boolean ENDLESS_MODE) {
        properties.put("endlessMode", new SimpleBooleanProperty(ENDLESS_MODE));
        return this;
    }

    public final GaugeModel build() {
        final GaugeModel MODEL = new GaugeModel();
        for (String key : properties.keySet()) {
            if ("value".equals(key)) {
                MODEL.setValue(((DoubleProperty) properties.get(key)).get());
            } else if ("valueAnimationEnabled".equals(key)) {
                MODEL.setValueAnimationEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("animationDuration".equals(key)) {
                MODEL.setAnimationDuration(((DoubleProperty) properties.get(key)).get());
            } else if ("redrawTolerance".equals(key)) {
                MODEL.setRedrawTolerance(((DoubleProperty) properties.get(key)).get());
            } else if ("minValue".equals(key)) {
                MODEL.setMinValue(((DoubleProperty) properties.get(key)).get());
            } else if ("maxValue".equals(key)) {
                MODEL.setMaxValue(((DoubleProperty) properties.get(key)).get());
            } else if ("threshold".equals(key)) {
                MODEL.setThreshold(((DoubleProperty) properties.get(key)).get());
            } else if ("thresholdBehaviorInverted".equals(key)) {
                MODEL.setThresholdBehaviorInverted(((BooleanProperty) properties.get(key)).get());
            } else if ("title".equals(key)) {
                MODEL.setTitle(((StringProperty) properties.get(key)).get());
            } else if ("unit".equals(key)) {
                MODEL.setUnit(((StringProperty) properties.get(key)).get());
            } else if ("lcdValueCoupled".equals(key)) {
                MODEL.setLcdValueCoupled(((BooleanProperty) properties.get(key)).get());
            } else if ("lcdThreshold".equals(key)) {
                MODEL.setLcdThreshold(((DoubleProperty) properties.get(key)).get());
            } else if ("lcdThresholdBehaviorInverted".equals(key)) {
                MODEL.setLcdThresholdBehaviorInverted(((BooleanProperty) properties.get(key)).get());
            } else if ("lcdUnitString".equals(key)) {
                MODEL.setLcdUnit(((StringProperty) properties.get(key)).get());
            } else if ("lcdNumberSystem".equals(key)) {
                MODEL.setLcdNumberSystem(((ObjectProperty<NumberSystem>) properties.get(key)).get());
            } else if ("maxNoOfMajorTicks".equals(key)) {
                MODEL.setMaxNoOfMajorTicks(((IntegerProperty) properties.get(key)).get());
            } else if ("maxNoOfMinorTicks".equals(key)) {
                MODEL.setMaxNoOfMinorTicks(((IntegerProperty) properties.get(key)).get());
            } else if ("majorTickSpacing".equals(key)) {
                MODEL.setMajorTickSpacing(((IntegerProperty) properties.get(key)).get());
            } else if ("minorTickSpacing".equals(key)) {
                MODEL.setMinorTickSpacing(((IntegerProperty) properties.get(key)).get());
            } else if ("trend".equals(key)) {
                MODEL.setTrend(((ObjectProperty<Trend>) properties.get(key)).get());
            } else if ("niceScaling".equals(key)) {
                MODEL.setNiceScaling(((BooleanProperty) properties.get(key)).get());
            } else if ("sectionsArray".equals(key)) {
                MODEL.setSections(((ObjectProperty<Section[]>) properties.get(key)).get());
            } else if ("sectionsList".equals(key)) {
                MODEL.setSections(((ObjectProperty<List<Section>>) properties.get(key)).get());
            } else if ("areasArray".equals(key)) {
                MODEL.setAreas(((ObjectProperty<Section[]>) properties.get(key)).get());
            } else if ("areasList".equals(key)) {
                MODEL.setAreas(((ObjectProperty<List<Section>>) properties.get(key)).get());
            } else if ("tickMarkSectionsArray".equals(key)) {
                MODEL.setTickMarkSections(((ObjectProperty<Section[]>) properties.get(key)).get());
            } else if ("tickMarkSectionsList".equals(key)) {
                MODEL.setTickMarkSections(((ObjectProperty<List<Section>>) properties.get(key)).get());
            } else if ("markersArray".equals(key)) {
                MODEL.setMarkers(((ObjectProperty<Marker[]>) properties.get(key)).get());
            } else if ("markersList".equals(key)) {
                MODEL.setMarkers(((ObjectProperty<List<Marker>>) properties.get(key)).get());
            } else if ("endlessMode".equals(key)) {
                MODEL.setEndlessMode(((BooleanProperty) properties.get(key)).get());
            }
        }
        return MODEL;
    }
}
