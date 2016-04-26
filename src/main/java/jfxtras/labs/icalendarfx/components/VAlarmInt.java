package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.properties.component.alarm.Trigger;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;

public interface VAlarmInt<T> extends VComponentDescribable<T>, VComponentAttendee<T>, VComponentDuration<T>
{
//    /**
//     * ACTION
//     * RFC 5545 iCalendar 3.8.6.1 page 132,
//     * This property defines the action to be invoked when an
//     * alarm is triggered.
//     * actionvalue = "AUDIO" / "DISPLAY" / "EMAIL" / iana-token / x-name
//     * 
//     * Example:
//     * ACTION:DISPLAY
//     */
//    String getAction();
//    StringProperty actionProperty();
//    void setAction(String action);
//    
//    ObjectProperty<Action> actionProperty()
//    {
//        if (action == null)
//        {
//            action = new SimpleObjectProperty<>(this, PropertyEnum.TIME_TRANSPARENCY.toString());
//        }
//        return action;
//    }
//    private ObjectProperty<Action> action;
//    public Action getAction() { return actionProperty().get(); }
//    public void setAction(String action) { setAction(new Action(action)); }
//    public void setAction(Action action) { actionProperty().set(action); }
//    public void setAction(ActionType action) { setAction(new Action(action)); }
//    public VEventNew withAction(Action action) { setAction(action); return this; }
//    public VEventNew withAction(ActionType actionType) { setAction(actionType); return this; }
//    public VEventNew withAction(String action) { PropertyEnum.TIME_TRANSPARENCY.parse(this, action); return this; }

    
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
    
//    /**
//     * ATTENDEE: Attendee
//     * RFC 5545 iCalendar 3.8.4.1 page 107
//     * This property defines an "Attendee" within a calendar component.
//     * 
//     * Examples:
//     * ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com":
//     *  mailto:joecool@example.com
//     * ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=ACCEPTED;CN=Jane Doe
//     *  :mailto:jdoe@example.com
//     */
//    ObservableList<Attendee> getAttendees();
//    void setAttendees(ObservableList<Attendee> properties);
    
    // TODO - PUT IN OWN INTERFACE
//    /**
//     * DESCRIPTION:
//     * RFC 5545 iCalendar 3.8.1.12. page 84
//     * This property provides a more complete description of the
//     * calendar component than that provided by the "SUMMARY" property.
//     * Example:
//     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
//     *  design.\nHappy Face Conference Room. Phoenix design team
//     *  MUST attend this meeting.\nRSVP to team leader.
//     */
//    public ObjectProperty<Description> descriptionProperty();
//    public Description getDescription();
//    public void setDescription(Description description);
    
//    /** 
//     * DURATION
//     * RFC 5545 iCalendar 3.8.2.5 page 99,
//     * Can't be used if DTEND is used.  Must be one or the other.
//     * 
//     * Example:
//     * DURATION:PT15M
//     * */
//    @Override
//    ObjectProperty<TemporalAmount> durationProperty();
//    TemporalAmount getDuration();
//    void setDuration(TemporalAmount duration);
    
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
