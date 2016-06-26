package jfxtras.labs.icalendarfx;

import java.util.List;

/**
 * Interface for all calendar elements.  These include
 * calendar components (e.g. VEVENT), calendar properties (e.g. VERSION) and parameters (i.e. VALUE).
 * 
 * @author David Bal
 *
 */
public interface VCalendarElement
{
    /**
     * Produce iCalendar content string
     * 
     * @return the content string
     */
    String toContent();
    
    /** Parse content line into calendar element */
    void parseContent(String content);
    
    /**
     * Checks element to determine if necessary properties are set.
     * 
     * @return - true if component is valid, false otherwise
     */
    default boolean isValid() { return errors().isEmpty(); }
    
    /**
     * Produces a list of error messages indicating problems with calendar element
     * 
     * @return - list of error messages
     */
    List<String> errors();

//    /** Make a copy of the element and assign it to the appropriate property in the parent 
//     * @param <P>*/
//    <P> void copyToParent(P parent);
    
//    void copyToParent(P parent);

//    <V extends VCalendarElement> void copyToNewParent(V vComponent);
}
