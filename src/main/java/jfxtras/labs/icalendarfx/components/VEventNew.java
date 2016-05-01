package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;
import jfxtras.labs.icalendarfx.properties.component.time.TimeTransparency;
import jfxtras.labs.icalendarfx.properties.component.time.TimeTransparency.TimeTransparencyType;

/**
 * VEVENT
 * Event Component
 * RFC 5545, 3.6.1, page 52
 * 
 *    Description:  A "VEVENT" calendar component is a grouping of
      component properties, possibly including "VALARM" calendar
      components, that represents a scheduled amount of time on a
      calendar.  For example, it can be an activity; such as a one-hour
      long, department meeting from 8:00 AM to 9:00 AM, tomorrow.
      Generally, an event will take up time on an individual calendar.
      Hence, the event will appear as an opaque interval in a search for
      busy time.  Alternately, the event can have its Time Transparency

      set to "TRANSPARENT" in order to prevent blocking of the event in
      searches for busy time.

      The "VEVENT" is also the calendar component used to specify an
      anniversary or daily reminder within a calendar.  These events
      have a DATE value type for the "DTSTART" property instead of the
      default value type of DATE-TIME.  If such a "VEVENT" has a "DTEND"
      property, it MUST be specified as a DATE value also.  The
      anniversary type of "VEVENT" can span more than one date (i.e.,
      "DTEND" property value is set to a calendar date after the
      "DTSTART" property value).  If such a "VEVENT" has a "DURATION"
      property, it MUST be specified as a "dur-day" or "dur-week" value.

      The "DTSTART" property for a "VEVENT" specifies the inclusive
      start of the event.  For recurring events, it also specifies the
      very first instance in the recurrence set.  The "DTEND" property
      for a "VEVENT" calendar component specifies the non-inclusive end
      of the event.  For cases where a "VEVENT" calendar component
      specifies a "DTSTART" property with a DATE value type but no
      "DTEND" nor "DURATION" property, the event's duration is taken to
      be one day.  For cases where a "VEVENT" calendar component
      specifies a "DTSTART" property with a DATE-TIME value type but no
      "DTEND" property, the event ends on the same calendar date and
      time of day specified by the "DTSTART" property.

      The "VEVENT" calendar component cannot be nested within another
      calendar component.  However, "VEVENT" calendar components can be
      related to each other or to a "VTODO" or to a "VJOURNAL" calendar
      component with the "RELATED-TO" property.
      
         Example:  The following is an example of the "VEVENT" calendar
      component used to represent a meeting that will also be opaque to
      searches for busy time:

       BEGIN:VEVENT
       UID:19970901T130000Z-123401@example.com
       DTSTAMP:19970901T130000Z
       DTSTART:19970903T163000Z
       DTEND:19970903T190000Z
       SUMMARY:Annual Employee Review
       CLASS:PRIVATE
       CATEGORIES:BUSINESS,HUMAN RESOURCES
       END:VEVENT
 *
 * @author David Bal
 *
 */
public class VEventNew extends VComponentLocatableBase<VEventNew> implements VComponentDateTimeEnd<VEventNew>,
    VComponentDescribable2<VEventNew>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VEVENT;
    }

    /**
     * DTEND
     * Date-Time End
     * RFC 5545, 3.8.2.2, page 95
     * 
     * This property specifies when the calendar component ends.
     * 
     * The value type of this property MUST be the same as the "DTSTART" property, and
     * its value MUST be later in time than the value of the "DTSTART" property.
     * 
     * Example:
     * DTEND;VALUE=DATE:19980704
     */
    @Override
    public ObjectProperty<DateTimeEnd<? extends Temporal>> dateTimeEndProperty()
    {
        if (dateTimeEnd == null)
        {
            dateTimeEnd = new SimpleObjectProperty<>(this, PropertyEnum.DATE_TIME_END.toString());
            dateTimeEnd.addListener((observable, oldValue, newValue) -> checkDateTimeEndConsistency());
        }
        return dateTimeEnd;
    }
    @Override
    public DateTimeEnd<? extends Temporal> getDateTimeEnd() { return (dateTimeEnd == null) ? null : dateTimeEndProperty().get(); }
    private ObjectProperty<DateTimeEnd<? extends Temporal>> dateTimeEnd;
    /** Ensures DateTimeEnd has same date-time type as DateTimeStart.  Should be called by listener
     *  after dateTimeEndProperty() is initialized */
    @Override
    public void checkDateTimeEndConsistency()
    {
        VComponentDateTimeEnd.super.checkDateTimeEndConsistency();
        if ((getDateTimeEnd() != null) && (getDuration() != null))
        {
            throw new DateTimeException("DURATION and DTEND can't both be set");
        }
    }
    
    /** add listener to Duration to ensure both DURATION and DTEND are not both set */
    @Override public ObjectProperty<DurationProp> durationProperty()
    {
        ObjectProperty<DurationProp> duration = super.durationProperty();
        duration.addListener((obs) ->
        {
            if ((getDateTimeEnd() != null) && (getDuration() != null))
            {
                throw new DateTimeException("DURATION and DTEND can't both be set");
            }            
        });
        return duration;
    }

    /**
     * TRANSP
     * Time Transparency
     * RFC 5545 iCalendar 3.8.2.7. page 101
     * 
     * This property defines whether or not an event is transparent to busy time searches.
     * Events that consume actual time SHOULD be recorded as OPAQUE.  Other
     * events, which do not take up time SHOULD be recorded as TRANSPARENT.
     *    
     * Example:
     * TRANSP:TRANSPARENT
     */
    ObjectProperty<TimeTransparency> timeTransparencyProperty()
    {
        if (timeTransparency == null)
        {
            timeTransparency = new SimpleObjectProperty<>(this, PropertyEnum.TIME_TRANSPARENCY.toString());
        }
        return timeTransparency;
    }
    private ObjectProperty<TimeTransparency> timeTransparency;
    public TimeTransparency getTimeTransparency() { return timeTransparencyProperty().get(); }
    public void setTimeTransparency(String timeTransparency) { setTimeTransparency(TimeTransparency.parse(timeTransparency)); }
    public void setTimeTransparency(TimeTransparency timeTransparency) { timeTransparencyProperty().set(timeTransparency); }
    public void setTimeTransparency(TimeTransparencyType timeTransparency) { setTimeTransparency(new TimeTransparency(timeTransparency)); }
    public VEventNew withTimeTransparency(TimeTransparency timeTransparency) { setTimeTransparency(timeTransparency); return this; }
    public VEventNew withTimeTransparency(TimeTransparencyType timeTransparencyType) { setTimeTransparency(timeTransparencyType); return this; }
    public VEventNew withTimeTransparency(String timeTransparency) { PropertyEnum.TIME_TRANSPARENCY.parse(this, timeTransparency); return this; }
    
    /*
     * CONSTRUCTORS
     */
    public VEventNew() { super(); }

//    @Deprecated
    public VEventNew(String contentLines)
    {
        super(contentLines);
    }
    
    /** Copy constructor */
    public VEventNew(VEventNew source)
    {
        super(source);
    }

    @Override
    public boolean isValid()
    {
        boolean isDateTimeStartPresent = getDateTimeStart() != null;
        boolean isDateTimeEndPresent = getDateTimeEnd() != null;
        boolean isDurationPresent = getDuration() != null;
        boolean ok1 = isDateTimeEndPresent && ! isDurationPresent;
        boolean ok2 = ! isDateTimeEndPresent && isDurationPresent;
        boolean isDateTimeEndAndDurationOk = ok1 || ok2;
        boolean isDateTimeEndTypeOk = VComponentDateTimeEnd.super.isValid();
        return super.isValid() && isDateTimeStartPresent && isDateTimeEndAndDurationOk && isDateTimeEndTypeOk;
    }

    /** Parse content lines into calendar component object */
    public static VEventNew parse(String contentLines)
    {
        VEventNew component = new VEventNew();
        component.parseContent(contentLines);
        return component;
    }
}
