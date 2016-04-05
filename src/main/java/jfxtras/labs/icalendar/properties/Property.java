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
     * Returns the enum for the property as it would appear in the iCalendar content line
     * Examples:
     * DESCRIPTION
     * UID
     * PRODID
     * 
     * @return - the property enum
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
    /** Set the value of the property */
    void setValue(U value);
        
    /**
     * Property value type.  Optional in most cases.
     * Example:
     * VALUE=DATE
     */
    ValueParameter getValueParameter();
    /** property for value type */
    ObjectProperty<ValueParameter> valueParameterProperty();
    /** Set the value type */
    void setValueParameter(ValueParameter value);
    
    /**
     * other-param, 3.2 RFC 5545 page 14
     * the parameter name and value are combined into one object
     */
    ObservableList<Object> otherParameters();
    
    /**
     * List of parameters contained in the property
     */
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
    
}
