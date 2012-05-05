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

package jfxtras.labs.internal.scene.control.skin;

import jfxtras.labs.internal.scene.control.behavior.RadialHalfSBehavior;
import jfxtras.labs.scene.control.gauge.Gauge;
import jfxtras.labs.scene.control.gauge.Marker;
import jfxtras.labs.scene.control.gauge.GaugeModelEvent;
import jfxtras.labs.scene.control.gauge.RadialHalfS;
import jfxtras.labs.scene.control.gauge.Section;
import jfxtras.labs.scene.control.gauge.StyleModelEvent;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.util.ArrayList;


/**
 * Created by
 * User: hansolo
 * Date: 26.01.12
 * Time: 17:32
 */
public class RadialHalfSSkin extends GaugeSkinBase<RadialHalfS, RadialHalfSBehavior> {
    private static final Rectangle PREF_SIZE = new Rectangle(200, 130);
    private RadialHalfS      control;
    private Rectangle        gaugeBounds;
    private Point2D          framelessOffset;
    private Group            frame;
    private Group            background;
    private Group            trend;
    private Group            sections;
    private Group            areas;
    private Group            markers;
    private Group            titleAndUnit;
    private Group            tickmarks;
    private Group            glowOff;
    private Group            glowOn;
    private ArrayList<Color> glowColors;
    private Group            knobs;
    private Group            threshold;
    private Group            minMeasured;
    private Group            maxMeasured;
    private Group            pointer;
    private Group            bargraphOff;
    private Group            bargraphOn;
    private Group            ledOff;
    private Group            ledOn;
    private Group            userLedOff;
    private Group            userLedOn;
    private Group            foreground;
    private Point2D          center;
    private int              noOfLeds;
    private ArrayList<Shape> ledsOff;
    private ArrayList<Shape> ledsOn;
    private DoubleProperty   currentValue;
    private DoubleProperty   formerValue;
    private FadeTransition   glowPulse;
    private RotateTransition pointerRotation;
    private AnimationTimer   ledTimer;
    private boolean          ledOnVisible;
    private long             lastLedTimerCall;
    private AnimationTimer   userLedTimer;
    private boolean          userLedOnVisible;
    private long             lastUserLedTimerCall;
    private boolean          isDirty;
    private boolean          initialized;


    // ******************** Constructors **************************************
    public RadialHalfSSkin(final RadialHalfS CONTROL) {
        super(CONTROL, new RadialHalfSBehavior(CONTROL));
        control                = CONTROL;
        gaugeBounds            = new Rectangle(200, 130);
        framelessOffset        = new Point2D(0, 0);
        center                 = new Point2D(0, 0);
        frame                  = new Group();
        background             = new Group();
        trend                  = new Group();
        sections               = new Group();
        areas                  = new Group();
        markers                = new Group();
        titleAndUnit           = new Group();
        tickmarks              = new Group();
        glowOff                = new Group();
        glowOn                 = new Group();
        glowColors             = new ArrayList<Color>(4);
        knobs                  = new Group();
        threshold              = new Group();
        minMeasured            = new Group();
        maxMeasured            = new Group();
        pointer                = new Group();
        bargraphOff            = new Group();
        bargraphOn             = new Group();
        ledOff                 = new Group();
        ledOn                  = new Group();
        userLedOff             = new Group();
        userLedOn              = new Group();
        foreground             = new Group();
        noOfLeds               = 35;
        ledsOff                = new ArrayList<Shape>(noOfLeds);
        ledsOn                 = new ArrayList<Shape>(noOfLeds);
        currentValue           = new SimpleDoubleProperty(0);
        formerValue            = new SimpleDoubleProperty(0);
        glowPulse              = new FadeTransition(Duration.millis(800), glowOn);
        pointerRotation        = new RotateTransition(Duration.millis(control.getAnimationDuration()), pointer);
        isDirty                = false;
        ledTimer               = new AnimationTimer() {
            @Override public void handle(final long CURRENT_NANOSECONDS) {
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > lastLedTimerCall + BLINK_INTERVAL) {
                    ledOnVisible ^= true;
                    if (ledOnVisible) {
                        ledOn.setOpacity(1.0);
                    } else {
                        ledOn.setOpacity(0.0);
                    }
                    lastLedTimerCall = currentNanoTime;
                }
            }
        };
        lastLedTimerCall       = 0l;
        ledOnVisible           = false;
        userLedTimer           = new AnimationTimer() {
            @Override public void handle(final long CURRENT_NANOSECONDS) {
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > lastUserLedTimerCall + BLINK_INTERVAL) {
                    userLedOnVisible ^= true;
                    if (userLedOnVisible) {
                        userLedOn.setOpacity(1.0);
                    } else {
                        userLedOn.setOpacity(0.0);
                    }
                    lastUserLedTimerCall = currentNanoTime;
                }
            }
        };
        lastUserLedTimerCall   = 0l;
        userLedOnVisible       = false;
        initialized            = false;
        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        if (control.getPrefWidth() < 0 || control.getPrefHeight() < 0) {
            control.setPrefSize(PREF_SIZE.getWidth(), PREF_SIZE.getHeight());
        }
        control.recalcRange();

        glowColors.clear();
        final Color GLOW_COLOR = control.getGlowColor();
        glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.46, 0.96, 0.0));
        glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));
        glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 1.0, 1.0, 1.0));
        glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));

        glowPulse.setFromValue(0.1);
        glowPulse.setToValue(1.0);
        glowPulse.setInterpolator(Interpolator.SPLINE(0.0, 0.0, 0.4, 1.0));
        glowPulse.setInterpolator(Interpolator.EASE_OUT);
        glowPulse.setCycleCount(Timeline.INDEFINITE);
        glowPulse.setAutoReverse(true);

        if (control.isPulsatingGlow() && control.isGlowVisible()) {
            if (!glowOn.isVisible()) {
                glowOn.setVisible(true);
            }
            if (glowOn.getOpacity() < 1.0) {
                glowOn.setOpacity(1.0);
            }
            glowPulse.play();
        } else {
            glowPulse.stop();
            glowOn.setOpacity(0.0);
        }

        if (control.isGlowVisible()) {
            glowOff.setVisible(true);
            if (control.isGlowOn()) {
                glowOn.setOpacity(1.0);
            } else {
                glowOn.setOpacity(0.0);
            }
        } else {
            glowOff.setVisible(false);
            glowOn.setOpacity(0.0);
        }

        ledOn.setOpacity(0.0);

        if (control.isUserLedOn()) {
            userLedOn.setOpacity(1.0);
        } else {
            userLedOn.setOpacity(0.0);
        }

        if (control.isUserLedBlinking()) {
            userLedTimer.start();
        }

        if (!control.getSections().isEmpty()) {
            updateSections();
        }

        if (!control.getAreas().isEmpty()) {
            updateAreas();
        }

        noOfLeds = (int) (control.getRadialRange().ANGLE_RANGE / 5.0);

        //double value = Double.compare(control.getValue(), control.getMinValue()) < 0 ? control.getMinValue() : (Double.compare(control.getValue(), control.getMaxValue()) > 0 ? control.getMaxValue() : control.getValue());
        //pointer.setRotate(-(value - control.getMinValue()) * control.getAngleStep());

        control.recalcRange();
        control.setMinMeasuredValue(control.getMaxValue());
        control.setMaxMeasuredValue(control.getMinValue());

        addBindings();
        addListeners();

        initialized = true;
        paint();
    }

    private void addBindings() {
        if (frame.visibleProperty().isBound()) {
            frame.visibleProperty().unbind();
        }
        frame.visibleProperty().bind(control.frameVisibleProperty());

        if (background.visibleProperty().isBound()) {
            background.visibleProperty().unbind();
        }
        background.visibleProperty().bind(control.backgroundVisibleProperty());

        if (sections.visibleProperty().isBound()) {
            sections.visibleProperty().unbind();
        }
        sections.visibleProperty().bind(control.sectionsVisibleProperty());

        if (areas.visibleProperty().isBound()) {
            areas.visibleProperty().unbind();
        }
        areas.visibleProperty().bind(control.areasVisibleProperty());

        if (bargraphOff.visibleProperty().isBound()) {
            bargraphOff.visibleProperty().unbind();
        }
        bargraphOff.visibleProperty().bind(control.bargraphProperty());
        if (bargraphOn.visibleProperty().isBound()) {
            bargraphOn.visibleProperty().unbind();
        }
        bargraphOn.visibleProperty().bind(control.bargraphProperty());
        pointer.setVisible(!bargraphOff.isVisible());
        knobs.setVisible(!bargraphOff.isVisible());
        if (bargraphOff.isVisible()){
            areas.setOpacity(0.0);
            sections.setOpacity(0.0);
        } else {
            areas.setOpacity(1.0);
            sections.setOpacity(1.0);
        }


        if (markers.visibleProperty().isBound()) {
            markers.visibleProperty().unbind();
        }
        markers.visibleProperty().bind(control.markersVisibleProperty());

        if (ledOff.visibleProperty().isBound()) {
            ledOff.visibleProperty().unbind();
        }
        ledOff.visibleProperty().bind(control.ledVisibleProperty());

        if (ledOn.visibleProperty().isBound()) {
            ledOn.visibleProperty().unbind();
        }
        ledOn.visibleProperty().bind(control.ledVisibleProperty());

        if (userLedOff.visibleProperty().isBound()) {
            userLedOff.visibleProperty().unbind();
        }
        userLedOff.visibleProperty().bind(control.userLedVisibleProperty());

        if (userLedOn.visibleProperty().isBound()) {
            userLedOn.visibleProperty().unbind();
        }
        userLedOn.visibleProperty().bind(control.userLedVisibleProperty());

        if (threshold.visibleProperty().isBound()) {
            threshold.visibleProperty().unbind();
        }
        threshold.visibleProperty().bind(control.thresholdVisibleProperty());

        if (minMeasured.visibleProperty().isBound()) {
            minMeasured.visibleProperty().unbind();
        }
        minMeasured.visibleProperty().bind(control.minMeasuredValueVisibleProperty());

        if (maxMeasured.visibleProperty().isBound()) {
            maxMeasured.visibleProperty().unbind();
        }
        maxMeasured.visibleProperty().bind(control.maxMeasuredValueVisibleProperty());

        if (foreground.visibleProperty().isBound()) {
            foreground.visibleProperty().unbind();
        }
        foreground.visibleProperty().bind(control.foregroundVisibleProperty());

        if (trend.visibleProperty().isBound()) {
            trend.visibleProperty().unbind();
        }
        trend.visibleProperty().bind(control.trendVisibleProperty());
    }

    private void addListeners() {
        control.setOnGaugeModelEvent(new EventHandler<GaugeModelEvent>() {
            @Override
            public void handle(final GaugeModelEvent EVENT) {
                // Trigger repaint
                addBindings();
                paint();
            }
        });

        control.setOnStyleModelEvent(new EventHandler<StyleModelEvent>() {
            @Override
            public void handle(final StyleModelEvent EVENT) {
                // Trigger repaint
                addBindings();
                paint();
            }
        });

        control.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                control.setPrefHeight(newValue.doubleValue());
                isDirty = true;
            }
        });

        control.prefHeightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                control.setPrefWidth(newValue.doubleValue());
                isDirty = true;
            }
        });

        control.thresholdExceededProperty().addListener(new ChangeListener<Boolean>() {
            @Override public void changed(ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) {
                if(newValue) {
                    ledTimer.start();
                } else {
                    ledTimer.stop();
                }
            }
        });

        control.valueProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                formerValue.set(oldValue.doubleValue() < control.getMinValue() ? control.getMinValue() : oldValue.doubleValue());
                if (pointerRotation.getStatus() != Animation.Status.STOPPED) {
                    pointerRotation.stop();
                }
                if (control.isValueAnimationEnabled()) {
                    pointerRotation.setFromAngle(-(formerValue.doubleValue() - control.getMinValue()) * control.getAngleStep());
                    pointerRotation.setToAngle(-(newValue.doubleValue() - control.getMinValue()) * control.getAngleStep());
                    pointerRotation.setInterpolator(Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
                    pointerRotation.play();
                } else {
                    pointer.setRotate(-(newValue.doubleValue() - control.getMinValue()) * control.getAngleStep());
                }

                checkMarkers(control, oldValue.doubleValue(), newValue.doubleValue());

                // Highlight sections
                if (control.isSectionsHighlighting()) {
                    final InnerShadow SECTION_INNER_GLOW = new InnerShadow();
                    SECTION_INNER_GLOW.setBlurType(BlurType.GAUSSIAN);
                    final DropShadow SECTION_GLOW = new DropShadow();
                    SECTION_GLOW.setWidth(0.05 * gaugeBounds.getWidth());
                    SECTION_GLOW.setHeight(0.05 * gaugeBounds.getHeight());
                    SECTION_GLOW.setBlurType(BlurType.GAUSSIAN);
                    for (final Section section : control.getSections()) {
                        final Shape currentSection = section.getSectionArea();
                        if (section.contains(newValue.doubleValue())) {
                            SECTION_INNER_GLOW.setColor(section.getColor().darker());
                            SECTION_GLOW.setInput(SECTION_INNER_GLOW);
                            SECTION_GLOW.setColor(section.getColor().brighter());
                            currentSection.setEffect(SECTION_GLOW);
                        } else {
                            currentSection.setEffect(null);
                        }
                    }
                }

                // Highlight areas
                if (control.isAreasHighlighting()) {
                    final InnerShadow AREA_INNER_GLOW = new InnerShadow();
                    AREA_INNER_GLOW.setBlurType(BlurType.GAUSSIAN);
                    final DropShadow AREA_GLOW = new DropShadow();
                    AREA_GLOW.setWidth(0.05 * gaugeBounds.getWidth());
                    AREA_GLOW.setHeight(0.05 * gaugeBounds.getHeight());
                    AREA_GLOW.setBlurType(BlurType.GAUSSIAN);
                    for (final Section area : control.getAreas()) {
                        final Shape currentArea = area.getFilledArea();
                        if (area.contains(newValue.doubleValue())) {
                            AREA_INNER_GLOW.setColor(area.getColor().darker());
                            AREA_GLOW.setInput(AREA_INNER_GLOW);
                            AREA_GLOW.setColor(area.getColor().brighter());
                            currentArea.setEffect(AREA_GLOW);
                        } else {
                            currentArea.setEffect(null);
                        }
                    }
                }

            }
        });

        pointer.rotateProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                currentValue.set(-newValue.doubleValue() / control.getAngleStep() + control.getMinValue());

                if (bargraphOff.isVisible()) {
                    final int CALC_CURRENT_INDEX = (newValue.intValue() / 5) * (-1);
                    final int CALC_FORMER_INDEX = (oldValue.intValue() / 5) * (-1);
                    final int CURRENT_LED_INDEX = CALC_CURRENT_INDEX < 0 ? 0 : (CALC_CURRENT_INDEX >= noOfLeds ? noOfLeds : CALC_CURRENT_INDEX) ;
                    final int FORMER_LED_INDEX = CALC_FORMER_INDEX < 0 ? 0 : (CALC_FORMER_INDEX >= noOfLeds ? noOfLeds - 1: CALC_FORMER_INDEX) ;

                    final int THRESHOLD_LED_INDEX = (int)(control.getThreshold() * control.getAngleStep() / 5.0);
                    if (Double.compare(control.getValue(), formerValue.doubleValue()) >= 0) {
                        for (int i = FORMER_LED_INDEX ; i < CURRENT_LED_INDEX ; i++) {
                            ledsOn.get(i).setVisible(true);
                        }
                    } else {
                        for (int i = FORMER_LED_INDEX ; i > CURRENT_LED_INDEX ; i--) {
                            ledsOn.get(i).setVisible(false);
                        }
                    }
                    if (control.isThresholdVisible()) {
                        ledsOn.get(THRESHOLD_LED_INDEX).setStyle(control.getThresholdColor().CSS);
                        ledsOn.get(THRESHOLD_LED_INDEX).setId("bargraph-threshold");
                        ledsOn.get(THRESHOLD_LED_INDEX).setVisible(true);
                    }
                }

                if (Double.compare(currentValue.get(), control.getMinMeasuredValue()) < 0) {
                    control.setMinMeasuredValue(currentValue.get());
                } else if (Double.compare(currentValue.get(), control.getMaxMeasuredValue()) > 0) {
                    control.setMaxMeasuredValue(currentValue.get());
                }
                if (control.isThresholdBehaviorInverted()) {
                    control.setThresholdExceeded(currentValue.get() < control.getThreshold());
                } else {
                    control.setThresholdExceeded(currentValue.get() > control.getThreshold());
                }
                if (!control.isThresholdExceeded()) {
                    ledOn.setOpacity(0.0);
                }
            }
        });
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);

        if ("ANIMATION_DURATION".equals(PROPERTY)) {
            pointerRotation.setDuration(Duration.millis(control.getAnimationDuration()));
        } else if ("RADIAL_RANGE".equals(PROPERTY)) {
            noOfLeds = (int) (control.getRadialRange().ANGLE_RANGE / 5.0);
            isDirty = true;
        } else if ("FRAME_DESIGN".equals(PROPERTY)) {
            drawFrame();
        } else if ("BACKGROUND_DESIGN".equals(PROPERTY)) {
            drawBackground();
            drawCircularTickmarks(control, tickmarks, center, gaugeBounds);
        } else if ("KNOB_DESIGN".equals(PROPERTY)) {
            drawCircularKnobs(control, knobs, center, gaugeBounds);
        } else if ("KNOB_COLOR".equals(PROPERTY)) {
            drawCircularKnobs(control, knobs, center, gaugeBounds);
        } else if ("POINTER_TYPE".equals(PROPERTY)) {
            drawPointer();
        } else if ("VALUE_COLOR".equals(PROPERTY)) {
            drawPointer();
        } else if ("FOREGROUND_TYPE".equals(PROPERTY)) {
            drawForeground();
        } else if ("USER_LED_BLINKING".equals(PROPERTY)) {
            if (userLedOff.isVisible() && userLedOn.isVisible()) {
                if (control.isUserLedBlinking()) {
                    userLedTimer.start();
                } else {
                    userLedTimer.stop();
                    userLedOn.setOpacity(0.0);
                }
            }
        } else if ("LED_BLINKING".equals(PROPERTY)) {
            if (ledOff.isVisible() && ledOn.isVisible()) {
                if (control.isLedBlinking()) {
                    ledTimer.start();
                } else {
                    ledTimer.stop();
                    ledOn.setOpacity(0.0);
                }
            }
        } else if ("GLOW_COLOR".equals(PROPERTY)) {
            glowColors.clear();
            final Color GLOW_COLOR = control.getGlowColor();
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.46, 0.96, 0.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 1.0, 1.0, 1.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));
            drawGlowOn();
        } else if ("GLOW_VISIBILITY".equals(PROPERTY)) {
            glowOff.setVisible(control.isGlowVisible());
            if (!control.isGlowVisible()) {
                glowOn.setOpacity(0.0);
            }
        } else if ("GLOW_ON".equals(PROPERTY)) {
            if (glowOff.isVisible() && control.isGlowOn()) {
                glowOn.setOpacity(1.0);
                glowOff.setVisible(true);
            } else {
                glowOff.setVisible(true);
                glowOn.setOpacity(0.0);
            }
        } else if ("PULSATING_GLOW".equals(PROPERTY)) {
            if (control.isPulsatingGlow() && control.isGlowVisible()) {
                if (!glowOn.isVisible()) {
                    glowOn.setVisible(true);
                }
                if (glowOn.getOpacity() < 1.0) {
                    glowOn.setOpacity(1.0);
                }
                glowPulse.play();
            } else {
                glowPulse.stop();
                glowOn.setOpacity(0.0);
            }
        } else if ("RANGE".equals(PROPERTY)) {
            drawCircularTickmarks(control, tickmarks, center, gaugeBounds);
        }  else if ("MIN_MEASURED_VALUE".equals(PROPERTY)) {
            final double ZERO_OFFSET = -90 + control.getRadialRange().ROTATION_OFFSET;
            minMeasured.setRotate(ZERO_OFFSET - (control.getMinMeasuredValue() - control.getMinValue()) * control.getAngleStep());
        } else if ("MAX_MEASURED_VALUE".equals(PROPERTY)) {
            final double ZERO_OFFSET = -90 + control.getRadialRange().ROTATION_OFFSET;
            maxMeasured.setRotate(ZERO_OFFSET - (control.getMaxMeasuredValue() - control.getMinValue()) * control.getAngleStep());
        } else if ("TREND".equals(PROPERTY)) {
            drawCircularTrend(control, trend, gaugeBounds);
        } else if ("SIMPLE_GRADIENT_BASE".equals(PROPERTY)) {
            isDirty = true;
        }
    }


    // ******************** Methods *******************************************
    public void paint() {
        if (!initialized) {
            init();
        }
        calcGaugeBounds();
        setTranslateX(framelessOffset.getX());
        setTranslateY(framelessOffset.getY());
        center = new Point2D(gaugeBounds.getWidth() * 0.5, gaugeBounds.getWidth() * 0.15);
        getChildren().clear();
        drawFrame();
        drawBackground();
        drawCircularTrend(control, trend, gaugeBounds);
        updateSections();
        drawSections();
        updateAreas();
        drawAreas();
        drawTitleAndUnit();
        drawCircularTickmarks(control, tickmarks, center, gaugeBounds);
        drawCircularLed(control, ledOff, ledOn, gaugeBounds);
        drawCircularUserLed(control, userLedOff, userLedOn, gaugeBounds);
        drawThreshold();
        drawGlowOff();
        drawGlowOn();
        drawMinMeasuredIndicator();
        drawMaxMeasuredIndicator();
        drawIndicators();
        drawPointer();
        bargraphOff.getTransforms().clear();
        bargraphOff.setTranslateY(-gaugeBounds.getWidth() * 0.35);
        drawCircularBargraph(control, bargraphOff, noOfLeds, ledsOff, false, true, new Point2D(center.getX(), gaugeBounds.getWidth() * 0.5), gaugeBounds);
        bargraphOn.getTransforms().clear();
        bargraphOn.setTranslateY(-gaugeBounds.getWidth() * 0.35);
        drawCircularBargraph(control, bargraphOn, noOfLeds, ledsOn, true, false, new Point2D(center.getX(), gaugeBounds.getWidth() * 0.5), gaugeBounds);
        drawCircularKnobs(control, knobs, center, gaugeBounds);
        drawForeground();

        getChildren().addAll(frame,
                             background,
                             sections,
                             areas,
                             trend,
                             ledOff,
                             ledOn,
                             userLedOff,
                             userLedOn,
                             titleAndUnit,
                             tickmarks,
                             threshold,
                             glowOff,
                             glowOn,
                             pointer,
                             bargraphOff,
                             bargraphOn,
                             minMeasured,
                             maxMeasured,
                             markers,
                             knobs,
                             foreground);
    }

    @Override public void layoutChildren() {
        if (isDirty) {
            paint();
            isDirty = false;
        }
        super.layoutChildren();
    }

    @Override public RadialHalfS getSkinnable() {
        return control;
    }

    @Override public void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double HEIGHT) {
        double prefWidth = PREF_SIZE.getWidth();
        if (HEIGHT != -1) {
            prefWidth = Math.max(0, HEIGHT - getInsets().getLeft() - getInsets().getRight()) * 1.5384615385;
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double WIDTH) {
        double prefHeight = PREF_SIZE.getHeight();
        if (WIDTH != -1) {
            prefHeight = Math.max(0, WIDTH - getInsets().getTop() - getInsets().getBottom()) / 1.5384615385;
        }
        return super.computePrefWidth(prefHeight);
    }

    private void calcGaugeBounds() {
        if (control.isFrameVisible()) {
            gaugeBounds.setWidth(control.getPrefWidth());
            gaugeBounds.setHeight(control.getPrefHeight());
            framelessOffset = new Point2D(0, 0);
        } else {
            gaugeBounds.setWidth(control.getPrefWidth() * 1.202247191);
            gaugeBounds.setHeight(control.getPrefHeight() * 1.202247191);
            framelessOffset = new Point2D(-gaugeBounds.getWidth() * 0.0841121495, -gaugeBounds.getWidth() * 0.0841121495);
        }
    }

    private void updateSections() {
        final double OUTER_RADIUS = control.getPrefWidth() * 0.38;
        final double INNER_RADIUS = control.isExpandedSections() ? OUTER_RADIUS - control.getPrefWidth() * 0.12 : OUTER_RADIUS - control.getPrefWidth() * 0.04;
        final Shape INNER = new Circle(center.getX(), center.getY(), INNER_RADIUS);

        for (final Section section : control.getSections()) {
            final double SECTION_START = section.getStart() < control.getMinValue() ? control.getMinValue() : section.getStart();
            final double SECTION_STOP = section.getStop() > control.getMaxValue() ? control.getMaxValue() : section.getStop();
            final double ANGLE_START = control.getRadialRange().SECTIONS_OFFSET + (SECTION_START * control.getAngleStep()) - (control.getMinValue() * control.getAngleStep());
            final double ANGLE_EXTEND = (SECTION_STOP - SECTION_START) * control.getAngleStep();

            final Arc OUTER_ARC = new Arc();
            OUTER_ARC.setType(ArcType.ROUND);
            OUTER_ARC.setCenterX(center.getX());
            OUTER_ARC.setCenterY(center.getY());
            OUTER_ARC.setRadiusX(OUTER_RADIUS);
            OUTER_ARC.setRadiusY(OUTER_RADIUS);
            OUTER_ARC.setStartAngle(ANGLE_START);
            OUTER_ARC.setLength(ANGLE_EXTEND);
            final Shape SECTION = Shape.subtract(OUTER_ARC, INNER);

            section.setSectionArea(SECTION);
        }
    }

    private void updateAreas() {
        final double OUTER_RADIUS = control.getPrefWidth() * 0.38;
        final double INNER_RADIUS = control.isExpandedSections() ? control.getPrefWidth() * 0.12 : control.getPrefWidth() * 0.04;
        final double RADIUS = OUTER_RADIUS - INNER_RADIUS;

        for (final Section area : control.getAreas()) {
            final double AREA_START = area.getStart() < control.getMinValue() ? control.getMinValue() : area.getStart();
            final double AREA_STOP = area.getStop() > control.getMaxValue() ? control.getMaxValue() : area.getStop();
            final double ANGLE_START = control.getRadialRange().SECTIONS_OFFSET + (AREA_START * control.getAngleStep()) - (control.getMinValue() * control.getAngleStep());
            final double ANGLE_EXTEND = (AREA_STOP - AREA_START) * control.getAngleStep();

            final Arc ARC = new Arc();
            ARC.setType(ArcType.ROUND);
            ARC.setCenterX(center.getX());
            ARC.setCenterY(center.getY());
            ARC.setRadiusX(RADIUS);
            ARC.setRadiusY(RADIUS);
            ARC.setStartAngle(ANGLE_START);
            ARC.setLength(ANGLE_EXTEND);

            area.setFilledArea(ARC);
        }
    }


    // ******************** Drawing *******************************************
    public void drawFrame() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        frame.getChildren().clear();

        //final Shape SUBTRACT = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, WIDTH * 0.4158878326);

        //final Shape OUTER_FRAME = Shape.subtract(new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.5 * WIDTH), SUBTRACT);

        final Path OUTER_FRAME = new Path();
        OUTER_FRAME.setFillRule(FillRule.EVEN_ODD);
        OUTER_FRAME.getElements().add(new MoveTo(WIDTH, 0.23076923076923078 * HEIGHT));
        OUTER_FRAME.getElements().add(new CubicCurveTo(WIDTH, 0.6538461538461539 * HEIGHT,
                                                       0.775 * WIDTH, HEIGHT,
                                                       0.5 * WIDTH, HEIGHT));
        OUTER_FRAME.getElements().add(new CubicCurveTo(0.225 * WIDTH, HEIGHT,
                                                       0.0, 0.6538461538461539 * HEIGHT,
            0.0, 0.23076923076923078 * HEIGHT));
        OUTER_FRAME.getElements().add(new CubicCurveTo(0.0, 0.23076923076923078 * HEIGHT,
                                                       0.0, 0.0,
                                                       0.0, 0.0));
        OUTER_FRAME.getElements().add(new LineTo(WIDTH, 0.0));
        OUTER_FRAME.getElements().add(new CubicCurveTo(WIDTH, 0.0,
                                                       WIDTH, 0.23076923076923078 * HEIGHT,
                                                       WIDTH, 0.23076923076923078 * HEIGHT));
        OUTER_FRAME.getElements().add(new ClosePath());
        OUTER_FRAME.setFill(new Color(0.5176470588, 0.5176470588, 0.5176470588, 1));
        OUTER_FRAME.setStroke(null);
        frame.getChildren().add(OUTER_FRAME);

        //final Shape INNER_FRAME = Shape.subtract(new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4205607476635514 * WIDTH), SUBTRACT);
        final Path INNER_FRAME = new Path();
        INNER_FRAME.setFillRule(FillRule.EVEN_ODD);
        INNER_FRAME.getElements().add(new MoveTo(0.07 * WIDTH, 0.23846153846153847 * HEIGHT));
        INNER_FRAME.getElements().add(new CubicCurveTo(0.07 * WIDTH, 0.23846153846153847 * HEIGHT,
                                                       0.07 * WIDTH, 0.1076923076923077 * HEIGHT,
                                                       0.07 * WIDTH, 0.1076923076923077 * HEIGHT));
        INNER_FRAME.getElements().add(new LineTo(0.92 * WIDTH, 0.1076923076923077 * HEIGHT));
        INNER_FRAME.getElements().add(new CubicCurveTo(0.92 * WIDTH, 0.1076923076923077 * HEIGHT,
                                                       0.92 * WIDTH, 0.23846153846153847 * HEIGHT,
                                                       0.92 * WIDTH, 0.23846153846153847 * HEIGHT));
        INNER_FRAME.getElements().add(new CubicCurveTo(0.92 * WIDTH, 0.6 * HEIGHT,
                                                       0.73 * WIDTH, 0.8923076923076924 * HEIGHT,
                                                       0.495 * WIDTH, 0.8923076923076924 * HEIGHT));
        INNER_FRAME.getElements().add(new CubicCurveTo(0.26 * WIDTH, 0.8923076923076924 * HEIGHT,
                                                       0.07 * WIDTH, 0.6 * HEIGHT,
                                                       0.07 * WIDTH, 0.23846153846153847 * HEIGHT));
        INNER_FRAME.getElements().add(new ClosePath());
        INNER_FRAME.setFill(new Color(0.6, 0.6, 0.6, 0.8));
        INNER_FRAME.setStroke(null);

        //final Shape MAIN_FRAME = Shape.subtract(new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4953271028037383 * WIDTH), SUBTRACT);
        final Path MAIN_FRAME = new Path();
        MAIN_FRAME.setFillRule(FillRule.EVEN_ODD);
        MAIN_FRAME.getElements().add(new MoveTo(0.995 * WIDTH, 0.23076923076923078 * HEIGHT));
        MAIN_FRAME.getElements().add(new CubicCurveTo(0.995 * WIDTH, 0.6538461538461539 * HEIGHT,
                                                      0.775 * WIDTH, 0.9923076923076923 * HEIGHT,
                                                      0.5 * WIDTH, 0.9923076923076923 * HEIGHT));
        MAIN_FRAME.getElements().add(new CubicCurveTo(0.225 * WIDTH, 0.9923076923076923 * HEIGHT,
                                                      0.0050 * WIDTH, 0.6538461538461539 * HEIGHT,
            0.0050 * WIDTH, 0.23076923076923078 * HEIGHT));
        MAIN_FRAME.getElements().add(new CubicCurveTo(0.0050 * WIDTH, 0.23076923076923078 * HEIGHT,
                                                      0.0050 * WIDTH, 0.007692307692307693 * HEIGHT,
                                                      0.0050 * WIDTH, 0.007692307692307693 * HEIGHT));
        MAIN_FRAME.getElements().add(new LineTo(0.995 * WIDTH, 0.007692307692307693 * HEIGHT));
        MAIN_FRAME.getElements().add(new CubicCurveTo(0.995 * WIDTH, 0.007692307692307693 * HEIGHT,
                                                      0.995 * WIDTH, 0.23076923076923078 * HEIGHT,
                                                      0.995 * WIDTH, 0.23076923076923078 * HEIGHT));
        MAIN_FRAME.getElements().add(new ClosePath());
        final ImageView IMAGE_VIEW;
        switch (control.getFrameDesign()) {
            case GLOSSY_METAL:
                final Paint GLOSSY1_FILL = new RadialGradient(0, 0,
                                                              0.5 * WIDTH, 0.9923076923076923 * HEIGHT,
                                                              0.495 * WIDTH,
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, Color.color(0.8235294118, 0.8235294118, 0.8235294118, 1)),
                                                              new Stop(0.95, Color.color(0.8235294118, 0.8235294118, 0.8235294118, 1)),
                                                              new Stop(1.0, Color.color(0.9960784314, 0.9960784314, 0.9960784314, 1)));
                MAIN_FRAME.setFill(GLOSSY1_FILL);
                MAIN_FRAME.setStroke(null);

                final Path GLOSSY2 = new Path();
                GLOSSY2.setFillRule(FillRule.EVEN_ODD);
                GLOSSY2.getElements().add(new MoveTo(0.985 * WIDTH, 0.23076923076923078 * HEIGHT));
                GLOSSY2.getElements().add(new CubicCurveTo(0.985 * WIDTH, 0.6153846153846154 * HEIGHT,
                                                           0.775 * WIDTH, 0.9769230769230769 * HEIGHT,
                                                           0.5 * WIDTH, 0.9769230769230769 * HEIGHT));
                GLOSSY2.getElements().add(new CubicCurveTo(0.225 * WIDTH, 0.9769230769230769 * HEIGHT,
                                                           0.015 * WIDTH, 0.6153846153846154 * HEIGHT,
                                                           0.015 * WIDTH, 0.23076923076923078 * HEIGHT));
                GLOSSY2.getElements().add(new CubicCurveTo(0.015 * WIDTH, 0.23076923076923078 * HEIGHT,
                                                           0.015 * WIDTH, 0.023076923076923078 * HEIGHT,
                                                           0.015 * WIDTH, 0.023076923076923078 * HEIGHT));
                GLOSSY2.getElements().add(new LineTo(0.985 * WIDTH, 0.023076923076923078 * HEIGHT));
                GLOSSY2.getElements().add(new CubicCurveTo(0.985 * WIDTH, 0.023076923076923078 * HEIGHT,
                                                           0.985 * WIDTH, 0.23076923076923078 * HEIGHT,
                                                           0.985 * WIDTH, 0.23076923076923078 * HEIGHT));
                GLOSSY2.getElements().add(new ClosePath());
                final Paint GLOSSY2_FILL = new LinearGradient(0, 0.03076923076923077 * HEIGHT,
                                                              0, 0.9692307692307692 * HEIGHT,
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, Color.color(0.9764705882, 0.9764705882, 0.9764705882, 1)),
                                                              new Stop(0.23, Color.color(0.7843137255, 0.7647058824, 0.7490196078, 1)),
                                                              new Stop(0.34, Color.color(0.8235294118, 0.8235294118, 0.8235294118, 1)),
                                                              new Stop(0.65, Color.color(0.1215686275, 0.1215686275, 0.1215686275, 1)),
                                                              new Stop(0.84, Color.color(0.7843137255, 0.7607843137, 0.7529411765, 1)),
                                                              new Stop(1.0, Color.color(0.7843137255, 0.7607843137, 0.7529411765, 1)));
                GLOSSY2.setFill(GLOSSY2_FILL);
                GLOSSY2.setStroke(null);

                final Path GLOSSY3 = new Path();
                GLOSSY3.setFillRule(FillRule.EVEN_ODD);
                GLOSSY3.getElements().add(new MoveTo(0.935 * WIDTH, 0.23076923076923078 * HEIGHT));
                GLOSSY3.getElements().add(new CubicCurveTo(0.935 * WIDTH, 0.5692307692307692 * HEIGHT,
                                                           0.77 * WIDTH, 0.9153846153846154 * HEIGHT,
                                                           0.495 * WIDTH, 0.9153846153846154 * HEIGHT));
                GLOSSY3.getElements().add(new CubicCurveTo(0.22 * WIDTH, 0.9153846153846154 * HEIGHT,
                                                           0.055 * WIDTH, 0.5615384615384615 * HEIGHT,
                                                           0.055 * WIDTH, 0.23076923076923078 * HEIGHT));
                GLOSSY3.getElements().add(new CubicCurveTo(0.055 * WIDTH, 0.23076923076923078 * HEIGHT,
                                                           0.055 * WIDTH, 0.08461538461538462 * HEIGHT,
                                                           0.055 * WIDTH, 0.08461538461538462 * HEIGHT));
                GLOSSY3.getElements().add(new LineTo(0.935 * WIDTH, 0.08461538461538462 * HEIGHT));
                GLOSSY3.getElements().add(new CubicCurveTo(0.935 * WIDTH, 0.08461538461538462 * HEIGHT,
                                                           0.935 * WIDTH, 0.23076923076923078 * HEIGHT,
                                                           0.935 * WIDTH, 0.23076923076923078 * HEIGHT));
                GLOSSY3.getElements().add(new ClosePath());
                final Paint GLOSSY3_FILL = Color.color(0.9647058824, 0.9647058824, 0.9647058824, 1);
                GLOSSY3.setFill(GLOSSY3_FILL);
                GLOSSY3.setStroke(null);

                final Path GLOSSY4 = new Path();
                GLOSSY4.setFillRule(FillRule.EVEN_ODD);
                GLOSSY4.getElements().add(new MoveTo(0.065 * WIDTH, 0.23846153846153847 * HEIGHT));
                GLOSSY4.getElements().add(new CubicCurveTo(0.065 * WIDTH, 0.23846153846153847 * HEIGHT,
                                                           0.065 * WIDTH, 0.1 * HEIGHT,
                                                           0.065 * WIDTH, 0.1 * HEIGHT));
                GLOSSY4.getElements().add(new LineTo(0.925 * WIDTH, 0.1 * HEIGHT));
                GLOSSY4.getElements().add(new CubicCurveTo(0.925 * WIDTH, 0.1 * HEIGHT,
                                                           0.925 * WIDTH, 0.23846153846153847 * HEIGHT,
                                                           0.925 * WIDTH, 0.23846153846153847 * HEIGHT));
                GLOSSY4.getElements().add(new CubicCurveTo(0.925 * WIDTH, 0.6076923076923076 * HEIGHT,
                                                           0.73 * WIDTH, 0.9 * HEIGHT,
                                                           0.495 * WIDTH, 0.9 * HEIGHT));
                GLOSSY4.getElements().add(new CubicCurveTo(0.26 * WIDTH, 0.9 * HEIGHT,
                                                           0.065 * WIDTH, 0.6076923076923076 * HEIGHT,
                                                           0.065 * WIDTH, 0.23846153846153847 * HEIGHT));
                GLOSSY4.getElements().add(new ClosePath());
                final Paint GLOSSY4_FILL = Color.color(0.2, 0.2, 0.2, 1);
                GLOSSY4.setFill(GLOSSY4_FILL);
                GLOSSY4.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, GLOSSY2, GLOSSY3, GLOSSY4);
                break;
            case DARK_GLOSSY:
                final Paint DARK_GLOSSY1_FILL = new LinearGradient(0.855 * WIDTH, 0.0,
                                                                   0.1691064222490489 * WIDTH, 1.0552208888476171 * HEIGHT,
                                                                   false, CycleMethod.NO_CYCLE,
                                                                   new Stop(0.0, Color.color(0.6745098039, 0.6745098039, 0.6784313725, 1)),
                                                                   new Stop(0.08, Color.color(0.9960784314, 0.9960784314, 1, 1)),
                                                                   new Stop(0.52, Color.BLACK),
                                                                   new Stop(0.55, Color.color(0.0196078431, 0.0235294118, 0.0196078431, 1)),
                                                                   new Stop(0.84, Color.color(0.9725490196, 0.9803921569, 0.9764705882, 1)),
                                                                   new Stop(0.99, Color.color(0.7058823529, 0.7058823529, 0.7058823529, 1)),
                                                                   new Stop(1.0, Color.color(0.6980392157, 0.6980392157, 0.6980392157, 1)));
                MAIN_FRAME.setFill(DARK_GLOSSY1_FILL);
                MAIN_FRAME.setStroke(null);

                final Path DARK_GLOSSY2 = new Path();
                DARK_GLOSSY2.setFillRule(FillRule.EVEN_ODD);
                DARK_GLOSSY2.getElements().add(new MoveTo(0.985 * WIDTH, 0.23076923076923078 * HEIGHT));
                DARK_GLOSSY2.getElements().add(new CubicCurveTo(0.985 * WIDTH, 0.6153846153846154 * HEIGHT,
                                                                0.775 * WIDTH, 0.9769230769230769 * HEIGHT,
                                                                0.5 * WIDTH, 0.9769230769230769 * HEIGHT));
                DARK_GLOSSY2.getElements().add(new CubicCurveTo(0.225 * WIDTH, 0.9769230769230769 * HEIGHT,
                                                                0.015 * WIDTH, 0.6153846153846154 * HEIGHT,
                                                                0.015 * WIDTH, 0.23076923076923078 * HEIGHT));
                DARK_GLOSSY2.getElements().add(new CubicCurveTo(0.015 * WIDTH, 0.23076923076923078 * HEIGHT,
                                                                0.015 * WIDTH, 0.023076923076923078 * HEIGHT,
                                                                0.015 * WIDTH, 0.023076923076923078 * HEIGHT));
                DARK_GLOSSY2.getElements().add(new LineTo(0.985 * WIDTH, 0.023076923076923078 * HEIGHT));
                DARK_GLOSSY2.getElements().add(new CubicCurveTo(0.985 * WIDTH, 0.023076923076923078 * HEIGHT,
                                                                0.985 * WIDTH, 0.23076923076923078 * HEIGHT,
                                                                0.985 * WIDTH, 0.23076923076923078 * HEIGHT));
                DARK_GLOSSY2.getElements().add(new ClosePath());
                final Paint DARK_GLOSSY2_FILL = new LinearGradient(0, 0.023076923076923078 * HEIGHT,
                                                                   0, 0.9769230769230769 * HEIGHT,
                                                                   false, CycleMethod.NO_CYCLE,
                                                                   new Stop(0.0, Color.color(0.9764705882, 0.9764705882, 0.9764705882, 1)),
                                                                   new Stop(1.0E-4, Color.BLACK),
                                                                   new Stop(0.41, Color.color(0.2588235294, 0.2588235294, 0.2588235294, 1)),
                                                                   new Stop(1.0, Color.color(0.0509803922, 0.0509803922, 0.0509803922, 1)));
                DARK_GLOSSY2.setFill(DARK_GLOSSY2_FILL);
                DARK_GLOSSY2.setStroke(null);

                final Path DARK_GLOSSY3 = new Path();
                DARK_GLOSSY3.setFillRule(FillRule.EVEN_ODD);
                DARK_GLOSSY3.getElements().add(new MoveTo(0.985 * WIDTH, 0.26153846153846155 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.985 * WIDTH, 0.26153846153846155 * HEIGHT,
                                                                0.985 * WIDTH, 0.023076923076923078 * HEIGHT,
                                                                0.985 * WIDTH, 0.023076923076923078 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new LineTo(0.015 * WIDTH, 0.023076923076923078 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.015 * WIDTH, 0.023076923076923078 * HEIGHT,
                                                                0.015 * WIDTH, 0.23076923076923078 * HEIGHT,
                                                                0.015 * WIDTH, 0.23076923076923078 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.015 * WIDTH, 0.36153846153846153 * HEIGHT,
                                                                0.04 * WIDTH, 0.49230769230769234 * HEIGHT,
                                                                0.08 * WIDTH, 0.6 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.175 * WIDTH, 0.4153846153846154 * HEIGHT,
                                                                0.35 * WIDTH, 0.1076923076923077 * HEIGHT,
                                                                0.5 * WIDTH, 0.1076923076923077 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.66 * WIDTH, 0.1076923076923077 * HEIGHT,
                                                                0.83 * WIDTH, 0.4076923076923077 * HEIGHT,
                                                                0.92 * WIDTH, 0.6 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.96 * WIDTH, 0.49230769230769234 * HEIGHT,
                                                                0.98 * WIDTH, 0.3769230769230769 * HEIGHT,
                                                                0.985 * WIDTH, 0.26153846153846155 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new ClosePath());
                final Paint DARK_GLOSSY3_FILL = new LinearGradient(0, 0.03076923076923077 * HEIGHT,
                                                                   0, 0.6 * HEIGHT,
                                                                   false, CycleMethod.NO_CYCLE,
                                                                   new Stop(0.0, Color.color(0.9764705882, 0.9764705882, 0.9764705882, 1)),
                                                                   new Stop(0.26, Color.color(1, 1, 1, 0.7372549020)),
                                                                   new Stop(1.0, Color.color(1, 1, 1, 0)));
                DARK_GLOSSY3.setFill(DARK_GLOSSY3_FILL);
                DARK_GLOSSY3.setStroke(null);

                final Path DARK_GLOSSY4 = new Path();
                DARK_GLOSSY4.setFillRule(FillRule.EVEN_ODD);
                DARK_GLOSSY4.getElements().add(new MoveTo(0.065 * WIDTH, 0.23846153846153847 * HEIGHT));
                DARK_GLOSSY4.getElements().add(new CubicCurveTo(0.065 * WIDTH, 0.23846153846153847 * HEIGHT,
                                                                0.065 * WIDTH, 0.1 * HEIGHT,
                                                                0.065 * WIDTH, 0.1 * HEIGHT));
                DARK_GLOSSY4.getElements().add(new LineTo(0.925 * WIDTH, 0.1 * HEIGHT));
                DARK_GLOSSY4.getElements().add(new CubicCurveTo(0.925 * WIDTH, 0.1 * HEIGHT,
                                                                0.925 * WIDTH, 0.23846153846153847 * HEIGHT,
                                                                0.925 * WIDTH, 0.23846153846153847 * HEIGHT));
                DARK_GLOSSY4.getElements().add(new CubicCurveTo(0.925 * WIDTH, 0.6076923076923076 * HEIGHT,
                                                                0.73 * WIDTH, 0.9 * HEIGHT,
                                                                0.495 * WIDTH, 0.9 * HEIGHT));
                DARK_GLOSSY4.getElements().add(new CubicCurveTo(0.26 * WIDTH, 0.9 * HEIGHT,
                                                                0.065 * WIDTH, 0.6076923076923076 * HEIGHT,
                                                                0.065 * WIDTH, 0.23846153846153847 * HEIGHT));
                DARK_GLOSSY4.getElements().add(new ClosePath());
                final Paint DARK_GLOSSY4_FILL = new LinearGradient(0.805 * WIDTH, 0.1 * HEIGHT,
                                                                   0.1755776747036804 * WIDTH, 1.035117037156343 * HEIGHT,
                                                                   false, CycleMethod.NO_CYCLE,
                                                                   new Stop(0.0, Color.color(0.6745098039, 0.6745098039, 0.6784313725, 1)),
                                                                   new Stop(0.07, Color.color(0.9568627451, 0.9568627451, 0.9607843137, 1)),
                                                                   new Stop(0.08, Color.color(0.9960784314, 0.9960784314, 1, 1)),
                                                                   new Stop(0.52, Color.BLACK),
                                                                   new Stop(0.5201, Color.BLACK),
                                                                   new Stop(0.55, Color.color(0.0196078431, 0.0235294118, 0.0196078431, 1)),
                                                                   new Stop(0.56, Color.color(0.0352941176, 0.0392156863, 0.0352941176, 1)),
                                                                   new Stop(0.9, Color.color(0.9725490196, 0.9803921569, 0.9764705882, 1)),
                                                                   new Stop(0.92, Color.color(0.9058823529, 0.9137254902, 0.9098039216, 1)),
                                                                   new Stop(1.0, Color.color(0.6980392157, 0.6980392157, 0.6980392157, 1)));
                DARK_GLOSSY4.setFill(DARK_GLOSSY4_FILL);
                DARK_GLOSSY4.setStroke(null);


                frame.getChildren().addAll(MAIN_FRAME, DARK_GLOSSY2, DARK_GLOSSY3, DARK_GLOSSY4);
                break;
            default:
                IMAGE_VIEW = new ImageView();
                IMAGE_VIEW.setVisible(false);
                MAIN_FRAME.setId(control.getFrameDesign().CSS);
                MAIN_FRAME.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, INNER_FRAME);
                break;
        }
    }

    public void drawBackground() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        background.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        background.getChildren().add(IBOUNDS);

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setWidth(0.2 * SIZE);
        INNER_SHADOW.setHeight(0.2 * SIZE);
        INNER_SHADOW.setColor(Color.color(0, 0, 0, 1.0));
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final LinearGradient HL_GRADIENT = new LinearGradient(0, 0, SIZE, 0, false, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.color(0.0, 0.0, 0.0, 0.6)),
            new Stop(0.4, Color.color(0.0, 0.0, 0.0, 0.0)),
            new Stop(0.6, Color.color(0.0, 0.0, 0.0, 0.0)),
            new Stop(1.0, Color.color(0.0, 0.0, 0.0, 0.6)));

        final Path BACKGROUND = new Path();
        BACKGROUND.setFillRule(FillRule.EVEN_ODD);
        BACKGROUND.getElements().add(new MoveTo(0.075 * WIDTH, 0.23846153846153847 * HEIGHT));
        BACKGROUND.getElements().add(new CubicCurveTo(0.075 * WIDTH, 0.23846153846153847 * HEIGHT,
                                                      0.075 * WIDTH, 0.11538461538461539 * HEIGHT,
                                                      0.075 * WIDTH, 0.11538461538461539 * HEIGHT));
        BACKGROUND.getElements().add(new LineTo(0.915 * WIDTH, 0.11538461538461539 * HEIGHT));
        BACKGROUND.getElements().add(new CubicCurveTo(0.915 * WIDTH, 0.11538461538461539 * HEIGHT,
                                                      0.915 * WIDTH, 0.23846153846153847 * HEIGHT,
                                                      0.915 * WIDTH, 0.23846153846153847 * HEIGHT));
        BACKGROUND.getElements().add(new CubicCurveTo(0.915 * WIDTH, 0.5923076923076923 * HEIGHT,
                                                      0.725 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                      0.495 * WIDTH, 0.8846153846153846 * HEIGHT));
        BACKGROUND.getElements().add(new CubicCurveTo(0.265 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                      0.075 * WIDTH, 0.5923076923076923 * HEIGHT,
                                                      0.075 * WIDTH, 0.23846153846153847 * HEIGHT));
        BACKGROUND.getElements().add(new ClosePath());
        BACKGROUND.setStroke(null);

        final Path CLIP = new Path();
        CLIP.setFillRule(FillRule.EVEN_ODD);
        CLIP.getElements().add(new MoveTo(0.075 * WIDTH, 0.23846153846153847 * HEIGHT));
        CLIP.getElements().add(new CubicCurveTo(0.075 * WIDTH, 0.23846153846153847 * HEIGHT,
                                                0.075 * WIDTH, 0.11538461538461539 * HEIGHT,
                                                0.075 * WIDTH, 0.11538461538461539 * HEIGHT));
        CLIP.getElements().add(new LineTo(0.915 * WIDTH, 0.11538461538461539 * HEIGHT));
        CLIP.getElements().add(new CubicCurveTo(0.915 * WIDTH, 0.11538461538461539 * HEIGHT,
                                                0.915 * WIDTH, 0.23846153846153847 * HEIGHT,
                                                0.915 * WIDTH, 0.23846153846153847 * HEIGHT));
        CLIP.getElements().add(new CubicCurveTo(0.915 * WIDTH, 0.5923076923076923 * HEIGHT,
                                                0.725 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                0.495 * WIDTH, 0.8846153846153846 * HEIGHT));
        CLIP.getElements().add(new CubicCurveTo(0.265 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                0.075 * WIDTH, 0.5923076923076923 * HEIGHT,
                                                0.075 * WIDTH, 0.23846153846153847 * HEIGHT));
        CLIP.getElements().add(new ClosePath());

        final ImageView IMAGE_VIEW;
        switch (control.getBackgroundDesign()) {
            default:
                BACKGROUND.setStyle(control.getSimpleGradientBaseColorString());
                BACKGROUND.setId(control.getBackgroundDesign().CSS_BACKGROUND);
                BACKGROUND.setEffect(INNER_SHADOW);
                BACKGROUND.setStroke(null);
                background.getChildren().addAll(BACKGROUND);
                break;
        }
    }

    public void drawSections() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        sections.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        sections.getChildren().add(IBOUNDS);

        for (final Section section : control.getSections()) {
            final Shape currentSection = section.getSectionArea();
            currentSection.setFill(section.getTransparentColor());
            currentSection.setStroke(null);
            sections.getChildren().add(currentSection);
        }
    }

    public void drawAreas() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        areas.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        areas.getChildren().add(IBOUNDS);

        for (final Section area : control.getAreas()) {
            final Shape currentArea = area.getFilledArea();
            currentArea.setFill(area.getTransparentColor());
            currentArea.setStroke(null);
            areas.getChildren().add(currentArea);
        }
    }

    public void drawTitleAndUnit() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        titleAndUnit.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        titleAndUnit.getChildren().add(IBOUNDS);

        final Font TITLE_FONT = Font.font(control.getTitleFont(), FontWeight.NORMAL, (0.046728972 * WIDTH));
        final Text title = new Text();
        title.setTextOrigin(VPos.BOTTOM);
        title.setFont(TITLE_FONT);
        title.setText(control.getTitle());
        title.setX(((WIDTH - title.getLayoutBounds().getWidth()) / 2.0));
        title.setY(0.315 * WIDTH + title.getLayoutBounds().getHeight());
        title.setId(control.getBackgroundDesign().CSS_TEXT);

        final Font UNIT_FONT = Font.font(control.getUnitFont(), FontWeight.NORMAL, (0.046728972 * WIDTH));
        final Text unit = new Text();
        unit.setTextOrigin(VPos.BOTTOM);
        unit.setFont(UNIT_FONT);
        unit.setText(control.getUnit());
        unit.setX((WIDTH - unit.getLayoutBounds().getWidth()) / 2.0);
        unit.setY(0.25 * WIDTH + unit.getLayoutBounds().getHeight());
        unit.setId(control.getBackgroundDesign().CSS_TEXT);

        titleAndUnit.getChildren().addAll(title, unit);
    }

    public void drawGlowOff() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        glowOff.getChildren().clear();

        final Path GLOW_RING = new Path();
        GLOW_RING.setFillRule(FillRule.EVEN_ODD);
        GLOW_RING.getElements().add(new MoveTo(0.1 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.1 * WIDTH, 0.5846153846153846 * HEIGHT,
                                                     0.275 * WIDTH, 0.8461538461538461 * HEIGHT,
                                                     0.495 * WIDTH, 0.8461538461538461 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.715 * WIDTH, 0.8461538461538461 * HEIGHT,
                                                     0.89 * WIDTH, 0.5846153846153846 * HEIGHT,
                                                     0.89 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.89 * WIDTH, 0.2153846153846154 * HEIGHT,
                                                     0.89 * WIDTH, 0.15384615384615385 * HEIGHT,
                                                     0.89 * WIDTH, 0.15384615384615385 * HEIGHT));
        GLOW_RING.getElements().add(new LineTo(0.1 * WIDTH, 0.15384615384615385 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.1 * WIDTH, 0.15384615384615385 * HEIGHT,
                                                     0.1 * WIDTH, 0.2153846153846154 * HEIGHT,
                                                     0.1 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new ClosePath());
        GLOW_RING.getElements().add(new MoveTo(0.075 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.075 * WIDTH, 0.2153846153846154 * HEIGHT,
                                                     0.075 * WIDTH, 0.15384615384615385 * HEIGHT,
                                                     0.075 * WIDTH, 0.15384615384615385 * HEIGHT));
        GLOW_RING.getElements().add(new LineTo(0.075 * WIDTH, 0.11538461538461539 * HEIGHT));
        GLOW_RING.getElements().add(new LineTo(0.915 * WIDTH, 0.11538461538461539 * HEIGHT));
        GLOW_RING.getElements().add(new LineTo(0.915 * WIDTH, 0.15384615384615385 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.915 * WIDTH, 0.15384615384615385 * HEIGHT,
                                                     0.915 * WIDTH, 0.2153846153846154 * HEIGHT,
                                                     0.915 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.915 * WIDTH, 0.5923076923076923 * HEIGHT,
                                                     0.725 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                     0.495 * WIDTH, 0.8846153846153846 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.265 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                     0.075 * WIDTH, 0.5923076923076923 * HEIGHT,
                                                     0.075 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new ClosePath());
        final Paint GLOW_OFF_PAINT = new LinearGradient(0.495 * WIDTH, 0.11538461538461539 * HEIGHT,
                                                        0.495 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.8, 0.8, 0.8, 0.4)),
                                                        new Stop(0.17, Color.color(0.6, 0.6, 0.6, 0.4)),
                                                        new Stop(0.33, Color.color(0.9882352941, 0.9882352941, 0.9882352941, 0.4)),
                                                        new Stop(0.34, Color.color(1, 1, 1, 0.4)),
                                                        new Stop(0.63, Color.color(0.8, 0.8, 0.8, 0.4)),
                                                        new Stop(0.64, Color.color(0.7960784314, 0.7960784314, 0.7960784314, 0.4)),
                                                        new Stop(0.83, Color.color(0.6, 0.6, 0.6, 0.4)),
                                                        new Stop(1.0, Color.color(1, 1, 1, 0.4)));
        GLOW_RING.setFill(GLOW_OFF_PAINT);
        GLOW_RING.setStroke(null);

        final Path HIGHLIGHT_LOWER_RIGHT = new Path();
        HIGHLIGHT_LOWER_RIGHT.setFillRule(FillRule.EVEN_ODD);
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new MoveTo(0.86 * WIDTH, 0.49230769230769234 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.795 * WIDTH, 0.7153846153846154 * HEIGHT,
                                                                 0.66 * WIDTH, 0.8461538461538461 * HEIGHT,
                                                                 0.5 * WIDTH, 0.8461538461538461 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.5 * WIDTH, 0.8461538461538461 * HEIGHT,
                                                                 0.5 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                                 0.5 * WIDTH, 0.8846153846153846 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.665 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                                 0.81 * WIDTH, 0.7384615384615385 * HEIGHT,
                                                                 0.88 * WIDTH, 0.5076923076923077 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.88 * WIDTH, 0.5076923076923077 * HEIGHT,
                                                                 0.86 * WIDTH, 0.49230769230769234 * HEIGHT,
                                                                 0.86 * WIDTH, 0.49230769230769234 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new ClosePath());
        final Paint HIGHLIGHT_LOWER_RIGHT_FILL = new RadialGradient(0, 0,
                                                                    0.73 * WIDTH, 0.7615384615384615 * HEIGHT,
                                                                    0.2375 * WIDTH,
                                                                    false, CycleMethod.NO_CYCLE,
                                                                    new Stop(0.0, Color.color(1, 1, 1, 0.5490196078)),
                                                                    new Stop(1.0, Color.color(1, 1, 1, 0)));
        HIGHLIGHT_LOWER_RIGHT.setFill(HIGHLIGHT_LOWER_RIGHT_FILL);
        HIGHLIGHT_LOWER_RIGHT.setStroke(null);

        glowOff.getChildren().addAll(GLOW_RING, HIGHLIGHT_LOWER_RIGHT);
    }

    public void drawGlowOn() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        glowOn.getChildren().clear();

        final Path GLOW_RING = new Path();
        GLOW_RING.setFillRule(FillRule.EVEN_ODD);
        GLOW_RING.getElements().add(new MoveTo(0.1 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.1 * WIDTH, 0.5846153846153846 * HEIGHT,
                                                     0.275 * WIDTH, 0.8461538461538461 * HEIGHT,
                                                     0.495 * WIDTH, 0.8461538461538461 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.715 * WIDTH, 0.8461538461538461 * HEIGHT,
                                                     0.89 * WIDTH, 0.5846153846153846 * HEIGHT,
                                                     0.89 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.89 * WIDTH, 0.2153846153846154 * HEIGHT,
                                                     0.89 * WIDTH, 0.15384615384615385 * HEIGHT,
                                                     0.89 * WIDTH, 0.15384615384615385 * HEIGHT));
        GLOW_RING.getElements().add(new LineTo(0.1 * WIDTH, 0.15384615384615385 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.1 * WIDTH, 0.15384615384615385 * HEIGHT,
                                                     0.1 * WIDTH, 0.2153846153846154 * HEIGHT,
                                                     0.1 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new ClosePath());
        GLOW_RING.getElements().add(new MoveTo(0.075 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.075 * WIDTH, 0.2153846153846154 * HEIGHT,
                                                     0.075 * WIDTH, 0.15384615384615385 * HEIGHT,
                                                     0.075 * WIDTH, 0.15384615384615385 * HEIGHT));
        GLOW_RING.getElements().add(new LineTo(0.075 * WIDTH, 0.11538461538461539 * HEIGHT));
        GLOW_RING.getElements().add(new LineTo(0.915 * WIDTH, 0.11538461538461539 * HEIGHT));
        GLOW_RING.getElements().add(new LineTo(0.915 * WIDTH, 0.15384615384615385 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.915 * WIDTH, 0.15384615384615385 * HEIGHT,
                                                     0.915 * WIDTH, 0.2153846153846154 * HEIGHT,
                                                     0.915 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.915 * WIDTH, 0.5923076923076923 * HEIGHT,
                                                     0.725 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                     0.495 * WIDTH, 0.8846153846153846 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.265 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                     0.075 * WIDTH, 0.5923076923076923 * HEIGHT,
                                                     0.075 * WIDTH, 0.23846153846153847 * HEIGHT));
        GLOW_RING.getElements().add(new ClosePath());

        final Paint GLOW_ON_PAINT = new RadialGradient(0, 0,
                                                       0.5 * WIDTH, 0.5 * HEIGHT,
                                                       0.4158878504672897 * WIDTH,
                                                       false,
                                                       CycleMethod.NO_CYCLE,
                                                       new Stop(0.0, glowColors.get(0)),
                                                       new Stop(0.91, glowColors.get(1)),
                                                       new Stop(0.96, glowColors.get(2)),
                                                       new Stop(1.0, glowColors.get(3)));

        GLOW_RING.setFill(GLOW_ON_PAINT);
        GLOW_RING.setStroke(null);

        final DropShadow GLOW_EFFECT = new DropShadow();
        GLOW_EFFECT.setRadius(0.15 * WIDTH);
        GLOW_EFFECT.setBlurType(BlurType.GAUSSIAN);
        if (GLOW_EFFECT.colorProperty().isBound()) {
            GLOW_EFFECT.colorProperty().unbind();
        }
        GLOW_EFFECT.colorProperty().bind(control.glowColorProperty());
        GLOW_RING.setEffect(GLOW_EFFECT);

        final Path HIGHLIGHT_LOWER_RIGHT = new Path();
        HIGHLIGHT_LOWER_RIGHT.setFillRule(FillRule.EVEN_ODD);
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new MoveTo(0.86 * WIDTH, 0.49230769230769234 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.795 * WIDTH, 0.7153846153846154 * HEIGHT,
                                                                 0.66 * WIDTH, 0.8461538461538461 * HEIGHT,
                                                                 0.5 * WIDTH, 0.8461538461538461 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.5 * WIDTH, 0.8461538461538461 * HEIGHT,
                                                                 0.5 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                                 0.5 * WIDTH, 0.8846153846153846 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.665 * WIDTH, 0.8846153846153846 * HEIGHT,
                                                                 0.81 * WIDTH, 0.7384615384615385 * HEIGHT,
                                                                 0.88 * WIDTH, 0.5076923076923077 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.88 * WIDTH, 0.5076923076923077 * HEIGHT,
                                                                 0.86 * WIDTH, 0.49230769230769234 * HEIGHT,
                                                                 0.86 * WIDTH, 0.49230769230769234 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new ClosePath());
        final Paint HIGHLIGHT_LOWER_RIGHT_FILL = new RadialGradient(0, 0,
                                                                    0.73 * WIDTH, 0.7615384615384615 * HEIGHT,
                                                                    0.2375 * WIDTH,
                                                                    false, CycleMethod.NO_CYCLE,
                                                                    new Stop(0.0, Color.color(1, 1, 1, 0.5490196078)),
                                                                    new Stop(1.0, Color.color(1, 1, 1, 0)));
        HIGHLIGHT_LOWER_RIGHT.setFill(HIGHLIGHT_LOWER_RIGHT_FILL);
        HIGHLIGHT_LOWER_RIGHT.setStroke(null);
        glowOn.getChildren().addAll(GLOW_RING, HIGHLIGHT_LOWER_RIGHT);
    }

    public void drawIndicators() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        markers.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        markers.getChildren().add(IBOUNDS);

        for (final Marker marker : control.getMarkers()) {
            if (Double.compare(marker.getValue(), control.getMinValue()) >= 0 && Double.compare(marker.getValue(), control.getMaxValue()) <= 0) {
                final Group ARROW_GROUP = createIndicator(WIDTH, marker, new Point2D(WIDTH * 0.4813084112, WIDTH * 0.0841121495));
                ARROW_GROUP.getTransforms().clear();
                ARROW_GROUP.setTranslateY(-WIDTH * 0.35);
                ARROW_GROUP.getTransforms().add(Transform.rotate(-control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getX()));
                final double ZERO_OFFSET = -90 + control.getRadialRange().ROTATION_OFFSET;
                ARROW_GROUP.getTransforms().add(Transform.rotate(ZERO_OFFSET - (marker.getValue() - control.getMinValue()) * control.getAngleStep(), center.getX(), center.getX()));
                markers.getChildren().add(ARROW_GROUP);
            }
        }
    }

    public void drawThreshold() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        threshold.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        threshold.getChildren().add(IBOUNDS);

        final Path THRESHOLD = createTriangleShape(0.03 * WIDTH, 0.03 * WIDTH, false);
        THRESHOLD.setStrokeType(StrokeType.CENTERED);
        THRESHOLD.setStrokeLineCap(StrokeLineCap.ROUND);
        THRESHOLD.setStrokeLineJoin(StrokeLineJoin.ROUND);
        THRESHOLD.setStrokeWidth(0.002 * HEIGHT);

        THRESHOLD.getStyleClass().add("root");
        THRESHOLD.setStyle(control.getThresholdColor().CSS);
        THRESHOLD.setId("threshold-gradient");

        THRESHOLD.setTranslateX(0.485 * WIDTH);
        THRESHOLD.setTranslateY(0.14 * WIDTH);

        threshold.getChildren().addAll(THRESHOLD);

        threshold.getTransforms().clear();
        threshold.setTranslateY(-WIDTH * 0.35);
        threshold.getTransforms().add(Transform.rotate(-control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getX()));
        final double ZERO_OFFSET = -90 + control.getRadialRange().ROTATION_OFFSET;
        threshold.getTransforms().add(Transform.rotate(ZERO_OFFSET - (control.getThreshold() - control.getMinValue()) * control.getAngleStep(), center.getX(), center.getX()));
    }

    public void drawMinMeasuredIndicator() {
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getWidth();

        minMeasured.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        minMeasured.getChildren().add(IBOUNDS);

        final Path MIN_MEASURED = createTriangleShape(0.03 * WIDTH, 0.035 * WIDTH, true);
        MIN_MEASURED.setFill(Color.color(0.0, 0.0, 0.8));
        MIN_MEASURED.setStroke(null);

        MIN_MEASURED.setTranslateX(0.485 * WIDTH);
        MIN_MEASURED.setTranslateY(0.1 * WIDTH);

        minMeasured.getChildren().add(MIN_MEASURED);

        minMeasured.getTransforms().clear();
        minMeasured.setTranslateY(-WIDTH * 0.35);
        minMeasured.getTransforms().add(Transform.rotate(-control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getX()));
        final double ZERO_OFFSET = -90 + control.getRadialRange().ROTATION_OFFSET;
        minMeasured.setRotate(ZERO_OFFSET - (control.getMinMeasuredValue() - control.getMinValue()) * control.getAngleStep());
    }

    public void drawMaxMeasuredIndicator() {
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getWidth();

        maxMeasured.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        maxMeasured.getChildren().add(IBOUNDS);

        final Path MAX_MEASURED = createTriangleShape(0.03 * WIDTH, 0.035 * WIDTH, true);
        MAX_MEASURED.setFill(Color.color(0.8, 0.0, 0.0));
        MAX_MEASURED.setStroke(null);

        MAX_MEASURED.setTranslateX(0.485 * WIDTH);
        MAX_MEASURED.setTranslateY(0.1 * WIDTH);

        maxMeasured.getChildren().add(MAX_MEASURED);

        maxMeasured.getTransforms().clear();
        maxMeasured.setTranslateY(-WIDTH * 0.35);
        maxMeasured.getTransforms().add(Transform.rotate(-control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getX()));
        final double ZERO_OFFSET = -90 + control.getRadialRange().ROTATION_OFFSET;
        maxMeasured.setRotate(ZERO_OFFSET - (control.getMaxMeasuredValue() - control.getMinValue()) * control.getAngleStep());
    }

    public void drawPointer() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getWidth();

        pointer.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        pointer.getChildren().add(IBOUNDS);

        final Path POINTER = new Path();
        final Path POINTER_FRONT = new Path();
        POINTER.setSmooth(true);
        POINTER.getStyleClass().add("root");
        POINTER.setStyle("-fx-value: " + control.getValueColor().CSS);
        switch (control.getPointerType()) {
            case TYPE2:
                POINTER.setId("pointer2-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.514018691588785 * WIDTH, 0.4719626168224299 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5046728971962616 * WIDTH, 0.46261682242990654 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5046728971962616 * WIDTH, 0.3411214953271028 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5046728971962616 * WIDTH, 0.2336448598130841 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.5046728971962616 * WIDTH, 0.2336448598130841 * HEIGHT,
                    0.5046728971962616 * WIDTH, 0.1308411214953271 * HEIGHT,
                    0.4953271028037383 * WIDTH, 0.1308411214953271 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.49065420560747663 * WIDTH, 0.1308411214953271 * HEIGHT,
                    0.49065420560747663 * WIDTH, 0.2336448598130841 * HEIGHT,
                    0.49065420560747663 * WIDTH, 0.2336448598130841 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49065420560747663 * WIDTH, 0.3411214953271028 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49065420560747663 * WIDTH, 0.46261682242990654 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48130841121495327 * WIDTH, 0.4719626168224299 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.48130841121495327 * WIDTH, 0.4719626168224299 * HEIGHT,
                    0.4672897196261682 * WIDTH, 0.49065420560747663 * HEIGHT,
                    0.4672897196261682 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.4672897196261682 * WIDTH, 0.5186915887850467 * HEIGHT,
                    0.48130841121495327 * WIDTH, 0.5327102803738317 * HEIGHT,
                    0.4953271028037383 * WIDTH, 0.5327102803738317 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.514018691588785 * WIDTH, 0.5327102803738317 * HEIGHT,
                    0.5327102803738317 * WIDTH, 0.5186915887850467 * HEIGHT,
                    0.5327102803738317 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.5327102803738317 * WIDTH, 0.49065420560747663 * HEIGHT,
                    0.514018691588785 * WIDTH, 0.4719626168224299 * HEIGHT,
                    0.514018691588785 * WIDTH, 0.4719626168224299 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE3:
                POINTER.setId("pointer3-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.4953271028037383 * WIDTH, 0.1308411214953271 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5046728971962616 * WIDTH, 0.1308411214953271 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5046728971962616 * WIDTH, 0.5046728971962616 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.4953271028037383 * WIDTH, 0.5046728971962616 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.4953271028037383 * WIDTH, 0.1308411214953271 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE4:
                POINTER.setId("pointer4-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.5 * WIDTH, 0.1261682242990654 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5093457943925234 * WIDTH, 0.13551401869158877 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5327102803738317 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5233644859813084 * WIDTH, 0.602803738317757 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.4766355140186916 * WIDTH, 0.602803738317757 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.46261682242990654 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48598130841121495 * WIDTH, 0.13551401869158877 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.1261682242990654 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE5:
                POINTER.setId("pointer5-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(WIDTH * 0.5, HEIGHT * 0.4953271028037383));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5280373831775701, HEIGHT * 0.4953271028037383));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5, HEIGHT * 0.14953271028037382));
                POINTER.getElements().add(new LineTo(WIDTH * 0.4719626168224299, HEIGHT * 0.4953271028037383));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5, HEIGHT * 0.4953271028037383));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;

            case TYPE6:
                POINTER.setId("pointer6-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(WIDTH * 0.48130841121495327, HEIGHT * 0.48598130841121495));
                POINTER.getElements().add(new LineTo(WIDTH * 0.48130841121495327, HEIGHT * 0.3925233644859813));
                POINTER.getElements().add(new LineTo(WIDTH * 0.48598130841121495, HEIGHT * 0.3177570093457944));
                POINTER.getElements().add(new LineTo(WIDTH * 0.4953271028037383, HEIGHT * 0.1308411214953271));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5046728971962616, HEIGHT * 0.1308411214953271));
                POINTER.getElements().add(new LineTo(WIDTH * 0.514018691588785, HEIGHT * 0.3177570093457944));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5186915887850467, HEIGHT * 0.3878504672897196));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5186915887850467, HEIGHT * 0.48598130841121495));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5046728971962616, HEIGHT * 0.48598130841121495));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5046728971962616, HEIGHT * 0.3878504672897196));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5, HEIGHT * 0.3177570093457944));
                POINTER.getElements().add(new LineTo(WIDTH * 0.4953271028037383, HEIGHT * 0.3925233644859813));
                POINTER.getElements().add(new LineTo(WIDTH * 0.4953271028037383, HEIGHT * 0.48598130841121495));
                POINTER.getElements().add(new LineTo(WIDTH * 0.48130841121495327, HEIGHT * 0.48598130841121495));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE7:
                POINTER.setId("pointer7-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.49065420560747663 * WIDTH, 0.1308411214953271 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.4766355140186916 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5186915887850467 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5046728971962616 * WIDTH, 0.1308411214953271 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49065420560747663 * WIDTH, 0.1308411214953271 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE8:
                POINTER.setId("pointer8-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.4953271028037383 * WIDTH, 0.5327102803738317 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5327102803738317 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.5327102803738317 * WIDTH, 0.5 * HEIGHT,
                    0.5046728971962616 * WIDTH, 0.45794392523364486 * HEIGHT,
                    0.4953271028037383 * WIDTH, 0.14953271028037382 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.49065420560747663 * WIDTH, 0.45794392523364486 * HEIGHT,
                    0.46261682242990654 * WIDTH, 0.5 * HEIGHT,
                    0.46261682242990654 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.4953271028037383 * WIDTH, 0.5327102803738317 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;

            case TYPE9:
                POINTER.setId("pointer9-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(WIDTH * 0.4953271028037383, HEIGHT * 0.2336448598130841));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5046728971962616, HEIGHT * 0.2336448598130841));
                POINTER.getElements().add(new LineTo(WIDTH * 0.514018691588785, HEIGHT * 0.4392523364485981));
                POINTER.getElements().add(new LineTo(WIDTH * 0.48598130841121495, HEIGHT * 0.4392523364485981));
                POINTER.getElements().add(new LineTo(WIDTH * 0.4953271028037383, HEIGHT * 0.2336448598130841));
                POINTER.getElements().add(new ClosePath());
                POINTER.getElements().add(new MoveTo(WIDTH * 0.49065420560747663, HEIGHT * 0.1308411214953271));
                POINTER.getElements().add(new LineTo(WIDTH * 0.4719626168224299, HEIGHT * 0.4719626168224299));
                POINTER.getElements().add(new LineTo(WIDTH * 0.4719626168224299, HEIGHT * 0.5280373831775701));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.4719626168224299, HEIGHT * 0.5280373831775701, WIDTH * 0.4766355140186916, HEIGHT * 0.602803738317757, WIDTH * 0.4766355140186916, HEIGHT * 0.602803738317757));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.4766355140186916, HEIGHT * 0.6074766355140186, WIDTH * 0.48130841121495327, HEIGHT * 0.6074766355140186, WIDTH * 0.5, HEIGHT * 0.6074766355140186));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.5186915887850467, HEIGHT * 0.6074766355140186, WIDTH * 0.5233644859813084, HEIGHT * 0.6074766355140186, WIDTH * 0.5233644859813084, HEIGHT * 0.602803738317757));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.5233644859813084, HEIGHT * 0.602803738317757, WIDTH * 0.5280373831775701, HEIGHT * 0.5280373831775701, WIDTH * 0.5280373831775701, HEIGHT * 0.5280373831775701));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5280373831775701, HEIGHT * 0.4719626168224299));
                POINTER.getElements().add(new LineTo(WIDTH * 0.5093457943925234, HEIGHT * 0.1308411214953271));
                POINTER.getElements().add(new LineTo(WIDTH * 0.49065420560747663, HEIGHT * 0.1308411214953271));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);

                POINTER_FRONT.getStyleClass().add("root");
                POINTER_FRONT.setStyle("-fx-value: " + control.getValueColor().CSS);
                POINTER_FRONT.setId("pointer9-box");
                POINTER_FRONT.setFillRule(FillRule.EVEN_ODD);
                POINTER_FRONT.getElements().add(new MoveTo(WIDTH * 0.4953271028037383, HEIGHT * 0.21962616822429906));
                POINTER_FRONT.getElements().add(new LineTo(WIDTH * 0.5046728971962616, HEIGHT * 0.21962616822429906));
                POINTER_FRONT.getElements().add(new LineTo(WIDTH * 0.5046728971962616, HEIGHT * 0.13551401869158877));
                POINTER_FRONT.getElements().add(new LineTo(WIDTH * 0.4953271028037383, HEIGHT * 0.13551401869158877));
                POINTER_FRONT.getElements().add(new LineTo(WIDTH * 0.4953271028037383, HEIGHT * 0.21962616822429906));
                POINTER_FRONT.getElements().add(new ClosePath());
                POINTER_FRONT.setStroke(null);
                break;

            case TYPE10:
                POINTER.setId("pointer10-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.4953271028037383 * WIDTH, 0.14953271028037382 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.4953271028037383 * WIDTH, 0.14953271028037382 * HEIGHT,
                    0.4439252336448598 * WIDTH, 0.49065420560747663 * HEIGHT,
                    0.4439252336448598 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.4439252336448598 * WIDTH, 0.5327102803738317 * HEIGHT,
                    0.4672897196261682 * WIDTH, 0.5560747663551402 * HEIGHT,
                    0.4953271028037383 * WIDTH, 0.5560747663551402 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.5280373831775701 * WIDTH, 0.5560747663551402 * HEIGHT,
                    0.5560747663551402 * WIDTH, 0.5327102803738317 * HEIGHT,
                    0.5560747663551402 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.5560747663551402 * WIDTH, 0.49065420560747663 * HEIGHT,
                    0.4953271028037383 * WIDTH, 0.14953271028037382 * HEIGHT,
                    0.4953271028037383 * WIDTH, 0.14953271028037382 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;

            case TYPE11:
                POINTER.setId("pointer11-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.5 * WIDTH, 0.16822429906542055 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48598130841121495 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.48598130841121495 * WIDTH, 0.5 * HEIGHT,
                    0.48130841121495327 * WIDTH, 0.5841121495327103 * HEIGHT,
                    0.5 * WIDTH, 0.5841121495327103 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.514018691588785 * WIDTH, 0.5841121495327103 * HEIGHT,
                    0.5093457943925234 * WIDTH, 0.5 * HEIGHT,
                    0.5093457943925234 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.16822429906542055 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;

            case TYPE12:
                POINTER.setId("pointer12-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.5 * WIDTH, 0.16822429906542055 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48598130841121495 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.5046728971962616 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5093457943925234 * WIDTH, 0.5 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.16822429906542055 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;

            case TYPE13:
                POINTER.setId("pointer13-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.48598130841121495 * WIDTH, 0.16822429906542055 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.1308411214953271 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5093457943925234 * WIDTH, 0.16822429906542055 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5093457943925234 * WIDTH, 0.5093457943925234 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48598130841121495 * WIDTH, 0.5093457943925234 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48598130841121495 * WIDTH, 0.16822429906542055 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE14:
                POINTER.setId("pointer14-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.48598130841121495 * WIDTH, 0.16822429906542055 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.1308411214953271 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5093457943925234 * WIDTH, 0.16822429906542055 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5093457943925234 * WIDTH, 0.5093457943925234 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48598130841121495 * WIDTH, 0.5093457943925234 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48598130841121495 * WIDTH, 0.16822429906542055 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE15:
                POINTER.setId("pointer15-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.48 * WIDTH, 0.505 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48 * WIDTH, 0.275 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.46 * WIDTH, 0.275 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.495 * WIDTH, 0.15 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.53 * WIDTH, 0.275 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.515 * WIDTH, 0.275 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.515 * WIDTH, 0.505 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48 * WIDTH, 0.505 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE16:
                POINTER.setId("pointer16-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.495 * WIDTH, 0.625 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.515 * WIDTH, 0.625 * HEIGHT,
                                                           0.535 * WIDTH, 0.61 * HEIGHT,
                                                           0.535 * WIDTH, 0.61 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.525 * WIDTH, 0.6 * HEIGHT,
                                                           0.505 * WIDTH, 0.53 * HEIGHT,
                                                           0.505 * WIDTH, 0.53 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.505 * WIDTH, 0.47 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.505 * WIDTH, 0.17 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.165 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.13 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.495 * WIDTH, 0.13 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.495 * WIDTH, 0.165 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49 * WIDTH, 0.17 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49 * WIDTH, 0.47 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49 * WIDTH, 0.53 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.49 * WIDTH, 0.53 * HEIGHT,
                                                           0.47 * WIDTH, 0.6 * HEIGHT,
                                                           0.465 * WIDTH, 0.61 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.465 * WIDTH, 0.61 * HEIGHT,
                                                           0.475 * WIDTH, 0.625 * HEIGHT,
                                                           0.495 * WIDTH, 0.625 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE1:

            default:
                POINTER.setStyle("-fx-pointer: " + control.getValueColor().CSS);
                POINTER.setId("pointer1-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(WIDTH * 0.5186915887850467, HEIGHT * 0.4719626168224299));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.514018691588785, HEIGHT * 0.45794392523364486,
                                                           WIDTH * 0.5093457943925234, HEIGHT * 0.4158878504672897,
                                                           WIDTH * 0.5093457943925234, HEIGHT * 0.40186915887850466));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.5046728971962616, HEIGHT * 0.38317757009345793,
                                                           WIDTH * 0.5, HEIGHT * 0.1308411214953271,
                                                           WIDTH * 0.5, HEIGHT * 0.1308411214953271));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.5, HEIGHT * 0.1308411214953271,
                                                           WIDTH * 0.49065420560747663, HEIGHT * 0.38317757009345793,
                                                           WIDTH * 0.49065420560747663, HEIGHT * 0.397196261682243));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.49065420560747663, HEIGHT * 0.4158878504672897,
                                                           WIDTH * 0.48598130841121495, HEIGHT * 0.45794392523364486,
                                                           WIDTH * 0.48130841121495327, HEIGHT * 0.4719626168224299));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.4719626168224299, HEIGHT * 0.48130841121495327,
                                                           WIDTH * 0.4672897196261682, HEIGHT * 0.49065420560747663,
                                                           WIDTH * 0.4672897196261682, HEIGHT * 0.5));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.4672897196261682, HEIGHT * 0.5186915887850467,
                                                           WIDTH * 0.48130841121495327, HEIGHT * 0.5327102803738317,
                                                           WIDTH * 0.5, HEIGHT * 0.5327102803738317));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.5186915887850467, HEIGHT * 0.5327102803738317,
                                                           WIDTH * 0.5327102803738317, HEIGHT * 0.5186915887850467,
                                                           WIDTH * 0.5327102803738317, HEIGHT * 0.5));
                POINTER.getElements().add(new CubicCurveTo(WIDTH * 0.5327102803738317, HEIGHT * 0.49065420560747663,
                                                           WIDTH * 0.5280373831775701, HEIGHT * 0.48130841121495327,
                                                           WIDTH * 0.5186915887850467, HEIGHT * 0.4719626168224299));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;
        }

        final DropShadow SHADOW;
        if (control.isPointerShadowEnabled()) {
            SHADOW = new DropShadow();
            SHADOW.setHeight(0.03 * WIDTH);
            SHADOW.setWidth(0.03 * HEIGHT);
            SHADOW.setColor(Color.color(0, 0, 0, 0.75));

        } else {
            SHADOW = null;
        }

        // Pointer glow
        if (control.isPointerGlowEnabled()) {
            final DropShadow GLOW = new DropShadow();
            GLOW.setWidth(0.04 * SIZE);
            GLOW.setHeight(0.04 * SIZE);
            GLOW.setOffsetX(0.0);
            GLOW.setOffsetY(0.0);
            GLOW.setRadius(0.04 * SIZE);
            GLOW.setColor(control.getValueColor().COLOR);
            GLOW.setBlurType(BlurType.GAUSSIAN);
            if (control.getPointerType() == Gauge.PointerType.TYPE9) {
                POINTER.setEffect(SHADOW);
                POINTER_FRONT.setEffect(GLOW);
            } else {
                if (control.isPointerShadowEnabled()) {
                    GLOW.inputProperty().set(SHADOW);
                }
                POINTER.setEffect(GLOW);
            }
        } else {
            POINTER.setEffect(SHADOW);
        }

        pointer.getChildren().addAll(POINTER);
        if (control.getPointerType() == Gauge.PointerType.TYPE9) {
            pointer.getChildren().add(POINTER_FRONT);
        }

        pointer.getTransforms().clear();
        pointer.setTranslateY(-WIDTH * 0.35);
        pointer.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getX()));
    }

    public void drawForeground() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        foreground.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        foreground.getChildren().add(IBOUNDS);

        final Path FOREGROUND = new Path();
        switch (control.getForegroundType()) {
            case TYPE2:
                FOREGROUND.setFillRule(FillRule.EVEN_ODD);
                FOREGROUND.getElements().add(new MoveTo(0.495 * WIDTH, 0.36923076923076925 * HEIGHT));
                FOREGROUND.getElements().add(new CubicCurveTo(0.65 * WIDTH, 0.49230769230769234 * HEIGHT,
                                                              0.73 * WIDTH, 0.4846153846153846 * HEIGHT,
                                                              0.87 * WIDTH, 0.4846153846153846 * HEIGHT));
                FOREGROUND.getElements().add(new CubicCurveTo(0.765 * WIDTH, 0.8 * HEIGHT,
                                                              0.52 * WIDTH, 0.9461538461538461 * HEIGHT,
                                                              0.31 * WIDTH, 0.7769230769230769 * HEIGHT));
                FOREGROUND.getElements().add(new CubicCurveTo(0.155 * WIDTH, 0.6615384615384615 * HEIGHT,
                                                              0.075 * WIDTH, 0.4307692307692308 * HEIGHT,
                                                              0.08 * WIDTH, 0.19230769230769232 * HEIGHT));
                FOREGROUND.getElements().add(new CubicCurveTo(0.085 * WIDTH, 0.1 * HEIGHT,
                                                              0.35 * WIDTH, 0.25384615384615383 * HEIGHT,
                                                              0.495 * WIDTH, 0.36923076923076925 * HEIGHT));
                FOREGROUND.getElements().add(new ClosePath());
                FOREGROUND.setFill(new LinearGradient(0.495 * WIDTH, 0.23076923076923078 * HEIGHT,
                                                      0.3072113748856438 * WIDTH, 0.7741215956054937 * HEIGHT,
                                                      false, CycleMethod.NO_CYCLE,
                                                      new Stop(0.0, Color.color(1, 1, 1, 0)),
                                                      new Stop(1.0, Color.color(1, 1, 1, 0.0980392157))));
                FOREGROUND.setStroke(null);
                foreground.getChildren().addAll(FOREGROUND);
                break;
            case TYPE3:
                FOREGROUND.setFillRule(FillRule.EVEN_ODD);
                FOREGROUND.getElements().add(new MoveTo(0.08 * WIDTH, 0.26153846153846155 * HEIGHT));
                FOREGROUND.getElements().add(new CubicCurveTo(0.09 * WIDTH, 0.6 * HEIGHT,
                                                              0.27 * WIDTH, 0.8692307692307693 * HEIGHT,
                                                              0.495 * WIDTH, 0.8692307692307693 * HEIGHT));
                FOREGROUND.getElements().add(new CubicCurveTo(0.72 * WIDTH, 0.8692307692307693 * HEIGHT,
                                                              0.9 * WIDTH, 0.6 * HEIGHT,
                                                              0.91 * WIDTH, 0.26153846153846155 * HEIGHT));
                FOREGROUND.getElements().add(new CubicCurveTo(0.865 * WIDTH, 0.5538461538461539 * HEIGHT,
                                                              0.695 * WIDTH, 0.7692307692307693 * HEIGHT,
                                                              0.495 * WIDTH, 0.7692307692307693 * HEIGHT));
                FOREGROUND.getElements().add(new CubicCurveTo(0.295 * WIDTH, 0.7692307692307693 * HEIGHT,
                                                              0.125 * WIDTH, 0.5538461538461539 * HEIGHT,
                                                              0.08 * WIDTH, 0.26153846153846155 * HEIGHT));
                FOREGROUND.getElements().add(new ClosePath());
                FOREGROUND.setFill(new RadialGradient(0, 0,
                                                      0.495 * WIDTH, 0.23076923076923078 * HEIGHT,
                                                      0.415 * WIDTH,
                                                      false, CycleMethod.NO_CYCLE,
                                                      new Stop(0.0, Color.color(1, 1, 1, 0)),
                                                      new Stop(0.9, Color.color(1, 1, 1, 0.2274509804)),
                                                      new Stop(0.96, Color.color(1, 1, 1, 0)),
                                                      new Stop(1.0, Color.color(1, 1, 1, 0))));
                FOREGROUND.setStroke(null);
                foreground.getChildren().addAll(FOREGROUND);
                break;
            case TYPE4:

            case TYPE5:

            case TYPE1:
            default:
                FOREGROUND.setFillRule(FillRule.EVEN_ODD);
                FOREGROUND.getElements().add(new MoveTo(0.075 * WIDTH, 0.11538461538461539 * HEIGHT));
                FOREGROUND.getElements().add(new CubicCurveTo(0.145 * WIDTH, 0.11538461538461539 * HEIGHT,
                                                              0.855 * WIDTH, 0.11538461538461539 * HEIGHT,
                                                              0.915 * WIDTH, 0.11538461538461539 * HEIGHT));
                FOREGROUND.getElements().add(new CubicCurveTo(0.915 * WIDTH, 0.15384615384615385 * HEIGHT,
                                                              0.735 * WIDTH, 0.2076923076923077 * HEIGHT,
                                                              0.495 * WIDTH, 0.2076923076923077 * HEIGHT));
                FOREGROUND.getElements().add(new CubicCurveTo(0.255 * WIDTH, 0.2076923076923077 * HEIGHT,
                                                              0.075 * WIDTH, 0.15384615384615385 * HEIGHT,
                                                              0.075 * WIDTH, 0.11538461538461539 * HEIGHT));
                FOREGROUND.getElements().add(new ClosePath());
                FOREGROUND.setFill(new LinearGradient(0.5 * WIDTH, 0.11538461538461539 * HEIGHT,
                                                      0.5 * WIDTH, 0.2076923076923077 * HEIGHT,
                                                      false, CycleMethod.NO_CYCLE,
                                                      new Stop(0.0, Color.color(1, 1, 1, 0.2470588235)),
                                                      new Stop(1.0, Color.color(1, 1, 1, 0))));
                FOREGROUND.setStroke(null);
                foreground.getChildren().addAll(FOREGROUND);
                break;
        }
    }
}
