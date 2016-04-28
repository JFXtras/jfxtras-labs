package jfxtras.labs.icalendarfx.components;

// both this class and Standard are identical - need to extend common class
public class DaylightSavingTime extends StandardOrDaylightBase<DaylightSavingTime>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.DAYLIGHT;
    }
    
    /*
     * CONSTRUCTORS
     */
    public DaylightSavingTime() { }
    
    public DaylightSavingTime(String contentLines)
    {
        super(contentLines);
    }

    /** Parse content lines into calendar component object */
    public static DaylightSavingTime parse(String contentLines)
    {
        DaylightSavingTime component = new DaylightSavingTime();
        component.parseContent(contentLines);
        return component;
    }
}
