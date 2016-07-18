package jfxtras.labs.icalendarfx.components.revisors;

import java.util.Collection;

/**
 * Interface defining the edit behavior of a VComponent
 * 
 * @author David Bal
 *
 * @param <U> - calendar VComponent class
 */
public interface Reviser
{
    /** Revise calendar component based on properties set in subclasses
     *      */
    Collection<?> revise();

}
