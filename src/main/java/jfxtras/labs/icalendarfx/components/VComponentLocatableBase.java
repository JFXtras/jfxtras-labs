package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.GeographicPosition;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;

public abstract class VComponentLocatableBase<T> extends VComponentDisplayableBase<T> implements VComponentLocatable<T>
{
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
    
    /**
     * GEO: Geographic Position
     * RFC 5545 iCalendar 3.8.1.6 page 85, 3.3.6 page 85
     * This property specifies information related to the global
     * position for the activity specified by a calendar component.
     * 
     * This property value specifies latitude and longitude,
     * in that order (i.e., "LAT LON" ordering).
     * 
     * Example:
     * GEO:37.386013;-122.082932
     */
    @Override public ObjectProperty<GeographicPosition> geographicPositionProperty()
    {
        if (geographicPosition == null)
        {
            geographicPosition = new SimpleObjectProperty<>(this, PropertyEnum.GEOGRAPHIC_POSITION.toString());
        }
        return geographicPosition;
    }
    private ObjectProperty<GeographicPosition> geographicPosition;
    
    /*
     * CONSTRUCTORS
     */
    public VComponentLocatableBase() { }
    
    public VComponentLocatableBase(String contentLines)
    {
        super(contentLines);
    }
}
