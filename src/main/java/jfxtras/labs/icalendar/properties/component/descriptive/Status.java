package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.component.descriptive.Status.StatusType;

public class Status extends PropertyBase<Status, StatusType>
{
    public Status(String contentLine)
    {
        super(contentLine);
//        setValue(StatusType.valueOf(getPropertyValueString()));
    }
    
    public Status(Status source)
    {
        super(source);
    }
    
    public Status()
    {
        super();
    }
    
    public enum StatusType
    {
        TENTATIVE,
        CONFIRMED,
        CANCELLED,
        NEEDS_ACTION,
        COMPLETED,
        IN_PROCESS,
        DRAFT,
        FINAL;
        
        @Override
        public String toString()
        {
            return super.name().replace('_', '-');
        }
    }

//    @Override
//    public String getValueForContentLine()
//    {
//        return getValue().toString();
//    }
}
