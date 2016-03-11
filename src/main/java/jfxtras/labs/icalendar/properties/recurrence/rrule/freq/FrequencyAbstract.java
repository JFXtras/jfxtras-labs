package jfxtras.labs.icalendar.properties.recurrence.rrule.freq;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.Rule;

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
    @Override public List<Rule> getByRules() { return byRules; }
    private List<Rule> byRules = new ArrayList<Rule>();
    @Override public void addByRule(Rule byRule)
    {
        boolean alreadyPresent = getByRules().stream().anyMatch(a -> a.getClass() == byRule.getClass());
        if (alreadyPresent)
        {
            throw new IllegalArgumentException("Can't add BYxxx rule (" + byRule.getClass().getSimpleName() + ") more than once.");
        }
        getByRules().add(byRule);
        Collections.sort(getByRules());
    }

    /** Add varargs of ByRules to Frequency 
     * Collection of BYxxx rules that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
     * Each BYxxx rule can only occur once */
    public T withByRules(Rule...byRules)
    {
        for (Rule r : byRules)
        {
            addByRule(r);
        }
        return (T) this;
    }

    /** Time unit of last rule applied.  It represents the time span to apply future changes to the output stream of date/times
     * For example:
     * following FREQ=WEEKLY it is WEEKS
     * following FREQ=YEARLY it is YEARS
     * following FREQ=YEARLY;BYWEEKNO=20 it is WEEKS
     * following FREQ=YEARLY;BYMONTH=3 it is MONTHS
     * following FREQ=YEARLY;BYMONTH=3;BYDAY=TH it is DAYS
     */
    @Override
    public ObjectProperty<ChronoUnit> getChronoUnit() { return chronoUnit; };
    private ObjectProperty<ChronoUnit> chronoUnit;
    private final ChronoUnit initialChronoUnit;
    @Override
    public void setChronoUnit(ObjectProperty<ChronoUnit> chronoUnit)
    {
        switch (chronoUnit.get())
        {
        case DAYS:
        case MONTHS:
        case WEEKS:
        case YEARS:
            this.chronoUnit = chronoUnit;
            break;
        case HOURS:
        case MINUTES:
        case SECONDS:
            throw new RuntimeException("ChronoUnit not implemented: " + chronoUnit);
        default:
            throw new RuntimeException("Invalid ChronoUnit: " + chronoUnit);
        }
    }
    
    
    @Override
    public FrequencyType frequencyType() { return frequencyType; }
    final private FrequencyType frequencyType;

    // CONSTRUCTOR
    public FrequencyAbstract(FrequencyType frequencyType, ObjectProperty<ChronoUnit> chronoUnit)
    {
        this.frequencyType = frequencyType;
        this.initialChronoUnit = chronoUnit.get();
        setChronoUnit(chronoUnit);
    }

    @Override
    public Stream<Temporal> stream(Temporal start)
    {
        getChronoUnit().set(initialChronoUnit); // start with Frequency ChronoUnit when making a stream
        Stream<Temporal> stream = Stream.iterate(start, a -> a.with(adjuster()));
        Iterator<Rule> rulesIterator = getByRules().stream().sorted().iterator();
        while (rulesIterator.hasNext())
        {
            Rule rule = rulesIterator.next();
            stream = rule.stream(stream, getChronoUnit(), start);
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
        boolean rulesEquals;
        if (getByRules().size() == testObj.getByRules().size())
        {
            List<Boolean> rulesEqualsArray = new ArrayList<Boolean>();
            for (int i=0; i<getByRules().size(); i++)
            {
                boolean e = getByRules().get(i).equals(testObj.getByRules().get(i));
                rulesEqualsArray.add(e);
            }
            rulesEquals = rulesEqualsArray.stream().allMatch(a -> a == true );
        } else
        {
            rulesEquals = false;
        }
        System.out.println("frequency " + intervalEquals + " " + rulesEquals);
        return intervalEquals && rulesEquals;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = (31 * hash) + getInterval().hashCode();
        hash = (31 * hash) + getByRules().hashCode();
        return hash;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder("FREQ=" + frequencyType().toString());
        if (getInterval() > 1) builder.append(";INTERVAL=" + getInterval());
        return builder.toString();
    }

}
