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
public abstract class LanguageProperty<T,U> extends PropertyBase<T,U>
{
    /**
     * LANGUAGE
     * To specify the language for text values in a property or property parameter.
     * 
     * Examples:
     * SUMMARY;LANGUAGE=en-US:Company Holiday Party
     * LOCATION;LANGUAGE=no:Tyskland
     */
    public Language getLanguage() { return (language == null) ? null : language.get(); }
    public ObjectProperty<Language> languageProperty()
    {
        if (language == null)
        {
            language = new SimpleObjectProperty<>(this, ParameterEnum.LANGUAGE.toString());
        }
        return language;
    }
    private ObjectProperty<Language> language;
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
    protected LanguageProperty(String propertyString, Boolean makeMeUnique)
    {
        super(propertyString, null);
    }
    
    // copy constructor
    public LanguageProperty(LanguageProperty<T,U> property)
    {
        super(property);
        if (getLanguage() != null)
        {
            setLanguage(property.getLanguage());
        }
    }
    
    public LanguageProperty(U value)
    {
        setValue(value);
    }
    
    public LanguageProperty()
    {
        super();
//        ChangeListener<? super Parameter<?>> listener = (observable, oldValue, newValue) -> 
//        {
//            boolean isOldNull = oldValue == null;
//            boolean isNewNull = newValue == null;            
//            if ((isOldNull && ! isNewNull) || (! isOldNull && isNewNull))
//            {
//                System.out.println("updated parameters");
//                parmeters2 = parameters();
//            }
//        };
//        languageProperty().addListener(listener);
    }
    
//    @Override
//    public String toContentLine()
//    {        
//        return (getLanguage() == null) ? super.toContentLine() : super.toContentLine() + getLanguage().toContentLine();
//    }

}
