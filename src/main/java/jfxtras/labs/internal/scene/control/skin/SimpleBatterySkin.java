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
import jfxtras.labs.scene.control.gauge.SimpleBattery;


/**
 * Created by
 * User: hansolo
 * Date: 30.03.12
 * Time: 09:19
 */
public class SimpleBatterySkin extends SkinBase<SimpleBattery, SimpleBatteryBehavior> {
    private SimpleBattery control;
    private boolean      square;
    private boolean      keepAspect;
    private boolean      isDirty;
    private boolean      initialized;
    private Group        battery;
    private Group        main;
    private Group        foreground;


    // ******************** Constructors **************************************
    public SimpleBatterySkin(final SimpleBattery CONTROL) {
        super(CONTROL, new SimpleBatteryBehavior(CONTROL));
        control     = CONTROL;
        square      = control.isSquare();
        keepAspect  = control.isKeepAspect();
        initialized = false;
        isDirty     = false;
        battery     = new Group();
        main        = new Group();
        foreground  = new Group();

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

        initialized = true;
        paint();
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        getChildren().clear();
        drawBattery();
        drawMain();
        drawForeground();

        getChildren().addAll(battery,
                             main,
                             foreground);
    }

    @Override
    protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);

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
    public final void drawBattery() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = square ? SIZE : control.getPrefWidth();
        final double HEIGHT = square ? SIZE : control.getPrefHeight();
        battery.getChildren().clear();
        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        battery.getChildren().add(IBOUNDS);
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
        //BODY.setId("simple-battery-body");
        final Paint BODY_FILL = new LinearGradient(0.5078125 * WIDTH, 0.28125 * HEIGHT,
                                                   0.5078125 * WIDTH, 0.71875 * HEIGHT,
                                                   false, CycleMethod.NO_CYCLE,
                                                   new Stop(0.0, Color.color(0.8, 0.8, 0.8, 1)),
                                                   new Stop(0.02, Color.color(0.2, 0.2, 0.2, 1)),
                                                   new Stop(0.12, Color.WHITE),
                                                   new Stop(0.73, Color.color(0.2, 0.2, 0.2, 1)),
                                                   new Stop(0.86, Color.color(0.6, 0.6, 0.6, 1)),
                                                   new Stop(1.0, Color.color(0.2, 0.2, 0.2, 1)));
        BODY.setFill(BODY_FILL);
        BODY.setStroke(null);

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
        //CONNECTOR.setId("simple-battery-connector");
        final Paint CONNECTOR_FILL = new LinearGradient(0.9140625 * WIDTH, 0.40625 * HEIGHT,
                                                        0.9140625 * WIDTH, 0.59375 * HEIGHT,
                                                        false, CycleMethod.NO_CYCLE,
                                                        new Stop(0.0, Color.color(0.8, 0.8, 0.8, 1)),
                                                        new Stop(0.02, Color.color(0.2, 0.2, 0.2, 1)),
                                                        new Stop(0.12, Color.WHITE),
                                                        new Stop(0.73, Color.color(0.2, 0.2, 0.2, 1)),
                                                        new Stop(0.86, Color.color(0.6, 0.6, 0.6, 1)),
                                                        new Stop(1.0, Color.color(0.2, 0.2, 0.2, 1)));
        CONNECTOR.setFill(CONNECTOR_FILL);
        CONNECTOR.setStroke(null);

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

        battery.getChildren().addAll(BODY,
                                     CONNECTOR);
    }

    public final void drawMain() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = square ? SIZE : control.getPrefWidth();
        final double HEIGHT = square ? SIZE : control.getPrefHeight();
        main.getChildren().clear();
        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        main.getChildren().add(IBOUNDS);
        final Rectangle FLUID = new Rectangle(0.0703125 * WIDTH, 0.296875 * HEIGHT,
                                               0.7890625 * WIDTH, 0.40625 * HEIGHT);
        FLUID.setArcWidth(0.046875 * WIDTH);
        FLUID.setArcHeight(0.046875 * HEIGHT);
        //_FLUID.setId("simple-battery-fluid");
        final Paint _FLUID_FILL = new LinearGradient(0.46875 * WIDTH, 0.296875 * HEIGHT,
                                                     0.46875 * WIDTH, 0.703125 * HEIGHT,
                                                     false, CycleMethod.NO_CYCLE,
                                                     new Stop(0.0, Color.color(0.1647058824, 0.5450980392, 0, 1)),
                                                     new Stop(0.32, Color.color(0.1647058824, 0.5450980392, 0, 1)),
                                                     new Stop(1.0, Color.color(0.4666666667, 0.8588235294, 0, 1)));
        FLUID.setFill(_FLUID_FILL);
        FLUID.setStroke(null);

        main.getChildren().addAll(FLUID);
    }

    public final void drawForeground() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = square ? SIZE : control.getPrefWidth();
        final double HEIGHT = square ? SIZE : control.getPrefHeight();
        foreground.getChildren().clear();
        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        foreground.getChildren().add(IBOUNDS);
        final Rectangle REFLECTION = new Rectangle(0.0703125 * WIDTH, 0.296875 * HEIGHT,
                                                    0.7890625 * WIDTH, 0.40625 * HEIGHT);
        REFLECTION.setArcWidth(0.046875 * WIDTH);
        REFLECTION.setArcHeight(0.046875 * HEIGHT);
        //_REFLECTION.setId("simple-battery-reflection");
        final Paint _REFLECTION_FILL = new LinearGradient(0.46875 * WIDTH, 0.296875 * HEIGHT,
                                                          0.46875 * WIDTH, 0.703125 * HEIGHT,
                                                          false, CycleMethod.NO_CYCLE,
                                                          new Stop(0.0, Color.color(1, 1, 1, 0)),
                                                          new Stop(0.01, Color.color(1, 1, 1, 0.1960784314)),
                                                          new Stop(0.02, Color.color(1, 1, 1, 0.2980392157)),
                                                          new Stop(0.45, Color.color(1, 1, 1, 0.0156862745)),
                                                          new Stop(0.47, Color.color(1, 1, 1, 0.0392156863)),
                                                          new Stop(0.48, Color.color(1, 1, 1, 0)),
                                                          new Stop(0.71, Color.color(1, 1, 1, 0)),
                                                          new Stop(0.7101, Color.color(1, 1, 1, 0.0039215686)),
                                                          new Stop(0.81, Color.color(1, 1, 1, 0.0941176471)),
                                                          new Stop(0.8101, Color.color(1, 1, 1, 0.1019607843)),
                                                          new Stop(1.0, Color.color(1, 1, 1, 0.4)));
        REFLECTION.setFill(_REFLECTION_FILL);
        REFLECTION.setStroke(null);

        foreground.getChildren().addAll(REFLECTION);
    }

}
