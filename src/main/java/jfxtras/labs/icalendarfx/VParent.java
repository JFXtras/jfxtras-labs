package jfxtras.labs.icalendarfx;

import java.util.Collection;

public interface VParent
{
    /** Encapsulated framework to order child {@link VCalendarElement } elements */
//    Orderer orderer();
    
    /** returns collection of child elements */
    Collection<VElement> childrenUnmodifiable();
//    default Collection<VCalendarElement> childrenUnmodifiable()
//    {
//        return Collections.unmodifiableSet(orderer().elementSortOrderMap().keySet());
//    }
    
    /** Copy children elements from source into this parent */
    void copyChildrenFrom(VParent source);
//    default void copyChildrenFrom(VCalendarParent source)
//    {
//        orderer().copyChildrenFrom(source);
//    }
}
