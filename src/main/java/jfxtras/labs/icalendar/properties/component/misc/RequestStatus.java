package jfxtras.labs.icalendar.properties.component.misc;

import jfxtras.labs.icalendar.properties.LanguageBase;

public class RequestStatus extends LanguageBase<RequestStatus, String>
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
