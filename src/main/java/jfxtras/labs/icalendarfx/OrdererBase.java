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
import jfxtras.labs.icalendarfx.components.VComponentBase;
import jfxtras.labs.icalendarfx.properties.PropertyBase;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

/** Base class for framework to maintain a sort order of {@link VElement } contents
 * 
 * @see VParent
 * @see VCalendar
 * @see VComponentBase
 * @see PropertyBase
 * @see RecurrenceRule2
 *  */ 
public class OrdererBase implements Orderer
{
//    final private Callback<VElement, Void> copyChildCallback;
//    
//    /*
//     * CONSTRUCTOR
//     */
//    public OrdererBase(Callback<VElement, Void> copyChildCallback)
//    {
//        this.copyChildCallback = copyChildCallback;
//    }
    
    /** 
     * SORT ORDER
     * Component sort order map.  Key is element, value is sort order.  Follows sort order of parsed content or
     * order of added elements.
     * 
     * Generally, this map shouldn't be modified.  Only modify it when you want to force
     * a specific order.
     */
//    @Override
    public Map<VElement, Integer> elementSortOrderMap() { return elementSortOrderMap; }
    final private Map<VElement, Integer> elementSortOrderMap = new HashMap<>();
    private volatile Integer sortOrderCounter = 0;
    
    /**
     * Sort order listener for ObservableList properties
     * Maintains {@link #elementSortOrderMap} map
     */
    private ListChangeListener<VElement> sortOrderListChangeListener = (ListChangeListener.Change<? extends VElement> change) ->
    {
        while (change.next())
        {
            if (change.wasAdded())
            {
                change.getAddedSubList().forEach(vComponent ->  elementSortOrderMap.put(vComponent, sortOrderCounter));
                sortOrderCounter += 100;
            } else
            {
                if (change.wasRemoved())
                {
                    change.getRemoved().forEach(vComponent -> 
                    {
                        elementSortOrderMap.remove(vComponent);
                    });
                }                
            }
        }
    };
    @Override
    public void registerSortOrderProperty(ObservableList<? extends VElement> list)
    {
        list.addListener(sortOrderListChangeListener);
        if (! list.isEmpty())
        { // add existing elements to sort order
            list.forEach(vComponent ->  
            {
                elementSortOrderMap.put(vComponent, sortOrderCounter);
                sortOrderCounter += 100;
            });
        }
    }
    @Override
    public void unregisterSortOrderProperty(ObservableList<? extends VElement> list)
    {
        if (list != null)
        {
            list.removeListener(sortOrderListChangeListener);
        }
    }
    
    /** Sort order listener for object properties
     * Maintains {@link #elementSortOrderMap} map
     */
    private ChangeListener<? super VElement> sortOrderChangeListener = (obs, oldValue, newValue) ->
    {
        if (oldValue != null)
        {
            elementSortOrderMap.remove(oldValue);
        }
        if (newValue != null)
        {
            elementSortOrderMap.put(newValue, sortOrderCounter);
            sortOrderCounter += 100;
        }
    };
    @Override
    public void registerSortOrderProperty(ObjectProperty<? extends VElement> property)
    {
        property.addListener(sortOrderChangeListener);
        if (property.get() != null)
        { // add existing element to sort order
            elementSortOrderMap.put(property.get(), sortOrderCounter);
            sortOrderCounter += 100;
        }
    }
    @Override
    public void unregisterSortOrderProperty(ObjectProperty<? extends VElement> property)
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
        List<String> content = new ArrayList<>();
        // apply sort order (if element doesn't exist in map, don't sort)
        elementSortOrderMap.entrySet().stream()
                .sorted((Comparator<? super Entry<VElement, Integer>>) (e1, e2) -> 
                {
                    return e1.getValue().compareTo(e2.getValue());
                })
                .forEach(p -> 
                {
                    if (p.getKey() != null)
                    {
                        content.add(p.getKey().toContent());
                    }
                });
        
        return Collections.unmodifiableList(content);
    }

//    /** Copy parameters, properties, and subcomponents from source into this component,
//     * essentially making a copy of source 
//     * 
//     * Note: this method only overwrites properties found in source.  If there are properties in
//     * this component that are not present in source then those will remain unchanged.
//     * */
//    @Override
//    public void copyChildrenFrom(VCalendarParent source)
//    {
//        source.orderer().elementSortOrderMap
//                .entrySet().stream()
//                .sorted((Comparator<? super Entry<VCalendarElement, Integer>>) (e1, e2) -> 
//                {
//                    return e1.getValue().compareTo(e2.getValue());
//                })
//                .forEach((e) ->
//                {
//                    VCalendarElement child = e.getKey();
//                    copyChildCallback.call(child);
//                });
//    }
}
