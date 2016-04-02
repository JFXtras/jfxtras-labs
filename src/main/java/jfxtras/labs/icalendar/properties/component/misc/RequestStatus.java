package jfxtras.labs.icalendar.properties.component.misc;

import jfxtras.labs.icalendar.properties.LanguageProperty;

public class RequestStatus extends LanguageProperty<RequestStatus, String>
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
