package jfxtras.labs.icalendar.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
            Property<?> property = propertyType.getProperty(this);
//          System.out.println("property:" + propertyType + " " + property);
            if (property != null)
            {
                populatedProperties.add(propertyType);
            } else
            { // check property list
                List<? extends Property<?>> propertyList = propertyType.getPropertyList(this);
                if (propertyList != null)
                {
                    populatedProperties.add(propertyType);
                }
            }
        }
      return Collections.unmodifiableList(populatedProperties);
    }


    @Override
    public CharSequence toContentLines()
    {
        StringBuilder builder = new StringBuilder(400);
        builder.append(firstContentLine() + System.lineSeparator());
        List<PropertyEnum> properties = properties(); // make properties local to avoid creating list multiple times
        // add properties
        properties.stream()
                .map(p -> p.getProperty(this))
                .filter(p -> p != null)
                .forEach(p -> builder.append(p.toContentLine() + System.lineSeparator()));
        properties.stream()
                .flatMap(prop -> 
                {
                    List<? extends Property<?>> plist = prop.getPropertyList(this);
//                    System.out.println("prop:" + prop + " " + plist);
                    return (plist != null) ? plist.stream() : null;
                })
                .filter(p -> p != null)
                .forEach(p -> builder.append(p.toContentLine() + System.lineSeparator()));

        // TODO - SPLIT LINES
        // add non-standard parameters
        getNonStandardProperties().stream().forEach(p -> builder.append(";" + p));
        getIANAProperties().stream().forEach(p -> builder.append(";" + p));
        builder.append(lastContentLine());
        // add property value
//        builder.append(":" + propertyType().defaultValueType().makeContent(getValue()));
//        builder.append(":" + valueToString(getValue()));
        return builder;
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
