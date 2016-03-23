package jfxtras.labs.icalendar.properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

public abstract class PropertyBase implements Property
{
    @Override
    public ObservableList<Object> otherParameters() { return otherParameters; }
    private ObservableList<Object> otherParameters = FXCollections.observableArrayList();
    
    /** The type of the property in the content line, such as DESCRIPTION */
    @Override
    public PropertyEnum propertyType() { return propertyType; }
    private PropertyEnum propertyType;

    /**
     * List of all parameter enums in this property
     */
    public List<ParameterEnum> parameters() { return parameters; }
    private List<ParameterEnum> parameters = new ArrayList<>();
    
    /*
     * CONSTRUCTORS
     */
    
    /**
     * construct new property by parsing content line
     * sets parameters by running parseAndSet for each parameter enum
     * 
     * @param propertyString
     */
    public PropertyBase(String propertyString)
    {
        this();
        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyString);
        
        // add parameters
        map.entrySet()
            .stream()
            .filter(e -> ! (e.getKey() == ICalendarUtilities.PROPERTY_VALUE_KEY))
            .forEach(e ->
            {
    //            System.out.println("parameter:" + e.getKey());
                ParameterEnum p = ParameterEnum.enumFromName(e.getKey());
                if (p != null)
                {
                    p.parseAndSet(this, e.getValue());
                }                    
            });
        // add property value
        propertyValueString = map.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
    }
    private String propertyValueString;
    String getPropertyValueString() { return propertyValueString; }
    
    // construct empty property
    public PropertyBase()
    {
        propertyType = PropertyEnum.enumFromClass(getClass());
    }
    
    // copy constructor
    public PropertyBase(Property source)
    {
        this.propertyType = source.propertyType();
        parameters().stream().forEach(p -> p.copyTo(source, this));
//        parameters().stream().forEach(p -> p.copyTo(this));
//        ICalendarParameter.values(getClass())
//                .stream()
//                .forEach(p -> p.copyTo(source, this));
//        PropertyType.enumFromName(propertyName()).copyTo(source, this);
//        setValue = source.getValue();
//        source.copyValueTo(this);
    }
    
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
        StringBuilder builder = new StringBuilder(propertyType().toString());
        parameters().stream().forEach(p -> builder.append(p.toContentLine(this)));
        otherParameters().stream().forEach(p -> builder.append(";" + p));
        builder.append(":" + getValue().toString());
        return builder.toString();
    }
}
