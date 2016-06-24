package jfxtras.labs.icalendarfx;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/** Provide a framework to maintain a sort order of VCalendarElement contents */ 
public abstract class OrderedElement
{
    /** 
     * SORT ORDER
     * Component sort order map.  Key is element, value is sort order.  Follows sort order of parsed content or
     * order of added elements.
     * 
     * Generally, this map shouldn't be modified.  Only modify it when you want to force
     * a specific order.
     */
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
        elementSortOrderMap().put(newValue, sortOrderCounter);
        sortOrderCounter += 100;
    };
    public void registerSortOrderProperty(ObjectProperty<? extends VCalendarElement> property)
    {
        property.addListener(sortOrderChangeListener);
        if (property.get() != null)
        { // add existing element to sort order
            elementSortOrderMap().put(property.get(), sortOrderCounter);
            sortOrderCounter += 100;
        }
    }
    public void unregisterSortOrderProperty(ObjectProperty<? extends VCalendarElement> property)
    {
        property.removeListener(sortOrderChangeListener);
    }
    
    /**
     * Make a list of sorted content lines. Used by { @link VCalendarElement#toContent() }
     * 
     * @return - list of sorted content lines
     */
    protected List<String> sortedContent()
    {
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
//                        System.out.println(p.getKey().hashCode());
                        content.add(p.getKey().toContent());
                    }
                });
        
        return content;
    }
}
