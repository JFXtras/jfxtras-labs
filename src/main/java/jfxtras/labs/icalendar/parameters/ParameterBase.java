package jfxtras.labs.icalendar.parameters;

public class ParameterBase implements Parameter
{
    @Override
    public String toContentLine()
    {
        return ";" + ParameterEnum.enumFromClass(getClass()).toString() + "=" + getValue();
    }
    

    @Override
    public Object getValue()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
