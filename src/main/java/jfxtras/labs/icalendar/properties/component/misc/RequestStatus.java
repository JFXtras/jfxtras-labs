package jfxtras.labs.icalendar.properties.component.misc;

import jfxtras.labs.icalendar.properties.PropertyTextBase3;

public class RequestStatus extends PropertyTextBase3<RequestStatus>
{
    public RequestStatus(String contentLine)
    {
        super(contentLine);
    }

    public RequestStatus(RequestStatus source)
    {
        super(source);
    }
    
    public RequestStatus()
    {
        super();
    }
}
