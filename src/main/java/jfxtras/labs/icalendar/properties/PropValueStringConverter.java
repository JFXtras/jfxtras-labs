package jfxtras.labs.icalendar.properties;

import javafx.util.StringConverter;

/**
 * Property value string converter that stores the property as a field for use
 * in the toString and fromString methods.
 * 
 * @author David Bal
 *
 * @param <T> - property value
 */
public abstract class PropValueStringConverter<T> extends StringConverter<T>
{
    final private Property<T> property;
    protected Property<T> getProperty() { return property; }
    
    public PropValueStringConverter(Property<T> property)
    {
        this.property = property;
    }
}
