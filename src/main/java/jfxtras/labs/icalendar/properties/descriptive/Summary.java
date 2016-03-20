package jfxtras.labs.icalendar.properties.descriptive;

import jfxtras.labs.icalendar.properties.TextPropertyAbstract;
import jfxtras.labs.icalendar.properties.ComponentProperty;

public class Summary extends TextPropertyAbstract<Comment>
{
    private final static String NAME = ComponentProperty.SUMMARY.toString();

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
