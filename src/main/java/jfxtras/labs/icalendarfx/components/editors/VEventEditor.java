package jfxtras.labs.icalendarfx.components.editors;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/**
 * Revise VEvent behavior
 * 
 * @author David Bal
 *
 */
public class VEventEditor extends LocatableEditor<VEventEditor, VEvent>
{
    @Override
    public void adjustDateTime()
    {
        super.adjustDateTime();
        TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(getStartRecurrence(), getEndRecurrence());
        if (getVComponentEdited().getDuration() != null)
        {
            getVComponentEdited().setDuration(duration);
        } else if (getVComponentEdited().getDateTimeEnd() != null)
        {
            Temporal dtend = getVComponentEdited().getDateTimeStart().getValue().plus(duration);
            getVComponentEdited().setDateTimeEnd(dtend);
        } else
        {
            throw new RuntimeException("Either DTEND or DURATION must be set");
        }
    }
}
