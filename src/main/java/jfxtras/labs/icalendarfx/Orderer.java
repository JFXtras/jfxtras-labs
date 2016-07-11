package jfxtras.labs.icalendarfx;

import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;

/** Provide a framework to maintain a sort order of VCalendarElement contents
 * 
 * @see VCalendar
 * @see VComponent
 * @see Property
 * @see RecurrenceRule
 *  */ 
public interface Orderer
{
    /** 
     * SORT ORDER
     * Component sort order map.  Key is element, value is sort order.  Follows sort order of parsed content or
     * order of added elements.
     * 
     * Generally, this map shouldn't be modified.  Only modify it when you want to force
     * a specific order.
     */
    Map<VChild, Integer> elementSortOrderMap();
    
    /**
     * Make a list of sorted content elements. Used by { @link VCalendarElement#toContent() }
     * 
     * @return - unmodifiable list of sorted content lines
     */
    List<String> sortedContent();
    
    void registerSortOrderProperty(ObservableList<? extends VChild> list);
    void unregisterSortOrderProperty(ObservableList<? extends VChild> list);
    
    void registerSortOrderProperty(ObjectProperty<? extends VChild> property);
    void unregisterSortOrderProperty(ObjectProperty<? extends VChild> property);
}
