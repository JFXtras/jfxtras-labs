package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.stream.Stream;

/**
 * FREQUENCY
 * FREQ
 * RFC 5545 iCalendar 3.3.10 p40
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
 * 
 * Frequency value.  Possible values include:
 *  <br>
 * {@link FrequencyType#SECONDLY } <br>
 * {@link FrequencyType#MINUTELY } <br>
 * {@link FrequencyType#HOURLY } <br>
 * {@link FrequencyType#DAILY } <br>
 * {@link FrequencyType#WEEKLY } <br>
 * {@link FrequencyType#MONTHLY } <br>
 * {@link FrequencyType#YEARLY }
 * 
 * @author David Bal
 * 
 * @see FrequencyType
 */
public class Frequency extends RRuleElementBase<FrequencyType, Frequency>
{
    void setValue(String frequency) { parseContent(frequency); }
    public Frequency withValue(String frequency) { setValue(frequency); return this; }

//    /**
//     * FREQUENCY value
//     */
//    public ObjectProperty<FrequencyType> valueProperty() { return value; }
//    public FrequencyType getValue() { return value.get(); }
//    private ObjectProperty<FrequencyType> value = new SimpleObjectProperty<>(this, ElementType.FREQUENCY.toString());
//    public void setValue(FrequencyType value) { valueProperty().set(value); }
//    public void setValue(String value) { setValue(FrequencyType.valueOf(value)); }
//    public Frequency2 withValue(FrequencyType value) { setValue(value); return this; }
//    public Frequency2 withValue(String value) { setValue(value); return this; }
    
//    /**
//     * INTERVAL
//     * This value MUST be bound to the Interval value in {@link RecurrenceRule3#intervalProperty}
//     */
//    public IntegerProperty intervalProperty() { return interval; }
//    private IntegerProperty interval;
//    private Integer getInterval() { return (interval == null) ? null : interval.getValue(); }
//    private int _interval = 1;
//    public void setInterval(Integer i)
//    {
//        if (i > 0)
//        {
//            if (interval == null)
//            {
//                _interval = i;
//            } else
//            {
//                interval.set(i);
//            }
//        } else
//        {
//            throw new IllegalArgumentException("INTERVAL can't be less than 1. (" + i + ")");
//        }
//    }
//    public Frequency2 withInterval(int interval) { setInterval(interval); return this; }

//    /** BYxxx Rules 
//     * Collection of BYxxx rules that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
//     * Each BYxxx rule can only occur once */
//    public ObservableList<ByRule> byRules() { return byRules; }
//    private final ObservableList<ByRule> byRules = FXCollections.observableArrayList();
//    public Frequency2 withByRules(ByRule...byRules)
//    {
//        for (ByRule myByRule : byRules)
//        {
//            byRules().add(myByRule);
//        }
//        return this;
//    }
    
//    public ByRule lookupByRule(ByRuleType byRuleType)
//    {
//        Optional<ByRule> rule = byRules()
//                .stream()
//                .filter(r -> r.byRuleType() == byRuleType)
//                .findFirst();
//        return (rule.isPresent()) ? rule.get() : null;
//    }
//    @Override public ObservableSet<ByRule> byRules() { return byRules; }
//    private final ObservableSet<ByRule> byRules = FXCollections.observableSet(new TreeSet<>());
    
//    @Override public Map<ByRuleParameter, ByRule> byRules() { return byRules; }
//    private final Map<ByRuleParameter, ByRule> byRules = new HashMap<>();
//    @Override public void addByRule(Rule byRule)
//    {
//        boolean alreadyPresent = getByRules().stream().anyMatch(a -> a.getClass() == byRule.getClass());
//        if (alreadyPresent)
//        {
//            throw new IllegalArgumentException("Can't add BYxxx rule (" + byRule.getClass().getSimpleName() + ") more than once.");
//        }
//        getByRules().add(byRule);
//        Collections.sort(getByRules());
//    }




    /** Time unit of last rule applied.  It represents the time span to apply future changes to the output stream of date/times
     * For example:
     * 
     * following FREQ=WEEKLY it is WEEKS
     * following FREQ=YEARLY it is YEARS
     * following FREQ=YEARLY;BYWEEKNO=20 it is WEEKS
     * following FREQ=YEARLY;BYMONTH=3 it is MONTHS
     * following FREQ=YEARLY;BYMONTH=3;BYDAY=TH it is DAYS
     * 
     * Note: ChronoUnit is wrapped in an ObjectProperty to enable receiving classes to have the
     * reference to the object and make changes to it.  If I passed a ChronoUnit object, which is an enum,
     * changes are not propagated back.  In that case, I would need a reference to the Frequency object that owns
     * it.  The ObjectProperty wrapper is easier.
     */
//    public ObjectProperty<ChronoUnit> chronoUnitProperty() { return chronoUnit; }
//    ChronoUnit getChronoUnit() { return chronoUnit.get(); };
//    private ObjectProperty<ChronoUnit> chronoUnit = new SimpleObjectProperty<ChronoUnit>();
//    public void setChronoUnit(ChronoUnit chronoUnit) { this.chronoUnit.set(chronoUnit); }

//    public ChronoUnit chronoUnitProperty() { return chronoUnit; }
//      public ChronoUnit getChronoUnit() { return chronoUnit; };
//      final private ChronoUnit chronoUnit;


    
//    public FrequencyType frequencyType() { return frequencyType; }
//    private FrequencyType frequencyType;
    
    /** TemporalAdjuster to enable frequency stream in {@link RecurrenceRule2#streamRecurrences(Temporal)} */
    TemporalAdjuster adjuster(int interval)
    {
        return (temporal) -> temporal.plus(interval, getValue().getChronoUnit());
    }
    
    /*
     * CONSTRUCTORS
     */
    public Frequency(FrequencyType frequencyType)
    {
        this();
        setValue(frequencyType);
//        chronoUnit = frequencyType.getChronoUnit();
//        setChronoUnit(frequencyType.getChronoUnit());
        
//        // Listener that ensures user doesn't add same ByRule a second time.  Also keeps the byRules list sorted.
//        byRules().addListener((ListChangeListener<? super ByRule>) (change) ->
//        {
//            while (change.next())
//            {
//                if (change.wasAdded())
//                {
//                    change.getAddedSubList().stream().forEach(c ->
//                    {
//                        ByRule newByRule = c;
//                        long alreadyPresent = byRules()
//                                .stream()
//                                .map(r -> r.byRuleType())
//                                .filter(p -> p.equals(c.byRuleType()))
//                                .count();
//                        if (alreadyPresent > 1)
//                        {
//                            throw new IllegalArgumentException("Can't add " + newByRule.getClass().getSimpleName() + " (" + c.byRuleType() + ") more than once.");
//                        }
//                    });
//                    Collections.sort(byRules()); // sort additions
//                }
//            }
//        });
    }
    
//    // Copy constructor
//    public Frequency2(Frequency source)
//    {
//        this(source.frequencyType());
//        source.byRules().stream().forEach(b -> byRules().add(b.byRuleType().newInstance(b))); // copy each ByRule
//    }
    
    public Frequency()
    {
        super();
    }
    
    public Frequency(Frequency source)
    {
        this();
        setValue(source.getValue());
    }
    
    /** STREAM 
     * Resulting stream of start date/times by applying Frequency temporal adjuster and all, if any,
     * Rules.
     * Starts on startDateTime, which MUST be a valid occurrence date/time, but not necessarily the
     * first date/time (DTSTART) in the sequence. A later startDateTime can be used to more efficiently
     * get to later dates in the stream.
     * 
     * @param start - starting point of stream (MUST be a valid occurrence date/time)
     * @return
     */
    public Stream<Temporal> streamRecurrences(Temporal start, int interval)
    {
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(interval, getValue().getChronoUnit());
        return Stream.iterate(start, a -> a.with(adjuster));
    }
//        setChronoUnit(getValue().getChronoUnit()); // start with Frequency ChronoUnit when making a stream
//        return Stream.iterate(start, a -> a.with(adjuster()));
////        Iterator<ByRule> rulesIterator = byRules()
////                .stream()
////                .sorted()
////                .iterator();
////        while (rulesIterator.hasNext())
////        {
////            ByRule rule = rulesIterator.next();
////            stream = rule.stream(stream, chronoUnitProperty(), start);
////        }
////        return stream;
//    }
    
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (obj == this) return true;
//        if((obj == null) || (obj.getClass() != getClass())) {
//            return false;
//        }
//        Frequency testObj = (Frequency) obj;
//        
//        boolean intervalEquals = getInterval().equals(testObj.getInterval());
//        System.out.println("getInterval " + getInterval() + " " + testObj.getInterval());
//        boolean rulesEquals = byRules().equals(testObj.byRules());
//        System.out.println("frequency " + intervalEquals + " " + rulesEquals);
//        return intervalEquals && rulesEquals;
//    }
//    
//    @Override
//    public int hashCode()
//    {
//        int hash = 7;
//        hash = (31 * hash) + getInterval().hashCode();
//        hash = (31 * hash) + byRules().hashCode();
//        return hash;
//    }
    
//    @Override
//    public String toString()
//    {
//        return super.toString() + ", " + toContent(); 
////        StringBuilder builder = new StringBuilder("FREQ=" + frequencyType().toString());
////        if (getInterval() > 1) builder.append(";INTERVAL=" + getInterval());
////        return builder.toString();
//    }
    
//    @Override
//    public int hashCode()
//    {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + ((getValue() == null) ? 0 : getValue().hashCode());
//        return result;
//    }
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        Frequency2 other = (Frequency2) obj;
//        if (getValue() == null)
//        {
//            if (other.getValue() != null)
//                return false;
//        } else if (!getValue().equals(other.getValue()))
//            return false;
//        return true;
//    }

    @Override
    public void parseContent(String content)
    {
        setValue(FrequencyType.valueOf(content.toUpperCase()));
    }
    
    public static Frequency parse(String content)
    {
        Frequency element = new Frequency();
        element.parseContent(content);
        return element;
    }
}
