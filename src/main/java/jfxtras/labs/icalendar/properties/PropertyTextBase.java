package jfxtras.labs.icalendar.properties;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.properties.calendar.CalendarScale;
import jfxtras.labs.icalendar.properties.calendar.Method;
import jfxtras.labs.icalendar.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendar.properties.calendar.Version;

/**
 * Class for property with only a text value
 * 
 * @author David Bal
 *
 * @param <T>
 * @see CalendarScale
 * @see Method
 * @see ProductIdentifier
 * @see Version
 */
public abstract class PropertyTextBase<T> extends PropertyBase
{
    @Override
    public String getValue() { return text.get(); }
    public StringProperty valueProperty() { return text; }
    private StringProperty text = new SimpleStringProperty(this, propertyName());
    public void setValue(String text) { this.text.set(text); }
    public T withValue(String text) { setValue(text); return (T) this; }
    
    /*
     * CONSTRUCTORS
     */    
    protected PropertyTextBase(String name, String propertyString)
    {
        super(name, propertyString);
    }

    // copy constructor
    public PropertyTextBase(PropertyTextBase<T> property)
    {
        super(property);
        if (getValue() != null)
        {
            setValue(property.getValue());
        }
    }

    public PropertyTextBase(String name)
    {
        super(name);
    }
}
