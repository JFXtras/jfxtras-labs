package jfxtras.labs.icalendar.properties.descriptive;

import jfxtras.labs.icalendar.properties.LanguageAndAltRepTextProperty;

public class Summary extends LanguageAndAltRepTextProperty<Comment>
{
    public Summary(String contentLine)
    {
        super(contentLine);
    }

    public Summary(Summary summary)
    {
        super(summary);
    }
}
