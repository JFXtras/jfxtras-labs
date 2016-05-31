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
    public Count(int count)
    {
        this();
        setValue(count);
    }

    public Count()
    {
        super();
        valueProperty().addListener((obs, oldValue, newValue) ->
        {
            if ((newValue != null) && (newValue < 1))
            {
                setValue(oldValue);
                throw new IllegalArgumentException(elementType() + " is " + newValue + ".  It can't be less than 1");
            }
        });
    }

    public Count(Count source)
    {
        this();
        setValue(source.getValue());
    }

    @Override
    public void parseContent(String content)
    {
        setValue(Integer.parseInt(content));
    }

    public static Count parse(String content)
    {
        Count element = new Count();
        element.parseContent(content);
        return element;
    }
}
