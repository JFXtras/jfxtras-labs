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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollBar;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import jfxtras.labs.internal.scene.control.behavior.TimePickerBehavior;
import jfxtras.labs.scene.control.TimePicker;

import com.sun.javafx.scene.control.skin.SkinBase;

/**
 * @author Tom Eugelink
 *
 */
public class TimePickerSkin extends SkinBase<TimePicker, TimePickerBehavior>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public TimePickerSkin(TimePicker control)
	{
		super(control, new TimePickerBehavior(control));
		construct();
	}

	/*
	 * construct the component
	 */
	private void construct()
	{	
		// setup component
		createNodes();
		
		// react to changes in the calendar 
		getSkinnable().calendarProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				// paint
				refresh();
			} 
		});
		refresh();

//		// react to changes in the minuteStep 
//		getSkinnable().minuteStepProperty().addListener(new InvalidationListener() 
//		{
//			@Override
//			public void invalidated(Observable observable)
//			{
//				minuteScrollBar.setBlockIncrement(getSkinnable().getMinuteStep().doubleValue());
//				minuteScrollBar.setUnitIncrement(getSkinnable().getMinuteStep().doubleValue());
//			} 
//		});
//		minuteScrollBar.setBlockIncrement(getSkinnable().getMinuteStep().doubleValue());
//		minuteScrollBar.setUnitIncrement(getSkinnable().getMinuteStep().doubleValue());
	}
	
	// ==================================================================================================================
	// PROPERTIES
	

	// ==================================================================================================================
	// DRAW
	
    /**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// two scrollbars
		hourScrollBar.minProperty().set(00);
		hourScrollBar.maxProperty().set(23);
		minuteScrollBar.minProperty().set(00);
		minuteScrollBar.maxProperty().set(59);
		VBox lVBox = new VBox(0);
		lVBox.alignmentProperty().set(Pos.CENTER);
		lVBox.getChildren().add(hourScrollBar);
		lVBox.getChildren().add(minuteScrollBar);
		getChildren().add(lVBox);
		hourScrollBar.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				Calendar lCalendar = (Calendar)getSkinnable().getCalendar().clone();
				lCalendar.set(Calendar.HOUR_OF_DAY, newValue.intValue());
				getSkinnable().setCalendar(lCalendar);
			}
		});
		minuteScrollBar.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				Calendar lCalendar = (Calendar)getSkinnable().getCalendar().clone();
				int lValue = newValue.intValue();
				int lStepSize = getSkinnable().minuteStepProperty().get().intValue();
				if (lStepSize > 1)
				{
					lValue = (lValue + (lStepSize / 2)) / lStepSize;
					lValue *= lStepSize;
					if (lValue > 59) lValue = 59;
				}
				lCalendar.set(Calendar.MINUTE, lValue);
				getSkinnable().setCalendar(lCalendar);
			}
		});
		
		// add label
		timeText.setOpacity(0.5);
		timeText.setDisable(true);
		getChildren().add(timeText);
		
		// add self as CSS style
		this.getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control		
	}
	final private ScrollBar hourScrollBar = new ScrollBar();
	final private ScrollBar minuteScrollBar = new ScrollBar();
	final private Text timeText = new Text("XX:XX");
	
	/**
	 * 
	 */
	private void refresh()
	{
		Calendar lCalendar = getSkinnable().getCalendar();
		int lHour = lCalendar.get(Calendar.HOUR_OF_DAY);
		int lMinute = lCalendar.get(Calendar.MINUTE);
		hourScrollBar.valueProperty().set(lHour);
		minuteScrollBar.valueProperty().set(lMinute);
		timeText.setText( (lHour < 10 ? "0" : "") + lHour + ":" + (lMinute < 10 ? "0" : "") + lMinute);
	}
}
