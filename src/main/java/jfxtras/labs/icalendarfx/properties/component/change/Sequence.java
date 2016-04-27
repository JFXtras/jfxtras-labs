package jfxtras.labs.icalendarfx.properties.component.change;

import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyBase;

/**
 * SEQUENCE
 * Sequence Number
 * RFC 5545, 3.8.7.4, page 138
 * 
 * This property defines the revision sequence number of the
 * calendar component within a sequence of revisions.
 * 
 * Example:  The following is an example of this property for a calendar
 * component that was just created by the "Organizer":
 * SEQUENCE:0
 * 
 * The following is an example of this property for a calendar
 * component that has been revised two different times by the "Organizer":
 * SEQUENCE:2
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 */
public class Sequence extends PropertyBase<Integer, Sequence>
{
//    public Sequence(CharSequence contentLine)
//    {
//        super(contentLine);
//    }

    public Sequence(Integer value)
    {
        super(value);
    }
    
    public Sequence(Sequence source)
    {
        super(source);
    }
    
    public Sequence()
    {
        super(0); // default is 0
    }
    
    @Override
    public void setValue(Integer value)
    {
        if (value >= 0)
        {
            super.setValue(value);
        } else
        {
            throw new IllegalArgumentException(propertyType() + " must be greater than or equal to zero");
        }
    }

    public static Sequence parse(String propertyContent)
    {
        Sequence property = new Sequence();
        property.parseContent(propertyContent);
        return property;
    }
}
