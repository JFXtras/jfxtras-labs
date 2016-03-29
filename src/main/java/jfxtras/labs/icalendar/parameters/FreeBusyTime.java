package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.FreeBusyTime.FreeBusyTimeType;

/**
 * FBTYPE
 * Free/Busy Time Type
 * RFC 5545, 3.2.9, page 20
 * 
 * To specify the free or busy time type.
 * 
 * Example:
 * FREEBUSY;FBTYPE=BUSY:19980415T133000Z/19980415T170000Z
 * 
 * @author David Bal
 *
 */
public class FreeBusyTime extends ParameterBase<FreeBusyTime, FreeBusyTimeType>
{
    public FreeBusyTime()
    {
        super();
    }
  
    public FreeBusyTime(String content)
    {
        super(content);
    }

    public FreeBusyTime(FreeBusyTime source)
    {
        super(source);
    }
    
    public enum FreeBusyTimeType
    {
        FREE ("FREE"), // the time interval is free for scheduling
        BUSY ("BUSY"), // the time interval is busy because one or more events have been scheduled for that interval - THE DEFAULT
        BUSY_UNAVAILABLE ("BUSY-UNAVAILABLE"), // the time interval is busy and that the interval can not be scheduled
        BUSY_TENTATIVE ("BUSY-TENTATIVE"); // the time interval is busy because one or more events have been tentatively scheduled for that interval
        
        private String name;
        @Override public String toString() { return name; }
        FreeBusyTimeType(String name)
        {
            this.name = name;
        }
    }
}
