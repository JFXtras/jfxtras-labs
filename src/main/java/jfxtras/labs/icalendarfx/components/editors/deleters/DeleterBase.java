package jfxtras.labs.icalendarfx.components.editors.deleters;

/**
 * Abstract class that overrides return type from Object to generic type U
 * 
 * @author David Bal
 *
 */
public abstract class DeleterBase<U> implements Deleter
{
    @Override
    public abstract U delete();

}
