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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jfxtras.labs.internal.scene.control.behavior.NixieTubeBehavior;
import jfxtras.labs.scene.control.gauge.NixieTube;


/**
 * Created by
 * User: hansolo
 * Date: 24.02.12
 * Time: 20:14
 */
public class NixieTubeSkin extends SkinBase<NixieTube, NixieTubeBehavior> {
    private NixieTube    control;
    private boolean      isDirty;
    private boolean      initialized;
    private Path         hatch;
    private Group        numbers;
    private Path         zero;
    private Path         one;
    private Path         two;
    private Path         three;
    private Path         four;
    private Path         five;
    private Path         six;
    private Path         seven;
    private Path         eight;
    private Path         nine;
    private Group        tube;
    private Rectangle    leftLight;
    private Rectangle    rightLight;
    private DropShadow   hatchGlow;
    private InnerShadow  innerGlow;
    private DropShadow   glow;


    // ******************** Constructors **************************************
    public NixieTubeSkin(final NixieTube CONTROL) {
        super(CONTROL, new NixieTubeBehavior(CONTROL));
        control     = CONTROL;
        initialized = false;
        isDirty     = false;
        hatch       = new Path();
        numbers     = new Group();
        tube        = new Group();
        leftLight   = new Rectangle();
        rightLight  = new Rectangle();

        init();
    }


    // ******************** Initialization ************************************
    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(86, 145);
        }

        // Register listeners
        registerChangeListener(control.glowColorProperty(), "GLOW_COLOR");
        registerChangeListener(control.numberProperty(), "NUMBER");

        createGlows();

        initialized = true;
        paint();
        setNumber();
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        if (control.getScene() != null) {
            getChildren().clear();
            drawNumbers();
            drawTube();
            getChildren().addAll(numbers, tube);
        }
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("GLOW_COLOR".equals(PROPERTY)) {
            createGlows();
            setNumber();
            hatch.setEffect(hatchGlow);
        } else if ("NUMBER".equals(PROPERTY)) {
            setNumber();
        }
    }

    @Override public void layoutChildren() {
        if (isDirty) {
            paint();
            isDirty = false;
        }
        super.layoutChildren();
    }

    @Override public final NixieTube getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 86;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 145;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }

    private final void switchOffAllNumbers() {
        final Color colorOff = Color.color(0.2, 0.2, 0.2, 0.6);
        zero.setEffect(null);
        zero.setStroke(colorOff);
        zero.setStrokeWidth(1);
        one.setEffect(null);
        one.setStroke(colorOff);
        one.setStrokeWidth(1);
        two.setEffect(null);
        two.setStroke(colorOff);
        two.setStrokeWidth(1);
        three.setEffect(null);
        three.setStroke(colorOff);
        three.setStrokeWidth(1);
        four.setEffect(null);
        four.setStroke(colorOff);
        four.setStrokeWidth(1);
        five.setEffect(null);
        five.setStroke(colorOff);
        five.setStrokeWidth(1);
        six.setEffect(null);
        six.setStroke(colorOff);
        six.setStrokeWidth(1);
        seven.setEffect(null);
        seven.setStroke(colorOff);
        seven.setStrokeWidth(1);
        eight.setEffect(null);
        eight.setStroke(colorOff);
        eight.setStrokeWidth(1);
        nine.setEffect(null);
        nine.setStroke(colorOff);
        nine.setStrokeWidth(1);
    }

    private final void setNumber() {
        switchOffAllNumbers();
        final double STROKE_WIDTH = 0.023255814 * control.getPrefWidth();
        final Color colorOn = Color.color(0.2, 0.2, 0.2, 1.0);
        switch(control.getNumber()) {
            case 0:
                zero.setStroke(colorOn);
                zero.setEffect(glow);
                zero.setStrokeWidth(STROKE_WIDTH);
                break;
            case 1:
                one.setStroke(colorOn);
                one.setEffect(glow);
                one.setStrokeWidth(STROKE_WIDTH);
                break;
            case 2:
                two.setStroke(colorOn);
                two.setEffect(glow);
                two.setStrokeWidth(STROKE_WIDTH);
                break;
            case 3:
                three.setStroke(colorOn);
                three.setEffect(glow);
                three.setStrokeWidth(STROKE_WIDTH);
                break;
            case 4:
                four.setStroke(colorOn);
                four.setEffect(glow);
                four.setStrokeWidth(STROKE_WIDTH);
                break;
            case 5:
                five.setStroke(colorOn);
                five.setEffect(glow);
                five.setStrokeWidth(STROKE_WIDTH);
                break;
            case 6:
                six.setStroke(colorOn);
                six.setEffect(glow);
                six.setStrokeWidth(STROKE_WIDTH);
                break;
            case 7:
                seven.setStroke(colorOn);
                seven.setEffect(glow);
                seven.setStrokeWidth(STROKE_WIDTH);
                break;
            case 8:
                eight.setStroke(colorOn);
                eight.setEffect(glow);
                eight.setStrokeWidth(STROKE_WIDTH);
                break;
            case 9:
                nine.setStroke(colorOn);
                nine.setEffect(glow);
                nine.setStrokeWidth(STROKE_WIDTH);
                break;
        }
    }


    // ******************** Drawing related ***********************************
    private final void createGlows() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();
        innerGlow = new InnerShadow();
        innerGlow.setWidth(0.180 * WIDTH);
        innerGlow.setHeight(0.180 * WIDTH);
        innerGlow.setRadius(0.15 * WIDTH);
        innerGlow.setColor(control.getGlowColor().brighter().brighter());
        innerGlow.setBlurType(BlurType.GAUSSIAN);
        innerGlow.inputProperty().set(null);

        glow = new DropShadow();
        glow.setSpread(0.006 * WIDTH);
        glow.setRadius(0.15 * WIDTH);
        glow.setColor(control.getGlowColor());
        glow.setBlurType(BlurType.GAUSSIAN);
        glow.inputProperty().set(innerGlow);

        hatchGlow = new DropShadow();
        hatchGlow.setRadius(0.1 * WIDTH);
        hatchGlow.setColor(Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.6));
        hatch.setFill(Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.05));

        leftLight.setFill(new LinearGradient(0.011627906976744186 * WIDTH, 0.32413793103448274 * HEIGHT,
                                             0.011627906976744245 * WIDTH, 0.903448275862069 * HEIGHT,
                                             false, CycleMethod.NO_CYCLE,
                                             new Stop(0.0, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.1)),
                                             new Stop(0.5, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.4)),
                                             new Stop(1.0, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.1))));

        rightLight.setFill(new LinearGradient(WIDTH, 0.32413793103448274 * HEIGHT,
                                              WIDTH, 0.903448275862069 * HEIGHT,
                                              false, CycleMethod.NO_CYCLE,
                                              new Stop(0.0, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.1)),
                                              new Stop(0.5, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.4)),
                                              new Stop(1.0, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.1))));
    }

    private final void drawNumbers() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        numbers.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        numbers.getChildren().add(IBOUNDS);

        createGlows();

        Color colorOff = Color.color(0.2, 0.2, 0.2, 0.6);

        one = new Path();
        one.setFillRule(FillRule.EVEN_ODD);
        one.getElements().add(new MoveTo(0.4883720930232558 * WIDTH, 0.296551724137931 * HEIGHT));
        one.getElements().add(new CubicCurveTo(0.4883720930232558 * WIDTH, 0.2896551724137931 * HEIGHT,
                                               0.4883720930232558 * WIDTH, 0.2827586206896552 * HEIGHT,
                                               0.5 * WIDTH, 0.2827586206896552 * HEIGHT));
        one.getElements().add(new CubicCurveTo(0.5116279069767442 * WIDTH, 0.2827586206896552 * HEIGHT,
                                               0.5232558139534884 * WIDTH, 0.2896551724137931 * HEIGHT,
                                               0.5232558139534884 * WIDTH, 0.296551724137931 * HEIGHT));
        one.getElements().add(new CubicCurveTo(0.5232558139534884 * WIDTH, 0.296551724137931 * HEIGHT,
                                               0.5232558139534884 * WIDTH, 0.8482758620689655 * HEIGHT,
                                               0.5232558139534884 * WIDTH, 0.8482758620689655 * HEIGHT));
        one.getElements().add(new CubicCurveTo(0.5232558139534884 * WIDTH, 0.8551724137931035 * HEIGHT,
                                               0.5116279069767442 * WIDTH, 0.8620689655172413 * HEIGHT,
                                               0.5 * WIDTH, 0.8689655172413793 * HEIGHT));
        one.getElements().add(new CubicCurveTo(0.4883720930232558 * WIDTH, 0.8689655172413793 * HEIGHT,
                                               0.4883720930232558 * WIDTH, 0.8551724137931035 * HEIGHT,
                                               0.4883720930232558 * WIDTH, 0.8482758620689655 * HEIGHT));
        one.getElements().add(new CubicCurveTo(0.4883720930232558 * WIDTH, 0.8482758620689655 * HEIGHT,
                                               0.4883720930232558 * WIDTH, 0.296551724137931 * HEIGHT,
                                               0.4883720930232558 * WIDTH, 0.296551724137931 * HEIGHT));
        one.getElements().add(new ClosePath());
        one.setFill(null);
        one.setStroke(colorOff);

        zero = new Path();
        zero.setFillRule(FillRule.EVEN_ODD);
        zero.getElements().add(new MoveTo(0.2441860465116279 * WIDTH, 0.5862068965517241 * HEIGHT));
        zero.getElements().add(new CubicCurveTo(0.2441860465116279 * WIDTH, 0.43448275862068964 * HEIGHT,
                                                0.36046511627906974 * WIDTH, 0.31724137931034485 * HEIGHT,
                                                0.5116279069767442 * WIDTH, 0.31724137931034485 * HEIGHT));
        zero.getElements().add(new CubicCurveTo(0.6627906976744186 * WIDTH, 0.31724137931034485 * HEIGHT,
                                                0.7790697674418605 * WIDTH, 0.43448275862068964 * HEIGHT,
                                                0.7790697674418605 * WIDTH, 0.5862068965517241 * HEIGHT));
        zero.getElements().add(new CubicCurveTo(0.7790697674418605 * WIDTH, 0.7310344827586207 * HEIGHT,
                                                0.6627906976744186 * WIDTH, 0.8482758620689655 * HEIGHT,
                                                0.5116279069767442 * WIDTH, 0.8482758620689655 * HEIGHT));
        zero.getElements().add(new CubicCurveTo(0.36046511627906974 * WIDTH, 0.8482758620689655 * HEIGHT,
                                                0.2441860465116279 * WIDTH, 0.7310344827586207 * HEIGHT,
                                                0.2441860465116279 * WIDTH, 0.5862068965517241 * HEIGHT));
        zero.getElements().add(new ClosePath());
        zero.setFill(null);
        zero.setStroke(colorOff);

        two = new Path();
        two.setFillRule(FillRule.EVEN_ODD);
        two.getElements().add(new MoveTo(0.23255813953488372 * WIDTH, 0.47586206896551725 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.23255813953488372 * WIDTH, 0.47586206896551725 * HEIGHT,
                                               0.23255813953488372 * WIDTH, 0.43448275862068964 * HEIGHT,
                                               0.23255813953488372 * WIDTH, 0.42758620689655175 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.2441860465116279 * WIDTH, 0.4068965517241379 * HEIGHT,
                                               0.27906976744186046 * WIDTH, 0.3793103448275862 * HEIGHT,
                                               0.3023255813953488 * WIDTH, 0.36551724137931035 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.32558139534883723 * WIDTH, 0.35172413793103446 * HEIGHT,
                                               0.3488372093023256 * WIDTH, 0.33793103448275863 * HEIGHT,
                                               0.4069767441860465 * WIDTH, 0.32413793103448274 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.43023255813953487 * WIDTH, 0.31724137931034485 * HEIGHT,
                                               0.47674418604651164 * WIDTH, 0.31724137931034485 * HEIGHT,
                                               0.4883720930232558 * WIDTH, 0.31724137931034485 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.5697674418604651 * WIDTH, 0.31724137931034485 * HEIGHT,
                                               0.6162790697674418 * WIDTH, 0.32413793103448274 * HEIGHT,
                                               0.627906976744186 * WIDTH, 0.3310344827586207 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.6744186046511628 * WIDTH, 0.33793103448275863 * HEIGHT,
                                               0.686046511627907 * WIDTH, 0.3448275862068966 * HEIGHT,
                                               0.7093023255813954 * WIDTH, 0.3586206896551724 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.7441860465116279 * WIDTH, 0.3793103448275862 * HEIGHT,
                                               0.7674418604651163 * WIDTH, 0.41379310344827586 * HEIGHT,
                                               0.7674418604651163 * WIDTH, 0.4482758620689655 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.7674418604651163 * WIDTH, 0.496551724137931 * HEIGHT,
                                               0.7441860465116279 * WIDTH, 0.5241379310344828 * HEIGHT,
                                               0.686046511627907 * WIDTH, 0.5448275862068965 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.6627906976744186 * WIDTH, 0.5586206896551724 * HEIGHT,
                                               0.5813953488372093 * WIDTH, 0.5724137931034483 * HEIGHT,
                                               0.5348837209302325 * WIDTH, 0.5793103448275863 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.4883720930232558 * WIDTH, 0.5862068965517241 * HEIGHT,
                                               0.45348837209302323 * WIDTH, 0.6 * HEIGHT,
                                               0.45348837209302323 * WIDTH, 0.6 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.36046511627906974 * WIDTH, 0.6344827586206897 * HEIGHT,
                                               0.29069767441860467 * WIDTH, 0.6896551724137931 * HEIGHT,
                                               0.2558139534883721 * WIDTH, 0.7448275862068966 * HEIGHT));
        two.getElements().add(new CubicCurveTo(0.2441860465116279 * WIDTH, 0.7724137931034483 * HEIGHT,
                                               0.23255813953488372 * WIDTH, 0.8482758620689655 * HEIGHT,
                                               0.23255813953488372 * WIDTH, 0.8482758620689655 * HEIGHT));
        two.getElements().add(new LineTo(0.7906976744186046 * WIDTH, 0.8482758620689655 * HEIGHT));
        two.setFill(null);
        two.setStroke(colorOff);

        three = new Path();
        three.setFillRule(FillRule.EVEN_ODD);
        three.getElements().add(new MoveTo(0.23255813953488372 * WIDTH, 0.31724137931034485 * HEIGHT));
        three.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.31724137931034485 * HEIGHT));
        three.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.3310344827586207 * HEIGHT));
        three.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.5172413793103449 * HEIGHT));
        three.getElements().add(new CubicCurveTo(0.5232558139534884 * WIDTH, 0.5172413793103449 * HEIGHT,
                                                 0.5697674418604651 * WIDTH, 0.5241379310344828 * HEIGHT,
                                                 0.6046511627906976 * WIDTH, 0.5310344827586206 * HEIGHT));
        three.getElements().add(new CubicCurveTo(0.6744186046511628 * WIDTH, 0.5448275862068965 * HEIGHT,
                                                 0.7209302325581395 * WIDTH, 0.593103448275862 * HEIGHT,
                                                 0.7209302325581395 * WIDTH, 0.593103448275862 * HEIGHT));
        three.getElements().add(new CubicCurveTo(0.7674418604651163 * WIDTH, 0.6344827586206897 * HEIGHT,
                                                 0.7674418604651163 * WIDTH, 0.6758620689655173 * HEIGHT,
                                                 0.7674418604651163 * WIDTH, 0.6896551724137931 * HEIGHT));
        three.getElements().add(new CubicCurveTo(0.7674418604651163 * WIDTH, 0.7172413793103448 * HEIGHT,
                                                 0.7558139534883721 * WIDTH, 0.7448275862068966 * HEIGHT,
                                                 0.7325581395348837 * WIDTH, 0.7655172413793103 * HEIGHT));
        three.getElements().add(new CubicCurveTo(0.7093023255813954 * WIDTH, 0.7862068965517242 * HEIGHT,
                                                 0.686046511627907 * WIDTH, 0.8068965517241379 * HEIGHT,
                                                 0.6395348837209303 * WIDTH, 0.8275862068965517 * HEIGHT));
        three.getElements().add(new CubicCurveTo(0.5930232558139535 * WIDTH, 0.8413793103448276 * HEIGHT,
                                                 0.5116279069767442 * WIDTH, 0.8482758620689655 * HEIGHT,
                                                 0.5116279069767442 * WIDTH, 0.8482758620689655 * HEIGHT));
        three.getElements().add(new CubicCurveTo(0.5 * WIDTH, 0.8482758620689655 * HEIGHT,
                                                 0.45348837209302323 * WIDTH, 0.8482758620689655 * HEIGHT,
                                                 0.45348837209302323 * WIDTH, 0.8482758620689655 * HEIGHT));
        three.getElements().add(new CubicCurveTo(0.3953488372093023 * WIDTH, 0.8482758620689655 * HEIGHT,
                                                 0.32558139534883723 * WIDTH, 0.8275862068965517 * HEIGHT,
                                                 0.29069767441860467 * WIDTH, 0.8068965517241379 * HEIGHT));
        three.getElements().add(new CubicCurveTo(0.2558139534883721 * WIDTH, 0.7931034482758621 * HEIGHT,
                                                 0.23255813953488372 * WIDTH, 0.7655172413793103 * HEIGHT,
                                                 0.23255813953488372 * WIDTH, 0.7655172413793103 * HEIGHT));
        three.setFill(null);
        three.setStroke(colorOff);

        six = new Path();
        six.setFillRule(FillRule.EVEN_ODD);
        six.getElements().add(new MoveTo(0.5232558139534884 * WIDTH, 0.2827586206896552 * HEIGHT));
        six.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.5793103448275863 * HEIGHT));
        six.getElements().add(new MoveTo(0.22093023255813954 * WIDTH, 0.6758620689655173 * HEIGHT));
        six.getElements().add(new CubicCurveTo(0.22093023255813954 * WIDTH, 0.5862068965517241 * HEIGHT,
                                               0.3488372093023256 * WIDTH, 0.5103448275862069 * HEIGHT,
                                               0.5116279069767442 * WIDTH, 0.5103448275862069 * HEIGHT));
        six.getElements().add(new CubicCurveTo(0.6627906976744186 * WIDTH, 0.5103448275862069 * HEIGHT,
                                               0.7906976744186046 * WIDTH, 0.5862068965517241 * HEIGHT,
                                               0.7906976744186046 * WIDTH, 0.6758620689655173 * HEIGHT));
        six.getElements().add(new CubicCurveTo(0.7906976744186046 * WIDTH, 0.7655172413793103 * HEIGHT,
                                               0.6627906976744186 * WIDTH, 0.8413793103448276 * HEIGHT,
                                               0.5116279069767442 * WIDTH, 0.8413793103448276 * HEIGHT));
        six.getElements().add(new CubicCurveTo(0.3488372093023256 * WIDTH, 0.8413793103448276 * HEIGHT,
                                               0.22093023255813954 * WIDTH, 0.7655172413793103 * HEIGHT,
                                               0.22093023255813954 * WIDTH, 0.6758620689655173 * HEIGHT));
        six.getElements().add(new ClosePath());
        six.setFill(null);
        six.setStroke(colorOff);

        four = new Path();
        four.setFillRule(FillRule.EVEN_ODD);
        four.getElements().add(new MoveTo(0.7790697674418605 * WIDTH, 0.6827586206896552 * HEIGHT));
        four.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.6827586206896552 * HEIGHT));
        four.getElements().add(new CubicCurveTo(0.2441860465116279 * WIDTH, 0.6827586206896552 * HEIGHT,
                                                0.23255813953488372 * WIDTH, 0.6758620689655173 * HEIGHT,
                                                0.22093023255813954 * WIDTH, 0.6758620689655173 * HEIGHT));
        four.getElements().add(new CubicCurveTo(0.19767441860465115 * WIDTH, 0.6689655172413793 * HEIGHT,
                                                0.19767441860465115 * WIDTH, 0.6689655172413793 * HEIGHT,
                                                0.20930232558139536 * WIDTH, 0.6620689655172414 * HEIGHT));
        four.getElements().add(new CubicCurveTo(0.20930232558139536 * WIDTH, 0.6551724137931034 * HEIGHT,
                                                0.6046511627906976 * WIDTH, 0.30344827586206896 * HEIGHT,
                                                0.6046511627906976 * WIDTH, 0.30344827586206896 * HEIGHT));
        four.getElements().add(new LineTo(0.6046511627906976 * WIDTH, 0.8482758620689655 * HEIGHT));
        four.setFill(null);
        four.setStroke(colorOff);

        nine = new Path();
        nine.setFillRule(FillRule.EVEN_ODD);
        nine.getElements().add(new MoveTo(0.5465116279069767 * WIDTH, 0.8620689655172413 * HEIGHT));
        nine.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.5310344827586206 * HEIGHT));
        nine.getElements().add(new MoveTo(0.23255813953488372 * WIDTH, 0.46206896551724136 * HEIGHT));
        nine.getElements().add(new CubicCurveTo(0.23255813953488372 * WIDTH, 0.3724137931034483 * HEIGHT,
                                                0.36046511627906974 * WIDTH, 0.296551724137931 * HEIGHT,
                                                0.5116279069767442 * WIDTH, 0.296551724137931 * HEIGHT));
        nine.getElements().add(new CubicCurveTo(0.6627906976744186 * WIDTH, 0.296551724137931 * HEIGHT,
                                                0.7906976744186046 * WIDTH, 0.3724137931034483 * HEIGHT,
                                                0.7906976744186046 * WIDTH, 0.46206896551724136 * HEIGHT));
        nine.getElements().add(new CubicCurveTo(0.7906976744186046 * WIDTH, 0.5517241379310345 * HEIGHT,
                                                0.6627906976744186 * WIDTH, 0.6275862068965518 * HEIGHT,
                                                0.5116279069767442 * WIDTH, 0.6275862068965518 * HEIGHT));
        nine.getElements().add(new CubicCurveTo(0.36046511627906974 * WIDTH, 0.6275862068965518 * HEIGHT,
                                                0.23255813953488372 * WIDTH, 0.5517241379310345 * HEIGHT,
                                                0.23255813953488372 * WIDTH, 0.46206896551724136 * HEIGHT));
        nine.getElements().add(new ClosePath());
        nine.setFill(null);
        nine.setStroke(colorOff);

        five = new Path();
        five.setFillRule(FillRule.EVEN_ODD);
        five.getElements().add(new MoveTo(0.7093023255813954 * WIDTH, 0.32413793103448274 * HEIGHT));
        five.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.32413793103448274 * HEIGHT));
        five.getElements().add(new LineTo(0.26744186046511625 * WIDTH, 0.5655172413793104 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.26744186046511625 * WIDTH, 0.5655172413793104 * HEIGHT,
                                                0.29069767441860467 * WIDTH, 0.5655172413793104 * HEIGHT,
                                                0.29069767441860467 * WIDTH, 0.5655172413793104 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.3488372093023256 * WIDTH, 0.5448275862068965 * HEIGHT,
                                                0.4069767441860465 * WIDTH, 0.5310344827586206 * HEIGHT,
                                                0.4418604651162791 * WIDTH, 0.5310344827586206 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.4883720930232558 * WIDTH, 0.5241379310344828 * HEIGHT,
                                                0.5116279069767442 * WIDTH, 0.5241379310344828 * HEIGHT,
                                                0.5465116279069767 * WIDTH, 0.5310344827586206 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.6162790697674418 * WIDTH, 0.5379310344827586 * HEIGHT,
                                                0.686046511627907 * WIDTH, 0.5793103448275863 * HEIGHT,
                                                0.6976744186046512 * WIDTH, 0.593103448275862 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.7209302325581395 * WIDTH, 0.6068965517241379 * HEIGHT,
                                                0.7441860465116279 * WIDTH, 0.6413793103448275 * HEIGHT,
                                                0.7558139534883721 * WIDTH, 0.6482758620689655 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.7558139534883721 * WIDTH, 0.6620689655172414 * HEIGHT,
                                                0.7674418604651163 * WIDTH, 0.696551724137931 * HEIGHT,
                                                0.7674418604651163 * WIDTH, 0.696551724137931 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.7674418604651163 * WIDTH, 0.7172413793103448 * HEIGHT,
                                                0.7441860465116279 * WIDTH, 0.7448275862068966 * HEIGHT,
                                                0.7441860465116279 * WIDTH, 0.7448275862068966 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.7441860465116279 * WIDTH, 0.7586206896551724 * HEIGHT,
                                                0.7093023255813954 * WIDTH, 0.7931034482758621 * HEIGHT,
                                                0.7093023255813954 * WIDTH, 0.7931034482758621 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.6976744186046512 * WIDTH, 0.8068965517241379 * HEIGHT,
                                                0.6395348837209303 * WIDTH, 0.8275862068965517 * HEIGHT,
                                                0.6395348837209303 * WIDTH, 0.8275862068965517 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.5697674418604651 * WIDTH, 0.8482758620689655 * HEIGHT,
                                                0.47674418604651164 * WIDTH, 0.8482758620689655 * HEIGHT,
                                                0.47674418604651164 * WIDTH, 0.8482758620689655 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.3953488372093023 * WIDTH, 0.8482758620689655 * HEIGHT,
                                                0.3372093023255814 * WIDTH, 0.8206896551724138 * HEIGHT,
                                                0.3372093023255814 * WIDTH, 0.8206896551724138 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.3023255813953488 * WIDTH, 0.8137931034482758 * HEIGHT,
                                                0.26744186046511625 * WIDTH, 0.7862068965517242 * HEIGHT,
                                                0.26744186046511625 * WIDTH, 0.7862068965517242 * HEIGHT));
        five.getElements().add(new CubicCurveTo(0.2441860465116279 * WIDTH, 0.7724137931034483 * HEIGHT,
                                                0.23255813953488372 * WIDTH, 0.7517241379310344 * HEIGHT,
                                                0.23255813953488372 * WIDTH, 0.7517241379310344 * HEIGHT));
        five.setFill(null);
        five.setStroke(colorOff);

        eight = new Path();
        eight.setFillRule(FillRule.EVEN_ODD);
        eight.getElements().add(new MoveTo(0.20930232558139536 * WIDTH, 0.7034482758620689 * HEIGHT));
        eight.getElements().add(new CubicCurveTo(0.20930232558139536 * WIDTH, 0.6137931034482759 * HEIGHT,
                                                 0.3372093023255814 * WIDTH, 0.5448275862068965 * HEIGHT,
                                                 0.5 * WIDTH, 0.5448275862068965 * HEIGHT));
        eight.getElements().add(new CubicCurveTo(0.6627906976744186 * WIDTH, 0.5448275862068965 * HEIGHT,
                                                 0.7906976744186046 * WIDTH, 0.6137931034482759 * HEIGHT,
                                                 0.7906976744186046 * WIDTH, 0.7034482758620689 * HEIGHT));
        eight.getElements().add(new CubicCurveTo(0.7906976744186046 * WIDTH, 0.7862068965517242 * HEIGHT,
                                                 0.6627906976744186 * WIDTH, 0.8551724137931035 * HEIGHT,
                                                 0.5 * WIDTH, 0.8551724137931035 * HEIGHT));
        eight.getElements().add(new CubicCurveTo(0.3372093023255814 * WIDTH, 0.8551724137931035 * HEIGHT,
                                                 0.20930232558139536 * WIDTH, 0.7862068965517242 * HEIGHT,
                                                 0.20930232558139536 * WIDTH, 0.7034482758620689 * HEIGHT));
        eight.getElements().add(new ClosePath());
        eight.getElements().add(new MoveTo(0.26744186046511625 * WIDTH, 0.4206896551724138 * HEIGHT));
        eight.getElements().add(new CubicCurveTo(0.26744186046511625 * WIDTH, 0.3448275862068966 * HEIGHT,
                                                 0.37209302325581395 * WIDTH, 0.2896551724137931 * HEIGHT,
                                                 0.5116279069767442 * WIDTH, 0.2896551724137931 * HEIGHT));
        eight.getElements().add(new CubicCurveTo(0.6511627906976745 * WIDTH, 0.2896551724137931 * HEIGHT,
                                                 0.7558139534883721 * WIDTH, 0.3448275862068966 * HEIGHT,
                                                 0.7558139534883721 * WIDTH, 0.4206896551724138 * HEIGHT));
        eight.getElements().add(new CubicCurveTo(0.7558139534883721 * WIDTH, 0.4896551724137931 * HEIGHT,
                                                 0.6511627906976745 * WIDTH, 0.5448275862068965 * HEIGHT,
                                                 0.5116279069767442 * WIDTH, 0.5448275862068965 * HEIGHT));
        eight.getElements().add(new CubicCurveTo(0.37209302325581395 * WIDTH, 0.5448275862068965 * HEIGHT,
                                                 0.26744186046511625 * WIDTH, 0.4896551724137931 * HEIGHT,
                                                 0.26744186046511625 * WIDTH, 0.4206896551724138 * HEIGHT));
        eight.getElements().add(new ClosePath());
        eight.setFill(null);
        eight.setStroke(colorOff);

        seven = new Path();
        seven.setFillRule(FillRule.EVEN_ODD);
        seven.getElements().add(new MoveTo(0.22093023255813954 * WIDTH, 0.30344827586206896 * HEIGHT));
        seven.getElements().add(new LineTo(0.7906976744186046 * WIDTH, 0.296551724137931 * HEIGHT));
        seven.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.8620689655172413 * HEIGHT));
        seven.setFill(null);
        seven.setStroke(colorOff);

        numbers.getChildren().addAll(one,
                                     zero,
                                     two,
                                     three,
                                     six,
                                     four,
                                     nine,
                                     five,
                                     eight,
                                     seven);
    }

    private final void drawTube() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        tube.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        tube.getChildren().add(IBOUNDS);

        hatch = new Path();
        hatch.setFillRule(FillRule.EVEN_ODD);
        hatch.getElements().add(new MoveTo(0.8372093023255814 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.8758620689655172 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7674418604651163 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.8758620689655172 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6976744186046512 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.8758620689655172 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.627906976744186 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.8758620689655172 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5581395348837209 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.8758620689655172 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4883720930232558 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.8758620689655172 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4186046511627907 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.8758620689655172 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.3488372093023256 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.8758620689655172 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.27906976744186046 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.8758620689655172 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.20930232558139536 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.8758620689655172 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.8620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.8413793103448276 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.8275862068965517 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8023255813953488 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.8344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7325581395348837 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.8344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6627906976744186 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.8344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5930232558139535 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.8344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5232558139534884 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.8344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.45348837209302323 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.8344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.38372093023255816 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.8344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.313953488372093 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.8344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.2441860465116279 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.8344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.1744186046511628 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.8344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.8206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.8 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.7862068965517242 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8372093023255814 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.7931034482758621 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7674418604651163 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.7931034482758621 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6976744186046512 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.7931034482758621 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.627906976744186 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.7931034482758621 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5581395348837209 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.7931034482758621 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4883720930232558 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.7931034482758621 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4186046511627907 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.7931034482758621 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.3488372093023256 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.7931034482758621 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.27906976744186046 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.7931034482758621 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.20930232558139536 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.7931034482758621 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.7793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.7586206896551724 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.7448275862068966 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8023255813953488 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.7517241379310344 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7325581395348837 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.7517241379310344 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6627906976744186 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.7517241379310344 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5930232558139535 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.7517241379310344 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5232558139534884 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.7517241379310344 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.45348837209302323 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.7517241379310344 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.38372093023255816 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.7517241379310344 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.313953488372093 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.7517241379310344 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.2441860465116279 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.7517241379310344 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.1744186046511628 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.7517241379310344 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.7379310344827587 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.7172413793103448 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.7034482758620689 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8372093023255814 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.7103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7674418604651163 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.7103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6976744186046512 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.7103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.627906976744186 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.7103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5581395348837209 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.7103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4883720930232558 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.7103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4186046511627907 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.7103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.3488372093023256 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.7103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.27906976744186046 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.7103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.20930232558139536 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.7103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.696551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.6758620689655173 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.6620689655172414 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8023255813953488 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.6689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7325581395348837 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.6689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6627906976744186 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.6689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5930232558139535 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.6689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5232558139534884 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.6689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.45348837209302323 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.6689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.38372093023255816 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.6689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.313953488372093 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.6689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.2441860465116279 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.6689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.1744186046511628 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.6689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.6551724137931034 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.6344827586206897 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.6206896551724138 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8372093023255814 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.6275862068965518 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7674418604651163 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.6275862068965518 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6976744186046512 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.6275862068965518 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.627906976744186 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.6275862068965518 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5581395348837209 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.6275862068965518 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4883720930232558 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.6275862068965518 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4186046511627907 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.6275862068965518 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.3488372093023256 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.6275862068965518 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.27906976744186046 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.6275862068965518 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.20930232558139536 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.6275862068965518 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.6137931034482759 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.593103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.5793103448275863 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8023255813953488 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.5862068965517241 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7325581395348837 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.5862068965517241 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6627906976744186 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.5862068965517241 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5930232558139535 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.5862068965517241 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5232558139534884 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.5862068965517241 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.45348837209302323 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.5862068965517241 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.38372093023255816 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.5862068965517241 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.313953488372093 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.5862068965517241 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.2441860465116279 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.5862068965517241 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.1744186046511628 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.5862068965517241 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.5724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.5517241379310345 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.5379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8372093023255814 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.5448275862068965 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7674418604651163 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.5448275862068965 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6976744186046512 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.5448275862068965 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.627906976744186 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.5448275862068965 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5581395348837209 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.5448275862068965 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4883720930232558 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.5448275862068965 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4186046511627907 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.5448275862068965 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.3488372093023256 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.5448275862068965 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.27906976744186046 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.5448275862068965 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.20930232558139536 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.5448275862068965 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.5310344827586206 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.5103448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.496551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8023255813953488 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.503448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7325581395348837 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.503448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6627906976744186 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.503448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5930232558139535 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.503448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5232558139534884 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.503448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.45348837209302323 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.503448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.38372093023255816 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.503448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.313953488372093 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.503448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.2441860465116279 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.503448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.1744186046511628 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.503448275862069 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.4896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.4689655172413793 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.45517241379310347 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8372093023255814 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.46206896551724136 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7674418604651163 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.46206896551724136 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6976744186046512 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.46206896551724136 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.627906976744186 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.46206896551724136 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5581395348837209 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.46206896551724136 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4883720930232558 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.46206896551724136 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4186046511627907 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.46206896551724136 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.3488372093023256 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.46206896551724136 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.27906976744186046 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.46206896551724136 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.20930232558139536 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.46206896551724136 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.4482758620689655 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.42758620689655175 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.41379310344827586 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8023255813953488 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.4206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7325581395348837 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.4206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6627906976744186 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.4206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5930232558139535 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.4206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5232558139534884 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.4206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.45348837209302323 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.4206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.38372093023255816 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.4206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.313953488372093 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.4206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.2441860465116279 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.4206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.1744186046511628 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.4206896551724138 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.4068965517241379 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.38620689655172413 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.3724137931034483 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8372093023255814 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.872093023255814 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.3793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7674418604651163 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.3793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6976744186046512 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.3793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.627906976744186 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.3793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5581395348837209 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.3793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4883720930232558 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.3793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.4186046511627907 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.3793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.3488372093023256 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.3793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.27906976744186046 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.3793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.20930232558139536 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.3793103448275862 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.36551724137931035 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.3448275862068966 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.3310344827586207 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.8023255813953488 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8372093023255814 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.33793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.7325581395348837 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7674418604651163 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.33793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.6627906976744186 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6976744186046512 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.33793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.6627906976744186 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5930232558139535 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.33793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5930232558139535 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.5232558139534884 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.33793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.5232558139534884 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.45348837209302323 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4883720930232558 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.33793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.38372093023255816 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.4186046511627907 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.33793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.313953488372093 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.3488372093023256 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.33793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.313953488372093 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.2441860465116279 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.33793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.2441860465116279 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.getElements().add(new MoveTo(0.1744186046511628 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.33793103448275863 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.32413793103448274 * HEIGHT));
        hatch.getElements().add(new LineTo(0.13953488372093023 * WIDTH, 0.30344827586206896 * HEIGHT));
        hatch.getElements().add(new LineTo(0.1744186046511628 * WIDTH, 0.2896551724137931 * HEIGHT));
        hatch.getElements().add(new ClosePath());
        hatch.setFill(Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.05));
        hatch.setStroke(Color.color(0.2, 0.2, 0.2, 0.5));
        hatch.setEffect(hatchGlow);

        tube.getChildren().addAll(hatch);
        tube.setCache(true);

        continueTube(tube, WIDTH, HEIGHT);
    }

    private void continueTube(Group foreground2, final double WIDTH, final double HEIGHT) {
        final Path CONNECTOR = new Path();
        CONNECTOR.setFillRule(FillRule.EVEN_ODD);
        CONNECTOR.getElements().add(new MoveTo(0.7906976744186046 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.8023255813953488 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.7906976744186046 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.7906976744186046 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.getElements().add(new MoveTo(0.7325581395348837 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.7441860465116279 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.7441860465116279 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.7325581395348837 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.getElements().add(new MoveTo(0.6744186046511628 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.686046511627907 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.686046511627907 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.6744186046511628 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.6744186046511628 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.getElements().add(new MoveTo(0.6162790697674418 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.627906976744186 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.6162790697674418 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.6162790697674418 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.getElements().add(new MoveTo(0.5581395348837209 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.5697674418604651 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.5697674418604651 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.5581395348837209 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.getElements().add(new MoveTo(0.5 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.5116279069767442 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.5116279069767442 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.5 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.5 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.getElements().add(new MoveTo(0.4418604651162791 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.45348837209302323 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.4418604651162791 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.4418604651162791 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.getElements().add(new MoveTo(0.38372093023255816 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.3953488372093023 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.3953488372093023 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.38372093023255816 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.getElements().add(new MoveTo(0.32558139534883723 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.3372093023255814 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.3372093023255814 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.32558139534883723 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.32558139534883723 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.getElements().add(new MoveTo(0.26744186046511625 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.27906976744186046 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.26744186046511625 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.26744186046511625 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.getElements().add(new MoveTo(0.20930232558139536 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.22093023255813954 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.22093023255813954 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.9379310344827586 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.20930232558139536 * WIDTH, 0.8689655172413793 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.setFill(null);
        CONNECTOR.setStroke(Color.color(0.35, 0.35, 0.35, 0.3));

        final Path TUBE = new Path();
        TUBE.setFillRule(FillRule.EVEN_ODD);
        TUBE.getElements().add(new MoveTo(0.0, 0.35172413793103446 * HEIGHT));
        TUBE.getElements().add(new CubicCurveTo(0.0, 0.35172413793103446 * HEIGHT,
                                                0.0, 0.8827586206896552 * HEIGHT,
                                                0.0, 0.8827586206896552 * HEIGHT));
        TUBE.getElements().add(new CubicCurveTo(0.0, 0.9448275862068966 * HEIGHT,
                                                0.09302325581395349 * WIDTH, HEIGHT,
                                                0.20930232558139536 * WIDTH, HEIGHT));
        TUBE.getElements().add(new CubicCurveTo(0.20930232558139536 * WIDTH, HEIGHT,
                                                0.8023255813953488 * WIDTH, HEIGHT,
                                                0.8023255813953488 * WIDTH, HEIGHT));
        TUBE.getElements().add(new CubicCurveTo(0.9069767441860465 * WIDTH, HEIGHT,
                                                WIDTH, 0.9448275862068966 * HEIGHT,
                                                WIDTH, 0.8827586206896552 * HEIGHT));
        TUBE.getElements().add(new CubicCurveTo(WIDTH, 0.8827586206896552 * HEIGHT,
                                                WIDTH, 0.35172413793103446 * HEIGHT,
                                                WIDTH, 0.35172413793103446 * HEIGHT));
        TUBE.getElements().add(new CubicCurveTo(WIDTH, 0.2 * HEIGHT,
                                                0.813953488372093 * WIDTH, 0.07586206896551724 * HEIGHT,
                                                0.5697674418604651 * WIDTH, 0.05517241379310345 * HEIGHT));
        TUBE.getElements().add(new CubicCurveTo(0.5697674418604651 * WIDTH, 0.027586206896551724 * HEIGHT,
                                                0.5348837209302325 * WIDTH, 0.0,
                                                0.5 * WIDTH, 0.0));
        TUBE.getElements().add(new CubicCurveTo(0.46511627906976744 * WIDTH, 0.0,
                                                0.43023255813953487 * WIDTH, 0.027586206896551724 * HEIGHT,
                                                0.43023255813953487 * WIDTH, 0.05517241379310345 * HEIGHT));
        TUBE.getElements().add(new CubicCurveTo(0.18604651162790697 * WIDTH, 0.07586206896551724 * HEIGHT,
                                                0.0, 0.2 * HEIGHT,
                                                0.0, 0.35172413793103446 * HEIGHT));
        TUBE.getElements().add(new ClosePath());
        final Paint TUBE_FILL = new LinearGradient(0.011627906976744186 * WIDTH, 0.5655172413793104 * HEIGHT,
                                                   0.9883720930232558 * WIDTH, 0.5655172413793104 * HEIGHT,
                                                   false, CycleMethod.NO_CYCLE,
                                                   new Stop(0.0, Color.color(0, 0, 0, 0.6)),
                                                   new Stop(0.06, Color.color(0, 0, 0, 0.4)),
                                                   new Stop(0.07, Color.color(0, 0, 0, 0.3921568627)),
                                                   new Stop(0.35, Color.color(0, 0, 0, 0.2)),
                                                   new Stop(0.36, Color.color(0, 0, 0, 0.2)),
                                                   new Stop(0.66, Color.color(0, 0, 0, 0.2)),
                                                   new Stop(0.66010004, Color.color(0, 0, 0, 0.2)),
                                                   new Stop(0.92, Color.color(0, 0, 0, 0.5098039216)),
                                                   new Stop(1.0, Color.color(0, 0, 0, 0.6)));

        TUBE.setFill(TUBE_FILL);
        TUBE.setStroke(null);

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setWidth(0.16744186046511628 * TUBE.getLayoutBounds().getWidth());
        INNER_SHADOW.setHeight(0.16744186046511628 * TUBE.getLayoutBounds().getHeight());
        INNER_SHADOW.setOffsetX(0.0);
        INNER_SHADOW.setOffsetY(0.0);
        INNER_SHADOW.setRadius(0.16744186046511628 * TUBE.getLayoutBounds().getWidth());
        INNER_SHADOW.setColor(Color.BLACK);
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        INNER_SHADOW.inputProperty().set(null);
        TUBE.setEffect(INNER_SHADOW);

        final Rectangle HL_MIDDLE = new Rectangle(0.13953488372093023 * WIDTH, 0.2620689655172414 * HEIGHT,
                                                  0.7209302325581395 * WIDTH, 0.013793103448275862 * HEIGHT);
        HL_MIDDLE.getStyleClass().add("tube-hl-middle");

        final Path HL_LEFT = new Path();
        HL_LEFT.setFillRule(FillRule.EVEN_ODD);
        HL_LEFT.getElements().add(new MoveTo(0.18604651162790697 * WIDTH, 0.2689655172413793 * HEIGHT));
        HL_LEFT.getElements().add(new CubicCurveTo(0.18604651162790697 * WIDTH, 0.2689655172413793 * HEIGHT,
                                                   0.09302325581395349 * WIDTH, 0.2689655172413793 * HEIGHT,
                                                   0.09302325581395349 * WIDTH, 0.2689655172413793 * HEIGHT));
        HL_LEFT.getElements().add(new CubicCurveTo(0.046511627906976744 * WIDTH, 0.27586206896551724 * HEIGHT,
                                                   0.023255813953488372 * WIDTH, 0.2827586206896552 * HEIGHT,
                                                   0.023255813953488372 * WIDTH, 0.30344827586206896 * HEIGHT));
        HL_LEFT.getElements().add(new CubicCurveTo(0.023255813953488372 * WIDTH, 0.3103448275862069 * HEIGHT,
                                                   0.023255813953488372 * WIDTH, 0.9103448275862069 * HEIGHT,
                                                   0.023255813953488372 * WIDTH, 0.9103448275862069 * HEIGHT));
        HL_LEFT.getElements().add(new CubicCurveTo(0.023255813953488372 * WIDTH, 0.9241379310344827 * HEIGHT,
                                                   0.046511627906976744 * WIDTH, 0.9310344827586207 * HEIGHT,
                                                   0.046511627906976744 * WIDTH, 0.9310344827586207 * HEIGHT));
        HL_LEFT.getElements().add(new CubicCurveTo(0.046511627906976744 * WIDTH, 0.9310344827586207 * HEIGHT,
                                                   0.13953488372093023 * WIDTH, 0.9310344827586207 * HEIGHT,
                                                   0.13953488372093023 * WIDTH, 0.9310344827586207 * HEIGHT));
        HL_LEFT.getElements().add(new CubicCurveTo(0.16279069767441862 * WIDTH, 0.9310344827586207 * HEIGHT,
                                                   0.1744186046511628 * WIDTH, 0.9103448275862069 * HEIGHT,
                                                   0.1744186046511628 * WIDTH, 0.9103448275862069 * HEIGHT));
        HL_LEFT.getElements().add(new CubicCurveTo(0.1744186046511628 * WIDTH, 0.9103448275862069 * HEIGHT,
                                                   0.1744186046511628 * WIDTH, 0.31724137931034485 * HEIGHT,
                                                   0.1744186046511628 * WIDTH, 0.31724137931034485 * HEIGHT));
        HL_LEFT.getElements().add(new CubicCurveTo(0.1744186046511628 * WIDTH, 0.2896551724137931 * HEIGHT,
                                                   0.18604651162790697 * WIDTH, 0.2689655172413793 * HEIGHT,
                                                   0.18604651162790697 * WIDTH, 0.2689655172413793 * HEIGHT));
        HL_LEFT.getElements().add(new ClosePath());
        HL_LEFT.getStyleClass().add("tube-hl-left");

        final Path HL_RIGHT = new Path();
        HL_RIGHT.setFillRule(FillRule.EVEN_ODD);
        HL_RIGHT.getElements().add(new MoveTo(0.8604651162790697 * WIDTH, 0.2689655172413793 * HEIGHT));
        HL_RIGHT.getElements().add(new CubicCurveTo(0.8837209302325582 * WIDTH, 0.27586206896551724 * HEIGHT,
                                                    0.8953488372093024 * WIDTH, 0.2896551724137931 * HEIGHT,
                                                    0.8953488372093024 * WIDTH, 0.2896551724137931 * HEIGHT));
        HL_RIGHT.getElements().add(new CubicCurveTo(0.8953488372093024 * WIDTH, 0.2896551724137931 * HEIGHT,
                                                    0.8953488372093024 * WIDTH, 0.9172413793103448 * HEIGHT,
                                                    0.8953488372093024 * WIDTH, 0.9172413793103448 * HEIGHT));
        HL_RIGHT.getElements().add(new CubicCurveTo(0.8953488372093024 * WIDTH, 0.9241379310344827 * HEIGHT,
                                                    0.9302325581395349 * WIDTH, 0.9310344827586207 * HEIGHT,
                                                    0.9302325581395349 * WIDTH, 0.9310344827586207 * HEIGHT));
        HL_RIGHT.getElements().add(new CubicCurveTo(0.9651162790697675 * WIDTH, 0.9310344827586207 * HEIGHT,
                                                    0.9651162790697675 * WIDTH, 0.9172413793103448 * HEIGHT,
                                                    0.9651162790697675 * WIDTH, 0.9172413793103448 * HEIGHT));
        HL_RIGHT.getElements().add(new CubicCurveTo(0.9651162790697675 * WIDTH, 0.9172413793103448 * HEIGHT,
                                                    0.9651162790697675 * WIDTH, 0.2896551724137931 * HEIGHT,
                                                    0.9651162790697675 * WIDTH, 0.2896551724137931 * HEIGHT));
        HL_RIGHT.getElements().add(new CubicCurveTo(0.9651162790697675 * WIDTH, 0.27586206896551724 * HEIGHT,
                                                    0.9302325581395349 * WIDTH, 0.2689655172413793 * HEIGHT,
                                                    0.9302325581395349 * WIDTH, 0.2689655172413793 * HEIGHT));
        HL_RIGHT.getElements().add(new CubicCurveTo(0.9302325581395349 * WIDTH, 0.2689655172413793 * HEIGHT,
                                                    0.8604651162790697 * WIDTH, 0.2689655172413793 * HEIGHT,
                                                    0.8604651162790697 * WIDTH, 0.2689655172413793 * HEIGHT));
        HL_RIGHT.getElements().add(new ClosePath());
        HL_RIGHT.getStyleClass().add("tube-hl-right");

        final Ellipse HL_UPPER = new Ellipse(0.5 * WIDTH, 0.1482758620689655 * HEIGHT,
                                             0.3023255813953488 * WIDTH, 0.07241379310344828 * HEIGHT);
        HL_UPPER.getStyleClass().add("tube-hl-upper");

        final Ellipse HL_TOP = new Ellipse(0.47674418604651164 * WIDTH, 0.04482758620689655 * HEIGHT,
                                           0.03488372093023256 * WIDTH, 0.02413793103448276 * HEIGHT);
        HL_TOP.getStyleClass().add("tube-hl-top");

        leftLight = new Rectangle(0.0, 0.32413793103448274 * HEIGHT,
                                                  0.011627906976744186 * WIDTH, 0.5793103448275863 * HEIGHT);
        final Paint LEFTLIGHT_FILL = new LinearGradient(0.011627906976744186 * WIDTH, 0.32413793103448274 * HEIGHT,
                                                        0.011627906976744245 * WIDTH, 0.903448275862069 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.1)),
                                                        new Stop(0.5, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.4)),
                                                        new Stop(1.0, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.1)));
        leftLight.setFill(LEFTLIGHT_FILL);
        leftLight.setStroke(null);

        rightLight = new Rectangle(0.9883720930232558 * WIDTH, 0.32413793103448274 * HEIGHT,
                                                   0.011627906976744186 * WIDTH, 0.5793103448275863 * HEIGHT);
        final Paint RIGHTLIGHT_FILL = new LinearGradient(WIDTH, 0.32413793103448274 * HEIGHT,
                                                         WIDTH, 0.903448275862069 * HEIGHT,
                                                         false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.1)),
                                                         new Stop(0.5, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.4)),
                                                         new Stop(1.0, Color.color(control.getGlowColor().getRed(), control.getGlowColor().getGreen(), control.getGlowColor().getBlue(), 0.1)));
        rightLight.setFill(RIGHTLIGHT_FILL);
        rightLight.setStroke(null);

        foreground2.getChildren().addAll(CONNECTOR,
                                         TUBE,
                                         HL_MIDDLE,
                                         HL_LEFT,
                                         HL_RIGHT,
                                         HL_UPPER,
                                         HL_TOP,
                                         leftLight,
                                         rightLight);
        foreground2.setCache(true);
    }
}
