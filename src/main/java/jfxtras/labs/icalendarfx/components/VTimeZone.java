package jfxtras.labs.icalendarfx.components;

/**
 * VTIMEZONE: RFC 5545 iCalendar 3.6.5. page 62
 * currently not supported - ZonedDateTime is providing time zone information
 * 
 * While the ZoneId class has the IANA Time Zone Database (TZDB) so no other
 * time zone information is needed.
 * However, it can be important to output VTimeZone components to communicate with other
 * applications.  Therefore, this component may be implemented in the future.
 * 
 * @author David Bal
 *
 */
public abstract class VTimeZone<T> extends VComponentBase<T> implements VTimeZoneInt
{
    //  NEED TO OVERRIDE toContentLines, AND MAYBE OTHER METHODS, TO ACCOMODIATE THE LIST
    // OF StandardOrSavings, BECAUSE THOSE ARE NOT PROPERTIES, BUT COMPONENTS THEMSELVES.
    
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VTIMEZONE;
    }
    
    /*
     * CONSTRUCTORS
     */
    public VTimeZone() { }
    
    public VTimeZone(String contentLines)
    {
        super(contentLines);
    }
}
