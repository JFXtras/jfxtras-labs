package jfxtras.labs.icalendarfx.mocks;

import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.StandardOrSavings;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneIdentifier;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneURL;

public class VTimeZoneMock extends VTimeZone<VTimeZoneMock>
{
    /*
     * CONSTRUCTORS
     */
    public VTimeZoneMock() { }
    
    public VTimeZoneMock(String contentLines)
    {
        super(contentLines);
    }

    @Override
    public TimeZoneIdentifier getTimeZoneIdentifier()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<TimeZoneIdentifier> timeZoneIdentifierProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTimeZoneIdentifier(TimeZoneIdentifier url)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public TimeZoneURL getTimeZoneURL()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<TimeZoneURL> timeZoneURLProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setTimeZoneURL(TimeZoneURL url)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ObservableList<StandardOrSavings> getStandardOrSavingsTime()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setStandardOrSavingsTime(ObservableList<StandardOrSavings> properties)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ZonedDateTime getDateTimeLastModified()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ObjectProperty<ZonedDateTime> dateTimeLastModifiedProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setDateTimeLastModified(ZonedDateTime dtLastModified)
    {
        // TODO Auto-generated method stub
        
    }
}
