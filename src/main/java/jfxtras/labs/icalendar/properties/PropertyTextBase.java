package jfxtras.labs.icalendar.properties;

import java.util.Arrays;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.parameters.Value.ValueType;
import jfxtras.labs.icalendar.properties.calendar.CalendarScale;
import jfxtras.labs.icalendar.properties.calendar.Method;
import jfxtras.labs.icalendar.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendar.properties.calendar.Version;
import jfxtras.labs.icalendar.properties.component.relationship.UniqueIdentifier;

/**
 * Class for property with only a text value
 * 
 * @author David Bal
 *
 * @param <T>
 * @see UniqueIdentifier
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
    public T withValue(String text) { setValue(text); return (T) this; }

    final private static List<ValueType> SUPPORTED_VALUED_TYPES = Arrays.asList(ValueType.TEXT);
    @Override
    public void setValueType(ValueType value)
    {        
        if (SUPPORTED_VALUED_TYPES.contains(value))
        {
            super.setValueType(value);            
        } else
        {
            throw new IllegalArgumentException("Only Value types supported are: " + SUPPORTED_VALUED_TYPES);
        }
    }
    
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
