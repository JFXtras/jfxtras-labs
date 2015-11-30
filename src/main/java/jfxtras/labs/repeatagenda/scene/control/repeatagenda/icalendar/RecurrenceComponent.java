package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule;

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
    public Set<LocalDateTime> getDates()
    {
        if (dates == null) this.dates = FXCollections.observableSet(new HashSet<LocalDateTime>());
        return dates;
    }
    private ObservableSet<LocalDateTime> dates;
    public void setDates(Set<LocalDateTime> dates)
    {
        if (dates == null) this.dates = FXCollections.observableSet(new HashSet<LocalDateTime>());
        this.dates.addAll(dates);
    }
    public T withDates(LocalDateTime...dates)
    {
        for (int i=0; i<dates.length; i++)
        {
            getDates().add(dates[i]);
        }
        return (T) this;
    }
    public T withDates(Collection<LocalDateTime> dates)
    {
        getDates().addAll(dates);
        return (T) this;
    }
    
    public void copyTo(Rule destination) {
        // TODO Auto-generated method stub       
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        EXDate testObj = (EXDate) obj;
        boolean datesEquals;
        if (getDates() == null) return testObj.getDates() == null;
        if (getDates().size() != testObj.getDates().size()) return false;
        
        // Sort both sets as lists and compare each element
        final Comparator<LocalDateTime> c = (d1, d2) -> d1.compareTo(d2);
        List<LocalDateTime> l1 = new ArrayList<LocalDateTime>(getDates());
        l1.sort(c);
        List<LocalDateTime> l2 = new ArrayList<LocalDateTime>(testObj.getDates());
        l2.sort(c);
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
                .map(d -> VComponent.FORMATTER.format(d) + ",")
                .collect(Collectors.joining());
        return datesString.substring(0, datesString.length()-1); // remove last comma
    }
    
    /** convert a comma delimeted string of VComponent.FORMATTER dates to a List<LocalDateTime> */
    public static Collection<LocalDateTime> parseDates(String string)
    {
        return Arrays.asList(string.split(","))
                     .stream()
                     .map(s -> LocalDateTime.parse(s,VComponent.FORMATTER))
                     .collect(Collectors.toList());
    }

}
