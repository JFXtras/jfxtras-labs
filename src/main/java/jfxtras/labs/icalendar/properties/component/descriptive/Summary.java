package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.PropertyTextBase3;
import jfxtras.labs.icalendar.properties.PropertyType;

public class Summary extends PropertyTextBase3<Summary>
{
    private final static String NAME = PropertyType.SUMMARY.toString();

    public Summary(String contentLine)
    {
        super(NAME, contentLine);
    }

    public Summary(Summary summary)
    {
        super(summary);
    }
    
    public Summary() { super(NAME); }
}
