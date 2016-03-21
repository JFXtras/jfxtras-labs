package jfxtras.labs.icalendar.properties;

import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.parameters.ICalendarParameter;
import jfxtras.labs.icalendar.parameters.Language;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

/**
 * Property with language parameter added
 *  
 * @param <T>
 */
public abstract class Property2Base<T> extends PropertyBase implements Property2
{
    @Override
    public Language getLanguage() { return (language == null) ? _language : language.get(); }
    @Override
    public ObjectProperty<Language> languageProperty()
    {
        if (language == null)
        {
            language = new SimpleObjectProperty<>(this, ICalendarParameter.LANGUAGE.toString(), _language);
        }
        return language;
    }
    private Language _language;
    private ObjectProperty<Language> language;
    @Override
    public void setLanguage(Language language)
    {
        if (this.language == null)
        {
            _language = language;
        } else
        {
            this.language.set(language);
        }
    }
    public T withLanguage(Language language) { setLanguage(language); return (T) this; }
    
    private String name;
    public String getText() { return text.get(); }
    public StringProperty textProperty() { return text; }
    private StringProperty text = new SimpleStringProperty(this, name);
    public void setText(String text) { this.text.set(text); }
    
    /**
     * Return property content line for iCalendar output files.  See RFC 5545 3.5
     * Contains component property with its value and any populated parameters.
     * 
     * For example: SUMMARY;LANGUAGE=en-US:Company Holiday Party
     * 
     * @return - the content line
     */
    @Override
    public String toContentLine()
    {
        return name + Property2.super.toContentLine();
    }
    /*
     * CONSTRUCTOR
     */

    // construct new object by parsing property line
    protected Property2Base(String name, String propertyString)
    {
        this.name = name;
        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyString);
        map.entrySet()
        .stream()
        .forEach(e ->
        {
            ICalendarParameter.enumFromName(e.getKey())
                    .setValue(this, e.getValue());
        });
        setText(map.get(ICalendarUtilities.PROPERTY_VALUE_KEY));
    }
}
