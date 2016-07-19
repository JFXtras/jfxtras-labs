package jfxtras.labs.icalendarfx.components.revisors;

/**
 * Interface containing the edit behavior of a VComponent
 * 
 * @author David Bal
 *
 */
public interface Reviser
{
    /** Revise calendar component based on properties set in subclasses.
     * Returns true if component was revised (cancel returns false)
     *      */
    boolean revise();

}
