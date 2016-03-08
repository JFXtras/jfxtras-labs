package jfxtras.labs.icalendar.rrule;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
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
import jfxtras.labs.icalendar.DateTimeUtilities;
import jfxtras.labs.icalendar.DateTimeUtilities.DateTimeType;
import jfxtras.labs.icalendar.VComponent;
import jfxtras.labs.icalendar.rrule.byxxx.Rule;
import jfxtras.labs.icalendar.rrule.byxxx.Rule.ByRuleType;
import jfxtras.labs.icalendar.rrule.freq.Frequency;

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
        if ((getUntil() == null) || (i == 0))
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
        }
        else throw new IllegalArgumentException("can't set COUNT if UNTIL is already set.");
    }
    public RRule withCount(int count) { setCount(count); return this; }

    /**
     * UNTIL: (RFC 5545 iCalendar 3.3.10, page 41) date/time repeat rule ends
     * Must be same Temporal type as dateTimeStart (DTSTART)
     * If DTSTART has time then UNTIL must be UTC time.  That means the Temporal
     * can be LocalDate or ZonedDateTime with ZoneID.of("Z");
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
    public Set<VComponent<?>> recurrences() { return recurrences; }
    private Set<VComponent<?>> recurrences = new HashSet<>();
//    public void setRecurrences(Set<VComponent<?>> temporal) { recurrences = temporal; }
    public RRule withRecurrences(VComponent<?>...v) { recurrences.addAll(Arrays.asList(v)); return this; }

    /** Deep copy all fields from source to destination */
    private static void copy(RRule source, RRule destination)
    {
        if (source.getCount() > 0)
        {
            destination.setCount(source.getCount());
        }
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
        if (source.getUntil() != null)
        {
            destination.setUntil(source.getUntil());
        }
        Iterator<VComponent<?>> i = source.recurrences().iterator();
        while (i.hasNext())
        {
            destination.recurrences().add(i.next());
        }
    }
    
    /** Deep copy all fields from this to destination */
    public void copyTo(RRule destination)
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
        RRule testObj = (RRule) obj;

        boolean countEquals = getCount().equals(testObj.getCount());
        boolean frequencyEquals = getFrequency().equals(testObj.getFrequency()); // RRule requires a frequency
        System.out.println("untils:" + getUntil() + " " + testObj.getUntil() + ((getUntil() != null) ? getUntil().hashCode() : "")
                + " " + ((testObj.getUntil() != null) ? testObj.getUntil().hashCode() : ""));
        boolean untilEquals = (getUntil() == null) ? (testObj.getUntil() == null) : getUntil().equals(testObj.getUntil());
        boolean recurrencesEquals = (recurrences() == null) ? (testObj.recurrences() == null) : recurrences().equals(testObj.recurrences());

        System.out.println("RRule " + countEquals + " " + frequencyEquals + " " + recurrencesEquals + " " + untilEquals);
        return countEquals && frequencyEquals && recurrencesEquals && untilEquals;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = (31 * hash) + getCount().hashCode();
        hash = (31 * hash) + getFrequency().hashCode();
        hash = (31 * hash) + ((recurrences() == null) ? 0 : recurrences().hashCode());
        return hash;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append(getFrequency().toString());
        if (getCount() > 0) builder.append(";" + countProperty().getName() + "=" + getCount());
        if (getUntil() != null) builder.append(";" + untilProperty().getName() + "=" + DateTimeUtilities.format(getUntil()));
        String rules = getFrequency()
                .getByRules()
                .stream()
                .map(r -> ";" + r.toString())
                .collect(Collectors.joining());
        builder.append(rules);
        return builder.toString();
    }
    
    /**
     * Checks to see if object contains required properties.  Returns empty string if it is
     * valid.  Returns string of errors if not valid.
     */
    public String makeErrorString(VComponent<?> parent)
    {
        StringBuilder builder = new StringBuilder();
        if (recurrences() != null)
        {
            recurrences().stream()
                    .filter(r -> ! DateTimeType.of(r.getDateTimeRecurrence()).equals(parent.getDateTimeType()))
                    .forEach(r -> 
                    {
                        builder.append(System.lineSeparator()
                                + "Invalid RRule.  Recurrence ("
                                + r.getDateTimeRecurrence() + ", " + r.getDateTimeType()
                                + ") must have same DateTimeType as parent ("
                                + parent.getDateTimeType() + ")");
                    });
        }
        if (getUntil() != null)
        {
//            Temporal convertedUntil = DateTimeType.changeTemporal(getUntil(), parent.getDateTimeType());
            Temporal convertedUntil = parent.getDateTimeType().from(getUntil(), parent.getZoneId());
            if (VComponent.isBefore(convertedUntil, parent.getDateTimeStart())) builder.append(System.lineSeparator() + "Invalid RRule.  UNTIL can not come before DTSTART");
        }
        if ((getCount() == null) || (getCount() < 0))
        {
            builder.append(System.lineSeparator() + "Invalid RRule.  COUNT must not be less than 0");
        }
        if (getFrequency() == null)
        {
            builder.append(System.lineSeparator() + "Invalid RRule.  FREQ must not be null");
        } else builder.append(getFrequency().makeErrorString());
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
                Temporal dateTime = DateTimeUtilities.parse(value);
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
                for (ByRuleType b : ByRuleType.values())
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

    /**
     * Determines if recurrence set is goes on forever
     * 
     * @return - true if recurrence set is infinite, false otherwise
     */
    public boolean isInfinite()
    {
        return ((getCount() == 0) && (getUntil() == null));
    }
    
    /** Stream of date/times made after applying all modification rules.
     * Stream is infinite if COUNT or UNTIL not present or ends when COUNT or UNTIL condition
     * is met.
     * Starts on startDateTime, which MUST be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. */
    public Stream<Temporal> stream(Temporal start)
    {
        // filter out recurrences
        Stream<Temporal> filteredStream = (recurrences().size() == 0) ?
                getFrequency().stream(start) :
                getFrequency().stream(start).filter(t -> 
                {
                    return ! recurrences().stream()
                            .map(v -> v.getDateTimeRecurrence())
                            .filter(t2 -> t2.equals(t))
                            .findAny()
                            .isPresent();
                }); // ! getRecurrences().contains(t))
        if (getCount() > 0)
        {
            return filteredStream.limit(getCount());
        } else if (getUntil() != null)
        {
//            return frequency
//                    .stream(startDateTime)
//                    .takeWhile(a -> a.isBefore(getUntil())); // available in Java 9
//            Temporal convertedUntil = DateTimeType.changeTemporal(getUntil(), DateTimeType.of(start));
            ZoneId zone = (start instanceof ZonedDateTime) ? ((ZonedDateTime) start).getZone() : null;
            Temporal convertedUntil = DateTimeType.of(start).from(getUntil(), zone);
            return takeWhile(filteredStream, a -> ! VComponent.isAfter(a, convertedUntil));
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
