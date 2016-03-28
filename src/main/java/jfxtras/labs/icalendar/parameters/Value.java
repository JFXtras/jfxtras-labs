package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.Value.ValueType;

/**
 * Value Date Types
 * VALUE
 * RFC 5545 iCalendar 3.2.10 page 29
 * 
 * To explicitly specify the value type format for a property value.
 * 
 *  Example:
 *  ATTACH;FMTTYPE=application/msword:ftp://example.com/pub/docs/
 *   agenda.doc
 * 
 */
public class Value extends ParameterBase<Value, ValueType>
{
//    @Override
//    public ValueType getValue() { return value.get(); }
//    @Override
//    public ObjectProperty<ValueType> valueProperty() { return value; }
//    private ObjectProperty<ValueType> value = new SimpleObjectProperty<>(this, ParameterEnum.VALUE_DATA_TYPES.toString());
//    public void setValue(ValueType value) { this.value.set(value); }
//    
//    @Override
//    public void copyTo(Property source, Property destination)
//    {
//        Value castSource = (Value) source;
//        Value castDestinateion = (Value) destination;
//        castDestinateion.setValue(castSource.getValue());
//    }
    
    public enum ValueType
    {
        BINARY ("BINARY"), 
        BOOLEAN ("BOOLEAN"), 
        CALENDAR_USER_ADDRESS ("CAL-ADDRESS"),
        DATE ("DATE"),
        DATE_TIME ("DATE-TIME"),
        DURATION ("DURATION"),
        FLOAT ("FLOAT"),
        INTEGER ("INTEGER"),
        PERIOD ("PERIOD"),
        RECURRENCE_RULE ("RECUR"),
        TEXT ("TEXT"),
        TIME ("TIME"),
        UNIFORM_RESOURCE_IDENTIFIER ("URI"),
        UTC_OFFSET ("UTC-OFFSET");
        // x-name or IANA-token values must be added manually
        
        private String name;
        @Override public String toString() { return name; }
        ValueType(String name)
        {
            this.name = name;
        }
    }
}
