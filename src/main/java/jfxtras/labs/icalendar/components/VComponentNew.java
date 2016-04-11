package jfxtras.labs.icalendar.components;

import java.util.List;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.properties.PropertyEnum;

/**
 * top-level iCalendar component
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodoOld
 * @see VJournalOld
 * @see VFreeBusy
 * @see VTimeZoneOld
 * @see VAlarmOld
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
    
    default String firstContentLine()
    {
        return "BEGIN:" + componentType().toString();
    }
    
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
//    ObservableList<String> getXProperties();
//    void setXProperties(ObservableList<String> xprop);
    
    /**
     * 3.8.8.1.  IANA Properties
     * An IANA-registered property name
     * 
     * Examples:
     * NON-SMOKING;VALUE=BOOLEAN:TRUE
     * DRESSCODE:CASUAL
     */
//    ObservableList<String> getIANAProperties();
//    void setIANAProperties(ObservableList<String> iana);
    
    
    ObservableList<Object> otherProperties();
    
    
    List<PropertyEnum> properties();
    //    /**
//     * 
//     * @return
//     */
//    Map<PropertyEnum, List<Property>> propertyMap(); // Object can be a List or a single property
//    List<Pair<PropertyEnum, Property>> properties();
    CharSequence toContentLines();
}
