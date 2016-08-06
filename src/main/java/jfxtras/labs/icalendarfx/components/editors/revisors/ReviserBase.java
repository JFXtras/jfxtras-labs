package jfxtras.labs.icalendarfx.components.editors.revisors;

import java.util.List;

/**
 * Abstract class that overrides return type from Object to generic type U
 * 
 * @author David Bal
 *
 */
public abstract class ReviserBase<U> implements Reviser
{
    /** 
     * Revise a calendar component.  Returns a list of component(s) that exist after revision.  If
     * no components exist then an empty list if returned.
     */
    @Override
    public abstract List<U> revise();
}
