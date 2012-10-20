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

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import jfxtras.labs.scene.control.gauge.Gauge.NumberSystem;
import jfxtras.labs.scene.control.gauge.Gauge.Trend;

import java.util.List;


/**
 * Created by
 * User: hansolo
 * Date: 02.01.12
 * Time: 17:14
 */
public class GaugeModel {
    private DoubleProperty               value;
    private DoubleProperty               realValue;
    private BooleanProperty              valueAnimationEnabled;
    private DoubleProperty               animationDuration;
    private DoubleProperty               redrawTolerance;
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
    private ObjectProperty<LinearScale>  scale;
    private ObjectProperty<Trend>        trend;
    private ObservableList<Section>      sections;
    private ObservableList<Section>      areas;
    private ObservableList<Section>      tickMarkSections;
    private ObservableList<Marker>       markers;
    private BooleanProperty              endlessMode;


    // ******************** Constructors **************************************
    public GaugeModel() {
        value                           = new SimpleDoubleProperty(0);
        realValue                       = new SimpleDoubleProperty(0);
        valueAnimationEnabled           = new SimpleBooleanProperty(true);
        animationDuration               = new SimpleDoubleProperty(800);
        redrawTolerance                 = new SimpleDoubleProperty(0);
        minMeasuredValue                = new SimpleDoubleProperty(100);
        maxMeasuredValue                = new SimpleDoubleProperty(0);
        threshold                       = new SimpleDoubleProperty(50);
        thresholdBehaviorInverted       = new SimpleBooleanProperty(false);
        thresholdExceeded               = new SimpleBooleanProperty(false);
        title                           = new SimpleStringProperty("");
        unit                            = new SimpleStringProperty("");
        lcdValue                        = new SimpleDoubleProperty(0);
        lcdValueCoupled                 = new SimpleBooleanProperty(true);
        lcdThreshold                    = new SimpleDoubleProperty(50);
        lcdThresholdBehaviorInverted    = new SimpleBooleanProperty(false);
        lcdUnit                         = new SimpleStringProperty("");
        lcdNumberSystem                 = new SimpleObjectProperty<NumberSystem>(NumberSystem.DECIMAL);
        scale                           = new SimpleObjectProperty<LinearScale>(new LinearScale(0, 100));
        trend                           = new SimpleObjectProperty<Trend>(Trend.UNKNOWN);
        sections                        = FXCollections.observableArrayList();
        areas                           = FXCollections.observableArrayList();
        tickMarkSections                = FXCollections.observableArrayList();
        markers                         = FXCollections.observableArrayList();
        endlessMode                     = new SimpleBooleanProperty(false);

        sections.addListener(new InvalidationListener() {
            @Override public void invalidated(Observable ov) {
                fireGaugeModelEvent();
            }
        });
        areas.addListener(new InvalidationListener() {
            @Override public void invalidated(Observable ov) {
                fireGaugeModelEvent();
            }
        });
        tickMarkSections.addListener(new InvalidationListener() {
            @Override public void invalidated(Observable ov) {
                fireGaugeModelEvent();
            }
        });
        markers.addListener(new InvalidationListener() {
            @Override public void invalidated(Observable ov) {
                fireGaugeModelEvent();
            }
        });
    }


    // ******************** Event handling ************************************
    public final ObjectProperty<EventHandler<GaugeModelEvent>> onGaugeModelEventProperty() {
        return onGaugeModelEvent;
    }

    public final void setOnGaugeModelEvent(final EventHandler<GaugeModelEvent> HANDLER) {
        onGaugeModelEventProperty().set(HANDLER);
    }

    public final EventHandler<GaugeModelEvent> getOnGaugeModelEvent() {
        return onGaugeModelEventProperty().get();
    }

    private ObjectProperty<EventHandler<GaugeModelEvent>> onGaugeModelEvent = new SimpleObjectProperty<EventHandler<GaugeModelEvent>>();

    public void fireGaugeModelEvent() {
        final EventHandler<GaugeModelEvent> MODEL_EVENT_HANDLER = getOnGaugeModelEvent();
        if (MODEL_EVENT_HANDLER != null) {
            final GaugeModelEvent GAUGE_MODEL_EVENT = new GaugeModelEvent();
            MODEL_EVENT_HANDLER.handle(GAUGE_MODEL_EVENT);
        }
    }


    // ******************** Methods *******************************************
    public final double getValue() {
        return value.get();
    }

    public final void setValue(final double VALUE) {
        if (isEndlessMode()) {
            value.set(VALUE % getRange());
            realValue.set(VALUE);
        } else {
            value.set(clamp(scale.get().getNiceMinValue(), scale.get().getNiceMaxValue(), VALUE));
            realValue.set(value.get());
        }
        fireGaugeModelEvent();
    }

    public final DoubleProperty valueProperty() {
        return value;
    }

    public final double getRealValue() {
        return realValue.get();
    }

    public final ReadOnlyDoubleProperty realValueProperty() {
        return realValue;
    }

    public final boolean isValueAnimationEnabled() {
        return valueAnimationEnabled.get();
    }

    public final void setValueAnimationEnabled(final boolean VALUE_ANIMATION_ENABLED) {
        valueAnimationEnabled.set(VALUE_ANIMATION_ENABLED);
        fireGaugeModelEvent();
    }

    public final BooleanProperty valueAnimationEnabledProperty() {
        return valueAnimationEnabled;
    }

    public final double getAnimationDuration() {
        return animationDuration.get();
    }

    public final void setAnimationDuration(final double ANIMATION_DURATION) {
        animationDuration.set(ANIMATION_DURATION);
        fireGaugeModelEvent();
    }

    public final DoubleProperty animationDurationProperty() {
        return animationDuration;
    }

    public final double getRedrawTolerance() {
            return redrawTolerance.get();
        }

    public final void setRedrawTolerance(final double REDRAW_TOLERANCE) {
        redrawTolerance.set(clamp(0.0, 1.0, REDRAW_TOLERANCE));
    }

    public final DoubleProperty redrawToleranceProperty() {
        return redrawTolerance;
    }

    public final double getRedrawToleranceValue() {
        return redrawToleranceProperty().multiply(rangeProperty()).doubleValue();
    }

    public final double getMinValue() {
        return scale.get().getMinValue();
    }

    public final void setMinValue(final double MIN_VALUE) {
        scale.get().setMinValue(MIN_VALUE);
        scale.get().setUncorrectedMinValue(MIN_VALUE);
        fireGaugeModelEvent();
    }

    public final ReadOnlyDoubleProperty minValueProperty() {
        return scale.get().minValueProperty();
    }

    public final double getUncorrectedMinValue() {
        return scale.get().getUncorrectedMinValue();
    }

    public final double getMaxValue() {
        return scale.get().getMaxValue();
    }

    public final void setMaxValue(final double MAX_VALUE) {
        scale.get().setMaxValue(MAX_VALUE);
        scale.get().setUncorrectedMaxValue(MAX_VALUE);
        fireGaugeModelEvent();
    }

    public final ReadOnlyDoubleProperty maxValueProperty() {
        return scale.get().maxValueProperty();
    }

    public final double getUncorrectedMaxValue() {
        return scale.get().getUncorrectedMaxValue();
    }

    public final double getRange() {
        return scale.get().getRange();
    }

    public final ReadOnlyDoubleProperty rangeProperty() {
        return scale.get().rangeProperty();
    }

    public final double getMinMeasuredValue() {
        return minMeasuredValue.get();
    }

    public final void setMinMeasuredValue(final double MIN_MEASURED_VALUE) {
        minMeasuredValue.set(MIN_MEASURED_VALUE);
        fireGaugeModelEvent();
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
        fireGaugeModelEvent();
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
        threshold.set(Double.compare(THRESHOLD, scale.get().getNiceMinValue()) < 0 ? scale.get().getNiceMinValue() : (Double.compare(THRESHOLD, scale.get().getNiceMaxValue()) > 0 ? scale.get().getNiceMaxValue() : THRESHOLD));
        fireGaugeModelEvent();
    }

    public final DoubleProperty thresholdProperty() {
        return threshold;
    }

    public final boolean isThresholdBehaviorInverted() {
        return thresholdBehaviorInverted.get();
    }

    public final void setThresholdBehaviorInverted(final boolean THRESHOLD_BEHAVIOR_INVERTED) {
        thresholdBehaviorInverted.set(THRESHOLD_BEHAVIOR_INVERTED);
        fireGaugeModelEvent();
    }

    public final BooleanProperty thresholdBehaviorInvertedProperty() {
        return thresholdBehaviorInverted;
    }

    public final boolean isThresholdExceeded() {
        return thresholdExceeded.get();
    }

    public final void setThresholdExceeded(final boolean THRESHOLD_EXCEEDED) {
        thresholdExceeded.set(THRESHOLD_EXCEEDED);
        fireGaugeModelEvent();
    }

    public final BooleanProperty thresholdExceededProperty() {
        return thresholdExceeded;
    }

    public final String getTitle() {
        return title.get();
    }

    public final void setTitle(final String TITLE) {
        title.set(TITLE);
        fireGaugeModelEvent();
    }

    public final StringProperty titleProperty() {
        return title;
    }

    public final String getUnit() {
        return unit.get();
    }

    public final void setUnit(final String UNIT) {
        unit.set(UNIT);
        fireGaugeModelEvent();
    }

    public final StringProperty unitProperty() {
        return unit;
    }

    public final double getLcdValue() {
        return lcdValue.get();
    }

    public final void setLcdValue(final double LCD_VALUE) {
        lcdValue.set(LCD_VALUE);
        fireGaugeModelEvent();
    }

    public final DoubleProperty lcdValueProperty() {
        return lcdValue;
    }

    public final boolean isLcdValueCoupled() {
        return lcdValueCoupled.get();
    }

    public final void setLcdValueCoupled(final boolean LCD_VALUE_COUPLED) {
        lcdValueCoupled.set(LCD_VALUE_COUPLED);
        fireGaugeModelEvent();
    }

    public final BooleanProperty lcdValueCoupledProperty() {
        return lcdValueCoupled;
    }

    public final double getLcdThreshold() {
        return lcdThreshold.get();
    }

    public final void setLcdThreshold(final double LCD_THRESHOLD) {
        lcdThreshold.set(LCD_THRESHOLD);
        fireGaugeModelEvent();
    }

    public final DoubleProperty lcdThresholdProperty() {
        return lcdThreshold;
    }

    public final boolean isLcdThresholdBehaviorInverted() {
        return lcdThresholdBehaviorInverted.get();
    }

    public final void setLcdThresholdBehaviorInverted(final boolean LCD_THRESHOLD_BEHAVIOR_INVERTED) {
        lcdThresholdBehaviorInverted.set(LCD_THRESHOLD_BEHAVIOR_INVERTED);
        fireGaugeModelEvent();
    }

    public final BooleanProperty lcdThresholdBehaviorInvertedProperty() {
        return lcdThresholdBehaviorInverted;
    }

    public final String getLcdUnit() {
        return lcdUnit.get();
    }

    public final void setLcdUnit(final String LCD_UNIT_STRING) {
        lcdUnit.set(LCD_UNIT_STRING);
        fireGaugeModelEvent();
    }

    public final StringProperty lcdUnitProperty() {
        return lcdUnit;
    }

    public final NumberSystem getLcdNumberSystem() {
        return lcdNumberSystem.get();
    }

    public final void setLcdNumberSystem(final NumberSystem LCD_NUMBER_SYSTEM) {
        lcdNumberSystem.set(LCD_NUMBER_SYSTEM);
        fireGaugeModelEvent();
    }

    public final ObjectProperty lcdNumberSystemProperty() {
        return lcdNumberSystem;
    }

    public final int getMaxNoOfMajorTicks() {
        return scale.get().getMaxNoOfMajorTicks();
    }

    public final void setMaxNoOfMajorTicks(final int MAX_NO_OF_MAJOR_TICKS) {
        scale.get().setMaxNoOfMajorTicks(MAX_NO_OF_MAJOR_TICKS);
        fireGaugeModelEvent();
    }

    public final IntegerProperty maxNoOfMajorTicksProperty() {
        return scale.get().maxNoOfMajorTicksProperty();
    }

    public final int getMaxNoOfMinorTicks() {
        return scale.get().getMaxNoOfMinorTicks();
    }

    public final void setMaxNoOfMinorTicks(final int MAX_NO_OF_MINOR_TICKS) {
        scale.get().setMaxNoOfMinorTicks(MAX_NO_OF_MINOR_TICKS);
        fireGaugeModelEvent();
    }

    public final IntegerProperty maxNoOfMinorTicksProperty() {
        return scale.get().maxNoOfMinorTicksProperty();
    }

    public final double getMajorTickSpacing() {
        return scale.get().getMajorTickSpacing();
    }

    public final void setMajorTickSpacing(final double MAJOR_TICK_SPACING) {
        scale.get().setMajorTickSpacing(MAJOR_TICK_SPACING);
    }

    public final DoubleProperty majorTickSpacingProperty() {
        return scale.get().majorTickSpacingProperty();
    }

    public final double getMinorTickSpacing() {
        return scale.get().getMinorTickSpacing();
    }

    public final void setMinorTickSpacing(final double MINOR_TICK_SPACING) {
        scale.get().setMinorTickSpacing(MINOR_TICK_SPACING);
    }

    public final DoubleProperty minorTickSpacingProperty() {
        return scale.get().minorTickSpacingProperty();
    }

    public final Trend getTrend() {
        return trend.get();
    }

    public final void setTrend(final Trend TREND) {
        trend.set(TREND);
        fireGaugeModelEvent();
    }

    public final ObjectProperty<Trend> trendProperty() {
        return trend;
    }

    public final boolean isNiceScaling() {
        return scale.get().isNiceScaling();
    }

    public final void setNiceScaling(final boolean NICE_SCALING) {
        scale.get().setNiceScaling(NICE_SCALING);
        fireGaugeModelEvent();
    }

    public final BooleanProperty niceScalingProperty() {
        return scale.get().niceScalingProperty();
    }

    public final boolean isTightScale() {
        return scale.get().isTightScale();
    }

    public final void setTightScale(final boolean TIGHT_SCALE) {
        scale.get().setTightScale(TIGHT_SCALE);
    }

    public final BooleanProperty tightScaleProperty() {
        return scale.get().tightScaleProperty();
    }

    public final double getTightScaleOffset() {
        return scale.get().getTightScaleOffset();
    }

    public final boolean isLargeNumberScale() {
        return scale.get().isLargeNumberScale();
    }

    public final void setLargeNumberScale(final boolean LARGE_NUMBER_SCALE) {
        scale.get().setLargeNumberScale(LARGE_NUMBER_SCALE);
    }

    public final BooleanProperty largeNumberScaleProperty() {
        return scale.get().largeNumberScaleProperty();
    }

    public final boolean isLastLabelVisible() {
        return scale.get().isLastLabelVisible();
    }

    public final void setLastLabelVisible(final boolean LAST_LABEL_VISIBLE) {
        scale.get().setLastLabelVisible(LAST_LABEL_VISIBLE);
    }

    public final BooleanProperty lastLabelVisibleProperty() {
        return scale.get().lastLabelVisibleProperty();
    }

    public final ObservableList<Section> getSections() {
        return sections;
    }

    public final void setSections(final Section... SECTION_ARRAY) {
        sections.clear();
        for (final Section SECTION : SECTION_ARRAY) {
            sections.add(new Section(SECTION.getStart(), SECTION.getStop(), SECTION.getColor(), SECTION.getText()));
        }
        fireGaugeModelEvent();
    }

    public final void setSections(final List<Section> SECTIONS) {
        sections.clear();
        for (final Section SECTION : SECTIONS) {
            sections.add(new Section(SECTION.getStart(), SECTION.getStop(), SECTION.getColor(), SECTION.getText()));
        }
        fireGaugeModelEvent();
    }

    public final void addSection(final Section SECTION) {
        sections.add(new Section(SECTION.getStart(), SECTION.getStop(), SECTION.getColor(), SECTION.getText()));
        fireGaugeModelEvent();
    }

    public final void removeSection(final Section SECTION) {
        for (Section section : sections) {
            if (section.equals(SECTION)) {
                sections.remove(section);
                break;
            }
        }
        fireGaugeModelEvent();
    }

    public final void resetSections() {
        sections.clear();
        fireGaugeModelEvent();
    }

    public final ObservableList<Section> getAreas() {
        return areas;
    }

    public final void setAreas(final Section... AREA_ARRAY) {
        areas.clear();
        for (final Section AREA : AREA_ARRAY) {
            areas.add(new Section(AREA.getStart(), AREA.getStop(), AREA.getColor(), AREA.getText()));
        }
        fireGaugeModelEvent();
    }

    public final void setAreas(final List<Section> AREAS) {
        areas.clear();
        for (final Section AREA : AREAS) {
            areas.add(new Section(AREA.getStart(), AREA.getStop(), AREA.getColor(), AREA.getText()));
        }
        fireGaugeModelEvent();
    }

    public final void addArea(final Section AREA) {
        areas.add(new Section(AREA.getStart(), AREA.getStop(), AREA.getColor(), AREA.getText()));
        fireGaugeModelEvent();
    }

    public final void removeArea(final Section AREA) {
        for (Section area : areas) {
            if (area.equals(AREA)) {
                areas.remove(area);
                break;
            }
        }
        fireGaugeModelEvent();
    }

    public final void resetAreas() {
        areas.clear();
        fireGaugeModelEvent();
    }

    public final ObservableList<Section> getTickMarkSections() {
        return tickMarkSections;
    }

    public final void setTickMarkSections(final Section... SECTIONS_ARRAY) {
        tickMarkSections.clear();
        for (final Section SECTION : SECTIONS_ARRAY) {
            tickMarkSections.add(new Section(SECTION.getStart(), SECTION.getStop(), SECTION.getColor(), SECTION.getText()));
        }
        fireGaugeModelEvent();
    }

    public final void setTickMarkSections(final List<Section> SECTIONS) {
        tickMarkSections.clear();
        for (final Section SECTION : SECTIONS) {
            tickMarkSections.add(new Section(SECTION.getStart(), SECTION.getStop(), SECTION.getColor(), SECTION.getText()));
        }
        fireGaugeModelEvent();
    }

    public final void addTickMarkSection(final Section SECTION) {
        tickMarkSections.add(new Section(SECTION.getStart(), SECTION.getStop(), SECTION.getColor(), SECTION.getText()));
        fireGaugeModelEvent();
    }

    public final void removeTickMarkSection(final Section SECTION) {
        for (Section section : tickMarkSections) {
            if (section.equals(SECTION)) {
                tickMarkSections.remove(section);
                break;
            }
        }
        fireGaugeModelEvent();
    }

    public final void resetTickMarkSections() {
        tickMarkSections.clear();
        fireGaugeModelEvent();
    }

    public final ObservableList<Marker> getMarkers() {
        return markers;
    }

    public final void setMarkers(final Marker... MARKER_ARRAY) {
        markers.clear();
        for (final Marker MARKER : MARKER_ARRAY) {
            markers.add(new Marker(MARKER.getValue(), MARKER.getColor(), MARKER.getText(), MARKER.isVisible()));
        }
        fireGaugeModelEvent();
    }

    public final void setMarkers(final List<Marker> MARKERS) {
        markers.clear();
        for (final Marker MARKER : MARKERS) {
            markers.add(new Marker(MARKER.getValue(), MARKER.getColor(), MARKER.getText(), MARKER.isVisible()));
        }
        fireGaugeModelEvent();
    }

    public final void addMarker(final Marker MARKER) {
        markers.add(new Marker(MARKER.getValue(), MARKER.getColor(), MARKER.getText(), MARKER.isVisible()));
        fireGaugeModelEvent();
    }

    public final void removeMarker(final Marker MARKER) {
        for (Marker marker : markers) {
            if (marker.equals(MARKER)) {
                markers.remove(marker);
                break;
            }
        }
        fireGaugeModelEvent();
    }

    public final void resetMarkers() {
        markers.clear();
        fireGaugeModelEvent();
    }

    public final boolean isEndlessMode() {
        return endlessMode.get();
    }

    public final void setEndlessMode(final boolean ENDLESS_MODE) {
        endlessMode.set(ENDLESS_MODE);
    }

    public final BooleanProperty endlessModeProperty() {
        return endlessMode;
    }


    // ******************** Utility methods ***********************************
    private double clamp(final double MIN, final double MAX, final double VALUE) {
        return VALUE < MIN ? MIN : (VALUE > MAX ? MAX : VALUE);
    }

    /**
     * Calculate and update values for major and minor tick spacing and nice
     * minimum and maximum values on the axis.
     */
    protected void calcRange() {
        if (getMinValue() < getMaxValue()) {
            if (scale.get().isTightScale()) {
                scale.get().calculateTight();
            } else {
                scale.get().calculateLoose();
            }
        }
    }


    // ******************** Internal Classes **********************************
    public class GaugeModelEvent extends Event {

        // ******************** Constructors **************************************
        public GaugeModelEvent() {
            super(new EventType<GaugeModelEvent>());
        }

        public GaugeModelEvent(final Object source, final EventTarget target) {
            super(source, target, new EventType<GaugeModelEvent>());
        }
    }
}
