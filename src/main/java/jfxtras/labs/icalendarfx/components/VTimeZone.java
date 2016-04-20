package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.change.LastModified;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneIdentifier;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneURL;

/**
 * VTIMEZONE: RFC 5545 iCalendar 3.6.5. page 62
 * currently not supported - ZonedDateTime is providing time zone information
 * 
 * While the ZoneId class has the IANA Time Zone Database (TZDB) so no other
 * time zone information is needed.
 * However, it can be important to output VTimeZone components to communicate with other
 * applications.  Therefore, this component may be implemented in the future.
 * 
 * @author David Bal
 *
 */
public class VTimeZone extends VComponentBase<VTimeZone> implements VTimeZoneInt<VTimeZone>
{
    //  NEED TO OVERRIDE toContentLines, AND MAYBE OTHER METHODS, TO ACCOMODIATE THE LIST
    // OF StandardOrSavings, BECAUSE THOSE ARE NOT PROPERTIES, BUT COMPONENTS THEMSELVES.
    
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VTIMEZONE;
    }
 
    /**
    * LAST-MODIFIED
    * RFC 5545, 3.8.7.3, page 138
    * 
    * This property specifies the date and time that the
    * information associated with the calendar component was last
    * revised in the calendar store.
    *
    * Note: This is analogous to the modification date and time for a
    * file in the file system.
    * 
    * The value MUST be specified as a date with UTC time.
    * 
    * Example:
    * LAST-MODIFIED:19960817T133000Z
    */
    @Override
    public ObjectProperty<LastModified> dateTimeLastModifiedProperty()
    {
        if (lastModified == null)
        {
            lastModified = new SimpleObjectProperty<>(this, PropertyEnum.LAST_MODIFIED.toString());
        }
        return lastModified;
    }
    private ObjectProperty<LastModified> lastModified;
    
    /*
     * CONSTRUCTORS
     */
    public VTimeZone() { }
    
    public VTimeZone(String contentLines)
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
}
