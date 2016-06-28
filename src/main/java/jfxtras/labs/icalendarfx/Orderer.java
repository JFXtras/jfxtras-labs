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
    Map<VCalendarElement, Integer> elementSortOrderMap();
    
    void copyChildrenFrom(VCalendarParent source);
    
    List<String> sortedContent();
    
    void registerSortOrderProperty(ObservableList<? extends VCalendarElement> list);
    void unregisterSortOrderProperty(ObservableList<? extends VCalendarElement> list);
    
    void registerSortOrderProperty(ObjectProperty<? extends VCalendarElement> property);
    void unregisterSortOrderProperty(ObjectProperty<? extends VCalendarElement> property);
}
