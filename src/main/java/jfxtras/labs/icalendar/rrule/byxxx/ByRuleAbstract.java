package jfxtras.labs.icalendar.rrule.byxxx;

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
public abstract class ByRuleAbstract implements Rule, Comparable<Rule>
{
    /** ByRule enum containing order in which ByRules are processed */
    final private ByRuleType byRule;
    @Override public ByRuleType getByRuleType() { return byRule; }

    /** Constructor that takes ByRule type as parameter 
     * The type contains the processing order as defined in RFC 5545 iCalendar page 44 */
    ByRuleAbstract(ByRuleType byRule)
    {
        this.byRule = byRule;
    }

    @Override
    public int compareTo(Rule byRule)
    {
        int p1 = this.getByRuleType().getProcessOrder();
        int p2 = byRule.getByRuleType().getProcessOrder();
        return (p2 > p1) ? -1 :
               (p2 < p1) ? 1 : 0;
    }
}
