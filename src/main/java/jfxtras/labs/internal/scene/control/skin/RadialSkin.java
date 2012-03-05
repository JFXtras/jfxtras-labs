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

import jfxtras.labs.internal.scene.control.behavior.RadialBehavior;
import jfxtras.labs.scene.control.gauge.Gauge.PointerType;
import jfxtras.labs.scene.control.gauge.GaugeModelEvent;
import jfxtras.labs.scene.control.gauge.Radial;
import jfxtras.labs.scene.control.gauge.Section;
import jfxtras.labs.scene.control.gauge.StyleModelEvent;
import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
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
import javafx.scene.paint.Color;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.util.Duration;

import java.util.ArrayList;


/**
 * Created by
 * User: hansolo
 * Date: 02.01.12
 * Time: 17:14
 */
public class RadialSkin extends GaugeSkinBase<Radial, RadialBehavior> {
    private static final Rectangle PREF_SIZE = new Rectangle(200, 200);
    private Radial           control;
    private Rectangle        gaugeBounds;
    private Point2D          framelessOffset;
    private Group            frame;
    private Group            background;
    private Group            trend;
    private Group            sections;
    private Group            areas;
    private Group            indicators;
    private Group            titleAndUnit;
    private Group            tickmarks;
    private Group            glowOff;
    private Group            glowOn;
    private ArrayList<Color> glowColors;
    private Group            lcd;
    private Group            lcdContent;
    private Text             lcdValueString;
    private Text             lcdUnitString;
    private Group            lcdThresholdIndicator;
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
    private Timeline         rotationAngleTimeline;
    private DoubleProperty   gaugeValue;
    private double           negativeOffset;
    private Point2D          center;
    private int              noOfLeds;
    private ArrayList<Shape> ledsOff;
    private ArrayList<Shape> ledsOn;
    private DoubleProperty   currentValue;
    private DoubleProperty   formerValue;
    private DoubleProperty   lcdValue;
    private DoubleProperty   currentLcdValue;
    private FadeTransition   glowPulse;
    private Rotate           pointerRotation;
    private AnimationTimer   ledTimer;
    private boolean          ledOnVisible;
    private long             lastLedTimerCall;
    private AnimationTimer   userLedTimer;
    private boolean          userLedOnVisible;
    private long             lastUserLedTimerCall;
    private boolean          isDirty;
    private boolean          initialized;


    // ******************** Constructors **************************************
    public RadialSkin(final Radial CONTROL) {
        super(CONTROL, new RadialBehavior(CONTROL));
        control                = CONTROL;
        gaugeBounds            = new Rectangle(200, 200);
        framelessOffset        = new Point2D(0, 0);
        center                 = new Point2D(0, 0);
        frame                  = new Group();
        background             = new Group();
        trend                  = new Group();
        sections               = new Group();
        areas                  = new Group();
        indicators             = new Group();
        titleAndUnit           = new Group();
        tickmarks              = new Group();
        glowOff                = new Group();
        glowOn                 = new Group();
        glowColors             = new ArrayList<Color>(4);
        lcd                    = new Group();
        lcdContent             = new Group();
        lcdValueString         = new Text();
        lcdUnitString          = new Text();
        lcdThresholdIndicator  = new Group();
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
        rotationAngleTimeline  = new Timeline();
        gaugeValue             = new SimpleDoubleProperty(0);
        negativeOffset         = 0;
        noOfLeds               = 60;
        ledsOff = new ArrayList<Shape>(noOfLeds);
        ledsOn                 = new ArrayList<Shape>(noOfLeds);
        currentValue           = new SimpleDoubleProperty(0);
        formerValue            = new SimpleDoubleProperty(0);
        lcdValue               = new SimpleDoubleProperty(0);
        currentLcdValue        = new SimpleDoubleProperty(0);
        glowPulse              = new FadeTransition(Duration.millis(800), glowOn);
        pointerRotation        = new Rotate();
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

        double value = Double.compare(control.getValue(), control.getMinValue()) < 0 ? control.getMinValue() : (Double.compare(control.getValue(), control.getMaxValue()) > 0 ? control.getMaxValue() : control.getValue());
        pointer.setRotate((value - control.getMinValue()) * control.getAngleStep());

        if (gaugeValue.get() < control.getMinValue()) {
            gaugeValue.set(control.getMinValue());
        } else if (gaugeValue.get() > control.getMaxValue()) {
            gaugeValue.set(control.getMaxValue());
        }

        control.recalcRange();
        control.setMinMeasuredValue(control.getMaxValue());
        control.setMaxMeasuredValue(control.getMinValue());

        if (control.getMinValue() < 0) {
            negativeOffset = control.getMinValue() * control.getAngleStep();
        } else {
            negativeOffset = 0;
        }

        addBindings();
        addListeners();

        initialized = true;
        paint();
    }

    private void addBindings() {
        if (!frame.visibleProperty().isBound()) {
            frame.visibleProperty().bind(control.frameVisibleProperty());
        }

        if (!background.visibleProperty().isBound()) {
            background.visibleProperty().bind(control.backgroundVisibleProperty());
        }

        if (!sections.visibleProperty().isBound()) {
            sections.visibleProperty().bind(control.sectionsVisibleProperty());
        }

        if (!areas.visibleProperty().isBound()) {
            areas.visibleProperty().bind(control.areasVisibleProperty());
        }

        if (!bargraphOff.visibleProperty().isBound()) {
            bargraphOff.visibleProperty().bind(control.bargraphProperty());
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
        }

        if (!indicators.visibleProperty().isBound()) {
            indicators.visibleProperty().bind(control.markersVisibleProperty());
        }

        if (!ledOff.visibleProperty().isBound()) {
            ledOff.visibleProperty().bind(control.ledVisibleProperty());
        }

        if (!ledOn.visibleProperty().isBound()) {
            ledOn.visibleProperty().bind(control.ledVisibleProperty());
        }

        if (!userLedOff.visibleProperty().isBound()) {
            userLedOff.visibleProperty().bind(control.userLedVisibleProperty());
        }

        if (!userLedOn.visibleProperty().isBound()) {
            userLedOn.visibleProperty().bind(control.userLedVisibleProperty());
        }

        if (!threshold.visibleProperty().isBound()) {
            threshold.visibleProperty().bind(control.thresholdVisibleProperty());
        }

        if (!minMeasured.visibleProperty().isBound()) {
            minMeasured.visibleProperty().bind(control.minMeasuredValueVisibleProperty());
        }

        if (!maxMeasured.visibleProperty().isBound()) {
            maxMeasured.visibleProperty().bind(control.maxMeasuredValueVisibleProperty());
        }

        if (!lcdValue.isBound()) {
            lcdValue.bind(control.valueProperty());
        }

        if (!lcd.visibleProperty().isBound()) {
            lcd.visibleProperty().bind(control.lcdVisibleProperty());
        }

        if (!lcdContent.visibleProperty().isBound()) {
            lcdContent.visibleProperty().bind(control.lcdVisibleProperty());
        }

        if (!lcdThresholdIndicator.visibleProperty().isBound()) {
            if (control.isLcdThresholdVisible() && control.isLcdValueCoupled()) {
                lcdThresholdIndicator.visibleProperty().bind(control.thresholdExceededProperty());
            }
        }

        if (!foreground.visibleProperty().isBound()) {
            foreground.visibleProperty().bind(control.foregroundVisibleProperty());
        }

        if (!trend.visibleProperty().isBound()) {
            trend.visibleProperty().bind(control.trendVisibleProperty());
        }
    }

    private void addListeners() {
        control.setOnModelEvent(new EventHandler<GaugeModelEvent>() {
            @Override public void handle(final GaugeModelEvent EVENT) {
                // Trigger repaint
                paint();
            }
        });

        control.setOnViewModelEvent(new EventHandler<StyleModelEvent>() {
            @Override public void handle(final StyleModelEvent EVENT) {
                // Trigger repaint
                isDirty = true;
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

        control.valueProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                formerValue.set(oldValue.doubleValue());
                if (rotationAngleTimeline.getStatus() != Animation.Status.STOPPED) {
                    rotationAngleTimeline.stop();
                }
                if (control.isValueAnimationEnabled()) {
                    final KeyValue kv = new KeyValue(gaugeValue, newValue, Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
                    final KeyFrame kf = new KeyFrame(Duration.millis(control.getAnimationDuration()), kv);
                    rotationAngleTimeline  = new Timeline();
                    rotationAngleTimeline.getKeyFrames().add(kf);
                    rotationAngleTimeline.play();
                } else {
                    pointerRotation.setPivotX(center.getX());
                    pointerRotation.setPivotY(center.getY());
                    pointerRotation.setAngle((newValue.doubleValue() - control.getMinValue()) * control.getAngleStep());
                    pointer.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET + (control.getMinValue() * control.getAngleStep()), center.getX(), center.getY()));
                    pointer.getTransforms().add(pointerRotation);
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

        gaugeValue.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                if (bargraphOff.isVisible()) {
                    int CURRENT_LED_INDEX = noOfLeds - 1 - (int) ((newValue.doubleValue() - control.getMinValue()) * control.getAngleStep() / 5.0);
                    int FORMER_LED_INDEX = noOfLeds - 1 - (int) ((oldValue.doubleValue() - control.getMinValue()) * control.getAngleStep() / 5.0);
                    final int THRESHOLD_LED_INDEX = noOfLeds - 1 - (int)((control.getThreshold() - control.getMinValue()) * control.getAngleStep() / 5.0);

                    if (Double.compare(control.getValue(), formerValue.doubleValue()) >= 0) {
                        for (int i = CURRENT_LED_INDEX ; i <= FORMER_LED_INDEX ; i++) {
                            ledsOn.get(i).setVisible(true);
                        }
                    } else {
                        for (int i = CURRENT_LED_INDEX ; i >= FORMER_LED_INDEX ; i--) {
                            ledsOn.get(i).setVisible(false);
                        }
                    }
                    if (control.isThresholdVisible()) {
                        ledsOn.get(THRESHOLD_LED_INDEX).setId("bargraph-threshold");
                        ledsOn.get(THRESHOLD_LED_INDEX).setVisible(true);
                    }
                } else {
                    pointer.getTransforms().clear();
                    pointerRotation.setPivotX(center.getX());
                    pointerRotation.setPivotY(center.getY());
                    pointerRotation.setAngle((newValue.doubleValue() - control.getMinValue()) * control.getAngleStep());
                    pointer.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET + negativeOffset, center.getX(), center.getY()));
                    pointer.getTransforms().add(pointerRotation);
                }

                currentValue.set(newValue.doubleValue());
                currentLcdValue.set(control.isLcdValueCoupled() ? currentValue.get() : control.getLcdValue());

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
                if (control.isLcdVisible()) {
                    drawLcdContent();
                }
            }
        });
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);

        if (PROPERTY == "ANIMATION_DURATION") {
            //pointerRotation.setDuration(Duration.millis(control.getAnimationDuration()));
        } else if (PROPERTY == "RADIAL_RANGE") {
            noOfLeds = (int) (control.getRadialRange().ANGLE_RANGE / 5.0);
            isDirty = true;
        } else if (PROPERTY == "FRAME_DESIGN") {
            drawCircularFrame(control, frame, gaugeBounds);
        } else if (PROPERTY == "BACKGROUND_DESIGN") {
            drawCircularBackground(control, background, gaugeBounds);
            drawCircularTickmarks(control, tickmarks, center, gaugeBounds);
        } else if (PROPERTY == "KNOB_DESIGN") {
            drawCircularKnobs(control, knobs, center, gaugeBounds);
        } else if (PROPERTY == "KNOB_COLOR") {
            drawCircularKnobs(control, knobs, center, gaugeBounds);
        } else if (PROPERTY == "POINTER_TYPE") {
            drawPointer();
        } else if (PROPERTY == "VALUE_COLOR") {
            drawPointer();
        } else if (PROPERTY == "FOREGROUND_TYPE") {
            drawCircularForeground(control, foreground, gaugeBounds);
        } else if (PROPERTY == "LCD_DESIGN") {
            drawCircularLcd(control, lcd, gaugeBounds);
            drawLcdContent();
        } else if (PROPERTY == "LCD_NUMBER_SYSTEM") {
            drawLcdContent();
        } else if (PROPERTY == "USER_LED_BLINKING") {
            if (userLedOff.isVisible() && userLedOn.isVisible()) {
                if (control.isUserLedBlinking()) {
                    userLedTimer.start();
                } else {
                    userLedTimer.stop();
                    userLedOn.setOpacity(0.0);
                }
            }
        } else if (PROPERTY == "LED_BLINKING") {
            if (ledOff.isVisible() && ledOn.isVisible()) {
                if (control.isLedBlinking()) {
                    ledTimer.start();
                } else {
                    ledTimer.stop();
                    ledOn.setOpacity(0.0);
                }
            }
        } else if (PROPERTY == "GLOW_COLOR") {
            glowColors.clear();
            final Color GLOW_COLOR = control.getGlowColor();
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.46, 0.96, 0.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 1.0, 1.0, 1.0));
            glowColors.add(Color.hsb(GLOW_COLOR.getHue(), 0.67, 0.90, 1.0));
            drawCircularGlowOn(control, glowOn, glowColors, gaugeBounds);
        } else if (PROPERTY == "GLOW_VISIBILITY") {
            glowOff.setVisible(control.isGlowVisible());
            if (!control.isGlowVisible()) {
                glowOn.setOpacity(0.0);
            }
        } else if (PROPERTY == "GLOW_ON") {
            if (glowOff.isVisible() && control.isGlowOn()) {
                glowOn.setOpacity(1.0);
                glowOff.setVisible(true);
            } else {
                glowOff.setVisible(true);
                glowOn.setOpacity(0.0);
            }
        } else if (PROPERTY == "PULSATING_GLOW") {
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
        } else if (PROPERTY == "RANGE") {
            if (control.getMinValue() < 0) {
                negativeOffset = control.getMinValue() * control.getAngleStep();
            } else {
                negativeOffset = 0;
            }
            drawCircularTickmarks(control, tickmarks, center, gaugeBounds);
        } else if (PROPERTY.equals("MIN_MEASURED_VALUE")) {
            minMeasured.getTransforms().clear();
            minMeasured.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getY()));
            minMeasured.getTransforms().add(Transform.rotate(-control.getMinValue() * control.getAngleStep(), center.getX(), center.getY()));
            minMeasured.getTransforms().add(Transform.rotate(control.getMinMeasuredValue() * control.getAngleStep(), center.getX(), center.getY()));
        } else if (PROPERTY == "MAX_MEASURED_VALUE") {
            maxMeasured.getTransforms().clear();
            maxMeasured.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getY()));
            maxMeasured.getTransforms().add(Transform.rotate(-control.getMinValue() * control.getAngleStep(), center.getX(), center.getY()));
            maxMeasured.getTransforms().add(Transform.rotate(control.getMaxMeasuredValue() * control.getAngleStep(), center.getX(), center.getY()));
        } else if (PROPERTY == "TREND") {
            drawCircularTrend(control, trend, gaugeBounds);
        } else if (PROPERTY == "SIMPLE_GRADIENT_BASE") {
            paint();
        } else if (PROPERTY == "TICKMARK_GLOW_VISIBILITY") {
            drawCircularTickmarks(control, tickmarks, center, gaugeBounds);
        } else if (PROPERTY == "POINTER_GLOW") {
            drawPointer();
        } else if (PROPERTY == "POINTER_SHADOW") {
            drawPointer();
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
        center = new Point2D(gaugeBounds.getWidth() * 0.5, gaugeBounds.getHeight() * 0.5);
        getChildren().clear();
        drawCircularFrame(control, frame, gaugeBounds);
        drawCircularBackground(control, background, gaugeBounds);
        drawCircularTrend(control, glowOn, gaugeBounds);
        updateSections();
        drawCircularSections(control, sections, gaugeBounds);
        updateAreas();
        drawCircularAreas(control, areas, gaugeBounds);
        drawTitleAndUnit();
        drawCircularTickmarks(control, tickmarks, center, gaugeBounds);
        drawCircularLed(control, ledOff, ledOn, gaugeBounds);
        drawCircularUserLed(control, userLedOff, userLedOn, gaugeBounds);
        drawThreshold();
        drawCircularGlowOff(glowOff, gaugeBounds);
        drawCircularGlowOn(control, glowOn, glowColors, gaugeBounds);
        drawMinMeasuredIndicator();
        drawMaxMeasuredIndicator();
        drawCircularIndicators(control, indicators, center, gaugeBounds);
        drawCircularLcd(control, lcd, gaugeBounds);
        drawLcdContent();
        drawPointer();
        drawCircularBargraph(control, bargraphOff, noOfLeds, ledsOff, false, true, center, gaugeBounds);
        drawCircularBargraph(control, bargraphOn, noOfLeds, ledsOn, true, false, center, gaugeBounds);
        drawCircularKnobs(control, knobs, center, gaugeBounds);
        drawCircularForeground(control, foreground, gaugeBounds);

        getChildren().addAll(frame,
                             background,
                             trend,
                             sections,
                             areas,
                             ledOff,
                             ledOn,
                             userLedOff,
                             userLedOn,
                             titleAndUnit,
                             tickmarks,
                             threshold,
                             glowOff,
                             glowOn,
                             lcd,
                             lcdContent,
                             pointer,
                             bargraphOff,
                             bargraphOn,
                             minMeasured,
                             maxMeasured,
                             indicators,
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

    @Override public Radial getSkinnable() {
        return control;
    }

    @Override public void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double HEIGHT) {
        double prefWidth = PREF_SIZE.getWidth();
        if (HEIGHT != -1) {
            prefWidth = Math.max(0, HEIGHT - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double WIDTH) {
        double prefHeight = PREF_SIZE.getHeight();
        if (WIDTH != -1) {
            prefHeight = Math.max(0, WIDTH - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }

    private String formatLcdValue(final double VALUE) {
        final StringBuilder DEC_BUFFER = new StringBuilder(16);
        DEC_BUFFER.append("0");
        final int lcdDecimals = control.getLcdDecimals();
        final boolean lcdScientificFormatEnabled = false;

        if (lcdDecimals > 0) {
            DEC_BUFFER.append(".");
        }

        for (int i = 0; i < lcdDecimals; i++) {
            DEC_BUFFER.append("0");
        }

        if (lcdScientificFormatEnabled) {
            DEC_BUFFER.append("E0");
        }

        DEC_BUFFER.trimToSize();
        final java.text.DecimalFormat DEC_FORMAT = new java.text.DecimalFormat(DEC_BUFFER.toString(), new java.text.DecimalFormatSymbols(java.util.Locale.US));

        return DEC_FORMAT.format(VALUE);
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
            final double ANGLE_START = control.getRadialRange().SECTIONS_OFFSET - (SECTION_START * control.getAngleStep()) + (control.getMinValue() * control.getAngleStep());
            final double ANGLE_EXTEND = -(SECTION_STOP - SECTION_START) * control.getAngleStep();

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
            final double ANGLE_START = control.getRadialRange().SECTIONS_OFFSET - (AREA_START * control.getAngleStep()) + (control.getMinValue() * control.getAngleStep());
            final double ANGLE_EXTEND = -(AREA_STOP - AREA_START) * control.getAngleStep();

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
    public void drawTitleAndUnit() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        titleAndUnit.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, SIZE, SIZE);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        titleAndUnit.getChildren().add(IBOUNDS);

        final Font TITLE_FONT = Font.font("Verdana", FontWeight.NORMAL, (0.046728972 * SIZE));
        final Text title = new Text();
        title.setTextOrigin(VPos.BOTTOM);
        title.setFont(TITLE_FONT);
        title.setText(control.getTitle());
        title.setX(((SIZE - title.getLayoutBounds().getWidth()) / 2.0));
        title.setY(0.3 * SIZE + title.getLayoutBounds().getHeight());
        title.setId(control.getBackgroundDesign().CSS_TEXT);

        final Font UNIT_FONT = Font.font("Verdana", FontWeight.NORMAL, (0.046728972 * SIZE));
        final Text unit = new Text();
        unit.setTextOrigin(VPos.BOTTOM);
        unit.setFont(UNIT_FONT);
        unit.setText(control.getUnit());
        unit.setX((SIZE - unit.getLayoutBounds().getWidth()) / 2.0);
        unit.setY(0.365 * SIZE + unit.getLayoutBounds().getHeight());
        unit.setId(control.getBackgroundDesign().CSS_TEXT);

        titleAndUnit.getChildren().addAll(title, unit);
        titleAndUnit.setCache(true);
    }

    public void drawThreshold() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        threshold.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        threshold.getChildren().add(IBOUNDS);

        final Path THRESHOLD = createTriangleShape(0.03 * WIDTH, 0.03 * HEIGHT, false);
        THRESHOLD.setStrokeType(StrokeType.CENTERED);
        THRESHOLD.setStrokeLineCap(StrokeLineCap.ROUND);
        THRESHOLD.setStrokeLineJoin(StrokeLineJoin.ROUND);
        THRESHOLD.setStrokeWidth(0.002 * HEIGHT);

        THRESHOLD.getStyleClass().add("root");
        THRESHOLD.setStyle(control.getThresholdColor().CSS);
        THRESHOLD.setId("threshold-gradient");

        THRESHOLD.setTranslateX(0.485 * WIDTH);
        THRESHOLD.setTranslateY(0.14 * HEIGHT);

        threshold.getChildren().addAll(THRESHOLD);

        threshold.getTransforms().clear();
        threshold.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getY()));
        threshold.getTransforms().add(Transform.rotate(-control.getMinValue() * control.getAngleStep(), center.getX(), center.getY()));
        threshold.getTransforms().add(Transform.rotate(control.getThreshold() * control.getAngleStep(), center.getX(), center.getY()));
    }

    public void drawMinMeasuredIndicator() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        minMeasured.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        minMeasured.getChildren().add(IBOUNDS);

        final Path MIN_MEASURED = createTriangleShape(0.03 * WIDTH, 0.035 * HEIGHT, true);
        MIN_MEASURED.setFill(Color.color(0.0, 0.0, 0.8));
        MIN_MEASURED.setStroke(null);

        MIN_MEASURED.setTranslateX(0.485 * WIDTH);
        MIN_MEASURED.setTranslateY(0.1 * HEIGHT);

        minMeasured.getChildren().add(MIN_MEASURED);

        minMeasured.getTransforms().clear();
        minMeasured.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getY()));
        minMeasured.getTransforms().add(Transform.rotate(-control.getMinValue() * control.getAngleStep(), center.getX(), center.getY()));
        minMeasured.getTransforms().add(Transform.rotate(control.getMinMeasuredValue() * control.getAngleStep(), center.getX(), center.getY()));
    }

    public void drawMaxMeasuredIndicator() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        maxMeasured.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        maxMeasured.getChildren().add(IBOUNDS);

        final Path MAX_MEASURED = createTriangleShape(0.03 * WIDTH, 0.035 * HEIGHT, true);
        MAX_MEASURED.setFill(Color.color(0.8, 0.0, 0.0));
        MAX_MEASURED.setStroke(null);

        MAX_MEASURED.setTranslateX(0.485 * WIDTH);
        MAX_MEASURED.setTranslateY(0.1 * HEIGHT);

        maxMeasured.getChildren().add(MAX_MEASURED);

        maxMeasured.getTransforms().clear();
        maxMeasured.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getY()));
        maxMeasured.getTransforms().add(Transform.rotate(-control.getMinValue() * control.getAngleStep(), center.getX(), center.getY()));
        maxMeasured.getTransforms().add(Transform.rotate(control.getMaxMeasuredValue() * control.getAngleStep(), center.getX(), center.getY()));
    }

    public void drawPointer() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        pointer.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        pointer.getChildren().addAll(IBOUNDS);

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

        // Pointer shadow
        final DropShadow SHADOW;
        if (control.isPointerShadowEnabled()) {
            SHADOW = new DropShadow();
            SHADOW.setHeight(0.03 * WIDTH);
            SHADOW.setWidth(0.03 * HEIGHT);
            SHADOW.setRadius(0.03 * WIDTH);
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
            if (control.getPointerType() == PointerType.TYPE9) {
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
        if (control.getPointerType() == PointerType.TYPE9) {
            pointer.getChildren().add(POINTER_FRONT);
        }

        pointer.getTransforms().clear();
        pointer.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getY()));
        pointer.setCache(true);
    }

    public void drawLcdContent() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        lcdContent.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, SIZE, SIZE);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        lcdContent.getChildren().add(IBOUNDS);

        final Rectangle LCD_FRAME = new Rectangle(((SIZE - SIZE * control.getRadialRange().LCD_FACTORS.getX()) / 2.0), (SIZE * control.getRadialRange().LCD_FACTORS.getY()), (SIZE * control.getRadialRange().LCD_FACTORS.getWidth()), (SIZE * control.getRadialRange().LCD_FACTORS.getHeight()));

        final Font LCD_UNIT_FONT = Font.font(control.getLcdUnitFont(), FontWeight.NORMAL, (0.4 * LCD_FRAME.getLayoutBounds().getHeight()));
        final Font LCD_VALUE_FONT;
        final double UNIT_Y_OFFSET;
        if (control.isLcdDigitalFontEnabled()) {
            LCD_VALUE_FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/digital.ttf"), (0.75 * LCD_FRAME.getLayoutBounds().getHeight()));
            UNIT_Y_OFFSET = 1.5;
        } else {
            LCD_VALUE_FONT = Font.font("Verdana", FontWeight.NORMAL, (0.6 * LCD_FRAME.getLayoutBounds().getHeight()));
            UNIT_Y_OFFSET = 2.0;
        }

        lcdValueString.setFont(LCD_VALUE_FONT);
        lcdUnitString.setFont(LCD_UNIT_FONT);

        // Unit
        lcdUnitString.setText(control.isLcdValueCoupled() ? control.getUnit() : control.getLcdUnit());
        lcdUnitString.setTextOrigin(VPos.BOTTOM);
        lcdUnitString.setTextAlignment(TextAlignment.RIGHT);
        if (!lcdUnitString.visibleProperty().isBound()) {
            lcdUnitString.visibleProperty().bind(control.lcdUnitVisibleProperty());
        }
        if (control.isLcdUnitVisible()) {
            lcdUnitString.setX(LCD_FRAME.getX() + (LCD_FRAME.getWidth() - lcdUnitString.getLayoutBounds().getWidth()) - LCD_FRAME.getHeight() * 0.0625);
            lcdUnitString.setY(LCD_FRAME.getY() + (LCD_FRAME.getHeight() + lcdValueString.getLayoutBounds().getHeight()) / UNIT_Y_OFFSET - (lcdValueString.getLayoutBounds().getHeight() * 0.05));
        }
        lcdUnitString.getStyleClass().add("lcd");
        lcdUnitString.setStyle(control.getLcdDesign().CSS);
        lcdUnitString.setId("lcd-text");
        lcdUnitString.setStroke(null);

        // Value
        switch (control.getLcdNumberSystem()) {
            case HEXADECIMAL:
                lcdValueString.setText(Integer.toHexString((int) currentLcdValue.get()).toUpperCase());
                break;

            case OCTAL:
                lcdValueString.setText(Integer.toOctalString((int) currentLcdValue.get()).toUpperCase());
                break;

            case DECIMAL:

            default:
                lcdValueString.setText(formatLcdValue(currentLcdValue.get()));
                break;
        }
        if (control.isLcdUnitVisible()) {
            lcdValueString.setX((LCD_FRAME.getX() + (LCD_FRAME.getWidth() - lcdUnitString.getLayoutBounds().getWidth() - lcdValueString.getLayoutBounds().getWidth()) - LCD_FRAME.getHeight() * 0.0833333333));
        } else {
            lcdValueString.setX((LCD_FRAME.getX() + (LCD_FRAME.getWidth() - lcdValueString.getLayoutBounds().getWidth()) - LCD_FRAME.getHeight() * 0.0625));
        }
        lcdValueString.setY(LCD_FRAME.getY() + (LCD_FRAME.getHeight() + lcdValueString.getLayoutBounds().getHeight()) / 2.0);
        lcdValueString.setTextOrigin(VPos.BOTTOM);
        lcdValueString.setTextAlignment(TextAlignment.RIGHT);
        lcdValueString.getStyleClass().add("lcd");
        lcdValueString.setStyle(control.getLcdDesign().CSS);
        lcdValueString.setId("lcd-text");
        lcdValueString.setStroke(null);

        lcdContent.getChildren().addAll(lcdUnitString, lcdValueString);
    }
}
