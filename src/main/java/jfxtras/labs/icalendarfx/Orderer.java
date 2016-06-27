package jfxtras.labs.icalendarfx;

import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;

public interface Orderer
{
    Map<VCalendarElement, Integer> elementSortOrderMap();
    
    void copyChildrenFrom(VCalendarElement source);
    
    List<String> sortedContent();
    
    void registerSortOrderProperty(ObservableList<? extends VCalendarElement> list);
    void unregisterSortOrderProperty(ObservableList<? extends VCalendarElement> list);
    
    void registerSortOrderProperty(ObjectProperty<? extends VCalendarElement> property);
    void unregisterSortOrderProperty(ObjectProperty<? extends VCalendarElement> property);
}
