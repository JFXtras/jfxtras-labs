package jfxtras.labs.icalendarfx.method;

import jfxtras.labs.icalendarfx.properties.calendar.Method.MethodType;

public class DefaultMethodProcessFactory extends AbstractMethodProcessFactory
{

    @Override
    public Processable getMethodProcess(MethodType methodType)
    {
        switch (methodType)
        {
        case ADD:
            break;
        case CANCEL:
            return new ProcessCancel();
        case COUNTER:
            break;
        case DECLINECOUNTER:
            break;
        case PUBLISH:
            return new ProcessPublish();
        case REFRESH:
            break;
        case REPLY:
            break;
        case REQUEST:
            break;
        default:
            break;        
        }
        throw new RuntimeException("not implemented:" + methodType);
    }

}
