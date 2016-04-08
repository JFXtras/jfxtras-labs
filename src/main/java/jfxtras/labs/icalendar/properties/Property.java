package jfxtras.labs.icalendar.properties;

import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueParameter;

/**
 * top-level interface for all iCalendar properties
 * 
 * @author David Bal
 * @see PropertyEnum - enum of all supported Properties
 * @see PropertyBase
 *
 * @param <T> - type of value stored in Property
 */
public interface Property<T>
{    
    /**
     * The value of the property.
     * 
     * For example, in the below property:
     * LOCATION;LANGUAGE=en:Bob's house
     * The value is the String "Bob's house"
     * 
     */
    T getValue();
    /** property value object property */
    ObjectProperty<T> valueProperty();    
    /** Set the value of the property */
    void setValue(T value);
        
    /**
     * VALUE
     * Value Date Types
     * RFC 5545 iCalendar 3.2.10 page 29
     * 
     * To explicitly specify the value type format for a property value.
     * 
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
     * Non-standard parameters
     * 
     * other-param, 3.2 RFC 5545 page 14
     * the parameter name and value are combined into one object
     */
    ObservableList<Object> otherParameters();
    
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
     * Get the property's value string converter.  There is a default converter in ValueType associated
     * with the default value type of the property.  For most value types that converter is
     * acceptable.  However, for the TEXT value type it often needs to be replaced.
     * For example, the value type for TimeZoneIdentifier is TEXT, but the Java object is
     * ZoneId.  A different converter is required to make the conversion to ZoneId.
     * 
     * @return the string converter for this property
     */
    StringConverter<T> getConverter();
    /**
     * Set the property's value string converter.  There is a default converter in ValueType associated
     * with the default value type of the property.  For most value types that converter is
     * acceptable.  However, for the TEXT value type it often needs to be replaced.
     * For example, the value type for TimeZoneIdentifier is TEXT, but the Java object is
     * ZoneId.  A different converter is required to make the conversion to ZoneId.
     * This method can replace the default converter. 
     */
    void setConverter(StringConverter<T> converter);
    
    /**
     * List of parameters contained in the property
     */
    List<ParameterEnum> parameters();
    
    /**
     * Converts the property's value to a string for the content line
     * 
     * @return - string representation of property value consistent with iCalendar standards
     */
    CharSequence toContentLine();
    
    
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
