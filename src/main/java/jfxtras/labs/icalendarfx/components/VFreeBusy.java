package jfxtras.labs.icalendarfx.components;

/**
 * VFREEBUSY: RFC 5545 iCalendar 3.6.4. page 59
 * currently not supported - ZonedDateTime is providing time zone information
 * 
 * @author David Bal
 *
 */
public abstract class VFreeBusy<T> extends VComponentPersonalBase<T> implements VFreeBusyInt
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VFREEBUSY;
    }
    
    /*
     * CONSTRUCTORS
     */
    public VFreeBusy() { }
    
    public VFreeBusy(String contentLines)
    {
        super(contentLines);
    }
}
