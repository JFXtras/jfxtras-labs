package jfxtras.labs.icalendarfx.components;

import java.net.URI;
import java.time.ZoneOffset;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneName;

public interface StandardOrSavings extends VComponentRepeatable
{
    /**
     * TZID: Time Zone Identifier
     * RFC 5545 iCalendar 3.8.3.1. page 102
     * This property specifies the text value that uniquely
     * identifies the "VTIMEZONE" calendar component in the scope of an
     * iCalendar object.
     * 
     * Example:
     * TZID:America/New_York
     */
    String getTimeZoneIdentifier();
    StringProperty timeZoneIdentifierProperty();
    void setTimeZoneIdentifier(String tzid);

    /**
     * TZNAME: Time Zone Name
     * RFC 5545 iCalendar 3.8.3.2. page 103
     * This property specifies the text value that uniquely
     * identifies the "VTIMEZONE" calendar component in the scope of an
     * iCalendar object.
     * 
     * Example:
     * TZNAME;LANGUAGE=fr-CA:HNE
     */
    TimeZoneName getTimeZoneName();
    StringProperty timeZoneNameProperty();
    void setTimeZoneName(TimeZoneName tzname);
    
    /**
     * TZOFFSETFROM: Time Zone Offset From
     * RFC 5545 iCalendar 3.8.3.3. page 104
     * This property specifies the offset that is in use prior to
     * this time zone observance.
     * 
     * Example:
     * TZOFFSETFROM:-0500
     */
    ZoneOffset getTimeZoneOffsetFrom();
    ObjectProperty<ZoneOffset> timeZoneOffsetFromProperty();
    void setTimeZoneOffsetFrom(ZoneOffset timeZoneOffsetFrom);
    
    /**
     * TZOFFSETTO: Time Zone Offset To
     * RFC 5545 iCalendar 3.8.3.4. page 105
     * This property specifies the offset that is in use in this
     * time zone observance.
     * 
     * Example:
     * TZOFFSETTO:-0400
     */
    ZoneOffset getTimeZoneOffsetTo();
    ObjectProperty<ZoneOffset> timeZoneOffsetToProperty();
    void setTimeZoneOffsetTo(ZoneOffset timeZoneOffsetTo);
    
    /**
     * TZURL: Time Zone URL
     * RFC 5545 iCalendar 3.8.3.5. page 106
     * This property provides a means for a "VTIMEZONE" component
     * to point to a network location that can be used to retrieve an up-
     * to-date version of itself.
     */
    URI getTimeZoneURL();
    ObjectProperty<URI> timeZoneURLProperty();
    void setTimeZoneURL(URI timeZoneURL);
}
