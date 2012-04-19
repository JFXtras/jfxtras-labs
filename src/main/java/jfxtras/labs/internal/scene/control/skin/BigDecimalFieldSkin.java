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
import java.math.BigDecimal;
import java.text.ParseException;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import jfxtras.labs.internal.scene.control.behavior.BigDecimalFieldBehaviour;
import jfxtras.labs.scene.control.BigDecimalField;

/**
 * Skin implementation for {@link BigDecimalField}.
 *
 * @author Thomas Bolz
 */
public class BigDecimalFieldSkin extends SkinBase<BigDecimalField, BigDecimalFieldBehaviour> {

    public BigDecimalFieldSkin(BigDecimalField control) {
        super(control, new BigDecimalFieldBehaviour(control));
        createNodes();
        initFocusBehaviourWorkaround();
        requestLayout();
    }
    private GridPane root;
    private TextField textField;
    private StackPane btnUp;
    private StackPane btnDown;
    private Path arrowUp;
    private Path arrowDown;
    private final double ARROW_SIZE = 4;
    // arrow height is ARROW_HEIGHT * ARROW_SIZE
    private final double ARROW_HEIGHT = 0.7;

    /**
     * Creates and lays out the Nodes in this Skin
     */
    private void createNodes() {
        root = GridPaneBuilder.create().columnConstraints(
                ColumnConstraintsBuilder.create().fillWidth(true).build(),
                ColumnConstraintsBuilder.create().fillWidth(true).build()).rowConstraints(
                RowConstraintsBuilder.create().fillHeight(true).build(),
                RowConstraintsBuilder.create().fillHeight(true).build()).build();
        root.setFocusTraversable(true);
        textField = new NumberTextField();

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
        
        // We want the button's width to be the textfield's height
        // As we set fillWidth == true in the GridPane, sizing one button is enough
        btnUp.prefWidthProperty().bind(textField.heightProperty());
        btnDown.prefWidthProperty().bind(textField.prefHeightProperty());

        // Textfield and two Buttons are layed out in a GridPane
        root.add(textField, 0, 0, 1, GridPane.REMAINING);
        root.add(btnUp, 1, 0);
        root.add(btnDown, 1, 1);

        //
        // Mouse Handler for buttons
        //
        btnUp.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                getSkinnable().increment();
            }
        });
        btnDown.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent arg0) {
                getSkinnable().decrement();
            }
        });
        
        getChildren().setAll(root);

        // uncomment for debugging
//         root.setStyle("-fx-background-color:red;");
//         root.setGridLinesVisible(true);
    }
    
    /**
     * As we want to re-use the TextField's behaviour, but style the controls
     * focus, we have to use the following workaround:
     *
     */
    private void initFocusBehaviourWorkaround() {
        // If focus is gained on the Control, it is forwarded to the TextField.
        getSkinnable().focusedProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable arg0) {
                textField.requestFocus();
            }
        });
        // If the TextField gains/loses focus the style of the Control is changed
        textField.focusedProperty().addListener(new InvalidationListener() {

            @Override
            public void invalidated(Observable arg0) {
                if (textField.isFocused()) {
                    getSkinnable().getStyleClass().add("big-decimal-field-focused");
                } else {
                    getSkinnable().getStyleClass().remove("big-decimal-field-focused");
                }

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
            setText(getSkinnable().getText());
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
            getSkinnable().numberProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable arg0) {
                    setText(getSkinnable().getText());
                }
            });

            // key up/down ==> inc/dec
            addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

                @Override
                public void handle(KeyEvent keyEvent) {
                    if (keyEvent.getCode() == KeyCode.DOWN) {
                        getSkinnable().decrement();
                        keyEvent.consume();
                    }
                    if (keyEvent.getCode() == KeyCode.UP) {
                        getSkinnable().increment();
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
                    return;
                }
                Number parsedNumber = getSkinnable().getFormat().parse(input);
                BigDecimal newValue = new BigDecimal(parsedNumber.toString());
                // if parsing succeeded change number in Controller
                getSkinnable().setNumber(newValue);
                selectAll();
            } catch (ParseException ex) {
                // If parsing fails keep old number
                setText(getSkinnable().getText());
            }
        }
    }

}
