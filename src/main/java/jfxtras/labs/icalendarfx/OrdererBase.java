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
            final int sortOrder;
            if ((oldValue != null) && (elementSortOrderMap.get(oldValue) != null))
            {
                sortOrder = elementSortOrderMap.get(oldValue);
                elementSortOrderMap.remove(oldValue);
            } else
            {
                sortOrder = sortOrderCounter;
                sortOrderCounter += 100;
            }
            if (newValue != null)
            {
                elementSortOrderMap.put(newValue, sortOrder);
                newValue.setParent(parent);                
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
//        System.out.println("unregistered" + list);
        if (! list.isEmpty())
        { // remove existing elements to sort order
            list.forEach(vChild -> elementSortOrderMap.remove(vChild));
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
        elementSortOrderMap.remove(property);
//        property.removeListener(sortOrderChangeListener); // not needed
    }
    
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
}
