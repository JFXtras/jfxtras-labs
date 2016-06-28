package jfxtras.labs.icalendarfx.parameters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * Base class of all iCalendar Parameters
 * Example VALUE=DATE
 * 
 * @author David Bal
 *
 * @param <T> - type of value stored in the Parameter, such as String for text-based, or the enumerated type of the classes based on an enum
 * @param <U> - implemented subclass
 */
abstract public class ParameterBase<U,T> implements Parameter<T>
{    
    @Override
    public T getValue() { return value.get(); }
    @Override
    public ObjectProperty<T> valueProperty() { return value; }
    private ObjectProperty<T> value;
    @Override
    public void setValue(T value) { this.value.set(value); }
    public U withValue(T value) { setValue(value); return (U) this; }

    @Override
    public String toContent()
    {
        final String value;
        if (getValue() instanceof Collection)
        {
            value = ((Collection<?>) getValue()).stream()
                    .map(obj -> addDoubleQuotesIfNecessary(obj.toString()))
                    .collect(Collectors.joining(","));
        } else if (getValue() instanceof Boolean)
        {
            value = getValue().toString().toUpperCase();
        } else
        {
            value = addDoubleQuotesIfNecessary(getValue().toString());
        }
        return valueToContent(value);
//        return (getValue() != null) ? parameterType().toString() + "=" + value : null;
    }
    
    String valueToContent(String value)
    {
        return (getValue() != null) ? parameterType().toString() + "=" + value : null;
    }
    
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
            errors.add(parameterType() + " value is null.  The parameter MUST have a value."); 
        }
        return errors;
    }
    
    /*
     * STATIC UTILITY METHODS
     */

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
     * Add Double Quotes to front and end of string if text contains \ : ;
     * 
     * @param text
     * @return
     */
    static String addDoubleQuotesIfNecessary(String text)
    {
        boolean hasDQuote = text.contains("\"");
        boolean hasColon = text.contains(":");
        boolean hasSemiColon = text.contains(";");
        if (hasDQuote || hasColon || hasSemiColon)
        {
            return "\"" + text + "\""; // add double quotes
        } else
        {
            return text;
        }
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
    @Deprecated // may find a use with component line parsing
    static String extractValue(String content, String name)
    {
        if (content.substring(0, name.length()).equals(name))
        {
            return content.substring(name.length()+1);
        }
        return content;
    }
}
