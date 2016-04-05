package jfxtras.labs.icalendar.properties;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueParameter;

/**
 * top-level interface for all iCalendar properties
 * 
 * @author David Bal
 * @see PropertyEnum - enum of all supported Properties
 * @see PropertyBase
 * @see PropertyLanguage
 *
 * @param <U> - type of value stored in Property
 */
public interface Property<U>
{
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
     * LOCATION;LANGUAGE=en:Bob's house
     * The value is the String "Bob's house"
     * 
     */
    U getValue();

    /** property value object property */
    ObjectProperty<U> valueProperty();
    
    /** Set the value of this property */
    void setValue(U value);
        
    /**
     * Property value type.  Optional in most cases.
     * Example:
     * VALUE=DATE
     */
    ValueParameter getValueParameter();
    ObjectProperty<ValueParameter> valueParameterProperty();
    void setValueParameter(ValueParameter value);
    
    
    
    
    /**
     * other-param, 3.2 RFC 5545 page 14
     * the parameter name and value are combined into one object
     */
    ObservableList<Object> otherParameters();
    
//    /**
//     * Map of each represented parameter enum to its associated class
//     * Each parameter can occur only once
//     */
//    @Deprecated
//    Map<ParameterEnum, Parameter<?>> parameterMap();
//    
//    /**
//     * Map of each parameter that can occur more than once
//     */
//    Map<ParameterEnum, List<Parameter<?>>> parametersList();
    
    List<ParameterEnum> parameters();
    
    /**
     * Converts the property's value to a string for the content line
     * 
     * @return - string representation of property value consistent with iCalendar standards
     */
    String toContentLine();
    
    
    /**
     * tests if property's value and parameters are valid
     */
    default boolean isValid() { return getValue() != null; }
    
    /*
     * need: with map
     *  parse - in enum
     *  copy - in enum
     *  toContentLine - in class
     *  isEqualTo - use equals in class
     * 
     */
    
    /*
     * with collection
     * all in enum
     */
    
//    /**
//     * List of all parameters in this property
//     */
//    List<Parameter> parameters();
}
