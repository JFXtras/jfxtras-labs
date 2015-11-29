package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByMonthDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule.ByRules;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;

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
    private List<LocalDateTime> startCache = new ArrayList<>();
        
    /** 
     * FREQ rule as defined in RFC 5545 iCalendar 3.3.10 p37 (i.e. Daily, Weekly, Monthly, etc.) 
     */
    public ObjectProperty<Frequency> frequencyProperty() { return frequency; }
    private ObjectProperty<Frequency> frequency = new SimpleObjectProperty<>(this, "FREQ");
    public Frequency getFrequency() { return frequency.get(); }
    public void setFrequency(Frequency frequency) { this.frequency.set(frequency); }
    
    /**
     * COUNT: (RFC 5545 iCalendar 3.3.10, page 41) number of events to occur before repeat rule ends
     * Uses lazy initialization of property because often COUNT stays as the default value of 0.
     * Value of 0 means COUNT is not used.
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
//        System.out.println("until: " + getUntil());
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
            } else
            {
                throw new InvalidParameterException("COUNT can't be less than 0. (" + i + ")");
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
        if (getCount() == 0)
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
    public RRule withUntil(LocalDateTime until) { setUntil(until); return this; }
    
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
    private boolean instancesEquals(Collection<LocalDateTime> instancesTest)
    {
        Iterator<LocalDateTime> dateIterator = getInstances().iterator();
        while (dateIterator.hasNext())
        {
            LocalDateTime myDate = dateIterator.next();
            if (! instancesTest.contains(myDate)) return false;
        }
        return true;
    }
    

    /** Deep copy all fields from source to destination */
    public static void copy(RRule source, RRule destination)
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

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        RRule testObj = (RRule) obj;

        System.out.println("count: " + getCount() + " " + testObj.getCount());
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
        if (getCount() > 0) builder.append(";COUNT=" + getCount());
        if (getUntil() != null) builder.append(";UNTIL=" + VComponent.FORMATTER.format(getUntil()));
        String rules = getFrequency()
                .getRules()
                .stream()
                .map(r -> ";" + r.toString())
                .collect(Collectors.joining());
        builder.append(rules);
        return builder.toString();
    }
    
    /** Return new RRule with its properties set by parsing iCalendar compliant RRULE string */
    public static RRule parseRRule(String rRuleString)
    {
        RRule rrule = new RRule();
//        // RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6
//        List<String> stringList = Arrays.asList(rRuleString.split(";"));
//        
//        Iterator<String> iterator = stringList.iterator();
//        
//        while (iterator.hasNext())
//        {
//            String[] ruleAndValue = iterator.next().split("=");
//            if (ruleAndValue[0].equals(rrule.frequencyProperty().getName()))
//            { // FREQ
//                Frequency freq = Frequency.FrequencyType
//                        .valueOf(ruleAndValue[1])
//                        .newInstance();
//                rrule.setFrequency(freq);
//                iterator.remove();
//            }
//        }
        
        // Parse string
        Arrays.stream(rRuleString.split(";"))
                .forEach(r ->
                {
                    String[] ruleAndValue = r.split("=");
                    if (ruleAndValue[0].equals(rrule.frequencyProperty().getName()))
                    { // FREQ
                        Frequency freq = Frequency.FrequencyType
                                .valueOf(ruleAndValue[1])
                                .newInstance();
                        rrule.setFrequency(freq);
                    } else if (ruleAndValue[0].equals(rrule.frequencyProperty().getName()))
                    {
                        
                    } else
                    {
                        for (ByRules b : ByRules.values())
                        {
                            System.out.println("Testing: " + ruleAndValue[0] + " " + b + (ruleAndValue[0].equals(b.toString())));
                            if (ruleAndValue[0].equals(b.toString()))
                            {
                                try {
                                    Rule rule = b.newInstance(rrule.getFrequency(), ruleAndValue[1]);
                                    rrule.getFrequency().addByRule(rule);
                                    ByMonthDay b2 = (ByMonthDay) rule;
                                    System.out.println("days: ");
                                    Arrays.stream(b2.getDaysOfMonth()).forEach(System.out::println);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                System.out.println("got: " + b);
                            }
                        }
                    }
                });
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
            return takeWhile(filteredStream, a -> a.isBefore(getUntil()));
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

    public static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
       return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
    }

//    /** Enumeration of RRule parts
//     * Is used to make new instances of the different Frequencies by matching RRULE property
//     * to its matching class */
//    public static enum FrequencyParts
//    {
//        FREQ (Yearly.class)
//      , UNTIL (Monthly.class)
//      , COUNT (Weekly.class)
//      , INTERVAL (Daily.class)
//      , BYSECOND (Hourly.class) // Not implemented
//      , BYMINUTE (Minutely.class) // Not implemented
//      , BYHOUR (Secondly.class)
//      , BYDAY
//      , BYMONTHDAY
//      , BYYEARDAY
//      , BYWEEKNO
//      , BYMONTH
//      , BYSETPOS
//      , WKST);
//      
//        private Class<? extends Frequency> clazz;
//          
//        FrequencyParts(Class<? extends Rule> clazz)
//        {
//            this.clazz = clazz;
//        }
//          
//        public Frequency newInstance()
//        {
//            try {
//                return clazz.newInstance();
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
}
