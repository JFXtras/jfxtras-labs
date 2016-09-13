package jfxtras.labs.icalendarfx.itip;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VDisplayable;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExceptionDates;

/**
 * 3.2.5.  CANCEL

   The "CANCEL" method in a "VEVENT" calendar component is used to send
   a cancellation notice of an existing event request to the affected
   "Attendees".  The message is sent by the "Organizer" of the event.
   For a recurring event, either the whole event or instances of an
   event may be cancelled.  To cancel the complete range of a recurring
   event, the "UID" property value for the event MUST be specified and a
   "RECURRENCE-ID" MUST NOT be specified in the "CANCEL" method.  In
   order to cancel an individual instance of the event, the
   "RECURRENCE-ID" property value for the event MUST be specified in the
   "CANCEL" method.

   There are two options for canceling a sequence of instances of a
   recurring "VEVENT" calendar component:

   a.  The "RECURRENCE-ID" property for an instance in the sequence MUST
       be specified with the "RANGE" property parameter value of
       "THISANDFUTURE" to indicate cancellation of the specified
       "VEVENT" calendar component and all instances after.

   b.  Individual recurrence instances may be cancelled by specifying
       multiple "VEVENT" components each with a "RECURRENCE-ID" property
       corresponding to one of the instances to be cancelled.

   The "Organizer" MUST send a "CANCEL" message to each "Attendee"
   affected by the cancellation.  This can be done using a single
   "CANCEL" message for all "Attendees" or by using multiple messages
   with different subsets of the affected "Attendees" in each.

   When a "VEVENT" is cancelled, the "SEQUENCE" property value MUST be
   incremented as described in Section 2.1.4.

   This method type is an iCalendar object that conforms to the
   following property constraints:
<pre>
              +---------------------------------------------+
              | Constraints for a METHOD:CANCEL of a VEVENT |
              +---------------------------------------------+

   +--------------------+----------+-----------------------------------+
   | Component/Property | Presence | Comment                           |
   +--------------------+----------+-----------------------------------+
   | METHOD             | 1        | MUST be CANCEL.                   |
   |                    |          |                                   |
   | VEVENT             | 1+       | All must have the same UID.       |
   |   ATTENDEE         | 0+       | MUST include some or all          |
   |                    |          | Attendees being removed from the  |
   |                    |          | event.  MUST include some or all  |
   |                    |          | Attendees if the entire event is  |
   |                    |          | cancelled.                        |
   |   DTSTAMP          | 1        |                                   |
   |   ORGANIZER        | 1        |                                   |
   |   SEQUENCE         | 1        |                                   |
   |   UID              | 1        | MUST be the UID of the original   |
   |                    |          | REQUEST.                          |
   |   COMMENT          | 0+       |                                   |
   |   ATTACH           | 0+       |                                   |
   |   CATEGORIES       | 0+       |                                   |
   |   CLASS            | 0 or 1   |                                   |
   |   CONTACT          | 0+       |                                   |
   |   CREATED          | 0 or 1   |                                   |
   |   DESCRIPTION      | 0 or 1   |                                   |
   |   DTEND            | 0 or 1   | If present, DURATION MUST NOT be  |
   |                    |          | present.                          |
   |   DTSTART          | 0 or 1   |                                   |
   |   DURATION         | 0 or 1   | If present, DTEND MUST NOT be     |
   |                    |          | present.                          |
   |   EXDATE           | 0+       |                                   |
   |   GEO              | 0 or 1   |                                   |
   |   LAST-MODIFIED    | 0 or 1   |                                   |
   |   LOCATION         | 0 or 1   |                                   |
   |   PRIORITY         | 0 or 1   |                                   |
   |   RDATE            | 0+       |                                   |
   |   RECURRENCE-ID    | 0 or 1   | Only if referring to an instance  |
   |                    |          | of a recurring calendar           |
   |                    |          | component.  Otherwise, it MUST    |
   |                    |          | NOT be present.                   |
   |   RELATED-TO       | 0+       |                                   |
   |   RESOURCES        | 0+       |                                   |
   |   RRULE            | 0 or 1   |                                   |
   |   STATUS           | 0 or 1   | MUST be set to CANCELLED to       |
   |                    |          | cancel the entire event.  If      |
   |                    |          | uninviting specific Attendees,    |
   |                    |          | then MUST NOT be included.        |
   |   SUMMARY          | 0 or 1   |                                   |
   |   TRANSP           | 0 or 1   |                                   |
   |   URL              | 0 or 1   |                                   |
   |   IANA-PROPERTY    | 0+       |                                   |
   |   X-PROPERTY       | 0+       |                                   |
   |   REQUEST-STATUS   | 0        |                                   |
   |                    |          |                                   |
   |   VALARM           | 0        |                                   |
   |                    |          |                                   |
   | VTIMEZONE          | 0+       | MUST be present if any date/time  |
   |                    |          | refers to a timezone.             |
   |                    |          |                                   |
   | IANA-COMPONENT     | 0+       |                                   |
   | X-COMPONENT        | 0+       |                                   |
   |                    |          |                                   |
   | VTODO              | 0        |                                   |
   |                    |          |                                   |
   | VJOURNAL           | 0        |                                   |
   |                    |          |                                   |
   | VFREEBUSY          | 0        |                                   |
   +--------------------+----------+-----------------------------------+
   </pre>
 * 
 * @author David Bal
 *
 */
public class ProcessCancel implements Processable
{

    @Override
    public void process(VCalendar mainVCalendar, VCalendar inputVCalendar)
    {
        inputVCalendar.getAllVComponents().forEach(c ->
        {
            if (c instanceof VDisplayable)
            {
                VDisplayable<?> vDisplayable = ((VDisplayable<?>) c);
                String uid = vDisplayable.getUniqueIdentifier().getValue();
                Temporal recurrenceID = (vDisplayable.getRecurrenceId() != null) ? vDisplayable.getRecurrenceId().getValue() : null;
                // match RECURRENCE-ID (if present)
                if (mainVCalendar.uidComponentsMap().get(uid) != null)
                {
                    // if recurrence-id exists, and there is a matching vcomponent, then delete that vcomponent
                    // if recurrence-id exists without a matching vcomponent add EXDATE to parent
                    // if no recurrence date - delete all vcomponents
                    if (recurrenceID == null)
                    { // delete all related VComponents
                        List<VDisplayable<?>> relatedVComponents = mainVCalendar.uidComponentsMap().get(uid);
                        List<? extends VComponent> vComponents = mainVCalendar.getVComponents(c.getClass());
                        vComponents.removeAll(relatedVComponents);
                    } else
                    {
                        VDisplayable<?> matchingVComponent = mainVCalendar.uidComponentsMap().get(uid)
                                .stream()
                                .filter(v -> {
                                    Temporal mRecurrenceID = (v.getRecurrenceId() != null) ? v.getRecurrenceId().getValue() : null;
                                    return Objects.equals(recurrenceID, mRecurrenceID);
                                })
                                .findAny()
                                .orElseGet(() -> null);    
                        if (matchingVComponent != null)
                        { // delete one recurrence
                            List<? extends VComponent> vComponents = mainVCalendar.getVComponents(c.getClass());
                            vComponents.remove(matchingVComponent);
//                            mainVCalendar.removeVComponent(matchingVComponent);
                        } else
                        { // add recurrence as EXDATE to main
                            VDisplayable<?> parentVComponent = mainVCalendar.uidComponentsMap().get(uid).get(0);
                            final ObservableList<ExceptionDates> exceptions;
                            if (parentVComponent.getExceptionDates() == null)
                            {
                                exceptions = FXCollections.observableArrayList();
                            } else
                            {
                                exceptions = parentVComponent.getExceptionDates();
                            }
                            exceptions.add(new ExceptionDates(recurrenceID));
                            int oldSequence = (parentVComponent.getSequence() == null) ? 0 : parentVComponent.getSequence().getValue();
                            parentVComponent.setSequence(++oldSequence);
                        }
                    }
                        
                       
                    

                }
            } else
            { // non-displayable VComponents (only VFREEBUSY has UID)
                // TODO
                throw new RuntimeException("not implemented");
            }
        });
    }

}
