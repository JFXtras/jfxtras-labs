package jfxtras.labs.icalendarfx;

import java.util.HashMap;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/** Provide a framework to maintain a sort order for of VCalendarElements */ 
public abstract class OrderedElement
{
    /** 
     * SORT ORDER
     * Component sort order map.  Key is component, value is order.  Follows sort order of parsed content or
     * order of added components.
     * 
     * If a parameter is not present in the map, it is put at the end of the sorted by
     * DTSTAMP.  If DTSTAMP is not present, the component is put on top.
     * Generally, this map shouldn't be modified.  Only modify it when you want to force
     * a specific parameter order (e.g. unit testing).
     */
    public Map<VCalendarElement, Integer> elementSortOrder() { return elementSortOrder; }
    final private Map<VCalendarElement, Integer> elementSortOrder = new HashMap<>();
    private volatile Integer sortOrderCounter = 0;
    
    /**
     * Maintains {@link #elementSortOrder} map
     */
    protected ListChangeListener<VCalendarElement> sortOrderListChangeListener = (ListChangeListener.Change<? extends VCalendarElement> change) ->
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
    
    protected ChangeListener<? super VCalendarElement> sortOrderChangeListener = (obs, oldValue, newValue) ->
    {
        if (oldValue != null)
        {
            elementSortOrder().remove(oldValue);
        }
        elementSortOrder().put(newValue, sortOrderCounter);
        sortOrderCounter += 100;
    };
    
    public <E extends VCalendarElement> ObservableList<E> observableArrayListWithOrderListener()
    {
        ObservableList<E> list = FXCollections.observableArrayList();
        list.addListener(sortOrderListChangeListener);
        return list;
    }
}
