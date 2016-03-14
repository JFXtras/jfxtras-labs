package jfxtras.labs.icalendar.properties.descriptive;

import jfxtras.labs.icalendar.components.VComponentUtilities.VComponentProperty;
import jfxtras.labs.icalendar.properties.TextPropertyAbstract;

public class Summary extends TextPropertyAbstract<Comment>
{
    public Summary(String contentLine)
    {
        super(contentLine);
    }

    public Summary(Summary summary)
    {
        super(summary);
    }

    @Override
    public String toString()
    {
        return VComponentProperty.SUMMARY.toString() + super.toString();
    }
}
