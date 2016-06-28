package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jfxtras.labs.icalendarfx.CalendarElementType;
import jfxtras.labs.icalendarfx.VCalendarElement;
import jfxtras.labs.icalendarfx.VCalendarParent;
import jfxtras.labs.icalendarfx.components.revisors.Revisable;
import jfxtras.labs.icalendarfx.properties.Property;
import jfxtras.labs.icalendarfx.properties.PropertyType;

/**
 * iCalendar component
 * Contains the following properties:
 * Non-Standard Properties, IANA Properties
 * 
 * @author David Bal
 * @see VComponentBase
 * 
 * @see VComponentPrimary
 * 
 * @see VEventNewInt
 * @see VTodoInt
 * @see VJournalInt
 * @see VFreeBusy
 * @see VTimeZone
 * @see VAlarmInt
 */
public interface VComponent extends VCalendarElement, VCalendarParent
{
    /**
     * Returns the enum for the component as it would appear in the iCalendar content line
     * Examples:
     * VEVENT
     * VJOURNAL
     * 
     * @return - the component enum
     */
    CalendarElementType componentType();

    /**
     * List of all properties enums found in component.
     * The list is unmodifiable.
     * 
     * @return - the list of properties enums
     */
    List<PropertyType> propertyEnums();
    /**
     * List of all properties found in component.
     * The list is unmodifiable.
     * 
     * @return - the list of properties
     */

    default List<Property<?>> properties()
    {
        return Collections.unmodifiableList(
                propertyEnums().stream().flatMap(e ->
        {
            Object obj = e.getProperty(this);
            if (obj instanceof Property)
            {
                return Arrays.asList((Property<?>) obj).stream();
            } else if (obj instanceof List)
            {
                return ((List<Property<?>>) obj).stream();
            } else
            {
                throw new RuntimeException("Unsupported property type:" + obj.getClass());
            }
        })
        .collect(Collectors.toList()));
    }
    
    /** Encapsulated Component editor */
    Revisable newRevisor();
    
    /**
     * Return property content line for iCalendar output files.  See RFC 5545 3.4
     * Contains component properties with their values and any parameters.
     * 
     * The following is a simple example of an iCalendar component:
     *
     *  BEGIN:VEVENT
     *  UID:19970610T172345Z-AF23B2@example.com
     *  DTSTAMP:19970610T172345Z
     *  DTSTART:19970714T170000Z
     *  DTEND:19970715T040000Z
     *  SUMMARY:Bastille Day Party
     *  END:VEVENT
     * 
     * @return - the component content lines
     */
    @Override
    String toContent();
}
