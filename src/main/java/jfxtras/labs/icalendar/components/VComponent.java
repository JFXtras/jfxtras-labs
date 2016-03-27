package jfxtras.labs.icalendar.components;

import javafx.collections.ObservableList;

/**
 * top-level iCalendar component
 * 
 * @author David Bal
 * @see VEventOld
 * @see VTodoOld
 * @see VJournalOld
 * @see VFreeBusy
 * @see VTimeZoneOld
 * @see VAlarmOld
 */
public interface VComponent
{
    /**
     * 3.8.8.2.  Non-Standard Properties
     * Any property name with a "X-" prefix
     * 
     * Example:
     * X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.
     *  org/mysubj.au
     */
    ObservableList<String> getXProperties();
    void setXProperties(ObservableList<String> xprop);
    
    /**
     * 3.8.8.1.  IANA Properties
     * An IANA-registered property name
     * 
     * Examples:
     * NON-SMOKING;VALUE=BOOLEAN:TRUE
     * DRESSCODE:CASUAL
     */
    ObservableList<String> getIANAProperties();
    void setIANAProperties(ObservableList<String> iana);
    
//    /**
//     * 
//     * @return
//     */
//    Map<PropertyEnum, List<Property>> propertyMap(); // Object can be a List or a single property
//    List<Pair<PropertyEnum, Property>> properties();
    
}
