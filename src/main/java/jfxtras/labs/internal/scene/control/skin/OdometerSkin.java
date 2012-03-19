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
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.FillRule;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import jfxtras.labs.internal.scene.control.behavior.OdometerBehavior;
import jfxtras.labs.scene.control.gauge.Odometer;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by
 * User: hansolo
 * Date: 16.03.12
 * Time: 15:24
 */
public class OdometerSkin extends SkinBase<Odometer, OdometerBehavior> {
    private Odometer   control;
    private boolean    isDirty;
    private boolean    initialized;
    private Group      foreground;
    private List<Dial> listOfDials;
    private Group      background;
    private Font       font;


    // ******************** Constructors **************************************
    public OdometerSkin(final Odometer CONTROL) {
        super(CONTROL, new OdometerBehavior(CONTROL));
        control     = CONTROL;
        initialized = false;
        isDirty     = false;
        foreground  = new Group();
        listOfDials = new LinkedList<Dial>();
        background  = new Group();
        init();
    }

    private void init() {
        if (control.getPrefWidth() < 0 | control.getPrefHeight() < 0) {
            control.setPrefSize(0.68 * 40 * (control.getNoOfDigits() + control.getNoOfDecimals()), 40);
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
        registerChangeListener(control.rotationsProperty(), "ROTATION");

        initialized = true;
        paint();
    }


    // ******************** Methods *******************************************
    public final void paint() {
        if (!initialized) {
            init();
        }
        font = Font.loadFont(getClass().getResourceAsStream("/jfxtras/labs/scene/control/gauge/droidsansmono.ttf"), (0.85 * control.getPrefHeight()));
        setClip(new Rectangle(0, 1, control.getPrefWidth(), control.getPrefHeight()));
        getChildren().clear();
        drawBackground();
        setupDials();
        drawForeground();
        getChildren().add(background);
        for (Dial dial : listOfDials) {
            getChildren().addAll(dial.getNextNumberGroup(), dial.getCurrentNumberGroup());
        }
        getChildren().add(foreground);
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if (PROPERTY == "ROTATION") {
            for (int i = 1 ; i < (control.getNoOfDigits() + control.getNoOfDecimals() + 1) ; i++) {
                if (control.getRotations() == 0) {
                    listOfDials.get(i - 1).reset();
                } else {
                    listOfDials.get(i - 1).setNumber(control.getDialPosition(i));
                }
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

    @Override public final Odometer getSkinnable() {
        return control;
    }

    @Override public final void dispose() {
        control = null;
    }

    @Override protected double computePrefWidth(final double PREF_WIDTH) {
        double prefWidth = 0.68 * control.getPrefHeight() * (control.getNoOfDigits() + control.getNoOfDecimals());
        if (PREF_WIDTH != -1) {
            prefWidth = Math.max(0, PREF_WIDTH - getInsets().getLeft() - getInsets().getRight());
        }
        return super.computePrefWidth(prefWidth);
    }

    @Override protected double computePrefHeight(final double PREF_HEIGHT) {
        double prefHeight = 40;
        if (PREF_HEIGHT != -1) {
            prefHeight = Math.max(0, PREF_HEIGHT - getInsets().getTop() - getInsets().getBottom());
        }
        return super.computePrefWidth(prefHeight);
    }


    // ******************** Drawing related ***********************************
    public final void drawBackground() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();
        final double COLUMN_WIDTH  = control.getPrefHeight() * 0.5925925925925926;

        background.getChildren().clear();

        // background
        Rectangle digitBackground = new Rectangle(0, 0, WIDTH, HEIGHT);
        digitBackground.setFill(control.getColor());
        digitBackground.setStroke(null);
        background.getChildren().add(digitBackground);

        Rectangle decimalBackground = new Rectangle(0, 0, control.getNoOfDecimals() * COLUMN_WIDTH, HEIGHT);
        decimalBackground.setFill(control.getDecimalColor());
        decimalBackground.setStroke(null);
        decimalBackground.setTranslateX(WIDTH - decimalBackground.getLayoutBounds().getWidth());
        background.getChildren().add(decimalBackground);

        for (int i = 0 ; i < (control.getNoOfDigits()) ; i++) {
            Path path = new Path();
            path.setFillRule(FillRule.EVEN_ODD);
            path.getElements().add(new MoveTo(0, 0));
            path.getElements().add(new LineTo(0, HEIGHT));
            path.getElements().add(new MoveTo(COLUMN_WIDTH, 0));
            path.getElements().add(new LineTo(COLUMN_WIDTH, HEIGHT));
            path.setStrokeWidth(0.5);
            path.setStrokeType(StrokeType.CENTERED);
            path.setTranslateX(i * COLUMN_WIDTH);
            path.setStroke(Color.color(0.0, 0.0, 0.0, 0.8));
            background.getChildren().add(path);
        }
        Path path = new Path();
        path.setFillRule(FillRule.EVEN_ODD);
        path.getElements().add(new MoveTo(WIDTH, 0));
        path.getElements().add(new LineTo(WIDTH, HEIGHT));
        path.setStrokeWidth(0.5);
        path.setStrokeType(StrokeType.CENTERED);
        path.setStroke(Color.color(0.0, 0.0, 0.0, 0.8));
        background.getChildren().add(path);
        background.setCache(true);
    }

    public final void drawForeground() {
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();

        foreground.getChildren().clear();

        final Shape OVERLAY = new Rectangle(0, 0, WIDTH, HEIGHT);
        final LinearGradient OVERLAY_FILL  = new LinearGradient(0, 0,
                                                                0, HEIGHT,
                                                                false, CycleMethod.NO_CYCLE,
                                                                new Stop(0.0, Color.color(0, 0, 0, 1)),
                                                                new Stop(0.15, Color.color(0, 0, 0, 0.4)),
                                                                new Stop(0.33, Color.color(1, 1, 1, 0.45)),
                                                                new Stop(0.46, Color.color(1, 1, 1, 0.0)),
                                                                new Stop(0.85, Color.color(0, 0, 0, 0.4)),
                                                                new Stop(1.0, Color.color(0, 0, 0, 1)));
        OVERLAY.setFill(OVERLAY_FILL);
        OVERLAY.setStroke(null);

        foreground.getChildren().add(OVERLAY);
    }

    public final void setupDials() {
        final double COLUMN_WIDTH  = control.getPrefHeight() * 0.5925925925925926;
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();
        for (int i = 0 ; i < (control.getNoOfDigits() + control.getNoOfDecimals()) ; i++) {
            double offsetX = WIDTH / 2.0 - ((i + 1) * COLUMN_WIDTH) + COLUMN_WIDTH / 2;
            Color numberColor = i < control.getNoOfDecimals() ? control.getNumberDecimalColor() : control.getNumberColor();
            Dial dial = new Dial(numberColor, offsetX, HEIGHT);
            listOfDials.add(dial);
        }
    }

    private class Dial {
        private Group               nextNumberGroup;
        private Text                nextNumber;
        private Group               currentNumberGroup;
        private Text                currentNumber;
        private TranslateTransition next;
        private TranslateTransition current;
        private ParallelTransition  parallel;

        protected Dial(final Color NUMBER_COLOR, final double OFFSET_X, final double HEIGHT) {
            nextNumber         = new Text();
            nextNumberGroup    = createGroup(NUMBER_COLOR, nextNumber, 1, OFFSET_X, -HEIGHT);
            currentNumber      = new Text();
            currentNumberGroup = createGroup(NUMBER_COLOR, currentNumber, 0, OFFSET_X, 0);
            next               = new TranslateTransition(Duration.millis(control.getInterval()), nextNumberGroup);
            current            = new TranslateTransition(Duration.millis(control.getInterval()), currentNumberGroup);
            //parallel           = new ParallelTransition();
            next.setFromY(-control.getPrefHeight());
            next.setToY(0);
            next.setInterpolator(Interpolator.LINEAR);
            current.setFromY(0);
            current.setToY(control.getPrefHeight());
            current.setInterpolator(Interpolator.LINEAR);
            current.setDelay(Duration.ZERO);
            parallel = new ParallelTransition(next, current);
        }

        protected void setNumber(final int NUMBER) {
            if (parallel.getStatus() == Animation.Status.RUNNING) {
                parallel.stop();
                increaseNumbers();
            }
            if (!Integer.toString(NUMBER).equals(currentNumber.getText())) {
                parallel.play();

                parallel.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        increaseNumbers();
                    }
                });

            }
        }

        protected void reset() {
            parallel.stop();
            nextNumberGroup.setTranslateY(-control.getPrefHeight());
            nextNumber.setText("1");
            currentNumberGroup.setTranslateY(0);
            currentNumber.setText("0");
        }

        protected Group getNextNumberGroup() {
            return nextNumberGroup;
        }

        protected Group getCurrentNumberGroup() {
            return currentNumberGroup;
        }

        private Group createGroup(final Color NUMBER_COLOR, Text text, final int NUMBER, final double OFFSET_X, final double OFFSET_Y) {
            Group group = new Group();
            text.setText(Integer.toString(NUMBER));
            text.setTextAlignment(TextAlignment.CENTER);
            text.setTextOrigin(VPos.CENTER);
            text.setFont(font);
            text.setFill(NUMBER_COLOR);
            text.setStroke(null);
            text.setFontSmoothingType(FontSmoothingType.LCD);
            group.getChildren().addAll(text);
            group.setTranslateX(OFFSET_X);
            group.setTranslateY(OFFSET_Y);
            return group;
        }

        private void increaseNumbers() {
            int number = Integer.parseInt(nextNumber.getText());
            nextNumberGroup.setTranslateY(-control.getPrefHeight());
            nextNumber.setText(number == 9 ? "0" : Integer.toString(number + 1));
            currentNumberGroup.setTranslateY(0);
            currentNumber.setText(Integer.toString(number));
        }
    }
}
