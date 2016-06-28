package jfxtras.labs.icalendarfx.components;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.CalendarElementType;
import jfxtras.labs.icalendarfx.components.revisors.Revisable;
import jfxtras.labs.icalendarfx.components.revisors.ReviserVAlarm;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.alarm.Action;
import jfxtras.labs.icalendarfx.properties.component.alarm.Action.ActionType;
import jfxtras.labs.icalendarfx.properties.component.alarm.RepeatCount;
import jfxtras.labs.icalendarfx.properties.component.alarm.Trigger;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;

/** 
 * VALARM
 * Alarm Component
 * RFC 5545 iCalendar 3.6.6. page 71
 * 
 * Provide a grouping of component properties that define an alarm.
 * 
 * Description:  A "VALARM" calendar component is a grouping of
      component properties that is a reminder or alarm for an event or a
      to-do.  For example, it may be used to define a reminder for a
      pending event or an overdue to-do.

      The "VALARM" calendar component MUST include the "ACTION" and
      "TRIGGER" properties.  The "ACTION" property further constrains
      the "VALARM" calendar component in the following ways:

      When the action is "AUDIO", the alarm can also include one and
      only one "ATTACH" property, which MUST point to a sound resource,
      which is rendered when the alarm is triggered.

      When the action is "DISPLAY", the alarm MUST also include a
      "DESCRIPTION" property, which contains the text to be displayed
      when the alarm is triggered.

      When the action is "EMAIL", the alarm MUST include a "DESCRIPTION"
      property, which contains the text to be used as the message body,
      a "SUMMARY" property, which contains the text to be used as the
      message subject, and one or more "ATTENDEE" properties, which
      contain the email address of attendees to receive the message.  It
      can also include one or more "ATTACH" properties, which are
      intended to be sent as message attachments.  When the alarm is
      triggered, the email message is sent.

      The "VALARM" calendar component MUST only appear within either a
      "VEVENT" or "VTODO" calendar component.  "VALARM" calendar
      components cannot be nested.  Multiple mutually independent

      "VALARM" calendar components can be specified for a single
      "VEVENT" or "VTODO" calendar component.

      The "TRIGGER" property specifies when the alarm will be triggered.
      The "TRIGGER" property specifies a duration prior to the start of
      an event or a to-do.  The "TRIGGER" edge may be explicitly set to
      be relative to the "START" or "END" of the event or to-do with the
      "RELATED" parameter of the "TRIGGER" property.  The "TRIGGER"
      property value type can alternatively be set to an absolute
      calendar date with UTC time.

      In an alarm set to trigger on the "START" of an event or to-do,
      the "DTSTART" property MUST be present in the associated event or
      to-do.  In an alarm in a "VEVENT" calendar component set to
      trigger on the "END" of the event, either the "DTEND" property
      MUST be present, or the "DTSTART" and "DURATION" properties MUST
      both be present.  In an alarm in a "VTODO" calendar component set
      to trigger on the "END" of the to-do, either the "DUE" property
      MUST be present, or the "DTSTART" and "DURATION" properties MUST
      both be present.

      The alarm can be defined such that it triggers repeatedly.  A
      definition of an alarm with a repeating trigger MUST include both
      the "DURATION" and "REPEAT" properties.  The "DURATION" property
      specifies the delay period, after which the alarm will repeat.
      The "REPEAT" property specifies the number of additional
      repetitions that the alarm will be triggered.  This repetition
      count is in addition to the initial triggering of the alarm.  Both
      of these properties MUST be present in order to specify a
      repeating alarm.  If one of these two properties is absent, then
      the alarm will not repeat beyond the initial trigger.

      The "ACTION" property is used within the "VALARM" calendar
      component to specify the type of action invoked when the alarm is
      triggered.  The "VALARM" properties provide enough information for
      a specific action to be invoked.  It is typically the
      responsibility of a "Calendar User Agent" (CUA) to deliver the
      alarm in the specified fashion.  An "ACTION" property value of
      AUDIO specifies an alarm that causes a sound to be played to alert
      the user; DISPLAY specifies an alarm that causes a text message to
      be displayed to the user; and EMAIL specifies an alarm that causes
      an electronic email message to be delivered to one or more email
      addresses.

      In an AUDIO alarm, if the optional "ATTACH" property is included,
      it MUST specify an audio sound resource.  The intention is that
      the sound will be played as the alarm effect.  If an "ATTACH"
      property is specified that does not refer to a sound resource, or

      if the specified sound resource cannot be rendered (because its
      format is unsupported, or because it cannot be retrieved), then
      the CUA or other entity responsible for playing the sound may
      choose a fallback action, such as playing a built-in default
      sound, or playing no sound at all.

      In a DISPLAY alarm, the intended alarm effect is for the text
      value of the "DESCRIPTION" property to be displayed to the user.

      In an EMAIL alarm, the intended alarm effect is for an email
      message to be composed and delivered to all the addresses
      specified by the "ATTENDEE" properties in the "VALARM" calendar
      component.  The "DESCRIPTION" property of the "VALARM" calendar
      component MUST be used as the body text of the message, and the
      "SUMMARY" property MUST be used as the subject text.  Any "ATTACH"
      properties in the "VALARM" calendar component SHOULD be sent as
      attachments to the message.

         Note: Implementations should carefully consider whether they
         accept alarm components from untrusted sources, e.g., when
         importing calendar objects from external sources.  One
         reasonable policy is to always ignore alarm components that the
         calendar user has not set herself, or at least ask for
         confirmation in such a case.
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 */
public class VAlarm extends VComponentDescribableBase<VAlarm> implements VComponentDescribable2<VAlarm>,
        VComponentAttendee<VAlarm>, VComponentDuration<VAlarm>
{
    @Override
    public CalendarElementType componentType()
    {
        return CalendarElementType.VALARM;
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
            action = new SimpleObjectProperty<>(this, PropertyType.ACTION.toString());
            orderer().registerSortOrderProperty(action);
        }
        return action;
    }
    private ObjectProperty<Action> action;
    public Action getAction() { return actionProperty().get(); }
    public void setAction(String action) { setAction(Action.parse(action)); }
    public void setAction(Action action) { actionProperty().set(action); }
    public void setAction(ActionType action) { setAction(new Action(action)); }
    public VAlarm withAction(Action action) { setAction(action); return this; }
    public VAlarm withAction(ActionType actionType) { setAction(actionType); return this; }
    public VAlarm withAction(String action) { PropertyType.ACTION.parse(this, action); return this; }
    
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
    @Override
    public ObservableList<Attendee> getAttendees() { return attendees; }
    private ObservableList<Attendee> attendees;
    @Override
    public void setAttendees(ObservableList<Attendee> attendees)
    {
        if (attendees != null)
        {
            orderer().registerSortOrderProperty(attendees);
        } else
        {
            orderer().unregisterSortOrderProperty(this.attendees);
        }
        this.attendees = attendees;
    }
    
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
            description = new SimpleObjectProperty<>(this, PropertyType.DESCRIPTION.toString());
            orderer().registerSortOrderProperty(description);
        }
        return description;
    }
    @Override
    public Description getDescription() { return (description == null) ? null : descriptionProperty().get(); }
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
            duration = new SimpleObjectProperty<>(this, PropertyType.DURATION.toString());
            orderer().registerSortOrderProperty(duration);
        }
        return duration;
    }
    private ObjectProperty<DurationProp> duration;
    
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
    public ObjectProperty<RepeatCount> repeatCountProperty()
    {
        if (repeatCount == null)
        {
            repeatCount = new SimpleObjectProperty<>(this, PropertyType.ACTION.toString());
            orderer().registerSortOrderProperty(repeatCount);
        }
        return repeatCount;
    }
    private ObjectProperty<RepeatCount> repeatCount;
    public RepeatCount getRepeatCount() { return repeatCountProperty().get(); }
//    public void setRepeatCount(String repeatCount) { setRepeatCount(new RepeatCount(repeatCount)); }
    public void setRepeatCount(RepeatCount repeatCount) { repeatCountProperty().set(repeatCount); }
//    public void setRepeatCount(int repeatCount) { setRepeatCount(new RepeatCount(repeatCount)); }
    public VAlarm withRepeatCount(RepeatCount repeatCount) { setRepeatCount(repeatCount); return this; }
//    public VAlarm withRepeatCount(int repeatCount) { setRepeatCount(repeatCount); return this; }
//    public VAlarm withRepeatCount(String repeatCount) { PropertyEnum.REPEAT_COUNT.parse(this, repeatCount); return this; }
    
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
    public ObjectProperty<Trigger<?>> triggerProperty()
    {
        if (trigger == null)
        {
            trigger = new SimpleObjectProperty<>(this, PropertyType.ACTION.toString());
            orderer().unregisterSortOrderProperty(trigger);
        }
        return trigger;
    }
    private ObjectProperty<Trigger<?>> trigger;
    public Trigger<?> getTrigger() { return triggerProperty().get(); }
    public void setTrigger(String trigger) { PropertyType.TRIGGER.parse(this, trigger); }
    public void setTrigger(Trigger<?> trigger) { triggerProperty().set(trigger); }
    public void setTrigger(Duration trigger) { setTrigger(new Trigger<Duration>(trigger)); }
    public void setTrigger(ZonedDateTime trigger) { setTrigger(new Trigger<ZonedDateTime>(trigger)); }
    public VAlarm withTrigger(Trigger<?> trigger) { setTrigger(trigger); return this; }
    public VAlarm withTrigger(Duration trigger) { setTrigger(trigger); return this; }
    public VAlarm withTrigger(ZonedDateTime trigger) { setTrigger(trigger); return this; }
    public VAlarm withTrigger(String trigger) { setTrigger(trigger); return this; }

    
    /*
     * CONSTRUCTORS
     */
    public VAlarm() { super(); }
    
    public VAlarm(String contentLines)
    {
        super(contentLines);
    }
    
    /** Copy constructor */
    public VAlarm(VAlarm source)
    {
        super(source);
    }
    
    @Override
    public Revisable newRevisor() { return new ReviserVAlarm(this); }

    @Override
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
        if (getAction() == null)
        {
            errors.add("ACTION is not present.  ACTION is REQUIRED and MUST NOT occur more than once");
        }
        if (getTrigger() == null)
        {
            errors.add("TRIGGER is not present.  TRIGGER is REQUIRED and MUST NOT occur more than once");
        }
        boolean isDurationNull = getDuration() == null;
        boolean isRepeatNull = getRepeatCount() == null;
        
        if (isDurationNull && ! isRepeatNull)
        {
            errors.add("DURATION is present but REPEAT is not present.  DURATION and REPEAT are both OPTIONAL, and MUST NOT occur more than once each, but if one occurs, so MUST the other.");
        }
        if (! isDurationNull && isRepeatNull)
        {
            errors.add("REPEAT is present but DURATION is not present.  DURATION and REPEAT are both OPTIONAL, and MUST NOT occur more than once each, but if one occurs, so MUST the other.");
        }
        return Collections.unmodifiableList(errors);
    }
    
    @Override
    public boolean isValid()
    {
        boolean isActionPresent = getAction() != null;
        boolean isTriggerPresent = getTrigger() != null;
        boolean isDurationNull = getDuration() == null;
        boolean isRepeatNull = getRepeatCount() == null;
        boolean isBothNull = isDurationNull && isRepeatNull;
        boolean isNeitherNull = ! isDurationNull && ! isRepeatNull;
        boolean isDurationAndRepeatOK = isBothNull || isNeitherNull;
        return isActionPresent && isTriggerPresent && isDurationAndRepeatOK;
    }

    /** Parse content lines into calendar component object */
    public static VAlarm parse(String contentLines)
    {
        VAlarm component = new VAlarm();
        component.parseContent(contentLines);
        return component;
    }
}
