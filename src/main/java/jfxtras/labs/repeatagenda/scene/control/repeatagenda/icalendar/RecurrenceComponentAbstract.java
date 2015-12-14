package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

/** For EXDate and RDate
 * Stores either date or date-time values
 * @param <T> either EXDate or RDate
 * @see EXDate
 * @see RDate
 * */
public abstract class RecurrenceComponentAbstract<T> implements RecurrenceComponent
{
    /**
     * EXDATE or RDATE: Set of date/times included or excepted for recurring events, to-dos, journal entries.
     * 3.8.5.1, RFC 5545 iCalendar
     */
    public Set<VDateTime> getVDateTimes() { return vDateTimes; }
    private ObservableSet<VDateTime> vDateTimes = FXCollections.observableSet(new HashSet<VDateTime>());
    public void setVDateTimes(Temporal...dateOrDateTime)
    {
        for (Temporal d : dateOrDateTime)
        {
            this.getVDateTimes().add(new VDateTime(d));
        }
        if (dateOrDateTime.length > 0) getTemporalClass(); // test class sameness
        Class<? extends Temporal> firstClass = dateOrDateTime[0].getClass();
        checkTemporalTypes(firstClass);
    }

    /** Class of encapsulated Temporal objects */
    protected Class<? extends Temporal> getTemporalClass()
    {
        if (getVDateTimes().size() > 0)
        {
            Class<? extends Temporal> clazz = getVDateTimes().iterator().next().getTemporal().getClass();
            checkTemporalTypes(clazz);
            return clazz;                
        } else return null;
    }
    
    /**
     *  If vDateTimes set wraps a set of LocalDateTime Temporal objects then returns a collection 
     *  of LocalDateTime objects, otherwise throws an exception.
     * 
     * @return collection of LocalDateTime
     */
    public Collection<LocalDateTime> getLocalDateTimes()
    {
//        checkTemporalTypes(LocalDateTime.class);
        return getTemporalStream()
                .filter(d -> (d instanceof LocalDateTime))
                .map(d -> (LocalDateTime) d)
                .collect(Collectors.toList());
    }

    /**
     *  If vDateTimes set wraps a set of LocalDate Temporal objects then returns a collection 
     *  of LocalDate objects, otherwise throws an exception.
     * 
     * @return collection of LocalDate
     */
    public Collection<LocalDate> getLocalDates()
    {
//        checkTemporalTypes(LocalDate.class);
        return getTemporalStream()
                .filter(d -> (d instanceof LocalDate))
                .map(d -> (LocalDate) d)
                .collect(Collectors.toList());
    }
    protected Stream<Temporal> getTemporalStream()
    {
        return getVDateTimes()
                .stream()
                .map(d -> d.getDateOrDateTime());
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
    private void checkTemporalTypes(Class<? extends Temporal> clazz)
    {
        boolean same =  getVDateTimes()
                .stream()
                .peek(v -> System.out.println(v.getTemporal().getClass().getName()))
                .allMatch(v -> v.getTemporal().getClass().equals(clazz));
        if (! same) throw new IllegalArgumentException("Not all Temporal objects in VDateTime class of type:" + clazz.getSimpleName());
    }
    
//    @SuppressWarnings("unchecked")
//    public T withDateTimes(LocalDateTime... dateTime)
//    {
//        for (LocalDateTime d : dateTime)
//        {
//            this.getVDateTimes().add(new VDateTime(d));
//        }
//        return (T) this;
//    }
//    @SuppressWarnings("unchecked")
//    public T withDates(LocalDate... date)
//    {
//        for (LocalDate d : date)
//        {
//            this.getVDateTimes().add(new VDateTime(d));
//        }
//        return (T) this;
//    }
    
    // CONSTRUCTORS
    public RecurrenceComponentAbstract() { }
    public RecurrenceComponentAbstract(Temporal... dateOrDateTime) { withTemporals(dateOrDateTime); }
//    public RecurrenceComponentAbstract(LocalDate... date) { withDates(date); }

    /** Deep copy all fields from source to destination 
     * @param <U>*/
    private static <T> void copy(RecurrenceComponentAbstract<T> source, RecurrenceComponentAbstract<T> destination)
    {
        if (source.getVDateTimes() != null)
        {
            source.getVDateTimes().stream().forEach(r -> destination.getVDateTimes().add(r));
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

        if (getVDateTimes() == null) return testObj.getVDateTimes() == null;
        if (getVDateTimes().size() != testObj.getVDateTimes().size()) return false;
        
        // Sort both sets as lists and compare each element
//        final Comparator<VDateTime> c = (d1, d2) -> d1.getLocalDateTime().compareTo(d2.getLocalDateTime());
        List<VDateTime> l1 = new ArrayList<VDateTime>(getVDateTimes());
        Collections.sort(l1);
//        l1.sort(c);
        List<VDateTime> l2 = new ArrayList<VDateTime>(testObj.getVDateTimes());
        Collections.sort(l2);
//        l2.sort(c);
        for (int i=0; i<l1.size(); i++)
        {
            if(! l1.get(i).equals(l2.get(i))) return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        String datesString = getVDateTimes()
                .stream()
                .sorted()
                .map(d -> d.toString() + ",")
                .collect(Collectors.joining());
        return datesString.substring(0, datesString.length()-1); // remove last comma
    }
    
    /** convert a comma delimited string of VComponent.FORMATTER dates to a List<LocalDateTime> */
    public static Collection<VDateTime> parseDates(String string)
    {
        return Arrays.asList(string.split(","))
                     .stream()
                     .map(s -> VDateTime.parseString(s))
                     .collect(Collectors.toList());
    }

}
