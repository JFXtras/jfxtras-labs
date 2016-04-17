package jfxtras.labs.icalendarfx.components;

public class VComponentTest extends VComponentPersonalBase<VComponentTest>
{
    /*
     * CONSTRUCTORS
     */
    public VComponentTest() { }
    
    public VComponentTest(String contentLines)
    {
        super(contentLines);
    }
    
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VEVENT; // for testing
    }
}
