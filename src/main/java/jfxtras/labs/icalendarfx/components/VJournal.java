package jfxtras.labs.icalendarfx.components;

public abstract class VJournal<T, I> extends VComponentDisplayableBase<T, I> implements VJournalInt
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VJOURNAL;
    }
    
    /*
     * CONSTRUCTORS
     */
    public VJournal() { }
    
    public VJournal(String contentLines)
    {
        super(contentLines);
    }
}
