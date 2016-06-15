package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentRepeatable;

public interface VComponentRepeatableStore<V extends VComponentRepeatable<V>, R> extends VComponentStore<V, R>
{
    @Override
    default List<R> makeRecurrences(V vComponent)
    {
        if ((startRange == null) || (endRange == null))
        {
            throw new DateTimeException("Both startRange and endRange must not be null (" + startRange + ", " + endRange + ")");
        }
        List<R> newRecurrences = new ArrayList<>();
        Boolean isWholeDay = vComponent.getDateTimeStart().getValue() instanceof LocalDate;
        
        // Make start and end ranges in Temporal type that matches DTSTART
        final Temporal startRange2;
        final Temporal endRange2;
        if (isWholeDay)
        {
            startRange2 = LocalDate.from(startRange);
            endRange2 = LocalDate.from(endRange);            
        } else
        {
            startRange2 = vComponent.getDateTimeStart().getValue().with(startRange);
            endRange2 = vComponent.getDateTimeStart().getValue().with(endRange);            
        }
        vComponent.streamRecurrences(startRange2, endRange2)
            .forEach(startTemporal -> 
            {
                R recurrence = recurrenceCallBack.call(vComponentEditedCopy, startTemporal);
//                recurrenceVComponentMap.put(System.identityHashCode(recurrence), vComponentEditedCopy);
                newRecurrences.add(recurrence);
            });
        
}
