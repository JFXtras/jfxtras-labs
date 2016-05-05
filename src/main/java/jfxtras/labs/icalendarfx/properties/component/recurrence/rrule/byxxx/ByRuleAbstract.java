package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RRuleElement;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RRuleElementBase;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RRuleElementType;

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
public abstract class ByRuleAbstract<T, U> extends RRuleElementBase<T, U> implements ByRule<T>, Comparable<ByRule<T>>, RRuleElement<T>
{
//    /** ByRule enum containing order in which ByRules are processed */
//    final private ByRuleType byRuleEnum;
//    @Override public ByRuleType byRuleType() { return byRuleEnum; }
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

//    ByRuleAbstract(Class<? extends ByRule<T>> byRuleClass)
    ByRuleAbstract()
    {
        super();
//        byRuleEnum = ByRuleType.enumFromClass(byRuleClass);
    }

//    // Constructor that parses a string value
//    ByRuleAbstract(String value)
//    {
//        this();
//    }
    
    // Copy constructor
    ByRuleAbstract(ByRule<T> source)
    {
//        byRuleEnum = source.byRuleType();
        setValue(source.getValue());
//        source.copyTo(this);
    }

    @Override
    public int compareTo(ByRule<T> byRule)
    {        
//        int p1 = ByRuleType.propertyFromByRule(this).sortOrder();
//        int p2 = ByRuleType.propertyFromByRule(byRule).sortOrder();
//        int p1 = byRuleType().sortOrder();
//        int p2 = byRule.byRuleType().sortOrder();
        int p1 = RRuleElementType.enumFromClass(getClass()).sortOrder();
        int p2 = RRuleElementType.enumFromClass(byRule.getClass()).sortOrder();
        return Integer.compare(p1, p2);
    }
    
    @Override
    public String toString()
    {
        return super.toString() + ", " + toContent();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        ByRuleAbstract<T, U> testObj = (ByRuleAbstract<T, U>) obj;
        boolean valueEquals = getValue().equals(testObj.getValue());
        return valueEquals;
    }
    
    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = (31 * hash) + getValue().hashCode();
        return hash;
    }

}
