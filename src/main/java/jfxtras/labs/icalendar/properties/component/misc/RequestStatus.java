package jfxtras.labs.icalendar.properties.component.misc;

import jfxtras.labs.icalendar.properties.PropertyBaseLanguage;

public class RequestStatus extends PropertyBaseLanguage<RequestStatus, String>
{
    public RequestStatus(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }

    public RequestStatus(RequestStatus source)
    {
        super(source);
    }
}
