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

import java.time.LocalDate;
import java.util.Calendar;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.SkinBase;
import javafx.scene.layout.BorderPane;
import jfxtras.labs.scene.control.CalendarPicker;
import jfxtras.labs.scene.control.LocalDatePicker;
import jfxtras.labs.util.DateTimeUtil;

/**
 * This skin uses regular JavaFX controls
 * @author Tom Eugelink
 *
 */
public class LocalDatePickerSkin extends SkinBase<LocalDatePicker>
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalDatePickerSkin(LocalDatePicker control)
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
		
		// bind it up
		getSkinnable().localeProperty().bindBidirectional( calendarPicker.localeProperty() );
		getSkinnable().allowNullProperty().bindBidirectional( calendarPicker.allowNullProperty() );
		getSkinnable().styleProperty().bindBidirectional( calendarPicker.styleProperty() );
		bindMode();
		bindLocalDate();
		bindLocalDates();
	}
	
	private void bindLocalDate()
	{
		// if this value is changed by binding, make sure related things are updated
		calendarPicker.calendarProperty().addListener( (ObservableValue<? extends Calendar> observableValue, Calendar oldValue, Calendar newValue) -> {
			getSkinnable().localDateProperty().set(DateTimeUtil.createLocalDateFromCalendar(newValue)); 
		});
		
		// if the localDate value is changed, make sure calendar is updated
		getSkinnable().localDateProperty().addListener( (ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue) -> {
			calendarPicker.calendarProperty().set(newValue == null ? null : DateTimeUtil.createCalendarFromLocalDate(newValue, calendarPicker.getLocale()));
		});
	}
	
	private void bindLocalDates()
	{
		// forward changes from calendar
		calendarPicker.calendars().addListener( (ListChangeListener.Change<? extends Calendar> change) -> {
			while (change.next())
			{
				for (Calendar lCalendar : change.getRemoved())
				{
					LocalDate lLocalDate = DateTimeUtil.createLocalDateFromCalendar(lCalendar);
					if (getSkinnable().localDateTimes().contains(lLocalDate)) {
						getSkinnable().localDateTimes().remove(lLocalDate);
					}				
				}
				for (Calendar lCalendar : change.getAddedSubList()) 
				{
					LocalDate lLocalDate = DateTimeUtil.createLocalDateFromCalendar(lCalendar);
					if (getSkinnable().localDateTimes().contains(lLocalDate) == false) {
						getSkinnable().localDateTimes().add(lLocalDate);
					}
				}
			}
		});
		// forward changes to calendar
		getSkinnable().localDateTimes().addListener( (ListChangeListener.Change<? extends LocalDate> change) -> {
			while (change.next()) {
				for (LocalDate lLocalDate : change.getRemoved()) {
					Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDate(lLocalDate, calendarPicker.getLocale());
					if (calendarPicker.calendars().contains(lCalendar)) {
						calendarPicker.calendars().remove(lCalendar);
					}
				}
				for (LocalDate lLocalDate : change.getAddedSubList()) {
					Calendar lCalendar = DateTimeUtil.createCalendarFromLocalDate(lLocalDate, calendarPicker.getLocale());
					if (calendarPicker.calendars().contains(lCalendar) == false) {
						calendarPicker.calendars().add(lCalendar);
					}
				}
			}
		});
	}


	private void bindMode()
	{
		// if this value is changed by binding, make sure related things are updated
		calendarPicker.modeProperty().addListener( (ObservableValue<? extends CalendarPicker.Mode> observableValue, CalendarPicker.Mode oldValue, CalendarPicker.Mode newValue) -> {
			if (newValue == CalendarPicker.Mode.SINGLE) {
				getSkinnable().modeProperty().set(LocalDatePicker.Mode.SINGLE); 
			}
			if (newValue == CalendarPicker.Mode.RANGE) {
				getSkinnable().modeProperty().set(LocalDatePicker.Mode.RANGE); 
			}
			if (newValue == CalendarPicker.Mode.MULTIPLE) {
				getSkinnable().modeProperty().set(LocalDatePicker.Mode.MULTIPLE); 
			}
		});
		
		// if the localDate value is changed, make sure calendar is updated
		getSkinnable().modeProperty().addListener( (ObservableValue<? extends LocalDatePicker.Mode> observableValue, LocalDatePicker.Mode oldValue, LocalDatePicker.Mode newValue) -> {
			if (newValue == LocalDatePicker.Mode.SINGLE) {
				calendarPicker.modeProperty().set(CalendarPicker.Mode.SINGLE); 
			}
			if (newValue == LocalDatePicker.Mode.RANGE) {
				calendarPicker.modeProperty().set(CalendarPicker.Mode.RANGE); 
			}
			if (newValue == LocalDatePicker.Mode.MULTIPLE) {
				calendarPicker.modeProperty().set(CalendarPicker.Mode.MULTIPLE); 
			}
		});
	}


    // ==================================================================================================================
	// DRAW
	
	/**
	 * construct the nodes
	 */
	private void createNodes()
	{
		// setup the grid so all weekday togglebuttons will grow, but the weeknumbers do not
		calendarPicker = new CalendarPicker();
		
		// the result
		BorderPane lBorderPane = new BorderPane();
		lBorderPane.setCenter(calendarPicker);
		getChildren().add(lBorderPane);

		// setup CSS
        getSkinnable().getStyleClass().add(this.getClass().getSimpleName()); // always add self as style class, because CSS should relate to the skin not the control
	}
	private CalendarPicker calendarPicker = null;
}
