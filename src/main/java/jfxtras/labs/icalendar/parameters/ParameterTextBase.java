package jfxtras.labs.icalendar.parameters;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ParameterTextBase implements Parameter
{
    private final String parameterName;
    
    @Override
    public String getValue() { return value.get(); }
    public StringProperty valueProperty() { return value; }
    private StringProperty value;
    public void setValue(String value) { this.value.set(value); }
    
    @Override
    public String toContentLine()
    {
        return ";" + parameterName + "=" + getValue();
    }
    
    /*
     * CONSTRUCTORS
     */
    public ParameterTextBase(String name, String content)
    {
        this(name);
        String content2 = Parameter.removeName(content, name);
        setValue(Parameter.removeDoubleQuote(content2));
    }

    // copy constructor
    public ParameterTextBase(String name, ParameterTextBase source)
    {
        this(name);
        setValue(source.getValue());
    }
    
    public ParameterTextBase(String name)
    {
        this.parameterName = name;
        value = new SimpleStringProperty(this, parameterName);
    }
}
