package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx;

/**
 * BYxxx rule that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
 * The BYxxx rules must be applied in a specific order
 * 
 * @author David Bal
 * @see ByMonth
 * @see ByWeekNo
 * @see ByYearDay
 * @see ByMonthDay
 * @see ByDay
 * @see ByHour
 * @see ByMinute
 * @see BySecond
 * @see BySetPos
 */
public abstract class ByRuleAbstract implements Rule
{
    /** Order in which ByRules are processed */
    public Integer getProcessOrder() { return processOrder; }
    final private Integer processOrder;
        
    /** Constructor that takes processing order 
     * as defined in RFC 5545 iCalendar page 44 as parameters */
    ByRuleAbstract(Integer processOrder)
    {
        this.processOrder = processOrder;
    }
    
    @Override
    public int compareTo(Rule byRule)
    {
        return getProcessOrder().compareTo(byRule.getProcessOrder());
    }
}
