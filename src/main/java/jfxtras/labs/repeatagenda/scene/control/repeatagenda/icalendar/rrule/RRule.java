package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule.ByRules;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency.FrequencyType;

/**
 * Recurrence Rule, RRULE, as defined in RFC 5545 iCalendar 3.8.5.3, page 122.
 * Used as a part of a VEVENT as defined by 3.6.1, page 52.
 * 
 * Produces a stream of start date/times after applying all modification rules.
 * 
 * @author David Bal
 *
 */
public class RRule
{
    private final static String COUNT_NAME = "COUNT";
    private final static String UNTIL_NAME = "UNTIL";
    private final static String INTERVAL_NAME = "INTERVAL";
    
    /** Parent VEvent 
     * Contains following data necessary for RRule: DTSTART, DURATION */
//    final private VEvent vevent;

    // TODO - MAKE A CACHE LIST OF START DATES (from the stream)
    // try to avoid making new dates by starting from the first startLocalDateTime if possible
    // having a variety of valid start date/times, spaced by 100 or so could be a good solution.
    private List<LocalDateTime> startCache = new ArrayList<>();
        
    /** 
     * FREQ rule as defined in RFC 5545 iCalendar 3.3.10 p37 (i.e. Daily, Weekly, Monthly, etc.) 
     */
    public ObjectProperty<Frequency> frequencyProperty() { return frequency; }
    private ObjectProperty<Frequency> frequency = new SimpleObjectProperty<>(this, "FREQ");
    public Frequency getFrequency() { return frequency.get(); }
    public void setFrequency(Frequency frequency) { this.frequency.set(frequency); }
    public RRule withFrequency(Frequency frequency) { setFrequency(frequency); return this; }
    
    /**
     * COUNT: (RFC 5545 iCalendar 3.3.10, page 41) number of events to occur before repeat rule ends
     * Uses lazy initialization of property because often COUNT stays as the default value of 0.
     * Value of 0 means COUNT is not used.
     */
    public IntegerProperty countProperty()
    {
        if (count == null) count = new SimpleIntegerProperty(this, COUNT_NAME, _count);
        return count;
    }
    private IntegerProperty count;
    public Integer getCount() { return (count == null) ? _count : count.getValue(); }
    private int _count = 0;
    public void setCount(Integer i)
    {
        if (getUntil() == null)
        {
            if (i >= 0)
            {
                if (count == null)
                {
                    _count = i;
                } else
                {
                    count.set(i);
                }
            } else throw new IllegalArgumentException("COUNT can't be less than 0. (" + i + ")");
        } else throw new IllegalArgumentException("can't set COUNT if UNTIL is already set.");
    }
    public RRule withCount(int count) { setCount(count); return this; }

    /**
     * UNTIL: (RFC 5545 iCalendar 3.3.10, page 41) date/time repeat rule ends
     * Uses lazy initialization of property because often UNTIL stays as the default value of 0
     */
    public SimpleObjectProperty<Temporal> untilProperty()
    {
        if (until == null) until = new SimpleObjectProperty<Temporal>(this, UNTIL_NAME, _until);
        return until;
    }
    private SimpleObjectProperty<Temporal> until;
    public Temporal getUntil() { return (until == null) ? _until : until.getValue(); }
    private Temporal _until;
    public void setUntil(Temporal t)
    {
        if (getCount() == 0)
        {
            if (until == null)
            {
                _until = t;
            } else
            {
                until.set(t);
            }
        } else throw new IllegalArgumentException("can't set UNTIL if COUNT is already set.");
    }
    public RRule withUntil(Temporal until) { setUntil(until); return this; }
    
    /**
     * The set of specific instances of recurring "VEVENT", "VTODO", or "VJOURNAL" calendar components
     * specified individually in conjunction with "UID" and "SEQUENCE" properties.  Each instance 
     * has a RECURRENCE ID with a value equal to the original value of the "DTSTART" property of 
     * the recurrence instance.  The UID matches the UID of the parent calendar component.
     * See 3.8.4.4 of RFC 5545 iCalendar
     */
    public Set<LocalDateTime> getInstances() { return instances; }
    private Set<LocalDateTime> instances = new HashSet<>();
    public void setInstances(Set<LocalDateTime> dateTimes) { instances = dateTimes; }
    public RRule withInstances(Set<LocalDateTime> dateTimes) { setInstances(dateTimes); return this; }
//    private boolean instancesEquals(Collection<LocalDateTime> instancesTest)
//    {
//        Iterator<LocalDateTime> dateIterator = getInstances().iterator();
//        while (dateIterator.hasNext())
//        {
//            LocalDateTime myDate = dateIterator.next();
//            if (! instancesTest.contains(myDate)) return false;
//        }
//        return true;
//    }

    /** Deep copy all fields from source to destination */
    private static void copy(RRule source, RRule destination)
    {
        if (source.getCount() > 0) destination.setCount(source.getCount());
        if (source.getFrequency() != null)
        {
            try {
                Frequency newFreqency = source.getFrequency().getClass().newInstance();
                Frequency.copy(source.getFrequency(), newFreqency);
                destination.setFrequency(newFreqency);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        Iterator<LocalDateTime> i = source.getInstances().iterator();
        while (i.hasNext())
        {
            destination.getInstances().add(i.next());
        }
    }
    
    /** Deep copy all fields from this to destination */
    public void copyTo(RRule destination)
    {
        copy(this, (RRule) destination);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        RRule testObj = (RRule) obj;

        boolean countEquals = (getCount() == null) ?
                (testObj.getCount() == null) : getCount().equals(testObj.getCount());
        boolean frequencyEquals = (getFrequency() == null) ?
                (testObj.getFrequency() == null) : getFrequency().equals(testObj.getFrequency());
        boolean recurrencesEquals = (getInstances() == null) ?
                (testObj.getInstances() == null) : getInstances().equals(testObj.getInstances());

        System.out.println("RRule " + countEquals + " " + frequencyEquals + " " + recurrencesEquals);
        return countEquals && frequencyEquals && recurrencesEquals;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getFrequency().toString());
        if (getCount() > 0) builder.append(";" + countProperty().getName() + "=" + getCount());
        if (getUntil() != null) builder.append(";" + untilProperty().getName() + "=" + VComponent.temporalToString(getUntil()));
        String rules = getFrequency()
                .getByRules()
                .stream()
                .map(r -> ";" + r.toString())
                .collect(Collectors.joining());
        builder.append(rules);
        return builder.toString();
    }
    
    /**
     * Produce easy to read summary of repeat rule
     * Is limited to producing strings for following repeat rules:
     * Any individual Frequency (FREQ)
     * COUNT and UNTIL properties
     * MONTHLY and WEEKLY with ByDay Byxxx rule
     * 
     * @param startTemporal LocalDate or LocalDateTime of start date/time (DTSTART)
     * @return Easy to read summary of repeat rule
     */
  //TODO - use resource bundle instead of literal strings
    public String summary(Temporal startTemporal)
    {
        StringBuilder builder = new StringBuilder();
        if (getCount() == 1) return "Once";
        
        if (getFrequency().getInterval() > 1)
        {
            builder.append("Every ");
            builder.append(getFrequency().getInterval() + " ");
            builder.append(getFrequency().frequencyType().toStringPlural());
        } else if (getFrequency().getInterval().equals(1))
        {
            String frequency = FrequencyType.stringConverter.toString(getFrequency().frequencyType());
            builder.append(frequency);
        }
        
        ByDay byDay = (ByDay) getFrequency().getByRuleByType(ByRules.BYDAY);
        switch (getFrequency().frequencyType())
        {
        case DAILY: // add nothing else
            break;
        case MONTHLY:
            if (byDay == null)
            {
                builder.append(" on day " + LocalDate.from(startTemporal).getDayOfMonth());
            } else
            {
                builder.append(" on the " + byDay.summary());
            }
            break;
        case WEEKLY:
            if (byDay == null)
            {
                DayOfWeek dayOfWeek = LocalDate.from(startTemporal).getDayOfWeek();
                String dayOfWeekString = Settings.DAYS_OF_WEEK.get(dayOfWeek);
                builder.append(" on " + dayOfWeekString);
            } else
            {
                builder.append(" on " + byDay.summary());
            }
            break;
        case YEARLY:
            builder.append(" on " + Settings.DATE_FORMAT_AGENDA_MONTHDAY.format(startTemporal));
            break;
        case HOURLY:
        case MINUTELY:
        case SECONDLY:
            throw new IllegalArgumentException("Not supported:" + getFrequency().frequencyType());
        default:
            break;
        }
        
        if (getCount() > 0)
        {
            builder.append(", " + getCount() + " times");
        } else if (getUntil() != null)
        {
            builder.append(", until " + Settings.DATE_FORMAT_AGENDA_DATEONLY.format(getUntil()));
        }
        return builder.toString();
    }

    /** Return new RRule with its properties set by parsing iCalendar compliant RRULE string */
    public static RRule parseRRule(String rRuleString)
    {
        RRule rrule = new RRule();
        Integer interval = null;
        
        // Parse string
        for (String element : rRuleString.split(";"))
        {
            String property = element.substring(0, element.indexOf("="));
            String value = element.substring(element.indexOf("=") + 1).trim();
            if (property.equals(rrule.frequencyProperty().getName()))
            { // FREQ
                Frequency freq = Frequency.FrequencyType
                        .valueOf(value)
                        .newInstance();
                rrule.setFrequency(freq);
                if (interval != null) rrule.getFrequency().setInterval(interval);
            } else if (property.equals(COUNT_NAME))
            { // COUNT
                rrule.setCount(Integer.parseInt(value));
            } else if (property.equals(UNTIL_NAME))
            { // UNTIL
                Temporal dateTime = VComponent.parseTemporal(value);
                rrule.setUntil(dateTime);
            } else if (property.equals(INTERVAL_NAME))
            { // INTERVAL
                if (rrule.getFrequency() != null)
                {
                    rrule.getFrequency().setInterval(Integer.parseInt(value));
                } else
                {
                    interval = Integer.parseInt(value);
                }
            } else
            {
                for (ByRules b : ByRules.values())
                {
                    if (property.equals(b.toString()))
                    {
                        Rule rule = null;
                        try {
                            rule = b.newInstance(value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        rrule.getFrequency().addByRule(rule);
                    }
                }
            }            
        }
        return rrule;
    }

    /** Stream of date/times made after applying all modification rules.
     * Stream is infinite if COUNT or UNTIL not present or ends when COUNT or UNTIL condition
     * is met.
     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. */
    public Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        Stream<LocalDateTime> filteredStream = (getInstances().size() > 0) ?
                getFrequency().stream(startDateTime).filter(d -> ! getInstances().contains(d))
               : getFrequency().stream(startDateTime);
        if (getCount() > 0)
        {
            return filteredStream.limit(getCount());
        } else if (getUntil() != null)
        {
//            return frequency
//                    .stream(startDateTime)
//                    .takeWhile(a -> a.isBefore(getUntil())); // available in Java 9
            return takeWhile(filteredStream, a -> a.isBefore(VComponent.localDateTimeFromTemporal(getUntil())));
        }
        return filteredStream;
    };
    
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

    static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
       return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
    }
}
