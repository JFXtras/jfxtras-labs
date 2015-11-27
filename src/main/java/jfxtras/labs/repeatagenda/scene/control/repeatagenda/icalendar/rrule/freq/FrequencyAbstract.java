package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq;

import java.security.InvalidParameterException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule;

public abstract class FrequencyAbstract implements Frequency {
    
    /** INTERVAL: (RFC 5545 iCalendar 3.3.10, page 40) number of frequency periods to pass before new appointment */
    // Uses lazy initialization of property because often interval stays as the default value of 1
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
            throw new InvalidParameterException("INTERVAL can't be less than 1. (" + i + ")");
        }
    }
    public Frequency withInterval(int interval) { setInterval(interval); return this; }

    /** BYxxx Rules 
     * Collection of BYxxx rules that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
     * The BYxxx rules must be applied in a specific order and can only be occur once */
    @Override public List<Rule> getRules() { return byRules; }
    private List<Rule> byRules = new ArrayList<Rule>();
    @Override public void addByRule(Rule byRule)
    {
//        if (byRules == null) byRules = new ArrayList<Rule>();
        boolean alreadyPresent = getRules().stream().anyMatch(a -> a.getClass() == byRule.getClass());
        if (alreadyPresent){
            throw new InvalidParameterException("Can't add BYxxx rule (" 
                    + byRule.getClass().getName() + ") more than once.");
        }
        getRules().add(byRule);
    }
    
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
        Iterator<Rule> ruleIterator = getRules().iterator();
        List<Boolean> rulesEqualsArray = new ArrayList<Boolean>();
        for (int i=0; i<getRules().size(); i++)
        {
            boolean e = getRules().get(i).equals(testObj.getRules().get(i));
            rulesEqualsArray.add(e);
        }
        boolean rulesEquals = rulesEqualsArray.stream().allMatch(a -> true );
        System.out.println("frequency " + intervalEquals + " " + rulesEquals);
        return intervalEquals && rulesEquals;
    }
    
    @Override
    public String toString()
    {
        return "FREQ=";
    }

    /** Time unit of last rule applied.  It represents the time span to apply future changes to the output stream of date/times
     * For example:
     * following FREQ=WEEKLY it is WEEKS
     * following FREQ=YEARLY it is YEARS
     * following FREQ=YEARLY;BYWEEKNO=20 it is WEEKS
     * following FREQ=YEARLY;BYMONTH=3 it is MONTHS
     * following FREQ=YEARLY;BYMONTH=3;BYDAY=TH it is DAYS
     */
    public ChronoUnit getChronoUnit() { return chronoUnit; };
    private ChronoUnit chronoUnit;
    public void setChronoUnit(ChronoUnit chronoUnit)
    {
        switch (chronoUnit)
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

}
