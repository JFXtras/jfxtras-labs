package jfxtras.labs.icalendarfx.components.deleters;

/**
 * Interface containing the delete behavior of a VComponent
 * 
 * @author David Bal
 *
 */
public abstract class DeleterBase<U> implements Deleter
{
//    /** Executes delete algorithm */
//    public abstract boolean delete();

    /** Executes delete algorithm */
    @Override
    public abstract U delete();

}
