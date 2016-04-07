package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.Language;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.properties.component.descriptive.Categories;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneName;

/**
 * Property with language and a text-based value
 *  
 * @param <T>
 * 
 * concrete subclasses
 * @see Categories
 * @see TimeZoneName
 */
public abstract class PropertyBaseLanguage<T,U> extends PropertyBase<T,U> implements PropertyLanguage<U>
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
    public T withLanguage(Language language) { setLanguage(language); return (T) this; }
    public T withLanguage(String content) { ParameterEnum.LANGUAGE.parse(this, content); return (T) this; }    
    
    /*
     * CONSTRUCTORS
     */    
    protected PropertyBaseLanguage(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    // copy constructor
    public PropertyBaseLanguage(PropertyBaseLanguage<T,U> property)
    {
        super(property);
        if (getLanguage() != null)
        {
            setLanguage(property.getLanguage());
        }
    }
    
    public PropertyBaseLanguage(U value)
    {
        super(value);
    }
}
