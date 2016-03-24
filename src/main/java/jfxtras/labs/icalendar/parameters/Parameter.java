package jfxtras.labs.icalendar.parameters;

@Deprecated
public interface Parameter
{
//    /**
//     * Parse string representation of parameter value and set parameter's value
//     * For example:
//     * MEMBER="mailto:ietf-calsch@example.org"
//     * sets the string ietf-calsch@example.org into the parameter's value field.
//     * The mailto and double quotes are omitted, but return in the toContentLine method.
//     */
//    Object parseValue(String value);
    
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
