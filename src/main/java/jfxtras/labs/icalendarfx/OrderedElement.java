package jfxtras.labs.icalendarfx;

import java.util.HashMap;
import java.util.Map;

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
    public Map<VCalendarElement, Integer> elementSortOrder() { return elementSortOrder; }
    final private Map<VCalendarElement, Integer> elementSortOrder = new HashMap<>();
    private volatile Integer sortOrderCounter = 0;
    
    /**
     * Sort order listener for ObservableList properties
     * Maintains {@link #elementSortOrder} map
     */
    private ListChangeListener<VCalendarElement> sortOrderListChangeListener = (ListChangeListener.Change<? extends VCalendarElement> change) ->
    {
        while (change.next())
        {
            if (change.wasAdded())
            {
                change.getAddedSubList().forEach(vComponent ->  elementSortOrder().put(vComponent, sortOrderCounter));
                sortOrderCounter += 100;
            } else
            {
                if (change.wasRemoved())
                {
                    change.getRemoved().forEach(vComponent -> 
                    {
                        elementSortOrder().remove(vComponent);
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
            list.forEach(vComponent ->  elementSortOrder().put(vComponent, sortOrderCounter));
            sortOrderCounter += 100;
        }
    }
    public void unregisterSortOrderProperty(ObservableList<? extends VCalendarElement> list)
    {
        list.removeListener(sortOrderListChangeListener);
    }
    
    /** Sort order listener for object properties
     * Maintains {@link #elementSortOrder} map
     */
    private ChangeListener<? super VCalendarElement> sortOrderChangeListener = (obs, oldValue, newValue) ->
    {
        if (oldValue != null)
        {
            elementSortOrder().remove(oldValue);
        }
        elementSortOrder().put(newValue, sortOrderCounter);
        sortOrderCounter += 100;
    };
    public void registerSortOrderProperty(ObjectProperty<? extends VCalendarElement> property)
    {
        property.addListener(sortOrderChangeListener);
        if (property.get() != null)
        { // add existing element to sort order
            elementSortOrder().put(property.get(), sortOrderCounter);
            sortOrderCounter += 100;
        }
    }
    public void unregisterSortOrderProperty(ObjectProperty<? extends VCalendarElement> property)
    {
        property.removeListener(sortOrderChangeListener);
    }
}
