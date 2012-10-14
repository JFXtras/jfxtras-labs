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

import java.util.Calendar;
import java.util.Date;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Popup;
import jfxtras.labs.internal.scene.control.behavior.CalendarTextFieldBehavior;
import jfxtras.labs.scene.control.CalendarPicker;
import jfxtras.labs.scene.control.CalendarTextField;
import jfxtras.labs.util.NodeUtil;

import com.sun.javafx.scene.control.skin.SkinBase;

/**
 * 
 * @author Tom Eugelink
 * 
 * Possible extension: drop down list or grid for quick selection
 */
public class CalendarTextFieldCaspianSkin extends SkinBase<CalendarTextField, CalendarTextFieldBehavior>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarTextFieldCaspianSkin(CalendarTextField control)
	{
		super(control, new CalendarTextFieldBehavior(control));
		construct();
		// show where the skin is loaded from (for debugging in Ensemble) System.out.println("!!! " + this.getClass().getProtectionDomain().getCodeSource().getLocation());
	}

	/*
	 * 
	 */
	private void construct()
	{
		// setup component
		createNodes();
		
		// react to value changes in the model
		getSkinnable().valueProperty().addListener(new ChangeListener<Calendar>()
		{
			@Override
			public void changed(ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue)
			{
				refreshValue();
			}
		});
		refreshValue();
	}
	
	/*
	 * 
	 */
	private void refreshValue()
	{
		// write out to textfield
		Calendar c = getSkinnable().getValue();
		String s = c == null ? "" : getSkinnable().getDateFormat().format( c.getTime() );
		textField.setText( s );
	}
	
	// ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// the main textField
		textField = new TextField();
		textField.setPrefColumnCount(20);
		textField.focusedProperty().addListener(new InvalidationListener()
		{			
			@Override
			public void invalidated(Observable arg0)
			{
				if (textField.isFocused() == false) 
				{
					parse();
				}
			}
		});
		textField.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent evt)
			{
				parse();
			}
		});
		textField.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent keyEvent)
			{
				if (keyEvent.getCode() == KeyCode.UP || keyEvent.getCode() == KeyCode.DOWN)
				{
					// parse the content
					parse();
					
					// get the calendar to modify
					Calendar lCalendar = (Calendar)getSkinnable().getValue().clone();
					
					// modify
					int lField = Calendar.DATE;
					if (keyEvent.isShiftDown() == false && keyEvent.isControlDown()) lField = Calendar.MONTH;
					if (keyEvent.isShiftDown() == false && keyEvent.isAltDown()) lField = Calendar.YEAR;
					if (keyEvent.isShiftDown() == true && keyEvent.isControlDown() && getSkinnable().showTimeProperty().get()) lField = Calendar.HOUR_OF_DAY;
					if (keyEvent.isShiftDown() == true && keyEvent.isAltDown() && getSkinnable().showTimeProperty().get()) lField = Calendar.MINUTE;
					lCalendar.add(lField, keyEvent.getCode() == KeyCode.UP ? 1 : -1);
					
					// set it
					getSkinnable().setValue(lCalendar);
				}
			}
		});
		// bind the textField's tooltip to our (so it will show up) and give it a default value describing the mutation features
		// TODO: internationalize the tooltip
		textField.tooltipProperty().bind(getSkinnable().tooltipProperty()); 
		getSkinnable().setTooltip(new Tooltip("Type a date or use # for today, or +/-<number>[d|w|m|y] for delta's (for example: -3m for minus 3 months)\nUse cursor up and down plus optional shift (week), ctrl (month) or alt (year) for quick keyboard changes."));
        textField.promptTextProperty().bind(getSkinnable().promptTextProperty());

		// the icon
		Image lImage = new Image(this.getClass().getResourceAsStream(this.getClass().getSimpleName() + "Icon.png"));
		imageView = new ImageView(lImage);
		imageView.setPickOnBounds(true);
		imageView.setOnMouseClicked(new EventHandler<MouseEvent>()
		{
			@Override public void handle(MouseEvent evt)
			{
				if (textField.focusedProperty().get() == true) {
					parse();
				}
				showPopup(evt);
			}
		});
		
		// construct a gridpane: one row, two columns
		gridPane = new GridPane();
		gridPane.setHgap(3);
		gridPane.add(textField, 0, 0);
		gridPane.add(imageView, 1, 0);
		ColumnConstraints column0 = new ColumnConstraints(100, 10, Double.MAX_VALUE);
		column0.setHgrow(Priority.ALWAYS);
		gridPane.getColumnConstraints().addAll(column0); // first column gets any extra width
		
		// add to self
		this.getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
		getChildren().add(gridPane);
		
		// focus
		this.focusedProperty().addListener(new ChangeListener<Boolean>()
		{
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue)
			{
				if (newValue == true)
				{
					textField.requestFocus();
				}
			}
		});
		
		// prep the picker
		calendarPicker = new CalendarPicker();
		calendarPicker.setMode(CalendarPicker.Mode.SINGLE);
		calendarPicker.showTimeProperty().bind(getSkinnable().showTimeProperty());
		// bind our properties to the picker's 
		Bindings.bindBidirectional(calendarPicker.localeProperty(), getSkinnable().localeProperty()); // order is important, because the value of the first field is overwritten initially with the value of the last field
		Bindings.bindBidirectional(calendarPicker.calendarProperty(), getSkinnable().valueProperty()); // order is important, because the value of the first field is overwritten initially with the value of the last field
		calendarPicker.calendarProperty().addListener(new ChangeListener<Calendar>()
		{
			@Override
			public void changed(ObservableValue<? extends Calendar> observable, Calendar oldValue, Calendar newValue)
			{
				if (popup != null && getSkinnable().showTimeProperty().get() == false) 
				{
					popup.hide(); popup = null;
				}
			}
		});
		
		// close icon
		closeIconImage = new Image(this.getClass().getResourceAsStream(this.getClass().getSimpleName() + "CloseWindowIcon.png"));
	}
	private TextField textField = null;
	private ImageView imageView = null;
	private GridPane gridPane = null;
	private CalendarPicker calendarPicker = null;
	private Image closeIconImage = null;

	/**
	 * parse the contents that was typed in the textfield
	 */
	private void parse()
	{
		try
		{
			// get the text to parse
			String lText = textField.getText();
			lText = lText.trim();
			if (lText.length() == 0) 
			{
				getSkinnable().setValue(null);
				return;
			}
			
			// starts with - means substract days
			if (lText.startsWith("-") || lText.startsWith("+"))
			{
				// + has problems 
				if (lText.startsWith("+")) lText = lText.substring(1);
				
				// special units Day, Week, Month, Year
				// TODO: internationalize?
				int lUnit = Calendar.DATE;
				if (lText.toLowerCase().endsWith("d")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.DATE; }
				if (lText.toLowerCase().endsWith("w")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.WEEK_OF_YEAR; }
				if (lText.toLowerCase().endsWith("m")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.MONTH; }
				if (lText.toLowerCase().endsWith("y")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.YEAR; }
				
				// parse the delta
				int lDelta = Integer.parseInt(lText);
				Calendar lCalendar = (Calendar)getSkinnable().getValue().clone(); // TODO locale
				lCalendar.add(lUnit, lDelta);
				
				// set the value
				getSkinnable().setValue(lCalendar);
			}
			else if (lText.equals("#"))
			{
				// set the value
				getSkinnable().setValue(Calendar.getInstance()); // TODO locale
			}
			else
			{
				// parse using the formatter
				Date lDate = getSkinnable().getDateFormat().parse( lText );
				Calendar lCalendar = Calendar.getInstance(); // TODO: how to get the correct locale
				lCalendar.setTime(lDate);
				
				// set the value
				getSkinnable().setValue(lCalendar);
			}
		}
		catch (Throwable t) 
		{ 
			// TODO: show error in popup
			t.printStackTrace();
		} 
	}
	
	/*
	 * 
	 */
	private void showPopup(MouseEvent evt)
	{
		// create popup
		if (popup == null)
		{
			popup = new Popup();
			popup.setAutoFix(true);
			popup.setAutoHide(true);
			popup.setHideOnEscape(true);
			
			BorderPane lBorderPane = new BorderPane();
			lBorderPane.getStyleClass().add(this.getClass().getSimpleName() + "_popup");
			lBorderPane.setCenter(calendarPicker);
			
			// add a close button
			if (getSkinnable().showTimeProperty().get() == true)
			{
				ImageView lImageView = new ImageView(closeIconImage);
				lImageView.setPickOnBounds(true);
				lImageView.setOnMouseClicked(new EventHandler<MouseEvent>()
				{
					@Override public void handle(MouseEvent evt)
					{
						popup.hide(); popup = null;
					}
				});
				lBorderPane.rightProperty().set(lImageView);
			}
			
			// add to popup
			popup.getContent().add(lBorderPane);
		}
		
		// show it just below the textfield
		popup.show(textField, NodeUtil.screenX(getSkinnable()), NodeUtil.screenY(getSkinnable()) + textField.getHeight());
		
		// move the focus over
		// TODO: not working
		calendarPicker.requestFocus();
	}
	private Popup popup = null;
}
