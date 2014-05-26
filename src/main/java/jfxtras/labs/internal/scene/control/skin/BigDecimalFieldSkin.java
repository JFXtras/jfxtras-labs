/**
 * BigDecimalFieldSkin.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;
import jfxtras.labs.scene.control.BigDecimalField;

import java.math.BigDecimal;
import java.text.ParseException;


/**
 * Skin implementation for {@link BigDecimalField}.
 *
 * @author Thomas Bolz
 */
public class BigDecimalFieldSkin extends SkinBase<BigDecimalField> {

    private BigDecimalField CONTROL;

    public BigDecimalFieldSkin(BigDecimalField control) {
        super(control);
        this.CONTROL = control;
        createNodes();
        initFocusSimulation();
        getSkinnable().requestLayout();
    }

    private NumberTextField	textField;
    private StackPane	btnUp;
    private StackPane	btnDown;
    private Path		arrowUp;
    private Path		arrowDown;
    private final double	ARROW_SIZE	= 4;
    private final double	ARROW_HEIGHT	= 0.7;
    private Timeline  timeline;

    public BigDecimalField getControl() {return CONTROL;}

    /**
     * Creates the Nodes in this Skin
     */
    private void createNodes() {
        textField = new NumberTextField();
        textField.promptTextProperty().bind(CONTROL.promptTextProperty());

        //
        // The Buttons are StackPanes with a Path on top
        //
        // Button Up
        btnUp = new StackPane();
        btnUp.getStyleClass().add("arrow-button");
        arrowUp = new Path();
        arrowUp.getStyleClass().add("spinner-arrow");
        arrowUp.getElements().addAll(new MoveTo(-ARROW_SIZE, 0), new LineTo(0, -ARROW_SIZE * ARROW_HEIGHT),
                new LineTo(ARROW_SIZE, 0));
        btnUp.getChildren().add(arrowUp);

        // Button Down
        btnDown = new StackPane();
        btnDown.getStyleClass().add("arrow-button");
        arrowDown = new Path();
        arrowDown.getStyleClass().add("spinner-arrow");
        arrowDown.getElements().addAll(new MoveTo(-ARROW_SIZE, 0), new LineTo(0, ARROW_SIZE * ARROW_HEIGHT),
                new LineTo(ARROW_SIZE, 0));
        btnDown.getChildren().add(arrowDown);


        this.getChildren().addAll(textField, btnUp, btnDown);

        //
        // Mouse Handler for buttons
        //
        timeline = new Timeline();
        final EventHandler<ActionEvent> btnUpHandler = actionEvent -> CONTROL.increment();
        btnUp.setOnMousePressed(arg0 -> {
            final KeyFrame kf = new KeyFrame(Duration.millis(50), btnUpHandler);
            timeline.setDelay(Duration.millis(500));
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(kf);
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            btnUpHandler.handle(null);
        });
        btnUp.setOnMouseReleased(mouseEvent -> timeline.stop());


        final EventHandler<ActionEvent> btnDownHandler = actionEvent -> {
            CONTROL.decrement();
        };
        btnDown.setOnMousePressed(arg0 -> {
            final KeyFrame kf = new KeyFrame(Duration.millis(50), btnDownHandler);
            timeline.setDelay(Duration.millis(500));
            timeline.getKeyFrames().clear();
            timeline.getKeyFrames().add(kf);
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();
            btnDownHandler.handle(null);
        });
        btnDown.setOnMouseReleased(mouseEvent -> timeline.stop());


    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        super.layoutChildren(contentX, contentY, contentWidth, contentHeight); //To change body of generated methods, choose Tools | Templates.
        Insets insets = getSkinnable().getInsets();
        double x = insets.getLeft();
        double y = insets.getTop();
        double textfieldHeight = contentHeight;
        double buttonWidth = textField.prefHeight(-1);
        Insets buttonInsets = btnDown.getInsets();
        double textfieldWidth = this.getSkinnable().getWidth()-insets.getLeft()-insets.getRight() - buttonWidth - buttonInsets.getLeft() - buttonInsets.getRight();
        layoutInArea(textField, x, y, textfieldWidth, textfieldHeight, Control.USE_PREF_SIZE, HPos.LEFT, VPos.TOP);
        layoutInArea(btnUp, x+textfieldWidth+buttonInsets.getLeft(), y, buttonWidth, textfieldHeight/2, Control.USE_PREF_SIZE, HPos.LEFT, VPos.TOP);
        layoutInArea(btnDown, x+textfieldWidth+buttonInsets.getLeft(), y+textfieldHeight/2, buttonWidth, textfieldHeight/2, Control.USE_PREF_SIZE, HPos.LEFT, VPos.TOP);
    }


    @Override
    protected double computePrefWidth(double PREF_WIDTH, double topInset, double rightInset, double bottomInset, double leftInset) {
        super.computePrefWidth(PREF_WIDTH, topInset, rightInset, bottomInset, leftInset);
        double prefWidth = getSkinnable().getInsets().getLeft()
                + textField.prefWidth(PREF_WIDTH)
                + textField.prefHeight(PREF_WIDTH)
                + getSkinnable().getInsets().getRight();
        return prefWidth;
    }

    @Override
    protected double computePrefHeight(double PREF_HEIGHT, double topInset, double rightInset, double bottomInset, double leftInset) {
        super.computePrefHeight(PREF_HEIGHT, topInset, rightInset, bottomInset, leftInset);
        double prefHeight = getSkinnable().getInsets().getTop()
                + textField.prefHeight(PREF_HEIGHT)
                +getSkinnable().getInsets().getBottom();
        return prefHeight;
    }





    /**
     * When the CONTROL or the textfield of the Skin gains the focus we make that visible by adding/removing
     * a CSS style that visually simulates the focus.
     *
     */
    private void initFocusSimulation() {

        // If the TextField gains/loses focus the style of the CONTROL is changed
        CONTROL.focusedProperty().addListener((ov, wasFocused, isFocused) -> {
            if (isFocused) {
                Platform.runLater(() -> textField.requestFocus());
            }
        });
        // If the TextField gains/loses focus the style of the CONTROL is changed
        textField.focusedProperty().addListener((ov, wasFocused, isFocused) -> {
            if (isFocused) {
                CONTROL.getStyleClass().add("big-decimal-field-focused");
            } else {
                CONTROL.getStyleClass().remove("big-decimal-field-focused");
            }
        });

    }

    /**
     * The TextField that is used to edit and display the BigDecimal.
     */
    public class NumberTextField extends javafx.scene.control.TextField {

        public NumberTextField() {
            getStyleClass().add("number-text-field");
            initHandlers();
            setText(CONTROL.getText());
        }

        private void initHandlers() {

            // try to parse when RETURN is hit...
            setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent arg0) {
                    parseAndFormatInput();
                }
            });

            // ...or focus is lost
            focusedProperty().addListener(new ChangeListener<Boolean>() {

                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    if (!newValue.booleanValue()) {
                        parseAndFormatInput();
                    }
                }
            });

            // If number in controller changes update the TextField with the formatted number string
            CONTROL.numberProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable arg0) {
                    setText(CONTROL.getText());
                }
            });

            // key up/down ==> inc/dec
            addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent keyEvent) {
                    if (keyEvent.getCode() == KeyCode.DOWN) {
                        CONTROL.decrement();
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP) {
                        CONTROL.increment();
                        keyEvent.consume();
                    }
                }
            });

        }

        /**
         * Tries to parse the user input to a number according to the provided
         * NumberFormat
         */
        private void parseAndFormatInput() {
            try {
                String input = getText();
                if (input == null || input.length() == 0) {
                    CONTROL.setNumber(null);
                    return;
                }
                Number parsedNumber = CONTROL.getFormat().parse(input);
                BigDecimal newValue = new BigDecimal(parsedNumber.toString());
                // if parsing succeeded change number in Controller
                CONTROL.setNumber(newValue);
                selectAll();
            } catch (ParseException ex) {
                // If parsing fails keep old number
                setText(CONTROL.getText());
            } catch (IllegalArgumentException ex) {
                // If minValue and/or maxValue are set and the new number is out of these bounds
                // keep also the old number
                setText(CONTROL.getText());
            }
        }

    }

}
