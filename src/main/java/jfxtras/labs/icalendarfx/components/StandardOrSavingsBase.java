package jfxtras.labs.icalendarfx.components;

import java.net.URI;
import java.time.ZoneOffset;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceStreamer;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneName;

public abstract class StandardOrSavingsBase<T> extends VComponentRepeatableBase<T> implements StandardOrSavings<T>
{
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
//    @Override
//    public Stream<Temporal> streamRecurrences(Temporal startTemporal)
//    {
//        return streamer.stream(startTemporal);
//    }
    
    @Override
    public String getTimeZoneIdentifier()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringProperty timeZoneIdentifierProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTimeZoneIdentifier(String tzid)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public TimeZoneName getTimeZoneName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringProperty timeZoneNameProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTimeZoneName(TimeZoneName tzname)
    {
        // TODO Auto-generated method stub
        
    }

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

    @Override
    public URI getTimeZoneURL()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<URI> timeZoneURLProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTimeZoneURL(URI timeZoneURL)
    {
        // TODO Auto-generated method stub
        
    }

}
