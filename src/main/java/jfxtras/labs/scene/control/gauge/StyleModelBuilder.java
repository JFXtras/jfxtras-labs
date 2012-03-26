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
public class StyleModelBuilder {
    private StyleModel styleModel;

    public final StyleModelBuilder create() {
        styleModel = new StyleModel();
        return this;
    }

    public final StyleModelBuilder bargraph(final boolean BARGRAPH) {
        styleModel.setBargraph(BARGRAPH);
        return this;
    }

    public final StyleModelBuilder minMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE) {
        styleModel.setMinMeasuredValueVisible(MIN_MEASURED_VALUE_VISIBLE);
        return this;
    }

    public final StyleModelBuilder maxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE) {
        styleModel.setMaxMeasuredValueVisible(MAX_MEASURED_VALUE_VISIBLE);
        return this;
    }

    public final StyleModelBuilder thresholdVisible(final boolean THRESHOLD_VISIBLE) {
        styleModel.setThresholdVisible(THRESHOLD_VISIBLE);
        return this;
    }

    public final StyleModelBuilder thresholdColor(final Gauge.ThresholdColor THRESHOLD_COLOR) {
        styleModel.setThresholdColor(THRESHOLD_COLOR);
        return this;
    }

    public final StyleModelBuilder frameDesign(final Gauge.FrameDesign FRAME_DESIGN) {
        styleModel.setFrameDesign(FRAME_DESIGN);
        return this;
    }

    public final StyleModelBuilder frameVisible(final boolean FRAME_VISIBLE) {
        styleModel.setFrameVisible(FRAME_VISIBLE);
        return this;
    }

    public final StyleModelBuilder backgroundDesign(final Gauge.BackgroundDesign BACKGROUND_DESIGN) {
        styleModel.setBackgroundDesign(BACKGROUND_DESIGN);
        return this;
    }

    public final StyleModelBuilder backgroundVisible(final boolean BACKGROUND_VISIBLE) {
        styleModel.setBackgroundVisible(BACKGROUND_VISIBLE);
        return this;
    }

    public final StyleModelBuilder knobDesign(final Gauge.KnobDesign KNOB_DESIGN) {
        styleModel.setKnobDesign(KNOB_DESIGN);
        return this;
    }

    public final StyleModelBuilder knobColor(final Gauge.KnobColor KNOB_COLOR) {
        styleModel.setKnobColor(KNOB_COLOR);
        return this;
    }

    public final StyleModelBuilder postsVisible(final boolean POSTS_VISIBLE) {
        styleModel.setPostsVisible(POSTS_VISIBLE);
        return this;
    }

    public final StyleModelBuilder pointerType(final Gauge.PointerType POINTER_TYPE) {
        styleModel.setPointerType(POINTER_TYPE);
        return this;
    }

    public final StyleModelBuilder valueColor(final ColorDef VALUE_COLOR) {
        styleModel.setValueColor(VALUE_COLOR);
        return this;
    }

    public final StyleModelBuilder pointerGlowEnabled(final boolean POINTER_GLOW_ENABLED) {
        styleModel.setPointerGlowEnabled(POINTER_GLOW_ENABLED);
        return this;
    }

    public final StyleModelBuilder pointerShadowEnabled(final boolean POINTER_SHADOW_ENABLED) {
        styleModel.setPointerShadowEnabled(POINTER_SHADOW_ENABLED);
        return this;
    }

    public final StyleModelBuilder ledVisible(final boolean LED_VISIBLE) {
        styleModel.setLedVisible(LED_VISIBLE);
        return this;
    }

    public final StyleModelBuilder ledColor(final LedColor LED_COLOR) {
        styleModel.setLedColor(LED_COLOR);
        return this;
    }

    public final StyleModelBuilder userLedVisible(final boolean USER_LED_VISIBLE) {
        styleModel.setUserLedVisible(USER_LED_VISIBLE);
        return this;
    }

    public final StyleModelBuilder userLedColor(final LedColor USER_LED_COLOR) {
        styleModel.setUserLedColor(USER_LED_COLOR);
        return this;
    }

    public final StyleModelBuilder userLedOn(final boolean USER_LED_ON) {
        styleModel.setUserLedOn(USER_LED_ON);
        return this;
    }

    public final StyleModelBuilder userLedBlinking(final boolean USER_LED_BLINKING) {
        styleModel.setUserLedBlinking(USER_LED_BLINKING);
        return this;
    }

    public final StyleModelBuilder titleFont(final String TITLE_FONT) {
        styleModel.setTitleFont(TITLE_FONT);
        return this;
    }

    public final StyleModelBuilder unitfont(final String UNIT_FONT) {
        styleModel.setUnitFont(UNIT_FONT);
        return this;
    }

    public final StyleModelBuilder foregroundType(final Radial.ForegroundType FOREGROUND_TYPE) {
        styleModel.setForegroundType(FOREGROUND_TYPE);
        return this;
    }

    public final StyleModelBuilder foregroundVisible(final boolean FOREGROUND_VISIBLE) {
        styleModel.setForegroundVisible(FOREGROUND_VISIBLE);
        return this;
    }

    public final StyleModelBuilder lcdThresholdVisible(final boolean LCD_THRESHOLD_VISIBLE) {
        styleModel.setLcdThresholdVisible(LCD_THRESHOLD_VISIBLE);
        return this;
    }

    public final StyleModelBuilder lcdDesign(final LcdDesign LCD_Design) {
        styleModel.setLcdDesign(LCD_Design);
        return this;
    }

    public final StyleModelBuilder lcdVisible(final boolean LCD_VISIBLE) {
        styleModel.setLcdVisible(LCD_VISIBLE);
        return this;
    }

    public final StyleModelBuilder lcdUnitStringVisible(final boolean LCD_UNIT_STRING_VISIBLE) {
        styleModel.setLcdUnitVisible(LCD_UNIT_STRING_VISIBLE);
        return this;
    }

    public final StyleModelBuilder lcdDigitalFontEnabled(final boolean LCD_DIGITAL_FONT_ENABLED) {
        styleModel.setLcdDigitalFontEnabled(LCD_DIGITAL_FONT_ENABLED);
        return this;
    }

    public final StyleModelBuilder lcdNumberSystemVisible(final boolean NUMBER_SYSTEM_VISIBLE) {
        styleModel.setLcdNumberSystemVisible(NUMBER_SYSTEM_VISIBLE);
        return this;
    }

    public final StyleModelBuilder lcdUnitFont(final String UNIT_FONT) {
        styleModel.setLcdUnitFont(UNIT_FONT);
        return this;
    }

    public final StyleModelBuilder lcdTitleFont(final String TITLE_FONT) {
        styleModel.setLcdTitleFont(TITLE_FONT);
        return this;
    }

    public final StyleModelBuilder lcdDecimals(final int LCD_DECIMALS) {
        styleModel.setLcdDecimals(LCD_DECIMALS);
        return this;
    }

    public final StyleModelBuilder lcdBlinking(final boolean LCD_BLINKING) {
        styleModel.setLcdBlinking(LCD_BLINKING);
        return this;
    }

    public final StyleModelBuilder glowVisible(final boolean GLOW_VISIBLE) {
        styleModel.setGlowVisible(GLOW_VISIBLE);
        return this;
    }

    public final StyleModelBuilder glowOn(final boolean GLOW_ON) {
        styleModel.setGlowOn(GLOW_ON);
        return this;
    }

    public final StyleModelBuilder pulsatingGlow(final boolean PULSATING_GLOW) {
        styleModel.setPulsatingGlow(PULSATING_GLOW);
        return this;
    }

    public final StyleModelBuilder glowColor(final Color GLOW_COLOR) {
        styleModel.setGlowColor(GLOW_COLOR);
        return this;
    }

    public final StyleModelBuilder tickmarksVisible(final boolean TICKMARKS_VISIBLE) {
        styleModel.setTickmarksVisible(TICKMARKS_VISIBLE);
        return this;
    }

    public final StyleModelBuilder majorTickmarksVisible(final boolean MAJOR_TICKMARKS_VISIBLE) {
        styleModel.setMajorTicksVisible(MAJOR_TICKMARKS_VISIBLE);
        return this;
    }

    public final StyleModelBuilder majorTickmarkType(final TickmarkType TICKMARK_TYPE) {
        styleModel.setMajorTickmarkType(TICKMARK_TYPE);
        return this;
    }

    public final StyleModelBuilder minorTickmarksVisible(final boolean MINOR_TICKMARKS_VISIBLE) {
        styleModel.setMinorTicksVisible(MINOR_TICKMARKS_VISIBLE);
        return this;
    }

    public final StyleModelBuilder tickLabelsVisible(final boolean TICKLABELS_VISIBLE) {
        styleModel.setTickLabelsVisible(TICKLABELS_VISIBLE);
        return this;
    }

    public final StyleModelBuilder tickLabelOrientation(final Gauge.TicklabelOrientation TICKLABEL_ORIENTATION) {
        styleModel.setTickLabelOrientation(TICKLABEL_ORIENTATION);
        return this;
    }

    public final StyleModelBuilder tickLabelNumberFormat(final Gauge.NumberFormat TICKLABEL_NUMBER_FORMAT) {
        styleModel.setTickLabelNumberFormat(TICKLABEL_NUMBER_FORMAT);
        return this;
    }

    public final StyleModelBuilder tickmarkGlowEnabled(final boolean TICKMARK_GLOW_ENABLED) {
        styleModel.setTickmarkGlowEnabled(TICKMARK_GLOW_ENABLED);
        return this;
    }

    public final StyleModelBuilder tickmarkGlowColor(final Color TICKMARK_GLOW_COLOR) {
        styleModel.setTickmarkGlowColor(TICKMARK_GLOW_COLOR);
        return this;
    }

    public final StyleModelBuilder sectionsVisible(final boolean SECTIONS_VISIBLE) {
        styleModel.setSectionsVisible(SECTIONS_VISIBLE);
        return this;
    }

    public final StyleModelBuilder sectionsHighlighting(final boolean SECTIONS_HIGHLIGHTING) {
        styleModel.setSectionsHighlighting(SECTIONS_HIGHLIGHTING);
        return this;
    }

    public final StyleModelBuilder showSectionTickmarksOnly(final boolean SHOW_SECTION_TICKMARKS_ONLY) {
        styleModel.setShowSectionTickmarksOnly(SHOW_SECTION_TICKMARKS_ONLY);
        return this;
    }

    public final StyleModelBuilder areasVisible(final boolean AREAS_VISIBLE) {
        styleModel.setAreasVisible(AREAS_VISIBLE);
        return this;
    }

    public final StyleModelBuilder areasHighlighting(final boolean AREAS_HIGHLIGHTING) {
        styleModel.setAreasHighlighting(AREAS_HIGHLIGHTING);
        return this;
    }

    public final StyleModelBuilder markersVisible(final boolean MARKERS_VISIBLE) {
        styleModel.setMarkersVisible(MARKERS_VISIBLE);
        return this;
    }

    public final StyleModelBuilder textureColor(final Color TEXTURE_COLOR) {
        styleModel.setTextureColor(TEXTURE_COLOR);
        return this;
    }

    public final StyleModelBuilder simpleGradientBaseColor(final Color SIMPLE_GRADIENT_BASE_COLOR) {
        styleModel.setSimpleGradientBaseColor(SIMPLE_GRADIENT_BASE_COLOR);
        return this;
    }

    public final StyleModelBuilder titleVisible(final boolean TITLE_VISIBLE) {
        styleModel.setTitleVisible(TITLE_VISIBLE);
        return this;
    }

    public final StyleModelBuilder unitVisible(final boolean UNIT_VISIBLE) {
        styleModel.setUnitVisible(UNIT_VISIBLE);
        return this;
    }

    public final StyleModelBuilder trendVisible(final boolean TREND_VISIBLE) {
        styleModel.setTrendVisible(TREND_VISIBLE);
        return this;
    }

    public final StyleModelBuilder trendUpColor(final Color TREND_UP_COLOR) {
        styleModel.setTrendUpColor(TREND_UP_COLOR);
        return this;
    }

    public final StyleModelBuilder trendSteadyColor(final Color TREND_STEADY_COLOR) {
        styleModel.setTrendSteadyColor(TREND_STEADY_COLOR);
        return this;
    }

    public final StyleModelBuilder trendDownColor(final Color TREND_DOWN_COLOR) {
        styleModel.setTrendDownColor(TREND_DOWN_COLOR);
        return this;
    }

    public final StyleModel build() {
        return styleModel;
    }
}
