package jfxtras.labs.icalendarfx.components.revisors;

import java.util.Collection;

/**
 * Interface defining the edit behavior of a VComponent
 * 
 * @author David Bal
 *
 * @param <T> - subclass
 * @param <U> - calendar component class
 */
public interface Revisable<T, U>
{
    /** Revise component based on properties set in subclasses
     * Can return one or two components.  Returns two if changes are applied to ONE or THIS-AND-FUTURE
     * 
     * @return - Collection of revised component(s)
     */
    Collection<U> revise();

}
