package jfxtras.labs.icalendarfx.components.revisors;

import java.util.Collection;

/**
 * Interface defining the edit behavior of a VComponent
 * 
 * @author David Bal
 *
 * @param <U> - calendar VComponent class
 */
public interface Revisable
{
    /** Revise component based on properties set in subclasses
     * Can return one or two components.  Returns two if changes are applied to ONE or THIS-AND-FUTURE
     * 
     * @return - Collection of revised component(s)
     */
    Collection<?> revise();

}
