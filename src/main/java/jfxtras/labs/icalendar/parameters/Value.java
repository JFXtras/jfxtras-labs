package jfxtras.labs.icalendar.parameters;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

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
public class Value
{
    public ValueType getValue() { return value.get(); }
    public ObjectProperty<ValueType> valueProperty() { return value; }
    private ObjectProperty<ValueType> value = new SimpleObjectProperty<>();
    public void setValue(ValueType value) { this.value.set(value); }

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
        UTC_OFFSET ("UTC-OFFSET"),
        X_NAME ("x-name"),
        IANA_TOKEN ("iana-token");
        
        private String name;
        @Override public String toString() { return name; }
        ValueType(String name)
        {
            this.name = name;
        }
        
    }
}
