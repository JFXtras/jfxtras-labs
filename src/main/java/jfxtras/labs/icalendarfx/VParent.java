package jfxtras.labs.icalendarfx;

import java.util.Collection;

/**
 * Interface for parent calendar components.  Has methods for getting list of children and copying
 * children to another parent.
 * 
 * Adding children is not exposed by the implementation, but rather handled internally when a calendar 
 * component has a property change.
 * 
 * @author David Bal
 */
public interface VParent extends VElement
{
    /** returns unmodifiable collection of {@link VChild} elements.  The order of the children equals the order they were added.
     * Adding children is not exposed by the implementation, but rather handled internally.  When a {@link VChild} has its
     * value set it's automatically included in the collection of children. */
    Collection<VChild> childrenUnmodifiable();
    
    /** Copy {@link VChild} elements from source into this {@link VParent} */
    void copyChildrenFrom(VParent source);
}
