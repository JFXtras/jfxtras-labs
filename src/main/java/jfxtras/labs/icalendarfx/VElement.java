package jfxtras.labs.icalendarfx;

import java.util.List;

/**
 * Interface for all calendar elements.  These include
 * calendar components (e.g. VEVENT), calendar properties (e.g. VERSION) and parameters (i.e. VALUE).
 * 
 * @author David Bal
 *
 */
public interface VElement
{
    /**
     * Produce iCalendar content string.  If element is a parent it {@link #toContent() }is invoked recursively to 
     * produce content of both parent and children.
     * 
     * @return the content string
     */
    String toContent();
    
    /** Parse content line into calendar element.
     * If element contains children {@link #parseContent(String)} is invoked recursively to parse child elements also
     *  */
    void parseContent(String content);
    
    /**
     * Checks element to determine if necessary properties are set.
     * {@link #isValid()} is invoked recursively to test child elements if element is a parent
     * 
     * @return - true if component is valid, false otherwise
     */
    default boolean isValid() { return errors().isEmpty(); }
    
    /**
     * Produces a list of error messages indicating problems with calendar element
     * {@link #errors()} is invoked recursively to return errors of child elements in addition to errors in parent
     * 
     * @return - list of error messages
     */
    List<String> errors();

}
