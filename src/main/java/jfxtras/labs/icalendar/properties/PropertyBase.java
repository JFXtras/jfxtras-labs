package jfxtras.labs.icalendar.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.parameters.Parameter;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.Value.ValueType;
import jfxtras.labs.icalendar.properties.calendar.CalendarScale;
import jfxtras.labs.icalendar.properties.calendar.Method;
import jfxtras.labs.icalendar.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendar.properties.calendar.Version;
import jfxtras.labs.icalendar.properties.component.relationship.UniqueIdentifier;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

/**
 * Base iCalendar property class
 * Contains other-parameters
 * Also contains methods used by all properties
 *  
 * @see UniqueIdentifier
 * @see CalendarScale
 * @see Method
 * @see ProductIdentifier
 * @see Version
 * 
 * @author David Bal
 *
 * @param <T> - type of implementing subclass
 * @param <U> - type of property value
 */
public abstract class PropertyBase<T,U> implements Property<U>
{
    @Override
    public U getValue() { return value.get(); }
    public ObjectProperty<U> valueProperty() { return value; }
    private ObjectProperty<U> value;
    @Override
    public void setValue(U value) { this.value.set(value); }
    public T withValue(U value) { setValue(value); return (T) this; }
    
    /**
     * VALUE: Value Data Types
     * RFC 5545, 3.2.20, page 28
     * To specify the value for text values in a property or property parameter.
     * This parameter is optional for properties when the default value type is used.
     * 
     * Examples:
     * VALUE=DATE-TIME  (Date-Time is default value, so it isn't necessary to specify)
     * VALUE=DATE
     */
    public ValueType getValueType() { return (valueType == null) ? _valueType : valueType.get(); }
    public ObjectProperty<ValueType> valueParameterProperty()
    {
        if (valueType == null)
        {
            valueType = new SimpleObjectProperty<>(this, ParameterEnum.VALUE_DATA_TYPES.toString(), _valueType);
        }
        return valueType;
    }
    private ValueType _valueType;
    private ObjectProperty<ValueType> valueType;
    public void setValueType(ValueType value)
    {
//        if (value != null)
//        {
//            parametersModifiable().add(ParameterEnum.VALUE_DATA_TYPES);
////            parameterMapModifiable().put(ParameterEnum.VALUE_DATA_TYPES, value);
//        } else
//        {
//            parametersModifiable().remove(ParameterEnum.VALUE_DATA_TYPES);            
////            parameterMapModifiable().remove(ParameterEnum.VALUE_DATA_TYPES);
//        }
//        parameterMap().put(ParameterEnum.VALUE_DATA_TYPES, value);
        if (this.valueType == null)
        {
            _valueType = value;
        } else
        {
            this.valueType.set(value);
        }
    }
    public T withValueType(ValueType value) { setValueType(value); return (T) this; } 
    
    /**
     * other-param, 3.2 RFC 5545 page 14
     * the parameter name and value are combined into one object
     */
    @Override
    public ObservableList<Object> otherParameters() { return otherParameters; }
    private ObservableList<Object> otherParameters = FXCollections.observableArrayList();
    public T withOtherParameters(Object... parameter) { otherParameters().addAll(parameter); return (T) this; }
    
    /** The type of the property in the content line, such as DESCRIPTION */
    @Override
    public PropertyEnum propertyType() { return propertyType; }
    private PropertyEnum propertyType;

    
    @Override
    public List<ParameterEnum> parameters()
    {
        List<ParameterEnum> populatedParameters = new ArrayList<>();
        Iterator<ParameterEnum> i = propertyType().possibleParameters().stream().iterator();
        while (i.hasNext())
        {
            ParameterEnum parameterType = i.next();
            Parameter<?> parameter = parameterType.getParameter(this);
            if (parameter != null)
            {
                populatedParameters.add(parameterType);
            }
        }
        return Collections.unmodifiableList(populatedParameters);
        
//        List<ParameterEnum> populatedParameters = propertyType().possibleParameters().stream()
//                .filter(p -> ! p.isEqualTo(this, null))
//                .collect(Collectors.toList());
//        return Collections.unmodifiableList(populatedParameters);
    }
    
    @Override
    @Deprecated
    public Map<ParameterEnum, Parameter<?>> parameterMap()
    {
        Map<ParameterEnum, Parameter<?>> populatedParameterMap = new LinkedHashMap<>();
        Iterator<ParameterEnum> i = propertyType().possibleParameters().stream().iterator();
        while (i.hasNext())
        {
            ParameterEnum parameterType = i.next();
            Parameter<?> parameter = parameterType.getParameter(this);
            if (parameter != null)
            {
                populatedParameterMap.put(parameterType, parameter);
            }
        }
        return Collections.unmodifiableMap(populatedParameterMap);
    }
//    final private List<ParameterEnum> parameters = new ArrayList<>();
//    protected List<ParameterEnum> parametersModifiable() { return parameters; }
    
    /*
     * PARAMETER MAPS
     */
    
//    @Override
//    public Map<ParameterEnum, Parameter<?>> parameterMap() { return Collections.unmodifiableMap(parameterMap); }
//    protected Map<ParameterEnum, Parameter<?>> parameterMapModifiable() { return parameterMap; }
//    private Map<ParameterEnum, Parameter<?>> parameterMap = new LinkedHashMap<>();

//    Map<ParameterEnum, List<Parameter<?>>> parametersList()
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
    
//    @Override
//    public List<Parameter<?>> parameters()
//    {
//        Stream<Parameter<?>> streamIndividual = parametersIndividual().entrySet().stream().map(e -> e.getValue());
//        Stream<Parameter<?>> streamList = parametersList().entrySet().stream().flatMap(e -> e.getValue().stream());
//        Comparator<? super Parameter<?>> comparator = (p1, p2) -> p1.toContentLine().compareTo(p2.toContentLine());
//        return Collections.unmodifiableList(Stream.concat(streamIndividual, streamList).sorted(comparator).collect(Collectors.toList()));
//    }
//    
//    private Collection<ParameterEnum> parameterEnums()
//    {
//        Stream<ParameterEnum> streamIndividual = parametersIndividual().entrySet().stream().map(e -> e.getKey());
//        Stream<ParameterEnum> streamList = parametersList().entrySet().stream().map(e -> e.getKey());
//        return Collections.unmodifiableList(Stream.concat(streamIndividual, streamList).collect(Collectors.toList()));
//    }
    
//    /**
//     * List of all parameter enums in this property
//     */
//    @Override
//    public Collection<ParameterEnum> parameters() { return parameters; }
//    private Collection<ParameterEnum> parameters = new HashSet<>();
    
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
        
        // strip off property name if present
        int endIndex = propertyType.toString().length();
        if (propertyString.substring(0, endIndex).equals(propertyType.toString()))
        {
            propertyString = propertyString.substring(endIndex);
        } else
        {
            propertyString = ":" + propertyString; // indicates propertyString is property value without any properties
        }
        
        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyString);
        // add parameters
        map.entrySet()
            .stream()
            .filter(e -> ! (e.getKey() == ICalendarUtilities.PROPERTY_VALUE_KEY))
            .forEach(e ->
            {
                ParameterEnum p = ParameterEnum.enumFromName(e.getKey());
                if (p != null)
                {
                    p.parse(this, e.getValue());
                } else if ((e.getKey() != null) && (e.getValue() != null))
                { // unknown parameter - store as other parameter
                    otherParameters().add(e.getKey() + "=" + e.getValue());
                } // if parameter doesn't contain both a key and a value it is ignored
            });
        // add property value
        propertyValueString = map.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
    }
    private String propertyValueString;
    protected String getPropertyValueString() { return propertyValueString; }
    
    // construct empty property
    public PropertyBase()
    {
        propertyType = PropertyEnum.enumFromClass(getClass());
        value = new SimpleObjectProperty<U>(this, propertyType.toString());
    }
    
    // copy constructor
    public PropertyBase(Property<?> source)
    {
//        parameterMap().entrySet().stream()
        Iterator<ParameterEnum> i = source.parameters().stream().iterator();
        while (i.hasNext())
        {
            ParameterEnum sourceParameterType = i.next();
            Parameter<?> sourceParameter = sourceParameterType.getParameter(source);
            sourceParameterType.copyPropertyTo(sourceParameter, this);
//            setParameter(sourceParameterType, sourceParameter);
//            sourceParameter.copyTo(destination);
//            Parameter<?> destinationParameter = sourceParameter.getClass()
//                    .getConstructor(sourceParameter.getClass())
//                    .newInstance(sourceParameter);
//            sourceParameterType.copyParameterTo(this, destinationParameter);
        }

//        parameters().stream().forEach(p -> p.getParameter(source).copyTo(p.getParameter(this)));
//        parameterMap().entrySet().stream()
//                .map(p -> p.getKey())
//                .forEach(p -> p.copyTo(source, this));
        this.propertyType = source.propertyType();
    }
    
//    private void setParameter(ParameterEnum sourceParameterType, Parameter<?> sourceParameter)
//    {
//        sourceParameterType.copyPropertyTo(sourceParameter, this);
//    }
    public PropertyBase(U value)
    {
        this();
        setValue(value);
    }
    
    /**
     * Return property content line for iCalendar output files.  See RFC 5545 3.5
     * Contains component property with its value and any populated parameters.
     * Only property name and parameter name/value pairs are added here.
     * Property value is added in subclasses
     * 
     * For example: SUMMARY;LANGUAGE=en-US:Company Holiday Party
     * 
     * @return - the content line
     */
    @Override
    public String toContentLine()
    {
        StringBuilder builder = new StringBuilder(propertyType().toString());
//        Stream<Parameter<?>> streamIndividual = parametersIndividual().entrySet().stream().map(e -> e.getValue());
//        Stream<Parameter<?>> streamList = parametersList().entrySet().stream().flatMap(e -> e.getValue().stream());
//        parameters().stream().forEach(p -> builder.append(p.toContentLine()));
        parameterMap().entrySet().stream()
                .map(p -> p.getValue())
                .forEach(p -> builder.append(p.toContentLine()));
        otherParameters().stream().forEach(p -> builder.append(";" + p));
        builder.append(":" + getValue().toString());
        return builder.toString();
    }
    
//    @Override
//    public String toContentLine()
//    {
//        StringBuilder builder = new StringBuilder(propertyType().toString());
//        otherParameters().stream().forEach(p -> builder.append(";" + p));
//        return builder.toString();
//    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = (31 * hash) + getValue().hashCode();
        Iterator<Entry<ParameterEnum, Parameter<?>>> i = parameterMap().entrySet().iterator();
        while (i.hasNext())
        {
            Parameter<?> parameter = i.next().getValue();
            hash = (31 * hash) + parameter.getValue().hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        PropertyBase<?,?> testObj = (PropertyBase<?,?>) obj;
        boolean valueEquals = getValue().equals(testObj.getValue());
        boolean otherParametersEquals = otherParameters().equals(testObj.otherParameters());
        
        final boolean parametersEquals;
        if (parameterMap().size() == testObj.parameterMap().size())
        {
            Iterator<Entry<ParameterEnum, Parameter<?>>> i1 = parameterMap().entrySet().iterator();
            Iterator<Entry<ParameterEnum, Parameter<?>>> i2 = testObj.parameterMap().entrySet().iterator();
            boolean isFailure = false;
            while (i1.hasNext())
            {
                Parameter<?> p1 = i1.next().getValue();
                Parameter<?> p2 = i2.next().getValue();
                if (! p1.equals(p2))
                {
                    isFailure = true;
                    break;
                }
            }
            parametersEquals = (isFailure) ? false : true;
        } else
        {
            parametersEquals = false;
        }
        
        return valueEquals && otherParametersEquals && parametersEquals;
    }

    @Override
    public String toString()
    {
        return super.toString() + "," + toContentLine();
    }
}
