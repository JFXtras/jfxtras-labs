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
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import jfxtras.labs.scene.control.gauge.Gauge.BackgroundDesign;
import jfxtras.labs.scene.control.gauge.Gauge.FrameDesign;
import jfxtras.labs.scene.control.gauge.Gauge.KnobColor;
import jfxtras.labs.scene.control.gauge.Gauge.KnobDesign;
import jfxtras.labs.scene.control.gauge.Gauge.NumberFormat;
import jfxtras.labs.scene.control.gauge.Gauge.PointerType;
import jfxtras.labs.scene.control.gauge.Gauge.ThresholdColor;
import jfxtras.labs.scene.control.gauge.Gauge.TicklabelOrientation;
import jfxtras.labs.scene.control.gauge.Gauge.TickmarkType;
import jfxtras.labs.scene.control.gauge.Radial.ForegroundType;
import jfxtras.labs.util.Util;


/**
 * Created by
 * User: hansolo
 * Date: 29.01.12
 * Time: 09:47
 */
public class StyleModel {
    private BooleanProperty                      bargraph;
    private BooleanProperty                      minMeasuredValueVisible;
    private BooleanProperty                      maxMeasuredValueVisible;
    private BooleanProperty                      thresholdVisible;
    private ObjectProperty<ThresholdColor>       thresholdColor;
    private ObjectProperty<FrameDesign>          frameDesign;
    private ObjectProperty<Color>                frameBaseColor;
    private BooleanProperty                      frameVisible;
    private ObjectProperty<BackgroundDesign>     backgroundDesign;
    private BooleanProperty                      backgroundVisible;
    private ObjectProperty<KnobDesign>           knobDesign;
    private ObjectProperty<KnobColor>            knobColor;
    private BooleanProperty                      knobsVisible;
    private ObjectProperty<PointerType>          pointerType;
    private BooleanProperty                      pointerShadowEnabled;
    private BooleanProperty                      pointerGlowEnabled;
    private ObjectProperty<ColorDef>             valueColor;
    private BooleanProperty                      ledVisible;
    private ObjectProperty<LedColor>             ledColor;
    private BooleanProperty                      ledBlinking;
    private BooleanProperty                      userLedVisible;
    private BooleanProperty                      userLedOn;
    private ObjectProperty<LedColor>             userLedColor;
    private BooleanProperty                      userLedBlinking;
    private StringProperty                       titleFont;
    private StringProperty                       unitFont;
    private ObjectProperty<ForegroundType>       foregroundType;
    private BooleanProperty                      foregroundVisible;
    private BooleanProperty                      lcdThresholdVisible;
    private ObjectProperty<LcdDesign>            lcdDesign;
    private BooleanProperty                      lcdVisible;
    private BooleanProperty                      lcdUnitVisible;
    private StringProperty                       lcdUnitFont;
    private StringProperty                       lcdTitleFont;
    private ObjectProperty<Gauge.LcdFont>        lcdValueFont;
    private IntegerProperty                      lcdDecimals;
    private BooleanProperty                      lcdNumberSystemVisible;
    private BooleanProperty                      lcdBlinking;
    private BooleanProperty                      glowVisible;
    private ObjectProperty<Color>                glowColor;
    private BooleanProperty                      glowOn;
    private BooleanProperty                      pulsatingGlow;
    private BooleanProperty                      tickmarksVisible;
    private BooleanProperty                      majorTicksVisible;
    private ObjectProperty<TickmarkType>         majorTickmarkType;
    private ObjectProperty<Color>                majorTickmarkColor;
    private BooleanProperty                      majorTickmarkColorEnabled;
    private BooleanProperty                      minorTicksVisible;
    private ObjectProperty<Color>                minorTickmarkColor;
    private BooleanProperty                      minorTickmarkColorEnabled;
    private BooleanProperty                      tickLabelsVisible;
    private ObjectProperty<TicklabelOrientation> tickLabelOrientation;
    private ObjectProperty<NumberFormat>         tickLabelNumberFormat;
    private ObjectProperty<Point2D>              tickmarksOffset;
    private BooleanProperty                      tickmarkGlowEnabled;
    private ObjectProperty<Color>                tickmarkGlowColor;
    private BooleanProperty                      sectionsVisible;
    private BooleanProperty                      expandedSections;
    private BooleanProperty                      sectionsHighlighting;
    private BooleanProperty                      showSectionTickmarksOnly;
    private BooleanProperty                      areasVisible;
    private BooleanProperty                      areasHighlighting;
    private BooleanProperty                      markersVisible;
    private ObjectProperty<Color>                textureColor;
    private ObjectProperty<Color>                simpleGradientBaseColor;
    private BooleanProperty                      titleVisible;
    private BooleanProperty                      unitVisible;
    private BooleanProperty                      trendVisible;
    private ObjectProperty<Color>                trendUpColor;
    private ObjectProperty<Color>                trendRisingColor;
    private ObjectProperty<Color>                trendSteadyColor;
    private ObjectProperty<Color>                trendFallingColor;
    private ObjectProperty<Color>                trendDownColor;


    // ******************** Constructors **************************************
    public StyleModel() {
        bargraph                        = new SimpleBooleanProperty(false);
        minMeasuredValueVisible         = new SimpleBooleanProperty(false);
        maxMeasuredValueVisible         = new SimpleBooleanProperty(false);
        thresholdVisible                = new SimpleBooleanProperty(false);
        thresholdColor                  = new SimpleObjectProperty<ThresholdColor>(Gauge.ThresholdColor.RED);
        frameDesign                     = new SimpleObjectProperty<FrameDesign>(Gauge.FrameDesign.METAL);
        frameBaseColor                  = new SimpleObjectProperty<Color>(Color.rgb(160, 160, 160));
        frameVisible                    = new SimpleBooleanProperty(true);
        backgroundDesign                = new SimpleObjectProperty<BackgroundDesign>(Gauge.BackgroundDesign.DARK_GRAY);
        backgroundVisible               = new SimpleBooleanProperty(true);
        knobDesign                      = new SimpleObjectProperty<KnobDesign>(Gauge.KnobDesign.STANDARD);
        knobColor                       = new SimpleObjectProperty<KnobColor>(Gauge.KnobColor.SILVER);
        knobsVisible                    = new SimpleBooleanProperty(true);
        pointerType                     = new SimpleObjectProperty<PointerType>(Gauge.PointerType.TYPE1);
        valueColor                      = new SimpleObjectProperty<ColorDef>(ColorDef.RED);
        pointerShadowEnabled            = new SimpleBooleanProperty(true);
        pointerGlowEnabled              = new SimpleBooleanProperty(false);
        ledVisible                      = new SimpleBooleanProperty(true);
        ledColor                        = new SimpleObjectProperty<LedColor>(LedColor.RED);
        ledBlinking                     = new SimpleBooleanProperty(false);
        userLedVisible                  = new SimpleBooleanProperty(false);
        userLedColor                    = new SimpleObjectProperty<LedColor>(LedColor.BLUE);
        userLedBlinking                 = new SimpleBooleanProperty(false);
        userLedOn                       = new SimpleBooleanProperty(false);
        titleFont                       = new SimpleStringProperty("Verdana");
        unitFont                        = new SimpleStringProperty("Verdana");
        foregroundType                  = new SimpleObjectProperty<ForegroundType>(Radial.ForegroundType.TYPE1);
        foregroundVisible               = new SimpleBooleanProperty(true);
        lcdThresholdVisible             = new SimpleBooleanProperty(false);
        lcdDesign                       = new SimpleObjectProperty<LcdDesign>(LcdDesign.WHITE);
        lcdVisible                      = new SimpleBooleanProperty(true);
        lcdUnitVisible                  = new SimpleBooleanProperty(false);
        lcdUnitFont                     = new SimpleStringProperty("Verdana");
        lcdTitleFont                    = new SimpleStringProperty("Verdana");
        lcdValueFont                    = new SimpleObjectProperty<Gauge.LcdFont>(Gauge.LcdFont.STANDARD);
        lcdDecimals                     = new SimpleIntegerProperty(0);
        lcdNumberSystemVisible          = new SimpleBooleanProperty(false);
        lcdBlinking                     = new SimpleBooleanProperty(false);
        glowVisible                     = new SimpleBooleanProperty(false);
        glowColor                       = new SimpleObjectProperty<Color>(Color.rgb(51, 255, 255));
        glowOn                          = new SimpleBooleanProperty(false);
        pulsatingGlow                   = new SimpleBooleanProperty(false);
        tickmarksVisible                = new SimpleBooleanProperty(true);
        majorTicksVisible               = new SimpleBooleanProperty(true);
        majorTickmarkType               = new SimpleObjectProperty<TickmarkType>(TickmarkType.LINE);
        majorTickmarkColor              = new SimpleObjectProperty<Color>(Color.WHITE);
        majorTickmarkColorEnabled       = new SimpleBooleanProperty(false);
        minorTicksVisible               = new SimpleBooleanProperty(true);
        minorTickmarkColor              = new SimpleObjectProperty<Color>(Color.WHITE);
        minorTickmarkColorEnabled       = new SimpleBooleanProperty(false);
        tickLabelsVisible               = new SimpleBooleanProperty(true);
        tickLabelOrientation            = new SimpleObjectProperty<TicklabelOrientation>(Gauge.TicklabelOrientation.NORMAL);
        tickLabelNumberFormat           = new SimpleObjectProperty<NumberFormat>(Gauge.NumberFormat.AUTO);
        tickmarksOffset                 = new SimpleObjectProperty<Point2D>(new Point2D(0, 0));
        tickmarkGlowEnabled             = new SimpleBooleanProperty(false);
        tickmarkGlowColor               = new SimpleObjectProperty<Color>(Color.color(0.5, 0.7, 0.9, 0.8));
        sectionsVisible                 = new SimpleBooleanProperty(false);
        expandedSections                = new SimpleBooleanProperty(false);
        sectionsHighlighting            = new SimpleBooleanProperty(false);
        showSectionTickmarksOnly        = new SimpleBooleanProperty(false);
        areasVisible                    = new SimpleBooleanProperty(false);
        areasHighlighting               = new SimpleBooleanProperty(false);
        markersVisible                  = new SimpleBooleanProperty(false);
        textureColor                    = new SimpleObjectProperty<Color>(Color.rgb(29, 33, 35));
        simpleGradientBaseColor         = new SimpleObjectProperty<Color>(Color.rgb(213, 0, 0));
        titleVisible                    = new SimpleBooleanProperty(true);
        unitVisible                     = new SimpleBooleanProperty(true);
        trendVisible                    = new SimpleBooleanProperty(false);
        trendUpColor                    = new SimpleObjectProperty<Color>(Color.LIME);
        trendRisingColor                = new SimpleObjectProperty<Color>(Color.YELLOWGREEN);
        trendSteadyColor                = new SimpleObjectProperty<Color>(Color.YELLOW);
        trendFallingColor               = new SimpleObjectProperty<Color>(Color.ORANGE);
        trendDownColor                  = new SimpleObjectProperty<Color>(Color.RED);
    }


    // ******************** Event handling ************************************
    public final ObjectProperty<EventHandler<StyleModelEvent>> onStyleModelEventProperty() {
        return onStyleModelEvent;
    }

    public final void setOnStyleModelEvent(final EventHandler<StyleModelEvent> HANDLER) {
        onStyleModelEventProperty().set(HANDLER);
    }

    public final EventHandler<StyleModelEvent> getOnStyleModelEvent() {
        return onStyleModelEventProperty().get();
    }

    private ObjectProperty<EventHandler<StyleModelEvent>> onStyleModelEvent = new SimpleObjectProperty<EventHandler<StyleModelEvent>>();

    public void fireStyleModelEvent() {
        final EventHandler<StyleModelEvent> VIEW_MODEL_EVENT_HANDLER = getOnStyleModelEvent();
        if (VIEW_MODEL_EVENT_HANDLER != null) {
            final StyleModelEvent STYLE_MODEL_EVENT = new StyleModelEvent();
            VIEW_MODEL_EVENT_HANDLER.handle(STYLE_MODEL_EVENT);
        }
    }


    // ******************** Methods *******************************************
    public final boolean isBargraph() {
        return bargraph.get();
    }

    public final void setBargraph(final boolean BARGRAPH) {
        bargraph.set(BARGRAPH);
        fireStyleModelEvent();
    }

    public final BooleanProperty bargraphProperty() {
        return bargraph;
    }

    public final boolean isMinMeasuredValueVisible() {
        return minMeasuredValueVisible.get();
    }

    public final void setMinMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE) {
        minMeasuredValueVisible.set(MIN_MEASURED_VALUE_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty minMeasuredValueVisibleProperty() {
        return minMeasuredValueVisible;
    }

    public final boolean isMaxMeasuredValueVisible() {
        return maxMeasuredValueVisible.get();
    }

    public final void setMaxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE) {
        maxMeasuredValueVisible.set(MAX_MEASURED_VALUE_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty maxMeasuredValueVisibleProperty() {
        return maxMeasuredValueVisible;
    }

    public final boolean isThresholdVisible() {
        return thresholdVisible.get();
    }

    public final void setThresholdVisible(final boolean THRESHOLD_VISIBLE) {
        thresholdVisible.set(THRESHOLD_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty thresholdVisibleProperty() {
        return thresholdVisible;
    }

    public final Gauge.ThresholdColor getThresholdColor() {
        return thresholdColor.get();
    }

    public final void setThresholdColor(final Gauge.ThresholdColor THRESHOLD_COLOR) {
        thresholdColor.set(THRESHOLD_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Gauge.ThresholdColor> thresholdColorProperty() {
        return thresholdColor;
    }

    public final Gauge.FrameDesign getFrameDesign() {
        return frameDesign.get();
    }

    public final void setFrameDesign(final Gauge.FrameDesign FRAME_DESIGN) {
        frameDesign.set(FRAME_DESIGN);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Gauge.FrameDesign> frameDesignProperty() {
        return frameDesign;
    }

    public final Color getFrameBaseColor() {
        return frameBaseColor.get();
    }

    public final void setFrameBaseColor(final Color FRAME_BASE_COLOR) {
        frameBaseColor.set(FRAME_BASE_COLOR);
    }

    public final ObjectProperty<Color> frameBaseColorProperty() {
        return frameBaseColor;
    }

    public final boolean isFrameVisible() {
        return frameVisible.get();
    }

    public final void setFrameVisible(final boolean FRAME_VISIBLE) {
        frameVisible.set(FRAME_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty frameVisibleProperty() {
        return frameVisible;
    }

    public final Gauge.BackgroundDesign getBackgroundDesign() {
        return backgroundDesign.get();
    }

    public final void setBackgroundDesign(final Gauge.BackgroundDesign BACKGROUND_DESIGN) {
        backgroundDesign.set(BACKGROUND_DESIGN);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Gauge.BackgroundDesign> backgroundDesignProperty() {
        return backgroundDesign;
    }

    public final boolean isBackgroundVisible() {
        return backgroundVisible.get();
    }

    public final void setBackgroundVisible(final boolean BACKGROUND_VISIBLE) {
        backgroundVisible.set(BACKGROUND_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty backgroundVisibleProperty() {
        return backgroundVisible;
    }

    public final Gauge.KnobDesign getKnobDesign() {
        return knobDesign.get();
    }

    public final void setKnobDesign(final Gauge.KnobDesign KNOB_DESIGN) {
        knobDesign.set(KNOB_DESIGN);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Gauge.KnobDesign> knobDesignProperty() {
        return knobDesign;
    }

    public final Gauge.KnobColor getKnobColor() {
        return knobColor.get();
    }

    public final void setKnobColor(final Gauge.KnobColor KNOB_COLOR) {
        knobColor.set(KNOB_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Gauge.KnobColor> knobColorProperty() {
        return knobColor;
    }

    public final boolean getKnobsVisible() {
        return knobsVisible.get();
    }

    public final void setKnobsVisible(final boolean KNOBS_VISIBLE) {
        knobsVisible.set(KNOBS_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty knobsVisibleProperty() {
        return knobsVisible;
    }

    public final Gauge.PointerType getPointerType() {
        return pointerType.get();
    }

    public final void setPointerType(final Gauge.PointerType POINTER_TYPE) {
        pointerType.set(POINTER_TYPE);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Gauge.PointerType> pointerTypeProperty() {
        return pointerType;
    }

    public final ColorDef getValueColor() {
        return valueColor.get();
    }

    public final void setValueColor(final ColorDef VALUE_COLOR) {
        valueColor.set(VALUE_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<ColorDef> valueColorProperty() {
        return valueColor;
    }

    public final boolean isPointerGlowEnabled() {
        return pointerGlowEnabled.get();
    }

    public final void setPointerGlowEnabled(final boolean POINTER_GLOW_ENABLED) {
        pointerGlowEnabled.set(POINTER_GLOW_ENABLED);
        fireStyleModelEvent();
    }

    public final BooleanProperty pointerGlowEnabledProperty() {
        return pointerGlowEnabled;
    }

    public final boolean isPointerShadowEnabled() {
        return pointerShadowEnabled.get();
    }

    public final void setPointerShadowEnabled(final boolean POINTER_SHADOW_ENABLED) {
        pointerShadowEnabled.set(POINTER_SHADOW_ENABLED);
        fireStyleModelEvent();
    }

    public final BooleanProperty pointerShadowEnabledProperty() {
        return pointerShadowEnabled;
    }

    public final boolean isLedVisible() {
        return ledVisible.get();
    }

    public final void setLedVisible(final boolean LED_VISIBLE) {
        ledVisible.set(LED_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty ledVisibleProperty() {
        return ledVisible;
    }

    public final LedColor getLedColor() {
        return ledColor.get();
    }

    public final void setLedColor(final LedColor LED_COLOR) {
        ledColor.set(LED_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<LedColor> ledColorProperty() {
        return ledColor;
    }

    public final boolean isLedBlinking() {
        return ledBlinking.get();
    }

    public final void setLedBlinking(final boolean LED_BLINKING) {
        ledBlinking.set(LED_BLINKING);
        fireStyleModelEvent();
    }

    public final BooleanProperty ledBlinkingProperty() {
        return ledBlinking;
    }

    public final boolean isUserLedVisible() {
        return userLedVisible.get();
    }

    public final void setUserLedVisible(final boolean USER_LED_VISIBLE) {
        userLedVisible.set(USER_LED_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty userLedVisibleProperty() {
        return userLedVisible;
    }

    public final LedColor getUserLedColor() {
        return userLedColor.get();
    }

    public final void setUserLedColor(final LedColor USER_LED_COLOR) {
        userLedColor.set(USER_LED_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<LedColor> userLedColorProperty() {
        return userLedColor;
    }

    public final boolean isUserLedOn() {
        return userLedOn.get();
    }

    public final void setUserLedOn(final boolean USER_LED_ON) {
        userLedOn.set(USER_LED_ON);
        fireStyleModelEvent();
    }

    public final BooleanProperty userLedOnProperty() {
        return userLedOn;
    }

    public final boolean isUserLedBlinking() {
        return userLedBlinking.get();
    }

    public final void setUserLedBlinking(final boolean USER_LED_BLINKING) {
        userLedBlinking.set(USER_LED_BLINKING);
        fireStyleModelEvent();
    }

    public final BooleanProperty userLedBlinkingProperty() {
        return userLedBlinking;
    }

    public final String getTitleFont() {
        return titleFont.get();
    }

    public final void setTitleFont(final String TITLE_FONT) {
        titleFont.set(TITLE_FONT);
        fireStyleModelEvent();
    }

    public final StringProperty titleFontProperty() {
        return titleFont;
    }

    public final String getUnitFont() {
        return unitFont.get();
    }

    public final void setUnitFont(final String UNIT_FONT) {
        unitFont.set(UNIT_FONT);
        fireStyleModelEvent();
    }

    public final StringProperty unitFontProperty() {
        return unitFont;
    }

    public final Radial.ForegroundType getForegroundType() {
        return foregroundType.get();
    }

    public final void setForegroundType(final Radial.ForegroundType FOREGROUND_TYPE) {
        foregroundType.set(FOREGROUND_TYPE);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Radial.ForegroundType> foregroundTypeProperty() {
        return foregroundType;
    }

    public final boolean isForegroundVisible() {
        return foregroundVisible.get();
    }

    public final void setForegroundVisible(final boolean FOREGROUND_VISIBLE) {
        foregroundVisible.set(FOREGROUND_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty foregroundVisibleProperty() {
        return foregroundVisible;
    }

    public final boolean isLcdThresholdVisible() {
        return lcdThresholdVisible.get();
    }

    public final void setLcdThresholdVisible(final boolean LCD_THRESHOLD_VISIBLE) {
        lcdThresholdVisible.set(LCD_THRESHOLD_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty lcdThresholdVisibleProperty() {
        return lcdThresholdVisible;
    }

    public final LcdDesign getLcdDesign() {
        return lcdDesign.get();
    }

    public final void setLcdDesign(final LcdDesign LCD_Design) {
        lcdDesign.set(LCD_Design);
        fireStyleModelEvent();
    }

    public final ObjectProperty lcdDesignProperty() {
        return lcdDesign;
    }

    public final boolean isLcdVisible() {
        return lcdVisible.get();
    }

    public final void setLcdVisible(final boolean LCD_VISIBLE) {
        lcdVisible.set(LCD_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty lcdVisibleProperty() {
        return lcdVisible;
    }

    public final boolean getLcdUnitVisible() {
        return lcdUnitVisible.get();
    }

    public final void setLcdUnitVisible(final boolean LCD_UNIT_STRING_VISIBLE) {
        lcdUnitVisible.set(LCD_UNIT_STRING_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty lcdUnitVisibleProperty() {
        return lcdUnitVisible;
    }

    public final String getLcdUnitFont() {
        return lcdUnitFont.get();
    }

    public final void setLcdUnitFont(final String UNIT_FONT) {
        lcdUnitFont.set(UNIT_FONT);
        fireStyleModelEvent();
    }

    public final StringProperty lcdUnitFontProperty() {
        return lcdUnitFont;
    }

    public final String getLcdTitleFont() {
        return lcdTitleFont.get();
    }

    public final void setLcdTitleFont(final String TITLE_FONT) {
        lcdTitleFont.set(TITLE_FONT);
        fireStyleModelEvent();
    }

    public final StringProperty lcdTitleFontProperty() {
        return lcdTitleFont;
    }

    public final Gauge.LcdFont getLcdValueFont() {
        return lcdValueFont.get();
    }

    public final void setLcdValueFont(final Gauge.LcdFont VALUE_FONT) {
        lcdValueFont.set(VALUE_FONT);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Gauge.LcdFont> lcdValueFontProperty() {
        return lcdValueFont;
    }

    public final int getLcdDecimals() {
        return lcdDecimals.get();
    }

    public final void setLcdDecimals(final int LCD_DECIMALS) {
        final int DECIMALS = LCD_DECIMALS > 5 ? 5 : (LCD_DECIMALS < 0 ? 0 : LCD_DECIMALS);
        lcdDecimals.set(DECIMALS);
        fireStyleModelEvent();
    }

    public final IntegerProperty lcdDecimalsProperty() {
        return lcdDecimals;
    }

    public final boolean isLcdNumberSystemVisible() {
        return lcdNumberSystemVisible.get();
    }

    public final void setLcdNumberSystemVisible(final boolean LCD_NUMBER_SYSTEM_VISIBLE) {
        lcdNumberSystemVisible.set(LCD_NUMBER_SYSTEM_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty lcdNumberSystemVisibleProperty() {
        return lcdNumberSystemVisible;
    }

    public final boolean isLcdBlinking() {
        return lcdBlinking.get();
    }

    public final void setLcdBlinking(final boolean LCD_BLINKING) {
        lcdBlinking.set(LCD_BLINKING);
        fireStyleModelEvent();
    }

    public final BooleanProperty lcdBlinkingProperty() {
        return lcdBlinking;
    }

    public final boolean isGlowVisible() {
        return glowVisible.get();
    }

    public final void setGlowVisible(final boolean GLOW_VISIBLE) {
        glowVisible.set(GLOW_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty glowVisibleProperty() {
        return glowVisible;
    }

    public final Color getGlowColor() {
        return glowColor.get();
    }

    public final void setGlowColor(final Color GLOW_COLOR) {
        glowColor.set(GLOW_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Color> glowColorProperty() {
        return glowColor;
    }

    public final boolean isGlowOn() {
        return glowOn.get();
    }

    public final void setGlowOn(final boolean GLOW_ON) {
        glowOn.set(GLOW_ON);
        fireStyleModelEvent();
    }

    public final BooleanProperty glowOnProperty() {
        return glowOn;
    }

    public final boolean isPulsatingGlow() {
        return pulsatingGlow.get();
    }

    public final void setPulsatingGlow(final boolean PULSATING_GLOW) {
        pulsatingGlow.set(PULSATING_GLOW);
        fireStyleModelEvent();
    }

    public final BooleanProperty pulsatingGlowProperty() {
        return pulsatingGlow;
    }

    public final boolean isTickmarksVisible() {
        return tickmarksVisible.get();
    }

    public final void setTickmarksVisible(final boolean TICKMARKS_VISIBLE) {
        tickmarksVisible.set(TICKMARKS_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty tickmarksVisibleProperty() {
        return tickmarksVisible;
    }

    public final boolean isMajorTicksVisible() {
        return majorTicksVisible.get();
    }

    public final void setMajorTicksVisible(final boolean MAJOR_TICKS_VISIBLE) {
        majorTicksVisible.set(MAJOR_TICKS_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty majorTicksVisibleProperty() {
        return majorTicksVisible;
    }

    public final TickmarkType getMajorTickmarkType() {
        return majorTickmarkType.get();
    }

    public final void setMajorTickmarkType(final TickmarkType TICKMARK_TYPE) {
        majorTickmarkType.set(TICKMARK_TYPE);
        fireStyleModelEvent();
    }

    public final ObjectProperty<TickmarkType> majorTickmarkTypeProperty() {
        return majorTickmarkType;
    }

    public final Color getMajorTickmarkColor() {
        return majorTickmarkColor.get();
    }

    public final void setMajorTickmarkColor(final Color MAJOR_TICKMARK_COLOR) {
        majorTickmarkColor.set(MAJOR_TICKMARK_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Color> majorTickmarkColorProperty() {
        return majorTickmarkColor;
    }

    public final boolean isMajorTickmarkColorEnabled() {
        return majorTickmarkColorEnabled.get();
    }

    public final void setMajorTickmarkColorEnabled(final boolean MAJOR_TICKMARK_COLOR_ENABLED) {
        majorTickmarkColorEnabled.set(MAJOR_TICKMARK_COLOR_ENABLED);
        fireStyleModelEvent();
    }

    public final BooleanProperty majorTickmarkColorEnabledProperty() {
        return majorTickmarkColorEnabled;
    }

    public final boolean isMinorTicksVisible() {
        return minorTicksVisible.get();
    }

    public final void setMinorTicksVisible(final boolean MINOR_TICKS_VISIBLE) {
        minorTicksVisible.set(MINOR_TICKS_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty minorTicksVisibleProperty() {
        return minorTicksVisible;
    }

    public final Color getMinorTickmarkColor() {
        return minorTickmarkColor.get();
    }

    public final void setMinorTickmarkColor(final Color MINOR_TICKMARK_COLOR) {
        minorTickmarkColor.set(MINOR_TICKMARK_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Color> minorTickmarkColorProperty() {
        return minorTickmarkColor;
    }

    public final boolean isMinorTickmarkColorEnabled() {
        return minorTickmarkColorEnabled.get();
    }

    public final void setMinorTickmarkColorEnabled(final boolean MINOR_TICKMARK_COLOR_ENABLED) {
        minorTickmarkColorEnabled.set(MINOR_TICKMARK_COLOR_ENABLED);
        fireStyleModelEvent();
    }

    public final BooleanProperty minorTickmarkColorEnabledProperty() {
        return minorTickmarkColorEnabled;
    }

    public final boolean isTickLabelsVisible() {
        return tickLabelsVisible.get();
    }

    public final void setTickLabelsVisible(final boolean TICKLABELS_VISIBLE) {
        tickLabelsVisible.set(TICKLABELS_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty ticklabelsVisibleProperty() {
        return tickLabelsVisible;
    }

    public final Gauge.TicklabelOrientation getTickLabelOrientation() {
        return tickLabelOrientation.get();
    }

    public final void setTickLabelOrientation(final Gauge.TicklabelOrientation TICKLABEL_ORIENTATION) {
        tickLabelOrientation.set(TICKLABEL_ORIENTATION);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Gauge.TicklabelOrientation> tickLabelOrientationProperty() {
        return tickLabelOrientation;
    }

    public final Gauge.NumberFormat getTickLabelNumberFormat() {
        return tickLabelNumberFormat.get();
    }

    public final void setTickLabelNumberFormat(final Gauge.NumberFormat TICKLABEL_NUMBER_FORMAT) {
        tickLabelNumberFormat.set(TICKLABEL_NUMBER_FORMAT);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Gauge.NumberFormat> tickLabelNumberFormatProperty() {
        return tickLabelNumberFormat;
    }

    public final Point2D getTickmarksOffset() {
        return tickmarksOffset.get();
    }

    public final void setTickmarksOffset(final Point2D TICKMARKS_OFFSET) {
        tickmarksOffset.set(TICKMARKS_OFFSET);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Point2D> tickmarksOffsetProperty() {
        return tickmarksOffset;
    }

    public final boolean isTickmarkGlowEnabled() {
        return tickmarkGlowEnabled.get();
    }

    public final void setTickmarkGlowEnabled(final boolean TICKMARK_GLOW_ENABLED) {
        tickmarkGlowEnabled.set(TICKMARK_GLOW_ENABLED);
        fireStyleModelEvent();
    }

    public final BooleanProperty tickmarkGlowEnabledProperty() {
        return tickmarkGlowEnabled;
    }

    public final Color getTickmarkGlowColor() {
        return tickmarkGlowColor.get();
    }

    public final void setTickmarkGlowColor(final Color TICKMARK_GLOW_COLOR) {
        tickmarkGlowColor.set(TICKMARK_GLOW_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Color> tickmarkGlowColorProperty() {
        return tickmarkGlowColor;
    }

    public final boolean isSectionsVisible() {
        return sectionsVisible.get();
    }

    public final void setSectionsVisible(final boolean SECTIONS_VISIBLE) {
        sectionsVisible.set(SECTIONS_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty sectionsVisibleProperty() {
        return sectionsVisible;
    }

    public final boolean isExpandedSections() {
        return expandedSections.get();
    }

    public final void setExpandedSections(final boolean EXPANDED_SECTIONS) {
        expandedSections.set(EXPANDED_SECTIONS);
        fireStyleModelEvent();
    }

    public final BooleanProperty expandedSectionsProperty() {
        return expandedSections;
    }

    public final boolean isSectionsHighlighting() {
        return sectionsHighlighting.get();
    }

    public final void setSectionsHighlighting(final boolean SECTIONS_HIGHLIGHTING) {
        sectionsHighlighting.set(SECTIONS_HIGHLIGHTING);
        fireStyleModelEvent();
    }

    public final BooleanProperty sectionsHighlightingProperty() {
        return sectionsHighlighting;
    }

    public final boolean isShowSectionTickmarksOnly() {
        return showSectionTickmarksOnly.get();
    }

    public final void setShowSectionTickmarksOnly(final boolean SHOW_SECTION_TICKMARKS_ONLY) {
        showSectionTickmarksOnly.set(SHOW_SECTION_TICKMARKS_ONLY);
        fireStyleModelEvent();
    }

    public final BooleanProperty showSectionTickmarksOnlyProperty() {
        return showSectionTickmarksOnly;
    }

    public final boolean isAreasVisible() {
        return areasVisible.get();
    }

    public final void setAreasVisible(final boolean AREAS_VISIBLE) {
        areasVisible.set(AREAS_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty areasVisibleProperty() {
        return areasVisible;
    }

    public final boolean isAreasHighlighting() {
        return areasHighlighting.get();
    }

    public final void setAreasHighlighting(final boolean AREAS_HIGHLIGHTING) {
        areasHighlighting.set(AREAS_HIGHLIGHTING);
        fireStyleModelEvent();
    }

    public final BooleanProperty areasHighlightingProperty() {
        return areasHighlighting;
    }

    public final boolean isMarkersVisible() {
        return markersVisible.get();
    }

    public final void setMarkersVisible(final boolean MARKERS_VISIBLE) {
        markersVisible.set(MARKERS_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty markersVisibleProperty() {
        return markersVisible;
    }

    public final Color getTextureColor() {
        return textureColor.get();
    }

    public final String getTextureColorString() {
        final StringBuilder COLOR_STRING = new StringBuilder(30);
        COLOR_STRING.append("-fx-texture: ");
        COLOR_STRING.append(Util.createCssColor(getTextureColor()));
        return COLOR_STRING.toString();
    }

    public final void setTextureColor(final Color TEXTURE_COLOR) {
        textureColor.set(TEXTURE_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Color> textureColorProperty() {
        return textureColor;
    }

    public final Color getSimpleGradientBaseColor() {
        return simpleGradientBaseColor.get();
    }

    public final String getSimpleGradientBaseColorString() {
        final StringBuilder COLOR_STRING = new StringBuilder(30);
        COLOR_STRING.append("-fx-simplegradient-base: ");
        COLOR_STRING.append(Util.createCssColor(getSimpleGradientBaseColor()));
        return COLOR_STRING.toString();
    }

    public final void setSimpleGradientBaseColor(final Color SIMPLE_GRADIENT_BASE_COLOR) {
        simpleGradientBaseColor.set(SIMPLE_GRADIENT_BASE_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Color> simpleGradientBaseColorProperty() {
        return simpleGradientBaseColor;
    }

    public final boolean isTitleVisible() {
        return titleVisible.get();
    }

    public final void setTitleVisible(final boolean TITLE_VISIBLE) {
        titleVisible.set(TITLE_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty titleVisibleProperty() {
        return titleVisible;
    }

    public final boolean isUnitVisible() {
        return unitVisible.get();
    }

    public final void setUnitVisible(final boolean UNIT_VISIBLE) {
        unitVisible.set(UNIT_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty unitVisibleProperty() {
        return unitVisible;
    }

    public final boolean isTrendVisible() {
        return trendVisible.get();
    }

    public final void setTrendVisible(final boolean TREND_VISIBLE) {
        trendVisible.set(TREND_VISIBLE);
        fireStyleModelEvent();
    }

    public final BooleanProperty trendVisibleProperty() {
        return trendVisible;
    }

    public final Color getTrendUpColor() {
        return trendUpColor.get();
    }

    public final void setTrendUpColor(final Color TREND_UP_COLOR) {
        trendUpColor.set(TREND_UP_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Color> trendUpColorProperty() {
        return trendUpColor;
    }

    public final Color getTrendRisingColor() {
        return trendRisingColor.get();
    }

    public final void setTrendRisingColor(final Color TREND_RISING_COLOR) {
        trendRisingColor.set(TREND_RISING_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Color> trendRisingColorProperty() {
        return trendRisingColor;
    }

    public final Color getTrendSteadyColor() {
            return trendSteadyColor.get();
    }

    public final void setTrendSteadyColor(final Color TREND_STEADY_COLOR) {
        trendSteadyColor.set(TREND_STEADY_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Color> trendSteadyColorProperty() {
        return trendSteadyColor;
    }

    public final Color getTrendFallingColor() {
        return trendFallingColor.get();
    }

    public final void setTrendFallingColor(final Color TREND_FALLING_COLOR) {
        trendFallingColor.set(TREND_FALLING_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Color> trendFallingColorProperty() {
        return trendFallingColor;
    }

    public final Color getTrendDownColor() {
        return trendDownColor.get();
    }

    public final void setTrendDownColor(final Color TREND_DOWN_COLOR) {
        trendDownColor.set(TREND_DOWN_COLOR);
        fireStyleModelEvent();
    }

    public final ObjectProperty<Color> trendDownColorProperty() {
        return trendDownColor;
    }


    // ******************** Internal classes **********************************
    public class StyleModelEvent extends Event {

        // ******************** Constructors **************************************
        public StyleModelEvent() {
            super(new EventType<StyleModelEvent>());
        }

        public StyleModelEvent(final Object source, final EventTarget target) {
            super(source, target, new EventType<StyleModelEvent>());
        }
    }
}
