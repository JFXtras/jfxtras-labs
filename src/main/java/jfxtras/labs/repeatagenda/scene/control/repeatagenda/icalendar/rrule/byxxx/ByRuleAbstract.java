package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx;

/**
 * BYxxx rule that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
 * The BYxxx rules must be applied in a specific order
 * @author David Bal
 *
 */
public abstract class ByRuleAbstract implements Rule
{
    /** Order in which ByRules should be processed */
    public Integer getSortOrder() { return sortOrder; }
    final private Integer sortOrder;
        
    /** Constructor that takes processing order 
     * as defined in RFC 5545 iCalendar page 44 as parameters */
    ByRuleAbstract(Integer sortOrder)
    {
//        this.frequency = frequency;
        this.sortOrder = sortOrder;
    }
    
    @Override
    public int compareTo(Rule byRule)
    {
        return getSortOrder().compareTo(byRule.getSortOrder());
    }
}
