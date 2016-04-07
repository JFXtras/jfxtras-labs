package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.Language;
import jfxtras.labs.icalendar.properties.component.descriptive.Categories;
import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneName;

/**
 * Property with language and a text-based value
 *  
 * @param <T>
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
