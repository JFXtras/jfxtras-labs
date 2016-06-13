package jfxtras.labs.icalendarfx.components.revisors;

import java.util.Collection;

/**
 * Interface defining the edit behavior of a VComponent
 * 
 * @author David Bal
 *
 * @param <T> - subclass
 */
//public interface Revisable<T, U extends VComponent<U>>
public interface Revisable<T, U>
{
    /** Revise component based on properties set in subclasses
     * Adds new components to vComponents argument
     * 
     */
//    void revise(Collection<U> VComponents);
    Collection<U> revise();

}
