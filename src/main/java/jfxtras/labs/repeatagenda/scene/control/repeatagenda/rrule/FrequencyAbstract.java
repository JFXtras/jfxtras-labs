package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byrule.ByRule;

public abstract class FrequencyAbstract implements Frequency {
    
    /** Constructor */
    FrequencyAbstract(LocalDateTime startLocalDateTime)
    {
        this.startLocalDateTime = startLocalDateTime;
    }
    
    /** Start date/time of repeat rule.  DTSTART from RFC 5545 iCalendar 3.8.2.4 page 97 */
    // startLocalDateTime is only set through constructor
    private LocalDateTime startLocalDateTime;
    @Override public LocalDateTime getStartLocalDateTime() { return startLocalDateTime; }
    
    /** number of frequency periods to pass before new appointment, RFC 5545 iCalendar 3.3.10, page 40 */
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
            throw new InvalidParameterException("Repeat interval can't be less than 1. (" + i + ")");
        }
    }

    @Override
    public Integer getCount() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setCount(Integer count) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public LocalDateTime getUntil() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setUntil(LocalDateTime dateTime) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public LinkedHashSet<ByRule> getByRules() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setByRules(LinkedHashSet<ByRule> c) {
        // TODO Auto-generated method stub
        
    }
    


}
