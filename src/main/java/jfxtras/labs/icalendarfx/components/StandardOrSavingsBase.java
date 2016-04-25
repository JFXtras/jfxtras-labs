package jfxtras.labs.icalendarfx.components;

import java.time.ZoneOffset;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceStreamer;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneName;

public abstract class StandardOrSavingsBase<T> extends VComponentRepeatableBase<T> implements StandardOrSavings<T>
{
    /**
     * TZNAME
     * Time Zone Name
     * RFC 5545, 3.8.3.2, page 103
     * 
     * This property specifies the customary designation for a time zone description.
     * 
     * This property specifies the text value that uniquely
     * identifies the "VTIMEZONE" calendar component in the scope of an
     * 
     * EXAMPLES:
     * TZNAME:EST
     * TZNAME;LANGUAGE=fr-CA:HN
     */
    @Override
    public ObjectProperty<TimeZoneName> timeZoneNameProperty()
    {
        if (timeZoneName == null)
        {
            timeZoneName = new SimpleObjectProperty<>(this, PropertyEnum.TIME_ZONE_NAME.toString());
        }
        return timeZoneName;
    }
    private ObjectProperty<TimeZoneName> timeZoneName;
    
    /*
     * CONSTRUCTORS
     */
    public StandardOrSavingsBase() { }
    
    public StandardOrSavingsBase(String contentLines)
    {
        super(contentLines);
    }
    
    // Recurrence streamer - produces recurrence set
    private RecurrenceStreamer streamer = new RecurrenceStreamer(this);
    @Override
    public RecurrenceStreamer recurrenceStreamer() { return streamer; }


    @Override
    public ZoneOffset getTimeZoneOffsetFrom()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<ZoneOffset> timeZoneOffsetFromProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTimeZoneOffsetFrom(ZoneOffset timeZoneOffsetFrom)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ZoneOffset getTimeZoneOffsetTo()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<ZoneOffset> timeZoneOffsetToProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTimeZoneOffsetTo(ZoneOffset timeZoneOffsetTo)
    {
        // TODO Auto-generated method stub
        
    }

}
