package jfxtras.labs.icalendarfx.components;

import java.util.Iterator;
import java.util.List;

import jfxtras.labs.icalendarfx.VChild;
import jfxtras.labs.icalendarfx.VParent;
import jfxtras.labs.icalendarfx.properties.component.misc.RequestStatus;

/**
 * Top-level calendar component
 * 
 * @author David Bal
 */
public interface VComponent extends VParent, VChild
{
    /**
     * <p>Returns the name of the component as it would appear in the iCalendar content line.</p>
     * <p>Examples:
     * <ul>
     * <li>VEVENT
     * <li>VJOURNAL
     * </ul>
     * </p>
     * @return - the component enum
     */
    String componentName();
    
    /**
     * <p>Returns content line for a calendar component.  See RFC 5545 3.4
     * Contains component properties with their values and any parameters.</p>
     * <p>
     * The following is a example of iCalendar content text:
     *  <ul>
     *  BEGIN:VEVENT<br>
     *  UID:19970610T172345Z-AF23B2@example.com<br>
     *  DTSTAMP:19970610T172345Z<br>
     *  DTSTART:19970714T170000Z<br>
     *  DTEND:19970715T040000Z<br>
     *  SUMMARY:Bastille Day Party<br>
     *  END:VEVENT
     *  </ul>
     * 
     * @return - the component content lines
     */
    @Override
    String toContent();
    
    /** Copy state from source VComponent */
    void copyFrom(VComponent source);
    
    /** Parse a VComponent from a {@code Iterator<String>}.  Returns list of error strings for {@link RequestStatus} */
    public List<String> parseContent(Iterator<String> contentLines);
}
