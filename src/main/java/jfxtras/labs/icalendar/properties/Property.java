package jfxtras.labs.icalendar.properties;

import javafx.collections.ObservableList;

/**
 * top-level interface for all iCalendar properties
 * 
 * @author David Bal
 * @see PropertyType - enum of all supported Properties
 * @see PropertyBase
 * @see PropertyTextBase2
 */
public interface Property
{
    /**
     * Parse content line string into property's value type
     * 
     * @param value - string value
     */
    void parseAndSetValue(String value);

    /**
     * Converts the property's value to a string for the content line
     * 
     * @return - string representation of property value consistent with iCalendar standards
     */
    String toContentLine();

    /**
     * Returns the name of the property as it would appear in the iCalendar content line
     * Examples:
     * DESCRIPTION
     * UID
     * PRODID
     * 
     * @return - the property name
     */
    String propertyName();
    
    /**
     * The value of the property.
     * 
     * For example, in the below property:
     * LOCATION:Bob's house
     * The value is Bob's house
     * 
     * Note: the value's object must have an overridden toString method that complies
     * with iCalendar content line output.
     */
    Object getValue();
    
    /**
     * other-param, 3.2 RFC 5545 page 14
     */
    ObservableList<Object> getOtherParameters();
    void setOtherParameters(ObservableList<Object> other);
    
//    /**
//     * List of all parameters in this property
//     */
//    List<Parameter> parameters();
}
