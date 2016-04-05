package jfxtras.labs.icalendar.properties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.parameters.Parameter;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueParameter;
import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.properties.calendar.CalendarScale;
import jfxtras.labs.icalendar.properties.calendar.Method;
import jfxtras.labs.icalendar.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendar.properties.calendar.Version;
import jfxtras.labs.icalendar.properties.component.relationship.UniqueIdentifier;
import jfxtras.labs.icalendar.properties.component.time.DateAbstract;
import jfxtras.labs.icalendar.properties.component.time.DateTimeAbstract;
import jfxtras.labs.icalendar.properties.component.time.TimeZoneAbstract;
import jfxtras.labs.icalendar.properties.component.time.UTCTimeAbstract;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

/**
 * Base iCalendar property class
 * Contains property value, value parameter (ValueType) and other-parameters
 * Also contains several support methods used by other properties
 * 
 * abstract subclasses
 * @see PropertyAlternateTextRepresentation
 * @see PropertyCalendarUserAddress
 * @see PropertyLanguage
 * @see TimeZoneAbstract
 * @see UTCTimeAbstract
 * @see DateAbstract
 * @see DateTimeAbstract
 * 
 * concrete subclasses
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
    @Override
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
    @Override
    public ValueParameter getValueParameter() { return (valueType == null) ? null : valueType.get(); }
    @Override
    public ObjectProperty<ValueParameter> valueParameterProperty()
    {
        if (valueType == null)
        {
            valueType = new SimpleObjectProperty<>(this, ParameterEnum.VALUE_DATA_TYPES.toString());
        }
        return valueType;
    }
    private ObjectProperty<ValueParameter> valueType;
    @Override
    public void setValueParameter(ValueParameter value)
    {
        if (value.getValue().equals(propertyType().defaultValueType()) || value.getValue().equals(ValueType.UNKNOWN))
        {
            valueParameterProperty().set(value);
        } else
        {
            throw new IllegalArgumentException("Invalid Value Date Type:" + value.getValue() + ", allowed = " + propertyType().defaultValueType());
        }
    }
    public void setValueParameter(ValueType value) { setValueParameter(new ValueParameter(value)); }
    public void setValueParameter(String value) { setValueParameter(new ValueParameter(value)); }
    public T withValueParameter(ValueType value) { setValueParameter(value); return (T) this; } 
    public T withValueParameter(String value) { setValueParameter(value); return (T) this; } 
    
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
        if (getValueParameter() == null)
        {
            return Property.super.isValid();
        } else
        {
            boolean isValueTypeOK = getValueParameter().getValue().equals(propertyType().defaultValueType()) || getValueParameter().getValue().equals(ValueType.UNKNOWN);
            return (Property.super.isValid()) && isValueTypeOK;
        }
    }
    
//    /** Allowed value types for this property.  These are the values than can be an
//     * argument to {@link #setValueType(ValueParameter)}
//     * RFC 5545, 3.2.20, page 29
//     */
//    protected List<ValueType> allowedValueTypes()
//    {
//        PropertyEnum.enumFromClass(myClass)
//        return Arrays.asList(ValueType.TEXT); // default is TEXT, override if otherwise
//    }
//    /**
//     * Property's value portion of content line.
//     * Default method is for a property that has a properly overridden toString method.
//     * If not, then the subclass must override this method.
//     * 
//     * @return property value as a String formatted for a iCalendar content line
//     */
//    protected String getValueForContentLine()
//    {
//        return propertyType().valueType().makeContent(getValue());
////        return getValue().toString();
//    }
    
//    @Override
//    protected List<ParameterEnum> parmeters2;
    @Override // TODO - MAYBE THIS CAN BE IMPROVED - USE LISTENERS TO POPULATE LIST?
    public List<ParameterEnum> parameters() // CAN I SAVE LIST - UPDATE ONLY WHEN NEW PARAMETER CHANGE OCCURS?
    {
        List<ParameterEnum> populatedParameters = new ArrayList<>();
//        System.out.println("parameters:" + propertyType().possibleParameters().size());
        Iterator<ParameterEnum> i = propertyType().allowedParameters().stream().iterator();
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
    
    /*
     * CONSTRUCTORS
     */
    
    private String propertyValueString;
    protected String getPropertyValueString() { return propertyValueString; } // in subclasses additional text can be concatenated to string (e.g. ZonedDateTime classes)
    
    /**
     * Parse iCalendar content line constructor
     * 
     * construct new property by parsing content line
     * sets parameters by running parse for each parameter enum
     * 
     * @param propertyString
     */
    public PropertyBase(CharSequence propertyString)
    {
        this();
        
        // strip off property name if present
        // TODO - MAKE SURE PROPERTY NAME MATCHES PROPERTY
        int endIndex = propertyType.toString().length();
        boolean isLongEnough = propertyString.length() > endIndex;
        final boolean hasPropertyName;
        if (isLongEnough)
        {
            String front = propertyString.subSequence(0, endIndex).toString().toUpperCase();
            hasPropertyName = front.equals(propertyType.toString());
        } else
        {
            hasPropertyName = false;
        }
        if (hasPropertyName)
        {
            propertyString = propertyString.subSequence(endIndex, propertyString.length());
        } else
        {
            propertyString = ":" + propertyString; // indicates propertyString is property value without any properties
        }
        // add parameters
        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyString.toString());
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
                    if (propertyType().allowedParameters().contains(p))
                    {
                        p.parse(this, e.getValue());
                    } else
                    {
                        throw new IllegalArgumentException("Parameter " + p + " not allowed for property " + propertyType());
                    }
                } else if ((e.getKey() != null) && (e.getValue() != null))
                { // unknown parameter - store as String in other parameter
                    otherParameters().add(e.getKey() + "=" + e.getValue());
                } // if parameter doesn't contain both a key and a value it is ignored
            });

        // save property value
        propertyValueString = map.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
        setValue(valueFromString(getPropertyValueString()));
        
        if (! isValid())
        {
            throw new IllegalArgumentException("Error in parsing " + propertyType().toString() + " content line");
        }
    }
    
    /* parse property value, override in subclasses if necessary */
    protected U valueFromString(String propertyValueString)
    {
        return (U) propertyValueString;
    }
    
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
    
    // constructor with only value parameter
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
        StringBuilder builder = new StringBuilder(50);
        builder.append(propertyType().toString());
        // add parameters
        parameters().stream().forEach(p -> builder.append(p.getParameter(this).toContent()));
        // add non-standard parameters
        otherParameters().stream().forEach(p -> builder.append(";" + p));
        // add property value
//        builder.append(":" + propertyType().defaultValueType().makeContent(getValue()));
        builder.append(":" + valueToString(getValue()));
        return builder.toString();
    }
    
    /* Convert property value to string.  Override in subclass if necessary */
    protected String valueToString(U value)
    {
        return value.toString();
    }
    
//    protected StringBuilder contentLinePart1()
//    {
//        StringBuilder builder = new StringBuilder(propertyType().toString());
////      System.out.println("parameters:" + parameters().size());
//        // add parameters
//        parameters().stream().forEach(p -> builder.append(p.getParameter(this).toContent()));
//        // add non-standard parameters
//        otherParameters().stream().forEach(p -> builder.append(";" + p));
//        return builder;
//    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = (31 * hash) + getValue().hashCode();
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
        List<ParameterEnum> parameters = parameters(); // make parameters local to avoid creating list multiple times
        List<ParameterEnum> testParameters = testObj.parameters(); // make parameters local to avoid creating list multiple times
        if (parameters.size() == testParameters.size())
        {
            Iterator<ParameterEnum> i1 = parameters.iterator();
            Iterator<ParameterEnum> i2 = testParameters.iterator();
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
