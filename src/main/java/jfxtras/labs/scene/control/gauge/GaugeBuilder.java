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

import javafx.scene.paint.Color;

import java.util.List;


/**
 * Created by
 * User: hansolo
 * Date: 05.03.12
 * Time: 10:03
 */
public class GaugeBuilder<T extends Gauge> {
    // ******************** Variable definitions ******************************
    private GaugeType  gaugeType  = GaugeType.RADIAL;
    private GaugeModel gaugeModel = new GaugeModel();
    private StyleModel styleModel = new StyleModel();

    // ******************** Enum definitions **********************************
    public enum GaugeType {
        LCD,
        LINEAR,
        RADIAL_HALF_N,
        RADIAL_HALF_S,
        RADIAL_QUARTER_N,
        RADIAL_QUARTER_E,
        RADIAL_QUARTER_S,
        RADIAL_QUARTER_W,
        RADIAL
    }


    // ******************** Methods *******************************************
    public static final GaugeBuilder create(final GaugeType GAUGE_TYPE) {
        return new GaugeBuilder();
    }

    public final Gauge build() {
        switch (gaugeType) {
            case LCD:
                return (T) new Lcd(gaugeModel, styleModel);
            case LINEAR:
                return (T) new Linear(gaugeModel, styleModel);
            case RADIAL_HALF_N:
                return (T) new RadialHalfN(gaugeModel, styleModel);
            case RADIAL_HALF_S:
                return (T) new RadialHalfS(gaugeModel, styleModel);
            case RADIAL_QUARTER_N:
                return (T) new RadialQuarterN(gaugeModel, styleModel);
            case RADIAL_QUARTER_E:
                return (T) new RadialQuarterE(gaugeModel, styleModel);
            case RADIAL_QUARTER_S:
                return (T) new RadialQuarterS(gaugeModel, styleModel);
            case RADIAL_QUARTER_W:
                return (T) new RadialQuarterW(gaugeModel, styleModel);
            case RADIAL:
            default:
                return (T) new Radial(gaugeModel, styleModel);
        }
    }


    // ******************** GaugeModel related ********************************
    public final GaugeBuilder gaugeType(final GaugeType GAUGE_TYPE) {
        gaugeType = GAUGE_TYPE;
        return this;
    }

    public final GaugeBuilder value(final double VALUE) {
        gaugeModel.setValue(VALUE);
        return this;
    }

    public final GaugeBuilder valueAnimationEnabled(final boolean VALUE_ANIMATION_ENABLED) {
        gaugeModel.setValueAnimationEnabled(VALUE_ANIMATION_ENABLED);
        return this;
    }

    public final GaugeBuilder animationDuration(final double ANIMATION_DURATION) {
        gaugeModel.setAnimationDuration(ANIMATION_DURATION);
        return this;
    }

    public final GaugeBuilder minValue(final double MIN_VALUE) {
        gaugeModel.setMinValue(MIN_VALUE);
        return this;
    }

    public final GaugeBuilder maxValue(final double MAX_VALUE) {
        gaugeModel.setMaxValue(MAX_VALUE);
        return this;
    }

    public final GaugeBuilder threshold(final double THRESHOLD) {
        gaugeModel.setThreshold(THRESHOLD);
        return this;
    }

    public final GaugeBuilder thresholdBehaviorInverted(final boolean THRESHOLD_BEHAVIOR_INVERTED) {
        gaugeModel.setThresholdBehaviorInverted(THRESHOLD_BEHAVIOR_INVERTED);
        return this;
    }

    public final GaugeBuilder title(final String TITLE) {
        gaugeModel.setTitle(TITLE);
        return this;
    }

    public final GaugeBuilder unit(final String UNIT) {
        gaugeModel.setUnit(UNIT);
        return this;
    }

    public final GaugeBuilder lcdValueCoupled(final boolean LCD_VALUE_COUPLED) {
        gaugeModel.setLcdValueCoupled(LCD_VALUE_COUPLED);
        return this;
    }

    public final GaugeBuilder lcdThreshold(final double LCD_THRESHOLD) {
        gaugeModel.setLcdThreshold(LCD_THRESHOLD);
        return this;
    }

    public final GaugeBuilder lcdThresholdBehaviorInverted(final boolean LCD_THRESHOLD_BEHAVIOR_INVERTED) {
        gaugeModel.setLcdThresholdBehaviorInverted(LCD_THRESHOLD_BEHAVIOR_INVERTED);
        return this;
    }

    public final GaugeBuilder lcdUnitString(final String LCD_UNIT_STRING) {
        gaugeModel.setLcdUnit(LCD_UNIT_STRING);
        return this;
    }

    public final GaugeBuilder lcdNumberSystem(final Gauge.NumberSystem LCD_NUMBER_SYSTEM) {
        gaugeModel.setLcdNumberSystem(LCD_NUMBER_SYSTEM);
        return this;
    }

    public final GaugeBuilder maxNoOfMajorTicks(final int MAX_NO_OF_MAJOR_TICKS) {
        gaugeModel.setMaxNoOfMajorTicks(MAX_NO_OF_MAJOR_TICKS);
        return this;
    }

    public final GaugeBuilder maxNoOfMinorTicks(final int MAX_NO_OF_MINOR_TICKS) {
        gaugeModel.setMaxNoOfMinorTicks(MAX_NO_OF_MINOR_TICKS);
        return this;
    }

    public final GaugeBuilder majorTickSpacing(final int MAJOR_TICKSPACING) {
        gaugeModel.setMajorTickSpacing(MAJOR_TICKSPACING);
        return this;
    }

    public final GaugeBuilder minorTickSpacing(final int MINOR_TICKSPACING) {
        gaugeModel.setMinorTickSpacing(MINOR_TICKSPACING);
        return this;
    }

    public final GaugeBuilder trend(final Gauge.Trend TREND) {
        gaugeModel.setTrend(TREND);
        return this;
    }

    public final GaugeBuilder niceScaling(final boolean NICE_SCALING) {
        gaugeModel.setNiceScaling(NICE_SCALING);
        return this;
    }

    public final GaugeBuilder sections(final Section... SECTION_ARRAY) {
        gaugeModel.setSections(SECTION_ARRAY);
        return this;
    }

    public final GaugeBuilder sections(final List<Section> SECTIONS) {
        gaugeModel.setSections(SECTIONS);
        return this;
    }

    public final GaugeBuilder areas(final Section... AREAS_ARRAY) {
        gaugeModel.setAreas(AREAS_ARRAY);
        return this;
    }

    public final GaugeBuilder areas(final List<Section> AREAS) {
        gaugeModel.setAreas(AREAS);
        return this;
    }

    public final GaugeBuilder markers(final Marker... MARKER_ARRAY) {
        gaugeModel.setMarkers(MARKER_ARRAY);
        return this;
    }

    public final GaugeBuilder markers(final List<Marker> MARKERS) {
        gaugeModel.setMarkers(MARKERS);
        return this;
    }



    // ******************** StyleModel related ********************************
    public final GaugeBuilder bargraph(final boolean BARGRAPH) {
        styleModel.setBargraph(BARGRAPH);
        return this;
    }

    public final GaugeBuilder minMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE) {
        styleModel.setMinMeasuredValueVisible(MIN_MEASURED_VALUE_VISIBLE);
        return this;
    }

    public final GaugeBuilder maxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE) {
        styleModel.setMaxMeasuredValueVisible(MAX_MEASURED_VALUE_VISIBLE);
        return this;
    }

    public final GaugeBuilder thresholdVisible(final boolean THRESHOLD_VISIBLE) {
        styleModel.setThresholdVisible(THRESHOLD_VISIBLE);
        return this;
    }

    public final GaugeBuilder thresholdColor(final Gauge.ThresholdColor THRESHOLD_COLOR) {
        styleModel.setThresholdColor(THRESHOLD_COLOR);
        return this;
    }

    public final GaugeBuilder frameDesign(final Gauge.FrameDesign FRAME_DESIGN) {
        styleModel.setFrameDesign(FRAME_DESIGN);
        return this;
    }

    public final GaugeBuilder frameVisible(final boolean FRAME_VISIBLE) {
        styleModel.setFrameVisible(FRAME_VISIBLE);
        return this;
    }

    public final GaugeBuilder backgroundDesign(final Gauge.BackgroundDesign BACKGROUND_DESIGN) {
        styleModel.setBackgroundDesign(BACKGROUND_DESIGN);
        return this;
    }

    public final GaugeBuilder backgroundVisible(final boolean BACKGROUND_VISIBLE) {
        styleModel.setBackgroundVisible(BACKGROUND_VISIBLE);
        return this;
    }

    public final GaugeBuilder knobDesign(final Gauge.KnobDesign KNOB_DESIGN) {
        styleModel.setKnobDesign(KNOB_DESIGN);
        return this;
    }

    public final GaugeBuilder knobColor(final Gauge.KnobColor KNOB_COLOR) {
        styleModel.setKnobColor(KNOB_COLOR);
        return this;
    }

    public final GaugeBuilder postsVisible(final boolean POSTS_VISIBLE) {
        styleModel.setPostsVisible(POSTS_VISIBLE);
        return this;
    }

    public final GaugeBuilder pointerType(final Gauge.PointerType POINTER_TYPE) {
        styleModel.setPointerType(POINTER_TYPE);
        return this;
    }

    public final GaugeBuilder valueColor(final ColorDef VALUE_COLOR) {
        styleModel.setValueColor(VALUE_COLOR);
        return this;
    }

    public final GaugeBuilder pointerGlowEnabled(final boolean POINTER_GLOW_ENABLED) {
        styleModel.setPointerGlowEnabled(POINTER_GLOW_ENABLED);
        return this;
    }

    public final GaugeBuilder pointerShadowEnabled(final boolean POINTER_SHADOW_ENABLED) {
        styleModel.setPointerShadowEnabled(POINTER_SHADOW_ENABLED);
        return this;
    }

    public final GaugeBuilder ledVisible(final boolean LED_VISIBLE) {
        styleModel.setLedVisible(LED_VISIBLE);
        return this;
    }

    public final GaugeBuilder ledColor(final LedColor LED_COLOR) {
        styleModel.setLedColor(LED_COLOR);
        return this;
    }

    public final GaugeBuilder userLedVisible(final boolean USER_LED_VISIBLE) {
        styleModel.setUserLedVisible(USER_LED_VISIBLE);
        return this;
    }

    public final GaugeBuilder userLedColor(final LedColor USER_LED_COLOR) {
        styleModel.setUserLedColor(USER_LED_COLOR);
        return this;
    }

    public final GaugeBuilder userLedOn(final boolean USER_LED_ON) {
        styleModel.setUserLedOn(USER_LED_ON);
        return this;
    }

    public final GaugeBuilder userLedBlinking(final boolean USER_LED_BLINKING) {
        styleModel.setUserLedBlinking(USER_LED_BLINKING);
        return this;
    }

    public final GaugeBuilder foregroundType(final Radial.ForegroundType FOREGROUND_TYPE) {
        styleModel.setForegroundType(FOREGROUND_TYPE);
        return this;
    }

    public final GaugeBuilder foregroundVisible(final boolean FOREGROUND_VISIBLE) {
        styleModel.setForegroundVisible(FOREGROUND_VISIBLE);
        return this;
    }

    public final GaugeBuilder lcdThresholdVisible(final boolean LCD_THRESHOLD_VISIBLE) {
        styleModel.setLcdThresholdVisible(LCD_THRESHOLD_VISIBLE);
        return this;
    }

    public final GaugeBuilder lcdDesign(final LcdDesign LCD_Design) {
        styleModel.setLcdDesign(LCD_Design);
        return this;
    }

    public final GaugeBuilder lcdVisible(final boolean LCD_VISIBLE) {
        styleModel.setLcdVisible(LCD_VISIBLE);
        return this;
    }

    public final GaugeBuilder lcdUnitStringVisible(final boolean LCD_UNIT_STRING_VISIBLE) {
        styleModel.setLcdUnitVisible(LCD_UNIT_STRING_VISIBLE);
        return this;
    }

    public final GaugeBuilder lcdDigitalFontEnabled(final boolean LCD_DIGITAL_FONT_ENABLED) {
        styleModel.setLcdDigitalFontEnabled(LCD_DIGITAL_FONT_ENABLED);
        return this;
    }

    public final GaugeBuilder lcdNumberSystemVisible(final boolean NUMBER_SYSTEM_VISIBLE) {
        styleModel.setLcdNumberSystemVisible(NUMBER_SYSTEM_VISIBLE);
        return this;
    }

    public final GaugeBuilder lcdUnitFont(final String UNIT_FONT) {
        styleModel.setLcdUnitFont(UNIT_FONT);
        return this;
    }

    public final GaugeBuilder lcdDecimals(final int LCD_DECIMALS) {
        styleModel.setLcdDecimals(LCD_DECIMALS);
        return this;
    }

    public final GaugeBuilder lcdBlinking(final boolean LCD_BLINKING) {
        styleModel.setLcdBlinking(LCD_BLINKING);
        return this;
    }

    public final GaugeBuilder glowVisible(final boolean GLOW_VISIBLE) {
        styleModel.setGlowVisible(GLOW_VISIBLE);
        return this;
    }

    public final GaugeBuilder glowOn(final boolean GLOW_ON) {
        styleModel.setGlowOn(GLOW_ON);
        return this;
    }

    public final GaugeBuilder pulsatingGlow(final boolean PULSATING_GLOW) {
        styleModel.setPulsatingGlow(PULSATING_GLOW);
        return this;
    }

    public final GaugeBuilder glowColor(final Color GLOW_COLOR) {
        styleModel.setGlowColor(GLOW_COLOR);
        return this;
    }

    public final GaugeBuilder tickmarksVisible(final boolean TICKMARKS_VISIBLE) {
        styleModel.setTickmarksVisible(TICKMARKS_VISIBLE);
        return this;
    }

    public final GaugeBuilder majorTickmarksVisible(final boolean MAJOR_TICKMARKS_VISIBLE) {
        styleModel.setMajorTicksVisible(MAJOR_TICKMARKS_VISIBLE);
        return this;
    }

    public final GaugeBuilder majorTickmarkType(final Gauge.TickmarkType TICKMARK_TYPE) {
        styleModel.setMajorTickmarkType(TICKMARK_TYPE);
        return this;
    }

    public final GaugeBuilder minorTickmarksVisible(final boolean MINOR_TICKMARKS_VISIBLE) {
        styleModel.setMinorTicksVisible(MINOR_TICKMARKS_VISIBLE);
        return this;
    }

    public final GaugeBuilder tickLabelsVisible(final boolean TICKLABELS_VISIBLE) {
        styleModel.setTickLabelsVisible(TICKLABELS_VISIBLE);
        return this;
    }

    public final GaugeBuilder tickLabelOrientation(final Gauge.TicklabelOrientation TICKLABEL_ORIENTATION) {
        styleModel.setTickLabelOrientation(TICKLABEL_ORIENTATION);
        return this;
    }

    public final GaugeBuilder tickLabelNumberFormat(final Gauge.NumberFormat TICKLABEL_NUMBER_FORMAT) {
        styleModel.setTickLabelNumberFormat(TICKLABEL_NUMBER_FORMAT);
        return this;
    }

    public final GaugeBuilder tickmarkGlowEnabled(final boolean TICKMARK_GLOW_ENABLED) {
        styleModel.setTickmarkGlowEnabled(TICKMARK_GLOW_ENABLED);
        return this;
    }

    public final GaugeBuilder tickmarkGlowColor(final Color TICKMARK_GLOW_COLOR) {
        styleModel.setTickmarkGlowColor(TICKMARK_GLOW_COLOR);
        return this;
    }

    public final GaugeBuilder sectionsVisible(final boolean SECTIONS_VISIBLE) {
        styleModel.setSectionsVisible(SECTIONS_VISIBLE);
        return this;
    }

    public final GaugeBuilder sectionsHighlighting(final boolean SECTIONS_HIGHLIGHTING) {
        styleModel.setSectionsHighlighting(SECTIONS_HIGHLIGHTING);
        return this;
    }

    public final GaugeBuilder areasVisible(final boolean AREAS_VISIBLE) {
        styleModel.setAreasVisible(AREAS_VISIBLE);
        return this;
    }

    public final GaugeBuilder areasHighlighting(final boolean AREAS_HIGHLIGHTING) {
        styleModel.setAreasHighlighting(AREAS_HIGHLIGHTING);
        return this;
    }

    public final GaugeBuilder markersVisible(final boolean MARKERS_VISIBLE) {
        styleModel.setMarkersVisible(MARKERS_VISIBLE);
        return this;
    }

    public final GaugeBuilder textureColor(final Color TEXTURE_COLOR) {
        styleModel.setTextureColor(TEXTURE_COLOR);
        return this;
    }

    public final GaugeBuilder simpleGradientBaseColor(final Color SIMPLE_GRADIENT_BASE_COLOR) {
        styleModel.setSimpleGradientBaseColor(SIMPLE_GRADIENT_BASE_COLOR);
        return this;
    }

    public final GaugeBuilder titleVisible(final boolean TITLE_VISIBLE) {
        styleModel.setTitleVisible(TITLE_VISIBLE);
        return this;
    }

    public final GaugeBuilder unitVisible(final boolean UNIT_VISIBLE) {
        styleModel.setUnitVisible(UNIT_VISIBLE);
        return this;
    }

    public final GaugeBuilder trendVisible(final boolean TREND_VISIBLE) {
        styleModel.setTrendVisible(TREND_VISIBLE);
        return this;
    }

    public final GaugeBuilder trendUpColor(final Color TREND_UP_COLOR) {
        styleModel.setTrendUpColor(TREND_UP_COLOR);
        return this;
    }

    public final GaugeBuilder trendSteadyColor(final Color TREND_STEADY_COLOR) {
        styleModel.setTrendSteadyColor(TREND_STEADY_COLOR);
        return this;
    }

    public final GaugeBuilder trendDownColor(final Color TREND_DOWN_COLOR) {
        styleModel.setTrendDownColor(TREND_DOWN_COLOR);
        return this;
    }
}
