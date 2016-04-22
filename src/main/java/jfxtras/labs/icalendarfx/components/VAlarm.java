package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.alarm.Trigger;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;

/** 
 * VALARM: RFC 5545 iCalendar 3.6.6. page 71
 * 
 * Not implemented
 */
public class VAlarm extends VComponentDescribableBase<VAlarm> implements VAlarmInt<VAlarm>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VALARM;
    }
 
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
    public String getAction()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringProperty actionProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setAction(String action)
    {
        // TODO Auto-generated method stub
        
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
    public ObjectProperty<Description> descriptionProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Description getDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDescription(Description description)
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
