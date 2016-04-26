package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.properties.component.time.FreeBusyTime;

/**
 * VFREEBUSY
 * Free/Busy Component
 * RFC 5545 iCalendar 3.6.4. page 59
 * 
A "VFREEBUSY" calendar component is a grouping of
      component properties that represents either a request for free or
      busy time information, a reply to a request for free or busy time
      information, or a published set of busy time information.

      When used to request free/busy time information, the "ATTENDEE"
      property specifies the calendar users whose free/busy time is
      being requested; the "ORGANIZER" property specifies the calendar
      user who is requesting the free/busy time; the "DTSTART" and
      "DTEND" properties specify the window of time for which the free/
      busy time is being requested; the "UID" and "DTSTAMP" properties
      are specified to assist in proper sequencing of multiple free/busy
      time requests.

      When used to reply to a request for free/busy time, the "ATTENDEE"
      property specifies the calendar user responding to the free/busy
      time request; the "ORGANIZER" property specifies the calendar user
      that originally requested the free/busy time; the "FREEBUSY"
      property specifies the free/busy time information (if it exists);
      and the "UID" and "DTSTAMP" properties are specified to assist in
      proper sequencing of multiple free/busy time replies.

      When used to publish busy time, the "ORGANIZER" property specifies
      the calendar user associated with the published busy time; the
      "DTSTART" and "DTEND" properties specify an inclusive time window
      that surrounds the busy time information; the "FREEBUSY" property
      specifies the published busy time information; and the "DTSTAMP"
      property specifies the DATE-TIME that iCalendar object was
      created.

      The "VFREEBUSY" calendar component cannot be nested within another
      calendar component.  Multiple "VFREEBUSY" calendar components can
      be specified within an iCalendar object.  This permits the
      grouping of free/busy information into logical collections, such
      as monthly groups of busy time information.

      The "VFREEBUSY" calendar component is intended for use in
      iCalendar object methods involving requests for free time,
      requests for busy time, requests for both free and busy, and the
      associated replies.

      Free/Busy information is represented with the "FREEBUSY" property.
      This property provides a terse representation of time periods.
      One or more "FREEBUSY" properties can be specified in the
      "VFREEBUSY" calendar component.

      When present in a "VFREEBUSY" calendar component, the "DTSTART"
      and "DTEND" properties SHOULD be specified prior to any "FREEBUSY"
      properties.

      The recurrence properties ("RRULE", "RDATE", "EXDATE") are not
      permitted within a "VFREEBUSY" calendar component.  Any recurring
      events are resolved into their individual busy time periods using
      the "FREEBUSY" property.

   Example:  The following is an example of a "VFREEBUSY" calendar
      component used to request free or busy time information:

       BEGIN:VFREEBUSY
       UID:19970901T082949Z-FA43EF@example.com
       ORGANIZER:mailto:jane_doe@example.com
       ATTENDEE:mailto:john_public@example.com
       DTSTART:19971015T050000Z
       DTEND:19971016T050000Z
       DTSTAMP:19970901T083000Z
       END:VFREEBUSY
 * 
 * @author David Bal
 *
 */
public class VFreeBusy extends VComponentPersonalBase<VFreeBusy> implements VFreeBusyInt<VFreeBusy>, VComponentDateTimeEnd<VFreeBusy>
{
    @Override
    public VComponentEnum componentType()
    {
        return VComponentEnum.VFREEBUSY;
    }
    
    /**
     * DTEND
     * Date-Time End (for local-date)
     * RFC 5545, 3.8.2.2, page 95
     * 
     * This property specifies when the calendar component ends.
     * 
     * The value type of this property MUST be the same as the "DTSTART" property, and
     * its value MUST be later in time than the value of the "DTSTART" property.
     * 
     * Example:
     * DTEND;VALUE=DATE:19980704
     */
    @Override public ObjectProperty<DateTimeEnd<? extends Temporal>> dateTimeEndProperty()
    {
        if (dateTimeEnd == null)
        {
            dateTimeEnd = new SimpleObjectProperty<>(this, PropertyEnum.DATE_TIME_END.toString());
        }
        return dateTimeEnd;
    }
    private ObjectProperty<DateTimeEnd<? extends Temporal>> dateTimeEnd;
    
    /*
     * CONSTRUCTORS
     */
    public VFreeBusy() { }
    
    public VFreeBusy(String contentLines)
    {
        super(contentLines);
    }
    
    @Override
    public void checkDateTimeStartConsistency()
    {
//        VComponentDateTimeEnd.super.checkDateTimeEndConsistency();
    }

    @Override
    public ObservableList<FreeBusyTime> getFreeBusy()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setFreeBusy(ObservableList<FreeBusyTime> properties)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String getContact()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public StringProperty contactProperty()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setContact(String contact)
    {
        // TODO Auto-generated method stub
        
    }
}
