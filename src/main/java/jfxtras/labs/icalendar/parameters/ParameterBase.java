package jfxtras.labs.icalendar.parameters;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.properties.Property;

/**
 * Base class of all iCalendar Parameters
 * Example VALUE=DATE
 * 
 * @author David Bal
 *
 * @param <U> - value class of Parameter, such as String for text-based, or the enumerated type of the classes based on an enum
 */
public abstract class ParameterBase<T,U> implements Parameter
{
    private final ParameterEnum myParameterEnum;
    ParameterEnum myParameterEnum() { return myParameterEnum(); }
    
    @Override
    public U getValue() { return value.get(); }
    public ObjectProperty<U> valueProperty() { return value; }
    private ObjectProperty<U> value;
    public void setValue(U value) { this.value.set(value); }
    public T withValue(U value) { setValue(value); return (T) this; }

    @Override
    public String toContentLine()
    {
        return (getValue() != null) ? ";" + myParameterEnum.toString() + "=" + getValue() : null;
    }
    
    @Override
    public boolean isEqualTo(Property property1, Property property2)
    {
        return property1.getValue().equals(property2.getValue());
    }
    
    @Override
    public void copyTo(Property source, Property destination)
    {
        ParameterBase<T,U> castSource = (ParameterBase<T,U>) source;
        ParameterBase<T,U> castDestination = (ParameterBase<T,U>) destination;
        castDestination.setValue(castSource.getValue());
    }
    
    /*
     * CONSTRUCTORS
     */
    ParameterBase()
    {
        myParameterEnum = ParameterEnum.enumFromClass(getClass());
        value = new SimpleObjectProperty<>(this, myParameterEnum.toString());
    }
    
    ParameterBase(String content)
    {
        this();
        U value = myParameterEnum.parse(content);
        setValue(value);
    }
    
    ParameterBase(ParameterBase<T,U> source)
    {
        myParameterEnum = source.myParameterEnum;
        setValue(source.getValue());
    }

}
