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

import javafx.animation.Animation;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
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
import jfxtras.labs.internal.scene.control.behavior.RadialQuarterNBehavior;
import jfxtras.labs.scene.control.gauge.Gauge;
import jfxtras.labs.scene.control.gauge.RadialQuarterN;
import jfxtras.labs.scene.control.gauge.Section;

import java.util.ArrayList;


/**
 * Created by
 * User: hansolo
 * Date: 01.02.12
 * Time: 17:28
 */
public class RadialQuarterNSkin extends GaugeSkinBase<RadialQuarterN, RadialQuarterNBehavior> {
    private static final Rectangle MIN_SIZE  = new Rectangle(25, 25);
    private static final Rectangle PREF_SIZE = new Rectangle(200, 200);
    private static final Rectangle MAX_SIZE  = new Rectangle(1024, 1024);
    private RadialQuarterN   control;
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
    private Group            lcd;
    private Group            lcdContent;
    private Font             lcdUnitFont;
    private Font             lcdValueFont;
    private Text             lcdValueString;
    private Text             lcdUnitString;
    private Group            lcdThresholdIndicator;
    private Group            knobs;
    private Group            knobsShadow;
    private Group            threshold;
    private Group            minMeasured;
    private Group            maxMeasured;
    private Group            pointer;
    private Group            pointerShadow;
    private Group            ledOff;
    private Group            ledOn;
    private Group            userLedOff;
    private Group            userLedOn;
    private Group            foreground;
    private Point2D          center;
    private Timeline         rotationAngleTimeline;
    private DoubleProperty   gaugeValue;
    private DoubleProperty   currentValue;
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
    public RadialQuarterNSkin(final RadialQuarterN CONTROL) {
        super(CONTROL, new RadialQuarterNBehavior(CONTROL));
        control                = CONTROL;
        gaugeBounds            = new Rectangle(200, 200);
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
        lcd                    = new Group();
        lcdContent             = new Group();
        lcdValueString         = new Text();
        lcdUnitString          = new Text();
        lcdThresholdIndicator  = new Group();
        knobs                  = new Group();
        knobsShadow            = new Group(knobs);
        threshold              = new Group();
        minMeasured            = new Group();
        maxMeasured            = new Group();
        pointer                = new Group();
        pointerShadow          = new Group(pointer);
        ledOff                 = new Group();
        ledOn                  = new Group();
        userLedOff             = new Group();
        userLedOn              = new Group();
        foreground             = new Group();
        rotationAngleTimeline  = new Timeline();
        gaugeValue             = new SimpleDoubleProperty(0);
        currentValue           = new SimpleDoubleProperty(0);
        lcdValue               = new SimpleDoubleProperty(0);
        currentLcdValue        = new SimpleDoubleProperty(0);
        glowPulse              = new FadeTransition(Duration.millis(800), glowOn);
        pointerRotation        = new Rotate();
        isDirty                = false;
        ledTimer               = new AnimationTimer() {
            @Override public void handle(final long NOW) {
                if (NOW > lastLedTimerCall + getBlinkInterval()) {
                    ledOnVisible ^= true;
                    if (ledOnVisible) {
                        ledOn.setOpacity(1.0);
                    } else {
                        ledOn.setOpacity(0.0);
                    }
                    lastLedTimerCall = NOW;
                }
            }
        };
        lastLedTimerCall       = 0l;
        ledOnVisible           = false;
        userLedTimer           = new AnimationTimer() {
            @Override public void handle(final long NOW) {
                if (NOW > lastUserLedTimerCall + getBlinkInterval()) {
                    userLedOnVisible ^= true;
                    if (userLedOnVisible) {
                        userLedOn.setOpacity(1.0);
                    } else {
                        userLedOn.setOpacity(0.0);
                    }
                    lastUserLedTimerCall = NOW;
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

        if (gaugeValue.get() < control.getMinValue()) {
            gaugeValue.set(control.getMinValue());
        } else if (gaugeValue.get() > control.getMaxValue()) {
            gaugeValue.set(control.getMaxValue());
        }

        control.recalcRange();
        control.setMinMeasuredValue(control.getMaxValue());
        control.setMaxMeasuredValue(control.getMinValue());

        addBindings();
        addListeners();

        calcGaugeBounds();

        initialized = true;
        repaint();
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

        if (lcdValue.isBound()) {
            lcdValue.unbind();
        }
        lcdValue.bind(control.valueProperty());

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
        control.getAreas().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                updateAreas();
                drawCircularAreas(control, areas, gaugeBounds);
            }
        });

        control.getSections().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                updateSections();
                drawCircularSections(control, sections, gaugeBounds);
            }
        });

        control.getMarkers().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                drawCircularIndicators(control, markers, center, gaugeBounds);
            }
        });

        control.valueProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                if (rotationAngleTimeline.getStatus() != Animation.Status.STOPPED) {
                    rotationAngleTimeline.stop();
                }

                // If the new value is in the range between the old value +- the redraw tolerance return and do nothing
                if (newValue.doubleValue() > (oldValue.doubleValue() - control.getRedrawToleranceValue()) &&
                    newValue.doubleValue() < (oldValue.doubleValue() + control.getRedrawToleranceValue())) {
                    return;
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
                    pointer.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getY()));
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
                pointer.getTransforms().clear();
                pointerRotation.setPivotX(center.getX());
                pointerRotation.setPivotY(center.getY());
                pointerRotation.setAngle((newValue.doubleValue() - control.getMinValue()) * control.getAngleStep());
                pointer.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getY()));
                pointer.getTransforms().add(pointerRotation);

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


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);

        if ("ANIMATION_DURATION".equals(PROPERTY)) {
            //pointerRotation.setDuration(Duration.millis(control.getAnimationDuration()));
        } else if ("RADIAL_RANGE".equals(PROPERTY)) {
            isDirty = true;
        } else if ("FRAME_DESIGN".equals(PROPERTY)) {
            drawCircularFrame(control, frame, gaugeBounds);
        } else if ("BACKGROUND_DESIGN".equals(PROPERTY)) {
            drawCircularBackground(control, background, gaugeBounds);
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
            drawCircularForeground(control, foreground, gaugeBounds);
        } else if ("LCD".equals(PROPERTY)) {
            drawCircularLcd(control, lcd, gaugeBounds);
            lcdUnitString.getStyleClass().clear();
            lcdUnitString.getStyleClass().add("lcd");
            lcdUnitString.getStyleClass().add(control.getLcdDesign().CSS);
            lcdUnitString.getStyleClass().add("lcd-text");
            lcdValueString.getStyleClass().clear();
            lcdValueString.getStyleClass().add("lcd");
            lcdValueString.getStyleClass().add(control.getLcdDesign().CSS);
            lcdValueString.getStyleClass().add("lcd-text");
            drawLcdContent();
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
            drawCircularGlowOn(control, glowOn, glowColors, gaugeBounds);
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

        } else if ("TICKMARKS".equals(PROPERTY)) {
            drawCircularTickmarks(control, tickmarks, center, gaugeBounds);
        } else if ("MIN_MEASURED_VALUE".equals(PROPERTY)) {
            minMeasured.getTransforms().clear();
            minMeasured.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getY()));
            minMeasured.getTransforms().add(Transform.rotate(-control.getMinValue() * control.getAngleStep(), center.getX(), center.getY()));
            minMeasured.getTransforms().add(Transform.rotate(control.getMinMeasuredValue() * control.getAngleStep(), center.getX(), center.getY()));
        } else if ("MAX_MEASURED_VALUE".equals(PROPERTY)) {
            maxMeasured.getTransforms().clear();
            maxMeasured.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getY()));
            maxMeasured.getTransforms().add(Transform.rotate(-control.getMinValue() * control.getAngleStep(), center.getX(), center.getY()));
            maxMeasured.getTransforms().add(Transform.rotate(control.getMaxMeasuredValue() * control.getAngleStep(), center.getX(), center.getY()));
        } else if ("TREND".equals(PROPERTY)) {
            drawCircularTrend(control, trend, gaugeBounds);
        } else if ("GAUGE_MODEL".equals(PROPERTY)) {
            addBindings();
        } else if ("STYLE_MODEL".equals(PROPERTY)) {
            addBindings();
        } else if ("THRESHOLD_EXCEEDED".equals(PROPERTY)) {
            if(control.isThresholdExceeded()) {
                ledTimer.start();
            } else {
                ledTimer.stop();
            }
        } else if ("PREF_WIDTH".equals(PROPERTY)) {
            repaint();
        } else if ("PREF_HEIGHT".equals(PROPERTY)) {
            repaint();
        } else if ("AREAS".equals(PROPERTY)) {
            updateAreas();
            drawCircularAreas(control, areas, gaugeBounds);
        } else if ("SECTIONS".equals(PROPERTY)) {
            updateSections();
            drawCircularSections(control, sections, gaugeBounds);
        } else if ("MARKERS".equals(PROPERTY)) {
            drawCircularIndicators(control, markers, center, gaugeBounds);
        }
    }

    public void repaint() {
        isDirty = true;
        requestLayout();
    }

    @Override public void layoutChildren() {
        if (!isDirty) {
            return;
        }
        adjustLcdFont();
        if (!initialized) {
            init();
        }
        if (control.getScene() != null) {
            calcGaugeBounds();
            setTranslateX(framelessOffset.getX());
            setTranslateY(framelessOffset.getY());
            center = new Point2D(gaugeBounds.getWidth() * 0.5, gaugeBounds.getHeight() * 0.735);
            getChildren().clear();
            drawCircularFrame(control, frame, gaugeBounds);
            drawCircularBackground(control, background, gaugeBounds);
            drawCircularTrend(control, trend, gaugeBounds);
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
            drawCircularLcd(control, lcd, gaugeBounds);
            drawLcdContent();
            drawPointer();
            drawCircularKnobs(control, knobs, center, gaugeBounds);
            drawCircularForeground(control, foreground, gaugeBounds);
            if (control.isPointerShadowEnabled() && !control.isPointerGlowEnabled()) {
                addDropShadow(control, knobs, pointerShadow);
            }

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
                minMeasured,
                maxMeasured,
                markers,
                lcd,
                lcdContent,
                pointerShadow,
                knobsShadow,
                foreground);
        }
        isDirty = false;

        super.layoutChildren();
    }

    @Override public RadialQuarterN getSkinnable() {
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

    @Override protected double computeMinWidth(final double WIDTH) {
        return super.computeMinWidth(Math.max(MIN_SIZE.getWidth(), WIDTH - getInsets().getLeft() - getInsets().getRight()));
    }

    @Override protected double computeMinHeight(final double HEIGHT) {
        return super.computeMinHeight(Math.max(MIN_SIZE.getHeight(), HEIGHT - getInsets().getTop() - getInsets().getBottom()));
    }

    @Override protected double computeMaxWidth(final double WIDTH) {
        return super.computeMaxWidth(Math.max(MAX_SIZE.getWidth(), WIDTH - getInsets().getLeft() - getInsets().getRight()));
    }

    @Override protected double computeMaxHeight(final double HEIGHT) {
        return super.computeMaxHeight(Math.max(MAX_SIZE.getHeight(), HEIGHT - getInsets().getTop() - getInsets().getBottom()));
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
        final double OUTER_RADIUS = control.getPrefWidth() * 0.5;
        final double INNER_RADIUS = control.isExpandedSections() ? OUTER_RADIUS - control.getPrefWidth() * 0.12 : OUTER_RADIUS - control.getPrefWidth() * 0.04;
        final Shape INNER = new Circle(center.getX(), center.getY(), INNER_RADIUS);
        final double ANGLE_OFFSET = control.getMinValue() * control.getAngleStep() + control.getRadialRange().ROTATION_OFFSET;

        final double ZERO_OFFSET = -45 - control.getMinValue() * control.getAngleStep();

        for (final Section section : control.getSections()) {
            final double SECTION_START = section.getStart() < control.getMinValue() ? control.getMinValue() : section.getStart();
            final double SECTION_STOP = section.getStop() > control.getMaxValue() ? control.getMaxValue() : section.getStop();
            final double ANGLE_START = ZERO_OFFSET + control.getRadialRange().SECTIONS_OFFSET - (SECTION_START * control.getAngleStep()) + (control.getMinValue() * control.getAngleStep());
            final double ANGLE_EXTEND = -(SECTION_STOP - SECTION_START) * control.getAngleStep();

            final Arc OUTER_ARC = new Arc();
            OUTER_ARC.setType(ArcType.ROUND);
            OUTER_ARC.setCenterX(center.getX());
            OUTER_ARC.setCenterY(center.getY());
            OUTER_ARC.setRadiusX(OUTER_RADIUS);
            OUTER_ARC.setRadiusY(OUTER_RADIUS);
            OUTER_ARC.setStartAngle(ANGLE_OFFSET + ANGLE_START);
            OUTER_ARC.setLength(ANGLE_EXTEND);
            final Shape SECTION = Shape.subtract(OUTER_ARC, INNER);

            section.setSectionArea(SECTION);
        }
    }

    private void updateAreas() {
        final double OUTER_RADIUS = control.getPrefWidth() * 0.5;
        final double INNER_RADIUS = control.isExpandedSections() ? control.getPrefWidth() * 0.12 : control.getPrefWidth() * 0.04;
        final double RADIUS = OUTER_RADIUS - INNER_RADIUS;
        final double ANGLE_OFFSET = control.getMinValue() * control.getAngleStep() + control.getRadialRange().ROTATION_OFFSET;
        final Rectangle SUBTRACT = new Rectangle(0, control.getRadialRange().LCD_FACTORS.getY() * control.getPrefHeight(),
                                                 control.getPrefWidth(), control.getPrefHeight() / 2);
        final double ZERO_OFFSET = -45 - control.getMinValue() * control.getAngleStep();

        for (final Section area : control.getAreas()) {
            final double AREA_START = area.getStart() < control.getMinValue() ? control.getMinValue() : area.getStart();
            final double AREA_STOP = area.getStop() > control.getMaxValue() ? control.getMaxValue() : area.getStop();
            final double ANGLE_START = ZERO_OFFSET + control.getRadialRange().SECTIONS_OFFSET - (AREA_START * control.getAngleStep()) + (control.getMinValue() * control.getAngleStep());
            final double ANGLE_EXTEND = -(AREA_STOP - AREA_START) * control.getAngleStep();

            final Arc ARC = new Arc();
            ARC.setType(ArcType.ROUND);
            ARC.setCenterX(center.getX());
            ARC.setCenterY(center.getY());
            ARC.setRadiusX(RADIUS);
            ARC.setRadiusY(RADIUS);
            ARC.setStartAngle(ANGLE_OFFSET + ANGLE_START);
            ARC.setLength(ANGLE_EXTEND);
            final Shape AREA = Shape.subtract(ARC, SUBTRACT);

            area.setFilledArea(AREA);
        }
    }

    private void adjustLcdFont() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        // Load font for lcd
        final double LCD_HEIGHT = SIZE * control.getRadialRange().LCD_FACTORS.getHeight();
        lcdUnitFont = Font.font(control.getLcdUnitFont(), FontWeight.NORMAL, (0.4 * LCD_HEIGHT));
        switch(control.getLcdValueFont()) {
            case LCD:
                lcdValueFont = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/digital.ttf"), (0.75 * LCD_HEIGHT));
                break;
            case BUS:
                lcdValueFont = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/bus.otf"), (0.6 * LCD_HEIGHT));
                break;
            case PIXEL:
                lcdValueFont = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/pixel.ttf"), (0.6 * LCD_HEIGHT));
                break;
            case PHONE_LCD:
                lcdValueFont = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/phonelcd.ttf"), (0.6 * LCD_HEIGHT));
                break;
            case STANDARD:
            default:
                lcdValueFont = Font.font("Verdana", FontWeight.NORMAL, (0.6 * LCD_HEIGHT));
                break;
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

        final Font TITLE_FONT = Font.font(control.getTitleFont(), FontWeight.NORMAL, (0.046728972 * SIZE));
        final Text title = new Text();
        title.setTextOrigin(VPos.BOTTOM);
        title.setFont(TITLE_FONT);
        title.setText(control.getTitle());
        title.setX(((SIZE - title.getLayoutBounds().getWidth()) / 2.0));
        title.setY(0.16 * SIZE + title.getLayoutBounds().getHeight());
        title.getStyleClass().add(control.getBackgroundDesign().CSS_TEXT);

        final Font UNIT_FONT = Font.font(control.getUnitFont(), FontWeight.NORMAL, (0.046728972 * SIZE));
        final Text unit = new Text();
        unit.setTextOrigin(VPos.BOTTOM);
        unit.setFont(UNIT_FONT);
        unit.setText(control.getUnit());
        unit.setX((SIZE - unit.getLayoutBounds().getWidth()) / 2.0);
        unit.setY(0.365 * SIZE + unit.getLayoutBounds().getHeight());
        unit.getStyleClass().add(control.getBackgroundDesign().CSS_TEXT);

        titleAndUnit.getChildren().addAll(title, unit);
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
        THRESHOLD.getStyleClass().add("threshold-gradient");

        THRESHOLD.setTranslateX(0.485 * WIDTH);
        THRESHOLD.setTranslateY(0.25 * HEIGHT);

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
        MIN_MEASURED.setTranslateY(0.21 * HEIGHT);

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
        MAX_MEASURED.setTranslateY(0.21 * HEIGHT);

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
                POINTER.getStyleClass().add("pointer2-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.52 * WIDTH, 0.71 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.515 * WIDTH, 0.695 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.51 * WIDTH, 0.545 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.505 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.495 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49 * WIDTH, 0.545 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.485 * WIDTH, 0.695 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48 * WIDTH, 0.71 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.48 * WIDTH, 0.71 * HEIGHT,
                                                           0.465 * WIDTH, 0.72 * HEIGHT,
                                                           0.465 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.465 * WIDTH, 0.75 * HEIGHT,
                                                           0.48 * WIDTH, 0.765 * HEIGHT,
                                                           0.5 * WIDTH, 0.765 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.515 * WIDTH, 0.765 * HEIGHT,
                                                           0.535 * WIDTH, 0.75 * HEIGHT,
                                                           0.535 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.535 * WIDTH, 0.72 * HEIGHT,
                                                           0.52 * WIDTH, 0.71 * HEIGHT,
                                                           0.52 * WIDTH, 0.71 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE3:
                POINTER.getStyleClass().add("pointer3-gradient");
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
                POINTER.getStyleClass().add("pointer4-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.5 * WIDTH, 0.255 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.51 * WIDTH, 0.265 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.535 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.525 * WIDTH, 0.855 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.475 * WIDTH, 0.855 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.465 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49 * WIDTH, 0.265 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.255 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE5:
                POINTER.getStyleClass().add("pointer5-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.5 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.525 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.245 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.475 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;

            case TYPE6:
                POINTER.getStyleClass().add("pointer6-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.475 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.475 * WIDTH, 0.595 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.48 * WIDTH, 0.49 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.495 * WIDTH, 0.255 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.495 * WIDTH, 0.255 * HEIGHT,
                                                           0.495 * WIDTH, 0.25 * HEIGHT,
                                                           0.5 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.505 * WIDTH, 0.25 * HEIGHT,
                                                           0.505 * WIDTH, 0.255 * HEIGHT,
                                                           0.505 * WIDTH, 0.255 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.52 * WIDTH, 0.49 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.525 * WIDTH, 0.595 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.525 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.505 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.505 * WIDTH, 0.595 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.505 * WIDTH, 0.595 * HEIGHT,
                                                           0.505 * WIDTH, 0.495 * HEIGHT,
                                                           0.5 * WIDTH, 0.495 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.495 * WIDTH, 0.495 * HEIGHT,
                                                           0.495 * WIDTH, 0.595 * HEIGHT,
                                                           0.495 * WIDTH, 0.595 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.495 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.475 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE7:
                POINTER.getStyleClass().add("pointer7-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.49 * WIDTH, 0.255 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.47 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.525 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.51 * WIDTH, 0.255 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49 * WIDTH, 0.255 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE8:
                POINTER.getStyleClass().add("pointer8-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.5 * WIDTH, 0.765 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.535 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.535 * WIDTH, 0.735 * HEIGHT,
                                                           0.505 * WIDTH, 0.65 * HEIGHT,
                                                           0.5 * WIDTH, 0.255 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.495 * WIDTH, 0.65 * HEIGHT,
                                                           0.465 * WIDTH, 0.735 * HEIGHT,
                                                           0.465 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.765 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;

            case TYPE9:
                POINTER.getStyleClass().add("pointer9-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.495 * WIDTH, 0.37 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.505 * WIDTH, 0.37 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.515 * WIDTH, 0.66 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.485 * WIDTH, 0.66 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.495 * WIDTH, 0.37 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.getElements().add(new MoveTo(0.49 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.475 * WIDTH, 0.705 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.475 * WIDTH, 0.765 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.475 * WIDTH, 0.765 * HEIGHT,
                                                           0.475 * WIDTH, 0.855 * HEIGHT,
                                                           0.475 * WIDTH, 0.855 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.475 * WIDTH, 0.855 * HEIGHT,
                                                           0.48 * WIDTH, 0.86 * HEIGHT,
                                                           0.5 * WIDTH, 0.86 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.52 * WIDTH, 0.86 * HEIGHT,
                                                           0.525 * WIDTH, 0.855 * HEIGHT,
                                                           0.525 * WIDTH, 0.855 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.525 * WIDTH, 0.855 * HEIGHT,
                                                           0.525 * WIDTH, 0.765 * HEIGHT,
                                                           0.525 * WIDTH, 0.765 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.525 * WIDTH, 0.705 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.51 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);

                POINTER_FRONT.getStyleClass().add("root");
                POINTER_FRONT.setStyle("-fx-value: " + control.getValueColor().CSS);
                POINTER_FRONT.getStyleClass().add("pointer9-box");
                POINTER_FRONT.setFillRule(FillRule.EVEN_ODD);
                POINTER_FRONT.getElements().add(new MoveTo(0.495 * WIDTH, 0.355 * HEIGHT));
                POINTER_FRONT.getElements().add(new LineTo(0.505 * WIDTH, 0.355 * HEIGHT));
                POINTER_FRONT.getElements().add(new LineTo(0.505 * WIDTH, 0.255 * HEIGHT));
                POINTER_FRONT.getElements().add(new LineTo(0.495 * WIDTH, 0.255 * HEIGHT));
                POINTER_FRONT.getElements().add(new LineTo(0.495 * WIDTH, 0.355 * HEIGHT));
                POINTER_FRONT.getElements().add(new ClosePath());
                POINTER_FRONT.setStroke(null);
                break;

            case TYPE10:
                POINTER.getStyleClass().add("pointer10-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.5 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.5 * WIDTH, 0.25 * HEIGHT,
                                                           0.435 * WIDTH, 0.715 * HEIGHT,
                                                           0.435 * WIDTH, 0.725 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.435 * WIDTH, 0.765 * HEIGHT,
                                                           0.465 * WIDTH, 0.795 * HEIGHT,
                                                           0.5 * WIDTH, 0.795 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.535 * WIDTH, 0.795 * HEIGHT,
                                                           0.565 * WIDTH, 0.765 * HEIGHT,
                                                           0.565 * WIDTH, 0.725 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.565 * WIDTH, 0.715 * HEIGHT,
                                                           0.5 * WIDTH, 0.25 * HEIGHT,
                                                           0.5 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;

            case TYPE11:
                POINTER.getStyleClass().add("pointer11-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.5 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.485 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.485 * WIDTH, 0.735 * HEIGHT,
                                                           0.485 * WIDTH, 0.81 * HEIGHT,
                                                           0.5 * WIDTH, 0.81 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.515 * WIDTH, 0.81 * HEIGHT,
                                                           0.515 * WIDTH, 0.735 * HEIGHT,
                                                           0.515 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;

            case TYPE12:
                POINTER.getStyleClass().add("pointer12-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.5 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.485 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.75 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.515 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;

            case TYPE13:
                POINTER.getStyleClass().add("pointer13-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.485 * WIDTH, 0.275 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.245 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.515 * WIDTH, 0.275 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.515 * WIDTH, 0.745 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.485 * WIDTH, 0.745 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.485 * WIDTH, 0.275 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE14:
                POINTER.getStyleClass().add("pointer14-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.485 * WIDTH, 0.275 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.245 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.515 * WIDTH, 0.275 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.515 * WIDTH, 0.745 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.485 * WIDTH, 0.745 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.485 * WIDTH, 0.275 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE15:
                POINTER.getStyleClass().add("pointer15-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.485 * WIDTH, 0.745 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.485 * WIDTH, 0.37 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.46 * WIDTH, 0.37 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.5 * WIDTH, 0.245 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.535 * WIDTH, 0.37 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.515 * WIDTH, 0.37 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.515 * WIDTH, 0.745 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.485 * WIDTH, 0.745 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE16:
                POINTER.getStyleClass().add("pointer16-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.5 * WIDTH, 0.86 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.52 * WIDTH, 0.86 * HEIGHT,
                                                           0.54 * WIDTH, 0.845 * HEIGHT,
                                                           0.54 * WIDTH, 0.845 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.53 * WIDTH, 0.835 * HEIGHT,
                                                           0.51 * WIDTH, 0.765 * HEIGHT,
                                                           0.51 * WIDTH, 0.765 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.51 * WIDTH, 0.705 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.51 * WIDTH, 0.295 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.505 * WIDTH, 0.29 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.505 * WIDTH, 0.24 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.495 * WIDTH, 0.24 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.495 * WIDTH, 0.29 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49 * WIDTH, 0.295 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49 * WIDTH, 0.705 * HEIGHT));
                POINTER.getElements().add(new LineTo(0.49 * WIDTH, 0.765 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.49 * WIDTH, 0.765 * HEIGHT,
                                                           0.47 * WIDTH, 0.835 * HEIGHT,
                                                           0.46 * WIDTH, 0.845 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.46 * WIDTH, 0.845 * HEIGHT,
                                                           0.475 * WIDTH, 0.86 * HEIGHT,
                                                           0.5 * WIDTH, 0.86 * HEIGHT));
                POINTER.getElements().add(new ClosePath());
                POINTER.setStroke(null);
                break;

            case TYPE1:

            default:
                POINTER.setStyle("-fx-pointer: " + control.getValueColor().CSS);
                POINTER.getStyleClass().add("pointer1-gradient");
                POINTER.setFillRule(FillRule.EVEN_ODD);
                POINTER.getElements().add(new MoveTo(0.515 * WIDTH, 0.705 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.515 * WIDTH, 0.69 * HEIGHT,
                                                           0.51 * WIDTH, 0.63 * HEIGHT,
                                                           0.51 * WIDTH, 0.615 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.505 * WIDTH, 0.6 * HEIGHT,
                                                           0.5 * WIDTH, 0.25 * HEIGHT,
                                                           0.5 * WIDTH, 0.25 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.5 * WIDTH, 0.25 * HEIGHT,
                                                           0.49 * WIDTH, 0.595 * HEIGHT,
                                                           0.49 * WIDTH, 0.61 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.49 * WIDTH, 0.63 * HEIGHT,
                                                           0.485 * WIDTH, 0.69 * HEIGHT,
                                                           0.485 * WIDTH, 0.705 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.475 * WIDTH, 0.715 * HEIGHT,
                                                           0.465 * WIDTH, 0.725 * HEIGHT,
                                                           0.465 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.465 * WIDTH, 0.75 * HEIGHT,
                                                           0.48 * WIDTH, 0.765 * HEIGHT,
                                                           0.5 * WIDTH, 0.765 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.52 * WIDTH, 0.765 * HEIGHT,
                                                           0.535 * WIDTH, 0.75 * HEIGHT,
                                                           0.535 * WIDTH, 0.735 * HEIGHT));
                POINTER.getElements().add(new CubicCurveTo(0.535 * WIDTH, 0.725 * HEIGHT,
                                                           0.525 * WIDTH, 0.715 * HEIGHT,
                                                           0.515 * WIDTH, 0.705 * HEIGHT));
                POINTER.getElements().add(new ClosePath());

                POINTER.setStrokeType(StrokeType.CENTERED);
                POINTER.setStrokeLineCap(StrokeLineCap.BUTT);
                POINTER.setStrokeLineJoin(StrokeLineJoin.ROUND);
                POINTER.setStrokeWidth(0.002 * WIDTH);
                break;
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
                POINTER_FRONT.setEffect(GLOW);
            } else {
                POINTER.setEffect(GLOW);
            }
        }

        pointer.getChildren().addAll(POINTER);
        if (control.getPointerType() == Gauge.PointerType.TYPE9) {
            pointer.getChildren().add(POINTER_FRONT);
        }

        pointer.getTransforms().clear();
        pointer.getTransforms().add(Transform.rotate(control.getRadialRange().ROTATION_OFFSET, center.getX(), center.getY()));

        pointer.setCache(true);
        pointer.setCacheHint(CacheHint.ROTATE);
    }

    public void drawLcdContent() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        lcdContent.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, SIZE, SIZE);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        lcdContent.getChildren().add(IBOUNDS);

        final Rectangle LCD_FRAME = new Rectangle(((SIZE - SIZE * control.getRadialRange().LCD_FACTORS.getX()) / 2.0), (SIZE * control.getRadialRange().LCD_FACTORS.getY()), (SIZE * control.getRadialRange().LCD_FACTORS.getWidth()), (SIZE * control.getRadialRange().LCD_FACTORS.getHeight()));

        final double UNIT_Y_OFFSET;
        switch(control.getLcdValueFont()) {
            case LCD:
                UNIT_Y_OFFSET = 1.5;
                break;
            case BUS:
                UNIT_Y_OFFSET = 2.0;
                break;
            case PIXEL:
                UNIT_Y_OFFSET = 2.0;
                break;
            case PHONE_LCD:
                UNIT_Y_OFFSET = 2.0;
                break;
            case STANDARD:
            default:
                UNIT_Y_OFFSET = 2.0;
                break;
        }
        if (lcdValueFont == null) {
            adjustLcdFont();
        }
        lcdValueString.setFont(lcdValueFont);
        lcdUnitString.setFont(lcdUnitFont);

        // Unit
        lcdUnitString.setText(control.isLcdValueCoupled() ? control.getUnit() : control.getLcdUnit());
        lcdUnitString.setTextOrigin(VPos.BOTTOM);
        lcdUnitString.setTextAlignment(TextAlignment.RIGHT);
        if (!lcdUnitString.visibleProperty().isBound()) {
            lcdUnitString.visibleProperty().bind(control.lcdUnitVisibleProperty());
        }
        if (control.isLcdUnitVisible()) {
            lcdUnitString.setX(LCD_FRAME.getX() + (LCD_FRAME.getWidth() - lcdUnitString.getLayoutBounds().getWidth()) - LCD_FRAME.getHeight() *0.0833333333 ); // 0.0625
            lcdUnitString.setY(LCD_FRAME.getY() + (LCD_FRAME.getHeight() + lcdValueString.getLayoutBounds().getHeight()) / UNIT_Y_OFFSET - (lcdValueString.getLayoutBounds().getHeight() * 0.05));
        }
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
            lcdValueString.setX((LCD_FRAME.getX() + (LCD_FRAME.getWidth() - lcdValueString.getLayoutBounds().getWidth()) - LCD_FRAME.getHeight() * 0.0833333333)); // 0.0625
        }
        lcdValueString.setY(LCD_FRAME.getY() + (LCD_FRAME.getHeight() + lcdValueString.getLayoutBounds().getHeight()) / 2.0);
        lcdValueString.setTextOrigin(VPos.BOTTOM);
        lcdValueString.setTextAlignment(TextAlignment.RIGHT);
        lcdValueString.setStroke(null);

        lcdContent.getChildren().addAll(lcdUnitString, lcdValueString);
    }
}
