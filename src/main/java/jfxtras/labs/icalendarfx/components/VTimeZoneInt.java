package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneURL;

@Deprecated // put into abstract class when done building VTodo
public interface VTimeZoneInt<T> extends VComponentNew<T>, VComponentLastModified<T>
{
    /**
     * TZID
     * Time Zone Identifier
     * RFC 5545, 3.8.3.1, page 102
     * 
     * To specify the identifier for the time zone definition for
     * a time component in the property value
     * 
     * LIMITATION: globally unique time zones are stored as strings and the ZoneID is null.
     * Only the toString and toContentLine methods will display the original string.  Another
     * method to convert the unique time zone string into a ZoneId is required.
     * 
     * EXAMPLE:
     * TZID:America/Los_Angeles
     */
//    TimeZoneIdentifier getTimeZoneIdentifier();
//    ObjectProperty<TimeZoneIdentifier> timeZoneIdentifierProperty();
//    void setTimeZoneIdentifier(TimeZoneIdentifier url);
    
    /**
     * TZURL
     * Time Zone URL
     * RFC 5545, 3.8.3.5, page 106
     * 
     * This property provides a means for a "VTIMEZONE" component
     * to point to a network location that can be used to retrieve an up-
     * to-date version of itself
     * 
     * EXAMPLES:
     * TZURL:http://timezones.example.org/tz/America-Los_Angeles.ics
     */
    TimeZoneURL getTimeZoneURL();
    ObjectProperty<TimeZoneURL> timeZoneURLProperty();
    void setTimeZoneURL(TimeZoneURL url);
    
    /**
     * Either StandardTime or DaylightSavingsTime.
     * Both classes have identical methods.
     *
     */
    ObservableList<StandardOrSavings> getStandardOrSavingsTime();
    void setStandardOrSavingsTime(ObservableList<StandardOrSavings> properties);
    
    //  NEED TO OVERRIDE toContentLines, AND MAYBE OTHER METHODS, TO ACCOMODIATE THE LIST
    // OF StandardOrSavings, BECAUSE THOSE ARE NOT PROPERTIES, BUT COMPONENTS THEMSELVES.
}
