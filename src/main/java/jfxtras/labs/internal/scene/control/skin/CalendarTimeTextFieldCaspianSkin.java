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

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
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
import jfxtras.labs.internal.scene.control.behavior.CalendarTimeTextFieldBehavior;
import jfxtras.labs.scene.control.CalendarTimeTextField;
import jfxtras.labs.scene.control.CalendarTimePicker;
import jfxtras.labs.util.NodeUtil;

import com.sun.javafx.scene.control.skin.SkinBase;

/**
 * 
 * @author Tom Eugelink
 * 
 * Possible extension: drop down list or grid for quick selection
 */
public class CalendarTimeTextFieldCaspianSkin extends SkinBase<CalendarTimeTextField, CalendarTimeTextFieldBehavior>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarTimeTextFieldCaspianSkin(CalendarTimeTextField control)
	{
		super(control, new CalendarTimeTextFieldBehavior(control));
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
		getSkinnable().valueProperty().addListener(new InvalidationListener() // invalidation is also fired if there is no change (which is the case if a time if blocked to the existing time), but the text must be refreshed
		{
			
			@Override
			public void invalidated(Observable arg0)
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
		String s = CalendarTimePickerSkin.calendarTimeToText(c);
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
					if (keyEvent.isControlDown()) lCalendar.add(Calendar.HOUR_OF_DAY, keyEvent.getCode() == KeyCode.UP ? 1 : -1);
					else lCalendar.add(Calendar.MINUTE, keyEvent.getCode() == KeyCode.UP ? getSkinnable().getMinuteStep() : -1 * getSkinnable().getMinuteStep());
					
					// set it
					getSkinnable().setValue( CalendarTimePickerSkin.blockMinutesToStep(lCalendar, getSkinnable().getMinuteStep()) );
				}
			}
		});
		// bind the textField's tooltip to our (so it will show up) and give it a default value describing the mutation features
		// TODO: internationalize the tooltip
		Bindings.bindBidirectional(textField.tooltipProperty(), getSkinnable().tooltipProperty()); // order is important, because the value of the first field is overwritten initially with the value of the last field
		textField.setTooltip(new Tooltip("Type a time or use # for now, or +/-<number>[h|m] for delta's (for example: -3m for minus 3 minutes)\nUse cursor up and down plus optional ctrl (hour) for quick keyboard changes."));

		// the icon
		Image lImage = new Image(this.getClass().getResourceAsStream(this.getClass().getSimpleName() + "Icon.png"));
		imageView = new ImageView(lImage);
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
		TimePicker = new CalendarTimePicker();
		// bind our properties to the picker's 
		Bindings.bindBidirectional(TimePicker.calendarProperty(), getSkinnable().valueProperty()); // order is important, because the value of the first field is overwritten initially with the value of the last field
		Bindings.bindBidirectional(TimePicker.minuteStepProperty(), getSkinnable().minuteStepProperty()); // order is important, because the value of the first field is overwritten initially with the value of the last field
	}
	private TextField textField = null;
	private ImageView imageView = null;
	private GridPane gridPane = null;
	private CalendarTimePicker TimePicker = null;
	
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
				
				// special units hour, minute
				// TODO: internationalize?
				int lUnit = Calendar.DATE;
				if (lText.toLowerCase().endsWith("m")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.MINUTE; }
				if (lText.toLowerCase().endsWith("h")) { lText = lText.substring(0, lText.length() - 1); lUnit = Calendar.HOUR_OF_DAY; }
				
				// parse the delta
				int lDelta = Integer.parseInt(lText);
				Calendar lCalendar = (Calendar)getSkinnable().getValue().clone(); // TODO locale
				lCalendar.add(lUnit, lDelta);
				
				// set the value
				getSkinnable().setValue( CalendarTimePickerSkin.blockMinutesToStep(lCalendar, getSkinnable().getMinuteStep()) );
			}
			else if (lText.equals("#"))
			{
				// set the value
				getSkinnable().setValue( CalendarTimePickerSkin.blockMinutesToStep(Calendar.getInstance(), getSkinnable().getMinuteStep()) ); // TODO locale
			}
			else
			{
				// parse using the formatter
				int lIdx = lText.indexOf(":");
				if (lIdx > 0)
				{
					int lHour = Integer.parseInt(lText.substring(0, lIdx));
					int lMinute = Integer.parseInt(lText.substring(lIdx + 1));
					Calendar lCalendar = (getSkinnable().getValue() != null ? (Calendar)getSkinnable().getValue().clone() : Calendar.getInstance());
					lCalendar.set(Calendar.HOUR_OF_DAY, lHour);
					lCalendar.set(Calendar.MINUTE, lMinute);
					getSkinnable().setValue( CalendarTimePickerSkin.blockMinutesToStep(lCalendar, getSkinnable().getMinuteStep()) );
				}
				else 
				{
					refreshValue();
				}
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
			lBorderPane.setCenter(TimePicker);
			
			Button lButton = new Button("close");
			lButton.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent arg0)
				{
					popup.hide();
				}
			});
			lBorderPane.bottomProperty().set(lButton);
			
			popup.getContent().add(lBorderPane);
		}
		
		// show it just below the textfield
		popup.show(textField, NodeUtil.screenX(getSkinnable()), NodeUtil.screenY(getSkinnable()) + textField.getHeight());
		
		// move the focus over
		// TODO: not working
		//TimePicker.requestFocus();
	}
	private Popup popup = null;
}
