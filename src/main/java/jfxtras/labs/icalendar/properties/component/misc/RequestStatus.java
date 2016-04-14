package jfxtras.labs.icalendar.properties.component.misc;

import jfxtras.labs.icalendar.properties.PropertyBaseLanguage;

// TODO - need statcode, statdesc and extdata (exception data) fields
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
