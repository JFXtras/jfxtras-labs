package jfxtras.labs.icalendarfx.parameters;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import jfxtras.labs.icalendarfx.VParent;

/**
 * Base class of all iCalendar Parameters.  Parameters can't have children.
 * Example VALUE=DATE
 * 
 * @author David Bal
 *
 * @param <T> - type of value stored in the Parameter, such as String for text-based, or the enumerated type of the classes based on an enum
 * @param <U> - implemented subclass
 */
abstract public class ParameterBase<U,T> implements Parameter<T>
{
    private VParent myParent;
    @Override public void setParent(VParent parent) { myParent = parent; }
    @Override public VParent getParent() { return myParent; }
    
    final private String name;
    @Override
    public String name() { return name; }

    /*
     * PARAMETER VALUE
     */
    @Override
    public T getValue()
    {
        return value.get();
    }
    @Override
    public ObjectProperty<T> valueProperty()
    {
        return value;
    }
    private ObjectProperty<T> value;
    @Override
    public void setValue(T value)
    {
        this.value.set(value);
    }
    public U withValue(T value)
    {
        setValue(value);
        return (U) this;
    }

    @Override
    public String toContent()
    {
        String string = valueAsString();
        String content = (getValue() != null) ? parameterType().toString() + "=" + string : null;
        return content;
    }
    
    // convert value to string, is overridden for enum-based parameters to handle UNKNOWN value
    String valueAsString()
    {
        return getConverter().toString(getValue());
    }
    
    @Override
    public List<String> parseContent(String content)
    {
//        System.out.println("parameter content:"+ content);
//        // verify left side of equals is a parameter name, or assume content is parameter value
//        int equalsIndex = content.indexOf('=');
//        final String valueString;
//        if (equalsIndex > 0)
//        {
//            String name = content.substring(0, equalsIndex);
//            boolean hasName1 = ParameterType.enumFromName(name.toUpperCase()) != null;
//            boolean hasName2 = IANAParameter.REGISTERED_IANA_PARAMETER_NAMES.contains(name.toUpperCase());
//            System.out.println("hasName:" + hasName1 + " " + hasName2 + " " + name);
//            valueString = (hasName1 || hasName2) ? content.substring(equalsIndex+1) : content;    
//        } else
//        {
//            valueString = content;
//        }
        String valueString = Parameter.extractValue(content);
        T value = getConverter().fromString(valueString);
        System.out.println("valueString:" + valueString + " " + value);
        setValue(value);
        return errors();
    }
    
    /**
     * STRING CONVERTER
     * 
     * Get the parameter value string converter.
     */ 
    private StringConverter<T> getConverter()
    {
        return converter;
    }
    final private StringConverter<T> converter;
    
    /**
     * PARAMETER TYPE
     * 
     *  The enumerated type of the parameter.
     */
    @Override
    public ParameterType parameterType() { return parameterType; }
    final private ParameterType parameterType;

    @Override
    public String toString()
    {
        return super.toString() + "," + toContent();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        ParameterBase<U,T> testObj = (ParameterBase<U,T>) obj;

        return getValue().equals(testObj.getValue());
    }
    
    @Override
    public int hashCode()
    {
        return getValue().hashCode();
    }
    
    @Override
    public int compareTo(Parameter<T> test)
    {
        return toContent().compareTo(test.toContent());
    }
    
    /*
     * CONSTRUCTORS
     */
    ParameterBase()
    {
        parameterType = ParameterType.enumFromClass(getClass());
        name = parameterType.toString();
        converter = parameterType.getConverter();
        value = new SimpleObjectProperty<>(this, parameterType.toString());
    }

    ParameterBase(T value)
    {
        this();
        setValue(value);
    }

    
    ParameterBase(ParameterBase<U,T> source)
    {
        this();
        setValue(source.getValue());
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
        if (getValue() == null)
        {
            errors.add(name() + " value is null.  The parameter MUST have a value."); 
        }
        return errors;
    }
}
