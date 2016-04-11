package jfxtras.labs.icalendar.properties.component.descriptive;

import javafx.util.StringConverter;
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
    private final static StringConverter<ClassificationType> CONVERTER = new StringConverter<ClassificationType>()
    {
        @Override
        public String toString(ClassificationType object)
        {
            // null means value is unknown and non-converted string in PropertyBase unknownValue should be used instead
            return (object == ClassificationType.UNKNOWN) ? null: object.toString();
        }

        @Override
        public ClassificationType fromString(String string)
        {
            return ClassificationType.valueOf2(string);
        }
    };
    
    public Classification(CharSequence contentLine)
    {
        super();
        setConverter(CONVERTER);
        parseContent(contentLine);
    }
    
    public Classification(ClassificationType type)
    {
        super();
        setConverter(CONVERTER);
        setValue(type);
    }
    
    public Classification(Classification source)
    {
        super(source);
    }
    
    public Classification()
    {
        super();
        setConverter(CONVERTER);
        setValue(ClassificationType.PUBLIC); // default value
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
