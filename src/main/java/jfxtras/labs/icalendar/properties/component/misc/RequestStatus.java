package jfxtras.labs.icalendar.properties.component.misc;

import java.util.Map;

import jfxtras.labs.icalendar.parameters.Parameter;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
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

    @Override
    public Map<ParameterEnum, Parameter> parameterMap()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
