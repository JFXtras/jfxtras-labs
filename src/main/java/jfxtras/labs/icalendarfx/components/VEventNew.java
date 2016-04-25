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

public class VEventNew extends VComponentLocatableBase<VEventNew> implements VComponentDateTimeEnd<VEventNew>, VComponentDisplayable<VEventNew>
{    
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VEVENT;
    }

    /**
     * DTEND
     * Date-Time End (for local-date)
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
    public void setTimeTransparency(String timeTransparency) { setTimeTransparency(new TimeTransparency(timeTransparency)); }
    public void setTimeTransparency(TimeTransparency timeTransparency) { timeTransparencyProperty().set(timeTransparency); }
    public void setTimeTransparency(TimeTransparencyType timeTransparency) { setTimeTransparency(new TimeTransparency(timeTransparency)); }
    public VEventNew withTimeTransparency(TimeTransparency timeTransparency) { setTimeTransparency(timeTransparency); return this; }
    public VEventNew withTimeTransparency(TimeTransparencyType timeTransparencyType) { setTimeTransparency(timeTransparencyType); return this; }
    public VEventNew withTimeTransparency(String timeTransparency) { PropertyEnum.TIME_TRANSPARENCY.parse(this, timeTransparency); return this; }

    
    /*
     * CONSTRUCTORS
     */
    public VEventNew() { }
    
    public VEventNew(String contentLines)
    {
        super(contentLines);
    }
}
