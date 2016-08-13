package jfxtras.labs.icalendarfx.properties;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.parameters.Language;
import jfxtras.labs.icalendarfx.parameters.PropertyParameter;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneName;

/**
 * Property with language and a text-based value
 *  
 * concrete subclasses
 * @see Categories
 * @see TimeZoneName
 * 
 * @author David Bal
 *
 * @param <U> - type of implementing subclass
 * @param <T> - type of property value
 */
public abstract class PropertyBaseLanguage<T,U> extends PropertyBase<T,U> implements PropertyLanguage<T>
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
            language = new SimpleObjectProperty<>(this, PropertyParameter.LANGUAGE.toString());
            orderer().registerSortOrderProperty(language);
        }
        return language;
    }
    private ObjectProperty<Language> language;
    @Override
    public void setLanguage(Language language) { languageProperty().set(language); }
    public void setLanguage(String value) { setLanguage(Language.parse(value)); }
    public U withLanguage(Language language) { setLanguage(language); return (U) this; }
    public U withLanguage(String content) { PropertyParameter.LANGUAGE.parse(this, content); return (U) this; }    
    
    /*
     * CONSTRUCTORS
     */    
//    protected PropertyBaseLanguage(String contentLine)
//    {
//        super(contentLine);
//    }
    
    // copy constructor
    public PropertyBaseLanguage(PropertyBaseLanguage<T,U> property)
    {
        super(property);
    }
    
    public PropertyBaseLanguage(T value)
    {
        super(value);
    }
    
    protected PropertyBaseLanguage()
    {
        super();
    }
}
