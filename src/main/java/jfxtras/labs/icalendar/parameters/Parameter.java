package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.properties.Property;

public interface Parameter
{
    /**
     * Parse string representation of parameter value and set parameter's value
     * For example:
     * MEMBER="mailto:ietf-calsch@example.org"
     * sets the string ietf-calsch@example.org into the parameter's value field.
     * The mailto and double quotes are omitted, but return in the toContentLine method.
     */
    void parseAndSetValue(String value);
    /**
     * return parameter name-value pair string separated by an "="
     * for example:
     * LANGUAGE=en-US
     */
    String toContentLine();
    
    /**
     * The value of the parameter.
     * 
     * For example, in the below parameter:
     * CN=David Bal
     * The value is David Bal
     * 
     * Note: the value's object must have an overridden toString method that complies
     * with iCalendar content line output.
     */
    Object getValue();
    
    /**
     * 
     * @param property - destination property
     * @return
     */
    Object copyTo(Property destination);
}
