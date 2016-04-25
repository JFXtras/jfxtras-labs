package jfxtras.labs.icalendarfx.components;

import java.time.ZoneOffset;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneName;

/**
 * Either StandardTime or DaylightSavingsTime.
 * Both classes have identical methods.
 * 
 * @author David Bal
 *
 */
public interface StandardOrSavings<T> extends VComponentRepeatable<T>
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
    ObjectProperty<TimeZoneName> timeZoneNameProperty();
    default TimeZoneName getTimeZoneName() { return timeZoneNameProperty().get(); }
    default void setTimeZoneName(TimeZoneName timeZoneName) { timeZoneNameProperty().set(timeZoneName); }
    default T withTimeZoneName(TimeZoneName timeZoneName) { setTimeZoneName(timeZoneName); return (T) this; }
    default T withTimeZoneName(String timeZoneName) { PropertyEnum.TIME_ZONE_NAME.parse(this, timeZoneName); return (T) this; }
  
    
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
}
