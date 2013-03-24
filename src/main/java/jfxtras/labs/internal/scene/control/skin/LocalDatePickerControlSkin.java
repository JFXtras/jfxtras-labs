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
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

import javax.time.calendar.DayOfWeek;
import javax.time.calendar.LocalDate;
import javax.time.calendar.format.DateTimeFormatter;
import javax.time.calendar.format.DateTimeFormatterBuilder;
import javax.time.calendar.format.DateTimeFormatters;

import jfxtras.labs.internal.scene.control.behavior.LocalDatePickerBehavior;
import jfxtras.labs.scene.control.LocalDatePicker;
import jfxtras.labs.scene.control.ListSpinner;
import jfxtras.labs.scene.control.ListSpinner.CycleEvent;
import jfxtras.labs.scene.control.ListSpinnerIntegerList;

import javafx.scene.control.SkinBase;

/**
 * This skin uses regular JavaFX controls
 * @author Tom Eugelink
 *
 */
public class LocalDatePickerControlSkin extends SkinBase<LocalDatePicker>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalDatePickerControlSkin(LocalDatePicker control)
	{
		super(control);//, new LocalDatePickerBehavior(control));
		construct();
	}

	/*
	 * construct the component
	 */
	private void construct()
	{
		// setup component
		createNodes();
		
		// set displayed date
		setDisplayedLocalDate(LocalDate.now());
		
		// react to changes in the locale 
		getSkinnable().localeProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				refreshLocale();
			} 
		});
		refreshLocale();
		
		// start listening to changes
		// if the LocalDate changes, the display LocalDate will jump to show that
		getSkinnable().localDateProperty().addListener(new ChangeListener<LocalDate>()  
		{
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue)
			{
				if (newValue != null) {
					setDisplayedLocalDate(newValue);
				}
			} 
		});
		if (getSkinnable().getLocalDate() != null) {
			setDisplayedLocalDate(getSkinnable().getLocalDate());
		}
		
		// if the LocalDates change, the selection must be refreshed
		getSkinnable().localDates().addListener(new ListChangeListener<LocalDate>()  
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends LocalDate> arg0)
			{
				refreshDayButtonToggleState();
			} 
		});
		
		// if the displayed LocalDate changes, the screen must be refreshed
		displayedLocalDate().addListener(new ChangeListener<LocalDate>()  
		{
			@Override
			public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue)
			{
				refresh();
			} 
		});
		
		// update the data
		refresh();
	}
	
	// ==================================================================================================================
	// PROPERTIES
	
	/** 
	 * DisplayedLocalDate: this LocalDate always points to the first of the month being shown, 
	 * it also is used to determine first-day-of-week, weekday labels, etc
	 * The LocalDate should not be modified using any of its add or set methods (it should be considered immutable)  
	 */
	public LocalDate getDisplayedLocalDate() { return displayedLocalDateObjectProperty.getValue(); }
	public void setDisplayedLocalDate(LocalDate value)  
	{
		// set value
		if (value.getDayOfMonth() != 1) value = LocalDate.of(value.getYear(), value.getMonthOfYear(), 1);
		displayedLocalDateObjectProperty.setValue( value ); 
	}
	public LocalDatePickerControlSkin withDisplayedLocalDate(LocalDate value) { setDisplayedLocalDate(value); return this; } 
	public ObjectProperty<LocalDate> displayedLocalDate() { return displayedLocalDateObjectProperty; }
	volatile private ObjectProperty<LocalDate> displayedLocalDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "displayedLocalDate");

	/**
	 * 
	 */
	private void refreshLocale()
	{
		// update the displayed LocalDate
		setDisplayedLocalDate(getDisplayedLocalDate());
	}
	

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
		monthXSpinner = new ListSpinner<String>(lMonthLabels).withIndex(LocalDate.now().getMonthOfYear().getValue()).withCyclic(Boolean.TRUE);
		// on cycle overflow to year
		monthXSpinner.setOnCycle(new EventHandler<ListSpinner.CycleEvent>()
		{
			@Override
			public void handle(CycleEvent evt)
			{
				// if we've cycled down
				if (evt.cycledDown()) 
				{
					yearXSpinner.increment();
				}
				else 
				{
					yearXSpinner.decrement();				
				}
					 
			}
		});
		// if the value changed, update the displayed LocalDate
		monthXSpinner.valueProperty().addListener(new ChangeListener<String>()
		{
			@Override
			public void changed(ObservableValue arg0, String arg1, String arg2)
			{
				setDisplayedLocalDateFromSpinners();
			}
		});
		lGridPane.add(monthXSpinner, 0, 0, 5, 1); // col, row, hspan, vspan
		
		// year spinner
		yearXSpinner = new ListSpinner<Integer>(new ListSpinnerIntegerList()).withValue(LocalDate.now().getYear());
		// if the value changed, update the displayed LocalDate
		yearXSpinner.valueProperty().addListener(new ChangeListener<Integer>()
		{
			@Override
			public void changed(ObservableValue observableValue, Integer oldValue, Integer newValue)
			{
				setDisplayedLocalDateFromSpinners();
			}
		});
		lGridPane.add(yearXSpinner, 5, 0, 3, 1); // col, row, hspan, vspan
		
		// double click here to show today
		Label lTodayLabel = new Label("   ");
		lTodayLabel.onMouseClickedProperty().set(new EventHandler<MouseEvent>()
		{
			@Override
			public void handle(MouseEvent event)
			{
				if (event.getClickCount() < 1) return;
				setDisplayedLocalDateToToday();
			}
		});
		lGridPane.add(lTodayLabel, 0, 1);  // col, row
		
		// weekday labels
		for (int i = 0; i < 7; i++)
		{
			// create buttons
			Label lLabel = new Label("" + i);
			// style class is set together with the label
			lLabel.setAlignment(Pos.CENTER);
			
			// add it
			lGridPane.add(lLabel, i + 1, 1);  // col, row
			
			// remember the column it is associated with
			lLabel.setUserData(Integer.valueOf(i));
			lLabel.onMouseClickedProperty().set(weekdayLabelMouseClickedPropertyEventHandler);

			// remember it
			weekdayLabels.add(lLabel);
			lLabel.setAlignment(Pos.BASELINE_CENTER); // TODO: not working
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
			lToggleButton.getStyleClass().add("day");
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

		// add to self
		getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
		getChildren().add(lGridPane);
	}
	private ListSpinner<String> monthXSpinner = null;
	private ListSpinner<Integer> yearXSpinner = null;
	final private List<Label> weekdayLabels = new ArrayList<Label>();
	final private List<Label> weeknumberLabels = new ArrayList<Label>();
	final private List<ToggleButton> dayButtons = new ArrayList<ToggleButton>();
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
			if (getSkinnable().getMode() == LocalDatePicker.Mode.SINGLE) return;
			if (getSkinnable().getMode() == LocalDatePicker.Mode.RANGE) return;
			
			// process the LocalDates
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
			if (getSkinnable().getMode() == LocalDatePicker.Mode.SINGLE) return;
			
			// in range mode we clear the existing selection
			if (getSkinnable().getMode() == LocalDatePicker.Mode.RANGE)
			{
				getSkinnable().localDates().clear();
			}
			
			// process the LocalDates
			int lRowIdx = ((Integer)((Label)event.getSource()).getUserData()).intValue();
			for (int i = lRowIdx * 7; i < (lRowIdx * 7) + 7; i++)
			{			
				ToggleButton lToggleButton = dayButtons.get(i);	
				if (getSkinnable().getMode() == LocalDatePicker.Mode.RANGE) 
				{
					getSkinnable().localDates().add( LocalDateForToggleButton(lToggleButton) );
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
	private LocalDate LocalDateForToggleButton(ToggleButton toggleButton)
	{
		// base reference
		int lDayToggleButtonIdx = dayButtons.indexOf(toggleButton);
		
		// calculate the day-of-month
		int lFirstOfMonthIdx = determineFirstOfMonthDayOfWeek();
		int lDayOfMonth = lDayToggleButtonIdx - lFirstOfMonthIdx + 1;

		// create LocalDate representing the date that was toggled
		LocalDate lToggledLocalDate = LocalDate.of(getDisplayedLocalDate().getYear(), getDisplayedLocalDate().getMonthOfYear().getValue(), lDayOfMonth);
		
		// return
		return lToggledLocalDate;
	}
	
	/**
	 * 
	 * @param toggleButton
	 * @param shiftIsPressed
	 */
	private void toggle(ToggleButton toggleButton, boolean shiftIsPressed)		
	{
		// create LocalDate representing the date that was toggled
		LocalDate lToggledLocalDate = LocalDateForToggleButton(toggleButton);

		// select or deselect
		List<LocalDate> lLocalDates = getSkinnable().localDates();
		boolean lSelect = !lLocalDates.contains(lToggledLocalDate); 
		if (lSelect) 
		{
			// only add if not present
			lLocalDates.add(lToggledLocalDate);
			
			// make sure it adheres to the mode
			// SINGLE: clear all but the last selected
			while (getSkinnable().getMode() == LocalDatePicker.Mode.SINGLE && lLocalDates.size() > 1) 
			{
				lLocalDates.remove(0);
			}
			// MULTIPLE: do nothing, just add the new one
			//           if shift is pressed, behave like RANGE below
			while (getSkinnable().getMode() == LocalDatePicker.Mode.SINGLE && lLocalDates.size() > 1) 
			{
				lLocalDates.remove(0);
			}
			// RANGE: if shift is not pressed, behave like single
			//        if shift is pressed, also add the dates between 
			while (getSkinnable().getMode() == LocalDatePicker.Mode.RANGE && shiftIsPressed == false && lLocalDates.size() > 1) 
			{
				lLocalDates.remove(0);
			}
			if ((getSkinnable().getMode() == LocalDatePicker.Mode.MULTIPLE || getSkinnable().getMode() == LocalDatePicker.Mode.RANGE) && shiftIsPressed == true) 
			{
				// we must have a last selected			
				if (iLastSelected != null) 
				{
					// get the other LocalDate and make sure other <= toggle
					LocalDate lPreviousSelectedLocalDate = iLastSelected;
					int lDirection = (lPreviousSelectedLocalDate.isAfter(lToggledLocalDate) ? -1 : 1);
					
					// walk towards the toggled date and add all in between
					LocalDate lWalker = lPreviousSelectedLocalDate;
					lWalker = lWalker.plusDays(lDirection);
					while (!lWalker.equals(lToggledLocalDate))
					{
						lLocalDates.add(lWalker); 
						lWalker = lWalker.plusDays(lDirection);
					}
					lLocalDates.remove(lToggledLocalDate); // make sure this is added last 
					lLocalDates.add(lToggledLocalDate); // make sure this is added last 
				}
			}
			
			// remember
			iLastSelected = lToggledLocalDate;
		}
		else 
		{
			// remove
			lLocalDates.remove(lToggledLocalDate);
			iLastSelected = null;
		}
		
		// make sure the buttons are toggled correctly
		refreshDayButtonToggleState();
	}
	private LocalDate iLastSelected = null;

	/*
	 * 
	 */
	private void setDisplayedLocalDateFromSpinners()
	{
		// get spinner values
		int lYear = yearXSpinner.getValue().intValue();
		int lMonth = monthXSpinner.getIndex();
		
		// get new LocalDate to display
		LocalDate lLocalDate = LocalDate.of(lYear, lMonth, 1);
		
		// set it
		setDisplayedLocalDate(lLocalDate);
	}
	

	/*
	 * 
	 */
	private void setDisplayedLocalDateToToday()
	{
		// get spinner values
		LocalDate lTodayLocalDate = LocalDate.now();
		
		// set it
		setDisplayedLocalDate(lTodayLocalDate);
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
		// get LocalDate
		LocalDate lLocalDate = (LocalDate)getDisplayedLocalDate();

		// get the value for the corresponding index and set that
		List<String> lMonthLabels = getMonthLabels();
		String lMonthLabel = lMonthLabels.get( lLocalDate.getMonthOfYear().getValue() ); 
		monthXSpinner.setValue( lMonthLabel );
		
		// set value
		yearXSpinner.setValue(lLocalDate.getYear());
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
			lLabel.getStyleClass().add(isWeekend(i) ? "weekend" : "weekday"); 
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
		// displayed LocalDate always points to the 1st of the month
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
		LocalDate lDisplayedLocalDate = getDisplayedLocalDate();
		for (int i = 1; i <= lDaysInMonth; i++)
		{
			// set the date
			LocalDate lLocalDate = LocalDate.of(lDisplayedLocalDate.getYear(), lDisplayedLocalDate.getMonthOfYear(), i);

			// determine the index in the buttons
			int lIdx = lFirstOfMonthIdx + i - 1;

			// update the button
			ToggleButton lToggleButton = dayButtons.get(lIdx); 
			lToggleButton.setVisible(true);
			lToggleButton.setText("" + i);
			lToggleButton.getStyleClass().remove("weekend");
			lToggleButton.getStyleClass().remove("weekday");
			lToggleButton.getStyleClass().add(isWeekend(lIdx % 7) ? "weekend" : "weekday"); 

			// make the corresponding weeklabel visible
			weeknumberLabels.get(lIdx / 7).setVisible(true);

			// highlight today
			if (isToday(lLocalDate))
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
			// displayed LocalDate always points to the 1st of the month
			int lFirstOfMonthIdx = determineFirstOfMonthDayOfWeek();
			List<LocalDate> lLocalDates = getSkinnable().localDates();
			
			// set the month buttons
			int lDaysInMonth = determineDaysInMonth();
			LocalDate lDisplayedLocalDate = getDisplayedLocalDate();
			for (int i = 1; i <= lDaysInMonth; i++)
			{
				// set the date
				LocalDate lLocalDate = LocalDate.of(lDisplayedLocalDate.getYear(), lDisplayedLocalDate.getMonthOfYear(), i);
	
				// determine the index in the buttons
				int lIdx = lFirstOfMonthIdx + i - 1;
	
				// is selected
				boolean lSelected = lLocalDates.contains(lLocalDate);
				dayButtons.get(lIdx).setSelected( lSelected );
			}
		}
		finally
		{
			iRefreshingSelection.addAndGet(-1);
		}
	}
	final private AtomicInteger iRefreshingSelection = new AtomicInteger(0);
	
	// ==================================================================================================================
	// SUPPORT
	
	/**
	 * get the weekday labels starting with the weekday that is the first-day-of-the-week according to the locale in the displayed LocalDate
	 */
	protected List<String> getWeekdayLabels()
	{
		// result
		List<String> lWeekdayLabels = new ArrayList<String>();

		// first day of week
		// 1 = monday, 7 = sunday
		int lFirstDayOfWeek = DayOfWeek.firstDayOfWeekFor(getSkinnable().getLocale()).getValue();

		// formatter
		DateTimeFormatter lDateTimeFormatter = new DateTimeFormatterBuilder()
			.append(DateTimeFormatters.pattern("E"))
			.toFormatter();
				
		// setup the dayLabels
		for (int i = 0; i < 7; i++)
		{
			// next
			LocalDate lLocalDate = LocalDate.of(2009, 7, 12 + lFirstDayOfWeek + i); // july 13th 2009 is a Monday, so a firstDayOfWeek=1 must come out of the 13th

			// assign day
			lWeekdayLabels.add( lDateTimeFormatter.withLocale(getSkinnable().getLocale()).print(lLocalDate) );
		}
		
		// done
		return lWeekdayLabels;
	}
	
	/**
	 * Get a list with the weeklabels
	 */
	protected List<Integer> getWeeknumbers()
	{
		// result
		List<Integer> lWeekLabels = new ArrayList<Integer>();

		// formatter
		DateTimeFormatter lDateTimeFormatter = new DateTimeFormatterBuilder()
			.append(DateTimeFormatters.pattern("ww"))
			.toFormatter();
				
		// setup the weekLabels
		LocalDate lLocalDate = getDisplayedLocalDate();
		for (int i = 0; i <= 5; i++)
		{
			// set label
			lWeekLabels.add( Integer.valueOf(lDateTimeFormatter.withLocale(getSkinnable().getLocale()).print(lLocalDate)) );

			// next week
			lLocalDate = lLocalDate.plusDays(7);
		}

		// done
		return lWeekLabels;
	}

	/**
	 * get the weekday labels starting with the weekday that is the first-day-of-the-week according to the locale in the displayed LocalDate
	 */
	protected List<String> getMonthLabels()
	{
		// result
		List<String> lMonthLabels = new ArrayList<String>();
		lMonthLabels.add("dummy"); // idx=0 is not used

		// formatter
		DateTimeFormatter lDateTimeFormatter = new DateTimeFormatterBuilder()
			.append(DateTimeFormatters.pattern("MMMM"))
			.toFormatter();
				
		// setup the month
		for (int i = 0; i < 12; i++)
		{
			// next
			LocalDate lLocalDate = LocalDate.of(2009, i + 1, 1); 

			// assign day
			lMonthLabels.add( lDateTimeFormatter.withLocale(getSkinnable().getLocale()).print(lLocalDate));
		}
		
		// done
		return lMonthLabels;
	}
	
	/**
	 * check if a certain weekday name is a certain day-of-the-week
	 */
	protected boolean isWeekend(int idx) 
	{
		// 1 = monday, 7 = sunday
		int lFirstDayOfWeek = DayOfWeek.firstDayOfWeekFor(getSkinnable().getLocale()).getValue();
		int lDayOfWeek = (lFirstDayOfWeek + idx - 1) % 7;
		return lDayOfWeek == 5 || lDayOfWeek == 6;
	}
	
	/**
	 * determine on which day of week idx the first of the months is
	 */
	protected int determineFirstOfMonthDayOfWeek()
	{
		// determine with which button to start
		int lFirstDayOfWeek = DayOfWeek.firstDayOfWeekFor(getSkinnable().getLocale()).getValue();
		int lFirstOfMonthIdx = getDisplayedLocalDate().getDayOfWeek().getValue() - lFirstDayOfWeek;
		if (lFirstOfMonthIdx < 0) lFirstOfMonthIdx += 7;
		return lFirstOfMonthIdx;
	}
	
	/**
	 * determine the number of days in the month
	 */
	protected int determineDaysInMonth()
	{
		// determine the number of days in the month
		LocalDate lLocalDate = getDisplayedLocalDate(); // always the 1st of the month
		lLocalDate = lLocalDate.plusMonths(1);
		lLocalDate = lLocalDate.plusDays(-1);
		int lDaysInMonth = lLocalDate.getDayOfMonth();
		return lDaysInMonth;
	}

	/**
	 * determine if a date is today
	 */
	protected boolean isToday(LocalDate localDate)
	{
		LocalDate lNow = LocalDate.now();
		return lNow.getYear() == localDate.getYear()
			&& lNow.getMonthOfYear().getValue() == localDate.getMonthOfYear().getValue()
			&& lNow.getDayOfMonth() == localDate.getDayOfMonth()
			;
	}
}
