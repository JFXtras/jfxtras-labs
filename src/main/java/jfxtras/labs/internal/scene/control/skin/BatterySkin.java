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
import javafx.scene.effect.InnerShadow;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jfxtras.labs.internal.scene.control.behavior.BatteryBehavior;
import jfxtras.labs.scene.control.gauge.Battery;
import jfxtras.labs.scene.control.gauge.GradientLookup;


/**
 * Created by
 * User: hansolo
 * Date: 23.03.12
 * Time: 11:07
 */
public class BatterySkin extends SkinBase<Battery, BatteryBehavior> {
    private Battery        control;
    private boolean        isDirty;
    private boolean        initialized;
    private Group          background;
    private Group          main;
    private Group          foreground;
    private Path           plug;
    private Path           flashFrame;
    private Path           flashMain;
    private Rectangle      fluid;
    private Rectangle      fluidHighlight;
    private GradientLookup lookup;
    private Color          currentLevelColor;


    // ******************** Constructors **************************************
    public BatterySkin(final Battery CONTROL) {
        super(CONTROL, new BatteryBehavior(CONTROL));
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
        fluidHighlight    = new Rectangle();
        lookup            = new GradientLookup(new Stop[]{new Stop(0.0, Color.RED), new Stop(0.55, Color.YELLOW), new Stop(1.0, Color.LIME)});
        currentLevelColor = Color.RED;

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(120, 255);
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

        // Add bindings
        addBindings();

        // Register listeners
        registerChangeListener(control.chargingProperty(), "CHARGING");
        registerChangeListener(control.chargeIndicatorProperty(), "CHARGE_INDICATOR");
        registerChangeListener(control.chargingLevelProperty(), "CHARGE_LEVEL");

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

    private void addBindings() {
        if (plug.visibleProperty().isBound()) {
            plug.visibleProperty().unbind();
        }
        plug.visibleProperty().bind(control.chargingProperty());

        if (flashFrame.visibleProperty().isBound()) {
            flashFrame.visibleProperty().unbind();
        }
        flashFrame.visibleProperty().bind(control.chargingProperty());
        if (flashMain.visibleProperty().isBound()) {
            flashMain.visibleProperty().unbind();
        }
        flashMain.visibleProperty().bind(control.chargingProperty());
    }

    @Override
    protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if (PROPERTY == "CHARGING") {
            //chargeTimer.start();
        } else if(PROPERTY == "CHARGE_INDICATOR") {
            if (control.getChargeIndicator() == Battery.ChargeIndicator.PLUG) {
                plug.setOpacity(1.0);
                flashFrame.setOpacity(0.0);
                flashMain.setOpacity(0.0);
            } else {
                plug.setOpacity(0.0);
                flashFrame.setOpacity(1.0);
                flashMain.setOpacity(0.0);
            }
        } else if(PROPERTY == "CHARGE_LEVEL") {
            currentLevelColor = lookup.getColorAt(control.getChargingLevel());
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
    public final Battery getSkinnable() {
        return control;
    }

    @Override
    public final void dispose() {
        control = null;
    }

    @Override
    protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 120;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override
    protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 255;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }


    // ******************** Drawing related ***********************************
    public final void drawBackground() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();
        background = new Group();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        background.getChildren().add(IBOUNDS);

        final Path EMPTY_BOTTOM = new Path();
        EMPTY_BOTTOM.setFillRule(FillRule.EVEN_ODD);
        EMPTY_BOTTOM.getElements().add(new MoveTo(0.016666666666666666 * WIDTH, 0.9372549019607843 * HEIGHT));
        EMPTY_BOTTOM.getElements().add(new CubicCurveTo(0.016666666666666666 * WIDTH, 0.9294117647058824 * HEIGHT,
                                                        0.23333333333333334 * WIDTH, 0.9215686274509803 * HEIGHT,
                                                        0.5 * WIDTH, 0.9215686274509803 * HEIGHT));
        EMPTY_BOTTOM.getElements().add(new CubicCurveTo(0.7666666666666667 * WIDTH, 0.9215686274509803 * HEIGHT,
                                                        0.9833333333333333 * WIDTH, 0.9294117647058824 * HEIGHT,
                                                        0.9833333333333333 * WIDTH, 0.9372549019607843 * HEIGHT));
        EMPTY_BOTTOM.getElements().add(new CubicCurveTo(0.9833333333333333 * WIDTH, 0.9450980392156862 * HEIGHT,
                                                        0.7666666666666667 * WIDTH, 0.9529411764705882 * HEIGHT,
                                                        0.5 * WIDTH, 0.9529411764705882 * HEIGHT));
        EMPTY_BOTTOM.getElements().add(new CubicCurveTo(0.23333333333333334 * WIDTH, 0.9529411764705882 * HEIGHT,
                                                        0.016666666666666666 * WIDTH, 0.9450980392156862 * HEIGHT,
                                                        0.016666666666666666 * WIDTH, 0.9372549019607843 * HEIGHT));
        EMPTY_BOTTOM.getElements().add(new ClosePath());
        final Paint EMPTY_BOTTOM_FILL = Color.color(0.2, 0.2, 0.2, 1);
        EMPTY_BOTTOM.setFill(EMPTY_BOTTOM_FILL);
        EMPTY_BOTTOM.setStroke(null);

        final Rectangle EMPTY_BACKGROUND = new Rectangle(0.0166666667 * WIDTH, 0.1137254902 * HEIGHT,
                                                         0.9666666667 * WIDTH, 0.8235294118 * HEIGHT);
        final Paint EMPTY_BACKGROUND_FILL = new LinearGradient(0.0166666667 * WIDTH, 0,
                                                               0.0166666667 * WIDTH + 0.9666666667 * WIDTH, 0,
                                                               false, CycleMethod.NO_CYCLE,
                                                               new Stop(0.0, Color.color(0.2, 0.2, 0.2, 0.6)),
                                                               new Stop(0.23, Color.color(0.4, 0.4, 0.4, 0.5019607843)),
                                                               new Stop(0.48, Color.color(0.9764705882, 0.9764705882, 0.9764705882, 0.4)),
                                                               new Stop(0.49, Color.color(1, 1, 1, 0.4039215686)),
                                                               new Stop(0.81, Color.color(0.6, 0.6, 0.6, 0.5254901961)),
                                                               new Stop(1.0, Color.color(0.2, 0.2, 0.2, 0.6)));
        EMPTY_BACKGROUND.setFill(EMPTY_BACKGROUND_FILL);
        EMPTY_BACKGROUND.setStroke(null);

        background.getChildren().addAll(EMPTY_BOTTOM,
                                        EMPTY_BACKGROUND);
    }

    public final void drawMain() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        main = new Group();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        main.getChildren().add(IBOUNDS);

        fluid = new Rectangle(0.0166666667 * WIDTH, 0.1137254902 * HEIGHT,
                              0.9666666667 * WIDTH, 0.8235294118 * HEIGHT);
        final LinearGradient FLUID_FILL = new LinearGradient(0.0166666667 * WIDTH, 0,
                                                             0.0166666667 * WIDTH + 0.9666666667 * WIDTH, 0,
                                                             false, CycleMethod.NO_CYCLE,
                                                             new Stop(0.0, currentLevelColor.darker().darker()),
                                                             new Stop(0.2, currentLevelColor.darker()),
                                                             new Stop(0.5, currentLevelColor),
                                                             new Stop(0.8, currentLevelColor.darker()),
                                                             new Stop(1.0, currentLevelColor.darker().darker()));
        fluid.setFill(FLUID_FILL);
        fluid.setStroke(null);
        if (Double.compare(control.getChargingLevel(), 0.0) == 0) {
            fluid.setVisible(false);
        }

        fluidHighlight = new Rectangle(0.0166666667 * WIDTH, 0.1137254902 * HEIGHT,
                                       0.9666666667 * WIDTH, 0.0078431373 * HEIGHT);
        final Paint FLUID_HIGHLIGHT_FILL = new LinearGradient(0.0166666667 * WIDTH, 0,
                                                              0.0166666667 * WIDTH + 0.9666666667 * WIDTH, 0,
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, Color.color(1, 1, 1, 0.4980392157)),
                                                              new Stop(0.35, Color.color(1, 1, 1, 0.7764705882)),
                                                              new Stop(0.63, Color.color(1, 1, 1, 0.7843137255)),
                                                              new Stop(1.0, Color.color(1, 1, 1, 0.4980392157)));
        fluidHighlight.setFill(FLUID_HIGHLIGHT_FILL);
        fluidHighlight.setStroke(null);

        plug = new Path();
        plug.setFillRule(FillRule.EVEN_ODD);
        plug.getElements().add(new MoveTo(0.4666666666666667 * WIDTH, 0.4196078431372549 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.4666666666666667 * WIDTH, 0.4196078431372549 * HEIGHT,
                                                0.4666666666666667 * WIDTH, 0.34509803921568627 * HEIGHT,
                                                0.4666666666666667 * WIDTH, 0.34509803921568627 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.4666666666666667 * WIDTH, 0.3411764705882353 * HEIGHT,
                                                0.45 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.44166666666666665 * WIDTH, 0.33725490196078434 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.44166666666666665 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.43333333333333335 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.43333333333333335 * WIDTH, 0.33725490196078434 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.4166666666666667 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.4083333333333333 * WIDTH, 0.3411764705882353 * HEIGHT,
                                                0.4083333333333333 * WIDTH, 0.34901960784313724 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.4083333333333333 * WIDTH, 0.34901960784313724 * HEIGHT,
                                                0.4083333333333333 * WIDTH, 0.4196078431372549 * HEIGHT,
                                                0.4083333333333333 * WIDTH, 0.4196078431372549 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.35 * WIDTH, 0.4196078431372549 * HEIGHT,
                                                0.31666666666666665 * WIDTH, 0.4235294117647059 * HEIGHT,
                                                0.31666666666666665 * WIDTH, 0.4235294117647059 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.31666666666666665 * WIDTH, 0.5137254901960784 * HEIGHT,
                                                0.375 * WIDTH, 0.5686274509803921 * HEIGHT,
                                                0.4666666666666667 * WIDTH, 0.5803921568627451 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.4666666666666667 * WIDTH, 0.5803921568627451 * HEIGHT,
                                                0.4666666666666667 * WIDTH, 0.6588235294117647 * HEIGHT,
                                                0.4666666666666667 * WIDTH, 0.6588235294117647 * HEIGHT));
        plug.getElements().add(new LineTo(0.5583333333333333 * WIDTH, 0.6588235294117647 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5583333333333333 * WIDTH, 0.6588235294117647 * HEIGHT,
                                                0.5583333333333333 * WIDTH, 0.5843137254901961 * HEIGHT,
                                                0.5583333333333333 * WIDTH, 0.5843137254901961 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.65 * WIDTH, 0.5725490196078431 * HEIGHT,
                                                0.7166666666666667 * WIDTH, 0.5294117647058824 * HEIGHT,
                                                0.7166666666666667 * WIDTH, 0.4235294117647059 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.7166666666666667 * WIDTH, 0.4235294117647059 * HEIGHT,
                                                0.6833333333333333 * WIDTH, 0.4235294117647059 * HEIGHT,
                                                0.625 * WIDTH, 0.4196078431372549 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.625 * WIDTH, 0.4196078431372549 * HEIGHT,
                                                0.625 * WIDTH, 0.34901960784313724 * HEIGHT,
                                                0.625 * WIDTH, 0.34901960784313724 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.625 * WIDTH, 0.3411764705882353 * HEIGHT,
                                                0.6166666666666667 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.6 * WIDTH, 0.33725490196078434 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.6 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.5833333333333334 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.5833333333333334 * WIDTH, 0.33725490196078434 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5666666666666667 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.5583333333333333 * WIDTH, 0.3411764705882353 * HEIGHT,
                                                0.5583333333333333 * WIDTH, 0.34901960784313724 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5583333333333333 * WIDTH, 0.34901960784313724 * HEIGHT,
                                                0.5583333333333333 * WIDTH, 0.4196078431372549 * HEIGHT,
                                                0.5583333333333333 * WIDTH, 0.4196078431372549 * HEIGHT));
        plug.getElements().add(new LineTo(0.4666666666666667 * WIDTH, 0.4196078431372549 * HEIGHT));
        plug.getElements().add(new ClosePath());
        final Paint PLUG_FILL = Color.color(0.7411764706, 0.7411764706, 0.7411764706, 1);
        plug.setFill(PLUG_FILL);
        plug.setStroke(null);

        final InnerShadow PLUG_INNER_SHADOW = new InnerShadow();
        PLUG_INNER_SHADOW.setWidth(0.03 * plug.getLayoutBounds().getWidth());
        PLUG_INNER_SHADOW.setHeight(0.03 * plug.getLayoutBounds().getHeight());
        PLUG_INNER_SHADOW.setOffsetX(0.0);
        PLUG_INNER_SHADOW.setOffsetY(0.0);
        PLUG_INNER_SHADOW.setRadius(0.03 * plug.getLayoutBounds().getWidth());
        PLUG_INNER_SHADOW.setColor(Color.BLACK);
        PLUG_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        PLUG_INNER_SHADOW.inputProperty().set(null);
        plug.setEffect(PLUG_INNER_SHADOW);
        if (control.getChargeIndicator() == Battery.ChargeIndicator.PLUG) {
            plug.setOpacity(1.0);
        } else {
            plug.setOpacity(0.0);
        }
        if (!control.isCharging()) {
            plug.setVisible(false);
        }

        flashFrame = new Path();
        flashFrame.setFillRule(FillRule.EVEN_ODD);
        flashFrame.getElements().add(new MoveTo(0.19166666666666668 * WIDTH, 0.37254901960784315 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.575 * WIDTH, 0.6274509803921569 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.575 * WIDTH, 0.5254901960784314 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.7916666666666666 * WIDTH, 0.615686274509804 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.48333333333333334 * WIDTH, 0.3843137254901961 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.48333333333333334 * WIDTH, 0.5019607843137255 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.19166666666666668 * WIDTH, 0.37254901960784315 * HEIGHT));
        flashFrame.getElements().add(new ClosePath());
        final Paint FLASH_FRAME_FILL = Color.WHITE;
        flashFrame.setFill(FLASH_FRAME_FILL);
        flashFrame.setStroke(null);
        if (control.getChargeIndicator() == Battery.ChargeIndicator.FLASH) {
            flashFrame.setOpacity(1.0);
        } else {
            flashFrame.setOpacity(0.0);
        }
        if (!control.isCharging()) {
            flashFrame.setVisible(false);
        }

        flashMain = new Path();
        flashMain.setFillRule(FillRule.EVEN_ODD);
        flashMain.getElements().add(new MoveTo(0.25 * WIDTH, 0.403921568627451 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.5666666666666667 * WIDTH, 0.611764705882353 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.5666666666666667 * WIDTH, 0.5137254901960784 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.75 * WIDTH, 0.592156862745098 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.49166666666666664 * WIDTH, 0.4 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.49166666666666664 * WIDTH, 0.5137254901960784 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.25 * WIDTH, 0.403921568627451 * HEIGHT));
        flashMain.getElements().add(new ClosePath());
        final Paint FLASH_MAIN_FILL = Color.color(0.9960784314, 0.9215686275, 0, 1);
        flashMain.setFill(FLASH_MAIN_FILL);
        flashMain.setStroke(null);
        if (control.getChargeIndicator() == Battery.ChargeIndicator.FLASH) {
            flashMain.setOpacity(1.0);
        } else {
            flashMain.setOpacity(0.0);
        }
        if (!control.isCharging()) {
            flashMain.setVisible(false);
        }

        final InnerShadow FLASH_MAIN_INNER_SHADOW = new InnerShadow();
        FLASH_MAIN_INNER_SHADOW.setWidth(0.180 * flashMain.getLayoutBounds().getWidth());
        FLASH_MAIN_INNER_SHADOW.setHeight(0.180 * flashMain.getLayoutBounds().getHeight());
        FLASH_MAIN_INNER_SHADOW.setOffsetX(0.0);
        FLASH_MAIN_INNER_SHADOW.setOffsetY(-0.0);
        FLASH_MAIN_INNER_SHADOW.setRadius(0.180 * flashMain.getLayoutBounds().getWidth());
        FLASH_MAIN_INNER_SHADOW.setColor(Color.color(0.8509803922, 0.5294117647, 0, 1));
        FLASH_MAIN_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        FLASH_MAIN_INNER_SHADOW.inputProperty().set(null);
        flashMain.setEffect(FLASH_MAIN_INNER_SHADOW);

        main.getChildren().addAll(fluid,
                                  fluidHighlight,
                                  plug,
                                  flashFrame,
                                  flashMain);
    }

    private final void updateFluid() {
        if (Double.compare(control.getChargingLevel(), 0) == 0) {
            fluid.setVisible(false);
        } else {
            fluid.setVisible(true);
        }
        fluid.setHeight(control.getChargingLevel() * 0.8235294118 * control.getPrefHeight());
        fluid.setY(0.1137254902 * control.getPrefHeight() + (0.8235294118 * control.getPrefHeight() - fluid.getHeight()));
        fluid.setFill(new LinearGradient(0.0166666667 * control.getPrefWidth(), 0,
                                         0.0166666667 * control.getPrefWidth() + 0.9666666667 * control.getPrefWidth(), 0,
                                         false, CycleMethod.NO_CYCLE,
                                         new Stop(0.0, currentLevelColor.darker().darker()),
                                         new Stop(0.2, currentLevelColor.darker()),
                                         new Stop(0.5, currentLevelColor),
                                         new Stop(0.8, currentLevelColor.darker()),
                                         new Stop(1.0, currentLevelColor.darker().darker())));
        fluidHighlight.setY(0.1137254902 * control.getPrefHeight() + (0.8235294118 * control.getPrefHeight() - fluid.getHeight()));
    }

    public final void drawForeground() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();
        foreground = new Group();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        foreground.getChildren().add(IBOUNDS);

        final Rectangle MAIN_REFLECTION = new Rectangle(0.0083333333 * WIDTH, 0.1137254902 * HEIGHT,
                                                        0.9833333333 * WIDTH, 0.8235294118 * HEIGHT);
        final Paint MAIN_REFLECTION_FILL = new LinearGradient(0.0083333333 * WIDTH, 0,
                                                              0.0083333333 * WIDTH + 0.9833333333 * WIDTH, 0,
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, Color.color(1, 1, 1, 0.2862745098)),
                                                              new Stop(0.04, Color.color(1, 1, 1, 0.1176470588)),
                                                              new Stop(0.05, Color.color(1, 1, 1, 0.0470588235)),
                                                              new Stop(0.11, Color.color(1, 1, 1, 0.0470588235)),
                                                              new Stop(0.13, Color.color(1, 1, 1, 0.0470588235)),
                                                              new Stop(0.15, Color.color(1, 1, 1, 0.1686274510)),
                                                              new Stop(0.151, Color.color(1, 1, 1, 0.2)),
                                                              new Stop(0.39, Color.color(1, 1, 1, 0.0313725490)),
                                                              new Stop(0.43, Color.color(1, 1, 1, 0.0392156863)),
                                                              new Stop(0.44, Color.color(1, 1, 1, 0)),
                                                              new Stop(0.55, Color.color(1, 1, 1, 0.0901960784)),
                                                              new Stop(0.56, Color.color(1, 1, 1, 0.0980392157)),
                                                              new Stop(0.69, Color.color(1, 1, 1, 0.0078431373)),
                                                              new Stop(0.7, Color.color(1, 1, 1, 0.0352941176)),
                                                              new Stop(0.78, Color.color(1, 1, 1, 0.0862745098)),
                                                              new Stop(0.79, Color.color(1, 1, 1, 0.0980392157)),
                                                              new Stop(0.8, Color.color(1, 1, 1, 0.1137254902)),
                                                              new Stop(0.81, Color.color(1, 1, 1, 0.1490196078)),
                                                              new Stop(0.89, Color.color(1, 1, 1, 0.1960784314)),
                                                              new Stop(0.891, Color.color(1, 1, 1, 0.2)),
                                                              new Stop(0.92, Color.color(1, 1, 1, 0.0470588235)),
                                                              new Stop(0.93, Color.color(1, 1, 1, 0.0470588235)),
                                                              new Stop(0.96, Color.color(1, 1, 1, 0.0470588235)),
                                                              new Stop(0.97, Color.color(1, 1, 1, 0.0470588235)),
                                                              new Stop(0.98, Color.color(1, 1, 1, 0.2235294118)),
                                                              new Stop(1.0, Color.color(1, 1, 1, 0.4980392157)));
        MAIN_REFLECTION.setFill(MAIN_REFLECTION_FILL);
        MAIN_REFLECTION.setStroke(null);

        final Path TOP_REFLECTION = new Path();
        TOP_REFLECTION.setFillRule(FillRule.EVEN_ODD);
        TOP_REFLECTION.getElements().add(new MoveTo(0.008333333333333333 * WIDTH, 0.11372549019607843 * HEIGHT));
        TOP_REFLECTION.getElements().add(new LineTo(0.9916666666666667 * WIDTH, 0.11372549019607843 * HEIGHT));
        TOP_REFLECTION.getElements().add(new LineTo(0.9916666666666667 * WIDTH, 0.3176470588235294 * HEIGHT));
        TOP_REFLECTION.getElements().add(new CubicCurveTo(0.9916666666666667 * WIDTH, 0.3176470588235294 * HEIGHT,
                                                          0.8583333333333333 * WIDTH, 0.3411764705882353 * HEIGHT,
                                                          0.675 * WIDTH, 0.41568627450980394 * HEIGHT));
        TOP_REFLECTION.getElements().add(new CubicCurveTo(0.49166666666666664 * WIDTH, 0.47843137254901963 * HEIGHT,
                                                          0.275 * WIDTH, 0.48627450980392156 * HEIGHT,
                                                          0.275 * WIDTH, 0.48627450980392156 * HEIGHT));
        TOP_REFLECTION.getElements().add(new CubicCurveTo(0.16666666666666666 * WIDTH, 0.48627450980392156 * HEIGHT,
                                                          0.008333333333333333 * WIDTH, 0.47058823529411764 * HEIGHT,
                                                          0.008333333333333333 * WIDTH, 0.47058823529411764 * HEIGHT));
        TOP_REFLECTION.getElements().add(new LineTo(0.008333333333333333 * WIDTH, 0.11372549019607843 * HEIGHT));
        TOP_REFLECTION.getElements().add(new ClosePath());
        final Paint TOP_REFLECTION_FILL = new LinearGradient(0.008333333333333333 * WIDTH, 0.2549019607843137 * HEIGHT,
                                                             WIDTH, 0.2549019607843137 * HEIGHT,
                                                             false, CycleMethod.NO_CYCLE,
                                                             new Stop(0.0, Color.color(1, 1, 1, 0.2862745098)),
                                                             new Stop(0.04, Color.color(1, 1, 1, 0.1176470588)),
                                                             new Stop(0.05, Color.color(1, 1, 1, 0.0470588235)),
                                                             new Stop(0.11, Color.color(1, 1, 1, 0.0470588235)),
                                                             new Stop(0.13, Color.color(1, 1, 1, 0.0470588235)),
                                                             new Stop(0.15, Color.color(1, 1, 1, 0.1686274510)),
                                                             new Stop(0.151, Color.color(1, 1, 1, 0.2)),
                                                             new Stop(0.39, Color.color(1, 1, 1, 0.0313725490)),
                                                             new Stop(0.43, Color.color(1, 1, 1, 0.0392156863)),
                                                             new Stop(0.44, Color.color(1, 1, 1, 0)),
                                                             new Stop(0.55, Color.color(1, 1, 1, 0.0901960784)),
                                                             new Stop(0.56, Color.color(1, 1, 1, 0.0980392157)),
                                                             new Stop(0.69, Color.color(1, 1, 1, 0.0078431373)),
                                                             new Stop(0.7, Color.color(1, 1, 1, 0.0352941176)),
                                                             new Stop(0.78, Color.color(1, 1, 1, 0.0862745098)),
                                                             new Stop(0.79, Color.color(1, 1, 1, 0.0980392157)),
                                                             new Stop(0.8, Color.color(1, 1, 1, 0.1137254902)),
                                                             new Stop(0.81, Color.color(1, 1, 1, 0.1490196078)),
                                                             new Stop(0.89, Color.color(1, 1, 1, 0.1960784314)),
                                                             new Stop(0.891, Color.color(1, 1, 1, 0.2)),
                                                             new Stop(0.92, Color.color(1, 1, 1, 0.0470588235)),
                                                             new Stop(0.93, Color.color(1, 1, 1, 0.0470588235)),
                                                             new Stop(1.0, Color.color(1, 1, 1, 0.0470588235)));
        TOP_REFLECTION.setFill(TOP_REFLECTION_FILL);
        TOP_REFLECTION.setStroke(null);

        final Path BOTTOM = new Path();
        BOTTOM.setFillRule(FillRule.EVEN_ODD);
        BOTTOM.getElements().add(new MoveTo(0.0, 0.9490196078431372 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.0, 0.9411764705882353 * HEIGHT,
                                                  0.008333333333333333 * WIDTH, 0.9333333333333333 * HEIGHT,
                                                  0.025 * WIDTH, 0.9333333333333333 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.025 * WIDTH, 0.9333333333333333 * HEIGHT,
                                                  0.19166666666666668 * WIDTH, 0.9372549019607843 * HEIGHT,
                                                  0.5 * WIDTH, 0.9372549019607843 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.8 * WIDTH, 0.9372549019607843 * HEIGHT,
                                                  0.975 * WIDTH, 0.9333333333333333 * HEIGHT,
                                                  0.975 * WIDTH, 0.9333333333333333 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.9916666666666667 * WIDTH, 0.9333333333333333 * HEIGHT,
                                                  WIDTH, 0.9411764705882353 * HEIGHT,
                                                  WIDTH, 0.9490196078431372 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(WIDTH, 0.9490196078431372 * HEIGHT,
                                                  WIDTH, 0.9764705882352941 * HEIGHT,
                                                  WIDTH, 0.9764705882352941 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(WIDTH, 0.984313725490196 * HEIGHT,
                                                  0.9916666666666667 * WIDTH, 0.9921568627450981 * HEIGHT,
                                                  0.975 * WIDTH, 0.9921568627450981 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.975 * WIDTH, 0.9921568627450981 * HEIGHT,
                                                  0.8833333333333333 * WIDTH, 0.996078431372549 * HEIGHT,
                                                  0.5 * WIDTH, 0.996078431372549 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.125 * WIDTH, 0.996078431372549 * HEIGHT,
                                                  0.025 * WIDTH, 0.9921568627450981 * HEIGHT,
                                                  0.025 * WIDTH, 0.9921568627450981 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.008333333333333333 * WIDTH, 0.9921568627450981 * HEIGHT,
                                                  0.0, 0.984313725490196 * HEIGHT,
                                                  0.0, 0.9764705882352941 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.0, 0.9764705882352941 * HEIGHT,
                                                  0.0, 0.9490196078431372 * HEIGHT,
                                                  0.0, 0.9490196078431372 * HEIGHT));
        BOTTOM.getElements().add(new ClosePath());
        final Paint BOTTOM_FILL = new LinearGradient(0.0, 0.9607843137254902 * HEIGHT,
                                                     WIDTH, 0.9607843137254902 * HEIGHT,
                                                     false, CycleMethod.NO_CYCLE,
                                                     new Stop(0.0, Color.color(0.3686274510, 0.3725490196, 0.3803921569, 1)),
                                                     new Stop(0.13, Color.color(0.2, 0.2, 0.2, 1)),
                                                     new Stop(0.15, Color.color(0.9254901961, 0.9254901961, 0.9333333333, 1)),
                                                     new Stop(0.4, Color.color(0.6156862745, 0.6196078431, 0.6274509804, 1)),
                                                     new Stop(0.44, Color.BLACK),
                                                     new Stop(0.78, Color.color(0.0862745098, 0.0784313725, 0.0901960784, 1)),
                                                     new Stop(0.89, Color.color(0.9294117647, 0.9294117647, 0.9294117647, 1)),
                                                     new Stop(0.92, Color.color(0.0980392157, 0.0901960784, 0.1058823529, 1)),
                                                     new Stop(0.97, Color.BLACK),
                                                     new Stop(1.0, Color.color(0.3803921569, 0.3921568627, 0.4117647059, 1)));
        BOTTOM.setFill(BOTTOM_FILL);
        BOTTOM.setStroke(null);

        final InnerShadow BOTTOM_INNER_SHADOW0 = new InnerShadow();
        BOTTOM_INNER_SHADOW0.setWidth(0.15 * BOTTOM.getLayoutBounds().getWidth());
        BOTTOM_INNER_SHADOW0.setHeight(0.15 * BOTTOM.getLayoutBounds().getHeight());
        BOTTOM_INNER_SHADOW0.setOffsetX(0.0);
        BOTTOM_INNER_SHADOW0.setOffsetY(0.0);
        BOTTOM_INNER_SHADOW0.setRadius(0.15 * BOTTOM.getLayoutBounds().getWidth());
        BOTTOM_INNER_SHADOW0.setColor(Color.color(1, 1, 1, 0.6470588235));
        BOTTOM_INNER_SHADOW0.setBlurType(BlurType.GAUSSIAN);
        BOTTOM_INNER_SHADOW0.inputProperty().set(null);
        BOTTOM.setEffect(BOTTOM_INNER_SHADOW0);

        final Path HEAD = new Path();
        HEAD.setFillRule(FillRule.EVEN_ODD);
        HEAD.getElements().add(new MoveTo(0.0, 0.07058823529411765 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.0, 0.06274509803921569 * HEIGHT,
                                                0.008333333333333333 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                0.025 * WIDTH, 0.054901960784313725 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.025 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                0.19166666666666668 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                0.5 * WIDTH, 0.058823529411764705 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.8 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                0.975 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                0.975 * WIDTH, 0.054901960784313725 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.9916666666666667 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                WIDTH, 0.06274509803921569 * HEIGHT,
                                                WIDTH, 0.07058823529411765 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(WIDTH, 0.07058823529411765 * HEIGHT,
                                                WIDTH, 0.09803921568627451 * HEIGHT,
                                                WIDTH, 0.09803921568627451 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(WIDTH, 0.10588235294117647 * HEIGHT,
                                                0.9916666666666667 * WIDTH, 0.11372549019607843 * HEIGHT,
                                                0.975 * WIDTH, 0.11372549019607843 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.975 * WIDTH, 0.11372549019607843 * HEIGHT,
                                                0.8833333333333333 * WIDTH, 0.11764705882352941 * HEIGHT,
                                                0.5 * WIDTH, 0.11764705882352941 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.125 * WIDTH, 0.12156862745098039 * HEIGHT,
                                                0.025 * WIDTH, 0.11372549019607843 * HEIGHT,
                                                0.025 * WIDTH, 0.11372549019607843 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.008333333333333333 * WIDTH, 0.11372549019607843 * HEIGHT,
                                                0.0, 0.10588235294117647 * HEIGHT,
                                                0.0, 0.09803921568627451 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.0, 0.09803921568627451 * HEIGHT,
                                                0.0, 0.07058823529411765 * HEIGHT,
                                                0.0, 0.07058823529411765 * HEIGHT));
        HEAD.getElements().add(new ClosePath());
        final Paint HEAD_FILL = new LinearGradient(0.0, 0.08235294117647059 * HEIGHT,
                                                   WIDTH, 0.08235294117647059 * HEIGHT,
                                                   false, CycleMethod.NO_CYCLE,
                                                   new Stop(0.0, Color.color(0.3686274510, 0.3725490196, 0.3803921569, 1)),
                                                   new Stop(0.13, Color.color(0.2, 0.2, 0.2, 1)),
                                                   new Stop(0.15, Color.color(0.9254901961, 0.9254901961, 0.9333333333, 1)),
                                                   new Stop(0.4, Color.color(0.6156862745, 0.6196078431, 0.6274509804, 1)),
                                                   new Stop(0.44, Color.BLACK),
                                                   new Stop(0.78, Color.color(0.0862745098, 0.0784313725, 0.0901960784, 1)),
                                                   new Stop(0.89, Color.color(0.9294117647, 0.9294117647, 0.9294117647, 1)),
                                                   new Stop(0.92, Color.color(0.0980392157, 0.0901960784, 0.1058823529, 1)),
                                                   new Stop(0.97, Color.BLACK),
                                                   new Stop(1.0, Color.color(0.3803921569, 0.3921568627, 0.4117647059, 1)));
        HEAD.setFill(HEAD_FILL);
        HEAD.setStroke(null);

        final InnerShadow HEAD_INNER_SHADOW0 = new InnerShadow();
        HEAD_INNER_SHADOW0.setWidth(0.15 * HEAD.getLayoutBounds().getWidth());
        HEAD_INNER_SHADOW0.setHeight(0.15 * HEAD.getLayoutBounds().getHeight());
        HEAD_INNER_SHADOW0.setOffsetX(0.0);
        HEAD_INNER_SHADOW0.setOffsetY(0.0);
        HEAD_INNER_SHADOW0.setRadius(0.15 * HEAD.getLayoutBounds().getWidth());
        HEAD_INNER_SHADOW0.setColor(Color.color(1, 1, 1, 0.6470588235));
        HEAD_INNER_SHADOW0.setBlurType(BlurType.GAUSSIAN);
        HEAD_INNER_SHADOW0.inputProperty().set(null);
        HEAD.setEffect(HEAD_INNER_SHADOW0);

        final Path HEAD_HIGHLIGHT = new Path();
        HEAD_HIGHLIGHT.setFillRule(FillRule.EVEN_ODD);
        HEAD_HIGHLIGHT.getElements().add(new MoveTo(0.25 * WIDTH, 0.058823529411764705 * HEIGHT));
        HEAD_HIGHLIGHT.getElements().add(new CubicCurveTo(0.25 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                          0.025 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                          0.025 * WIDTH, 0.054901960784313725 * HEIGHT));
        HEAD_HIGHLIGHT.getElements().add(new CubicCurveTo(0.041666666666666664 * WIDTH, 0.07058823529411765 * HEIGHT,
                                                          0.09166666666666666 * WIDTH, 0.08235294117647059 * HEIGHT,
                                                          0.14166666666666666 * WIDTH, 0.08235294117647059 * HEIGHT));
        HEAD_HIGHLIGHT.getElements().add(new CubicCurveTo(0.19166666666666668 * WIDTH, 0.08235294117647059 * HEIGHT,
                                                          0.24166666666666667 * WIDTH, 0.07450980392156863 * HEIGHT,
                                                          0.25 * WIDTH, 0.058823529411764705 * HEIGHT));
        HEAD_HIGHLIGHT.getElements().add(new ClosePath());
        final Paint HEADHIGHLIGHT_FILL = new RadialGradient(0, 0,
                                                            0.14166666666666666 * WIDTH, 0.047058823529411764 * HEIGHT,
                                                            0.10416666666666667 * WIDTH,
                                                            false, CycleMethod.NO_CYCLE,
                                                            new Stop(0.0, Color.WHITE),
                                                            new Stop(1.0, Color.color(1, 1, 1, 0)));
        HEAD_HIGHLIGHT.setFill(HEADHIGHLIGHT_FILL);
        HEAD_HIGHLIGHT.setStroke(null);

        final Path CONNECTOR_FRAME = new Path();
        CONNECTOR_FRAME.setFillRule(FillRule.EVEN_ODD);
        CONNECTOR_FRAME.getElements().add(new MoveTo(0.24166666666666667 * WIDTH, 0.011764705882352941 * HEIGHT));
        CONNECTOR_FRAME.getElements().add(new CubicCurveTo(0.24166666666666667 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                           0.25 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                           0.25833333333333336 * WIDTH, 0.00392156862745098 * HEIGHT));
        CONNECTOR_FRAME.getElements().add(new CubicCurveTo(0.25833333333333336 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                           0.31666666666666665 * WIDTH, 0.0,
                                                           0.31666666666666665 * WIDTH, 0.0));
        CONNECTOR_FRAME.getElements().add(new LineTo(0.65 * WIDTH, 0.0));
        CONNECTOR_FRAME.getElements().add(new CubicCurveTo(0.65 * WIDTH, 0.0,
                                                           0.725 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                           0.725 * WIDTH, 0.00392156862745098 * HEIGHT));
        CONNECTOR_FRAME.getElements().add(new CubicCurveTo(0.725 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                           0.7333333333333333 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                           0.7333333333333333 * WIDTH, 0.011764705882352941 * HEIGHT));
        CONNECTOR_FRAME.getElements().add(new CubicCurveTo(0.7333333333333333 * WIDTH, 0.011764705882352941 * HEIGHT,
                                                           0.7333333333333333 * WIDTH, 0.050980392156862744 * HEIGHT,
                                                           0.7333333333333333 * WIDTH, 0.050980392156862744 * HEIGHT));
        CONNECTOR_FRAME.getElements().add(new CubicCurveTo(0.7333333333333333 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                           0.725 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                           0.725 * WIDTH, 0.058823529411764705 * HEIGHT));
        CONNECTOR_FRAME.getElements().add(new CubicCurveTo(0.725 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                           0.25833333333333336 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                           0.25833333333333336 * WIDTH, 0.058823529411764705 * HEIGHT));
        CONNECTOR_FRAME.getElements().add(new CubicCurveTo(0.25 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                           0.24166666666666667 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                           0.24166666666666667 * WIDTH, 0.050980392156862744 * HEIGHT));
        CONNECTOR_FRAME.getElements().add(new CubicCurveTo(0.24166666666666667 * WIDTH, 0.050980392156862744 * HEIGHT,
                                                           0.24166666666666667 * WIDTH, 0.011764705882352941 * HEIGHT,
                                                           0.24166666666666667 * WIDTH, 0.011764705882352941 * HEIGHT));
        CONNECTOR_FRAME.getElements().add(new ClosePath());
        final Paint CONNECTOR_FRAME_FILL = new LinearGradient(0.24166666666666667 * WIDTH, 0.03137254901960784 * HEIGHT,
                                                              0.725 * WIDTH, 0.03137254901960784 * HEIGHT,
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, Color.color(0.8784313725, 0.8588235294, 0.8666666667, 1)),
                                                              new Stop(1.0, Color.color(0.1647058824, 0.1450980392, 0.1921568627, 1)));
        CONNECTOR_FRAME.setFill(CONNECTOR_FRAME_FILL);
        CONNECTOR_FRAME.setStroke(null);

        final Path CONNECTOR_MAIN = new Path();
        CONNECTOR_MAIN.setFillRule(FillRule.EVEN_ODD);
        CONNECTOR_MAIN.getElements().add(new MoveTo(0.25 * WIDTH, 0.01568627450980392 * HEIGHT));
        CONNECTOR_MAIN.getElements().add(new CubicCurveTo(0.25 * WIDTH, 0.011764705882352941 * HEIGHT,
                                                          0.25833333333333336 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                          0.26666666666666666 * WIDTH, 0.00784313725490196 * HEIGHT));
        CONNECTOR_MAIN.getElements().add(new CubicCurveTo(0.26666666666666666 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                          0.30833333333333335 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                          0.30833333333333335 * WIDTH, 0.00392156862745098 * HEIGHT));
        CONNECTOR_MAIN.getElements().add(new LineTo(0.6666666666666666 * WIDTH, 0.00392156862745098 * HEIGHT));
        CONNECTOR_MAIN.getElements().add(new CubicCurveTo(0.6666666666666666 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                          0.7166666666666667 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                          0.7166666666666667 * WIDTH, 0.00784313725490196 * HEIGHT));
        CONNECTOR_MAIN.getElements().add(new CubicCurveTo(0.7166666666666667 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                          0.725 * WIDTH, 0.011764705882352941 * HEIGHT,
                                                          0.725 * WIDTH, 0.01568627450980392 * HEIGHT));
        CONNECTOR_MAIN.getElements().add(new CubicCurveTo(0.725 * WIDTH, 0.01568627450980392 * HEIGHT,
                                                          0.725 * WIDTH, 0.047058823529411764 * HEIGHT,
                                                          0.725 * WIDTH, 0.047058823529411764 * HEIGHT));
        CONNECTOR_MAIN.getElements().add(new CubicCurveTo(0.725 * WIDTH, 0.050980392156862744 * HEIGHT,
                                                          0.7166666666666667 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                          0.7166666666666667 * WIDTH, 0.054901960784313725 * HEIGHT));
        CONNECTOR_MAIN.getElements().add(new CubicCurveTo(0.7166666666666667 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                          0.26666666666666666 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                          0.26666666666666666 * WIDTH, 0.054901960784313725 * HEIGHT));
        CONNECTOR_MAIN.getElements().add(new CubicCurveTo(0.25833333333333336 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                          0.25 * WIDTH, 0.050980392156862744 * HEIGHT,
                                                          0.25 * WIDTH, 0.047058823529411764 * HEIGHT));
        CONNECTOR_MAIN.getElements().add(new CubicCurveTo(0.25 * WIDTH, 0.047058823529411764 * HEIGHT,
                                                          0.25 * WIDTH, 0.01568627450980392 * HEIGHT,
                                                          0.25 * WIDTH, 0.01568627450980392 * HEIGHT));
        CONNECTOR_MAIN.getElements().add(new ClosePath());
        final Paint CONNECTOR_MAIN_FILL = new LinearGradient(0.25 * WIDTH, 0.027450980392156862 * HEIGHT,
                                                             0.7166666666666667 * WIDTH, 0.027450980392156862 * HEIGHT,
                                                             false, CycleMethod.NO_CYCLE,
                                                             new Stop(0.0, Color.color(0.8156862745, 0.8196078431, 0.8470588235, 1)),
                                                             new Stop(0.11, Color.color(0.2, 0.2, 0.2, 1)),
                                                             new Stop(0.13, Color.color(0.9254901961, 0.9254901961, 0.9333333333, 1)),
                                                             new Stop(0.38, Color.color(0.6156862745, 0.6196078431, 0.6274509804, 1)),
                                                             new Stop(0.45, Color.BLACK),
                                                             new Stop(0.78, Color.color(0.0862745098, 0.0784313725, 0.0901960784, 1)),
                                                             new Stop(0.92, Color.color(0.9294117647, 0.9294117647, 0.9294117647, 1)),
                                                             new Stop(0.95, Color.color(0.0980392157, 0.0901960784, 0.1058823529, 1)),
                                                             new Stop(0.98, Color.BLACK),
                                                             new Stop(1.0, Color.color(0.3803921569, 0.3921568627, 0.4117647059, 1)));
        CONNECTOR_MAIN.setFill(CONNECTOR_MAIN_FILL);
        CONNECTOR_MAIN.setStroke(null);

        final Path CONNECTOR_HIGHLIGHT = new Path();
        CONNECTOR_HIGHLIGHT.setFillRule(FillRule.EVEN_ODD);
        CONNECTOR_HIGHLIGHT.getElements().add(new MoveTo(0.49166666666666664 * WIDTH, 0.0));
        CONNECTOR_HIGHLIGHT.getElements().add(new CubicCurveTo(0.49166666666666664 * WIDTH, 0.0,
                                                               0.325 * WIDTH, 0.0,
                                                               0.325 * WIDTH, 0.0));
        CONNECTOR_HIGHLIGHT.getElements().add(new CubicCurveTo(0.3416666666666667 * WIDTH, 0.01568627450980392 * HEIGHT,
                                                               0.36666666666666664 * WIDTH, 0.03529411764705882 * HEIGHT,
                                                               0.4083333333333333 * WIDTH, 0.03529411764705882 * HEIGHT));
        CONNECTOR_HIGHLIGHT.getElements().add(new CubicCurveTo(0.4583333333333333 * WIDTH, 0.03529411764705882 * HEIGHT,
                                                               0.48333333333333334 * WIDTH, 0.01568627450980392 * HEIGHT,
                                                               0.49166666666666664 * WIDTH, 0.0));
        CONNECTOR_HIGHLIGHT.getElements().add(new ClosePath());
        final Paint CONNECTOR_HIGHLIGHT_FILL = new RadialGradient(0, 0,
                                                                  0.4083333333333333 * WIDTH, 0.0,
                                                                  0.07083333333333333 * WIDTH,
                                                                  false, CycleMethod.NO_CYCLE,
                                                                  new Stop(0.0, Color.WHITE),
                                                                  new Stop(1.0, Color.color(1, 1, 1, 0)));
        CONNECTOR_HIGHLIGHT.setFill(CONNECTOR_HIGHLIGHT_FILL);
        CONNECTOR_HIGHLIGHT.setStroke(null);

        foreground.getChildren().addAll(MAIN_REFLECTION,
                                        TOP_REFLECTION,
                                        BOTTOM,
                                        HEAD,
                                        HEAD_HIGHLIGHT,
                                        CONNECTOR_FRAME,
                                        CONNECTOR_MAIN,
                                        CONNECTOR_HIGHLIGHT);
    }
}
