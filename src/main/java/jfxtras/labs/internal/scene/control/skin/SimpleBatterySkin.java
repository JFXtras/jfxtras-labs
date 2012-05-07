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
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
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
import jfxtras.labs.internal.scene.control.behavior.SimpleBatteryBehavior;
import jfxtras.labs.scene.control.gauge.Battery;
import jfxtras.labs.scene.control.gauge.GradientLookup;
import jfxtras.labs.scene.control.gauge.SimpleBattery;


/**
 * Created by
 * User: hansolo
 * Date: 30.03.12
 * Time: 09:19
 */
public class SimpleBatterySkin extends SkinBase<SimpleBattery, SimpleBatteryBehavior> {
    private SimpleBattery  control;
    private boolean        isDirty;
    private boolean        initialized;
    private Group          background;
    private Group          main;
    private Group          foreground;
    private Path           plug;
    private Path           flashFrame;
    private Path           flashMain;
    private Rectangle      fluid;
    private GradientLookup lookup;
    private Color          currentLevelColor;


    // ******************** Constructors **************************************
    public SimpleBatterySkin(final SimpleBattery CONTROL) {
        super(CONTROL, new SimpleBatteryBehavior(CONTROL));
        control           = CONTROL;
        initialized       = false;
        isDirty           = false;
        background        = new Group();
        main              = new Group();
        foreground        = new Group();
        plug              = new Path();
        flashFrame        = new Path();
        flashMain         = new Path();
        fluid             = new Rectangle();
        lookup            = new GradientLookup(control.getLevelColors());
        currentLevelColor = Color.RED;

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(128, 128);
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
        registerChangeListener(control.chargingProperty(), "CHARGING");
        registerChangeListener(control.chargeIndicatorProperty(), "CHARGE_INDICATOR");
        registerChangeListener(control.chargingLevelProperty(), "CHARGE_LEVEL");
        registerChangeListener(control.levelColorsProperty(), "LEVEL_COLORS");

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
        drawMain();
        drawForeground();

        getChildren().addAll(background,
                             main,
                             foreground);
    }

    @Override
    protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("CHARGING".equals(PROPERTY)) {
            plug.setVisible(control.isCharging());
            flashFrame.setVisible(control.isCharging());
            flashMain.setVisible(control.isCharging());
        } else if("CHARGE_INDICATOR".equals(PROPERTY)) {
            if (control.getChargeIndicator() == SimpleBattery.ChargeIndicator.PLUG) {
                plug.setOpacity(1.0);
                flashFrame.setOpacity(0.0);
                flashMain.setOpacity(0.0);
            } else {
                plug.setOpacity(0.0);
                flashFrame.setOpacity(1.0);
                flashMain.setOpacity(1.0);
            }
        } else if("CHARGE_LEVEL".equals(PROPERTY)) {
            currentLevelColor = lookup.getColorAt(control.getChargingLevel());
            updateFluid();
        } else if ("LEVEL_COLORS".equals(PROPERTY)) {
            lookup = new GradientLookup(control.getLevelColors());
            updateFluid();
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
    public final SimpleBattery getSkinnable() {
        return control;
    }

    @Override
    public final void dispose() {
        control = null;
    }

    @Override
    protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 128;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override
    protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 128;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }


    // ******************** Drawing related ***********************************
    public final void drawBackground() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        background.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        background.getChildren().add(IBOUNDS);

        final Path BODY = new Path();
        BODY.setFillRule(FillRule.EVEN_ODD);
        BODY.getElements().add(new MoveTo(0.0703125 * WIDTH, 0.3203125 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.0703125 * WIDTH, 0.296875 * HEIGHT,
                                                0.0703125 * WIDTH, 0.296875 * HEIGHT,
                                                0.09375 * WIDTH, 0.296875 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.09375 * WIDTH, 0.296875 * HEIGHT,
                                                0.8359375 * WIDTH, 0.296875 * HEIGHT,
                                                0.8359375 * WIDTH, 0.296875 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.859375 * WIDTH, 0.296875 * HEIGHT,
                                                0.859375 * WIDTH, 0.296875 * HEIGHT,
                                                0.859375 * WIDTH, 0.3203125 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.859375 * WIDTH, 0.3203125 * HEIGHT,
                                                0.859375 * WIDTH, 0.6796875 * HEIGHT,
                                                0.859375 * WIDTH, 0.6796875 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.859375 * WIDTH, 0.703125 * HEIGHT,
                                                0.859375 * WIDTH, 0.703125 * HEIGHT,
                                                0.8359375 * WIDTH, 0.703125 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.8359375 * WIDTH, 0.703125 * HEIGHT,
                                                0.09375 * WIDTH, 0.703125 * HEIGHT,
                                                0.09375 * WIDTH, 0.703125 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.0703125 * WIDTH, 0.703125 * HEIGHT,
                                                0.0703125 * WIDTH, 0.703125 * HEIGHT,
                                                0.0703125 * WIDTH, 0.6796875 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.0703125 * WIDTH, 0.6796875 * HEIGHT,
                                                0.0703125 * WIDTH, 0.3203125 * HEIGHT,
                                                0.0703125 * WIDTH, 0.3203125 * HEIGHT));
        BODY.getElements().add(new ClosePath());
        BODY.getElements().add(new MoveTo(0.0546875 * WIDTH, 0.3203125 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.0546875 * WIDTH, 0.3203125 * HEIGHT,
                                                0.0546875 * WIDTH, 0.6796875 * HEIGHT,
                                                0.0546875 * WIDTH, 0.6796875 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.0546875 * WIDTH, 0.7109375 * HEIGHT,
                                                0.0625 * WIDTH, 0.71875 * HEIGHT,
                                                0.09375 * WIDTH, 0.71875 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.09375 * WIDTH, 0.71875 * HEIGHT,
                                                0.8359375 * WIDTH, 0.71875 * HEIGHT,
                                                0.8359375 * WIDTH, 0.71875 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.8671875 * WIDTH, 0.71875 * HEIGHT,
                                                0.875 * WIDTH, 0.7109375 * HEIGHT,
                                                0.875 * WIDTH, 0.6796875 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.875 * WIDTH, 0.6796875 * HEIGHT,
                                                0.875 * WIDTH, 0.59375 * HEIGHT,
                                                0.875 * WIDTH, 0.59375 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.875 * WIDTH, 0.59375 * HEIGHT,
                                                0.9375 * WIDTH, 0.59375 * HEIGHT,
                                                0.9375 * WIDTH, 0.59375 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.9453125 * WIDTH, 0.59375 * HEIGHT,
                                                0.953125 * WIDTH, 0.5859375 * HEIGHT,
                                                0.953125 * WIDTH, 0.578125 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.953125 * WIDTH, 0.578125 * HEIGHT,
                                                0.953125 * WIDTH, 0.4296875 * HEIGHT,
                                                0.953125 * WIDTH, 0.4296875 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.953125 * WIDTH, 0.4140625 * HEIGHT,
                                                0.9453125 * WIDTH, 0.40625 * HEIGHT,
                                                0.9375 * WIDTH, 0.40625 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.9375 * WIDTH, 0.40625 * HEIGHT,
                                                0.875 * WIDTH, 0.40625 * HEIGHT,
                                                0.875 * WIDTH, 0.40625 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.875 * WIDTH, 0.40625 * HEIGHT,
                                                0.875 * WIDTH, 0.3203125 * HEIGHT,
                                                0.875 * WIDTH, 0.3203125 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.875 * WIDTH, 0.2890625 * HEIGHT,
                                                0.8671875 * WIDTH, 0.28125 * HEIGHT,
                                                0.8359375 * WIDTH, 0.28125 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.8359375 * WIDTH, 0.28125 * HEIGHT,
                                                0.09375 * WIDTH, 0.28125 * HEIGHT,
                                                0.09375 * WIDTH, 0.28125 * HEIGHT));
        BODY.getElements().add(new CubicCurveTo(0.0625 * WIDTH, 0.28125 * HEIGHT,
                                                0.0546875 * WIDTH, 0.2890625 * HEIGHT,
                                                0.0546875 * WIDTH, 0.3203125 * HEIGHT));
        BODY.getElements().add(new ClosePath());
        BODY.getStyleClass().add("simple-battery-body");

        final Path CONNECTOR = new Path();
        CONNECTOR.setFillRule(FillRule.EVEN_ODD);
        CONNECTOR.getElements().add(new MoveTo(0.875 * WIDTH, 0.40625 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.875 * WIDTH, 0.40625 * HEIGHT));
        CONNECTOR.getElements().add(new CubicCurveTo(0.875 * WIDTH, 0.40625 * HEIGHT,
                                                     0.9375 * WIDTH, 0.40625 * HEIGHT,
                                                     0.9375 * WIDTH, 0.40625 * HEIGHT));
        CONNECTOR.getElements().add(new CubicCurveTo(0.9453125 * WIDTH, 0.40625 * HEIGHT,
                                                     0.953125 * WIDTH, 0.4140625 * HEIGHT,
                                                     0.953125 * WIDTH, 0.4296875 * HEIGHT));
        CONNECTOR.getElements().add(new CubicCurveTo(0.953125 * WIDTH, 0.4296875 * HEIGHT,
                                                     0.953125 * WIDTH, 0.578125 * HEIGHT,
                                                     0.953125 * WIDTH, 0.578125 * HEIGHT));
        CONNECTOR.getElements().add(new CubicCurveTo(0.953125 * WIDTH, 0.5859375 * HEIGHT,
                                                     0.9453125 * WIDTH, 0.59375 * HEIGHT,
                                                     0.9375 * WIDTH, 0.59375 * HEIGHT));
        CONNECTOR.getElements().add(new CubicCurveTo(0.9375 * WIDTH, 0.59375 * HEIGHT,
                                                     0.875 * WIDTH, 0.59375 * HEIGHT,
                                                     0.875 * WIDTH, 0.59375 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.875 * WIDTH, 0.59375 * HEIGHT));
        CONNECTOR.getElements().add(new LineTo(0.875 * WIDTH, 0.40625 * HEIGHT));
        CONNECTOR.getElements().add(new ClosePath());
        CONNECTOR.getStyleClass().add("simple-battery-connector");

        final InnerShadow CONNECTOR_INNER_SHADOW0 = new InnerShadow();
        CONNECTOR_INNER_SHADOW0.setWidth(0.05625 * CONNECTOR.getLayoutBounds().getWidth());
        CONNECTOR_INNER_SHADOW0.setHeight(0.05625 * CONNECTOR.getLayoutBounds().getHeight());
        CONNECTOR_INNER_SHADOW0.setOffsetX(0.0);
        CONNECTOR_INNER_SHADOW0.setOffsetY(0.0);
        CONNECTOR_INNER_SHADOW0.setRadius(0.05625 * CONNECTOR.getLayoutBounds().getWidth());
        CONNECTOR_INNER_SHADOW0.setColor(Color.color(0, 0, 0, 0.6470588235));
        CONNECTOR_INNER_SHADOW0.setBlurType(BlurType.GAUSSIAN);
        CONNECTOR_INNER_SHADOW0.inputProperty().set(null);
        CONNECTOR.setEffect(CONNECTOR_INNER_SHADOW0);

        background.getChildren().addAll(BODY, CONNECTOR);
        background.setCache(true);
    }

    public final void drawMain() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        main.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        main.getChildren().add(IBOUNDS);
        fluid = new Rectangle(0.0703125 * WIDTH, 0.296875 * HEIGHT,
                              0.7890625 * WIDTH, 0.40625 * HEIGHT);
        fluid.setArcWidth(0.025 * WIDTH);
        fluid.setArcHeight(0.025 * HEIGHT);
        final Paint FLUID_FILL = new LinearGradient(0, 0.296875 * HEIGHT,
                                                    0, 0.703125 * HEIGHT,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, Color.color(0.1647058824, 0.5450980392, 0, 1)),
                                                    new Stop(0.32, Color.color(0.1647058824, 0.5450980392, 0, 1)),
                                                    new Stop(1.0, Color.color(0.4666666667, 0.8588235294, 0, 1)));
        fluid.setFill(FLUID_FILL);
        fluid.setStroke(null);
        if (Double.compare(control.getChargingLevel(), 0.0) == 0) {
            this.fluid.setVisible(false);
        }

        flashFrame = new Path();
        flashFrame.setFillRule(FillRule.EVEN_ODD);
        flashFrame.getElements().add(new MoveTo(0.59375 * WIDTH, 0.3671875 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.34375 * WIDTH, 0.546875 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.4453125 * WIDTH, 0.546875 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.3515625 * WIDTH, 0.6484375 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.5859375 * WIDTH, 0.5 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.46875 * WIDTH, 0.5 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.59375 * WIDTH, 0.3671875 * HEIGHT));
        flashFrame.getElements().add(new ClosePath());

        final Paint FLASH_FRAME_FILL = Color.WHITE;
        flashFrame.setFill(FLASH_FRAME_FILL);
        flashFrame.setStroke(null);
        if (control.getChargeIndicator() == SimpleBattery.ChargeIndicator.FLASH) {
            flashFrame.setOpacity(1.0);
        } else {
            flashFrame.setOpacity(0.0);
        }
        if (!control.isCharging()) {
            flashFrame.setVisible(false);
        }

        flashMain = new Path();
        flashMain.setFillRule(FillRule.EVEN_ODD);
        flashMain.getElements().add(new MoveTo(0.5625 * WIDTH, 0.390625 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.359375 * WIDTH, 0.5390625 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.453125 * WIDTH, 0.5390625 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.375 * WIDTH, 0.625 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.5703125 * WIDTH, 0.5078125 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.453125 * WIDTH, 0.5078125 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.5625 * WIDTH, 0.390625 * HEIGHT));
        flashMain.getElements().add(new ClosePath());

        final Paint FLASH_MAIN_FILL = Color.color(0.9960784314, 0.9215686275, 0, 1);
        flashMain.setFill(FLASH_MAIN_FILL);
        flashMain.setStroke(null);
        if (control.getChargeIndicator() == SimpleBattery.ChargeIndicator.FLASH) {
            flashMain.setOpacity(1.0);
        } else {
            flashMain.setOpacity(0.0);
        }
        if (!control.isCharging()) {
            flashMain.setVisible(false);
        }

        final InnerShadow FLASH_MAIN_INNER_SHADOW = new InnerShadow();
        FLASH_MAIN_INNER_SHADOW.setWidth(0.084375 * flashMain.getLayoutBounds().getWidth());
        FLASH_MAIN_INNER_SHADOW.setHeight(0.084375 * flashMain.getLayoutBounds().getHeight());
        FLASH_MAIN_INNER_SHADOW.setOffsetX(0.0);
        FLASH_MAIN_INNER_SHADOW.setOffsetY(0.0);
        FLASH_MAIN_INNER_SHADOW.setRadius(0.084375 * flashMain.getLayoutBounds().getWidth());
        FLASH_MAIN_INNER_SHADOW.setColor(Color.color(0.8509803922, 0.5294117647, 0, 1));
        FLASH_MAIN_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        FLASH_MAIN_INNER_SHADOW.inputProperty().set(null);
        flashMain.setEffect(FLASH_MAIN_INNER_SHADOW);

        plug = new Path();
        plug.setFillRule(FillRule.EVEN_ODD);
        plug.getElements().add(new MoveTo(0.5390625 * WIDTH, 0.484375 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5390625 * WIDTH, 0.484375 * HEIGHT,
                                                    0.609375 * WIDTH, 0.484375 * HEIGHT,
                                                    0.609375 * WIDTH, 0.484375 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.6171875 * WIDTH, 0.484375 * HEIGHT,
                                                    0.625 * WIDTH, 0.4765625 * HEIGHT,
                                                    0.625 * WIDTH, 0.46875 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.625 * WIDTH, 0.46875 * HEIGHT,
                                                    0.625 * WIDTH, 0.4609375 * HEIGHT,
                                                    0.625 * WIDTH, 0.4609375 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.625 * WIDTH, 0.4609375 * HEIGHT,
                                                    0.6171875 * WIDTH, 0.453125 * HEIGHT,
                                                    0.609375 * WIDTH, 0.453125 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.609375 * WIDTH, 0.453125 * HEIGHT,
                                                    0.5390625 * WIDTH, 0.453125 * HEIGHT,
                                                    0.5390625 * WIDTH, 0.453125 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5390625 * WIDTH, 0.4296875 * HEIGHT,
                                                    0.5390625 * WIDTH, 0.40625 * HEIGHT,
                                                    0.5390625 * WIDTH, 0.40625 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.4453125 * WIDTH, 0.40625 * HEIGHT,
                                                    0.390625 * WIDTH, 0.4453125 * HEIGHT,
                                                    0.375 * WIDTH, 0.484375 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.375 * WIDTH, 0.484375 * HEIGHT,
                                                    0.3046875 * WIDTH, 0.484375 * HEIGHT,
                                                    0.3046875 * WIDTH, 0.484375 * HEIGHT));
        plug.getElements().add(new LineTo(0.3046875 * WIDTH, 0.5234375 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.3046875 * WIDTH, 0.5234375 * HEIGHT,
                                                    0.3828125 * WIDTH, 0.5234375 * HEIGHT,
                                                    0.3828125 * WIDTH, 0.5234375 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.390625 * WIDTH, 0.5625 * HEIGHT,
                                                    0.4296875 * WIDTH, 0.59375 * HEIGHT,
                                                    0.5390625 * WIDTH, 0.59375 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5390625 * WIDTH, 0.59375 * HEIGHT,
                                                    0.5390625 * WIDTH, 0.578125 * HEIGHT,
                                                    0.5390625 * WIDTH, 0.5546875 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5390625 * WIDTH, 0.5546875 * HEIGHT,
                                                    0.6171875 * WIDTH, 0.5546875 * HEIGHT,
                                                    0.6171875 * WIDTH, 0.5546875 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.6171875 * WIDTH, 0.5546875 * HEIGHT,
                                                    0.625 * WIDTH, 0.546875 * HEIGHT,
                                                    0.625 * WIDTH, 0.546875 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.625 * WIDTH, 0.546875 * HEIGHT,
                                                    0.625 * WIDTH, 0.53125 * HEIGHT,
                                                    0.625 * WIDTH, 0.53125 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.625 * WIDTH, 0.53125 * HEIGHT,
                                                    0.6171875 * WIDTH, 0.5234375 * HEIGHT,
                                                    0.6171875 * WIDTH, 0.5234375 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.6171875 * WIDTH, 0.5234375 * HEIGHT,
                                                    0.5390625 * WIDTH, 0.5234375 * HEIGHT,
                                                    0.5390625 * WIDTH, 0.5234375 * HEIGHT));
        plug.getElements().add(new LineTo(0.5390625 * WIDTH, 0.484375 * HEIGHT));
        plug.getElements().add(new ClosePath());

        plug.setFill(Color.rgb(51, 51, 51));
        plug.setStroke(null);

        if (control.getChargeIndicator() == SimpleBattery.ChargeIndicator.PLUG) {
            plug.setOpacity(1.0);
        } else {
            plug.setOpacity(0.0);
        }
        if (!control.isCharging()) {
            plug.setVisible(false);
        }

        main.getChildren().addAll(fluid,
                                  flashFrame,
                                  flashMain,
                                  plug);
    }

    private final void updateFluid() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
            if (Double.compare(control.getChargingLevel(), 0) == 0) {
                fluid.setVisible(false);
            } else {
                fluid.setVisible(true);
            }

            fluid.setWidth(control.getChargingLevel() * 0.7890625 * control.getPrefWidth());
            fluid.setFill(new LinearGradient(0, 0.296875 * control.getPrefHeight(),
                                             0, 0.703125 * control.getPrefHeight(),
                                             false, CycleMethod.NO_CYCLE,
                                             new Stop(0.0, Color.hsb(currentLevelColor.getHue(), currentLevelColor.getSaturation(), 0.5)),
                                             new Stop(0.32, Color.hsb(currentLevelColor.getHue(), currentLevelColor.getSaturation(), 0.5)),
                                             new Stop(1.0, currentLevelColor)));
            }
        });
    }

    public final void drawForeground() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;
        foreground.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        foreground.getChildren().add(IBOUNDS);

        final Rectangle REFLECTION = new Rectangle(0.0703125 * WIDTH, 0.296875 * HEIGHT,
                                                   0.7890625 * WIDTH, 0.40625 * HEIGHT);
        REFLECTION.setArcWidth(0.025 * WIDTH);
        REFLECTION.setArcHeight(0.025 * HEIGHT);
        REFLECTION.getStyleClass().add("simple-battery-reflection");

        foreground.getChildren().addAll(REFLECTION);
        foreground.setCache(true);
    }

}
