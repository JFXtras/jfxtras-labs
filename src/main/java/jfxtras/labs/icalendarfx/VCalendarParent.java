package jfxtras.labs.icalendarfx;

public interface VCalendarParent
{
    /** Encapsulated framework to order children elements */
    Orderer orderer();
    
    /** Copy children elements from source into this parent */
    default void copyChildrenFrom(VCalendarParent source)
    {
        orderer().copyChildrenFrom(source);
    }
}
