package jfxtras.labs.icalendarfx.components.deleters;

import java.util.Collection;

public interface Deleter
{
    /** Delete component based on properties set in subclasses
     * Can return zero or one.  Returns one if changes are applied to ONE or THIS-AND-FUTURE,
     * zero for ALL.
     * 
     * @return - Collection of revised component after deletion, or empty collection
     */
    Collection<?> delete();
}
