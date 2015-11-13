package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule;

import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Frequency;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

/** Recurrence Rule, RRULE, as defined in RFC 5545 iCalendar 3.8.5.3, page 122 */
public class RRule {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss");
    
    /** Range for which appointments are to be generated.  Should match the dates displayed on the calendar. */
    public LocalDateTimeRange getAppointmentDateTimeRange() { return appointmentDateTimeRange; }
    private LocalDateTimeRange appointmentDateTimeRange;
    public void setAppointmentDateTimeRange(LocalDateTimeRange appointmentDateTimeRange) { this.appointmentDateTimeRange = appointmentDateTimeRange; }
    public RRule withAppointmentDateTimeRange(LocalDateTimeRange appointmentDateTimeRange) { setAppointmentDateTimeRange(appointmentDateTimeRange); return this; }

    /** RRule doesn't know how to make an appointment.  An appointment factory makes new appointments.  The Class of the appointment
     * is an argument for the AppointmentFactory.  The appointmentClass is set in the constructor. */
    public Class<? extends RepeatableAppointment> getAppointmentClass() { return appointmentClass; }
    private Class<? extends RepeatableAppointment> appointmentClass;
//    private void setAppointmentClass(Class<? extends RepeatableAppointment> appointmentClass) { this.appointmentClass = appointmentClass; }
//    public RRule withAppointmentClass(Class<? extends RepeatableAppointment> appointmentClass) { setAppointmentClass(appointmentClass); return this; }
    
    /** DTSTART from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule */
    final private ObjectProperty<LocalDateTime> startLocalDateTime = new SimpleObjectProperty<LocalDateTime>();
    public ObjectProperty<LocalDateTime> startLocalDateTimeProperty() { return startLocalDateTime; }
    public LocalDateTime getStartLocalDateTime() { return startLocalDateTime.getValue(); }
    public void setStartLocalDate(LocalDateTime startDate) { this.startLocalDateTime.set(startDate); }
    public RRule withStartLocalDate(LocalDateTime startDate) { setStartLocalDate(startDate); return this; }

    /** FREQ rule as defined in RFC 5545 iCalendar 3.3.10 p37 (i.e. Daily, Weekly, Monthly, etc.) */
    public Frequency getFrequency() { return frequency; }
    private Frequency frequency;
    public void setFrequency(Frequency frequency) { this.frequency = frequency; }
    
    /** COUNT: (RFC 5545 iCalendar 3.3.10, page 41) number of events to occur before repeat rule ends */
    // Uses lazy initialization of property because often COUNT stays as the default value of 0
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

    /** UNTIL: (RFC 5545 iCalendar 3.3.10, page 41) date/time repeat rule ends */
    // Uses lazy initialization of property because often UNTIL stays as the default value of 0
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
    
    /** Start of week - default start of week is Monday */
//    public DayOfWeek getWeekStart() { return weekStart; }
    public static DayOfWeek WEEK_START = DayOfWeek.MONDAY; // TODO - WHAT AM I GOING TO DO WITH THIS?  IT IS SUPPOSE TO BE IN LOCALE.
//    public void setWeekStart(DayOfWeek weekStart) { this.weekStart = weekStart; }
    
    /** Constructor.  Sets appointmentClass used to make new appointments in the AppointmentFactory */
    public RRule(Class<? extends RepeatableAppointment> appointmentClass)
    {
        this.appointmentClass = appointmentClass;
    }
    
    /** Resulting stream of date/times by applying rules 
     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. */
    public Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        if (getCount() > 0)
        {
            return frequency.stream(startDateTime).limit(getCount());
        } else if (getUntil() != null)
        {
//            return frequency
//                    .stream(startDateTime)
//                    .takeWhile(a -> a.isBefore(getUntil())); // available in Java 9
            return takeWhile(frequency.stream(startDateTime), a -> a.isBefore(LocalDateTime.of(2015, 11, 19, 10, 0)));
        }
        return frequency.stream(startDateTime);
    };

    /** Resulting stream of date/times by applying rules 
     * Uses startLocalDateTime - first date/time in sequence (DTSTART) as a default starting point */
    public Stream<LocalDateTime> stream()
    {
        return stream(getStartLocalDateTime());
    }
    
    // takeWhile - From http://stackoverflow.com/questions/20746429/limit-a-stream-by-a-predicate
    static <T> Spliterator<T> takeWhile(
            Spliterator<T> splitr, Predicate<? super T> predicate) {
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

        static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
           return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
        }
    

}
