package jfxtras.labs.icalendar.properties.descriptive;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.parameter.ICalendarParameter;
import jfxtras.labs.icalendar.properties.VComponentProperty;
import jfxtras.labs.icalendar.properties.ComponentProperty;

public class Categories implements VComponentProperty
{
    private final static String NAME = ComponentProperty.CATEGORIES.toString();
    
    /**
     * LANGUAGE: RFC 5545 iCalendar 3.2.10. page 21
     * Optional
     * To specify the language for text values in a property or property parameter.
     * Example:
     *  SUMMARY;LANGUAGE=en-US:Company Holiday Party
     *  LOCATION;LANGUAGE=no:Tyskland
     * */
    public StringProperty languageProperty()
    {
        if (language == null) language = new SimpleStringProperty(this, ICalendarParameter.LANGUAGE.toString(), _language);
        return language;
    }
    private StringProperty language;
    private String _language;
    public String getLanguage() { return (language == null) ? _language : language.get(); }
    public void setLanguage(String language)
    {
        if (this.language == null)
        {
            _language = language;
        } else
        {
            this.language.set(language);            
        }
    }
    public Categories withLanguage(String language) { setLanguage(language); return this; }
    
    /**
     * This property defines the categories for a calendar
     * component.  Within the "VEVENT", "VTODO", or "VJOURNAL" calendar components,
     * more than one category can be specified as a COMMA-separated list
     * of categories.
     * 
     * For example:
     * CATEGORIES:APPOINTMENT,EDUCATION
     * CATEGORIES:MEETING
     */
    public StringProperty textProperty() { return textProperty; }
    final private StringProperty textProperty = new SimpleStringProperty(this, ComponentProperty.CATEGORIES.toString());
    public String getText() { return textProperty.get(); }
    public void setText(String value) { textProperty.set(value); }
    public Categories withText(String s) { setText(s); return this; }
    
    public String toContentLine()
    {
        StringBuilder builder = new StringBuilder(NAME);
        if (getLanguage() != null)
        {
            builder.append(";" + toString() + "=" + getLanguage());
        }
        builder.append(":" + getText());
        return builder.toString();
    }
    
    /*
     * CONSTRUCTORS
     */
//    public Categories(String contentLine)
//    {
//        super(NAME, contentLine);
//    }
}
