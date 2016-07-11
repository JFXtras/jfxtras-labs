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

/** Base class for framework to maintain a sort order of {@link VChild } contents
 * 
 * @see VParent
 * @see VCalendar
 * @see VComponentBase
 * @see PropertyBase
 * @see RecurrenceRule2
 *  */ 
public class OrdererBase implements Orderer
{
    final private VParent parent;
    
    /** 
     * SORT ORDER
     * Component sort order map.  Key is element, value is sort order.  Follows sort order of parsed content or
     * order of added elements.
     * 
     * Generally, this map shouldn't be modified.  Only modify it when you want to force
     * a specific order.
     */
    @Override
    public Map<VChild, Integer> elementSortOrderMap() { return elementSortOrderMap; }
    final private Map<VChild, Integer> elementSortOrderMap = new HashMap<>();
    private volatile Integer sortOrderCounter = 0;

    /*
     * CONSTRUCTOR
     */
    public OrdererBase(VParent aParent)
    {
        this.parent = aParent;
        sortOrderListChangeListener = (ListChangeListener.Change<? extends VChild> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList().forEach(vChild ->  
                    {
                        elementSortOrderMap.put(vChild, sortOrderCounter);
                        vChild.setParent(parent);
                        sortOrderCounter += 100;
                    });
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
        
        sortOrderChangeListener = (obs, oldValue, newValue) ->
        {
            if (oldValue != null)
            {
                elementSortOrderMap.remove(oldValue);
            }
            if (newValue != null)
            {
                elementSortOrderMap.put(newValue, sortOrderCounter);
                newValue.setParent(parent);
                sortOrderCounter += 100;
            }
        };
    }
    
    
    /**
     * Sort order listener for ObservableList properties
     * Maintains {@link #elementSortOrderMap} map
     */
    final private ListChangeListener<VChild> sortOrderListChangeListener;

    @Override
    public void registerSortOrderProperty(ObservableList<? extends VChild> list)
    {
        list.addListener(sortOrderListChangeListener);
        if (! list.isEmpty())
        { // add existing elements to sort order
            list.forEach(vChild ->  
            {
                elementSortOrderMap.put(vChild, sortOrderCounter);
                vChild.setParent(parent);
                sortOrderCounter += 100;
            });
        }
    }
    @Override
    public void unregisterSortOrderProperty(ObservableList<? extends VChild> list)
    {
        if (list != null)
        {
            list.removeListener(sortOrderListChangeListener);
        }
    }
    
    /** Sort order listener for object properties
     * Maintains {@link #elementSortOrderMap} map
     */
    final private ChangeListener<? super VChild> sortOrderChangeListener;

    @Override
    public void registerSortOrderProperty(ObjectProperty<? extends VChild> property)
    {
        property.addListener(sortOrderChangeListener);
        if (property.get() != null)
        { // add existing element to sort order
            elementSortOrderMap.put(property.get(), sortOrderCounter);
            property.get().setParent(parent);
            sortOrderCounter += 100;
        }
    }
    @Override
    public void unregisterSortOrderProperty(ObjectProperty<? extends VChild> property)
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
                .sorted((Comparator<? super Entry<VChild, Integer>>) (e1, e2) -> 
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
