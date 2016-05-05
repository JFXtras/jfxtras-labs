package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

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
public class Interval extends RRuleElementBase<Integer, Interval>
{
    static final Integer DEFAULT_INTERVAL = 1;
    @Override
    public Integer getValue() { return (valueProperty() == null) ? DEFAULT_INTERVAL : valueProperty().get(); }
    // TODO - LISTENER TO PREVENT INTERVAL FROM BEING LESS THAN 1
    
    public Interval(Integer interval)
    {
        setValue(interval);
    }
}
