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

import javafx.animation.Animation;
import javafx.animation.AnimationBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.PauseTransitionBuilder;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.util.Duration;
import jfxtras.labs.animation.Timer;
import jfxtras.labs.internal.scene.control.behavior.SpinnerBehavior;
import jfxtras.labs.scene.control.Spinner;
import jfxtras.labs.scene.control.Spinner.ArrowDirection;

import com.sun.javafx.scene.control.skin.SkinBase;

/**
 * 
 * @author Tom Eugelink
 * 
 * Possible extension: drop down list or grid for quick selection
 */
public class SpinnerCaspianSkin<T> extends SkinBase<Spinner<T>, SpinnerBehavior<T>>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public SpinnerCaspianSkin(Spinner<T> control)
	{
		super(control, new SpinnerBehavior<T>(control));
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
		getSkinnable().arrowDirectionProperty().addListener(new ChangeListener<Spinner.ArrowDirection>()
		{
			@Override
			public void changed(ObservableValue<? extends ArrowDirection> arg0, ArrowDirection arg1, ArrowDirection arg2)
			{
				setArrowCSS();
				setArrowLocation();
			}
		});
		setArrowCSS();
		setArrowLocation();
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
			textField.setText( getSkinnable().getStringConverter().toString(lValue) );
		}
		else
		{
			// get node for this value
			Node lNode = getSkinnable().getCellFactory().call( getSkinnable() );
			
			// if node changed
			if (valueNode != lNode)
			{
				// remove old, add new
				gridPane.getChildren().remove(valueNode);
				valueNode = lNode;
				gridPane.add(valueNode, valueNodeX, valueNodeY, valueNodeW, valueNodeH);
			}
		}
	}
	
	// ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// left arrow
		decrementArrow = new Region();
		decrementArrow.getStyleClass().add("idle");

		// for showing the value
		valueNode = getSkinnable().getCellFactory().call(getSkinnable());
		valueNode.getStyleClass().add("value");
		
		// right arrow
		incrementArrow = new Region();
		incrementArrow.getStyleClass().add("idle");

		// construct a gridpane
		gridPane = new GridPane();

		// we're not catching the mouse events on the individual children, but let it bubble up to the parent and handle it there
		// makes our life much more simple
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
					unclickTimer.playFrom(Duration.millis(0));
					return;
				}
				
				// if click was the in the greater vicinity of the increment arrow
				if (mouseEventOverArrow(evt, incrementArrow))
				{
					// right
					unclickArrows();
					incrementArrow.getStyleClass().add("clicked");
					getSkinnable().increment();
					unclickTimer.playFrom(Duration.millis(0));
					return;
				}
			}
		});
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
		
		// add to self
		this.getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
		getChildren().add(gridPane); 
	}
	private Region decrementArrow = null;
	private Node valueNode = null;
	private Region incrementArrow = null;
	private GridPane gridPane = null;
	
	// transition to remove the click styling on the arrows afer a certain timeframe
	final private PauseTransition unclickTimer = PauseTransitionBuilder.create().onFinished(new EventHandler<ActionEvent>()
	{
		@Override
		public void handle(ActionEvent arg0)
		{
			unclickArrows();
		}
	}).duration(Duration.millis(100)).build();

	// timers to handle the holding-the-button
	final private Timer repeatDecrementClickTimer = new Timer(new Runnable()
	{
		@Override
		public void run()
		{
			getSkinnable().decrement();
		}
	}).withDelay(Duration.millis(500)).withCycleDuration(Duration.millis(100));
	final private Timer repeatIncrementClickTimer = new Timer(new Runnable()
	{
		@Override
		public void run()
		{
			getSkinnable().increment();
		}
	}).withDelay(Duration.millis(500)).withCycleDuration(Duration.millis(100));

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
	
	/*
	 * 
	 */
	private void unclickArrows()
	{
		decrementArrow.getStyleClass().remove("clicked");
		incrementArrow.getStyleClass().remove("clicked");
	}
	
	/*
	 * 
	 */
	private void replaceValueNode()
	{
		// if not editable
		if (getSkinnable().isEditable() == false)
		{
			// use the cell factory
			valueNode = getSkinnable().getCellFactory().call(getSkinnable());
		}
		else
		{
			// use the textfield
			if (textField == null) 
			{
				textField = new TextField();
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
			}
			valueNode = textField;
		}	
		
		// replace node
		gridPane.getChildren().remove(valueNode);
		gridPane.add(valueNode, valueNodeX, valueNodeY, valueNodeW, valueNodeH);
	}
	private TextField textField = null;
	
	
	// ==================================================================================================================
	// EDITABLE
	
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
	
	/*
	 * Places the node.
	 * This code pretty much is prepared to support any layout, but for now we stick to two.
	 */
	private void setArrowLocation()
	{
		// get the things we decide on
		ArrowDirection lArrowDirection = getSkinnable().getArrowDirection();
		
		// get helper values
		ColumnConstraints lColumnGrowing = new ColumnConstraints(5, 10, Double.MAX_VALUE);
		lColumnGrowing.setHgrow(Priority.ALWAYS);
		ColumnConstraints lColumnArrow = new ColumnConstraints(10);
		
		// get helper values
		RowConstraints lRowGrowing = new RowConstraints(5, 10, Double.MAX_VALUE);
		lRowGrowing.setVgrow(Priority.ALWAYS);
		RowConstraints lRowArrow = new RowConstraints(5);

		// clear the grid
		gridPane.getChildren().clear();
		gridPane.getColumnConstraints().clear();
		gridPane.getRowConstraints().clear();
		//gridPane.setGridLinesVisible(true);
		
		if (lArrowDirection == ArrowDirection.HORIZONTAL)
		{
			// construct a gridpane: one row, three columns: arrow, value, arrow
			gridPane.setHgap(3);
			gridPane.setVgap(0);
			gridPane.add(decrementArrow, 0, 0);
			valueNodeX = 1;
			valueNodeY = 0;
			valueNodeW = 1;
			valueNodeH = 1;
			gridPane.add(incrementArrow, 2, 0);
			gridPane.getColumnConstraints().addAll(lColumnArrow, lColumnGrowing, lColumnArrow); 
		}
		if (lArrowDirection == ArrowDirection.VERTICAL)
		{
			// construct a gridpane: two rows, two columns: value, arrows on top
			gridPane.setHgap(3);
			gridPane.setVgap(0);
			valueNodeX = 0;
			valueNodeY = 0;
			valueNodeW = 1;
			valueNodeH = 2;
			gridPane.add(incrementArrow, 1, 0);
			gridPane.add(decrementArrow, 1, 1);
			gridPane.getColumnConstraints().addAll(lColumnGrowing, lColumnArrow); 
			gridPane.getRowConstraints().addAll(lRowGrowing, lRowGrowing);
		}
		
		// place value node
		replaceValueNode();
	}
	private int valueNodeX = 0;
	private int valueNodeY = 0;
	private int valueNodeW = 1;
	private int valueNodeH = 1;
	
	/*
	 * Set the CSS so the correct arrows are shown
	 */
	private void setArrowCSS()
	{
		if (getSkinnable().getArrowDirection().equals(Spinner.ArrowDirection.HORIZONTAL))
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
