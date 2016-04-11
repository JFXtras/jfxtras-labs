package jfxtras.labs.icalendar.properties.component.misc;

import jfxtras.labs.icalendar.properties.PropertyBaseLanguage;

public class RequestStatus extends PropertyBaseLanguage<RequestStatus, String>
{
    public RequestStatus(CharSequence contentLine)
    {
        super(contentLine);
    }

    public RequestStatus(RequestStatus source)
    {
        super(source);
    }
}
