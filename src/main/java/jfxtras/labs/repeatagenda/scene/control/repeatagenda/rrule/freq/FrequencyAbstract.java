package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByRule;

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
    
    /** COUNT: (RFC 5545 iCalendar 3.3.10, page 41) number of events to occur before repeat rule ends */
    // Uses lazy initialization of property because often count stays as the default value of 0
    public IntegerProperty countProperty()
    {
        if (count == null) count = new SimpleIntegerProperty(this, "count", _count);
        return count;
    }
    private IntegerProperty count;
    @Override public Integer getCount() { return (count == null) ? _count : count.getValue(); }
    private int _count = 0;
    @Override public void setCount(Integer i)
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
    }
    
//    /** COUNT: (RFC 5545 iCalendar 3.3.10, page 41) number of events to occur before repeat rule ends */
//    final private IntegerProperty count = new SimpleIntegerProperty();
//    public Integer getCount() { return count.getValue(); }
//    public IntegerProperty countProperty() { return count; }
//    public void setCount(Integer count)
//    {
//        this.count.set(count);
    
    private final List<ByRule> byRules = new ArrayList<ByRule>();
    @Override public List<ByRule> getByRules() { return byRules; }
    @Override public void addByRule(ByRule byRule)
    {
        boolean alreadyPresent = getByRules().stream().anyMatch(a -> a.getClass() == byRule.getClass());
        if (alreadyPresent){
            throw new InvalidParameterException("Can't add BYxxx rule (" 
                    + byRule.getClass().getName() + ") more than once.");
        }
        getByRules().add(byRule);
    }



//    @Override public void setByRules(List<ByRule> c) { }
    
    
    /** Constructor */
    FrequencyAbstract(LocalDateTime startLocalDateTime)
    {
        this.startLocalDateTime = startLocalDateTime;
    }
    
    /** Start date/time of repeat rule.  DTSTART from RFC 5545 iCalendar 3.8.2.4 page 97 */
    // startLocalDateTime is only set through constructor
    private LocalDateTime startLocalDateTime;
    @Override public LocalDateTime getStartLocalDateTime() { return startLocalDateTime; }
    

    @Override
    public LocalDateTime getUntil() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setUntil(LocalDateTime dateTime) {
        // TODO Auto-generated method stub
        
    }

}