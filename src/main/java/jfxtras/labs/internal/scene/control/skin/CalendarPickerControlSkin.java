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
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import jfxtras.labs.scene.control.CalendarPicker;
import jfxtras.labs.scene.control.CalendarTimePicker;
import jfxtras.labs.scene.control.ListSpinner;
import jfxtras.labs.scene.control.ListSpinner.CycleEvent;
import jfxtras.labs.scene.control.ListSpinnerIntegerList;

/**
 * This skin uses regular JavaFX controls
 * @author Tom Eugelink
 *
 */
public class CalendarPickerControlSkin extends CalendarPickerMonthlySkinAbstract<CalendarPickerControlSkin>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarPickerControlSkin(CalendarPicker control)
	{
		super(control);
		construct();
	}

	/*
	 * construct the component
	 */
	private void construct()
	{
		// setup component
		createNodes();
		
		// start listening to changes
		// if the calendar changes, the display calendar will jump to show that
		getSkinnable().calendarProperty().addListener(new InvalidationListener()
		{
			
			@Override
			public void invalidated(Observable arg0)
			{
				if (getSkinnable().getCalendar() != null) 
				{
					setDisplayedCalendar(getSkinnable().getCalendar());
				}
			} 
		});
		if (getSkinnable().getCalendar() != null) 
		{
			setDisplayedCalendar(getSkinnable().getCalendar());
		}
		
		// if the calendars change, the selection must be refreshed
		getSkinnable().calendars().addListener(new ListChangeListener<Calendar>()  
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Calendar> arg0)
			{
				refreshDayButtonToggleState();
			} 
		});
		
		// if the displayed calendar changes, the screen must be refreshed
		displayedCalendar().addListener(new InvalidationListener()
		{
			@Override
			public void invalidated(Observable arg0)
			{
				refresh();
			} 
		});
		
		// update the data
		refresh();
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
		// the result
		GridPane lGridPane = new GridPane();
		lGridPane.setVgap(2.0);
		lGridPane.setHgap(2.0);
		
		// setup the grid so all weekday togglebuttons will grow, but the weeknumbers do not
		ColumnConstraints lColumnConstraintsAlwaysGrow = new ColumnConstraints();
		lColumnConstraintsAlwaysGrow.setHgrow(Priority.ALWAYS);
		ColumnConstraints lColumnConstraintsNeverGrow = new ColumnConstraints();
		lColumnConstraintsNeverGrow.setHgrow(Priority.NEVER);
		lGridPane.getColumnConstraints().addAll(lColumnConstraintsNeverGrow, lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow, lColumnConstraintsAlwaysGrow);

		// month spinner
		List<String> lMonthLabels = getMonthLabels();
		monthListSpinner = new ListSpinner<String>(lMonthLabels).withIndex(Calendar.getInstance().get(Calendar.MONTH)).withCyclic(Boolean.TRUE);
		// on cycle overflow to year
		monthListSpinner.setOnCycle(new EventHandler<ListSpinner.CycleEvent>()
		{
			@Override
			public void handle(CycleEvent evt)
			{
				// if we've cycled down
				if (evt.cycledDown()) 
				{
					yearListSpinner.increment();
				}
				else 
				{
					yearListSpinner.decrement();				
				}
					 
			}
		});
		// if the value changed, update the displayed calendar
		monthListSpinner.valueProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue arg0, String arg1, String arg2)
			{
				setDisplayedCalendarFromSpinners();
			}
		});
		lGridPane.add(monthListSpinner, 1, 0, 4, 1); // col, row, hspan, vspan
		
		// year spinner
		yearListSpinner = new ListSpinner<Integer>(new ListSpinnerIntegerList()).withValue(Calendar.getInstance().get(Calendar.YEAR));
		// if the value changed, update the displayed calendar
		yearListSpinner.valueProperty().addListener(new ChangeListener<Integer>()
		{
			@Override
			public void changed(ObservableValue observableValue, Integer oldValue, Integer newValue)
			{
				setDisplayedCalendarFromSpinners();
			}
		});
		lGridPane.add(yearListSpinner, 5, 0, 3, 1); // col, row, hspan, vspan
		
		// double click here to show today
		Label lTodayLabel = new Label("   ");
		lTodayLabel.onMouseClickedProperty().set(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if (event.getClickCount() < 1) return;
				setDisplayedCalendarToToday();
			}
		});
		lGridPane.add(lTodayLabel, 0, 1);  // col, row
		
		// weekday labels
		for (int i = 0; i < 7; i++)
		{
			// create buttons
			Label lLabel = new Label("" + i);
			// style class is set together with the label
			lLabel.getStyleClass().add("weekday-label"); 
			lLabel.setMaxWidth(Integer.MAX_VALUE); // this is one of those times; why the @#$@#$@#$ do I need to specify this in order to make the damn label centered?
			
			// add it
			lGridPane.add(lLabel, i + 1, 1);  // col, row
			
			// remember the column it is associated with
			lLabel.setUserData(Integer.valueOf(i));
			lLabel.onMouseClickedProperty().set(weekdayLabelMouseClickedPropertyEventHandler);

			// remember it
			weekdayLabels.add(lLabel);
		}
		
		// weeknumber labels
		for (int i = 0; i < 6; i++)
		{
			// create buttons
			Label lLabel = new Label("" + i);
			lLabel.getStyleClass().add("weeknumber");
			lLabel.setAlignment(Pos.BASELINE_RIGHT);
			
			// remember it
			weeknumberLabels.add(lLabel);
			
			// remember the row it is associated with
			lLabel.setUserData(Integer.valueOf(i));
			lLabel.onMouseClickedProperty().set(weeknumerLabelMouseClickedPropertyEventHandler);

			// first of a row: add the weeknumber
			lGridPane.add(weeknumberLabels.get(i), 0, i + 2);  // col, row
		}
		
		// setup: 6 rows of 7 days per week (which is the maximum number of buttons required in the worst case layout)
		for (int i = 0; i < 6 * 7; i++)
		{
			// create buttons
			ToggleButton lToggleButton = new ToggleButton("" + i);
			lToggleButton.getStyleClass().add("day-button");
			lToggleButton.selectedProperty().addListener(toggleButtonSelectedPropertyChangeListener); // for minimal memory usage, use a single listener
			lToggleButton.onMouseReleasedProperty().set(toggleButtonMouseReleasedPropertyEventHandler); // for minimal memory usage, use a single listener
			lToggleButton.onKeyReleasedProperty().set(toggleButtonKeyReleasedPropertyEventHandler); // for minimal memory usage, use a single listener
			
			// remember which button belongs to this property
			booleanPropertyToDayToggleButtonMap.put(lToggleButton.selectedProperty(), lToggleButton);
			
			// add it
			lGridPane.add(lToggleButton, (i % 7) + 1, (i / 7) + 2);  // col, row
			lToggleButton.setMaxWidth(Double.MAX_VALUE); // make the button grow to fill a GridPane's cell
			lToggleButton.setAlignment(Pos.BASELINE_CENTER);
			
			// remember it
			dayButtons.add(lToggleButton);
		}

		// add timepicker
		// TODO: this is done upon construction, we need to make this dynamic based on Mode and showTime
		if (getSkinnable().getMode().equals(CalendarPicker.Mode.SINGLE) && getSkinnable().showTimeProperty().get() == true)
		{
			lGridPane.add(timePicker, 1, 8, 7, 1); // col, row, hspan, vspan
			Bindings.bindBidirectional(timePicker.calendarProperty(), getSkinnable().calendarProperty()); 
		}
		
		// add to self
		this.getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
		getChildren().add(lGridPane);
	}
	private ListSpinner<String> monthListSpinner = null;
	private ListSpinner<Integer> yearListSpinner = null;
	final private List<Label> weekdayLabels = new ArrayList<Label>();
	final private List<Label> weeknumberLabels = new ArrayList<Label>();
	final private List<ToggleButton> dayButtons = new ArrayList<ToggleButton>();
	final private CalendarTimePicker timePicker = new CalendarTimePicker();
	final private Map<BooleanProperty, ToggleButton> booleanPropertyToDayToggleButtonMap = new WeakHashMap<BooleanProperty, ToggleButton>();
	final private ChangeListener<Boolean> toggleButtonSelectedPropertyChangeListener = new ChangeListener<Boolean>()
	{
		@Override
		public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue)
		{
			refreshDayButtonToggleState();
		}
	};
	final private EventHandler<MouseEvent> toggleButtonMouseReleasedPropertyEventHandler = new EventHandler<MouseEvent>()
	{
		@Override
		public void handle(MouseEvent event)
		{
			ToggleButton lToggleButton = (ToggleButton)event.getSource();					
			toggle(lToggleButton, event.isShiftDown());
		}
	};
	final private EventHandler<KeyEvent> toggleButtonKeyReleasedPropertyEventHandler = new EventHandler<KeyEvent>()
	{
		@Override
		public void handle(KeyEvent event)
		{
			ToggleButton lToggleButton = (ToggleButton)event.getSource();
			if (" ".equals(event.getText()))
			{
				toggle(lToggleButton, event.isShiftDown());
			}
		}
	};
	final private EventHandler<MouseEvent> weekdayLabelMouseClickedPropertyEventHandler = new EventHandler<MouseEvent>()
	{
		@Override
		public void handle(MouseEvent event)
		{
			// in single or range mode this does not do anything
			if (getSkinnable().getMode() == CalendarPicker.Mode.SINGLE) return;
			if (getSkinnable().getMode() == CalendarPicker.Mode.RANGE) return;
			
			// process the calendars
			int lColIdx = ((Integer)((Label)event.getSource()).getUserData()).intValue();
			for (int lRowIdx = 0; lRowIdx < 6; lRowIdx++)
			{			
				int lIdx = (lRowIdx * 7) + lColIdx;
				ToggleButton lToggleButton = dayButtons.get(lIdx);	
				if (lToggleButton.isVisible() == true) toggle(lToggleButton, false);
			}
		}
	};
	final private EventHandler<MouseEvent> weeknumerLabelMouseClickedPropertyEventHandler = new EventHandler<MouseEvent>()
	{
		@Override
		public void handle(MouseEvent event)
		{
			// in single mode this does not do anything
			if (getSkinnable().getMode() == CalendarPicker.Mode.SINGLE) return;
			
			// in range mode we clear the existing selection
			if (getSkinnable().getMode() == CalendarPicker.Mode.RANGE)
			{
				getSkinnable().calendars().clear();
			}
			
			// process the calendars
			int lRowIdx = ((Integer)((Label)event.getSource()).getUserData()).intValue();
			for (int i = lRowIdx * 7; i < (lRowIdx * 7) + 7; i++)
			{			
				ToggleButton lToggleButton = dayButtons.get(i);	
				if (getSkinnable().getMode() == CalendarPicker.Mode.RANGE) 
				{
					getSkinnable().calendars().add( calendarForToggleButton(lToggleButton) );
				}
				else
				{
					toggle(lToggleButton, false);
				}
			}
		}
	};
		
	/**
	 * 
	 * @param toggleButton
	 * @return
	 */
	private Calendar calendarForToggleButton(ToggleButton toggleButton)
	{
		// base reference
		int lDayToggleButtonIdx = dayButtons.indexOf(toggleButton);
		
		// calculate the day-of-month
		int lFirstOfMonthIdx = determineFirstOfMonthDayOfWeek();
		int lDayOfMonth = lDayToggleButtonIdx - lFirstOfMonthIdx + 1;

		// create calendar representing the date that was toggled
		Calendar lToggledCalendar = (Calendar)getDisplayedCalendar().clone();
		lToggledCalendar.set(Calendar.YEAR, getDisplayedCalendar().get(Calendar.YEAR));
		lToggledCalendar.set(Calendar.MONTH, getDisplayedCalendar().get(Calendar.MONTH));
		lToggledCalendar.set(Calendar.DATE, lDayOfMonth);
		
		// include time
		if (timePicker.isVisible() && timePicker.getCalendar() != null)
		{
			lToggledCalendar.set(Calendar.HOUR_OF_DAY, timePicker.getCalendar().get(Calendar.HOUR_OF_DAY));
			lToggledCalendar.set(Calendar.MINUTE, timePicker.getCalendar().get(Calendar.MINUTE));
		}
		
		// return
		return lToggledCalendar;
	}
	
	/**
	 * 
	 * @param toggleButton
	 * @param shiftIsPressed
	 */
	private void toggle(ToggleButton toggleButton, boolean shiftIsPressed)		
	{
		// create calendar representing the date that was toggled
		Calendar lToggledCalendar = calendarForToggleButton(toggleButton);

		// select or deselect
		List<Calendar> lCalendars = getSkinnable().calendars();
		boolean lSelect = !contains(lCalendars, lToggledCalendar);  
		if (lSelect) 
		{
			// only add if not present
			lCalendars.add(lToggledCalendar);
			
			// make sure it adheres to the mode
			// SINGLE: clear all but the last selected
			while (getSkinnable().getMode() == CalendarPicker.Mode.SINGLE && lCalendars.size() > 1) 
			{
				lCalendars.remove(0);
			}
			// MULTIPLE: do nothing, just add the new one
			//           if shift is pressed, behave like RANGE below
			while (getSkinnable().getMode() == CalendarPicker.Mode.SINGLE && lCalendars.size() > 1) 
			{
				lCalendars.remove(0);
			}
			// RANGE: if shift is not pressed, behave like single
			//        if shift is pressed, also add the dates between 
			while (getSkinnable().getMode() == CalendarPicker.Mode.RANGE && shiftIsPressed == false && lCalendars.size() > 1) 
			{
				lCalendars.remove(0);
			}
			if ((getSkinnable().getMode() == CalendarPicker.Mode.MULTIPLE || getSkinnable().getMode() == CalendarPicker.Mode.RANGE) && shiftIsPressed == true) 
			{
				// we muust have a last selected			
				if (iLastSelected != null) 
				{
					// get the other calendar and make sure other <= toggle
					Calendar lOtherCalendar = iLastSelected;
					if (lOtherCalendar.after(lToggledCalendar))
					{
						Calendar lSwap = lOtherCalendar;
						lOtherCalendar = lToggledCalendar;
						lToggledCalendar = lSwap;
					}
					
					// walk towards the toggled date and add all in between
					Calendar lWalker = (Calendar)lOtherCalendar.clone(); // the @#$#@$@# calendars are mutable
					lWalker.add(Calendar.DATE, 1);
					while (lWalker.before(lToggledCalendar))
					{
						lCalendars.add((Calendar)lWalker.clone()); // the @#$#@$@# calendars are mutable
						lWalker.add(Calendar.DATE, 1);
					}
					
					// let's have a nice collection
					Collections.sort(lCalendars);
				}
			}
			
			// remember
			iLastSelected = (Calendar)lToggledCalendar.clone();
		}
		else 
		{
			// remove
			lCalendars.remove(lToggledCalendar);
			iLastSelected = null;
		}
		
		// make sure the buttons are toggled correctly
		refreshDayButtonToggleState();
	}
	private Calendar iLastSelected = null;

	/*
	 * 
	 */
	private void setDisplayedCalendarFromSpinners()
	{
		// get spinner values
		int lYear = yearListSpinner.getValue().intValue();
		int lMonth = monthListSpinner.getIndex();
		
		// get new calendar to display
		Calendar lCalendar = (Calendar)getDisplayedCalendar().clone();
		lCalendar.set(Calendar.YEAR, lYear);
		lCalendar.set(Calendar.MONTH, lMonth);
		
		// set it
		setDisplayedCalendar(lCalendar);
	}
	

	/*
	 * 
	 */
	private void setDisplayedCalendarToToday()
	{
		// get spinner values
		Calendar lTodayCalendar = Calendar.getInstance();
		
		// get new calendar to display
		Calendar lCalendar = (Calendar)getDisplayedCalendar().clone();
		lCalendar.set(Calendar.YEAR, lTodayCalendar.get(Calendar.YEAR));
		lCalendar.set(Calendar.MONTH, lTodayCalendar.get(Calendar.MONTH));
		
		// set it
		setDisplayedCalendar(lCalendar);
	}
	
	/**
	 * refresh all
	 */
	private void refresh()
	{
		refreshSpinner();
		refreshWeekdayLabels();
		refreshWeeknumberLabels();
		refreshDayButtonsVisibilityAndLabel();
		refreshDayButtonToggleState();
	}
	
	/*
	 * 
	 */
	private void refreshSpinner()
	{
		// get calendar
		Calendar lCalendar = (Calendar)getDisplayedCalendar();

		// get the value for the corresponding index and set that
		List<String> lMonthLabels = getMonthLabels();
		String lMonthLabel = lMonthLabels.get( lCalendar.get(Calendar.MONTH) ); 
		monthListSpinner.setValue( lMonthLabel );
		
		// set value
		yearListSpinner.setValue(lCalendar.get(Calendar.YEAR));
		
	}
	
	/*
	 * 
	 */
	private void refreshWeekdayLabels()
	{
		// get labels
		List<String> lWeekdayLabels = getWeekdayLabels();
		
		// set them
		for (int i = 0; i < weekdayLabels.size(); i++)
		{
			Label lLabel = weekdayLabels.get(i);
			lLabel.setText( lWeekdayLabels.get(i) );
			lLabel.getStyleClass().remove("weekend");
			lLabel.getStyleClass().remove("weekday");
			lLabel.getStyleClass().add(isWeekdayWeekend(i) ? "weekend" : "weekday"); 
		}
	}
	
	/*
	 * 
	 */
	private void refreshWeeknumberLabels()
	{
		// get labels
		List<Integer> lWeeknumbers = getWeeknumbers();
		
		// set them
		for (int i = 0; i < lWeeknumbers.size(); i++)
		{
			weeknumberLabels.get(i).setText( (lWeeknumbers.get(i).intValue() < 10 ? "0" : "") + lWeeknumbers.get(i).toString() );
		}
	}
	
	/*
	 * 
	 */
	private void refreshDayButtonsVisibilityAndLabel()
	{
		// setup the buttons [0..(6*7)-1]
		// displayed calendar always points to the 1st of the month
		int lFirstOfMonthIdx = determineFirstOfMonthDayOfWeek();

		// hide the preceeding buttons
		for (int i = 0; i < lFirstOfMonthIdx; i++)
		{
			dayButtons.get(i).setVisible(false);
		}
		// make all weeklabels invisible
		for (int i = 1; i < 6; i++)
		{
			weeknumberLabels.get(i).setVisible(false);
		}
		
		// set the month buttons
		int lDaysInMonth = determineDaysInMonth();
		Calendar lCalendar = (Calendar)getDisplayedCalendar().clone();
		for (int i = 1; i <= lDaysInMonth; i++)
		{
			// set the date
			lCalendar.set(java.util.Calendar.DATE, i);

			// determine the index in the buttons
			int lIdx = lFirstOfMonthIdx + i - 1;

			// update the button
			ToggleButton lToggleButton = dayButtons.get(lIdx); 
			lToggleButton.setVisible(true);
			lToggleButton.setText("" + i);
			lToggleButton.getStyleClass().remove("weekend");
			lToggleButton.getStyleClass().remove("weekday");
			lToggleButton.getStyleClass().add(isWeekdayWeekend(lIdx % 7) ? "weekend" : "weekday"); 

			// make the corresponding weeklabel visible
			weeknumberLabels.get(lIdx / 7).setVisible(true);

			// highlight today
			if (isToday(lCalendar))
			{
				lToggleButton.getStyleClass().add("today");
			}	
			else
			{
				lToggleButton.getStyleClass().remove("today");
			}
		}

		// hide the trailing buttons
		for (int i = lFirstOfMonthIdx + lDaysInMonth; i < 6*7; i++)
		{
			dayButtons.get(i).setVisible(false);
		}
	}

	/*
	 * 
	 */
	private void refreshDayButtonToggleState()
	{
		iRefreshingSelection.addAndGet(1);
		try
		{
			// setup the buttons [0..(6*7)-1]
			// displayed calendar always points to the 1st of the month
			int lFirstOfMonthIdx = determineFirstOfMonthDayOfWeek();
			List<Calendar> lCalendars = getSkinnable().calendars();
			
			// set the month buttons
			int lDaysInMonth = determineDaysInMonth();
			Calendar lCalendar = (Calendar)getDisplayedCalendar().clone();
			for (int i = 1; i <= lDaysInMonth; i++)
			{
				// set the date
				lCalendar.set(java.util.Calendar.DATE, i);
	
				// determine the index in the buttons
				int lIdx = lFirstOfMonthIdx + i - 1;
	
				// is selected
				boolean lSelected = contains(lCalendars, lCalendar);  
				dayButtons.get(lIdx).setSelected( lSelected );
			}
		}
		finally
		{
			iRefreshingSelection.addAndGet(-1);
		}
	}
	final private AtomicInteger iRefreshingSelection = new AtomicInteger(0);
	
	/**
	 * contains only check YMD
	 * @param calendars
	 * @param calendar
	 * @return
	 */
	private boolean contains(List<Calendar> calendars, Calendar calendar)
	{
		for (Calendar c : calendars)
		{
			if ( c.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)
			  && c.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
			  && c.get(Calendar.DATE) == calendar.get(Calendar.DATE)
			   )
			{
				return true;
			}
		}
		return false;
	}
}
