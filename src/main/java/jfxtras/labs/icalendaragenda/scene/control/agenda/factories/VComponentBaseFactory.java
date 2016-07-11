package jfxtras.labs.icalendaragenda.scene.control.agenda.factories;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;

/**
 * Base store to create VComponentDisplayable objects from a recurrence and recurrences 
 * from VComponentDisplayable objects.
 * 
 * @author David Bal
 *
 * @param <R> - type of recurrence
 */
@Deprecated
public abstract class VComponentBaseFactory<R> implements VComponentFactoryOld<R>
{
    private ObjectProperty<LocalDateTime> startRange = new SimpleObjectProperty<LocalDateTime>(); // must be updated when range changes
    @Override public ObjectProperty<LocalDateTime> startRangeProperty() { return startRange; }
    @Override public void setStartRange(LocalDateTime startRange) { this.startRange.set(startRange); }
    @Override public LocalDateTime getStartRange() { return startRange.get(); }

    private ObjectProperty<LocalDateTime> endRange = new SimpleObjectProperty<LocalDateTime>(); // must be updated when range changes
    @Override public ObjectProperty<LocalDateTime> endRangeProperty() { return endRange; }
    @Override public void setEndRange(LocalDateTime endRange) { this.endRange.set(endRange); }
    @Override public LocalDateTime getEndRange() { return endRange.get(); }
    
    abstract CallbackTwoParameters<VComponentDisplayable<?>, Temporal, R> recurrenceCallBack();
    
    @Override
    public List<R> makeRecurrences(VComponentDisplayable<?> vComponent)
    {
        if ((getStartRange() == null) || (getEndRange() == null))
        {
            throw new DateTimeException("Both startRange and endRange MUST NOT be null (" + startRange + ", " + endRange + ")");
        }
        List<R> newRecurrences = new ArrayList<>();
        Boolean isWholeDay = vComponent.getDateTimeStart().getValue() instanceof LocalDate;
        
        // Make start and end ranges in Temporal type that matches DTSTART
        final Temporal startRange2;
        final Temporal endRange2;
        if (isWholeDay)
        {
            startRange2 = LocalDate.from(getStartRange());
            endRange2 = LocalDate.from(getEndRange());            
        } else
        {
            startRange2 = vComponent.getDateTimeStart().getValue().with(getStartRange());
            endRange2 = vComponent.getDateTimeStart().getValue().with(getEndRange());            
        }
        // make recurrences
        vComponent.streamRecurrences(startRange2, endRange2)
            .forEach(startTemporal -> 
            {
                R recurrence = recurrenceCallBack().call(vComponent, startTemporal);
                newRecurrences.add(recurrence);
            });
        return newRecurrences;
    }
    
    /** Based on {@link Callback<P,R>} */
    @FunctionalInterface
    interface CallbackTwoParameters<P1, P2, R> {
        /**
         * The <code>call</code> method is called when required, and is given 
         * two arguments of type P1 and P2, with a requirement that an object of type R
         * is returned.
         *
         * @param param1 The first argument upon which the returned value should be
         *      determined.
         * @param param1 The second argument upon which the returned value should be
         *      determined.
         * @return An object of type R that may be determined based on the provided
         *      parameter value.
         */
        public R call(P1 param1, P2 param2);
    }
        
}
