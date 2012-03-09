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

import jfxtras.labs.scene.control.gauge.Gauge.NumberSystem;
import jfxtras.labs.scene.control.gauge.Gauge.Trend;

import java.util.List;


/**
 * Created by
 * User: hansolo
 * Date: 02.01.12
 * Time: 15:50
 */
public class GaugeModelBuilder {
    private GaugeModel gaugeModel;

    public final GaugeModelBuilder create() {
        gaugeModel = new GaugeModel();
        return this;
    }

    public final GaugeModelBuilder value(final double VALUE) {
        gaugeModel.setValue(VALUE);
        return this;
    }

    public final GaugeModelBuilder valueAnimationEnabled(final boolean VALUE_ANIMATION_ENABLED) {
        gaugeModel.setValueAnimationEnabled(VALUE_ANIMATION_ENABLED);
        return this;
    }

    public final GaugeModelBuilder animationDuration(final double ANIMATION_DURATION) {
        gaugeModel.setAnimationDuration(ANIMATION_DURATION);
        return this;
    }

    public final GaugeModelBuilder minValue(final double MIN_VALUE) {
        gaugeModel.setMinValue(MIN_VALUE);
        return this;
    }

    public final GaugeModelBuilder maxValue(final double MAX_VALUE) {
        gaugeModel.setMaxValue(MAX_VALUE);
        return this;
    }

    public final GaugeModelBuilder threshold(final double THRESHOLD) {
        gaugeModel.setThreshold(THRESHOLD);
        return this;
    }

    public final GaugeModelBuilder thresholdBehaviorInverted(final boolean THRESHOLD_BEHAVIOR_INVERTED) {
        gaugeModel.setThresholdBehaviorInverted(THRESHOLD_BEHAVIOR_INVERTED);
        return this;
    }

    public final GaugeModelBuilder title(final String TITLE) {
        gaugeModel.setTitle(TITLE);
        return this;
    }

    public final GaugeModelBuilder unit(final String UNIT) {
        gaugeModel.setUnit(UNIT);
        return this;
    }

    public final GaugeModelBuilder lcdValueCoupled(final boolean LCD_VALUE_COUPLED) {
        gaugeModel.setLcdValueCoupled(LCD_VALUE_COUPLED);
        return this;
    }

    public final GaugeModelBuilder lcdThreshold(final double LCD_THRESHOLD) {
        gaugeModel.setLcdThreshold(LCD_THRESHOLD);
        return this;
    }

    public final GaugeModelBuilder lcdThresholdBehaviorInverted(final boolean LCD_THRESHOLD_BEHAVIOR_INVERTED) {
        gaugeModel.setLcdThresholdBehaviorInverted(LCD_THRESHOLD_BEHAVIOR_INVERTED);
        return this;
    }

    public final GaugeModelBuilder lcdUnitString(final String LCD_UNIT_STRING) {
        gaugeModel.setLcdUnit(LCD_UNIT_STRING);
        return this;
    }

    public final GaugeModelBuilder lcdNumberSystem(final NumberSystem LCD_NUMBER_SYSTEM) {
        gaugeModel.setLcdNumberSystem(LCD_NUMBER_SYSTEM);
        return this;
    }

    public final GaugeModelBuilder maxNoOfMajorTicks(final int MAX_NO_OF_MAJOR_TICKS) {
        gaugeModel.setMaxNoOfMajorTicks(MAX_NO_OF_MAJOR_TICKS);
        return this;
    }

    public final GaugeModelBuilder maxNoOfMinorTicks(final int MAX_NO_OF_MINOR_TICKS) {
        gaugeModel.setMaxNoOfMinorTicks(MAX_NO_OF_MINOR_TICKS);
        return this;
    }

    public final GaugeModelBuilder majorTickSpacing(final int MAJOR_TICKSPACING) {
        gaugeModel.setMajorTickSpacing(MAJOR_TICKSPACING);
        return this;
    }

    public final GaugeModelBuilder minorTickSpacing(final int MINOR_TICKSPACING) {
        gaugeModel.setMinorTickSpacing(MINOR_TICKSPACING);
        return this;
    }

    public final GaugeModelBuilder trend(final Trend TREND) {
        gaugeModel.setTrend(TREND);
        return this;
    }

    public final GaugeModelBuilder niceScaling(final boolean NICE_SCALING) {
        gaugeModel.setNiceScaling(NICE_SCALING);
        return this;
    }

    public final GaugeModelBuilder sections(final Section... SECTION_ARRAY) {
        gaugeModel.setSections(SECTION_ARRAY);
        return this;
    }

    public final GaugeModelBuilder sections(final List<Section> SECTIONS) {
        gaugeModel.setSections(SECTIONS);
        return this;
    }

    public final GaugeModelBuilder areas(final Section... AREAS_ARRAY) {
        gaugeModel.setAreas(AREAS_ARRAY);
        return this;
    }

    public final GaugeModelBuilder areas(final List<Section> AREAS) {
        gaugeModel.setAreas(AREAS);
        return this;
    }

    public final GaugeModelBuilder markers(final Marker... MARKER_ARRAY) {
        gaugeModel.setMarkers(MARKER_ARRAY);
        return this;
    }

    public final GaugeModelBuilder markers(final List<Marker> MARKERS) {
        gaugeModel.setMarkers(MARKERS);
        return this;
    }

    public final GaugeModel build() {
        return gaugeModel;
    }
}
