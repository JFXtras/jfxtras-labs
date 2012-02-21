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

import jfxtras.labs.scene.control.gauge.gauges.Gauge.BackgroundDesign;
import jfxtras.labs.scene.control.gauge.gauges.Gauge.FrameDesign;
import jfxtras.labs.scene.control.gauge.gauges.Gauge.KnobColor;
import jfxtras.labs.scene.control.gauge.gauges.Gauge.KnobDesign;
import jfxtras.labs.scene.control.gauge.gauges.Gauge.NumberFormat;
import jfxtras.labs.scene.control.gauge.gauges.Gauge.PointerType;
import jfxtras.labs.scene.control.gauge.gauges.Gauge.ThresholdColor;
import jfxtras.labs.scene.control.gauge.gauges.Gauge.TicklabelOrientation;
import jfxtras.labs.scene.control.gauge.gauges.Gauge.TickmarkType;
import jfxtras.labs.scene.control.gauge.gauges.Radial.ForegroundType;
import jfxtras.labs.scene.control.gauge.tools.Util;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;


/**
 * Created by
 * User: hansolo
 * Date: 29.01.12
 * Time: 09:47
 */
public class ViewModel {
    private BooleanProperty                      bargraph;
    private BooleanProperty                      minMeasuredValueVisible;
    private BooleanProperty                      maxMeasuredValueVisible;
    private BooleanProperty                      thresholdVisible;
    private ObjectProperty<ThresholdColor>       thresholdColor;
    private ObjectProperty<FrameDesign>          frameDesign;
    private BooleanProperty                      frameVisible;
    private ObjectProperty<BackgroundDesign>     backgroundDesign;
    private BooleanProperty                      backgroundVisible;
    private ObjectProperty<KnobDesign>           knobDesign;
    private ObjectProperty<KnobColor>            knobColor;
    private BooleanProperty                      postsVisible;
    private ObjectProperty<PointerType>          pointerType;
    private BooleanProperty                      pointerShadowVisible;
    private ObjectProperty<ColorDef>             valueColor;
    private BooleanProperty                      ledVisible;
    private ObjectProperty<LedColor>             ledColor;
    private BooleanProperty                      ledBlinking;
    private BooleanProperty                      userLedVisible;
    private BooleanProperty                      userLedOn;
    private ObjectProperty<LedColor>             userLedColor;
    private BooleanProperty                      userLedBlinking;
    private ObjectProperty<ForegroundType>       foregroundType;
    private BooleanProperty                      foregroundVisible;
    private BooleanProperty                      lcdThresholdVisible;
    private ObjectProperty<LcdDesign>            lcdDesign;
    private BooleanProperty                      lcdVisible;
    private BooleanProperty                      lcdUnitVisible;
    private BooleanProperty                      lcdDigitalFontEnabled;
    private StringProperty                       lcdUnitFont;
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
    private BooleanProperty                      minorTicksVisible;
    private BooleanProperty                      tickLabelsVisible;
    private ObjectProperty<TicklabelOrientation> tickLabelOrientation;
    private ObjectProperty<NumberFormat>         tickLabelNumberFormat;
    private ObjectProperty<Point2D>              tickmarksOffset;
    private BooleanProperty                      sectionsVisible;
    private BooleanProperty                      expandedSections;
    private BooleanProperty                      sectionsHighlighting;
    private BooleanProperty                      areasVisible;
    private BooleanProperty                      areasHighlighting;
    private BooleanProperty                      indicatorsVisible;
    private ObjectProperty<Color>                textureColor;
    private ObjectProperty<Color>                simpleGradientBaseColor;
    private BooleanProperty                      titleVisible;
    private BooleanProperty                      unitVisible;
    private BooleanProperty                      trendVisible;
    private ObjectProperty<Color>                trendUpColor;
    private ObjectProperty<Color>                trendSteadyColor;
    private ObjectProperty<Color>                trendDownColor;


    // ******************** Constructors **************************************
    public ViewModel() {
        bargraph                        = new SimpleBooleanProperty(false);
        minMeasuredValueVisible         = new SimpleBooleanProperty(false);
        maxMeasuredValueVisible         = new SimpleBooleanProperty(false);
        thresholdVisible                = new SimpleBooleanProperty(false);
        thresholdColor                  = new SimpleObjectProperty<>(Gauge.ThresholdColor.RED);
        frameDesign                     = new SimpleObjectProperty<>(Gauge.FrameDesign.METAL);
        frameVisible                    = new SimpleBooleanProperty(true);
        backgroundDesign                = new SimpleObjectProperty<>(Gauge.BackgroundDesign.DARK_GRAY);
        backgroundVisible               = new SimpleBooleanProperty(true);
        knobDesign                      = new SimpleObjectProperty<>(Gauge.KnobDesign.STANDARD);
        knobColor                       = new SimpleObjectProperty<>(Gauge.KnobColor.SILVER);
        postsVisible                    = new SimpleBooleanProperty(true);
        pointerType                     = new SimpleObjectProperty<>(Gauge.PointerType.TYPE1);
        valueColor                      = new SimpleObjectProperty<>(ColorDef.RED);
        pointerShadowVisible            = new SimpleBooleanProperty(true);
        ledVisible                      = new SimpleBooleanProperty(true);
        ledColor                        = new SimpleObjectProperty<>(LedColor.RED);
        ledBlinking                     = new SimpleBooleanProperty(false);
        userLedVisible                  = new SimpleBooleanProperty(false);
        userLedColor                    = new SimpleObjectProperty<>(LedColor.BLUE);
        userLedBlinking                 = new SimpleBooleanProperty(false);
        userLedOn                       = new SimpleBooleanProperty(false);
        foregroundType                  = new SimpleObjectProperty<>(Radial.ForegroundType.TYPE1);
        foregroundVisible               = new SimpleBooleanProperty(true);
        lcdThresholdVisible             = new SimpleBooleanProperty(false);
        lcdDesign                       = new SimpleObjectProperty<>(LcdDesign.WHITE);
        lcdVisible                      = new SimpleBooleanProperty(true);
        lcdUnitVisible                  = new SimpleBooleanProperty(false);
        lcdDigitalFontEnabled           = new SimpleBooleanProperty(false);
        lcdUnitFont                     = new SimpleStringProperty("Verdana");
        lcdDecimals                     = new SimpleIntegerProperty(0);
        lcdNumberSystemVisible          = new SimpleBooleanProperty(false);
        lcdBlinking                     = new SimpleBooleanProperty(false);
        glowVisible                     = new SimpleBooleanProperty(false);
        glowColor                       = new SimpleObjectProperty<>(Color.rgb(51, 255, 255));
        glowOn                          = new SimpleBooleanProperty(false);
        pulsatingGlow                   = new SimpleBooleanProperty(false);
        tickmarksVisible                = new SimpleBooleanProperty(true);
        majorTicksVisible               = new SimpleBooleanProperty(true);
        majorTickmarkType               = new SimpleObjectProperty<>(TickmarkType.LINE);
        minorTicksVisible               = new SimpleBooleanProperty(true);
        tickLabelsVisible               = new SimpleBooleanProperty(true);
        tickLabelOrientation            = new SimpleObjectProperty<>(Gauge.TicklabelOrientation.NORMAL);
        tickLabelNumberFormat           = new SimpleObjectProperty<>(Gauge.NumberFormat.AUTO);
        tickmarksOffset                 = new SimpleObjectProperty<>(new Point2D(0, 0));
        sectionsVisible                 = new SimpleBooleanProperty(false);
        expandedSections                = new SimpleBooleanProperty(false);
        sectionsHighlighting            = new SimpleBooleanProperty(false);
        areasVisible                    = new SimpleBooleanProperty(false);
        areasHighlighting               = new SimpleBooleanProperty(false);
        indicatorsVisible               = new SimpleBooleanProperty(false);
        textureColor                    = new SimpleObjectProperty<>(Color.rgb(104, 104, 104));
        simpleGradientBaseColor         = new SimpleObjectProperty<>(Color.rgb(213, 0, 0));
        titleVisible                    = new SimpleBooleanProperty(true);
        unitVisible                     = new SimpleBooleanProperty(true);
        trendVisible                    = new SimpleBooleanProperty(false);
        trendUpColor                    = new SimpleObjectProperty<>(Color.LIME);
        trendSteadyColor                = new SimpleObjectProperty<>(Color.LIGHTBLUE);
        trendDownColor                  = new SimpleObjectProperty<>(Color.RED);
    }


    // ******************** Event handling ************************************
    public final ObjectProperty<EventHandler<ViewModelEvent>> onViewModelEventProperty() {
        return onViewModelEvent;
    }

    public final void setOnViewModelEvent(final EventHandler<ViewModelEvent> HANDLER) {
        onViewModelEventProperty().set(HANDLER);
    }

    public final EventHandler<ViewModelEvent> getOnViewModelEvent() {
        return onViewModelEventProperty().get();
    }

    private ObjectProperty<EventHandler<ViewModelEvent>> onViewModelEvent = new SimpleObjectProperty<>();

    public void fireViewModelEvent() {
        final EventHandler<ViewModelEvent> VIEW_MODEL_EVENT_HANDLER = getOnViewModelEvent();
        if (VIEW_MODEL_EVENT_HANDLER != null) {
            final ViewModelEvent VIEW_MODEL_EVENT = new ViewModelEvent();
            VIEW_MODEL_EVENT_HANDLER.handle(VIEW_MODEL_EVENT);
        }
    }


    // ******************** Methods *******************************************
    public final boolean isBargraph() {
        return bargraph.get();
    }

    public final void setBargraph(final boolean BARGRAPH) {
        bargraph.set(BARGRAPH);
    }

    public final BooleanProperty bargraphProperty() {
        return bargraph;
    }

    public final boolean isMinMeasuredValueVisible() {
        return minMeasuredValueVisible.get();
    }

    public final void setMinMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE) {
        minMeasuredValueVisible.set(MIN_MEASURED_VALUE_VISIBLE);
    }

    public final BooleanProperty minMeasuredValueVisibleProperty() {
        return minMeasuredValueVisible;
    }

    public final boolean isMaxMeasuredValueVisible() {
        return maxMeasuredValueVisible.get();
    }

    public final void setMaxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE) {
        maxMeasuredValueVisible.set(MAX_MEASURED_VALUE_VISIBLE);
    }

    public final BooleanProperty maxMeasuredValueVisibleProperty() {
        return maxMeasuredValueVisible;
    }

    public final boolean isThresholdVisible() {
        return thresholdVisible.get();
    }

    public final void setThresholdVisible(final boolean THRESHOLD_VISIBLE) {
        thresholdVisible.set(THRESHOLD_VISIBLE);
    }

    public final BooleanProperty thresholdVisibleProperty() {
        return thresholdVisible;
    }

    public final Gauge.ThresholdColor getThresholdColor() {
        return thresholdColor.get();
    }

    public final void setThresholdColor(final Gauge.ThresholdColor THRESHOLD_COLOR) {
        thresholdColor.set(THRESHOLD_COLOR);
    }

    public final ObjectProperty<Gauge.ThresholdColor> thresholdColorProperty() {
        return thresholdColor;
    }

    public final Gauge.FrameDesign getFrameDesign() {
        return frameDesign.get();
    }

    public final void setFrameDesign(final Gauge.FrameDesign FRAME_DESIGN) {
        frameDesign.set(FRAME_DESIGN);
    }

    public final ObjectProperty<Gauge.FrameDesign> frameDesignProperty() {
        return frameDesign;
    }

    public final boolean isFrameVisible() {
        return frameVisible.get();
    }

    public final void setFrameVisible(final boolean FRAME_VISIBLE) {
        frameVisible.set(FRAME_VISIBLE);
    }

    public final BooleanProperty frameVisibleProperty() {
        return frameVisible;
    }

    public final Gauge.BackgroundDesign getBackgroundDesign() {
        return backgroundDesign.get();
    }

    public final void setBackgroundDesign(final Gauge.BackgroundDesign BACKGROUND_DESIGN) {
        backgroundDesign.set(BACKGROUND_DESIGN);
    }

    public final ObjectProperty<Gauge.BackgroundDesign> backgroundDesignProperty() {
        return backgroundDesign;
    }

    public final boolean isBackgroundVisible() {
        return backgroundVisible.get();
    }

    public final void setBackgroundVisible(final boolean BACKGROUND_VISIBLE) {
        backgroundVisible.set(BACKGROUND_VISIBLE);
    }

    public final BooleanProperty backgroundVisibleProperty() {
        return backgroundVisible;
    }

    public final Gauge.KnobDesign getKnobDesign() {
        return knobDesign.get();
    }

    public final void setKnobDesign(final Gauge.KnobDesign KNOB_DESIGN) {
        knobDesign.set(KNOB_DESIGN);
    }

    public final ObjectProperty<Gauge.KnobDesign> knobDesignProperty() {
        return knobDesign;
    }

    public final Gauge.KnobColor getKnobColor() {
        return knobColor.get();
    }

    public final void setKnobColor(final Gauge.KnobColor KNOB_COLOR) {
        knobColor.set(KNOB_COLOR);
    }

    public final ObjectProperty<Gauge.KnobColor> knobColorProperty() {
        return knobColor;
    }

    public final boolean isPostsVisible() {
        return postsVisible.get();
    }

    public final void setPostsVisible(final boolean POSTS_VISIBLE) {
        postsVisible.set(POSTS_VISIBLE);
    }

    public final BooleanProperty postsVisibleProperty() {
        return postsVisible;
    }

    public final Gauge.PointerType getPointerType() {
        return pointerType.get();
    }

    public final void setPointerType(final Gauge.PointerType POINTER_TYPE) {
        pointerType.set(POINTER_TYPE);
    }

    public final ObjectProperty<Gauge.PointerType> pointerTypeProperty() {
        return pointerType;
    }

    public final boolean isPointerShadowVisible() {
        return pointerShadowVisible.get();
    }

    public final void setPointerShadowVisible(final boolean POINTER_SHADOW_VISIBLE) {
        pointerShadowVisible.set(POINTER_SHADOW_VISIBLE);
    }

    public final BooleanProperty pointerShadowVisibleProperty() {
        return pointerShadowVisible;
    }

    public final ColorDef getValueColor() {
        return valueColor.get();
    }

    public final void setValueColor(final ColorDef VALUE_COLOR) {
        valueColor.set(VALUE_COLOR);
    }

    public final ObjectProperty<ColorDef> valueColorProperty() {
        return valueColor;
    }

    public final boolean isLedVisible() {
        return ledVisible.get();
    }

    public final void setLedVisible(final boolean LED_VISIBLE) {
        ledVisible.set(LED_VISIBLE);
    }

    public final BooleanProperty ledVisibleProperty() {
        return ledVisible;
    }

    public final LedColor getLedColor() {
        return ledColor.get();
    }

    public final void setLedColor(final LedColor LED_COLOR) {
        ledColor.set(LED_COLOR);
    }

    public final ObjectProperty<LedColor> ledColorProperty() {
        return ledColor;
    }

    public final boolean isLedBlinking() {
        return ledBlinking.get();
    }

    public final void setLedBlinking(final boolean LED_BLINKING) {
        ledBlinking.set(LED_BLINKING);
    }

    public final BooleanProperty ledBlinkingProperty() {
        return ledBlinking;
    }

    public final boolean isUserLedVisible() {
        return userLedVisible.get();
    }

    public final void setUserLedVisible(final boolean USER_LED_VISIBLE) {
        userLedVisible.set(USER_LED_VISIBLE);
    }

    public final BooleanProperty userLedVisibleProperty() {
        return userLedVisible;
    }

    public final LedColor getUserLedColor() {
        return userLedColor.get();
    }

    public final void setUserLedColor(final LedColor USER_LED_COLOR) {
        userLedColor.set(USER_LED_COLOR);
    }

    public final ObjectProperty<LedColor> userLedColorProperty() {
        return userLedColor;
    }

    public final boolean isUserLedOn() {
        return userLedOn.get();
    }

    public final void setUserLedOn(final boolean USER_LED_ON) {
        userLedOn.set(USER_LED_ON);
    }

    public final BooleanProperty userLedOnProperty() {
        return userLedOn;
    }

    public final boolean isUserLedBlinking() {
        return userLedBlinking.get();
    }

    public final void setUserLedBlinking(final boolean USER_LED_BLINKING) {
        userLedBlinking.set(USER_LED_BLINKING);
    }

    public final BooleanProperty userLedBlinkingProperty() {
        return userLedBlinking;
    }

    public final Radial.ForegroundType getForegroundType() {
        return foregroundType.get();
    }

    public final void setForegroundType(final Radial.ForegroundType FOREGROUND_TYPE) {
        foregroundType.set(FOREGROUND_TYPE);
    }

    public final ObjectProperty<Radial.ForegroundType> foregroundTypeProperty() {
        return foregroundType;
    }

    public final boolean isForegroundVisible() {
        return foregroundVisible.get();
    }

    public final void setForegroundVisible(final boolean FOREGROUND_VISIBLE) {
        foregroundVisible.set(FOREGROUND_VISIBLE);
    }

    public final BooleanProperty foregroundVisibleProperty() {
        return foregroundVisible;
    }

    public final boolean isLcdThresholdVisible() {
        return lcdThresholdVisible.get();
    }

    public final void setLcdThresholdVisible(final boolean LCD_THRESHOLD_VISIBLE) {
        lcdThresholdVisible.set(LCD_THRESHOLD_VISIBLE);
    }

    public final BooleanProperty lcdThresholdVisibleProperty() {
        return lcdThresholdVisible;
    }

    public final LcdDesign getLcdDesign() {
        return lcdDesign.get();
    }

    public final void setLcdDesign(final LcdDesign LCD_Design) {
        lcdDesign.set(LCD_Design);
    }

    public final ObjectProperty lcdDesignProperty() {
        return lcdDesign;
    }

    public final boolean isLcdVisible() {
        return lcdVisible.get();
    }

    public final void setLcdVisible(final boolean LCD_VISIBLE) {
        lcdVisible.set(LCD_VISIBLE);
    }

    public final BooleanProperty lcdVisibleProperty() {
        return lcdVisible;
    }

    public final boolean isLcdDigitalFontEnabled() {
        return lcdDigitalFontEnabled.get();
    }

    public final void setLcdDigitalFontEnabled(final boolean LCD_DIGITAL_FONT_ENABLED) {
        lcdDigitalFontEnabled.set(LCD_DIGITAL_FONT_ENABLED);
    }

    public final BooleanProperty lcdDigitalFontEnabledProperty() {
        return lcdDigitalFontEnabled;
    }

    public final boolean getLcdUnitVisible() {
        return lcdUnitVisible.get();
    }

    public final void setLcdUnitVisible(final boolean LCD_UNIT_STRING_VISIBLE) {
        lcdUnitVisible.set(LCD_UNIT_STRING_VISIBLE);
    }

    public final BooleanProperty lcdUnitVisibleProperty() {
        return lcdUnitVisible;
    }

    public final String getLcdUnitFont() {
        return lcdUnitFont.get();
    }

    public final void setLcdUnitFont(final String UNIT_FONT) {
        lcdUnitFont.set(UNIT_FONT);
    }

    public final StringProperty lcdUnitFontProperty() {
        return lcdUnitFont;
    }

    public final int getLcdDecimals() {
        return lcdDecimals.get();
    }

    public final void setLcdDecimals(final int LCD_DECIMALS) {
        final int DECIMALS = LCD_DECIMALS > 5 ? 5 : (LCD_DECIMALS < 0 ? 0 : LCD_DECIMALS);
        lcdDecimals.set(DECIMALS);
    }

    public final IntegerProperty lcdDecimalsProperty() {
        return lcdDecimals;
    }

    public final boolean isLcdNumberSystemVisible() {
        return lcdNumberSystemVisible.get();
    }

    public final void setLcdNumberSystemVisible(final boolean LCD_NUMBER_SYSTEM_VISIBLE) {
        lcdNumberSystemVisible.set(LCD_NUMBER_SYSTEM_VISIBLE);
    }

    public final BooleanProperty lcdNumberSystemVisibleProperty() {
        return lcdNumberSystemVisible;
    }

    public final boolean isLcdBlinking() {
        return lcdBlinking.get();
    }

    public final void setLcdBlinking(final boolean LCD_BLINKING) {
        lcdBlinking.set(LCD_BLINKING);
    }

    public final BooleanProperty lcdBlinkingProperty() {
        return lcdBlinking;
    }

    public final boolean isGlowVisible() {
        return glowVisible.get();
    }

    public final void setGlowVisible(final boolean GLOW_VISIBLE) {
        glowVisible.set(GLOW_VISIBLE);
    }

    public final BooleanProperty glowVisibleProperty() {
        return glowVisible;
    }

    public final Color getGlowColor() {
        return glowColor.get();
    }

    public final void setGlowColor(final Color GLOW_COLOR) {
        glowColor.set(GLOW_COLOR);
    }

    public final ObjectProperty<Color> glowColorProperty() {
        return glowColor;
    }

    public final boolean isGlowOn() {
        return glowOn.get();
    }

    public final void setGlowOn(final boolean GLOW_ON) {
        glowOn.set(GLOW_ON);
    }

    public final BooleanProperty glowOnProperty() {
        return glowOn;
    }

    public final boolean isPulsatingGlow() {
        return pulsatingGlow.get();
    }

    public final void setPulsatingGlow(final boolean PULSATING_GLOW) {
        pulsatingGlow.set(PULSATING_GLOW);
    }

    public final BooleanProperty pulsatingGlowProperty() {
        return pulsatingGlow;
    }

    public final boolean isTickmarksVisible() {
        return tickmarksVisible.get();
    }

    public final void setTickmarksVisible(final boolean TICKMARKS_VISIBLE) {
        tickmarksVisible.set(TICKMARKS_VISIBLE);
    }

    public final BooleanProperty tickmarksVisibleProperty() {
        return tickmarksVisible;
    }

    public final boolean isMajorTicksVisible() {
        return majorTicksVisible.get();
    }

    public final void setMajorTicksVisible(final boolean MAJOR_TICKS_VISIBLE) {
        majorTicksVisible.set(MAJOR_TICKS_VISIBLE);
    }

    public final BooleanProperty majorTicksVisibleProperty() {
        return majorTicksVisible;
    }

    public final TickmarkType getMajorTickmarkType() {
        return majorTickmarkType.get();
    }

    public final void setMajorTickmarkType(final TickmarkType TICKMARK_TYPE) {
        majorTickmarkType.set(TICKMARK_TYPE);
    }

    public final ObjectProperty<TickmarkType> majorTickmarkTypeProperty() {
        return majorTickmarkType;
    }

    public final boolean isMinorTicksVisible() {
        return minorTicksVisible.get();
    }

    public final void setMinorTicksVisible(final boolean MINOR_TICKS_VISIBLE) {
        minorTicksVisible.set(MINOR_TICKS_VISIBLE);
    }

    public final BooleanProperty minorTicksVisibleProperty() {
        return minorTicksVisible;
    }

    public final boolean isTickLabelsVisible() {
        return tickLabelsVisible.get();
    }

    public final void setTickLabelsVisible(final boolean TICKLABELS_VISIBLE) {
        tickLabelsVisible.set(TICKLABELS_VISIBLE);
    }

    public final BooleanProperty ticklabelsVisibleProperty() {
        return tickLabelsVisible;
    }

    public final Gauge.TicklabelOrientation getTickLabelOrientation() {
        return tickLabelOrientation.get();
    }

    public final void setTickLabelOrientation(final Gauge.TicklabelOrientation TICKLABEL_ORIENTATION) {
        tickLabelOrientation.set(TICKLABEL_ORIENTATION);
    }

    public final ObjectProperty<Gauge.TicklabelOrientation> tickLabelOrientationProperty() {
        return tickLabelOrientation;
    }

    public final Gauge.NumberFormat getTickLabelNumberFormat() {
        return tickLabelNumberFormat.get();
    }

    public final void setTickLabelNumberFormat(final Gauge.NumberFormat TICKLABEL_NUMBER_FORMAT) {
        tickLabelNumberFormat.set(TICKLABEL_NUMBER_FORMAT);
    }

    public final ObjectProperty<Gauge.NumberFormat> tickLabelNumberFormatProperty() {
        return tickLabelNumberFormat;
    }

    public final Point2D getTickmarksOffset() {
        return tickmarksOffset.get();
    }

    public final void setTickmarksOffset(final Point2D TICKMARKS_OFFSET) {
        tickmarksOffset.set(TICKMARKS_OFFSET);
    }

    public final ObjectProperty<Point2D> tickmarksOffsetProperty() {
        return tickmarksOffset;
    }

    public final boolean isSectionsVisible() {
        return sectionsVisible.get();
    }

    public final void setSectionsVisible(final boolean SECTIONS_VISIBLE) {
        sectionsVisible.set(SECTIONS_VISIBLE);
    }

    public final BooleanProperty sectionsVisibleProperty() {
        return sectionsVisible;
    }

    public final boolean isExpandedSections() {
        return expandedSections.get();
    }

    public final void setExpandedSections(final boolean EXPANDED_SECTIONS) {
        expandedSections.set(EXPANDED_SECTIONS);
    }

    public final BooleanProperty expandedSectionsProperty() {
        return expandedSections;
    }

    public final boolean isSectionsHighlighting() {
        return sectionsHighlighting.get();
    }

    public final void setSectionsHighlighting(final boolean SECTIONS_HIGHLIGHTING) {
        sectionsHighlighting.set(SECTIONS_HIGHLIGHTING);
    }

    public final BooleanProperty sectionsHighlightingProperty() {
        return sectionsHighlighting;
    }

    public final boolean isAreasVisible() {
        return areasVisible.get();
    }

    public final void setAreasVisible(final boolean AREAS_VISIBLE) {
        areasVisible.set(AREAS_VISIBLE);
    }

    public final BooleanProperty areasVisibleProperty() {
        return areasVisible;
    }

    public final boolean isAreasHighlighting() {
        return areasHighlighting.get();
    }

    public final void setAreasHighlighting(final boolean AREAS_HIGHLIGHTING) {
        areasHighlighting.set(AREAS_HIGHLIGHTING);
    }

    public final BooleanProperty areasHighlightingProperty() {
        return areasHighlighting;
    }

    public final boolean isIndicatorsVisible() {
        return indicatorsVisible.get();
    }

    public final void setIndicatorsVisible(final boolean INDICATORS_VISIBLE) {
        indicatorsVisible.set(INDICATORS_VISIBLE);
    }

    public final BooleanProperty indicatorsVisibleProperty() {
        return indicatorsVisible;
    }

    public final Color getTextureColor() {
        return textureColor.get();
    }

    public final String getTextureColorString() {
        final StringBuilder COLOR_STRING = new StringBuilder(30);
        COLOR_STRING.append("-fx-texture: ");
        COLOR_STRING.append(Util.INSTANCE.createCssColor(getTextureColor()));
        return COLOR_STRING.toString();
    }

    public final void setTextureColor(final Color TEXTURE_COLOR) {
        textureColor.set(TEXTURE_COLOR);
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
        COLOR_STRING.append(Util.INSTANCE.createCssColor(getSimpleGradientBaseColor()));
        return COLOR_STRING.toString();
    }

    public final void setSimpleGradientBaseColor(final Color SIMPLE_GRADIENT_BASE_COLOR) {
        simpleGradientBaseColor.set(SIMPLE_GRADIENT_BASE_COLOR);
    }

    public final ObjectProperty<Color> simpleGradientBaseColorProperty() {
        return simpleGradientBaseColor;
    }

    public final boolean isTitleVisible() {
        return titleVisible.get();
    }

    public final void setTitleVisible(final boolean TITLE_VISIBLE) {
        titleVisible.set(TITLE_VISIBLE);
    }

    public final BooleanProperty titleVisibleProperty() {
        return titleVisible;
    }

    public final boolean isUnitVisible() {
        return unitVisible.get();
    }

    public final void setUnitVisible(final boolean UNIT_VISIBLE) {
        unitVisible.set(UNIT_VISIBLE);
    }

    public final BooleanProperty unitVisibleProperty() {
        return unitVisible;
    }

    public final boolean isTrendVisible() {
        return trendVisible.get();
    }

    public final void setTrendVisible(final boolean TREND_VISIBLE) {
        trendVisible.set(TREND_VISIBLE);
    }

    public final BooleanProperty trendVisibleProperty() {
        return trendVisible;
    }

    public final Color getTrendUpColor() {
        return trendUpColor.get();
    }

    public final void setTrendUpColor(final Color TREND_UP_COLOR) {
        trendUpColor.set(TREND_UP_COLOR);
    }

    public final ObjectProperty<Color> trendUpColorProperty() {
        return trendUpColor;
    }

    public final Color getTrendSteadyColor() {
            return trendSteadyColor.get();
    }

    public final void setTrendSteadyColor(final Color TREND_STEADY_COLOR) {
        trendSteadyColor.set(TREND_STEADY_COLOR);
    }

    public final ObjectProperty<Color> trendSteadyColorProperty() {
        return trendSteadyColor;
    }

    public final Color getTrendDownColor() {
        return trendDownColor.get();
    }

    public final void setTrendDownColor(final Color TREND_DOWN_COLOR) {
        trendDownColor.set(TREND_DOWN_COLOR);
    }

    public final ObjectProperty<Color> trendDownColorProperty() {
        return trendDownColor;
    }
}
