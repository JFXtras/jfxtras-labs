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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.WritableValue;
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

import com.sun.javafx.css.StyleableObjectProperty;
import com.sun.javafx.css.StyleableProperty;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.SkinBase;

/**
 * 
 * @author Tom Eugelink
 * 
 * Possible extension: drop down list or grid for quick selection
 */
public class ListSpinnerCaspianSkin<T> extends SkinBase<ListSpinner<T>, ListSpinnerBehavior<T>>
{
	// TODO: vertical centering 
	
	// ==================================================================================================================
	// CONSTRUCTOR
	
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
		getSkinnable().editableProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable arg0)
			{
				replaceValueNode();
			}
		});
		replaceValueNode();
		
		// react to value changes in the model
		getSkinnable().valueProperty().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable arg0)
			{
				refreshValue();
			}
		});
		refreshValue();
		
		// arrows
		setArrowHorizontalOrVerticalCSSClass();
		layoutGridPane();
		
		// value
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
	// StyleableProperties
	
	/** ArrowPosition: */
	public ObjectProperty<ArrowPosition> arrowPositionProperty() { return this.arrowPositionObjectProperty; }
	final private StyleableObjectProperty<ArrowPosition> arrowPositionObjectProperty = new StyleableObjectProperty<ArrowPosition>(ArrowPosition.TRAILING) {

		@Override 
		public void invalidated() 
		{
			super.invalidated();
			setArrowHorizontalOrVerticalCSSClass();
			layoutGridPane();
		}
		
        @Override
		public void set(ArrowPosition value)
		{
			if (value == null) throw new NullPointerException("Null not allowed as the value for arrowPosition");
			super.set(value);
		}
        
        @Override
        public StyleableProperty<ListSpinnerCaspianSkin,ArrowPosition> getStyleableProperty() {
            return ARROW_POSITION;
        }

        @Override
        public Object getBean() {
            return ListSpinnerCaspianSkin.this;
        }

        @Override
        public String getName() {
            return "arrowPosition";
        }
    };			
	public ArrowPosition getArrowPosition() { return this.arrowPositionObjectProperty.getValue(); }
	public void setArrowPosition(ArrowPosition value) { this.arrowPositionObjectProperty.setValue(value); }
	public ListSpinnerCaspianSkin<T> withArrowPosition(ArrowPosition value) { setArrowPosition(value); return this; }
	public enum ArrowPosition {LEADING, TRAILING, SPLIT}	
    private static final StyleableProperty<ListSpinnerCaspianSkin,ArrowPosition> ARROW_POSITION = new StyleableProperty<ListSpinnerCaspianSkin,ArrowPosition>("-fxx-arrow-position", new EnumConverter<ArrowPosition>(ArrowPosition.class), ArrowPosition.TRAILING) 
	{
        @Override
        public boolean isSettable(ListSpinnerCaspianSkin owner) {
            return !owner.arrowPositionObjectProperty.isBound();
        }

        @Override
        public WritableValue<ArrowPosition> getWritableValue(ListSpinnerCaspianSkin owner) {
            return owner.arrowPositionProperty();
        }
    };
    
	/** ArrowDirection: */
	public ObjectProperty<ArrowDirection> arrowDirectionProperty() { return this.arrowDirectionObjectProperty; }
	final private StyleableObjectProperty<ArrowDirection> arrowDirectionObjectProperty = new StyleableObjectProperty<ArrowDirection>(ArrowDirection.HORIZONTAL) {

		@Override 
		public void invalidated() 
		{
			super.invalidated();
			setArrowHorizontalOrVerticalCSSClass();
			layoutGridPane();
		}
		
        @Override
		public void set(ArrowDirection value)
		{
			if (value == null) throw new NullPointerException("Null not allowed as the value for arrowDirection");
			super.set(value);
		}
        
        @Override
        public StyleableProperty<ListSpinnerCaspianSkin,ArrowDirection> getStyleableProperty() {
            return ARROW_DIRECTION;
        }

        @Override
        public Object getBean() {
            return ListSpinnerCaspianSkin.this;
        }

        @Override
        public String getName() {
            return "arrowDirection";
        }
    };			
	public ArrowDirection getArrowDirection() { return this.arrowDirectionObjectProperty.getValue(); }
	public void setArrowDirection(ArrowDirection value) { this.arrowDirectionObjectProperty.setValue(value); }
	public ListSpinnerCaspianSkin<T> withArrowDirection(ArrowDirection value) { setArrowDirection(value); return this; }
	public enum ArrowDirection {VERTICAL, HORIZONTAL}	
    private static final StyleableProperty<ListSpinnerCaspianSkin,ArrowDirection> ARROW_DIRECTION = new StyleableProperty<ListSpinnerCaspianSkin,ArrowDirection>("-fxx-arrow-direction", new EnumConverter<ArrowDirection>(ArrowDirection.class), ArrowDirection.HORIZONTAL) 
	{
        @Override
        public boolean isSettable(ListSpinnerCaspianSkin owner) {
            return !owner.arrowDirectionObjectProperty.isBound();
        }

        @Override
        public WritableValue<ArrowDirection> getWritableValue(ListSpinnerCaspianSkin owner) {
            return owner.arrowDirectionProperty();
        }
    };
    
	/** ValueAlignment: */
	public ObjectProperty<Pos> valueAlignmentProperty() { return this.valueAlignmentObjectProperty; }
	final private StyleableObjectProperty<Pos> valueAlignmentObjectProperty = new StyleableObjectProperty<Pos>(Pos.CENTER_LEFT) {

		@Override 
		public void invalidated() 
		{
			super.invalidated();
			alignValue();
		}
		
        @Override
		public void set(Pos value)
		{
			if (value == null) throw new NullPointerException("Null not allowed as the value for value alignment");
			super.set(value);
		}
        
        @Override
        public StyleableProperty<ListSpinnerCaspianSkin,Pos> getStyleableProperty() {
            return VALUE_ALIGNMENT;
        }

        @Override
        public Object getBean() {
            return ListSpinnerCaspianSkin.this;
        }

        @Override
        public String getName() {
            return "valueAlignment";
        }
    };			
	public Pos getValueAlignment() { return this.valueAlignmentObjectProperty.getValue(); }
	public void setValueAlignment(Pos value) { this.valueAlignmentObjectProperty.setValue(value); }
	public ListSpinnerCaspianSkin<T> withAlignment(Pos value) { setValueAlignment(value); return this; }
    private static final StyleableProperty<ListSpinnerCaspianSkin,Pos> VALUE_ALIGNMENT = new StyleableProperty<ListSpinnerCaspianSkin,Pos>("-fxx-value-alignment", new EnumConverter<Pos>(Pos.class), Pos.CENTER_LEFT) 
	{
        @Override
        public boolean isSettable(ListSpinnerCaspianSkin owner) {
            return !owner.valueAlignmentObjectProperty.isBound();
        }

        @Override
        public WritableValue<Pos> getWritableValue(ListSpinnerCaspianSkin owner) {
            return owner.valueAlignmentProperty();
        }
    };
    
    // ----------------------------
    // communicate the styleables
    
    /**
     * 
     */
    @Override
    @Deprecated
    public List<StyleableProperty> impl_getStyleableProperties() 
    {
        if (STYLEABLES == null) 
        {
            final List<StyleableProperty> styleables = new ArrayList<StyleableProperty>(super.impl_getStyleableProperties());
            Collections.addAll(styleables, ARROW_POSITION, ARROW_DIRECTION, VALUE_ALIGNMENT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
        return STYLEABLES;
    }
    private static List<StyleableProperty> STYLEABLES;
            
	// ==================================================================================================================
	// DRAW
	
	/**
	 * Construct the nodes. 
	 * Spinner uses a GridPane where the arrows and the node for the value are laid out according to the arrows direction and location.
	 * A place holder in inserted into the GridPane to hold the value node, so the spinner can alternate between editable or readonly mode, without having to recreate the GridPane.  
	 */
	private void createNodes()
	{
		// left arrow
		decrementArrow = new Region();
		decrementArrow.getStyleClass().add("idle");

		// place holder for showing the value
		valueGroup = new BorderPane();
		valueGroup.getStyleClass().add("valuePane");
		
		// right arrow
		incrementArrow = new Region();
		incrementArrow.getStyleClass().add("idle");

		// construct a gridpane
		gridPane = new GridPane();

		// we're not catching the mouse events on the individual children, but let it bubble up to the parent and handle it there, this makes our life much more simple
		// process mouse clicks
		gridPane.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override public void handle(MouseEvent evt)
			{
				// if click was the in the greater vicinity of the decrement arrow
				if (mouseEventOverArrow(evt, decrementArrow))
				{
					// left
					unclickArrows();
					decrementArrow.getStyleClass().add("clicked");
					getSkinnable().decrement();
					unclickTimer.restart();
					return;
				}
				
				// if click was the in the greater vicinity of the increment arrow
				if (mouseEventOverArrow(evt, incrementArrow))
				{
					// right
					unclickArrows();
					incrementArrow.getStyleClass().add("clicked");
					getSkinnable().increment();
					unclickTimer.restart();
					return;
				}
			}
		});
		// process mouse holds
		gridPane.setOnMousePressed(new EventHandler<MouseEvent>()
		{
			@Override public void handle(MouseEvent evt)
			{
				// if click was the in the greater vicinity of the decrement arrow
				if (mouseEventOverArrow(evt, decrementArrow))
				{
					// left
					decrementArrow.getStyleClass().add("clicked");
					repeatDecrementClickTimer.restart();
					return;
				}
				
				// if click was the in the greater vicinity of the increment arrow
				if (mouseEventOverArrow(evt, incrementArrow))
				{
					// right
					incrementArrow.getStyleClass().add("clicked");
					repeatIncrementClickTimer.restart();
					return;
				}
			}
		});
		gridPane.setOnMouseReleased(new EventHandler<MouseEvent>()
		{
			@Override public void handle(MouseEvent evt)
			{
				unclickArrows();
				repeatDecrementClickTimer.stop();
				repeatIncrementClickTimer.stop();
			}
		});
		gridPane.setOnMouseExited(new EventHandler<MouseEvent>()
		{
			@Override public void handle(MouseEvent evt)
			{
				unclickArrows();
				repeatDecrementClickTimer.stop();
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
					// left
					unclickArrows();
					decrementArrow.getStyleClass().add("clicked");
					getSkinnable().decrement();
					unclickTimer.restart();
					return;
				}
				
				// if click was the in the greater vicinity of the increment arrow
				if (evt.getDeltaY() > 0 || evt.getDeltaX() > 0)
				{
					// right
					unclickArrows();
					incrementArrow.getStyleClass().add("clicked");
					getSkinnable().increment();
					unclickTimer.restart();
					return;
				}
			}
		});
		
		// add to self
		this.getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
		getChildren().add(gridPane); 
	}
	private Region decrementArrow = null;
	private Region incrementArrow = null;
	private GridPane gridPane = null;
	private BorderPane valueGroup;
	
	// timer to remove the click styling on the arrows after a certain delay
	final private Timer unclickTimer = new Timer(new Runnable()
	{
		@Override
		public void run()
		{
			unclickArrows();
		}
	}).withDelay(Duration.millis(100)).withRepeats(false);

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
	 * Check if the mouse event is considered to have happened over the arrow
	 * @param evt
	 * @param region
	 * @return
	 */
	private boolean mouseEventOverArrow(MouseEvent evt, Region region)
	{
		// if click was the in the greater vicinity of the decrement arrow
		Point2D lClickInRelationToArrow = region.sceneToLocal(evt.getSceneX(), evt.getSceneY());
		if ( lClickInRelationToArrow.getX() >= 0.0 && lClickInRelationToArrow.getX() <= region.getWidth()
		  && lClickInRelationToArrow.getY() >= 0.0 && lClickInRelationToArrow.getY() <= region.getHeight()
		   )
		{
			return true;
		}
		return false;
	}
	
	/**
	 * Remove clicked CSS styling from the arrows
	 */
	private void unclickArrows()
	{
		decrementArrow.getStyleClass().remove("clicked");
		incrementArrow.getStyleClass().remove("clicked");
	}
	
	/**
	 * Put the correct node for the value's place holder: 
	 * - either the TextField when in editable mode, 
	 * - or a node generated by the cell factory when in readonly mode.  
	 */
	private void replaceValueNode()
	{
		// clear
		valueGroup.getChildren().clear();
		
		// if not editable
		if (getSkinnable().isEditable() == false)
		{
			// use the cell factory
			Node lNode = getSkinnable().getCellFactory().call(getSkinnable());
			valueGroup.setCenter( lNode );
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
				textField.alignmentProperty().bind(valueAlignmentProperty());
			}
			valueGroup.setCenter(textField);
		}
		
		// align
		alignValue();
	}
	private TextField textField = null;

	/**
	 * align the value inside the plave holder
	 */
	private void alignValue()
	{
		// valueGroup always only holds one child (the value)
		BorderPane.setAlignment(valueGroup.getChildren().get(0), valueAlignmentProperty().getValue());
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
		return;
	}
	
	/**
	 * Lays out the spinner, depending on the location and direction of the arrows.
	 */
	private void layoutGridPane()
	{
		// get the things we decide on
		ArrowDirection lArrowDirection = getArrowDirection();
		ArrowPosition lArrowPosition = getArrowPosition();
		
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
				gridPane.add(decrementArrow, 0, 0);
				gridPane.add(incrementArrow, 1, 0);
				gridPane.add(valueGroup, 2, 0);
				gridPane.getColumnConstraints().addAll(lColumnArrow, lColumnArrow, lColumnValue);
			}
			if (lArrowPosition == ArrowPosition.TRAILING)
			{
				// construct a gridpane: one row, three columns: value, arrow, arrow
				gridPane.setHgap(3);
				gridPane.setVgap(0);
				gridPane.add(valueGroup, 0, 0);
				gridPane.add(decrementArrow, 1, 0);
				gridPane.add(incrementArrow, 2, 0);
				gridPane.getColumnConstraints().addAll(lColumnValue, lColumnArrow, lColumnArrow);
			}
			if (lArrowPosition == ArrowPosition.SPLIT)
			{
				// construct a gridpane: one row, three columns: arrow, value, arrow
				gridPane.setHgap(3);
				gridPane.setVgap(0);
				gridPane.add(decrementArrow, 0, 0);
				gridPane.add(valueGroup, 1, 0);
				gridPane.add(incrementArrow, 2, 0);
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
				gridPane.add(incrementArrow, 0, 0);
				gridPane.add(decrementArrow, 0, 1);
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
				gridPane.add(incrementArrow, 1, 0);
				gridPane.add(decrementArrow, 1, 1);
				gridPane.getColumnConstraints().addAll(lColumnValue, lColumnArrow); 
				gridPane.getRowConstraints().addAll(lRowArrow, lRowArrow);
			}
			if (lArrowPosition == ArrowPosition.SPLIT)
			{
				// construct a gridpane: three rows, one columns: arrow, value, arrow
				gridPane.setHgap(3);
				gridPane.setVgap(0);
				gridPane.add(incrementArrow, 0, 0);
				gridPane.add(valueGroup, 0, 1);
				gridPane.add(decrementArrow, 0, 2);
				gridPane.getColumnConstraints().addAll(lColumnValue); 
				gridPane.getRowConstraints().addAll(lRowArrow, lRowValue, lRowArrow);
			}
		}
	}
	
	/**
	 * Set the CSS according to the direction of the arrows, so the correct arrows are shown
	 */
	private void setArrowHorizontalOrVerticalCSSClass()
	{
		if (getArrowDirection().equals(ArrowDirection.HORIZONTAL))
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
