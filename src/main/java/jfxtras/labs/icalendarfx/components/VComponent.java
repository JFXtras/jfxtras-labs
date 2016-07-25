package jfxtras.labs.icalendarfx.components;

import jfxtras.labs.icalendarfx.VChild;
import jfxtras.labs.icalendarfx.VParent;

/**
 * iCalendar component with no properties
 * 
 * @author David Bal
 */
public interface VComponent extends VParent, VChild
{
    /**
     * Returns the enum for the component as it would appear in the iCalendar content line
     * Examples:
     * VEVENT
     * VJOURNAL
     * 
     * @return - the component enum
     */
    String componentName();
    
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
    
    /** Copy children and parent from source into this VComponent */
    default void copyFrom(VComponent source)
    {
        setParent(source.getParent());
        copyChildrenFrom(source);
    }
}
