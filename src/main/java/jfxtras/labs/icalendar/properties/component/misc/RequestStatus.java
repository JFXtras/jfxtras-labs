package jfxtras.labs.icalendar.properties.component.misc;

import java.util.Map;

import jfxtras.labs.icalendar.parameters.Parameter;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.properties.AlternateTextRepresentationBase;

public class RequestStatus extends AlternateTextRepresentationBase<RequestStatus>
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

    @Override
    public Map<ParameterEnum, Parameter> parameterMap()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
