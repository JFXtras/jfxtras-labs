package jfxtras.labs.icalendarfx.components;

public class StandardTime extends StandardOrSavingsBase<StandardTime>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.STANDARD;
    }
    
    /*
     * CONSTRUCTORS
     */
    public StandardTime() { }
    
    public StandardTime(String contentLines)
    {
        super(contentLines);
    }
}
