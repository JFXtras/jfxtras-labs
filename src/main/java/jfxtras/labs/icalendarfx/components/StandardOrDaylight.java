package jfxtras.labs.icalendarfx.components;

import java.time.ZoneOffset;
import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.ComponentElement;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneName;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneOffsetFrom;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneOffsetTo;

/**
 * Subcomponent of VAlarm
 * Either StandardTime or DaylightSavingsTime.
 * Both classes have identical methods.
 * 
 * @author David Bal
 *
 */
public interface StandardOrDaylight<T> extends VComponentRepeatable<T>
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
    ObservableList<TimeZoneName> getTimeZoneNames();
    void setTimeZoneNames(ObservableList<TimeZoneName> properties);
    default T withTimeZoneNames(ObservableList<TimeZoneName> timeZoneNames) { setTimeZoneNames(timeZoneNames); return (T) this; }
    default T withTimeZoneNames(String...timeZoneNames)
    {
        Arrays.stream(timeZoneNames).forEach(c -> ComponentElement.TIME_ZONE_NAME.parse(this, c));
        return (T) this;
    }
    default T withTimeZoneNames(TimeZoneName...timeZoneNames)
    {
        if (getTimeZoneNames() == null)
        {
            setTimeZoneNames(FXCollections.observableArrayList(timeZoneNames));
        } else
        {
            getTimeZoneNames().addAll(timeZoneNames);
        }
        return (T) this;
    } 
    
    /**
     * TZOFFSETFROM
     * Time Zone Offset From
     * RFC 5545, 3.8.3.3, page 104
     * 
     * This property specifies the offset that is in use prior to this time zone observance.
     * 
     * EXAMPLES:
     * TZOFFSETFROM:-0500
     * TZOFFSETFROM:+1345
     */
    ObjectProperty<TimeZoneOffsetFrom> timeZoneOffsetFromProperty();
    default TimeZoneOffsetFrom getTimeZoneOffsetFrom() { return timeZoneOffsetFromProperty().get(); }
    default void setTimeZoneOffsetFrom(TimeZoneOffsetFrom timeZoneOffsetFrom) { timeZoneOffsetFromProperty().set(timeZoneOffsetFrom); }
    default void setTimeZoneOffsetFrom(ZoneOffset zoneOffset) { setTimeZoneOffsetFrom(new TimeZoneOffsetFrom(zoneOffset)); }
    default T withTimeZoneOffsetFrom(TimeZoneOffsetFrom timeZoneOffsetFrom) { setTimeZoneOffsetFrom(timeZoneOffsetFrom); return (T) this; }
    default T withTimeZoneOffsetFrom(ZoneOffset zoneOffset) { setTimeZoneOffsetFrom(zoneOffset); return (T) this; }
    default T withTimeZoneOffsetFrom(String timeZoneOffsetFrom) { ComponentElement.TIME_ZONE_OFFSET_FROM.parse(this, timeZoneOffsetFrom); return (T) this; }
    
    /**
     * TZOFFSETTO
     * Time Zone Offset To
     * RFC 5545, 3.8.3.4, page 105
     * 
     * This property specifies the offset that is in use in this time zone observance
     * 
     * EXAMPLES:
     * TZOFFSETTO:-0400
     * TZOFFSETTO:+1245
     */
    ObjectProperty<TimeZoneOffsetTo> timeZoneOffsetToProperty();
    default TimeZoneOffsetTo getTimeZoneOffsetTo() { return timeZoneOffsetToProperty().get(); }
    default void setTimeZoneOffsetTo(TimeZoneOffsetTo timeZoneOffsetTo) { timeZoneOffsetToProperty().set(timeZoneOffsetTo); }
    default void setTimeZoneOffsetTo(ZoneOffset zoneOffset) { setTimeZoneOffsetTo(new TimeZoneOffsetTo(zoneOffset)); }
    default T withTimeZoneOffsetTo(TimeZoneOffsetTo timeZoneOffsetTo) { setTimeZoneOffsetTo(timeZoneOffsetTo); return (T) this; }
    default T withTimeZoneOffsetTo(ZoneOffset zoneOffset) { setTimeZoneOffsetTo(zoneOffset); return (T) this; }
    default T withTimeZoneOffsetTo(String timeZoneOffsetTo) { ComponentElement.TIME_ZONE_OFFSET_TO.parse(this, timeZoneOffsetTo); return (T) this; }
}
