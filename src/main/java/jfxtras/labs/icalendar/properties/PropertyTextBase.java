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
    public String getValue() { return value.get(); }
    public StringProperty valueProperty() { return value; }
    final private StringProperty value = new SimpleStringProperty(this, propertyType().toString());
    public void setValue(String text) { this.value.set(text); }
    public T withValue(String text) { setValue(text); return (T) this; }
    
    @Override
    public void parseAndSetValue(String value)
    {
//        System.out.println("value:base:" + this.text + " " + valueProperty());
        setValue(value);
    }
    
    /*
     * CONSTRUCTORS
     */    
    protected PropertyTextBase(String propertyString)
    {
        super(propertyString);
        parseAndSetValue(getPropertyValueString());
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

    public PropertyTextBase()
    {
        super();
    }
}
