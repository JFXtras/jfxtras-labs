package jfxtras.labs.icalendarfx.components;

// both this class and Standard are identical - need to extend common class
public class DaylightSavingTime extends StandardOrDaylightBase<DaylightSavingTime>
{
    @Override
    public CalendarElement componentType()
    {
        return CalendarElement.DAYLIGHT_SAVING_TIME;
    }
    
    /*
     * CONSTRUCTORS
     */
    public DaylightSavingTime() { super(); }
    
    public DaylightSavingTime(String contentLines)
    {
        super(contentLines);
    }

    public DaylightSavingTime(DaylightSavingTime a)
    {
        // TODO Auto-generated constructor stub
    }

    /** Parse content lines into calendar component object */
    public static DaylightSavingTime parse(String contentLines)
    {
        DaylightSavingTime component = new DaylightSavingTime();
        component.parseContent(contentLines);
        return component;
    }
}
