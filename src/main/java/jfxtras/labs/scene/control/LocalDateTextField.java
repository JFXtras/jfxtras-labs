/**
 * LocalDateTextField.java
 *
 * Copyright (c) 2011-2014, JFXtras
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the organization nor the
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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Locale;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.util.Callback;
import jfxtras.labs.internal.scene.control.skin.LocalDateTextFieldSkin;


/**
 * LocalDate (JSR-310) text field component.
 * This component allows selecting of one date.
 * 
 * @author Tom Eugelink
 */
public class LocalDateTextField extends Control
{
	// ==================================================================================================================
	// CONSTRUCTOR
	
	/**
	 * 
	 */
	public LocalDateTextField()
	{
		construct();
	}

	/**
	 * 
	 * @param localDate
	 */
	public LocalDateTextField(LocalDate localDate)
	{
		construct();
		setLocalDate(localDate);
	}
	
	/*
	 * 
	 */
	private void construct()
	{
	}
	
	@Override public Skin createDefaultSkin() {
		return new LocalDateTextFieldSkin(this);
	}

	
	// ==================================================================================================================
	// PROPERTIES
	
	/** LocalDate: */
	public ObjectProperty<LocalDate> localDateProperty() { return localDateObjectProperty; }
	private final ObjectProperty<LocalDate> localDateObjectProperty = new SimpleObjectProperty<LocalDate>(this, "localDate");
	public LocalDate getLocalDate() { return localDateObjectProperty.getValue(); }
	public void setLocalDate(LocalDate value) { localDateObjectProperty.setValue(value); }
	public LocalDateTextField withLocalDate(LocalDate value) { setLocalDate(value); return this; } 

	/** Locale: the locale is used to determine first-day-of-week, weekday labels, etc */
	public ObjectProperty<Locale> localeProperty() { return localeObjectProperty; }
	final private ObjectProperty<Locale> localeObjectProperty = new SimpleObjectProperty<Locale>(Locale.getDefault());
	public Locale getLocale() { return localeObjectProperty.getValue(); }
	public void setLocale(Locale value) { localeObjectProperty.setValue(value); }
	public LocalDateTextField withLocale(Locale value) { setLocale(value); return this; } 

	// TODO: this should be the DateTime formatter
	/** 
	 * The DateFormat used to render/parse the date in the textfield.
	 * It is allow to show time as well for example by SimpleDateFormat.getDateTimeInstance().
	 */
	public ObjectProperty<DateFormat> dateFormatProperty() { return dateFormatObjectProperty; }
	final private ObjectProperty<DateFormat> dateFormatObjectProperty = new SimpleObjectProperty<DateFormat>(this, "dateFormat", SimpleDateFormat.getDateInstance(DateFormat.LONG, getLocale()));
	public DateFormat getDateFormat() { return dateFormatObjectProperty.getValue(); }
	public void setDateFormat(DateFormat value) { dateFormatObjectProperty.setValue(value); }
	public LocalDateTextField withDateFormat(DateFormat value) { setDateFormat(value); return this; }
	
	/** DateFormats: a list of alternate dateFormats used for parsing only */
	public ListProperty<DateFormat> dateFormatsProperty() { return dateFormatsProperty; }
	ListProperty<DateFormat> dateFormatsProperty = new SimpleListProperty<DateFormat>(javafx.collections.FXCollections.observableList(new ArrayList<DateFormat>()));
	public ObservableList<DateFormat> getDateFormats() { return dateFormatsProperty.getValue(); }
	public void setDateFormats(ObservableList<DateFormat> value) { dateFormatsProperty.setValue(value); }
	public LocalDateTextField withDateFormat(ObservableList<DateFormat> value) { setDateFormats(value); return this; }

	/** PromptText: */
	public ObjectProperty<String> promptTextProperty() { return promptTextObjectProperty; }
	final private ObjectProperty<String> promptTextObjectProperty = new SimpleObjectProperty<String>(this, "promptText", null);
	public String getPromptText() { return promptTextObjectProperty.get(); }
	public void setPromptText(String value) { promptTextObjectProperty.set(value); }
	public LocalDateTextField withPromptText(String value) { setPromptText(value); return this; }

	/** parse error callback:
	 * If something did not parse correctly, you may handle it. 
	 * Otherwise the exception will be logged on the console.
	 */
	public ObjectProperty<Callback<Throwable, Void>> parseErrorCallbackProperty() { return parseErrorCallbackObjectProperty; }
	final private ObjectProperty<Callback<Throwable, Void>> parseErrorCallbackObjectProperty = new SimpleObjectProperty<Callback<Throwable, Void>>(this, "parseErrorCallback", null);
	public Callback<Throwable, Void> getParseErrorCallback() { return this.parseErrorCallbackObjectProperty.getValue(); }
	public void setParseErrorCallback(Callback<Throwable, Void> value) { this.parseErrorCallbackObjectProperty.setValue(value); }
	public LocalDateTextField withParseErrorCallback(Callback<Throwable, Void> value) { setParseErrorCallback(value); return this; }

	// ==================================================================================================================
	// SUPPORT
}
