package jfxtras.labs.icalendarfx.components.editors.deleters;

/**
 * Interface for the delete behavior of a VComponent
 * 
 * <p>Delete options include:
 * <ul>
 * <li>One
 * <li>All
 * <li>This-and-Future
 * </ul>
 * </p>
 * 
 * @author David Bal
 *
 */
public interface Deleter
{
    /** Executes the delete algorithms and returns the resulting VComponent, if any. 
     * Returns null if ALL recurrences are deleted */
    Object delete();
}
