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

import java.util.List;


/**
 * Created by
 * User: hansolo
 * Date: 02.01.12
 * Time: 15:50
 */
public class ModelBuilder {
    private Model model;

    public final ModelBuilder create() {
        model = new Model();
        return this;
    }

    public final ModelBuilder value(final double VALUE) {
        model.setValue(VALUE);
        return this;
    }

    public final ModelBuilder valueAnimationEnabled(final boolean VALUE_ANIMATION_ENABLED) {
        model.setValueAnimationEnabled(VALUE_ANIMATION_ENABLED);
        return this;
    }

    public final ModelBuilder animationDuration(final double ANIMATION_DURATION) {
        model.setAnimationDuration(ANIMATION_DURATION);
        return this;
    }

    public final ModelBuilder minValue(final double MIN_VALUE) {
        model.setMinValue(MIN_VALUE);
        return this;
    }

    public final ModelBuilder maxValue(final double MAX_VALUE) {
        model.setMaxValue(MAX_VALUE);
        return this;
    }

    public final ModelBuilder threshold(final double THRESHOLD) {
        model.setThreshold(THRESHOLD);
        return this;
    }

    public final ModelBuilder thresholdBehaviorInverted(final boolean THRESHOLD_BEHAVIOR_INVERTED) {
        model.setThresholdBehaviorInverted(THRESHOLD_BEHAVIOR_INVERTED);
        return this;
    }

    public final ModelBuilder title(final String TITLE) {
        model.setTitle(TITLE);
        return this;
    }

    public final ModelBuilder unit(final String UNIT) {
        model.setUnit(UNIT);
        return this;
    }

    public final ModelBuilder lcdValueCoupled(final boolean LCD_VALUE_COUPLED) {
        model.setLcdValueCoupled(LCD_VALUE_COUPLED);
        return this;
    }

    public final ModelBuilder lcdThreshold(final double LCD_THRESHOLD) {
        model.setLcdThreshold(LCD_THRESHOLD);
        return this;
    }

    public final ModelBuilder lcdThresholdBehaviorInverted(final boolean LCD_THRESHOLD_BEHAVIOR_INVERTED) {
        model.setLcdThresholdBehaviorInverted(LCD_THRESHOLD_BEHAVIOR_INVERTED);
        return this;
    }

    public final ModelBuilder lcdUnitString(final String LCD_UNIT_STRING) {
        model.setLcdUnit(LCD_UNIT_STRING);
        return this;
    }

    public final ModelBuilder lcdNumberSystem(final NumberSystem LCD_NUMBER_SYSTEM) {
        model.setLcdNumberSystem(LCD_NUMBER_SYSTEM);
        return this;
    }

    public final ModelBuilder maxNoOfMajorTicks(final int MAX_NO_OF_MAJOR_TICKS) {
        model.setMaxNoOfMajorTicks(MAX_NO_OF_MAJOR_TICKS);
        return this;
    }

    public final ModelBuilder maxNoOfMinorTicks(final int MAX_NO_OF_MINOR_TICKS) {
        model.setMaxNoOfMinorTicks(MAX_NO_OF_MINOR_TICKS);
        return this;
    }

    public final ModelBuilder majorTickSpacing(final int MAJOR_TICKSPACING) {
        model.setMajorTickSpacing(MAJOR_TICKSPACING);
        return this;
    }

    public final ModelBuilder minorTickSpacing(final int MINOR_TICKSPACING) {
        model.setMinorTickSpacing(MINOR_TICKSPACING);
        return this;
    }

    public final ModelBuilder trend(final Trend TREND) {
        model.setTrend(TREND);
        return this;
    }

    public final ModelBuilder niceScaling(final boolean NICE_SCALING) {
        model.setNiceScaling(NICE_SCALING);
        return this;
    }

    public final ModelBuilder sections(final Section... SECTION_ARRAY) {
        model.setSections(SECTION_ARRAY);
        return this;
    }

    public final ModelBuilder sections(final List<Section> SECTIONS) {
        model.setSections(SECTIONS);
        return this;
    }

    public final ModelBuilder areas(final Section... AREAS_ARRAY) {
        model.setAreas(AREAS_ARRAY);
        return this;
    }

    public final ModelBuilder areas(final List<Section> AREAS) {
        model.setAreas(AREAS);
        return this;
    }

    public final ModelBuilder indicators(final Indicator... INDICATOR_ARRAY) {
        model.setIndicators(INDICATOR_ARRAY);
        return this;
    }

    public final ModelBuilder indicators(final List<Indicator> INDICATORS) {
        model.setIndicators(INDICATORS);
        return this;
    }

    public Model build() {
        return model;
    }
}
