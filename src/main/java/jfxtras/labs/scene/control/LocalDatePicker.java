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
package jfxtras.labs.scene.control;

import java.util.Locale;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;

import javax.time.calendar.LocalDate;

/**
 * LocalDate picker component
 * Unfortunately this control suffers from JavaFX issue RT-16732 (http://javafx-jira.kenai.com/browse/RT-16732)!
 * The workaround is to load the required CSS in the application's own CSS. 
 * 
 * @author Tom Eugelink
 */
public class LocalDatePicker extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalDatePicker()
	{
		construct();
	}

	/**
	 * 
	 * @param localDate
	 */
	public LocalDatePicker(LocalDate localDate)
	{
		construct();
		setLocalDate(localDate);
	}
	
	/*
	 * 
	 */
	private void construct()
	{
		// setup the CSS
		// the -fx-skin attribute in the CSS sets which Skin class is used
		this.getStyleClass().add(this.getClass().getSimpleName());
		
		// construct properties
		constructLocalDate();
		constructLocalDates();
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override protected String getUserAgentStylesheet()
	{
		return this.getClass().getResource("/jfxtras/labs/internal/scene/control/" + DEFAULT_STYLE_CLASS + ".css").toString();
	}
    private static final String DEFAULT_STYLE_CLASS = LocalDatePicker.class.getSimpleName();
	
	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalDate: */
	public final /* this final was added under heavy objection */ ObjectProperty<LocalDate> localDateProperty() { return localDateObjectProperty; }
	private final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
	public final /* this final was added under heavy objection */ LocalDate getLocalDate() { return localDateObjectProperty.getValue(); }
	public final /* this final was added under heavy objection */ void setLocalDate(LocalDate value) { localDateObjectProperty.setValue(value); }
	public LocalDatePicker withLocalDate(LocalDate value) { setLocalDate(value); return this; } 
	private void constructLocalDate()
	{
		// if this value is changed by binding, make sure related things are updated
		localDateProperty().addListener(new ChangeListener<LocalDate>()
		{
			@Override
			public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue)
			{
				// if the new value is set to null, remove the old value
				if (oldValue != null && newValue == null) {
					localDates().remove(oldValue);
				}
				if (newValue != null && localDates().contains(newValue) == false) {
					localDates().add(newValue);
				}
			} 
		});
	}

	/** LocalDates: */
	public final /* this final was added under heavy objection */ ObservableList<LocalDate> localDates() { return localDates; }
	private final ObservableList<LocalDate> localDates =  javafx.collections.FXCollections.observableArrayList();
	private void constructLocalDates()
	{
		// make sure the singled out LocalDate is 
		localDates().addListener(new ListChangeListener<LocalDate>() 
		{
			@Override
			public void onChanged(ListChangeListener.Change<? extends LocalDate> change)
			{
				// update the localDate
				if (localDates.size() == 0)
				{
					// clear date
					setLocalDate(null);
				}
				else
				{
					// set the last one
					setLocalDate(localDates.get(localDates.size() - 1));
				}
			} 
		});
	}

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return iLocaleObjectProperty; }
	volatile private ObjectProperty<Locale> iLocaleObjectProperty = new SimpleObjectProperty<Locale>(this, "locale", Locale.getDefault());
	public final /* this final was added under heavy objection */ Locale getLocale() { return iLocaleObjectProperty.getValue(); }
	public final /* this final was added under heavy objection */ void setLocale(Locale value) { iLocaleObjectProperty.setValue(value); }
	public LocalDatePicker withLocale(Locale value) { setLocale(value); return (LocalDatePicker)this; } 
	
	/** Mode: single, range or multiple */
	public final /* this final was added under heavy objection */ ObjectProperty<Mode> modeProperty() { return modeObjectProperty; }
	private final SimpleObjectProperty<Mode> modeObjectProperty = new SimpleObjectProperty<Mode>(this, "mode", Mode.SINGLE)
	{
		public void invalidated()
		{
			// do super
			super.invalidated();
			
			// update the collection; remove excessive LocalDates
			while (modeObjectProperty.getValue() == Mode.SINGLE && localDates().size() > 1) {
				localDates().remove(localDates().size() - 1);
			}
		}
		
		public void set(Mode value)
		{
			if (value == null) throw new NullPointerException("Null not allowed");
			super.set(value);
		}
	};
	public final /* this final was added under heavy objection */ Mode getMode() { return modeObjectProperty.getValue(); }
	public final /* this final was added under heavy objection */ void setMode(Mode value) { modeObjectProperty.setValue(value); }
	public LocalDatePicker withMode(Mode value) { setMode(value); return this; } 
	public enum Mode { SINGLE, MULTIPLE, RANGE };
}
