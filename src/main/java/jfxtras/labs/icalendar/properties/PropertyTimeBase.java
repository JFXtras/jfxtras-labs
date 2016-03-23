package jfxtras.labs.icalendar.properties;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.Value;

public class PropertyTimeBase<T> extends PropertyBase
{
    @Override
    public Temporal getValue() { return value.get(); }
    public ObjectProperty<Temporal> valueProperty() { return value; }
    final private ObjectProperty<Temporal> value = new SimpleObjectProperty<Temporal>(this, propertyType().toString());
    public void setValue(Temporal temporal) { this.value.set(temporal); }
    public T withValue(Temporal temporal) { setValue(temporal); return (T) this; }
    
    /**
     * VALUE
     * To specify the value for text values in a property or property parameter.
     * 
     * Examples:
     * VALUE=BINARY
     * VALUE=DATE
     */
    public Value getValueType() { return (valueType == null) ? _valueType : valueType.get(); }
    public ObjectProperty<Value> valueParameterProperty()
    {
        if (valueType == null)
        {
            valueType = new SimpleObjectProperty<>(this, ParameterEnum.VALUE_DATA_TYPES.toString(), _valueType);
        }
        return valueType;
    }
    private Value _valueType;
    private ObjectProperty<Value> valueType;
    public void setValue(Value value)
    {
        if (value != null)
        {
            parameters().add(ParameterEnum.VALUE_DATA_TYPES);
        } else
        {
            parameters().remove(ParameterEnum.VALUE_DATA_TYPES);            
        }
        if (this.valueType == null)
        {
            _valueType = value;
        } else
        {
            this.valueType.set(value);
        }
    }
    public T withValue(String content) { setValue(new Value(content)); return (T) this; } 
    
    @Override
    public void parseAndSetValue(String value)
    {
        // TODO Auto-generated method stub
        
    }

}
