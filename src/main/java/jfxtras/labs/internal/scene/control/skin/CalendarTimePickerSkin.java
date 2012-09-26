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
import javafx.scene.control.Slider;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import jfxtras.labs.internal.scene.control.behavior.CalendarTimePickerBehavior;
import jfxtras.labs.scene.control.CalendarTimePicker;

import com.sun.javafx.scene.control.skin.SkinBase;

/**
 * @author Tom Eugelink
 *
 */
public class CalendarTimePickerSkin extends SkinBase<CalendarTimePicker, CalendarTimePickerBehavior>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarTimePickerSkin(CalendarTimePicker control)
	{
		super(control, new CalendarTimePickerBehavior(control));
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

		// react to changes in the minuteStep 
		getSkinnable().minuteStepProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				minuteScrollSlider.setBlockIncrement(getSkinnable().getMinuteStep().doubleValue());
//				minuteScrollBar.setUnitIncrement(getSkinnable().getMinuteStep().doubleValue());
			} 
		});
		minuteScrollSlider.setBlockIncrement(getSkinnable().getMinuteStep().doubleValue());
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
		// two sliders
		hourScrollSlider.minProperty().set(00);
		hourScrollSlider.maxProperty().set(23);
//		hourScrollSlider.setShowTickLabels(true);
//		hourScrollSlider.setShowTickMarks(true);
		hourScrollSlider.setMajorTickUnit(12);
		hourScrollSlider.setMinorTickCount(3);
		minuteScrollSlider.minProperty().set(00);
		minuteScrollSlider.maxProperty().set(59);
		minuteScrollSlider.setShowTickLabels(true);
		minuteScrollSlider.setShowTickMarks(true);
		minuteScrollSlider.setMajorTickUnit(10);
		hourScrollSlider.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				Calendar lCalendar = (Calendar)getSkinnable().getCalendar();
				lCalendar = (lCalendar == null ? Calendar.getInstance() : (Calendar)lCalendar.clone());
				lCalendar.set(Calendar.HOUR_OF_DAY, newValue.intValue());
				getSkinnable().setCalendar(lCalendar);
			}
		});
		minuteScrollSlider.valueProperty().addListener(new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				Calendar lCalendar = (Calendar)getSkinnable().getCalendar();
				lCalendar = (lCalendar == null ? Calendar.getInstance() : (Calendar)lCalendar.clone());
				
				// in order no to first set a non stepsize calendar, we step the minutes here 
				int lMinutes = newValue.intValue();
				int lMinuteStep = getSkinnable().getMinuteStep();
				if (lMinuteStep > 1)
				{
					lMinutes += getSkinnable().getMinuteStep() / 2; // add half a step, so the scroller jumps to the next tick when the mouse is half way
					if (lMinutes > 59) lMinutes -= lMinuteStep;
				}
				lCalendar.set(Calendar.MINUTE, lMinutes);
				lCalendar = blockMinutesToStep(lCalendar, getSkinnable().getMinuteStep());
				getSkinnable().setCalendar(lCalendar);
			}
		});
		
		// add label
		timeText.setOpacity(0.5);
		timeText.setDisable(true);
		
		// layout
		// TODO: the timeText should be over the hour slider, now it is at the top
		VBox lVBox = new VBox(0);
		lVBox.alignmentProperty().set(Pos.CENTER);
		lVBox.getChildren().add(hourScrollSlider);
		lVBox.getChildren().add(minuteScrollSlider);
		getChildren().add(lVBox);
		getChildren().add(timeText);
//		StackPane.setAlignment(timeText, Pos.TOP_CENTER);

		// add self as CSS style
		this.getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control		
	}
	final private Slider hourScrollSlider = new Slider();
	final private Slider minuteScrollSlider = new Slider();
	final private Text timeText = new Text("XX:XX");
	
	/**
	 * 
	 */
	private void refresh()
	{
		Calendar lCalendar = getSkinnable().getCalendar();
		int lHour = lCalendar == null ? 0 : lCalendar.get(Calendar.HOUR_OF_DAY);
		int lMinute = lCalendar == null ? 0 : lCalendar.get(Calendar.MINUTE);
		hourScrollSlider.valueProperty().set(lHour);
		minuteScrollSlider.valueProperty().set(lMinute);
		timeText.setText( calendarTimeToText(lCalendar));
	}
	
	/**
	 * 
	 * @param calendar
	 * @return
	 */
	static public String calendarTimeToText(Calendar calendar)
	{
		if (calendar == null) return "";
		int lHour = calendar.get(Calendar.HOUR_OF_DAY);
		int lMinute = calendar.get(Calendar.MINUTE);
		String lText = (lHour < 10 ? "0" : "") + lHour + ":" + (lMinute < 10 ? "0" : "") + lMinute;
		return lText;
	}
	
	/**
	 * minutes fit in the minute steps
	 */
	static public Calendar blockMinutesToStep(Calendar calendar, Integer stepSize)
	{
		if (stepSize == null || calendar == null) return calendar;
			
		// set the minutes to match the step size
		int lMinutes = calendar.get(Calendar.MINUTE);
		if (stepSize == 1) return calendar;
		lMinutes = lMinutes / stepSize; // trunk
		lMinutes *= stepSize;
		if (calendar.get(Calendar.MINUTE) != lMinutes)
		{
			Calendar lCalendar = (Calendar)calendar.clone();
			lCalendar.set(Calendar.MINUTE, lMinutes);
			calendar = lCalendar;
		}
		return calendar;
	}


}
