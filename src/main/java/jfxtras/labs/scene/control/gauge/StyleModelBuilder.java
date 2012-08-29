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
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.scene.control.gauge.Gauge.TickmarkType;
import javafx.scene.paint.Color;

import java.util.HashMap;


/**
 * Created by
 * User: hansolo
 * Date: 29.01.12
 * Time: 10:43
 */
public class StyleModelBuilder {
    private HashMap<String, Property> properties = new HashMap<String, Property>();

    public static final StyleModelBuilder create() {
        return new StyleModelBuilder();
    }

    public final StyleModelBuilder bargraph(final boolean BARGRAPH) {
        properties.put("bargraph", new SimpleBooleanProperty(BARGRAPH));
        return this;
    }

    public final StyleModelBuilder minMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE) {
        properties.put("minMeasuredValueVisible", new SimpleBooleanProperty(MIN_MEASURED_VALUE_VISIBLE));
        return this;
    }

    public final StyleModelBuilder maxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE) {
        properties.put("maxMeasuredValueVisible", new SimpleBooleanProperty(MAX_MEASURED_VALUE_VISIBLE));
        return this;
    }

    public final StyleModelBuilder thresholdVisible(final boolean THRESHOLD_VISIBLE) {
        properties.put("thresholdVisible", new SimpleBooleanProperty(THRESHOLD_VISIBLE));
        return this;
    }

    public final StyleModelBuilder thresholdColor(final Gauge.ThresholdColor THRESHOLD_COLOR) {
        properties.put("thresholdColor", new SimpleObjectProperty<Gauge.ThresholdColor>(THRESHOLD_COLOR));
        return this;
    }

    public final StyleModelBuilder frameDesign(final Gauge.FrameDesign FRAME_DESIGN) {
        properties.put("frameDesign", new SimpleObjectProperty<Gauge.FrameDesign>(FRAME_DESIGN));
        return this;
    }

    public final StyleModelBuilder frameBaseColor(final Color FRAME_BASE_COLOR) {
        properties.put("frameBaseColor", new SimpleObjectProperty<Color>(FRAME_BASE_COLOR));
        return this;
    }

    public final StyleModelBuilder frameVisible(final boolean FRAME_VISIBLE) {
        properties.put("frameVisible", new SimpleBooleanProperty(FRAME_VISIBLE));
        return this;
    }

    public final StyleModelBuilder backgroundDesign(final Gauge.BackgroundDesign BACKGROUND_DESIGN) {
        properties.put("backgroundDesign", new SimpleObjectProperty<Gauge.BackgroundDesign>(BACKGROUND_DESIGN));
        return this;
    }

    public final StyleModelBuilder backgroundVisible(final boolean BACKGROUND_VISIBLE) {
        properties.put("backgroundVisible", new SimpleBooleanProperty(BACKGROUND_VISIBLE));
        return this;
    }

    public final StyleModelBuilder knobDesign(final Gauge.KnobDesign KNOB_DESIGN) {
        properties.put("knobDesign", new SimpleObjectProperty<Gauge.KnobDesign>(KNOB_DESIGN));
        return this;
    }

    public final StyleModelBuilder knobColor(final Gauge.KnobColor KNOB_COLOR) {
        properties.put("knobColor", new SimpleObjectProperty<Gauge.KnobColor>(KNOB_COLOR));
        return this;
    }

    public final StyleModelBuilder knobsVisible(final boolean KNOBS_VISIBLE) {
        properties.put("knobsVisible", new SimpleBooleanProperty(KNOBS_VISIBLE));
        return this;
    }

    public final StyleModelBuilder pointerType(final Gauge.PointerType POINTER_TYPE) {
        properties.put("pointerType", new SimpleObjectProperty<Gauge.PointerType>(POINTER_TYPE));
        return this;
    }

    public final StyleModelBuilder valueColor(final ColorDef VALUE_COLOR) {
        properties.put("valueColor", new SimpleObjectProperty<ColorDef>(VALUE_COLOR));
        return this;
    }

    public final StyleModelBuilder pointerGlowEnabled(final boolean POINTER_GLOW_ENABLED) {
        properties.put("pointerGlowEnabled", new SimpleBooleanProperty(POINTER_GLOW_ENABLED));
        return this;
    }

    public final StyleModelBuilder pointerShadowEnabled(final boolean POINTER_SHADOW_ENABLED) {
        properties.put("pointerShadowEnabled", new SimpleBooleanProperty(POINTER_SHADOW_ENABLED));
        return this;
    }

    public final StyleModelBuilder ledVisible(final boolean LED_VISIBLE) {
        properties.put("ledVisible", new SimpleBooleanProperty(LED_VISIBLE));
        return this;
    }

    public final StyleModelBuilder ledColor(final LedColor LED_COLOR) {
        properties.put("ledColor", new SimpleObjectProperty<LedColor>(LED_COLOR));
        return this;
    }

    public final StyleModelBuilder userLedVisible(final boolean USER_LED_VISIBLE) {
        properties.put("userLedVisible", new SimpleBooleanProperty(USER_LED_VISIBLE));
        return this;
    }

    public final StyleModelBuilder userLedColor(final LedColor USER_LED_COLOR) {
        properties.put("userLedColor", new SimpleObjectProperty<LedColor>(USER_LED_COLOR));
        return this;
    }

    public final StyleModelBuilder userLedOn(final boolean USER_LED_ON) {
        properties.put("userLedOn", new SimpleBooleanProperty(USER_LED_ON));
        return this;
    }

    public final StyleModelBuilder userLedBlinking(final boolean USER_LED_BLINKING) {
        properties.put("userLedBlinking", new SimpleBooleanProperty(USER_LED_BLINKING));
        return this;
    }

    public final StyleModelBuilder titleFont(final String TITLE_FONT) {
        properties.put("titleFont", new SimpleStringProperty(TITLE_FONT));
        return this;
    }

    public final StyleModelBuilder unitfont(final String UNIT_FONT) {
        properties.put("unitFont", new SimpleStringProperty(UNIT_FONT));
        return this;
    }

    public final StyleModelBuilder foregroundType(final Radial.ForegroundType FOREGROUND_TYPE) {
        properties.put("foregroundType", new SimpleObjectProperty<Radial.ForegroundType>(FOREGROUND_TYPE));
        return this;
    }

    public final StyleModelBuilder foregroundVisible(final boolean FOREGROUND_VISIBLE) {
        properties.put("foregroundVisible", new SimpleBooleanProperty(FOREGROUND_VISIBLE));
        return this;
    }

    public final StyleModelBuilder lcdThresholdVisible(final boolean LCD_THRESHOLD_VISIBLE) {
        properties.put("lcdThresholdVisible", new SimpleBooleanProperty(LCD_THRESHOLD_VISIBLE));
        return this;
    }

    public final StyleModelBuilder lcdDesign(final LcdDesign LCD_Design) {
        properties.put("lcdDesign", new SimpleObjectProperty<LcdDesign>(LCD_Design));
        return this;
    }

    public final StyleModelBuilder lcdVisible(final boolean LCD_VISIBLE) {
        properties.put("lcdVisible", new SimpleBooleanProperty(LCD_VISIBLE));
        return this;
    }

    public final StyleModelBuilder lcdUnitStringVisible(final boolean LCD_UNIT_STRING_VISIBLE) {
        properties.put("lcdUnitStringVisible", new SimpleBooleanProperty(LCD_UNIT_STRING_VISIBLE));
        return this;
    }

    public final StyleModelBuilder lcdNumberSystemVisible(final boolean NUMBER_SYSTEM_VISIBLE) {
        properties.put("lcdNumberSystemVisible", new SimpleBooleanProperty(NUMBER_SYSTEM_VISIBLE));
        return this;
    }

    public final StyleModelBuilder lcdUnitFont(final String UNIT_FONT) {
        properties.put("lcdUnitFont", new SimpleStringProperty(UNIT_FONT));
        return this;
    }

    public final StyleModelBuilder lcdTitleFont(final String TITLE_FONT) {
        properties.put("lcdTitleFont", new SimpleStringProperty(TITLE_FONT));
        return this;
    }

    public final StyleModelBuilder lcdValueFont(final Gauge.LcdFont VALUE_FONT) {
        properties.put("lcdValueFont", new SimpleObjectProperty<Gauge.LcdFont>(VALUE_FONT));
        return this;
    }

    public final StyleModelBuilder lcdDecimals(final int LCD_DECIMALS) {
        properties.put("lcdDecimals", new SimpleIntegerProperty(LCD_DECIMALS));
        return this;
    }

    public final StyleModelBuilder lcdBlinking(final boolean LCD_BLINKING) {
        properties.put("lcdBlinking", new SimpleBooleanProperty(LCD_BLINKING));
        return this;
    }

    public final StyleModelBuilder glowVisible(final boolean GLOW_VISIBLE) {
        properties.put("glowVisible", new SimpleBooleanProperty(GLOW_VISIBLE));
        return this;
    }

    public final StyleModelBuilder glowOn(final boolean GLOW_ON) {
        properties.put("glowOn", new SimpleBooleanProperty(GLOW_ON));
        return this;
    }

    public final StyleModelBuilder pulsatingGlow(final boolean PULSATING_GLOW) {
        properties.put("pulsatingGlow", new SimpleBooleanProperty(PULSATING_GLOW));
        return this;
    }

    public final StyleModelBuilder glowColor(final Color GLOW_COLOR) {
        properties.put("glowColor", new SimpleObjectProperty<Color>(GLOW_COLOR));
        return this;
    }

    public final StyleModelBuilder tickmarksVisible(final boolean TICKMARKS_VISIBLE) {
        properties.put("tickmarksVisible", new SimpleBooleanProperty(TICKMARKS_VISIBLE));
        return this;
    }

    public final StyleModelBuilder majorTickmarksVisible(final boolean MAJOR_TICKMARKS_VISIBLE) {
        properties.put("majorTickmarksVisible", new SimpleBooleanProperty(MAJOR_TICKMARKS_VISIBLE));
        return this;
    }

    public final StyleModelBuilder majorTickmarkType(final TickmarkType TICKMARK_TYPE) {
        properties.put("majorTickmarkType", new SimpleObjectProperty<TickmarkType>(TICKMARK_TYPE));
        return this;
    }

    public final StyleModelBuilder majorTickmarkColor(final Color MAJOR_TICKMAKR_COLOR) {
        properties.put("majorTickmarkColor", new SimpleObjectProperty<Color>(MAJOR_TICKMAKR_COLOR));
        return this;
    }

    public final StyleModelBuilder majorTickmarkColorEnabled(final boolean MAJOR_TICKMARK_COLOR_ENABLED) {
        properties.put("majorTickmarkColorEnabled", new SimpleBooleanProperty(MAJOR_TICKMARK_COLOR_ENABLED));
        return this;
    }

    public final StyleModelBuilder minorTickmarksVisible(final boolean MINOR_TICKMARKS_VISIBLE) {
        properties.put("minorTickmarksVisible", new SimpleBooleanProperty(MINOR_TICKMARKS_VISIBLE));
        return this;
    }

    public final StyleModelBuilder minorTickmarkColor(final Color MINOR_TICKMARK_COLOR) {
        properties.put("minorTickmarkColor", new SimpleObjectProperty<Color>(MINOR_TICKMARK_COLOR));
        return this;
    }

    public final StyleModelBuilder minorTickmarkColorEnabled(final boolean MINOR_TICKMARK_COLOR_ENABLED) {
        properties.put("minorTickmarkColorEnabled", new SimpleBooleanProperty(MINOR_TICKMARK_COLOR_ENABLED));
        return this;
    }

    public final StyleModelBuilder tickLabelsVisible(final boolean TICKLABELS_VISIBLE) {
        properties.put("tickLablesVisible", new SimpleBooleanProperty(TICKLABELS_VISIBLE));
        return this;
    }

    public final StyleModelBuilder tickLabelOrientation(final Gauge.TicklabelOrientation TICKLABEL_ORIENTATION) {
        properties.put("tickLabelOrientation", new SimpleObjectProperty<Gauge.TicklabelOrientation>(TICKLABEL_ORIENTATION));
        return this;
    }

    public final StyleModelBuilder tickLabelNumberFormat(final Gauge.NumberFormat TICKLABEL_NUMBER_FORMAT) {
        properties.put("tickLabelNumberFormat", new SimpleObjectProperty<Gauge.NumberFormat>(TICKLABEL_NUMBER_FORMAT));
        return this;
    }

    public final StyleModelBuilder tickmarkGlowEnabled(final boolean TICKMARK_GLOW_ENABLED) {
        properties.put("tickmarkGlowEnabled", new SimpleBooleanProperty(TICKMARK_GLOW_ENABLED));
        return this;
    }

    public final StyleModelBuilder tickmarkGlowColor(final Color TICKMARK_GLOW_COLOR) {
        properties.put("tickmarkGlowColor", new SimpleObjectProperty(TICKMARK_GLOW_COLOR));
        return this;
    }

    public final StyleModelBuilder sectionsVisible(final boolean SECTIONS_VISIBLE) {
        properties.put("sectionsVisible", new SimpleBooleanProperty(SECTIONS_VISIBLE));
        return this;
    }

    public final StyleModelBuilder sectionsHighlighting(final boolean SECTIONS_HIGHLIGHTING) {
        properties.put("sectionsHighlighting", new SimpleBooleanProperty(SECTIONS_HIGHLIGHTING));
        return this;
    }

    public final StyleModelBuilder showSectionTickmarksOnly(final boolean SHOW_SECTION_TICKMARKS_ONLY) {
        properties.put("showSectionTickmarksOnly", new SimpleBooleanProperty(SHOW_SECTION_TICKMARKS_ONLY));
        return this;
    }

    public final StyleModelBuilder areasVisible(final boolean AREAS_VISIBLE) {
        properties.put("areasVisible", new SimpleBooleanProperty(AREAS_VISIBLE));
        return this;
    }

    public final StyleModelBuilder areasHighlighting(final boolean AREAS_HIGHLIGHTING) {
        properties.put("areasHighlighting", new SimpleBooleanProperty(AREAS_HIGHLIGHTING));
        return this;
    }

    public final StyleModelBuilder markersVisible(final boolean MARKERS_VISIBLE) {
        properties.put("markersVisible", new SimpleBooleanProperty(MARKERS_VISIBLE));
        return this;
    }

    public final StyleModelBuilder textureColor(final Color TEXTURE_COLOR) {
        properties.put("textureColor", new SimpleObjectProperty<Color>(TEXTURE_COLOR));
        return this;
    }

    public final StyleModelBuilder simpleGradientBaseColor(final Color SIMPLE_GRADIENT_BASE_COLOR) {
        properties.put("simpleGradientBaseColor", new SimpleObjectProperty<Color>(SIMPLE_GRADIENT_BASE_COLOR));
        return this;
    }

    public final StyleModelBuilder titleVisible(final boolean TITLE_VISIBLE) {
        properties.put("titleVisible", new SimpleBooleanProperty(TITLE_VISIBLE));
        return this;
    }

    public final StyleModelBuilder unitVisible(final boolean UNIT_VISIBLE) {
        properties.put("unitVisible", new SimpleBooleanProperty(UNIT_VISIBLE));
        return this;
    }

    public final StyleModelBuilder trendVisible(final boolean TREND_VISIBLE) {
        properties.put("trendVisible", new SimpleBooleanProperty(TREND_VISIBLE));
        return this;
    }

    public final StyleModelBuilder trendUpColor(final Color TREND_UP_COLOR) {
        properties.put("trendUpColor", new SimpleObjectProperty<Color>(TREND_UP_COLOR));
        return this;
    }

    public final StyleModelBuilder trendRisingColor(final Color TREND_RISING_COLOR) {
        properties.put("trendRisingColor", new SimpleObjectProperty<Color>(TREND_RISING_COLOR));
        return this;
    }

    public final StyleModelBuilder trendSteadyColor(final Color TREND_STEADY_COLOR) {
        properties.put("trendSteadyColor", new SimpleObjectProperty<Color>(TREND_STEADY_COLOR));
        return this;
    }

    public final StyleModelBuilder trendFallingColor(final Color TREND_FALLING_COLOR) {
        properties.put("trendFallingColor", new SimpleObjectProperty<Color>(TREND_FALLING_COLOR));
        return this;
    }

    public final StyleModelBuilder trendDownColor(final Color TREND_DOWN_COLOR) {
        properties.put("trendDownColor", new SimpleObjectProperty<Color>(TREND_DOWN_COLOR));
        return this;
    }

    public final StyleModel build() {
        final StyleModel MODEL = new StyleModel();
        for(String key : properties.keySet()) {
            if ("bargraph".equals(key)) {
                MODEL.setBargraph(((BooleanProperty) properties.get(key)).get());
            } else if ("minMeasuredValueVisible".equals(key)) {
                MODEL.setMinMeasuredValueVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("maxMeasuredValueVisible".equals(key)) {
                MODEL.setMaxMeasuredValueVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("thresholdVisible".equals(key)) {
                MODEL.setThresholdVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("thresholdColor".equals(key)) {
                MODEL.setThresholdColor(((ObjectProperty<Gauge.ThresholdColor>) properties.get(key)).get());
            } else if ("frameDesign".equals(key)) {
                MODEL.setFrameDesign(((ObjectProperty<Gauge.FrameDesign>) properties.get(key)).get());
            } else if ("frameBaseColor".equals(key)) {
                MODEL.setFrameBaseColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("frameVisible".equals(key)) {
                MODEL.setFrameVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("backgroundDesign".equals(key)) {
                MODEL.setBackgroundDesign(((ObjectProperty<Gauge.BackgroundDesign>) properties.get(key)).get());
            } else if ("backgroundVisible".equals(key)) {
                MODEL.setBackgroundVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("knobDesign".equals(key)) {
                MODEL.setKnobDesign(((ObjectProperty<Gauge.KnobDesign>) properties.get(key)).get());
            } else if ("knobColor".equals(key)) {
                MODEL.setKnobColor(((ObjectProperty<Gauge.KnobColor>) properties.get(key)).get());
            } else if ("knobsVisible".equals(key)) {
                MODEL.setKnobsVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("pointerType".equals(key)) {
                MODEL.setPointerType(((ObjectProperty<Gauge.PointerType>) properties.get(key)).get());
            } else if ("valueColor".equals(key)) {
                MODEL.setValueColor(((ObjectProperty<ColorDef>) properties.get(key)).get());
            } else if ("pointerGlowEnabled".equals(key)) {
                MODEL.setPointerGlowEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("pointerShadowEnabled".equals(key)) {
                MODEL.setPointerShadowEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("ledVisible".equals(key)) {
                MODEL.setLedVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("ledColor".equals(key)) {
                MODEL.setLedColor(((ObjectProperty<LedColor>) properties.get(key)).get());
            } else if ("userLedVisible".equals(key)) {
                MODEL.setUserLedVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("userLedColor".equals(key)) {
                MODEL.setUserLedColor(((ObjectProperty<LedColor>) properties.get(key)).get());
            } else if ("userLedOn".equals(key)) {
                MODEL.setUserLedOn(((BooleanProperty) properties.get(key)).get());
            } else if ("userLedBlinking".equals(key)) {
                MODEL.setUserLedBlinking(((BooleanProperty) properties.get(key)).get());
            } else if ("titleFont".equals(key)) {
                MODEL.setTitleFont(((StringProperty) properties.get(key)).get());
            } else if ("unitFont".equals(key)) {
                MODEL.setUnitFont(((StringProperty) properties.get(key)).get());
            } else if ("foregroundType".equals(key)) {
                MODEL.setForegroundType(((ObjectProperty<Radial.ForegroundType>) properties.get(key)).get());
            } else if ("foregroundVisible".equals(key)) {
                MODEL.setForegroundVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("lcdThresholdVisible".equals(key)) {
                MODEL.setLcdThresholdVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("lcdDesign".equals(key)) {
                MODEL.setLcdDesign(((ObjectProperty<LcdDesign>) properties.get(key)).get());
            } else if ("lcdVisible".equals(key)) {
                MODEL.setLcdVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("lcdUnitStringVisible".equals(key)) {
                MODEL.setLcdUnitVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("lcdNumberSystemVisible".equals(key)) {
                MODEL.setLcdNumberSystemVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("lcdUnitFont".equals(key)) {
                MODEL.setLcdUnitFont(((StringProperty) properties.get(key)).get());
            } else if ("lcdTitleFont".equals(key)) {
                MODEL.setLcdTitleFont(((StringProperty) properties.get(key)).get());
            } else if ("lcdValueFont".equals(key)) {
                MODEL.setLcdValueFont(((ObjectProperty<Gauge.LcdFont>) properties.get(key)).get());
            } else if ("lcdDecimals".equals(key)) {
                MODEL.setLcdDecimals(((IntegerProperty) properties.get(key)).get());
            } else if ("lcdBlinking".equals(key)) {
                MODEL.setLcdBlinking(((BooleanProperty) properties.get(key)).get());
            } else if ("glowVisible".equals(key)) {
                MODEL.setGlowVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("glowOn".equals(key)) {
                MODEL.setGlowOn(((BooleanProperty) properties.get(key)).get());
            } else if ("tickmarksVisible".equals(key)) {
                MODEL.setTickmarksVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("majorTickmarksVisible".equals(key)) {
                MODEL.setMajorTicksVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("majorTickmarkType".equals(key)) {
                MODEL.setMajorTickmarkType(((ObjectProperty<TickmarkType>) properties.get(key)).get());
            } else if ("majorTickmarkColor".equals(key)) {
                MODEL.setMajorTickmarkColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("majorTickmarkColorEnabled".equals(key)) {
                MODEL.setMajorTickmarkColorEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("minorTickmarksVisible".equals(key)) {
                MODEL.setMinorTicksVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("minorTickmarkColor".equals(key)) {
                MODEL.setMinorTickmarkColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("minorTickmarkColorEnabled".equals(key)) {
                MODEL.setMinorTickmarkColorEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("tickLabelsVisible".equals(key)) {
                MODEL.setTickLabelsVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("tickLabelOrientation".equals(key)) {
                MODEL.setTickLabelOrientation(((ObjectProperty<Gauge.TicklabelOrientation>) properties.get(key)).get());
            } else if ("tickmarkGlowEnabled".equals(key)) {
                MODEL.setTickmarkGlowEnabled(((BooleanProperty) properties.get(key)).get());
            } else if ("tickmarkGlowColor".equals(key)) {
                MODEL.setTickmarkGlowColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("sectionsVisible".equals(key)) {
                MODEL.setSectionsVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("sectionsHighlighting".equals(key)) {
                MODEL.setSectionsHighlighting(((BooleanProperty) properties.get(key)).get());
            } else if ("showSectionTickmarksOnly".equals(key)) {
                MODEL.setShowSectionTickmarksOnly(((BooleanProperty) properties.get(key)).get());
            } else if ("areasVisible".equals(key)) {
                MODEL.setAreasVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("areasHighlighting".equals(key)) {
                MODEL.setAreasHighlighting(((BooleanProperty) properties.get(key)).get());
            } else if ("markersVisible".equals(key)) {
                MODEL.setMarkersVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("textureColor".equals(key)) {
                MODEL.setTextureColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("simpleGradientBaseColor".equals(key)) {
                MODEL.setSimpleGradientBaseColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("titleVisible".equals(key)) {
                MODEL.setTitleVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("unitVisible".equals(key)) {
                MODEL.setUnitVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("trendVisible".equals(key)) {
                MODEL.setTrendVisible(((BooleanProperty) properties.get(key)).get());
            } else if ("trendUpColor".equals(key)) {
                MODEL.setTrendUpColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("trendRisingColor".equals(key)) {
                MODEL.setTrendRisingColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("trendSteadyColor".equals(key)) {
                MODEL.setTrendSteadyColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("trendFallingColor".equals(key)) {
                MODEL.setTrendFallingColor(((ObjectProperty<Color>) properties.get(key)).get());
            } else if ("trendDownColor".equals(key)) {
                MODEL.setTrendDownColor(((ObjectProperty<Color>) properties.get(key)).get());
            }
        }
        return MODEL;
    }
}
