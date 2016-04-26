package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.descriptive.GeographicPosition;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Priority;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Resources;

/**
 * Calendar component with location, among other properties
 * 
 * @author David Bal
 *
 * @param <I> class of recurrence instance
 * @see VEvent
 * @see VTodoOld
 */
public interface VComponentLocatable<T> extends VComponentDisplayable<T>, VComponentDuration<T>, VComponentDescribable2<T>
{
//    /**
//     * DESCRIPTION:
//     * RFC 5545 iCalendar 3.8.1.12. page 84
//     * This property provides a more complete description of the
//     * calendar component than that provided by the "SUMMARY" property.
//     * Example:
//     * DESCRIPTION:Meeting to provide technical review for "Phoenix"
//     *  design.\nHappy Face Conference Room. Phoenix design team
//     *  MUST attend this meeting.\nRSVP to team leader.
//     */
//    public ObjectProperty<Description> descriptionProperty();
//    default Description getDescription() { return descriptionProperty().get(); }
//    default void setDescription(Description description) { descriptionProperty().set(description); }
//    default T withDescription(Description description) { setDescription(description); return (T) this; }
//    default T withDescription(String description) { PropertyEnum.DESCRIPTION.parse(this, description); return (T) this; }

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
    default T withGeographicPosition(GeographicPosition geographicPosition) { setGeographicPosition(geographicPosition); return (T) this; }
    default T withGeographicPosition(String geographicPosition) { PropertyEnum.GEOGRAPHIC_POSITION.parse(this, geographicPosition); return (T) this; }
    
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
    default T withLocation(Location location) { setLocation(location); return (T) this; }
    default T withLocation(String location) { PropertyEnum.LOCATION.parse(this, location); return (T) this; }
    
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
    default void setPriority(int priority) { priorityProperty().set(new Priority(priority)); }
    default T withPriority(Priority priority) { setPriority(priority); return (T) this; }
    default T withPriority(String priority) { PropertyEnum.PRIORITY.parse(this, priority); return (T) this; }
    default T withPriority(int priority) { setPriority(priority); return (T) this; }
    
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
        Arrays.stream(resources).forEach(c -> PropertyEnum.RESOURCES.parse(this, c));
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
}
