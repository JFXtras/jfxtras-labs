package jfxtras.labs.icalendarfx.itip;

import java.time.temporal.Temporal;
import java.util.Objects;

import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VDisplayableBase;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.calendar.Method.MethodType;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.labs.icalendarfx.properties.component.relationship.Organizer;

/** 
 * 
 * <h2>3.2.1.  PUBLISH</h2>

   <p>The {@link MethodType#Publish PUBLISH} method in a {@link VEvent VEVENT} calendar component is an
   unsolicited posting of an iCalendar object.  Any CU may add published
   components to their calendar.  The {@link Organizer} MUST be present in a
   published iCalendar component.  {@link Attendee Attendees} MUST NOT be present.  Its
   expected usage is for encapsulating an arbitrary event as an
   iCalendar object.  The {@link Organizer} may subsequently update (with
   another {@link MethodType#Publish PUBLISH} method), add instances to (with an {@link MethodType#Add ADD} method),
   or cancel (with a {@link MethodType#Cancel CANCEL} method) a previously published {@link VEvent VEVENT}
   calendar component.</p>

   <p>This method type is an iCalendar object that conforms to the
   following property constraints:</p>
<pre>
             +----------------------------------------------+
             | Constraints for a METHOD:PUBLISH of a VEVENT |
             +----------------------------------------------+

   +--------------------+----------+-----------------------------------+
   | Component/Property | Presence | Comment                           |
   +--------------------+----------+-----------------------------------+
   | METHOD             | 1        | MUST equal PUBLISH.               |
   |                    |          |                                   |
   | VEVENT             | 1+       |                                   |
   |   DTSTAMP          | 1        |                                   |
   |   DTSTART          | 1        |                                   |
   |   ORGANIZER        | 1        |                                   |
   |   SUMMARY          | 1        | Can be null.                      |
   |   UID              | 1        |                                   |
   |   RECURRENCE-ID    | 0 or 1   | Only if referring to an instance  |
   |                    |          | of a recurring calendar           |
   |                    |          | component.  Otherwise, it MUST    |
   |                    |          | NOT be present.                   |
   |   SEQUENCE         | 0 or 1   | MUST be present if value is       |
   |                    |          | greater than 0; MAY be present if |
   |                    |          | 0.                                |
   |   ATTACH           | 0+       |                                   |
   |   CATEGORIES       | 0+       |                                   |
   |   CLASS            | 0 or 1   |                                   |
   |   COMMENT          | 0+       |                                   |
   |   CONTACT          | 0 or 1   |                                   |
   |   CREATED          | 0 or 1   |                                   |
   |   DESCRIPTION      | 0 or 1   | Can be null.                      |
   |   DTEND            | 0 or 1   | If present, DURATION MUST NOT be  |
   |                    |          | present.                          |
   |   DURATION         | 0 or 1   | If present, DTEND MUST NOT be     |
   |                    |          | present.                          |
   |   EXDATE           | 0+       |                                   |
   |   GEO              | 0 or 1   |                                   |
   |   LAST-MODIFIED    | 0 or 1   |                                   |
   |   LOCATION         | 0 or 1   |                                   |
   |   PRIORITY         | 0 or 1   |                                   |
   |   RDATE            | 0+       |                                   |
   |   RELATED-TO       | 0+       |                                   |
   |   RESOURCES        | 0+       |                                   |
   |   RRULE            | 0 or 1   |                                   |
   |   STATUS           | 0 or 1   | MAY be one of                     |
   |                    |          | TENTATIVE/CONFIRMED/CANCELLED.    |
   |   TRANSP           | 0 or 1   |                                   |
   |   URL              | 0 or 1   |                                   |
   |   IANA-PROPERTY    | 0+       |                                   |
   |   X-PROPERTY       | 0+       |                                   |
   |   ATTENDEE         | 0        |                                   |
   |   REQUEST-STATUS   | 0        |                                   |
   |                    |          |                                   |
   |   VALARM           | 0+       |                                   |
   |                    |          |                                   |
   | VFREEBUSY          | 0        |                                   |
   |                    |          |                                   |
   | VJOURNAL           | 0        |                                   |
   |                    |          |                                   |
   | VTODO              | 0        |                                   |
   |                    |          |                                   |
   | VTIMEZONE          | 0+       | MUST be present if any date/time  |
   |                    |          | refers to a timezone.             |
   |                    |          |                                   |
   | IANA-COMPONENT     | 0+       |                                   |
   | X-COMPONENT        | 0+       |                                   |
   +--------------------+----------+-----------------------------------+
   </pre>
 * @author David Bal   
 */
public class ProcessPublish implements Processable
{
    @Override
    public void process(VCalendar mainVCalendar, VCalendar inputVCalendar)
    {
        inputVCalendar.getAllVComponents().forEach(c ->
        {
            if (c instanceof VDisplayableBase)
            {
                VDisplayableBase<?> vDisplayable = ((VDisplayableBase<?>) c);
                
                final boolean hasOrganizer = vDisplayable.getOrganizer() != null;
                if (! hasOrganizer) throw new IllegalArgumentException("Can't process PUBLISH method for VCALENDAR: ORGANIZER MUST be present, and it's absent");
                final boolean hasNoAttendees = vDisplayable.getAttendees() == null;
                if (! hasNoAttendees) throw new IllegalArgumentException("Can't process PUBLISH method for VCALENDAR: ATTENDEE property MUST NOT be present, and it's exists");
                final int newSequence = (vDisplayable.getSequence() == null) ? 0 : vDisplayable.getSequence().getValue();
                boolean isNewSequenceHigher = true;
                
                String uid = vDisplayable.getUniqueIdentifier().getValue();
                Temporal recurrenceID = (vDisplayable.getRecurrenceId() != null) ? vDisplayable.getRecurrenceId().getValue() : null;
                // match RECURRENCE-ID (if present)
                if (mainVCalendar.uidComponentsMap().get(uid) != null)
                {
                    /* if new has recurrence id:
                     *      if old has matching recurrence-id then replace it
                     *      if old doesn't have recurrence-id, add component as new recurrence
                     * if new doesn't have recurrence id, but match exists, replace match
                     * If no match then just add - can't have recurrence id 
                     */
                    VDisplayableBase<?> matchingMainVComponent = mainVCalendar.uidComponentsMap().get(uid)
                            .stream()
                            .filter(v -> {
                                Temporal mRecurrenceID = (v.getRecurrenceId() != null) ? v.getRecurrenceId().getValue() : null;
                                return Objects.equals(recurrenceID, mRecurrenceID);
                            })
                            .findAny()
                            .orElseGet(() -> null);
                    
                    if (matchingMainVComponent != null)
                    {
                        int oldSequence = (matchingMainVComponent.getSequence() == null) ? 0 : matchingMainVComponent.getSequence().getValue();
                        isNewSequenceHigher = newSequence > oldSequence || (newSequence == 0 && oldSequence == 0);
                        // SEQUENCE INCREASE ONLY APPLIES FOR PARENTS - NOT RECURRENCES THAT ONLY HAVE RECURRENCE-ID CHANGED
                        if (! isNewSequenceHigher) throw new IllegalArgumentException("Can't process PUBLISH method for VCALENDAR: SEQUENCY property MUST be higher than previously published component (new=" + newSequence + " old=" + oldSequence + ")");
                        if (isNewSequenceHigher)
                        {
                            mainVCalendar.getVComponents(matchingMainVComponent.getClass()).remove(matchingMainVComponent); // remove old VComponent because we're replacing
                        }                        
                    }
                }
                if (hasOrganizer && hasNoAttendees && isNewSequenceHigher)
                {
                    mainVCalendar.addVComponent(c); // add published VComponent
                }
            } else
            { // non-displayable VComponents (only VFREEBUSY has UID)
                // TODO
                throw new RuntimeException("not implemented");
            }
            mainVCalendar.addVComponent(c);
        });
    }

}
