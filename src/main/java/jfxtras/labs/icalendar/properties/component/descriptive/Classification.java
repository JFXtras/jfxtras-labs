package jfxtras.labs.icalendar.properties.component.descriptive;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.component.descriptive.Classification.ClassificationType;

public class Classification extends PropertyBase<Classification, ClassificationType>
{
    private StringConverter<ClassificationType> converter = new StringConverter<ClassificationType>()
    {
        @Override public String toString(ClassificationType type)
        {
            return type.toString();
        }
        @Override public ClassificationType fromString(String string)
        {
            return ClassificationType.valueOf2(string);
        }
    };
    
    private String unknownValue;

    public Classification(String content)
    {
        super(content);
//        super(ClassificationType.valueOf2(content));
//        if (getValue() == ClassificationType.UNKNOWN)
//        {
//            unknownValue = content;
//        }
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
    
    @Override
    public String toContentLine()
    {
        StringBuilder builder = contentLinePart1();
        System.out.println("value:" + getValue() + " " + propertyType().defaultValueType());
        String value;
        if (getValue() == ClassificationType.UNKNOWN)
            value = unknownValue;
        else
        {
            System.out.println("here:" + getValue());
            ValueType defaultValueType = propertyType().defaultValueType();
            ClassificationType value2 = getValue();
            value = defaultValueType.makeContent(value2);
        }
        builder.append(":" + value);
        return builder.toString();
    }
    
    public enum ClassificationType
    {
        PUBLIC,
        PRIVATE,
        CONFIDENTIAL,
        UNKNOWN; // must treat as PRIVATE
        
        static ClassificationType valueOf2(String value)
        {
            try
            {
                return valueOf(value);
            } catch (IllegalArgumentException e)
            {
                return UNKNOWN;
            }
        }
    }
}
