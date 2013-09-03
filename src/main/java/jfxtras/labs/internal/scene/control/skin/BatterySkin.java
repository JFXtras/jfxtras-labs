/**
 * BatterySkin.java
 *
 * Copyright (c) 2011-2013, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the <organization> nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package jfxtras.labs.internal.scene.control.skin;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.application.Platform;
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
        lookup            = new GradientLookup(control.getLevelColors());
        currentLevelColor = Color.RED;

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(120, 255);
        }

        if (control.getMinWidth() < 0 | control.getMinHeight() < 0) {
            control.setMinSize(30, 85);
        }

        if (control.getMaxWidth() < 0 | control.getMaxHeight() < 0) {
            control.setMaxSize(1200, 2550);
        }

        // Register listeners
        registerChangeListener(control.prefWidthProperty(), "PREF_WIDTH");
        registerChangeListener(control.prefHeightProperty(), "PREF_HEIGHT");
        registerChangeListener(control.chargingProperty(), "CHARGING");
        registerChangeListener(control.chargeIndicatorProperty(), "CHARGE_INDICATOR");
        registerChangeListener(control.chargingLevelProperty(), "CHARGE_LEVEL");
        registerChangeListener(control.levelColorsProperty(), "LEVEL_COLORS");

        initialized = true;
        repaint();
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("CHARGING".equals(PROPERTY)) {
            plug.setVisible(control.isCharging());
            flashFrame.setVisible(control.isCharging());
            flashMain.setVisible(control.isCharging());
        } else if("CHARGE_INDICATOR".equals(PROPERTY)) {
            if (control.getChargeIndicator() == Battery.ChargeIndicator.PLUG) {
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
        } else if("LEVEL_COLORS".equals(PROPERTY)) {
            lookup = new GradientLookup(control.getLevelColors());
            updateFluid();
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
            drawBackground();
            drawMain();
            drawForeground();

            getChildren().setAll(background,
                                 main,
                                 foreground);
        }
        isDirty = false;

        super.layoutChildren();
    }

    @Override public final Battery getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 255;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 255;
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

        final Path EMPTY_BOTTOM = new Path();
        EMPTY_BOTTOM.setFillRule(FillRule.EVEN_ODD);
        EMPTY_BOTTOM.getElements().add(new MoveTo(0.27450980392156865 * WIDTH, 0.9372549019607843 * HEIGHT));
        EMPTY_BOTTOM.getElements().add(new CubicCurveTo(0.27450980392156865 * WIDTH, 0.9294117647058824 * HEIGHT,
                                                        0.3764705882352941 * WIDTH, 0.9215686274509803 * HEIGHT,
                                                        0.5019607843137255 * WIDTH, 0.9215686274509803 * HEIGHT));
        EMPTY_BOTTOM.getElements().add(new CubicCurveTo(0.6274509803921569 * WIDTH, 0.9215686274509803 * HEIGHT,
                                                        0.7294117647058823 * WIDTH, 0.9294117647058824 * HEIGHT,
                                                        0.7294117647058823 * WIDTH, 0.9372549019607843 * HEIGHT));
        EMPTY_BOTTOM.getElements().add(new CubicCurveTo(0.7294117647058823 * WIDTH, 0.9450980392156862 * HEIGHT,
                                                        0.6274509803921569 * WIDTH, 0.9529411764705882 * HEIGHT,
                                                        0.5019607843137255 * WIDTH, 0.9529411764705882 * HEIGHT));
        EMPTY_BOTTOM.getElements().add(new CubicCurveTo(0.3764705882352941 * WIDTH, 0.9529411764705882 * HEIGHT,
                                                        0.27450980392156865 * WIDTH, 0.9450980392156862 * HEIGHT,
                                                        0.27450980392156865 * WIDTH, 0.9372549019607843 * HEIGHT));
        EMPTY_BOTTOM.getElements().add(new ClosePath());

        final Paint EMPTY_BOTTOM_FILL = Color.color(0.2, 0.2, 0.2, 1);
        EMPTY_BOTTOM.setFill(EMPTY_BOTTOM_FILL);
        EMPTY_BOTTOM.setStroke(null);

        final Rectangle EMPTY_BACKGROUND = new Rectangle(0.2745098039 * WIDTH, 0.1137254902 * HEIGHT,
                                                         0.4549019608 * WIDTH, 0.8235294118 * HEIGHT);

        final Paint EMPTY_BACKGROUND_FILL = new LinearGradient(0.2745098039 * WIDTH, 0,
                                                               0.2745098039 * WIDTH + 0.4549019608 * WIDTH, 0,
                                                               false, CycleMethod.NO_CYCLE,
                                                               new Stop(0.0, Color.color(0.2, 0.2, 0.2, 0.15)),
                                                               new Stop(0.23, Color.color(0.4, 0.4, 0.4, 0.13)),
                                                               new Stop(0.48, Color.color(0.9764705882, 0.9764705882, 0.9764705882, 0.1)),
                                                               new Stop(0.49, Color.color(1, 1, 1, 0.1)),
                                                               new Stop(0.81, Color.color(0.6, 0.6, 0.6, 0.13)),
                                                               new Stop(1.0, Color.color(0.2, 0.2, 0.2, 0.15)));
        EMPTY_BACKGROUND.setFill(EMPTY_BACKGROUND_FILL);
        EMPTY_BACKGROUND.setStroke(null);

        background.getChildren().addAll(EMPTY_BOTTOM, EMPTY_BACKGROUND);
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

        fluid = new Rectangle(0.2745098039 * WIDTH, 0.1137254902 * HEIGHT,
                              0.4549019608 * WIDTH, 0.8235294118 * HEIGHT);

        final Paint FLUID_FILL = new LinearGradient(0.2745098039 * WIDTH, 0,
                                                    0.2745098039 * WIDTH + 0.4549019608 * WIDTH, 0,
                                                    false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, Color.color(0.1411764706, 0.2666666667, 0.1372549020, 1)),
                                                    new Stop(0.23, Color.color(0.1647058824, 0.5450980392, 0, 1)),
                                                    new Stop(0.49, Color.color(0.4666666667, 0.8588235294, 0, 1)),
                                                    new Stop(0.81, Color.color(0.1647058824, 0.5450980392, 0, 1)),
                                                    new Stop(1.0, Color.color(0.1411764706, 0.2666666667, 0.1372549020, 1)));
        fluid.setFill(FLUID_FILL);
        fluid.setStroke(null);
        if (Double.compare(control.getChargingLevel(), 0.0) == 0) {
            fluid.setVisible(false);
        }

        fluidHighlight = new Rectangle(0.2745098039 * WIDTH, 0.1137254902 * HEIGHT,
                                       0.4549019608 * WIDTH, 0.0078431373 * HEIGHT);

        final Paint FLUID_HIGHLIGHT_FILL = new LinearGradient(0.2745098039 * WIDTH, 0,
                                                             0.2745098039 * WIDTH + 0.4549019608 * WIDTH, 0,
                                                             false, CycleMethod.NO_CYCLE,
                                                             new Stop(0.0, Color.color(1, 1, 1, 0.4980392157)),
                                                             new Stop(0.35, Color.color(1, 1, 1, 0.7764705882)),
                                                             new Stop(0.63, Color.color(1, 1, 1, 0.7843137255)),
                                                             new Stop(1.0, Color.color(1, 1, 1, 0.4980392157)));
        fluidHighlight.setFill(FLUID_HIGHLIGHT_FILL);
        fluidHighlight.setStroke(null);

        plug = new Path();
        plug.setFillRule(FillRule.EVEN_ODD);
        plug.getElements().add(new MoveTo(0.48627450980392156 * WIDTH, 0.4196078431372549 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.48627450980392156 * WIDTH, 0.4196078431372549 * HEIGHT,
                                                0.48627450980392156 * WIDTH, 0.34509803921568627 * HEIGHT,
                                                0.48627450980392156 * WIDTH, 0.34509803921568627 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.48627450980392156 * WIDTH, 0.3411764705882353 * HEIGHT,
                                                0.47843137254901963 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.4745098039215686 * WIDTH, 0.33725490196078434 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.4745098039215686 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.47058823529411764 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.47058823529411764 * WIDTH, 0.33725490196078434 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.4627450980392157 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.4588235294117647 * WIDTH, 0.3411764705882353 * HEIGHT,
                                                0.4588235294117647 * WIDTH, 0.34901960784313724 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.4588235294117647 * WIDTH, 0.34901960784313724 * HEIGHT,
                                                0.4588235294117647 * WIDTH, 0.4196078431372549 * HEIGHT,
                                                0.4588235294117647 * WIDTH, 0.4196078431372549 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.43137254901960786 * WIDTH, 0.4196078431372549 * HEIGHT,
                                                0.41568627450980394 * WIDTH, 0.4235294117647059 * HEIGHT,
                                                0.41568627450980394 * WIDTH, 0.4235294117647059 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.41568627450980394 * WIDTH, 0.5137254901960784 * HEIGHT,
                                                0.44313725490196076 * WIDTH, 0.5686274509803921 * HEIGHT,
                                                0.48627450980392156 * WIDTH, 0.5803921568627451 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.48627450980392156 * WIDTH, 0.5803921568627451 * HEIGHT,
                                                0.48627450980392156 * WIDTH, 0.6588235294117647 * HEIGHT,
                                                0.48627450980392156 * WIDTH, 0.6588235294117647 * HEIGHT));
        plug.getElements().add(new LineTo(0.5294117647058824 * WIDTH, 0.6588235294117647 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5294117647058824 * WIDTH, 0.6588235294117647 * HEIGHT,
                                                0.5294117647058824 * WIDTH, 0.5843137254901961 * HEIGHT,
                                                0.5294117647058824 * WIDTH, 0.5843137254901961 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5725490196078431 * WIDTH, 0.5725490196078431 * HEIGHT,
                                                0.6039215686274509 * WIDTH, 0.5294117647058824 * HEIGHT,
                                                0.6039215686274509 * WIDTH, 0.4235294117647059 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.6039215686274509 * WIDTH, 0.4235294117647059 * HEIGHT,
                                                0.5882352941176471 * WIDTH, 0.4235294117647059 * HEIGHT,
                                                0.5607843137254902 * WIDTH, 0.4196078431372549 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5607843137254902 * WIDTH, 0.4196078431372549 * HEIGHT,
                                                0.5607843137254902 * WIDTH, 0.34901960784313724 * HEIGHT,
                                                0.5607843137254902 * WIDTH, 0.34901960784313724 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5607843137254902 * WIDTH, 0.3411764705882353 * HEIGHT,
                                                0.5568627450980392 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.5490196078431373 * WIDTH, 0.33725490196078434 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5490196078431373 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.5411764705882353 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.5411764705882353 * WIDTH, 0.33725490196078434 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5333333333333333 * WIDTH, 0.33725490196078434 * HEIGHT,
                                                0.5294117647058824 * WIDTH, 0.3411764705882353 * HEIGHT,
                                                0.5294117647058824 * WIDTH, 0.34901960784313724 * HEIGHT));
        plug.getElements().add(new CubicCurveTo(0.5294117647058824 * WIDTH, 0.34901960784313724 * HEIGHT,
                                                0.5294117647058824 * WIDTH, 0.4196078431372549 * HEIGHT,
                                                0.5294117647058824 * WIDTH, 0.4196078431372549 * HEIGHT));
        plug.getElements().add(new LineTo(0.48627450980392156 * WIDTH, 0.4196078431372549 * HEIGHT));
        plug.getElements().add(new ClosePath());
        plug.setFill(Color.rgb(75, 75, 75));
        plug.setStroke(null);

        final InnerShadow PLUG_INNER_SHADOW = new InnerShadow();
        PLUG_INNER_SHADOW.setWidth(0.01411764705882353 * plug.getLayoutBounds().getWidth());
        PLUG_INNER_SHADOW.setHeight(0.01411764705882353 * plug.getLayoutBounds().getHeight());
        PLUG_INNER_SHADOW.setOffsetX(0.0);
        PLUG_INNER_SHADOW.setOffsetY(-0.0);
        PLUG_INNER_SHADOW.setRadius(0.01411764705882353 * plug.getLayoutBounds().getWidth());
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
        flashFrame.getElements().add(new MoveTo(0.3568627450980392 * WIDTH, 0.37254901960784315 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.5372549019607843 * WIDTH, 0.6274509803921569 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.5372549019607843 * WIDTH, 0.5254901960784314 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.6392156862745098 * WIDTH, 0.615686274509804 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.49411764705882355 * WIDTH, 0.3843137254901961 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.49411764705882355 * WIDTH, 0.5019607843137255 * HEIGHT));
        flashFrame.getElements().add(new LineTo(0.3568627450980392 * WIDTH, 0.37254901960784315 * HEIGHT));
        flashFrame.getElements().add(new ClosePath());
        flashFrame.setFill(Color.WHITE);
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
        flashMain.getElements().add(new MoveTo(0.3843137254901961 * WIDTH, 0.403921568627451 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.5333333333333333 * WIDTH, 0.611764705882353 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.5333333333333333 * WIDTH, 0.5137254901960784 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.6196078431372549 * WIDTH, 0.592156862745098 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.4980392156862745 * WIDTH, 0.4 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.4980392156862745 * WIDTH, 0.5137254901960784 * HEIGHT));
        flashMain.getElements().add(new LineTo(0.3843137254901961 * WIDTH, 0.403921568627451 * HEIGHT));
        flashMain.getElements().add(new ClosePath());
        flashMain.setFill(Color.color(0.9960784314, 0.9215686275, 0, 1));
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
        FLASH_MAIN_INNER_SHADOW.setWidth(0.08470588235294117 * flashMain.getLayoutBounds().getWidth());
        FLASH_MAIN_INNER_SHADOW.setHeight(0.08470588235294117 * flashMain.getLayoutBounds().getHeight());
        FLASH_MAIN_INNER_SHADOW.setOffsetX(0.0);
        FLASH_MAIN_INNER_SHADOW.setOffsetY(0.0);
        FLASH_MAIN_INNER_SHADOW.setRadius(0.08470588235294117 * flashMain.getLayoutBounds().getWidth());
        FLASH_MAIN_INNER_SHADOW.setColor(Color.color(0.8509803922, 0.5294117647, 0, 1));
        FLASH_MAIN_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        flashMain.setEffect(FLASH_MAIN_INNER_SHADOW);

        main.getChildren().addAll(fluid,
                                  fluidHighlight,
                                  plug,
                                  flashFrame,
                                  flashMain);
    }

    private final void updateFluid() {
        Platform.runLater(new Runnable() {
            @Override public void run() {
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
                                                 new Stop(0.0, Color.hsb(currentLevelColor.getHue(), currentLevelColor.getSaturation(), 0.1)),
                                                 new Stop(0.2, Color.hsb(currentLevelColor.getHue(), currentLevelColor.getSaturation(), 0.4)),
                                                 new Stop(0.5, currentLevelColor),
                                                 new Stop(0.8, Color.hsb(currentLevelColor.getHue(), currentLevelColor.getSaturation(), 0.4)),
                                                 new Stop(1.0, Color.hsb(currentLevelColor.getHue(), currentLevelColor.getSaturation(), 0.1))));
                fluidHighlight.setY(0.1137254902 * control.getPrefHeight() + (0.8235294118 * control.getPrefHeight() - fluid.getHeight()));
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

        final Rectangle MAIN_REFLECTION = new Rectangle(0.2705882353 * WIDTH, 0.1137254902 * HEIGHT,
                                                        0.462745098 * WIDTH, 0.8235294118 * HEIGHT);
        //MAIN_REFLECTION.getStyleClass().add("foreground-mainreflection");
        final Paint MAIN_REFLECTION_FILL = new LinearGradient(0.2705882353 * WIDTH, 0,
                                                              0.2705882353 * WIDTH + 0.462745098 * WIDTH, 0,
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
        TOP_REFLECTION.getElements().add(new MoveTo(0.27058823529411763 * WIDTH, 0.11372549019607843 * HEIGHT));
        TOP_REFLECTION.getElements().add(new LineTo(0.7333333333333333 * WIDTH, 0.11372549019607843 * HEIGHT));
        TOP_REFLECTION.getElements().add(new LineTo(0.7333333333333333 * WIDTH, 0.3176470588235294 * HEIGHT));
        TOP_REFLECTION.getElements().add(new CubicCurveTo(0.7333333333333333 * WIDTH, 0.3176470588235294 * HEIGHT,
                                                          0.6705882352941176 * WIDTH, 0.3411764705882353 * HEIGHT,
                                                          0.5843137254901961 * WIDTH, 0.41568627450980394 * HEIGHT));
        TOP_REFLECTION.getElements().add(new CubicCurveTo(0.4980392156862745 * WIDTH, 0.47843137254901963 * HEIGHT,
                                                          0.396078431372549 * WIDTH, 0.48627450980392156 * HEIGHT,
                                                          0.396078431372549 * WIDTH, 0.48627450980392156 * HEIGHT));
        TOP_REFLECTION.getElements().add(new CubicCurveTo(0.34509803921568627 * WIDTH, 0.48627450980392156 * HEIGHT,
                                                          0.27058823529411763 * WIDTH, 0.47058823529411764 * HEIGHT,
                                                          0.27058823529411763 * WIDTH, 0.47058823529411764 * HEIGHT));
        TOP_REFLECTION.getElements().add(new LineTo(0.27058823529411763 * WIDTH, 0.11372549019607843 * HEIGHT));
        TOP_REFLECTION.getElements().add(new ClosePath());
        //TOP_REFLECTION.getStyleClass().add("foreground-topreflection");
        final Paint TOP_REFLECTION_FILL = new LinearGradient(0.2705882353 * WIDTH, 0,
                                                             0.2705882353 * WIDTH + 0.462745098 * WIDTH, 0,
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
        BOTTOM.getElements().add(new MoveTo(0.26666666666666666 * WIDTH, 0.9490196078431372 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.26666666666666666 * WIDTH, 0.9411764705882353 * HEIGHT,
                                                  0.27058823529411763 * WIDTH, 0.9333333333333333 * HEIGHT,
                                                  0.2784313725490196 * WIDTH, 0.9333333333333333 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.2784313725490196 * WIDTH, 0.9333333333333333 * HEIGHT,
                                                  0.3568627450980392 * WIDTH, 0.9372549019607843 * HEIGHT,
                                                  0.5019607843137255 * WIDTH, 0.9372549019607843 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.6431372549019608 * WIDTH, 0.9372549019607843 * HEIGHT,
                                                  0.7254901960784313 * WIDTH, 0.9333333333333333 * HEIGHT,
                                                  0.7254901960784313 * WIDTH, 0.9333333333333333 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.7333333333333333 * WIDTH, 0.9333333333333333 * HEIGHT,
                                                  0.7372549019607844 * WIDTH, 0.9411764705882353 * HEIGHT,
                                                  0.7372549019607844 * WIDTH, 0.9490196078431372 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.7372549019607844 * WIDTH, 0.9490196078431372 * HEIGHT,
                                                  0.7372549019607844 * WIDTH, 0.9764705882352941 * HEIGHT,
                                                  0.7372549019607844 * WIDTH, 0.9764705882352941 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.7372549019607844 * WIDTH, 0.984313725490196 * HEIGHT,
                                                  0.7333333333333333 * WIDTH, 0.9921568627450981 * HEIGHT,
                                                  0.7254901960784313 * WIDTH, 0.9921568627450981 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.7254901960784313 * WIDTH, 0.9921568627450981 * HEIGHT,
                                                  0.6823529411764706 * WIDTH, 0.996078431372549 * HEIGHT,
                                                  0.5019607843137255 * WIDTH, 0.996078431372549 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.3254901960784314 * WIDTH, 0.996078431372549 * HEIGHT,
                                                  0.2784313725490196 * WIDTH, 0.9921568627450981 * HEIGHT,
                                                  0.2784313725490196 * WIDTH, 0.9921568627450981 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.27058823529411763 * WIDTH, 0.9921568627450981 * HEIGHT,
                                                  0.26666666666666666 * WIDTH, 0.984313725490196 * HEIGHT,
                                                  0.26666666666666666 * WIDTH, 0.9764705882352941 * HEIGHT));
        BOTTOM.getElements().add(new CubicCurveTo(0.26666666666666666 * WIDTH, 0.9764705882352941 * HEIGHT,
                                                  0.26666666666666666 * WIDTH, 0.9490196078431372 * HEIGHT,
                                                  0.26666666666666666 * WIDTH, 0.9490196078431372 * HEIGHT));
        BOTTOM.getElements().add(new ClosePath());
        //BOTTOM.getStyleClass().add("foreground-bottom");
        final Paint BOTTOM_FILL = new LinearGradient(0.26666666666666666 * WIDTH, 0.9607843137254902 * HEIGHT,
                                                     0.7372549019607844 * WIDTH, 0.9607843137254902 * HEIGHT,
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
        BOTTOM_INNER_SHADOW0.setWidth(0.07058823529411765 * BOTTOM.getLayoutBounds().getWidth());
        BOTTOM_INNER_SHADOW0.setHeight(0.07058823529411765 * BOTTOM.getLayoutBounds().getHeight());
        BOTTOM_INNER_SHADOW0.setOffsetX(0.0);
        BOTTOM_INNER_SHADOW0.setOffsetY(0.0);
        BOTTOM_INNER_SHADOW0.setRadius(0.07058823529411765 * BOTTOM.getLayoutBounds().getWidth());
        BOTTOM_INNER_SHADOW0.setColor(Color.color(1, 1, 1, 0.6470588235));
        BOTTOM_INNER_SHADOW0.setBlurType(BlurType.GAUSSIAN);
        BOTTOM_INNER_SHADOW0.inputProperty().set(null);
        BOTTOM.setEffect(BOTTOM_INNER_SHADOW0);

        final Path HEAD = new Path();
        HEAD.setFillRule(FillRule.EVEN_ODD);
        HEAD.getElements().add(new MoveTo(0.26666666666666666 * WIDTH, 0.07058823529411765 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.26666666666666666 * WIDTH, 0.06274509803921569 * HEIGHT,
                                                0.27058823529411763 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                0.2784313725490196 * WIDTH, 0.054901960784313725 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.2784313725490196 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                0.3568627450980392 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                0.5019607843137255 * WIDTH, 0.058823529411764705 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.6431372549019608 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                0.7254901960784313 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                0.7254901960784313 * WIDTH, 0.054901960784313725 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.7333333333333333 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                0.7372549019607844 * WIDTH, 0.06274509803921569 * HEIGHT,
                                                0.7372549019607844 * WIDTH, 0.07058823529411765 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.7372549019607844 * WIDTH, 0.07058823529411765 * HEIGHT,
                                                0.7372549019607844 * WIDTH, 0.09803921568627451 * HEIGHT,
                                                0.7372549019607844 * WIDTH, 0.09803921568627451 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.7372549019607844 * WIDTH, 0.10588235294117647 * HEIGHT,
                                                0.7333333333333333 * WIDTH, 0.11372549019607843 * HEIGHT,
                                                0.7254901960784313 * WIDTH, 0.11372549019607843 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.7254901960784313 * WIDTH, 0.11372549019607843 * HEIGHT,
                                                0.6823529411764706 * WIDTH, 0.11764705882352941 * HEIGHT,
                                                0.5019607843137255 * WIDTH, 0.11764705882352941 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.3254901960784314 * WIDTH, 0.12156862745098039 * HEIGHT,
                                                0.2784313725490196 * WIDTH, 0.11372549019607843 * HEIGHT,
                                                0.2784313725490196 * WIDTH, 0.11372549019607843 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.27058823529411763 * WIDTH, 0.11372549019607843 * HEIGHT,
                                                0.26666666666666666 * WIDTH, 0.10588235294117647 * HEIGHT,
                                                0.26666666666666666 * WIDTH, 0.09803921568627451 * HEIGHT));
        HEAD.getElements().add(new CubicCurveTo(0.26666666666666666 * WIDTH, 0.09803921568627451 * HEIGHT,
                                                0.26666666666666666 * WIDTH, 0.07058823529411765 * HEIGHT,
                                                0.26666666666666666 * WIDTH, 0.07058823529411765 * HEIGHT));
        HEAD.getElements().add(new ClosePath());
        //HEAD.getStyleClass().add("foreground-head");
        final Paint HEAD_FILL = new LinearGradient(0.26666666666666666 * WIDTH, 0.08235294117647059 * HEIGHT,
                                                   0.7372549019607844 * WIDTH, 0.08235294117647059 * HEIGHT,
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
        HEAD_INNER_SHADOW0.setWidth(0.07058823529411765 * HEAD.getLayoutBounds().getWidth());
        HEAD_INNER_SHADOW0.setHeight(0.07058823529411765 * HEAD.getLayoutBounds().getHeight());
        HEAD_INNER_SHADOW0.setOffsetX(0.0);
        HEAD_INNER_SHADOW0.setOffsetY(0.0);
        HEAD_INNER_SHADOW0.setRadius(0.07058823529411765 * HEAD.getLayoutBounds().getWidth());
        HEAD_INNER_SHADOW0.setColor(Color.color(1, 1, 1, 0.6470588235));
        HEAD_INNER_SHADOW0.setBlurType(BlurType.GAUSSIAN);
        HEAD_INNER_SHADOW0.inputProperty().set(null);
        HEAD.setEffect(HEAD_INNER_SHADOW0);

        final Path HEADHIGHLIGHT = new Path();
        HEADHIGHLIGHT.setFillRule(FillRule.EVEN_ODD);
        HEADHIGHLIGHT.getElements().add(new MoveTo(0.3843137254901961 * WIDTH, 0.058823529411764705 * HEIGHT));
        HEADHIGHLIGHT.getElements().add(new CubicCurveTo(0.3843137254901961 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                         0.2784313725490196 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                         0.2784313725490196 * WIDTH, 0.054901960784313725 * HEIGHT));
        HEADHIGHLIGHT.getElements().add(new CubicCurveTo(0.28627450980392155 * WIDTH, 0.07058823529411765 * HEIGHT,
                                                         0.30980392156862746 * WIDTH, 0.08235294117647059 * HEIGHT,
                                                         0.3333333333333333 * WIDTH, 0.08235294117647059 * HEIGHT));
        HEADHIGHLIGHT.getElements().add(new CubicCurveTo(0.3568627450980392 * WIDTH, 0.08235294117647059 * HEIGHT,
                                                         0.3803921568627451 * WIDTH, 0.07450980392156863 * HEIGHT,
                                                         0.3843137254901961 * WIDTH, 0.058823529411764705 * HEIGHT));
        HEADHIGHLIGHT.getElements().add(new ClosePath());
        //HEADHIGHLIGHT.getStyleClass().add("foreground-headhighlight");
        final Paint HEADHIGHLIGHT_FILL = new RadialGradient(0, 0,
                                                            0.3333333333333333 * WIDTH, 0.047058823529411764 * HEIGHT,
                                                            0.049019607843137254 * WIDTH,
                                                            false, CycleMethod.NO_CYCLE,
                                                            new Stop(0.0, Color.WHITE),
                                                            new Stop(1.0, Color.color(1, 1, 1, 0)));
        HEADHIGHLIGHT.setFill(HEADHIGHLIGHT_FILL);
        HEADHIGHLIGHT.setStroke(null);

        final Path CONNTECTORFRAME = new Path();
        CONNTECTORFRAME.setFillRule(FillRule.EVEN_ODD);
        CONNTECTORFRAME.getElements().add(new MoveTo(0.3803921568627451 * WIDTH, 0.011764705882352941 * HEIGHT));
        CONNTECTORFRAME.getElements().add(new CubicCurveTo(0.3803921568627451 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                           0.3843137254901961 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                           0.38823529411764707 * WIDTH, 0.00392156862745098 * HEIGHT));
        CONNTECTORFRAME.getElements().add(new CubicCurveTo(0.38823529411764707 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                           0.41568627450980394 * WIDTH, 0.0,
                                                           0.41568627450980394 * WIDTH, 0.0));
        CONNTECTORFRAME.getElements().add(new LineTo(0.5725490196078431 * WIDTH, 0.0));
        CONNTECTORFRAME.getElements().add(new CubicCurveTo(0.5725490196078431 * WIDTH, 0.0,
                                                           0.6078431372549019 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                           0.6078431372549019 * WIDTH, 0.00392156862745098 * HEIGHT));
        CONNTECTORFRAME.getElements().add(new CubicCurveTo(0.6078431372549019 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                           0.611764705882353 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                           0.611764705882353 * WIDTH, 0.011764705882352941 * HEIGHT));
        CONNTECTORFRAME.getElements().add(new CubicCurveTo(0.611764705882353 * WIDTH, 0.011764705882352941 * HEIGHT,
                                                           0.611764705882353 * WIDTH, 0.050980392156862744 * HEIGHT,
                                                           0.611764705882353 * WIDTH, 0.050980392156862744 * HEIGHT));
        CONNTECTORFRAME.getElements().add(new CubicCurveTo(0.611764705882353 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                           0.6078431372549019 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                           0.6078431372549019 * WIDTH, 0.058823529411764705 * HEIGHT));
        CONNTECTORFRAME.getElements().add(new CubicCurveTo(0.6078431372549019 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                           0.38823529411764707 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                           0.38823529411764707 * WIDTH, 0.058823529411764705 * HEIGHT));
        CONNTECTORFRAME.getElements().add(new CubicCurveTo(0.3843137254901961 * WIDTH, 0.058823529411764705 * HEIGHT,
                                                           0.3803921568627451 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                           0.3803921568627451 * WIDTH, 0.050980392156862744 * HEIGHT));
        CONNTECTORFRAME.getElements().add(new CubicCurveTo(0.3803921568627451 * WIDTH, 0.050980392156862744 * HEIGHT,
                                                           0.3803921568627451 * WIDTH, 0.011764705882352941 * HEIGHT,
                                                           0.3803921568627451 * WIDTH, 0.011764705882352941 * HEIGHT));
        CONNTECTORFRAME.getElements().add(new ClosePath());
        //CONNTECTORFRAME.getStyleClass().add("foreground-conntectorframe");
        final Paint CONNTECTORFRAME_FILL = new LinearGradient(0.3803921568627451 * WIDTH, 0.03137254901960784 * HEIGHT,
                                                              0.6078431372549019 * WIDTH, 0.03137254901960784 * HEIGHT,
                                                              false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, Color.color(0.8784313725, 0.8588235294, 0.8666666667, 1)),
                                                              new Stop(1.0, Color.color(0.1647058824, 0.1450980392, 0.1921568627, 1)));
        CONNTECTORFRAME.setFill(CONNTECTORFRAME_FILL);
        CONNTECTORFRAME.setStroke(null);

        final Path CONNECTORMAIN = new Path();
        CONNECTORMAIN.setFillRule(FillRule.EVEN_ODD);
        CONNECTORMAIN.getElements().add(new MoveTo(0.3843137254901961 * WIDTH, 0.01568627450980392 * HEIGHT));
        CONNECTORMAIN.getElements().add(new CubicCurveTo(0.3843137254901961 * WIDTH, 0.011764705882352941 * HEIGHT,
                                                         0.38823529411764707 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                         0.39215686274509803 * WIDTH, 0.00784313725490196 * HEIGHT));
        CONNECTORMAIN.getElements().add(new CubicCurveTo(0.39215686274509803 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                         0.4117647058823529 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                         0.4117647058823529 * WIDTH, 0.00392156862745098 * HEIGHT));
        CONNECTORMAIN.getElements().add(new LineTo(0.5803921568627451 * WIDTH, 0.00392156862745098 * HEIGHT));
        CONNECTORMAIN.getElements().add(new CubicCurveTo(0.5803921568627451 * WIDTH, 0.00392156862745098 * HEIGHT,
                                                         0.6039215686274509 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                         0.6039215686274509 * WIDTH, 0.00784313725490196 * HEIGHT));
        CONNECTORMAIN.getElements().add(new CubicCurveTo(0.6039215686274509 * WIDTH, 0.00784313725490196 * HEIGHT,
                                                         0.6078431372549019 * WIDTH, 0.011764705882352941 * HEIGHT,
                                                         0.6078431372549019 * WIDTH, 0.01568627450980392 * HEIGHT));
        CONNECTORMAIN.getElements().add(new CubicCurveTo(0.6078431372549019 * WIDTH, 0.01568627450980392 * HEIGHT,
                                                         0.6078431372549019 * WIDTH, 0.047058823529411764 * HEIGHT,
                                                         0.6078431372549019 * WIDTH, 0.047058823529411764 * HEIGHT));
        CONNECTORMAIN.getElements().add(new CubicCurveTo(0.6078431372549019 * WIDTH, 0.050980392156862744 * HEIGHT,
                                                         0.6039215686274509 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                         0.6039215686274509 * WIDTH, 0.054901960784313725 * HEIGHT));
        CONNECTORMAIN.getElements().add(new CubicCurveTo(0.6039215686274509 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                         0.39215686274509803 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                         0.39215686274509803 * WIDTH, 0.054901960784313725 * HEIGHT));
        CONNECTORMAIN.getElements().add(new CubicCurveTo(0.38823529411764707 * WIDTH, 0.054901960784313725 * HEIGHT,
                                                         0.3843137254901961 * WIDTH, 0.050980392156862744 * HEIGHT,
                                                         0.3843137254901961 * WIDTH, 0.047058823529411764 * HEIGHT));
        CONNECTORMAIN.getElements().add(new CubicCurveTo(0.3843137254901961 * WIDTH, 0.047058823529411764 * HEIGHT,
                                                         0.3843137254901961 * WIDTH, 0.01568627450980392 * HEIGHT,
                                                         0.3843137254901961 * WIDTH, 0.01568627450980392 * HEIGHT));
        CONNECTORMAIN.getElements().add(new ClosePath());
        //CONNECTORMAIN.getStyleClass().add("foreground-connectormain");
        final Paint CONNECTORMAIN_FILL = new LinearGradient(0.3843137254901961 * WIDTH, 0.027450980392156862 * HEIGHT,
                                                            0.6039215686274509 * WIDTH, 0.027450980392156862 * HEIGHT,
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
        CONNECTORMAIN.setFill(CONNECTORMAIN_FILL);
        CONNECTORMAIN.setStroke(null);

        final Path CONNECTORHIGHLIGHT = new Path();
        CONNECTORHIGHLIGHT.setFillRule(FillRule.EVEN_ODD);
        CONNECTORHIGHLIGHT.getElements().add(new MoveTo(0.4980392156862745 * WIDTH, 0.0));
        CONNECTORHIGHLIGHT.getElements().add(new CubicCurveTo(0.4980392156862745 * WIDTH, 0.0,
                                                              0.4196078431372549 * WIDTH, 0.0,
                                                              0.4196078431372549 * WIDTH, 0.0));
        CONNECTORHIGHLIGHT.getElements().add(new CubicCurveTo(0.42745098039215684 * WIDTH, 0.01568627450980392 * HEIGHT,
                                                              0.4392156862745098 * WIDTH, 0.03529411764705882 * HEIGHT,
                                                              0.4588235294117647 * WIDTH, 0.03529411764705882 * HEIGHT));
        CONNECTORHIGHLIGHT.getElements().add(new CubicCurveTo(0.4823529411764706 * WIDTH, 0.03529411764705882 * HEIGHT,
                                                              0.49411764705882355 * WIDTH, 0.01568627450980392 * HEIGHT,
                                                              0.4980392156862745 * WIDTH, 0.0));
        CONNECTORHIGHLIGHT.getElements().add(new ClosePath());
        //CONNECTORHIGHLIGHT.getStyleClass().add("foreground-connectorhighlight");
        final Paint CONNECTORHIGHLIGHT_FILL = new RadialGradient(0, 0,
                                                                 0.4588235294117647 * WIDTH, 0.0,
                                                                 0.03333333333333333 * WIDTH,
                                                                 false, CycleMethod.NO_CYCLE,
                                                                 new Stop(0.0, Color.WHITE),
                                                                 new Stop(1.0, Color.color(1, 1, 1, 0)));
        CONNECTORHIGHLIGHT.setFill(CONNECTORHIGHLIGHT_FILL);
        CONNECTORHIGHLIGHT.setStroke(null);

        foreground.getChildren().addAll(MAIN_REFLECTION,
                                        TOP_REFLECTION,
                                        BOTTOM,
                                        HEAD,
                                        HEADHIGHLIGHT,
                                        CONNTECTORFRAME,
                                        CONNECTORMAIN,
                                        CONNECTORHIGHLIGHT);
        foreground.setCache(true);
    }
}
