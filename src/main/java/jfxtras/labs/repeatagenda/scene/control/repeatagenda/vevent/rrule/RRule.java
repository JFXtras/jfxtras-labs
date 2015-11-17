package jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.freq.Frequency;

/**
 * Recurrence Rule, RRULE, as defined in RFC 5545 iCalendar 3.8.5.3, page 122.
 * Used as a part of a VEVENT as defined by 3.6.1, page 52.
 * 
 * Produces a stream of start date/times after applying all modification rules.
 * 
 * @author David Bal
 *
 */
public class RRule {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
    
    /**
     *  RRule doesn't know how to make an appointment.  An appointment factory makes new appointments.  The Class of the appointment
     * is an argument for the AppointmentFactory.  The appointmentClass is set in the constructor.  A RRule object is not valid without
     * the appointmentClass.
     */
    public Class<? extends RepeatableAppointment> getAppointmentClass() { return appointmentClass; }
    private Class<? extends RepeatableAppointment> appointmentClass;
    private void setAppointmentClass(Class<? extends RepeatableAppointment> appointmentClass) { this.appointmentClass = appointmentClass; }
    public RRule withAppointmentClass(Class<? extends RepeatableAppointment> appointmentClass) { setAppointmentClass(appointmentClass); return this; }
    
//    public LocalDateTimeRange getDateTimeRange() { return appointmentDateTimeRange; }
//    private LocalDateTimeRange appointmentDateTimeRange;
//    public void setAppointmentDateTimeRange(LocalDateTimeRange appointmentDateTimeRange) { this.appointmentDateTimeRange = appointmentDateTimeRange; }
//    public VEvent withAppointmentDateTimeRange(LocalDateTimeRange appointmentDateTimeRange) { setAppointmentDateTimeRange(appointmentDateTimeRange); return this; }

    
    /** Parent VEvent 
     * Contains following data necessary for RRule: DTSTART, DURATION */
//    final private VEvent vevent;

    // TODO - MAKE A CACHE LIST OF START DATES (from the stream)
    // try to avoid making new dates by starting from the first startLocalDateTime if possible
    // having a variety of valid start date/times, spaced by 100 or so could be a good solution.
    private List<LocalDateTime> startCache = new ArrayList<LocalDateTime>();
        
    /** 
     * FREQ rule as defined in RFC 5545 iCalendar 3.3.10 p37 (i.e. Daily, Weekly, Monthly, etc.) 
     */
    public Frequency getFrequency() { return frequency; }
    private Frequency frequency;
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }
    
    /**
     * COUNT: (RFC 5545 iCalendar 3.3.10, page 41) number of events to occur before repeat rule ends
     * Uses lazy initialization of property because often COUNT stays as the default value of 0
     */
    public IntegerProperty countProperty()
    {
        if (count == null) count = new SimpleIntegerProperty(this, "count", _count);
        return count;
    }
    private IntegerProperty count;
    public Integer getCount() { return (count == null) ? _count : count.getValue(); }
    private int _count = 0;
    public void setCount(Integer i)
    {
        if (getUntil() == null)
        {
            if (i > 0)
            {
                if (count == null)
                {
                    _count = i;
                } else
                {
                    count.set(i);
                }
            } else
            {
                throw new InvalidParameterException("COUNT can't be less than 1. (" + i + ")");
            }
        } else
        {
            throw new InvalidParameterException("can't set COUNT if UNTIL is already set.");
        }
    }
    public RRule withCount(int count) { setCount(count); return this; }

    /**
     * UNTIL: (RFC 5545 iCalendar 3.3.10, page 41) date/time repeat rule ends
     * Uses lazy initialization of property because often UNTIL stays as the default value of 0
     */
    public SimpleObjectProperty<LocalDateTime> untilProperty()
    {
        if (until == null) until = new SimpleObjectProperty<LocalDateTime>(this, "until", _until);
        return until;
    }
    private SimpleObjectProperty<LocalDateTime> until;
    public LocalDateTime getUntil() { return (until == null) ? _until : until.getValue(); }
    private LocalDateTime _until;
    public void setUntil(LocalDateTime t)
    {
        if (getCount() > 0)
        {
            if (until == null)
            {
                _until = t;
            } else
            {
                until.set(t);
            }
        } else
        {
            throw new InvalidParameterException("can't set UNTIL if COUNT is already set.");
        }
    }
    public RRule withUntil(int until) { setCount(until); return this; }
    
    // deleted appointments - skip these when making appointments from the repeat rule
    // TODO - Should this be an Observable Collection?
//    /** EXDATE */  - in own class
//    final private ObservableList<LocalDateTime> exceptions = FXCollections.observableArrayList();
//    public ObservableList<LocalDateTime> getExceptions() { return exceptions; }
////    public void setExceptions(ObservableList<LocalDateTime> dates) { exceptions = dates; }
//    public RRule withExceptions(ObservableList<LocalDateTime> dates) { exceptions.addAll(dates); return this; }
//    private boolean exceptionsEquals(Collection<LocalDateTime> exceptionsTest)
//    { // test doesn't require order to be same 
//        Iterator<LocalDateTime> dateIterator = getExceptions().iterator();
//        while (dateIterator.hasNext())
//        {
//            LocalDateTime myDate = dateIterator.next();
//            if (! exceptionsTest.contains(myDate)) return false;
//        }
//        return true;
//    }
    
    // TODO - 
    /** RDATE */
//    final private ObservableList<LocalDateTime> rdate = FXCollections.observableArrayList(); - in own class
    
    
    // List of RECURRENCE-ID events represented by a individual appointment with some unique data
    // SHOULD RECURRENCES BE A SPECIAL CLASS OF APPOINTMENT WITH RECURRENCT-ID?
    // IF SO THEN I COULD MAKE A COLLECTION OF APPOINTMENTS HERE
    private Set<LocalDateTime> recurrences = new HashSet<LocalDateTime>();
    public Set<LocalDateTime> getRecurrences() { return recurrences; }
    public void setRecurrences(Set<LocalDateTime> dates) { recurrences = dates; }
    public RRule withRecurrences(Set<LocalDateTime> dates) { setRecurrences(dates); return this; }
    private boolean recurrencesEquals(Collection<LocalDateTime> recurrencesTest)
    {
        recurrencesTest.stream().forEach(a -> System.out.println("test " + a));
        Iterator<LocalDateTime> dateIterator = getRecurrences().iterator();
        while (dateIterator.hasNext())
        {
            LocalDateTime myDate = dateIterator.next();
            System.out.println(myDate);
            if (! recurrencesTest.contains(myDate)) return false;
        }
        return true;
    }
    
    
    
    
    
    
    
    
    
    
    
//    /** Constructor.  Sets parent VEvent object */
//    // TODO  REMOVE VEVENT AS A PARAMETER
//    public RRule(VEvent vevent)
//    {
//        this.vevent = vevent;
//    }
    

    /** Stream of date/times made after applying all modification rules.
     * Stream is infinite if COUNT or UNTIL not present or ends when COUNT or UNTIL condition
     * is met.
     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. */
    public Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
//        Stream<LocalDateTime> rangeFileredStream = takeWhile(frequency.stream(startDateTime), a -> 
//        {
//            boolean notTooLate = (getStartRangeDateTime() == null) ? true : ! a.isAfter(getEndRangeDateTime());
//            boolean notTooEarly = (getEndRangeDateTime() == null) ? true : ! a.isBefore(getStartRangeDateTime());
//            System.out.println("test3 " + a + " " + notTooEarly + " " + notTooLate);
//            return notTooEarly && notTooLate;
//        });
        
        if (getCount() > 0)
        {
            return frequency.stream(startDateTime).limit(getCount());
        } else if (getUntil() != null)
        {
//            return frequency
//                    .stream(startDateTime)
//                    .takeWhile(a -> a.isBefore(getUntil())); // available in Java 9
            return takeWhile(frequency.stream(startDateTime), a -> a.isBefore(getUntil()));
        }
        return frequency.stream(startDateTime);
    };
    
//    /** Filtered RRule stream to include only dates within start and end range */
//    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream)
//    {
//        return takeWhile(inStream
//                , a -> ! a.isBefore(getStartDateTimeRange())
//                    || ! a.isAfter(getEndDateTimeRange()));               
//    };

//    /** Resulting stream of date/times by applying rules 
//     * Uses startLocalDateTime - first date/time in sequence (DTSTART) as a default starting point */
//    public Stream<LocalDateTime> stream()
//    {
//        return stream(vevent.getDateTimeStart());
//    }
    
    // takeWhile - From http://stackoverflow.com/questions/20746429/limit-a-stream-by-a-predicate
    static <T> Spliterator<T> takeWhile(Spliterator<T> splitr, Predicate<? super T> predicate)
    {
      return new Spliterators.AbstractSpliterator<T>(splitr.estimateSize(), 0) {
        boolean stillGoing = true;
        @Override public boolean tryAdvance(Consumer<? super T> consumer) {
          if (stillGoing) {
            boolean hadNext = splitr.tryAdvance(elem -> {
              if (predicate.test(elem)) {
                consumer.accept(elem);
              } else {
                stillGoing = false;
              }
            });
            return hadNext && stillGoing;
          }
          return false;
        }
      };
    }

    public static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
       return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
    }
    
//        /**
//         * A Datetime range.  The range of dates within the recurrence set to be included in
//         * the stream
//         */
//        static public class LocalDateTimeRange
//        {
//            public LocalDateTimeRange(LocalDateTime start, LocalDateTime end)
//            {
//                this.start = start;
//                this.end = end;
//            }
//            
//            public LocalDateTime getStartLocalDateTime() { return start; }
//            final LocalDateTime start;
//            
//            public LocalDateTime getEndLocalDateTime() { return end; }
//            final LocalDateTime end; 
//
//        }

}
