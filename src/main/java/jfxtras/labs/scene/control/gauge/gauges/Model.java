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

package jfxtras.labs.scene.control.gauge.gauges;


import jfxtras.labs.scene.control.gauge.gauges.Gauge.NumberSystem;
import jfxtras.labs.scene.control.gauge.gauges.Gauge.Trend;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by
 * User: hansolo
 * Date: 02.01.12
 * Time: 17:14
 */
public class Model {
    private DoubleProperty               value;
    private BooleanProperty              valueAnimationEnabled;
    private DoubleProperty               animationDuration;
    private DoubleProperty               minValue;
    private DoubleProperty               maxValue;
    private DoubleProperty               range;
    private BooleanProperty              niceScaling;
    private DoubleProperty               niceMinValue;
    private DoubleProperty               niceMaxValue;
    private DoubleProperty               niceRange;
    private DoubleProperty               minMeasuredValue;
    private DoubleProperty               maxMeasuredValue;
    private DoubleProperty               threshold;
    private BooleanProperty              thresholdBehaviorInverted;
    private BooleanProperty              thresholdExceeded;
    private StringProperty               title;
    private StringProperty               unit;
    private DoubleProperty               lcdValue;
    private BooleanProperty              lcdValueCoupled;
    private DoubleProperty               lcdThreshold;
    private BooleanProperty              lcdThresholdBehaviorInverted;
    private StringProperty               lcdUnit;
    private ObjectProperty<NumberSystem> lcdNumberSystem;
    private IntegerProperty              maxNoOfMajorTicks;
    private IntegerProperty              maxNoOfMinorTicks;
    private IntegerProperty              majorTickSpacing;
    private IntegerProperty              minorTickSpacing;
    private ObjectProperty<Trend>        trend;
    private ObservableList<Section>      sections;
    private ObservableList<Section>      areas;
    private ObservableList<Indicator>    indicators;


    // ******************** Constructors **************************************
    public Model() {
        value                           = new SimpleDoubleProperty(0);
        valueAnimationEnabled           = new SimpleBooleanProperty(true);
        animationDuration               = new SimpleDoubleProperty(800);
        minValue                        = new SimpleDoubleProperty(0);
        maxValue                        = new SimpleDoubleProperty(100);
        range                           = new SimpleDoubleProperty(100);
        niceScaling                     = new SimpleBooleanProperty(true);
        niceMinValue                    = new SimpleDoubleProperty(0);
        niceMaxValue                    = new SimpleDoubleProperty(100);
        niceRange                       = new SimpleDoubleProperty(100);
        minMeasuredValue                = new SimpleDoubleProperty(100);
        maxMeasuredValue                = new SimpleDoubleProperty(0);
        threshold                       = new SimpleDoubleProperty(50);
        thresholdBehaviorInverted       = new SimpleBooleanProperty(false);
        thresholdExceeded               = new SimpleBooleanProperty(false);
        title                           = new SimpleStringProperty("title");
        unit                            = new SimpleStringProperty("unit");
        lcdValue                        = new SimpleDoubleProperty(0);
        lcdValueCoupled                 = new SimpleBooleanProperty(true);
        lcdThreshold                    = new SimpleDoubleProperty(50);
        lcdThresholdBehaviorInverted    = new SimpleBooleanProperty(false);
        lcdUnit                         = new SimpleStringProperty("");
        lcdNumberSystem                 = new SimpleObjectProperty<>(NumberSystem.DECIMAL);
        maxNoOfMajorTicks               = new SimpleIntegerProperty(10);
        maxNoOfMinorTicks               = new SimpleIntegerProperty(10);
        majorTickSpacing                = new SimpleIntegerProperty(10);
        minorTickSpacing                = new SimpleIntegerProperty(1);
        trend                           = new SimpleObjectProperty<Trend>(Trend.UNKNOWN);
        sections                        = FXCollections.observableArrayList();
        areas                           = FXCollections.observableArrayList();
        indicators                      = FXCollections.observableArrayList();
    }


    // ******************** Event handling ************************************
    public final ObjectProperty<EventHandler<ModelEvent>> onModelEventProperty() {
        return onModelEvent;
    }

    public final void setOnModelEvent(final EventHandler<ModelEvent> HANDLER) {
        onModelEventProperty().set(HANDLER);
    }

    public final EventHandler<ModelEvent> getOnModelEvent() {
        return onModelEventProperty().get();
    }

    private ObjectProperty<EventHandler<ModelEvent>> onModelEvent = new SimpleObjectProperty<>();

    public void fireModelEvent() {
        final EventHandler<ModelEvent> MODEL_EVENT_HANDLER = getOnModelEvent();
        if (MODEL_EVENT_HANDLER != null) {
            final ModelEvent MODEL_EVENT = new ModelEvent();
            MODEL_EVENT_HANDLER.handle(MODEL_EVENT);
        }
    }


    // ******************** Methods *******************************************
    public final double getValue() {
        return value.get();
    }

    public final void setValue(final double VALUE) {
        value.set(Double.compare(VALUE, niceMinValue.get()) < 0 ? niceMinValue.get() : (Double.compare(VALUE, niceMaxValue.get()) > 0 ? niceMaxValue.get() : VALUE));
    }

    public final DoubleProperty valueProperty() {
        return value;
    }

    public final boolean isValueAnimationEnabled() {
        return valueAnimationEnabled.get();
    }

    public final void setValueAnimationEnabled(final boolean VALUE_ANIMATION_ENABLED) {
        valueAnimationEnabled.set(VALUE_ANIMATION_ENABLED);
    }

    public final BooleanProperty valueAnimationEnabledProperty() {
        return valueAnimationEnabled;
    }

    public final double getAnimationDuration() {
        return animationDuration.get();
    }

    public final void setAnimationDuration(final double ANIMATION_DURATION) {
        animationDuration.set(ANIMATION_DURATION);
    }

    public final DoubleProperty animationDurationProperty() {
        return animationDuration;
    }

    public final double getMinValue() {
        return minValue.get();
    }

    public final void setMinValue(final double MIN_VALUE) {
        minValue.set(MIN_VALUE);
    }

    public final DoubleProperty minValueProperty() {
        return minValue;
    }

    public final double getMaxValue() {
        return maxValue.get();
    }

    public final void setMaxValue(final double MAX_VALUE) {
        maxValue.set(MAX_VALUE);
    }

    public final DoubleProperty maxValueProperty() {
        return maxValue;
    }

    public final double getRange() {
        return range.get();
    }

    public final DoubleProperty rangeProperty() {
        return range;
    }

    public final double getMinMeasuredValue() {
        return minMeasuredValue.get();
    }

    public final void setMinMeasuredValue(final double MIN_MEASURED_VALUE) {
        minMeasuredValue.set(MIN_MEASURED_VALUE);
    }

    public final DoubleProperty minMeasuredValueProperty() {
        return minMeasuredValue;
    }

    public final void resetMinMeasuredValue() {
        setMinMeasuredValue(getValue());
    }

    public final double getMaxMeasuredValue() {
        return maxMeasuredValue.get();
    }

    public final void setMaxMeasuredValue(final double MAX_MEASURED_VALUE) {
        maxMeasuredValue.set(MAX_MEASURED_VALUE);
    }

    public final DoubleProperty maxMeasuredValueProperty() {
        return maxMeasuredValue;
    }

    public final void resetMaxMeasuredValue() {
        setMaxMeasuredValue(getValue());
    }

    public final void resetMinMaxMeasuredValue() {
        setMinMeasuredValue(getValue());
        setMaxMeasuredValue(getValue());
    }

    public final double getThreshold() {
        return threshold.get();
    }

    public final void setThreshold(final double THRESHOLD) {
        threshold.set(Double.compare(THRESHOLD, niceMinValue.get()) < 0 ? niceMinValue.get() : (Double.compare(THRESHOLD, niceMaxValue.get()) > 0 ? niceMaxValue.get() : THRESHOLD));
    }

    public final DoubleProperty thresholdProperty() {
        return threshold;
    }

    public final boolean isThresholdBehaviorInverted() {
        return thresholdBehaviorInverted.get();
    }

    public final void setThresholdBehaviorInverted(final boolean THRESHOLD_BEHAVIOR_INVERTED) {
        thresholdBehaviorInverted.set(THRESHOLD_BEHAVIOR_INVERTED);
    }

    public final BooleanProperty thresholdBehaviorInvertedProperty() {
        return thresholdBehaviorInverted;
    }

    public final boolean isThresholdExceeded() {
        return thresholdExceeded.get();
    }

    public final void setThresholdExceeded(final boolean THRESHOLD_EXCEEDED) {
        thresholdExceeded.set(THRESHOLD_EXCEEDED);
    }

    public final BooleanProperty thresholdExceededProperty() {
        return thresholdExceeded;
    }

    public final String getTitle() {
        return title.get();
    }

    public final void setTitle(final String TITLE) {
        title.set(TITLE);
    }

    public final StringProperty titleProperty() {
        return title;
    }

    public final String getUnit() {
        return unit.get();
    }

    public final void setUnit(final String UNIT) {
        unit.set(UNIT);
    }

    public final StringProperty unitProperty() {
        return unit;
    }

    public final double getLcdValue() {
        return lcdValue.get();
    }

    public final void setLcdValue(final double LCD_VALUE) {
        lcdValue.set(LCD_VALUE);
    }

    public final DoubleProperty lcdValueProperty() {
        return lcdValue;
    }

    public final boolean isLcdValueCoupled() {
        return lcdValueCoupled.get();
    }

    public final void setLcdValueCoupled(final boolean LCD_VALUE_COUPLED) {
        lcdValueCoupled.set(LCD_VALUE_COUPLED);
    }

    public final BooleanProperty lcdValueCoupledProperty() {
        return lcdValueCoupled;
    }

    public final double getLcdThreshold() {
        return lcdThreshold.get();
    }

    public final void setLcdThreshold(final double LCD_THRESHOLD) {
        lcdThreshold.set(LCD_THRESHOLD);
    }

    public final DoubleProperty lcdThresholdProperty() {
        return lcdThreshold;
    }

    public final boolean isLcdThresholdBehaviorInverted() {
        return lcdThresholdBehaviorInverted.get();
    }

    public final void setLcdThresholdBehaviorInverted(final boolean LCD_THRESHOLD_BEHAVIOR_INVERTED) {
        lcdThresholdBehaviorInverted.set(LCD_THRESHOLD_BEHAVIOR_INVERTED);
    }

    public final BooleanProperty lcdThresholdBehaviorInvertedProperty() {
        return lcdThresholdBehaviorInverted;
    }

    public final String getLcdUnit() {
        return lcdUnit.get();
    }

    public final void setLcdUnit(final String LCD_UNIT_STRING) {
        lcdUnit.set(LCD_UNIT_STRING);
    }

    public final StringProperty lcdUnitProperty() {
        return lcdUnit;
    }

    public final NumberSystem getLcdNumberSystem() {
        return lcdNumberSystem.get();
    }

    public final void setLcdNumberSystem(final NumberSystem LCD_NUMBER_SYSTEM) {
        lcdNumberSystem.set(LCD_NUMBER_SYSTEM);
    }

    public final ObjectProperty lcdNumberSystemProperty() {
        return lcdNumberSystem;
    }

    public final int getMaxNoOfMajorTicks() {
        return maxNoOfMajorTicks.get();
    }

    public final void setMaxNoOfMajorTicks(final int MAX_NO_OF_MAJOR_TICKS) {
        maxNoOfMajorTicks.set(MAX_NO_OF_MAJOR_TICKS);
    }

    public final IntegerProperty maxNoOfMajorTicksProperty() {
        return maxNoOfMajorTicks;
    }

    public final int getMaxNoOfMinorTicks() {
        return maxNoOfMinorTicks.get();
    }

    public final void setMaxNoOfMinorTicks(final int MAX_NO_OF_MINOR_TICKS) {
        maxNoOfMinorTicks.set(MAX_NO_OF_MINOR_TICKS);
    }

    public final IntegerProperty maxNoOfMinorTicksProperty() {
        return maxNoOfMinorTicks;
    }

    public final int getMajorTickSpacing() {
        return majorTickSpacing.get();
    }

    public final void setMajorTickSpacing(final int MAJOR_TICKSPACING) {
        majorTickSpacing.set(MAJOR_TICKSPACING);
    }

    public final IntegerProperty majorTickSpacingProperty() {
        return majorTickSpacing;
    }

    public final int getMinorTickSpacing() {
        return minorTickSpacing.get();
    }

    public final void setMinorTickSpacing(final int MINOR_TICKSPACING) {
        minorTickSpacing.set(MINOR_TICKSPACING);
    }

    public final IntegerProperty minorTickSpacingProperty() {
        return minorTickSpacing;
    }

    public final Trend getTrend() {
        return trend.get();
    }

    public final void setTrend(final Trend TREND) {
        trend.set(TREND);
    }

    public final ObjectProperty<Trend> trendProperty() {
        return trend;
    }

    public final boolean isNiceScaling() {
        return niceScaling.get();
    }

    public final void setNiceScaling(final boolean NICE_SCALING) {
        niceScaling.set(NICE_SCALING);
    }

    public final BooleanProperty niceScalingProperty() {
        return niceScaling;
    }

    public final List<Section> getSections() {
        final List<Section> SECTIONS_COPY = new ArrayList<>(sections.size());
        SECTIONS_COPY.addAll(sections);
        return SECTIONS_COPY;
    }

    public final void setSections(final Section... SECTION_ARRAY) {
        sections.clear();
        for (final Section SECTION : SECTION_ARRAY) {
            sections.add(new Section(SECTION.getStart(), SECTION.getStop(), SECTION.getColor(), SECTION.getText()));
        }
        fireModelEvent();
    }

    public final void setSections(final List<Section> SECTIONS) {
        sections.clear();
        for (final Section SECTION : SECTIONS) {
            sections.add(new Section(SECTION.getStart(), SECTION.getStop(), SECTION.getColor(), SECTION.getText()));
        }
        fireModelEvent();
    }

    public final void addSection(final Section SECTION) {
        sections.add(SECTION);
        fireModelEvent();
    }

    public final void removeSection(final Section SECTION) {
        if (sections.contains(SECTION)) {
            sections.remove(SECTION);
        }
        fireModelEvent();
    }

    public final void resetSections() {
        sections.clear();
        fireModelEvent();
    }

    public final List<Section> getAreas() {
        final List<Section> AREAS_COPY = new ArrayList<>(areas.size());
        AREAS_COPY.addAll(areas);
        return AREAS_COPY;
    }

    public final void setAreas(final Section... AREA_ARRAY) {
        areas.clear();
        for (final Section AREA : AREA_ARRAY) {
            areas.add(new Section(AREA.getStart(), AREA.getStop(), AREA.getColor(), AREA.getText()));
        }
        fireModelEvent();
    }

    public final void setAreas(final List<Section> AREAS) {
        areas.clear();
        for (final Section AREA : AREAS) {
            areas.add(new Section(AREA.getStart(), AREA.getStop(), AREA.getColor(), AREA.getText()));
        }
        fireModelEvent();
    }

    public final void addArea(final Section AREA) {
        areas.add(AREA);
        fireModelEvent();
    }

    public final void removeArea(final Section AREA) {
        if (areas.contains(AREA)) {
            areas.remove(AREA);
        }
        fireModelEvent();
    }

    public final void resetAreas() {
        areas.clear();
        fireModelEvent();
    }

    public final List<Indicator> getIndicators() {
        final List<Indicator> INDICATORS_COPY = new ArrayList<>(indicators.size());
        INDICATORS_COPY.addAll(indicators);
        return INDICATORS_COPY;
    }

    public final void setIndicators(final Indicator... INDICATOR_ARRAY) {
        indicators.clear();
        for (final Indicator INDICATOR : INDICATOR_ARRAY) {
            indicators.add(new Indicator(INDICATOR.getIndicatorValue(), INDICATOR.getIndicatorColor(), INDICATOR.getIndicatorText(), INDICATOR.isVisible()));
        }
        fireModelEvent();
    }

    public final void setIndicators(final List<Indicator> INDICATORS) {
        indicators.clear();
        for (final Indicator INDICATOR : INDICATORS) {
            indicators.add(new Indicator(INDICATOR.getIndicatorValue(), INDICATOR.getIndicatorColor(), INDICATOR.getIndicatorText(), INDICATOR.isVisible()));
        }
        fireModelEvent();
    }

    public final void addIndicator(final Indicator INDICATOR) {
        indicators.add(INDICATOR);
        fireModelEvent();
    }

    public final void removeIndicator(final Indicator INDICATOR) {
        if (indicators.contains(INDICATOR)) {
            indicators.remove(INDICATOR);
        }
        fireModelEvent();
    }

    public final void resetIndicators() {
        indicators.clear();
        fireModelEvent();
    }

    /**
     * Calculate and update values for major and minor tick spacing and nice
     * minimum and maximum values on the axis.
     */
    protected void calcRange(final double ANGLE_RANGE) {
        if (isNiceScaling()) {
            niceRange.set(calcNiceNumber(range.doubleValue(), false));
            majorTickSpacing.set((int) (calcNiceNumber(niceRange.get() / (getMaxNoOfMajorTicks() - 1), true)));
            niceMinValue.set(Math.floor(getMinValue() / majorTickSpacing.doubleValue()) * majorTickSpacing.doubleValue());
            niceMaxValue.set(Math.ceil(getMaxValue() / majorTickSpacing.doubleValue()) * majorTickSpacing.doubleValue());
            minorTickSpacing.set((int) calcNiceNumber((double) getMajorTickSpacing() / ((double) getMaxNoOfMajorTicks() - 1), true));
            minValue.set(niceMinValue.get());
            maxValue.set(niceMaxValue.get());
            range.set(niceMaxValue.get() - niceMinValue.get());
        } else {
            niceRange.set(getMaxValue() - getMinValue());
            niceMinValue.set(getMinValue());
            niceMaxValue.set(getMaxValue());
            range.set(getMaxValue() - getMinValue());
        }
    }

    /**
     * Returns a "nice" number approximately equal to the range.
     * Rounds the number if ROUND == true.
     * Takes the ceiling if ROUND = false.
     *
     * @param RANGE the value range (maxValue - minValue)
     * @param ROUND whether to round the result or ceil
     * @return a "nice" number to be used for the value range
     */
    private static double calcNiceNumber(final double RANGE, final boolean ROUND) {
        final double EXPONENT = Math.floor(Math.log10(RANGE));   // exponent of range
        final double FRACTION = RANGE / Math.pow(10, EXPONENT);  // fractional part of range

        // nice, rounded fraction
        final double NICE_FRACTION;

        if (ROUND) {
            if (FRACTION < 1.5) {
                NICE_FRACTION = 1;
            } else if (FRACTION < 3) {
                NICE_FRACTION = 2;
            } else if (FRACTION < 7) {
                NICE_FRACTION = 5;
            } else {
                NICE_FRACTION = 10;
            }
        } else {
            if (Double.compare(FRACTION, 1) <= 0) {
                NICE_FRACTION = 1;
            } else if (Double.compare(FRACTION, 2) <= 0) {
                NICE_FRACTION = 2;
            } else if (Double.compare(FRACTION, 5) <= 0) {
                NICE_FRACTION = 5;
            } else {
                NICE_FRACTION = 10;
            }
        }
        return NICE_FRACTION * Math.pow(10, EXPONENT);
    }

}
