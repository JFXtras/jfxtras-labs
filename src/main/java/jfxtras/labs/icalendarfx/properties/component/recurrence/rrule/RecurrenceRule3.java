package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.VCalendarElement;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByHour;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMinute;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.BySecond;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByYearDay;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

/**
 * RRULE
 * Recurrence Rule
 * RFC 5545 iCalendar 3.3.10 page 38
 * 
 * Contains the following Recurrence Rule elements:
 * COUNT
 * UNTIL
 * FREQUENCY
 * INTERVAL
 * BYxxx RULES in a List
 * 
 * The value part of the recurrence rule.  It supports the following elements: <br>
 * ( "FREQ" "=" freq ) <br>
 * ( "UNTIL" "=" enddate ) <br>
 * ( "COUNT" "=" 1*DIGIT ) <br>
 * ( "INTERVAL" "=" 1*DIGIT ) <br>
 * ( "BYSECOND" "=" byseclist ) <br>
 * ( "BYMINUTE" "=" byminlist ) <br>
 * ( "BYHOUR" "=" byhrlist ) <br>
 * ( "BYDAY" "=" bywdaylist ) <br>
 * ( "BYMONTHDAY" "=" bymodaylist ) <br>
 * ( "BYYEARDAY" "=" byyrdaylist ) <br>
 * ( "BYWEEKNO" "=" bywknolist ) <br>
 * ( "BYMONTH" "=" bymolist ) <br>
 * ( "BYSETPOS" "=" bysplist ) <br>
 * ( "WKST" "=" weekday ) <br>
 * 
 * In addition to methods to support iCalendar recurrence rule parts, there is a method
 * {@link #streamRecurrences(Temporal)}  that produces a stream of start date/times for the recurrences
 * defined by the rule.
 * 
 * @author David Bal
 * @see RecurrenceRule
 *
 */
// TODO - PRESERVE ORDER OF PARAMETERS FROM PARSED STRING
// TODO - LISTENER TO PREVENT COUNT AND LISTENER FROM BOTH BEING SET
public class RecurrenceRule3 implements VCalendarElement
{
    /** 
     * BYxxx Rules
     * RFC 5545, iCalendar 3.3.10 Page 42
     * 
     * List contains any of the following.  The following list also indicates the processing order:
     * {@link ByMonth} {@link ByWeekNo} {@link ByYearDay} {@link ByMonthDay} {@link ByDay} {@link ByHour}
     * {@link ByMinute} {@link BySecond} {@link BySetPos}
     * 
     * BYxxx rules modify the recurrence set by either expanding or limiting it.
     * 
     * Each BYxxx rule can only occur once
     *  */
    public ObservableList<ByRule<?>> byRules() { return byRules; }
    final private ObservableList<ByRule<?>> byRules; // = FXCollections.observableArrayList();
//    public void setByRules(ObservableList<ByRule<?>> byRules) { this.byRules = byRules; }
//    public RecurrenceRule3 withByRules(ObservableList<ByRule<?>> byRules) { setByRules(byRules); return this; }
    public RecurrenceRule3 withByRules(ByRule<?>...byRules)
    {
        for (ByRule<?> myByRule : byRules)
        {
            byRules().add(myByRule);
        }
        return this;
    }
    public RecurrenceRule3 withByRules(String...byRules)
    {
        Arrays.stream(byRules).forEach(c -> parseContent(c));
        return this;
    }
    
    /** Return ByRule associated with class type */
    public ByRule<?> lookupByRule(Class<? extends ByRule<?>> byRuleClass)
    {
        Optional<ByRule<?>> rule = byRules()
                .stream()
                .filter(r -> byRuleClass.isInstance(r))
                .findFirst();
        return (rule.isPresent()) ? rule.get() : null;
    }
    
    /**
     * COUNT:
     * RFC 5545 iCalendar 3.3.10, page 41
     * 
     * The COUNT rule part defines the number of occurrences at which to
     * range-bound the recurrence.  The "DTSTART" property value always
     * counts as the first occurrence.
     */
    public ObjectProperty<Count> countProperty()
    {
        if (count == null)
        {
            count = new SimpleObjectProperty<>(this, RRuleElementType.COUNT.toString());
            // TODO - add listener to ensure COUNT and UNTIL are not both set
            // listener to ensure >0 throw new IllegalArgumentException("COUNT can't be less than 0. (" + count + ")");
//            else throw new IllegalArgumentException("can't set COUNT if UNTIL is already set.");
        }
        return count;
    }
    private ObjectProperty<Count> count;
    public Count getCount() { return (count == null) ? null : countProperty().get(); }
    public void setCount(Count count) { countProperty().set(count); }
    public void setCount(int count) { setCount(new Count(count)); }
    public RecurrenceRule3 withCount(Count count)
    {
        if (getCount() == null)
        {
            setCount(count);
            return this;
        } else
        {
            throw new IllegalArgumentException("Property previously set.  It can only occur once in the calendar component");
        }
    }    public RecurrenceRule3 withCount(int count)
    {
        if (getCount() == null)
        {
            setCount(count);
            return this;
        } else
        {
            throw new IllegalArgumentException("Property previously set.  It can only occur once in the calendar component");
        }
    }
    
    /** 
     * FREQUENCY
     * FREQ
     * RFC 5545 iCalendar 3.3.10 p40
     * 
     * required element
     * 
     * The FREQ rule part identifies the type of recurrence rule.  This
     * rule part MUST be specified in the recurrence rule.  Valid values
     * include SECONDLY, to specify repeating events based on an interval
     * of a second or more; MINUTELY, to specify repeating events based
     * on an interval of a minute or more; HOURLY, to specify repeating
     * events based on an interval of an hour or more; DAILY, to specify
     * repeating events based on an interval of a day or more; WEEKLY, to
     * specify repeating events based on an interval of a week or more;
     * MONTHLY, to specify repeating events based on an interval of a
     * month or more; and YEARLY, to specify repeating events based on an
     * interval of a year or more.
     */
    public ObjectProperty<Frequency2> frequencyProperty() { return frequency; }
    final private ObjectProperty<Frequency2> frequency = new SimpleObjectProperty<>(this, RRuleElementType.FREQUENCY.toString());
    public Frequency2 getFrequency() { return frequency.get(); }
    public void setFrequency(Frequency2 frequency) { this.frequency.set(frequency); }
    public void setFrequency(String frequency) { setFrequency(Frequency2.parse(frequency)); }
    public void setFrequency(FrequencyType frequency) { setFrequency(new Frequency2(frequency)); }
    public RecurrenceRule3 withFrequency(Frequency2 frequency)
    {
        if (getFrequency() == null)
        {
            setFrequency(frequency);
            return this;
        } else
        {
            throw new IllegalArgumentException("Property previously set.  It can only occur once in the calendar component");
        }
    }
    public RecurrenceRule3 withFrequency(String frequency)
    {
        if (getFrequency() == null)
        {
            setFrequency(frequency);
            return this;
        } else
        {
            throw new IllegalArgumentException("Property previously set.  It can only occur once in the calendar component");
        }
    }
    public RecurrenceRule3 withFrequency(FrequencyType frequency)
    {
        if (getFrequency() == null)
        {
            setFrequency(frequency);
            return this;
        } else
        {
            throw new IllegalArgumentException("Property previously set.  It can only occur once in the calendar component");
        }
    }
    
    /**
     * INTERVAL
     * RFC 5545 iCalendar 3.3.10, page 40
     * 
     * The INTERVAL rule part contains a positive integer representing at
     * which intervals the recurrence rule repeats.  The default value is
     * "1", meaning every second for a SECONDLY rule, every minute for a
     * MINUTELY rule, every hour for an HOURLY rule, every day for a
     * DAILY rule, every week for a WEEKLY rule, every month for a
     * MONTHLY rule, and every year for a YEARLY rule.  For example,
     * within a DAILY rule, a value of "8" means every eight days.
     */
    public ObjectProperty<Interval> intervalProperty()
    {
        if (interval == null)
        {
            interval = new SimpleObjectProperty<>(this, RRuleElementType.INTERVAL.toString());
        }
        return interval;
    }
    private ObjectProperty<Interval> interval;
    public Interval getInterval() { return (intervalProperty() == null) ? null : intervalProperty().get(); }
    public void setInterval(Interval interval) { intervalProperty().set(interval); }
    public void setInterval(Integer interval) { setInterval(new Interval(interval)); }
    public RecurrenceRule3 withInterval(int interval)
    {
        if (getInterval() == null)
        {
            setInterval(interval);
            return this;
        } else
        {
            throw new IllegalArgumentException("Property previously set.  It can only occur once in the calendar component");
        }
    }
    public RecurrenceRule3 withInterval(Interval interval)
    {
        if (getInterval() == null)
        {
            setInterval(interval);
            return this;
        } else
        {
            throw new IllegalArgumentException("Property previously set.  It can only occur once in the calendar component");
        }
    }
    
    /**
     * UNTIL:
     * RFC 5545 iCalendar 3.3.10, page 41
     * 
     * The UNTIL rule part defines a DATE or DATE-TIME value that bounds
     * the recurrence rule in an inclusive manner.  If the value
     * specified by UNTIL is synchronized with the specified recurrence,
     * this DATE or DATE-TIME becomes the last instance of the
     * recurrence.  The value of the UNTIL rule part MUST have the same
     * value type as the "DTSTART" property.  Furthermore, if the
     * "DTSTART" property is specified as a date with local time, then
     * the UNTIL rule part MUST also be specified as a date with local
     * time.  If the "DTSTART" property is specified as a date with UTC
     * time or a date with local time and time zone reference, then the
     * UNTIL rule part MUST be specified as a date with UTC time.  In the
     * case of the "STANDARD" and "DAYLIGHT" sub-components the UNTIL
     * rule part MUST always be specified as a date with UTC time.  If
     * specified as a DATE-TIME value, then it MUST be specified in a UTC
     * time format.  If not present, and the COUNT rule part is also not
     * present, the "RRULE" is considered to repeat forever
     */
    public SimpleObjectProperty<Until> untilProperty()
    {
        if (until == null)
        {
            until = new SimpleObjectProperty<>(this, RRuleElementType.UNTIL.toString());
            // TODO - add listener to ensure COUNT and UNTIL are not both set
            // TODO - LISTENER TO ENSURE UTC OR DATE
            // if ((DateTimeType.of(until) != DateTimeType.DATE) && (DateTimeType.of(until) != DateTimeType.DATE_WITH_UTC_TIME))
        }
        return until;
    }
    private SimpleObjectProperty<Until> until;
    public Until getUntil() { return (until == null) ? null : untilProperty().getValue(); }
    public void setUntil(Until until) { untilProperty().set(until); }
    public void setUntil(Temporal until) { setUntil(new Until(until)); }
    public void setUntil(String until) { setUntil(DateTimeUtilities.temporalFromString(until)); }
    public RecurrenceRule3 withUntil(Temporal until)
    {
        if (getUntil() == null)
        {
            setUntil(until);
            return this;
        } else
        {
            throw new IllegalArgumentException("Property previously set.  It can only occur once in the calendar component");
        }
    }
    public RecurrenceRule3 withUntil(String until)
    {
        if (getUntil() == null)
        {
            setUntil(until);
            return this;
        } else
        {
            throw new IllegalArgumentException("Property previously set.  It can only occur once in the calendar component");
        }
    }
    public RecurrenceRule3 withUntil(Until until)
    {
        if (getUntil() == null)
        {
            setUntil(until);
            return this;
        } else
        {
            throw new IllegalArgumentException("Property previously set.  It can only occur once in the calendar component");
        }
    }
    
    /**
     * Week Start
     * WKST:
     * RFC 5545 iCalendar 3.3.10, page 42
     * 
     * The WKST rule part specifies the day on which the workweek starts.
     * Valid values are MO, TU, WE, TH, FR, SA, and SU.  This is
     * significant when a WEEKLY "RRULE" has an interval greater than 1,
     * and a BYDAY rule part is specified.  This is also significant when
     * in a YEARLY "RRULE" when a BYWEEKNO rule part is specified.  The
     * default value is MO.
     */
    public SimpleObjectProperty<WeekStart> weekStartProperty()
    {
        if (weekStart == null)
        {
            weekStart = new SimpleObjectProperty<>(this, RRuleElementType.WEEK_START.toString());
            weekStart.addListener((obs, oldValue, newValue) ->
            {
                // bind to values in the Byxxx rules that use it
                addWeekStartBindings(lookupByRule(ByDay.class));
                addWeekStartBindings(lookupByRule(ByWeekNumber.class));                
            });
        }
        return weekStart;
    }
    private SimpleObjectProperty<WeekStart> weekStart;
    public WeekStart getWeekStart() { return (weekStart == null) ? null : weekStartProperty().get(); }
    public void setWeekStart(WeekStart weekStart) { weekStartProperty().set(weekStart); }
    public void setWeekStart(DayOfWeek weekStart) { weekStartProperty().set(new WeekStart(weekStart)); }
    public RecurrenceRule3 withWeekStart(WeekStart weekStart) { setWeekStart(weekStart); return this; }
    public RecurrenceRule3 withWeekStart(DayOfWeek weekStart) { setWeekStart(weekStart); return this; }
    
   // bind to values in the Byxxx rules that use Week Start
    private void addWeekStartBindings(ByRule<?> rule)
    {
        if (getWeekStart() != null)
        {
            if (rule instanceof ByDay)
            {
//                System.out.println(weekStartProperty());
//                System.out.println(getWeekStart());
                ((ByDay) rule).weekStartProperty().bind(getWeekStart().valueProperty());
            } else if (rule instanceof ByWeekNumber)
            {
                ((ByWeekNumber) rule).weekStartProperty().bind(getWeekStart().valueProperty());
            }
        }
    }
    
    /**
     * List of all recurrence rule elements found in object.
     * The list is unmodifiable.
     * 
     * @return - the list of elements
     */
    public List<RRuleElementType> elements()
    {
        List<RRuleElementType> populatedElements = Arrays.stream(RRuleElementType.values())
            .filter(p -> (p.getElement(this) != null))
            .collect(Collectors.toList());
      return Collections.unmodifiableList(populatedElements);
    }
    
    /** 
     * SORT ORDER
     * 
     * Element sort order map.  Key is element, value is the sort order.  The map is automatically
     * populated when parsing the content lines to preserve the existing property order.
     * 
     * When producing the content lines, if a element is not present in the map, it is put at
     * the end of the sorted ones in the order appearing in {@link #RecurrenceRuleElement} 
     * Generally, this map shouldn't be modified.  Only modify it when you want
     * to force a specific property order (e.g. unit testing).
     */
    public Map<RRuleElementType, Integer> elementSortOrder() { return elementSortOrder; }
    final private Map<RRuleElementType, Integer> elementSortOrder = new HashMap<>();
    private Integer elementCounter = 0;
    
    // TODO - THESE MUST GO TO REPEATABLE INTERFACE
//    /**
//     * The set of specific instances of recurring "VEVENT", "VTODO", or "VJOURNAL" calendar components
//     * specified individually in conjunction with "UID" and "SEQUENCE" properties.  Each instance 
//     * has a RECURRENCE ID with a value equal to the original value of the "DTSTART" property of 
//     * the recurrence instance.  The UID matches the UID of the parent calendar component.
//     * See 3.8.4.4 of RFC 5545 iCalendar
//     * 
//     * These are components that contain RECURRENCE-ID's for the recurrences defined by this rule.
//     * The RECURRENCE-ID Temporals should be removed from the streamRecurrences.
//     */
//    public Set<VComponentDisplayable<?>> recurrences() { return recurrences; }
//    private Set<VComponentDisplayable<?>> recurrences = new HashSet<>();
////    public void setRecurrences(Set<VComponent<?>> temporal) { recurrences = temporal; }
//    public RecurrenceRule3 withRecurrences(VComponentDisplayable<?>...v) { recurrences.addAll(Arrays.asList(v)); return this; }

    /*
     * CONSTRUCTORS
     */
    
    public RecurrenceRule3()
    {
        byRules = FXCollections.observableArrayList();
        
        // Listener that ensures user doesn't add same ByRule a second time.  Also keeps the byRules list sorted.
        byRules().addListener((ListChangeListener<? super ByRule<?>>) (change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList().stream().forEach(c ->
                    {
                        ByRule<?> newByRule = c;
                        long alreadyPresent = byRules()
                                .stream()
                                .map(r -> r.elementType())
                                .filter(p -> p.equals(c.elementType()))
                                .count();
                        if (alreadyPresent > 1)
                        {
                            throw new IllegalArgumentException("Can't add " + newByRule.getClass().getSimpleName() + " (" + c.elementType() + ") more than once.");
                        }
                        addWeekStartBindings(newByRule);
                    });
                    Collections.sort(byRules()); // sort additions
                }
            }
        });
//        frequency = new SimpleObjectProperty<Frequency2>(this, RecurrenceRuleElement.FREQUENCY.toString(), new Frequency2());
//        System.out.println("RecurrenceRule3:" + byRules() + " " + getFrequency());
        // TODO - FREQUENCY IS NULL - CAN'T BIND TO BYRULES
        
    }

//    /** construct new object by parsing property line */
//    @Deprecated
//    public RecurrenceRule3(String contentLine)
//    {
//        this();
//        parseContent(contentLine);
//    }
    
    /** Parse component from content line */
    @Override
    public void parseContent(String contentLine)
    {
        ICalendarUtilities.propertyLineToParameterMap(contentLine)
        .entrySet()
        .stream()
//        .peek(System.out::println)
        .forEach(entry ->
        {
            RRuleElementType element = RRuleElementType.enumFromName(entry.getKey());
            if (element != null)
            {
                element.parse(this, entry.getValue());
                elementSortOrder().put(element, elementCounter);
                elementCounter += 100; // add 100 to allow insertions in between
            } else
            {
                throw new IllegalArgumentException("Unsupported Recurrence Rule element: " + entry.getKey());                        
            }
        });
    }

    // Copy constructor
    public RecurrenceRule3(RecurrenceRule3 source)
    {
        this();
        copyRecurrenceRuleFrom(source);
    }
    
    /** Copy elements from source into this recurrence rule, making a copy of source */
    public void copyRecurrenceRuleFrom(RecurrenceRule3 source)
    {
        elementSortOrder().putAll(source.elementSortOrder());
        source.elements().forEach(p -> p.copyElement(source, this));
    }
    
//    private void setupFrequencyListener()
//    {
//        // add listener to bind interval and byRules to fields in Frequency
//        frequencyProperty().addListener(obs ->
//        {
//            interval.bind(getFrequency().intervalProperty());
//            Bindings.bindContentBidirectional(byRules(), getFrequency().byRules());            
//        });
//    }
    
//    @Override
//    public void parse(String propertyString)
//    {
//        Map<String, String> p = ICalendarUtilities.PropertyLineToParameterMap(propertyString);
//        p.entrySet().stream().forEach(e ->
//        {
//            RRuleProperty.propertyFromName(e.getKey()).setValue(this, e.getValue());
//        });
//    }
    
//    @Override
//    public String toString()
//    {
//        return super.toString() + "," + toContent();
//    }
    
    /** Deep copy all fields from source to destination */
//    @Deprecated // revise with looping through enum
//    private static void copy(RRule source, RRule destination)
//    {
//        if (source.getCount() > 0)
//        {
//            destination.setCount(source.getCount());
//        }
//        if (source.getFrequency() != null)
//        {
//            try {
//                Frequency newFreqency = source.getFrequency().getClass().newInstance();
//                FrequencyUtilities.copy(source.getFrequency(), newFreqency);
//                destination.setFrequency(newFreqency);
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        if (source.getUntil() != null)
//        {
//            destination.setUntil(source.getUntil());
//        }
//        Iterator<VComponent<?>> i = source.recurrences().iterator();
//        while (i.hasNext())
//        {
//            destination.recurrences().add(i.next());
//        }
//    }
    
//    /** Deep copy all fields from this to destination */
//    public void copyTo(RRule destination)
//    {
//        copy(this, destination);
//    }

//    public TemporalAdjuster adjuster()
//    {
//        int interval = (getInterval() == null) ? Interval.DEFAULT_INTERVAL : getInterval().getValue();
//        return (temporal) -> temporal.plus(interval, getFrequency().getValue().getChronoUnit());
//    }

    /**
     * STREAM RECURRENCES
     * 
     * Resulting stream of start date/times by applying Frequency temporal adjuster and all, if any,
     *
     * Starts on startDateTime, which MUST be a valid occurrence date/time, but not necessarily the
     * first date/time (DTSTART) in the sequence. A later startDateTime can be used to more efficiently
     * get to later dates in the stream.
     * 
     * @param start - starting point of stream (MUST be a valid occurrence date/time)
     * @return
     */
    public Stream<Temporal> streamRecurrences(Temporal start)
    {
//        getFrequency().setChronoUnit(getFrequency().getValue().getChronoUnit()); // start with Frequency ChronoUnit when making a stream
//        Stream<Temporal> stream = Stream.iterate(start, a -> a.with(adjuster()));
        int interval = (getInterval() == null) ? Interval.DEFAULT_INTERVAL : getInterval().getValue();
        Stream<Temporal> frequencyStream = getFrequency().streamRecurrences(start, interval);
        
        Stream<Temporal> recurrenceStream = frequencyStream
                .flatMap(value ->
                {
                    // process byRules
                    chronoUnit = getFrequency().getValue().getChronoUnit(); // initial chronoUnit from Frequency
                    myStream = Arrays.asList(value).stream();
                    byRules().stream()
                            .sorted()
                            .forEach(rule ->
                            {
//                                System.out.println("rule:" + rule.elementType() + " " + chronoUnit + " " + rule.getClass().getSimpleName());
                                myStream = rule.streamRecurrences(myStream, chronoUnit, start);
                                chronoUnit = rule.getChronoUnit();
                            });
                    // must filter out too early recurrences
                    return myStream.filter(r -> ! DateTimeUtilities.isBefore(r, start));
                });
        
        if (getCount() != null)
        {
            return recurrenceStream.limit(getCount().getValue());
        } else if (getUntil() != null)
        {
//            return frequency
//                    .stream(startDateTime)
//                    .takeWhile(a -> a.isBefore(getUntil())); // available in Java 9
//            Temporal convertedUntil = DateTimeType.changeTemporal(getUntil(), DateTimeType.of(start));

            ZoneId zone = (start instanceof ZonedDateTime) ? ((ZonedDateTime) start).getZone() : null;
            Temporal convertedUntil = DateTimeType.of(start).from(getUntil().getValue(), zone);
            return takeWhile(recurrenceStream, a -> ! DateTimeUtilities.isAfter(a, convertedUntil));
        }

//        ChronoUnit chronoUnit = getFrequency().getValue().getChronoUnit(); // initial chronoUnit from Frequency
//        Stream<Temporal> recurrenceStream = null;
//        Iterator<Temporal> frequencyTemporalIterator = frequencyStream.iterator();
//        while (frequencyTemporalIterator.hasNext())
//        {
//            Iterator<ByRule<?>> ruleIterator = byRules().iterator();
//            recurrenceStream = Arrays.asList(frequencyTemporalIterator.next()).stream();
//            while (ruleIterator.hasNext())
//            {
//                ByRule<?> rule = ruleIterator.next();
//                Stream<Temporal> newStream = rule.streamRecurrences(recurrenceStream, chronoUnit, start);
//                recurrenceStream = newStream;
//                chronoUnit = rule.getChronoUnit();
//                System.out.println(chronoUnit);
//            }
//        }
        
//        Iterator<ByRule<?>> rulesIterator = byRules()
//                .stream()
//                .sorted()
//                .iterator();
//        while (rulesIterator.hasNext())
//        {
//            ByRule<?> rule = rulesIterator.next();
////            stream = rule.streamRecurrences(stream, getFrequency().chronoUnitProperty(), start);
////            recurrenceStream = rule.streamRecurrences(recurrenceStream, chronoUnit, start);
//
////            recurrenceStream = recurrenceStream
////                    .flatMap(value -> streamRecurrences(recurrenceStream, chronoUnit, start);
//            chronoUnit = rule.getChronoUnit(); // update chronoUnit
//        }
        return recurrenceStream;
    }
    private ChronoUnit chronoUnit; // must be field instead of local variable due to use in lambda expression
    private Stream<Temporal> myStream; // must be field instead of local variable due to use in lambda expression
    
    @Override
    public boolean equals(Object obj)
    {
//        System.out.println("rrule equal:" + obj + " " + (obj.getClass() != getClass()));
        if (obj == this) return true;
//        System.out.println("rrule equal:" + obj + " " + obj.getClass() + " " + getClass());
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        RecurrenceRule3 testObj = (RecurrenceRule3) obj;

        List<RRuleElementType> myElements = elements();
        List<RRuleElementType> testElements = testObj.elements();
//        System.out.println("rrule equal:" + obj + " " + (obj.getClass() != getClass()));
        
//        System.out.println("rrule equal:" + myElements.size() + " " + testElements.size());
        boolean isSameNumberOfElements = myElements.size() == testElements.size();
        if (! isSameNumberOfElements)
        {
            return false;
        }
        boolean elementsEqual = true;
        for (int i=0; i<myElements.size(); i++)
        {
            if (! myElements.get(i).getElement(this).equals(testElements.get(i).getElement(testObj)))
            {
                System.out.println("Recurrence rule not equal:" + myElements.get(i).getElement(this).toContent() +
                        " " + testElements.get(i).getElement(testObj).toContent());
                elementsEqual = false;
            }
        }
        return elementsEqual;
        
//        boolean propertiesEquals = Arrays.stream(RRuleEnum.values())
////                .peek(e -> System.out.println(e.toString() + " equals:" + e.isPropertyEqual(this, testObj)))
//                .map(e -> e.isPropertyEqual(this, testObj))
//                .allMatch(b -> b == true);
//        boolean recurrencesEquals = (recurrences() == null) ? (testObj.recurrences() == null) : recurrences().equals(testObj.recurrences());
//        return propertiesEquals && recurrencesEquals;
//        System.out.println("propertiesEquals" + propertiesEquals);
//        boolean countEquals = getCount().equals(testObj.getCount());
//        boolean frequencyEquals = getFrequency().equals(testObj.getFrequency()); // RRule requires a frequency
//        System.out.println("untils:" + getUntil() + " " + testObj.getUntil() + ((getUntil() != null) ? getUntil().hashCode() : "")
//                + " " + ((testObj.getUntil() != null) ? testObj.getUntil().hashCode() : ""));
//        boolean untilEquals = (getUntil() == null) ? (testObj.getUntil() == null) : getUntil().equals(testObj.getUntil());
//
//        System.out.println("RRule " + countEquals + " " + frequencyEquals + " " + recurrencesEquals + " " + untilEquals);
//        return countEquals && frequencyEquals && recurrencesEquals && untilEquals;
    }
//    
//    @Override
//    public int hashCode()
//    {
//        int hash = 7;
//        hash = (31 * hash) + getCount().hashCode();
//        hash = (31 * hash) + getFrequency().hashCode();
//        hash = (31 * hash) + ((recurrences() == null) ? 0 : recurrences().hashCode());
//        return hash;
//    }
    
    /**
     * Checks to see if object contains required properties.  Returns empty string if it is
     * valid.  Returns string of errors if not valid.
     */
//    public String makeErrorString(VComponent<?> parent)
//    {
//        StringBuilder builder = new StringBuilder();
//        if (recurrences() != null)
//        {
//            recurrences().stream()
//                    .filter(r -> ! DateTimeType.of(r.getDateTimeRecurrence()).equals(parent.getDateTimeType()))
//                    .forEach(r -> 
//                    {
//                        builder.append(System.lineSeparator()
//                                + "Invalid RRule.  Recurrence ("
//                                + r.getDateTimeRecurrence() + ", " + r.getDateTimeType()
//                                + ") must have same DateTimeType as parent ("
//                                + parent.getDateTimeType() + ")");
//                    });
//        }
//        if (getUntil() != null)
//        {
////            Temporal convertedUntil = DateTimeType.changeTemporal(getUntil(), parent.getDateTimeType());
//            Temporal convertedUntil = parent.getDateTimeType().from(getUntil(), parent.getZoneId());
//            if (DateTimeUtilities.isBefore(convertedUntil, parent.getDateTimeStart())) builder.append(System.lineSeparator() + "Invalid RRule.  UNTIL can not come before DTSTART");
//        }
//        if ((getCount() == null) || (getCount() < 0))
//        {
//            builder.append(System.lineSeparator() + "Invalid RRule.  COUNT must not be less than 0");
//        }
//        if (getFrequency() == null)
//        {
//            builder.append(System.lineSeparator() + "Invalid RRule.  FREQ must not be null");
//        } else builder.append(getFrequency().makeErrorString());
//        return builder.toString();
//    }

    /** Return new RRule with its properties set by parsing iCalendar compliant RRULE string */
//    @Deprecated // replace with generic static method for dividing all properties into list of parameters and value
//    public static RRule parseRRule(String propertyString)
//    {
//        return new RRule(propertyString);
//        rrule.parse(propertyString);
//        return rrule;
//        Integer interval = null;
//        
//        // Parse string
//        for (String element : rRuleString.split(";"))
//        {
//            String property = element.substring(0, element.indexOf("="));
//            String value = element.substring(element.indexOf("=") + 1).trim();
//            if (property.equals(rrule.frequencyProperty().getName()))
//            { // FREQ
//                Frequency freq = Frequency.FrequencyType
//                        .valueOf(value)
//                        .newInstance();
//                rrule.setFrequency(freq);
//                if (interval != null) rrule.getFrequency().setInterval(interval);
//            } else if (property.equals(COUNT_NAME))
//            { // COUNT
//                rrule.setCount(Integer.parseInt(value));
//            } else if (property.equals(UNTIL_NAME))
//            { // UNTIL
//                Temporal dateTime = DateTimeUtilities.parse(value);
//                rrule.setUntil(dateTime);
//            } else if (property.equals(INTERVAL_NAME))
//            { // INTERVAL
//                if (rrule.getFrequency() != null)
//                {
//                    rrule.getFrequency().setInterval(Integer.parseInt(value));
//                } else
//                {
//                    interval = Integer.parseInt(value);
//                }
//            } else
//            {
//                for (ByRuleType b : ByRuleType.values())
//                {
//                    if (property.equals(b.toString()))
//                    {
//                        Rule rule = null;
//                        try {
//                            rule = b.newInstance(value);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        rrule.getFrequency().addByRule(rule);
//                    }
//                }
//            }            
//        }
//        return rrule;
//    }

    /**
     * Determines if recurrence set is goes on forever
     * 
     * @return - true if recurrence set is infinite, false otherwise
     */
    public boolean isInfinite()
    {
        return ((getCount() == null) && (getUntil() == null));
    }
    
    /** Stream of date/times made after applying all modification rules.
     * Stream is infinite if COUNT or UNTIL not present or ends when COUNT or UNTIL condition
     * is met.
     * Starts on startDateTime, which MUST be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. */
//    public Stream<Temporal> streamRecurrence(Temporal start)
//    {
//        // filter out recurrences
//        Stream<Temporal> filteredStream = (recurrences().size() == 0) ?
//                getFrequency().streamRecurrences(start) :
//                getFrequency().streamRecurrences(start).filter(t -> 
//                {
//                    return ! recurrences().stream()
//                            .map(v -> v.getRecurrenceId().getValue())
//                            .filter(t2 -> t2.equals(t))
//                            .findAny()
//                            .isPresent();
//                }); // ! getRecurrences().contains(t))
//        if (getCount() > 0)
//        {
//            return filteredStream.limit(getCount());
//        } else if (getUntil() != null)
//        {
////            return frequency
////                    .stream(startDateTime)
////                    .takeWhile(a -> a.isBefore(getUntil())); // available in Java 9
////            Temporal convertedUntil = DateTimeType.changeTemporal(getUntil(), DateTimeType.of(start));
//            ZoneId zone = (start instanceof ZonedDateTime) ? ((ZonedDateTime) start).getZone() : null;
//            Temporal convertedUntil = DateTimeType.of(start).from(getUntil(), zone);
//            return takeWhile(filteredStream, a -> ! DateTimeUtilities.isAfter(a, convertedUntil));
//        }
//        return filteredStream;
//    };
    
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
    
    @Override
    public String toContent()
    {
        return elements().stream()
                .sorted((Comparator<? super RRuleElementType>) (e1, e2) -> 
                {
                    Integer s1 = elementSortOrder().get(e1);
                    Integer sort1 = (s1 == null) ? Integer.MAX_VALUE : s1;
                    Integer s2 = elementSortOrder().get(e2);
                    Integer sort2 = (s2 == null) ? Integer.MAX_VALUE : s2;
                    return sort1.compareTo(sort2);
                })
                // all element objects MUST override toString
                .map(e -> 
                {
                    RRuleElement<?> element = e.getElement(this);
                    return element.toContent();
//                    return e.getConverter().toString(element);
                })
                .collect(Collectors.joining(";"));
    }
    
    @Override
    public String toString()
    {
        return super.toString() + ", " + toContent();
    }


    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((byRules == null) ? 0 : byRules.hashCode());
        result = prime * result + ((count == null) ? 0 : count.hashCode());
        result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
        result = prime * result + ((interval == null) ? 0 : interval.hashCode());
        result = prime * result + ((until == null) ? 0 : until.hashCode());
        result = prime * result + ((weekStart == null) ? 0 : weekStart.hashCode());
        return result;
    }
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        RecurrenceRule3 other = (RecurrenceRule3) obj;
//        if (byRules == null)
//        {
//            if (other.byRules != null)
//                return false;
//        } else if (!byRules.equals(other.byRules))
//            return false;
//        if (count == null)
//        {
//            if (other.count != null)
//                return false;
//        } else if (!count.equals(other.count))
//            return false;
//        if (frequency == null)
//        {
//            if (other.frequency != null)
//                return false;
//        } else if (!frequency.equals(other.frequency))
//            return false;
//        if (interval == null)
//        {
//            if (other.interval != null)
//                return false;
//        } else if (!interval.equals(other.interval))
//            return false;
//        if (until == null)
//        {
//            if (other.until != null)
//                return false;
//        } else if (!until.equals(other.until))
//            return false;
//        if (weekStart == null)
//        {
//            if (other.weekStart != null)
//                return false;
//        } else if (!weekStart.equals(other.weekStart))
//            return false;
//        return true;
//    }
    
    public static RecurrenceRule3 parse(String propertyContent)
    {
        RecurrenceRule3 property = new RecurrenceRule3();
        property.parseContent(propertyContent);
        return property;
    }
}
