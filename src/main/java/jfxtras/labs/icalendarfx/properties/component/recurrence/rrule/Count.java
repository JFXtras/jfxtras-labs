package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

/**
 * COUNT:
 * RFC 5545 iCalendar 3.3.10, page 41
 * 
 * The COUNT rule part defines the number of occurrences at which to
 * range-bound the recurrence.  The "DTSTART" property value always
 * counts as the first occurrence.
 */
public class Count extends RRuleElementBase<Integer, Count>
{
    static final Integer DEFAULT_INTERVAL = 1;

    public Count(int count)
    {
        setValue(count);
    }

    @Override
    public Integer getValue() { return (valueProperty() == null) ? DEFAULT_INTERVAL : valueProperty().get(); }
    // TODO - LISTENER TO PREVENT INTERVAL FROM BEING LESS THAN 1

}
