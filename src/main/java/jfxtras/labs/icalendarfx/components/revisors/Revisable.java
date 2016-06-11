package jfxtras.labs.icalendarfx.components.revisors;

import java.util.Collection;

/**
 * Interface defining the edit behavior of a VComponent
 * 
 * @author David Bal
 *
 * @param <T> - subclass
 */
public interface Revisable<T, U>
{
    /** Revise component based on properties set in subclasses
     * 
     * @return - new VComponents based on changed
     */
    Collection<U> revise();
}
