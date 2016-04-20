package jfxtras.labs.icalendarfx.components;

import java.time.temporal.TemporalAmount;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.alarm.Trigger;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;

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
    public ObjectProperty<TemporalAmount> durationProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TemporalAmount getDuration()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDuration(TemporalAmount duration)
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
