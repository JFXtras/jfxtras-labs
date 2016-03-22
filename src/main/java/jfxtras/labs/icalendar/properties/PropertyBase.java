package jfxtras.labs.icalendar.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.parameters.ICalendarParameter;
import jfxtras.labs.icalendar.parameters.Parameter;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

public abstract class PropertyBase implements Property
{
    // Uses lazy initialization because its rarely used
    @Override
    public ObservableList<Object> getOtherParameters()
    {
        if (otherParameters == null)
        {
            otherParameters = FXCollections.observableArrayList();
        }
        return otherParameters;
    }
    ObservableList<Object> otherParameters;
    @Override
    public void setOtherParameters(ObservableList<Object> other)
    {
        this.otherParameters = other;
    }
    
    /** The name of the property in the content line, such as DESCRIPTION */
    @Override
    public String propertyName() { return propertyName; }
    private String propertyName;

    /**
     * List of all parameters in this property
     */
    public List<Parameter> parameters() { return parameters; }
    private List<Parameter> parameters = new ArrayList<>();
    
    /*
     * CONSTRUCTORS
     */
    
    // construct new property by parsing content line
    public PropertyBase(String name, String propertyString)
    {
        this(name);
        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyString);
        
        // add parameters
        map.entrySet()
            .stream()
            .filter(e -> ! (e.getKey() == ICalendarUtilities.PROPERTY_VALUE_KEY))
            .forEach(e ->
            {
    //            System.out.println("parameter:" + e.getKey());
                ICalendarParameter p = ICalendarParameter.enumFromName(e.getKey());
                if (p != null)
                {
                    p.parseAndSetValue(this, e.getValue());
                }                    
            });
        // add property value
        parseAndSetValue(map.get(ICalendarUtilities.PROPERTY_VALUE_KEY));
    }
    
    // construct empty property
    public PropertyBase(String name)
    {
        this.propertyName = name;        
    }
    
    // copy constructor
    public PropertyBase(Property source)
    {
        this.propertyName = source.propertyName();
        parameters().stream().forEach(p -> p.copyTo(this));
//        ICalendarParameter.values(getClass())
//                .stream()
//                .forEach(p -> p.copyTo(source, this));
//        PropertyType.enumFromName(propertyName()).copyTo(source, this);
//        setValue = source.getValue();
//        source.copyValueTo(this);
    }
    
    /*
     * NEED THE FOLLOWING
     * TO STRING
     * PARSE STRING
     * COPY
     * EQUALS
     */
    
    /**
     * Return property content line for iCalendar output files.  See RFC 5545 3.5
     * Contains component property with its value and any populated parameters.
     * 
     * For example: SUMMARY;LANGUAGE=en-US:Company Holiday Party
     * 
     * @return - the content line
     */
    @Override
    public String toContentLine()
    {
        StringBuilder builder = new StringBuilder(propertyName());
        parameters().stream().forEach(p -> builder.append(p.toContentLine()));
        builder.append(":" + getValue().toString());
        return builder.toString();
    }
}
