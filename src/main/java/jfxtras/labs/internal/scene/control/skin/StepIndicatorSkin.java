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
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import jfxtras.labs.internal.scene.control.behavior.StepIndicatorBehavior;
import jfxtras.labs.scene.control.gauge.StepIndicator;
import jfxtras.labs.util.Util;


/**
 * Created by
 * User: hansolo
 * Date: 21.03.12
 * Time: 11:52
 */
public class StepIndicatorSkin extends SkinBase<StepIndicator, StepIndicatorBehavior> {
    private StepIndicator control;
    private boolean       isDirty;
    private boolean       initialized;
    private int           noOfCircles;
    private Group         circles;
    private Group         selectedCircles;


    // ******************** Constructors **************************************
    public StepIndicatorSkin(final StepIndicator CONTROL) {
        super(CONTROL, new StepIndicatorBehavior(CONTROL));
        control               = CONTROL;
        initialized           = false;
        isDirty               = false;
        noOfCircles           = control.getNoOfSteps();
        circles               = new Group();
        selectedCircles       = new Group();
        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(noOfCircles * 60 + (noOfCircles - 1) * 20, 60);
        }

        // Register listeners
        registerChangeListener(control.prefWidthProperty(), "PREF_WIDTH");
        registerChangeListener(control.prefHeightProperty(), "PREF_HEIGHT");
        registerChangeListener(control.colorProperty(), "COLOR");
        registerChangeListener(control.noOfStepsProperty(), "NO_OF_CIRCLES");
        registerChangeListener(control.currentStepProperty(), "SELECTION");

        initialized = true;
        repaint();
    }


    // ******************** Methods *******************************************
    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("NO_OF_CIRCLES".equals(PROPERTY)) {
            noOfCircles = control.getNoOfSteps();
            repaint();
        } else if ("SELECTION".equals(PROPERTY)) {
            drawSelectedCircles();
        } else if ("COLOR".equals(PROPERTY)) {
            drawSelectedCircles();
        } else if ("PREF_WIDTH".equals(PROPERTY)) {
            repaint();
        } else if ("PREF_HEIGHT".equals(PROPERTY)) {
            repaint();
        }
    }

    public final void repaint() {
        isDirty = true;
        repaint();
    }

    @Override public void layoutChildren() {
        if (!isDirty) {
            return;
        }
        if (!initialized) {
            init();
        }
        if (control.getScene() != null) {
            getChildren().clear();
            drawCircles();
            drawSelectedCircles();
            getChildren().addAll(circles, selectedCircles);
        }
        isDirty = false;

        super.layoutChildren();
    }

    @Override public final StepIndicator getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 200;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 60;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }


    // ******************** Mouse event handling ******************************
    private void addMouseEventListener(final Shape CIRCLE, final int INDEX) {
        CIRCLE.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent EVENT) {
                control.setSelectedStep(INDEX);
            }
        });

        CIRCLE.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent EVENT) {
                control.setSelectedStep(-1);
            }
        });
    }


    // ******************** Drawing related ***********************************
    private final void drawCircles() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        circles.getChildren().clear();

        circles.setStyle("-fx-step-indicator-selected-inner-frame-fill: " + Util.createCssColor(control.getColor().darker()) +
                         "-fx-step-indicator-selected-inner-background-fill: " + Util.createCssColor(control.getColor()) +
                         "-fx-step-indicator-selected-text-fill: " + Util.createCssColor(control.getColor().darker().darker()) +
                         "-fx-step-indicator-inner-frame-fill: rgb(158, 158, 158);" +
                         "-fx-step-indicator-inner-background-fill: rgb(244, 244, 244);" +
                         "-fx-step-indicator-stroke: transparent");

        // Create shadows
        final InnerShadow OUTER_BACKGROUND_INNER_SHADOW = new InnerShadow();
        OUTER_BACKGROUND_INNER_SHADOW.setWidth(0.1933333333 * HEIGHT);
        OUTER_BACKGROUND_INNER_SHADOW.setHeight(0.1933333333 * HEIGHT);
        OUTER_BACKGROUND_INNER_SHADOW.setOffsetX(0.0);
        OUTER_BACKGROUND_INNER_SHADOW.setOffsetY(0.04 * SIZE);
        OUTER_BACKGROUND_INNER_SHADOW.setRadius(0.1933333333 * HEIGHT);
        OUTER_BACKGROUND_INNER_SHADOW.setColor(Color.color(0.6, 0.6, 0.6, 0.65));
        OUTER_BACKGROUND_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        OUTER_BACKGROUND_INNER_SHADOW.inputProperty().set(null);

        final DropShadow INNER_FRAME_DROP_SHADOW = new DropShadow();
        INNER_FRAME_DROP_SHADOW.setWidth(0.048 * HEIGHT);
        INNER_FRAME_DROP_SHADOW.setHeight(0.048 * HEIGHT);
        INNER_FRAME_DROP_SHADOW.setOffsetX(0.0);
        INNER_FRAME_DROP_SHADOW.setOffsetY(0.02 * SIZE);
        INNER_FRAME_DROP_SHADOW.setRadius(0.048 * HEIGHT);
        INNER_FRAME_DROP_SHADOW.setColor(Color.color(0.4, 0.4, 0.4, 0.65));
        INNER_FRAME_DROP_SHADOW.setBlurType(BlurType.GAUSSIAN);
        INNER_FRAME_DROP_SHADOW.inputProperty().set(null);

        final InnerShadow INNER_BACKGROUND_INNER_SHADOW = new InnerShadow();
        INNER_BACKGROUND_INNER_SHADOW.setWidth(0.092 * HEIGHT);
        INNER_BACKGROUND_INNER_SHADOW.setHeight(0.092 * HEIGHT);
        INNER_BACKGROUND_INNER_SHADOW.setOffsetX(0.0);
        INNER_BACKGROUND_INNER_SHADOW.setOffsetY(0.0);
        INNER_BACKGROUND_INNER_SHADOW.setRadius(0.092 * HEIGHT);
        INNER_BACKGROUND_INNER_SHADOW.setColor(Color.WHITE);
        INNER_BACKGROUND_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        INNER_BACKGROUND_INNER_SHADOW.inputProperty().set(null);

        // Create outer circles and connectors
        final double STEP_SIZE = 1.3333333333 * WIDTH;
        Shape outerFrame = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.5 * WIDTH);
        Shape outerBackground = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.48333333333333334 * WIDTH);
        for (int i = 1 ; i < control.getNoOfSteps() ; i++) {
            final Rectangle FRAME_CONNECTOR = new Rectangle(0.5 * WIDTH + ((i - 1) * STEP_SIZE), 0.3166666667 * HEIGHT, 1.3333333333 * WIDTH, 0.3666666667 * HEIGHT);
            outerFrame = Shape.union(outerFrame, FRAME_CONNECTOR);

            final Circle OUTER_FRAME = new Circle(0.5 * WIDTH + (i * STEP_SIZE), 0.5 * HEIGHT, 0.5 * WIDTH);
            outerFrame = Shape.union(outerFrame, OUTER_FRAME);

            final Rectangle BACKGROUND_CONNECTOR = new Rectangle(0.5 * WIDTH + ((i - 1) * STEP_SIZE), 0.3333333333 * HEIGHT, 1.3333333333 * WIDTH, 0.3333333333 * HEIGHT);
            outerBackground = Shape.union(outerBackground, BACKGROUND_CONNECTOR);

            final Circle OUTER_BACKGROUND = new Circle(0.5 * WIDTH + (i * STEP_SIZE), 0.5 * HEIGHT, 0.48333333333333334 * WIDTH);
            outerBackground = Shape.union(outerBackground, OUTER_BACKGROUND);
        }
        outerFrame.getStyleClass().add("step-indicator-outer-frame");
        outerBackground.getStyleClass().add("step-indicator-outer-background");

        // Because the css styling does not work on shapes that have been create via Shape.union or Shape.subtract
        // we need to set the fill with code. This might change in the future
        outerFrame.setFill(Color.rgb(201, 201, 201));
        outerBackground.setFill(Color.WHITE);

        // Add effect
        outerBackground.setEffect(OUTER_BACKGROUND_INNER_SHADOW);

        // Create inner circles and connectors
        Shape innerFrame = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4 * WIDTH);
        Shape innerBackground = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.38333333333333336 * WIDTH);
        for (int i = 1 ; i < control.getNoOfSteps() ; i++) {
            if (i != 0) {
                final Rectangle FRAME_CONNECTOR = new Rectangle(0.5 * WIDTH + ((i - 1) * STEP_SIZE), 0.4333333333 * HEIGHT, 1.3333333333 * WIDTH, 0.1333333333 * HEIGHT);
                innerFrame = Shape.union(innerFrame, FRAME_CONNECTOR);
            }
            final Circle INNER_FRAME = new Circle(0.5 * WIDTH + (i * STEP_SIZE), 0.5 * HEIGHT, 0.4 * WIDTH);
            innerFrame = Shape.union(innerFrame, INNER_FRAME);
            if (i != 0) {
                final Rectangle BACKGROUND_CONNECTOR = new Rectangle(0.5 * WIDTH + ((i - 1) * STEP_SIZE), 0.45 * HEIGHT, 1.3333333333 * WIDTH, 0.1 * HEIGHT);
                innerBackground = Shape.union(innerBackground, BACKGROUND_CONNECTOR);
            }
            final Circle INNER_BACKGROUND = new Circle(0.5 * WIDTH + (i * STEP_SIZE), 0.5 * HEIGHT, 0.38333333333333336 * WIDTH);
            innerBackground = Shape.union(innerBackground, INNER_BACKGROUND);
        }
        innerFrame.getStyleClass().add("step-indicator-inner-frame");
        innerBackground.getStyleClass().add("step-indicator-inner-background");

        // Because the css styling does not work on shapes that have been create via Shape.union or Shape.subtract
        // we need to set the fill with code. This might change in the future
        innerFrame.setFill(Color.rgb(158, 158, 158));
        innerBackground.setFill(Color.rgb(244, 244, 244));

        // Add effects
        innerFrame.setEffect(INNER_FRAME_DROP_SHADOW);
        innerBackground.setEffect(INNER_BACKGROUND_INNER_SHADOW);

        circles.getChildren().addAll(outerFrame, outerBackground, innerFrame, innerBackground);
        circles.setCache(true);
    }

    private final void drawSelectedCircles() {
        final double SIZE = control.getPrefWidth() < control.getPrefHeight() ? control.getPrefWidth() : control.getPrefHeight();
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        selectedCircles.getChildren().clear();

        final Rectangle IBOUNDS = new Rectangle(0, 0, control.getNoOfSteps() * WIDTH + (control.getNoOfSteps() - 1) * 0.3333333333 * HEIGHT, HEIGHT);
        IBOUNDS.setOpacity(0.0);
        selectedCircles.getChildren().add(IBOUNDS);

        selectedCircles.setStyle("-fx-step-indicator-selected-inner-frame-fill: " + Util.createCssColor(control.getColor().darker()) +
                                 "-fx-step-indicator-selected-inner-background-fill: " + Util.createCssColor(control.getColor()) +
                                 "-fx-step-indicator-selected-text-fill: " + Util.createCssColor(control.getColor().darker().darker()) +
                                 "-fx-step-indicator-inner-frame-fill: rgb(158, 158, 158);" +
                                 "-fx-step-indicator-inner-background-fill: rgb(244, 244, 244);" +
                                 "-fx-step-indicator-stroke: transparent");

        // Create shadow
        final InnerShadow INNER_BACKGROUND_INNER_SHADOW = new InnerShadow();
        INNER_BACKGROUND_INNER_SHADOW.setWidth(0.092 * HEIGHT);
        INNER_BACKGROUND_INNER_SHADOW.setHeight(0.092 * HEIGHT);
        INNER_BACKGROUND_INNER_SHADOW.setOffsetX(0.0);
        INNER_BACKGROUND_INNER_SHADOW.setOffsetY(0.0);
        INNER_BACKGROUND_INNER_SHADOW.setRadius(0.092 * HEIGHT);
        INNER_BACKGROUND_INNER_SHADOW.setColor(Color.WHITE);
        INNER_BACKGROUND_INNER_SHADOW.setBlurType(BlurType.GAUSSIAN);
        INNER_BACKGROUND_INNER_SHADOW.inputProperty().set(null);

        // Create selected inner circles and connectors
        final double STEP_SIZE = 1.3333333333 * WIDTH;
        Shape innerFrameSelected = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.4 * WIDTH);
        Shape innerBackgroundSelected = new Circle(0.5 * WIDTH, 0.5 * HEIGHT, 0.38333333333333336 * WIDTH);
        for (int i = 1 ; i < control.getCurrentStep() ; i++) {
            final Rectangle FRAME_CONNECTOR = new Rectangle(0.5 * WIDTH + ((i - 1) * STEP_SIZE), 0.4333333333 * HEIGHT, 1.3333333333 * WIDTH, 0.1333333333 * HEIGHT);
            innerFrameSelected = Shape.union(innerFrameSelected, FRAME_CONNECTOR);

            final Circle INNER_FRAME = new Circle(0.5 * WIDTH + (i * STEP_SIZE), 0.5 * HEIGHT, 0.4 * WIDTH);
            innerFrameSelected = Shape.union(innerFrameSelected, INNER_FRAME);

            final Rectangle BACKGROUND_CONNECTOR = new Rectangle(0.5 * WIDTH + ((i - 1) * STEP_SIZE), 0.45 * HEIGHT, 1.3333333333 * WIDTH, 0.1 * HEIGHT);
            innerBackgroundSelected = Shape.union(innerBackgroundSelected, BACKGROUND_CONNECTOR);

            final Circle INNER_BACKGROUND = new Circle(0.5 * WIDTH + (i * STEP_SIZE), 0.5 * HEIGHT, 0.38333333333333336 * WIDTH);
            innerBackgroundSelected = Shape.union(innerBackgroundSelected, INNER_BACKGROUND);
        }
        innerFrameSelected.getStyleClass().add("step-indicator-inner-frame");
        innerBackgroundSelected.getStyleClass().add("step-indicator-inner-background");

        // Because the css styling does not work on shapes that have been create via Shape.union or Shape.subtract
        // we need to set the fill with code. This might change in the future
        innerFrameSelected.setFill(control.getColor().darker());
        innerBackgroundSelected.setFill(control.getColor());

        // Add effects
        innerBackgroundSelected.setEffect(INNER_BACKGROUND_INNER_SHADOW);

        if (control.getCurrentStep() == 0) {
            innerFrameSelected.setVisible(false);
            innerBackgroundSelected.setVisible(false);
        }
        selectedCircles.getChildren().addAll(innerFrameSelected, innerBackgroundSelected);

        // Add numbers
        final Font FONT = Font.font("Arial", FontWeight.BOLD, 0.4 * HEIGHT);
        for (int i = 0 ; i < control.getNoOfSteps() ; i++) {
            final Text NUMBER = new Text(Integer.toString(i + 1));
            NUMBER.setTextOrigin(VPos.CENTER);
            NUMBER.setTextAlignment(TextAlignment.CENTER);
            NUMBER.setFontSmoothingType(FontSmoothingType.LCD);
            NUMBER.setFont(FONT);
            if (i < control.getCurrentStep()) {
                NUMBER.getStyleClass().add("step-indicator-selected-text");
            } else {
                NUMBER.getStyleClass().add("step-indicator-text");
            }
            NUMBER.setTranslateX((WIDTH - NUMBER.getLayoutBounds().getWidth()) / 2.0 + (i * STEP_SIZE));
            NUMBER.setTranslateY(0.5 * HEIGHT);
            selectedCircles.getChildren().add(NUMBER);
        }

        // Add mouse event listener for completed steps
        for (int i = 0 ; i < control.getCurrentStep() ; i++) {
            final Circle HOT_SPOT = new Circle(0.5 * WIDTH + (i * STEP_SIZE), 0.5 * HEIGHT, 0.4 * WIDTH);
            HOT_SPOT.setFill(Color.TRANSPARENT);
            HOT_SPOT.setStroke(Color.TRANSPARENT);
            selectedCircles.getChildren().add(HOT_SPOT);
            addMouseEventListener(HOT_SPOT, i + 1);
        }


        selectedCircles.setCache(true);
    }
}
