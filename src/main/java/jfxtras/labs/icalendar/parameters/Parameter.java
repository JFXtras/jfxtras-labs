package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.properties.Property;

/**
 * Every parameter requires the following methods:
 * toContentLine - make iCalendar string
 * getValue - return parameters value
 * isEqualsTo - checks equality between two parameters
 * parse - convert string into parameter - this method is in ParameterEnum
 * 
 * @author David Bal
 *
 */
public interface Parameter
{    
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
     * return parameter name-value pair string separated by an "="
     * for example:
     * LANGUAGE=en-US
     */
    String toContentLine();
    
    /**
     * Copy parameter from source property to destination property
     * 
     * @param source
     * @param propertyBase
     */
    void copyTo(Property source, Property destination);
    
       
    
    boolean isEqualTo(Property property, Property testObj);


//    /**
//     * 
//     * @param property - destination property
//     * @return
//     */
//    void copyTo(Property destination);
    
    /**
     * Remove leading and trailing double quotes
     * 
     * @param input - string with or without double quotes at front and end
     * @return - string stripped of leading and trailing double quotes
     */
    static String removeDoubleQuote(String input)
    {
        final char quote = '\"';
        StringBuilder builder = new StringBuilder(input);
        if (builder.charAt(0) == quote)
        {
            builder.deleteCharAt(0);
        }
        if (builder.charAt(builder.length()-1) == quote)
        {
            builder.deleteCharAt(builder.length()-1);
        }
        return builder.toString();
    }
    
    /**
     * Remove parameter name and equals sign, if present, otherwise return input string
     * 
     * @param input - parameter content with or without name and equals sign
     * @param name - name of parameter
     * @return - nameless string
     * 
     * example input:
     * ALTREP="CID:part3.msg.970415T083000@example.com"
     * output:
     * "CID:part3.msg.970415T083000@example.com"
     */
    static String removeName(String input, String name)
    {
        int nameIndex = input.indexOf(name + "=");
        return (nameIndex >= 0) ? input.substring(name.length()+2) : input;
            
    }


}
