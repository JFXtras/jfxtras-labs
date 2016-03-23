package jfxtras.labs.icalendar.properties;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

/**
 * Base iCalendar property class
 * Contains other-parameters
 * Also contains methods used by all properties
 * 
 * @author David Bal
 *
 */
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
    // TODO - MAY NOT KEEP?  MAY JUST LOOP THROUGH ALL PARAMETERS INSTEAD
    public Collection<ParameterEnum> parameters() { return parameters; }
    private Collection<ParameterEnum> parameters = new HashSet<>();
    
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

    @Override
    public int hashCode()
    {
        // TODO Auto-generated method stub
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        PropertyBase testObj = (PropertyBase) obj;
        boolean valueEquals = getValue().equals(testObj.getValue());
        boolean otherParametersEquals = otherParameters().equals(testObj.otherParameters());
        boolean parametersEquals = parameters()
               .stream()
               .map(p -> p.isEqualTo(this, testObj))
//              .peek(e -> System.out.println(e.toString() + " equals:" + e.isPropertyEqual(this, testObj)))
              .allMatch(b -> b == true);
        return valueEquals && otherParametersEquals && parametersEquals;
    }

    @Override
    public String toString()
    {
        // TODO Auto-generated method stub
        return super.toString();
    }
}
