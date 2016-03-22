package jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx;

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
    /** ByRule enum containing order in which ByRules are processed */
    final private ByRuleEnum byRuleEnum;
    @Override public ByRuleEnum byRuleType() { return byRuleEnum; }
//
//    /** Constructor that takes ByRule type as parameter 
//     * The type contains the processing order as defined in RFC 5545 iCalendar page 44 */
//    ByRuleAbstract(ByRuleType byRule)
//    {
//        this.byRule = byRule;
//    }
    
    /*
     * Constructors
     */

    ByRuleAbstract(Class<? extends ByRule> byRuleClass)
    {
        byRuleEnum = ByRuleEnum.enumFromClass(byRuleClass);
    }

    // Constructor that parses a string value
    ByRuleAbstract(Class<? extends ByRule> byRuleClass, String value)
    {
        this(byRuleClass);
    }
    
    // Copy constructor
    ByRuleAbstract(ByRule source)
    {
        byRuleEnum = source.byRuleType();
        source.copyTo(this);
    }

    @Override
    public int compareTo(ByRule byRule)
    {        
//        int p1 = ByRuleType.propertyFromByRule(this).sortOrder();
//        int p2 = ByRuleType.propertyFromByRule(byRule).sortOrder();
        int p1 = byRuleType().sortOrder();
        int p2 = byRule.byRuleType().sortOrder();
        return Integer.compare(p1, p2);
    }
}
