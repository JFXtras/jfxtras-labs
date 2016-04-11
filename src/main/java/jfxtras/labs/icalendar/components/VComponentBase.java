package jfxtras.labs.icalendar.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.properties.Property;
import jfxtras.labs.icalendar.properties.PropertyEnum;

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
     * other-properties, 3.8.8 RFC 5545 page 139
     * IANA and non-standard properties
     * the entire property name and value are combined into one object
     * This is a limitation as the parameters are not separated
     * TODO - MAKE OBJECTS PROPERTIES
     */
    @Override
    public ObservableList<Object> otherProperties() { return otherProperties; }
    private ObservableList<Object> otherProperties = FXCollections.observableArrayList();
    public T withOtherParameters(Object... properties) { otherProperties().addAll(properties); return (T) this; }

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
//          System.out.println("pt:" + propertyType + " " + property);
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
//    @Override public ObservableList<String> getXProperties() { return xProperties; }
//    private ObservableList<String> xProperties = FXCollections.observableArrayList();
//    @Override public void setXProperties(ObservableList<String> xprop) { xProperties = xprop; }
//    public T withXProperties(String... xProperties) { getXProperties().addAll(xProperties); return (T) this; }
//
//    @Override public ObservableList<String> getIANAProperties() { return ianaProperties; }
//    private ObservableList<String> ianaProperties = FXCollections.observableArrayList();
//    @Override public void setIANAProperties(ObservableList<String> iana) { ianaProperties = iana; }
//    public T withIANAProperties(String... ianaProperties) { getIANAProperties().addAll(ianaProperties); return (T) this; }

    @Override
    public CharSequence toContentLines()
    {
        StringBuilder builder = new StringBuilder(400);
        builder.append(firstContentLine() + System.lineSeparator());
        List<PropertyEnum> properties = properties(); // make properties local to avoid creating list multiple times
        // add parameters
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
        otherProperties().stream().forEach(p -> builder.append(";" + p));
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
