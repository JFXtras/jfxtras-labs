package jfxtras.labs.icalendarfx.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.parameters.Language;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneName;

/**
 * Property with language and a text-based value
 *  
 * @param <T> - type of property value
 * 
 * @see PropertyBaseLanguage
 * 
 * concrete subclasses
 * @see Categories
 * @see TimeZoneName
 */
public interface PropertyLanguage<T> extends Property<T>
{
    /**
     * LANGUAGE
     * To specify the language for text values in a property or property parameter.
     * 
     * Examples:
     * SUMMARY;LANGUAGE=en-US:Company Holiday Party
     * LOCATION;LANGUAGE=no:Tyskland
     */

    Language getLanguage();
    ObjectProperty<Language> languageProperty();
    void setLanguage(Language language);
}
