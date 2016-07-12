package jfxtras.labs.icalendarfx.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jfxtras.labs.icalendarfx.VChild;
import jfxtras.labs.icalendarfx.VParent;
import jfxtras.labs.icalendarfx.VParentBase;
import jfxtras.labs.icalendarfx.content.SingleLineContent;
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
public abstract class PropertyBase<T,U> extends VParentBase implements Property<T>, Comparable<Property<T>>
{
    private VParent myParent;
    @Override public void setParent(VParent parent) { myParent = parent; }
    @Override public VParent getParent() { return myParent; }
    
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
//        if (propertyName == null)
//        {
//            return propertyType().toString();
//        }
        return propertyName.get();
    }
    public ObjectProperty<String> propertyNameProperty() { return propertyName; }
    private ObjectProperty<String> propertyName =  new SimpleObjectProperty<String>();
    /** Set the name of the property.  Only allowed for non-standard and IANA properties */
    public void setPropertyName(String name)
    {
        if (propertyType().equals(PropertyType.NON_STANDARD))
        {
            if (name.substring(0, 2).toUpperCase().equals("X-"))
            {
                propertyName.set(name);
            } else
            {
                throw new RuntimeException("Non-standard properties must begin with X-");                
            }
        } else if (propertyType().equals(PropertyType.IANA_PROPERTY))
        {
            if (IANAProperty.REGISTERED_IANA_PROPERTY_NAMES.contains(name))
            {
                propertyName.set(name);
            } else
            {
                throw new RuntimeException(name + " is not an IANA-registered property name.  Registered names are in IANAProperty.REGISTERED_IANA_PROPERTY_NAMES");
            }
        } else if (propertyType().toString().equals(name)) // let setting name to default value have no operation
        {
            propertyName.set(name);
        } else
        {
            throw new RuntimeException("Custom property names can only be set for non-standard and IANA-registered properties.");
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
     * List of all child parameters.
     * The list is unmodifiable.
     * 
     * @return - the list of elements
     * @deprecated  not needed due to addition of Orderer, may be deleted
     */
    @Deprecated
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
    
    @Override
    public Callback<VChild, Void> copyChildCallback()
    {        
        return (child) ->
        {
            ParameterType type = ParameterType.enumFromClass(child.getClass());
            type.copyParameter((Parameter<?>) child, this);
            return null;
        };
    }
    
    // property value as string - kept if string converter changes the value can change
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
        return ! getConverter().equals(defaultConverter);
    }
    
    /*
     * CONSTRUCTORS
     */
    
    protected PropertyBase()
    {
        orderer().registerSortOrderProperty(valueTypeProperty());
        propertyType = PropertyType.enumFromClass(getClass());
        setPropertyName(propertyType.toString());
        value = new SimpleObjectProperty<T>(this, propertyType.toString());
        ValueType defaultValueType = propertyType.allowedValueTypes().get(0);
        defaultConverter = defaultValueType.getConverter();
        setConverter(defaultConverter);
        valueTypeProperty().addListener(valueParameterChangeListener); // keeps value synched with value type
        setContentLineGenerator(new SingleLineContent(
                orderer(),
                propertyNameProperty(),
                50));
    }

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
        setConverter(source.getConverter());
        copyChildrenFrom(source);
        T valueCopy = copyValue(source.getValue());
        setValue(valueCopy);
        setPropertyName(source.getPropertyName());
    }
    
    // constructor with only value parameter
    public PropertyBase(T value)
    {
        this();
        setValue(value);
    }

    // return a copy of the value
    protected T copyValue(T source)
    {
        return source; // for mutable values override in subclasses
    }
    
    // Set converter when using constructor with class parameter
    protected void setConverterByClass(Class<T> valueClass)
    {
        // do nothing - hook to override in subclass for functionality
    }
    
    /** Parse content line into calendar property */
    @Override
    public void parseContent(String contentLine)
    {
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
        map.entrySet()
            .stream()
            .filter(entry -> ! (entry.getKey() == ICalendarUtilities.PROPERTY_VALUE_KEY))
            .forEach(entry ->
            {
                ParameterType parameterType = ParameterType.enumFromName(entry.getKey());
                if (parameterType != null)
                {
                    if (propertyType().allowedParameters().contains(parameterType))
                    {
                        parameterType.parse(this, entry.getValue());
                    } else
                    {
                        throw new IllegalArgumentException("Parameter " + parameterType + " not allowed for property " + propertyType());
                    }
                } else if ((entry.getKey() != null) && (entry.getValue() != null))
                { // unknown parameter - store as String in other parameter
                    ParameterType.OTHER.parse(this, entry.getKey() + "=" + entry.getValue());
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

    @Override
    public String toContent()
    {
        StringBuilder builder = new StringBuilder(super.toContent());
        builder.append(":" + valueContent());
        // return folded line
        return ICalendarUtilities.foldLine(builder).toString();
    }
    
    @Override
    public String toString()
    {
        return super.toString() + "," + toContent();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        boolean parametersEquals = super.equals(obj);
        if (! parametersEquals) return false;
        PropertyBase<?,?> testObj = (PropertyBase<?,?>) obj;
        boolean valueEquals = (getValue() == null) ? (testObj.getValue() == null) : getValue().equals(testObj.getValue());
        boolean nameEquals = getPropertyName().equals(testObj.getPropertyName());
        return valueEquals && parametersEquals && nameEquals;
    }

    @Override
    public int hashCode()
    {
        int hash = super.hashCode();
        final int prime = 31;
        hash = prime * hash + ((converter == null) ? 0 : converter.hashCode());
        hash = prime * hash + ((propertyName == null) ? 0 : propertyName.hashCode());
        hash = prime * hash + ((propertyValueString == null) ? 0 : propertyValueString.hashCode());
        hash = prime * hash + ((unknownValue == null) ? 0 : unknownValue.hashCode());
        hash = prime * hash + ((value == null) ? 0 : value.hashCode());
        return hash;
    }

    @Override
    public int compareTo(Property<T> otherProperty)
    {
        return Integer.compare(propertyType().ordinal(), otherProperty.propertyType().ordinal());
    }
}
