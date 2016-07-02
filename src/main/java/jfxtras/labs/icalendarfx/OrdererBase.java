package jfxtras.labs.icalendarfx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;

/** Base class for framework to maintain a sort order of VCalendarElement contents
 * 
 * @see VCalendar
 * @see VComponent
 * @see Property
 * @see RecurrenceRule
 *  */ 
public class OrdererBase implements Orderer
{
    final private Callback<VCalendarElement, Void> copyChildCallback;
//    final private Callback<Void, List<String>> elementNameListCallback;
//    final private Callback<VCalendarElement, String> elementNameCallback;
    
    public OrdererBase(
            Callback<VCalendarElement, Void> copyChildCallback
//            Callback<Void, List<String>> elementNamesCallback,
//            Callback<VCalendarElement, String> elementNameCallback
            )
    {
        this.copyChildCallback = copyChildCallback;
//        this.elementNameListCallback = elementNamesCallback;
//        this.elementNameCallback = elementNameCallback;
    }
    
    /** 
     * SORT ORDER
     * Component sort order map.  Key is element, value is sort order.  Follows sort order of parsed content or
     * order of added elements.
     * 
     * Generally, this map shouldn't be modified.  Only modify it when you want to force
     * a specific order.
     */
    @Override
    public Map<VCalendarElement, Integer> elementSortOrderMap() { return elementSortOrderMap; }
    final private Map<VCalendarElement, Integer> elementSortOrderMap = new HashMap<>();
    private volatile Integer sortOrderCounter = 0;
    
    /**
     * Sort order listener for ObservableList properties
     * Maintains {@link #elementSortOrderMap} map
     */
    private ListChangeListener<VCalendarElement> sortOrderListChangeListener = (ListChangeListener.Change<? extends VCalendarElement> change) ->
    {
        while (change.next())
        {
            if (change.wasAdded())
            {
                change.getAddedSubList().forEach(vComponent ->  elementSortOrderMap().put(vComponent, sortOrderCounter));
                sortOrderCounter += 100;
            } else
            {
                if (change.wasRemoved())
                {
                    change.getRemoved().forEach(vComponent -> 
                    {
                        elementSortOrderMap().remove(vComponent);
                    });
                }                
            }
        }
    };
    @Override
    public void registerSortOrderProperty(ObservableList<? extends VCalendarElement> list)
    {
        list.addListener(sortOrderListChangeListener);
        if (! list.isEmpty())
        { // add existing elements to sort order
            list.forEach(vComponent ->  
            {
                elementSortOrderMap().put(vComponent, sortOrderCounter);
                sortOrderCounter += 100;
            });
        }
    }
    @Override
    public void unregisterSortOrderProperty(ObservableList<? extends VCalendarElement> list)
    {
        if (list != null)
        {
            list.removeListener(sortOrderListChangeListener);
        }
    }
    
    /** Sort order listener for object properties
     * Maintains {@link #elementSortOrderMap} map
     */
    private ChangeListener<? super VCalendarElement> sortOrderChangeListener = (obs, oldValue, newValue) ->
    {
        if (oldValue != null)
        {
            elementSortOrderMap().remove(oldValue);
        }
        if (newValue != null)
        {
//            System.out.println("added prop:" + sortOrderCounter + " " + newValue );
            elementSortOrderMap().put(newValue, sortOrderCounter);
//            elementSortOrderMap().put(sortOrderCounter, newValue);
            sortOrderCounter += 100;
//            System.out.println("sortOrderCounter3:" + sortOrderCounter);
        }
    };
    @Override
    public void registerSortOrderProperty(ObjectProperty<? extends VCalendarElement> property)
    {
        property.addListener(sortOrderChangeListener);
        if (property.get() != null)
        { // add existing element to sort order
            elementSortOrderMap().put(property.get(), sortOrderCounter);
//            elementSortOrderMap().put(sortOrderCounter, property.get());
            sortOrderCounter += 100;
//            System.out.println("sortOrderCounter4:" + sortOrderCounter);
        }
    }
    @Override
    public void unregisterSortOrderProperty(ObjectProperty<? extends VCalendarElement> property)
    {
        property.removeListener(sortOrderChangeListener);
    }
    
    /**
     * Make a list of sorted content elements. Used by { @link VCalendarElement#toContent() }
     * 
     * @return - unmodifiable list of sorted content lines
     */
    @Override
    public List<String> sortedContent()
    {        
//        // check properties to make sure all are accounted for in map
//        List<String> elementNames = elementNameListCallback.call(null);
//        Optional<VCalendarElement> propertyNotFound = elementSortOrderMap().entrySet()
//            .stream()
//            .map(e -> e.getKey())
//            .filter(v ->
//            {
////                PropertyType myType = PropertyType.enumFromClass(v.getClass());
//                String myElementName = elementNameCallback.call(v);
//                return (myElementName == null) ? false : elementNames.contains(myElementName);
//            })
//            .findAny();
//        if (propertyNotFound.isPresent())
//        {
//            throw new RuntimeException("element not found:" + propertyNotFound.get());
//        }

        List<String> content = new ArrayList<>();
        // apply sort order (if element doesn't exist in map, don't sort)
        elementSortOrderMap().entrySet().stream()
                .sorted((Comparator<? super Entry<VCalendarElement, Integer>>) (e1, e2) -> 
                {
                    return e1.getValue().compareTo(e2.getValue());
                })
                .forEach(p -> 
                {
                    if (p.getKey() != null)
                    {
//                        System.out.println(p.getKey() + " " + p.getValue());
//                        content.add(p.getValue().toContent());
                        content.add(p.getKey().toContent());
                    }
                });
        
        return Collections.unmodifiableList(content);
    }

    /** Copy parameters, properties, and subcomponents from source into this component,
     * essentially making a copy of source 
     * 
     * Note: this method only overwrites properties found in source.  If there are properties in
     * this component that are not present in source then those will remain unchanged.
     * */
    @Override
    public void copyChildrenFrom(VCalendarParent source)
    {
        source.orderer().elementSortOrderMap()
                .entrySet().stream()
                .sorted((Comparator<? super Entry<VCalendarElement, Integer>>) (e1, e2) -> 
                {
                    return e1.getValue().compareTo(e2.getValue());
                })
                .forEach((e) ->
                {
                    VCalendarElement child = e.getKey();
                    copyChildCallback.call(child);
                });
    }
    
//    /** Copy child to particular subclass parent */
//    protected abstract void copyChild(VCalendarElement key);

}
