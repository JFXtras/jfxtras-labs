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
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
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
import javafx.scene.paint.Stop;
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
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import jfxtras.labs.internal.scene.control.behavior.LinearBehavior;
import jfxtras.labs.scene.control.gauge.Gauge.NumberFormat;
import jfxtras.labs.scene.control.gauge.Linear;
import jfxtras.labs.scene.control.gauge.Marker;
import jfxtras.labs.scene.control.gauge.Section;
import jfxtras.labs.util.ConicalGradient;
import jfxtras.labs.util.Util;

import java.util.ArrayList;


/**
 * Created by
 * User: hansolo
 * Date: 09.01.12
 * Time: 18:04
 */
public class LinearSkin extends GaugeSkinBase<Linear, LinearBehavior> {
    private static final Rectangle MIN_SIZE  = new Rectangle(25, 50);
    private static final Rectangle PREF_SIZE = new Rectangle(170, 340);
    private static final Rectangle MAX_SIZE  = new Rectangle(512, 1024);
    private Linear           control;
    private Rectangle        gaugeBounds;
    private Point2D          framelessOffset;
    private Group            frame;
    private Group            background;
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
    private Text             lcdValueString;
    private Text             lcdUnitString;
    private Group            lcdThresholdIndicator;
    private Group            threshold;
    private Group            minMeasured;
    private Group            maxMeasured;
    private Group            bar;
    private Rectangle        currentBar;
    private Rectangle        currentBarHl;
    private Group            ledOff;
    private Group            ledOn;
    private Group            userLedOff;
    private Group            userLedOn;
    private Group            foreground;
    private DoubleProperty   currentValue;
    private double           stepsize;
    private double           formerValue;
    private DoubleProperty   lcdValue;
    private DoubleProperty   currentLcdValue;
    private FadeTransition   glowPulse;
    private Transition       toValueAnimation;
    private AnimationTimer   ledTimer;
    private boolean          ledOnVisible;
    private long             lastLedTimerCall;
    private AnimationTimer   userLedTimer;
    private boolean          userLedOnVisible;
    private long             lastUserLedTimerCall;
    private boolean          isDirty;
    private boolean          initialized;


    // ******************** Constructors **************************************
    public LinearSkin(final Linear CONTROL) {
        super(CONTROL, new LinearBehavior(CONTROL));
        control                = CONTROL;
        gaugeBounds            = new Rectangle(150, 350);
        framelessOffset        = new Point2D(0, 0);
        frame                  = new Group();
        background             = new Group();
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
        threshold              = new Group();
        minMeasured            = new Group();
        maxMeasured            = new Group();
        bar                    = new Group();
        ledOff                 = new Group();
        ledOn                  = new Group();
        userLedOff             = new Group();
        userLedOn              = new Group();
        foreground             = new Group();
        currentValue           = new SimpleDoubleProperty(0);
        lcdValue               = new SimpleDoubleProperty(0);
        currentLcdValue        = new SimpleDoubleProperty(0);
        glowPulse              = new FadeTransition(Duration.millis(800), glowOn);
        toValueAnimation       = new Transition() {
            {
                setCycleDuration(Duration.millis(control.getAnimationDuration()));
            }
            protected void interpolate(double frac) {
                currentValue.set(formerValue + (control.getValue() - formerValue) * frac);
            }
        };
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
        isDirty                = false;
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

        addBindings();
        addListeners();

        control.recalcRange();
        control.setMinMeasuredValue(control.getMaxValue());
        control.setMaxMeasuredValue(control.getMinValue());

        calcGaugeBounds();

        // Init styles
        lcdUnitString.getStyleClass().clear();
        lcdUnitString.getStyleClass().add("lcd");
        lcdUnitString.getStyleClass().add(control.getLcdDesign().CSS);
        lcdUnitString.getStyleClass().add("lcd-text");
        lcdValueString.getStyleClass().clear();
        lcdValueString.getStyleClass().add("lcd");
        lcdValueString.getStyleClass().add(control.getLcdDesign().CSS);
        lcdValueString.getStyleClass().add("lcd-text");

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

        if (lcd.visibleProperty().isBound()) {
            lcd.visibleProperty().unbind();
        }
        lcd.visibleProperty().bind(control.lcdVisibleProperty());

        if (lcdContent.visibleProperty().isBound()) {
            lcdContent.visibleProperty().unbind();
        }
        lcdContent.visibleProperty().bind(control.lcdVisibleProperty());

        if (foreground.visibleProperty().isBound()) {
            foreground.visibleProperty().unbind();
        }
        foreground.visibleProperty().bind(control.foregroundVisibleProperty());
    }

    private void addListeners() {
        /*
        control.getAreas().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable observable) {

            }
        });

        control.getSections().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable observable) {

            }
        });
        */

        control.getMarkers().addListener(new InvalidationListener() {
            @Override public void invalidated(Observable observable) {
                drawIndicators();
            }
        });

        control.valueProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                formerValue = oldValue.doubleValue();
                if (toValueAnimation.getStatus() != Animation.Status.STOPPED) {
                    toValueAnimation.stop();
                }

                // If the new value is in the range between the old value +- the redraw tolerance return and do nothing
                if (newValue.doubleValue() > (oldValue.doubleValue() - control.getRedrawToleranceValue()) &&
                    newValue.doubleValue() < (oldValue.doubleValue() + control.getRedrawToleranceValue())) {
                    return;
                }

                if (control.isValueAnimationEnabled()) {
                    toValueAnimation.setInterpolator(Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
                    toValueAnimation.play();
                } else {
                    currentValue.set(newValue.doubleValue());
                    updateBar();
                }

                checkMarkers(control, oldValue.doubleValue(), newValue.doubleValue());
            }
        });

        currentValue.addListener(new ChangeListener<Number>() {
            @Override public void changed(final ObservableValue<? extends Number> ov, final Number oldValue, final Number newValue) {
                currentLcdValue.set(control.isLcdValueCoupled() ? currentValue.get() : control.getLcdValue());
                if (Double.compare(currentValue.get(), control.getMinMeasuredValue()) < 0) {
                    control.setMinMeasuredValue(currentValue.get());
                }
                if (Double.compare(currentValue.get(), control.getMaxMeasuredValue()) > 0) {
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
                if (!control.getSections().isEmpty()) {
                    for (Section section : control.getSections()) {
                        if (Double.compare(currentValue.get(),section.getStart()) >= 0 && Double.compare(currentValue.get(),section.getStop()) <= 0) {
                            currentBar.setStyle("-fx-value: " + section.getCssColor());
                            break;
                        } else {
                            currentBar.setStyle("-fx-value: " + control.getValueColor().CSS);
                        }
                    }
                    if (control.getWidth() <= control.getHeight()) {
                        // vertical
                        currentBar.getStyleClass().add("bar-vertical-solid");
                    } else {
                        // horizontal
                        currentBar.getStyleClass().add("bar-horizontal-solid");
                    }

                }
                updateBar();
            }
        });
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);

        if ("FRAME_DESIGN".equals(PROPERTY)) {
            drawFrame();
        } else if ("BACKGROUND_DESIGN".equals(PROPERTY)) {
            drawBackground();
            drawTickmarks();
        } else if ("VALUE_COLOR".equals(PROPERTY)) {
            drawBar();
        } else if ("FOREGROUND_TYPE".equals(PROPERTY)) {
            drawForeground();
        } else if ("LCD_DESIGN".equals(PROPERTY)) {
            drawLcd();
            drawLcdContent();
        } else if ("LCD_NUMBER_SYSTEM".equals(PROPERTY)) {
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

        } else if ("TICKMARKS".equals(PROPERTY)) {
            drawTickmarks();
        } else if ("MIN_MEASURED_VALUE".equals(PROPERTY)) {
            if (control.getPrefWidth() <= control.getPrefHeight()) {
                // vertical
                minMeasured.setTranslateY((-(control.getMinMeasuredValue() - control.getMinValue()) * stepsize));
            } else {
                // horizontal
                minMeasured.setTranslateX(Math.abs((control.getMinMeasuredValue() - control.getMinValue()) * stepsize));
            }

        } else if ("MAX_MEASURED_VALUE".equals(PROPERTY)) {
            if (control.getPrefWidth() <= control.getPrefHeight()) {
                // vertical
                maxMeasured.setTranslateY((-(control.getMaxMeasuredValue() - control.getMinValue()) * stepsize));
            } else {
                // horizontal
                maxMeasured.setTranslateX(Math.abs((control.getMaxMeasuredValue() - control.getMinValue()) * stepsize));
            }
        } else if ("SIMPLE_GRADIENT_BASE".equals(PROPERTY)) {
            isDirty = true;
        } else if ("GAUGE_MODEL".equals(PROPERTY)) {
            addBindings();
        } else if ("STYLE_MODEL".equals(PROPERTY)) {
            addBindings();
        } else if ("AREAS".equals(PROPERTY)) {

        } else if ("SECTIONS".equals(PROPERTY)) {

        } else if ("MARKERS".equals(PROPERTY)) {
            drawIndicators();
        } else if ("PREF_WIDTH".equals(PROPERTY)) {
            repaint();
        } else if ("PREF_HEIGHT".equals(PROPERTY)) {
            repaint();
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
        if (!initialized) {
            init();
        }
        if (control.getScene() != null) {
            calcGaugeBounds();
            setTranslateX(framelessOffset.getX());
            setTranslateY(framelessOffset.getY());

            getChildren().clear();

            drawFrame();
            drawBackground();
            drawTitleAndUnit();
            drawTickmarks();
            drawLed();
            drawUserLed();
            drawThreshold();
            drawGlowOff();
            drawGlowOn();
            drawMinMeasuredIndicator();
            drawMaxMeasuredIndicator();
            drawIndicators();
            drawLcd();
            drawLcdContent();
            drawBar();
            drawForeground();

            getChildren().addAll(frame,
                                 background,
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
                                 bar,
                                 foreground);
        }
        isDirty = false;

        super.layoutChildren();
    }

    @Override public Linear getSkinnable() {
        return control;
    }

    @Override public void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double WIDTH) {
        double prefWidth;
        if (WIDTH < getPrefHeight()) {
            // vertical
            prefWidth = PREF_SIZE.getWidth();
        } else {
            // horizontal
            prefWidth = PREF_SIZE.getHeight();
        }
        if (WIDTH != -1) {
            prefWidth = Math.max(0, WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double HEIGHT) {
        double prefHeight;
        if (HEIGHT < getPrefHeight()) {
            // vertical
            prefHeight = PREF_SIZE.getHeight();
        } else {
            // horizontal
            prefHeight = PREF_SIZE.getWidth();
        }
        if (HEIGHT != -1) {
            prefHeight = Math.max(0, HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefHeight(prefHeight);
    }

    @Override protected double computeMinWidth(final double WIDTH) {
        double minWidth;
        if (getPrefWidth() < getPrefHeight()) {
            // vertical
            minWidth = Math.max(MIN_SIZE.getWidth(), WIDTH);
        } else {
            // horizontal
            minWidth = Math.max(MIN_SIZE.getHeight(), WIDTH);
        }
        return super.computeMinWidth(minWidth);
    }

    @Override protected double computeMinHeight(final double HEIGHT) {
        double minHeight;
        if (getPrefWidth() < getPrefHeight()) {
            // vertical
            minHeight = Math.max(MIN_SIZE.getHeight(), HEIGHT);
        } else {
            // horizontal
            minHeight = Math.max(MIN_SIZE.getWidth(), HEIGHT);
        }
        return super.computeMinHeight(minHeight);
    }

    @Override protected double computeMaxWidth(final double WIDTH) {
        double maxWidth;
        if (getPrefWidth() < getPrefHeight()) {
            // vertical
            maxWidth = Math.max(MAX_SIZE.getWidth(), WIDTH);
        } else {
            // horizontal
            maxWidth = Math.max(MAX_SIZE.getHeight(), WIDTH);
        }
        return super.computeMaxWidth(maxWidth);
    }

    @Override protected double computeMaxHeight(final double HEIGHT) {
        double maxHeight;
        if (getPrefWidth() < getPrefHeight()) {
            // vertical
            maxHeight = Math.max(MAX_SIZE.getHeight(), HEIGHT);
        } else {
            // horizontal
            maxHeight = Math.max(MAX_SIZE.getWidth(), HEIGHT);
        }
        return super.computeMaxHeight(maxHeight);
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
            final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
            gaugeBounds.setWidth(control.getPrefWidth() + SIZE * 0.168224299 + 2);
            gaugeBounds.setHeight(control.getPrefHeight() + SIZE * 0.168224299 + 2);
            framelessOffset = new Point2D(-SIZE * 0.0841121495 - 1, -SIZE * 0.0841121495 - 1);
        }
    }


    // ******************** Drawing *******************************************
    public void drawFrame() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        frame.getChildren().clear();

        final Rectangle SUBTRACT = new Rectangle(0.0841121495 * SIZE + 1, 0.0841121495 * SIZE + 1,
                                                 WIDTH - (2 * 0.0841121495 * SIZE) - 2, HEIGHT - (2 * 0.0841121495 * SIZE) - 2);
        SUBTRACT.setArcWidth(0.05 * SIZE);
        SUBTRACT.setArcHeight(0.05 * SIZE);

        final Rectangle OUTER_FRAME = new Rectangle(0.0, 0.0,
                                                    WIDTH, HEIGHT);
        OUTER_FRAME.setArcWidth(0.09333333333333334 * SIZE);
        OUTER_FRAME.setArcHeight(0.09333333333333334 * SIZE);
        OUTER_FRAME.setFill(Color.color(0.5176470588, 0.5176470588, 0.5176470588, 1));
        OUTER_FRAME.setStroke(null);
        frame.getChildren().add(OUTER_FRAME);

        final Rectangle MAIN_FRAME = new Rectangle(1, 1, WIDTH - 2, HEIGHT - 2);
        MAIN_FRAME.setArcWidth(0.08 * SIZE);
        MAIN_FRAME.setArcHeight(0.08 * SIZE);
        MAIN_FRAME.setStroke(null);

        final Rectangle INNER_FRAME = new Rectangle(0.0841121495 * SIZE, 0.0841121495 * SIZE,
                                                    WIDTH - (2 * 0.0841121495 * SIZE), HEIGHT - (2 * 0.0841121495 * SIZE));
        INNER_FRAME.setArcWidth(0.05 * SIZE);
        INNER_FRAME.setArcHeight(0.05 * SIZE);
        INNER_FRAME.setFill(Color.color(0.6, 0.6, 0.6, 0.8));
        INNER_FRAME.setStroke(null);


        final ImageView IMAGE_VIEW;
        switch (control.getFrameDesign()) {
            case BLACK_METAL:
                ConicalGradient bmGradient = new ConicalGradient(new Point2D(MAIN_FRAME.getLayoutBounds().getWidth() / 2,
                                                                             MAIN_FRAME.getLayoutBounds().getHeight() / 2),
                                                                 new Stop(0.0000, Color.rgb(254, 254, 254)),
                                                                 new Stop(0.1250, Color.rgb(0, 0, 0)),
                                                                 new Stop(0.3472, Color.rgb(153, 153, 153)),
                                                                 new Stop(0.5000, Color.rgb(0, 0, 0)),
                                                                 new Stop(0.6805, Color.rgb(153, 153, 153)),
                                                                 new Stop(0.8750, Color.rgb(0, 0, 0)),
                                                                 new Stop(1.0000, Color.rgb(254, 254, 254)));
                MAIN_FRAME.setFill(bmGradient.apply(MAIN_FRAME));
                MAIN_FRAME.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, INNER_FRAME);
                break;
            case SHINY_METAL:
                ConicalGradient smGradient = new ConicalGradient(new Point2D(MAIN_FRAME.getLayoutBounds().getWidth() / 2,
                                                                             MAIN_FRAME.getLayoutBounds().getHeight() / 2),
                                                                 new Stop(0.0000, Color.rgb(254, 254, 254)),
                                                                 new Stop(0.1250, Util.darker(control.getFrameBaseColor(), 0.15)),
                                                                 new Stop(0.2500, control.getFrameBaseColor().darker()),
                                                                 new Stop(0.3472, control.getFrameBaseColor().brighter()),
                                                                 new Stop(0.5000, control.getFrameBaseColor().darker().darker()),
                                                                 new Stop(0.6527, control.getFrameBaseColor().brighter()),
                                                                 new Stop(0.7500, control.getFrameBaseColor().darker()),
                                                                 new Stop(0.8750, Util.darker(control.getFrameBaseColor(), 0.15)),
                                                                 new Stop(1.0000, Color.rgb(254, 254, 254)));
                MAIN_FRAME.setFill(smGradient.apply(MAIN_FRAME));
                MAIN_FRAME.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, INNER_FRAME);
                break;
            case CHROME:
                ConicalGradient cmGradient = new ConicalGradient(new Point2D(MAIN_FRAME.getLayoutBounds().getWidth() / 2,
                                                                             MAIN_FRAME.getLayoutBounds().getHeight() / 2),
                                                                 new Stop(0.00, Color.WHITE),
                                                                 new Stop(0.09, Color.WHITE),
                                                                 new Stop(0.12, Color.rgb(136, 136, 138)),
                                                                 new Stop(0.16, Color.rgb(164, 185, 190)),
                                                                 new Stop(0.25, Color.rgb(158, 179, 182)),
                                                                 new Stop(0.29, Color.rgb(112, 112, 112)),
                                                                 new Stop(0.33, Color.rgb(221, 227, 227)),
                                                                 new Stop(0.38, Color.rgb(155, 176, 179)),
                                                                 new Stop(0.48, Color.rgb(156, 176, 177)),
                                                                 new Stop(0.52, Color.rgb(254, 255, 255)),
                                                                 new Stop(0.63, Color.WHITE),
                                                                 new Stop(0.68, Color.rgb(156, 180, 180)),
                                                                 new Stop(0.80, Color.rgb(198, 209, 211)),
                                                                 new Stop(0.83, Color.rgb(246, 248, 247)),
                                                                 new Stop(0.87, Color.rgb(204, 216, 216)),
                                                                 new Stop(0.97, Color.rgb(164, 188, 190)),
                                                                 new Stop(1.00, Color.WHITE));
                MAIN_FRAME.setFill(cmGradient.apply(MAIN_FRAME));
                MAIN_FRAME.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, INNER_FRAME);
                break;
            case GLOSSY_METAL:
                MAIN_FRAME.setFill(new LinearGradient(0.4714285714285714 * WIDTH, 0.014285714285714285 * HEIGHT,
                                                      0.47142857142857153 * WIDTH, 0.9785714285714285 * HEIGHT,
                                                      false, CycleMethod.NO_CYCLE,
                                                      new Stop(0.0, Color.color(0.9764705882, 0.9764705882, 0.9764705882, 1)),
                                                      new Stop(0.1, Color.color(0.7843137255, 0.7647058824, 0.7490196078, 1)),
                                                      new Stop(0.26, Color.WHITE),
                                                      new Stop(0.73, Color.color(0.1137254902, 0.1137254902, 0.1137254902, 1)),
                                                      new Stop(1.0, Color.color(0.8196078431, 0.8196078431, 0.8196078431, 1))));
                final Rectangle GLOSSY2 = new Rectangle(0.08571428571428572 * WIDTH, 0.08571428571428572 * HEIGHT,
                                                        0.8285714285714286 * WIDTH, 0.8285714285714286 * HEIGHT);
                GLOSSY2.setArcWidth(0.05714285714285714 * SIZE);
                GLOSSY2.setArcHeight(0.05714285714285714 * SIZE);
                GLOSSY2.setFill(new LinearGradient(0, GLOSSY2.getLayoutBounds().getMinY(),
                                                   0, GLOSSY2.getLayoutBounds().getMaxY(),
                                                   false, CycleMethod.NO_CYCLE,
                                                   new Stop(0.0, Color.color(0.9764705882, 0.9764705882, 0.9764705882, 1.0)),
                                                   new Stop(0.23, Color.color(0.7843137255, 0.7647058824, 0.7490196078, 1.0)),
                                                   new Stop(0.36, Color.color(1.0, 1.0, 1.0, 1.0)),
                                                   new Stop(0.59, Color.color(0.1137254902, 0.1137254902, 0.1137254902, 1.0)),
                                                   new Stop(0.76, Color.color(0.7843137255, 0.7607843137, 0.7529411765, 1.0)),
                                                   new Stop(1.0, Color.color(0.8196078431, 0.8196078431, 0.8196078431, 1.0))));
                final Rectangle GLOSSY3 = new Rectangle(INNER_FRAME.getX() - 2, INNER_FRAME.getY() - 2, INNER_FRAME.getWidth() + 4, INNER_FRAME.getHeight() + 4);
                GLOSSY3.setArcWidth(INNER_FRAME.getArcWidth() + 1);
                GLOSSY3.setArcHeight(INNER_FRAME.getArcHeight() + 1);
                GLOSSY3.setFill(Color.web("#F6F6F6"));
                final Rectangle GLOSSY4 = new Rectangle(GLOSSY3.getX() + 2, GLOSSY3.getY() + 2, GLOSSY3.getWidth() - 4, GLOSSY3.getHeight() - 4);
                GLOSSY4.setArcWidth(GLOSSY3.getArcWidth() - 1);
                GLOSSY4.setArcHeight(GLOSSY3.getArcHeight() - 1);
                GLOSSY4.setFill(Color.web("#333333"));
                frame.getChildren().addAll(MAIN_FRAME, GLOSSY2, GLOSSY3, GLOSSY4);
                break;
            case DARK_GLOSSY:
                MAIN_FRAME.setFill(new LinearGradient(0.8551401869158879 * WIDTH, 0.14953271028037382 * HEIGHT,
                                                      0.15794611761513314 * WIDTH, 0.8467267795811287 * HEIGHT,
                                                      false, CycleMethod.NO_CYCLE,
                                                      new Stop(0.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)),
                                                      new Stop(0.08, Color.color(0.9960784314, 0.9960784314, 1, 1)),
                                                      new Stop(0.52, Color.color(0, 0, 0, 1)),
                                                      new Stop(0.55, Color.color(0.0196078431, 0.0235294118, 0.0196078431, 1)),
                                                      new Stop(0.84, Color.color(0.9725490196, 0.9803921569, 0.9764705882, 1)),
                                                      new Stop(0.99, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)),
                                                      new Stop(1.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1))));
                final Rectangle DARK_GLOSSY2 = new Rectangle(0.08571428571428572 * WIDTH, 0.08571428571428572 * HEIGHT,
                                                             0.8285714285714286 * WIDTH, 0.8285714285714286 * HEIGHT);
                DARK_GLOSSY2.setArcWidth(0.05714285714285714 * SIZE);
                DARK_GLOSSY2.setArcHeight(0.05714285714285714 * SIZE);
                DARK_GLOSSY2.setFill(new LinearGradient(0.5 * WIDTH, 0.014018691588785047 * HEIGHT,
                                                        0.5 * WIDTH, 0.985981308411215 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.2588235294, 0.2588235294, 0.2588235294, 1)),
                                                        new Stop(0.42, Color.color(0.2588235294, 0.2588235294, 0.2588235294, 1)),
                                                        new Stop(1.0, Color.color(0.0509803922, 0.0509803922, 0.0509803922, 1))));
                DARK_GLOSSY2.setStroke(null);

                final Rectangle DARK_GLOSSY3 = new Rectangle(MAIN_FRAME.getX(), MAIN_FRAME.getY(),
                                                             MAIN_FRAME.getWidth(), MAIN_FRAME.getHeight() * 0.5);
                DARK_GLOSSY3.setArcWidth(MAIN_FRAME.getArcWidth());
                DARK_GLOSSY3.setArcHeight(MAIN_FRAME.getArcHeight());
                DARK_GLOSSY3.setFill(new LinearGradient(0.5 * WIDTH, 0.014018691588785047 * HEIGHT,
                                                        0.5 * WIDTH, 0.5280373831775701 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(1, 1, 1, 1)),
                                                        new Stop(0.26, Color.color(1, 1, 1, 1)),
                                                        new Stop(0.26009998, Color.color(1, 1, 1, 1)),
                                                        new Stop(1.0, Color.color(1, 1, 1, 0))));
                DARK_GLOSSY3.setStroke(null);

                final Rectangle DARK_GLOSSY4 = new Rectangle(INNER_FRAME.getX() - 2, INNER_FRAME.getY() - 2,
                                                             INNER_FRAME.getWidth() + 4, INNER_FRAME.getHeight() + 4);
                DARK_GLOSSY4.setArcWidth(INNER_FRAME.getArcWidth() + 1);
                DARK_GLOSSY4.setArcHeight(INNER_FRAME.getArcHeight() + 1);
                DARK_GLOSSY4.setFill(new LinearGradient(0.8037383177570093 * WIDTH, 0.1822429906542056 * HEIGHT,
                                                        0.18584594354259637 * WIDTH, 0.8001353648686187 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.6745098039, 0.6745098039, 0.6784313725, 1)),
                                                        new Stop(0.08, Color.color(0.9960784314, 0.9960784314, 1, 1)),
                                                        new Stop(0.52, Color.color(0, 0, 0, 1)),
                                                        new Stop(0.55, Color.color(0.0196078431, 0.0235294118, 0.0196078431, 1)),
                                                        new Stop(0.91, Color.color(0.9725490196, 0.9803921569, 0.9764705882, 1)),
                                                        new Stop(0.99, Color.color(0.6980392157, 0.6980392157, 0.6980392157, 1)),
                                                        new Stop(1.0, Color.color(0.6980392157, 0.6980392157, 0.6980392157, 1))));
                DARK_GLOSSY4.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, DARK_GLOSSY2, DARK_GLOSSY3, DARK_GLOSSY4);
                break;
            default:
                IMAGE_VIEW = new ImageView();
                IMAGE_VIEW.setVisible(false);
                MAIN_FRAME.getStyleClass().add(control.getFrameDesign().CSS);
                MAIN_FRAME.setStroke(null);
                frame.getChildren().addAll(MAIN_FRAME, INNER_FRAME);
                break;
        }
        frame.setCache(true);
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
        INNER_SHADOW.setOffsetY(0.03 * SIZE);
        INNER_SHADOW.setColor(Color.color(0, 0, 0, 0.7));
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final LinearGradient HL_GRADIENT = new LinearGradient(0, 0, WIDTH, 0, false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, Color.color(0.0, 0.0, 0.0, 0.6)),
                                                              new Stop(0.4, Color.color(0.0, 0.0, 0.0, 0.0)),
                                                              new Stop(0.6, Color.color(0.0, 0.0, 0.0, 0.0)),
                                                              new Stop(1.0, Color.color(0.0, 0.0, 0.0, 0.6)));

        final Rectangle BACKGROUND = new Rectangle(0.0841121495 * SIZE + 1, 0.0841121495 * SIZE + 1,
                                                   WIDTH - (2 * 0.0841121495 * SIZE) - 2, HEIGHT - (2 * 0.0841121495 * SIZE) - 2);
        BACKGROUND.setArcWidth(0.05 * SIZE);
        BACKGROUND.setArcHeight(0.05 * SIZE);
        BACKGROUND.setStroke(null);

        final Rectangle CLIP_SHAPE = new Rectangle(0.0841121495 * SIZE + 1, 0.0841121495 * SIZE + 1,
                                                   WIDTH - (2 * 0.0841121495 * SIZE) - 2, HEIGHT - (2 * 0.0841121495 * SIZE) - 2);
        CLIP_SHAPE.setArcWidth(0.05 * SIZE);
        CLIP_SHAPE.setArcHeight(0.05 * SIZE);

        switch (control.getBackgroundDesign()) {
            case STAINLESS:
                Color c1 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#FDFDFD").getBrightness());
                Color c2 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#E2E2E2").getBrightness());
                Color c3 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#B2B2B4").getBrightness());
                Color c4 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#ACACAE").getBrightness());
                Color c5 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#FDFDFD").getBrightness());
                Color c6 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#6E6E70").getBrightness());
                Color c7 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#6E6E70").getBrightness());
                Color c8 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#FDFDFD").getBrightness());
                Color c9 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#6E6E70").getBrightness());
                Color c10 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#6E6E70").getBrightness());
                Color c11 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#FDFDFD").getBrightness());
                Color c12 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#ACACAE").getBrightness());
                Color c13 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#B2B2B4").getBrightness());
                Color c14 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#E2E2E2").getBrightness());
                Color c15 = Color.hsb(control.getTextureColor().getHue(), control.getTextureColor().getSaturation(), Color.web("#FDFDFD").getBrightness());

                ConicalGradient gradient = new ConicalGradient(new Point2D(SIZE / 2, SIZE / 2),
                                                               new Stop(0.00, c1),
                                                               new Stop(0.03, c2),
                                                               new Stop(0.10, c3),
                                                               new Stop(0.14, c4),
                                                               new Stop(0.24, c5),
                                                               new Stop(0.33, c6),
                                                               new Stop(0.38, c7),
                                                               new Stop(0.50, c8),
                                                               new Stop(0.62, c9),
                                                               new Stop(0.67, c10),
                                                               new Stop(0.76, c11),
                                                               new Stop(0.81, c12),
                                                               new Stop(0.85, c13),
                                                               new Stop(0.97, c14),
                                                               new Stop(1.00, c15));
                BACKGROUND.setFill(gradient.apply(BACKGROUND));
                BACKGROUND.setEffect(INNER_SHADOW);
                background.getChildren().addAll(BACKGROUND);
                break;
            case CARBON:
                BACKGROUND.setFill(Util.createCarbonPattern());
                BACKGROUND.setStroke(null);
                final Rectangle SHADOW_OVERLAY1 = new Rectangle(0.0841121495 * SIZE + 1, 0.0841121495 * SIZE + 1,
                                                                WIDTH - (2 * 0.0841121495 * SIZE) - 2, HEIGHT - (2 * 0.0841121495 * SIZE) - 2);
                SHADOW_OVERLAY1.setArcWidth(0.05 * SIZE);
                SHADOW_OVERLAY1.setArcHeight(0.05 * SIZE);
                SHADOW_OVERLAY1.setStroke(null);
                SHADOW_OVERLAY1.setFill(new LinearGradient(SHADOW_OVERLAY1.getLayoutBounds().getMinX(), 0,
                                                     SHADOW_OVERLAY1.getLayoutBounds().getMaxX(), 0,
                                                     false, CycleMethod.NO_CYCLE,
                                                     new Stop(0.0, Color.color(0.0, 0.0, 0.0, 0.5)),
                                                     new Stop(0.4, Color.color(1.0, 1.0, 1.0, 0.0)),
                                                     new Stop(0.6, Color.color(1.0, 1.0, 1.0, 0.0)),
                                                     new Stop(1.0, Color.color(0.0, 0.0, 0.0, 0.5))));
                SHADOW_OVERLAY1.setStroke(null);
                background.getChildren().addAll(BACKGROUND, SHADOW_OVERLAY1);
                break;
            case PUNCHED_SHEET:
                BACKGROUND.setFill(Util.createPunchedSheetPattern(control.getTextureColor()));
                BACKGROUND.setStroke(null);
                final Rectangle SHADOW_OVERLAY2 = new Rectangle(0.0841121495 * SIZE + 1, 0.0841121495 * SIZE + 1,
                                                                WIDTH - (2 * 0.0841121495 * SIZE) - 2, HEIGHT - (2 * 0.0841121495 * SIZE) - 2);
                SHADOW_OVERLAY2.setArcWidth(0.05 * SIZE);
                SHADOW_OVERLAY2.setArcHeight(0.05 * SIZE);
                SHADOW_OVERLAY2.setFill(new LinearGradient(SHADOW_OVERLAY2.getLayoutBounds().getMinX(), 0,
                                                           SHADOW_OVERLAY2.getLayoutBounds().getMaxX(), 0,
                                                           false, CycleMethod.NO_CYCLE,
                                                           new Stop(0.0, Color.color(0.0, 0.0, 0.0, 0.5)),
                                                           new Stop(0.4, Color.color(1.0, 1.0, 1.0, 0.0)),
                                                           new Stop(0.6, Color.color(1.0, 1.0, 1.0, 0.0)),
                                                           new Stop(1.0, Color.color(0.0, 0.0, 0.0, 0.5))));

                SHADOW_OVERLAY2.setStroke(null);
                background.getChildren().addAll(BACKGROUND, SHADOW_OVERLAY2);
                break;
            case NOISY_PLASTIC:
                final Rectangle BACKGROUND_PLAIN = new Rectangle(0.0841121495 * SIZE + 1, 0.0841121495 * SIZE + 1,
                                                                 WIDTH - (2 * 0.0841121495 * SIZE) - 2, HEIGHT - (2 * 0.0841121495 * SIZE) - 2);
                BACKGROUND_PLAIN.setArcWidth(0.05 * SIZE);
                BACKGROUND_PLAIN.setArcHeight(0.05 * SIZE);
                BACKGROUND_PLAIN.setFill(new LinearGradient(0.0, BACKGROUND_PLAIN.getLayoutY(),
                                                            0.0, BACKGROUND_PLAIN.getLayoutBounds().getHeight(),
                                                            false, CycleMethod.NO_CYCLE,
                                                            new Stop(0.0, Util.brighter(control.getTextureColor(), 0.15)),
                                                            new Stop(1.0, Util.darker(control.getTextureColor(), 0.15))));
                BACKGROUND_PLAIN.setStroke(null);
                BACKGROUND_PLAIN.setEffect(INNER_SHADOW);
                BACKGROUND.setFill(Util.applyNoisyBackground(BACKGROUND, control.getTextureColor()));
                background.getChildren().addAll(BACKGROUND_PLAIN, BACKGROUND);
                break;
            case BRUSHED_METAL:
                BACKGROUND.setFill(Util.applyBrushedMetalBackground(BACKGROUND, control.getTextureColor()));
                BACKGROUND.setEffect(INNER_SHADOW);
                background.getChildren().addAll(BACKGROUND);
                break;
            default:
                BACKGROUND.setStyle(control.getSimpleGradientBaseColorString());
                BACKGROUND.getStyleClass().add(control.getBackgroundDesign().CSS_BACKGROUND);
                BACKGROUND.setEffect(INNER_SHADOW);
                background.getChildren().addAll(BACKGROUND);
                break;
        }

        // bar background
        if (WIDTH <= HEIGHT) {
            // vertical
            final Rectangle BAR_BACKGROUND = new Rectangle(0.42 * WIDTH + 2, 0.1657142857142857 * HEIGHT, 0.16 * WIDTH - 3, 0.6714285714 * HEIGHT);
            final Paint BACKGROUND_1_VALUE_BACKGROUND_1_2_FILL = new LinearGradient(0.5 * WIDTH, 0.1657142857142857 * HEIGHT,
                                                                                    0.5 * WIDTH, 0.8371428571428572 * HEIGHT,
                                                                                    false, CycleMethod.NO_CYCLE,
                                                                                    new Stop(0.0, Color.color(1, 1, 1, 0.0470588235)),
                                                                                    new Stop(0.5, Color.color(1, 1, 1, 0.1490196078)),
                                                                                    new Stop(1.0, Color.color(1, 1, 1, 0.0470588235)));
            BAR_BACKGROUND.setFill(BACKGROUND_1_VALUE_BACKGROUND_1_2_FILL);
            BAR_BACKGROUND.setStroke(null);

            final Rectangle BAR_LEFT_BORDER = new Rectangle(0.42 * WIDTH, 0.1657142857142857 * HEIGHT, 1, 0.6714285714 * HEIGHT);
            final Paint BACKGROUND_1_VALUE_LEFT_BORDER_2_3_FILL = new LinearGradient(BAR_LEFT_BORDER.getLayoutBounds().getMinX(), BAR_LEFT_BORDER.getLayoutBounds().getMinY(),
                                                                                     BAR_LEFT_BORDER.getLayoutBounds().getMinX(), BAR_LEFT_BORDER.getLayoutBounds().getMaxY(),
                                                                                     false, CycleMethod.NO_CYCLE,
                                                                                     new Stop(0.0, Color.color(1, 1, 1, 0.2901960784)),
                                                                                     new Stop(0.5, Color.color(1, 1, 1, 0.3450980392)),
                                                                                     new Stop(1.0, Color.color(1, 1, 1, 0.4)));
            BAR_LEFT_BORDER.setFill(BACKGROUND_1_VALUE_LEFT_BORDER_2_3_FILL);
            BAR_LEFT_BORDER.setStroke(null);

            final Rectangle BAR_RIGHT_BORDER = new Rectangle(0.58 * WIDTH, 0.1657142857142857 * HEIGHT, 1, 0.6714285714 * HEIGHT);
            final Paint BACKGROUND_1_VALUE_RIGHT_BORDER_3_4_FILL = new LinearGradient(BAR_RIGHT_BORDER.getLayoutBounds().getMinX(), BAR_RIGHT_BORDER.getLayoutBounds().getMinY(),
                                                                                      BAR_RIGHT_BORDER.getLayoutBounds().getMinX(), BAR_RIGHT_BORDER.getLayoutBounds().getMaxY(),
                                                                                      false, CycleMethod.NO_CYCLE,
                                                                                      new Stop(0.0, Color.color(1, 1, 1, 0.2901960784)),
                                                                                      new Stop(0.5, Color.color(1, 1, 1, 0.3450980392)),
                                                                                      new Stop(1.0, Color.color(1, 1, 1, 0.4)));
            BAR_RIGHT_BORDER.setFill(BACKGROUND_1_VALUE_RIGHT_BORDER_3_4_FILL);
            BAR_RIGHT_BORDER.setStroke(null);
            background.getChildren().addAll(BAR_BACKGROUND, BAR_LEFT_BORDER, BAR_RIGHT_BORDER);
        } else {
            // horizontal
            final Rectangle BAR_BACKGROUND = new Rectangle(0.1657142857142857 * WIDTH, 0.42 * HEIGHT + 2, 0.6714285714 * WIDTH, 0.16 * HEIGHT - 3);
            final Paint BACKGROUND_1_VALUE_BACKGROUND_1_2_FILL = new LinearGradient(0.1657142857142857 * WIDTH, 0.5 * HEIGHT,
                                                                                    0.8371428571428572 * WIDTH, 0.5 * HEIGHT,
                                                                                    false, CycleMethod.NO_CYCLE,
                                                                                    new Stop(0.0, Color.color(1, 1, 1, 0.0470588235)),
                                                                                    new Stop(0.5, Color.color(1, 1, 1, 0.1490196078)),
                                                                                    new Stop(1.0, Color.color(1, 1, 1, 0.0470588235)));
            BAR_BACKGROUND.setFill(BACKGROUND_1_VALUE_BACKGROUND_1_2_FILL);
            BAR_BACKGROUND.setStroke(null);

            final Rectangle BAR_LEFT_BORDER = new Rectangle(0.1657142857142857 * WIDTH, 0.42 * HEIGHT, 0.6714285714 * WIDTH, 1);
            final Paint BACKGROUND_1_VALUE_LEFT_BORDER_2_3_FILL = new LinearGradient(BAR_LEFT_BORDER.getLayoutBounds().getMinX(), BAR_LEFT_BORDER.getLayoutBounds().getMinY(),
                                                                                     BAR_LEFT_BORDER.getLayoutBounds().getMaxX(), BAR_LEFT_BORDER.getLayoutBounds().getMinY(),
                                                                                     false, CycleMethod.NO_CYCLE,
                                                                                     new Stop(0.0, Color.color(1, 1, 1, 0.2901960784)),
                                                                                     new Stop(0.5, Color.color(1, 1, 1, 0.3450980392)),
                                                                                     new Stop(1.0, Color.color(1, 1, 1, 0.4)));
            BAR_LEFT_BORDER.setFill(BACKGROUND_1_VALUE_LEFT_BORDER_2_3_FILL);
            BAR_LEFT_BORDER.setStroke(null);

            final Rectangle BAR_RIGHT_BORDER = new Rectangle(0.1657142857142857 * WIDTH, 0.58 * HEIGHT, 0.6714285714 * WIDTH, 1);
            final Paint BACKGROUND_1_VALUE_RIGHT_BORDER_3_4_FILL = new LinearGradient(BAR_RIGHT_BORDER.getLayoutBounds().getMinX(), BAR_RIGHT_BORDER.getLayoutBounds().getMinY(),
                                                                                      BAR_RIGHT_BORDER.getLayoutBounds().getMaxX(), BAR_RIGHT_BORDER.getLayoutBounds().getMinY(),
                                                                                      false, CycleMethod.NO_CYCLE,
                                                                                      new Stop(0.0, Color.color(1, 1, 1, 0.2901960784)),
                                                                                      new Stop(0.5, Color.color(1, 1, 1, 0.3450980392)),
                                                                                      new Stop(1.0, Color.color(1, 1, 1, 0.4)));
            BAR_RIGHT_BORDER.setFill(BACKGROUND_1_VALUE_RIGHT_BORDER_3_4_FILL);
            BAR_RIGHT_BORDER.setStroke(null);
            background.getChildren().addAll(BAR_BACKGROUND, BAR_LEFT_BORDER, BAR_RIGHT_BORDER);
        }
        background.setCache(true);
    }

    public void drawTitleAndUnit() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        titleAndUnit.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);

        final Font TITLE_FONT = Font.font(control.getTitleFont(), FontWeight.NORMAL, (0.08 * SIZE));
        final Text title = new Text();
        title.setTextOrigin(VPos.BOTTOM);
        title.setFont(TITLE_FONT);
        title.setText(control.getTitle());
        if (WIDTH <= HEIGHT) {
            // vertical
            title.setX(((WIDTH - title.getLayoutBounds().getWidth()) / 2.0));
            title.setY(0.0657142857 * HEIGHT + title.getLayoutBounds().getHeight());
        } else {
            // horizontal
            title.setX(0.0628571429 * WIDTH);
            title.setY(0.1533333333 * HEIGHT + title.getLayoutBounds().getHeight());
        }
        title.getStyleClass().add(control.getBackgroundDesign().CSS_TEXT);

        final Font UNIT_FONT = Font.font(control.getUnitFont(), FontWeight.NORMAL, (0.0666666667 * SIZE));
        final Text unit = new Text();
        unit.setTextOrigin(VPos.BOTTOM);
        unit.setFont(UNIT_FONT);
        unit.setText(control.getUnit());
        if (WIDTH <= HEIGHT) {
            // vertical
            unit.setX((WIDTH - unit.getLayoutBounds().getWidth()) / 2.0);
            unit.setY(0.1085714286 * HEIGHT + unit.getLayoutBounds().getHeight());
        } else {
            // horizontal
            unit.setX(0.0628571429 * WIDTH);
            unit.setY(0.2666666667 * HEIGHT + unit.getLayoutBounds().getHeight());
        }
        unit.getStyleClass().add(control.getBackgroundDesign().CSS_TEXT);

        titleAndUnit.getChildren().addAll(IBOUNDS, title, unit);
        titleAndUnit.setCache(true);
    }

    public void drawGlowOff() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        glowOff.getChildren().clear();

        final Rectangle BACKGROUND = new Rectangle(0.0841121495 * SIZE + 1, 0.0841121495 * SIZE + 1,
                                                   WIDTH - (2 * 0.0841121495 * SIZE) - 2, HEIGHT - (2 * 0.0841121495 * SIZE) - 2);
        BACKGROUND.setArcWidth(0.05 * SIZE);
        BACKGROUND.setArcHeight(0.05 * SIZE);
        BACKGROUND.setStroke(null);

        final Rectangle SUBTRACT = new Rectangle(0.11 * SIZE + 1, 0.11 * SIZE + 1,
                                                 WIDTH - (2 * 0.11 * SIZE) - 2, HEIGHT - (2 * 0.11 * SIZE) - 2);
        SUBTRACT.setArcWidth(0.045 * SIZE);
        SUBTRACT.setArcHeight(0.045 * SIZE);

        final Shape GLOW_RING = Shape.subtract(BACKGROUND, SUBTRACT);

        final Paint GLOW_OFF_PAINT = new LinearGradient(0.5 * WIDTH, 0.08411214953271028 * HEIGHT,
            0.5 * WIDTH, 0.9112149532710281 * HEIGHT,
            false,
            CycleMethod.NO_CYCLE,
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
        glowOff.getChildren().addAll(GLOW_RING);
        glowOff.setCache(true);
    }

    public void drawGlowOn() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        glowOn.getChildren().clear();

        final Rectangle BACKGROUND = new Rectangle(0.0841121495 * SIZE + 1, 0.0841121495 * SIZE + 1,
                                                           WIDTH - (2 * 0.0841121495 * SIZE) - 2, HEIGHT - (2 * 0.0841121495 * SIZE) - 2);
        BACKGROUND.setArcWidth(0.05 * SIZE);
        BACKGROUND.setArcHeight(0.05 * SIZE);
        BACKGROUND.setStroke(null);

        final Rectangle SUBTRACT = new Rectangle(0.11 * SIZE + 1, 0.11 * SIZE + 1,
                                                 WIDTH - (2 * 0.11 * SIZE) - 2, HEIGHT - (2 * 0.11 * SIZE) - 2);
        SUBTRACT.setArcWidth(0.045 * SIZE);
        SUBTRACT.setArcHeight(0.045 * SIZE);

        final Shape GLOW_RING = Shape.subtract(BACKGROUND, SUBTRACT);

        GLOW_RING.setFill(control.getGlowColor());
        GLOW_RING.setStroke(null);

        final DropShadow GLOW_EFFECT = new DropShadow();
        GLOW_EFFECT.setRadius(0.15 * SIZE);
        GLOW_EFFECT.setBlurType(BlurType.GAUSSIAN);
        if (GLOW_EFFECT.colorProperty().isBound()) {
            GLOW_EFFECT.colorProperty().unbind();
        }
        GLOW_EFFECT.colorProperty().bind(control.glowColorProperty());
        GLOW_RING.effectProperty().set(GLOW_EFFECT);

        glowOn.getChildren().addAll(GLOW_RING);
        glowOn.setCache(true);
    }

    public void drawIndicators() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        markers.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        markers.getChildren().add(IBOUNDS);
        markers.getTransforms().clear();

        for (final Marker marker : control.getMarkers()) {
            final Group ARROW;
            if (WIDTH <= HEIGHT) {
                // vertical
                ARROW = createIndicator(SIZE, marker, new Point2D(0.59 * WIDTH, 0.8345 * HEIGHT - SIZE * 0.0210280374 - Math.abs(marker.getValue()) * stepsize));
                ARROW.setRotate(90);
            } else {
                // horizontal
                ARROW = createIndicator(SIZE, marker, new Point2D(0.1657142857142857 * WIDTH - SIZE * 0.0210280374 + Math.abs(marker.getValue()) * stepsize, 0.36 * HEIGHT));
            }
            markers.getChildren().add(ARROW);
        }
    }

    public void drawThreshold() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        threshold.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);

        final Path THRESHOLD = new Path();
        THRESHOLD.setFillRule(FillRule.EVEN_ODD);
        if (WIDTH <= HEIGHT) {
            // vertical
            THRESHOLD.getElements().add(new MoveTo(0.3673333333 * WIDTH, 0.8371428571 * HEIGHT));
            THRESHOLD.getElements().add(new LineTo(0.414 * WIDTH, 0.8471428571 * HEIGHT));
            THRESHOLD.getElements().add(new LineTo(0.414 * WIDTH, 0.8271428571 * HEIGHT));
            THRESHOLD.getElements().add(new ClosePath());
            threshold.setTranslateY(-Math.abs(control.getThreshold() - control.getMinValue()) * stepsize);
        } else {
            // horizontal
            THRESHOLD.getElements().add(new MoveTo(0.1657142857 * WIDTH, 0.645 * HEIGHT));
            THRESHOLD.getElements().add(new LineTo(0.1757142857 * WIDTH, 0.6 * HEIGHT));
            THRESHOLD.getElements().add(new LineTo(0.1557142857 * WIDTH, 0.6 * HEIGHT));
            THRESHOLD.getElements().add(new ClosePath());
            threshold.setTranslateX(Math.abs(control.getThreshold() - control.getMinValue()) * stepsize);
        }
        THRESHOLD.setStrokeType(StrokeType.CENTERED);
        THRESHOLD.setStrokeLineCap(StrokeLineCap.ROUND);
        THRESHOLD.setStrokeLineJoin(StrokeLineJoin.ROUND);
        THRESHOLD.setStrokeWidth(0.005 * SIZE);
        THRESHOLD.getStyleClass().add("root");
        THRESHOLD.setStyle(control.getThresholdColor().CSS);
        THRESHOLD.getStyleClass().add("threshold-gradient");

        threshold.getChildren().addAll(IBOUNDS, THRESHOLD);
    }

    public void drawMinMeasuredIndicator() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        minMeasured.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        minMeasured.getChildren().add(IBOUNDS);
        final Path MARKER = createTriangleShape(SIZE * 0.032, SIZE * 0.038, true);
        MARKER.setFill(Color.color(0.0, 0.0, 0.8, 1.0));
        MARKER.setStroke(null);

        if (WIDTH <= HEIGHT) {
            // vertical
            MARKER.setRotate(-90);
            MARKER.setTranslateX(0.335 * WIDTH);
            MARKER.setTranslateY(0.8345 * HEIGHT - SIZE * 0.014);
        } else {
            // horizontal
            MARKER.setRotate(180);
            MARKER.setTranslateX(0.1657142857142857 * WIDTH - SIZE * 0.016);
            MARKER.setTranslateY(0.65 * HEIGHT);
        }
        minMeasured.getChildren().add(MARKER);
    }

    public void drawMaxMeasuredIndicator() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        maxMeasured.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        maxMeasured.getChildren().add(IBOUNDS);

        final Path MARKER = createTriangleShape(SIZE * 0.032, SIZE * 0.038, true);
        MARKER.setFill(Color.color(0.8, 0.0, 0.0, 1.0));
        MARKER.setStroke(null);

        if (WIDTH <= HEIGHT) {
            // vertical
            MARKER.setRotate(-90);
            MARKER.setTranslateX(0.335 * WIDTH);
            MARKER.setTranslateY(0.8345 * HEIGHT - SIZE * 0.014);
        } else {
            // horizontal
            MARKER.setRotate(180);
            MARKER.setTranslateX(0.1657142857142857 * WIDTH - SIZE * 0.016);
            MARKER.setTranslateY(0.65 * HEIGHT);
        }
        maxMeasured.getChildren().add(MARKER);
    }

    public void drawBar() {
        //final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        bar.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        bar.getChildren().add(IBOUNDS);

        final Rectangle BAR_BOUNDS;
        final double MIN_POSITION;
        final double MAX_POSITION;
        final double START_POSITION;
        if (WIDTH <= HEIGHT) {
            // vertical
            BAR_BOUNDS = new Rectangle(0.42 * WIDTH + 3, 0.1657142857142857 * HEIGHT, 0.16 * WIDTH - 5, 0.6714285714 * HEIGHT);
            MIN_POSITION = BAR_BOUNDS.getLayoutBounds().getMaxY();
            MAX_POSITION = BAR_BOUNDS.getLayoutBounds().getMinY();
            stepsize = Math.abs(BAR_BOUNDS.getLayoutBounds().getHeight() / control.getRange());
            START_POSITION = control.getMinValue() > 0 ? MIN_POSITION : (control.getMaxValue() < 0 ? MAX_POSITION : MIN_POSITION + (control.getMinValue() * stepsize));
            currentBar = new Rectangle(BAR_BOUNDS.getLayoutBounds().getMinX(), START_POSITION,
                                       BAR_BOUNDS.getLayoutBounds().getWidth(), Math.abs(currentValue.get()) * stepsize);
            if (currentValue.get() > 0) {
                currentBar.setTranslateY(-Math.abs(currentValue.get()) * stepsize);
            }
            currentBar.getStyleClass().add("root");
            currentBar.setStyle("-fx-value: " + control.getValueColor().CSS);
            currentBar.getStyleClass().add("bar-vertical-solid");
            currentBarHl = new Rectangle(currentBar.getLayoutBounds().getMinX(), currentBar.getLayoutBounds().getMinY(),
                                         currentBar.getLayoutBounds().getWidth(), currentBar.getLayoutBounds().getHeight());
            currentBarHl.getStyleClass().add("bar-vertical-highlight");
        } else {
            // horizontal
            BAR_BOUNDS = new Rectangle(0.1657142857142857 * WIDTH, 0.42 * HEIGHT + 3, 0.6714285714 * WIDTH, 0.16 * HEIGHT - 5);
            MIN_POSITION = BAR_BOUNDS.getLayoutBounds().getMinX();
            MAX_POSITION = BAR_BOUNDS.getLayoutBounds().getMaxX();
            stepsize = BAR_BOUNDS.getLayoutBounds().getWidth() / control.getRange();
            START_POSITION = control.getMinValue() > 0 ? MIN_POSITION : (control.getMaxValue() < 0 ? MAX_POSITION : MIN_POSITION + Math.abs(control.getMinValue() * stepsize));
            currentBar = new Rectangle(START_POSITION, BAR_BOUNDS.getLayoutBounds().getMinY(),
                                       Math.abs(currentValue.get()) * stepsize, BAR_BOUNDS.getLayoutBounds().getHeight());
            if (currentValue.get() < 0) {
                currentBar.setTranslateX(currentValue.get() * stepsize);
            }
            currentBar.getStyleClass().add("root");
            currentBar.setStyle("-fx-value: " + control.getValueColor().CSS);
            currentBar.getStyleClass().add("bar-horizontal-solid");
            currentBarHl = new Rectangle(currentBar.getLayoutBounds().getMinX(), currentBar.getLayoutBounds().getMinY(),
                                         currentBar.getLayoutBounds().getWidth(), currentBar.getLayoutBounds().getHeight());
            currentBarHl.getStyleClass().add("bar-horizontal-highlight");
        }
        currentBar.setStroke(null);
        currentBarHl.setStroke(null);

        bar.getChildren().addAll(currentBar, currentBarHl);
    }

    public void updateBar() {
        final double WIDTH    = gaugeBounds.getWidth();
        final double HEIGHT   = gaugeBounds.getHeight();
        final double barValue = currentValue.get();
        if (WIDTH <= HEIGHT) {
            // vertical
            currentBar.setHeight(Math.abs(barValue) * stepsize);
            currentBarHl.setHeight(Math.abs(barValue) * stepsize);
            currentBar.setTranslateY(0);
            currentBarHl.setTranslateY(0);
            if (Double.compare(barValue, 0) >= 0) {
                currentBar.setTranslateY(-barValue * stepsize);
                currentBarHl.setTranslateY(-barValue * stepsize);
            }
        } else {
            // horizontal
            currentBar.setWidth(Math.abs(barValue) * stepsize);
            currentBarHl.setWidth(Math.abs(barValue) * stepsize);
            currentBar.setTranslateX(0);
            currentBarHl.setTranslateX(0);
            if (Double.compare(barValue, 0) <= 0) {
                currentBar.setTranslateX(-Math.abs(barValue) * stepsize);
                currentBarHl.setTranslateX(-Math.abs(barValue) * stepsize);
            }
        }
    }

    public void drawBarGraph() {
        //final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        final double LED_WIDTH;
        final double LED_HEIGHT;
        if (WIDTH <= HEIGHT) {
            // vertical
            LED_WIDTH = 0.16 * WIDTH - 5;
            LED_HEIGHT = 0.0085714286 * HEIGHT;
        } else {
            // horizontal
            LED_WIDTH = 0.0085714286 * WIDTH;
            LED_HEIGHT = 0.16 * HEIGHT - 5;
        }
    }

    public void drawLed() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        //OFF
        ledOff.getChildren().clear();
        final Shape IBOUNDS_OFF = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS_OFF.setOpacity(0.0);
        IBOUNDS_OFF.setStroke(null);
        final Group LED_OFF = createLed(SIZE * 0.14, control.getLedColor(), false);
        if (WIDTH <= HEIGHT) {
            // vertical
            LED_OFF.setLayoutX(WIDTH * 0.68);
            LED_OFF.setLayoutY(HEIGHT * 0.10);
        } else {
            // horizontal
            LED_OFF.setLayoutX(WIDTH * 0.87);
            LED_OFF.setLayoutY(HEIGHT * 0.5 - (LED_OFF.getLayoutBounds().getHeight()));
        }
        ledOff.getChildren().addAll(IBOUNDS_OFF, LED_OFF);
        ledOff.setCache(true);

        // ON
        ledOn.getChildren().clear();
        final Shape IBOUNDS_ON = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS_ON.setOpacity(0.0);
        IBOUNDS_ON.setStroke(null);
        final Group LED_ON = createLed(SIZE * 0.14, control.getLedColor(), true);
        if (WIDTH <= HEIGHT) {
            // vertical
            LED_ON.setLayoutX(WIDTH * 0.68);
            LED_ON.setLayoutY(HEIGHT * 0.10);
        } else {
            // horizontal
            LED_ON.setLayoutX(WIDTH * 0.87);
            LED_ON.setLayoutY(HEIGHT * 0.5 - (LED_OFF.getLayoutBounds().getHeight()));
        }
        ledOn.getChildren().addAll(IBOUNDS_ON, LED_ON);
        ledOn.setCache(true);
    }

    public void drawUserLed() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        //OFF
        userLedOff.getChildren().clear();
        final Shape IBOUNDS_OFF = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS_OFF.setOpacity(0.0);
        IBOUNDS_OFF.setStroke(null);
        final Group LED_OFF = createLed(SIZE * 0.14, control.getUserLedColor(), false);
        if (WIDTH <= HEIGHT) {
            // vertical
            LED_OFF.setLayoutX(WIDTH * 0.1933333333);
            LED_OFF.setLayoutY(HEIGHT * 0.10);
        } else {
            // horizontal
            LED_OFF.setLayoutX(WIDTH * 0.0828571429);
            LED_OFF.setLayoutY(HEIGHT * 0.5 - (LED_OFF.getLayoutBounds().getHeight()));
        }
        userLedOff.getChildren().addAll(IBOUNDS_OFF, LED_OFF);

        // ON
        userLedOn.getChildren().clear();
        final Shape IBOUNDS_ON = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS_ON.setOpacity(0.0);
        IBOUNDS_ON.setStroke(null);
        final Group LED_ON = createLed(SIZE * 0.14, control.getUserLedColor(), true);
        if (WIDTH <= HEIGHT) {
            // vertical
            LED_ON.setLayoutX(WIDTH * 0.1933333333);
            LED_ON.setLayoutY(HEIGHT * 0.10);
        } else {
            // horizontal
            LED_ON.setLayoutX(WIDTH * 0.0828571429);
            LED_ON.setLayoutY(HEIGHT * 0.5 - (LED_OFF.getLayoutBounds().getHeight()));
        }
        userLedOn.getChildren().addAll(IBOUNDS_ON, LED_ON);
    }

    public void drawLcd() {
        //final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        lcd.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        lcd.getChildren().addAll(IBOUNDS);
        final Rectangle LCD_FRAME;
        if (WIDTH <= HEIGHT) {
            // vertical
            LCD_FRAME = new Rectangle(0.16666666666666666 * WIDTH, 0.8571428571428571 * HEIGHT,
                                       0.6666666666666666 * WIDTH, 0.07142857142857142 * HEIGHT);
        } else {
            // horizontal
            LCD_FRAME = new Rectangle(0.6457142857142857 * WIDTH, 0.17333333333333334 * HEIGHT,
                                                       0.28 * WIDTH, 0.15333333333333332 * HEIGHT);
        }
        final double LCD_FRAME_CORNER_RADIUS = LCD_FRAME.getWidth() > LCD_FRAME.getHeight() ? (LCD_FRAME.getHeight() * 0.15) : (LCD_FRAME.getWidth() * 0.15);
        LCD_FRAME.setArcWidth(LCD_FRAME_CORNER_RADIUS);
        LCD_FRAME.setArcHeight(LCD_FRAME_CORNER_RADIUS);
        final LinearGradient LCD_FRAME_FILL = new LinearGradient(0, LCD_FRAME.getLayoutBounds().getMinY(),
                                                                 0, LCD_FRAME.getLayoutBounds().getMaxY(),
                                                                 false, CycleMethod.NO_CYCLE,
                                                                 new Stop(0.0, Color.color(0.1, 0.1, 0.1, 1.0)),
                                                                 new Stop(0.1, Color.color(0.3, 0.3, 0.3, 1.0)),
                                                                 new Stop(0.93, Color.color(0.3, 0.3, 0.3, 1.0)),
                                                                 new Stop(1.0, Color.color(0.86, 0.86, 0.86, 1.0)));
        LCD_FRAME.setFill(LCD_FRAME_FILL);
        LCD_FRAME.setStroke(null);
        final Rectangle LCD_MAIN = new Rectangle(LCD_FRAME.getX() + 1, LCD_FRAME.getY() + 1, LCD_FRAME.getWidth() - 2, LCD_FRAME.getHeight() - 2);
        final double LCD_MAIN_CORNER_RADIUS = LCD_FRAME.getArcWidth() - 1;
        LCD_MAIN.setArcWidth(LCD_MAIN_CORNER_RADIUS);
        LCD_MAIN.setArcHeight(LCD_MAIN_CORNER_RADIUS);
        LCD_MAIN.getStyleClass().add("lcd");
        LCD_MAIN.setStyle(control.getLcdDesign().CSS);
        LCD_MAIN.getStyleClass().add("lcd-main");
        LCD_MAIN.setStroke(null);

        final InnerShadow INNER_GLOW = new InnerShadow();
        INNER_GLOW.setWidth(0.25 * LCD_FRAME.getHeight());
        INNER_GLOW.setHeight(0.25 * LCD_FRAME.getHeight());
        INNER_GLOW.setOffsetY(-0.05 * LCD_FRAME.getHeight());
        INNER_GLOW.setOffsetX(0.0);
        INNER_GLOW.setColor(Color.color(1, 1, 1, 0.2));

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setInput(INNER_GLOW);
        INNER_SHADOW.setWidth(0.15 * LCD_FRAME.getHeight());
        INNER_SHADOW.setHeight(0.15 * LCD_FRAME.getHeight());
        INNER_SHADOW.setOffsetY(0.025 * LCD_FRAME.getHeight());
        INNER_SHADOW.setColor(Color.color(0, 0, 0, 0.65));
        LCD_MAIN.setEffect(INNER_SHADOW);

        lcd.getChildren().addAll(LCD_FRAME, LCD_MAIN);
        lcd.setCache(true);
    }

    public void drawLcdContent() {
        //final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        lcdContent.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);

        final Rectangle LCD_FRAME;
        if (WIDTH <= HEIGHT) {
            // vertical
            LCD_FRAME = new Rectangle(0.16666666666666666 * WIDTH, 0.8571428571428571 * HEIGHT,
                                       0.6666666666666666 * WIDTH, 0.07142857142857142 * HEIGHT);
        } else {
            // horizontal
            LCD_FRAME = new Rectangle(0.6457142857142857 * WIDTH, 0.17333333333333334 * HEIGHT,
                                       0.28 * WIDTH, 0.15333333333333332 * HEIGHT);
        }

        final Font LCD_UNIT_FONT = Font.font(control.getLcdUnitFont(), FontWeight.NORMAL, (0.4 * LCD_FRAME.getLayoutBounds().getHeight()));
        final Font LCD_VALUE_FONT;
        final double UNIT_Y_OFFSET;

        switch(control.getLcdValueFont()) {
            case LCD:
                LCD_VALUE_FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/digital.ttf"), (0.75 * LCD_FRAME.getLayoutBounds().getHeight()));
                UNIT_Y_OFFSET = 1.5;
                break;
            case BUS:
                LCD_VALUE_FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/bus.otf"), (0.6 * LCD_FRAME.getLayoutBounds().getHeight()));
                UNIT_Y_OFFSET = 2.0;
                break;
            case PIXEL:
                LCD_VALUE_FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/pixel.ttf"), (0.6 * LCD_FRAME.getLayoutBounds().getHeight()));
                UNIT_Y_OFFSET = 2.0;
                break;
            case PHONE_LCD:
                LCD_VALUE_FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/phonelcd.ttf"), (0.6 * LCD_FRAME.getLayoutBounds().getHeight()));
                UNIT_Y_OFFSET = 2.0;
                break;
            case STANDARD:
            default:
                LCD_VALUE_FONT = Font.font("Verdana", FontWeight.NORMAL, (0.6 * LCD_FRAME.getLayoutBounds().getHeight()));
                UNIT_Y_OFFSET = 2.0;
                break;
        }
        lcdValueString.setFont(LCD_VALUE_FONT);
        lcdUnitString.setFont(LCD_UNIT_FONT);

        // Unit
        lcdUnitString.setText(control.isLcdValueCoupled() ? control.getUnit() : control.getLcdUnit());
        lcdUnitString.setTextOrigin(VPos.BOTTOM);
        lcdUnitString.setTextAlignment(TextAlignment.RIGHT);
        if (lcdUnitString.visibleProperty().isBound()) {
            lcdUnitString.visibleProperty().unbind();
        }
        lcdUnitString.visibleProperty().bind(control.lcdUnitVisibleProperty());
        if (control.isLcdUnitVisible()) {
            lcdUnitString.setX(LCD_FRAME.getX() + (LCD_FRAME.getWidth() - lcdUnitString.getLayoutBounds().getWidth()) - LCD_FRAME.getHeight() * 0.0625);
            lcdUnitString.setY(LCD_FRAME.getY() + (LCD_FRAME.getHeight() + lcdValueString.getLayoutBounds().getHeight()) / UNIT_Y_OFFSET - (lcdValueString.getLayoutBounds().getHeight() * 0.05));
        }
        lcdUnitString.getStyleClass().add("lcd");
        lcdUnitString.setStyle(control.getLcdDesign().CSS);
        lcdUnitString.getStyleClass().add("lcd-text");
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
        lcdValueString.getStyleClass().add("lcd-text");
        lcdValueString.setStroke(null);

        lcdContent.getChildren().addAll(IBOUNDS, lcdUnitString, lcdValueString);
    }

    public void drawTickmarks() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        tickmarks.getChildren().clear();
        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        tickmarks.getChildren().add(IBOUNDS);

        // Adjust the number format of the ticklabels
        final NumberFormat numberFormat;
        if (control.getTickLabelNumberFormat() == NumberFormat.AUTO) {
            if (Math.abs(control.getMajorTickSpacing()) > 1000) {
                numberFormat = NumberFormat.SCIENTIFIC;
            } else if (control.getMajorTickSpacing() % 1.0 != 0) {
                numberFormat = NumberFormat.FRACTIONAL;
            } else {
                numberFormat = NumberFormat.STANDARD;
            }
        } else {
            numberFormat = control.getTickLabelNumberFormat();
        }

        // Definitions
        final Font STD_FONT = Font.font("Verdana", FontWeight.NORMAL, (0.06 * SIZE));
        final double MINOR_TICK_LENGTH = (0.022 * SIZE);
        final double MEDIUM_TICK_LENGTH = (0.035 * SIZE);
        final double MAJOR_TICK_LENGTH = (0.05 * SIZE);
        Point2D textPoint;
        Point2D innerPoint;
        Point2D outerPoint;
        final Path MAJOR_TICKMARKS_PATH = new Path();
        MAJOR_TICKMARKS_PATH.setFillRule(FillRule.EVEN_ODD);
        MAJOR_TICKMARKS_PATH.setSmooth(true);
        MAJOR_TICKMARKS_PATH.strokeTypeProperty().set(StrokeType.CENTERED);
        MAJOR_TICKMARKS_PATH.strokeLineCapProperty().set(StrokeLineCap.ROUND);
        MAJOR_TICKMARKS_PATH.strokeLineJoinProperty().set(StrokeLineJoin.BEVEL);
        MAJOR_TICKMARKS_PATH.strokeWidthProperty().set(0.0045 * SIZE);

        final Path MEDIUM_TICKMARKS_PATH = new Path();
        MEDIUM_TICKMARKS_PATH.setFillRule(FillRule.EVEN_ODD);
        MEDIUM_TICKMARKS_PATH.setSmooth(true);
        MEDIUM_TICKMARKS_PATH.strokeTypeProperty().set(StrokeType.CENTERED);
        MEDIUM_TICKMARKS_PATH.strokeLineCapProperty().set(StrokeLineCap.ROUND);
        MEDIUM_TICKMARKS_PATH.strokeLineJoinProperty().set(StrokeLineJoin.BEVEL);
        MEDIUM_TICKMARKS_PATH.strokeWidthProperty().set(0.0025 * SIZE);

        final Path MINOR_TICKMARKS_PATH = new Path();
        MINOR_TICKMARKS_PATH.setFillRule(FillRule.EVEN_ODD);
        MINOR_TICKMARKS_PATH.setSmooth(true);
        MINOR_TICKMARKS_PATH.strokeTypeProperty().set(StrokeType.CENTERED);
        MINOR_TICKMARKS_PATH.strokeLineCapProperty().set(StrokeLineCap.ROUND);
        MINOR_TICKMARKS_PATH.strokeLineJoinProperty().set(StrokeLineJoin.BEVEL);
        MINOR_TICKMARKS_PATH.strokeWidthProperty().set(0.0015 * SIZE);

        final ArrayList<Text> tickmarkLabel = new ArrayList<Text>();

        // Set stroke dependend on the current backgrounddesign
        MAJOR_TICKMARKS_PATH.getStyleClass().add(control.getBackgroundDesign().CSS_BACKGROUND);
        MEDIUM_TICKMARKS_PATH.getStyleClass().add(control.getBackgroundDesign().CSS_BACKGROUND);
        MINOR_TICKMARKS_PATH.getStyleClass().add(control.getBackgroundDesign().CSS_BACKGROUND);

        final Rectangle BAR_BOUNDS;
        final double MIN_POSITION;
        final double MAX_POSITION;
        final int ORIENTATION;
        final int VERTICAL = 0;
        final int HORIZONTAL = 1;
        final double TICK_OFFSET;
        if (WIDTH <= HEIGHT) {
            // vertical
            ORIENTATION = VERTICAL;
            BAR_BOUNDS = new Rectangle(0.42 * WIDTH + 3, 0.1657142857142857 * HEIGHT, 0.16 * WIDTH - 5, 0.6714285714 * HEIGHT);
            MIN_POSITION = BAR_BOUNDS.getLayoutBounds().getMaxY();
            MAX_POSITION = BAR_BOUNDS.getLayoutBounds().getMinY();
            stepsize = Math.abs(BAR_BOUNDS.getLayoutBounds().getHeight() / control.getRange());
            TICK_OFFSET = BAR_BOUNDS.getLayoutBounds().getMinX() - SIZE * 0.05;
            textPoint = new Point2D(TICK_OFFSET - MAJOR_TICK_LENGTH - 5, 0);
        } else {
            // horizontal
            ORIENTATION = HORIZONTAL;
            BAR_BOUNDS = new Rectangle(0.1657142857142857 * WIDTH, 0.42 * HEIGHT + 3, 0.6714285714 * WIDTH, 0.16 * HEIGHT - 5);
            MIN_POSITION = BAR_BOUNDS.getLayoutBounds().getMinX();
            MAX_POSITION = BAR_BOUNDS.getLayoutBounds().getMaxX();
            stepsize = BAR_BOUNDS.getLayoutBounds().getWidth() / control.getRange();
            TICK_OFFSET = BAR_BOUNDS.getLayoutBounds().getMaxY() + SIZE * 0.05;
            textPoint = new Point2D(0, TICK_OFFSET + MAJOR_TICK_LENGTH + (0.06 * SIZE) + 5);
        }

        final double TICK_START = ORIENTATION == VERTICAL ? MAX_POSITION : MIN_POSITION;
        final double TICK_STOP = ORIENTATION == VERTICAL ? MIN_POSITION : MAX_POSITION;
        double valueCounter = ORIENTATION == VERTICAL ? control.getMaxValue() : control.getMinValue();
        int majorTickCounter = control.getMaxNoOfMinorTicks() - 1;
        for (double counter = TICK_START ; Double.compare(counter, TICK_STOP + 1) <= 0 ; counter += stepsize) {
            majorTickCounter++;
            if (majorTickCounter == control.getMaxNoOfMinorTicks()) {
                if (ORIENTATION == VERTICAL) {
                    innerPoint = new Point2D(TICK_OFFSET, 0);
                    outerPoint = new Point2D(TICK_OFFSET - MAJOR_TICK_LENGTH, 0);
                    MAJOR_TICKMARKS_PATH.getElements().add(new MoveTo(innerPoint.getX(), counter));
                    MAJOR_TICKMARKS_PATH.getElements().add(new LineTo(outerPoint.getX(), counter));
                } else {
                    innerPoint = new Point2D(0, TICK_OFFSET);
                    outerPoint = new Point2D(0, TICK_OFFSET + MAJOR_TICK_LENGTH);
                    MAJOR_TICKMARKS_PATH.getElements().add(new MoveTo(counter, innerPoint.getY()));
                    MAJOR_TICKMARKS_PATH.getElements().add(new LineTo(counter, outerPoint.getY()));
                }

                // Draw ticklabels
                if (control.isTickLabelsVisible()) {
                    final Text tickLabel = new Text(numberFormat.format(valueCounter));
                    tickLabel.setSmooth(true);
                    tickLabel.setFontSmoothingType(FontSmoothingType.LCD);
                    tickLabel.setTextAlignment(TextAlignment.CENTER);
                    tickLabel.setTextOrigin(VPos.BOTTOM);
                    tickLabel.getStyleClass().add(control.getBackgroundDesign().CSS_TEXT);
                    tickLabel.setStroke(null);
                    tickLabel.setFont(STD_FONT);
                    if (ORIENTATION == VERTICAL) {
                        tickLabel.setX(textPoint.getX() - tickLabel.getLayoutBounds().getWidth());
                        tickLabel.setY(counter + tickLabel.getLayoutBounds().getHeight() / 2.0);
                    } else {
                        tickLabel.setX(counter - tickLabel.getLayoutBounds().getWidth() / 2.0);
                        tickLabel.setY(textPoint.getY());
                    }
                    tickmarkLabel.add(tickLabel);
                    if (ORIENTATION == VERTICAL) {
                        valueCounter -= control.getMajorTickSpacing();
                    } else {
                        valueCounter += control.getMajorTickSpacing();
                    }
                }
                majorTickCounter = 0;
                continue;
            }

            if (control.getMaxNoOfMinorTicks() % 2 == 0 && majorTickCounter == (control.getMaxNoOfMinorTicks() / 2)) {
                // Draw the medium tickmarks
                if (ORIENTATION == VERTICAL) {
                    innerPoint = new Point2D(TICK_OFFSET, 0);
                    outerPoint = new Point2D(TICK_OFFSET - MEDIUM_TICK_LENGTH, 0);
                    MEDIUM_TICKMARKS_PATH.getElements().add(new MoveTo(innerPoint.getX(), counter));
                    MEDIUM_TICKMARKS_PATH.getElements().add(new LineTo(outerPoint.getX(), counter));
                } else {
                    innerPoint = new Point2D(0, TICK_OFFSET);
                    outerPoint = new Point2D(0, TICK_OFFSET + MEDIUM_TICK_LENGTH);
                    MEDIUM_TICKMARKS_PATH.getElements().add(new MoveTo(counter, innerPoint.getY()));
                    MEDIUM_TICKMARKS_PATH.getElements().add(new LineTo(counter, outerPoint.getY()));
                }
            } else if (control.isTickmarksVisible() && control.isMinorTicksVisible()) {
                // Draw the minor tickmarks
                if (ORIENTATION == VERTICAL) {
                    innerPoint = new Point2D(TICK_OFFSET, 0);
                    outerPoint = new Point2D(TICK_OFFSET - MINOR_TICK_LENGTH, 0);
                    MINOR_TICKMARKS_PATH.getElements().add(new MoveTo(innerPoint.getX(), counter));
                    MINOR_TICKMARKS_PATH.getElements().add(new LineTo(outerPoint.getX(), counter));
                } else {
                    innerPoint = new Point2D(0, TICK_OFFSET);
                    outerPoint = new Point2D(0, TICK_OFFSET + MINOR_TICK_LENGTH);
                    MINOR_TICKMARKS_PATH.getElements().add(new MoveTo(counter, innerPoint.getY()));
                    MINOR_TICKMARKS_PATH.getElements().add(new LineTo(counter, outerPoint.getY()));
                }
            }
        }

        tickmarks.getChildren().addAll(MAJOR_TICKMARKS_PATH, MEDIUM_TICKMARKS_PATH, MINOR_TICKMARKS_PATH);
        tickmarks.getChildren().addAll(tickmarkLabel);
        tickmarks.setCache(true);
    }

    public void drawForeground() {
        final double SIZE = gaugeBounds.getWidth() <= gaugeBounds.getHeight() ? gaugeBounds.getWidth() : gaugeBounds.getHeight();
        final double WIDTH = gaugeBounds.getWidth();
        final double HEIGHT = gaugeBounds.getHeight();

        foreground.getChildren().clear();

        final Insets INSETS = new Insets(0.0841121495 * SIZE + 2, WIDTH - 0.0841121495 * SIZE - 2, HEIGHT - 0.0841121495 * SIZE - 2, 0.0841121495 * SIZE + 2);
        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        foreground.getChildren().addAll(IBOUNDS);

        final Path FOREGROUND = new Path();
        final Point2D START;
        final Point2D STOP;
        // Highlight
        if (WIDTH >= HEIGHT) {
            // Horizontal glass effect
            FOREGROUND.setFillRule(FillRule.EVEN_ODD);
            FOREGROUND.getElements().add(new MoveTo(INSETS.getLeft(), INSETS.getBottom()));
            FOREGROUND.getElements().add(new LineTo(INSETS.getRight(), INSETS.getBottom()));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getRight(), INSETS.getBottom(),
                                                          INSETS.getRight() - 13, 0.7 * HEIGHT,
                                                          INSETS.getRight() - 13, 0.5 * HEIGHT));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getRight() - 13, 0.3 * HEIGHT,
                                                          INSETS.getRight(), INSETS.getTop(),
                                                          INSETS.getRight(), INSETS.getTop()));
            FOREGROUND.getElements().add(new LineTo(INSETS.getLeft(), INSETS.getTop()));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getLeft(), INSETS.getTop(),
                                                          INSETS.getLeft() + 13, 0.3 * HEIGHT,
                                                          INSETS.getLeft() + 13, 0.5 * HEIGHT));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getLeft() + 13, 0.7 * HEIGHT,
                                                          INSETS.getLeft(), INSETS.getBottom(),
                                                          INSETS.getLeft(), INSETS.getBottom()));
            FOREGROUND.getElements().add(new ClosePath());
            START = new Point2D(0, FOREGROUND.getLayoutBounds().getMaxY());
            STOP = new Point2D(0, FOREGROUND.getLayoutBounds().getMinY());
        } else {
            // Vertical glass effect
            FOREGROUND.setFillRule(FillRule.EVEN_ODD);
            FOREGROUND.getElements().add(new MoveTo(INSETS.getLeft(), INSETS.getTop()));
            FOREGROUND.getElements().add(new LineTo(INSETS.getLeft(), INSETS.getBottom()));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getLeft(), INSETS.getBottom(),
                                                          0.3 * WIDTH, INSETS.getBottom() - 13,
                                                          0.5 * WIDTH, INSETS.getBottom() - 13));
            FOREGROUND.getElements().add(new CubicCurveTo(0.7 * WIDTH, INSETS.getBottom() - 13,
                                                          INSETS.getRight(), INSETS.getBottom(),
                                                          INSETS.getRight(), INSETS.getBottom()));
            FOREGROUND.getElements().add(new LineTo(INSETS.getRight(), INSETS.getTop()));
            FOREGROUND.getElements().add(new CubicCurveTo(INSETS.getRight(), INSETS.getTop(),
                                                          0.7 * WIDTH, INSETS.getTop() + 13,
                                                          0.5 * WIDTH, INSETS.getTop() + 13));
            FOREGROUND.getElements().add(new CubicCurveTo(0.3 * WIDTH, INSETS.getTop() + 13,
                                                          INSETS.getLeft(), INSETS.getTop(),
                                                          INSETS.getLeft(), INSETS.getTop()));
            FOREGROUND.getElements().add(new ClosePath());
            START = new Point2D(FOREGROUND.getLayoutBounds().getMinX(), 0);
            STOP = new Point2D(FOREGROUND.getLayoutBounds().getMaxX(), 0);
        }

        final LinearGradient GRADIENT = new LinearGradient(START.getX(), START.getY(),
                                                           STOP.getX(), STOP.getY(),
                                                           false, CycleMethod.NO_CYCLE,
                                                           new Stop(0.0, Color.color(1, 1, 1, 0)),
                                                           new Stop(0.06, Color.color(1, 1, 1, 0)),
                                                           new Stop(0.07, Color.color(1, 1, 1, 0)),
                                                           new Stop(0.12, Color.color(1, 1, 1, 0.05)),
                                                           new Stop(0.17, Color.color(1, 1, 1, 0.0)),
                                                           new Stop(0.18, Color.color(1, 1, 1, 0)),
                                                           new Stop(0.23, Color.color(1, 1, 1, 0.02)),
                                                           new Stop(0.30, Color.color(1, 1, 1, 0.0)),
                                                           new Stop(0.8, Color.color(1, 1, 1, 0)),
                                                           new Stop(0.84, Color.color(1, 1, 1, 0.08)),
                                                           new Stop(0.93, Color.color(1, 1, 1, 0.18)),
                                                           new Stop(0.94, Color.color(1, 1, 1, 0.20)),
                                                           new Stop(0.96, Color.color(1, 1, 1, 0.10)),
                                                           new Stop(0.97, Color.color(1, 1, 1, 0)),
                                                           new Stop(1.0, Color.color(1, 1, 1, 0)));
        FOREGROUND.setFill(GRADIENT);
        FOREGROUND.setStroke(null);

        foreground.getChildren().addAll(FOREGROUND);
        foreground.setCache(true);
    }
}
