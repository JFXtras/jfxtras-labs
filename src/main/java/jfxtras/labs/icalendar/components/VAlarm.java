package jfxtras.labs.icalendar.components;

import java.time.temporal.TemporalAmount;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.properties.component.alarm.Trigger;
import jfxtras.labs.icalendar.properties.component.relationship.Attendee;

public interface VAlarm extends VComponentDescribable
{
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
    String getAction();
    StringProperty actionProperty();
    void setAction(String action);
    
    /**
     * ATTENDEE: Attendee
     * RFC 5545 iCalendar 3.8.4.1 page 107
     * This property defines an "Attendee" within a calendar component.
     * 
     * Examples:
     * ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com":
     *  mailto:joecool@example.com
     * ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=ACCEPTED;CN=Jane Doe
     *  :mailto:jdoe@example.com
     */
    Attendee getAttendee();
    ObjectProperty<Attendee> attendeeProperty();
    void setAttendee(Attendee attendee);
    
    /** 
     * DURATION
     * RFC 5545 iCalendar 3.8.2.5 page 99,
     * Can't be used if DTEND is used.  Must be one or the other.
     * 
     * Example:
     * DURATION:PT15M
     * */
    ObjectProperty<TemporalAmount> durationProperty();
    TemporalAmount getDuration();
    void setDuration(TemporalAmount duration);
    
    /**
     * REPEAT: Repeat Count
     * RFC 5545 iCalendar 3.8.6.2 page 133,
     * This property defines the number of times the alarm should
     * be repeated, after the initial trigger.
     * 
     * If the alarm triggers more than once, then this property MUST be specified
     * along with the "DURATION" property.
     * 
     * Example:  The following is an example of this property for an alarm
     * that repeats 4 additional times with a 5-minute delay after the
     * initial triggering of the alarm:
     * 
     * REPEAT:4
     * DURATION:PT5M
     */
    int getRepeat();
    IntegerProperty repeatProperty();
    void setRepeat(int repeat);
    
    /**
     * TRIGGER
     * RFC 5545 iCalendar 3.8.6.3 page 133,
     * This property specifies when an alarm will trigger.
     * 
     * Examples:
     * A trigger set 15 minutes prior to the start of the event or to-do.
     * TRIGGER:-PT15M
     *  
     * A trigger set five minutes after the end of an event or the due
     * date of a to-do.
     * TRIGGER;RELATED=END:PT5M
     * 
     * A trigger set to an absolute DATE-TIME.
     * TRIGGER;VALUE=DATE-TIME:19980101T050000Z
     */
    Trigger getTrigger();
    ObjectProperty<Trigger> triggerProperty();
    void setTrigger(Trigger trigger);
}
