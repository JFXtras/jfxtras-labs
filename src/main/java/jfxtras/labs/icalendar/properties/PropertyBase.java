package jfxtras.labs.icalendar.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.parameters.Parameter;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.parameters.ValueType.ValueEnum;
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
     * RFC 5545, 3.2.20, page 29
     * To specify the value for text values in a property or property parameter.
     * This parameter is optional for properties when the default value type is used.
     * 
     * Examples:
     * VALUE=DATE-TIME  (Date-Time is default value, so it isn't necessary to specify)
     * VALUE=DATE
     */
    public ValueType getValueType() { return (valueType == null) ? null : valueType.get(); }
    public ObjectProperty<ValueType> valueParameterProperty()
    {
        if (valueType == null)
        {
            valueType = new SimpleObjectProperty<>(this, ParameterEnum.VALUE_DATA_TYPES.toString());
        }
        return valueType;
    }
    private ObjectProperty<ValueType> valueType;
    public void setValueType(ValueType value)
    {
        if (allowedValueTypes().contains(value.getValue()))
        {
            valueParameterProperty().set(value);
        } else
        {
            throw new IllegalArgumentException("Invalid Value Date Type:" + value.getValue() + ", allowed = " + allowedValueTypes());
        }
    }
    public T withValueType(ValueEnum value) { setValueType(new ValueType(value)); return (T) this; } 
    public T withValueType(String content) { setValueType(new ValueType(content)); return (T) this; } 
    
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
    public boolean isValid()
    {
        if (getValueType() == null)
        {
            return Property.super.isValid();
        } else
        {
            return (Property.super.isValid()) && (allowedValueTypes().contains(getValueType().getValue()));
        }
    }
    
    /** Allowed value types for this property.  These are the values than can be an
     * argument to {@link #setValueType(ValueType)}
     * RFC 5545, 3.2.20, page 29
     */
    protected List<ValueEnum> allowedValueTypes()
    {
        return Arrays.asList(ValueEnum.TEXT); // default is TEXT, override if otherwise
    }
    /**
     * Property's value portion of content line.
     * Default method is for a property that has a properly overridden toString method.
     * If not, then the subclass must override this method.
     * 
     * @return property value as a String formatted for a iCalendar content line
     */
    protected String getValueForContentLine()
    {
        return getValue().toString();
    }
    
//    @Override
//    protected List<ParameterEnum> parmeters2;
    @Override // TODO - MAYBE THIS CAN BE IMPROVED - USE LISTENERS TO POPULATE LIST?
    public List<ParameterEnum> parameters() // CAN I SAVE LIST - UPDATE ONLY WHEN NEW PARAMETER CHANGE OCCURS?
    {
        List<ParameterEnum> populatedParameters = new ArrayList<>();
//        System.out.println("parameters:" + propertyType().possibleParameters().size());
        Iterator<ParameterEnum> i = propertyType().possibleParameters().stream().iterator();
        while (i.hasNext())
        {
            ParameterEnum parameterType = i.next();
            Parameter<?> parameter = parameterType.getParameter(this);
//            System.out.println("pt:" + parameterType + " " + parameter);
            if (parameter != null)
            {
                populatedParameters.add(parameterType);
            }
        }
        return Collections.unmodifiableList(populatedParameters);
    }
    
//    @Override
//    @Deprecated
//    public Map<ParameterEnum, Parameter<?>> parameterMap()
//    {
//        Map<ParameterEnum, Parameter<?>> populatedParameterMap = new LinkedHashMap<>();
//        Iterator<ParameterEnum> i = propertyType().possibleParameters().stream().iterator();
//        while (i.hasNext())
//        {
//            ParameterEnum parameterType = i.next();
//            Parameter<?> parameter = parameterType.getParameter(this);
//            if (parameter != null)
//            {
//                populatedParameterMap.put(parameterType, parameter);
//            }
//        }
//        return Collections.unmodifiableMap(populatedParameterMap);
//    }
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
    @SuppressWarnings("unchecked")
    public PropertyBase(String propertyString)
    {
        this();
        
        // strip off property name if present
        // TODO - MAKE SURE PROPERTY NAME MATCHES PROPERTY
        int endIndex = propertyType.toString().length();
        boolean isLongEnough = propertyString.length() > endIndex;
        boolean hasPropertyName = (isLongEnough) ? propertyString.substring(0, endIndex).equals(propertyType.toString()) : false;
        if (hasPropertyName)
        {
            propertyString = propertyString.substring(endIndex);
        } else
        {
            propertyString = ":" + propertyString; // indicates propertyString is property value without any properties
        }
        // add parameters
        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyString);
//        System.out.println("propertyString:" + propertyString + " " + map.size());
        map.entrySet()
            .stream()
//            .peek(System.out::println)
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
        
        // save property value
        String propertyValueString = map.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
        final U value;
        if (getValue() instanceof List)
        {
            value = (U) Arrays.asList(propertyValueString.split(","))
                    .stream()
                    .map(e -> PropertyEnum.enumFromClass(getClass())
                            .getValueType()
                            .parse(e))
                    .collect(Collectors.toList());
        } else
        {
            value = PropertyEnum.enumFromClass(getClass())
                        .getValueType()
                        .parse(propertyValueString);    
        }        
        setValue(value);
        
        if (! isValid())
        {
            throw new IllegalArgumentException("Error in parsing " + propertyType().toString() + " content line");
        }
    }
//    @Deprecated
//    private String propertyValueString;
//    @Deprecated
//    protected String getPropertyValueString() { return propertyValueString; }
    
    // construct empty property
    public PropertyBase()
    {
        propertyType = PropertyEnum.enumFromClass(getClass());
        value = new SimpleObjectProperty<U>(this, propertyType.toString());
//        valueParameterProperty().addListener((ChangeListener<? super ValueEnum>) (observable, oldValue, newValue) -> 
//        {
//            boolean isOldNull = oldValue == null;
//            boolean isNewNull = newValue == null;            
//            if ((isOldNull && ! isNewNull) || (! isOldNull && isNewNull))
//            {
//                System.out.println("updated parameters");
//                parmeters2 = parameters();
//            }
//        });
    }
    
    // copy constructor
    public PropertyBase(Property<U> source)
    {
        this();
        Iterator<ParameterEnum> i = source.parameters().stream().iterator();
        while (i.hasNext())
        {
            ParameterEnum sourceParameterType = i.next();
            Parameter<?> sourceParameter = sourceParameterType.getParameter(source);
            sourceParameterType.copyTo(sourceParameter, this);
        }
        setValue(source.getValue());
        this.propertyType = source.propertyType();
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
//        System.out.println("parameters:" + parameters().size());
        parameters().stream().forEach(p -> builder.append(p.getParameter(this).toContentLine()));
        otherParameters().stream().forEach(p -> builder.append(";" + p));
        builder.append(":" + getValueForContentLine());
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
//        Iterator<Entry<ParameterEnum, Parameter<?>>> i = parameterMap().entrySet().iterator();
        Iterator<ParameterEnum> i = parameters().iterator();
        while (i.hasNext())
        {
            Parameter<?> parameter = i.next().getParameter(this);
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
//        System.out.println("VALUES:" + getValue() + " " + testObj.getValue());
        boolean otherParametersEquals = otherParameters().equals(testObj.otherParameters());
        
        final boolean parametersEquals;
        if (parameters().size() == testObj.parameters().size())
        {
            Iterator<ParameterEnum> i1 = parameters().iterator();
            Iterator<ParameterEnum> i2 = testObj.parameters().iterator();
            boolean isFailure = false;
            while (i1.hasNext())
            {
                Parameter<?> p1 = i1.next().getParameter(this);
                Parameter<?> p2 = i2.next().getParameter(testObj);
//                System.out.println("p1,p2:" + p1 + " " + p2);
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
//        System.out.println("equals:" + valueEquals + " " + otherParametersEquals + " " + parametersEquals);
        return valueEquals && otherParametersEquals && parametersEquals;
    }

    @Override
    public String toString()
    {
        return super.toString() + "," + toContentLine();
    }
}
