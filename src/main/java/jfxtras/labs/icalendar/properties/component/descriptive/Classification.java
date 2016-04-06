package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.component.descriptive.Classification.ClassificationType;

/**
 * CLASS
 * Classification
 * RFC 5545, 3.8.1.3, page 82
 * 
 * This property defines the access classification for a calendar component.
 * 
 * Example:
 * CLASS:PUBLIC
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 */
public class Classification extends PropertyBase<Classification, ClassificationType>
{
    private String unknownValue; // contains exact string for unknown property value

    public Classification(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public Classification(ClassificationType type)
    {
        super(type);
    }
    
    public Classification(Classification source)
    {
        super(source);
    }
    
    public Classification()
    {
        super(ClassificationType.PUBLIC); // default value
    }
    
    @Override
    protected ClassificationType valueFromString(String propertyValueString)
    {
        ClassificationType type = ClassificationType.valueOf2(propertyValueString);
        if (type == ClassificationType.UNKNOWN)
        {
            unknownValue = propertyValueString;
        }
        return type;
    }
    
    @Override
    protected String valueToString(ClassificationType value)
    {
        if (value == ClassificationType.UNKNOWN)
        {
            return unknownValue;
        }
        return getValue().toString();
    }
    
    public enum ClassificationType
    {
        PUBLIC,
        PRIVATE,
        CONFIDENTIAL,
        UNKNOWN; // must treat as PRIVATE
        
        static ClassificationType valueOf2(String value)
        {
            try
            {
                return valueOf(value);
            } catch (IllegalArgumentException e)
            {
                return UNKNOWN;
            }
        }
    }
}
