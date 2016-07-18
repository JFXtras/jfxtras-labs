package jfxtras.labs.icalendarfx.components.revisors;

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
    void revise();

}
