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
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.ShapeBuilder;
import javafx.scene.transform.Transform;
import jfxtras.labs.internal.scene.control.behavior.ClockBehavior;
import jfxtras.labs.scene.control.gauge.Clock;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;


/**
 * Created by
 * User: hansolo
 * Date: 12.03.12
 * Time: 12:44
 */
public class ClockSkin extends SkinBase<Clock, ClockBehavior> {
    private static final long INTERVAL = 50000000l;
    private static final Calendar CAL  = Calendar.getInstance();
    private Clock                 control;
    private int                   dst;
    private Group                 hourPointer;
    private Group                 minutePointer;
    private Group                 secondPointer;
    private Group                 clock;
    private DoubleProperty        hourAngle;
    private DoubleProperty        minuteAngle;
    private DoubleProperty        secondAngle;
    private int                   hourOffset;
    private int                   minuteOffset;
    private int                   lastHour;
    private long                  lastTimerCall;
    private boolean               isDay;
    private AnimationTimer        timer;
    private boolean               isDirty;
    private boolean               initialized;


    // ******************** Constructors **************************************
    public ClockSkin(final Clock CONTROL) {
        super(CONTROL, new ClockBehavior(CONTROL));
        control       = CONTROL;
        CAL.setTimeZone(TimeZone.getTimeZone(control.getTimeZone()));
        dst           = control.isDaylightSavingTime() ? 1 : 0;
        hourPointer   = new Group();
        minutePointer = new Group();
        secondPointer = new Group();
        clock         = new Group();
        hourAngle     = new SimpleDoubleProperty(360 / 12 * Calendar.getInstance().get(Calendar.HOUR) + dst);
        minuteAngle   = new SimpleDoubleProperty(360 / 60 * Calendar.getInstance().get(Calendar.MINUTE));
        secondAngle   = new SimpleDoubleProperty(360 / 60 * Calendar.getInstance().get(Calendar.SECOND));
        hourOffset    = CAL.get(Calendar.HOUR_OF_DAY) - Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        minuteOffset  = CAL.get(Calendar.MINUTE) - Calendar.getInstance().get(Calendar.MINUTE);
        lastHour      = CAL.get(Calendar.HOUR_OF_DAY);
        lastTimerCall = 0;
        isDay         = true;
        timer         = new AnimationTimer() {
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
                    minuteAngle.set((minuteOffset + Calendar.getInstance().get(Calendar.MINUTE)) * 6);
                    // Hours
                    hourAngle.set((hourOffset + Calendar.getInstance().get(Calendar.HOUR)) * 30 + 0.5 * Calendar.getInstance().get(Calendar.MINUTE));
                    lastTimerCall = currentNanoTime;
                    lastHour = CAL.get(Calendar.HOUR_OF_DAY);
                }
            }
        };
        isDirty       = false;
        initialized   = false;

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

        setTime();
        initialized = true;
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

        getChildren().addAll(clock,
                             minutePointer,
                             hourPointer,
                             secondPointer);
    }

    @Override
    protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if (PROPERTY == "TIME_ZONE") {
          setTime();
        } else if (PROPERTY == "RUNNING") {
            if (control.isRunning()) {
                setTime();
                timer.start();
            } else {
                timer.stop();
            }
        } else if (PROPERTY == "SECOND") {
            secondPointer.setRotate(secondAngle.get());
        } else if (PROPERTY == "MINUTE") {
            minutePointer.setRotate(minuteAngle.get());
        } else if (PROPERTY == "HOUR") {
            hourPointer.setRotate(hourAngle.get());
        }
    }

    @Override
    public void layoutChildren() {
        if (isDirty) {
            paint();
            isDirty = false;
        }
        super.layoutChildren();
    }

    @Override
    public final Clock getSkinnable() {
        return control;
    }

    @Override
    public final void dispose() {
        control = null;
    }

    @Override
    protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 127;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override
    protected double computePrefHeight(final double PREF_HEIGHT) {
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
        hourOffset    = CAL.get(Calendar.HOUR_OF_DAY) + dst - Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + localDst;
        minuteOffset  = CAL.get(Calendar.MINUTE) - Calendar.getInstance().get(Calendar.MINUTE);
        secondAngle.set(Calendar.getInstance().get(Calendar.SECOND) * 6 + Calendar.getInstance().get(Calendar.MILLISECOND) * 0.006);
        minuteAngle.set((minuteOffset + Calendar.getInstance().get(Calendar.MINUTE)) * 6);
        hourAngle.set((hourOffset + Calendar.getInstance().get(Calendar.HOUR)) * 30 + 0.5 * Calendar.getInstance().get(Calendar.MINUTE));

        checkForNight();
    }

    private void checkForNight() {
        if (control.isAutoDimEnabled()) {
            if (CAL.get(Calendar.HOUR_OF_DAY) > 6 && CAL.get(Calendar.HOUR_OF_DAY) < 18) {
                isDay = true;
            } else {
                isDay = false;
            }
        } else {
            isDay = true;
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
        FRAME.setId("clock-frame-fill");
        FRAME.setStroke(null);

        final Circle BACKGROUND = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4921259842519685 * WIDTH);
        if (isDay) {
            BACKGROUND.setId("clock-bright-background-fill");
        } else {
            BACKGROUND.setId("clock-dark-background-fill");
        }
        BACKGROUND.setStroke(null);

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setWidth(0.08503937007874016 * BACKGROUND.getLayoutBounds().getWidth());
        INNER_SHADOW.setHeight(0.08503937007874016 * BACKGROUND.getLayoutBounds().getHeight());
        INNER_SHADOW.setOffsetX(1.157146581871515E-18 * SIZE);
        INNER_SHADOW.setOffsetY(0.01889763779527559 * SIZE);
        INNER_SHADOW.setRadius(0.08503937007874016 * BACKGROUND.getLayoutBounds().getWidth());
        INNER_SHADOW.setColor(Color.color(0, 0, 0, 0.4980392157));
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        INNER_SHADOW.inputProperty().set(null);
        BACKGROUND.setEffect(INNER_SHADOW);

        final Group TICK_MARKS = new Group();
        for (int angle = 0 ; angle < 360 ; angle += 6) {
            final Transform TRANSFORM = Transform.rotate(angle, SIZE / 2.0, SIZE / 2.0);
            final Rectangle TICK;
            if (angle % 30 == 0) {
                // Big tickmarks
                TICK = new Rectangle(0.48031496062992124 * WIDTH, 0.023622047244094488 * HEIGHT,
                                     0.03937007874015748 * WIDTH, 0.14960629921259844 * HEIGHT);
            } else {
                // Small tickmarks
                TICK = new Rectangle(0.4881889763779528 * WIDTH, 0.023622047244094488 * HEIGHT,
                                     0.023622047244094488 * WIDTH, 0.047244094488188976 * HEIGHT);
            }
            if (isDay) {
                TICK.setId("clock-bright-foreground-fill");
            } else {
                TICK.setId("clock-dark-foreground-fill");
            }
            TICK.setStroke(null);
            TICK.getTransforms().add(TRANSFORM);
            TICK_MARKS.getChildren().add(TICK);
        }

        clock.getChildren().addAll(FRAME,
                                   BACKGROUND,
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

        final Rectangle MINUTE = new Rectangle(0.48031496062992124 * WIDTH, 0.047244094488188976 * HEIGHT,
                                             0.03937007874015748 * WIDTH, 0.47244094488188976 * HEIGHT);
        if (isDay) {
            MINUTE.setId("clock-bright-foreground-fill");
        } else {
            MINUTE.setId("clock-dark-foreground-fill");
        }
        MINUTE.setStroke(null);

        final DropShadow DROP_SHADOW = new DropShadow();
        DROP_SHADOW.setWidth(2.0);
        DROP_SHADOW.setHeight(2.0);
        DROP_SHADOW.setOffsetX(0.0);
        DROP_SHADOW.setOffsetY(0.0);
        DROP_SHADOW.setRadius(2.0);
        DROP_SHADOW.setColor(Color.color(0, 0, 0, 0.65));
        DROP_SHADOW.setBlurType(BlurType.GAUSSIAN);
        DROP_SHADOW.inputProperty().set(null);
        MINUTE.setEffect(DROP_SHADOW);

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

        final Rectangle HOUR = new Rectangle(0.47244094488188976 * WIDTH, 0.2125984251968504 * HEIGHT,
                                               0.05511811023622047 * WIDTH, 0.2992125984251969 * HEIGHT);
        if (isDay) {
            HOUR.setId("clock-bright-foreground-fill");
        } else {
            HOUR.setId("clock-dark-foreground-fill");
        }
        HOUR.setStroke(null);

        final DropShadow DROP_SHADOW = new DropShadow();
        DROP_SHADOW.setWidth(2.0);
        DROP_SHADOW.setHeight(2.0);
        DROP_SHADOW.setOffsetX(0.0);
        DROP_SHADOW.setOffsetY(0.0);
        DROP_SHADOW.setRadius(2.0);
        DROP_SHADOW.setColor(Color.color(0, 0, 0, 0.65));
        DROP_SHADOW.setBlurType(BlurType.GAUSSIAN);
        DROP_SHADOW.inputProperty().set(null);
        HOUR.setEffect(DROP_SHADOW);

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

        final Circle OUTER_CIRCLE = new Circle(SIZE * 0.5,
                                               SIZE * 0.190909091,
                                               SIZE * 0.0454545454);
        final Circle INNER_CIRCLE = new Circle(SIZE * 0.5,
                                               SIZE * 0.190909091,
                                               SIZE * 0.0363636364);

        Path second = (Path) Path.subtract(OUTER_CIRCLE, INNER_CIRCLE);
        second.getElements().add(new MoveTo(WIDTH * 0.4863636364, SIZE * 0.5));
        second.getElements().add(new LineTo(WIDTH * 0.5136363636, SIZE * 0.5));
        second.getElements().add(new LineTo(WIDTH * 0.5045454545, WIDTH * 0.0363636364));
        second.getElements().add(new LineTo(WIDTH * 0.4954545455, WIDTH * 0.0363636364));
        second.getElements().add(new ClosePath());
        second = (Path) Path.subtract(second, new Circle(SIZE * 0.5, SIZE * 0.190909091, SIZE * 0.0363636364));

        //second.setId("clock-second-pointer-fill"); // somehow doesn't work with elements created by subtract method
        second.setFill(Color.rgb(237, 0, 58));
        second.setStroke(null);

        final DropShadow DROP_SHADOW = new DropShadow();
        DROP_SHADOW.setWidth(2.0);
        DROP_SHADOW.setHeight(2.0);
        DROP_SHADOW.setOffsetX(0.0);
        DROP_SHADOW.setOffsetY(0.0);
        DROP_SHADOW.setRadius(2.0);
        DROP_SHADOW.setColor(Color.color(0, 0, 0, 0.65));
        DROP_SHADOW.setBlurType(BlurType.GAUSSIAN);
        second.setEffect(DROP_SHADOW);

        final Circle CENTER_KNOB = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.051181102362204724 * WIDTH);
        if (isDay) {
            CENTER_KNOB.setId("clock-bright-foreground-fill");
        } else {
            CENTER_KNOB.setId("clock-dark-foreground-fill");
        }
        CENTER_KNOB.setStroke(null);

        secondPointer.setRotate(secondAngle.get());

        secondPointer.getChildren().addAll(second, CENTER_KNOB);
        secondPointer.setCache(true);
    }
}

