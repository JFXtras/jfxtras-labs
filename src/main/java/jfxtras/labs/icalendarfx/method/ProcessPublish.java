package jfxtras.labs.icalendarfx.method;

import java.time.temporal.Temporal;
import java.util.Objects;

import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VDisplayableBase;

public class ProcessPublish implements Processable
{

    @Override
    public VCalendar process(VCalendar main, VCalendar input)
    {
        input.getAllVComponents().forEach(c ->
        {
            if (c instanceof VDisplayableBase)
            {
                /* The "Organizer" MUST be present in a
                published iCalendar component.  "Attendees" MUST NOT be present. 
                
                 The "UID" property is used by the client to identify the event.  The
   "SEQUENCE" property indicates that this is a change to the event.
   The event with a matching "UID" and sequence number 0 is superseded
   by this event.

   The "SEQUENCE" property provides a reliable way to distinguish
   different versions of the same event.  Each time an event is
   published, its sequence number is incremented.  If a client receives
   an event with a sequence number 5 and finds it has the same event
   with sequence number 2, the event SHOULD be updated.  However, if the
   client received an event with sequence number 2 and finds it already
   has sequence number 5 of the same event, the event MUST NOT be
   updated.
                */
                VDisplayableBase<?> vDisplayable = ((VDisplayableBase<?>) c);
                String uid = vDisplayable.getUniqueIdentifier().getValue();
                Temporal recurrenceID = (vDisplayable.getRecurrenceId() != null) ? vDisplayable.getRecurrenceId().getValue() : null;
                // match RECURRENCE-ID (if present)
                VDisplayableBase<?> matchingMainVComponent = main.uidComponentsMap().get(uid)
                        .stream()
                        .filter(v -> {
                            Temporal mRecurrenceID = (v.getRecurrenceId() != null) ? v.getRecurrenceId().getValue() : null;
                            return Objects.equals(recurrenceID, mRecurrenceID);
                        })
                        .findAny()
                        .orElseGet(() -> null);
                main.getVComponents(matchingMainVComponent.getClass()).remove(matchingMainVComponent); // remove old VComponent
                main.addVComponent(c); // add published VComponent
            } else
            { // non-displayable VComponents (only VFREEBUSY has UID)
                
            }
            main.addVComponent(c);
        });
        return main;
        // extract VComponents in input
        // try to find matching UID
        // if SEQUENCE if same or higher replace old component (error if lower)
        // if no match add component
    }

}
