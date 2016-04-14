package jfxtras.labs.icalendar.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.properties.Property;
import jfxtras.labs.icalendar.properties.PropertyEnum;
import jfxtras.labs.icalendar.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendar.properties.component.misc.NonStandardProperty;

/**
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see VTimeZone
 * @see VAlarm
 */
public class VComponentBase<T> implements VComponentNew
{
    /**
     * 3.8.8.2.  Non-Standard Properties
     * Any property name with a "X-" prefix
     * 
     * Example:
     * X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.
     *  org/mysubj.au
     */
    @Override
    public ObservableList<NonStandardProperty> getNonStandardProperties()
    {
        if (nonStandardProps == null)
        {
            nonStandardProps = FXCollections.observableArrayList();
        }
        return nonStandardProps;
    }
    private ObservableList<NonStandardProperty> nonStandardProps;
    @Override
    public void setNonStandardProperties(ObservableList<NonStandardProperty> comments) { this.nonStandardProps = comments; }
    /** add comma separated comments into separate comment objects */
    public T withNonStandardProperty(String...comments)
    {
        Arrays.stream(comments).forEach(c -> getNonStandardProperties().add(new NonStandardProperty(c)));
        return (T) this;
    }
    
    /**
     * 3.8.8.1.  IANA Properties
     * An IANA-registered property name
     * 
     * Examples:
     * NON-SMOKING;VALUE=BOOLEAN:TRUE
     * DRESSCODE:CASUAL
     */
    @Override
    public ObservableList<IANAProperty> getIANAProperties()
    {
        if (IANAProps == null)
        {
            IANAProps = FXCollections.observableArrayList();
        }
        return IANAProps;
    }
    private ObservableList<IANAProperty> IANAProps;
    @Override
    public void setIANAProperties(ObservableList<IANAProperty> comments) { this.IANAProps = comments; }
    /** add comma separated comments into separate comment objects */
    public T withIANAProperty(String...comments)
    {
        Arrays.stream(comments).forEach(c -> getIANAProperties().add(new IANAProperty(c)));
        return (T) this;
    }
    
    /**
     * List of all properties found in component.
     * The list is unmodifiable.
     * 
     * @return - the list of properties
     */
    @Override
    public List<PropertyEnum> properties()
    {
        List<PropertyEnum> populatedProperties = new ArrayList<>();
//      System.out.println("parameters:" + propertyType().possibleParameters().size());
        Iterator<PropertyEnum> i = componentType().allowedProperties().stream().iterator();
        while (i.hasNext())
        {
            PropertyEnum propertyType = i.next();
            Object property = propertyType.getProperty(this);
            if (property != null)
            {
                populatedProperties.add(propertyType);
            }
        }
      return Collections.unmodifiableList(populatedProperties);
    }
    
    // List of properties obtained by parsing content lines, used to maintain order when generating content lines
    private LinkedList<Property<?>> propertyOrderList = new LinkedList<>();

    // Property sort order map.  Key is property name.  Follows sort order of parsed content.  If not present sorts alphabetical
    private Map<String, Integer> propertySortOrder = new HashMap<>();
    
    /*
     * CONSTRUCTORS
     */
    VComponentBase() { }
    
    /** Parse content lines into calendar component */
    VComponentBase(String contentLines)
    {
        Arrays.stream(contentLines.split(System.lineSeparator()))
        .forEach(line ->
        {
            List<Integer> indices = new ArrayList<>();
            indices.add(line.indexOf(':'));
            indices.add(line.indexOf(';'));
            int nameEndIndex = indices
                    .stream()
                    .filter(v -> v > 0)
                    .min(Comparator.naturalOrder())
                    .get();
            String propertyName = line.substring(0, nameEndIndex);
//            String propertyContent = line.substring(nameEndIndex);
//            System.out.println("propertyName" + propertyName);
//            System.out.println("propertyContent" + propertyContent);
            PropertyEnum propertyType = PropertyEnum.enumFromName(propertyName);
            // ignore unknown properties
            if (propertyType != null)
            {
                propertyType.parse(this, line);
                Object property = propertyType.getProperty(this);
                if (property instanceof Property<?>)
                {
                    propertyOrderList.add((Property<?>) property);
                } else if (property instanceof List<?>)
                {
                    ((List<? extends Property<?>>) property).stream()
                            .forEach(p -> propertyOrderList.add(p));
                } else
                {
                    throw new IllegalArgumentException("Invalid property type:" + property.getClass());
                }
            }
        });
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        Iterator<PropertyEnum> i = properties().iterator();
        while (i.hasNext())
        {
            Object property = i.next().getProperty(this);
            hash = (31 * hash) + property.hashCode();
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
        VComponentBase<?> testObj = (VComponentBase<?>) obj;
        
        final boolean propertiesEquals;
        List<PropertyEnum> properties = properties(); // make properties local to avoid creating list multiple times
        List<PropertyEnum> testProperties = testObj.properties(); // make properties local to avoid creating list multiple times
        if (properties.size() == testProperties.size())
        {
            Iterator<PropertyEnum> i1 = properties.iterator();
            Iterator<PropertyEnum> i2 = testProperties.iterator();
            boolean isFailure = false;
            while (i1.hasNext())
            {
                Object p1 = i1.next().getProperty(this);
                Object p2 = i2.next().getProperty(testObj);
                System.out.println("p1,p2:" + p1 + " " + p2 + " " + p1.equals(p2));
                if (! p1.equals(p2))
                {
                    isFailure = true;
                    break;
                }
            }
            propertiesEquals = ! isFailure;
        } else
        {
            propertiesEquals = false;
        }
//        System.out.println("equals:" + valueEquals + " " + otherParametersEquals + " " + parametersEquals);
        return propertiesEquals;
    }
    
    @Override
    public String toContentLines()
    {
//        List<PropertyEnum> properties = properties(); // make properties local to avoid creating list multiple times
        // add individual properties
//        List<Pair<Object,CharSequence>> propertyAndContent = new ArrayList<>();
        Map<Object, CharSequence> propertyAndContentMap = new HashMap<>();
        
//        Map<Object, Object> nameAndContent = properties.stream().collect(Collectors
//                .toMap(p -> p.getProperty(this),
//                       p -> p.getProperty(this).toConentLine() ));
        
        Iterator<PropertyEnum> i = properties().iterator();
        while (i.hasNext())
        {
            Object property = i.next().getProperty(this);
            if (property instanceof Property<?>)
            {
                propertyAndContentMap.put(property, ((Property<?>) property).toContentLine());
//                propertyAndContent.add(new Pair<>(property, ((Property<?>) property).toContentLine()));
            } else if (property instanceof List<?>)
            {
                ((List<? extends Property<?>>) property).stream()
//                        .forEach(p -> propertyAndContent.add( new Pair<>(property, ((Property<?>) p).toContentLine())));                
                        .forEach(p -> propertyAndContentMap.put(property, ((Property<?>) p).toContentLine()));                
            } else
            {
                throw new IllegalArgumentException("Invalid property type:" + property.getClass());
            }
        }
//        properties.stream()
//                .map(p -> p.getProperty(this))
//                .flatMap(prop -> 
//                {
////                    Object prop = p.getProperty(this);
//                    if (prop instanceof Property<?>)
//                    {
//                        return Arrays.asList((Property<?>) prop).stream();
//                    } else if (prop instanceof List<?>)
//                    {
//                        return ((List<? extends Property<?>>) prop).stream();
//                    } else
//                    {
//                        throw new IllegalArgumentException("Invalid property type:" + prop.getClass());
//                    }
//                })
//                .filter(p -> p != null)
////                .forEach(p -> builder.append(p.toContentLine() + System.lineSeparator()));
//                .forEach(p -> nameAndContent.put(p, p.toContentLine()));

        // TODO - SPLIT LINES
        // add non-standard parameters
//        getNonStandardProperties().stream().forEach(p -> builder.append(";" + p));
//        getIANAProperties().stream().forEach(p -> builder.append(";" + p));
        
//        propertyAndContent.stream().forEach(System.out::println);
        
        StringBuilder builder = new StringBuilder(400);
        builder.append(firstContentLine() + System.lineSeparator());
//        propertyOrderList.stream().forEach(p -> System.out.println(p.hashCode()));
        // add properties from propertyOrder sort order
        propertyOrderList.stream().forEach(p -> 
        {
            boolean inList = propertyAndContent.contains(p);
            if (inList)
            {
                String line = 
                // TODO - MAYBE CHANGE TO HAVING SORT ORDER BASED BY PROPERTY NAME
                builder.append( + System.lineSeparator());
            }
        } );
        // add properties not in propertyOrder
        propertyAndContent.stream().forEach(p -> builder.append(p.getValue() + System.lineSeparator()));
        
        builder.append(lastContentLine());
        // add property value
//        builder.append(":" + propertyType().defaultValueType().makeContent(getValue()));
//        builder.append(":" + valueToString(getValue()));
        return builder.toString();
    }

    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VEVENT; // for testing
    }
    
//    Map<PropertyEnum, List<Property>> propertyMap() { return propertyMap; }
//    Map<PropertyEnum, Property> propertyMap() { return propertyMap; } // properties that can only occur once
 //   Map<PropertyEnum, List<? extends Property>> propertyListMap() { return propertyListMap; } // properties that can occur more than once
//    Map<PropertyEnum, List<? extends Property>> propertyMap() { return propertyMap; }
//    private final Map<PropertyEnum, Property> propertyMap = new HashMap<>();
//    private final Map<PropertyEnum, List<? extends Property>> propertyListMap = new HashMap<>();
//    private final Map<PropertyEnum, List<Property>> propertyMap = new HashMap<>();
}
