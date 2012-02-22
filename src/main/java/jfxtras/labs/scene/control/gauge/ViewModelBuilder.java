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

import jfxtras.labs.scene.control.gauge.Gauge.TickmarkType;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 29.01.12
 * Time: 10:43
 */
public class ViewModelBuilder {
    private ViewModel viewModel;

    public final ViewModelBuilder create() {
        viewModel = new ViewModel();
        return this;
    }

    public final ViewModelBuilder bargraph(final boolean BARGRAPH) {
        viewModel.setBargraph(BARGRAPH);
        return this;
    }

    public final ViewModelBuilder minMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE) {
        viewModel.setMinMeasuredValueVisible(MIN_MEASURED_VALUE_VISIBLE);
        return this;
    }

    public final ViewModelBuilder maxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE) {
        viewModel.setMaxMeasuredValueVisible(MAX_MEASURED_VALUE_VISIBLE);
        return this;
    }

    public final ViewModelBuilder thresholdVisible(final boolean THRESHOLD_VISIBLE) {
        viewModel.setThresholdVisible(THRESHOLD_VISIBLE);
        return this;
    }

    public final ViewModelBuilder thresholdColor(final Gauge.ThresholdColor THRESHOLD_COLOR) {
        viewModel.setThresholdColor(THRESHOLD_COLOR);
        return this;
    }

    public final ViewModelBuilder frameDesign(final Gauge.FrameDesign FRAME_DESIGN) {
        viewModel.setFrameDesign(FRAME_DESIGN);
        return this;
    }

    public final ViewModelBuilder frameVisible(final boolean FRAME_VISIBLE) {
        viewModel.setFrameVisible(FRAME_VISIBLE);
        return this;
    }

    public final ViewModelBuilder backgroundDesign(final Gauge.BackgroundDesign BACKGROUND_DESIGN) {
        viewModel.setBackgroundDesign(BACKGROUND_DESIGN);
        return this;
    }

    public final ViewModelBuilder backgroundVisible(final boolean BACKGROUND_VISIBLE) {
        viewModel.setBackgroundVisible(BACKGROUND_VISIBLE);
        return this;
    }

    public final ViewModelBuilder knobDesign(final Gauge.KnobDesign KNOB_DESIGN) {
        viewModel.setKnobDesign(KNOB_DESIGN);
        return this;
    }

    public final ViewModelBuilder knobColor(final Gauge.KnobColor KNOB_COLOR) {
        viewModel.setKnobColor(KNOB_COLOR);
        return this;
    }

    public final ViewModelBuilder postsVisible(final boolean POSTS_VISIBLE) {
        viewModel.setPostsVisible(POSTS_VISIBLE);
        return this;
    }

    public final ViewModelBuilder pointerType(final Gauge.PointerType POINTER_TYPE) {
        viewModel.setPointerType(POINTER_TYPE);
        return this;
    }

    public final ViewModelBuilder valueColor(final ColorDef VALUE_COLOR) {
        viewModel.setValueColor(VALUE_COLOR);
        return this;
    }

    public final ViewModelBuilder pointerShadowVisible(final boolean POINTER_SHADOW_VISIBLE) {
        viewModel.setPointerShadowVisible(POINTER_SHADOW_VISIBLE);
        return this;
    }

    public final ViewModelBuilder ledVisible(final boolean LED_VISIBLE) {
        viewModel.setLedVisible(LED_VISIBLE);
        return this;
    }

    public final ViewModelBuilder ledColor(final LedColor LED_COLOR) {
        viewModel.setLedColor(LED_COLOR);
        return this;
    }

    public final ViewModelBuilder userLedVisible(final boolean USER_LED_VISIBLE) {
        viewModel.setUserLedVisible(USER_LED_VISIBLE);
        return this;
    }

    public final ViewModelBuilder userLedColor(final LedColor USER_LED_COLOR) {
        viewModel.setUserLedColor(USER_LED_COLOR);
        return this;
    }

    public final ViewModelBuilder userLedOn(final boolean USER_LED_ON) {
        viewModel.setUserLedOn(USER_LED_ON);
        return this;
    }

    public final ViewModelBuilder userLedBlinking(final boolean USER_LED_BLINKING) {
        viewModel.setUserLedBlinking(USER_LED_BLINKING);
        return this;
    }

    public final ViewModelBuilder foregroundType(final Radial.ForegroundType FOREGROUND_TYPE) {
        viewModel.setForegroundType(FOREGROUND_TYPE);
        return this;
    }

    public final ViewModelBuilder foregroundVisible(final boolean FOREGROUND_VISIBLE) {
        viewModel.setForegroundVisible(FOREGROUND_VISIBLE);
        return this;
    }

    public final ViewModelBuilder lcdThresholdVisible(final boolean LCD_THRESHOLD_VISIBLE) {
        viewModel.setLcdThresholdVisible(LCD_THRESHOLD_VISIBLE);
        return this;
    }

    public final ViewModelBuilder lcdDesign(final LcdDesign LCD_Design) {
        viewModel.setLcdDesign(LCD_Design);
        return this;
    }

    public final ViewModelBuilder lcdVisible(final boolean LCD_VISIBLE) {
        viewModel.setLcdVisible(LCD_VISIBLE);
        return this;
    }

    public final ViewModelBuilder lcdUnitStringVisible(final boolean LCD_UNIT_STRING_VISIBLE) {
        viewModel.setLcdUnitVisible(LCD_UNIT_STRING_VISIBLE);
        return this;
    }

    public final ViewModelBuilder lcdDigitalFontEnabled(final boolean LCD_DIGITAL_FONT_ENABLED) {
        viewModel.setLcdDigitalFontEnabled(LCD_DIGITAL_FONT_ENABLED);
        return this;
    }

    public final ViewModelBuilder lcdNumberSystemVisible(final boolean NUMBER_SYSTEM_VISIBLE) {
        viewModel.setLcdNumberSystemVisible(NUMBER_SYSTEM_VISIBLE);
        return this;
    }

    public final ViewModelBuilder lcdUnitFont(final String UNIT_FONT) {
        viewModel.setLcdUnitFont(UNIT_FONT);
        return this;
    }

    public final ViewModelBuilder lcdDecimals(final int LCD_DECIMALS) {
        viewModel.setLcdDecimals(LCD_DECIMALS);
        return this;
    }

    public final ViewModelBuilder lcdBlinking(final boolean LCD_BLINKING) {
        viewModel.setLcdBlinking(LCD_BLINKING);
        return this;
    }

    public final ViewModelBuilder glowVisible(final boolean GLOW_VISIBLE) {
        viewModel.setGlowVisible(GLOW_VISIBLE);
        return this;
    }

    public final ViewModelBuilder glowOn(final boolean GLOW_ON) {
        viewModel.setGlowOn(GLOW_ON);
        return this;
    }

    public final ViewModelBuilder pulsatingGlow(final boolean PULSATING_GLOW) {
        viewModel.setPulsatingGlow(PULSATING_GLOW);
        return this;
    }

    public final ViewModelBuilder glowColor(final Color GLOW_COLOR) {
        viewModel.setGlowColor(GLOW_COLOR);
        return this;
    }

    public final ViewModelBuilder tickmarksVisible(final boolean TICKMARKS_VISIBLE) {
        viewModel.setTickmarksVisible(TICKMARKS_VISIBLE);
        return this;
    }

    public final ViewModelBuilder majorTickmarksVisible(final boolean MAJOR_TICKMARKS_VISIBLE) {
        viewModel.setMajorTicksVisible(MAJOR_TICKMARKS_VISIBLE);
        return this;
    }

    public final ViewModelBuilder majorTickmarkType(final TickmarkType TICKMARK_TYPE) {
        viewModel.setMajorTickmarkType(TICKMARK_TYPE);
        return this;
    }

    public final ViewModelBuilder minorTickmarksVisible(final boolean MINOR_TICKMARKS_VISIBLE) {
        viewModel.setMinorTicksVisible(MINOR_TICKMARKS_VISIBLE);
        return this;
    }

    public final ViewModelBuilder tickLabelsVisible(final boolean TICKLABELS_VISIBLE) {
        viewModel.setTickLabelsVisible(TICKLABELS_VISIBLE);
        return this;
    }

    public final ViewModelBuilder tickLabelOrientation(final Gauge.TicklabelOrientation TICKLABEL_ORIENTATION) {
        viewModel.setTickLabelOrientation(TICKLABEL_ORIENTATION);
        return this;
    }

    public final ViewModelBuilder tickLabelNumberFormat(final Gauge.NumberFormat TICKLABEL_NUMBER_FORMAT) {
        viewModel.setTickLabelNumberFormat(TICKLABEL_NUMBER_FORMAT);
        return this;
    }

    public final ViewModelBuilder sectionsVisible(final boolean SECTIONS_VISIBLE) {
        viewModel.setSectionsVisible(SECTIONS_VISIBLE);
        return this;
    }

    public final ViewModelBuilder sectionsHighlighting(final boolean SECTIONS_HIGHLIGHTING) {
        viewModel.setSectionsHighlighting(SECTIONS_HIGHLIGHTING);
        return this;
    }

    public final ViewModelBuilder areasVisible(final boolean AREAS_VISIBLE) {
        viewModel.setAreasVisible(AREAS_VISIBLE);
        return this;
    }

    public final ViewModelBuilder areasHighlighting(final boolean AREAS_HIGHLIGHTING) {
        viewModel.setAreasHighlighting(AREAS_HIGHLIGHTING);
        return this;
    }

    public final ViewModelBuilder indicatorsVisible(final boolean INDICATORS_VISIBLE) {
        viewModel.setIndicatorsVisible(INDICATORS_VISIBLE);
        return this;
    }

    public final ViewModelBuilder textureColor(final Color TEXTURE_COLOR) {
        viewModel.setTextureColor(TEXTURE_COLOR);
        return this;
    }

    public final ViewModelBuilder simpleGradientBaseColor(final Color SIMPLE_GRADIENT_BASE_COLOR) {
        viewModel.setSimpleGradientBaseColor(SIMPLE_GRADIENT_BASE_COLOR);
        return this;
    }

    public final ViewModelBuilder titleVisible(final boolean TITLE_VISIBLE) {
        viewModel.setTitleVisible(TITLE_VISIBLE);
        return this;
    }

    public final ViewModelBuilder unitVisible(final boolean UNIT_VISIBLE) {
        viewModel.setUnitVisible(UNIT_VISIBLE);
        return this;
    }

    public final ViewModelBuilder trendVisible(final boolean TREND_VISIBLE) {
        viewModel.setTrendVisible(TREND_VISIBLE);
        return this;
    }

    public final ViewModelBuilder trendUpColor(final Color TREND_UP_COLOR) {
        viewModel.setTrendUpColor(TREND_UP_COLOR);
        return this;
    }

    public final ViewModelBuilder trendSteadyColor(final Color TREND_STEADY_COLOR) {
        viewModel.setTrendSteadyColor(TREND_STEADY_COLOR);
        return this;
    }

    public final ViewModelBuilder trendDownColor(final Color TREND_DOWN_COLOR) {
        viewModel.setTrendDownColor(TREND_DOWN_COLOR);
        return this;
    }

    public ViewModel build() {
        return viewModel;
    }
}
