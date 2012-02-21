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

import jfxtras.labs.scene.control.gauge.gauges.Radial.ForegroundType;
import jfxtras.labs.scene.control.gauge.tools.Util;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Control;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by
 * User: hansolo
 * Date: 02.01.12
 * Time: 17:14
 */
public abstract class Gauge extends Control {
    // ******************** Variable definitions ******************************
    private ObjectProperty<Model>       modelProperty;
    private ObjectProperty<ViewModel>   viewModelProperty;
    private Model                       model;
    private ViewModel                   viewModel;
    private ObjectProperty<RadialRange> radialRange;
    private DoubleProperty              angleStep;


    // ******************** Enum definitions **********************************
    public enum BackgroundDesign {
        DARK_GRAY("backgrounddesign-darkgray"),
        SATIN_GRAY("backgrounddesign-satingray"),
        LIGHT_GRAY("backgrounddesign-lightgray"),
        WHITE("backgrounddesign-white"),
        BLACK("backgrounddesign-black"),
        BEIGE("backgrounddesign-beige"),
        BROWN("backgrounddesign-brown"),
        RED("backgrounddesign-red"),
        GREEN("backgrounddesign-green"),
        BLUE("backgrounddesign-blue"),
        ANTHRACITE("backgrounddesign-anthracite"),
        MUD("backgrounddesign-mud"),
        //CARBON("backgrounddesign-carbon"),                       // Swing based
        //STAINLESS("backgrounddesign-stainless"),                 // Swing based
        //STAINLESS_GRINDED("backgrounddesign-stainlessgrinded"),  // Swing based
        //BRUSHED_METAL("backgrounddesign-brushedmetal"),          // Swing based
        //PUNCHED_SHEET("backgrounddesign-punchedsheet"),          // Swing based
        //LINEN("backgrounddesign-linen"),                         // Swing based
        //NOISY_PLASTIC("backgrounddesign-noisyplastic"),          // Swing based
        SIMPLE_GRADIENT("backgrounddesign-simplegradient"),
        TRANSPARENT("backgrounddesign-transparent"),
        CUSTOM("backgrounddesign-custom");

        public final String CSS_BACKGROUND;
        public final String CSS_TEXT;

        BackgroundDesign(final String CSS_BACKGROUND) {
            this.CSS_BACKGROUND = CSS_BACKGROUND;
            this.CSS_TEXT = CSS_BACKGROUND + "-text";
        }
    }
    public enum FrameDesign {
        //BLACK_METAL("framedesign-blackmetal"),   // Swing based
        //SHINY_METAL("framedesign-shinymetal"),   // Swing based
        //CHROME("framedesign-chrome"),            // Swing based
        METAL("framedesign-metal"),
        GLOSSY_METAL("framedesign-glossymetal"),
        DARK_GLOSSY("framedesign-darkglossy"),
        BRASS("framedesign-brass"),
        STEEL("framedesign-steel"),
        GOLD("framedesign-gold"),
        ANTHRACITE("framedesign-anthracite"),
        TILTED_GRAY("framedesign-tiltedgray"),
        TILTED_BLACK("framedesign-tiltedblack"),
        CUSTOM("framedesign-custom");

        public final String CSS;

        FrameDesign(final String CSS) {
            this.CSS = CSS;
        }
    }
    public enum RadialRange {
        RADIAL_300(300, -150, 240, new Rectangle(0.4, 0.56, 0.4, 0.12), 150, new Point2D(0.6, 0.4),new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_270(270, -180, 270, new Rectangle(0.4, 0.56, 0.4, 0.12), 180, new Point2D(0.6, 0.4),new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_180(180, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 90, new Point2D(0.6, 0.4),new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_180N(180, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 90, new Point2D(0.6, 0.35),new Point2D(0.3, 0.35), 1, 0.38),
        RADIAL_180S(180, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 0, new Point2D(0.6, 0.2),new Point2D(0.3, 0.2), -1, 0.38),
        RADIAL_90(90, -90, 180, new Rectangle(0.55, 0.56, 0.55, 0.12), 91, new Point2D(0.6, 0.4),new Point2D(0.3, 0.4), 1, 0.38),
        RADIAL_90N(90, 315, 225, new Rectangle(0.55, 0.52, 0.55, 0.12), 45, new Point2D(0.6, 0.4),new Point2D(0.3, 0.4), 1, 0.5),
        RADIAL_90W(90, 225, 45, new Rectangle(0.2, 0.58, 0.45, 0.12), 45, new Point2D(0.12, 0.35),new Point2D(0.12, 0.55), 1, 0.5),
        RADIAL_90S(90, -135, 45, new Rectangle(0.55, 0.36, 0.55, 0.12), 45, new Point2D(0.6, 0.5),new Point2D(0.3, 0.5), -1, 0.5),
        RADIAL_90E(90, 135, 225, new Rectangle(0.2, 0.58, 0.45, 0.12), 45, new Point2D(0.78, 0.35),new Point2D(0.78, 0.55), -1, 0.5);

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
    public enum KnobColor {
            BLACK,
            BRASS,
            SILVER
        }
    public enum KnobDesign {
        STANDARD,
        METAL,
        BIG
    }
    public enum NumberFormat {
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
    public enum NumberSystem {
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
    public enum PointerType {
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
    public enum ThresholdColor {
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
        GREENLCD("-fx-greenlcd;"),
        JUG_GREEN("-fx-juggreen;"),
        CUSTOM("-fx-custom;");

        public final String CSS;

        ThresholdColor(final String CSS_COLOR) {
            this.CSS = "-fx-threshold: " + CSS_COLOR;
        }
    }
    public enum TickmarkType {
        LINE,
        TRIANGLE
    }
    public enum TicklabelOrientation {
            NORMAL,
            HORIZONTAL,
            TANGENT
        }
    public enum Trend {
        UP("up"),
        STEADY("steady"),
        DOWN("down"),
        UNKNOWN("unknown");

        public final ArrayList<int[]> ledMatrix = new ArrayList<>(9);

        private Trend(final String TYPE) {
            if (TYPE == "up") {
                ledMatrix.add(new int[] {0, 0, 1, 1, 1, 1, 1, 1, 1});
                ledMatrix.add(new int[] {0, 0, 0, 1, 1, 1, 1, 1, 1});
                ledMatrix.add(new int[] {0, 0, 0, 0, 1, 1, 1, 1, 1});
                ledMatrix.add(new int[] {0, 0, 0, 1, 1, 1, 1, 1, 1});
                ledMatrix.add(new int[] {0, 0, 1, 1, 1, 1, 1, 1, 1});
                ledMatrix.add(new int[] {0, 1, 1, 1, 1, 1, 0, 1, 1});
                ledMatrix.add(new int[] {1, 1, 1, 1, 1, 0, 0, 0, 1});
                ledMatrix.add(new int[] {0, 1, 1, 1, 0, 0, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 1, 0, 0, 0, 0, 0, 0});
            }
            if (TYPE == "steady") {
                ledMatrix.add(new int[] {0, 0, 0, 0, 1, 0, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 1, 1, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 1, 1, 1, 0, 0});
                ledMatrix.add(new int[] {1, 1, 1, 1, 1, 1, 1, 1, 0});
                ledMatrix.add(new int[] {1, 1, 1, 1, 1, 1, 1, 1, 1});
                ledMatrix.add(new int[] {1, 1, 1, 1, 1, 1, 1, 1, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 1, 1, 1, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 1, 1, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 1, 0, 0, 0, 0});
            }
            if (TYPE == "down") {
                ledMatrix.add(new int[] {0, 0, 1, 0, 0, 0, 0, 0, 0});
                ledMatrix.add(new int[] {0, 1, 1, 1, 0, 0, 0, 0, 0});
                ledMatrix.add(new int[] {1, 1, 1, 1, 1, 0, 0, 0, 1});
                ledMatrix.add(new int[] {0, 1, 1, 1, 1, 1, 0, 1, 1});
                ledMatrix.add(new int[] {0, 0, 1, 1, 1, 1, 1, 1, 1});
                ledMatrix.add(new int[] {0, 0, 0, 1, 1, 1, 1, 1, 1});
                ledMatrix.add(new int[] {0, 0, 0, 0, 1, 1, 1, 1, 1});
                ledMatrix.add(new int[] {0, 0, 0, 1, 1, 1, 1, 1, 1});
                ledMatrix.add(new int[] {0, 0, 1, 1, 1, 1, 1, 1, 1});
            }
            if (TYPE == "unknown")
            {
                ledMatrix.add(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0});
                ledMatrix.add(new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0});
            }
        }
    }


    // ******************** Constructors **************************************
    public Gauge() {
        this(new Model());
    }

    public Gauge(final Model MODEL) {
        this(MODEL, new ViewModel());
    }

    public Gauge(final ViewModel VIEW_MODEL) {
        this(new Model(), VIEW_MODEL);
    }

    public Gauge(final Model MODEL, final ViewModel VIEW_MODEL) {
        modelProperty     = new SimpleObjectProperty<>(MODEL);
        viewModelProperty = new SimpleObjectProperty<>(VIEW_MODEL);
        model             = modelProperty.get();
        viewModel         = viewModelProperty.get();
        radialRange       = new SimpleObjectProperty<>(RadialRange.RADIAL_300);
        angleStep         = new SimpleDoubleProperty(radialRange.get().ANGLE_RANGE / model.getRange());
        ledBlinkingProperty().bind(thresholdExceededProperty());
        addModelListener();
        addViewModelListener();
    }


    // ******************** Initialization ************************************
    public abstract void init();


    // ******************** Event handling ************************************
    private final void addModelListener() {
        model.setOnModelEvent(new EventHandler<ModelEvent>() {
            public void handle(final ModelEvent EVENT) {
                forwardModelEvent(EVENT);
            }
        });
    }

    private final void addViewModelListener() {
        viewModel.setOnViewModelEvent(new EventHandler<ViewModelEvent>() {
            public void handle(final ViewModelEvent EVENT) {
                forwardViewModelEvent(EVENT);
            }
        });
    }

    public final ObjectProperty<EventHandler<ModelEvent>> onModelEventProperty() {
        return onModelEvent;
    }

    public final void setOnModelEvent(final EventHandler<ModelEvent> HANDLER) {
        onModelEventProperty().set(HANDLER);
    }

    public final EventHandler<ModelEvent> getOnModelEvent() {
        return onModelEventProperty().get();
    }

    private final ObjectProperty<EventHandler<ModelEvent>> onModelEvent = new SimpleObjectProperty<>();

    public void forwardModelEvent(final ModelEvent EVENT) {
        final EventHandler<ModelEvent> MODEL_EVENT_HANDLER = getOnModelEvent();
        if (MODEL_EVENT_HANDLER != null) {
            MODEL_EVENT_HANDLER.handle(EVENT);
        }
    }

    public final ObjectProperty<EventHandler<ViewModelEvent>> onViewModelEventProperty() {
        return onViewModelEvent;
    }

    public final void setOnViewModelEvent(final EventHandler<ViewModelEvent> HANDLER) {
        onViewModelEventProperty().set(HANDLER);
    }

    public final EventHandler<ViewModelEvent> getOnViewModelEvent() {
        return onViewModelEventProperty().get();
    }

    private final ObjectProperty<EventHandler<ViewModelEvent>> onViewModelEvent = new SimpleObjectProperty<>();

    public void forwardViewModelEvent(final ViewModelEvent EVENT) {
        final EventHandler<ViewModelEvent> VIEW_MODEL_EVENT_HANDLER = getOnViewModelEvent();
        if (VIEW_MODEL_EVENT_HANDLER != null) {
            VIEW_MODEL_EVENT_HANDLER.handle(EVENT);
        }
    }


    // ******************** Stylesheet handling *******************************
    @Override public String getUserAgentStylesheet() {
        return getClass().getResource("steelseries.css").toExternalForm();
    }


    // ******************** Gauge Methods *************************************
    public final ViewModel getViewModel() {
        return viewModelProperty.get();
    }

    public final void setViewModel(final ViewModel VIEW_MODEL) {
        viewModelProperty.set(VIEW_MODEL);
        viewModel = viewModelProperty().get();
        addModelListener();
        init();
    }

    public final ObjectProperty<Model> modelProperty() {
        return modelProperty;
    }

    public final Model getModel() {
        return modelProperty.get();
    }

    public final void setModel(final Model MODEL) {
        modelProperty.set(MODEL);
        model = modelProperty.get();
        addViewModelListener();
        init();
    }

    public final ObjectProperty<ViewModel> viewModelProperty() {
        return viewModelProperty;
    }

    public final Gauge.RadialRange getRadialRange() {
        return radialRange.get();
    }

    public void setRadialRange(final Gauge.RadialRange RADIAL_RANGE) {
        radialRange.set(RADIAL_RANGE);
        model.calcRange(radialRange.get().ANGLE_RANGE);
        angleStep.set(radialRange.get().ANGLE_RANGE / model.getRange());
    }

    public final ObjectProperty<Gauge.RadialRange> radialRangeProperty() {
        return radialRange;
    }

    public final double getAngleStep() {
        return angleStep.get();
    }

    public final void recalcRange() {
        model.calcRange(radialRange.get().ANGLE_RANGE);
        angleStep.set(radialRange.get().ANGLE_RANGE / model.getRange());
    }

    public final DoubleProperty angleStepProperty() {
        return angleStep;
    }

    public final Point2D getLedPosition() {
        return radialRange.get().LED_POSITION;
    }

    public final Point2D getUserLedPosition() {
            return radialRange.get().USER_LED_POSITION;
        }


    // ******************** Model Methods *************************************
    public final double getValue() {
        return model.getValue();
    }

    public final void setValue(final double VALUE) {
        model.setValue(VALUE);
    }

    public final DoubleProperty valueProperty() {
        return model.valueProperty();
    }

    public final boolean isValueAnimationEnabled() {
        return model.isValueAnimationEnabled();
    }

    public final void setValueAnimationEnabled(final boolean VALUE_ANIMATION_ENABLED) {
        model.setValueAnimationEnabled(VALUE_ANIMATION_ENABLED);
    }

    public final BooleanProperty valueAnimationEnabledProperty() {
        return model.valueAnimationEnabledProperty();
    }

    public final double getAnimationDuration() {
        return model.getAnimationDuration();
    }

    public final void setAnimationDuration(final double ANIMATION_DURATION) {
        model.setAnimationDuration(ANIMATION_DURATION);
    }

    public final DoubleProperty animationDurationProperty() {
        return model.animationDurationProperty();
    }

    public final double getMinValue() {
        return model.getMinValue();
    }

    public final void setMinValue(final double MIN_VALUE) {
        model.setMinValue(MIN_VALUE);
        model.calcRange(radialRange.get().ANGLE_RANGE);
        angleStep.set(radialRange.get().ANGLE_RANGE / model.getRange());
    }

    public final DoubleProperty minValueProperty() {
        return model.minValueProperty();
    }

    public final double getMaxValue() {
        return model.getMaxValue();
    }

    public final void setMaxValue(final double MAX_VALUE) {
        model.setMaxValue(MAX_VALUE);
        model.calcRange(radialRange.get().ANGLE_RANGE);
        angleStep.set(radialRange.get().ANGLE_RANGE / model.getRange());
    }

    public final DoubleProperty maxValueProperty() {
        return model.maxValueProperty();
    }

    public final double getRange() {
        return model.getRange();
    }

    public final DoubleProperty rangeProperty() {
        return model.rangeProperty();
    }

    public final double getMinMeasuredValue() {
        return model.getMinMeasuredValue();
    }

    public final void setMinMeasuredValue(final double MIN_MEASURED_VALUE) {
        model.setMinMeasuredValue(MIN_MEASURED_VALUE);
    }

    public final DoubleProperty minMeasuredValueProperty() {
        return model.minMeasuredValueProperty();
    }

    public final boolean isBargraph() {
        return viewModel.isBargraph();
    }

    public final void setBargraph(final boolean BARGRAPH) {
        viewModel.setBargraph(BARGRAPH);
    }

    public final BooleanProperty bargraphProperty() {
        return viewModel.bargraphProperty();
    }

    public final boolean isMinMeasuredValueVisible() {
        return viewModel.isMinMeasuredValueVisible();
    }

    public final void setMinMeasuredValueVisible(final boolean MIN_MEASURED_VALUE_VISIBLE) {
        viewModel.setMinMeasuredValueVisible(MIN_MEASURED_VALUE_VISIBLE);
    }

    public final BooleanProperty minMeasuredValueVisibleProperty() {
        return viewModel.minMeasuredValueVisibleProperty();
    }

    public final void resetMinMeasuredValue() {
        model.resetMinMeasuredValue();
    }

    public final double getMaxMeasuredValue() {
        return model.getMaxMeasuredValue();
    }

    public final void setMaxMeasuredValue(final double MAX_MEASURED_VALUE) {
        model.setMaxMeasuredValue(MAX_MEASURED_VALUE);
    }

    public final DoubleProperty maxMeasuredValueProperty() {
        return model.maxMeasuredValueProperty();
    }

    public final boolean isMaxMeasuredValueVisible() {
        return viewModel.isMaxMeasuredValueVisible();
    }

    public final void setMaxMeasuredValueVisible(final boolean MAX_MEASURED_VALUE_VISIBLE) {
        viewModel.setMaxMeasuredValueVisible(MAX_MEASURED_VALUE_VISIBLE);
    }

    public final BooleanProperty maxMeasuredValueVisibleProperty() {
        return viewModel.maxMeasuredValueVisibleProperty();
    }

    public final void resetMaxMeasuredValue() {
        model.resetMaxMeasuredValue();
    }

    public final void resetMinMaxMeasuredValue() {
        model.resetMinMaxMeasuredValue();
    }

    public final double getThreshold() {
        return model.getThreshold();
    }

    public final void setThreshold(final double THRESHOLD) {
        model.setThreshold(THRESHOLD);
    }

    public final DoubleProperty thresholdProperty() {
        return model.thresholdProperty();
    }

    public final boolean isThresholdBehaviorInverted() {
        return model.isThresholdBehaviorInverted();
    }

    public final void setThresholdBehaviorInverted(final boolean THRESHOLD_BEHAVIOR_INVERTED) {
        model.setThresholdBehaviorInverted(THRESHOLD_BEHAVIOR_INVERTED);
    }

    public final BooleanProperty thresholdBehaviorInvertedProperty() {
        return model.thresholdBehaviorInvertedProperty();
    }

    public final boolean isThresholdExceeded() {
        return model.isThresholdExceeded();
    }

    public final void setThresholdExceeded(final boolean THRESHOLD_EXCEEDED) {
        model.setThresholdExceeded(THRESHOLD_EXCEEDED);
    }

    public final BooleanProperty thresholdExceededProperty() {
        return model.thresholdExceededProperty();
    }

    public final boolean isThresholdVisible() {
        return viewModel.isThresholdVisible();
    }

    public final void setThresholdVisible(final boolean THRESHOLD_VISIBLE) {
        viewModel.setThresholdVisible(THRESHOLD_VISIBLE);
    }

    public final BooleanProperty thresholdVisibleProperty() {
        return viewModel.thresholdVisibleProperty();
    }

    public final ThresholdColor getThresholdColor() {
        return viewModel.getThresholdColor();
    }

    public final void setThresholdColor(final ThresholdColor THRESHOLD_COLOR) {
        viewModel.setThresholdColor(THRESHOLD_COLOR);
    }

    public final ObjectProperty<ThresholdColor> thresholdColorProperty() {
        return viewModel.thresholdColorProperty();
    }

    public final String getTitle() {
        return model.getTitle();
    }

    public final void setTitle(final String TITLE) {
        model.setTitle(TITLE);
    }

    public final StringProperty titleProperty() {
        return model.titleProperty();
    }

    public final String getUnit() {
        return model.getUnit();
    }

    public final void setUnit(final String UNIT) {
        model.setUnit(UNIT);
    }

    public final StringProperty unitProperty() {
        return model.unitProperty();
    }

    public final FrameDesign getFrameDesign() {
        return viewModel.getFrameDesign();
    }

    public final void setFrameDesign(final FrameDesign FRAME_DESIGN) {
        viewModel.setFrameDesign(FRAME_DESIGN);
    }

    public final ObjectProperty<FrameDesign> frameDesignProperty() {
        return viewModel.frameDesignProperty();
    }

    public final boolean isFrameVisible() {
        return viewModel.isFrameVisible();
    }

    public final void setFrameVisible(final boolean FRAME_VISIBLE) {
        viewModel.setFrameVisible(FRAME_VISIBLE);
    }

    public final BooleanProperty frameVisibleProperty() {
        return viewModel.frameVisibleProperty();
    }

    public final BackgroundDesign getBackgroundDesign() {
        return viewModel.getBackgroundDesign();
    }

    public final void setBackgroundDesign(final BackgroundDesign BACKGROUND_DESIGN) {
        viewModel.setBackgroundDesign(BACKGROUND_DESIGN);
    }

    public final ObjectProperty<BackgroundDesign> backgroundDesignProperty() {
        return viewModel.backgroundDesignProperty();
    }

    public final boolean isBackgroundVisible() {
        return viewModel.isBackgroundVisible();
    }

    public final void setBackgroundVisible(final boolean BACKGROUND_VISIBLE) {
        viewModel.setBackgroundVisible(BACKGROUND_VISIBLE);
    }

    public final BooleanProperty backgroundVisibleProperty() {
        return viewModel.backgroundVisibleProperty();
    }

    public final KnobDesign getKnobDesign() {
        return viewModel.getKnobDesign();
    }

    public final void setKnobDesign(final KnobDesign KNOB_DESIGN) {
        viewModel.setKnobDesign(KNOB_DESIGN);
    }

    public final ObjectProperty<KnobDesign> knobDesignProperty() {
        return viewModel.knobDesignProperty();
    }

    public final KnobColor getKnobColor() {
        return viewModel.getKnobColor();
    }

    public final void setKnobColor(final KnobColor KNOB_COLOR) {
        viewModel.setKnobColor(KNOB_COLOR);
    }

    public final ObjectProperty<KnobColor> knobColorProperty() {
        return viewModel.knobColorProperty();
    }

    public final boolean isPostsVisible() {
        return viewModel.isPostsVisible();
    }

    public final void setPostsVisible(final boolean POSTS_VISIBLE) {
        viewModel.setPostsVisible(POSTS_VISIBLE);
    }

    public final BooleanProperty postsVisibleProperty() {
        return viewModel.postsVisibleProperty();
    }

    public final PointerType getPointerType() {
        return viewModel.getPointerType();
    }

    public final void setPointerType(final PointerType POINTER_TYPE) {
        viewModel.setPointerType(POINTER_TYPE);
    }

    public final ObjectProperty<PointerType> pointerTypeProperty() {
        return viewModel.pointerTypeProperty();
    }

    public final ColorDef getValueColor() {
        return viewModel.getValueColor();
    }

    public final void setValueColor(final ColorDef VALUE_COLOR) {
        viewModel.setValueColor(VALUE_COLOR);
    }

    public final ObjectProperty<ColorDef> valueColorProperty() {
        return viewModel.valueColorProperty();
    }

    public final boolean isPointerShadowVisible() {
        return viewModel.isPointerShadowVisible();
    }

    public final void setPointerShadowVisible(final boolean POINTER_SHADOW_VISIBLE) {
        viewModel.setPointerShadowVisible(POINTER_SHADOW_VISIBLE);
    }

    public final BooleanProperty pointerShadowVisibleProperty() {
        return viewModel.pointerShadowVisibleProperty();
    }

    public final boolean isLedVisible() {
        return viewModel.isLedVisible();
    }

    public final void setLedVisible(final boolean LED_VISIBLE) {
        viewModel.setLedVisible(LED_VISIBLE);
    }

    public final BooleanProperty ledVisibleProperty() {
        return viewModel.ledVisibleProperty();
    }

    public final LedColor getLedColor() {
        return viewModel.getLedColor();
    }

    public final void setLedColor(final LedColor LED_COLOR) {
        viewModel.setLedColor(LED_COLOR);
    }

    public final ObjectProperty<LedColor> ledColorProperty() {
        return viewModel.ledColorProperty();
    }

    public final boolean isLedBlinking() {
        return viewModel.isLedBlinking();
    }

    public final void setLedBlinking(final boolean LED_BLINKING) {
        viewModel.setLedBlinking(LED_BLINKING);
    }

    public final BooleanProperty ledBlinkingProperty() {
        return viewModel.ledBlinkingProperty();
    }

    public final boolean isUserLedVisible() {
        return viewModel.isUserLedVisible();
    }

    public final void setUserLedVisible(final boolean USER_LED_VISIBLE) {
        viewModel.setUserLedVisible(USER_LED_VISIBLE);
    }

    public final BooleanProperty userLedVisibleProperty() {
        return viewModel.userLedVisibleProperty();
    }

    public final LedColor getUserLedColor() {
        return viewModel.getUserLedColor();
    }

    public final void setUserLedColor(final LedColor USER_LED_COLOR) {
        viewModel.setUserLedColor(USER_LED_COLOR);
    }

    public final ObjectProperty<LedColor> userLedColorProperty() {
        return viewModel.userLedColorProperty();
    }

    public final boolean isUserLedOn() {
        return viewModel.isUserLedOn();
    }

    public final void setUserLedOn(final boolean USER_LED_ON) {
        viewModel.setUserLedOn(USER_LED_ON);
    }

    public final BooleanProperty userLedOnProperty() {
        return viewModel.userLedOnProperty();
    }

    public final boolean isUserLedBlinking() {
        return viewModel.isUserLedBlinking();
    }

    public final void setUserLedBlinking(final boolean USER_LED_BLINKING) {
        viewModel.setUserLedBlinking(USER_LED_BLINKING);
    }

    public final BooleanProperty userLedBlinkingProperty() {
        return viewModel.userLedBlinkingProperty();
    }

    public final ForegroundType getForegroundType() {
        return viewModel.getForegroundType();
    }

    public final void setForegroundType(final ForegroundType FOREGROUND_TYPE) {
        viewModel.setForegroundType(FOREGROUND_TYPE);
    }

    public final ObjectProperty<ForegroundType> foregroundTypeProperty() {
        return viewModel.foregroundTypeProperty();
    }

    public final boolean isForegroundVisible() {
        return viewModel.isForegroundVisible();
    }

    public final void setForegroundVisible(final boolean FOREGROUND_VISIBLE) {
        viewModel.setForegroundVisible(FOREGROUND_VISIBLE);
    }

    public final BooleanProperty foregroundVisibleProperty() {
        return viewModel.foregroundVisibleProperty();
    }

    public final double getLcdValue() {
            return model.getLcdValue();
        }

    public final void setLcdValue(final double LCD_VALUE) {
        model.setLcdValue(LCD_VALUE);
    }

    public final DoubleProperty lcdValueProperty() {
        return model.lcdValueProperty();
    }

    public final boolean isLcdValueCoupled() {
        return model.isLcdValueCoupled();
    }

    public final void setLcdValueCoupled(final boolean LCD_VALUE_COUPLED) {
        model.setLcdValueCoupled(LCD_VALUE_COUPLED);
    }

    public final BooleanProperty lcdValueCoupledProperty() {
        return model.lcdValueCoupledProperty();
    }

    public final double getLcdThreshold() {
        return model.getLcdThreshold();
    }

    public final void setLcdThreshold(final double LCD_THRESHOLD) {
        model.setLcdThreshold(LCD_THRESHOLD);
    }

    public final DoubleProperty lcdThresholdProperty() {
        return model.lcdThresholdProperty();
    }

    public final boolean isLcdThresholdBehaviorInverted() {
        return model.isLcdThresholdBehaviorInverted();
    }

    public final void setLcdThresholdBehaviorInverted(final boolean LCD_THRESHOLD_BEHAVIOR_INVERTED) {
        model.setLcdThresholdBehaviorInverted(LCD_THRESHOLD_BEHAVIOR_INVERTED);
    }

    public final BooleanProperty lcdThresholdBehaviorInvertedProperty() {
        return model.lcdThresholdBehaviorInvertedProperty();
    }

    public final boolean isLcdThresholdVisible() {
        return viewModel.isLcdThresholdVisible();
    }

    public final void setLcdThresholdVisible(final boolean LCD_THRESHOLD_VISIBLE) {
        viewModel.setLcdThresholdVisible(LCD_THRESHOLD_VISIBLE);
    }

    public final BooleanProperty lcdThresholdVisibleProperty() {
        return viewModel.lcdThresholdVisibleProperty();
    }

    public final LcdDesign getLcdDesign() {
        return viewModel.getLcdDesign();
    }

    public final void setLcdDesign(final LcdDesign LCD_Design) {
        viewModel.setLcdDesign(LCD_Design);
    }

    public final ObjectProperty lcdDesignProperty() {
        return viewModel.lcdDesignProperty();
    }

    public final boolean isLcdVisible() {
        return viewModel.isLcdVisible();
    }

    public final void setLcdVisible(final boolean LCD_VISIBLE) {
        viewModel.setLcdVisible(LCD_VISIBLE);
    }

    public final BooleanProperty lcdVisibleProperty() {
        return viewModel.lcdVisibleProperty();
    }

    public final boolean isLcdDigitalFontEnabled() {
        return viewModel.isLcdDigitalFontEnabled();
    }

    public final void setLcdDigitalFontEnabled(final boolean LCD_DIGITAL_FONT_ENABLED) {
        viewModel.setLcdDigitalFontEnabled(LCD_DIGITAL_FONT_ENABLED);
    }

    public final BooleanProperty lcdDigitalFontEnabledProperty() {
        return viewModel.lcdDigitalFontEnabledProperty();
    }

    public final String getLcdUnit() {
        return model.getLcdUnit();
    }

    public final void setLcdUnit(final String LCD_UNIT) {
        model.setLcdUnit(LCD_UNIT);
    }

    public final StringProperty lcdUnitProperty() {
        return model.lcdUnitProperty();
    }

    public final boolean isLcdUnitVisible() {
        return viewModel.getLcdUnitVisible();
    }

    public final void setLcdUnitVisible(final boolean LCD_UNIT_VISIBLE) {
        viewModel.setLcdUnitVisible(LCD_UNIT_VISIBLE);
    }

    public final BooleanProperty lcdUnitVisibleProperty() {
        return viewModel.lcdUnitVisibleProperty();
    }

    public final String getLcdUnitFont() {
        return viewModel.getLcdUnitFont();
    }

    public final void setLcdUnitFont(final String UNIT_FONT) {
        viewModel.setLcdUnitFont(UNIT_FONT);
    }

    public final StringProperty lcdUnitFontProperty() {
        return viewModel.lcdUnitFontProperty();
    }

    public final NumberSystem getLcdNumberSystem() {
        return model.getLcdNumberSystem();
    }

    public final void setLcdNumberSystem(final NumberSystem LCD_NUMBER_SYSTEM) {
        model.setLcdNumberSystem(LCD_NUMBER_SYSTEM);
    }

    public final ObjectProperty lcdNumberSystemProperty() {
        return model.lcdNumberSystemProperty();
    }

    public final boolean isLcdNumberSystemVisible() {
        return viewModel.isLcdNumberSystemVisible();
    }

    public final void setLcdNumberSystemVisible(final boolean LCD_NUMBER_SYSTEM_VISIBLE) {
        viewModel.setLcdNumberSystemVisible(LCD_NUMBER_SYSTEM_VISIBLE);
    }

    public final BooleanProperty lcdNumberSystemVisibleProperty() {
        return viewModel.lcdNumberSystemVisibleProperty();
    }

    public final int getLcdDecimals() {
        return viewModel.getLcdDecimals();
    }

    public final void setLcdDecimals(final int LCD_DECIMALS) {
        viewModel.setLcdDecimals(LCD_DECIMALS);
    }

    public final IntegerProperty lcdDecimalsProperty() {
        return viewModel.lcdDecimalsProperty();
    }

    public final boolean isLcdBlinking() {
        return viewModel.isLcdBlinking();
    }

    public final void setLcdBlinking(final boolean LCD_BLINKING) {
        viewModel.setLcdBlinking(LCD_BLINKING);
    }

    public final BooleanProperty lcdBlinkingProperty() {
        return viewModel.lcdBlinkingProperty();
    }

    public final boolean isGlowVisible() {
        return viewModel.isGlowVisible();
    }

    public final void setGlowVisible(final boolean GLOW_VISIBLE) {
        viewModel.setGlowVisible(GLOW_VISIBLE);
    }

    public final BooleanProperty glowVisibleProperty() {
        return viewModel.glowVisibleProperty();
    }

    public final boolean isGlowOn() {
        return viewModel.isGlowOn();
    }

    public final void setGlowOn(final boolean GLOW_ON) {
        viewModel.setGlowOn(GLOW_ON);
    }

    public final BooleanProperty glowOnProperty() {
        return viewModel.glowOnProperty();
    }

    public final boolean isPulsatingGlow() {
        return viewModel.isPulsatingGlow();
    }

    public final void setPulsatingGlow(final boolean PULSATING_GLOW) {
        viewModel.setPulsatingGlow(PULSATING_GLOW);
    }

    public final BooleanProperty pulsatingGlowProperty() {
        return viewModel.pulsatingGlowProperty();
    }

    public final Color getGlowColor() {
        return viewModel.getGlowColor();
    }

    public final void setGlowColor(final Color GLOW_COLOR) {
        viewModel.setGlowColor(GLOW_COLOR);
    }

    public final ObjectProperty<Color> glowColorProperty() {
        return viewModel.glowColorProperty();
    }

    public final boolean isTickmarksVisible() {
        return viewModel.isTickmarksVisible();
    }

    public final void setTickmarksVisible(final boolean TICKMARKS_VISIBLE) {
        viewModel.setTickmarksVisible(TICKMARKS_VISIBLE);
    }

    public final BooleanProperty tickmarksVisibleProperty() {
        return viewModel.tickmarksVisibleProperty();
    }

    public final boolean isMajorTicksVisible() {
        return viewModel.isMajorTicksVisible();
    }

    public final void setMajorTicksVisible(final boolean MAJOR_TICKS_VISIBLE) {
        viewModel.setMajorTicksVisible(MAJOR_TICKS_VISIBLE);
    }

    public final BooleanProperty majorTicksVisibleProperty() {
        return viewModel.majorTicksVisibleProperty();
    }

    public final TickmarkType getMajorTickmarkType() {
        return viewModel.getMajorTickmarkType();
    }

    public final void setMajorTickmarkType(final TickmarkType TICKMARK_TYPE) {
        viewModel.setMajorTickmarkType(TICKMARK_TYPE);
    }

    public final ObjectProperty<TickmarkType> majorTickmarkTypeProperty() {
        return viewModel.majorTickmarkTypeProperty();
    }

    public final boolean isMinorTicksVisible() {
        return viewModel.isMinorTicksVisible();
    }

    public final void setMinorTicksVisible(final boolean MINOR_TICKS_VISIBLE) {
        viewModel.setMinorTicksVisible(MINOR_TICKS_VISIBLE);
    }

    public final BooleanProperty minorTicksVisibleProperty() {
        return viewModel.minorTicksVisibleProperty();
    }

    public final boolean isTickLabelsVisible() {
        return viewModel.isTickLabelsVisible();
    }

    public final void setTickLabelsVisible(final boolean TICKLABELS_VISIBLE) {
        viewModel.setTickLabelsVisible(TICKLABELS_VISIBLE);
    }

    public final BooleanProperty ticklabelsVisibleProperty() {
        return viewModel.ticklabelsVisibleProperty();
    }

    public final TicklabelOrientation getTickLabelOrientation() {
        return viewModel.getTickLabelOrientation();
    }

    public final void setTickLabelOrientation(final TicklabelOrientation TICKLABEL_ORIENTATION) {
        viewModel.setTickLabelOrientation(TICKLABEL_ORIENTATION);
    }

    public final ObjectProperty<TicklabelOrientation> tickLabelOrientationProperty() {
        return viewModel.tickLabelOrientationProperty();
    }

    public final NumberFormat getTickLabelNumberFormat() {
        return viewModel.getTickLabelNumberFormat();
    }

    public final void setTickLabelNumberFormat(final NumberFormat TICKLABEL_NUMBER_FORMAT) {
        viewModel.setTickLabelNumberFormat(TICKLABEL_NUMBER_FORMAT);
    }

    public final ObjectProperty<NumberFormat> tickLabelNumberFormatProperty() {
        return viewModel.tickLabelNumberFormatProperty();
    }

    public final Point2D getTickmarksOffset() {
        return viewModel.getTickmarksOffset();
    }

    public final void setTickmarksOffset(final Point2D TICKMARKS_OFFSET) {
        viewModel.setTickmarksOffset(TICKMARKS_OFFSET);
    }

    public final ObjectProperty<Point2D> tickmarksOffsetProperty() {
        return viewModel.tickmarksOffsetProperty();
    }

    public final int getMaxNoOfMajorTicks() {
        return model.getMaxNoOfMajorTicks();
    }

    public final void setMaxNoOfMajorTicks(final int MAX_NO_OF_MAJOR_TICKS) {
        model.setMaxNoOfMajorTicks(MAX_NO_OF_MAJOR_TICKS);
    }

    public final IntegerProperty maxNoOfMajorTicksProperty() {
        return model.maxNoOfMajorTicksProperty();
    }

    public final int getMaxNoOfMinorTicks() {
        return model.getMaxNoOfMinorTicks();
    }

    public final void setMaxNoOfMinorTicks(final int MAX_NO_OF_MINOR_TICKS) {
        model.setMaxNoOfMinorTicks(MAX_NO_OF_MINOR_TICKS);
    }

    public final IntegerProperty maxNoOfMinorTicksProperty() {
        return model.maxNoOfMinorTicksProperty();
    }

    public final int getMajorTickSpacing() {
        return model.getMajorTickSpacing();
    }

    public final void setMajorTickSpacing(final int MAJOR_TICKSPACING) {
        model.setMajorTickSpacing(MAJOR_TICKSPACING);
    }

    public final IntegerProperty majorTickSpacingProperty() {
        return model.majorTickSpacingProperty();
    }

    public final int getMinorTickSpacing() {
        return model.getMinorTickSpacing();
    }

    public final void setMinorTickSpacing(final int MINOR_TICKSPACING) {
        model.setMinorTickSpacing(MINOR_TICKSPACING);
    }

    public final IntegerProperty minorTickSpacingProperty() {
        return model.minorTickSpacingProperty();
    }

    public final boolean isNiceScaling() {
        return model.isNiceScaling();
    }

    public final void setNiceScaling(final boolean NICE_SCALING) {
        model.setNiceScaling(NICE_SCALING);
        recalcRange();
    }

    public final BooleanProperty niceScalingProperty() {
        return model.niceScalingProperty();
    }

    public final List<Section> getSections() {
        return model.getSections();
    }

    public final void setSections(final Section... SECTION_ARRAY) {
        model.setSections(SECTION_ARRAY);
    }

    public final void setSections(final List<Section> SECTIONS) {
        model.setSections(SECTIONS);
    }

    public final void addSection(final Section SECTION) {
        model.addSection(SECTION);
    }

    public final void removeSection(final Section SECTION) {
        model.removeSection(SECTION);
    }

    public final void resetSections() {
        model.resetSections();
    }

    public final boolean isSectionsVisible() {
        return viewModel.isSectionsVisible();
    }

    public final void setSectionsVisible(final boolean SECTIONS_VISIBLE) {
        viewModel.setSectionsVisible(SECTIONS_VISIBLE);
    }

    public final BooleanProperty sectionsVisibleProperty() {
        return viewModel.sectionsVisibleProperty();
    }

    public final boolean isExpandedSections() {
        return viewModel.isExpandedSections();
    }

    public final void setExpandedSections(final boolean EXPANDED_SECTIONS) {
        viewModel.setExpandedSections(EXPANDED_SECTIONS);
    }

    public final BooleanProperty expandedSectionsProperty() {
        return viewModel.expandedSectionsProperty();
    }

    public final boolean isSectionsHighlighting() {
        return viewModel.isSectionsHighlighting();
    }

    public final void setSectionsHighlighting(final boolean SECTIONS_HIGHLIGHTING) {
        viewModel.setSectionsHighlighting(SECTIONS_HIGHLIGHTING);
    }

    public final BooleanProperty sectionsHighlightingProperty() {
        return viewModel.sectionsHighlightingProperty();
    }

    public final List<Section> getAreas() {
        return model.getAreas();
    }

    public final void setAreas(final Section... AREA_ARRAY) {
        model.setAreas(AREA_ARRAY);
    }

    public final void setAreas(final List<Section> AREAS) {
        model.setAreas(AREAS);
    }

    public final void addArea(final Section AREA) {
        model.addArea(AREA);
    }

    public final void removeArea(final Section AREA) {
        model.removeArea(AREA);
    }

    public final void resetAreas() {
        model.resetAreas();
    }

    public final boolean isAreasVisible() {
        return viewModel.isAreasVisible();
    }

    public final void setAreasVisible(final boolean AREAS_VISIBLE) {
        viewModel.setAreasVisible(AREAS_VISIBLE);
    }

    public final BooleanProperty areasVisibleProperty() {
        return viewModel.areasVisibleProperty();
    }

    public final boolean isAreasHighlighting() {
        return viewModel.isAreasHighlighting();
    }

    public final void setAreasHighlighting(final boolean AREAS_HIGHLIGHTING) {
        viewModel.setAreasHighlighting(AREAS_HIGHLIGHTING);
    }

    public final BooleanProperty areasHighlightingProperty() {
        return viewModel.areasHighlightingProperty();
    }

    public final List<Indicator> getIndicators() {
        return model.getIndicators();
    }

    public final void setIndicators(final Indicator... INDICATOR_ARRAY) {
        model.setIndicators(INDICATOR_ARRAY);
    }

    public final void setIndicators(final List<Indicator> INDICATORS) {
        model.setIndicators(INDICATORS);
    }

    public final void addIndicator(final Indicator INDICATOR) {
        model.addIndicator(INDICATOR);
    }

    public final void removeIndicator(final Indicator INDICATOR) {
        model.removeIndicator(INDICATOR);
    }

    public final void resetIndicators() {
        model.resetIndicators();
    }

    public final boolean isIndicatorsVisible() {
        return viewModel.isIndicatorsVisible();
    }

    public final void setIndicatorsVisible(final boolean INDICATORS_VISIBLE) {
        viewModel.setIndicatorsVisible(INDICATORS_VISIBLE);
    }

    public final BooleanProperty indicatorsVisibleProperty() {
        return viewModel.indicatorsVisibleProperty();
    }

    public final Color getTextureColor() {
        return viewModel.getTextureColor();
    }

    public final String getTextureColorString() {
        return viewModel.getTextureColorString();
    }

    public final void setTextureColor(final Color TEXTURE_COLOR) {
        viewModel.setTextureColor(TEXTURE_COLOR);
    }

    public final ObjectProperty<Color> textureColorProperty() {
        return viewModel.textureColorProperty();
    }

    public final Color getSimpleGradientBaseColor() {
        return viewModel.getSimpleGradientBaseColor();
    }

    public final String getSimpleGradientBaseColorString() {
        return viewModel.getSimpleGradientBaseColorString();
    }

    public final void setSimpleGradientBaseColor(final Color SIMPLE_GRADIENT_BASE_COLOR) {
        viewModel.setSimpleGradientBaseColor(SIMPLE_GRADIENT_BASE_COLOR);
    }

    public final ObjectProperty<Color> simpleGradientBaseColorProperty() {
            return viewModel.simpleGradientBaseColorProperty();
        }

    public final boolean isTitleVisible() {
        return viewModel.isTitleVisible();
    }

    public final void setTitleVisible(final boolean TITLE_VISIBLE) {
        viewModel.setTitleVisible(TITLE_VISIBLE);
    }

    public final BooleanProperty titleVisibleProperty() {
        return viewModel.titleVisibleProperty();
    }

    public final boolean isUnitVisible() {
        return viewModel.isUnitVisible();
    }

    public final void setUnitVisible(final boolean UNIT_VISIBLE) {
        viewModel.setUnitVisible(UNIT_VISIBLE);
    }

    public final BooleanProperty unitVisibleProperty() {
        return viewModel.unitVisibleProperty();
    }

    public final Trend getTrend() {
        return model.getTrend();
    }

    public final void setTrend(final Trend TREND) {
        model.setTrend(TREND);
    }

    public final ObjectProperty<Trend> trendProperty() {
        return model.trendProperty();
    }

    public final boolean isTrendVisible() {
        return viewModel.isTrendVisible();
    }

    public final void setTrendVisible(final boolean TREND_VISIBLE) {
        viewModel.setTrendVisible(TREND_VISIBLE);
    }

    public final BooleanProperty trendVisibleProperty() {
        return viewModel.trendVisibleProperty();
    }

    public final Color getTrendUpColor() {
            return viewModel.getTrendUpColor();
        }

    public final void setTrendUpColor(final Color TREND_UP_COLOR) {
        viewModel.setTrendUpColor(TREND_UP_COLOR);
    }

    public final ObjectProperty<Color> trendUpColorProperty() {
        return viewModel.trendUpColorProperty();
    }

    public final Color getTrendSteadyColor() {
            return viewModel.getTrendSteadyColor();
    }

    public final void setTrendSteadyColor(final Color TREND_STEADY_COLOR) {
        viewModel.setTrendSteadyColor(TREND_STEADY_COLOR);
    }

    public final ObjectProperty<Color> trendSteadyColorProperty() {
        return viewModel.trendSteadyColorProperty();
    }

    public final Color getTrendDownColor() {
        return viewModel.getTrendDownColor();
    }

    public final void setTrendDownColor(final Color TREND_DOWN_COLOR) {
        viewModel.setTrendDownColor(TREND_DOWN_COLOR);
    }

    public final ObjectProperty<Color> trendDownColorProperty() {
            return viewModel.trendDownColorProperty();
        }
}
