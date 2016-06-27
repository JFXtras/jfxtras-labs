package jfxtras.labs.icalendarfx;

import java.util.Map;

public interface OrderedElement
{
    Map<VCalendarElement, Integer> elementSortOrderMap();
    
    void copyChildrenFrom(OrderedElement source);
}
