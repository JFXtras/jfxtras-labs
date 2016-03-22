package jfxtras.labs.icalendar.parameters;

import java.util.Arrays;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ParameterTextListBase implements Parameter
{
    private final String parameterName;
    
    @Override
    public ObservableList<String> getValue() { return value; }
    private ObservableList<String> value = FXCollections.observableArrayList();
    public void setValue(ObservableList<String> value) { this.value = value; }
    
    private String parseValue(String content)
    {
        final char quote = '\"';
        StringBuilder builder = new StringBuilder(content);
        if (builder.charAt(0) == quote)
        {
            builder.deleteCharAt(0);
        }
        if (builder.charAt(builder.length()-1) == quote)
        {
            builder.deleteCharAt(builder.length()-1);
        }
        String noQuoteString = builder.toString();
        int equalIndex = noQuoteString.indexOf('=');
        return (equalIndex >= 0) ? noQuoteString.substring(equalIndex+1) : noQuoteString;
    }
    
    @Override
    public String toContentLine()
    {
        return ";" + parameterName + "=" + getValue();
    }
    
    /*
     * CONSTRUCTORS
     */
    public ParameterTextListBase(String name, String content)
    {
        this(name);
        String content2 = Parameter.removeName(content, name);
        value.addAll( Arrays.stream(content2.split(","))
                .map(t -> Parameter.removeDoubleQuote(t))
                .collect(Collectors.toList()) );
    }

    // copy constructor
    public ParameterTextListBase(String name, ParameterTextListBase source)
    {
        this(name);
        setValue(source.getValue());
    }
    
    public ParameterTextListBase(String name)
    {
        this.parameterName = name;
    }
}
