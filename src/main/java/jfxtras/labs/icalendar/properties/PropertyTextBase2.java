package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneName;

/**
 * Property with language and a text-based value
 *  
 * @param <T>
 * @see Categories
 * @see TimeZoneName
 */
public abstract class PropertyTextBase2<T> extends PropertyTextBase<T>
{
    /**
     * LANGUAGE
     * To specify the language for text values in a property or property parameter.
     * 
     * Examples:
     * SUMMARY;LANGUAGE=en-US:Company Holiday Party
     * LOCATION;LANGUAGE=no:Tyskland
     */
    public String getLanguage() { return (language == null) ? _language : language.get(); }
    public ObjectProperty<String> languageProperty()
    {
        if (language == null)
        {
            language = new SimpleObjectProperty<>(this, ParameterEnum.LANGUAGE.toString(), _language);
        }
        return language;
    }
    private String _language;
    private ObjectProperty<String> language;
    public void setLanguage(String language)
    {
        if (language != null)
        {
            parameters().add(ParameterEnum.LANGUAGE);
        } else
        {
            parameters().remove(ParameterEnum.LANGUAGE);            
        }
        if (this.language == null)
        {
            _language = language;
        } else
        {
            this.language.set(language);
        }
    }
    public T withLanguage(String content) { setLanguage(content); return (T) this; }    
    
    /*
     * CONSTRUCTORS
     */    
    protected PropertyTextBase2(String propertyString)
    {
        super(propertyString);
    }
    
    // copy constructor
    public PropertyTextBase2(PropertyTextBase2<T> property)
    {
        super(property);
        if (getLanguage() != null)
        {
            setLanguage(property.getLanguage());
        }
    }
    
    public PropertyTextBase2() { super(); }

}
