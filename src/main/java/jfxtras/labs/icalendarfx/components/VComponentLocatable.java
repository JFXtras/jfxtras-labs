package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.GeographicPosition;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Priority;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Resources;

/**
 * Calendar component with location, among other properties
 * 
 * @author David Bal
 *
 * @param <T> - implemented class
 * @param <R> - class of Recurrence instance
 * @see VEventOld
 * @see VTodoOld
 */
public interface VComponentLocatable<T> extends VComponentDisplayable<T>, VComponentDuration<T>, VComponentDescribable2<T>
{
    /**
     * GEO: Geographic Position
     * RFC 5545 iCalendar 3.8.1.6 page 85, 3.3.6 page 85
     * This property specifies information related to the global
     * position for the activity specified by a calendar component.
     * 
     * This property value specifies latitude and longitude,
     * in that order (i.e., "LAT LON" ordering).
     * 
     * Example:
     * GEO:37.386013;-122.082932
     */
    public ObjectProperty<GeographicPosition> geographicPositionProperty();
    default GeographicPosition getGeographicPosition() { return geographicPositionProperty().get(); }
    default void setGeographicPosition(GeographicPosition geographicPosition) { geographicPositionProperty().set(geographicPosition); }
    default void setGeographicPosition(String geographicPosition) { setGeographicPosition(GeographicPosition.parse(geographicPosition)); }
    default void setGeographicPosition(double latitude, double longitude) { setGeographicPosition(new GeographicPosition(latitude, longitude)); }
    default T withGeographicPosition(GeographicPosition geographicPosition)
    {
        if (getGeographicPosition() == null)
        {
            setGeographicPosition(geographicPosition);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withGeographicPosition(String geographicPosition)
    {
        if (getGeographicPosition() == null)
        {
            setGeographicPosition(geographicPosition);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withGeographicPosition(double latitude, double longitude)
    {
        if (getGeographicPosition() == null)
        {
            setGeographicPosition(latitude, longitude);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    
    /**
     * LOCATION:
     * RFC 5545 iCalendar 3.8.1.12. page 87
     * This property defines the intended venue for the activity
     * defined by a calendar component.
     * Example:
     * LOCATION:Conference Room - F123\, Bldg. 002
     */
    public ObjectProperty<Location> locationProperty();
    default Location getLocation() { return locationProperty().get(); }
    default void setLocation(Location location) { locationProperty().set(location); }
    default void setLocation(String location) { setLocation(Location.parse(location)); }
    default T withLocation(Location location)
    {
        if (getLocation() == null)
        {
            setLocation(location);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withLocation(String location)
    {
        if (getLocation() == null)
        {
            setLocation(location);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }

    /**
     * PRIORITY
     * RFC 5545 iCalendar 3.8.1.6 page 85, 3.3.6 page 85
     * This property defines the relative priority for a calendar component.
     * This priority is specified as an integer in the range 0 to 9.
     * 
     * Example: The following is an example of a property with the highest priority:
     * PRIORITY:1
     */
    public ObjectProperty<Priority> priorityProperty();
    default Priority getPriority() { return priorityProperty().get(); }
    default void setPriority(Priority priority) { priorityProperty().set(priority); }
    default void setPriority(String priority) { setPriority(Priority.parse(priority)); }
    default void setPriority(int priority) { setPriority(new Priority(priority)); }
    default T withPriority(Priority priority)
    {
        if (getPriority() == null)
        {
            setPriority(priority);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withPriority(String priority)
    {
        if (getPriority() == null)
        {
            setPriority(priority);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withPriority(int priority)
    {
        if (getPriority() == null)
        {
            setPriority(priority);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    
    /**
     * RESOURCES:
     * RFC 5545 iCalendar 3.8.1.10. page 91
     * This property defines the equipment or resources
     * anticipated for an activity specified by a calendar component.
     * More than one resource can be specified as a COMMA-separated list
     * Example:
     * RESOURCES:EASEL,PROJECTOR,VCR
     * RESOURCES;LANGUAGE=fr:Nettoyeur haute pression
     */
    ObservableList<Resources> getResources();
    void setResources(ObservableList<Resources> properties);
    default T withResources(ObservableList<Resources> resources) { setResources(resources); return (T) this; }
    default T withResources(String...resources)
    {
        Arrays.stream(resources).forEach(c -> PropertyType.RESOURCES.parse(this, c));
        return (T) this;
    }
    default T withResources(Resources...resources)
    {
        if (getResources() == null)
        {
            setResources(FXCollections.observableArrayList(resources));
        } else
        {
            getResources().addAll(resources);
        }
        return (T) this;
    }
    
    /** 
     * VALARM
     * Alarm Component
     * RFC 5545 iCalendar 3.6.6. page 71
     * 
     * Provide a grouping of component properties that define an alarm.
     * MUST be contained inside a VEVENT or VTODO component
     */
    ObservableList<VAlarm> getVAlarms();
    void setVAlarms(ObservableList<VAlarm> properties);
    default T withVAlarms(ObservableList<VAlarm> vAlarms) { setVAlarms(vAlarms); return (T) this; }
    default T withVAlarms(VAlarm...vAlarms)
    {
        if (getVAlarms() == null)
        {
            setVAlarms(FXCollections.observableArrayList(vAlarms));
        } else
        {
            getVAlarms().addAll(vAlarms);
        }
        return (T) this;
    }
}
