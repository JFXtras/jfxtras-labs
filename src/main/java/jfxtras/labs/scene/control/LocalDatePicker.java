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
		constructLocale();
	}

	/**
	 * Return the path to the CSS file so things are setup right
	 */
	@Override protected String getUserAgentStylesheet()
	{
		return this.getClass().getResource("/jfxtras/labs/internal/scene/control/" + this.getClass().getSimpleName() + ".css").toString();
	}
	
	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalDate: */
	public ObjectProperty<LocalDate> localDateProperty() { return iLocalDateObjectProperty; }
	final private ObjectProperty<LocalDate> iLocalDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "LocalDate");
	// construct property
	private void constructLocalDate()
	{
		// if this value is changed by binding, make sure related things are updated
		localDateProperty().addListener(new ChangeListener<LocalDate>()
		{
			@Override
			public void changed(ObservableValue<? extends LocalDate> observableValue, LocalDate oldValue, LocalDate newValue)
			{
				// if the new value is set to null, remove the old value
				if (oldValue != null) {
					localDates().remove(oldValue);
				}
				if (newValue != null && localDates().contains(newValue) == false) {
					localDates().add(newValue);
				}
			} 
		});
	}
	// java bean API
	public LocalDate getLocalDate() { return iLocalDateObjectProperty.getValue(); }
	public void setLocalDate(LocalDate value) { iLocalDateObjectProperty.setValue(value); }
	public LocalDatePicker withLocalDate(LocalDate value) { setLocalDate(value); return this; } 

	/** LocalDates: */
	public ObservableList<LocalDate> localDates() { return iLocalDates; }
	final private ObservableList<LocalDate> iLocalDates =  javafx.collections.FXCollections.observableArrayList();
	final static public String LocalDateS_PROPERTY_ID = "LocalDates";
	// construct property
	private void constructLocalDates()
	{
		// make sure the singled out LocalDate is 
		localDates().addListener(new ListChangeListener<LocalDate>() 
		{
			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends LocalDate> change)
			{
				// if the active LocalDate is not longer in LocalDates, select another
				if (!localDates().contains(getLocalDate())) 
				{
					// if there are other left
					if (localDates().size() > 0) 
					{
						// select the first
						setLocalDate( localDates().get(0) );
					}
					else 
					{
						// clear it
						setLocalDate(null);
					}
				}
			} 
		});
	}

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return iLocaleObjectProperty; }
	volatile private ObjectProperty<Locale> iLocaleObjectProperty = new SimpleObjectProperty<Locale>(this, "locale", Locale.getDefault());
	// java bean API
	public Locale getLocale() { return iLocaleObjectProperty.getValue(); }
	public void setLocale(Locale value) { iLocaleObjectProperty.setValue(value); }
	public LocalDatePicker withLocale(Locale value) { setLocale(value); return (LocalDatePicker)this; } 
	// construct property
	private void constructLocale()
	{
		// make sure the singled out LocalDate is 
		localeProperty().addListener(new InvalidationListener() 
		{
			@Override
			public void invalidated(Observable observable)
			{
				// if the locale is changed, all selected LocalDates must be cleared
				// because "equals" takes the locale of the LocalDate in account and suddenly LocalDates that were equal in date no longer are
// TODO: skin are created after the control, so this clears any set values because it is initialized in the constructor
// so we need a smarter way to do this, however LocalDate does not have a "getLocale"
// should we recreated all values copying DMY?				
//LocalDates().clear();
			} 
		});
	}
	
	/** Mode: single, range or multiple */
	public ObjectProperty<Mode> modeProperty() { return iModeObjectProperty; }
	final private SimpleObjectProperty<Mode> iModeObjectProperty = new SimpleObjectProperty<Mode>(this, "mode", Mode.SINGLE);
	public enum Mode { SINGLE, MULTIPLE, RANGE };
	// java bean API
	public Mode getMode() { return iModeObjectProperty.getValue(); }
	public void setMode(Mode value)
	{
		if (value == null) throw new IllegalArgumentException("NULL not allowed");
		
		// set it
		iModeObjectProperty.setValue(value);
		
		// update the collection; remove excessive LocalDates
		while (getMode() == Mode.SINGLE && localDates().size() > 1) {
			localDates().remove(localDates().size() - 1);
		}
	}
	public LocalDatePicker withMode(Mode value) { setMode(value); return this; } 
}
