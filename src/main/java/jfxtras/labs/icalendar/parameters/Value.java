package jfxtras.labs.icalendar.parameters;

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
public class Value extends ParameterBase
{
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
//
//    public Value(String content)
//    {
//        // TODO Auto-generated constructor stub
//    }
//
//    @Override
//    public String toContentLine()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public Object getValue()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
}
