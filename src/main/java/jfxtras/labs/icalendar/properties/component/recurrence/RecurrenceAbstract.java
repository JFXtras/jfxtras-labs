package jfxtras.labs.icalendar.properties.component.recurrence;

import java.time.DateTimeException;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

/** For EXDate and RDate
 * Stores either date or date-time values
 * @param <T> either EXDate or RDate
 * @see ExDate
 * @see RDate
 * */
public abstract class RecurrenceAbstract<T> implements Recurrence
{
    /**
     * EXDATE or RDATE: Set of dates or date/times included or excepted for recurring events, to-dos, journal entries.
     * 3.8.5.1, RFC 5545 iCalendar
     * Must be same Temporal type as dateTimeStart (DTSTART)
     */
    private Class<? extends Temporal> temporalClass;
    public ObservableSet<Temporal> getTemporals() { return vDateTimes; }
    private ObservableSet<Temporal> vDateTimes = FXCollections.observableSet(new HashSet<Temporal>());
    void setVDateTimes(Temporal...dateOrDateTime)
    {
        for (Temporal d : dateOrDateTime)
        {
            this.getTemporals().add(d);
        }
    }
    
    protected Stream<Temporal> getTemporalStream()
    {
        return getTemporals().stream();
    }

    /**
     * Allows initialization of LocalDate or LocalDate Temporal objects.
     * 
     * @param dateOrDateTime
     * @return
     */
    @SuppressWarnings("unchecked")
    public T withTemporals(Temporal... dateOrDateTime)
    {
        setVDateTimes(dateOrDateTime);
        return (T) this;
    }
    
    // CONSTRUCTORS
    public RecurrenceAbstract()
    {
        // Ensure Temporal class of newly added element matches earlier elements
        SetChangeListener<? super Temporal> listener = (SetChangeListener.Change<? extends Temporal> change) ->
        {
            System.out.println("recurrence change:");
            if (change.wasAdded())
            {
                Class<? extends Temporal> newTemporalClass = change.getElementAdded().getClass();
                if (temporalClass != null)
                {
//                    Class<? extends Temporal> originalTemporalClass = getTemporals().iterator().next().getClass();
                    System.out.println("classes:" + temporalClass + " " + newTemporalClass);
                    if (newTemporalClass != temporalClass)
                    {
                        getTemporals().remove(change.getElementAdded());
                        throw new DateTimeException("Added element " + change.getElementAdded() + " is wrong Temporal class:"
                                + newTemporalClass.getSimpleName() + ". Must match other elements of class:" + temporalClass.getSimpleName());
                    }
                } else
                {
                    temporalClass = newTemporalClass;
                }
            } else if (change.wasRemoved())
            {
                if (getTemporals().isEmpty())
                {
                    temporalClass = null; // when collection is emptied nulify temporalClass
                }
            }
        };

        vDateTimes.addListener(listener);
    }
    public RecurrenceAbstract(Temporal... dateOrDateTime)
    {
        this();
        withTemporals(dateOrDateTime);
    }

    /** Deep copy all fields from source to destination 
     * @param <U>*/
    private static <T> void copy(RecurrenceAbstract<T> source, RecurrenceAbstract<T> destination)
    {
        if (source.getTemporals() != null)
        {
            source.getTemporals().stream().forEach(r -> destination.getTemporals().add(r));
        }
    }
    
    /** Deep copy all fields from this to destination */
    public void copyTo(RecurrenceAbstract<T> destination)
    {
        copy(this, destination);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        @SuppressWarnings("unchecked")
        RecurrenceAbstract<T> testObj = (RecurrenceAbstract<T>) obj;

        if (getTemporals() == null) return testObj.getTemporals() == null;
        if (getTemporals().size() != testObj.getTemporals().size()) return false;
        
        // Sort both sets as lists and compare each element
        List<Temporal> l1 = new ArrayList<Temporal>(getTemporals());
        Collections.sort(l1, DateTimeUtilities.TEMPORAL_COMPARATOR);
        List<Temporal> l2 = new ArrayList<Temporal>(testObj.getTemporals());
        Collections.sort(l2, DateTimeUtilities.TEMPORAL_COMPARATOR);
        for (int i=0; i<l1.size(); i++)
        {
            if(! l1.get(i).equals(l2.get(i))) return false;
        }
        return true;
    }
    
    @Override
    public int hashCode()
    {
        return getTemporals().hashCode();
    }

    @Override
    public String toString()
    {
        String datesString = getTemporals()
                .stream()
                .sorted()
                .map(d -> DateTimeUtilities.temporalToString(d) + ",")
                .collect(Collectors.joining());
        return datesString.substring(0, datesString.length()-1); // remove last comma
    }

}
