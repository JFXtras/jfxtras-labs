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
public abstract class PropertyTextBase<T> extends PropertyBase<T>
{
    @Override
    public String getValue() { return value.get(); }
    public StringProperty valueProperty() { return value; }
    final private StringProperty value = new SimpleStringProperty(this, propertyType().toString());
    public void setValue(String text) { this.value.set(text); }
    @Override
    public T withValue(String text) { setValue(text); return (T) this; }
        
    /*
     * CONSTRUCTORS
     */    
    protected PropertyTextBase(String propertyString)
    {
        super(propertyString);
        setValue(getPropertyValueString());
    }

    // copy constructor
    public PropertyTextBase(PropertyTextBase<T> source)
    {
        super(source);
        if (getValue() != null)
        {
            setValue(source.getValue());
        }
    }

    public PropertyTextBase()
    {
        super();
    }
    
    @Override
    public String toContentLine()
    {
        return super.toContentLine() + ":" + getValue().toString();
    }
}
