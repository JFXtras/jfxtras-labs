package jfxtras.labs.icalendarfx.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
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
import javafx.util.Callback;
import javafx.util.StringConverter;
import jfxtras.labs.icalendarfx.Orderer;
import jfxtras.labs.icalendarfx.OrdererBase;
import jfxtras.labs.icalendarfx.VCalendarElement;
import jfxtras.labs.icalendarfx.parameters.OtherParameter;
import jfxtras.labs.icalendarfx.parameters.Parameter;
import jfxtras.labs.icalendarfx.parameters.ParameterType;
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
public abstract class PropertyBase<T,U> implements Property<T>, Comparable<Property<T>>
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
    public U withValue(T value) { setValue(value); return (U) this; } // in constructor

    /** The propery's value converted by string converted to content string */
    protected String valueContent()
    {
        /* default code below works for all properties with a single value.  Properties with multiple embedded values,
         * such as RequestStatus, require an overridden method */
        return (getConverter().toString(getValue()) == null) ? getUnknownValue() : getConverter().toString(getValue());
    }

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
        if (propertyType().equals(PropertyType.NON_STANDARD))
        {
            if (name.substring(0, 2).toUpperCase().equals("X-"))
            {
                propertyName = name;
            } else
            {
                throw new RuntimeException("Non-standard properties must begin with X-");                
            }
        } else if (propertyType().equals(PropertyType.IANA_PROPERTY))
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
     *  The enumerated type of the property.
     */
    @Override
    public PropertyType propertyType() { return propertyType; }
    final private PropertyType propertyType;
    
    /*
     * Unknown values
     * contains exact string for unknown property value
     */
    private String unknownValue;
    protected String getUnknownValue() { return unknownValue; }
    private void setUnknownValue(String value) { unknownValue = value; }
    
    /**
     * VALUE TYPE
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
    public ValueParameter getValueType() { return valueType.get(); }
    @Override
    public ObjectProperty<ValueParameter> valueTypeProperty() { return valueType; }
    private ObjectProperty<ValueParameter> valueType = new SimpleObjectProperty<>(this, ParameterType.VALUE_DATA_TYPES.toString());
    @Override
    public void setValueType(ValueParameter valueType)
    {
        if (isValueTypeValid(valueType.getValue()))
        {
            valueTypeProperty().set(valueType);
        } else
        {
            throw new IllegalArgumentException("Invalid Value Date Type:" + valueType.getValue() + ", allowed = " + propertyType().allowedValueTypes());
        }
    }
    public void setValueType(ValueType value) { setValueType(new ValueParameter(value)); }
    public void setValueType(String value) { setValueType(ValueParameter.parse(value)); }
    public U withValueType(ValueType value) { setValueType(value); return (U) this; } 
    public U withValueType(String value) { setValueType(value); return (U) this; }
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
     * OTHER PARAMETER
     * other-param, 3.2 RFC 5545 page 14
     * Has custom name and String value
     */
    @Override
    public ObservableList<OtherParameter> getOtherParameters() { return otherParameters; }
    private ObservableList<OtherParameter> otherParameters;
    @Override
    public void setOtherParameters(ObservableList<OtherParameter> otherParameters)
    {
        if (otherParameters != null)
        {
            orderer().registerSortOrderProperty(otherParameters);
        } else
        {
            orderer().unregisterSortOrderProperty(this.otherParameters);
        }
        this.otherParameters = otherParameters;
    }
    public U withOtherParameters(ObservableList<OtherParameter> otherParameters) { setOtherParameters(otherParameters); return (U) this; }
    public U withOtherParameters(String...otherParameters)
    {
        final ObservableList<OtherParameter> list;
        if (getOtherParameters() == null)
        {
            list = FXCollections.observableArrayList();
            setOtherParameters(list);
        } else
        {
            list = getOtherParameters();
        }
        Arrays.asList(otherParameters).forEach(p -> list.add(new OtherParameter(p)));
        return (U) this;
    }
    public U withOtherParameters(OtherParameter...otherParameters)
    {
        if (getOtherParameters() == null)
        {
            setOtherParameters(FXCollections.observableArrayList(otherParameters));
        } else
        {
            getOtherParameters().addAll(otherParameters);
        }
        return (U) this;
    }

    
    /**
     * other-param, 3.2 RFC 5545 page 14
     * the parameter name and value are combined into one object
     */
//    @Override
//    public ObservableList<Object> otherParameters() { return otherParameters; }
//    private ObservableList<Object> otherParameters = FXCollections.observableArrayList();
//    public U withOtherParameters(Object... parameter) { otherParameters().addAll(parameter); return (U) this; }

    @Override
    public List<ParameterType> parameterEnums()
    {
        List<ParameterType> populatedParameters = new ArrayList<>();
        Iterator<ParameterType> i = propertyType().allowedParameters().stream().iterator();
        while (i.hasNext())
        {
            ParameterType parameterType = i.next();
            Object parameter = parameterType.getParameter(this);
            if (parameter != null)
            {
                populatedParameters.add(parameterType);
            }
        }
        return Collections.unmodifiableList(populatedParameters);
    }
    
    /*
     * SORT ORDER FOR CHILD ELEMENTS
     */
    final private Orderer orderer;
    @Override
    public Orderer orderer() { return orderer; }

    private Callback<VCalendarElement, Void> copyParameterChildCallback = (child) ->
    {
        ParameterType type = ParameterType.enumFromClass(child.getClass());
        type.copyParameter((Parameter<?>) child, this);
        return null;
    };   
    
//    /** 
//     * SORT ORDER
//     * 
//     * Parameter sort order map.  Key is parameter, value is order.  Follows sort order of parsed content.
//     * If a parameter is not present in the map, it is put at the end of the sorted ones in
//     * the order appearing in {@link #ParameterEnum} (should be alphabetical)
//     * Generally, this map shouldn't be modified.  Only modify it when you want to force
//     * a specific parameter order (e.g. unit testing).
//     */
    @Deprecated
    public Map<ParameterType, Integer> parameterSortOrder() { return parameterSortOrder; }
    final private Map<ParameterType, Integer> parameterSortOrder = new HashMap<>();
    private Integer parameterCounter = 0;
    
    // property value
    private String propertyValueString = null;
    // Note: in subclasses additional text can be concatenated to string (e.g. ZonedDateTime classes add time zone as prefix)
    @Deprecated
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
        orderer = new OrdererBase(copyParameterChildCallback);
        orderer.registerSortOrderProperty(valueTypeProperty());
        propertyType = PropertyType.enumFromClass(getClass());
        value = new SimpleObjectProperty<T>(this, propertyType.toString());
        ValueType defaultValueType = propertyType.allowedValueTypes().get(0);
        defaultConverter = defaultValueType.getConverter();
        setConverter(defaultConverter);
        valueTypeProperty().addListener(valueParameterChangeListener); // keeps value synched with value type
    }

//    /**
//     * Parse iCalendar content line constructor
//     * 
//     * Construct new property by parsing content line using default string converter
//     * @see ValueType
//     * Sets parameters by running parse for each parameter enum
//     * 
//     * The input parameter is CharSequence to avoid ambiguous constructors for properties
//     * that has the parameterized type T of String
//     * 
//     * @param contentLine - property text string
//     */
//    @Deprecated // use parse method instead
//    public PropertyBase(String contentLine)
//    {
//        this();
//        parseContent(contentLine);
//    }

    public PropertyBase(Class<T> valueClass, String contentLine)
    {
        this();
        this.valueClass = valueClass;
        setConverterByClass(valueClass);
        parseContent(contentLine);
    }

    // copy constructor
    public PropertyBase(PropertyBase<T,U> source)
    {
        this();
        copyPropertyFrom(source);
    }
    
    // constructor with only value parameter
    public PropertyBase(T value)
    {
        this();
        setValue(value);
    }
    
    /** Copy parameters and value from source into this property,
     *  essentially making a copy of source
     *  
     *  Note: This method only works if the property value is immutable.  If it is not 
     *  immutable this method must be overridden to provide a deep copy of the value (e.g. RRULE) */    
    public void copyPropertyFrom(PropertyBase<T,U> source)
    {
        setConverter(source.getConverter());
        copyChildrenFrom(source);
//        parameterSortOrder().putAll(source.parameterSortOrder());
//        source.parameterEnums().forEach(p -> p.copyParameter(source, this));
        if (source.propertyType().equals(PropertyType.NON_STANDARD) || source.propertyType().equals(PropertyType.IANA_PROPERTY))
        {
            setPropertyName(source.getPropertyName());
        }
        T valueCopy = copyValue(source.getValue());
        setValue(valueCopy);
    }

    // return a copy of the value
    protected T copyValue(T source)
    {
        return source; // for mutable values override in subclasses
    }
    
    // Set converter when using constructor with class parameter
    protected void setConverterByClass(Class<T> valueClass)
    {
        // do nothing - override in subclass for functionality
    }
    
    /** Parse content line into calendar property */
    @Override
    public void parseContent(String contentLine)
    {
//        String contentLine = contentLine.toString();
        
        // perform tests, make changes if necessary
        final String propertyValue;
        List<Integer> indices = new ArrayList<>();
        indices.add(contentLine.indexOf(':'));
        indices.add(contentLine.indexOf(';'));
        Optional<Integer> hasPropertyName = indices
                .stream()
                .filter(v -> v > 0)
                .min(Comparator.naturalOrder());
        if (hasPropertyName.isPresent())
        {
            int endNameIndex = hasPropertyName.get();
            String propertyName = (endNameIndex > 0) ? contentLine.subSequence(0, endNameIndex).toString().toUpperCase() : null;
            boolean isMatch = propertyName.equals(propertyType.toString());
            boolean isNonStandard = propertyName.substring(0, PropertyType.NON_STANDARD.toString().length()).equals(PropertyType.NON_STANDARD.toString());
            boolean isIANA = propertyType.equals(PropertyType.IANA_PROPERTY);
            if (isMatch || isNonStandard || isIANA)
            {
                if (isNonStandard || isIANA)
                {
                    setPropertyName(contentLine.substring(0,endNameIndex));
                }
                propertyValue = contentLine.substring(endNameIndex, contentLine.length()); // strip off property name
            } else
            {
                if (PropertyType.enumFromName(propertyName) == null)
                {
                    propertyValue = ":" + contentLine; // doesn't match a known property name, assume its all a property value
                } else
                {
                    throw new IllegalArgumentException("Property name " + propertyName + " doesn't match class " +
                            getClass().getSimpleName() + ".  Property name associated with class " + 
                            getClass().getSimpleName() + " is " +  propertyType.toString());
                }
            }
        } else
        {

            propertyValue = ":" + contentLine;
        }
        
        // parse parameters
        Map<String, String> map = ICalendarUtilities.propertyLineToParameterMap(propertyValue);
//        System.out.println("contentLine:" + contentLine + " " + map.size());
        map.entrySet()
            .stream()
//            .peek(System.out::println)
            .filter(entry -> ! (entry.getKey() == ICalendarUtilities.PROPERTY_VALUE_KEY))
            .forEach(entry ->
            {
                ParameterType parameterType = ParameterType.enumFromName(entry.getKey());
//                parameterSortOrder().put(p, parameterCounter++);
                if (parameterType != null)
                {
                    if (propertyType().allowedParameters().contains(parameterType))
                    {
//                        System.out.println("parse parameters" + parameterType);
                        parameterType.parse(this, entry.getValue());
//                        parameterSortOrder().put(parameterType, parameterCounter);
//                        parameterCounter += 100; // add 100 to allow insertions in between
                    } else
                    {
                        throw new IllegalArgumentException("Parameter " + parameterType + " not allowed for property " + propertyType());
                    }
                } else if ((entry.getKey() != null) && (entry.getValue() != null))
                { // unknown parameter - store as String in other parameter
//                    OtherParameter other = new OtherParameter(entry.getKey()).withValue(entry.getValue());    
//                    System.out.println("found Other:" + entry);
                    ParameterType.OTHER.parse(this, entry.getKey() + "=" + entry.getValue());
//                    otherParameters().add(entry.getKey() + "=" + entry.getValue());
                } // if parameter doesn't contain both a key and a value it is ignored
            });

        // save property value        
        propertyValueString = map.get(ICalendarUtilities.PROPERTY_VALUE_KEY);
//        System.out.println("propertyValueString:" + getPropertyValueString());
        T value = getConverter().fromString(getPropertyValueString());
//        System.out.println("value class:" + value.getClass() + " " + isCustomConverter());
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
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
        if (getValue() == null)
        {
            errors.add(getPropertyName() + " value is null.  The property MUST have a value."); 
        }
        final ValueType valueType;
        if (getValueType() != null)
        {
            valueType = getValueType().getValue();
            boolean isValueTypeOK = isValueTypeValid(valueType);
            if (! isValueTypeOK)
            {
                errors.add(getPropertyName() + " value type " + getValueType().getValue() + " is not supported.  Supported types include:" +
                        propertyType().allowedValueTypes().stream().map(v -> v.toString()).collect(Collectors.joining(",")));
            }
        } else
        {
            // use default valueType
            valueType = propertyType().allowedValueTypes().get(0);
        }
        List<String> createErrorList = valueType.createErrorList(getValue());
        if (createErrorList != null)
        {
            errors.addAll(createErrorList);
        }
        orderer().elementSortOrderMap().forEach((key, value) -> errors.addAll(key.errors()));
//        parameters().forEach(b -> errors.addAll(b.errors()));
        return errors;
    }
    
    /* test if value type is valid */
    private boolean isValueTypeValid(ValueType value)
    {
        boolean isValueTypeOK = propertyType().allowedValueTypes().contains(value);
        boolean isUnknownType = value.equals(ValueType.UNKNOWN);
        boolean isNonStandardProperty = propertyType().equals(PropertyType.NON_STANDARD) || propertyType().equals(PropertyType.IANA_PROPERTY);
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
//    @Override
    public String toContentOld()
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
        Map<Parameter<?>, CharSequence> parameterNameContentMap = new LinkedHashMap<>();
        parameters().forEach(p -> parameterNameContentMap.put((Parameter<?>) p, ((VCalendarElement) p).toContent()));
        
        // restore parameter sort order if parameters were parsed from content
        parameterNameContentMap.entrySet().stream()
                .sorted((Comparator<? super Entry<Parameter<?>, CharSequence>>) (e1, e2) -> 
                {
                    Integer s1 = parameterSortOrder().get(e1.getKey().parameterType());
                    Integer sort1 = (s1 == null) ? Integer.MAX_VALUE : s1;
                    Integer s2 = parameterSortOrder().get(e2.getKey().parameterType());
                    Integer sort2 = (s2 == null) ? Integer.MAX_VALUE : s2;
                    return sort1.compareTo(sort2);
                })
                .peek(System.out::println)
                .forEach(p -> 
                {
                    builder.append(p.getValue());
                });
        
        // add non-standard parameters - sort order doesn't apply to non-standard parameters
//        otherParameters().stream().forEach(p -> builder.append(";" + p));
        // add property value
        String stringValue = valueContent();
        builder.append(":" + stringValue);
        // return folded line
        return ICalendarUtilities.foldLine(builder).toString();
    }
    
    @Override
    public String toContent()
    {
        checkContentList(); // test elements for completeness (can be removed for improved speed)
        StringBuilder builder = new StringBuilder(50);
        if (propertyName == null)
        {
            builder.append(propertyType().toString());
        } else
        {
            builder.append(propertyName);
        }
//        sortedContent().stream().forEach(System.out::println);
        String content = orderer().sortedContent().stream()
//                .map(s -> ICalendarUtilities.foldLine(s))
                .collect(Collectors.joining(";"));
        if (! content.isEmpty())
        {
            builder.append(";" + content);
        }
        builder.append(":" + valueContent());
        // return folded line
        return ICalendarUtilities.foldLine(builder).toString();
    }
    
    // Ensures all elements in elementSortOrderMap are found in parameterEnums list
    private void checkContentList()
    {
        List<String> elementNames1 = parameterEnums().stream().map(e -> e.toString()).collect(Collectors.toList());
//        System.out.println(elementNames1);
        List<String> elementNames2 = orderer().elementSortOrderMap().entrySet()
                .stream()
                .map(e -> ParameterType.enumFromClass(e.getKey().getClass()).toString())
                .collect(Collectors.toList());
//        System.out.println(elementNames2);
        Optional<String> propertyNotFound1 = elementNames1.stream().filter(s -> ! elementNames2.contains(s)).findAny();
        if (propertyNotFound1.isPresent())
        {
            throw new RuntimeException("element not found:" + propertyNotFound1.get());
        }
        Optional<String> propertyNotFound2 = elementNames2.stream().filter(s -> ! elementNames1.contains(s)).findAny();
        if (propertyNotFound2.isPresent())
        {
            throw new RuntimeException("element not found:" + propertyNotFound2.get());
        }
    }

//    @Override
//    public int hashCode()
//    {
//        int hash = 7;
//        hash = (31 * hash) + getValue().hashCode();
//        Iterator<ParameterType> i = parameterEnums().iterator();
//        while (i.hasNext())
//        {
//            Parameter<?> parameter = i.next().getParameter(this);
//            hash = (31 * hash) + parameter.getValue().hashCode();
//        }
//        return hash;
//    }
//
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        PropertyBase<?,?> testObj = (PropertyBase<?,?>) obj;
//        boolean valueEquals = isValueEqual(getValue(), testObj.getValue());
        boolean valueEquals = (getValue() == null) ? (testObj.getValue() == null) : getValue().equals(testObj.getValue());
//        System.out.println("VALUES:" + getValue() + " " + testObj.getValue() + " " + valueEquals);
//        boolean otherParametersEquals = otherParameters().equals(testObj.otherParameters());
        boolean nameEquals = getPropertyName().equals(testObj.getPropertyName());
        final boolean parametersEquals;
        // TODO - FIX EQUALS TO USE MAP
        List<ParameterType> parameters = parameterEnums(); // make parameters local to avoid creating list multiple times
        List<ParameterType> testParameters = testObj.parameterEnums(); // make parameters local to avoid creating list multiple times
        if (parameters.size() == testParameters.size())
        {
            Iterator<ParameterType> i1 = parameters.iterator();
            Iterator<ParameterType> i2 = testParameters.iterator();
            boolean isFailure = false;
            while (i1.hasNext())
            {
                Object p1 = i1.next().getParameter(this);
                Object p2 = i2.next().getParameter(testObj);
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
//        System.out.println("equals:" + valueEquals + " " + otherParametersEquals + " " + parametersEquals + " " + nameEquals);
        return valueEquals && parametersEquals && nameEquals;
    }
    

    @Override
    public String toString()
    {
        return super.toString() + "," + toContent();
    }
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int hash = 1;
        hash = prime * hash + ((converter == null) ? 0 : converter.hashCode());
        hash = prime * hash + ((otherParameters == null) ? 0 : otherParameters.hashCode());
        hash = prime * hash + ((propertyName == null) ? 0 : propertyName.hashCode());
        hash = prime * hash + ((propertyValueString == null) ? 0 : propertyValueString.hashCode());
        hash = prime * hash + ((unknownValue == null) ? 0 : unknownValue.hashCode());
        hash = prime * hash + ((value == null) ? 0 : value.hashCode());
        Iterator<ParameterType> i = parameterEnums().iterator();
        while (i.hasNext())
        {
            Object parameter = i.next().getParameter(this);
            hash = (31 * hash) + parameter.hashCode();
        }
        return hash;
    }

    @Override
    public int compareTo(Property<T> otherProperty)
    {
        return Integer.compare(propertyType().ordinal(), otherProperty.propertyType().ordinal());
    }
}
