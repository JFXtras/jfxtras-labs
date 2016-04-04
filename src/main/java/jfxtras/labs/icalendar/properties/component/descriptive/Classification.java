package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.component.descriptive.Classification.ClassificationType;

public class Classification extends PropertyBase<Classification, ClassificationType>
{
    public Classification(String contentLine)
    {
        super(contentLine);
    }
    
    public Classification(ClassificationType type)
    {
        super(type);
    }
    
    public Classification(Classification source)
    {
        super(source);
    }
    
    public Classification()
    {
        super(ClassificationType.PUBLIC); // default value
    }
    
    public enum ClassificationType
    {
        PUBLIC,
        PRIVATE,
        CONFIDENTIAL;
        
    }
}
