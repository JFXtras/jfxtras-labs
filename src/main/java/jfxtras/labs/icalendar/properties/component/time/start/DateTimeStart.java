package jfxtras.labs.icalendar.properties.component.time.start;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifier;
import jfxtras.labs.icalendar.properties.Property;

public interface DateTimeStart<U extends Temporal> extends Property<U>
{
    /*
     * default Time Zone methods are overridden by classes that require them
     */
    default TimeZoneIdentifier getTimeZoneIdentifier()
    {
        return null;
//        throw new RuntimeException("Not implemented for class:" + getClass().getSimpleName());
    }
    default ObjectProperty<TimeZoneIdentifier> timeZoneIdentifierProperty()
    {
        return null;
//        throw new RuntimeException("Not implemented for class:" + getClass().getSimpleName());
    }
    default void setTimeZoneIdentifier(TimeZoneIdentifier timeZoneIdentifier)
    {
        // do nothing - not implemented
//        throw new RuntimeException("Not implemented for class:" + getClass().getSimpleName());
    }
}
