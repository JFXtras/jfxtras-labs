package jfxtras.labs.icalendarfx.components;

import jfxtras.labs.icalendarfx.components.revisors.DaylightSavingTimeReviser;
import jfxtras.labs.icalendarfx.components.revisors.Revisable;

// both this class and Standard are identical - need to extend common class
public class DaylightSavingTime extends StandardOrDaylightBase<DaylightSavingTime>
{
    @Override
    public CalendarElementType componentType()
    {
        return CalendarElementType.DAYLIGHT_SAVING_TIME;
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
    
    @Override
    public Revisable<DaylightSavingTimeReviser, DaylightSavingTime> newRevisor() { return new DaylightSavingTimeReviser(this); }

    /** Parse content lines into calendar component object */
    public static DaylightSavingTime parse(String contentLines)
    {
        DaylightSavingTime component = new DaylightSavingTime();
        component.parseContent(contentLines);
        return component;
    }
}
