package jfxtras.labs.icalendar.parameters;

import java.util.Collection;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Base class of all iCalendar Parameters
 * Example VALUE=DATE
 * 
 * @author David Bal
 *
 * @param <U> - type of value stored in the Parameter, such as String for text-based, or the enumerated type of the classes based on an enum
 * @param <T> - implemented subclass
 */
public abstract class ParameterBase<T,U> implements Parameter<U>
{
    private final ParameterEnum myParameterEnum;
    ParameterEnum myParameterEnum() { return myParameterEnum; }
    
    @Override
    public U getValue() { return value.get(); }
    public ObjectProperty<U> valueProperty() { return value; }
    private ObjectProperty<U> value;
    @Override
    public void setValue(U value) { this.value.set(value); }
    public T withValue(U value) { setValue(value); return (T) this; }

    @Override
    public String toContentLine()
    {
        final String value;
        if (getValue() instanceof Collection)
        {
            value = ((Collection<?>) getValue()).stream()
                    .map(obj -> Parameter.addDoubleQuotesIfNecessary(obj.toString()))
                    .collect(Collectors.joining(","));
        } else
        {
            value = Parameter.addDoubleQuotesIfNecessary(getValue().toString());
        }
        return (getValue() != null) ? ";" + myParameterEnum.toString() + "=" + value : null;
    }
    
    @Override
    public boolean isEqualTo(Parameter<U> parameter1, Parameter<U> parameter2)
    {
        return parameter1.getValue().equals(parameter2.getValue());
    }
    
    @Override
    public void copyTo(Parameter<U> source, Parameter<U> destination)
    {
        destination.setValue(source.getValue());
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
