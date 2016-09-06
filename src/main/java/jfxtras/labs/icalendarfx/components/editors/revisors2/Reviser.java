package jfxtras.labs.icalendarfx.components.editors.revisors2;

/**
 * Interface for the edit behavior of a VComponent
 * 
 * <p>Reviser options include:
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
public interface Reviser
{
    /** Revise a calendar component.  Returns the result of the revision */
    Object revise();

}
