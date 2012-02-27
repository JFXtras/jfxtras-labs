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
import javafx.animation.AnimationTimer;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import jfxtras.labs.internal.scene.control.behavior.OdometerBehavior;
import jfxtras.labs.scene.control.gauge.Odometer;

import java.util.LinkedList;
import java.util.List;


/**
 * Created by
 * User: hansolo
 * Date: 25.02.12
 * Time: 15:40
 */
public class OdometerSkin extends SkinBase<Odometer, OdometerBehavior> {
    private Odometer       control;
    private static double  MIN_FLIP_TIME = 1000000000.0 / 60.0; // 60 fps
    private boolean        isDirty;
    private boolean        initialized;
    private Group          foreground;
    private Group[]        digits;
    private List<Column>   listOfColumns;
    private Group          background;
    private int            counter;
    private double         value;
    private double         stepSize;
    private AnimationTimer timer;
    private Font           font;


    // ******************** Constructors **************************************
    public OdometerSkin(final Odometer CONTROL) {
        super(CONTROL, new OdometerBehavior(CONTROL));
        control           = CONTROL;
        initialized       = false;
        isDirty           = false;
        foreground        = new Group();
        digits            = new Group[3];
        listOfColumns     = new LinkedList<>();
        background        = new Group();
        counter           = 1;
        value             = 0;
        stepSize          = 1;
        timer             = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (initialized) {
                    if (control.isCountdownMode()) {

                    } else {
                        countUp();
                    }
                }
            }
        };
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
        registerChangeListener(control.valueProperty(), "VALUE");

        initialized = true;
        paint();
        stepSize = control.getPrefHeight() / (control.getFlipTime() / (MIN_FLIP_TIME));
        if (control.getValue() != value) {
            timer.start();
        }
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
        drawDigits();
        drawForeground();
        getChildren().add(background);
        for (Column column : listOfColumns) {
            getChildren().addAll(column.getGroups());
        }
        getChildren().add(foreground);
    }

    @Override protected void handleControlPropertyChanged(final String PROPERTY) {
        super.handleControlPropertyChanged(PROPERTY);
        if (PROPERTY == "VALUE") {
            if(Double.compare(control.getValue(),value) != 0) {
                timer.start();
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

    private void countUp() {
        increaseDigit(0);

        if (listOfColumns.get(0).getValue() == 0) {
            increaseDigit(1);
        }

    }

    private void increaseDigit(final int INDEX) {
        final double HEIGHT = control.getPrefHeight();
        final Column COLUMN = listOfColumns.get(INDEX);
        COLUMN.getPositions()[0] += stepSize;
        if (COLUMN.getPositions()[0] > HEIGHT) {
            COLUMN.getPositions()[0] = -2 * HEIGHT;
            COLUMN.getNumbers()[0].setText(getCounter(INDEX));
        }
        COLUMN.getGroups()[0].setTranslateY(COLUMN.getPositions()[0]);

        COLUMN.getPositions()[1] += stepSize;
        if (COLUMN.getPositions()[1] > HEIGHT) {
            COLUMN.getPositions()[1] = -2 * HEIGHT;
            COLUMN.getNumbers()[1].setText(getCounter(INDEX));
        }
        COLUMN.getGroups()[1].setTranslateY(COLUMN.getPositions()[1]);

        COLUMN.getPositions()[2] += stepSize;
        if (COLUMN.getPositions()[2] > HEIGHT) {
            COLUMN.getPositions()[2] = -2 * HEIGHT;
            COLUMN.getNumbers()[2].setText(getCounter(INDEX));
        }
        COLUMN.getGroups()[2].setTranslateY(COLUMN.getPositions()[2]);
    }

    private String getCounter(final int INDEX) {
        if (control.isCountdownMode()) {
            listOfColumns.get(INDEX).decreaseCounter();
        } else {
            listOfColumns.get(INDEX).increaseCounter();
        }
        return Integer.toString(listOfColumns.get(INDEX).getCounter());
    }

    private void reset() {
        paint();
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

    public final void drawDigits() {
        final double COLUMN_WIDTH  = control.getPrefHeight() * 0.5925925925925926;
        final double WIDTH = control.getPrefWidth();
        final double HEIGHT = control.getPrefHeight();
        for (int i = 0 ; i < (control.getNoOfDigits() + control.getNoOfDecimals()) ; i++) {
            double offsetX = WIDTH / 2.0 - ((i + 1) * COLUMN_WIDTH) + COLUMN_WIDTH / 2;
            Color numberColor = i < control.getNoOfDecimals() ? control.getNumberDecimalColor() : control.getNumberColor();

            // Group and text at the bottom
            Group groupBottom = new Group();
            Text  textBottom  = new Text();
            textBottom.setTextAlignment(TextAlignment.CENTER);
            textBottom.setTextOrigin(VPos.CENTER);
            textBottom.setFont(font);
            textBottom.setFill(numberColor);
            textBottom.setStroke(null);
            textBottom.setText("1");
            textBottom.setFontSmoothingType(FontSmoothingType.LCD);
            groupBottom.getChildren().addAll(textBottom);
            groupBottom.setTranslateY(HEIGHT);
            groupBottom.setTranslateX(offsetX);

            // Group and text at the middle
            Group groupMiddle = new Group();
            Text  textMiddle  = new Text();
            textMiddle.setTextAlignment(TextAlignment.CENTER);
            textMiddle.setTextOrigin(VPos.CENTER);
            textMiddle.setFont(font);
            textMiddle.setFill(numberColor);
            textMiddle.setStroke(null);
            textMiddle.setText("0");
            textMiddle.setFontSmoothingType(FontSmoothingType.LCD);
            groupMiddle.getChildren().addAll(textMiddle);
            groupMiddle.setTranslateX(offsetX);

            // Group and text at the top
            Group groupTop = new Group();
            Text  textTop  = new Text();
            textTop.setTextAlignment(TextAlignment.CENTER);
            textTop.setTextOrigin(VPos.CENTER);
            textTop.setFont(font);
            textTop.setFill(numberColor);
            textTop.setStroke(null);
            textTop.setText("9");
            textTop.setFontSmoothingType(FontSmoothingType.LCD);
            groupTop.getChildren().addAll(textTop);
            groupTop.setTranslateY(-HEIGHT);
            groupTop.setTranslateX(offsetX);

            Column column = new Column(new Group[] {groupBottom, groupMiddle, groupTop},
                                       new Text[] {textBottom, textMiddle, textTop},
                                       new double[] {-HEIGHT, 0, HEIGHT});
            listOfColumns.add(column);
        }
    }

    private class Column {
        private Group[]  groups;
        private Text[]   numbers;
        private double[] positions;
        private int      counter;
        private int      value;

        public Column(final Group[] GROUPS, final Text[] NUMBERS, final double[] POSITIONS) {
            groups    = GROUPS;
            numbers   = NUMBERS;
            positions = POSITIONS;
            counter   = 1;
            value     = 0;
        }

        public Group[] getGroups() {
            return groups;
        }

        public Text[] getNumbers() {
            return numbers;
        }

        public double[] getPositions() {
            return positions;
        }

        public int getCounter() {
            return counter;
        }

        public int getValue() {
            return value;
        }

        public void increaseCounter() {
            counter++;
            value++;
            if (control.getType() == Odometer.Type.NUMERIC) {
                if (counter > 9) {
                    counter = 0;
                }
                if (value > 9) {
                    value = 0;
                }
            } else {
                if (counter > 5) {
                    counter = 0;
                }
                if (value > 5) {
                    value = 0;
                }
            }
        }

        public void decreaseCounter() {
            counter--;
            value--;
            if (control.getType() == Odometer.Type.NUMERIC) {
                if (counter < 0) {
                    counter = 9;
                }
                if (value < 0) {
                    value = 9;
                }
            } else {
                if (counter < 0) {
                    counter = 5;
                }
                if (value < 0) {
                    value = 5;
                }
            }
        }
    }
}
