package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.alarm.Action;
import jfxtras.labs.icalendarfx.properties.component.alarm.Action.ActionType;
import jfxtras.labs.icalendarfx.properties.component.alarm.Trigger;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;

/** 
 * VALARM: RFC 5545 iCalendar 3.6.6. page 71
 * 
 * Not implemented
 */
public class VAlarm extends VComponentDescribableBase<VAlarm> implements VAlarmInt<VAlarm>, VComponentDescribable2<VAlarm>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VALARM;
    }
 
    /**
     * ACTION
     * RFC 5545 iCalendar 3.8.6.1 page 132,
     * This property defines the action to be invoked when an
     * alarm is triggered.
     * actionvalue = "AUDIO" / "DISPLAY" / "EMAIL" / iana-token / x-name
     * 
     * Example:
     * ACTION:DISPLAY
     */
    public ObjectProperty<Action> actionProperty()
    {
        if (action == null)
        {
            action = new SimpleObjectProperty<>(this, PropertyEnum.ACTION.toString());
        }
        return action;
    }
    private ObjectProperty<Action> action;
    public Action getAction() { return actionProperty().get(); }
    public void setAction(String action) { setAction(new Action(action)); }
    public void setAction(Action action) { actionProperty().set(action); }
    public void setAction(ActionType action) { setAction(new Action(action)); }
    public VAlarm withAction(Action action) { setAction(action); return this; }
    public VAlarm withAction(ActionType actionType) { setAction(actionType); return this; }
    public VAlarm withAction(String action) { PropertyEnum.ACTION.parse(this, action); return this; }
    
    /**
     * DESCRIPTION
     * RFC 5545 iCalendar 3.8.1.5. page 84
     * 
     * This property provides a more complete description of the
     * calendar component than that provided by the "SUMMARY" property.
     * 
     * Example:
     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
     *  design.\nHappy Face Conference Room. Phoenix design team
     *  MUST attend this meeting.\nRSVP to team leader.
     *
     * Note: Only VJournal allows multiple instances of DESCRIPTION
     */
    @Override public ObjectProperty<Description> descriptionProperty()
    {
        if (description == null)
        {
            description = new SimpleObjectProperty<>(this, PropertyEnum.DESCRIPTION.toString());
        }
        return description;
    }
    private ObjectProperty<Description> description;
    
    /** 
     * DURATION
     * RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Can't be used if DTEND is used.  Must be one or the other.
     * 
     * Example:
     * DURATION:PT15M
     * */
    @Override public ObjectProperty<DurationProp> durationProperty()
    {
        if (duration == null)
        {
            duration = new SimpleObjectProperty<>(this, PropertyEnum.DURATION.toString());
        }
        return duration;
    }
    private ObjectProperty<DurationProp> duration;
    
    /*
     * CONSTRUCTORS
     */
    public VAlarm() { }
    
    public VAlarm(String contentLines)
    {
        super(contentLines);
    }


    @Override
    public Attendee getAttendee()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<Attendee> attendeeProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttendee(Attendee attendee)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ObservableList<Attendee> getAttendees()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAttendees(ObservableList<Attendee> properties)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getRepeat()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public IntegerProperty repeatProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setRepeat(int repeat)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Trigger getTrigger()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<Trigger> triggerProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTrigger(Trigger trigger)
    {
        // TODO Auto-generated method stub
        
    }

}
