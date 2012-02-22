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
import jfxtras.labs.scene.control.gauge.Gauge;
import jfxtras.labs.scene.control.gauge.Gauge.KnobColor;
import jfxtras.labs.scene.control.gauge.Indicator;
import jfxtras.labs.scene.control.gauge.LedColor;
import jfxtras.labs.scene.control.gauge.Section;
import jfxtras.labs.internal.scene.control.behavior.GaugeBehaviorBase;
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
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
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
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;

import java.util.ArrayList;


/**
 * Created by
 * User: hansolo
 * Date: 17.01.12
 * Time: 09:03
 */
public abstract class GaugeSkinBase<C extends Gauge, B extends GaugeBehaviorBase<C>> extends SkinBase<C, B> {
    public static final long BLINK_INTERVAL = 500000000l;


    // ******************** Constructors **************************************
    public GaugeSkinBase(final C CONTROL, final B BEHAVIOR) {
        super(CONTROL, BEHAVIOR);

        // Labels do not block the mouse by default, unlike most other UI Controls.
        //consumeMouseEvents(false);

        // Register listeners
        registerChangeListener(CONTROL.widthProperty(), "WIDTH");
        registerChangeListener(CONTROL.heightProperty(), "HEIGHT");
        registerChangeListener(CONTROL.animationDurationProperty(), "ANIMATION_DURATION");
        registerChangeListener(CONTROL.radialRangeProperty(), "RADIAL_RANGE");
        registerChangeListener(CONTROL.frameDesignProperty(), "FRAME_DESIGN");
        registerChangeListener(CONTROL.backgroundDesignProperty(), "BACKGROUND_DESIGN");
        registerChangeListener(CONTROL.knobDesignProperty(), "KNOB_DESIGN");
        registerChangeListener(CONTROL.knobColorProperty(), "KNOB_COLOR");
        registerChangeListener(CONTROL.pointerTypeProperty(), "POINTER_TYPE");
        registerChangeListener(CONTROL.valueColorProperty(), "VALUE_COLOR");
        registerChangeListener(CONTROL.foregroundTypeProperty(), "FOREGROUND_TYPE");
        registerChangeListener(CONTROL.lcdDesignProperty(), "LCD_DESIGN");
        registerChangeListener(CONTROL.lcdNumberSystemProperty(), "LCD_NUMBER_SYSTEM");
        registerChangeListener(CONTROL.userLedBlinkingProperty(), "USER_LED_BLINKING");
        registerChangeListener(CONTROL.ledBlinkingProperty(), "LED_BLINKING");
        registerChangeListener(CONTROL.glowColorProperty(), "GLOW_COLOR");
        registerChangeListener(CONTROL.glowVisibleProperty(), "GLOW_VISIBILITY");
        registerChangeListener(CONTROL.glowOnProperty(), "GLOW_ON");
        registerChangeListener(CONTROL.pulsatingGlowProperty(), "PULSATING_GLOW");
        registerChangeListener(CONTROL.rangeProperty(), "RANGE");
        registerChangeListener(CONTROL.minMeasuredValueProperty(), "MIN_MEASURED_VALUE");
        registerChangeListener(CONTROL.maxMeasuredValueProperty(), "MAX_MEASURED_VALUE");
        registerChangeListener(CONTROL.trendProperty(), "TREND");
        registerChangeListener(CONTROL.simpleGradientBaseColorProperty(), "SIMPLE_GRADIENT_BASE");
    }


     // ******************** Skin Layout **************************************
    @Override protected double computeMinWidth(final double HEIGHT) {
        return getSkinnable().prefWidth(HEIGHT);
    }

    @Override protected double computeMinHeight(final double WIDTH) {
        return getSkinnable().prefHeight(WIDTH);
    }

    @Override protected double computePrefWidth(final double HEIGHT) {
        return getSkinnable().prefWidth(HEIGHT);
    }

    @Override protected double computePrefHeight(final double WIDTH) {
        return getSkinnable().prefHeight(WIDTH);
    }

    @Override protected double computeMaxWidth(final double HEIGHT) {
        return getSkinnable().prefWidth(HEIGHT);
    }

    @Override protected double computeMaxHeight(final double WIDTH) {
        return getSkinnable().prefHeight(WIDTH);
    }

    @Override protected void layoutChildren() {
        final Insets padding = getInsets();

        final double x = padding.getLeft();
        final double y = padding.getTop();
        final double w = getWidth() - (padding.getLeft() + padding.getRight());
        final double h = getHeight() - (padding.getTop() + padding.getBottom());

        //layoutGauge(x, y, w, h);
    }


    // ******************** Drawing *******************************************
    protected void drawCircularFrame(final Gauge control, final Group FRAME, final Rectangle GAUGE_BOUNDS) {
        final double SIZE = GAUGE_BOUNDS.getWidth() <= GAUGE_BOUNDS.getHeight() ? GAUGE_BOUNDS.getWidth() : GAUGE_BOUNDS.getHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        FRAME.getChildren().clear();

        final Shape SUBTRACT = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, WIDTH * 0.4158878326);

        //final Shape OUTER_FRAME = Shape.subtract(new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.5 * WIDTH), SUBTRACT);
        final Shape OUTER_FRAME = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.5 * WIDTH);
        OUTER_FRAME.setFill(Color.color(0.5176470588, 0.5176470588, 0.5176470588, 1));
        OUTER_FRAME.setStroke(null);
        FRAME.getChildren().add(OUTER_FRAME);

        //final Shape INNER_FRAME = Shape.subtract(new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4205607476635514 * WIDTH), SUBTRACT);
        final Shape INNER_FRAME = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4205607476635514 * WIDTH);
        INNER_FRAME.setFill(Color.color(0.6, 0.6, 0.6, 0.8));
        INNER_FRAME.setStroke(null);

        //final Shape MAIN_FRAME = Shape.subtract(new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4953271028037383 * WIDTH), SUBTRACT);
        final Shape MAIN_FRAME = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4953271028037383 * WIDTH);
        MAIN_FRAME.setStroke(null);
        final ImageView IMAGE_VIEW;
        switch (control.getFrameDesign()) {
            case GLOSSY_METAL:
                MAIN_FRAME.setFill(new RadialGradient(0, 0, 0.5 * WIDTH, 0.5 * HEIGHT, 0.5 * WIDTH,
                                                      false, CycleMethod.NO_CYCLE,
                                                      new Stop(0.0, Color.color(0.8117647059, 0.8117647059, 0.8117647059, 1.0)),
                                                      new Stop(0.96, Color.color(0.8039215686, 0.8, 0.8039215686, 1.0)),
                                                      new Stop(1.0, Color.color(0.9568627451, 0.9568627451, 0.9568627451, 1.0))));
                final Shape GLOSSY2 = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4859813084 * WIDTH);
                GLOSSY2.setFill(new LinearGradient(0, GLOSSY2.getLayoutBounds().getMinY(),
                                                   0, GLOSSY2.getLayoutBounds().getMaxY(),
                                                   false, CycleMethod.NO_CYCLE,
                                                   new Stop(0.0, Color.color(0.9764705882, 0.9764705882, 0.9764705882, 1.0)),
                                                   new Stop(0.23, Color.color(0.7843137255, 0.7647058824, 0.7490196078, 1.0)),
                                                   new Stop(0.36, Color.color(1.0, 1.0, 1.0, 1.0)),
                                                   new Stop(0.59, Color.color(0.1137254902, 0.1137254902, 0.1137254902, 1.0)),
                                                   new Stop(0.76, Color.color(0.7843137255, 0.7607843137, 0.7529411765, 1.0)),
                                                   new Stop(1.0, Color.color(0.8196078431, 0.8196078431, 0.8196078431, 1.0))));
                final Shape GLOSSY3 = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4345794393 * WIDTH);
                GLOSSY3.setFill(Color.web("#F6F6F6"));
                final Shape GLOSSY4 = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4252336449 * WIDTH);
                GLOSSY4.setFill(Color.web("#333333"));
                FRAME.getChildren().addAll(MAIN_FRAME, GLOSSY2, GLOSSY3, GLOSSY4);
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
                final Circle DARK_GLOSSY2 = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.48598130841121495 * WIDTH);
                DARK_GLOSSY2.setFill(new LinearGradient(0.5 * WIDTH, 0.014018691588785047 * HEIGHT,
                                                        0.5 * WIDTH, 0.985981308411215 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.2588235294, 0.2588235294, 0.2588235294, 1)),
                                                        new Stop(0.42, Color.color(0.2588235294, 0.2588235294, 0.2588235294, 1)),
                                                        new Stop(1.0, Color.color(0.0509803922, 0.0509803922, 0.0509803922, 1))));
                DARK_GLOSSY2.setStroke(null);

                final Path DARK_GLOSSY3 = new Path();
                DARK_GLOSSY3.setFillRule(FillRule.EVEN_ODD);
                DARK_GLOSSY3.getElements().add(new MoveTo(0.014018691588785047 * WIDTH, 0.5 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.014018691588785047 * WIDTH, 0.514018691588785 * HEIGHT,
                                                                0.014018691588785047 * WIDTH, 0.5233644859813084 * HEIGHT,
                                                                0.014018691588785047 * WIDTH, 0.5373831775700935 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.07009345794392523 * WIDTH, 0.37383177570093457 * HEIGHT,
                                                                0.26635514018691586 * WIDTH, 0.2570093457943925 * HEIGHT,
                                                                0.5 * WIDTH, 0.2570093457943925 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.7336448598130841 * WIDTH, 0.2570093457943925 * HEIGHT,
                                                                0.9299065420560748 * WIDTH, 0.37383177570093457 * HEIGHT,
                                                                0.985981308411215 * WIDTH, 0.5373831775700935 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.985981308411215 * WIDTH, 0.5233644859813084 * HEIGHT,
                                                                0.985981308411215 * WIDTH, 0.514018691588785 * HEIGHT,
                                                                0.985981308411215 * WIDTH, 0.5 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.985981308411215 * WIDTH, 0.2336448598130841 * HEIGHT,
                                                                0.7663551401869159 * WIDTH, 0.014018691588785047 * HEIGHT,
                                                                0.5 * WIDTH, 0.014018691588785047 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new CubicCurveTo(0.2336448598130841 * WIDTH, 0.014018691588785047 * HEIGHT,
                                                                0.014018691588785047 * WIDTH, 0.2336448598130841 * HEIGHT,
                                                                0.014018691588785047 * WIDTH, 0.5 * HEIGHT));
                DARK_GLOSSY3.getElements().add(new ClosePath());
                DARK_GLOSSY3.setFill(new LinearGradient(0.5 * WIDTH, 0.014018691588785047 * HEIGHT,
                                                        0.5 * WIDTH, 0.5280373831775701 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(1, 1, 1, 1)),
                                                        new Stop(0.26, Color.color(1, 1, 1, 1)),
                                                        new Stop(0.26009998, Color.color(1, 1, 1, 1)),
                                                        new Stop(1.0, Color.color(1, 1, 1, 0))));
                DARK_GLOSSY3.setStroke(null);

                final Circle DARK_GLOSSY4 = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4392523364485981 * WIDTH);
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
                FRAME.getChildren().addAll(MAIN_FRAME, DARK_GLOSSY2, DARK_GLOSSY3, DARK_GLOSSY4);
                break;
            default:
                IMAGE_VIEW = new ImageView();
                IMAGE_VIEW.setVisible(false);
                MAIN_FRAME.setId(control.getFrameDesign().CSS);
                MAIN_FRAME.setStroke(null);
                FRAME.getChildren().addAll(MAIN_FRAME, INNER_FRAME);
                break;
        }
        FRAME.setCache(true);
    }

    protected void drawCircularBackground(final Gauge CONTROL, final Group BACKGROUND, final Rectangle GAUGE_BOUNDS) {
        final double SIZE = GAUGE_BOUNDS.getWidth() <= GAUGE_BOUNDS.getHeight() ? GAUGE_BOUNDS.getWidth() : GAUGE_BOUNDS.getHeight();
        BACKGROUND.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, SIZE, SIZE);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        BACKGROUND.getChildren().add(IBOUNDS);

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setWidth(0.2 * SIZE);
        INNER_SHADOW.setHeight(0.2 * SIZE);
        INNER_SHADOW.setColor(Color.color(0, 0, 0, 1.0));
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        /*// needed for carbon and punched sheet look
        final LinearGradient HL_GRADIENT = new LinearGradient(0, 0, SIZE, 0, false, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.color(0.0, 0.0, 0.0, 0.6)),
            new Stop(0.4, Color.color(0.0, 0.0, 0.0, 0.0)),
            new Stop(0.6, Color.color(0.0, 0.0, 0.0, 0.0)),
            new Stop(1.0, Color.color(0.0, 0.0, 0.0, 0.6)));

            final Shape CLIP = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.4158878504672897 * SIZE);
            final ImageView IMAGE_VIEW;
        */

        final Shape BACKGROUND_SHAPE = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.4158878504672897 * SIZE);
        BACKGROUND_SHAPE.setStroke(null);

        switch (CONTROL.getBackgroundDesign()) {
            default:
                BACKGROUND_SHAPE.setStyle(CONTROL.getSimpleGradientBaseColorString());
                BACKGROUND_SHAPE.setId(CONTROL.getBackgroundDesign().CSS_BACKGROUND);
                BACKGROUND_SHAPE.setEffect(INNER_SHADOW);
                BACKGROUND_SHAPE.setStroke(null);
                BACKGROUND.getChildren().addAll(BACKGROUND_SHAPE);
                break;
        }
        BACKGROUND.setCache(true);
    }

    protected void drawCircularTrend(final Gauge CONTROL, final Group trend, final Rectangle GAUGE_BOUNDS) {
        final double WIDTH = GAUGE_BOUNDS.getWidth();
        final double HEIGHT = GAUGE_BOUNDS.getHeight();

        trend.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        trend.getChildren().add(IBOUNDS);

        final double RADIUS = 0.005 * WIDTH;
        final double DIAMETER = 2 * RADIUS;

        double matrixOffsetX = 0.455 * WIDTH;
        double matrixOffsetY = 0.79 * WIDTH;

        switch(CONTROL.getRadialRange()) {
            case RADIAL_300:
                matrixOffsetX = 0.455 * WIDTH;
                matrixOffsetY = 0.79 * WIDTH;
                break;
            case RADIAL_270:
                matrixOffsetX = 0.60 * WIDTH;
                matrixOffsetY = 0.72 * HEIGHT;
                break;
            case RADIAL_180:
                matrixOffsetX = 0.455 * WIDTH;
                matrixOffsetY = 0.79 * WIDTH;
                break;
            case RADIAL_180N:
                matrixOffsetX = 0.60 * WIDTH;
                matrixOffsetY = 0.45 * WIDTH;
                break;
            case RADIAL_180S:
                matrixOffsetX = 0.60 * WIDTH;
                matrixOffsetY = 0.11 * WIDTH;
                break;
            case RADIAL_90:
                matrixOffsetX = 0.455 * WIDTH;
                matrixOffsetY = 0.79 * WIDTH;
                break;
            case RADIAL_90N:
                matrixOffsetX = 0.60 * WIDTH;
                matrixOffsetY = 0.72 * WIDTH;
                break;
            case RADIAL_90S:
                matrixOffsetX = 0.60 * WIDTH;
                matrixOffsetY = 0.2 * WIDTH;
                break;
        }

        // Frame
        final Rectangle MATRIX_FRAME = new Rectangle(matrixOffsetX - 1, matrixOffsetY - 1,
                                                     0.1 * WIDTH, 0.1 * WIDTH);
        MATRIX_FRAME.setFill(new LinearGradient(0, matrixOffsetY - 1,
            0, matrixOffsetY - 1 + MATRIX_FRAME.getLayoutBounds().getHeight(),
            false, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.color(0.1, 0.1, 0.1, 1.0)),
            new Stop(0.1, Color.color(0.3, 0.3, 0.3, 1.0)),
            new Stop(0.93, Color.color(0.3, 0.3, 0.3, 1.0)),
            new Stop(1.0, Color.color(0.86, 0.86, 0.86, 1.0))));
        MATRIX_FRAME.setStroke(null);
        trend.getChildren().add(MATRIX_FRAME);

        // Background
        final Rectangle MATRIX_BACKGROUND = new Rectangle(MATRIX_FRAME.getX() + 1, MATRIX_FRAME.getY() + 1,
                                                          MATRIX_FRAME.getWidth() - 2, MATRIX_FRAME.getHeight() - 2);
        MATRIX_BACKGROUND.setFill(new LinearGradient(0, matrixOffsetY,
            0, matrixOffsetY + MATRIX_FRAME.getLayoutBounds().getHeight(),
            false, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.color(0.3, 0.3, 0.3, 1.0)),
            new Stop(1.0, Color.color(0.1, 0.1, 0.1, 1.0))));
        MATRIX_BACKGROUND.setStroke(null);
        trend.getChildren().add(MATRIX_BACKGROUND);

        // Led's
        Paint ledPaint;
        Color ledColor;
        switch(CONTROL.getTrend()) {
            case UP:
                ledPaint = CONTROL.getTrendUpColor();
                ledColor = CONTROL.getTrendUpColor();
                break;
            case STEADY:
                ledPaint = CONTROL.getTrendSteadyColor();
                ledColor = CONTROL.getTrendSteadyColor();
                break;
            case DOWN:
                ledPaint = CONTROL.getTrendDownColor();
                ledColor = CONTROL.getTrendDownColor();
                break;
            default:
                ledPaint = new RadialGradient(0, 0,
                                              matrixOffsetX + RADIUS, matrixOffsetY + RADIUS,
                                              RADIUS,
                                              false, CycleMethod.NO_CYCLE,
                                              new Stop(0.0, Color.rgb(102, 102, 102)),
                                              new Stop(1.0, Color.rgb(51, 51, 51)));
                ledColor = Color.rgb(75, 75, 75);
                break;
        }

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setWidth(0.002 * WIDTH);
        INNER_SHADOW.setHeight(0.002 * WIDTH);
        INNER_SHADOW.setColor(Color.color(0, 0, 0, 1.0));
        INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        final DropShadow GLOW_EFFECT = new DropShadow();
        GLOW_EFFECT.setInput(INNER_SHADOW);
        GLOW_EFFECT.setSpread(0.1);
        GLOW_EFFECT.setRadius(0.01 * WIDTH);
        GLOW_EFFECT.setBlurType(BlurType.GAUSSIAN);
        GLOW_EFFECT.setColor(ledColor);
        double ledOffsetX = matrixOffsetX;
        double ledOffsetY = matrixOffsetY;
        for (int y = 0 ; y < 9 ; y++) {
            for (int x = 0 ; x < 9 ; x++) {
                Circle led = new Circle(ledOffsetX + RADIUS, ledOffsetY + RADIUS, RADIUS);
                if (CONTROL.getTrend().ledMatrix.get(y)[x] == 1) {
                    led.setFill(new RadialGradient(0, 0,
                                                   ledOffsetX + RADIUS, ledOffsetY + RADIUS,
                                                   RADIUS,
                                                   false, CycleMethod.NO_CYCLE,
                                                   new Stop(0.0, ((Color) ledPaint)),
                                                   new Stop(1.0, ((Color) ledPaint).darker().darker())));
                    led.setEffect(GLOW_EFFECT);
                } else {
                    led.setFill(new RadialGradient(0, 0,
                                                   ledOffsetX + RADIUS, ledOffsetY + RADIUS,
                                                   RADIUS,
                                                   false, CycleMethod.NO_CYCLE,
                                                   new Stop(0.0, Color.rgb(80, 80, 80)),
                                                   new Stop(1.0, Color.rgb(50, 50, 50))));
                    led.setEffect(INNER_SHADOW);
                }
                led.setSmooth(true);
                trend.getChildren().add(led);
                ledOffsetX += DIAMETER;
            }
            ledOffsetX = matrixOffsetX;
            ledOffsetY += DIAMETER;
        }
        trend.setCache(true);
    }

    protected void drawCircularSections(final Gauge CONTROL, final Group SECTIONS, final Rectangle GAUGE_BOUNDS) {
        final double SIZE = GAUGE_BOUNDS.getWidth() <= GAUGE_BOUNDS.getHeight() ? GAUGE_BOUNDS.getWidth() : GAUGE_BOUNDS.getHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        SECTIONS.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        SECTIONS.getChildren().addAll(IBOUNDS);

        for (final Section section : CONTROL.getSections()) {
            final Shape currentSection = section.getSectionArea();
            currentSection.setFill(section.getTransparentColor());
            currentSection.setStroke(null);
            SECTIONS.getChildren().add(currentSection);
        }
    }

    protected void drawCircularAreas(final Gauge CONTROL, final Group AREAS, final Rectangle GAUGE_BOUNDS) {
        final double SIZE = GAUGE_BOUNDS.getWidth() <= GAUGE_BOUNDS.getHeight() ? GAUGE_BOUNDS.getWidth() : GAUGE_BOUNDS.getHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        AREAS.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        AREAS.getChildren().add(IBOUNDS);

        for (final Section area : CONTROL.getAreas()) {
            final Shape currentArea = area.getFilledArea();
            currentArea.setFill(area.getTransparentColor());
            currentArea.setStroke(null);
            AREAS.getChildren().add(currentArea);
        }
    }

    protected void drawCircularGlowOff(final Group GLOW_OFF, final Rectangle GAUGE_BOUNDS) {
        final double SIZE = GAUGE_BOUNDS.getWidth() <= GAUGE_BOUNDS.getHeight() ? GAUGE_BOUNDS.getWidth() : GAUGE_BOUNDS.getHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        GLOW_OFF.getChildren().clear();

        final Path GLOW_RING = new Path();
        GLOW_RING.setFillRule(FillRule.EVEN_ODD);
        GLOW_RING.getElements().add(new MoveTo(0.10747663551401869 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.10747663551401869 * WIDTH, 0.2850467289719626 * HEIGHT, 0.2850467289719626 * WIDTH, 0.10747663551401869 * HEIGHT, 0.5 * WIDTH, 0.10747663551401869 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.7149532710280374 * WIDTH, 0.10747663551401869 * HEIGHT, 0.8925233644859814 * WIDTH, 0.2850467289719626 * HEIGHT, 0.8925233644859814 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.8925233644859814 * WIDTH, 0.7149532710280374 * HEIGHT, 0.7149532710280374 * WIDTH, 0.8925233644859814 * HEIGHT, 0.5 * WIDTH, 0.8925233644859814 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.2850467289719626 * WIDTH, 0.8925233644859814 * HEIGHT, 0.10747663551401869 * WIDTH, 0.7149532710280374 * HEIGHT, 0.10747663551401869 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new ClosePath());
        GLOW_RING.getElements().add(new MoveTo(0.08411214953271028 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.08411214953271028 * WIDTH, 0.7289719626168224 * HEIGHT, 0.27102803738317754 * WIDTH, 0.9158878504672897 * HEIGHT, 0.5 * WIDTH, 0.9158878504672897 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.7289719626168224 * WIDTH, 0.9158878504672897 * HEIGHT, 0.9158878504672897 * WIDTH, 0.7289719626168224 * HEIGHT, 0.9158878504672897 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.9158878504672897 * WIDTH, 0.27102803738317754 * HEIGHT, 0.7289719626168224 * WIDTH, 0.08411214953271028 * HEIGHT, 0.5 * WIDTH, 0.08411214953271028 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.27102803738317754 * WIDTH, 0.08411214953271028 * HEIGHT, 0.08411214953271028 * WIDTH, 0.27102803738317754 * HEIGHT, 0.08411214953271028 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new ClosePath());
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

        final Path HIGHLIGHT_LOWER_RIGHT = new Path();
        HIGHLIGHT_LOWER_RIGHT.setFillRule(FillRule.EVEN_ODD);
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new MoveTo(0.8598130841121495 * WIDTH, 0.6588785046728972 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.794392523364486 * WIDTH, 0.8037383177570093 * HEIGHT, 0.6588785046728972 * WIDTH, 0.8925233644859814 * HEIGHT, 0.5 * WIDTH, 0.8925233644859814 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.5 * WIDTH, 0.8925233644859814 * HEIGHT, 0.5 * WIDTH, 0.9158878504672897 * HEIGHT, 0.5 * WIDTH, 0.9158878504672897 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.6682242990654206 * WIDTH, 0.9158878504672897 * HEIGHT, 0.8084112149532711 * WIDTH, 0.822429906542056 * HEIGHT, 0.8785046728971962 * WIDTH, 0.6682242990654206 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.8785046728971962 * WIDTH, 0.6682242990654206 * HEIGHT, 0.8598130841121495 * WIDTH, 0.6588785046728972 * HEIGHT, 0.8598130841121495 * WIDTH, 0.6588785046728972 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new ClosePath());
        HIGHLIGHT_LOWER_RIGHT.setFill(new RadialGradient(0, 0, 0.7336448598130841 * WIDTH, 0.8364485981308412 * HEIGHT, 0.23598130841121495 * WIDTH, false, CycleMethod.NO_CYCLE, new Stop(0.0, Color.color(1, 1, 1, 0.5490196078)), new Stop(1.0, Color.color(1, 1, 1, 0))));
        HIGHLIGHT_LOWER_RIGHT.setStroke(null);

        final Path HIGHLIGHT_UPPER_LEFT = new Path();
        HIGHLIGHT_UPPER_LEFT.setFillRule(FillRule.EVEN_ODD);
        HIGHLIGHT_UPPER_LEFT.getElements().add(new MoveTo(0.14018691588785046 * WIDTH, 0.3411214953271028 * HEIGHT));
        HIGHLIGHT_UPPER_LEFT.getElements().add(new CubicCurveTo(0.205607476635514 * WIDTH, 0.19626168224299065 * HEIGHT, 0.3411214953271028 * WIDTH, 0.10747663551401869 * HEIGHT, 0.5 * WIDTH, 0.10747663551401869 * HEIGHT));
        HIGHLIGHT_UPPER_LEFT.getElements().add(new CubicCurveTo(0.5 * WIDTH, 0.10747663551401869 * HEIGHT, 0.5 * WIDTH, 0.08411214953271028 * HEIGHT, 0.5 * WIDTH, 0.08411214953271028 * HEIGHT));
        HIGHLIGHT_UPPER_LEFT.getElements().add(new CubicCurveTo(0.3317757009345794 * WIDTH, 0.08411214953271028 * HEIGHT, 0.18691588785046728 * WIDTH, 0.17757009345794392 * HEIGHT, 0.12149532710280374 * WIDTH, 0.3317757009345794 * HEIGHT));
        HIGHLIGHT_UPPER_LEFT.getElements().add(new CubicCurveTo(0.12149532710280374 * WIDTH, 0.3317757009345794 * HEIGHT, 0.14018691588785046 * WIDTH, 0.3411214953271028 * HEIGHT, 0.14018691588785046 * WIDTH, 0.3411214953271028 * HEIGHT));
        HIGHLIGHT_UPPER_LEFT.getElements().add(new ClosePath());
        HIGHLIGHT_UPPER_LEFT.setFill(new RadialGradient(0, 0, 0.26635514018691586 * WIDTH, 0.16355140186915887 * HEIGHT, 0.23598130841121495 * WIDTH, false, CycleMethod.NO_CYCLE, new Stop(0.0, Color.color(1, 1, 1, 0.4)), new Stop(1.0, Color.color(1, 1, 1, 0))));
        HIGHLIGHT_UPPER_LEFT.setStroke(null);

        GLOW_OFF.getChildren().addAll(GLOW_RING, HIGHLIGHT_LOWER_RIGHT, HIGHLIGHT_UPPER_LEFT);

        GLOW_OFF.setCache(true);
    }

    protected void drawCircularGlowOn(final Gauge CONTROL, final Group GLOW_ON, final ArrayList<Color> GLOW_COLORS, final Rectangle GAUGE_BOUNDS) {
        final double SIZE = GAUGE_BOUNDS.getWidth() <= GAUGE_BOUNDS.getHeight() ? GAUGE_BOUNDS.getWidth() : GAUGE_BOUNDS.getHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        GLOW_ON.getChildren().clear();

        final Path GLOW_RING = new Path();
        GLOW_RING.setFillRule(FillRule.EVEN_ODD);
        GLOW_RING.getElements().add(new MoveTo(0.10747663551401869 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.10747663551401869 * WIDTH, 0.2850467289719626 * HEIGHT, 0.2850467289719626 * WIDTH, 0.10747663551401869 * HEIGHT, 0.5 * WIDTH, 0.10747663551401869 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.7149532710280374 * WIDTH, 0.10747663551401869 * HEIGHT, 0.8925233644859814 * WIDTH, 0.2850467289719626 * HEIGHT, 0.8925233644859814 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.8925233644859814 * WIDTH, 0.7149532710280374 * HEIGHT, 0.7149532710280374 * WIDTH, 0.8925233644859814 * HEIGHT, 0.5 * WIDTH, 0.8925233644859814 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.2850467289719626 * WIDTH, 0.8925233644859814 * HEIGHT, 0.10747663551401869 * WIDTH, 0.7149532710280374 * HEIGHT, 0.10747663551401869 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new ClosePath());
        GLOW_RING.getElements().add(new MoveTo(0.08411214953271028 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.08411214953271028 * WIDTH, 0.7289719626168224 * HEIGHT, 0.27102803738317754 * WIDTH, 0.9158878504672897 * HEIGHT, 0.5 * WIDTH, 0.9158878504672897 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.7289719626168224 * WIDTH, 0.9158878504672897 * HEIGHT, 0.9158878504672897 * WIDTH, 0.7289719626168224 * HEIGHT, 0.9158878504672897 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.9158878504672897 * WIDTH, 0.27102803738317754 * HEIGHT, 0.7289719626168224 * WIDTH, 0.08411214953271028 * HEIGHT, 0.5 * WIDTH, 0.08411214953271028 * HEIGHT));
        GLOW_RING.getElements().add(new CubicCurveTo(0.27102803738317754 * WIDTH, 0.08411214953271028 * HEIGHT, 0.08411214953271028 * WIDTH, 0.27102803738317754 * HEIGHT, 0.08411214953271028 * WIDTH, 0.5 * HEIGHT));
        GLOW_RING.getElements().add(new ClosePath());

        final Paint GLOW_ON_PAINT = new RadialGradient(0, 0,
            0.5 * WIDTH, 0.5 * HEIGHT,
            0.4158878504672897 * WIDTH,
            false,
            CycleMethod.NO_CYCLE,
            new Stop(0.0, GLOW_COLORS.get(0)),
            new Stop(0.91, GLOW_COLORS.get(1)),
            new Stop(0.96, GLOW_COLORS.get(2)),
            new Stop(1.0, GLOW_COLORS.get(3)));

        GLOW_RING.setFill(GLOW_ON_PAINT);
        GLOW_RING.setStroke(null);

        final DropShadow GLOW_EFFECT = new DropShadow();
        GLOW_EFFECT.setRadius(0.15 * WIDTH);
        GLOW_EFFECT.setBlurType(BlurType.GAUSSIAN);
        if (!GLOW_EFFECT.colorProperty().isBound()) {
            GLOW_EFFECT.colorProperty().bind(CONTROL.glowColorProperty());
        }
        GLOW_RING.setEffect(GLOW_EFFECT);

        final Path HIGHLIGHT_LOWER_RIGHT = new Path();
        HIGHLIGHT_LOWER_RIGHT.setFillRule(FillRule.EVEN_ODD);
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new MoveTo(0.8598130841121495 * WIDTH, 0.6588785046728972 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.794392523364486 * WIDTH, 0.8037383177570093 * HEIGHT, 0.6588785046728972 * WIDTH, 0.8925233644859814 * HEIGHT, 0.5 * WIDTH, 0.8925233644859814 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.5 * WIDTH, 0.8925233644859814 * HEIGHT, 0.5 * WIDTH, 0.9158878504672897 * HEIGHT, 0.5 * WIDTH, 0.9158878504672897 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.6682242990654206 * WIDTH, 0.9158878504672897 * HEIGHT, 0.8084112149532711 * WIDTH, 0.822429906542056 * HEIGHT, 0.8785046728971962 * WIDTH, 0.6682242990654206 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new CubicCurveTo(0.8785046728971962 * WIDTH, 0.6682242990654206 * HEIGHT, 0.8598130841121495 * WIDTH, 0.6588785046728972 * HEIGHT, 0.8598130841121495 * WIDTH, 0.6588785046728972 * HEIGHT));
        HIGHLIGHT_LOWER_RIGHT.getElements().add(new ClosePath());
        HIGHLIGHT_LOWER_RIGHT.setFill(new RadialGradient(0, 0, 0.7336448598130841 * WIDTH, 0.8364485981308412 * HEIGHT, 0.23598130841121495 * WIDTH, false, CycleMethod.NO_CYCLE, new Stop(0.0, Color.color(1, 1, 1, 0.5490196078)), new Stop(1.0, Color.color(1, 1, 1, 0))));
        HIGHLIGHT_LOWER_RIGHT.setStroke(null);

        final Path HIGHLIGHT_UPPER_LEFT = new Path();
        HIGHLIGHT_UPPER_LEFT.setFillRule(FillRule.EVEN_ODD);
        HIGHLIGHT_UPPER_LEFT.getElements().add(new MoveTo(0.14018691588785046 * WIDTH, 0.3411214953271028 * HEIGHT));
        HIGHLIGHT_UPPER_LEFT.getElements().add(new CubicCurveTo(0.205607476635514 * WIDTH, 0.19626168224299065 * HEIGHT, 0.3411214953271028 * WIDTH, 0.10747663551401869 * HEIGHT, 0.5 * WIDTH, 0.10747663551401869 * HEIGHT));
        HIGHLIGHT_UPPER_LEFT.getElements().add(new CubicCurveTo(0.5 * WIDTH, 0.10747663551401869 * HEIGHT, 0.5 * WIDTH, 0.08411214953271028 * HEIGHT, 0.5 * WIDTH, 0.08411214953271028 * HEIGHT));
        HIGHLIGHT_UPPER_LEFT.getElements().add(new CubicCurveTo(0.3317757009345794 * WIDTH, 0.08411214953271028 * HEIGHT, 0.18691588785046728 * WIDTH, 0.17757009345794392 * HEIGHT, 0.12149532710280374 * WIDTH, 0.3317757009345794 * HEIGHT));
        HIGHLIGHT_UPPER_LEFT.getElements().add(new CubicCurveTo(0.12149532710280374 * WIDTH, 0.3317757009345794 * HEIGHT, 0.14018691588785046 * WIDTH, 0.3411214953271028 * HEIGHT, 0.14018691588785046 * WIDTH, 0.3411214953271028 * HEIGHT));
        HIGHLIGHT_UPPER_LEFT.getElements().add(new ClosePath());
        HIGHLIGHT_UPPER_LEFT.setFill(new RadialGradient(0, 0, 0.26635514018691586 * WIDTH, 0.16355140186915887 * HEIGHT, 0.23598130841121495 * WIDTH, false, CycleMethod.NO_CYCLE, new Stop(0.0, Color.color(1, 1, 1, 0.4)), new Stop(1.0, Color.color(1, 1, 1, 0))));
        HIGHLIGHT_UPPER_LEFT.setStroke(null);

        GLOW_ON.getChildren().addAll(GLOW_RING, HIGHLIGHT_LOWER_RIGHT, HIGHLIGHT_UPPER_LEFT);

        GLOW_ON.setCache(true);
    }

    protected void drawCircularIndicators(final Gauge CONTROL, final Group INDICATORS, final Point2D CENTER, final Rectangle GAUGE_BOUNDS) {
        final double SIZE = GAUGE_BOUNDS.getWidth() <= GAUGE_BOUNDS.getHeight() ? GAUGE_BOUNDS.getWidth() : GAUGE_BOUNDS.getHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        INDICATORS.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        INDICATORS.getChildren().add(IBOUNDS);

        INDICATORS.getTransforms().clear();
        INDICATORS.getTransforms().add(Transform.rotate(CONTROL.getRadialRange().ROTATION_OFFSET, CENTER.getX(), CENTER.getY()));
        INDICATORS.getTransforms().add(Transform.rotate(-CONTROL.getMinValue() * CONTROL.getAngleStep(), CENTER.getX(), CENTER.getY()));

        for (final Indicator indicator : CONTROL.getIndicators()) {
            if (Double.compare(indicator.getIndicatorValue(), CONTROL.getMinValue()) >= 0 && Double.compare(indicator.getIndicatorValue(), CONTROL.getMaxValue()) <= 0) {
                final Group ARROW = createIndicator(SIZE, indicator, new Point2D(SIZE * 0.4813084112, SIZE * 0.0841121495));
                ARROW.getTransforms().add(Transform.rotate(indicator.getIndicatorValue() * CONTROL.getAngleStep(), CENTER.getX(), CENTER.getY()));
                INDICATORS.getChildren().add(ARROW);
            }
        }
    }

    protected void drawCircularKnobs(final Gauge CONTROL, final Group KNOBS, final Point2D CENTER, final Rectangle GAUGE_BOUNDS) {
        final double SIZE = GAUGE_BOUNDS.getWidth() <= GAUGE_BOUNDS.getHeight() ? GAUGE_BOUNDS.getWidth() : GAUGE_BOUNDS.getHeight();
        final double WIDTH = GAUGE_BOUNDS.getWidth();
        final double HEIGHT = GAUGE_BOUNDS.getHeight();

        KNOBS.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        KNOBS.getChildren().add(IBOUNDS);

        final DropShadow SHADOW = new DropShadow();
        SHADOW.setBlurType(BlurType.GAUSSIAN);
        SHADOW.setColor(Color.color(0, 0, 0, 0.65));

        final DropShadow POST_SHADOW = new DropShadow();
        POST_SHADOW.setBlurType(BlurType.GAUSSIAN);
        POST_SHADOW.setColor(Color.color(0, 0, 0, 0.35));

        final Group CENTER_KNOB;
        final Point2D CENTER_OFFSET;
        final double KNOB_SIZE;
        switch (CONTROL.getKnobDesign()) {
            case BIG:
                KNOB_SIZE = Math.ceil(WIDTH * 0.1214953271);
                CENTER_KNOB = createBigKnob(KNOB_SIZE, CONTROL.getKnobColor());
                break;
            case METAL:
                KNOB_SIZE = Math.ceil(WIDTH * 0.0841121495);
                CENTER_KNOB = createMetalKnob(KNOB_SIZE, CONTROL.getKnobColor());
                break;
            case STANDARD:
            default:
                KNOB_SIZE = Math.ceil(WIDTH * 0.0841121495);
                CENTER_KNOB = createStandardKnob(KNOB_SIZE, CONTROL.getKnobColor());
                break;
        }
        CENTER_OFFSET = new Point2D(CENTER.getX() - KNOB_SIZE / 2.0, CENTER.getY() - KNOB_SIZE / 2.0);
        CENTER_KNOB.effectProperty().set(SHADOW);
        CENTER_KNOB.setTranslateX(CENTER_OFFSET.getX());
        CENTER_KNOB.setTranslateY(CENTER_OFFSET.getY());

        final Group MIN_POST = createStandardKnob(Math.ceil(WIDTH * 0.03738316893577576), CONTROL.getKnobColor());
        final Group MAX_POST = createStandardKnob(Math.ceil(WIDTH * 0.03738316893577576), CONTROL.getKnobColor());
        final Point2D MIN_OFFSET;
        final Point2D MAX_OFFSET;
        switch (CONTROL.getRadialRange()) {
            case RADIAL_90N:
                MIN_OFFSET = new Point2D(0.12, 0.4);
                MAX_OFFSET = new Point2D(0.845, 0.4);
                break;
            case RADIAL_90W:
                MIN_OFFSET = new Point2D(0.4, 0.845);
                MAX_OFFSET = new Point2D(0.4, 0.12);
                break;
            case RADIAL_90S:
                MIN_OFFSET = new Point2D(0.12, 0.56);
                MAX_OFFSET = new Point2D(0.845, 0.56);
                break;
            case RADIAL_90E:
                MIN_OFFSET = new Point2D(0.56, 0.845);
                MAX_OFFSET = new Point2D(0.56, 0.12);
                break;
            case RADIAL_90:
                MIN_OFFSET = new Point2D(0.13084112107753754, 0.514018714427948);
                MAX_OFFSET = new Point2D(0.5233644843101501, 0.13084112107753754);
                break;
            case RADIAL_180:
                MIN_OFFSET = new Point2D(0.13084112107753754, 0.514018714427948);
                MAX_OFFSET = new Point2D(0.8317757248878479, 0.514018714427948);
                break;
            case RADIAL_180N:
                MIN_OFFSET = new Point2D(0.13084112107753754, 0.514018714427948);
                MAX_OFFSET = new Point2D(0.8317757248878479, 0.514018714427948);
                break;
            case RADIAL_180S:
                MIN_OFFSET = new Point2D(0.13084112107753754, 0.1);
                MAX_OFFSET = new Point2D(0.8317757248878479, 0.1);
                break;
            case RADIAL_270:
                MIN_OFFSET = new Point2D(0.5233644843101501, 0.8317757248878479);
                MAX_OFFSET = new Point2D(0.8317757248878479, 0.514018714427948);
                break;
            case RADIAL_300:
            default:
                MIN_OFFSET = new Point2D(0.336448609828949, 0.8037382960319519);
                MAX_OFFSET = new Point2D(0.6261682510375977, 0.8037382960319519);
                break;
        }
        MIN_POST.setEffect(POST_SHADOW);
        MAX_POST.setEffect(POST_SHADOW);
        MIN_POST.setTranslateX(WIDTH * MIN_OFFSET.getX());
        MIN_POST.setTranslateY(WIDTH * MIN_OFFSET.getY());
        MAX_POST.setTranslateX(WIDTH * MAX_OFFSET.getX());
        MAX_POST.setTranslateY(WIDTH * MAX_OFFSET.getY());

        MIN_POST.visibleProperty().bind(CONTROL.postsVisibleProperty());
        MAX_POST.visibleProperty().bind(CONTROL.postsVisibleProperty());

        KNOBS.getChildren().addAll(CENTER_KNOB, MIN_POST, MAX_POST);
    }

    protected void drawCircularLed(final Gauge CONTROL, final Group LED_OFF, final Group LED_ON, final Rectangle GAUGE_BOUNDS) {
        final double WIDTH = GAUGE_BOUNDS.getWidth();
        final double HEIGHT = GAUGE_BOUNDS.getHeight();

        // OFF
        LED_OFF.getChildren().clear();
        final Shape IBOUNDS_OFF = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS_OFF.setOpacity(0.0);
        IBOUNDS_OFF.setStroke(null);
        final Group LED_OFF_GROUP = createLed(WIDTH * 0.1, CONTROL.getLedColor(), false);
        LED_OFF_GROUP.setLayoutX(WIDTH * CONTROL.getLedPosition().getX());
        LED_OFF_GROUP.setLayoutY(WIDTH * CONTROL.getLedPosition().getY());
        LED_OFF.getChildren().addAll(IBOUNDS_OFF, LED_OFF_GROUP);
        LED_OFF.setCache(true);

        // ON
        LED_ON.getChildren().clear();
        final Shape IBOUNDS_ON = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS_ON.setOpacity(0.0);
        IBOUNDS_ON.setStroke(null);
        final Group LED_ON_GROUP = createLed(WIDTH * 0.1, CONTROL.getLedColor(), true);
        LED_ON_GROUP.setLayoutX(WIDTH * CONTROL.getLedPosition().getX());
        LED_ON_GROUP.setLayoutY(WIDTH * CONTROL.getLedPosition().getY());
        LED_ON.getChildren().addAll(IBOUNDS_ON, LED_ON_GROUP);
        LED_ON.setCache(true);
    }

    protected void drawCircularUserLed(final Gauge CONTROL, final Group USER_LED_OFF, final Group USER_LED_ON, final Rectangle GAUGE_BOUNDS) {
        final double WIDTH = GAUGE_BOUNDS.getWidth();
        final double HEIGHT = GAUGE_BOUNDS.getHeight();

        // OFF
        USER_LED_OFF.getChildren().clear();
        final Shape IBOUNDS_OFF = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS_OFF.setOpacity(0.0);
        IBOUNDS_OFF.setStroke(null);
        final Group LED_OFF_GROUP = createLed(WIDTH * 0.1, CONTROL.getUserLedColor(), false);
        LED_OFF_GROUP.setLayoutX(WIDTH * CONTROL.getUserLedPosition().getX());
        LED_OFF_GROUP.setLayoutY(WIDTH * CONTROL.getUserLedPosition().getY());
        USER_LED_OFF.getChildren().addAll(IBOUNDS_OFF, LED_OFF_GROUP);

        // ON
        USER_LED_ON.getChildren().clear();
        final Shape IBOUNDS_ON = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS_ON.setOpacity(0.0);
        IBOUNDS_ON.setStroke(null);
        final Group LED_ON_GROUP = createLed(WIDTH * 0.1, CONTROL.getUserLedColor(), true);
        LED_ON_GROUP.setLayoutX(WIDTH * CONTROL.getUserLedPosition().getX());
        LED_ON_GROUP.setLayoutY(WIDTH * CONTROL.getUserLedPosition().getY());
        USER_LED_ON.getChildren().addAll(IBOUNDS_ON, LED_ON_GROUP);
    }

    protected void drawCircularLcd(final Gauge CONTROL, final Group LCD, final Rectangle GAUGE_BOUNDS) {
        final double SIZE = GAUGE_BOUNDS.getWidth() <= GAUGE_BOUNDS.getHeight() ? GAUGE_BOUNDS.getWidth() : GAUGE_BOUNDS.getHeight();

        LCD.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, SIZE, SIZE);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        LCD.getChildren().add(IBOUNDS);

        final Rectangle LCD_FRAME = new Rectangle(((SIZE - SIZE * CONTROL.getRadialRange().LCD_FACTORS.getX()) / 2.0),
                                                   (SIZE * CONTROL.getRadialRange().LCD_FACTORS.getY()),
                                                   (SIZE * CONTROL.getRadialRange().LCD_FACTORS.getWidth()),
                                                   (SIZE * CONTROL.getRadialRange().LCD_FACTORS.getHeight()));
        final double LCD_FRAME_CORNER_RADIUS = LCD_FRAME.getWidth() > LCD_FRAME.getHeight() ? (LCD_FRAME.getHeight() * 0.15) : (LCD_FRAME.getWidth() * 0.15);
        LCD_FRAME.arcWidthProperty().set(LCD_FRAME_CORNER_RADIUS);
        LCD_FRAME.arcHeightProperty().set(LCD_FRAME_CORNER_RADIUS);
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
        LCD_MAIN.setStyle(CONTROL.getLcdDesign().CSS);
        LCD_MAIN.setId("lcd-main");
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

        LCD.getChildren().addAll(LCD_FRAME, LCD_MAIN);
    }

    protected void drawCircularBargraph(final Gauge CONTROL, final Group BARGRAPH, final int NO_OF_LEDS, final ArrayList<Shape> LEDS, final boolean ON, final Point2D CENTER, final Rectangle GAUGE_BOUNDS) {
        final double WIDTH = GAUGE_BOUNDS.getWidth();
        final double HEIGHT = GAUGE_BOUNDS.getHeight();

        BARGRAPH.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        BARGRAPH.getChildren().addAll(IBOUNDS);

        final double ADDITIONAL_OFFSET;
        switch(CONTROL.getRadialRange()) {
            case RADIAL_90:
                ADDITIONAL_OFFSET = 180;
                break;
            case RADIAL_180:
                ADDITIONAL_OFFSET = 90;
                break;
            case RADIAL_180N:
                ADDITIONAL_OFFSET = 90;
                break;
            case RADIAL_180S:
                ADDITIONAL_OFFSET = -90;
                break;
            case RADIAL_270:
                ADDITIONAL_OFFSET = 180;
                break;
            case RADIAL_300:
            default:
                ADDITIONAL_OFFSET = 90;
                break;
        }

        LEDS.clear();

        for (int i = 0 ; i < NO_OF_LEDS ; i++) {
            final Shape LED = createBargraphLed(GAUGE_BOUNDS, CONTROL, ON);
            LED.getTransforms().add(Transform.rotate(CONTROL.getRadialRange().SECTIONS_OFFSET - ADDITIONAL_OFFSET - 2.5 - (5 * i), CENTER.getX(), CENTER.getY()));
            LEDS.add(LED);
            BARGRAPH.getChildren().add(LED);
        }
        BARGRAPH.setCache(true);
    }

    protected void drawCircularForeground(final Gauge CONTROL, final Group FOREGROUND, final Rectangle GAUGE_BOUNDS) {
        final double SIZE = GAUGE_BOUNDS.getWidth() <= GAUGE_BOUNDS.getHeight() ? GAUGE_BOUNDS.getWidth() : GAUGE_BOUNDS.getHeight();
        FOREGROUND.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, SIZE, SIZE);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        FOREGROUND.getChildren().addAll(IBOUNDS);

        final Path FOREGROUND_SHAPE = new Path();
        switch (CONTROL.getForegroundType()) {
            case TYPE2:
                FOREGROUND_SHAPE.setFillRule(FillRule.EVEN_ODD);
                FOREGROUND_SHAPE.getElements().add(new MoveTo(0.13551401869158877 * SIZE, 0.6962616822429907 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.21495327102803738 * SIZE,
                                                                    0.5887850467289719 * SIZE, 0.3177570093457944 * SIZE,
                                                                    0.5 * SIZE, 0.46261682242990654 * SIZE, 0.4252336448598131 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.6121495327102804 * SIZE, 0.34579439252336447 * SIZE,
                                                                    0.7336448598130841 * SIZE, 0.3177570093457944 * SIZE,
                                                                    0.8738317757009346 * SIZE, 0.32242990654205606 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.7663551401869159 * SIZE, 0.11214953271028037 * SIZE,
                                                                    0.5280373831775701 * SIZE, 0.02336448598130841 * SIZE,
                                                                    0.3130841121495327 * SIZE, 0.1308411214953271 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.09813084112149532 * SIZE, 0.2383177570093458 * SIZE,
                                                                    0.028037383177570093 * SIZE, 0.48598130841121495 * SIZE,
                                                                    0.13551401869158877 * SIZE, 0.6962616822429907 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new ClosePath());
                FOREGROUND_SHAPE.setId("foreground-type2");
                FOREGROUND_SHAPE.setStroke(null);
                FOREGROUND.getChildren().addAll(FOREGROUND_SHAPE);
                break;
            case TYPE3:
                FOREGROUND_SHAPE.setFillRule(FillRule.EVEN_ODD);
                FOREGROUND_SHAPE.getElements().add(new MoveTo(0.08411214953271028 * SIZE, 0.5093457943925234 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.2102803738317757 * SIZE, 0.5560747663551402 * SIZE,
                                                                    0.46261682242990654 * SIZE, 0.5607476635514018 * SIZE,
                                                                    0.5 * SIZE, 0.5607476635514018 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.5373831775700935 * SIZE, 0.5607476635514018 * SIZE,
                                                                    0.794392523364486 * SIZE, 0.5607476635514018 * SIZE,
                                                                    0.9158878504672897 * SIZE, 0.5093457943925234 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.9158878504672897 * SIZE, 0.2757009345794392 * SIZE,
                                                                    0.7383177570093458 * SIZE, 0.08411214953271028 * SIZE,
                                                                    0.5 * SIZE, 0.08411214953271028 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.2616822429906542 * SIZE, 0.08411214953271028 * SIZE,
                                                                    0.08411214953271028 * SIZE, 0.2757009345794392 * SIZE,
                                                                    0.08411214953271028 * SIZE, 0.5093457943925234 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new ClosePath());
                FOREGROUND_SHAPE.setId("foreground-type3");
                FOREGROUND_SHAPE.setStroke(null);
                FOREGROUND.getChildren().addAll(FOREGROUND_SHAPE);
                break;
            case TYPE4:
                FOREGROUND_SHAPE.setFillRule(FillRule.EVEN_ODD);
                FOREGROUND_SHAPE.getElements().add(new MoveTo(0.677570093457944 * SIZE, 0.24299065420560748 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.7710280373831776 * SIZE, 0.308411214953271 * SIZE,
                                                                    0.822429906542056 * SIZE, 0.411214953271028 * SIZE,
                                                                    0.8130841121495327 * SIZE, 0.5280373831775701 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.7990654205607477 * SIZE, 0.6542056074766355 * SIZE,
                                                                    0.719626168224299 * SIZE, 0.7570093457943925 * SIZE,
                                                                    0.5934579439252337 * SIZE, 0.7990654205607477 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.48598130841121495 * SIZE, 0.8317757009345794 * SIZE,
                                                                    0.3691588785046729 * SIZE, 0.8084112149532711 * SIZE,
                                                                    0.2850467289719626 * SIZE, 0.7289719626168224 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.2757009345794392 * SIZE, 0.719626168224299 * SIZE,
                                                                    0.2523364485981308 * SIZE, 0.7149532710280374 * SIZE,
                                                                    0.2336448598130841 * SIZE, 0.7289719626168224 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.21495327102803738 * SIZE, 0.7476635514018691 * SIZE,
                                                                    0.21962616822429906 * SIZE, 0.7710280373831776 * SIZE,
                                                                    0.22897196261682243 * SIZE, 0.7757009345794392 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.3317757009345794 * SIZE, 0.8785046728971962 * SIZE,
                                                                    0.4766355140186916 * SIZE, 0.9158878504672897 * SIZE,
                                                                    0.616822429906542 * SIZE, 0.8691588785046729 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.7710280373831776 * SIZE, 0.822429906542056 * SIZE,
                                                                    0.8738317757009346 * SIZE, 0.6915887850467289 * SIZE,
                                                                    0.8878504672897196 * SIZE, 0.5327102803738317 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.897196261682243 * SIZE, 0.3878504672897196 * SIZE,
                                                                    0.8364485981308412 * SIZE, 0.2570093457943925 * SIZE,
                                                                    0.719626168224299 * SIZE, 0.1822429906542056 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.705607476635514 * SIZE, 0.17289719626168223 * SIZE,
                                                                    0.6822429906542056 * SIZE, 0.16355140186915887 * SIZE,
                                                                    0.6635514018691588 * SIZE, 0.18691588785046728 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.6542056074766355 * SIZE, 0.205607476635514 * SIZE,
                                                                    0.6682242990654206 * SIZE, 0.2383177570093458 * SIZE,
                                                                    0.677570093457944 * SIZE, 0.24299065420560748 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new ClosePath());
                FOREGROUND_SHAPE.setFill(new RadialGradient(0, 0,
                                                            0.5 * SIZE,
                                                            0.5 * SIZE, 0.3878504672897196 * SIZE,
                                                            false, CycleMethod.NO_CYCLE,
                                                            new Stop(0.0, Color.color(1, 1, 1, 0)),
                                                            new Stop(0.83, Color.color(1, 1, 1, 0)),
                                                            new Stop(1.0, Color.color(1, 1, 1, 0.0980392157))));
                FOREGROUND_SHAPE.setStroke(null);

                final Path FOREGROUND1_PATH = new Path();
                FOREGROUND1_PATH.setFillRule(FillRule.EVEN_ODD);
                FOREGROUND1_PATH.getElements().add(new MoveTo(0.2616822429906542 * SIZE, 0.22429906542056074 * SIZE));
                FOREGROUND1_PATH.getElements().add(new CubicCurveTo(0.2850467289719626 * SIZE, 0.2383177570093458 * SIZE,
                                                                    0.2523364485981308 * SIZE, 0.2850467289719626 * SIZE,
                                                                    0.24299065420560748 * SIZE, 0.3177570093457944 * SIZE));
                FOREGROUND1_PATH.getElements().add(new CubicCurveTo(0.24299065420560748 * SIZE, 0.35046728971962615 * SIZE,
                                                                    0.27102803738317754 * SIZE, 0.38317757009345793 * SIZE,
                                                                    0.27102803738317754 * SIZE, 0.397196261682243 * SIZE));
                FOREGROUND1_PATH.getElements().add(new CubicCurveTo(0.2757009345794392 * SIZE, 0.4158878504672897 * SIZE,
                                                                    0.2616822429906542 * SIZE, 0.45794392523364486 * SIZE,
                                                                    0.2383177570093458 * SIZE, 0.5093457943925234 * SIZE));
                FOREGROUND1_PATH.getElements().add(new CubicCurveTo(0.22429906542056074 * SIZE, 0.5420560747663551 * SIZE,
                                                                    0.17757009345794392 * SIZE, 0.6121495327102804 * SIZE,
                                                                    0.1588785046728972 * SIZE, 0.6121495327102804 * SIZE));
                FOREGROUND1_PATH.getElements().add(new CubicCurveTo(0.14485981308411214 * SIZE, 0.6121495327102804 * SIZE,
                                                                    0.08878504672897196 * SIZE, 0.5467289719626168 * SIZE,
                                                                    0.1308411214953271 * SIZE, 0.3691588785046729 * SIZE));
                FOREGROUND1_PATH.getElements().add(new CubicCurveTo(0.14018691588785046 * SIZE, 0.3364485981308411 * SIZE,
                                                                    0.21495327102803738 * SIZE, 0.20093457943925233 * SIZE,
                                                                    0.2616822429906542 * SIZE, 0.22429906542056074 * SIZE));
                FOREGROUND1_PATH.getElements().add(new ClosePath());
                FOREGROUND_SHAPE.setId("foreground-type4");
                FOREGROUND1_PATH.setStroke(null);
                FOREGROUND.getChildren().addAll(FOREGROUND_SHAPE, FOREGROUND1_PATH);
                break;
            case TYPE5:
                FOREGROUND_SHAPE.setFillRule(FillRule.EVEN_ODD);
                FOREGROUND_SHAPE.getElements().add(new MoveTo(0.08411214953271028 * SIZE, 0.5 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.08411214953271028 * SIZE, 0.27102803738317754 * SIZE,
                                                                    0.27102803738317754 * SIZE, 0.08411214953271028 * SIZE,
                                                                    0.5 * SIZE, 0.08411214953271028 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.7009345794392523 * SIZE, 0.08411214953271028 * SIZE,
                                                                    0.8644859813084113 * SIZE, 0.22429906542056074 * SIZE,
                                                                    0.9065420560747663 * SIZE, 0.411214953271028 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.9112149532710281 * SIZE, 0.4392523364485981 * SIZE,
                                                                    0.9112149532710281 * SIZE, 0.5186915887850467 * SIZE,
                                                                    0.8457943925233645 * SIZE, 0.5373831775700935 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.794392523364486 * SIZE, 0.5467289719626168 * SIZE,
                                                                    0.5514018691588785 * SIZE, 0.411214953271028 * SIZE,
                                                                    0.3925233644859813 * SIZE, 0.45794392523364486 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.16822429906542055 * SIZE, 0.5093457943925234 * SIZE,
                                                                    0.13551401869158877 * SIZE, 0.7757009345794392 * SIZE,
                                                                    0.09345794392523364 * SIZE, 0.5934579439252337 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.08878504672897196 * SIZE, 0.5607476635514018 * SIZE,
                                                                    0.08411214953271028 * SIZE, 0.5327102803738317 * SIZE,
                                                                    0.08411214953271028 * SIZE, 0.5 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new ClosePath());
                FOREGROUND_SHAPE.setId("foreground-type5");
                FOREGROUND_SHAPE.setStroke(null);
                FOREGROUND.getChildren().addAll(FOREGROUND_SHAPE);
                break;
            case TYPE1:
            default:
                FOREGROUND_SHAPE.setFillRule(FillRule.EVEN_ODD);
                FOREGROUND_SHAPE.getElements().add(new MoveTo(0.08411214953271028 * SIZE, 0.5093457943925234 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.205607476635514 * SIZE, 0.4485981308411215 * SIZE,
                                                                    0.3364485981308411 * SIZE, 0.4158878504672897 * SIZE,
                                                                    0.5 * SIZE, 0.4158878504672897 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.6728971962616822 * SIZE, 0.4158878504672897 * SIZE,
                                                                    0.7897196261682243 * SIZE, 0.4439252336448598 * SIZE,
                                                                    0.9158878504672897 * SIZE, 0.5093457943925234 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.9158878504672897 * SIZE, 0.2757009345794392 * SIZE,
                                                                    0.7383177570093458 * SIZE, 0.08411214953271028 * SIZE,
                                                                    0.5 * SIZE, 0.08411214953271028 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new CubicCurveTo(0.2616822429906542 * SIZE, 0.08411214953271028 * SIZE,
                                                                    0.08411214953271028 * SIZE, 0.2757009345794392 * SIZE,
                                                                    0.08411214953271028 * SIZE, 0.5093457943925234 * SIZE));
                FOREGROUND_SHAPE.getElements().add(new ClosePath());
                FOREGROUND_SHAPE.setId("foreground-type1");
                FOREGROUND_SHAPE.setStroke(null);
                FOREGROUND.getChildren().addAll(FOREGROUND_SHAPE);
                break;
        }
        FOREGROUND.setCache(true);
    }

    protected void drawCircularTickmarks(final Gauge CONTROL, final Group TICKMARKS, final Point2D CENTER, final Rectangle GAUGE_BOUNDS) {
        final double SIZE = GAUGE_BOUNDS.getWidth() <= GAUGE_BOUNDS.getHeight() ? GAUGE_BOUNDS.getWidth() : GAUGE_BOUNDS.getHeight();
        final double WIDTH = GAUGE_BOUNDS.getWidth();
        final double HEIGHT = GAUGE_BOUNDS.getHeight();

        final double RADIUS_FACTOR = CONTROL.getRadialRange().RADIUS_FACTOR;

        final double TEXT_DISTANCE_FACTOR;
        switch (CONTROL.getTickLabelOrientation()) {
            case TANGENT:
                TEXT_DISTANCE_FACTOR = 0.07;
                break;
            case HORIZONTAL:
                TEXT_DISTANCE_FACTOR = 0.08;
                break;
            case NORMAL:
            default:
                TEXT_DISTANCE_FACTOR = 0.09;
                break;
        }

        TICKMARKS.getTransforms().clear();
        TICKMARKS.getChildren().clear();
        TICKMARKS.setId(CONTROL.getBackgroundDesign().CSS_BACKGROUND);
        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        TICKMARKS.getChildren().add(IBOUNDS);

        final Path MAJOR_TICKMARKS_PATH = new Path();
        MAJOR_TICKMARKS_PATH.setFillRule(FillRule.EVEN_ODD);
        MAJOR_TICKMARKS_PATH.setSmooth(true);
        MAJOR_TICKMARKS_PATH.setStrokeType(StrokeType.CENTERED);
        MAJOR_TICKMARKS_PATH.setStrokeLineCap(StrokeLineCap.ROUND);
        MAJOR_TICKMARKS_PATH.setStrokeLineJoin(StrokeLineJoin.BEVEL);
        MAJOR_TICKMARKS_PATH.setStrokeWidth(0.0046728972 * WIDTH);
        switch(CONTROL.getMajorTickmarkType()) {
            case TRIANGLE:
                MAJOR_TICKMARKS_PATH.setId(CONTROL.getBackgroundDesign().CSS_TEXT);
                //MAJOR_TICKMARKS_PATH.setStroke(null);
                break;
            default:
                MAJOR_TICKMARKS_PATH.setId(CONTROL.getBackgroundDesign().CSS_BACKGROUND);
                break;
        }

        final Path MEDIUM_TICKMARKS_PATH = new Path();
        MEDIUM_TICKMARKS_PATH.setFillRule(FillRule.EVEN_ODD);
        MEDIUM_TICKMARKS_PATH.setSmooth(true);
        MEDIUM_TICKMARKS_PATH.setStrokeType(StrokeType.CENTERED);
        MEDIUM_TICKMARKS_PATH.setStrokeLineCap(StrokeLineCap.ROUND);
        MEDIUM_TICKMARKS_PATH.setStrokeLineJoin(StrokeLineJoin.BEVEL);
        MEDIUM_TICKMARKS_PATH.setStrokeWidth(0.0023364486 * WIDTH);
        MEDIUM_TICKMARKS_PATH.setId(CONTROL.getBackgroundDesign().CSS_BACKGROUND);

        final Path MINOR_TICKMARKS_PATH = new Path();
        MINOR_TICKMARKS_PATH.setFillRule(FillRule.EVEN_ODD);
        MINOR_TICKMARKS_PATH.setSmooth(true);
        MINOR_TICKMARKS_PATH.setStrokeType(StrokeType.CENTERED);
        MINOR_TICKMARKS_PATH.setStrokeLineCap(StrokeLineCap.ROUND);
        MINOR_TICKMARKS_PATH.setStrokeLineJoin(StrokeLineJoin.BEVEL);
        MINOR_TICKMARKS_PATH.setStrokeWidth(0.0014018692 * WIDTH);
        MINOR_TICKMARKS_PATH.setId(CONTROL.getBackgroundDesign().CSS_BACKGROUND);

        final double TEXT_BARGRAPH_OFFSET;
        if (CONTROL.isBargraph()) {
            MAJOR_TICKMARKS_PATH.setVisible(false);
            MEDIUM_TICKMARKS_PATH.setVisible(false);
            MINOR_TICKMARKS_PATH.setVisible(false);
            TEXT_BARGRAPH_OFFSET = 0.03;
        } else {
            MAJOR_TICKMARKS_PATH.setVisible(true);
            MEDIUM_TICKMARKS_PATH.setVisible(true);
            MINOR_TICKMARKS_PATH.setVisible(true);
            TEXT_BARGRAPH_OFFSET = 0.0;
        }

        final ArrayList<Text> tickmarkLabel = new ArrayList<>();

        // Adjust the number format of the ticklabels
        final Gauge.NumberFormat numberFormat;
        if (CONTROL.getTickLabelNumberFormat() == Gauge.NumberFormat.AUTO) {
            if (Math.abs(CONTROL.getMajorTickSpacing()) > 1000) {
                numberFormat = Gauge.NumberFormat.SCIENTIFIC;
            } else if (CONTROL.getMajorTickSpacing() % 1.0 != 0) {
                numberFormat = Gauge.NumberFormat.FRACTIONAL;
            } else {
                numberFormat = Gauge.NumberFormat.STANDARD;
            }
        } else {
            numberFormat = CONTROL.getTickLabelNumberFormat();
        }

        // Definitions
        final Font STD_FONT                  = Font.font("Verdana", FontWeight.NORMAL, (0.035 * WIDTH));
        final double TEXT_DISTANCE           = (TEXT_DISTANCE_FACTOR + TEXT_BARGRAPH_OFFSET) * WIDTH;
        final double ticklabelRotationOffset = 0;
        final double MINOR_TICK_LENGTH       = (0.0133333333 * WIDTH);
        final double MEDIUM_TICK_LENGTH      = (0.02 * WIDTH);
        final double MAJOR_TICK_LENGTH       = (0.03 * WIDTH);
        Point2D textPoint;
        Point2D innerPoint;
        Point2D outerPoint;

        final double ROTATION_OFFSET = CONTROL.getRadialRange().ROTATION_OFFSET; // Depends on RadialRange
        final double RADIUS          = WIDTH * RADIUS_FACTOR;
        final double ANGLE_STEP      = (CONTROL.getRadialRange().ANGLE_RANGE / ((CONTROL.getMaxValue() - CONTROL.getMinValue()) / CONTROL.getMinorTickSpacing())) * CONTROL.getRadialRange().ANGLE_STEP_SIGN;
        double valueCounter          = CONTROL.getMinValue();
        int majorTickCounter         = CONTROL.getMaxNoOfMinorTicks() - 1; // Indicator when to draw the major tickmark
        double sinValue;
        double cosValue;

        // Set some default parameters for the graphics object
        if (CONTROL.getTickmarksOffset() != null) {
            TICKMARKS.translateXProperty().set(CONTROL.getTickmarksOffset().getX());
            TICKMARKS.translateYProperty().set(CONTROL.getTickmarksOffset().getY());
        }

        final Transform transform = Transform.rotate(ROTATION_OFFSET - 180, CENTER.getX(), CENTER.getY());
        TICKMARKS.getTransforms().add(transform);

        for (double alpha = 0, counter = CONTROL.getMinValue(); Double.compare(counter, CONTROL.getMaxValue()) <= 0; alpha -= ANGLE_STEP, counter += CONTROL.getMinorTickSpacing()) {
            sinValue = Math.sin(Math.toRadians(alpha));
            cosValue = Math.cos(Math.toRadians(alpha));

            majorTickCounter++;

            // Draw tickmark every major tickmark spacing
            if (majorTickCounter == CONTROL.getMaxNoOfMinorTicks()) {
                innerPoint = new Point2D(CENTER.getX() + (RADIUS - MAJOR_TICK_LENGTH) * sinValue, CENTER.getY() + (RADIUS - MAJOR_TICK_LENGTH) * cosValue);
                outerPoint = new Point2D(CENTER.getX() + RADIUS * sinValue, CENTER.getY() + RADIUS * cosValue);
                textPoint  = new Point2D(CENTER.getX() + (RADIUS - TEXT_DISTANCE) * sinValue, CENTER.getY() + (RADIUS - TEXT_DISTANCE) * cosValue);

                // Draw the major TICKMARKS
                if (CONTROL.isTickmarksVisible() && CONTROL.isMajorTicksVisible()) {
                    switch(CONTROL.getMajorTickmarkType()) {
                        case TRIANGLE:
                            Point2D outerPointLeft  = new Point2D(CENTER.getX() + RADIUS * Math.sin(Math.toRadians(alpha - 1.2)), CENTER.getY() + RADIUS * Math.cos(Math.toRadians(alpha - 1.2)));
                            Point2D outerPointRight = new Point2D(CENTER.getX() + RADIUS * Math.sin(Math.toRadians(alpha + 1.2)), CENTER.getY() + RADIUS * Math.cos(Math.toRadians(alpha + 1.2)));
                            MAJOR_TICKMARKS_PATH.getElements().add(new MoveTo(innerPoint.getX(), innerPoint.getY()));
                            MAJOR_TICKMARKS_PATH.getElements().add(new LineTo(outerPointLeft.getX(), outerPointLeft.getY()));
                            MAJOR_TICKMARKS_PATH.getElements().add(new LineTo(outerPointRight.getX(), outerPointRight.getY()));
                            MAJOR_TICKMARKS_PATH.getElements().add(new ClosePath());
                            break;
                        default:
                            drawRadialTicks(MAJOR_TICKMARKS_PATH, innerPoint, outerPoint);
                            break;
                    }
                }

                // Draw the standard tickmark labels
                if (CONTROL.isTickLabelsVisible()) {
                    final Text tickLabel = new Text(numberFormat.format(valueCounter));
                    tickLabel.setSmooth(true);
                    tickLabel.setFontSmoothingType(FontSmoothingType.LCD);
                    tickLabel.setTextOrigin(VPos.BOTTOM);
                    tickLabel.setId(CONTROL.getBackgroundDesign().CSS_TEXT);
                    tickLabel.setStroke(null);
                    tickLabel.setFont(STD_FONT);
                    tickLabel.setX(textPoint.getX() - tickLabel.getLayoutBounds().getHeight() / 2.0);
                    tickLabel.setY(textPoint.getY() + tickLabel.getLayoutBounds().getHeight() / 2.0);
                    switch (CONTROL.getTickLabelOrientation()) {
                        case NORMAL:
                            if (Double.compare(alpha, -CONTROL.getRadialRange().TICKLABEL_ORIENATION_CHANGE_ANGLE) > 0) {
                                tickLabel.rotateProperty().set(-90 - alpha);
                            } else {
                                tickLabel.rotateProperty().set(90 - alpha);
                            }
                            break;
                        case HORIZONTAL:
                            tickLabel.rotateProperty().set(180 - CONTROL.getRadialRange().ROTATION_OFFSET);
                            break;
                        case TANGENT:

                        default:
                            tickLabel.rotateProperty().set(180 - alpha + ticklabelRotationOffset);
                            break;
                    }
                    tickmarkLabel.add(tickLabel);
                }

                valueCounter += CONTROL.getMajorTickSpacing();
                majorTickCounter = 0;
                continue;
            }

            // Draw tickmark every minor tickmark spacing
            innerPoint = new Point2D(CENTER.getX() + (RADIUS - MINOR_TICK_LENGTH) * sinValue, CENTER.getY() + (RADIUS - MINOR_TICK_LENGTH) * cosValue);
            outerPoint = new Point2D(CENTER.getX() + RADIUS * sinValue, CENTER.getY() + RADIUS * cosValue);
            if (CONTROL.getMaxNoOfMinorTicks() % 2 == 0 && majorTickCounter == (CONTROL.getMaxNoOfMinorTicks() / 2)) {
                // Draw the medium TICKMARKS
                innerPoint = new Point2D(CENTER.getX() + (RADIUS - MEDIUM_TICK_LENGTH) * sinValue,
                    CENTER.getY() + (RADIUS - MEDIUM_TICK_LENGTH) * cosValue);
                outerPoint = new Point2D(CENTER.getX() + RADIUS * sinValue, CENTER.getY() + RADIUS * cosValue);
                if (CONTROL.isTickmarksVisible() && CONTROL.isMinorTicksVisible()) {
                    drawRadialTicks(MEDIUM_TICKMARKS_PATH, innerPoint, outerPoint);
                }
            } else if (CONTROL.isTickmarksVisible() && CONTROL.isMinorTicksVisible()) {
                // Draw the minor TICKMARKS
                drawRadialTicks(MINOR_TICKMARKS_PATH, innerPoint, outerPoint);
            }
        }

        TICKMARKS.getChildren().addAll(MAJOR_TICKMARKS_PATH, MEDIUM_TICKMARKS_PATH, MINOR_TICKMARKS_PATH);
        TICKMARKS.getChildren().addAll(tickmarkLabel);

        TICKMARKS.setCache(true);
    }

    private static void drawRadialTicks(final Path TICKMARKS_PATH, final Point2D INNER_POINT, final Point2D OUTER_POINT) {
            TICKMARKS_PATH.getElements().add(new MoveTo(INNER_POINT.getX(), INNER_POINT.getY()));
            TICKMARKS_PATH.getElements().add(new LineTo(OUTER_POINT.getX(), OUTER_POINT.getY()));
        }


    // ******************** Shape and Group creation **************************
    protected Group createLed(final double SIZE, final LedColor LED_COLOR, final boolean isOn) {
       final Group LED = new Group();

       final Circle LED_BACKGROUND = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.25 * SIZE);
       LED_BACKGROUND.setFill(new LinearGradient(0.0, 0.0,
           0.0, SIZE,
           false, CycleMethod.NO_CYCLE,
           new Stop(0.0, Color.color(0, 0, 0, 0.9)),
           new Stop(0.4, Color.color(0.2, 0.2, 0.2, 0.96)),
           new Stop(1.0, Color.color(0.6, 0.6, 0.6, 1))
       ));
       LED_BACKGROUND.setStroke(null);

       final InnerShadow INNER_SHADOW = new InnerShadow();
       INNER_SHADOW.blurTypeProperty().set(BlurType.GAUSSIAN);
       INNER_SHADOW.setRadius(10);
       INNER_SHADOW.setWidth(0.2 * SIZE);
       INNER_SHADOW.setHeight(0.2 * SIZE);
       INNER_SHADOW.setColor(Color.color(0, 0, 0, 1.0));

       final DropShadow GLOW_EFFECT = new DropShadow();
       GLOW_EFFECT.setInput(INNER_SHADOW);
       GLOW_EFFECT.setSpread(0.6);
       GLOW_EFFECT.setWidth(2 * SIZE);
       GLOW_EFFECT.setHeight(2 * SIZE);
       GLOW_EFFECT.setBlurType(BlurType.GAUSSIAN);
       GLOW_EFFECT.setColor(LED_COLOR.GLOW_COLOR);

       final Circle MAIN_LED = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.2261904762 * SIZE);
       if (isOn) {
           MAIN_LED.getStyleClass().add("root");
           MAIN_LED.setStyle(LED_COLOR.CSS);
           MAIN_LED.setId("led-on-gradient");
           MAIN_LED.setStroke(null);
           MAIN_LED.setEffect(GLOW_EFFECT);
       } else {
           MAIN_LED.getStyleClass().add("root");
           MAIN_LED.setStyle(LED_COLOR.CSS);
           MAIN_LED.setId("led-off-gradient");
           MAIN_LED.setStroke(null);
           MAIN_LED.effectProperty().set(INNER_SHADOW);
       }

       final Circle LED_INNER_SHADOW = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.2261904762 * SIZE);
       LED_INNER_SHADOW.setFill(new RadialGradient(0, 0, 0.47619047619047616 * SIZE, 0.47619047619047616 * SIZE, 0.4523809523809524 * SIZE, false, CycleMethod.NO_CYCLE, new Stop(0.0, Color.color(0, 0, 0, 0)), new Stop(0.86, Color.color(0, 0, 0, 0.3450980392)),
           new Stop(1.0, new Color(0, 0, 0, 0.4))));
       LED_INNER_SHADOW.setStroke(null);

       final Ellipse LED_HIGHLIGHT = new Ellipse(0.5 * SIZE, 0.4 * SIZE, 0.1 * SIZE, 0.06 * SIZE);
       LED_HIGHLIGHT.setFill(new LinearGradient(0.0, 0.3 * SIZE, 0.0, 0.5 * SIZE, false, CycleMethod.NO_CYCLE, new Stop(0.0, new Color(1, 1, 1, 0.4)), new Stop(1.0, new Color(1, 1, 1, 0))));
       LED_HIGHLIGHT.setStroke(null);

       LED.getChildren().addAll(LED_BACKGROUND, MAIN_LED, LED_HIGHLIGHT);
       return LED;
   }

    protected Group createIndicator(final double SIZE, final Indicator INDICATOR, final Point2D OFFSET) {
        final Group INDICATOR_GROUP = new Group();
        final double WIDTH = (SIZE * 0.04);
        final double HEIGHT = (SIZE * 0.1);
        INDICATOR_GROUP.getChildren().add(createIndicatorShape(WIDTH, HEIGHT, INDICATOR, OFFSET));
        return INDICATOR_GROUP;
    }

    protected Shape createIndicatorShape(final double WIDTH, final double HEIGHT, final Indicator INDICATOR, final Point2D OFFSET) {
        final Path MARKER = new Path();
        MARKER.setFillRule(FillRule.EVEN_ODD);
        MARKER.getElements().add(new MoveTo(WIDTH * 0.1111111111111111, HEIGHT * 0.047619047619047616));
        MARKER.getElements().add(new LineTo(WIDTH * 0.8888888888888888, HEIGHT * 0.047619047619047616));
        MARKER.getElements().add(new LineTo(WIDTH * 0.8888888888888888, HEIGHT * 0.3333333333333333));
        MARKER.getElements().add(new LineTo(WIDTH * 0.5, HEIGHT * 0.5714285714285714));
        MARKER.getElements().add(new LineTo(WIDTH * 0.1111111111111111, HEIGHT * 0.3333333333333333));
        MARKER.getElements().add(new ClosePath());

        MARKER.setStroke(Color.WHITE);
        MARKER.setStrokeType(StrokeType.CENTERED);
        MARKER.setStrokeLineCap(StrokeLineCap.ROUND);
        MARKER.setStrokeLineJoin(StrokeLineJoin.ROUND);
        MARKER.setStrokeWidth(0.02 * HEIGHT);

        final LinearGradient HL_GRADIENT = new LinearGradient(MARKER.getLayoutX(), 0, MARKER.getLayoutX() + MARKER.getLayoutBounds().getWidth(),
                                                              0, false, CycleMethod.NO_CYCLE,
                                                              new Stop(0.0, INDICATOR.getIndicatorColor().brighter()),
                                                              new Stop(0.55, INDICATOR.getIndicatorColor().brighter()),
                                                              new Stop(0.55, INDICATOR.getIndicatorColor().darker()),
                                                              new Stop(1.0, INDICATOR.getIndicatorColor().darker()));
        MARKER.setFill(HL_GRADIENT);

        final InnerShadow INNER_SHADOW = new InnerShadow();
        INNER_SHADOW.setColor(Color.color(0.0, 0.0, 0.0, 0.4));

        final DropShadow SHADOW = new DropShadow();
        SHADOW.setHeight(0.325 * HEIGHT);
        SHADOW.setWidth(0.325 * HEIGHT);
        SHADOW.setColor(Color.color(0, 0, 0, 0.65));
        SHADOW.setInput(INNER_SHADOW);
        MARKER.setEffect(SHADOW);

        MARKER.setLayoutX(OFFSET.getX());
        MARKER.setLayoutY(OFFSET.getY());

        return MARKER;
    }

    protected Group createStandardKnob(final double SIZE, final KnobColor KNOB_COLOR) {
        final Group KNOB = new Group();

        final Stop[] KNOB_MAIN_STOPS;
        switch (KNOB_COLOR) {
            case BLACK:
                KNOB_MAIN_STOPS = new Stop[] {
                    new Stop(0.0, Color.web("#BFBFBF")),
                    new Stop(0.5, Color.web("#2B2A2F")),
                    new Stop(1.0, Color.web("#7D7E80"))
                };
                break;
            case BRASS:
                KNOB_MAIN_STOPS = new Stop[] {
                    new Stop(0.0, Color.web("#DFD0AE")),
                    new Stop(0.5, Color.web("#7A5E3E")),
                    new Stop(1.0, Color.web("#CFBE9D"))
                };
                break;
            case SILVER:
            default:
                KNOB_MAIN_STOPS = new Stop[] {
                    new Stop(0.0, Color.web("#D7D7D7")),
                    new Stop(0.5, Color.web("#747474")),
                    new Stop(1.0, Color.web("#D7D7D7"))
                };
                break;
        }

        final Circle KNOB_FRAME = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.5 * SIZE);
        KNOB_FRAME.setFill(new LinearGradient(0.5 * SIZE, 0.0, 0.5 * SIZE, SIZE, false,
            CycleMethod.NO_CYCLE,
            new Stop(0.0, new Color(0.7058823529, 0.7058823529, 0.7058823529, 1)),
            new Stop(0.46, new Color(0.2470588235, 0.2470588235, 0.2470588235, 1)),
            new Stop(1.0, new Color(0.1568627451, 0.1568627451, 0.1568627451, 1))));
        KNOB_FRAME.setStroke(null);

        final Circle KNOB_MAIN = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.3888888888888889 * SIZE);
        KNOB_MAIN.setFill(new LinearGradient(0.5 * SIZE, 0.1111111111111111 * SIZE, 0.5 * SIZE,
            0.8888888888888888 * SIZE, false, CycleMethod.NO_CYCLE,
            KNOB_MAIN_STOPS));
        KNOB_MAIN.setStroke(null);

        KNOB.getChildren().addAll(KNOB_FRAME, KNOB_MAIN);
        return KNOB;
    }

    protected Group createMetalKnob(final double SIZE, final KnobColor KNOB_COLOR) {
        final Group KNOB = new Group();

        final Circle KNOB_FRAME = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.5 * SIZE);
        KNOB_FRAME.setFill(new LinearGradient(0.5 * SIZE, 0.0, 0.5 * SIZE, SIZE, false,
            CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.color(0.3607843137, 0.3725490196, 0.3960784314, 1)),
            new Stop(0.47, Color.color(0.1803921569, 0.1921568627, 0.2078431373, 1)),
            new Stop(1.0, Color.color(0.0862745098, 0.0901960784, 0.1019607843, 1))));
        KNOB_FRAME.setStroke(null);

        final Stop[] KNOB_MAIN_STOPS;
        switch (KNOB_COLOR) {
            case BLACK:
                KNOB_MAIN_STOPS = new Stop[] {
                    new Stop(0.0, Color.web("#2B2A2F")),
                    new Stop(1.0, Color.web("#1A1B20"))
                };
                break;
            case BRASS:
                KNOB_MAIN_STOPS = new Stop[] {
                    new Stop(0.0, Color.web("#966E36")),
                    new Stop(1.0, Color.web("#7C5F3D"))
                };
                break;
            case SILVER:
            default:
                KNOB_MAIN_STOPS = new Stop[] {
                    new Stop(0.0, Color.color(0.8, 0.8, 0.8, 1)),
                    new Stop(1.0, Color.color(0.3411764706, 0.3607843137, 0.3843137255, 1))
                };
                break;
        }

        final Circle KNOB_MAIN = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.4444444444444444 * SIZE);
        KNOB_MAIN.setFill(new LinearGradient(0.5 * SIZE, 0.05555555555555555 * SIZE, 0.5 * SIZE,
                                             0.9444444444444444 * SIZE, false, CycleMethod.NO_CYCLE,
                                             KNOB_MAIN_STOPS));
        KNOB_MAIN.setStroke(null);

        final Path KNOB_LOWER_HIGHLIGHT = new Path();
        KNOB_LOWER_HIGHLIGHT.setFillRule(FillRule.EVEN_ODD);
        KNOB_LOWER_HIGHLIGHT.getElements().add(new MoveTo(0.7777777777777778 * SIZE, 0.8333333333333334 * SIZE));
        KNOB_LOWER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.7222222222222222 * SIZE, 0.7222222222222222 * SIZE,
                                                                0.6111111111111112 * SIZE, 0.6666666666666666 * SIZE,
                                                                0.5 * SIZE, 0.6666666666666666 * SIZE));
        KNOB_LOWER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.3888888888888889 * SIZE, 0.6666666666666666 * SIZE,
                                                                0.2777777777777778 * SIZE, 0.7222222222222222 * SIZE,
                                                                0.2222222222222222 * SIZE, 0.8333333333333334 * SIZE));
        KNOB_LOWER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.2777777777777778 * SIZE, 0.8888888888888888 * SIZE,
                                                                0.3888888888888889 * SIZE, 0.9444444444444444 * SIZE,
                                                                0.5 * SIZE, 0.9444444444444444 * SIZE));
        KNOB_LOWER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.6111111111111112 * SIZE, 0.9444444444444444 * SIZE,
                                                                0.7222222222222222 * SIZE, 0.8888888888888888 * SIZE,
                                                                0.7777777777777778 * SIZE, 0.8333333333333334 * SIZE));
        KNOB_LOWER_HIGHLIGHT.getElements().add(new ClosePath());
        KNOB_LOWER_HIGHLIGHT.setFill(new RadialGradient(0, 0, 0.5555555555555556 * SIZE, 0.9444444444444444 * SIZE,
                                                        0.3888888888888889 * SIZE, false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(1, 1, 1, 0.6)),
                                                        new Stop(1.0, Color.color(1, 1, 1, 0))));
        KNOB_LOWER_HIGHLIGHT.setStroke(null);

        final Path KNOB_UPPER_HIGHLIGHT = new Path();
        KNOB_UPPER_HIGHLIGHT.setFillRule(FillRule.EVEN_ODD);
        KNOB_UPPER_HIGHLIGHT.getElements().add(new MoveTo(0.9444444444444444 * SIZE, 0.2777777777777778 * SIZE));
        KNOB_UPPER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.8333333333333334 * SIZE, 0.1111111111111111 * SIZE,
                                                                0.6666666666666666 * SIZE, 0.0, 0.5 * SIZE, 0.0));
        KNOB_UPPER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.3333333333333333 * SIZE, 0.0, 0.16666666666666666 * SIZE,
                                                                0.1111111111111111 * SIZE, 0.05555555555555555 * SIZE, 0.2777777777777778 * SIZE));
        KNOB_UPPER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.16666666666666666 * SIZE, 0.3333333333333333 * SIZE,
                                                                0.3333333333333333 * SIZE, 0.3888888888888889 * SIZE,
                                                                0.5 * SIZE, 0.3888888888888889 * SIZE));
        KNOB_UPPER_HIGHLIGHT.getElements().add(new CubicCurveTo(0.6666666666666666 * SIZE, 0.3888888888888889 * SIZE,
                                                                0.8333333333333334 * SIZE, 0.3333333333333333 * SIZE,
                                                                0.9444444444444444 * SIZE, 0.2777777777777778 * SIZE));
        KNOB_UPPER_HIGHLIGHT.getElements().add(new ClosePath());
        KNOB_UPPER_HIGHLIGHT.setFill(new RadialGradient(0, 0, 0.5 * SIZE, 0.0, 0.5833333333333334 * SIZE,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(1, 1, 1, 0.7490196078)),
                                                        new Stop(1.0, Color.color(1, 1, 1, 0))));
        KNOB_UPPER_HIGHLIGHT.setStroke(null);

        final Circle KNOB_INNER_FRAME = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.2777777777777778 * SIZE);
        KNOB_INNER_FRAME.setFill(new LinearGradient(0.5 * SIZE, 0.2222222222222222 * SIZE, 0.5 * SIZE, 0.7777777777777778 * SIZE, false, CycleMethod.NO_CYCLE,
                                                    new Stop(0.0, Color.color(0, 0, 0, 1)),
                                                    new Stop(1.0, Color.color(0.8, 0.8, 0.8, 1))));
        KNOB_INNER_FRAME.setStroke(null);

        final Circle KNOB_INNER_BACKGROUND = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.2222222222222222 * SIZE);
        KNOB_INNER_BACKGROUND.setFill(new LinearGradient(0.5 * SIZE, 0.2777777777777778 * SIZE, 0.5 * SIZE, 0.7222222222222222 * SIZE, false, CycleMethod.NO_CYCLE,
                                                         new Stop(0.0, Color.color(0.0039215686, 0.0235294118, 0.0431372549, 1)),
                                                         new Stop(1.0, Color.color(0.1960784314, 0.2039215686, 0.2196078431, 1))));
        KNOB_INNER_BACKGROUND.setStroke(null);

        KNOB.getChildren().addAll(KNOB_FRAME, KNOB_MAIN, KNOB_LOWER_HIGHLIGHT, KNOB_UPPER_HIGHLIGHT, KNOB_INNER_FRAME, KNOB_INNER_BACKGROUND);
        return KNOB;
    }

    protected Group createBigKnob(final double SIZE, final KnobColor KNOB_COLOR) {
        final Group KNOB = new Group();

        final Stop[] KNOB_FRAME_STOPS;
        final Stop[] KNOB_BACKGROUND_STOPS;
        final Stop[] KNOB_FOREGROUND_FRAME_STOPS;
        final Stop[] KNOB_FOREGROUND_STOPS;
        switch (KNOB_COLOR) {
            case BLACK:
                KNOB_FRAME_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(129, 133, 136)),
                    new Stop(1.0, Color.rgb(61, 61, 73))
                };
                KNOB_BACKGROUND_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(26, 27, 32)),
                    new Stop(1.0, Color.rgb(96, 97, 102))
                };
                KNOB_FOREGROUND_FRAME_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(191, 191, 191)),
                    new Stop(0.47, Color.rgb(56, 57, 61)),
                    new Stop(1.0, Color.rgb(143, 144, 146))
                };
                KNOB_FOREGROUND_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(191, 191, 191)),
                    new Stop(0.21, Color.rgb(94, 93, 99)),
                    new Stop(0.5, Color.rgb(43, 42, 47)),
                    new Stop(0.78, Color.rgb(78, 79, 81)),
                    new Stop(1.0, Color.rgb(143, 144, 146))
                };
                break;
            case BRASS:
                KNOB_FRAME_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(143, 117, 80)),
                    new Stop(1.0, Color.rgb(100, 76, 49))
                };
                KNOB_BACKGROUND_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(98, 75, 49)),
                    new Stop(1.0, Color.rgb(149, 109, 54))
                };
                KNOB_FOREGROUND_FRAME_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(147, 108, 54)),
                    new Stop(0.47, Color.rgb(82, 66, 50)),
                    new Stop(1.0, Color.rgb(147, 108, 54))
                };
                KNOB_FOREGROUND_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(223, 208, 174)),
                    new Stop(0.21, Color.rgb(159, 136, 104)),
                    new Stop(0.5, Color.rgb(122, 94, 62)),
                    new Stop(0.78, Color.rgb(159, 136, 104)),
                    new Stop(1.0, Color.rgb(223, 208, 174))
                };
                break;
            case SILVER:
            default:
                KNOB_FRAME_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(152, 152, 152)),
                    new Stop(1.0, Color.rgb(118, 121, 126))
                };
                KNOB_BACKGROUND_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(118, 121, 126)),
                    new Stop(1.0, Color.rgb(191, 191, 191))
                };
                KNOB_FOREGROUND_FRAME_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(191, 191, 191)),
                    new Stop(0.47, Color.rgb(116, 116, 116)),
                    new Stop(1.0, Color.rgb(143, 144, 146))
                };
                KNOB_FOREGROUND_STOPS = new Stop[] {
                    new Stop(0.0, Color.rgb(215, 215, 215)),
                    new Stop(0.21, Color.rgb(139, 142, 145)),
                    new Stop(0.5, Color.rgb(100, 100, 100)),
                    new Stop(0.78, Color.rgb(139, 142, 145)),
                    new Stop(1.0, Color.rgb(215, 215, 215))
                };
                break;
        }

        final Circle KNOB_FRAME = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.5 * SIZE);
        KNOB_FRAME.setFill(new LinearGradient(0.5 * SIZE, 0.0, 0.5 * SIZE, SIZE, false,
                                              CycleMethod.NO_CYCLE,
                                              KNOB_FRAME_STOPS));
        KNOB_FRAME.setStroke(null);


        final Circle KNOB_BACKGROUND = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.46153846153846156 * SIZE);
        KNOB_BACKGROUND.setFill(new LinearGradient(0.5 * SIZE, 0.038461538461538464 * SIZE,
                                                   0.5 * SIZE, 0.9615384615384616 * SIZE, false, CycleMethod.NO_CYCLE,
                                                   KNOB_BACKGROUND_STOPS));
        KNOB_BACKGROUND.setStroke(null);

        final Circle KNOB_FOREGROUND_FRAME = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.38461538461538464 * SIZE);
        KNOB_FOREGROUND_FRAME.setFill(new LinearGradient(0.5 * SIZE, 0.11538461538461539 * SIZE,
                                                         0.5 * SIZE, 0.8846153846153846 * SIZE, false, CycleMethod.NO_CYCLE,
                                                         KNOB_FOREGROUND_FRAME_STOPS));
        KNOB_FOREGROUND_FRAME.setStroke(null);

        final Circle KNOB_FOREGROUND = new Circle(0.5 * SIZE, 0.5 * SIZE, 0.34615384615384615 * SIZE);
        KNOB_FOREGROUND.setFill(new LinearGradient(0.5 * SIZE, 0.15384615384615385 * SIZE, 0.5 * SIZE,
                                                   0.8461538461538461 * SIZE, false, CycleMethod.NO_CYCLE,
                                                   KNOB_FOREGROUND_STOPS));
        KNOB_FOREGROUND.setStroke(null);

        KNOB.getChildren().addAll(KNOB_FRAME, KNOB_BACKGROUND, KNOB_FOREGROUND_FRAME, KNOB_FOREGROUND);
        return KNOB;
    }

    protected Path createTriangleShape(final double WIDTH, final double HEIGHT, final boolean TOP_DOWN) {
        final Path SHAPE = new Path();
        SHAPE.setFillRule(FillRule.EVEN_ODD);
        if (TOP_DOWN) {
            SHAPE.getElements().add(new MoveTo(0, 0));
            SHAPE.getElements().add(new LineTo(WIDTH * 0.5, HEIGHT));
            SHAPE.getElements().add(new LineTo(WIDTH, 0));
        } else {
            SHAPE.getElements().add(new MoveTo(0.5 * WIDTH, 0));
            SHAPE.getElements().add(new LineTo(0, HEIGHT));
            SHAPE.getElements().add(new LineTo(WIDTH, HEIGHT));
        }
        SHAPE.getElements().add(new ClosePath());
        return SHAPE;
    }

    protected Shape createBargraphLed(final Rectangle GAUGE_BOUNDS, final Gauge CONTROL, boolean ON) {
        final double WIDTH = GAUGE_BOUNDS.getWidth();
        final Path LED = new Path();

        LED.setFillRule(FillRule.EVEN_ODD);
        LED.getElements().add(new MoveTo(0.485 * WIDTH, 0.185 * WIDTH));
        LED.getElements().add(new CubicCurveTo(0.495 * WIDTH, 0.185 * WIDTH,
                                               0.505 * WIDTH, 0.185 * WIDTH,
                                               0.515 * WIDTH, 0.185 * WIDTH));
        LED.getElements().add(new CubicCurveTo(0.515 * WIDTH, 0.185 * WIDTH,
                                               0.515 * WIDTH, 0.11 * WIDTH,
                                               0.515 * WIDTH, 0.11 * WIDTH));
        LED.getElements().add(new CubicCurveTo(0.505 * WIDTH, 0.11 * WIDTH,
                                               0.495 * WIDTH, 0.11 * WIDTH,
                                               0.485 * WIDTH, 0.11 * WIDTH));
        LED.getElements().add(new CubicCurveTo(0.485 * WIDTH, 0.11 * WIDTH,
                                               0.485 * WIDTH, 0.185 * WIDTH,
                                               0.485 * WIDTH, 0.185 * WIDTH));
        LED.getElements().add(new ClosePath());
        final InnerShadow LED_INNER_SHADOW = new InnerShadow();
        LED_INNER_SHADOW.setRadius(0.02 * WIDTH);
        LED_INNER_SHADOW.setColor(Color.BLACK);
        LED.setEffect(LED_INNER_SHADOW);

        LED.setStyle("-fx-value: " + CONTROL.getValueColor().CSS);
        if (ON) {
            LED.setId("bargraph-on");
        } else {
            LED.setId("bargraph-off");
        }
        LED.setStroke(null);

        /*
        final DropShadow LED_GLOW = new DropShadow();
        LED_GLOW.setRadius(0.018 * WIDTH);
        LED_GLOW.setColor(Color.BLACK);
        LED_GLOW.inputProperty().set(LED_INNER_SHADOW);
        LED.setEffect(LED_GLOW);
        */
        return LED;
    }

    protected Group createLcdThresholdIndicator(final double WIDTH, final double HEIGHT, final String CSS_STYLE) {
        final Group LCD_THRESHOLD_INDICATOR = new Group();
        LCD_THRESHOLD_INDICATOR.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        IBOUNDS.setStroke(null);
        LCD_THRESHOLD_INDICATOR.getChildren().add(IBOUNDS);

        final Path INDICATOR = new Path();
        INDICATOR.setFillRule(FillRule.EVEN_ODD);
        INDICATOR.getElements().add(new MoveTo(WIDTH * 0.4444444444444444, HEIGHT * 0.7777777777777778));
        INDICATOR.getElements().add(new LineTo(WIDTH * 0.5555555555555556, HEIGHT * 0.7777777777777778));
        INDICATOR.getElements().add(new LineTo(WIDTH * 0.5555555555555556, HEIGHT * 0.8888888888888888));
        INDICATOR.getElements().add(new LineTo(WIDTH * 0.4444444444444444, HEIGHT * 0.8888888888888888));
        INDICATOR.getElements().add(new LineTo(WIDTH * 0.4444444444444444, HEIGHT * 0.7777777777777778));
        INDICATOR.getElements().add(new ClosePath());
        INDICATOR.getElements().add(new MoveTo(WIDTH * 0.4444444444444444, HEIGHT * 0.3333333333333333));
        INDICATOR.getElements().add(new LineTo(WIDTH * 0.5555555555555556, HEIGHT * 0.3333333333333333));
        INDICATOR.getElements().add(new LineTo(WIDTH * 0.5555555555555556, HEIGHT * 0.7222222222222222));
        INDICATOR.getElements().add(new LineTo(WIDTH * 0.4444444444444444, HEIGHT * 0.7222222222222222));
        INDICATOR.getElements().add(new LineTo(WIDTH * 0.4444444444444444, HEIGHT * 0.3333333333333333));
        INDICATOR.getElements().add(new ClosePath());
        INDICATOR.getElements().add(new MoveTo(0.0, HEIGHT));
        INDICATOR.getElements().add(new LineTo(WIDTH, HEIGHT));
        INDICATOR.getElements().add(new LineTo(WIDTH * 0.5, 0.0));
        INDICATOR.getElements().add(new LineTo(0.0, HEIGHT));
        INDICATOR.getElements().add(new ClosePath());
        INDICATOR.getStyleClass().add("lcd");
        // CSS_STYLE = control.getLcdDesign().CSS
        INDICATOR.setStyle(CSS_STYLE);
        INDICATOR.setId("lcd-foreground");
        INDICATOR.setStroke(null);

        LCD_THRESHOLD_INDICATOR.getChildren().addAll(INDICATOR);

        return LCD_THRESHOLD_INDICATOR;
    }
}
