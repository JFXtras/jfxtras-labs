package jfxtras.labs.icalendar.parameters;

/**
 * Every parameter requires the following methods:
 * toContentLine - make iCalendar string
 * getValue - return parameters value
 * isEqualsTo - checks equality between two parameters
 * parse - convert string into parameter - this method is in ParameterEnum
 * 
 * @author David Bal
 * @param <U>
 *
 */
public interface Parameter<U> extends Comparable<Parameter<U>>
{    
    /**
     * The value of the parameter.
     * 
     * For example, in the below parameter:
     * CN=John Doe
     * The value is the String "John Doe"
     * 
     * Note: the value's object must have an overridden toString method that complies
     * with iCalendar content line output.
     */
    U getValue();
  
    /** Set the value of this parameter */  
    void setValue(U value);
    
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
    void copyTo(Parameter<U> destination);

//    /** implements Comparable */
//    int compareTo(Parameter<?> test);
    
       
//    /**
//     * Tests equality between two Parameter objects
//     * 
//     * @param property
//     * @param testObj
//     * @return
//     */
//    @Deprecated
//    boolean isEqualTo(Parameter<U> parameter1, Parameter<U> parameter2);
    



}
