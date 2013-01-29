/**
 * Copyright (c) 2011, JFXtras
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

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;
import jfxtras.labs.animation.Timer;
import jfxtras.labs.internal.scene.control.behavior.ListSpinnerBehavior;
import jfxtras.labs.scene.control.ListSpinner;
import jfxtras.labs.scene.control.ListSpinner.ArrowDirection;
import jfxtras.labs.scene.control.ListSpinner.ArrowPosition;

import com.sun.javafx.scene.control.skin.SkinBase;
import javafx.scene.layout.StackPane;

/**
 * 
 * @author Tom Eugelink, Pedro Duque Vieira
 * 
 * Possible extension: drop down list or grid for quick selection
 */
public class ListSpinnerCaspianSkin<T> extends SkinBase<ListSpinner<T>, ListSpinnerBehavior<T>>
{
    public static final String CSS_CLASS_NAME = "list-spinner";
    
    StackPane decrementButton, incrementButton;
    private Region decrementArrow;
    private Region incrementArrow;
    private GridPane gridPane;
    private StackPane valueGroup;
    private TextField textField;
	
    // timer to handle the holding of the decrement button
    final private Timer repeatDecrementClickTimer = new Timer(new Runnable()
    {
        @Override
        public void run()
        {
            getSkinnable().decrement();
        }
    }).withDelay(Duration.millis(500)).withCycleDuration(Duration.millis(50));

    // timer to handle the holding of the increment button
    final private Timer repeatIncrementClickTimer = new Timer(new Runnable()
    {
        @Override
        public void run()
        {
            getSkinnable().increment();
        }
    }).withDelay(Duration.millis(500)).withCycleDuration(Duration.millis(50));
    
    /**
     * 
     */
    public ListSpinnerCaspianSkin(ListSpinner<T> control)
    {
        super(control, new ListSpinnerBehavior<T>(control));
        construct();
    }

    /*
     * 
     */
    private void construct()
    {
        // setup component
        createNodes();

        // react to value changes in the model
        getSkinnable().editableProperty().addListener(new ChangeListener<Boolean>()
        {
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2)
            {
                replaceValueNode();
            }
        });
        replaceValueNode();

        // react to value changes in the model
        getSkinnable().valueProperty().addListener(new ChangeListener<T>()
        {
            @Override
            public void changed(ObservableValue<? extends T> observableValue, T oldValue, T newValue)
            {
                refreshValue();
            }
        });
        refreshValue();

        // react to value changes in the model
        getSkinnable().arrowDirectionProperty().addListener(new ChangeListener<ListSpinner.ArrowDirection>()
        {
            @Override
            public void changed(ObservableValue<? extends ArrowDirection> observableValue, ArrowDirection oldValue, ArrowDirection newValue)
            {
                setArrowCSS();
                layoutGridPane();
            }
        });
        setArrowCSS();
        layoutGridPane();

        // react to value changes in the model
        getSkinnable().alignmentProperty().addListener(new ChangeListener<Pos>()
        {
            @Override
            public void changed(ObservableValue<? extends Pos> observableValue, Pos oldValue, Pos newValue)
            {
                alignValue();
            }
        });
        alignValue();
    }
	
    /*
     * 
     */
    private void refreshValue() 
    {
        // if editable
        if (getSkinnable().isEditable() == true)
        {
            // update textfield
            T lValue = getSkinnable().getValue();
            textField.setText( getSkinnable().getPrefix() + getSkinnable().getStringConverter().toString(lValue) + getSkinnable().getPostfix() );
        }
        else
        {
            // get node for this value
            Node lNode = getSkinnable().getCellFactory().call( getSkinnable() );
        }
    }

    // ==================================================================================================================
    // DRAW

    /**
     * Construct the nodes. 
     * Spinner uses a GridPane where the arrows and the node for the value are laid out according to the arrows direction and location.
     * A place holder in inserted into the GridPane to hold the value node, so the spinner can alternate between editable or readonly mode, without having to recreate the GridPane.  
     */
    private void createNodes()
    {
        // left button (container of arrow)
        decrementButton = new StackPane();
        decrementButton.getStyleClass().add("decrement-button");

        // left arrow
        decrementArrow = new Region();
        decrementButton.getChildren().add(decrementArrow);

        // place holder for showing the value
        valueGroup = new StackPane();
        valueGroup.getStyleClass().add("value-pane");

        // right button (container of arrow)
        incrementButton = new StackPane();
        incrementButton.getStyleClass().add("increment-button");
        // right arrow
        incrementArrow = new Region();
        incrementButton.getChildren().add(incrementArrow);

        // construct a gridpane
        gridPane = new GridPane();

        decrementButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                getSkinnable().decrement();
            }
        });
        incrementButton.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                getSkinnable().increment();
            }
        });
        
        decrementButton.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                repeatDecrementClickTimer.restart();
            }
        });
        incrementButton.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                repeatIncrementClickTimer.restart();
            }
        });
        
        decrementButton.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                repeatDecrementClickTimer.stop();
            }
        });
        incrementButton.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                repeatIncrementClickTimer.stop();
            }
        });
        
        decrementButton.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                repeatDecrementClickTimer.stop();
            }
        });
        incrementButton.setOnMouseExited(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                repeatIncrementClickTimer.stop();
            }
        });

        
        // mouse wheel
        gridPane.setOnScroll(new EventHandler<ScrollEvent>()
        {
            @Override
            public void handle(ScrollEvent evt)
            {
                // if click was the in the greater vicinity of the decrement arrow
                if (evt.getDeltaY() < 0 || evt.getDeltaX() < 0)
                {
                    getSkinnable().decrement();
                }

                // if click was the in the greater vicinity of the increment arrow
                else if (evt.getDeltaY() > 0 || evt.getDeltaX() > 0)
                {
                    getSkinnable().increment();
                }
            }
        });

        getChildren().add(gridPane); 
    }

    /**
     * Put the correct node for the value's place holder: 
     * - either the TextField when in editable mode, 
     * - or a node generated by the cell factory when in readonly mode.  
     */
    private void replaceValueNode()
    {
        // if not editable
        if (getSkinnable().isEditable() == false)
        {
            // use the cell factory
            Node lNode = getSkinnable().getCellFactory().call(getSkinnable());
            valueGroup.getChildren().setAll(lNode);
            if (lNode.getStyleClass().contains("value") == false) lNode.getStyleClass().add("value");
            if (lNode.getStyleClass().contains("readonly") == false) lNode.getStyleClass().add("readonly");
        }
        else
        {
            // use the textfield
            if (textField == null) 
            {
                textField = new TextField();
                textField.getStyleClass().add("value");
                textField.getStyleClass().add("editable");

                // process text entry
                textField.focusedProperty().addListener(new InvalidationListener()
                {			
                    @Override
                    public void invalidated(Observable arg0)
                    {
                        if (textField.isFocused() == false) 
                        {
                            parse(textField);
                        }
                    }
                });
                textField.setOnAction(new EventHandler<ActionEvent>()
                {
                    @Override
                    public void handle(ActionEvent evt)
                    {
                        parse(textField);
                    }
                });
                textField.setOnKeyPressed(new EventHandler<KeyEvent>() 
                {
                    @Override public void handle(KeyEvent t) 
                    {
                        if (t.getCode() == KeyCode.ESCAPE) 
                        {
                            // refresh
                            refreshValue();
                        }
                    }
                });

                // alignment
                textField.alignmentProperty().bind(getSkinnable().alignmentProperty());
            }
            valueGroup.getChildren().setAll(textField);
        }

        // align
        alignValue();
    }
    

    /**
     * align the value inside the place holder
     */
    private void alignValue()
    {
        // valueGroup always only holds one child (the value)
        BorderPane.setAlignment(valueGroup.getChildren().get(0), getSkinnable().alignmentProperty().getValue());
    }

    // ==================================================================================================================
    // EDITABLE

    /**
     * Parse the contents of the textfield
     * @param textField
     */
    protected void parse(TextField textField)
    {
        // get the text to parse
        String lText = textField.getText();

        // process it
        getBehavior().parse(lText);

        // refresh
        refreshValue();
    }

    /**
     * Lays out the spinner, depending on the location and direction of the arrows.
     */
    private void layoutGridPane()
    {
            // get the things we decide on
            ArrowDirection lArrowDirection = getSkinnable().getArrowDirection();
            ArrowPosition lArrowPosition = getSkinnable().getArrowPosition();

            // get helper values
            ColumnConstraints lColumnValue = new ColumnConstraints(valueGroup.getMinWidth(), valueGroup.getPrefWidth(), Double.MAX_VALUE);
            lColumnValue.setHgrow(Priority.ALWAYS);
            ColumnConstraints lColumnArrow = new ColumnConstraints(10);

            // get helper values
            RowConstraints lRowValue = new RowConstraints(valueGroup.getMinHeight(), valueGroup.getPrefHeight(), Double.MAX_VALUE);
            lRowValue.setVgrow(Priority.ALWAYS);
            RowConstraints lRowArrow = new RowConstraints(10);

            // clear the grid
            gridPane.getChildren().clear();
            gridPane.getColumnConstraints().clear();
            gridPane.getRowConstraints().clear();
            //gridPane.setGridLinesVisible(true);

            if (lArrowDirection == ArrowDirection.HORIZONTAL)
            {
                    if (lArrowPosition == ArrowPosition.LEADING)
                    {
                            // construct a gridpane: one row, three columns: arrow, arrow, value
                            gridPane.setHgap(3);
                            gridPane.setVgap(0);
                            gridPane.add(decrementButton, 0, 0);
                            gridPane.add(incrementButton, 1, 0);
                            gridPane.add(valueGroup, 2, 0);
                            gridPane.getColumnConstraints().addAll(lColumnArrow, lColumnArrow, lColumnValue);
                    }
                    if (lArrowPosition == ArrowPosition.TRAILING)
                    {
                            // construct a gridpane: one row, three columns: value, arrow, arrow
                            gridPane.setHgap(3);
                            gridPane.setVgap(0);
                            gridPane.add(valueGroup, 0, 0);
                            gridPane.add(decrementButton, 1, 0);
                            gridPane.add(incrementButton, 2, 0);
                            gridPane.getColumnConstraints().addAll(lColumnValue, lColumnArrow, lColumnArrow);
                    }
                    if (lArrowPosition == ArrowPosition.SPLIT)
                    {
                            // construct a gridpane: one row, three columns: arrow, value, arrow
                            gridPane.setHgap(3);
                            gridPane.setVgap(0);
                            gridPane.add(decrementButton, 0, 0);
                            gridPane.add(valueGroup, 1, 0);
                            gridPane.add(incrementButton, 2, 0);
                            gridPane.getColumnConstraints().addAll(lColumnArrow, lColumnValue, lColumnArrow);
                    }
            }
            if (lArrowDirection == ArrowDirection.VERTICAL)
            {
                    if (lArrowPosition == ArrowPosition.LEADING)
                    {
                            // construct a gridpane: two rows, two columns: arrows on top, value
                            gridPane.setHgap(3);
                            gridPane.setVgap(0);
                            gridPane.add(decrementButton, 0, 0);
                            gridPane.add(incrementButton, 0, 1);
                            gridPane.add(valueGroup, 1, 0, 1, 2);
                            gridPane.getColumnConstraints().addAll(lColumnArrow, lColumnValue); 
                            gridPane.getRowConstraints().addAll(lRowArrow, lRowArrow);
                    }
                    if (lArrowPosition == ArrowPosition.TRAILING)
                    {
                            // construct a gridpane: two rows, two columns: value, arrows on top
                            gridPane.setHgap(3);
                            gridPane.setVgap(0);
                            gridPane.add(valueGroup, 0, 0, 1, 2);
                            gridPane.add(decrementButton, 1, 0);
                            gridPane.add(incrementButton, 1, 1);
                            gridPane.getColumnConstraints().addAll(lColumnValue, lColumnArrow); 
                            gridPane.getRowConstraints().addAll(lRowArrow, lRowArrow);
                    }
                    if (lArrowPosition == ArrowPosition.SPLIT)
                    {
                            // construct a gridpane: three rows, one columns: arrow, value, arrow
                            gridPane.setHgap(3);
                            gridPane.setVgap(0);
                            gridPane.add(decrementButton, 0, 0);
                            gridPane.add(valueGroup, 0, 1);
                            gridPane.add(incrementButton, 0, 2);
                            gridPane.getColumnConstraints().addAll(lColumnValue); 
                            gridPane.getRowConstraints().addAll(lRowArrow, lRowValue, lRowArrow);
                    }
            }
    }

    /**
     * Set the CSS according to the direction of the arrows, so the correct arrows are shown
     */
    private void setArrowCSS()
    {
            if (getSkinnable().getArrowDirection().equals(ListSpinner.ArrowDirection.HORIZONTAL))
            {
                    decrementArrow.getStyleClass().add("left-arrow");
                    incrementArrow.getStyleClass().add("right-arrow");
            }
            else
            {
                    decrementArrow.getStyleClass().add("down-arrow");
                    incrementArrow.getStyleClass().add("up-arrow");
            }
    }
}
