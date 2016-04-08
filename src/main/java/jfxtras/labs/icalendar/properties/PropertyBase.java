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
import javafx.util.StringConverter;
import jfxtras.labs.icalendar.parameters.Parameter;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueParameter;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.calendar.CalendarScale;
import jfxtras.labs.icalendar.properties.calendar.Method;
import jfxtras.labs.icalendar.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendar.properties.calendar.Version;
import jfxtras.labs.icalendar.properties.component.relationship.UniqueIdentifier;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

/**
 * Base iCalendar property class
 * Contains property value, value parameter (ValueType) and other-parameters
 * Also contains several support methods used by other properties
 * 
 * extended interfaces
 * @see PropertyAltText
 * @see PropertyAttendee
 * @see PropertyCalendarUser
 * @see PropertyDateTime
 * @see PropertyFreeBusy
 * @see PropertyLanguage
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
 * @param <U> - type of implementing subclass
 * @param <T> - type of property value
 */
public abstract class PropertyBase<U,T> implements Property<T>
{
    /** The name of the property, such as DESCRIPTION
     * Remains the default value unless set by a non-standard property*/
    public String getPropertyName()
    {
        if (propertyName == null)
        {
            return propertyType().toString();
        }
        return propertyName;
    }
    private String propertyName;
    /** Set the name of the property.  Only allowed for non-standard and IANA properties */
    public void setPropertyName(String name)
    {
        if (propertyType().equals(PropertyEnum.NON_STANDARD))
        {
            if (name.substring(0, 2).toUpperCase().equals("X-"))
            {
                propertyName = name;
            } else
            {
                throw new RuntimeException("Non-standard properties must begin with X-");                
            }
        } else if (propertyType().equals(PropertyEnum.IANA_PROPERTY))
        {
            propertyName = name;            
        } else
        {
            throw new RuntimeException("Only non-standard properties can be renamed.  This property must be named " + propertyType().toString());
        }
    }
    public U withPropertyName(String name) { setPropertyName(name); return (U) this; }
    
    /** The type of the property from the enum of all properties. */
    @Override
    public PropertyEnum propertyType() { return propertyType; }
    private PropertyEnum propertyType;

    /**
     * The property's value
     */
    @Override
    public T getValue() { return value.get(); }
    @Override
    public ObjectProperty<T> valueProperty() { return value; }
    private ObjectProperty<T> value;
    @Override
    public void setValue(T value) { this.value.set(value); }
    public U withValue(T value) { setValue(value); return (U) this; }
    
    /*
     * Unknown values
     * contains exact string for unknown property value
     */
    private String unknownValue;
    protected String getUnknownValue() { return unknownValue; }
    private void setUnknownValue(String value) { unknownValue = value; }
    
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
    public void setValueParameter(ValueParameter valueType)
    {
        if (isValueParameterValid(valueType))
        {
            valueParameterProperty().set(valueType);
            if (getConverter() == null)
            {
                setConverter(valueType.getValue().stringConverter());
            }
            // If value previously set as string (as done with non-standard properties) convert to type T
            if ((getValue() != null) && (getValue() instanceof String))
            {
                T newPropValue= getConverter().fromString(getPropertyValueString());
                setValue(newPropValue);
            }
        } else
        {
            throw new IllegalArgumentException("Invalid Value Date Type:" + valueType.getValue() + ", allowed = " + propertyType().defaultValueType());
        }
    }
    public void setValueParameter(ValueType value) { setValueParameter(new ValueParameter(value)); }
    public void setValueParameter(String value) { setValueParameter(new ValueParameter(value)); }
    public U withValueParameter(ValueType value) { setValueParameter(value); return (U) this; } 
    public U withValueParameter(String value) { setValueParameter(value); return (U) this; } 
    
    /**
     * other-param, 3.2 RFC 5545 page 14
     * the parameter name and value are combined into one object
     */
    @Override
    public ObservableList<Object> otherParameters() { return otherParameters; }
    private ObservableList<Object> otherParameters = FXCollections.observableArrayList();
    public U withOtherParameters(Object... parameter) { otherParameters().addAll(parameter); return (U) this; }
        
    @Override
    public boolean isValid()
    {
        if (getValueParameter() == null)
        {
            return Property.super.isValid();
        } else
        {
            boolean isValueTypeOK = (getValueParameter() == null) ? true: isValueParameterValid(getValueParameter());
            return (Property.super.isValid()) && isValueTypeOK;
        }
    }
    /* test if value type is valid */
    private boolean isValueParameterValid(ValueParameter value)
    {
        boolean isMatchingType = value.getValue().equals(propertyType().defaultValueType());
        boolean isUnknownType = value.getValue().equals(ValueType.UNKNOWN);
        boolean isNonStandardProperty = propertyType().equals(PropertyEnum.NON_STANDARD) || propertyType().equals(PropertyEnum.IANA_PROPERTY);
        return (isMatchingType || isUnknownType || isNonStandardProperty);
    }

    // TODO CAN I CACHE THE LIST? - UPDATE ONLY WHEN NEW PARAMETER CHANGE OCCURS?
    @Override
    public List<ParameterEnum> parameters()
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
    
    // property value
    private String propertyValueString;
    // Note: in subclasses additional text can be concatenated to string (e.g. ZonedDateTime classes)
    protected String getPropertyValueString() { return propertyValueString; }
    
    public StringConverter<T> getConverter() { return converter; }
    private StringConverter<T> converter;
//    private StringConverter<T> converter = new StringConverter<T>()
//    {
//        @Override
//        public String toString(T object)
//        {
//            return object.toString();
//        }
//
//        @Override
//        public T fromString(String string)
//        {
//             return (T) string;            
//        }
//    };
    public void setConverter(StringConverter<T> converter) { this.converter = converter; }
    
    /*
     * CONSTRUCTORS
     */
    
    // construct empty property
    private PropertyBase(StringConverter<T> converter)
    {
        propertyType = PropertyEnum.enumFromClass(getClass());
        setConverter(converter);
        value = new SimpleObjectProperty<T>(this, propertyType.toString());
    }
    
    /**
     * Parse iCalendar content line constructor
     * 
     * construct new property by parsing content line
     * sets parameters by running parse for each parameter enum
     * 
     * parameter is CharSequence to avoid ambiguous constructors for parameters that have
     * the value of String
     * 
     * @param propertyString
     */
    public PropertyBase(CharSequence contentLine, StringConverter<T> converter)
    {
        this(converter);
        String propertyString = contentLine.toString();
        
        final String propertyValue;
        int colonIndex = propertyString.indexOf(':');
        int semiColonIndex = propertyString.indexOf(';');
        int endNameIndex = (semiColonIndex > 0) ? semiColonIndex : colonIndex;
        String propertyName = (endNameIndex > 0) ? propertyString.subSequence(0, endNameIndex).toString().toUpperCase() : null;
        if (propertyName == null)
        {
            propertyValue = ":" + propertyString; // indicates propertySequence doesn't contain a property name - only parameters and value
        } else
        {
            boolean isNonStandard = propertyName.substring(0, 2).equals(PropertyEnum.NON_STANDARD.toString());
            boolean isMatch = propertyName.equals(propertyType.toString());
            if (isMatch || isNonStandard)
            {
                if (isNonStandard)
                {
                    setPropertyName(propertyString.substring(0,endNameIndex));
                }
                propertyValue = propertyString.substring(endNameIndex, propertyString.length()); // strip off property name
            } else
            {
                throw new IllegalArgumentException("Property name " + propertyName + " doesn't match class " +
                        getClass().getSimpleName() + ".  Property name associated with class " + 
                        getClass().getSimpleName() + " is " +  propertyType.toString());
            }
        }
        
        // add parameters
        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyValue);
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
        T value = getConverter().fromString(getPropertyValueString());
        if (value == null)
        {
            setUnknownValue(propertyValueString);
        } else
        {
            setValue(value);            
        }
//        ValueType valueType = (getValueParameter() == null) ? propertyType().defaultValueType() : getValueParameter().getValue();
//        T value = (T) valueType.stringConverter().fromString(getPropertyValueString());
//        setValue(valueFromString(getPropertyValueString()));
        
        if (! isValid())
        {
            throw new IllegalArgumentException("Error in parsing " + propertyType().toString() + " content line");
        }
    }
    
    // copy constructor
    public PropertyBase(Property<T> source, StringConverter<T> converter)
    {
        this(converter);
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
    public PropertyBase(T value, StringConverter<T> converter)
    {
        this(converter);
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
        if (propertyName == null)
        {
            builder.append(propertyType().toString());
        } else
        {
            builder.append(propertyName);
        }
        // add parameters
        parameters().stream().forEach(p -> builder.append(p.getParameter(this).toContent()));
        // add non-standard parameters
        otherParameters().stream().forEach(p -> builder.append(";" + p));
        // add property value
//        System.out.println("value:" + valueToString(getValue()));
//        builder.append(":" + propertyType().defaultValueType().makeContent(getValue()));
        ValueType valueType = (getValueParameter() == null) ? propertyType().defaultValueType() : getValueParameter().getValue();
        String stringValue = (getValue() == null) ? getUnknownValue() : valueType.stringConverter().toString(getValue());
        builder.append(":" + stringValue);
//        builder.append(":" + valueToString(getValue()));
        return builder.toString();
    }
    
//    /*
//     * DEFAULT STRING CONVERTERS
//     * override in subclasses if necessary
//     */
//    
//    public PropValueStringConverter<T> getConverter() { return converter; }
//    private PropValueStringConverter<T> converter = new PropValueStringConverter<T>(this)
//    {
//        @Override
//        public String toString(T object)
//        {
//            return object.toString();
//        }
//
//        @Override
//        public T fromString(String string)
//        {
//            Type[] types = ((ParameterizedType)getProperty().getClass().getGenericSuperclass())
//                    .getActualTypeArguments();
//            System.out.println("class2:" + getProperty().getClass());
//             Class<T> myClass = (Class<T>) types[types.length-1]; // get last parameterized type
//             if (myClass.equals(String.class))
//             {
//                 return (T) propertyValueString;            
//             }
//             throw new RuntimeException("can't convert property value to type: " + myClass.getSimpleName() +
//                     ". You need to override string converter for class " + getProperty().getClass().getSimpleName());
//        }
//    };
//    public void setConverter(PropValueStringConverter<T> converter) { this.converter = converter; }
//    
//    
//    /* Convert property value to string.  Override in subclass if necessary */
//    @Deprecated
//    protected String valueToString(T value)
//    {
//        return converter.toString(value);
////        return value.toString();
//    }    
//    /* parse property value, override in subclasses if necessary */
////    @SuppressWarnings("unchecked")
//    @Deprecated
//    protected T valueFromString(String propertyValueString)
//    {
//        System.out.println("class1:" + getClass());
//        return converter.fromString(propertyValueString);
////        Type[] types = ((ParameterizedType)getClass().getGenericSuperclass())
////                   .getActualTypeArguments();
////        Class<T> myClass = (Class<T>) types[types.length-1]; // get last parameterized type
////        if (myClass.equals(String.class))
////        {
////            return (T) propertyValueString;            
////        }
////        throw new RuntimeException("can't convert property value to type: " + myClass.getSimpleName() +
////                ". You need to override valueFromString in subclass " + getClass().getSimpleName());
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
        boolean valueEquals = (getValue() == null) ? (testObj.getValue() == null) : getValue().equals(testObj.getValue());
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
