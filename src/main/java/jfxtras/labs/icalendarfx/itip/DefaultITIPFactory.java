package jfxtras.labs.icalendarfx.itip;

import jfxtras.labs.icalendarfx.properties.calendar.Method.MethodType;

public class DefaultITIPFactory extends AbstractITIPFactory
{

    @Override
    public Processable getITIPMessageProcess(MethodType methodType)
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
            return new ProcessRequest();
        default:
            break;        
        }
        throw new RuntimeException("not implemented:" + methodType);
    }

}
