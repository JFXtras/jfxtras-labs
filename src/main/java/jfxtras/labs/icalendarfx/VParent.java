package jfxtras.labs.icalendarfx;

import java.util.Collection;

/**
 * Interface for parent calendar components.  Has methods for getting list of children and copying
 * children to another parent.
 * 
 * Adding children is not exposed by the implementation, but rather handled internally when a calendar 
 * component has a property change.
 * 
 * The order of the children is determines the order of the content outputed by a calendar component.
 * 
 * @author David Bal
 */
public interface VParent extends VElement
{
    /** returns unmodifiable collection of child elements */
    Collection<VElement> childrenUnmodifiable();
    
    /** Copy children elements from source into this parent */
    void copyChildrenFrom(VParent source);
}
