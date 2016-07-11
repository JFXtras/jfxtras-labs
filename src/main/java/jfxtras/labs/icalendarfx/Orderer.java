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
    Map<VChild, Integer> elementSortOrderMap();
    
    List<String> sortedContent();
    
    void registerSortOrderProperty(ObservableList<? extends VChild> list);
    void unregisterSortOrderProperty(ObservableList<? extends VChild> list);
    
    void registerSortOrderProperty(ObjectProperty<? extends VChild> property);
    void unregisterSortOrderProperty(ObjectProperty<? extends VChild> property);
}
