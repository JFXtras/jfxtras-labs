package jfxtras.labs.icalendar.properties.recurrence.rrule.freq;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByRule;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.FrequencyUtilities.FrequencyParameter;

public abstract class FrequencyAbstract<T> implements Frequency {
    
    /** INTERVAL: (RFC 5545 iCalendar 3.3.10, page 40) number of frequency periods to pass before new appointment */
    // Uses lazy initialization of property because often interval stays as the default value of 1
    @Override
    public IntegerProperty intervalProperty()
    {
        if (interval == null) interval = new SimpleIntegerProperty(this, "interval", _interval);
        return interval;
    }
    private IntegerProperty interval;
    @Override public Integer getInterval() { return (interval == null) ? _interval : interval.getValue(); }
    private int _interval = 1;
    @Override public void setInterval(Integer i)
    {
        if (i > 0)
        {
            if (interval == null)
            {
                _interval = i;
            } else
            {
                interval.set(i);
            }
        } else
        {
            throw new IllegalArgumentException("INTERVAL can't be less than 1. (" + i + ")");
        }
    }
    public T withInterval(int interval) { setInterval(interval); return (T) this; }

    /** BYxxx Rules 
     * Collection of BYxxx rules that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
     * Each BYxxx rule can only occur once */
    @Override public Collection<ByRule> byRules() { return byRules; }
    private final Collection<ByRule> byRules = new TreeSet<>();
    
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

    /** Add varargs of ByRules to Frequency 
     * Collection of BYxxx rules that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
     * Each BYxxx rule can only occur once */
    public T withByRules(ByRule...byRules)
    {
        for (ByRule myByRule : byRules)
        {
//            byRules().put(ByRuleParameter.propertyFromByRule(r), r);
            byRules().add(myByRule);
        }
        return (T) this;
    }

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
    ObjectProperty<ChronoUnit> chronoUnitProperty() { return chronoUnit; }
    ChronoUnit getChronoUnit() { return chronoUnit.get(); };
    private ObjectProperty<ChronoUnit> chronoUnit = new SimpleObjectProperty<ChronoUnit>();
    public void setChronoUnit(ChronoUnit chronoUnit) { this.chronoUnit.set(chronoUnit); }
    
    @Override
    public FrequencyParameter frequencyType() { return frequencyType; }
    final private FrequencyParameter frequencyType;
    
    @Override
    public TemporalAdjuster adjuster() { return (temporal) -> temporal.plus(getInterval(), frequencyType.getChronoUnit()); }
    
    // CONSTRUCTOR
    public FrequencyAbstract(FrequencyParameter frequencyType)
    {
        this.frequencyType = frequencyType;
//        this.initialChronoUnit = chronoUnit.get();
        setChronoUnit(frequencyType.getChronoUnit());
//        ListChangeListener<? super ByRule> listchange = (change) ->
//        {
//            while (change.next())
//            {
//                if (change.wasAdded())
//                {
//                    change.getAddedSubList().stream().forEach(newByRule ->
//                    {
//                        boolean alreadyPresent = byRules()
//                                .stream()
//                                .anyMatch(myByRule -> ByRuleParameter.propertyFromByRule(myByRule) == ByRuleParameter.propertyFromByRule(newByRule));
//                        System.out.println("change byRule:" + alreadyPresent);
//                        if (alreadyPresent)
//                        {
//                            throw new IllegalArgumentException("Can't add BYxxx rule (" + newByRule.getClass().getSimpleName() + ") more than once.");
//                        }
//                        Collections.sort(byRules());
//                    });
//                }
//            }
//        };
//        byRules().addListener(listchange);
    }

    @Override
    public Stream<Temporal> stream(Temporal start)
    {
        setChronoUnit(frequencyType.getChronoUnit()); // start with Frequency ChronoUnit when making a stream
        Stream<Temporal> stream = Stream.iterate(start, a -> a.with(adjuster()));
        Iterator<ByRule> rulesIterator = byRules()
                .stream()
                .sorted()
                .iterator();
        while (rulesIterator.hasNext())
        {
            ByRule rule = rulesIterator.next();
            stream = rule.stream(stream, chronoUnitProperty(), start);
        }
        return stream;
    }
    
//    @Override
//    public boolean isInstance(Temporal start, Temporal timeAdjustedSelection)
//    {
//        
//    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        Frequency testObj = (Frequency) obj;
        
        boolean intervalEquals = (getInterval() == null) ?
                (testObj.getInterval() == null) : getInterval().equals(testObj.getInterval());
        System.out.println("getInterval " + getInterval() + " " + testObj.getInterval());
        boolean rulesEquals = byRules().equals(testObj.byRules());
        System.out.println("frequency " + intervalEquals + " " + rulesEquals);
        return intervalEquals && rulesEquals;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = (31 * hash) + getInterval().hashCode();
        hash = (31 * hash) + byRules().hashCode();
        return hash;
    }
    
    @Override
    public String toString()
    {
        return frequencyType().toString();
//        StringBuilder builder = new StringBuilder("FREQ=" + frequencyType().toString());
//        if (getInterval() > 1) builder.append(";INTERVAL=" + getInterval());
//        return builder.toString();
    }

}
