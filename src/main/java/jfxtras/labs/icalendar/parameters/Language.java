package jfxtras.labs.icalendar.parameters;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * LANGUAGE
 * To specify the language for text values in a property or property parameter.
 * 
 * Examples:
 * SUMMARY;LANGUAGE=en-US:Company Holiday Party
 * LOCATION;LANGUAGE=no:Tyskland
 */
public class Language
{
    public String getLanguage() { return language.get(); }
    public StringProperty languageProperty() { return language; }
    private StringProperty language = new SimpleStringProperty(this, ICalendarParameter.LANGUAGE.toString());
    public void setLanguage(String language) { this.language.set(language); }
}
