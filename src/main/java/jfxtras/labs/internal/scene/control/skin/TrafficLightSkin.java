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

import com.sun.javafx.scene.control.skin.SkinBase;
import jfxtras.labs.internal.scene.control.behavior.TrafficLightBehavior;
import jfxtras.labs.scene.control.gauge.TrafficLight;
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;


/**
 * Created by
 * User: hansolo
 * Date: 20.02.12
 * Time: 20:52
 */
public class TrafficLightSkin extends SkinBase<TrafficLight, TrafficLightBehavior> {
    public static final long BLINK_INTERVAL = 500000000l;
    private TrafficLight control;
    private boolean        isDirty;
    private boolean        initialized;
    private Group          trafficlight;
    private Shape          redOn;
    private Shape          highlightRedOn;
    private Shape          yellowOn;
    private Shape          highlightYellowOn;
    private Shape          greenOn;
    private Shape          highlightGreenOn;
    private boolean        on;
    private AnimationTimer timer;
    private long           lastTimerCall;


    // ******************** Constructors **************************************
    public TrafficLightSkin(final TrafficLight CONTROL) {
        super(CONTROL, new TrafficLightBehavior(CONTROL));
        control           = CONTROL;
        initialized       = false;
        isDirty           = false;
        redOn             = new Circle();
        highlightRedOn    = new Ellipse();
        yellowOn          = new Circle();
        highlightYellowOn = new Ellipse();
        greenOn           = new Circle();
        highlightGreenOn  = new Ellipse();
        trafficlight      = new Group();
        timer             = new AnimationTimer() {
            @Override
            public void handle(long l) {
                long currentNanoTime = System.nanoTime();
                if (currentNanoTime > lastTimerCall + BLINK_INTERVAL) {
                    on ^= true;
                    if (control.isRedBlinking()) {
                        redOn.setVisible(on);
                        highlightRedOn.setVisible(on);
                    }
                    if (control.isYellowBlinking()) {
                        yellowOn.setVisible(on);
                        highlightYellowOn.setVisible(on);
                    }
                    if (control.isGreenBlinking()) {
                        greenOn.setVisible(on);
                        highlightGreenOn.setVisible(on);
                    }
                    lastTimerCall = currentNanoTime;
                }
            }
        };
        lastTimerCall = 0l;

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(80, 200);
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

        // Register listeners
        registerChangeListener(control.redOnProperty(), "RED");
        registerChangeListener(control.redBlinkingProperty(), "RED_BLINKING");
        registerChangeListener(control.yellowOnProperty(), "YELLOW");
        registerChangeListener(control.yellowBlinkingProperty(), "YELLOW_BLINKING");
        registerChangeListener(control.greenOnProperty(), "GREEN");
        registerChangeListener(control.greenBlinkingProperty(), "GREEN_BLINKING");
        registerChangeListener(control.darkBackgroundProperty(), "DARK_BACKGROUND");

        timer.start();

        initialized = true;
        paint();
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        getChildren().clear();
        drawTrafficLight();

        getChildren().add(trafficlight);
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("RED".equals(PROPERTY)) {
            redOn.setVisible(control.isRedOn());
            highlightRedOn.setVisible(control.isRedOn());
        } else if ("RED_BLINKING".equals(PROPERTY)) {

        } else if ("YELLOW".equals(PROPERTY)) {
            yellowOn.setVisible(control.isYellowOn());
            highlightYellowOn.setVisible(control.isYellowOn());
        } else if ("YELLOW_BLINKING".equals(PROPERTY)) {

        } else if ("GREEN".equals(PROPERTY)) {
            greenOn.setVisible(control.isGreenOn());
            highlightGreenOn.setVisible(control.isGreenOn());
        } else if ("GREEN_BLINKING".equals(PROPERTY)) {

        } else if ("DARK_BACKGROUND".equals(PROPERTY)) {
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

    @Override public final TrafficLight getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 80;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 200;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }


    // ******************** Drawing related ***********************************
    public final void drawTrafficLight() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        trafficlight.getChildren().clear();

        final Rectangle HOUSING_BACKGROUND = new Rectangle(0.125 * WIDTH, 0.055 * HEIGHT, 0.75 * WIDTH, 0.9 * HEIGHT);
        HOUSING_BACKGROUND.setArcWidth(0.75 * WIDTH);
        HOUSING_BACKGROUND.setArcHeight(0.3 * HEIGHT);
        final Paint HOUSING_BACKGROUND_FILL = control.isDarkBackground() ? Color.color(0.2, 0.2, 0.2, 0.6) : Color.color(0.8, 0.8, 0.8, 0.6);
        HOUSING_BACKGROUND.setFill(HOUSING_BACKGROUND_FILL);
        HOUSING_BACKGROUND.setStroke(null);

        final Path HOUSING_FRAME = new Path();
        HOUSING_FRAME.setFillRule(FillRule.EVEN_ODD);
        HOUSING_FRAME.getElements().add(new MoveTo(0.125 * WIDTH, 0.205 * HEIGHT));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(0.125 * WIDTH, 0.12 * HEIGHT,
                                                         0.2875 * WIDTH, 0.055 * HEIGHT,
                                                         0.5 * WIDTH, 0.055 * HEIGHT));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(0.7125 * WIDTH, 0.055 * HEIGHT,
                                                         0.875 * WIDTH, 0.12 * HEIGHT,
                                                         0.875 * WIDTH, 0.205 * HEIGHT));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(0.875 * WIDTH, 0.205 * HEIGHT,
                                                         0.875 * WIDTH, 0.805 * HEIGHT,
                                                         0.875 * WIDTH, 0.805 * HEIGHT));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(0.875 * WIDTH, 0.89 * HEIGHT,
                                                         0.7125 * WIDTH, 0.955 * HEIGHT,
                                                         0.5 * WIDTH, 0.955 * HEIGHT));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(0.2875 * WIDTH, 0.955 * HEIGHT,
                                                         0.125 * WIDTH, 0.89 * HEIGHT,
                                                         0.125 * WIDTH, 0.805 * HEIGHT));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(0.125 * WIDTH, 0.805 * HEIGHT,
                                                         0.125 * WIDTH, 0.205 * HEIGHT,
                                                         0.125 * WIDTH, 0.205 * HEIGHT));
        HOUSING_FRAME.getElements().add(new ClosePath());
        HOUSING_FRAME.getElements().add(new MoveTo(0.0, 0.2 * HEIGHT));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(0.0, 0.2 * HEIGHT,
                                                         0.0, 0.8 * HEIGHT,
                                                         0.0, 0.8 * HEIGHT));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(0.0, 0.91 * HEIGHT,
                                                         0.225 * WIDTH, HEIGHT,
                                                         0.5 * WIDTH, HEIGHT));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(0.775 * WIDTH, HEIGHT,
                                                         WIDTH, 0.91 * HEIGHT,
                                                         WIDTH, 0.8 * HEIGHT));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(WIDTH, 0.8 * HEIGHT,
                                                         WIDTH, 0.2 * HEIGHT,
                                                         WIDTH, 0.2 * HEIGHT));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(WIDTH, 0.09 * HEIGHT,
                                                         0.775 * WIDTH, 0.0,
                                                         0.5 * WIDTH, 0.0));
        HOUSING_FRAME.getElements().add(new CubicCurveTo(0.225 * WIDTH, 0.0,
                                                         0.0, 0.09 * HEIGHT,
                                                         0.0, 0.2 * HEIGHT));
        HOUSING_FRAME.getElements().add(new ClosePath());
        final Paint HOUSING_FRAME_FILL = control.isDarkBackground() ? Color.color(0.8, 0.8, 0.8, 0.6) : Color.color(0.2, 0.2, 0.2, 0.6);
        HOUSING_FRAME.setFill(HOUSING_FRAME_FILL);
        HOUSING_FRAME.setStroke(null);

        trafficlight.getChildren().addAll(HOUSING_BACKGROUND, HOUSING_FRAME);


        // ******************** SHADOW & GLOW *********************************
        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setWidth(0.140625 * WIDTH);
        INNER_SHADOW.setHeight(0.140625 * WIDTH);
        INNER_SHADOW.setRadius(0.140625 * WIDTH);
        INNER_SHADOW.setColor(Color.BLACK);
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final DropShadow RED_GLOW = new DropShadow();
        RED_GLOW.setWidth(0.25 * WIDTH);
        RED_GLOW.setHeight(0.25 * WIDTH);
        RED_GLOW.setRadius(0.18 * WIDTH);
        RED_GLOW.setColor(Color.RED);
        RED_GLOW.setBlurType(BlurType.GAUSSIAN);
        RED_GLOW.inputProperty().set(INNER_SHADOW);

        final DropShadow YELLOW_GLOW = new DropShadow();
        YELLOW_GLOW.setWidth(0.25 * WIDTH);
        YELLOW_GLOW.setHeight(0.25 * WIDTH);
        YELLOW_GLOW.setRadius(0.18 * WIDTH);
        YELLOW_GLOW.setColor(Color.YELLOW);
        YELLOW_GLOW.setBlurType(BlurType.GAUSSIAN);
        YELLOW_GLOW.inputProperty().set(INNER_SHADOW);

        final DropShadow GREEN_GLOW = new DropShadow();
        GREEN_GLOW.setWidth(0.25 * WIDTH);
        GREEN_GLOW.setHeight(0.25 * WIDTH);
        GREEN_GLOW.setRadius(0.18 * WIDTH);
        GREEN_GLOW.setColor(Color.LIME);
        GREEN_GLOW.setBlurType(BlurType.GAUSSIAN);
        GREEN_GLOW.inputProperty().set(INNER_SHADOW);


        // ******************** RED *******************************************
        final Circle redOff = new Circle(0.5 * WIDTH, 0.2 * HEIGHT, 0.3125 * WIDTH);
        final Paint RED_OFF_FILL = new RadialGradient(0, 0,
                                                      0.5 * WIDTH, 0.285 * HEIGHT,
                                                      0.59375 * WIDTH,
                                                      false, CycleMethod.NO_CYCLE,
                                                      new Stop(0.0, Color.color(0.3019607843, 0, 0, 1)),
                                                      new Stop(0.98, Color.color(0.0039215686, 0, 0, 1)),
                                                      new Stop(0.99, Color.BLACK),
                                                      new Stop(1.0, Color.BLACK));
        redOff.setFill(RED_OFF_FILL);
        redOff.setStroke(null);
        redOff.setEffect(INNER_SHADOW);

        final Ellipse HIGHLIGHT_RED_OFF = new Ellipse(0.49375 * WIDTH, 0.13 * HEIGHT, 0.23125 * WIDTH, 0.05 * HEIGHT);
        final Paint HIGHLIGHT_RED_OFF_FILL = new RadialGradient(0, 0,
                                                                0.5 * WIDTH, 0.105 * HEIGHT,
                                                                0.2125 * WIDTH,
                                                                false, CycleMethod.NO_CYCLE,
                                                                new Stop(0.0, Color.color(1, 1, 1, 0.2235294118)),
                                                                new Stop(0.98, Color.color(1, 1, 1, 0.0274509804)),
                                                                new Stop(1.0, Color.color(1, 1, 1, 0.0274509804)));
        HIGHLIGHT_RED_OFF.setFill(HIGHLIGHT_RED_OFF_FILL);
        HIGHLIGHT_RED_OFF.setStroke(null);

        redOn = new Circle(0.5 * WIDTH, 0.2 * HEIGHT, 0.3125 * WIDTH);
        final Paint RED_ON_FILL = new RadialGradient(0, 0,
                                                     0.5 * WIDTH, 0.285 * HEIGHT,
                                                     0.59375 * WIDTH,
                                                     false, CycleMethod.NO_CYCLE,
                                                     new Stop(0.0, Color.RED),
                                                     new Stop(0.98, Color.color(0.2549019608, 0, 0, 1)),
                                                     new Stop(0.99, Color.color(0.2470588235, 0, 0, 1)),
                                                     new Stop(1.0, Color.color(0.2470588235, 0, 0, 1)));
        redOn.setFill(RED_ON_FILL);
        redOn.setStroke(null);
        redOn.setEffect(RED_GLOW);
        redOn.setVisible(control.isRedOn());

        highlightRedOn = new Ellipse(0.49375 * WIDTH, 0.13 * HEIGHT, 0.23125 * WIDTH, 0.05 * HEIGHT);
        final Paint HIGHLIGHT_RED_ON_FILL = new RadialGradient(0, 0,
                                                               0.5 * WIDTH, 0.105 * HEIGHT,
                                                               0.2125 * WIDTH,
                                                               false, CycleMethod.NO_CYCLE,
                                                               new Stop(0.0, Color.color(1, 1, 1, 0.6745098039)),
                                                               new Stop(0.98, Color.color(1, 1, 1, 0.0862745098)),
                                                               new Stop(1.0, Color.color(1, 1, 1, 0.0862745098)));
        highlightRedOn.setFill(HIGHLIGHT_RED_ON_FILL);
        highlightRedOn.setStroke(null);
        highlightRedOn.setVisible(control.isRedOn());

        trafficlight.getChildren().addAll(redOff,
                                          HIGHLIGHT_RED_OFF,
                                          redOn,
                                          highlightRedOn);


        // ******************** YELLOW ****************************************
        final Circle yellowOff = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.3125 * WIDTH);
        final Paint YELLOW_OFF_FILL = new RadialGradient(0, 0,
                                                         0.5 * WIDTH, 0.585 * HEIGHT,
                                                         0.59375 * WIDTH,
                                                         false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, Color.color(0.3254901961, 0.3333333333, 0, 1)),
                                                         new Stop(0.98, Color.color(0.0039215686, 0.0039215686, 0, 1)),
                                                         new Stop(0.99, Color.BLACK),
                                                         new Stop(1.0, Color.BLACK));
        yellowOff.setFill(YELLOW_OFF_FILL);
        yellowOff.setStroke(null);
        yellowOff.setEffect(INNER_SHADOW);

        final Ellipse HIGHLIGHT_YELLOW_OFF = new Ellipse(0.49375 * WIDTH, 0.43 * HEIGHT, 0.23125 * WIDTH, 0.05 * HEIGHT);
        final Paint HIGHLIGHT_YELLOW_OFF_FILL = new RadialGradient(0, 0,
                                                                   0.5 * WIDTH, 0.405 * HEIGHT,
                                                                   0.2125 * WIDTH,
                                                                   false, CycleMethod.NO_CYCLE,
                                                                   new Stop(0.0, Color.color(1, 1, 1, 0.2235294118)),
                                                                   new Stop(0.98, Color.color(1, 1, 1, 0.0274509804)),
                                                                   new Stop(1.0, Color.color(1, 1, 1, 0.0274509804)));
        HIGHLIGHT_YELLOW_OFF.setFill(HIGHLIGHT_YELLOW_OFF_FILL);
        HIGHLIGHT_YELLOW_OFF.setStroke(null);

        yellowOn = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.3125 * WIDTH);
        final Paint YELLOW_ON_FILL = new RadialGradient(0, 0,
                                                        0.5 * WIDTH, 0.585 * HEIGHT,
                                                        0.59375 * WIDTH,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.YELLOW),
                                                        new Stop(0.98, Color.color(0.3333333333, 0.3411764706, 0, 1)),
                                                        new Stop(0.99, Color.color(0.3254901961, 0.3333333333, 0, 1)),
                                                        new Stop(1.0, Color.color(0.3254901961, 0.3333333333, 0, 1)));
        yellowOn.setFill(YELLOW_ON_FILL);
        yellowOn.setStroke(null);
        yellowOn.setEffect(YELLOW_GLOW);
        yellowOn.setVisible(control.isYellowOn());

        highlightYellowOn = new Ellipse(0.49375 * WIDTH, 0.43 * HEIGHT, 0.23125 * WIDTH, 0.05 * HEIGHT);
        final Paint HIGHLIGHT_YELLOW_ON_FILL = new RadialGradient(0, 0,
                                                                  0.5 * WIDTH, 0.405 * HEIGHT,
                                                                  0.2125 * WIDTH,
                                                                  false, CycleMethod.NO_CYCLE,
                                                                  new Stop(0.0, Color.color(1, 1, 1, 0.6745098039)),
                                                                  new Stop(0.98, Color.color(1, 1, 1, 0.0862745098)),
                                                                  new Stop(1.0, Color.color(1, 1, 1, 0.0862745098)));
        highlightYellowOn.setFill(HIGHLIGHT_YELLOW_ON_FILL);
        highlightYellowOn.setStroke(null);
        highlightYellowOn.setVisible(control.isYellowOn());

        trafficlight.getChildren().addAll(yellowOff,
                                          HIGHLIGHT_YELLOW_OFF,
                                          yellowOn,
                                          highlightYellowOn);


        // ******************** GREEN *****************************************
        final Circle greenOff = new Circle(0.5 * WIDTH, 0.8 * HEIGHT, 0.3125 * WIDTH);
        final Paint GREEN_OFF_FILL = new RadialGradient(0, 0,
                                                        0.5 * WIDTH, 0.885 * HEIGHT,
                                                        0.59375 * WIDTH,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.0980392157, 0.3372549020, 0, 1)),
                                                        new Stop(0.98, Color.color(0, 0.0039215686, 0, 1)),
                                                        new Stop(0.99, Color.BLACK),
                                                        new Stop(1.0, Color.BLACK));
        greenOff.setFill(GREEN_OFF_FILL);
        greenOff.setStroke(null);
        greenOff.setEffect(INNER_SHADOW);

        final Ellipse HIGHLIGHT_GREEN_OFF = new Ellipse(0.49375 * WIDTH, 0.73 * HEIGHT, 0.23125 * WIDTH, 0.05 * HEIGHT);
        final Paint HIGHLIGHT_OFF_GREEN_FILL = new RadialGradient(0, 0,
                                                                  0.5 * WIDTH, 0.705 * HEIGHT,
                                                                  0.2125 * WIDTH,
                                                                  false, CycleMethod.NO_CYCLE,
                                                                  new Stop(0.0, Color.color(1, 1, 1, 0.2235294118)),
                                                                  new Stop(0.98, Color.color(1, 1, 1, 0.0274509804)),
                                                                  new Stop(1.0, Color.color(1, 1, 1, 0.0274509804)));
        HIGHLIGHT_GREEN_OFF.setFill(HIGHLIGHT_OFF_GREEN_FILL);
        HIGHLIGHT_GREEN_OFF.setStroke(null);

        greenOn = new Circle(0.5 * WIDTH, 0.8 * HEIGHT, 0.3125 * WIDTH);
        final Paint GREEN_ON_FILL = new RadialGradient(0, 0,
                                                       0.5 * WIDTH, 0.885 * HEIGHT,
                                                       0.59375 * WIDTH,
                                                       false, CycleMethod.NO_CYCLE,
                                                       new Stop(0.0, Color.LIME),
                                                       new Stop(0.98, Color.color(0.1254901961, 0.2784313725, 0.1411764706, 1)),
                                                       new Stop(0.99, Color.color(0.1254901961, 0.2705882353, 0.1411764706, 1)),
                                                       new Stop(1.0, Color.color(0.1254901961, 0.2705882353, 0.1411764706, 1)));
        greenOn.setFill(GREEN_ON_FILL);
        greenOn.setStroke(null);
        greenOn.setEffect(GREEN_GLOW);
        greenOn.setVisible(control.isGreenOn());

        highlightGreenOn = new Ellipse(0.49375 * WIDTH, 0.73 * HEIGHT, 0.23125 * WIDTH, 0.05 * HEIGHT);
        final Paint HIGHLIGHT_GREEN_ON_FILL = new RadialGradient(0, 0,
                                                                 0.5 * WIDTH, 0.705 * HEIGHT,
                                                                 0.2125 * WIDTH,
                                                                 false, CycleMethod.NO_CYCLE,
                                                                 new Stop(0.0, Color.color(1, 1, 1, 0.6745098039)),
                                                                 new Stop(0.98, Color.color(1, 1, 1, 0.0862745098)),
                                                                 new Stop(1.0, Color.color(1, 1, 1, 0.0862745098)));
        highlightGreenOn.setFill(HIGHLIGHT_GREEN_ON_FILL);
        highlightGreenOn.setStroke(null);
        highlightGreenOn.setVisible(control.isGreenOn());

        trafficlight.getChildren().addAll(greenOff,
                                          HIGHLIGHT_GREEN_OFF,
                                          greenOn,
                                          highlightGreenOn);
    }
}
