package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import jfxtras.labs.icalendar.parameters.Language;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.properties.component.descriptive.Categories;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneName;

/**
 * Property with language and a text-based value
 *  
 * @param <U>
 * 
 * concrete subclasses
 * @see Categories
 * @see TimeZoneName
 */
public abstract class PropertyBaseLanguage<U,T> extends PropertyBase<U,T> implements PropertyLanguage<T>
{
    /**
     * LANGUAGE
     * To specify the language for text values in a property or property parameter.
     * 
     * Examples:
     * SUMMARY;LANGUAGE=en-US:Company Holiday Party
     * LOCATION;LANGUAGE=no:Tyskland
     */
    @Override
    public Language getLanguage() { return (language == null) ? null : language.get(); }
    @Override
    public ObjectProperty<Language> languageProperty()
    {
        if (language == null)
        {
            language = new SimpleObjectProperty<>(this, ParameterEnum.LANGUAGE.toString());
        }
        return language;
    }
    private ObjectProperty<Language> language;
    @Override
    public void setLanguage(Language language)
    {
        if (language != null)
        {
            languageProperty().set(language);
        }
    }
    public void setLanguage(String value) { setLanguage(new Language(value)); }
    public U withLanguage(Language language) { setLanguage(language); return (U) this; }
    public U withLanguage(String content) { ParameterEnum.LANGUAGE.parse(this, content); return (U) this; }    
    
    /*
     * CONSTRUCTORS
     */    
    protected PropertyBaseLanguage(CharSequence contentLine, StringConverter<T> converter)
    {
        super(contentLine, converter);
    }
    
    // copy constructor
    public PropertyBaseLanguage(PropertyBaseLanguage<U,T> property)
    {
        super(property);
        if (getLanguage() != null)
        {
            setLanguage(property.getLanguage());
        }
    }
    
    public PropertyBaseLanguage(T value, StringConverter<T> converter)
    {
        super(value, converter);
    }
}
