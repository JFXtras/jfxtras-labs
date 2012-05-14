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
import com.sun.javafx.scene.traversal.Direction;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import jfxtras.labs.internal.scene.control.behavior.XYControlBehavior;
import jfxtras.labs.scene.control.gauge.XYControl;



/**
 * Created by
 * User: hansolo
 * Date: 10.05.12
 * Time: 12:55
 */
public class XYControlSkin extends SkinBase<XYControl, XYControlBehavior> {
    private XYControl    control;
    private EventHandler handler;
    private StackPane    area;
    private StackPane    incrementX;
    private StackPane    decrementX;
    private StackPane    incrementY;
    private StackPane    decrementY;
    private StackPane    horSlider;
    private StackPane    horSliderThumb;
    private StackPane    verSlider;
    private StackPane    verSliderThumb;
    private StackPane    reset;
    private StackPane    thumb;
    private double       thumbX;
    private double       thumbY;
    private double       mouseX;
    private double       mouseY;

    private boolean      isDirty;
    private boolean      initialized;


    // ******************** Constructors **************************************
    public XYControlSkin(final XYControl CONTROL) {
        super(CONTROL, new XYControlBehavior(CONTROL));
        control     = CONTROL;
        handler     = new EventHandler<Event>() {
            @Override
            public void handle(final Event EVENT) {
                if (MouseEvent.MOUSE_CLICKED == EVENT.getEventType()) {
                    if (incrementX.equals(EVENT.getSource())) {
                        control.incrementX();
                    } else if (decrementX.equals(EVENT.getSource())) {
                        control.decrementX();
                    } else if (incrementY.equals(EVENT.getSource())) {
                        control.decrementY();
                    } else if (decrementY.equals(EVENT.getSource())) {
                        control.incrementY();
                    } else if (reset.equals(EVENT.getSource())) {
                        control.reset();
                    }
                } else if (MouseEvent.MOUSE_PRESSED == EVENT.getEventType()) {
                    if (thumb.equals(EVENT.getSource())) {
                        mouseX = ((MouseEvent)EVENT).getSceneX();
                        mouseY = ((MouseEvent)EVENT).getSceneY();
                        thumbX = thumb.getLayoutX();
                        thumbY = thumb.getLayoutY();
                    }
                } else if (MouseEvent.MOUSE_DRAGGED == EVENT.getEventType()) {
                    if (thumb.equals(EVENT.getSource())) {
                        thumbX += (((MouseEvent)EVENT).getSceneX() - mouseX) * (control.getSensitivity().STEP_SIZE * 10);
                        thumbY += (((MouseEvent)EVENT).getSceneY() - mouseY) * (control.getSensitivity().STEP_SIZE * 10);

                        if (thumbX > area.getLayoutX() + area.getLayoutBounds().getWidth() - thumb.getLayoutBounds().getWidth()) {
                            thumbX = area.getLayoutX() + area.getLayoutBounds().getWidth() - thumb.getLayoutBounds().getWidth();
                        } else if (thumbX < area.getLayoutX()) {
                            thumbX = area.getLayoutX();
                        }

                        if (thumbY > area.getLayoutY() + area.getLayoutBounds().getHeight() - thumb.getLayoutBounds().getHeight()) {
                            thumbY = area.getLayoutY() + area.getLayoutBounds().getHeight() - thumb.getLayoutBounds().getHeight();
                        } else if (thumbY < area.getLayoutY()) {
                            thumbY = area.getLayoutY();
                        }

                        double x = thumbX / ((area.getLayoutBounds().getWidth() - thumb.getLayoutBounds().getWidth()) / 2) - 1;
                        double y = (thumbY / (-(area.getLayoutBounds().getHeight() - thumb.getLayoutBounds().getHeight()) / 2) + 1) * (-1);

                        control.setXValue(x);
                        control.setYValue(y);

                        mouseX = ((MouseEvent)EVENT).getSceneX();
                        mouseY = ((MouseEvent)EVENT).getSceneY();
                    }
                } else if (MouseEvent.MOUSE_RELEASED == EVENT.getEventType()) {
                    if (thumb.equals(EVENT.getSource())) {
                    }
                } else if (ScrollEvent.SCROLL == EVENT.getEventType()) {
                    if (((ScrollEvent) EVENT).getDeltaY() < -20) {
                        switch(control.getSensitivity()) {
                            case COARSE:

                                break;
                            case MEDIUM:
                                control.setSensitivity(XYControl.Sensitivity.COARSE);
                                break;
                            case FINE:
                                control.setSensitivity(XYControl.Sensitivity.MEDIUM);
                                break;
                        }
                    } else if (((ScrollEvent) EVENT).getDeltaY() > 20) {
                        switch(control.getSensitivity()) {
                            case COARSE:
                                control.setSensitivity(XYControl.Sensitivity.MEDIUM);
                                break;
                            case MEDIUM:
                                control.setSensitivity(XYControl.Sensitivity.FINE);
                                break;
                            case FINE:

                                break;
                        }
                    }
                }
            }
        };
        area           = new StackPane();
        incrementX     = new StackPane();
        decrementX     = new StackPane();
        incrementY     = new StackPane();
        decrementY     = new StackPane();
        horSlider      = new StackPane();
        horSliderThumb = new StackPane();
        verSlider      = new StackPane();
        verSliderThumb = new StackPane();
        reset          = new StackPane();
        thumb          = new StackPane();
        thumbX         = 95;
        thumbY         = 95;
        mouseX         = 0;
        mouseY         = 0;
        initialized    = false;
        isDirty        = false;

        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(220, 220);
        }

        control.prefWidthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });

        control.prefHeightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                isDirty = true;
            }
        });

        // Register listeners
        registerChangeListener(control.xValueProperty(), "X");
        registerChangeListener(control.yValueProperty(), "Y");
        registerChangeListener(control.sensitivityProperty(), "SENSITIVITY");
        incrementX.addEventFilter(MouseEvent.MOUSE_CLICKED, handler);
        decrementX.addEventFilter(MouseEvent.MOUSE_CLICKED, handler);
        incrementY.addEventFilter(MouseEvent.MOUSE_CLICKED, handler);
        decrementY.addEventFilter(MouseEvent.MOUSE_CLICKED, handler);
        reset.addEventFilter(MouseEvent.MOUSE_CLICKED, handler);
        thumb.addEventFilter(MouseEvent.MOUSE_PRESSED, handler);
        thumb.addEventFilter(MouseEvent.MOUSE_DRAGGED, handler);
        thumb.addEventFilter(MouseEvent.MOUSE_RELEASED, handler);
        addEventFilter(ScrollEvent.SCROLL, handler);

        initialized = true;
        paint();
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        drawControl();
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if ("X".equals(PROPERTY)) {
            double areaWidth = area.getLayoutBounds().getWidth() - thumb.getLayoutBounds().getWidth();
            double posX = control.getXValue() * areaWidth / 2 + areaWidth / 2;
            thumb.setLayoutX(posX);
            horSliderThumb.setLayoutX(control.getXValue() * horSlider.getLayoutBounds().getWidth() / 2 + horSlider.getLayoutBounds().getWidth() / 2 - horSliderThumb.getLayoutBounds().getWidth() / 2 + decrementX.getLayoutBounds().getWidth());
        } else if ("Y".equals(PROPERTY)) {
            double areaHeight = area.getLayoutBounds().getHeight() - thumb.getLayoutBounds().getHeight();
            double posY = control.getYValue() * areaHeight / 2 + areaHeight / 2;
            thumb.setLayoutY(posY);
            verSliderThumb.setLayoutY(control.getYValue() * verSlider.getLayoutBounds().getHeight() / 2 + verSlider.getLayoutBounds().getHeight() / 2 - verSliderThumb.getLayoutBounds().getHeight() / 2 + incrementY.getLayoutBounds().getHeight());
        } else if ("STEP_SIZE".equals(PROPERTY)) {

        } else if ("SENSITIVITY".equals(PROPERTY)) {
            switch (control.getSensitivity()) {
                case COARSE:
                    thumb.setStyle("-fx-sensitivity-color: red;");
                    horSliderThumb.setStyle("-fx-sensitivity-color: red;");
                    verSliderThumb.setStyle("-fx-sensitivity-color: red;");
                    break;
                case MEDIUM:
                    thumb.setStyle("-fx-sensitivity-color: rgb(255, 191, 0);");
                    horSliderThumb.setStyle("-fx-sensitivity-color: rgb(255, 191, 0);");
                    verSliderThumb.setStyle("-fx-sensitivity-color: rgb(255, 191, 0);");
                    break;
                case FINE:
                    thumb.setStyle("-fx-sensitivity-color: green;");
                    horSliderThumb.setStyle("-fx-sensitivity-color: green;");
                    verSliderThumb.setStyle("-fx-sensitivity-color: green;");
                    break;
            }
        }
    }

    @Override public void layoutChildren() {
        if (isDirty) {
            paint();
            isDirty = false;
        }
        super.layoutChildren();
    }

    @Override public final XYControl getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 220;
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 220;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }


    // ******************** Drawing related ***********************************
    private void drawControl() {
        getChildren().clear();

        Pane pane = new Pane();

        //area.getStyleClass().setAll("xy-area");
        Rectangle background = new Rectangle(0, 0, 200, 200);
        background.setFill(new LinearGradient(0, background.getLayoutBounds().getMinY(), 0, background.getLayoutBounds().getMaxY(),
                                              false, CycleMethod.NO_CYCLE,
                                              new Stop(0.0, Color.rgb(200, 200, 200)),
                                              new Stop(1.0, Color.rgb(180, 180, 180))));
        Line xAxis = new Line(0, 100, 200, 100);
        xAxis.setFill(Color.rgb(80, 80, 80));
        Line yAxis = new Line(100, 0, 100, 200);
        yAxis.setFill(Color.rgb(80, 80, 80));
        area.getChildren().addAll(background, xAxis, yAxis);
        area.relocate(0, 0);
        pane.getChildren().add(area);

        decrementX.getStyleClass().setAll("xy-button");
        decrementX.relocate(0, 200);
        Group arrowDecX = createArrow(Direction.LEFT, 20);
        decrementX.getChildren().add(arrowDecX);
        StackPane.setAlignment(arrowDecX, Pos.CENTER);
        StackPane.setMargin(arrowDecX, new Insets(0, 0, 0, 0));
        pane.getChildren().add(decrementX);

        horSlider.getStyleClass().setAll("xy-slider-horizontal");
        horSlider.relocate(20, 200);
        pane.getChildren().add(horSlider);

        horSliderThumb.getStyleClass().setAll("xy-slider-horizontal-thumb");
        horSliderThumb.relocate(98, 200);
        pane.getChildren().add(horSliderThumb);

        incrementX.getStyleClass().setAll("xy-button");
        incrementX.relocate(180, 200);
        Group arrowIncX = createArrow(Direction.RIGHT, 20);
        incrementX.getChildren().add(arrowIncX);
        StackPane.setAlignment(arrowIncX, Pos.CENTER);
        StackPane.setMargin(arrowIncX, new Insets(0, 0, 0, 0));
        pane.getChildren().add(incrementX);

        incrementY.getStyleClass().setAll("xy-button");
        incrementY.relocate(200, 0);
        Group arrowIncY = createArrow(Direction.UP, 20);
        incrementY.getChildren().add(arrowIncY);
        StackPane.setAlignment(arrowIncY, Pos.CENTER);
        StackPane.setMargin(arrowIncY, new Insets(0, 0, 0, 0));
        pane.getChildren().add(incrementY);

        verSlider.getStyleClass().setAll("xy-slider-vertical");
        verSlider.relocate(200, 20);
        pane.getChildren().add(verSlider);

        verSliderThumb.getStyleClass().setAll("xy-slider-vertical-thumb");
        verSliderThumb.relocate(200, 98);
        pane.getChildren().add(verSliderThumb);

        decrementY.getStyleClass().setAll("xy-button");
        decrementY.relocate(200, 180);
        Group arrowDecY = createArrow(Direction.DOWN, 20);
        decrementY.getChildren().add(arrowDecY);
        StackPane.setAlignment(arrowDecY, Pos.CENTER);
        StackPane.setMargin(arrowDecY, new Insets(0, 0, 0, 0));
        pane.getChildren().add(decrementY);

        reset.getStyleClass().setAll("xy-button");
        reset.relocate(200, 200);
        final Group RESET_GROUP = new Group();
        final Rectangle IBOUNDS = new Rectangle(20, 20);
        IBOUNDS.setOpacity(0.0);
        final Ellipse ZERO = new Ellipse(10, 10, 4, 5);
        ZERO.setStrokeWidth(2.0);
        ZERO.getStyleClass().add("zero");
        RESET_GROUP.getChildren().addAll(IBOUNDS, ZERO);
        reset.getChildren().add(RESET_GROUP);
        pane.getChildren().add(reset);

        thumb.getStyleClass().setAll("xy-thumb");
        thumb.relocate(95, 95);
        pane.getChildren().add(thumb);

        getChildren().addAll(pane);
    }

    private Group createArrow(final Direction DIRECTION, final double SIZE) {
        final double WIDTH = SIZE;
        final double HEIGHT = SIZE;

        final Rectangle IBOUNDS = new Rectangle(SIZE, SIZE);
        IBOUNDS.setOpacity(0.0);

        final Group ARROW = new Group();
        final Path PATH = new Path();
        PATH.setFillRule(FillRule.EVEN_ODD);
        switch(DIRECTION) {
            case UP:
                PATH.getElements().add(new MoveTo(0.25 * WIDTH, 0.75 * HEIGHT));
                PATH.getElements().add(new LineTo(0.5 * WIDTH, 0.25 * HEIGHT));
                PATH.getElements().add(new LineTo(0.75 * WIDTH, 0.75 * HEIGHT));
                PATH.getElements().add(new LineTo(0.25 * WIDTH, 0.75 * HEIGHT));
                PATH.getElements().add(new ClosePath());
                break;
            case RIGHT:
                PATH.getElements().add(new MoveTo(0.25 * WIDTH, 0.25 * HEIGHT));
                PATH.getElements().add(new LineTo(0.75 * WIDTH, 0.5 * HEIGHT));
                PATH.getElements().add(new LineTo(0.25 * WIDTH, 0.75 * HEIGHT));
                PATH.getElements().add(new LineTo(0.25 * WIDTH, 0.25 * HEIGHT));
                PATH.getElements().add(new ClosePath());
                break;
            case DOWN:
                PATH.getElements().add(new MoveTo(0.25 * WIDTH, 0.25 * HEIGHT));
                PATH.getElements().add(new LineTo(0.5 * WIDTH, 0.75 * HEIGHT));
                PATH.getElements().add(new LineTo(0.75 * WIDTH, 0.25 * HEIGHT));
                PATH.getElements().add(new LineTo(0.25 * WIDTH, 0.25 * HEIGHT));
                PATH.getElements().add(new ClosePath());
                break;
            case LEFT:
                PATH.getElements().add(new MoveTo(0.75 * WIDTH, 0.25 * HEIGHT));
                PATH.getElements().add(new LineTo(0.25 * WIDTH, 0.5 * HEIGHT));
                PATH.getElements().add(new LineTo(0.75 * WIDTH, 0.75 * HEIGHT));
                PATH.getElements().add(new LineTo(0.75 * WIDTH, 0.25 * HEIGHT));
                PATH.getElements().add(new ClosePath());
                break;
        }
        PATH.getStyleClass().add("arrow");
        ARROW.getChildren().addAll(IBOUNDS, PATH);
        return ARROW;
    }

}
