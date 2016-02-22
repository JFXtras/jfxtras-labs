package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

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

/** For EXDate and RDate
 * Stores either date or date-time values
 * @param <T> either EXDate or RDate
 * @see ExDate
 * @see RDate
 * */
public abstract class RecurrenceComponentAbstract<T> implements RecurrenceComponent
{
    /**
     * EXDATE or RDATE: Set of dates or date/times included or excepted for recurring events, to-dos, journal entries.
     * 3.8.5.1, RFC 5545 iCalendar
     * Must be same Temporal type as dateTimeStart (DTSTART)
     */
    public ObservableSet<Temporal> getTemporals() { return vDateTimes; }
//    private Set<Temporal> vDateTimes = new HashSet<Temporal>();
//    public void setTemporals(Set<Temporal> temporals) { vDateTimes = temporals; }
    private ObservableSet<Temporal> vDateTimes = FXCollections.observableSet(new HashSet<Temporal>());
    void setVDateTimes(Temporal...dateOrDateTime)
    {
        for (Temporal d : dateOrDateTime)
        {
            this.getTemporals().add(d);
        }
        if (dateOrDateTime.length > 0) temporalClass(); // test class sameness
        Class<? extends Temporal> firstClass = dateOrDateTime[0].getClass();
        checkTemporalTypes(firstClass);
    }

    /** Class of encapsulated Temporal objects - LocalDate, LocalDateTime or ZonedDateTime */
    protected Class<? extends Temporal> temporalClass()
    {
        if (getTemporals().size() > 0)
        {
            Class<? extends Temporal> clazz = getTemporals().iterator().next().getClass();
            checkTemporalTypes(clazz);
            return clazz;                
        } else return null;
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
    
    /* checks if all Temporal objects in vDateTimes are the same */
    @Deprecated // obsolete because of new checking
    private void checkTemporalTypes(Class<? extends Temporal> clazz)
    {
        boolean same =  getTemporals()
                .stream()
                .allMatch(v -> v.getClass().equals(clazz));
        if (! same) throw new IllegalArgumentException("Not all Temporal objects in VDateTime class of type:" + clazz.getSimpleName());
    }
    
    // CONSTRUCTORS
    public RecurrenceComponentAbstract()
    {
        SetChangeListener<? super Temporal> listener = (SetChangeListener.Change<? extends Temporal> change) ->
        {
            if (change.wasAdded())
            {
                Temporal newTemporal = change.getElementAdded();
                
//                if (change.wasAdded())
//                {
//                    
//                }
            }
        };

        vDateTimes.addListener(listener);
    }
    public RecurrenceComponentAbstract(Temporal... dateOrDateTime)
    {
        this();
        withTemporals(dateOrDateTime);
    }
//    public RecurrenceComponentAbstract(LocalDate... date) { withDates(date); }

    /** Deep copy all fields from source to destination 
     * @param <U>*/
    private static <T> void copy(RecurrenceComponentAbstract<T> source, RecurrenceComponentAbstract<T> destination)
    {
        if (source.getTemporals() != null)
        {
            source.getTemporals().stream().forEach(r -> destination.getTemporals().add(r));
        }
    }
    
    /** Deep copy all fields from this to destination */
    public void copyTo(RecurrenceComponentAbstract<T> destination)
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
        RecurrenceComponentAbstract<T> testObj = (RecurrenceComponentAbstract<T>) obj;

        if (getTemporals() == null) return testObj.getTemporals() == null;
        if (getTemporals().size() != testObj.getTemporals().size()) return false;
        
        // Sort both sets as lists and compare each element
        List<Temporal> l1 = new ArrayList<Temporal>(getTemporals());
        Collections.sort(l1, VComponent.TEMPORAL_COMPARATOR);
        List<Temporal> l2 = new ArrayList<Temporal>(testObj.getTemporals());
        Collections.sort(l2, VComponent.TEMPORAL_COMPARATOR);
        for (int i=0; i<l1.size(); i++)
        {
            if(! l1.get(i).equals(l2.get(i))) return false;
        }
        return true;
    }
    
    @Override
    public int hashCode()
    {
        // TODO - ADD IMPLEMENTATION
        return super.hashCode();
    }

    @Override
    public String toString()
    {
        String datesString = getTemporals()
                .stream()
                .sorted()
                .map(d -> VComponent.temporalToString(d) + ",")
                .collect(Collectors.joining());
        return datesString.substring(0, datesString.length()-1); // remove last comma
    }

}
