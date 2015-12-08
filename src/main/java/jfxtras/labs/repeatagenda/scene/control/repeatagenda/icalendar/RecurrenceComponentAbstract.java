package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

/** For EXDate and RDate
 * Limitation: only DATE-TIME supported.  DATE is not supported.
 * @param <T>
 * @see EXDate
 * @see RDate
 * */
public abstract class RecurrenceComponent<T>
{
    /**
     * EXDATE or RDATE: Set of date/times included or excepted for recurring events, to-dos, journal entries.
     * 3.8.5.1, RFC 5545 iCalendar
     */
    public Set<VDateTime> getDates()
    {
        if (dates == null) this.dates = FXCollections.observableSet(new HashSet<VDateTime>());
        return dates;
    }
    private ObservableSet<VDateTime> dates;
    public void setDates(Set<VDateTime> dates)
    {
        if (dates == null) this.dates = FXCollections.observableSet(new HashSet<VDateTime>());
        this.dates.addAll(dates);
    }
    public T withDates(VDateTime...dates)
    {
        for (int i=0; i<dates.length; i++)
        {
            getDates().add(dates[i]);
        }
        return (T) this;
    }
    public T withDates(Collection<VDateTime> dates)
    {
        getDates().addAll(dates);
        return (T) this;
    }
    
    /** Deep copy all fields from source to destination 
     * @param <U>*/
    private static <U> void copy(RecurrenceComponent<U> source, RecurrenceComponent<U> destination)
    {
        if (source.getDates() != null)
        {
            source.getDates().stream().forEach(r -> destination.getDates().add(r));
        }
    }
    
    /** Deep copy all fields from this to destination */
    public void copyTo(RecurrenceComponent<T> destination)
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
        EXDate testObj = (EXDate) obj;

        if (getDates() == null) return testObj.getDates() == null;
        if (getDates().size() != testObj.getDates().size()) return false;
        
        // Sort both sets as lists and compare each element
//        final Comparator<VDateTime> c = (d1, d2) -> d1.getLocalDateTime().compareTo(d2.getLocalDateTime());
        List<VDateTime> l1 = new ArrayList<VDateTime>(getDates());
        Collections.sort(l1);
//        l1.sort(c);
        List<VDateTime> l2 = new ArrayList<VDateTime>(testObj.getDates());
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
        String datesString = getDates()
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
