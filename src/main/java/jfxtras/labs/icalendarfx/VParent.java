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
    Collection<VChild> childrenUnmodifiable();
//    Orderer orderer();
    
//    Callback<VChild, Void> copyChildCallback();
    
    /** Copy children elements from source into this parent */
    void copyChildrenFrom(VParent source);
//    
//    /** returns read-only collection of child elements following the sort order controlled by {@link Orderer} */
//    default Collection<VChild> childrenUnmodifiable()
//    {
//        return Collections.unmodifiableList(
//                orderer().elementSortOrderMap().entrySet().stream()
//                .sorted((Comparator<? super Entry<VChild, Integer>>) (e1, e2) -> e1.getValue().compareTo(e2.getValue()))
//                .map(e -> e.getKey())
//                .collect(Collectors.toList())
//                );
//    }
//    
//    /** Copy children elements from source into this parent */
//    /** Copy parameters, properties, and subcomponents from source into this component,
//    * essentially making a copy of source 
//    * 
//    * Note: this method only overwrites properties found in source.  If there are properties in
//    * this component that are not present in source then those will remain unchanged.
//    * */
//    default void copyChildrenFrom(VParent source)
//    {
//         source.childrenUnmodifiable().forEach((e) -> copyChildCallback().call(e));
//    }
//    
//    @Override
//    default List<String> errors()
//    {
//        return childrenUnmodifiable().stream()
//                .flatMap(c -> c.errors().stream())
//                .collect(Collectors.toList());
//    }
}
