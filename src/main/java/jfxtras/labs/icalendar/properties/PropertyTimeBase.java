package jfxtras.labs.icalendar.properties;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

public class PropertyTimeBase<T> extends PropertyBase<T>
{
    @Override
    public Temporal getValue() { return value.get(); }
    public ObjectProperty<Temporal> valueProperty() { return value; }
    final private ObjectProperty<Temporal> value = new SimpleObjectProperty<Temporal>(this, propertyType().toString());
    public void setValue(Temporal temporal) { this.value.set(temporal); }
    public T withValue(Temporal temporal) { setValue(temporal); return (T) this; }
    
    /*
     * CONSTRUCTORS
     */    
    protected PropertyTimeBase(String propertyString)
    {
        super(propertyString);
        setValue(DateTimeUtilities.parse(getPropertyValueString()));
    }

    // copy constructor
    public PropertyTimeBase(PropertyTimeBase<T> source)
    {
        super(source);
        if (getValue() != null)
        {
            setValue(source.getValue());
        }
    }

    public PropertyTimeBase()
    {
        super();
    }
    
    @Override
    public String toContentLine()
    {
        return super.toContentLine() + ":" + DateTimeUtilities.format(getValue());
    }
}
