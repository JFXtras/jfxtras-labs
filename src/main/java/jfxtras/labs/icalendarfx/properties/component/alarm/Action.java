package jfxtras.labs.icalendarfx.properties.component.alarm;

import javafx.util.StringConverter;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.properties.PropertyBase;
import jfxtras.labs.icalendarfx.properties.component.alarm.Action.ActionType;

/**
 * ACTION
 * RFC 5545, 3.8.6.1, page 132
 * 
 * This property defines the action to be invoked when an alarm is triggered.
 * 
 * actionvalue = "AUDIO" / "DISPLAY" / "EMAIL" / iana-token / x-name
 * 
 * Applications MUST ignore alarms with x-name and iana-token values they don't recognize.
 * 
 * Examples:
 * ACTION:AUDIO
 * ACTION:DISPLAY
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VAlarm
 */
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

    public Action()
    {
        super();
        setConverter(CONVERTER);
    }
    
    public static Action parse(String value)
    {
        Action property = new Action();
        property.parseContent(value);
        return property;
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