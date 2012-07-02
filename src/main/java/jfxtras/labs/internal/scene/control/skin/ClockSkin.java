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

package jfxtras.labs.internal.scene.control.skin;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import jfxtras.labs.internal.scene.control.behavior.ClockBehavior;
import jfxtras.labs.scene.control.gauge.Clock;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by
 * User: hansolo
 * Date: 12.03.12
 * Time: 12:44
 */
public class ClockSkin extends SkinBase<Clock, ClockBehavior> {
    private static final long     INTERVAL = 50000000l;
    private static final Calendar CAL      = Calendar.getInstance();
    private Clock                 control;
    private int                   dst;
    private Group                 hourPointer;
    private Group                 hourPointerShadow;
    private Group                 minutePointer;
    private Group                 minutePointerShadow;
    private Group                 secondPointer;
    private Group                 secondPointerShadow;
    private Group                 clock;
    private DoubleProperty        hourAngle;
    private DoubleProperty        minuteAngle;
    private DoubleProperty        secondAngle;
    private int                   hourOffset;
    private int                   minuteOffset;
    private int                   lastHour;
    private DoubleProperty        minute;
    private long                  lastTimerCall;
    private boolean               isDay;
    private Timeline              timeline;
    private AnimationTimer        timer;
    private boolean               isDirty;
    private boolean               initialized;


    // ******************** Constructors **************************************
    public ClockSkin(final Clock CONTROL) {
        super(CONTROL, new ClockBehavior(CONTROL));
        control             = CONTROL;
        CAL.setTimeZone(TimeZone.getTimeZone(control.getTimeZone()));
        dst                 = control.isDaylightSavingTime() ? 1 : 0;
        hourPointer         = new Group();
        hourPointerShadow   = new Group(hourPointer);
        minutePointer       = new Group();
        minutePointerShadow = new Group(minutePointer);
        secondPointer       = new Group();
        secondPointerShadow = new Group(secondPointer);
        clock               = new Group();
        hourAngle           = new SimpleDoubleProperty(360 / 12 * Calendar.getInstance().get(Calendar.HOUR) + dst);
        minuteAngle         = new SimpleDoubleProperty(360 / 60 * Calendar.getInstance().get(Calendar.MINUTE));
        secondAngle         = new SimpleDoubleProperty(360 / 60 * Calendar.getInstance().get(Calendar.SECOND));
        hourOffset          = CAL.get(Calendar.HOUR_OF_DAY) - Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        minuteOffset        = CAL.get(Calendar.MINUTE) - Calendar.getInstance().get(Calendar.MINUTE);
        lastHour            = CAL.get(Calendar.HOUR_OF_DAY);
        minute              = new SimpleDoubleProperty(0);
        lastTimerCall       = 0;
        isDay               = true;
        timeline            = new Timeline();
        timer               = new AnimationTimer() {
            @Override public void handle(long l) {
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime >= lastTimerCall + INTERVAL) {
                    CAL.setTimeZone(TimeZone.getTimeZone(control.getTimeZone()));
                    if (CAL.get(Calendar.HOUR_OF_DAY) < lastHour) {
                        checkForNight();
                        paint();
                    }
                    // Seconds
                    secondAngle.set(Calendar.getInstance().get(Calendar.SECOND) * 6 + Calendar.getInstance().get(Calendar.MILLISECOND) * 0.006);
                    // Minutes
                    minute.set((minuteOffset + Calendar.getInstance().get(Calendar.MINUTE)) * 6);
                    // Hours
                    hourAngle.set((hourOffset + Calendar.getInstance().get(Calendar.HOUR)) * 30 + 0.5 * Calendar.getInstance().get(Calendar.MINUTE));
                    lastTimerCall = currentNanoTime;
                    lastHour = CAL.get(Calendar.HOUR_OF_DAY);
                }
            }
        };
        minute.addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                moveMinutePointer(newValue.doubleValue());
            }
        });
        isDirty             = false;
        initialized         = false;
        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(200, 200);
        }

        control.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });

        control.prefHeightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });

        if (control.getBrightBackgroundPaint() == null) {
            control.setBrightBackgroundPaint(new RadialGradient(0, 0,
                                                                getPrefWidth() / 2, getPrefHeight() / 2,
                                                                getPrefWidth() / 2, false, CycleMethod.NO_CYCLE,
                                                                new Stop(0, Color.rgb(191, 207, 197)),
                                                                new Stop(0.7, Color.rgb(226, 239, 229)),
                                                                new Stop(1.0, Color.rgb(199, 216, 206))));
        }
        if (control.getDarkBackgroundPaint() == null) {
                    control.setDarkBackgroundPaint(new LinearGradient(0, 0,
                                                                      0, getPrefHeight(),
                                                                      false, CycleMethod.NO_CYCLE,
                                                                      new Stop(0, Color.rgb(62, 59, 50)),
                                                                      new Stop(1.0, Color.rgb(35, 37, 32))));
        }

        // Bindings
        if (secondPointer.visibleProperty().isBound()) {
            secondPointer.visibleProperty().unbind();
        }
        secondPointer.visibleProperty().bind(control.secondPointerVisibleProperty());

        // Register listeners
        registerChangeListener(control.runningProperty(), "RUNNING");
        registerChangeListener(control.timeZoneProperty(), "TIME_ZONE");
        registerChangeListener(secondAngle, "SECOND");
        registerChangeListener(minuteAngle, "MINUTE");
        registerChangeListener(hourAngle, "HOUR");
        registerChangeListener(control.themeProperty(), "THEME");
        registerChangeListener(control.clockStyleProperty(), "CLOCK_STYLE");
        registerChangeListener(control.brightBackgroundPaintProperty(), "BRIGHT_BACKGROUND_PAINT");
        registerChangeListener(control.darkBackgroundPaintProperty(), "DARK_BACKGROUND_PAINT");
        registerChangeListener(control.brightPointerPaintProperty(), "BRIGHT_POINTER_PAINT");
        registerChangeListener(control.darkPointerPaintProperty(), "DARK_POINTER_PAINT");
        registerChangeListener(control.brightTickMarkPaintProperty(), "BRIGHT_TICK_MARK_PAINT");
        registerChangeListener(control.darkTickMarkPaintProperty(), "DARK_TICK_MARK_PAINT");
        registerChangeListener(control.secondPointerPaintProperty(), "SECOND_POINTER_PAINT");
        registerChangeListener(control.titleProperty(), "TITLE");

        setTime();
        initialized = true;
        checkForNight();
        paint();
        if (control.isRunning()) {
            timer.start();
        }
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        getChildren().clear();
        drawClock();
        drawMinutePointer();
        drawHourPointer();
        drawSecondPointer();
        drawShadows();
        getChildren().addAll(clock,
                             minutePointerShadow,
                             hourPointerShadow,
                             secondPointerShadow);
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("TIME_ZONE".equals(PROPERTY)) {
          setTime();
        } else if ("RUNNING".equals(PROPERTY)) {
            if (control.isRunning()) {
                setTime();
                timer.start();
            } else {
                timer.stop();
            }
        } else if ("SECOND".equals(PROPERTY)) {
            secondPointer.setRotate(secondAngle.get());
        } else if ("MINUTE".equals(PROPERTY)) {
            minutePointer.setRotate(minuteAngle.get());
        } else if ("HOUR".equals(PROPERTY)) {
            hourPointer.setRotate(hourAngle.get());
        } else if ("TYPE".equals(PROPERTY)) {
            checkForNight();
            paint();
        } else if ("THEME".equals(PROPERTY)) {
            paint();
        } else if ("CLOCK_STYLE".equals(PROPERTY)) {
            drawSecondPointer();
        } else if ("BRIGHT_BACKGROUND_PAINT".equals(PROPERTY)) {
            paint();
        } else if ("DARK_BACKGROUND_PAINT".equals(PROPERTY)) {
            paint();
        } else if ("BRIGHT_POINTER_PAINT".equals(PROPERTY)) {
            paint();
        } else if ("DARK_POINTER_PAINT".equals(PROPERTY)) {
            paint();
        } else if ("BRIGHT_TICK_MARK_PAINT".equals(PROPERTY)) {
            paint();
        } else if ("DARK_TICK_MARK_PAINT".equals(PROPERTY)) {
            paint();
        } else if ("SECOND_POINTER_PAINT".equals(PROPERTY)) {
            paint();
        } else if ("TITLE".equals(PROPERTY)) {
            paint();
        }
    }

    @Override public void layoutChildren() {
        if (isDirty) {
            paint();
            isDirty = false;
        }
        super.layoutChildren();
    }

    @Override public final Clock getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 127;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 127;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }

    private void setTime() {
        CAL.setTimeZone(TimeZone.getTimeZone(control.getTimeZone()));
        dst = control.isDaylightSavingTime() ? 1 : 0;
        int localDst = Calendar.getInstance().getTimeZone().inDaylightTime(new Date()) ? 1 : 0;
        hourOffset   = CAL.get(Calendar.HOUR_OF_DAY) + dst - Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + localDst;
        minuteOffset = CAL.get(Calendar.MINUTE) - Calendar.getInstance().get(Calendar.MINUTE);
        secondAngle.set(Calendar.getInstance().get(Calendar.SECOND) * 6 + Calendar.getInstance().get(Calendar.MILLISECOND) * 0.006);
        minuteAngle.set((minuteOffset + Calendar.getInstance().get(Calendar.MINUTE)) * 6);
        hourAngle.set((hourOffset + Calendar.getInstance().get(Calendar.HOUR)) * 30 + 0.5 * Calendar.getInstance().get(Calendar.MINUTE));

        checkForNight();
    }

    private void moveMinutePointer(double newMinuteAngle) {
        final KeyValue kv = new KeyValue(minuteAngle, newMinuteAngle, Interpolator.SPLINE(0.5, 0.4, 0.4, 1.0));
        final KeyFrame kf = new KeyFrame(Duration.millis(200), kv);
        timeline  = new Timeline();
        timeline.getKeyFrames().add(kf);
        timeline.play();
    }

    private void checkForNight() {
        if (control.isAutoDimEnabled()) {
            if (CAL.get(Calendar.HOUR_OF_DAY) > 6 && CAL.get(Calendar.HOUR_OF_DAY) < 18) {
                isDay = true;
            } else {
                isDay = false;
            }
        } else {
            isDay = control.getTheme() == Clock.Theme.BRIGHT;
        }
    }


    // ******************** Drawing related ***********************************
    public void drawClock() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        clock.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        clock.getChildren().add(IBOUNDS);

        final Circle FRAME = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.5 * WIDTH);
        FRAME.getStyleClass().clear();
        FRAME.getStyleClass().add("clock-frame-fill");
        FRAME.setStroke(null);

        final InnerShadow HIGHLIGHT = new InnerShadow();
        HIGHLIGHT.setWidth(0.15 * SIZE);
        HIGHLIGHT.setHeight(0.15 * SIZE);
        HIGHLIGHT.setOffsetY(-0.025 * SIZE);
        HIGHLIGHT.setColor(Color.color(1, 1, 1, 0.65));

        final InnerShadow SHADOW = new InnerShadow();
        SHADOW.setInput(HIGHLIGHT);
        SHADOW.setWidth(0.15 * SIZE);
        SHADOW.setHeight(0.15 * SIZE);
        SHADOW.setOffsetY(0.025 * SIZE);
        SHADOW.setColor(Color.color(0, 0, 0, 0.1));
        FRAME.setEffect(SHADOW);

        final Circle BACKGROUND = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4921259842519685 * WIDTH);
        if (control.isAutoDimEnabled()) {
            if (isDay) {
                BACKGROUND.setFill(control.getBrightBackgroundPaint());
            } else {
                BACKGROUND.setFill(control.getDarkBackgroundPaint());
            }
        } else {
            if (control.getTheme() == Clock.Theme.BRIGHT) {
                BACKGROUND.setFill(control.getBrightBackgroundPaint());
            } else {
                BACKGROUND.setFill(control.getDarkBackgroundPaint());
            }
        }
        BACKGROUND.setStroke(null);

        final Text TITLE = new Text(control.getTitle());
        TITLE.setFontSmoothingType(FontSmoothingType.LCD);
        TITLE.setFont(Font.font("Verdana", FontWeight.NORMAL, 0.06 * SIZE));
        TITLE.setTextAlignment(TextAlignment.CENTER);
        TITLE.setX((SIZE - TITLE.getLayoutBounds().getWidth()) / 2.0);
        TITLE.setY(0.6 * SIZE + TITLE.getLayoutBounds().getHeight());
        if (control.getTheme() == Clock.Theme.BRIGHT) {
            TITLE.setFill(control.getBrightTickMarkPaint());
        } else {
            TITLE.setFill(control.getDarkTickMarkPaint());
        }

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setWidth(0.0929889298892989 * BACKGROUND.getLayoutBounds().getWidth());
        INNER_SHADOW.setHeight(0.0929889298892989 * BACKGROUND.getLayoutBounds().getHeight());
        INNER_SHADOW.setOffsetY(0.008856088560885609 * SIZE);
        INNER_SHADOW.setRadius(0.0929889298892989 * BACKGROUND.getLayoutBounds().getWidth());
        INNER_SHADOW.setColor(Color.color(0, 0, 0, 0.6470588235));
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        INNER_SHADOW.inputProperty().set(null);
        BACKGROUND.setEffect(INNER_SHADOW);

        final Path GLASS_EFFECT = new Path();
        if (control.getClockStyle() == Clock.ClockStyle.IOS6) {
            GLASS_EFFECT.setFillRule(FillRule.EVEN_ODD);
            GLASS_EFFECT.getElements().add(new MoveTo(0.023622047244094488 * WIDTH, 0.36220472440944884 * HEIGHT));
            GLASS_EFFECT.getElements().add(new CubicCurveTo(0.08661417322834646 * WIDTH, 0.15748031496062992 * HEIGHT,
                                                            0.2677165354330709 * WIDTH, 0.007874015748031496 * HEIGHT,
                                                            0.5039370078740157 * WIDTH, 0.007874015748031496 * HEIGHT));
            GLASS_EFFECT.getElements().add(new CubicCurveTo(0.7322834645669292 * WIDTH, 0.007874015748031496 * HEIGHT,
                                                            0.9133858267716536 * WIDTH, 0.15748031496062992 * HEIGHT,
                                                            0.9763779527559056 * WIDTH, 0.36220472440944884 * HEIGHT));
            GLASS_EFFECT.getElements().add(new CubicCurveTo(0.984251968503937 * WIDTH, 0.3858267716535433 * HEIGHT,
                                                            0.7480314960629921 * WIDTH, 0.5039370078740157 * HEIGHT,
                                                            0.5039370078740157 * WIDTH, 0.5039370078740157 * HEIGHT));
            GLASS_EFFECT.getElements().add(new CubicCurveTo(0.25196850393700787 * WIDTH, 0.49606299212598426 * HEIGHT,
                                                            0.015748031496062992 * WIDTH, 0.3858267716535433 * HEIGHT,
                                                            0.023622047244094488 * WIDTH, 0.36220472440944884 * HEIGHT));
            GLASS_EFFECT.getElements().add(new ClosePath());
            GLASS_EFFECT.setFill(Color.color(1, 1, 1, 0.15));
            GLASS_EFFECT.setStroke(null);
        }

        final Group TICK_MARKS = new Group();
        for (int angle = 0 ; angle < 360 ; angle += 6) {
            final Transform TRANSFORM = Transform.rotate(angle, SIZE / 2.0, SIZE / 2.0);
            final Rectangle TICK;
            if (angle % 30 == 0) {
                // Big tickmarks
                if (control.getClockStyle() == Clock.ClockStyle.IOS6) {
                    if (angle % 90 == 0) {
                        TICK = new Rectangle(0.4763779528 * WIDTH, 0.023622047244094488 * HEIGHT,
                                             0.0472440945 * WIDTH, 0.110701107 * HEIGHT);
                    } else {
                        TICK = new Rectangle(0.48031496062992124 * WIDTH, 0.023622047244094488 * HEIGHT,
                                             0.03937007874015748 * WIDTH, 0.110701107 * HEIGHT);
                    }
                } else {
                    TICK = new Rectangle(0.48031496062992124 * WIDTH, 0.023622047244094488 * HEIGHT,
                                         0.03937007874015748 * WIDTH, 0.110701107 * HEIGHT);
                }
            } else {
                // Small tickmarks
                switch (control.getClockStyle()) {
                    case IOS6:
                        TICK = new Rectangle(0.4960629921 * WIDTH, 0.023622047244094488 * HEIGHT,
                                             0.0078740157 * WIDTH, 0.047244094488188976 * HEIGHT);
                        break;
                    case DB:
                    default:
                        TICK = new Rectangle(0.4881889763779528 * WIDTH, 0.023622047244094488 * HEIGHT,
                                             0.023622047244094488 * WIDTH, 0.047244094488188976 * HEIGHT);
                        break;
                }
            }
            if (control.isAutoDimEnabled()) {
                if (isDay) {
                    TICK.setFill(control.getBrightTickMarkPaint());
                } else {
                    TICK.setFill(control.getDarkTickMarkPaint());
                }
            } else {
                if (control.getTheme() == Clock.Theme.BRIGHT) {
                    TICK.setFill(control.getBrightTickMarkPaint());
                } else {
                    TICK.setFill(control.getDarkTickMarkPaint());
                }
            }
            TICK.setStroke(null);
            TICK.getTransforms().add(TRANSFORM);
            TICK_MARKS.getChildren().add(TICK);
        }

        clock.getChildren().addAll(FRAME,
            BACKGROUND,
            TITLE,
            GLASS_EFFECT,
            TICK_MARKS);
        clock.setCache(true);
    }

    public void drawMinutePointer() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        minutePointer.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        minutePointer.getChildren().add(IBOUNDS);
        final Shape MINUTE;
        switch(control.getClockStyle()) {
            case IOS6:
                MINUTE = new Rectangle(0.4783464567 * WIDTH, 0.047244094488188976 * HEIGHT,
                                       0.0433070866 * WIDTH, 0.5511811024 * HEIGHT);
                break;
            case DB:
            default:
                MINUTE = new Rectangle(0.48031496062992124 * WIDTH, 0.047244094488188976 * HEIGHT,
                                       0.03937007874015748 * WIDTH, 0.47244094488188976 * HEIGHT);
                break;
        }
        if (control.isAutoDimEnabled()) {
            if (isDay) {
                MINUTE.setFill(control.getBrightPointerPaint());
            } else {
                MINUTE.setFill(control.getDarkPointerPaint());
            }
        } else {
            if (control.getTheme() == Clock.Theme.BRIGHT) {
                MINUTE.setFill(control.getBrightPointerPaint());
            } else {
                MINUTE.setFill(control.getDarkPointerPaint());
            }
        }

        MINUTE.setStroke(null);

        minutePointer.setRotate(minuteAngle.get());
        minutePointer.getChildren().add(MINUTE);
        minutePointer.setCache(true);
    }

    public void drawHourPointer() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        hourPointer.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        hourPointer.getChildren().add(IBOUNDS);
        final Shape HOUR;
        switch (control.getClockStyle()) {
            case IOS6:
                HOUR = new Rectangle(0.4783464567 * WIDTH, 0.2440944882 * HEIGHT,
                                     0.0433070866 * WIDTH, 0.3543307087 * HEIGHT);
                break;
            case DB:
            default:
                HOUR = new Rectangle(0.47244094488188976 * WIDTH, 0.2125984251968504 * HEIGHT,
                                     0.05511811023622047 * WIDTH, 0.2992125984251969 * HEIGHT);
                break;
        }
        if (control.isAutoDimEnabled()) {
            if (isDay) {
                HOUR.setFill(control.getBrightPointerPaint());
            } else {
                HOUR.setFill(control.getDarkPointerPaint());
            }
        } else {
            if (control.getTheme() == Clock.Theme.BRIGHT) {
                HOUR.setFill(control.getBrightPointerPaint());
            } else {
                HOUR.setFill(control.getDarkPointerPaint());
            }
        }
        HOUR.setStroke(null);

        hourPointer.setRotate(hourAngle.get());
        hourPointer.getChildren().add(HOUR);
        hourPointer.setCache(true);
    }

    public void drawSecondPointer() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        secondPointer.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        secondPointer.getChildren().add(IBOUNDS);
        Path second;
        switch (control.getClockStyle()) {
            case IOS6:
                final Shape TOP_CIRCLE = new Circle(0.5 * WIDTH,
                                                    0.20078740157480315 * HEIGHT,
                                                    0.036900369 * WIDTH);
                final Shape BODY       = new Rectangle(0.4926199262 * WIDTH, 0.2204724409 * HEIGHT,
                                                       0.0147601476 * WIDTH, 0.4409448819 * HEIGHT);
                BODY.setFill(Color.BLACK);
                final Shape CENTER_CIRCLE = new Circle(0.5 * WIDTH,
                                                       0.5 * HEIGHT,
                                                       0.0236220472 * WIDTH);
                Shape tmp = Path.union(TOP_CIRCLE, BODY);
                tmp.setFill(Color.BLACK);
                second = (Path) Path.union(tmp, CENTER_CIRCLE);
                break;
            case DB:
            default:
                final Circle OUTER_CIRCLE = new Circle(SIZE * 0.5,
                                                       SIZE * 0.190909091,
                                                       SIZE * 0.0454545454);
                final Circle INNER_CIRCLE = new Circle(SIZE * 0.5,
                                                       SIZE * 0.190909091,
                                                       SIZE * 0.0363636364);
                second = (Path) Path.subtract(OUTER_CIRCLE, INNER_CIRCLE);
                second.getElements().add(new MoveTo(WIDTH * 0.4863636364, SIZE * 0.5));
                second.getElements().add(new LineTo(WIDTH * 0.5136363636, SIZE * 0.5));
                second.getElements().add(new LineTo(WIDTH * 0.5045454545, WIDTH * 0.0363636364));
                second.getElements().add(new LineTo(WIDTH * 0.4954545455, WIDTH * 0.0363636364));
                second.getElements().add(new ClosePath());
                second = (Path) Path.subtract(second, new Circle(SIZE * 0.5, SIZE * 0.190909091, SIZE * 0.0363636364));
                break;
        }
        second.setFill(control.getSecondPointerPaint());
        second.setStroke(null);

        secondPointer.getChildren().add(second);
        secondPointer.setRotate(secondAngle.get());

        final Circle CENTER_KNOB;
        if (control.getClockStyle() == Clock.ClockStyle.DB) {
            CENTER_KNOB = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.051181102362204724 * WIDTH);
            CENTER_KNOB.getStyleClass().clear();
            if (control.isAutoDimEnabled()) {
                if (isDay) {
                    CENTER_KNOB.setFill(control.getBrightPointerPaint());
                } else {
                    CENTER_KNOB.setFill(control.getDarkPointerPaint());
                }
            } else {
                if (Clock.Theme.BRIGHT == control.getTheme()) {
                    CENTER_KNOB.setFill(control.getBrightPointerPaint());
                } else {
                    CENTER_KNOB.setFill(control.getDarkPointerPaint());
                }
            }
            CENTER_KNOB.setStroke(null);
        } else {
            CENTER_KNOB = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.0078740157 * WIDTH);
            CENTER_KNOB.setFill(Color.color(0.8745098039, 0.8745098039, 0.8156862745, 1));
            CENTER_KNOB.setStroke(null);
        }
        secondPointer.getChildren().add(CENTER_KNOB);
        secondPointer.setCache(true);
    }

    private void drawShadows() {
        final double SIZE  = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;

        final Lighting LIGHTING   = new Lighting();
        final Light.Distant LIGHT = new Light.Distant();
        LIGHT.setAzimuth(270);
        LIGHTING.setLight(LIGHT);

        final DropShadow DROP_SHADOW = new DropShadow();
        DROP_SHADOW.setInput(LIGHTING);
        DROP_SHADOW.setOffsetY(0.015 * WIDTH);
        DROP_SHADOW.setRadius(0.015 * WIDTH);
        DROP_SHADOW.setBlurType(BlurType.GAUSSIAN);
        DROP_SHADOW.setColor(Color.color(0, 0, 0, 0.55));

        minutePointerShadow.setEffect(DROP_SHADOW);
        hourPointerShadow.setEffect(DROP_SHADOW);
        secondPointerShadow.setEffect(DROP_SHADOW);
    }
}

