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
import javafx.scene.effect.BlurType;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.DropShadow;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import jfxtras.labs.internal.scene.control.behavior.SimpleIndicatorBehavior;
import jfxtras.labs.scene.control.gauge.SimpleIndicator;
import jfxtras.labs.scene.control.gauge.Util;


/**
 * Created by
 * User: hansolo
 * Date: 06.03.12
 * Time: 13:53
 */
public class SimpleIndicatorSkin extends SkinBase<SimpleIndicator, SimpleIndicatorBehavior> {
    private SimpleIndicator control;
    private boolean         isDirty;
    private boolean         initialized;
    private Group           indicator;
    private Circle          main;
    private DropShadow      mainGlow;


    // ******************** Constructors **************************************
    public SimpleIndicatorSkin(final SimpleIndicator CONTROL) {
        super(CONTROL, new SimpleIndicatorBehavior(CONTROL));
        control     = CONTROL;
        initialized = false;
        isDirty     = false;
        indicator   = new Group();

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(50, 50);
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
        registerChangeListener(control.innerColorProperty(), "INNER_COLOR");
        registerChangeListener(control.outerColorProperty(), "OUTER_COLOR");
        registerChangeListener(control.glowVisibleProperty(), "GLOW_VISIBILITY");

        initialized = true;
        paint();
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        getChildren().clear();
        drawIndicator();

        getChildren().addAll(indicator);
    }

    @Override
    protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if (PROPERTY == "INNER_COLOR") {
            main.setStyle("-fx-indicator-inner-color: " + Util.INSTANCE.createCssColor(control.getInnerColor()));
            main.setId("indicator-main-fill");
            mainGlow.setColor(control.getInnerColor());
        } else if (PROPERTY == "OUTER_COLOR") {
            main.setStyle("-fx-indicator-outer-color: " + Util.INSTANCE.createCssColor(control.getOuterColor()));
            main.setId("indicator-main-fill");
        } else if (PROPERTY == "GLOW_VISIBILITY") {
            paint();
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
    public final SimpleIndicator getSkinnable() {
        return control;
    }

    @Override
    public final void dispose() {
        control = null;
    }

    @Override
    protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 250;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override
    protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 250;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }


    // ******************** Drawing related ***********************************
    public final void drawIndicator() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        indicator.setStyle("-fx-indicator-inner-color: " + Util.INSTANCE.createCssColor(control.getInnerColor()) +
                           "-fx-indicator-outer-color: " + Util.INSTANCE.createCssColor(control.getOuterColor()));

        indicator.getChildren().clear();

        final Shape IBOUNDS = new Rectangle(0, 0, WIDTH, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        indicator.getChildren().add(IBOUNDS);

        final Circle OUTER_FRAME = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.496 * WIDTH);
        OUTER_FRAME.setId("indicator-outer-frame-fill");
        OUTER_FRAME.setStroke(null);

        final InnerShadow OUTER_FRAME_INNER_SHADOW = new InnerShadow();
        OUTER_FRAME_INNER_SHADOW.setWidth(0.0432 * OUTER_FRAME.getLayoutBounds().getWidth());
        OUTER_FRAME_INNER_SHADOW.setHeight(0.0432 * OUTER_FRAME.getLayoutBounds().getHeight());
        OUTER_FRAME_INNER_SHADOW.setOffsetX(0.0);
        OUTER_FRAME_INNER_SHADOW.setOffsetY(0.0);
        OUTER_FRAME_INNER_SHADOW.setRadius(0.0432 * OUTER_FRAME.getLayoutBounds().getWidth());
        OUTER_FRAME_INNER_SHADOW.setColor(Color.color(0, 0, 0, 0.6470588235));
        OUTER_FRAME_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        OUTER_FRAME_INNER_SHADOW.inputProperty().set(null);
        OUTER_FRAME.setEffect(OUTER_FRAME_INNER_SHADOW);

        final Circle INNER_FRAME = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4 * WIDTH);
        INNER_FRAME.setId("indicator-inner-frame-fill");
        INNER_FRAME.setStroke(null);

        final DropShadow INNER_FRAME_DROP_SHADOW = new DropShadow();
        INNER_FRAME_DROP_SHADOW.setWidth(0.05760 * INNER_FRAME.getLayoutBounds().getWidth());
        INNER_FRAME_DROP_SHADOW.setHeight(0.05760 * INNER_FRAME.getLayoutBounds().getHeight());
        INNER_FRAME_DROP_SHADOW.setOffsetX(0.0);
        INNER_FRAME_DROP_SHADOW.setOffsetY(0.0);
        INNER_FRAME_DROP_SHADOW.setRadius(0.05760 * INNER_FRAME.getLayoutBounds().getWidth());
        INNER_FRAME_DROP_SHADOW.setColor(Color.color(0.2, 0.2, 0.2, 1));
        INNER_FRAME_DROP_SHADOW.setBlurType(BlurType.GAUSSIAN);
        INNER_FRAME_DROP_SHADOW.inputProperty().set(null);
        INNER_FRAME.setEffect(INNER_FRAME_DROP_SHADOW);

        main = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.38 * WIDTH);
        main.setId("indicator-main-fill");
        main.setStroke(null);

        final InnerShadow MAIN_INNER_SHADOW = new InnerShadow();
        MAIN_INNER_SHADOW.setWidth(0.2880 * main.getLayoutBounds().getWidth());
        MAIN_INNER_SHADOW.setHeight(0.2880 * main.getLayoutBounds().getHeight());
        MAIN_INNER_SHADOW.setOffsetX(0.0);
        MAIN_INNER_SHADOW.setOffsetY(0.0);
        MAIN_INNER_SHADOW.setRadius(0.2880 * main.getLayoutBounds().getWidth());
        MAIN_INNER_SHADOW.setColor(Color.BLACK);
        MAIN_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);

        mainGlow = new DropShadow();
        mainGlow.setWidth(0.2880 * main.getLayoutBounds().getWidth());
        mainGlow.setHeight(0.2880 * main.getLayoutBounds().getHeight());
        mainGlow.setOffsetX(0.0);
        mainGlow.setOffsetY(0.0);
        mainGlow.setRadius(0.2880 * main.getLayoutBounds().getWidth());
        mainGlow.setColor(control.getInnerColor());
        mainGlow.setBlurType(BlurType.GAUSSIAN);
        mainGlow.inputProperty().set(MAIN_INNER_SHADOW);
        if (control.isGlowVisible()) {
            main.setEffect(mainGlow);
        } else {
            main.setEffect(MAIN_INNER_SHADOW);
        }



        final Ellipse HIGHLIGHT = new Ellipse(0.504 * WIDTH, 0.294 * HEIGHT,
                                              0.26 * WIDTH, 0.15 * HEIGHT);
        HIGHLIGHT.setId("indicator-highlight-fill");
        HIGHLIGHT.setStroke(null);

        indicator.getChildren().addAll(OUTER_FRAME,
                                       INNER_FRAME,
                                       main,
                                       HIGHLIGHT);
    }

}

