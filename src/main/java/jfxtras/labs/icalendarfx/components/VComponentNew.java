package jfxtras.labs.icalendarfx.components;

import java.util.List;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;

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
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see VTimeZone
 * @see VAlarm
 */
public interface VComponentNew
{
    /**
     * Returns the enum for the component as it would appear in the iCalendar content line
     * Examples:
     * VEVENT
     * VJOURNAL
     * 
     * @return - the component enum
     */
    VComponentEnum componentType();
    
    /** first line of component delimiter string */
    default String firstContentLine()
    {
        return "BEGIN:" + componentType().toString();
    }

    /** last line of component delimiter string */    
    default String lastContentLine()
    {
        return "END:" + componentType().toString();
    }
    
    /**
     * 3.8.8.2.  Non-Standard Properties
     * Any property name with a "X-" prefix
     * 
     * Example:
     * X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.
     *  org/mysubj.au
     */
    ObservableList<NonStandardProperty> getNonStandardProperties();
    void setNonStandardProperties(ObservableList<NonStandardProperty> properties);
    
    /**
     * 3.8.8.1.  IANA Properties
     * An IANA-registered property name
     * 
     * Examples:
     * NON-SMOKING;VALUE=BOOLEAN:TRUE
     * DRESSCODE:CASUAL
     */
    ObservableList<IANAProperty> getIANAProperties();
    void setIANAProperties(ObservableList<IANAProperty> properties);
    
    /**
     * List of all properties found in component.
     * The list is unmodifiable.
     * 
     * @return - the list of properties
     */
    List<PropertyEnum> properties();
    

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
    CharSequence toContentLines();
}
