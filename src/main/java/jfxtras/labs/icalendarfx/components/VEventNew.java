package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.time.TimeTransparency;
import jfxtras.labs.icalendarfx.properties.component.time.TimeTransparency.TimeTransparencyType;

public class VEventNew extends VComponentLocatableBase<VEventNew> implements VComponentLocatable<VEventNew>, VComponentDateTimeEnd<VEventNew>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VEVENT;
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


    @Override
    public Stream<Temporal> streamRecurrences(Temporal startTemporal)
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public Temporal getDateTimeEnd()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public ObjectProperty<Temporal> dateTimeEndProperty()
    {
        // TODO Auto-generated method stub
        throw new RuntimeException("not implemented");
    }

    @Override
    public void setDateTimeEnd(Temporal dtEnd)
    {
        // TODO Auto-generated method stub
        
    }
}
