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
     * Returns component(s) {@code List<T>} that exist after revision
     *      */
    Object revise();

}
