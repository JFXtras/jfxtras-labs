package jfxtras.labs.icalendarfx.components;

import jfxtras.labs.icalendarfx.components.revisors.Revisable;
import jfxtras.labs.icalendarfx.components.revisors.ReviserDaylightSavingTime;

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

    public DaylightSavingTime(DaylightSavingTime source)
    {
        super(source);
    }
    
    @Override
    public Revisable newRevisor() { return new ReviserDaylightSavingTime(this); }

    /** Parse content lines into calendar component object */
    public static DaylightSavingTime parse(String contentLines)
    {
        DaylightSavingTime component = new DaylightSavingTime();
        component.parseContent(contentLines);
        return component;
    }
}
