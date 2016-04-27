package jfxtras.labs.icalendarfx.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.StringConverter;
import jfxtras.labs.icalendarfx.parameters.Parameter;
import jfxtras.labs.icalendarfx.parameters.ParameterEnum;
import jfxtras.labs.icalendarfx.parameters.ValueParameter;
import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.Method;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

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
public abstract class PropertyBase<T,U> implements Property<T>
{
    /**
     * PROPERTY VALUE
     * 
     * Example: for the property content LOCATION:The park the property
     * value is the string "The park"
     */
    @Override
    public T getValue() { return value.get(); }
    @Override
    public ObjectProperty<T> valueProperty() { return value; }
    private ObjectProperty<T> value; // initialized in constructor
    @Override
    public void setValue(T value) { this.value.set(value); }
    /** The propery's value converted by string converted to content string */
    protected String valueContent()
    {
        /* default code below works for all properties with a single value.  Properties with multiple embedded values,
         * such as RequestStatus, require an overridden method */
        return (getConverter().toString(getValue()) == null) ? getUnknownValue() : getConverter().toString(getValue());
    }
//    public U withValue(T value) { setValue(value); return (U) this; } // in constructor

    // class of value.  Used to verify value class is allowed for the property type
    private Class<T> valueClass;
    private Class<?> getValueClass()
    {
        if (valueClass == null)
        {
            if (getValue() != null)
            {
                if (getValue() instanceof Collection)
                {
                    return ((Collection<?>) getValue()).iterator().next().getClass();
                }
                return getValue().getClass();
            }
            return null;
        } else
        {
            return valueClass;
        }
    }
    
    /** The name of the property, such as DESCRIPTION
     * Remains the default value unless set by a non-standard property*/
    @Override
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
            if (IANAProperty.REGISTERED_IANA_PROPERTY_NAMES.contains(name))
            {
                propertyName = name;
            } else
            {
                throw new RuntimeException(name + " is not an IANA-registered property name.  Registered names are in IANAProperty.REGISTERED_IANA_PROPERTY_NAMES");
            }
        } else
        {
            throw new RuntimeException("Property names can only be set for non-standard and IANA-registered properties.");
        }
    }
    public U withPropertyName(String name) { setPropertyName(name); return (U) this; }
    
    /**
     * PROPERTY TYPE
     * 
     *  The type of the property from the enum of all properties.
     */
    @Override
    public PropertyEnum propertyType() { return propertyType; }
    final private PropertyEnum propertyType;
    
    /*
     * Unknown values
     * contains exact string for unknown property value
     */
    private String unknownValue;
    protected String getUnknownValue() { return unknownValue; }
    private void setUnknownValue(String value) { unknownValue = value; }
    
    /**
     * VALUE
     * Value Data Types
     * RFC 5545, 3.2.20, page 29
     * 
     * To specify the value for text values in a property or property parameter.
     * This parameter is optional for properties when the default value type is used.
     * 
     * Examples:
     * VALUE=DATE-TIME  (Date-Time is default value, so it isn't necessary to specify)
     * VALUE=DATE
     */
    @Override
    public ValueParameter getValueParameter() { return valueType.get(); }
    @Override
    public ObjectProperty<ValueParameter> valueParameterProperty() { return valueType; }
    private ObjectProperty<ValueParameter> valueType = new SimpleObjectProperty<>(this, ParameterEnum.VALUE_DATA_TYPES.toString());
    @Override
    public void setValueParameter(ValueParameter valueType)
    {
        if (isValueParameterValid(valueType.getValue()))
        {
            valueParameterProperty().set(valueType);
        } else
        {
            throw new IllegalArgumentException("Invalid Value Date Type:" + valueType.getValue() + ", allowed = " + propertyType().allowedValueTypes());
        }
    }
    public void setValueParameter(ValueType value) { setValueParameter(new ValueParameter(value)); }
    public void setValueParameter(String value) { setValueParameter(new ValueParameter(value)); }
    public U withValueParameter(ValueType value) { setValueParameter(value); return (U) this; } 
    public U withValueParameter(String value) { setValueParameter(value); return (U) this; }
    // Synch value with type produced by string converter
    private final ChangeListener<? super ValueParameter> valueParameterChangeListener = (observable, oldValue, newValue) ->
    {
        // replace converter if using default converter
        if (! isCustomConverter())
        {
//                System.out.println("set converter:" + newValue.getValue() + " " + valueClass + " " + getValue());
            setConverter(newValue.getValue().getConverter());

            // Convert property value string, if present
            if (getPropertyValueString() != null)
            {
                T newPropValue = getConverter().fromString(getPropertyValueString());
                setValue(newPropValue);
            }
        }
        
        // verify value class is allowed
        if (getValueClass() != null) // && ! newValue.getValue().allowedClasses().contains(getValueClass()))
        {
            boolean isMatch = newValue.getValue().allowedClasses()
                    .stream()
                    .map(c -> getValueClass().isAssignableFrom(c))
                    .findAny()
                    .isPresent();
            if (! isMatch)
            {
                throw new IllegalArgumentException("Value class " + getValueClass().getSimpleName() +
                        " doesn't match allowed value classes: " + newValue.getValue().allowedClasses());
            }
        }
    };
    
    /**
     * other-param, 3.2 RFC 5545 page 14
     * the parameter name and value are combined into one object
     */
    @Override
    public ObservableList<Object> otherParameters() { return otherParameters; }
    private ObservableList<Object> otherParameters = FXCollections.observableArrayList();
    public U withOtherParameters(Object... parameter) { otherParameters().addAll(parameter); return (U) this; }

    // TODO CAN I CACHE THE LIST? - UPDATE ONLY WHEN NEW PARAMETER CHANGE OCCURS?
    @Override
    public List<ParameterEnum> parameters()
    {
        List<ParameterEnum> populatedParameters = new ArrayList<>();
        Iterator<ParameterEnum> i = propertyType().allowedParameters().stream().iterator();
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
    }
    
    /** 
     * Parameter sort order map.  Key is parameter name.  Follows sort order of parsed content.
     * If a parameter is not present in the map, it is put at the end of the sorted ones in
     * the order appearing in {@link #ParameterEnum}
     * Generally, this map shouldn't be modified.  Only modify it when you want to force
     * a specific parameter order (e.g. unit testing).
     */
    public Map<ParameterEnum, Integer> parameterSortOrder() { return parameterSortOrder; }
    final private Map<ParameterEnum, Integer> parameterSortOrder = new HashMap<>();
    private Integer parameterCounter = 0;
    
    // property value
    private String propertyValueString = null;
    // Note: in subclasses additional text can be concatenated to string (e.g. ZonedDateTime classes add time zone as prefix)
    protected String getPropertyValueString() { return propertyValueString; }
    
    
    /**
     * STRING CONVERTER
     * 
     * Get the property's value string converter.  There is a default converter in ValueType associated
     * with the default value type of the property.  For most value types that converter is
     * acceptable.  However, for the TEXT value type it often needs to be replaced.
     * For example, the value type for TimeZoneIdentifier is TEXT, but the Java object is
     * ZoneId.  A different converter is required to make the conversion to ZoneId.
     */ 
    @Override
    public StringConverter<T> getConverter() { return converter; }
    private StringConverter<T> converter;
    @Override
    public void setConverter(StringConverter<T> converter) { this.converter = converter; }
    private StringConverter<T> defaultConverter;
    private boolean isCustomConverter()
    {
//        System.out.println("custom:" + getConverter() + " " + ValueType.UNIFORM_RESOURCE_IDENTIFIER.getConverter());
        return ! getConverter().equals(defaultConverter);
    }
    
    /*
     * CONSTRUCTORS
     */
    
    protected PropertyBase()
    {
        propertyType = PropertyEnum.enumFromClass(getClass());
        value = new SimpleObjectProperty<T>(this, propertyType.toString());
        ValueType defaultValueType = propertyType.allowedValueTypes().get(0);
        defaultConverter = defaultValueType.getConverter();
        setConverter(defaultConverter);
        valueParameterProperty().addListener(valueParameterChangeListener); // keeps value synched with value type
    }

    /**
     * Parse iCalendar content line constructor
     * 
     * Construct new property by parsing content line using default string converter
     * @see ValueType
     * Sets parameters by running parse for each parameter enum
     * 
     * The input parameter is CharSequence to avoid ambiguous constructors for properties
     * that has the parameterized type T of String
     * 
     * @param contentLine - property text string
     */
    public PropertyBase(CharSequence contentLine)
    {
        this();
        parseContent(contentLine);
    }

    public PropertyBase(Class<T> valueClass, CharSequence contentLine)
    {
        this();
        this.valueClass = valueClass;
        setConverterByClass(valueClass);
        parseContent(contentLine);
    }

    // copy constructor
    public PropertyBase(Property<T> source)
    {
        this();
        setConverter(source.getConverter());
//        isCustomConverter = source.isCustomConverter;
        Iterator<ParameterEnum> i = source.parameters().stream().iterator();
        while (i.hasNext())
        {
            ParameterEnum sourceParameterType = i.next();
            Parameter<?> sourceParameter = sourceParameterType.getParameter(source);
            sourceParameterType.copyTo(sourceParameter, this);
        }
        setValue(source.getValue());
    }
    
    // constructor with only value parameter
    public PropertyBase(T value)
    {
        this();
        setValue(value);
    }

    // Set converter when using constructor with class parameter
    protected void setConverterByClass(Class<T> valueClass)
    {
        // do nothing - override in subclass for functionality
    }
    
    protected void parseContent(CharSequence contentLine)
    {
//        setConverter(converter);
        String propertyString = contentLine.toString();
        
        // test line, make changes if necessary
        final String propertyValue;
        List<Integer> indices = new ArrayList<>();
        indices.add(propertyString.indexOf(':'));
        indices.add(propertyString.indexOf(';'));
        Optional<Integer> hasPropertyName = indices
                .stream()
                .filter(v -> v > 0)
                .min(Comparator.naturalOrder());
        if (hasPropertyName.isPresent())
        {
            int endNameIndex = hasPropertyName.get();
            String propertyName = (endNameIndex > 0) ? propertyString.subSequence(0, endNameIndex).toString().toUpperCase() : null;
            boolean isMatch = propertyName.equals(propertyType.toString());
            boolean isNonStandard = propertyName.substring(0, PropertyEnum.NON_STANDARD.toString().length()).equals(PropertyEnum.NON_STANDARD.toString());
            boolean isIANA = propertyType.equals(PropertyEnum.IANA_PROPERTY);
            if (isMatch || isNonStandard || isIANA)
            {
                if (isNonStandard || isIANA)
                {
                    setPropertyName(propertyString.substring(0,endNameIndex));
                }
                propertyValue = propertyString.substring(endNameIndex, propertyString.length()); // strip off property name
            } else
            {
                if (PropertyEnum.enumFromName(propertyName) == null)
                {
                    propertyValue = ":" + propertyString; // doesn't match a known property name, assume its all a property value
                } else
                {
                    throw new IllegalArgumentException("Property name " + propertyName + " doesn't match class " +
                            getClass().getSimpleName() + ".  Property name associated with class " + 
                            getClass().getSimpleName() + " is " +  propertyType.toString());
                }
            }
        } else
        {
            propertyValue = ":" + propertyString;
        }
        
        // process parameters
        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyValue);
//        System.out.println("propertyString:" + propertyString + " " + map.size());
        map.entrySet()
            .stream()
//            .peek(System.out::println)
            .filter(e -> ! (e.getKey() == ICalendarUtilities.PROPERTY_VALUE_KEY))
            .forEach(e ->
            {
                ParameterEnum p = ParameterEnum.enumFromName(e.getKey());
                parameterSortOrder().put(p, parameterCounter++);
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
//        System.out.println("propertyValueString:" + getPropertyValueString());
        T value = getConverter().fromString(getPropertyValueString());
//        System.out.println("value class:" + value.getClass());
        if (value == null)
        {
            setUnknownValue(propertyValueString);
        } else
        {
            setValue(value);
//            System.out.println("value class2:" + getValue().getClass());
            if (value.toString() == "UNKNOWN") // enum name indicating unknown value
            {
                setUnknownValue(propertyValueString);
            }
        }
        
        if (! isValid())
        {
            throw new IllegalArgumentException("Error in parsing " + propertyType().toString() + " content line");
        }
    }
    
    
    @Override
    public boolean isValid()
    {
        if (getValueParameter() == null)
        {
            return Property.super.isValid();
        } else
        {
            boolean isValueTypeOK = isValueParameterValid(getValueParameter().getValue());
//            System.out.println("PropertyBase isValid:" + Property.super.isValid() + " " + isValueTypeOK);
            return (Property.super.isValid()) && isValueTypeOK;
        }
    }
    /* test if value type is valid */
    private boolean isValueParameterValid(ValueType value)
    {
        boolean isValueTypeOK = propertyType().allowedValueTypes().contains(value);
        boolean isUnknownType = value.equals(ValueType.UNKNOWN);
        boolean isNonStandardProperty = propertyType().equals(PropertyEnum.NON_STANDARD) || propertyType().equals(PropertyEnum.IANA_PROPERTY);
//        System.out.println("parameter valid:" + isValueTypeOK + " " + isUnknownType + " " + isNonStandardProperty);
        return (isValueTypeOK || isUnknownType || isNonStandardProperty);
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
        // property name
        StringBuilder builder = new StringBuilder(50);
        if (propertyName == null)
        {
            builder.append(propertyType().toString());
        } else
        {
            builder.append(propertyName);
        }
        
        // PARAMETERS
        Map<String, CharSequence> parameterNameContentMap = parameters().stream()
                .collect(Collectors.toMap(p -> p.toString(),
                                          p -> p.getParameter(this).toContent()));
        
        // restore parameter sort order if parameters were parsed from content
        parameterNameContentMap.entrySet().stream()
                .sorted((Comparator<? super Entry<String, CharSequence>>) (e1, e2) -> 
                {
                    ParameterEnum p1 = ParameterEnum.enumFromName(e1.getKey());
                    Integer s1 = parameterSortOrder.get(p1);
                    Integer sort1 = (s1 == null) ? Integer.MAX_VALUE : s1;
                    ParameterEnum p2 = ParameterEnum.enumFromName(e2.getKey());
                    Integer s2 = parameterSortOrder.get(p2);
                    Integer sort2 = (s2 == null) ? Integer.MAX_VALUE : s2;
//                    System.out.println("s12:" + sort1 + " " + sort2);
                    return sort1.compareTo(sort2);
                })
                .forEach(p -> 
                {
                    builder.append(p.getValue());
                });
        
//        parameters().stream().forEach(p -> builder.append(p.getParameter(this).toContent()));
        // add non-standard parameters
        otherParameters().stream().forEach(p -> builder.append(";" + p));
        // add property value
        String stringValue = valueContent();
        builder.append(":" + stringValue);
//        builder.append(":" + valueToString(getValue()));
        return builder.toString();
    }

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
