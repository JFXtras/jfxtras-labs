package jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx;

/**
 * BYxxx rule that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
 * The BYxxx rules must be applied in a specific order
 * 
 * @author David Bal
 * @see ByMonth
 * @see ByWeekNumber
 * @see ByYearDay
 * @see ByMonthDay
 * @see ByDay
 * @see ByHour
 * @see ByMinute
 * @see BySecond
 * @see BySetPosition
 */
public abstract class ByRuleAbstract implements ByRule, Comparable<ByRule>
{
//    /** ByRule enum containing order in which ByRules are processed */
//    final private ByRuleParameter byRule;
//    @Override public ByRuleParameter getByRuleType() { return byRule; }
//
//    /** Constructor that takes ByRule type as parameter 
//     * The type contains the processing order as defined in RFC 5545 iCalendar page 44 */
//    ByRuleAbstract(ByRuleParameter byRule)
//    {
//        this.byRule = byRule;
//    }

    ByRuleAbstract() { }

    // Constructor that parses a string value
    ByRuleAbstract(String value)
    {
        super();
    }

    @Override
    public int compareTo(ByRule byRule)
    {        
        int p1 = ByRuleParameter.propertyFromByRule(this).getSortOrder();
        int p2 = ByRuleParameter.propertyFromByRule(byRule).getSortOrder();
        return Integer.compare(p1, p2);
    }
}
