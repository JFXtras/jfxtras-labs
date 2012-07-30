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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import jfxtras.labs.scene.control.gauge.Radial.ForegroundType;

import java.text.DecimalFormat;
import java.util.Locale;


/**
 * Created by
 * User: hansolo
 * Date: 02.01.12
 * Time: 17:14
 */
public abstract class Gauge extends Control {
    // ******************** Variable definitions ******************************
    private ObjectProperty<GaugeModel>  gaugeModelProperty;
    private ObjectProperty<StyleModel>  styleModelProperty;
    private GaugeModel                  gaugeModel;
    private StyleModel                  styleModel;
    private ObjectProperty<RadialRange> radialRange;
    private DoubleProperty              angleStep;


    // ******************** Enum definitions **********************************
    public static enum BackgroundDesign {
        DARK_GRAY("background-design-darkgray"),
        SATIN_GRAY("background-design-satingray"),
        LIGHT_GRAY("background-design-lightgray"),
        WHITE("background-design-white"),
        BLACK("background-design-black"),
        BEIGE("background-design-beige"),
        BROWN("background-design-brown"),
        RED("background-design-red"),
        GREEN("background-design-green"),
        BLUE("background-design-blue"),
        ANTHRACITE("background-design-anthracite"),
        MUD("background-design-mud"),
        CARBON("background-design-carbon"),
        STAINLESS("background-design-stainless"),
        //STAINLESS_GRINDED("background-design-stainlessgrinded"),
        //BRUSHED_METAL("background-design-brushedmetal"),
        PUNCHED_SHEET("background-design-punchedsheet"),
        //LINEN("background-design-linen"),
        NOISY_PLASTIC("backgroundd-design-noisyplastic"),
        SIMPLE_GRADIENT("background-design-simplegradient"),
        TRANSPARENT("background-design-transparent"),
        CUSTOM("background-design-custom");

        public final String CSS_BACKGROUND;
        public final String CSS_TEXT;

        BackgroundDesign(final String CSS_BACKGROUND) {
            this.CSS_BACKGROUND = CSS_BACKGROUND;
            this.CSS_TEXT = CSS_BACKGROUND + "-text";
        }
    }
    public static enum FrameDesign {
        BLACK_METAL("frame-design-blackmetal"),
        SHINY_METAL("frame-design-shinymetal"),
        CHROME("frame-design-chrome"),
        METAL("frame-design-metal"),
        GLOSSY_METAL("frame-design-glossymetal"),
        DARK_GLOSSY("frame-design-darkglossy"),
        BRASS("frame-design-brass"),
        STEEL("frame-design-steel"),
        GOLD("frame-design-gold"),
        ANTHRACITE("frame-design-anthracite"),
        TILTED_GRAY("frame-design-tiltedgray"),
        TILTED_BLACK("frame-design-tiltedblack"),
        CUSTOM("frame-design-custom");

        public final String CSS;

        FrameDesign(final String CSS) {
            this.CSS = CSS;
        }
    }
    public static enum KnobColor {
            BLACK,
            BRASS,
            SILVER
        }
    public static enum KnobDesign {
        STANDARD,
        PLAIN,
        METAL,
        BIG
    }
    public static enum LcdFont {
        STANDARD,
        LCD,
        BUS,
        PIXEL,
        PHONE_LCD
    }
    public static enum NumberFormat {
        AUTO("0"),
        STANDARD("0"),
        FRACTIONAL("0.0#"),
        SCIENTIFIC("0.##E0"),
        PERCENTAGE("##0.0%");

        private final DecimalFormat DF;

        private NumberFormat(final String FORMAT_STRING) {
            Locale.setDefault(new Locale("en", "US"));

            DF = new DecimalFormat(FORMAT_STRING);
        }

        public String format(final Number NUMBER) {
            return DF.format(NUMBER);
        }
    }
    public static enum NumberSystem {
        DECIMAL("dec"),
        HEXADECIMAL("hex"),
        OCTAL("oct");

        private String text;

        private NumberSystem(final String TEXT) {
            text = TEXT;
        }

        @Override public String toString() {
            return text;
        }
    }
    public static enum PointerType {
        TYPE1,
        TYPE2,
        TYPE3,
        TYPE4,
        TYPE5,
        TYPE6,
        TYPE7,
        TYPE8,
        TYPE9,
        TYPE10,
        TYPE11,
        TYPE12,
        TYPE13,
        TYPE14,
        TYPE15,
        TYPE16
    }
    public static enum RadialRange {
        RADIAL_360(360, 0, 0, new Rectangle(0.4, 0.56, 0.4, 0.12), 0, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_300(300, -150, 240, new Rectangle(0.4, 0.56, 0.4, 0.12), 150, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_270(270, -180, 270, new Rectangle(0.4, 0.56, 0.4, 0.12), 180, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_180(180, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 90, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_180N(180, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 90, new Point2D(0.6, 0.35), new Point2D(0.3, 0.35), 1, 0.38),
        RADIAL_180S(180, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 0, new Point2D(0.6, 0.2), new Point2D(0.3, 0.2), -1, 0.38),
        RADIAL_90(90, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 91, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_90N(90, 315, 225, new Rectangle(0.55, 0.52, 0.55, 0.12), 45, new Point2D(0.6, 0.4), new Point2D(0.3, 0.4), 1, 0.5),
        RADIAL_90W(90, 225, 45, new Rectangle(0.2, 0.58, 0.45, 0.12), 135, new Point2D(0.12, 0.35), new Point2D(0.12, 0.55), 1, 0.5),
        RADIAL_90S(90, -135, 45, new Rectangle(0.55, 0.36, 0.55, 0.12), 225, new Point2D(0.6, 0.5), new Point2D(0.3, 0.5), -1, 0.5),
        RADIAL_90E(90, 135, 225, new Rectangle(0.2, 0.58, 0.45, 0.12), -315, new Point2D(0.78, 0.35), new Point2D(0.78, 0.55), -1, 0.5);

        public final double    ANGLE_RANGE;
        public final double    ROTATION_OFFSET;
        public final double    SECTIONS_OFFSET;
        public final Rectangle LCD_FACTORS;
        public final double    TICKLABEL_ORIENATION_CHANGE_ANGLE;
        public final Point2D   LED_POSITION;
        public final Point2D   USER_LED_POSITION;
        public final double    ANGLE_STEP_SIGN;
        public final double    RADIUS_FACTOR;

        private RadialRange(final double ANGLE_RANGE,
                            final double ROTATION_OFFSET,
                            final double SECTIONS_OFFSET,
                            final Rectangle LCD_FACTORS,
                            final double TICKLABEL_ORIENATION_CHANGE_ANGLE,
                            final Point2D LED_POSITION,
                            final Point2D USER_LED_POSITION,
                            final double ANGLE_STEP_SIGN,
                            final double RADIUS_FACTOR) {
            this.ANGLE_RANGE                       = ANGLE_RANGE;
            this.ROTATION_OFFSET                   = ROTATION_OFFSET;
            this.SECTIONS_OFFSET                   = SECTIONS_OFFSET;
            this.LCD_FACTORS                       = LCD_FACTORS;
            this.TICKLABEL_ORIENATION_CHANGE_ANGLE = TICKLABEL_ORIENATION_CHANGE_ANGLE;
            this.LED_POSITION                      = LED_POSITION;
            this.USER_LED_POSITION                 = USER_LED_POSITION;
            this.ANGLE_STEP_SIGN                   = ANGLE_STEP_SIGN;
            this.RADIUS_FACTOR                     = RADIUS_FACTOR;
        }
    }
    public static enum ThresholdColor {
        RED("-fx-red;"),
        GREEN("-fx-green;"),
        BLUE("-fx-blue;"),
        ORANGE("-fx-orange;"),
        YELLOW("-fx-yellow;"),
        CYAN("-fx-cyan;"),
        MAGENTA("-fx-magenta;"),
        WHITE("-fx-white;"),
        GRAY("-fx-gray;"),
        BLACK("-fx-black;"),
        RAITH("-fx-raith;"),
        GREEN_LCD("-fx-green-lcd;"),
        JUG_GREEN("-fx-jug-green;"),
        CUSTOM("-fx-custom;");

        public final String CSS;

        ThresholdColor(final String CSS_COLOR) {
            this.CSS = "-fx-threshold: " + CSS_COLOR;
        }
    }
    public static enum TickmarkType {
        LINE,
        TRIANGLE
    }
    public static enum TicklabelOrientation {
            NORMAL,
            HORIZONTAL,
            TANGENT
        }
    public static enum Trend {
        UP,
        RISING,
        STEADY,
        FALLING,
        DOWN,
        UNKNOWN;
    }


    // ******************** Constructors **************************************
    protected Gauge() {
        this(new GaugeModel(), new StyleModel());
    }

    protected Gauge(final GaugeModel GAUGE_MODEL) {
        this(GAUGE_MODEL, new StyleModel());
    }

    protected Gauge(final StyleModel STYLE_MODEL) {
        this(new GaugeModel(), STYLE_MODEL);
    }

    protected Gauge(final GaugeModel GAUGE_MODEL, final StyleModel STYLE_MODEL) {
        gaugeModelProperty = new SimpleObjectProperty<GaugeModel>(GAUGE_MODEL);
        styleModelProperty = new SimpleObjectProperty<StyleModel>(STYLE_MODEL);
        gaugeModel         = gaugeModelProperty.get();
        styleModel         = styleModelProperty.get();
        radialRange        = new SimpleObjectProperty<RadialRange>(RadialRange.RADIAL_300);
        angleStep          = new SimpleDoubleProperty(radialRange.get().ANGLE_RANGE / gaugeModel.getRange());
        ledBlinkingProperty().bind(thresholdExceededProperty());
        addGaugeModelListener();
        addStyleModelListener();
    }


    // ******************** Initialization ************************************
    public abstract void init();


    // ******************** Event handling ************************************
    private final void addGaugeModelListener() {
        gaugeModel.setOnGaugeModelEvent(new EventHandler<GaugeModel.GaugeModelEvent>() {
            public void handle(final GaugeModel.GaugeModelEvent EVENT) {
                forwardModelEvent(EVENT);
            }
        });
    }

    public final ObjectProperty<EventHandler<GaugeModel.GaugeModelEvent>> onGaugeModelEventProperty() {
        return onGaugeModelEvent;
    }

    public final void setOnGaugeModelEvent(final EventHandler<GaugeModel.GaugeModelEvent> HANDLER) {
        onGaugeModelEventProperty().set(HANDLER);
    }

    public final EventHandler<GaugeModel.GaugeModelEvent> getOnGaugeModelEvent() {
        return onGaugeModelEventProperty().get();
    }

    private final ObjectProperty<EventHandler<GaugeModel.GaugeModelEvent>> onGaugeModelEvent = new SimpleObjectProperty<EventHandler<GaugeModel.GaugeModelEvent>>();

    public void forwardModelEvent(final GaugeModel.GaugeModelEvent EVENT) {
        final EventHandler<GaugeModel.GaugeModelEvent> MODEL_EVENT_HANDLER = getOnGaugeModelEvent();
        if (MODEL_EVENT_HANDLER != null) {
            MODEL_EVENT_HANDLER.handle(EVENT);
        }
    }

    private final void addStyleModelListener() {
        styleModel.setOnStyleModelEvent(new EventHandler<StyleModel.StyleModelEvent>() {
            public void handle(final StyleModel.StyleModelEvent EVENT) {
                forwardStyleModelEvent(EVENT);
            }
        });
    }

    public final ObjectProperty<EventHandler<StyleModel.StyleModelEvent>> onStyleModelEventProperty() {
        return onStyleModelEvent;
    }

    public final void setOnStyleModelEvent(final EventHandler<StyleModel.StyleModelEvent> HANDLER) {
        onStyleModelEventProperty().set(HANDLER);
    }

    public final EventHandler<StyleModel.StyleModelEvent> getOnStyleModelEvent() {
        return onStyleModelEventProperty().get();
    }

    private final ObjectProperty<EventHandler<StyleModel.StyleModelEvent>> onStyleModelEvent = new SimpleObjectProperty<EventHandler<StyleModel.StyleModelEvent>>();

    public void forwardStyleModelEvent(final StyleModel.StyleModelEvent EVENT) {
        final EventHandler<StyleModel.StyleModelEvent> STYLE_MODEL_EVENT_HANDLER = getOnStyleModelEvent();
        if (STYLE_MODEL_EVENT_HANDLER != null) {
            STYLE_MODEL_EVENT_HANDLER.handle(EVENT);
        }
    }


    // ******************** Stylesheet handling *******************************
    @Override public String getUserAgentStylesheet() {
        return getClass().getResource("steelseries.css").toExternalForm();
    }


    // ******************** Gauge Methods *************************************
    public final StyleModel getStyleModel() {
        return styleModelProperty.get();
    }

    public final void setStyleModel(final StyleModel STYLE_MODEL) {
        styleModelProperty.set(STYLE_MODEL);
        styleModel = styleModelProperty().get();
        addStyleModelListener();
    }

    public final ReadOnlyObjectProperty<StyleModel> styleModelProperty() {
            return styleModelProperty;
        }

    public final GaugeModel getGaugeModel() {
        return gaugeModelProperty.get();
    }

    public final void setGaugeModel(final GaugeModel GAUGE_MODEL) {
        gaugeModelProperty.set(GAUGE_MODEL);
        gaugeModel = gaugeModelProperty.get();
        addGaugeModelListener();
    }

    public final ReadOnlyObjectProperty<GaugeModel> gaugeModelProperty() {
        return gaugeModelProperty;
    }

    public final Gauge.RadialRange getRadialRange() {
        return radialRange.get();
    }

    public void setRadialRange(final RadialRange RADIAL_RANGE) {
        radialRange.set(RADIAL_RANGE);
        gaugeModel.calcRange(radialRange.get().ANGLE_RANGE);
        angleStep.set(radialRange.get().ANGLE_RANGE / gaugeModel.getRange());
        if (RADIAL_RANGE == RadialRange.RADIAL_360) {
            setKnobsVisible(false);
            setEndlessMode(true);
        } else {
            setEndlessMode(false);
        }
    }

    public final ObjectProperty<RadialRange> radialRangeProperty() {
        return radialRange;
    }

    public final double getAngleStep() {
        return angleStep.get();
    }

    public final void recalcRange() {
        gaugeModel.calcRange(radialRange.get().ANGLE_RANGE);
        angleStep.set(radialRange.get().ANGLE_RANGE / gaugeModel.getRange());
    }

    public final ReadOnlyDoubleProperty angleStepProperty() {
        return angleStep;
    }

    public final Point2D getLedPosition() {
        return radialRange.get().LED_POSITION;
    }

    public final Point2D getUserLedPosition() {
        return radialRange.get().USER_LED_POSITION;
    }


    // ******************** GaugeModel Methods ********************************
    public final double getValue() {
        return gaugeModel.getValue();
    }

    public final void setValue(final double VALUE) {
        gaugeModel.setValue(VALUE);
    }

    public final DoubleProperty valueProperty() {
        return gaugeModel.valueProperty();
    }

    public final double getRealValue() {
        return gaugeModel.getRealValue();
    }

    public final ReadOnlyDoubleProperty realValueProperty() {
        return gaugeModel.realValueProperty();
    }

    public final boolean isValueAnimationEnabled() {
        return gaugeModel.isValueAnimationEnabled();
    }

    public final void setValueAnimationEnabled(final boolean VALUE_ANIMATION_ENABLED) {
        gaugeModel.setValueAnimationEnabled(VALUE_ANIMATION_ENABLED);
    }

    public final BooleanProperty valueAnimationEnabledProperty() {
        return gaugeModel.valueAnimationEnabledProperty();
    }

    public final double getAnimationDuration() {
        return gaugeModel.getAnimationDuration();
    }

    public final void setAnimationDuration(final double ANIMATION_DURATION) {
        gaugeModel.setAnimationDuration(ANIMATION_DURATION);
    }

    public final DoubleProperty animationDurationProperty() {
        return gaugeModel.animationDurationProperty();
    }

    public final double getRedrawTolerance() {
        return gaugeModel.getRedrawTolerance();
    }

    public final void setRedrawTolerance(final double REDRAW_TOLERANCE) {
        gaugeModel.setRedrawTolerance(REDRAW_TOLERANCE);
    }

    public final DoubleProperty redrawToleranceProperty() {
        return gaugeModel.redrawToleranceProperty();
    }

    public final double getRedrawToleranceValue() {
        return gaugeModel.getRedrawToleranceValue();
    }

    public final double getMinValue() {
        return gaugeModel.getMinValue();
    }

    public final void setMinValue(final double MIN_VALUE) {
        gaugeModel.setMinValue(MIN_VALUE);
        gaugeModel.calcRange(radialRange.get().ANGLE_RANGE);
        angleStep.set(radialRange.get().ANGLE_RANGE / gaugeModel.getRange());
    }

    public final ReadOnlyDoubleProperty minValueProperty() {
        return gaugeModel.minValueProperty();
    }

    public final double getMaxValue() {
        return gaugeModel.getMaxValue();
    }

    public final void setMaxValue(final double MAX_VALUE) {
        gaugeModel.setMaxValue(MAX_VALUE);
        gaugeModel.calcRange(radialRange.get().ANGLE_RANGE);
        angleStep.set(radialRange.get().ANGLE_RANGE / gaugeModel.getRange());
    }

    public final ReadOnlyDoubleProperty maxValueProperty() {
        return gaugeModel.maxValueProperty();
    }

    public final double getRange() {
        return gaugeModel.getRange();
    }

    public final ReadOnlyDoubleProperty rangeProperty() {
        return gaugeModel.rangeProperty();
    }

    public final double getNiceMinValue() {
        return gaugeModel.getNiceMinValue();
    }

    public final ReadOnlyDoubleProperty niceMinValueProperty() {
        return gaugeModel.niceMinValueProperty();
    }

    public final double getNiceMaxValue() {
        return gaugeModel.getNiceMaxValue();
    }

    public final ReadOnlyDoubleProperty niceMaxValueProperty() {
        return gaugeModel.niceMaxValueProperty();
    }

    public final double getNiceRange() {
        return gaugeModel.getNiceRange();
    }

    public final ReadOnlyDoubleProperty niceRangeProperty() {
        return gaugeModel.niceRangeProperty();
    }

    public final double getMinMeasuredValue() {
        return gaugeModel.getMinMeasuredValue();
    }

    public final void setMinMeasuredValue(final double MIN_MEASURED_VALUE) {
        gaugeModel.setMinMeasuredValue(MIN_MEASURED_VALUE);
    }

    public final DoubleProperty minMeasuredValueProperty() {
        return gaugeModel.minMeasuredValueProperty();
    }

    public final boolean isBargraph() {
        return styleModel.isBargraph();
    }

    public final void setBargraph(final boolean BARGRAPH) {
        styleModel.setBargraph(BARGRAPH);
    }

    public final BooleanProperty bargraphProperty() {
        return styleModel.bargraphProperty();
    }

    public final boolean isMinMeasuredValueVisible() {
        return styleModel.isMinMeasuredValueVisible();
    }

    public final void setMinMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE) {
        styleModel.setMinMeasuredValueVisible(MIN_MEASURED_VALUE_VISIBLE);
    }

    public final BooleanProperty minMeasuredValueVisibleProperty() {
        return styleModel.minMeasuredValueVisibleProperty();
    }

    public final void resetMinMeasuredValue() {
        gaugeModel.resetMinMeasuredValue();
    }

    public final double getMaxMeasuredValue() {
        return gaugeModel.getMaxMeasuredValue();
    }

    public final void setMaxMeasuredValue(final double MAX_MEASURED_VALUE) {
        gaugeModel.setMaxMeasuredValue(MAX_MEASURED_VALUE);
    }

    public final DoubleProperty maxMeasuredValueProperty() {
        return gaugeModel.maxMeasuredValueProperty();
    }

    public final boolean isMaxMeasuredValueVisible() {
        return styleModel.isMaxMeasuredValueVisible();
    }

    public final void setMaxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE) {
        styleModel.setMaxMeasuredValueVisible(MAX_MEASURED_VALUE_VISIBLE);
    }

    public final BooleanProperty maxMeasuredValueVisibleProperty() {
        return styleModel.maxMeasuredValueVisibleProperty();
    }

    public final void resetMaxMeasuredValue() {
        gaugeModel.resetMaxMeasuredValue();
    }

    public final void resetMinMaxMeasuredValue() {
        gaugeModel.resetMinMaxMeasuredValue();
    }

    public final double getThreshold() {
        return gaugeModel.getThreshold();
    }

    public final void setThreshold(final double THRESHOLD) {
        gaugeModel.setThreshold(THRESHOLD);
    }

    public final DoubleProperty thresholdProperty() {
        return gaugeModel.thresholdProperty();
    }

    public final boolean isThresholdBehaviorInverted() {
        return gaugeModel.isThresholdBehaviorInverted();
    }

    public final void setThresholdBehaviorInverted(final boolean THRESHOLD_BEHAVIOR_INVERTED) {
        gaugeModel.setThresholdBehaviorInverted(THRESHOLD_BEHAVIOR_INVERTED);
    }

    public final BooleanProperty thresholdBehaviorInvertedProperty() {
        return gaugeModel.thresholdBehaviorInvertedProperty();
    }

    public final boolean isThresholdExceeded() {
        return gaugeModel.isThresholdExceeded();
    }

    public final void setThresholdExceeded(final boolean THRESHOLD_EXCEEDED) {
        gaugeModel.setThresholdExceeded(THRESHOLD_EXCEEDED);
    }

    public final BooleanProperty thresholdExceededProperty() {
        return gaugeModel.thresholdExceededProperty();
    }

    public final boolean isThresholdVisible() {
        return styleModel.isThresholdVisible();
    }

    public final void setThresholdVisible(final boolean THRESHOLD_VISIBLE) {
        styleModel.setThresholdVisible(THRESHOLD_VISIBLE);
    }

    public final BooleanProperty thresholdVisibleProperty() {
        return styleModel.thresholdVisibleProperty();
    }

    public final ThresholdColor getThresholdColor() {
        return styleModel.getThresholdColor();
    }

    public final void setThresholdColor(final ThresholdColor THRESHOLD_COLOR) {
        styleModel.setThresholdColor(THRESHOLD_COLOR);
    }

    public final ObjectProperty<ThresholdColor> thresholdColorProperty() {
        return styleModel.thresholdColorProperty();
    }

    public final String getTitle() {
        return gaugeModel.getTitle();
    }

    public final void setTitle(final String TITLE) {
        gaugeModel.setTitle(TITLE);
    }

    public final StringProperty titleProperty() {
        return gaugeModel.titleProperty();
    }

    public final String getUnit() {
        return gaugeModel.getUnit();
    }

    public final void setUnit(final String UNIT) {
        gaugeModel.setUnit(UNIT);
    }

    public final StringProperty unitProperty() {
        return gaugeModel.unitProperty();
    }


    // ******************** StyleModel Methods ********************************
    public final FrameDesign getFrameDesign() {
        return styleModel.getFrameDesign();
    }

    public final void setFrameDesign(final FrameDesign FRAME_DESIGN) {
        styleModel.setFrameDesign(FRAME_DESIGN);
    }

    public final ObjectProperty<FrameDesign> frameDesignProperty() {
        return styleModel.frameDesignProperty();
    }

    public final Color getFrameBaseColor() {
        return styleModel.getFrameBaseColor();
    }

    public final void setFrameBaseColor(final Color FRAME_BASE_COLOR) {
        styleModel.setFrameBaseColor(FRAME_BASE_COLOR);
    }

    public final ObjectProperty<Color> frameBaseColorProperty() {
        return styleModel.frameBaseColorProperty();
    }

    public final boolean isFrameVisible() {
        return styleModel.isFrameVisible();
    }

    public final void setFrameVisible(final boolean FRAME_VISIBLE) {
        styleModel.setFrameVisible(FRAME_VISIBLE);
    }

    public final BooleanProperty frameVisibleProperty() {
        return styleModel.frameVisibleProperty();
    }

    public final BackgroundDesign getBackgroundDesign() {
        return styleModel.getBackgroundDesign();
    }

    public final void setBackgroundDesign(final BackgroundDesign BACKGROUND_DESIGN) {
        styleModel.setBackgroundDesign(BACKGROUND_DESIGN);
    }

    public final ObjectProperty<BackgroundDesign> backgroundDesignProperty() {
        return styleModel.backgroundDesignProperty();
    }

    public final boolean isBackgroundVisible() {
        return styleModel.isBackgroundVisible();
    }

    public final void setBackgroundVisible(final boolean BACKGROUND_VISIBLE) {
        styleModel.setBackgroundVisible(BACKGROUND_VISIBLE);
    }

    public final BooleanProperty backgroundVisibleProperty() {
        return styleModel.backgroundVisibleProperty();
    }

    public final KnobDesign getKnobDesign() {
        return styleModel.getKnobDesign();
    }

    public final void setKnobDesign(final KnobDesign KNOB_DESIGN) {
        styleModel.setKnobDesign(KNOB_DESIGN);
    }

    public final ObjectProperty<KnobDesign> knobDesignProperty() {
        return styleModel.knobDesignProperty();
    }

    public final KnobColor getKnobColor() {
        return styleModel.getKnobColor();
    }

    public final void setKnobColor(final KnobColor KNOB_COLOR) {
        styleModel.setKnobColor(KNOB_COLOR);
    }

    public final ObjectProperty<KnobColor> knobColorProperty() {
        return styleModel.knobColorProperty();
    }

    public final boolean isKnobsVisible() {
        return styleModel.getKnobsVisible();
    }

    public final void setKnobsVisible(final boolean POSTS_VISIBLE) {
        styleModel.setKnobsVisible(POSTS_VISIBLE);
    }

    public final BooleanProperty knobsVisibleProperty() {
        return styleModel.knobsVisibleProperty();
    }

    public final PointerType getPointerType() {
        return styleModel.getPointerType();
    }

    public final void setPointerType(final PointerType POINTER_TYPE) {
        styleModel.setPointerType(POINTER_TYPE);
    }

    public final ObjectProperty<PointerType> pointerTypeProperty() {
        return styleModel.pointerTypeProperty();
    }

    public final ColorDef getValueColor() {
        return styleModel.getValueColor();
    }

    public final void setValueColor(final ColorDef VALUE_COLOR) {
        styleModel.setValueColor(VALUE_COLOR);
    }

    public final ObjectProperty<ColorDef> valueColorProperty() {
        return styleModel.valueColorProperty();
    }

    public final boolean isPointerGlowEnabled() {
        return styleModel.isPointerGlowEnabled();
    }

    public final void setPointerGlowEnabled(final boolean POINTER_GLOW_ENABLED) {
        styleModel.setPointerGlowEnabled(POINTER_GLOW_ENABLED);
    }

    public final BooleanProperty pointerGlowEnabledProperty() {
        return styleModel.pointerGlowEnabledProperty();
    }

    public final boolean isPointerShadowEnabled() {
        return styleModel.isPointerShadowEnabled();
    }

    public final void setPointerShadowEnabled(final boolean POINTER_SHADOW_ENABLED) {
        styleModel.setPointerShadowEnabled(POINTER_SHADOW_ENABLED);
    }

    public final BooleanProperty pointerShadowEnabledProperty() {
        return styleModel.pointerShadowEnabledProperty();
    }

    public final boolean isLedVisible() {
        return styleModel.isLedVisible();
    }

    public final void setLedVisible(final boolean LED_VISIBLE) {
        styleModel.setLedVisible(LED_VISIBLE);
    }

    public final BooleanProperty ledVisibleProperty() {
        return styleModel.ledVisibleProperty();
    }

    public final LedColor getLedColor() {
        return styleModel.getLedColor();
    }

    public final void setLedColor(final LedColor LED_COLOR) {
        styleModel.setLedColor(LED_COLOR);
    }

    public final ObjectProperty<LedColor> ledColorProperty() {
        return styleModel.ledColorProperty();
    }

    public final boolean isLedBlinking() {
        return styleModel.isLedBlinking();
    }

    public final void setLedBlinking(final boolean LED_BLINKING) {
        styleModel.setLedBlinking(LED_BLINKING);
    }

    public final BooleanProperty ledBlinkingProperty() {
        return styleModel.ledBlinkingProperty();
    }

    public final boolean isUserLedVisible() {
        return styleModel.isUserLedVisible();
    }

    public final void setUserLedVisible(final boolean USER_LED_VISIBLE) {
        styleModel.setUserLedVisible(USER_LED_VISIBLE);
    }

    public final BooleanProperty userLedVisibleProperty() {
        return styleModel.userLedVisibleProperty();
    }

    public final LedColor getUserLedColor() {
        return styleModel.getUserLedColor();
    }

    public final void setUserLedColor(final LedColor USER_LED_COLOR) {
        styleModel.setUserLedColor(USER_LED_COLOR);
    }

    public final ObjectProperty<LedColor> userLedColorProperty() {
        return styleModel.userLedColorProperty();
    }

    public final boolean isUserLedOn() {
        return styleModel.isUserLedOn();
    }

    public final void setUserLedOn(final boolean USER_LED_ON) {
        styleModel.setUserLedOn(USER_LED_ON);
    }

    public final BooleanProperty userLedOnProperty() {
        return styleModel.userLedOnProperty();
    }

    public final boolean isUserLedBlinking() {
        return styleModel.isUserLedBlinking();
    }

    public final void setUserLedBlinking(final boolean USER_LED_BLINKING) {
        styleModel.setUserLedBlinking(USER_LED_BLINKING);
    }

    public final BooleanProperty userLedBlinkingProperty() {
        return styleModel.userLedBlinkingProperty();
    }

    public final String getTitleFont() {
        return styleModel.getTitleFont();
    }

    public final void setTitleFont(final String TITLE_FONT) {
        styleModel.setTitleFont(TITLE_FONT);
    }

    public final StringProperty titleFontProperty() {
        return styleModel.titleFontProperty();
    }

    public final String getUnitFont() {
        return styleModel.getUnitFont();
    }

    public final void setUnitFont(final String UNIT_FONT) {
        styleModel.setUnitFont(UNIT_FONT);
    }

    public final StringProperty unitFontProperty() {
        return styleModel.unitFontProperty();
    }

    public final ForegroundType getForegroundType() {
        return styleModel.getForegroundType();
    }

    public final void setForegroundType(final ForegroundType FOREGROUND_TYPE) {
        styleModel.setForegroundType(FOREGROUND_TYPE);
    }

    public final ObjectProperty<ForegroundType> foregroundTypeProperty() {
        return styleModel.foregroundTypeProperty();
    }

    public final boolean isForegroundVisible() {
        return styleModel.isForegroundVisible();
    }

    public final void setForegroundVisible(final boolean FOREGROUND_VISIBLE) {
        styleModel.setForegroundVisible(FOREGROUND_VISIBLE);
    }

    public final BooleanProperty foregroundVisibleProperty() {
        return styleModel.foregroundVisibleProperty();
    }

    public final double getLcdValue() {
            return gaugeModel.getLcdValue();
        }

    public final void setLcdValue(final double LCD_VALUE) {
        gaugeModel.setLcdValue(LCD_VALUE);
    }

    public final DoubleProperty lcdValueProperty() {
        return gaugeModel.lcdValueProperty();
    }

    public final boolean isLcdValueCoupled() {
        return gaugeModel.isLcdValueCoupled();
    }

    public final void setLcdValueCoupled(final boolean LCD_VALUE_COUPLED) {
        gaugeModel.setLcdValueCoupled(LCD_VALUE_COUPLED);
    }

    public final BooleanProperty lcdValueCoupledProperty() {
        return gaugeModel.lcdValueCoupledProperty();
    }

    public final double getLcdThreshold() {
        return gaugeModel.getLcdThreshold();
    }

    public final void setLcdThreshold(final double LCD_THRESHOLD) {
        gaugeModel.setLcdThreshold(LCD_THRESHOLD);
    }

    public final DoubleProperty lcdThresholdProperty() {
        return gaugeModel.lcdThresholdProperty();
    }

    public final boolean isLcdThresholdBehaviorInverted() {
        return gaugeModel.isLcdThresholdBehaviorInverted();
    }

    public final void setLcdThresholdBehaviorInverted(final boolean LCD_THRESHOLD_BEHAVIOR_INVERTED) {
        gaugeModel.setLcdThresholdBehaviorInverted(LCD_THRESHOLD_BEHAVIOR_INVERTED);
    }

    public final BooleanProperty lcdThresholdBehaviorInvertedProperty() {
        return gaugeModel.lcdThresholdBehaviorInvertedProperty();
    }

    public final boolean isLcdThresholdVisible() {
        return styleModel.isLcdThresholdVisible();
    }

    public final void setLcdThresholdVisible(final boolean LCD_THRESHOLD_VISIBLE) {
        styleModel.setLcdThresholdVisible(LCD_THRESHOLD_VISIBLE);
    }

    public final BooleanProperty lcdThresholdVisibleProperty() {
        return styleModel.lcdThresholdVisibleProperty();
    }

    public final LcdDesign getLcdDesign() {
        return styleModel.getLcdDesign();
    }

    public final void setLcdDesign(final LcdDesign LCD_Design) {
        styleModel.setLcdDesign(LCD_Design);
    }

    public final ObjectProperty lcdDesignProperty() {
        return styleModel.lcdDesignProperty();
    }

    public final boolean isLcdVisible() {
        return styleModel.isLcdVisible();
    }

    public final void setLcdVisible(final boolean LCD_VISIBLE) {
        styleModel.setLcdVisible(LCD_VISIBLE);
    }

    public final BooleanProperty lcdVisibleProperty() {
        return styleModel.lcdVisibleProperty();
    }

    public final String getLcdUnit() {
        return gaugeModel.getLcdUnit();
    }

    public final void setLcdUnit(final String LCD_UNIT) {
        gaugeModel.setLcdUnit(LCD_UNIT);
    }

    public final StringProperty lcdUnitProperty() {
        return gaugeModel.lcdUnitProperty();
    }

    public final boolean isLcdUnitVisible() {
        return styleModel.getLcdUnitVisible();
    }

    public final void setLcdUnitVisible(final boolean LCD_UNIT_VISIBLE) {
        styleModel.setLcdUnitVisible(LCD_UNIT_VISIBLE);
    }

    public final BooleanProperty lcdUnitVisibleProperty() {
        return styleModel.lcdUnitVisibleProperty();
    }

    public final String getLcdUnitFont() {
        return styleModel.getLcdUnitFont();
    }

    public final void setLcdUnitFont(final String LCD_UNIT_FONT) {
        styleModel.setLcdUnitFont(LCD_UNIT_FONT);
    }

    public final StringProperty lcdUnitFontProperty() {
        return styleModel.lcdUnitFontProperty();
    }

    public final String getLcdTitleFont() {
        return styleModel.getLcdTitleFont();
    }

    public final void setLcdTitleFont(final String LCD_TITLE_FONT) {
        styleModel.setLcdTitleFont(LCD_TITLE_FONT);
    }

    public final StringProperty lcdTitleFontProperty() {
        return styleModel.lcdTitleFontProperty();
    }

    public final Gauge.LcdFont getLcdValueFont() {
        return styleModel.getLcdValueFont();
    }

    public final void setLcdValueFont(final Gauge.LcdFont VALUE_FONT) {
        styleModel.setLcdValueFont(VALUE_FONT);
    }

    public final ObjectProperty<Gauge.LcdFont> lcdValueFontProperty() {
        return styleModel.lcdValueFontProperty();
    }

    public final NumberSystem getLcdNumberSystem() {
        return gaugeModel.getLcdNumberSystem();
    }

    public final void setLcdNumberSystem(final NumberSystem LCD_NUMBER_SYSTEM) {
        gaugeModel.setLcdNumberSystem(LCD_NUMBER_SYSTEM);
    }

    public final ObjectProperty lcdNumberSystemProperty() {
        return gaugeModel.lcdNumberSystemProperty();
    }

    public final boolean isLcdNumberSystemVisible() {
        return styleModel.isLcdNumberSystemVisible();
    }

    public final void setLcdNumberSystemVisible(final boolean LCD_NUMBER_SYSTEM_VISIBLE) {
        styleModel.setLcdNumberSystemVisible(LCD_NUMBER_SYSTEM_VISIBLE);
    }

    public final BooleanProperty lcdNumberSystemVisibleProperty() {
        return styleModel.lcdNumberSystemVisibleProperty();
    }

    public final int getLcdDecimals() {
        return styleModel.getLcdDecimals();
    }

    public final void setLcdDecimals(final int LCD_DECIMALS) {
        styleModel.setLcdDecimals(LCD_DECIMALS);
    }

    public final IntegerProperty lcdDecimalsProperty() {
        return styleModel.lcdDecimalsProperty();
    }

    public final boolean isLcdBlinking() {
        return styleModel.isLcdBlinking();
    }

    public final void setLcdBlinking(final boolean LCD_BLINKING) {
        styleModel.setLcdBlinking(LCD_BLINKING);
    }

    public final BooleanProperty lcdBlinkingProperty() {
        return styleModel.lcdBlinkingProperty();
    }

    public final boolean isGlowVisible() {
        return styleModel.isGlowVisible();
    }

    public final void setGlowVisible(final boolean GLOW_VISIBLE) {
        styleModel.setGlowVisible(GLOW_VISIBLE);
    }

    public final BooleanProperty glowVisibleProperty() {
        return styleModel.glowVisibleProperty();
    }

    public final boolean isGlowOn() {
        return styleModel.isGlowOn();
    }

    public final void setGlowOn(final boolean GLOW_ON) {
        styleModel.setGlowOn(GLOW_ON);
    }

    public final BooleanProperty glowOnProperty() {
        return styleModel.glowOnProperty();
    }

    public final boolean isPulsatingGlow() {
        return styleModel.isPulsatingGlow();
    }

    public final void setPulsatingGlow(final boolean PULSATING_GLOW) {
        styleModel.setPulsatingGlow(PULSATING_GLOW);
    }

    public final BooleanProperty pulsatingGlowProperty() {
        return styleModel.pulsatingGlowProperty();
    }

    public final Color getGlowColor() {
        return styleModel.getGlowColor();
    }

    public final void setGlowColor(final Color GLOW_COLOR) {
        styleModel.setGlowColor(GLOW_COLOR);
    }

    public final ObjectProperty<Color> glowColorProperty() {
        return styleModel.glowColorProperty();
    }

    public final boolean isTickmarksVisible() {
        return styleModel.isTickmarksVisible();
    }

    public final void setTickmarksVisible(final boolean TICKMARKS_VISIBLE) {
        styleModel.setTickmarksVisible(TICKMARKS_VISIBLE);
    }

    public final BooleanProperty tickmarksVisibleProperty() {
        return styleModel.tickmarksVisibleProperty();
    }

    public final boolean isMajorTicksVisible() {
        return styleModel.isMajorTicksVisible();
    }

    public final void setMajorTicksVisible(final boolean MAJOR_TICKS_VISIBLE) {
        styleModel.setMajorTicksVisible(MAJOR_TICKS_VISIBLE);
    }

    public final BooleanProperty majorTicksVisibleProperty() {
        return styleModel.majorTicksVisibleProperty();
    }

    public final TickmarkType getMajorTickmarkType() {
        return styleModel.getMajorTickmarkType();
    }

    public final void setMajorTickmarkType(final TickmarkType TICKMARK_TYPE) {
        styleModel.setMajorTickmarkType(TICKMARK_TYPE);
    }

    public final ObjectProperty<TickmarkType> majorTickmarkTypeProperty() {
        return styleModel.majorTickmarkTypeProperty();
    }

    public final Color getMajorTickmarkColor() {
        return styleModel.getMajorTickmarkColor();
    }

    public final void setMajorTickmarkColor(final Color MAJOR_TICKMARK_COLOR) {
        styleModel.setMajorTickmarkColor(MAJOR_TICKMARK_COLOR);
    }

    public final ObjectProperty<Color> majorTickmarkColorProperty() {
        return styleModel.majorTickmarkColorProperty();
    }

    public final boolean isMajorTickmarkColorEnabled() {
        return styleModel.isMajorTickmarkColorEnabled();
    }

    public final void setMajorTickmarkColorEnabled(final boolean MAJOR_TICKMARK_COLOR_ENABLED) {
        styleModel.setMajorTickmarkColorEnabled(MAJOR_TICKMARK_COLOR_ENABLED);
    }

    public final BooleanProperty majorTickmarkColorEnabledProperty() {
        return styleModel.majorTickmarkColorEnabledProperty();
    }

    public final boolean isMinorTicksVisible() {
        return styleModel.isMinorTicksVisible();
    }

    public final void setMinorTicksVisible(final boolean MINOR_TICKS_VISIBLE) {
        styleModel.setMinorTicksVisible(MINOR_TICKS_VISIBLE);
    }

    public final BooleanProperty minorTicksVisibleProperty() {
        return styleModel.minorTicksVisibleProperty();
    }

    public final Color getMinorTickmarkColor() {
        return styleModel.getMinorTickmarkColor();
    }

    public final void setMinorTickmarkColor(final Color MINOR_TICKMARK_COLOR) {
        styleModel.setMinorTickmarkColor(MINOR_TICKMARK_COLOR);
    }

    public final ObjectProperty<Color> minorTickmarkColorProperty() {
        return styleModel.minorTickmarkColorProperty();
    }

    public final boolean isMinorTickmarkColorEnabled() {
        return styleModel.isMinorTickmarkColorEnabled();
    }

    public final void setMinorTickmarkColorEnabled(final boolean MINOR_TICKMARK_COLOR_ENABLED) {
        styleModel.setMinorTickmarkColorEnabled(MINOR_TICKMARK_COLOR_ENABLED);
    }

    public final BooleanProperty minorTickmarkColorEnabledProperty() {
        return styleModel.minorTickmarkColorEnabledProperty();
    }

    public final boolean isTickLabelsVisible() {
        return styleModel.isTickLabelsVisible();
    }

    public final void setTickLabelsVisible(final boolean TICKLABELS_VISIBLE) {
        styleModel.setTickLabelsVisible(TICKLABELS_VISIBLE);
    }

    public final BooleanProperty ticklabelsVisibleProperty() {
        return styleModel.ticklabelsVisibleProperty();
    }

    public final TicklabelOrientation getTickLabelOrientation() {
        return styleModel.getTickLabelOrientation();
    }

    public final void setTickLabelOrientation(final TicklabelOrientation TICKLABEL_ORIENTATION) {
        styleModel.setTickLabelOrientation(TICKLABEL_ORIENTATION);
    }

    public final ObjectProperty<TicklabelOrientation> tickLabelOrientationProperty() {
        return styleModel.tickLabelOrientationProperty();
    }

    public final NumberFormat getTickLabelNumberFormat() {
        return styleModel.getTickLabelNumberFormat();
    }

    public final void setTickLabelNumberFormat(final NumberFormat TICKLABEL_NUMBER_FORMAT) {
        styleModel.setTickLabelNumberFormat(TICKLABEL_NUMBER_FORMAT);
    }

    public final ObjectProperty<NumberFormat> tickLabelNumberFormatProperty() {
        return styleModel.tickLabelNumberFormatProperty();
    }

    public final Point2D getTickmarksOffset() {
        return styleModel.getTickmarksOffset();
    }

    public final void setTickmarksOffset(final Point2D TICKMARKS_OFFSET) {
        styleModel.setTickmarksOffset(TICKMARKS_OFFSET);
    }

    public final ObjectProperty<Point2D> tickmarksOffsetProperty() {
        return styleModel.tickmarksOffsetProperty();
    }

    public final boolean isTickmarkGlowEnabled() {
        return styleModel.isTickmarkGlowEnabled();
    }

    public final void setTickmarkGlowEnabled(final boolean TICKMARK_GLOW_ENABLED) {
        styleModel.setTickmarkGlowEnabled(TICKMARK_GLOW_ENABLED);
    }

    public final BooleanProperty tickmarkGlowEnabledProperty() {
        return styleModel.tickmarkGlowEnabledProperty();
    }

    public final Color getTickmarkGlowColor() {
        return styleModel.getTickmarkGlowColor();
    }

    public final void setTickmarkGlowColor(final Color TICKMARK_GLOW_COLOR) {
        styleModel.setTickmarkGlowColor(TICKMARK_GLOW_COLOR);
    }

    public final ObjectProperty<Color> tickmarkGlowProperty() {
        return styleModel.tickmarkGlowColorProperty();
    }

    public final int getMaxNoOfMajorTicks() {
        return gaugeModel.getMaxNoOfMajorTicks();
    }

    public final void setMaxNoOfMajorTicks(final int MAX_NO_OF_MAJOR_TICKS) {
        gaugeModel.setMaxNoOfMajorTicks(MAX_NO_OF_MAJOR_TICKS);
    }

    public final IntegerProperty maxNoOfMajorTicksProperty() {
        return gaugeModel.maxNoOfMajorTicksProperty();
    }

    public final int getMaxNoOfMinorTicks() {
        return gaugeModel.getMaxNoOfMinorTicks();
    }

    public final void setMaxNoOfMinorTicks(final int MAX_NO_OF_MINOR_TICKS) {
        gaugeModel.setMaxNoOfMinorTicks(MAX_NO_OF_MINOR_TICKS);
    }

    public final IntegerProperty maxNoOfMinorTicksProperty() {
        return gaugeModel.maxNoOfMinorTicksProperty();
    }

    public final double getMajorTickSpacing() {
        return gaugeModel.getMajorTickSpacing();
    }

    public final void setMajorTickSpacing(final double MAJOR_TICK_SPACING) {
        gaugeModel.setMajorTickSpacing(MAJOR_TICK_SPACING);
    }

    public final DoubleProperty majorTickSpacingProperty() {
        return gaugeModel.majorTickSpacingProperty();
    }

    public final double getMinorTickSpacing() {
        return gaugeModel.getMinorTickSpacing();
    }

    public final void setMinorTickSpacing(final double MINOR_TICK_SPACING) {
        gaugeModel.setMinorTickSpacing(MINOR_TICK_SPACING);
    }

    public final DoubleProperty minorTickSpacingProperty() {
        return gaugeModel.minorTickSpacingProperty();
    }

    public final boolean isNiceScaling() {
        return gaugeModel.isNiceScaling();
    }

    public final void setNiceScaling(final boolean NICE_SCALING) {
        gaugeModel.setNiceScaling(NICE_SCALING);
        recalcRange();
    }

    public final BooleanProperty niceScalingProperty() {
        return gaugeModel.niceScalingProperty();
    }

    public final boolean isTightScale() {
        return  gaugeModel.isTightScale();
    }

    public final void setTightScale(final boolean TIGHT_SCALE) {
        gaugeModel.setTightScale(TIGHT_SCALE);
    }

    public final BooleanProperty tightScaleProperty() {
        return gaugeModel.tightScaleProperty();
    }

    public final boolean isLargeNumberScale() {
        return gaugeModel.isLargeNumberScale();
    }

    public final void setLargeNumberScale(final boolean LARGE_NUMBER_SCALE) {
        gaugeModel.setLargeNumberScale(LARGE_NUMBER_SCALE);
    }

    public final BooleanProperty largeNumberScaleProperty() {
        return gaugeModel.largeNumberScaleProperty();
    }

    public final ObservableList<Section> getSections() {
        return gaugeModel.getSections();
    }

    /* Removed due to FXML compatibility
    public final void setSections(final Section... SECTION_ARRAY) {
        gaugeModel.setSections(SECTION_ARRAY);
    }

    public final void setSections(final List<Section> SECTIONS) {
        gaugeModel.setSections(SECTIONS);
    }
    */

    public final void addSection(final Section SECTION) {
        gaugeModel.addSection(SECTION);
    }

    public final void removeSection(final Section SECTION) {
        gaugeModel.removeSection(SECTION);
    }

    public final void resetSections() {
        gaugeModel.resetSections();
    }

    public final boolean isSectionsVisible() {
        return styleModel.isSectionsVisible();
    }

    public final void setSectionsVisible(final boolean SECTIONS_VISIBLE) {
        styleModel.setSectionsVisible(SECTIONS_VISIBLE);
    }

    public final BooleanProperty sectionsVisibleProperty() {
        return styleModel.sectionsVisibleProperty();
    }

    public final boolean isExpandedSections() {
        return styleModel.isExpandedSections();
    }

    public final void setExpandedSections(final boolean EXPANDED_SECTIONS) {
        styleModel.setExpandedSections(EXPANDED_SECTIONS);
    }

    public final BooleanProperty expandedSectionsProperty() {
        return styleModel.expandedSectionsProperty();
    }

    public final boolean isSectionsHighlighting() {
        return styleModel.isSectionsHighlighting();
    }

    public final void setSectionsHighlighting(final boolean SECTIONS_HIGHLIGHTING) {
        styleModel.setSectionsHighlighting(SECTIONS_HIGHLIGHTING);
    }

    public final BooleanProperty sectionsHighlightingProperty() {
        return styleModel.sectionsHighlightingProperty();
    }

    public final boolean isShowSectionTickmarksOnly() {
        return styleModel.isShowSectionTickmarksOnly();
    }

    public final void setShowSectionTickmarksOnly(final boolean SHOW_SECTION_TICKMARKS_ONLY) {
        styleModel.setShowSectionTickmarksOnly(SHOW_SECTION_TICKMARKS_ONLY);
    }

    public final BooleanProperty showSectionTickmarksOnlyProperty() {
        return styleModel.showSectionTickmarksOnlyProperty();
    }

    public final ObservableList<Section> getAreas() {
        return gaugeModel.getAreas();
    }

    /* Removed due to FXML compatibility
    public final void setAreas(final Section... AREA_ARRAY) {
        gaugeModel.setAreas(AREA_ARRAY);
    }

    public final void setAreas(final List<Section> AREAS) {
        gaugeModel.setAreas(AREAS);
    }
    */

    public final void addArea(final Section AREA) {
        gaugeModel.addArea(AREA);
    }

    public final void removeArea(final Section AREA) {
        gaugeModel.removeArea(AREA);
    }

    public final void resetAreas() {
        gaugeModel.resetAreas();
    }

    public final boolean isAreasVisible() {
        return styleModel.isAreasVisible();
    }

    public final void setAreasVisible(final boolean AREAS_VISIBLE) {
        styleModel.setAreasVisible(AREAS_VISIBLE);
    }

    public final BooleanProperty areasVisibleProperty() {
        return styleModel.areasVisibleProperty();
    }

    public final boolean isAreasHighlighting() {
        return styleModel.isAreasHighlighting();
    }

    public final void setAreasHighlighting(final boolean AREAS_HIGHLIGHTING) {
        styleModel.setAreasHighlighting(AREAS_HIGHLIGHTING);
    }

    public final BooleanProperty areasHighlightingProperty() {
        return styleModel.areasHighlightingProperty();
    }

    public final ObservableList<Section> getTickMarkSections() {
        return gaugeModel.getTickMarkSections();
    }

    /* Removed due to FXML compatibility
    public final void setTickMarkSections(final Section... TICK_MARK_SECTION_ARRAY) {
        gaugeModel.setTickMarkSections(TICK_MARK_SECTION_ARRAY);
    }

    public final void setTickMarkSections(final List<Section> TICK_MARK_SECTIONS) {
        gaugeModel.setTickMarkSections(TICK_MARK_SECTIONS);
    }
    */

    public final void addTickMarkSection(final Section TICK_MARK_SECTION) {
        gaugeModel.addTickMarkSection(TICK_MARK_SECTION);
    }

    public final void removeTickMarkSection(final Section TICK_MARK_SECTION) {
        gaugeModel.removeTickMarkSection(TICK_MARK_SECTION);
    }

    public final void resetTickMarkSections() {
        gaugeModel.resetTickMarkSections();
    }

    public final ObservableList<Marker> getMarkers() {
        return gaugeModel.getMarkers();
    }

    /* Removed due to FXML compatibility
    public final void setMarkers(final Marker... MARKER_ARRAY) {
        gaugeModel.setMarkers(MARKER_ARRAY);
    }

    public final void setMarkers(final List<Marker> MARKERS) {
        gaugeModel.setMarkers(MARKERS);
    }
    */

    public final void addMarker(final Marker MARKER) {
        gaugeModel.addMarker(MARKER);
    }

    public final void removeMarker(final Marker MARKER) {
        gaugeModel.removeMarker(MARKER);
    }

    public final void resetMarkers() {
        gaugeModel.resetMarkers();
    }

    public final boolean isMarkersVisible() {
        return styleModel.isMarkersVisible();
    }

    public final void setMarkersVisible(final boolean INDICATORS_VISIBLE) {
        styleModel.setMarkersVisible(INDICATORS_VISIBLE);
    }

    public final BooleanProperty markersVisibleProperty() {
        return styleModel.markersVisibleProperty();
    }

    public final boolean isEndlessMode() {
        return gaugeModel.isEndlessMode();
    }

    public final void setEndlessMode(final boolean ENDLESS_MODE) {
        if (getRadialRange() == RadialRange.RADIAL_360) {
            gaugeModel.setEndlessMode(ENDLESS_MODE);
        }
    }

    public final Color getTextureColor() {
        return styleModel.getTextureColor();
    }

    public final String getTextureColorString() {
        return styleModel.getTextureColorString();
    }

    public final void setTextureColor(final Color TEXTURE_COLOR) {
        styleModel.setTextureColor(TEXTURE_COLOR);
    }

    public final ObjectProperty<Color> textureColorProperty() {
        return styleModel.textureColorProperty();
    }

    public final Color getSimpleGradientBaseColor() {
        return styleModel.getSimpleGradientBaseColor();
    }

    public final String getSimpleGradientBaseColorString() {
        return styleModel.getSimpleGradientBaseColorString();
    }

    public final void setSimpleGradientBaseColor(final Color SIMPLE_GRADIENT_BASE_COLOR) {
        styleModel.setSimpleGradientBaseColor(SIMPLE_GRADIENT_BASE_COLOR);
    }

    public final ObjectProperty<Color> simpleGradientBaseColorProperty() {
        return styleModel.simpleGradientBaseColorProperty();
        }

    public final boolean isTitleVisible() {
        return styleModel.isTitleVisible();
    }

    public final void setTitleVisible(final boolean TITLE_VISIBLE) {
        styleModel.setTitleVisible(TITLE_VISIBLE);
    }

    public final BooleanProperty titleVisibleProperty() {
        return styleModel.titleVisibleProperty();
    }

    public final boolean isUnitVisible() {
        return styleModel.isUnitVisible();
    }

    public final void setUnitVisible(final boolean UNIT_VISIBLE) {
        styleModel.setUnitVisible(UNIT_VISIBLE);
    }

    public final BooleanProperty unitVisibleProperty() {
        return styleModel.unitVisibleProperty();
    }

    public final Trend getTrend() {
        return gaugeModel.getTrend();
    }

    public final void setTrend(final Trend TREND) {
        gaugeModel.setTrend(TREND);
    }

    public final ObjectProperty<Trend> trendProperty() {
        return gaugeModel.trendProperty();
    }

    public final boolean isTrendVisible() {
        return styleModel.isTrendVisible();
    }

    public final void setTrendVisible(final boolean TREND_VISIBLE) {
        styleModel.setTrendVisible(TREND_VISIBLE);
    }

    public final BooleanProperty trendVisibleProperty() {
        return styleModel.trendVisibleProperty();
    }

    public final Color getTrendUpColor() {
            return styleModel.getTrendUpColor();
        }

    public final void setTrendUpColor(final Color TREND_UP_COLOR) {
        styleModel.setTrendUpColor(TREND_UP_COLOR);
    }

    public final ObjectProperty<Color> trendUpColorProperty() {
        return styleModel.trendUpColorProperty();
    }

    public final Color getTrendRisingColor() {
        return styleModel.getTrendRisingColor();
    }

    public final void setTrendRisingColor(final Color TREND_RISING_COLOR) {
        styleModel.setTrendRisingColor(TREND_RISING_COLOR);
    }

    public final ObjectProperty<Color> trendRisingColorProperty() {
        return styleModel.trendRisingColorProperty();
    }

    public final Color getTrendSteadyColor() {
            return styleModel.getTrendSteadyColor();
    }

    public final void setTrendSteadyColor(final Color TREND_STEADY_COLOR) {
        styleModel.setTrendSteadyColor(TREND_STEADY_COLOR);
    }

    public final ObjectProperty<Color> trendSteadyColorProperty() {
        return styleModel.trendSteadyColorProperty();
    }

    public final Color getTrendFallingColor() {
            return styleModel.getTrendFallingColor();
        }

    public final void setTrendFallingColor(final Color TREND_FALLING_COLOR) {
        styleModel.setTrendFallingColor(TREND_FALLING_COLOR);
    }

    public final ObjectProperty<Color> trendFallingColorProperty() {
        return styleModel.trendFallingColorProperty();
    }

    public final Color getTrendDownColor() {
        return styleModel.getTrendDownColor();
    }

    public final void setTrendDownColor(final Color TREND_DOWN_COLOR) {
        styleModel.setTrendDownColor(TREND_DOWN_COLOR);
    }

    public final ObjectProperty<Color> trendDownColorProperty() {
            return styleModel.trendDownColorProperty();
        }
}
