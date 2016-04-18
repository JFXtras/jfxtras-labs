package jfxtras.labs.icalendarfx.components;

public class VComponentTest3 extends VComponentDescribableBase<VComponentTest3>
{

    /*
     * CONSTRUCTORS
     */
    public VComponentTest3() { }
    
    public VComponentTest3(String contentLines)
    {
        super(contentLines);
    }
    
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.STANDARD; // for testing
    }
}
