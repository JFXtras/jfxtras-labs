package jfxtras.labs.icalendar.components;

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
public class VTimeZone
{
    public VTimeZone() { throw new RuntimeException("not supported"); }
}
