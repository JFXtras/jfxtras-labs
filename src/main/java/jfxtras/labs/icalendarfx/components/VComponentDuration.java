package jfxtras.labs.icalendarfx.components;

import java.time.temporal.TemporalAmount;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;

/**
 * 
 * @author David Bal
 *
 * @param <T>
 * @see VComponentLocatable
 * @see VAlarm
 */
public interface VComponentDuration<T> extends VComponent
{
    /** 
     * DURATION
     * RFC 5545 iCalendar 3.8.2.5 page 99, 3.3.6 page 34
     * Can't be used if DTEND is used.  Must be one or the other.
     * 
     * Example:
     * DURATION:PT15M
     * */
    ObjectProperty<DurationProp> durationProperty();
    default DurationProp getDuration() { return durationProperty().get(); }
    default void setDuration(DurationProp duration) { durationProperty().set(duration); }
    default void setDuration(String duration)
    {
        if (getDuration() == null)
        {
            setDuration(DurationProp.parse(duration));
        } else
        {
            DurationProp temp = DurationProp.parse(duration);
            if (temp.getValue().getClass().equals(getDuration().getValue().getClass()))
            {
                getDuration().setValue(temp.getValue());
            } else
            {
                setDuration(temp);
            }
        }
    }
    default void setDuration(TemporalAmount duration)
    {
        if (getDuration() == null)
        {
            setDuration(new DurationProp(duration));
        } else
        {
            getDuration().setValue(duration);
        }
    }
    default T withDuration(TemporalAmount duration)
    {
        if (getDuration() == null)
        {
            setDuration(duration);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withDuration(String duration)
    {
        if (getDuration() == null)
        {
            setDuration(duration);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }

    default T withDuration(DurationProp duration)
    {
        if (getDuration() == null)
        {
            setDuration(duration);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
}
