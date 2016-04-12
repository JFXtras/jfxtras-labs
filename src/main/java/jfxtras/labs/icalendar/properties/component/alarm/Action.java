package jfxtras.labs.icalendar.properties.component.alarm;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.component.alarm.Action.ActionType;

public class Action extends PropertyBase<ActionType, Action>
{
    private final static StringConverter<ActionType> CONVERTER = new StringConverter<ActionType>()
    {
        @Override
        public String toString(ActionType object)
        {
            // null means value is unknown and non-converted string in PropertyBase unknownValue should be used instead
            return (object == ActionType.UNKNOWN) ? null: object.toString();
        }

        @Override
        public ActionType fromString(String string)
        {
            return ActionType.valueOf2(string);
        }
    };
    
    public Action(CharSequence contentLine)
    {
        super();
        setConverter(CONVERTER);
        parseContent(contentLine);
    }
    
    public Action(ActionType type)
    {
        super();
        setConverter(CONVERTER);
        setValue(type);
    }
    
    public Action(Action source)
    {
        super(source);
    }
    
    public enum ActionType
    {
        AUDIO,
        DISPLAY,
        EMAIL,
        UNKNOWN; // must ignore
        
        static ActionType valueOf2(String value)
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