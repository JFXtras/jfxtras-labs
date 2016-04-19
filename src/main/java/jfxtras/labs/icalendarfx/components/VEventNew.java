package jfxtras.labs.icalendarfx.components;

public abstract class VEventNew<T, I> extends VComponentLocatableBase<T, I> implements VEventNewInt<T,I>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VEVENT;
    }
    
    /*
     * CONSTRUCTORS
     */
    public VEventNew() { }
    
    public VEventNew(String contentLines)
    {
        super(contentLines);
    }
}
