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
 * @see Categories
 * @see TimeZoneName
 */
public abstract class LanguageBase<T,U> extends PropertyBase<T,U>
{
    /**
     * LANGUAGE
     * To specify the language for text values in a property or property parameter.
     * 
     * Examples:
     * SUMMARY;LANGUAGE=en-US:Company Holiday Party
     * LOCATION;LANGUAGE=no:Tyskland
     */
    public Language getLanguage() { return (language == null) ? _language : language.get(); }
    public ObjectProperty<Language> languageProperty()
    {
        if (language == null)
        {
            language = new SimpleObjectProperty<>(this, ParameterEnum.LANGUAGE.toString(), _language);
        }
        return language;
    }
    private Language _language;
    private ObjectProperty<Language> language;
    public void setLanguage(Language language)
    {
//        if (language != null)
//        {
//            parameterMapModifiable().put(ParameterEnum.LANGUAGE, language);
//        } else
//        {
//            parameterMapModifiable().remove(ParameterEnum.LANGUAGE);            
//        }
        if (this.language == null)
        {
            _language = language;
        } else
        {
            this.language.set(language);
        }
    }
    public T withLanguage(Language language) { setLanguage(language); return (T) this; }
    public T withLanguage(String content) { ParameterEnum.LANGUAGE.parse(this, content); return (T) this; }    
    
    /*
     * CONSTRUCTORS
     */    
    protected LanguageBase(String propertyString)
    {
        super(propertyString);
    }
    
    // copy constructor
    public LanguageBase(LanguageBase<T,U> property)
    {
        super(property);
        if (getLanguage() != null)
        {
            setLanguage(property.getLanguage());
        }
    }
    
    public LanguageBase() { super(); }
    
//    @Override
//    public String toContentLine()
//    {        
//        return (getLanguage() == null) ? super.toContentLine() : super.toContentLine() + getLanguage().toContentLine();
//    }

}
