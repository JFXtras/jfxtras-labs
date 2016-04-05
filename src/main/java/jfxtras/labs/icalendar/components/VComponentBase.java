package jfxtras.labs.icalendar.components;

import java.util.List;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.properties.PropertyEnum;

/**
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see VTimeZone
 * @see VAlarm
 */
public class VComponentBase<T> implements VComponent
{

    @Override
    public ObservableList<Object> otherParameters()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PropertyEnum> properties()
    {
        // TODO Auto-generated method stub
        return null;
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
    
//    Map<PropertyEnum, List<Property>> propertyMap() { return propertyMap; }
//    Map<PropertyEnum, Property> propertyMap() { return propertyMap; } // properties that can only occur once
 //   Map<PropertyEnum, List<? extends Property>> propertyListMap() { return propertyListMap; } // properties that can occur more than once
//    Map<PropertyEnum, List<? extends Property>> propertyMap() { return propertyMap; }
//    private final Map<PropertyEnum, Property> propertyMap = new HashMap<>();
//    private final Map<PropertyEnum, List<? extends Property>> propertyListMap = new HashMap<>();
//    private final Map<PropertyEnum, List<Property>> propertyMap = new HashMap<>();
}
