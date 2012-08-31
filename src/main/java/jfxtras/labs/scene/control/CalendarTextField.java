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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;

/**
 * A textField with displays a calendar (date) with a icon to popup the CalendarPickerX
 * Features relative mutation options, like -1 or -1d for yesterday, -1m for minus one month, +1w, +2y. # is today.
 * 
 * @author Tom Eugelink
 */
public class CalendarTextField extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public CalendarTextField()
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
		this.getStyleClass().add(this.getClass().getSimpleName().toLowerCase());
		
		// this is apparently needed for good focus behavior
		setFocusTraversable(false);
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
	
	/** Value: */
	public ObjectProperty<Calendar> valueProperty() { return iValueObjectProperty; }
	final private ObjectProperty<Calendar> iValueObjectProperty = new SimpleObjectProperty<Calendar>(this, "value", null);
	// java bean API
	public Calendar getValue() { return iValueObjectProperty.getValue(); }
	public void setValue(Calendar value) { iValueObjectProperty.setValue(value); }
	public CalendarTextField withValue(Calendar value) { setValue(value); return this; }
	final static public String VALUE_PROPERTY_ID = "value";

	/** DateFormat: */
	public ObjectProperty<DateFormat> dateFormatProperty() { return iDateFormatObjectProperty; }
	final private ObjectProperty<DateFormat> iDateFormatObjectProperty = new SimpleObjectProperty<DateFormat>(this, "dateFormat", SimpleDateFormat.getDateInstance());
	// java bean API
	public DateFormat getDateFormat() { return iDateFormatObjectProperty.getValue(); }
	public void setDateFormat(DateFormat value) { iDateFormatObjectProperty.setValue(value); }
	public CalendarTextField withDateFormat(DateFormat value) { setDateFormat(value); return this; }
	final static public String DATEFORMAT_PROPERTY_ID = "dateFormat";

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return iLocaleObjectProperty; }
	final private ObjectProperty<Locale> iLocaleObjectProperty = new SimpleObjectProperty<Locale>(Locale.getDefault());
	//
	public Locale getLocale() { return iLocaleObjectProperty.getValue(); }
	public void setLocale(Locale value) { iLocaleObjectProperty.setValue(value); }
	public CalendarTextField withLocale(Locale value) { setLocale(value); return this; } 
	final static public String LOCALE_PROPERTY_ID = "locale";
	

	// ==================================================================================================================
	// EVENTS
	
	// ==================================================================================================================
	// BEHAVIOR
	
}
