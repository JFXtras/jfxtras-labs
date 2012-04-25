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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
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
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import jfxtras.labs.internal.scene.control.behavior.SplitFlapBehavior;
import jfxtras.labs.scene.control.gauge.SplitFlap;


/**
 * Created by
 * User: hansolo
 * Date: 23.02.12
 * Time: 09:12
 */
public class SplitFlapSkin extends SkinBase<SplitFlap, SplitFlapBehavior> {
    private SplitFlap        control;
    private static double   MIN_FLIP_TIME = 16666666.6666667; // 60 fps
    private final AudioClip SOUND1 = new AudioClip(getClass().getResource("/jfxtras/labs/scene/control/gauge/flap.mp3").toExternalForm());
    private final AudioClip SOUND2 = new AudioClip(getClass().getResource("/jfxtras/labs/scene/control/gauge/flap1.mp3").toExternalForm());
    private final AudioClip SOUND3 = new AudioClip(getClass().getResource("/jfxtras/labs/scene/control/gauge/flap2.mp3").toExternalForm());
    private boolean         isDirty;
    private boolean         initialized;
    private Group           background;
    private Group           fixture;
    private Group           flip;
    private Group           frame;
    private Path            upper;
    private Text            upperText;
    private Path            upperNext;
    private Text            upperNextText;
    private Path            lower;
    private Text            lowerText;
    private Text            lowerNextText;
    private char            currentChar;
    private char            nextChar;
    private Rotate          rotate;
    private Rotate          lowerFlipVert;
    private double          angleStep;
    private double          currentAngle;
    private boolean         flipping;
    private AnimationTimer  timer;


    // ******************** Constructors **************************************
    public SplitFlapSkin(final SplitFlap CONTROL) {
        super(CONTROL, new SplitFlapBehavior(CONTROL));
        control               = CONTROL;
        initialized           = false;
        isDirty               = false;
        background            = new Group();
        fixture               = new Group();
        flip                  = new Group();
        frame                 = new Group();
        upperText             = new Text(Character.toString(control.getCharacter()));
        lowerText             = new Text(Character.toString(control.getCharacter()));
        upperNextText         = new Text(Character.toString((char) (control.getCharacter() + 1)));
        lowerNextText         = new Text(Character.toString((char) (control.getCharacter() + 1)));
        currentChar           = control.getCharacter();
        nextChar              = (char) (control.getCharacter() + 1);
        rotate                = new Rotate();
        angleStep             = 180.0 / ((control.getFlipTimeInMs() * 1000000) / (MIN_FLIP_TIME));
        currentAngle          = 0;
        flipping              = false;
        timer            = new AnimationTimer() {
            @Override public void handle(long l) {
                if (initialized) {
                    if (control.isCountdownMode()) {
                        flipBackward(angleStep);}
                    else {
                        flipForward(angleStep);
                    }
                }
            }
        };
        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(132, 227);
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

        rotate.setAxis(Rotate.X_AXIS);
        rotate.setPivotY(control.getPrefHeight() * 0.4625550661);

        lowerFlipVert = new Rotate();

        // Register listeners
        registerChangeListener(control.colorProperty(), "COLOR");
        registerChangeListener(control.characterColorProperty(), "CHARACTER_COLOR");
        registerChangeListener(control.characterProperty(), "CHARACTER");
        registerChangeListener(control.flipTimeInMsProperty(), "FLIP_TIME");
        registerChangeListener(control.frameVisibleProperty(), "FRAME_VISIBILITY");
        registerChangeListener(control.backgroundVisibleProperty(), "BACKGROUND_VISIBILITY");
        registerChangeListener(control.countdownModeProperty(), "COUNTDOWN_MODE");

        frame.setVisible(control.isFrameVisible());
        background.setVisible(control.isBackgroundVisible());

        initialized = true;
        paint();
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        getChildren().clear();
        drawBackground();
        drawFixture();
        drawFlip();
        drawFrame();
        getChildren().addAll(background,
                             fixture,
                             flip,
                             frame);
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if (PROPERTY == "COLOR") {
            paint();
        } else if (PROPERTY == "CHARACTER_COLOR") {
            paint();
        } else if (PROPERTY == "CHARACTER") {
            if (control.getCharacter() != currentChar) {
                timer.stop();
                flipping = true;
                timer.start();
            }
        } else if (PROPERTY == "FLIP_TIME") {
            angleStep = 180.0 / ((control.getFlipTimeInMs() * 1000000) / (MIN_FLIP_TIME));
        } else if (PROPERTY == "FRAME_VISIBILITY") {
            frame.setVisible(control.isFrameVisible());
        } else if (PROPERTY == "BACKGROUND_VISIBILITY") {
            background.setVisible(control.isBackgroundVisible());
        } else if (PROPERTY == "COUNDOWN_MODE") {
            currentAngle = 180;
        }
    }

    @Override public void layoutChildren() {
        if (isDirty) {
            paint();
            isDirty = false;
        }
        super.layoutChildren();
    }

    @Override public final SplitFlap getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 132;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 227;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }

    private void flipForward(final double ANGLE) {
        currentAngle += ANGLE;
        if (Double.compare(currentAngle, 180) >= 0) {
            if (control.isSoundOn()) {
                switch(control.getSound()) {
                    case SOUND1:
                        SOUND1.play();
                        break;
                    case SOUND2:
                        SOUND2.play();
                        break;
                    case SOUND3:
                        SOUND3.play();
                }
            }
            currentAngle = 0;
            upper.getTransforms().clear();
            upperText.getTransforms().clear();
            lowerNextText.getTransforms().clear();
            lowerNextText.setVisible(false);
            lowerFlipVert.setAxis(Rotate.X_AXIS);
            lowerFlipVert.setPivotY(control.getPrefHeight() * 0.4625550661);
            lowerFlipVert.setAngle(180);
            lowerNextText.getTransforms().add(lowerFlipVert);
            upperText.setVisible(true);

            currentChar++;
            if (currentChar > control.getType().UPPER_BOUND || currentChar < control.getType().LOWER_BOUND) {
                if (control.getCharacter() == 32) {
                    currentChar = 32;
                } else {
                    currentChar = (char) control.getType().LOWER_BOUND;
                }
            }
            nextChar = (char) (currentChar + 1);
            if (nextChar > control.getType().UPPER_BOUND || nextChar < control.getType().LOWER_BOUND) {
                if (control.getCharacter() == 32) {
                    nextChar = 32;
                } else {
                    nextChar = (char) control.getType().LOWER_BOUND;
                }
            }
            if (currentChar == control.getCharacter()) {
                timer.stop();
                flipping = false;
            }
            upperText.setText(Character.toString(currentChar));
            lowerText.setText(Character.toString(currentChar));
            upperNextText.setText(Character.toString(nextChar));
            lowerNextText.setText(Character.toString(nextChar));
        }
        if (currentAngle > 90) {
            upperText.setVisible(false);
            lowerNextText.setVisible(true);
        }
        if (flipping) {
            rotate.setAngle(ANGLE);
            upper.getTransforms().add(rotate);
            upperText.getTransforms().add(rotate);
            lowerNextText.getTransforms().add(rotate);
        }
    }

    private void flipBackward(final double ANGLE) {
        currentAngle -= ANGLE;
        if (Double.compare(currentAngle, 0) >= 0) {
            currentAngle = 0;
            upper.getTransforms().clear();
            upperText.getTransforms().clear();
            lowerNextText.getTransforms().clear();
            lowerNextText.setVisible(false);
            lowerFlipVert.setAxis(Rotate.X_AXIS);
            lowerFlipVert.setPivotY(control.getPrefHeight() * 0.04 + lowerNextText.getLayoutBounds().getHeight() / 2);
            lowerFlipVert.setAngle(0);
            lowerNextText.getTransforms().add(lowerFlipVert);
            upperText.setVisible(true);

            currentChar--;
            if (currentChar < control.getType().LOWER_BOUND) {
                if (control.getCharacter() == 32) {
                    currentChar = 32;
                } else {
                    currentChar = (char) control.getType().UPPER_BOUND;
                }
            }
            nextChar = (char) (currentChar - 1);
            if (nextChar < control.getType().LOWER_BOUND) {
                if (control.getCharacter() == 32) {
                    nextChar = 32;
                } else {
                    nextChar = (char) control.getType().UPPER_BOUND;
                }
            }
            if (currentChar == control.getCharacter()) {
                timer.stop();
                flipping = false;
            }
            upperText.setText(Character.toString(currentChar));
            lowerText.setText(Character.toString(currentChar));
            upperNextText.setText(Character.toString(nextChar));
            lowerNextText.setText(Character.toString(nextChar));
        }
        if (currentAngle > 90) {
            upperText.setVisible(false);
            lowerNextText.setVisible(true);
        } else {
            upperText.setVisible(true);
            lowerNextText.setVisible(false);
        }
        if (flipping) {
            rotate.setAngle(-ANGLE);
            upper.getTransforms().add(rotate);
            upperText.getTransforms().add(rotate);
            lowerNextText.getTransforms().add(rotate);
        }
    }


    // ******************** Mouse event handling ******************************
    private void addMouseEventListener(final Shape FLAP, final int FLAP_INDEX) {
        FLAP.setOnMousePressed(new EventHandler<MouseEvent>() {
            public void handle(final MouseEvent EVENT) {
                switch(FLAP_INDEX) {
                    case 1:
                        control.increase();
                        break;
                    case -1:
                        control.decrease();
                        break;
                }
            }
        });
    }


    // ******************** Drawing related ***********************************
    public final void drawBackground() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();
        background.getChildren().clear();
        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        background.getChildren().add(IBOUNDS);

        final Rectangle INNER_BACKGROUND = new Rectangle(0.0606060606 * WIDTH, 0.0352422907 * HEIGHT,
                                                         0.8787878788 * WIDTH,0.9207048458 * HEIGHT);
        final Paint INNER_BACKGROUND_FILL = new LinearGradient(0, 0.0352422907 * HEIGHT,
                                                               0, 0.9559471366 * HEIGHT,
                                                               false, CycleMethod.NO_CYCLE,
                                                               new Stop(0.0, Color.BLACK),
                                                               new Stop(1.0, Color.rgb(20, 20, 20)));
        INNER_BACKGROUND.setFill(INNER_BACKGROUND_FILL);
        INNER_BACKGROUND.setStroke(null);

        final Path LOWER3 = new Path();
        LOWER3.setFillRule(FillRule.EVEN_ODD);
        LOWER3.getElements().add(new MoveTo(0.8712121212121212 * WIDTH, 0.947136563876652 * HEIGHT));
        LOWER3.getElements().add(new CubicCurveTo(0.9090909090909091 * WIDTH, 0.947136563876652 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.933920704845815 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.9162995594713657 * HEIGHT));
        LOWER3.getElements().add(new CubicCurveTo(0.9242424242424242 * WIDTH, 0.9162995594713657 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.8149779735682819 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.8149779735682819 * HEIGHT));
        LOWER3.getElements().add(new LineTo(0.07575757575757576 * WIDTH, 0.8149779735682819 * HEIGHT));
        LOWER3.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.8149779735682819 * HEIGHT,
                                                  0.07575757575757576 * WIDTH, 0.9162995594713657 * HEIGHT,
                                                  0.07575757575757576 * WIDTH, 0.9162995594713657 * HEIGHT));
        LOWER3.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.933920704845815 * HEIGHT,
                                                  0.09090909090909091 * WIDTH, 0.947136563876652 * HEIGHT,
                                                  0.12878787878787878 * WIDTH, 0.947136563876652 * HEIGHT));
        LOWER3.getElements().add(new CubicCurveTo(0.12878787878787878 * WIDTH, 0.947136563876652 * HEIGHT,
                                                  0.8712121212121212 * WIDTH, 0.947136563876652 * HEIGHT,
                                                  0.8712121212121212 * WIDTH, 0.947136563876652 * HEIGHT));
        LOWER3.getElements().add(new ClosePath());
        final Paint LOWER3_FILL = new LinearGradient(0.5378787878787878 * WIDTH, 0.5374449339207048 * HEIGHT,
                                                     0.5378787878787878 * WIDTH, 0.9427312775330396 * HEIGHT,
                                                     false, CycleMethod.NO_CYCLE,
                                                     new Stop(0.0, control.getLowerFlapTopColor()),
                                                     new Stop(1.0, control.getLowerFlapTopColor()));
        LOWER3.setFill(LOWER3_FILL);
        LOWER3.setStroke(null);

        final InnerShadow LOWER_INNER_SHADOW = new InnerShadow();
        LOWER_INNER_SHADOW.setWidth(0.075 * LOWER3.getLayoutBounds().getWidth());
        LOWER_INNER_SHADOW.setHeight(0.28 * LOWER3.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setOffsetX(0.0);
        LOWER_INNER_SHADOW.setOffsetY(0.0);
        LOWER_INNER_SHADOW.setRadius(0.075 * LOWER3.getLayoutBounds().getWidth());
        LOWER_INNER_SHADOW.setColor(Color.BLACK);
        LOWER_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow LOWER_LIGHT_EFFECT = new InnerShadow();
        LOWER_LIGHT_EFFECT.setWidth(0.05 * LOWER3.getLayoutBounds().getWidth());
        LOWER_LIGHT_EFFECT.setHeight(0.1866666667 * LOWER3.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setOffsetX(0);
        LOWER_LIGHT_EFFECT.setOffsetY(0.018 * SIZE);
        LOWER_LIGHT_EFFECT.setRadius(0.05 * LOWER3.getLayoutBounds().getWidth());
        LOWER_LIGHT_EFFECT.setColor(Color.WHITE);
        LOWER_LIGHT_EFFECT.setBlurType(BlurType.GAUSSIAN);
        LOWER_LIGHT_EFFECT.inputProperty().set(LOWER_INNER_SHADOW);

        LOWER3.setEffect(LOWER_INNER_SHADOW);

        final Path LOWER2 = new Path();
        LOWER2.setFillRule(FillRule.EVEN_ODD);
        LOWER2.getElements().add(new MoveTo(0.8712121212121212 * WIDTH, 0.9295154185022027 * HEIGHT));
        LOWER2.getElements().add(new CubicCurveTo(0.9090909090909091 * WIDTH, 0.9295154185022027 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.9162995594713657 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.8986784140969163 * HEIGHT));
        LOWER2.getElements().add(new CubicCurveTo(0.9242424242424242 * WIDTH, 0.8986784140969163 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.7973568281938326 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.7973568281938326 * HEIGHT));
        LOWER2.getElements().add(new LineTo(0.07575757575757576 * WIDTH, 0.7973568281938326 * HEIGHT));
        LOWER2.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.7973568281938326 * HEIGHT,
                                                  0.07575757575757576 * WIDTH, 0.8986784140969163 * HEIGHT,
                                                  0.07575757575757576 * WIDTH, 0.8986784140969163 * HEIGHT));
        LOWER2.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.9162995594713657 * HEIGHT,
                                                  0.09090909090909091 * WIDTH, 0.9295154185022027 * HEIGHT,
                                                  0.12878787878787878 * WIDTH, 0.9295154185022027 * HEIGHT));
        LOWER2.getElements().add(new CubicCurveTo(0.12878787878787878 * WIDTH, 0.9295154185022027 * HEIGHT,
                                                  0.8712121212121212 * WIDTH, 0.9295154185022027 * HEIGHT,
                                                  0.8712121212121212 * WIDTH, 0.9295154185022027 * HEIGHT));
        LOWER2.getElements().add(new ClosePath());
        final Paint LOWER2_FILL = new LinearGradient(0.5378787878787878 * WIDTH, 0.5198237885462555 * HEIGHT,
                                                     0.5378787878787878 * WIDTH, 0.9251101321585903 * HEIGHT,
                                                     false, CycleMethod.NO_CYCLE,
                                                     new Stop(0.0, control.getLowerFlapTopColor()),
                                                     new Stop(1.0, control.getLowerFlapTopColor()));
        LOWER2.setFill(LOWER2_FILL);
        LOWER2.setStroke(null);
        LOWER2.setEffect(LOWER_INNER_SHADOW);

        final Path LOWER1 = new Path();
        LOWER1.setFillRule(FillRule.EVEN_ODD);
        LOWER1.getElements().add(new MoveTo(0.8712121212121212 * WIDTH, 0.9118942731277533 * HEIGHT));
        LOWER1.getElements().add(new CubicCurveTo(0.9090909090909091 * WIDTH, 0.9118942731277533 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.8986784140969163 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.8810572687224669 * HEIGHT));
        LOWER1.getElements().add(new CubicCurveTo(0.9242424242424242 * WIDTH, 0.8810572687224669 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.7797356828193832 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.7797356828193832 * HEIGHT));
        LOWER1.getElements().add(new LineTo(0.07575757575757576 * WIDTH, 0.7797356828193832 * HEIGHT));
        LOWER1.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.7797356828193832 * HEIGHT,
                                                  0.07575757575757576 * WIDTH, 0.8810572687224669 * HEIGHT,
                                                  0.07575757575757576 * WIDTH, 0.8810572687224669 * HEIGHT));
        LOWER1.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.8986784140969163 * HEIGHT,
                                                  0.09090909090909091 * WIDTH, 0.9118942731277533 * HEIGHT,
                                                  0.12878787878787878 * WIDTH, 0.9118942731277533 * HEIGHT));
        LOWER1.getElements().add(new CubicCurveTo(0.12878787878787878 * WIDTH, 0.9118942731277533 * HEIGHT,
                                                  0.8712121212121212 * WIDTH, 0.9118942731277533 * HEIGHT,
                                                  0.8712121212121212 * WIDTH, 0.9118942731277533 * HEIGHT));
        LOWER1.getElements().add(new ClosePath());
        final Paint LOWER1_FILL = new LinearGradient(0.5378787878787878 * WIDTH, 0.5022026431718062 * HEIGHT,
                                                     0.5378787878787878 * WIDTH, 0.9074889867841409 * HEIGHT,
                                                     false, CycleMethod.NO_CYCLE,
                                                     new Stop(0.0, control.getLowerFlapTopColor()),
                                                     new Stop(1.0, control.getLowerFlapTopColor()));
        LOWER1.setFill(LOWER1_FILL);
        LOWER1.setStroke(null);
        LOWER1.setEffect(LOWER_INNER_SHADOW);

        final Path LOWER0 = new Path();
        LOWER0.setFillRule(FillRule.EVEN_ODD);
        LOWER0.getElements().add(new MoveTo(0.8712121212121212 * WIDTH, 0.8942731277533039 * HEIGHT));
        LOWER0.getElements().add(new CubicCurveTo(0.9090909090909091 * WIDTH, 0.8942731277533039 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.8810572687224669 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.8634361233480177 * HEIGHT));
        LOWER0.getElements().add(new CubicCurveTo(0.9242424242424242 * WIDTH, 0.8634361233480177 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.762114537444934 * HEIGHT,
                                                  0.9242424242424242 * WIDTH, 0.762114537444934 * HEIGHT));
        LOWER0.getElements().add(new LineTo(0.07575757575757576 * WIDTH, 0.762114537444934 * HEIGHT));
        LOWER0.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.762114537444934 * HEIGHT,
                                                  0.07575757575757576 * WIDTH, 0.8634361233480177 * HEIGHT,
                                                  0.07575757575757576 * WIDTH, 0.8634361233480177 * HEIGHT));
        LOWER0.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.8810572687224669 * HEIGHT,
                                                  0.09090909090909091 * WIDTH, 0.8942731277533039 * HEIGHT,
                                                  0.12878787878787878 * WIDTH, 0.8942731277533039 * HEIGHT));
        LOWER0.getElements().add(new CubicCurveTo(0.12878787878787878 * WIDTH, 0.8942731277533039 * HEIGHT,
                                                  0.8712121212121212 * WIDTH, 0.8942731277533039 * HEIGHT,
                                                  0.8712121212121212 * WIDTH, 0.8942731277533039 * HEIGHT));
        LOWER0.getElements().add(new ClosePath());
        final Paint LOWER0_FILL = new LinearGradient(0.5378787878787878 * WIDTH, 0.4845814977973568 * HEIGHT,
                                                     0.5378787878787878 * WIDTH, 0.8898678414096917 * HEIGHT,
                                                     false, CycleMethod.NO_CYCLE,
                                                     new Stop(0.0, control.getLowerFlapTopColor()),
                                                     new Stop(1.0, control.getLowerFlapTopColor()));
        LOWER0.setFill(LOWER0_FILL);
        LOWER0.setStroke(null);
        LOWER0.setEffect(LOWER_INNER_SHADOW);

        background.getChildren().addAll(INNER_BACKGROUND,
                                        LOWER3,
                                        LOWER2,
                                        LOWER1,
                                        LOWER0);

        background.setCache(true);
    }

    public void drawFixture() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();
        fixture.getChildren().clear();
        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        fixture.getChildren().add(IBOUNDS);
        final Rectangle RIGHTFRAME = new Rectangle(0.8560606060606061 * WIDTH, 0.3920704845814978 * HEIGHT,
                                                   0.06818181818181818 * WIDTH, 0.13656387665198239 * HEIGHT);
        //RIGHTFRAME.setId("fixture-rightframe");
        final Paint RIGHTFRAME_FILL = new LinearGradient(0.8939393939393939 * WIDTH, 0.3920704845814978 * HEIGHT,
                                                         0.8939393939393939 * WIDTH, 0.5286343612334802 * HEIGHT,
                                                         false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                         new Stop(0.18, Color.color(0.6117647059, 0.6117647059, 0.6117647059, 1)),
                                                         new Stop(0.65, Color.color(0.1843137255, 0.1843137255, 0.1843137255, 1)),
                                                         new Stop(0.89, Color.color(0.3294117647, 0.3372549020, 0.3333333333, 1)),
                                                         new Stop(1.0, Color.color(0.2156862745, 0.2156862745, 0.2156862745, 1)));
        RIGHTFRAME.setFill(RIGHTFRAME_FILL);
        RIGHTFRAME.setStroke(null);

        final Rectangle RIGHTMAIN = new Rectangle(0.8636363636363636 * WIDTH, 0.3964757709251101 * HEIGHT,
                                                  0.05303030303030303 * WIDTH, 0.1277533039647577 * HEIGHT);
        //RIGHTMAIN.setId("fixture-rightmain");
        final Paint RIGHTMAIN_FILL = new LinearGradient(0.8939393939393939 * WIDTH, 0.3964757709251101 * HEIGHT,
                                                        0.8939393939393939 * WIDTH, 0.5242290748898678 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.4549019608, 0.4549019608, 0.4549019608, 1)),
                                                        new Stop(0.13, Color.color(0.8352941176, 0.8352941176, 0.8352941176, 1)),
                                                        new Stop(0.66, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                        new Stop(0.73, Color.color(0.2509803922, 0.2509803922, 0.2509803922, 1)),
                                                        new Stop(0.9, Color.color(0.4274509804, 0.4274509804, 0.4274509804, 1)),
                                                        new Stop(1.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)));
        RIGHTMAIN.setFill(RIGHTMAIN_FILL);
        RIGHTMAIN.setStroke(null);

        final Rectangle LEFTFRAME = new Rectangle(0.07575757575757576 * WIDTH, 0.3920704845814978 * HEIGHT,
                                                  0.06818181818181818 * WIDTH, 0.13656387665198239 * HEIGHT);
        //LEFTFRAME.setId("fixture-leftframe");
        final Paint LEFTFRAME_FILL = new LinearGradient(0.11363636363636363 * WIDTH, 0.3920704845814978 * HEIGHT,
                                                        0.11363636363636365 * WIDTH, 0.5286343612334802 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                        new Stop(0.18, Color.color(0.6117647059, 0.6117647059, 0.6117647059, 1)),
                                                        new Stop(0.65, Color.color(0.1843137255, 0.1843137255, 0.1843137255, 1)),
                                                        new Stop(0.89, Color.color(0.3294117647, 0.3372549020, 0.3333333333, 1)),
                                                        new Stop(1.0, Color.color(0.2156862745, 0.2156862745, 0.2156862745, 1)));
        LEFTFRAME.setFill(LEFTFRAME_FILL);
        LEFTFRAME.setStroke(null);

        final Rectangle LEFTMAIN = new Rectangle(0.08333333333333333 * WIDTH, 0.3964757709251101 * HEIGHT,
                                                 0.05303030303030303 * WIDTH, 0.1277533039647577 * HEIGHT);
        //LEFTMAIN.setId("fixture-leftmain");
        final Paint LEFTMAIN_FILL = new LinearGradient(0.11363636363636363 * WIDTH, 0.3964757709251101 * HEIGHT,
                                                       0.11363636363636365 * WIDTH, 0.5242290748898678 * HEIGHT,
                                                       false, CycleMethod.NO_CYCLE,
                                                       new Stop(0.0, Color.color(0.4549019608, 0.4549019608, 0.4549019608, 1)),
                                                       new Stop(0.13, Color.color(0.8352941176, 0.8352941176, 0.8352941176, 1)),
                                                       new Stop(0.66, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                       new Stop(0.73, Color.color(0.2509803922, 0.2509803922, 0.2509803922, 1)),
                                                       new Stop(0.9, Color.color(0.4274509804, 0.4274509804, 0.4274509804, 1)),
                                                       new Stop(1.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)));
        LEFTMAIN.setFill(LEFTMAIN_FILL);
        LEFTMAIN.setStroke(null);

        final Rectangle AXIS = new Rectangle(0.1439393939 * WIDTH, 0.4537444934 * HEIGHT,
                                             0.7121212121 * WIDTH, 0.013215859 * HEIGHT);
        //AXIS.setId("fixture-axis");
        final Paint AXIS_FILL = new LinearGradient(0.4166666666666667 * WIDTH, 0.40969162995594716 * HEIGHT,
                                                   0.4166666666666667 * WIDTH, 0.42290748898678415 * HEIGHT,
                                                   false, CycleMethod.NO_CYCLE,
                                                   new Stop(0.0, Color.color(0.4549019608, 0.4549019608, 0.4549019608, 1)),
                                                   new Stop(0.41, Color.color(0.8352941176, 0.8352941176, 0.8352941176, 1)),
                                                   new Stop(0.72, Color.color(0.2196078431, 0.2196078431, 0.2196078431, 1)),
                                                   new Stop(1.0, Color.color(0.3254901961, 0.3254901961, 0.3254901961, 1)));
        AXIS.setFill(AXIS_FILL);
        AXIS.setStroke(null);

        fixture.getChildren().addAll(RIGHTFRAME,
                                     RIGHTMAIN,
                                     LEFTFRAME,
                                     LEFTMAIN,
                                     AXIS);

        fixture.setCache(true);
    }

    public final void drawFlip() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        flip.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        flip.getChildren().add(IBOUNDS);

        lower = new Path();
        lower.setFillRule(FillRule.EVEN_ODD);
        lower.getElements().add(new MoveTo(0.8712121212121212 * WIDTH, 0.8766519823788547 * HEIGHT));
        lower.getElements().add(new CubicCurveTo(0.9090909090909091 * WIDTH, 0.8766519823788547 * HEIGHT,
                                                 0.9242424242424242 * WIDTH, 0.8634361233480177 * HEIGHT,
                                                 0.9242424242424242 * WIDTH, 0.8458149779735683 * HEIGHT));
        lower.getElements().add(new CubicCurveTo(0.9242424242424242 * WIDTH, 0.8458149779735683 * HEIGHT,
                                                 0.9242424242424242 * WIDTH, 0.5374449339207048 * HEIGHT,
                                                 0.9242424242424242 * WIDTH, 0.5374449339207048 * HEIGHT));
        lower.getElements().add(new LineTo(0.8409090909090909 * WIDTH, 0.5374449339207048 * HEIGHT));
        lower.getElements().add(new LineTo(0.8409090909090909 * WIDTH, 0.4669603524229075 * HEIGHT));
        lower.getElements().add(new LineTo(0.1590909090909091 * WIDTH, 0.4669603524229075 * HEIGHT));
        lower.getElements().add(new LineTo(0.1590909090909091 * WIDTH, 0.5374449339207048 * HEIGHT));
        lower.getElements().add(new LineTo(0.07575757575757576 * WIDTH, 0.5374449339207048 * HEIGHT));
        lower.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.5374449339207048 * HEIGHT,
                                                 0.07575757575757576 * WIDTH, 0.8458149779735683 * HEIGHT,
                                                 0.07575757575757576 * WIDTH, 0.8458149779735683 * HEIGHT));
        lower.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.8634361233480177 * HEIGHT,
                                                 0.09090909090909091 * WIDTH, 0.8766519823788547 * HEIGHT,
                                                 0.12878787878787878 * WIDTH, 0.8766519823788547 * HEIGHT));
        lower.getElements().add(new CubicCurveTo(0.12878787878787878 * WIDTH, 0.8766519823788547 * HEIGHT,
                                                 0.8712121212121212 * WIDTH, 0.8766519823788547 * HEIGHT,
                                                 0.8712121212121212 * WIDTH, 0.8766519823788547 * HEIGHT));
        lower.getElements().add(new ClosePath());
        final Paint LOWER_FILL = new LinearGradient(0.5378787878787878 * WIDTH, 0.4669603524229075 * HEIGHT,
                                                    0.5378787878787878 * WIDTH, 0.8722466960352423 * HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getLowerFlapTopColor()),
                                                    new Stop(1.0, control.getLowerFlapBottomColor()));
        lower.setFill(LOWER_FILL);
        lower.setStroke(null);
        lower.setCache(true);
        lower.setCacheHint(CacheHint.QUALITY);

        final InnerShadow LOWER_INNER_SHADOW = new InnerShadow();
        LOWER_INNER_SHADOW.setWidth(0.075 * lower.getLayoutBounds().getWidth());
        LOWER_INNER_SHADOW.setHeight(0.075 * lower.getLayoutBounds().getHeight());
        LOWER_INNER_SHADOW.setOffsetX(0.0);
        LOWER_INNER_SHADOW.setOffsetY(0.0);
        LOWER_INNER_SHADOW.setRadius(0.075 * lower.getLayoutBounds().getWidth());
        LOWER_INNER_SHADOW.setColor(Color.BLACK);
        LOWER_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow LOWER_LIGHT_EFFECT = new InnerShadow();
        LOWER_LIGHT_EFFECT.setWidth(0.05 * lower.getLayoutBounds().getWidth());
        LOWER_LIGHT_EFFECT.setHeight(0.05 * lower.getLayoutBounds().getHeight());
        LOWER_LIGHT_EFFECT.setOffsetX(0);
        LOWER_LIGHT_EFFECT.setOffsetY(0.013 * SIZE);
        LOWER_LIGHT_EFFECT.setRadius(0.05 * lower.getLayoutBounds().getWidth());
        LOWER_LIGHT_EFFECT.setColor(Color.color(1.0, 1.0, 1.0, 0.8));
        LOWER_LIGHT_EFFECT.setBlurType(BlurType.GAUSSIAN);
        LOWER_LIGHT_EFFECT.inputProperty().set(LOWER_INNER_SHADOW);
        lower.setEffect(LOWER_LIGHT_EFFECT);

        upper = new Path();
        upper.setFillRule(FillRule.EVEN_ODD);
        upper.getElements().add(new MoveTo(0.8712121212121212 * WIDTH, 0.04405286343612335 * HEIGHT));
        upper.getElements().add(new CubicCurveTo(0.9090909090909091 * WIDTH, 0.04405286343612335 * HEIGHT,
                                                 0.9242424242424242 * WIDTH, 0.05726872246696035 * HEIGHT,
                                                 0.9242424242424242 * WIDTH, 0.07488986784140969 * HEIGHT));
        upper.getElements().add(new CubicCurveTo(0.9242424242424242 * WIDTH, 0.07488986784140969 * HEIGHT,
                                                 0.9242424242424242 * WIDTH, 0.3832599118942731 * HEIGHT,
                                                 0.9242424242424242 * WIDTH, 0.3832599118942731 * HEIGHT));
        upper.getElements().add(new LineTo(0.8409090909090909 * WIDTH, 0.3832599118942731 * HEIGHT));
        upper.getElements().add(new LineTo(0.8409090909090909 * WIDTH, 0.45374449339207046 * HEIGHT));
        upper.getElements().add(new LineTo(0.1590909090909091 * WIDTH, 0.45374449339207046 * HEIGHT));
        upper.getElements().add(new LineTo(0.1590909090909091 * WIDTH, 0.3832599118942731 * HEIGHT));
        upper.getElements().add(new LineTo(0.07575757575757576 * WIDTH, 0.3832599118942731 * HEIGHT));
        upper.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.3832599118942731 * HEIGHT,
                                                 0.07575757575757576 * WIDTH, 0.07488986784140969 * HEIGHT,
                                                 0.07575757575757576 * WIDTH, 0.07488986784140969 * HEIGHT));
        upper.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.05726872246696035 * HEIGHT,
                                                 0.09090909090909091 * WIDTH, 0.04405286343612335 * HEIGHT,
                                                 0.12878787878787878 * WIDTH, 0.04405286343612335 * HEIGHT));
        upper.getElements().add(new CubicCurveTo(0.12878787878787878 * WIDTH, 0.04405286343612335 * HEIGHT,
                                                 0.8712121212121212 * WIDTH, 0.04405286343612335 * HEIGHT,
                                                 0.8712121212121212 * WIDTH, 0.04405286343612335 * HEIGHT));
        upper.getElements().add(new ClosePath());
        final Paint UPPER_FILL = new LinearGradient(0.5227272727272727 * WIDTH, 0.04405286343612335 * HEIGHT,
                                                    0.5227272727272727 * WIDTH, 0.45374449339207046 * HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getUpperFlapTopColor()),
                                                    new Stop(1.0, control.getUpperFlapBottomColor()));
        upper.setFill(UPPER_FILL);
        upper.setStroke(null);
        upper.setCache(true);
        upper.setCacheHint(CacheHint.SPEED);

        final InnerShadow UPPER_INNER_SHADOW = new InnerShadow();
        UPPER_INNER_SHADOW.setWidth(0.075 * upper.getLayoutBounds().getWidth());
        UPPER_INNER_SHADOW.setHeight(0.075 * upper.getLayoutBounds().getHeight());
        UPPER_INNER_SHADOW.setOffsetX(0.0);
        UPPER_INNER_SHADOW.setOffsetY(0.0);
        UPPER_INNER_SHADOW.setRadius(0.075 * upper.getLayoutBounds().getWidth());
        UPPER_INNER_SHADOW.setColor(Color.BLACK);
        UPPER_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final InnerShadow UPPER_LIGHT_EFFECT = new InnerShadow();
        UPPER_LIGHT_EFFECT.setWidth(0.05 * upper.getLayoutBounds().getWidth());
        UPPER_LIGHT_EFFECT.setHeight(0.05 * upper.getLayoutBounds().getHeight());
        UPPER_LIGHT_EFFECT.setOffsetX(0);
        UPPER_LIGHT_EFFECT.setOffsetY(0.013 * SIZE);
        UPPER_LIGHT_EFFECT.setRadius(0.05 * upper.getLayoutBounds().getWidth());
        UPPER_LIGHT_EFFECT.setColor(Color.color(1.0, 1.0, 1.0, 0.8));
        UPPER_LIGHT_EFFECT.setBlurType(BlurType.GAUSSIAN);
        UPPER_LIGHT_EFFECT.inputProperty().set(UPPER_INNER_SHADOW);
        upper.setEffect(UPPER_LIGHT_EFFECT);

        final Font FONT = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/droidsansmono.ttf"), (0.704845815 * HEIGHT));

        Rectangle upperClip = new Rectangle(0, upper.getLayoutBounds().getMinY(), WIDTH, upper.getLayoutBounds().getHeight());
        upperText.setTextOrigin(VPos.BOTTOM);
        upperText.setFont(FONT);
        upperText.setFontSmoothingType(FontSmoothingType.LCD);
        upperText.setText(Character.toString(control.getCharacter()));
        upperText.setX(((WIDTH - upperText.getLayoutBounds().getWidth()) / 2.0));
        upperText.setY(HEIGHT * 0.04 + upperText.getLayoutBounds().getHeight());
        upperText.setClip(upperClip);
        LinearGradient upperTextFill = new LinearGradient(0.0, upperText.getLayoutBounds().getMinY(),
                                                          0.0, upperText.getLayoutBounds().getMaxY(),
                                                          false, CycleMethod.NO_CYCLE,
                                                          new Stop(0.0, control.getCharacterUpperFlapColor()),
                                                          new Stop(0.47, control.getCharacterColor()),
                                                          new Stop(0.5, control.getCharacterColor().darker()));
        upperText.setFill(upperTextFill);

        Rectangle lowerClip = new Rectangle(0, lower.getLayoutBounds().getMinY(), WIDTH, lower.getLayoutBounds().getHeight());
        lowerText.setTextOrigin(VPos.BOTTOM);
        lowerText.setFont(FONT);
        lowerText.setFontSmoothingType(FontSmoothingType.LCD);
        lowerText.setText(Character.toString(control.getCharacter()));
        lowerText.setX(((WIDTH - upperText.getLayoutBounds().getWidth()) / 2.0));
        lowerText.setY(HEIGHT * 0.04 + upperText.getLayoutBounds().getHeight());
        lowerText.setClip(lowerClip);
        LinearGradient lowerTextFill = new LinearGradient(0.0, lowerText.getLayoutBounds().getMinY(),
                                                          0.0, lowerText.getLayoutBounds().getMaxY(),
                                                          false, CycleMethod.NO_CYCLE,
                                                          new Stop(0.5, control.getCharacterColor().brighter()),
                                                          new Stop(0.53, control.getCharacterLowerFlapColor()),
                                                          new Stop(1.0, control.getCharacterColor()));
        lowerText.setFill(lowerTextFill);
        lowerText.setStroke(null);

        upperNext = new Path();
        upperNext.setFillRule(FillRule.EVEN_ODD);
        upperNext.getElements().add(new MoveTo(0.8712121212121212 * WIDTH, 0.04405286343612335 * HEIGHT));
        upperNext.getElements().add(new CubicCurveTo(0.9090909090909091 * WIDTH, 0.04405286343612335 * HEIGHT,
                                                     0.9242424242424242 * WIDTH, 0.05726872246696035 * HEIGHT,
                                                     0.9242424242424242 * WIDTH, 0.07488986784140969 * HEIGHT));
        upperNext.getElements().add(new CubicCurveTo(0.9242424242424242 * WIDTH, 0.07488986784140969 * HEIGHT,
                                                     0.9242424242424242 * WIDTH, 0.3832599118942731 * HEIGHT,
                                                     0.9242424242424242 * WIDTH, 0.3832599118942731 * HEIGHT));
        upperNext.getElements().add(new LineTo(0.8409090909090909 * WIDTH, 0.3832599118942731 * HEIGHT));
        upperNext.getElements().add(new LineTo(0.8409090909090909 * WIDTH, 0.45374449339207046 * HEIGHT));
        upperNext.getElements().add(new LineTo(0.1590909090909091 * WIDTH, 0.45374449339207046 * HEIGHT));
        upperNext.getElements().add(new LineTo(0.1590909090909091 * WIDTH, 0.3832599118942731 * HEIGHT));
        upperNext.getElements().add(new LineTo(0.07575757575757576 * WIDTH, 0.3832599118942731 * HEIGHT));
        upperNext.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.3832599118942731 * HEIGHT,
                                                     0.07575757575757576 * WIDTH, 0.07488986784140969 * HEIGHT,
                                                     0.07575757575757576 * WIDTH, 0.07488986784140969 * HEIGHT));
        upperNext.getElements().add(new CubicCurveTo(0.07575757575757576 * WIDTH, 0.05726872246696035 * HEIGHT,
                                                     0.09090909090909091 * WIDTH, 0.04405286343612335 * HEIGHT,
                                                     0.12878787878787878 * WIDTH, 0.04405286343612335 * HEIGHT));
        upperNext.getElements().add(new CubicCurveTo(0.12878787878787878 * WIDTH, 0.04405286343612335 * HEIGHT,
                                                     0.8712121212121212 * WIDTH, 0.04405286343612335 * HEIGHT,
                                                     0.8712121212121212 * WIDTH, 0.04405286343612335 * HEIGHT));
        upperNext.getElements().add(new ClosePath());
        final Paint UPPER_NEXT_FILL = new LinearGradient(0.5205479452054794 * WIDTH, 0.0,
                                                         0.5205479452054794 * WIDTH, 0.49206349206349204 * HEIGHT,
                                                         false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, control.getUpperFlapTopColor()),
                                                         new Stop(1.0, control.getUpperFlapBottomColor()));
        upperNext.setFill(UPPER_NEXT_FILL);
        upperNext.setStroke(null);
        upperNext.setCache(true);
        upperNext.setCacheHint(CacheHint.SPEED);

        Rectangle upperNextClip = new Rectangle(0, upper.getLayoutBounds().getMinY(), WIDTH, upper.getLayoutBounds().getHeight());
        upperNextText.setTextOrigin(VPos.BOTTOM);
        upperNextText.setFont(FONT);
        upperNextText.setFontSmoothingType(FontSmoothingType.LCD);
        upperNextText.setText(Character.toString((char)(control.getCharacter() + 1)));
        upperNextText.setX(((WIDTH - upperText.getLayoutBounds().getWidth()) / 2.0));
        upperNextText.setY(HEIGHT * 0.04 + upperText.getLayoutBounds().getHeight());
        upperNextText.setClip(upperNextClip);
        LinearGradient upperNextTextFill = new LinearGradient(0.0, upperNextText.getLayoutBounds().getMinY(),
                                                              0.0, upperNextText.getLayoutBounds().getMaxY(),
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, control.getCharacterUpperFlapColor()),
                                                              new Stop(0.47, control.getCharacterColor()),
                                                              new Stop(0.5, control.getCharacterColor().darker()));
        upperNextText.setFill(upperNextTextFill);
        upperNextText.setStroke(null);

        Rectangle lowerNextClip = new Rectangle(0, lower.getLayoutBounds().getMinY(), WIDTH, lower.getLayoutBounds().getHeight());
        lowerNextText.setTextOrigin(VPos.BOTTOM);
        lowerNextText.setFont(FONT);
        lowerNextText.setFontSmoothingType(FontSmoothingType.LCD);
        lowerNextText.setText(Character.toString((char)(control.getCharacter() + 1)));
        lowerNextText.setX(((WIDTH - lowerNextText.getLayoutBounds().getWidth()) / 2.0));
        lowerNextText.setY(HEIGHT * 0.04 + lowerNextText.getLayoutBounds().getHeight());
        lowerNextText.setClip(lowerNextClip);
        LinearGradient lowerNextTextFill = new LinearGradient(0.0, lowerNextText.getLayoutBounds().getMinY(),
                                                              0.0, lowerNextText.getLayoutBounds().getMaxY(),
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.5, control.getCharacterColor().brighter()),
                                                              new Stop(0.53, control.getCharacterLowerFlapColor()),
                                                              new Stop(1.0, control.getCharacterColor()));
        lowerNextText.setFill(lowerNextTextFill);
        lowerNextText.setStroke(null);
        lowerNextText.setVisible(false);
        lowerFlipVert = new Rotate();
        lowerFlipVert.setAxis(Rotate.X_AXIS);
        lowerFlipVert.setPivotY(HEIGHT * 0.4625550661);
        lowerFlipVert.setAngle(180);
        lowerNextText.getTransforms().add(lowerFlipVert);

        if (control.isInteractive()) {
            addMouseEventListener(upperText, 1);
            addMouseEventListener(lowerText, -1);
        }

        flip.getChildren().addAll(lower,
                                  lowerText,
                                  upperNext,
                                  upperNextText,
                                  upper,
                                  upperText,
                                  lowerNextText
                                  );
    }

    public final void drawFrame() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();
        frame.getChildren().clear();

        final Shape FRAME = Shape.subtract(new Rectangle(0, 0, WIDTH, HEIGHT),
                                           new Rectangle(0.0606060606 * WIDTH, 0.0352422907 * HEIGHT,
                                                         0.8787878788 * WIDTH,0.9207048458 * HEIGHT));
        final Paint FRAME_FILL = new LinearGradient(0.0, 0.0,
                                                    0.0, HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, control.getFrameTopColor()),
                                                    new Stop(1.0, control.getFrameBottomColor()));
        FRAME.setFill(FRAME_FILL);
        FRAME.setStroke(null);

        frame.getChildren().addAll(FRAME);
        frame.setCache(true);
    }
}
