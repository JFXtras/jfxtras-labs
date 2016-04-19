package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.time.FreeBusyTime;

public interface VFreeBusyInt extends VComponentDateTimeEnd
{
    /**
     * FREEBUSY
     * Free/Busy Time
     * RFC 5545, 3.8.2.6, page 100
     * 
     * This property defines one or more free or busy time intervals.
     * 
     * These time periods can be specified as either a start
     * and end DATE-TIME or a start DATE-TIME and DURATION.  The date and
     * time MUST be a UTC time format.  Internally, the values are stored only as 
     * start DATE-TIME and DURATION.  Any values entered as start and end as both
     * DATE-TIME are converted to the start DATE-TIME and DURATION.
     * 
     * Examples:
     * FREEBUSY;FBTYPE=BUSY-UNAVAILABLE:19970308T160000Z/PT8H30M
     * FREEBUSY;FBTYPE=FREE:19970308T160000Z/PT3H,19970308T200000Z/PT1H
     *  ,19970308T230000Z/19970309T000000Z
     * 
     * Note: The above example is converted and outputed as the following:
     * FREEBUSY;FBTYPE=FREE:19970308T160000Z/PT3H,19970308T200000Z/PT1H
     *  ,19970308T230000Z/PT1H
     */
    ObservableList<FreeBusyTime> getFreeBusy();
    void setFreeBusy(ObservableList<FreeBusyTime> properties);

    
    /**
     * CONTACT
     * RFC 5545 iCalendar 3.8.4.2. page 109
     * 
     * This property is used to represent contact information or
     * alternately a reference to contact information associated with the
     * calendar component.
     * 
     * Example:
     * CONTACT:Jim Dolittle\, ABC Industries\, +1-919-555-1234
     */
    String getContact();
    StringProperty contactProperty();
    void setContact(String contact);
}
