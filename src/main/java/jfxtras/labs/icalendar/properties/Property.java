package jfxtras.labs.icalendar.properties;

import javafx.collections.ObservableList;

/**
 * top-level interface for all iCalendar properties
 * 
 * @author David Bal
 * @see PropertyEnum - enum of all supported Properties
 * @see PropertyBase
 * @see PropertyTextBase2
 */
public interface Property
{
//    /**
//     * Parse content line string into property's value and parameters
//     * 
//     * @param value - string value
//     */
//    void parseAndSetValue(String value);

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
    PropertyEnum propertyType();
    
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
     * the parameter name and value are combined into one object
     */
    ObservableList<Object> otherParameters();
    
    /**
     * Names of all the parameters embedded inside the property
     * Each parameter MUST not appear more than once.
     */
//    @Deprecated
//    Collection<ParameterEnum> parameters();
//    Map<ParameterEnum, Parameter> parameterMap();
    /*
     * need:
     *  parse - in enum
     *  copy - in class
     *  toContentLine - in class
     *  isEqualTo - in class
     * 
     */
    
//    /**
//     * List of all parameters in this property
//     */
//    List<Parameter> parameters();
}
